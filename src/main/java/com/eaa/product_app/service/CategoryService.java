package com.eaa.product_app.service;

import com.eaa.product_app.exception.ResourceNotFoundException;
import com.eaa.product_app.model.Category;
import com.eaa.product_app.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) {
        category.setId(null);
        category.setCategoryGuid(UUID.randomUUID().toString());
        return categoryRepository.save(category);
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No category found with id '" + id + "'"));
    }
}
