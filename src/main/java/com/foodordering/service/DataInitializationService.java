package com.foodordering.service;

import com.foodordering.entity.*;
import com.foodordering.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private FoodOrderRepository foodOrderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void run(String... args) throws Exception {
        if (foodOrderRepository.count() == 0 && customerRepository.count() == 0 && categoryRepository.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Initialize Categories
        List<Category> categories = Arrays.asList(
                new Category("Food Quality", "Issues related to food quality, taste, or freshness"),
                new Category("Delivery", "Problems with delivery time, location, or delivery person"),
                new Category("Order Issues", "Wrong items, missing items, or order modifications"),
                new Category("Payment", "Payment processing, refunds, or billing issues"),
                new Category("Customer Service", "General customer service inquiries and complaints"),
                new Category("Technical", "Website or app technical issues"));
        categoryRepository.saveAll(categories);

        // Initialize Food Orders with Sri Lankan cuisine
        List<FoodOrder> foodOrders = Arrays.asList(
                new FoodOrder("Kottu Roti - Chicken",
                        "Traditional Sri Lankan stir-fried bread with chicken, vegetables, and spices",
                        new BigDecimal("850.00"), "Galle Face Hotel"),

                new FoodOrder("Fish Curry with Rice",
                        "Authentic Sri Lankan fish curry served with steamed basmati rice and papadam",
                        new BigDecimal("1200.00"), "Ministry of Crab"),

                new FoodOrder("Hoppers (Appa) - Set of 3",
                        "Traditional bowl-shaped pancakes made from fermented rice flour and coconut milk",
                        new BigDecimal("450.00"), "Upali's by Nawaloka"),

                new FoodOrder("Lamprais",
                        "Dutch Burgher dish with rice, curry, and accompaniments wrapped in banana leaf",
                        new BigDecimal("1350.00"), "Dutch Hospital Precinct"),

                new FoodOrder("String Hoppers with Curry",
                        "Steamed rice noodle nests served with chicken curry and coconut sambol",
                        new BigDecimal("750.00"), "Shanmugas"),

                new FoodOrder("Pol Roti with Lunu Miris",
                        "Coconut flatbread served with spicy onion relish and dhal curry",
                        new BigDecimal("380.00"), "Green Cabin"),

                new FoodOrder("Devilled Chicken",
                        "Spicy stir-fried chicken with bell peppers, onions, and chili sauce",
                        new BigDecimal("950.00"), "Chinese Dragon Cafe"),

                new FoodOrder("Watalappan",
                        "Traditional Sri Lankan dessert made with coconut milk, jaggery, and spices",
                        new BigDecimal("320.00"), "Perera & Sons"));

        // Set image URLs and save
        foodOrders.get(0).setImageUrl("/images/kottu-roti.jpg");
        foodOrders.get(1).setImageUrl("/images/fish-curry.jpg");
        foodOrders.get(2).setImageUrl("/images/hoppers.jpg");
        foodOrders.get(3).setImageUrl("/images/lamprais.jpg");
        foodOrders.get(4).setImageUrl("/images/string-hoppers.jpg");
        foodOrders.get(5).setImageUrl("/images/pol-roti.jpg");
        foodOrders.get(6).setImageUrl("/images/devilled-chicken.jpg");
        foodOrders.get(7).setImageUrl("/images/watalappan.jpg");

        foodOrderRepository.saveAll(foodOrders);

        // Initialize Customers with Sri Lankan names
        List<Customer> customers = Arrays.asList(
                new Customer("Kasun Perera", "kasun.perera@email.com", "+94771234567", "123 Galle Road, Colombo 03"),
                new Customer("Nimali Fernando", "nimali.fernando@email.com", "+94772345678", "456 Kandy Road, Kandy"),
                new Customer("Roshan Silva", "roshan.silva@email.com", "+94773456789", "789 Negombo Road, Negombo"),
                new Customer("Chamari Jayawardena", "chamari.j@email.com", "+94774567890", "321 Matara Road, Galle"),
                new Customer("Dinesh Rajapaksa", "dinesh.r@email.com", "+94775678901",
                        "654 Kurunegala Road, Kurunegala"),
                new Customer("Sanduni Wickramasinghe", "sanduni.w@email.com", "+94776789012",
                        "987 Jaffna Road, Jaffna"),
                new Customer("Thilina Gunasekara", "thilina.g@email.com", "+94777890123",
                        "147 Ratnapura Road, Ratnapura"),
                new Customer("Malika Dissanayake", "malika.d@email.com", "+94778901234",
                        "258 Anuradhapura Road, Anuradhapura"));
        // Save customers one by one to handle duplicates
        for (Customer customer : customers) {
            if (customerRepository.findByEmail(customer.getEmail()).isEmpty()) {
                customerRepository.save(customer);
            }
        }

        // Initialize some sample tickets
        List<Ticket> tickets = Arrays.asList(
                new Ticket("Cold food delivered",
                        "The kottu roti I ordered was completely cold when it arrived. Very disappointing experience.",
                        Ticket.Priority.HIGH, customers.get(0), categories.get(0), foodOrders.get(0)),

                new Ticket("Wrong order received",
                        "I ordered fish curry but received chicken curry instead. Please help resolve this.",
                        Ticket.Priority.MEDIUM, customers.get(1), categories.get(2), foodOrders.get(1)),

                new Ticket("Delivery delay",
                        "My order was supposed to arrive at 7 PM but came at 9 PM. Food was also cold.",
                        Ticket.Priority.HIGH, customers.get(2), categories.get(1), foodOrders.get(2)),

                new Ticket("Payment issue",
                        "I was charged twice for my lamprais order. Please refund the extra charge.",
                        Ticket.Priority.URGENT, customers.get(3), categories.get(3), foodOrders.get(3)),

                new Ticket("Missing items",
                        "My string hoppers order was missing the coconut sambol. Please send it separately.",
                        Ticket.Priority.MEDIUM, customers.get(4), categories.get(2), foodOrders.get(4)));

        // Set some tickets to different statuses
        tickets.get(1).setStatus(Ticket.Status.IN_PROGRESS);
        tickets.get(1).setAssignedTo("Priya Mendis");
        tickets.get(4).setStatus(Ticket.Status.RESOLVED);

        ticketRepository.saveAll(tickets);

        System.out.println("✅ Sample data initialized successfully!");
        System.out.println("📊 Created: " + foodOrders.size() + " food orders, " +
                customers.size() + " customers, " +
                categories.size() + " categories, " +
                tickets.size() + " tickets");
    }
}
