package com.eaa.product_app.controller;

import com.eaa.product_app.dto.PagedResponse;
import com.eaa.product_app.dto.SubCategoryCreatedResponse;
import com.eaa.product_app.dto.SubCategoryRequest;
import com.eaa.product_app.dto.SubCategoryResponse;
import com.eaa.product_app.mapper.SubCategoryMapper;
import com.eaa.product_app.model.SubCategory;
import com.eaa.product_app.service.SubCategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * SubCategories (e.g. "T-Shirts", "Jeans") belong to a top-level {@link com.eaa.product_app.model.Category}
 * (e.g. "Clothes") and define the attribute schema that products/variants must conform to.
 */
@RestController
@RequestMapping("/subcategories")
@Slf4j
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubCategoryCreatedResponse create(@Valid @RequestBody SubCategoryRequest request) {
        SubCategory created = subCategoryService.create(SubCategoryMapper.toEntity(request));
        return new SubCategoryCreatedResponse(created.getSubCategoryGuid(), created.getName());
    }

    @GetMapping
    public PagedResponse<SubCategoryResponse> getAll(@RequestParam(required = false) String categoryGuid,
                                                       @PageableDefault(size = 20) Pageable pageable) {
        Page<SubCategory> subCategories = categoryGuid == null
                ? subCategoryService.findAll(pageable)
                : subCategoryService.findByCategoryGuid(categoryGuid, pageable);
        return PagedResponse.from(subCategories, SubCategoryMapper::toResponse);
    }

    @GetMapping("/{subCategoryGuid}")
    public SubCategoryResponse getByGuid(@PathVariable String subCategoryGuid) {
        return SubCategoryMapper.toResponse(subCategoryService.findByGuid(subCategoryGuid));
    }
}
