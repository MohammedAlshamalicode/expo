package be.vdab.expo.tickets;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql(scripts = {"/tickets.sql"})
@Import(TicketRepository.class)
class TicketRepositoryTest {

    private final TicketRepository ticketRepository;

    TicketRepositoryTest(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Test
    void findTicketReturnCorrectTicket() {
        Ticket ticket = ticketRepository.findTicket();
        assertThat(ticket).isNotNull();
        assertThat(ticket.getJuniorDag()).isEqualTo(100);
        assertThat(ticket.getSeniorDag()).isEqualTo(200);
    }

    @Test
    void decrementJuniorDay_UpdatesTicketCount() {
        int rowsUpdated = ticketRepository.decrementJuniorDay();
        assertThat(rowsUpdated).isEqualTo(1);
        Ticket ticket = ticketRepository.findTicket();
        assertThat(ticket.getJuniorDag()).isEqualTo(99);
    }

    @Test
    void decrementSeniorDay_UpdatesTicketCount() {
        int rowsUpdated = ticketRepository.decrementSeniorDay();
        assertThat(rowsUpdated).isEqualTo(1);
        Ticket ticket = ticketRepository.findTicket();
        assertThat(ticket.getSeniorDag()).isEqualTo(199);
    }

    @Test
    void decrementJuniorDay_NoUpdateWhenNoTicketsAvailable() {
        for (int i = 0; i < 100; i++) {
            ticketRepository.decrementJuniorDay();
        }
        int rowsUpdated = ticketRepository.decrementJuniorDay();
        assertThat(rowsUpdated).isEqualTo(0);
        Ticket ticket = ticketRepository.findTicket();
        assertThat(ticket.getJuniorDag()).isEqualTo(0);
    }

    @Test
    void decrementSeniorDay_NoUpdateWhenNoTicketsAvailable() {
        for (int i = 0; i < 200; i++) {
            ticketRepository.decrementSeniorDay();
        }
        int rowsUpdated = ticketRepository.decrementSeniorDay();
        assertThat(rowsUpdated).isEqualTo(0);
        Ticket ticket = ticketRepository.findTicket();
        assertThat(ticket.getSeniorDag()).isEqualTo(0);
    }
}
