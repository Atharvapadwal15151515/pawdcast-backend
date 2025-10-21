package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product_categories")
public class ProductCategory {

    @EmbeddedId
    private ProductCategoryId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    // Constructors
    public ProductCategory() {}

    public ProductCategory(Product product, Category category) {
        this.product = product;
        this.category = category;
        this.id = new ProductCategoryId(product.getId(), category.getId());
    }

    // Getters and Setters
    public ProductCategoryId getId() { return id; }
    public void setId(ProductCategoryId id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    // Public static embedded ID class
    @SuppressWarnings("serial")
	@Embeddable
    public static class ProductCategoryId implements Serializable {
        private Integer productId;
        private Integer categoryId;

        // Default constructor
        public ProductCategoryId() {}

        public ProductCategoryId(Integer productId, Integer categoryId) {
            this.productId = productId;
            this.categoryId = categoryId;
        }

        // Getters and Setters
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }

        public Integer getCategoryId() { return categoryId; }
        public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

        // hashCode and equals methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProductCategoryId that = (ProductCategoryId) o;
            return Objects.equals(productId, that.productId) && Objects.equals(categoryId, that.categoryId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(productId, categoryId);
        }
    }
}