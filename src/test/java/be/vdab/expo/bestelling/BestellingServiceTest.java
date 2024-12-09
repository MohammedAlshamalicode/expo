package be.vdab.expo.bestelling;

import be.vdab.expo.bestellingen.Bestelling;
import be.vdab.expo.bestellingen.BestellingRepository;
import be.vdab.expo.bestellingen.BestellingService;
import be.vdab.expo.exceptions.InvalidInputException;
import be.vdab.expo.exceptions.TicketNotAvailableException;
import be.vdab.expo.tickets.TicketsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BestellingServiceTest {
    @Mock
    private BestellingRepository bestellingRepository;

    @Mock
    private TicketsService ticketsService;

    @InjectMocks
    private BestellingService bestellingService;

    @Test
    void createBestelling_ThrowsInvalidInputException_WhenNameIsBlank() {
        assertThatThrownBy(() -> bestellingService.createBestelling("", 1))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Name cannot be null or blank.");
    }

    @Test
    void createBestelling_ThrowsInvalidInputException_WhenTicketTypeIsInvalid() {
        assertThatThrownBy(() -> bestellingService.createBestelling("John", 4))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("Ticket type must be between 1 and 3.");
    }

    @Test
    void createBestelling_ThrowsTicketNotAvailableException_WhenTicketsAreUnavailable() {
        when(ticketsService.reserveTicket(1)).thenReturn(false);

        assertThatThrownBy(() -> bestellingService.createBestelling("John", 1))
                .isInstanceOf(TicketNotAvailableException.class)
                .hasMessage("No tickets available for the selected type.");
    }

    @Test
    void createBestelling_AddsBestelling_WhenTicketsAreAvailable() {
        when(ticketsService.reserveTicket(1)).thenReturn(true);
        Bestelling bestelling = new Bestelling(0, "John", 1);
        when(bestellingRepository.create(any(Bestelling.class))).thenReturn(1);

        int bestellingId = bestellingService.createBestelling("John", 1);

        assertThat(bestellingId).isEqualTo(1);
        verify(bestellingRepository).create(any(Bestelling.class));
    }

    @Test
    void deleteBestellingById_DeletesBestelling_WhenIdIsValid() {
        int validId = 1;

        bestellingService.deleteBestellingById(validId);

        verify(bestellingRepository).deleteById(validId);
    }

    @Test
    void deleteBestellingById_ThrowsInvalidInputException_WhenIdIsInvalid() {
        int invalidId = 0;

        assertThatThrownBy(() -> bestellingService.deleteBestellingById(invalidId))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("ID must be greater than zero.");
    }

    @Test
    void getById_ReturnsBestelling_WhenIdExists() {
        int validId = 1;
        Bestelling bestelling = new Bestelling(validId, "John", 1);
        when(bestellingRepository.findById(validId)).thenReturn(Optional.of(bestelling));

        Optional<Bestelling> result = bestellingService.getById(validId);

        assertThat(result).isPresent();
        assertThat(result.get().getNaam()).isEqualTo("John");
        assertThat(result.get().getTicketType()).isEqualTo(1);
    }

    @Test
    void getById_ReturnsEmpty_WhenIdDoesNotExist() {
        int invalidId = 999;
        when(bestellingRepository.findById(invalidId)).thenReturn(Optional.empty());

        Optional<Bestelling> result = bestellingService.getById(invalidId);

        assertThat(result).isEmpty();
    }

    @Test
    void getAllBestellingen_ReturnsAllBestellingen() {
        List<Bestelling> bestellingen = List.of(
                new Bestelling(1, "John", 1),
                new Bestelling(2, "Jane", 2)
        );
        when(bestellingRepository.findAll()).thenReturn(bestellingen);

        List<Bestelling> result = bestellingService.getAllBestellingen();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNaam()).isEqualTo("John");
        assertThat(result.get(1).getNaam()).isEqualTo("Jane");
    }
}
