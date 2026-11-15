package com.eaa.product_app.controller;

import com.eaa.product_app.dto.PagedResponse;
import com.eaa.product_app.dto.ProductCreatedResponse;
import com.eaa.product_app.dto.ProductRequest;
import com.eaa.product_app.dto.ProductResponse;
import com.eaa.product_app.dto.VariantRequest;
import com.eaa.product_app.mapper.ProductMapper;
import com.eaa.product_app.mapper.VariantMapper;
import com.eaa.product_app.model.Product;
import com.eaa.product_app.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Products belong to a category and carry category-level attributes plus a list of variants.
 */
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreatedResponse create(@Valid @RequestBody ProductRequest request) {
        Product created = productService.create(ProductMapper.toEntity(request));
        return new ProductCreatedResponse(created.getProductGuid(), created.getDescription());
    }

    @GetMapping
    public PagedResponse<ProductResponse> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return PagedResponse.from(productService.findAll(pageable), ProductMapper::toResponse);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable String id) {
        return ProductMapper.toResponse(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable String id, @Valid @RequestBody ProductRequest request) {
        return ProductMapper.toResponse(productService.update(id, ProductMapper.toEntity(request)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }

    @PostMapping("/{id}/variants")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addVariant(@PathVariable String id, @Valid @RequestBody VariantRequest request) {
        return ProductMapper.toResponse(productService.addVariant(id, VariantMapper.toEntity(request)));
    }

    @PutMapping("/{id}/variants/{variantId}")
    public ProductResponse updateVariant(@PathVariable String id, @PathVariable String variantId,
                                          @Valid @RequestBody VariantRequest request) {
        return ProductMapper.toResponse(productService.updateVariant(id, variantId, VariantMapper.toEntity(request)));
    }

    @DeleteMapping("/{id}/variants/{variantId}")
    public ProductResponse deleteVariant(@PathVariable String id, @PathVariable String variantId) {
        return ProductMapper.toResponse(productService.deleteVariant(id, variantId));
    }
}
