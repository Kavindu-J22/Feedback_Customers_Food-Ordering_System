package com.foodordering.controller;

import com.foodordering.service.FoodOrderService;
import com.foodordering.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private FoodOrderService foodOrderService;
    
    @Autowired
    private TicketService ticketService;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("availableOrders", foodOrderService.getAvailableFoodOrdersOrderByCreatedAt());
        model.addAttribute("totalTickets", ticketService.getTotalTicketsCount());
        model.addAttribute("openTickets", ticketService.getOpenTicketsCount());
        model.addAttribute("resolvedTickets", ticketService.getResolvedTicketsCount());
        return "index";
    }
    
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", foodOrderService.getAvailableFoodOrdersOrderByCreatedAt());
        return "orders";
    }
}
