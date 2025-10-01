package com.foodordering.repository;

import com.foodordering.entity.TicketReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketReplyRepository extends JpaRepository<TicketReply, Long> {
    
    List<TicketReply> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
    
    List<TicketReply> findByAuthorNameContainingIgnoreCase(String authorName);
    
    @Query("SELECT r FROM TicketReply r WHERE r.ticket.id = :ticketId ORDER BY r.createdAt DESC")
    List<TicketReply> findByTicketIdOrderByCreatedAtDesc(@Param("ticketId") Long ticketId);
    
    @Query("SELECT COUNT(r) FROM TicketReply r WHERE r.ticket.id = :ticketId")
    long countByTicketId(@Param("ticketId") Long ticketId);
}
