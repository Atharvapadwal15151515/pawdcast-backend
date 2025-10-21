package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Product;
import com.pawdcast.pawdcast.application.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Existing endpoints...
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        List<Product> products = productService.getAvailableProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String q) {
        List<Product> products = productService.searchProducts(q);
        return ResponseEntity.ok(products);
    }

    // New endpoints for better image and product management
    @GetMapping("/with-images")
    public ResponseEntity<List<Product>> getProductsWithImages() {
        List<Product> products = productService.getProductsWithImages();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/without-images")
    public ResponseEntity<List<Product>> getProductsWithoutImages() {
        List<Product> products = productService.getProductsWithoutImages();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Product>> getOutOfStockProducts() {
        List<Product> products = productService.getOutOfStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stats/count")
    public ResponseEntity<ProductStats> getProductStats() {
        long totalActive = productService.getActiveProductCount();
        long available = productService.getAvailableProductCount();
        long outOfStock = totalActive - available;
        
        ProductStats stats = new ProductStats(totalActive, available, outOfStock);
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Void> updateProductImage(
            @PathVariable Integer id,
            @RequestParam String imageUrl) {
        productService.updateProductImage(id, imageUrl);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreProduct(@PathVariable Integer id) {
        productService.restoreProduct(id);
        return ResponseEntity.ok().build();
    }

    // DTO for statistics
    public static class ProductStats {
        private final long totalActive;
        private final long available;
        private final long outOfStock;

        public ProductStats(long totalActive, long available, long outOfStock) {
            this.totalActive = totalActive;
            this.available = available;
            this.outOfStock = outOfStock;
        }

        // Getters
        public long getTotalActive() { return totalActive; }
        public long getAvailable() { return available; }
        public long getOutOfStock() { return outOfStock; }
    }
}