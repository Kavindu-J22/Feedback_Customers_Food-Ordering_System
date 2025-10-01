package com.foodordering.controller;

import com.foodordering.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private TicketService ticketService;
    
    @GetMapping
    public String dashboard(Model model) {
        // Basic statistics
        model.addAttribute("totalTickets", ticketService.getTotalTicketsCount());
        model.addAttribute("openTickets", ticketService.getOpenTicketsCount());
        model.addAttribute("inProgressTickets", ticketService.getInProgressTicketsCount());
        model.addAttribute("resolvedTickets", ticketService.getResolvedTicketsCount());
        
        // Tickets by category
        List<Object[]> ticketsByCategory = ticketService.getTicketCountByCategory();
        model.addAttribute("ticketsByCategory", ticketsByCategory);
        
        // Top customers by ticket count
        List<Object[]> topCustomers = ticketService.getTop5CustomersByTicketCount();
        model.addAttribute("topCustomers", topCustomers);
        
        return "dashboard/overview";
    }
}
