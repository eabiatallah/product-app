package com.eaa.product_app.controller;

import com.eaa.product_app.dto.CategoryCreatedResponse;
import com.eaa.product_app.dto.CategoryRequest;
import com.eaa.product_app.dto.CategoryResponse;
import com.eaa.product_app.dto.PagedResponse;
import com.eaa.product_app.mapper.CategoryMapper;
import com.eaa.product_app.model.Category;
import com.eaa.product_app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Defines the attribute schema (category) that products and their variants must conform to.
 */
@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryCreatedResponse> create(@Valid @RequestBody CategoryRequest request) {
        Category created = categoryService.create(CategoryMapper.toEntity(request));
        return ResponseEntity.created(URI.create("/categories/" + created.getId()))
                .body(new CategoryCreatedResponse(created.getId(), created.getCategoryGuid(), created.getCode()));
    }

    @GetMapping
    public PagedResponse<CategoryResponse> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return PagedResponse.from(categoryService.findAll(pageable), CategoryMapper::toResponse);
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable String id) {
        return CategoryMapper.toResponse(categoryService.findById(id));
    }
}
