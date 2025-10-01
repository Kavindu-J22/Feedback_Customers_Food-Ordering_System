package com.foodordering.repository;

import com.foodordering.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT c FROM Customer c WHERE c.name LIKE %:keyword% OR c.email LIKE %:keyword% OR c.phone LIKE %:keyword%")
    List<Customer> searchCustomers(@Param("keyword") String keyword);
    
    @Query("SELECT c, COUNT(t) as ticketCount FROM Customer c LEFT JOIN c.tickets t GROUP BY c ORDER BY COUNT(t) DESC")
    List<Object[]> findTop5CustomersByTicketCount();
    
    boolean existsByEmail(String email);
}
