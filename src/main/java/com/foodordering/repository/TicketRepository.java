package com.foodordering.repository;

import com.foodordering.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    List<Ticket> findByStatus(Ticket.Status status);
    
    List<Ticket> findByPriority(Ticket.Priority priority);
    
    List<Ticket> findByCustomerId(Long customerId);
    
    List<Ticket> findByCategoryId(Long categoryId);
    
    List<Ticket> findByFoodOrderId(Long foodOrderId);
    
    @Query("SELECT t FROM Ticket t WHERE t.title LIKE %:keyword% OR t.description LIKE %:keyword% OR t.customer.name LIKE %:keyword%")
    List<Ticket> searchTickets(@Param("keyword") String keyword);
    
    @Query("SELECT t FROM Ticket t WHERE t.status = :status AND t.priority = :priority")
    List<Ticket> findByStatusAndPriority(@Param("status") Ticket.Status status, @Param("priority") Ticket.Priority priority);
    
    @Query("SELECT t FROM Ticket t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<Ticket> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(t) FROM Ticket t")
    long countTotalTickets();
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'OPEN'")
    long countOpenTickets();
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'RESOLVED'")
    long countResolvedTickets();
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'IN_PROGRESS'")
    long countInProgressTickets();
    
    @Query("SELECT t.category.name, COUNT(t) FROM Ticket t GROUP BY t.category.name ORDER BY COUNT(t) DESC")
    List<Object[]> getTicketCountByCategory();
    
    @Query("SELECT t.customer.name, COUNT(t) FROM Ticket t GROUP BY t.customer.name ORDER BY COUNT(t) DESC")
    List<Object[]> getTop5CustomersByTicketCount();
    
    List<Ticket> findByOrderByCreatedAtDesc();
}
