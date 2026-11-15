package com.eaa.product_app.repository;

import com.eaa.product_app.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    Optional<Category> findByCategoryGuid(String categoryGuid);
}
