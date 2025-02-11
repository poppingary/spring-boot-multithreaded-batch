package org.example.demo.repository;

import jakarta.transaction.Transactional;
import org.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query("DELETE FROM Product")
    void deleteAllProducts();

    @Transactional
    @Modifying
    @Query("UPDATE Product p " +
            "SET p.isOfferApplied = false, " +
            "    p.discountPercentage = 0, " +
            "    p.priceAfterDiscount = p.price " +
            "WHERE p.id IS NOT NULL")
    void resetProducts();

    @Transactional
    @Modifying
    @Query("UPDATE Product p " +
            "SET p.discountPercentage = CASE WHEN p.price >= 1000 THEN 10 WHEN p.price > 500 THEN 5 ELSE 0 END, " +
            "    p.isOfferApplied = CASE WHEN p.price >= 1000 OR p.price > 500 THEN true ELSE false END, " +
            "    p.priceAfterDiscount = FUNCTION('ROUND', p.price - (p.price * (CASE WHEN p.price >= 1000 THEN 10 WHEN p.price > 500 THEN 5 ELSE 0 END) / 100), 2) " +
            "WHERE p.id IN :ids")
    void updateDiscounts(@Param("ids") List<Long> ids);
}