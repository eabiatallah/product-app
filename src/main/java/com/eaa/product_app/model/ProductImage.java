package com.eaa.product_app.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A reference to an image stored in external object storage (e.g. S3/Blob/CDN),
 * or a local file:// URI for local testing.
 * This component only stores the metadata; the actual file upload/storage is external.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @NotBlank
    @Pattern(regexp = "^(https?|ftp|file)://.+$", message = "must be a valid URL (http, https, ftp, or file)")
    private String url;

    private String altText;

    @PositiveOrZero
    private int displayOrder;

    // At most one image per Product/Variant may be marked primary.
    private boolean primary;
}
