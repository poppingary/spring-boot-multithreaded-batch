package org.example.demo.service;

import jakarta.transaction.Transactional;
import org.example.demo.entity.Product;
import org.example.demo.handler.ProductDataGenerator;
import org.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductDataGenerator productDataGenerator;

    public ProductService(ProductRepository productRepository, ProductDataGenerator productDataGenerator) {
        this.productRepository = productRepository;
        this.productDataGenerator = productDataGenerator;
    }

    @Transactional
    public void clearAndInsertProducts(int totalProductsToGenerate) {
        productRepository.deleteAllProducts();

        long start = System.currentTimeMillis();
        List<Product> newProducts = productDataGenerator.generateProductsInParallel(totalProductsToGenerate);
        batchInsertProducts(newProducts);
        long end = System.currentTimeMillis();
        System.out.println("Time generate products: " + (end - start) + "ms");
    }

    private void batchInsertProducts(List<Product> products) {
        int batchSize = 50;
        for (int i = 0; i < products.size(); i += batchSize) {
            int end = Math.min(i + batchSize, products.size());
            List<Product> batch = products.subList(i, end);
            productRepository.saveAll(batch);
        }
    }

    @Transactional
    public String resetProductRecords() {
        productRepository.findAll()
                .forEach(product -> {
                    product.setOfferApplied(false);
                    product.setPriceAfterDiscount(product.getPrice());
                    product.setDiscountPercentage(0);
                    productRepository.save(product);
                });
        return "Product records reset successfully";
    }
}