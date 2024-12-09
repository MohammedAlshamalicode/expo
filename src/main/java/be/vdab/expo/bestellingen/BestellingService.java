package be.vdab.expo.bestellingen;

import be.vdab.expo.exceptions.InvalidInputException;
import be.vdab.expo.exceptions.TicketNotAvailableException;
import be.vdab.expo.tickets.TicketsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BestellingService {
    private final BestellingRepository bestellingRepository;
    private final TicketsService ticketsService;

    public BestellingService(BestellingRepository bestellingRepository, TicketsService ticketsService) {
        this.bestellingRepository = bestellingRepository;
        this.ticketsService = ticketsService;
    }

    @Transactional
    public int createBestelling(String naam, int ticketType) {
        if (naam == null || naam.isBlank()) {
            throw new InvalidInputException("Name cannot be null or blank.");
        }
        if (ticketType < 1 || ticketType > 3) {
            throw new InvalidInputException("Ticket type must be between 1 and 3.");
        }
        if (ticketsService.reserveTicket(ticketType)) {
            Bestelling bestelling = new Bestelling(0, naam, ticketType);
            return bestellingRepository.create(bestelling);
        } else {
            throw new TicketNotAvailableException("No tickets available for the selected type.");
        }
    }

    public List<Bestelling> getAllBestellingen() {
        return bestellingRepository.findAll();
    }

    public void deleteBestellingById(int id) {
        if (id <= 0) {
            throw new InvalidInputException("ID must be greater than zero.");
        }
        bestellingRepository.deleteById(id);
    }

    public Optional<Bestelling> getById(int id) {
        if (id <= 0) {
            throw new InvalidInputException("ID must be greater than zero.");
        }
        return bestellingRepository.findById(id);
    }

}
