package com.foodordering.repository;

import com.foodordering.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    
    List<FoodOrder> findByIsAvailableTrue();
    
    List<FoodOrder> findByFoodItemContainingIgnoreCase(String foodItem);
    
    List<FoodOrder> findByRestaurantNameContainingIgnoreCase(String restaurantName);
    
    List<FoodOrder> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT f FROM FoodOrder f WHERE f.foodItem LIKE %:keyword% OR f.restaurantName LIKE %:keyword% OR f.description LIKE %:keyword%")
    List<FoodOrder> searchFoodOrders(@Param("keyword") String keyword);
    
    @Query("SELECT f FROM FoodOrder f WHERE f.isAvailable = true ORDER BY f.createdAt DESC")
    List<FoodOrder> findAvailableOrdersOrderByCreatedAtDesc();
}
