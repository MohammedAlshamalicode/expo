
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
