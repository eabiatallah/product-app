package com.eaa.product_app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Enables Spring Data MongoDB's {@code @CreatedDate}/{@code @LastModifiedDate}/{@code @CreatedBy}
 * auditing support, populating audit fields on {@code Category}, {@code SubCategory} and
 * {@code Product} documents on save.
 */
@Configuration
@EnableMongoAuditing
public class MongoAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
