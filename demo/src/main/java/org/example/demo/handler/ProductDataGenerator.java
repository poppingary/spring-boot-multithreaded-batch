package org.example.demo.handler;

import org.example.demo.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ProductDataGenerator {
    private static final List<String> CATEGORIES = List.of(
            "Electronics", "Computers", "Gaming", "Home Appliances",
            "Wearables", "Cameras", "Mobile Phones", "Smart Home"
    );

    private static final List<String> BRANDS = List.of(
            "Apple", "Samsung", "Sony", "Dell", "HP", "LG", "Google", "Microsoft"
    );


    public List<Product> generateProductsInParallel(int totalProductsToGenerate) {
        return IntStream.range(0, totalProductsToGenerate)
                .parallel()
                .mapToObj(this::createProduct)
                .collect(Collectors.toList());
    }

    private Product createProduct(int index) {
        Random random = new Random();

        Product product = new Product();
        product.setCategory(getRandomCategory(random));
        product.setName(generateProductName(product.getCategory(), random));
        product.setPrice(generatePrice(product.getCategory(), random));
        product.setOfferApplied(false);
        product.setDiscountPercentage(0);
        product.setPriceAfterDiscount(product.getPrice());

        return product;
    }

    private String getRandomCategory(Random random) {
        return CATEGORIES.get(random.nextInt(CATEGORIES.size()));
    }

    private String generateProductName(String category, Random random) {
        String brand = BRANDS.get(random.nextInt(BRANDS.size()));

        return switch (category) {
            case "Electronics", "Computers" -> brand + " " + getSpecializedProductName(category, random);
            case "Gaming" -> brand + " Gaming " + getSpecializedProductName(category, random);
            default -> brand + " " + category + " Device " + (random.nextInt(1000) + 1);
        };
    }

    private String getSpecializedProductName(String category, Random random) {
        return switch (category) {
            case "Electronics" -> getElectronicsProductName(random);
            case "Computers" -> getComputerProductName(random);
            case "Gaming" -> getGamingProductName(random);
            default -> "Generic Device " + (random.nextInt(100) + 1);
        };
    }

    private String getElectronicsProductName(Random random) {
        String[] names = {"Smart Speaker", "Wireless Earbuds", "Noise Cancelling Headphones",
                "Portable Charger", "Smart Home Hub"};
        return names[random.nextInt(names.length)] + " " + (random.nextInt(10) + 1);
    }

    private String getComputerProductName(Random random) {
        String[] names = {"Laptop", "Desktop", "Ultrabook", "Workstation", "Convertible"};
        return names[random.nextInt(names.length)] + " " + (random.nextInt(20) + 1);
    }

    private String getGamingProductName(Random random) {
        String[] names = {"Console", "Mouse", "Keyboard", "Headset", "Controller"};
        return names[random.nextInt(names.length)] + " " + (random.nextInt(10) + 1);
    }

    private double generatePrice(String category, Random random) {
        return switch (category) {
            case "Electronics" -> 50 + random.nextDouble() * 1000;
            case "Computers" -> 300 + random.nextDouble() * 2500;
            case "Gaming", "Wearables" -> 50 + random.nextDouble() * 500;
            case "Home Appliances" -> 100 + random.nextDouble() * 1000;
            case "Cameras" -> 100 + random.nextDouble() * 2000;
            case "Mobile Phones" -> 200 + random.nextDouble() * 1500;
            case "Smart Home" -> 50 + random.nextDouble() * 300;
            default -> 100 + random.nextDouble() * 500;
        };
    }
}