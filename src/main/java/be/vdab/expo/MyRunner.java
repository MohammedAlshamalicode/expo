package be.vdab.expo;

import be.vdab.expo.bestellingen.BestellingService;
import be.vdab.expo.tickets.TicketsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {
    private final TicketsService ticketsService;
    private final BestellingService bestellingService;

    public MyRunner(TicketsService ticketsService, BestellingService bestellingService) {
        this.ticketsService = ticketsService;
        this.bestellingService = bestellingService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1 -> viewAvailableTickets();
                case 2 -> reserveTicket(scanner);
                case 3 -> viewAllOrders();
                case 4 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n*** Ticket Booking System ***");
        System.out.println("1. View available tickets");
        System.out.println("2. Reserve a ticket");
        System.out.println("3. View all orders");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private void viewAvailableTickets() {
        var tickets = ticketsService.getAvailableTickets();
        System.out.println("\nAvailable Tickets:");
        System.out.println("Junior Day: " + tickets.getJuniorDag());
        System.out.println("Senior Day: " + tickets.getSeniorDag());
    }

    private void reserveTicket(Scanner scanner) {
        try {
            System.out.print("\nEnter your name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
                return;
            }
            System.out.println("\nSelect ticket type:");
            System.out.println("1. Junior Day");
            System.out.println("2. Senior Day");
            System.out.println("3. All-In (Both Days)");
            System.out.print("Enter ticket type (1/2/3): ");
            int ticketType = scanner.nextInt();
            scanner.nextLine();
            int orderId = bestellingService.createBestelling(name, ticketType);
            System.out.println("Ticket reserved successfully! Your order ID is: " + orderId);
            viewAvailableTickets();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private void viewAllOrders() {
        var orders = bestellingService.getAllBestellingen();
        if (orders.isEmpty()) {
            System.out.println("\nNo orders have been placed yet.");
        } else {
            System.out.println("\nAll Orders:");
            orders.forEach(order ->
                    System.out.println("Order ID: " + order.getId() +
                            ", Name: " + order.getNaam() +
                            ", Ticket Type: " + order.getTicketType())
            );
        }
    }
}
