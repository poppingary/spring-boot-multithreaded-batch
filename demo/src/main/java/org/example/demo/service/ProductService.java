package org.example.demo.service;

import jakarta.transaction.Transactional;
import org.example.demo.dto.ProcessDiscountRequest;
import org.example.demo.entity.Product;
import org.example.demo.handler.ProductDataGenerator;
import org.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final int BATCH_SIZE = 10000;

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
        for (int i = 0; i < products.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, products.size());
            List<Product> batch = products.subList(i, end);
            productRepository.saveAll(batch);
        }
    }

    public void resetProductRecords() {
        productRepository.resetProducts();
    }

    public void processDiscount(List<ProcessDiscountRequest> processDiscountRequests) {
        productRepository.updateDiscounts(processDiscountRequests.parallelStream()
                .map(ProcessDiscountRequest::getId)
                .collect(Collectors.toList()));
    }
}