package com.eaa.product_app.service;

import com.eaa.product_app.exception.InvalidSchemaException;
import com.eaa.product_app.exception.ResourceNotFoundException;
import com.eaa.product_app.model.AttributeDefinition;
import com.eaa.product_app.model.AttributeLevel;
import com.eaa.product_app.model.Category;
import com.eaa.product_app.model.SubCategory;
import com.eaa.product_app.repository.CategoryRepository;
import com.eaa.product_app.repository.SubCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubCategoryService(SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    public SubCategory create(SubCategory subCategory) {
        subCategory.setId(null);
        subCategory.setSubCategoryGuid(UUID.randomUUID().toString());
        Category category = requireCategory(subCategory.getCategoryGuid());
        validateAxisCount(category, subCategory);
        return subCategoryRepository.save(subCategory);
    }

    public Page<SubCategory> findAll(Pageable pageable) {
        return subCategoryRepository.findAll(pageable);
    }

    public Page<SubCategory> findByCategoryGuid(String categoryGuid, Pageable pageable) {
        requireCategory(categoryGuid);
        return subCategoryRepository.findByCategoryGuid(categoryGuid, pageable);
    }

    public SubCategory findByGuid(String subCategoryGuid) {
        return subCategoryRepository.findBySubCategoryGuid(subCategoryGuid)
                .orElseThrow(() -> new ResourceNotFoundException("No sub-category found with subCategoryGuid '" + subCategoryGuid + "'"));
    }

    private Category requireCategory(String categoryGuid) {
        return categoryRepository.findByCategoryGuid(categoryGuid)
                .orElseThrow(() -> new ResourceNotFoundException("No category found with categoryGuid '" + categoryGuid + "'"));
    }

    // Counts VARIANT-level attributes flagged as variantAxis across the category's
    // commonAttributes and this sub-category's own attributeDefinitions, and rejects
    // the schema if it exceeds the category's variantPolicy.maxAxes.
    private void validateAxisCount(Category category, SubCategory subCategory) {
        if (category.getVariantPolicy() == null || category.getVariantPolicy().getMaxAxes() == null) {
            return;
        }
        long axisCount = Stream.concat(
                        streamOrEmpty(category.getCommonAttributes()),
                        streamOrEmpty(subCategory.getAttributeDefinitions()))
                .filter(def -> def.getLevel() == AttributeLevel.VARIANT && def.isVariantAxis())
                .map(AttributeDefinition::getName)
                .distinct()
                .count();

        int maxAxes = category.getVariantPolicy().getMaxAxes();
        if (axisCount > maxAxes) {
            throw new InvalidSchemaException("Sub-category '" + subCategory.getName() + "' defines " + axisCount
                    + " variant axis attribute(s), exceeding category's variantPolicy.maxAxes=" + maxAxes);
        }
    }

    private Stream<AttributeDefinition> streamOrEmpty(List<AttributeDefinition> list) {
        return list == null ? Stream.empty() : list.stream();
    }
}
