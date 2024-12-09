package be.vdab.expo.tickets;

/*
create table ticketsTest (
	juniorDag  int unsigned not null,
    seniorDag  int unsigned not null
);

insert into ticketsTest(juniorDag,seniorDag) values (100, 200);
grant update ,delete , select,insert on ticketsTest to cursist;
 */

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({TicketsService.class, TicketRepository.class})
@Sql({"/tickets.sql"})
class TicketsServiceTest {
    private static final String TICKETS_TABLE = "tickets";
    private final TicketsService ticketsService;
    private final JdbcClient jdbcClient;

    TicketsServiceTest(TicketsService ticketsService, JdbcClient jdbcClient) {
        this.ticketsService = ticketsService;
        this.jdbcClient = jdbcClient;
    }

//    private Ticket getTicket() {
//        return jdbcClient.sql("SELECT juniorDag, seniorDag FROM ticketsTest")
//                .query((rs, rowNum) -> new Ticket(rs.getInt("juniorDag"), rs.getInt("seniorDag")))
//                .single();
//    }

    @Test
    void reserveJuniorTicket_DecreasesJuniorDag() {
        // Act
        boolean result = ticketsService.reserveTicket(1);

        // Assert
        assertThat(result).isTrue();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, TICKETS_TABLE, "juniorDag = 99")).isOne();
    }

    @Test
    void reserveJuniorTicket_FailsWhenNoTicketsAvailable() {
        // Arrange
        jdbcClient.sql("UPDATE tickets SET juniorDag = 0").update();

        // Act
        boolean result = ticketsService.reserveTicket(1);

        // Assert
        assertThat(result).isFalse();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, TICKETS_TABLE, "juniorDag = 0")).isOne();
    }

    @Test
    void reserveSeniorTicket_DecreasesSeniorDag() {
        // Act
        boolean result = ticketsService.reserveTicket(2);

        // Assert
        assertThat(result).isTrue();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, TICKETS_TABLE, "seniorDag = 199")).isOne();
    }

    @Test
    void reserveSeniorTicket_FailsWhenNoTicketsAvailable() {
        // Arrange
        jdbcClient.sql("UPDATE tickets SET seniorDag = 0").update();

        // Act
        boolean result = ticketsService.reserveTicket(2);

        // Assert
        assertThat(result).isFalse();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, TICKETS_TABLE, "seniorDag = 0")).isOne();
    }

    @Test
    void reserveAllInTicket_DecreasesBothJuniorAndSeniorDag() {
        // Act
        boolean result = ticketsService.reserveTicket(3);

        // Assert
        assertThat(result).isTrue();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, TICKETS_TABLE, "juniorDag = 99 AND seniorDag = 199")).isOne();
    }

    @Test
    void reserveAllInTicket_FailsWhenNoTicketsAvailableForOneType() {
        // Arrange
        jdbcClient.sql("UPDATE tickets SET seniorDag = 0").update();

        // Act
        boolean result = ticketsService.reserveTicket(3);

        // Assert
        assertThat(result).isFalse();
    }
}
