
package be.vdab.expo.tickets;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepository {
    private final JdbcClient jdbcClient;

    public TicketRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Ticket findTicket() {
        String sql = """
                SELECT juniorDag, seniorDag
                FROM tickets
                """;
        return jdbcClient.sql(sql).query(Ticket.class).single();
    }

    private int decrementTicket(String columnName) {
        String sql = String.format("""
                UPDATE tickets
                SET %s = %s - 1
                WHERE %s > 0
                """, columnName, columnName, columnName);
        return jdbcClient.sql(sql).update();
    }

    public int decrementJuniorDay() {
        return decrementTicket("juniorDag");
    }

    public int decrementSeniorDay() {
        return decrementTicket("seniorDag");
    }
}
