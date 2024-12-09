//package be.vdab.expo.tickets;
//
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class TicketsService {
//
//    private final TicketRepository ticketRepository;
//
//    // Constructor for dependency injection
//    public TicketsService(TicketRepository ticketRepository) {
//        this.ticketRepository = ticketRepository;
//    }
//
//    /**
//     * Get the current ticket availability.
//     *
//     * @return Ticket object containing available tickets.
//     */
//    public Ticket getAvailableTickets() {
//        return ticketRepository.findTicket();
//    }
//
//    /**
//     * Reserve a ticket for Junior Day.
//     *
//     * @return boolean indicating success or failure.
//     */
//    @Transactional
//    public boolean reserveJuniorTicket() {
//        Ticket ticket = ticketRepository.findTicket();
//        if (ticket.getJuniorDag() > 0) {
//            ticketRepository.decrementJuniorDay();
//            return true;
//        } else {
//            return false; // No tickets available for Junior Day
//        }
//    }
//
//    /**
//     * Reserve a ticket for Senior Day.
//     *
//     * @return boolean indicating success or failure.
//     */
//    @Transactional
//    public boolean reserveSeniorTicket() {
//        Ticket ticket = ticketRepository.findTicket();
//        if (ticket.getSeniorDag() > 0) {
//            ticketRepository.decrementSeniorDay();
//            return true;
//        } else {
//            return false; // No tickets available for Senior Day
//        }
//    }
//
//    /**
//     * Reserve an "All-In" ticket (both Junior and Senior days).
//     *
//     * @return boolean indicating success or failure.
//     */
//    @Transactional
//    public boolean reserveAllInTicket() {
//        Ticket ticket = ticketRepository.findTicket();
//        if (ticket.getJuniorDag() > 0 && ticket.getSeniorDag() > 0) {
//            ticketRepository.decrementJuniorDay();
//            ticketRepository.decrementSeniorDay();
//            return true;
//        } else {
//            return false; // No tickets available for both days
//        }
//    }
//}

package be.vdab.expo.tickets;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketsService {
    private final TicketRepository ticketRepository;

    public TicketsService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket getAvailableTickets() {
        return ticketRepository.findTicket();
    }

    @Transactional
    public boolean reserveTicket(int ticketType) {
        switch (ticketType) {
            case 1: return ticketRepository.decrementJuniorDay() > 0;
            case 2: return ticketRepository.decrementSeniorDay() > 0;
            case 3:
                boolean juniorReserved = ticketRepository.decrementJuniorDay() > 0;
                boolean seniorReserved = ticketRepository.decrementSeniorDay() > 0;
                if (juniorReserved && seniorReserved) {
                    return true;
                } else {
                    rollbackAllInTicket(juniorReserved, seniorReserved);
                    return false;
                }
            default: throw new IllegalArgumentException("Invalid ticket type.");
        }
    }

    private void rollbackAllInTicket(boolean juniorReserved, boolean seniorReserved) {
        if (juniorReserved) ticketRepository.decrementJuniorDay();
        if (seniorReserved) ticketRepository.decrementSeniorDay();
    }
}
