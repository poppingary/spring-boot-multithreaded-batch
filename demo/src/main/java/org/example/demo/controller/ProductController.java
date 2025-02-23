package org.example.demo.controller;

import org.example.demo.dto.ProcessDiscountRequest;
import org.example.demo.dto.ProductCountRequest;
import org.example.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateProductRecords(@RequestBody ProductCountRequest request) {
        if (request.getCount() <= 0) {
            return ResponseEntity.badRequest().body("Count must be greater than 0");
        }
        try {
            productService.clearAndInsertProducts(request.getCount());
            return ResponseEntity.ok("Products generated successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating products: " + e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetProductRecords() {
        productService.resetProductRecords();
        return ResponseEntity.ok("Product records reset successfully");
    }

    @PostMapping("/process/discount")
    public ResponseEntity<String> processDiscount(@RequestBody List<ProcessDiscountRequest> processDiscountRequests) {
        productService.processDiscount(processDiscountRequests);
        return ResponseEntity.ok("Discount applied successfully");
    }
}