package com.foodordering.repository;

import com.foodordering.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    List<Category> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT c, COUNT(t) as ticketCount FROM Category c LEFT JOIN c.tickets t GROUP BY c ORDER BY COUNT(t) DESC")
    List<Object[]> findCategoriesWithTicketCount();
    
    boolean existsByName(String name);
}
