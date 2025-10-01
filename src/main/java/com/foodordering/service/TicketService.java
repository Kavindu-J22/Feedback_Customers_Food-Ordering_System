package com.foodordering.service;

import com.foodordering.entity.Ticket;
import com.foodordering.entity.TicketReply;
import com.foodordering.repository.TicketRepository;
import com.foodordering.repository.TicketReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketReplyRepository ticketReplyRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findByOrderByCreatedAtDesc();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getTicketsByStatus(Ticket.Status status) {
        return ticketRepository.findByStatus(status);
    }

    public List<Ticket> getTicketsByPriority(Ticket.Priority priority) {
        return ticketRepository.findByPriority(priority);
    }

    public List<Ticket> getTicketsByCustomer(Long customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    public List<Ticket> getTicketsByCategory(Long categoryId) {
        return ticketRepository.findByCategoryId(categoryId);
    }

    public List<Ticket> searchTickets(String keyword) {
        return ticketRepository.searchTickets(keyword);
    }

    public List<Ticket> getTicketsByStatusAndPriority(Ticket.Status status, Ticket.Priority priority) {
        return ticketRepository.findByStatusAndPriority(status, priority);
    }

    public List<Ticket> getTicketsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return ticketRepository.findByCreatedAtBetween(startDate, endDate);
    }

    // Statistics methods
    public long getTotalTicketsCount() {
        return ticketRepository.countTotalTickets();
    }

    public long getOpenTicketsCount() {
        return ticketRepository.countOpenTickets();
    }

    public long getResolvedTicketsCount() {
        return ticketRepository.countResolvedTickets();
    }

    public long getInProgressTicketsCount() {
        return ticketRepository.countInProgressTickets();
    }

    public List<Object[]> getTicketCountByCategory() {
        return ticketRepository.getTicketCountByCategory();
    }

    public List<Object[]> getTop5CustomersByTicketCount() {
        return ticketRepository.getTop5CustomersByTicketCount();
    }

    // Ticket Reply methods
    public List<TicketReply> getTicketReplies(Long ticketId) {
        return ticketReplyRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }

    public TicketReply saveTicketReply(TicketReply reply) {
        return ticketReplyRepository.save(reply);
    }

    public long getReplyCount(Long ticketId) {
        return ticketReplyRepository.countByTicketId(ticketId);
    }

    public boolean canEditTicket(Ticket ticket) {
        return ticket.getStatus() == Ticket.Status.OPEN;
    }
}
