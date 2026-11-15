package com.eaa.product_app.validation;

import com.eaa.product_app.exception.ProductSchemaViolationException;
import com.eaa.product_app.exception.ResourceNotFoundException;
import com.eaa.product_app.model.AttributeDefinition;
import com.eaa.product_app.model.AttributeLevel;
import com.eaa.product_app.model.Category;
import com.eaa.product_app.model.Product;
import com.eaa.product_app.model.ProductImage;
import com.eaa.product_app.model.SubCategory;
import com.eaa.product_app.model.Variant;
import com.eaa.product_app.model.VariantPolicy;
import com.eaa.product_app.repository.CategoryRepository;
import com.eaa.product_app.repository.SubCategoryRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Explicit service-layer validation (invoked by {@link com.eaa.product_app.service.ProductService}
 * after building/mutating a {@link Product}) that checks its attribute map (and each
 * {@link Variant}'s attribute map) against the effective attribute schema of the
 * {@link SubCategory} it references: the owning {@link Category}'s {@code commonAttributes}
 * merged with the sub-category's own {@code attributeDefinitions} (which take precedence on
 * a name collision). Also enforces the category's {@link VariantPolicy#isUniqueCombination()}
 * rule across the product's variants.
 */
@Component
public class ProductSchemaValidator {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public ProductSchemaValidator(SubCategoryRepository subCategoryRepository,
                                   CategoryRepository categoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public void validate(Product product) {
        if (product == null || product.getSubCategoryGuid() == null || product.getSubCategoryGuid().isBlank()) {
            return;
        }

        SubCategory subCategory = subCategoryRepository.findBySubCategoryGuid(product.getSubCategoryGuid())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No sub-category found with subCategoryGuid '" + product.getSubCategoryGuid() + "'"));

        Category category = categoryRepository.findByCategoryGuid(subCategory.getCategoryGuid())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No category found with categoryGuid '" + subCategory.getCategoryGuid() + "'"));

        Map<String, AttributeDefinition> mergedDefs = mergedDefinitions(category, subCategory);
        Map<String, AttributeDefinition> productLevelDefs = byLevel(mergedDefs, AttributeLevel.PRODUCT);
        Map<String, AttributeDefinition> variantLevelDefs = byLevel(mergedDefs, AttributeLevel.VARIANT);

        Map<String, String> errors = new LinkedHashMap<>();

        validateAttributes(product.getAttributes(), productLevelDefs, "attributes", errors);
        validateImages(product.getImages(), "images", errors);

        if (product.getVariants() != null) {
            for (int i = 0; i < product.getVariants().size(); i++) {
                Variant variant = product.getVariants().get(i);
                String path = "variants[" + i + "].attributes";
                validateAttributes(variant.getAttributes(), variantLevelDefs, path, errors);
                validateImages(variant.getImages(), "variants[" + i + "].images", errors);
            }
        }

        validateUniqueCombination(product, variantLevelDefs, category.getVariantPolicy(), errors);

        if (!errors.isEmpty()) {
            throw new ProductSchemaViolationException(errors);
        }
    }

    // Sub-category's own attribute definitions take precedence over the category's
    // common attributes when a name collides.
    private Map<String, AttributeDefinition> mergedDefinitions(Category category, SubCategory subCategory) {
        Map<String, AttributeDefinition> merged = new LinkedHashMap<>();
        streamOrEmpty(category.getCommonAttributes()).forEach(def -> merged.put(def.getName(), def));
        streamOrEmpty(subCategory.getAttributeDefinitions()).forEach(def -> merged.put(def.getName(), def));
        return merged;
    }

    private List<AttributeDefinition> streamOrEmpty(List<AttributeDefinition> list) {
        return list == null ? List.of() : list;
    }

    private Map<String, AttributeDefinition> byLevel(Map<String, AttributeDefinition> defs, AttributeLevel level) {
        return defs.values().stream()
                .filter(def -> def.getLevel() == level)
                .collect(Collectors.toMap(AttributeDefinition::getName, def -> def, (a, b) -> a, LinkedHashMap::new));
    }

    private void validateAttributes(Map<String, Object> attributes,
                                     Map<String, AttributeDefinition> definitions,
                                     String path,
                                     Map<String, String> errors) {
        Map<String, Object> values = attributes == null ? Map.of() : attributes;

        // Unknown attributes (strict schema)
        for (String key : values.keySet()) {
            if (!definitions.containsKey(key)) {
                addViolation(errors, path, "Unknown attribute '" + key + "' is not defined for this category");
            }
        }

        // Required + type/constraint checks
        for (AttributeDefinition def : definitions.values()) {
            Object value = values.get(def.getName());
            if (value == null) {
                if (def.isRequired()) {
                    addViolation(errors, path, "Missing required attribute '" + def.getName() + "'");
                }
                continue;
            }
            String error = validateValue(def, value);
            if (error != null) {
                addViolation(errors, path, error);
            }
        }
    }

    // At most one image may be flagged as primary within a single Product/Variant's image list.
    private void validateImages(List<ProductImage> images, String path, Map<String, String> errors) {
        if (images == null) {
            return;
        }
        long primaryCount = images.stream().filter(ProductImage::isPrimary).count();
        if (primaryCount > 1) {
            addViolation(errors, path, "At most one image may be marked as primary");
        }
    }

    // When variantPolicy.uniqueCombination is enabled, rejects products whose variants
    // share the same combination of values for attributes flagged as variantAxis=true.
    private void validateUniqueCombination(Product product,
                                            Map<String, AttributeDefinition> variantLevelDefs,
                                            VariantPolicy variantPolicy,
                                            Map<String, String> errors) {
        if (variantPolicy == null || !variantPolicy.isUniqueCombination() || product.getVariants() == null) {
            return;
        }

        List<String> axisKeys = variantLevelDefs.values().stream()
                .filter(AttributeDefinition::isVariantAxis)
                .map(AttributeDefinition::getName)
                .sorted()
                .toList();
        if (axisKeys.isEmpty()) {
            return;
        }

        Set<List<Object>> seenCombinations = new HashSet<>();
        for (Variant variant : product.getVariants()) {
            Map<String, Object> attrs = variant.getAttributes() == null ? Map.of() : variant.getAttributes();
            List<Object> combination = new ArrayList<>(axisKeys.size());
            for (String key : axisKeys) {
                combination.add(attrs.get(key));
            }
            if (!seenCombinations.add(combination)) {
                addViolation(errors, "variants",
                        "Duplicate variant combination for attributes " + axisKeys + ": " + combination);
            }
        }
    }

    private String validateValue(AttributeDefinition def, Object value) {
        return switch (def.getType()) {
            case STRING -> validateString(def, value);
            case NUMBER -> validateNumber(def, value);
            case BOOLEAN -> value instanceof Boolean ? null
                    : "Attribute '" + def.getName() + "' must be a boolean";
            case ENUM -> validateEnum(def, value);
        };
    }

    private String validateString(AttributeDefinition def, Object value) {
        if (!(value instanceof String str)) {
            return "Attribute '" + def.getName() + "' must be a string";
        }
        if (def.getRegex() != null && !def.getRegex().isBlank() && !str.matches(def.getRegex())) {
            return "Attribute '" + def.getName() + "' does not match required pattern";
        }
        return null;
    }

    private String validateNumber(AttributeDefinition def, Object value) {
        if (!(value instanceof Number number)) {
            return "Attribute '" + def.getName() + "' must be a number";
        }
        double d = number.doubleValue();
        if (def.getMin() != null && d < def.getMin()) {
            return "Attribute '" + def.getName() + "' must be >= " + def.getMin();
        }
        if (def.getMax() != null && d > def.getMax()) {
            return "Attribute '" + def.getName() + "' must be <= " + def.getMax();
        }
        return null;
    }

    private String validateEnum(AttributeDefinition def, Object value) {
        List<String> allowed = def.getAllowedValues();
        if (!(value instanceof String str) || allowed == null || !allowed.contains(str)) {
            return "Attribute '" + def.getName() + "' must be one of " + allowed;
        }
        return null;
    }

    private void addViolation(Map<String, String> errors, String path, String message) {
        errors.merge(path, message, (a, b) -> a + "; " + b);
    }
}
