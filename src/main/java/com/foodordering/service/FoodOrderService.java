package com.foodordering.service;

import com.foodordering.entity.FoodOrder;
import com.foodordering.repository.FoodOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FoodOrderService {
    
    @Autowired
    private FoodOrderRepository foodOrderRepository;
    
    public List<FoodOrder> getAllFoodOrders() {
        return foodOrderRepository.findAll();
    }
    
    public List<FoodOrder> getAvailableFoodOrders() {
        return foodOrderRepository.findByIsAvailableTrue();
    }
    
    public List<FoodOrder> getAvailableFoodOrdersOrderByCreatedAt() {
        return foodOrderRepository.findAvailableOrdersOrderByCreatedAtDesc();
    }
    
    public Optional<FoodOrder> getFoodOrderById(Long id) {
        return foodOrderRepository.findById(id);
    }
    
    public FoodOrder saveFoodOrder(FoodOrder foodOrder) {
        return foodOrderRepository.save(foodOrder);
    }
    
    public FoodOrder updateFoodOrder(FoodOrder foodOrder) {
        return foodOrderRepository.save(foodOrder);
    }
    
    public void deleteFoodOrder(Long id) {
        foodOrderRepository.deleteById(id);
    }
    
    public List<FoodOrder> searchFoodOrders(String keyword) {
        return foodOrderRepository.searchFoodOrders(keyword);
    }
    
    public List<FoodOrder> searchByFoodItem(String foodItem) {
        return foodOrderRepository.findByFoodItemContainingIgnoreCase(foodItem);
    }
    
    public List<FoodOrder> searchByRestaurant(String restaurantName) {
        return foodOrderRepository.findByRestaurantNameContainingIgnoreCase(restaurantName);
    }
    
    public List<FoodOrder> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return foodOrderRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
