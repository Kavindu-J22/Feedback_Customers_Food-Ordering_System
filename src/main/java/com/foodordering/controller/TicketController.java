package com.foodordering.controller;

import com.foodordering.entity.*;
import com.foodordering.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FoodOrderService foodOrderService;

    @GetMapping
    public String listTickets(@RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            Model model) {

        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("tickets", ticketService.searchTickets(search));
            model.addAttribute("search", search);
        } else if (status != null && !status.isEmpty()) {
            model.addAttribute("tickets", ticketService.getTicketsByStatus(Ticket.Status.valueOf(status)));
            model.addAttribute("status", status);
        } else if (priority != null && !priority.isEmpty()) {
            model.addAttribute("tickets", ticketService.getTicketsByPriority(Ticket.Priority.valueOf(priority)));
            model.addAttribute("priority", priority);
        } else {
            model.addAttribute("tickets", ticketService.getAllTickets());
        }

        model.addAttribute("statuses", Ticket.Status.values());
        model.addAttribute("priorities", Ticket.Priority.values());
        return "tickets/list";
    }

    @GetMapping("/create")
    public String createTicketForm(@RequestParam(required = false) Long orderId, Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("orders", foodOrderService.getAvailableFoodOrders());

        if (orderId != null) {
            Optional<FoodOrder> order = foodOrderService.getFoodOrderById(orderId);
            if (order.isPresent()) {
                model.addAttribute("selectedOrder", order.get());
            }
        }

        return "tickets/create";
    }

    @PostMapping("/create")
    public String createTicket(@Valid @ModelAttribute Ticket ticket,
            BindingResult result,
            @RequestParam String customerName,
            @RequestParam String customerEmail,
            @RequestParam String customerPhone,
            @RequestParam(required = false) String customerAddress,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validate customer input parameters
        boolean hasCustomerErrors = false;

        if (customerName == null || customerName.trim().isEmpty() || customerName.length() < 2
                || customerName.length() > 100) {
            model.addAttribute("customerNameError", "Name is required and must be between 2 and 100 characters");
            hasCustomerErrors = true;
        }

        if (customerEmail == null || customerEmail.trim().isEmpty()
                || !customerEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            model.addAttribute("customerEmailError", "Please provide a valid email address");
            hasCustomerErrors = true;
        }

        if (customerPhone == null || customerPhone.trim().isEmpty() ||
                !customerPhone.replaceAll("[\\s\\-\\(\\)\\+]", "").matches("\\d{9,15}")) {
            model.addAttribute("customerPhoneError", "Please provide a valid phone number (9-15 digits)");
            hasCustomerErrors = true;
        }

        if (customerAddress != null && customerAddress.length() > 200) {
            model.addAttribute("customerAddressError", "Address must be less than 200 characters");
            hasCustomerErrors = true;
        }

        // Check for ticket validation errors
        if (result.hasErrors() || hasCustomerErrors) {
            if (hasCustomerErrors) {
                model.addAttribute("errorMessage", "Please correct the customer information errors below.");
            }
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("orders", foodOrderService.getAvailableFoodOrders());

            // Preserve form data
            model.addAttribute("customerName", customerName);
            model.addAttribute("customerEmail", customerEmail);
            model.addAttribute("customerPhone", customerPhone);
            model.addAttribute("customerAddress", customerAddress);

            return "tickets/create";
        }

        try {
            // Clean phone number (remove spaces, hyphens, parentheses but keep +)
            String cleanPhone = customerPhone.replaceAll("[\\s\\-\\(\\)]", "");

            // Create or get customer
            Customer customer = customerService.createOrGetCustomer(
                    customerName.trim(),
                    customerEmail.trim().toLowerCase(),
                    cleanPhone,
                    customerAddress != null ? customerAddress.trim() : null);
            ticket.setCustomer(customer);

            // Save ticket
            ticketService.saveTicket(ticket);

            redirectAttributes.addFlashAttribute("successMessage", "Ticket created successfully!");
            return "redirect:/tickets";
        } catch (Exception e) {
            String errorMsg = "Error creating ticket: " + e.getMessage();

            // Check for specific database constraint violations
            if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("email")) {
                errorMsg = "A customer with this email address already exists. Please use a different email or contact support.";
            } else if (e.getMessage().contains("phone")) {
                errorMsg = "Invalid phone number format. Please use a valid Sri Lankan phone number format.";
            }

            model.addAttribute("errorMessage", errorMsg);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("orders", foodOrderService.getAvailableFoodOrders());

            // Preserve form data
            model.addAttribute("customerName", customerName);
            model.addAttribute("customerEmail", customerEmail);
            model.addAttribute("customerPhone", customerPhone);
            model.addAttribute("customerAddress", customerAddress);

            return "tickets/create";
        }
    }

    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
            model.addAttribute("replies", ticketService.getTicketReplies(id));
            model.addAttribute("newReply", new TicketReply());
            model.addAttribute("canEdit", ticketService.canEditTicket(ticket.get()));
            return "tickets/view";
        }
        return "redirect:/tickets";
    }

    @GetMapping("/{id}/edit")
    public String editTicketForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent()) {
            if (!ticketService.canEditTicket(ticket.get())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Cannot edit ticket. Only tickets with OPEN status can be edited. You can only add replies.");
                return "redirect:/tickets/" + id;
            }

            model.addAttribute("ticket", ticket.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("orders", foodOrderService.getAvailableFoodOrders());
            model.addAttribute("statuses", Ticket.Status.values());
            model.addAttribute("priorities", Ticket.Priority.values());
            return "tickets/edit";
        }
        return "redirect:/tickets";
    }

    @PostMapping("/{id}/edit")
    public String editTicket(@PathVariable Long id,
            @Valid @ModelAttribute Ticket ticket,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("orders", foodOrderService.getAvailableFoodOrders());
            model.addAttribute("statuses", Ticket.Status.values());
            model.addAttribute("priorities", Ticket.Priority.values());
            return "tickets/edit";
        }

        try {
            Optional<Ticket> existingTicket = ticketService.getTicketById(id);
            if (existingTicket.isPresent()) {
                Ticket existing = existingTicket.get();

                if (!ticketService.canEditTicket(existing)) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "Cannot edit ticket. Only tickets with OPEN status can be edited.");
                    return "redirect:/tickets/" + id;
                }

                existing.setTitle(ticket.getTitle());
                existing.setDescription(ticket.getDescription());
                existing.setPriority(ticket.getPriority());
                existing.setStatus(ticket.getStatus());
                existing.setCategory(ticket.getCategory());
                existing.setFoodOrder(ticket.getFoodOrder());
                existing.setAssignedTo(ticket.getAssignedTo());

                ticketService.updateTicket(existing);
                redirectAttributes.addFlashAttribute("successMessage", "Ticket updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating ticket: " + e.getMessage());
        }

        return "redirect:/tickets/" + id;
    }

    @PostMapping("/{id}/reply")
    public String addReply(@PathVariable Long id,
            @Valid @ModelAttribute TicketReply reply,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please provide a valid reply message.");
            return "redirect:/tickets/" + id;
        }

        try {
            Optional<Ticket> ticket = ticketService.getTicketById(id);
            if (ticket.isPresent()) {
                reply.setTicket(ticket.get());
                ticketService.saveTicketReply(reply);
                redirectAttributes.addFlashAttribute("successMessage", "Reply added successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding reply: " + e.getMessage());
        }

        return "redirect:/tickets/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ticketService.deleteTicket(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting ticket: " + e.getMessage());
        }
        return "redirect:/tickets";
    }
}
