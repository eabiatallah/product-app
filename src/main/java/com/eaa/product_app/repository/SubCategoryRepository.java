package com.eaa.product_app.repository;

import com.eaa.product_app.model.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubCategoryRepository extends MongoRepository<SubCategory, String> {

    Page<SubCategory> findByCategoryGuid(String categoryGuid, Pageable pageable);

    Optional<SubCategory> findBySubCategoryGuid(String subCategoryGuid);
}
