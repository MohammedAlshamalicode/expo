package be.vdab.expo.bestellingen;

import be.vdab.expo.exceptions.MensNietGevondenException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BestellingRepository {
    private final JdbcClient jdbcClient;

    public BestellingRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Bestelling> findAll() {
        String sql = """
                SELECT id, naam, ticketType
                FROM bestellingen
                ORDER BY id
                """;
        return jdbcClient.sql(sql).query(Bestelling.class).list();
    }

    public Optional<Bestelling> findById(int id) {
        String sql = """
                SELECT id, naam, ticketType
                FROM bestellingen
                WHERE id = ?
                """;
        return jdbcClient.sql(sql).param(id).query(Bestelling.class).optional();
    }

    public int create(Bestelling bestelling) {
        String sql = """
                INSERT INTO bestellingen (naam, ticketType)
                VALUES (?, ?)
                """;
        var keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql).params(bestelling.getNaam(), bestelling.getTicketType()).update(keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void deleteById(int id) {
        String sql = """
                DELETE FROM bestellingen
                WHERE id = ?
                """;
        jdbcClient.sql(sql).param(id).update();
    }

    public long findAantal() {
        var sql = """
                    select count(*) as aantalBestellingen
                    from bestellingen
                    """;
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }

    public void update(Bestelling bestelling) {
        var sql = """
        update bestellingen
        set naam = ?, ticketType = ?
        where id = ?
        """;
        if (jdbcClient.sql(sql)
                .params(bestelling.getNaam(), bestelling.getTicketType(), bestelling.getId())
                .update() == 0) {
            throw new MensNietGevondenException(bestelling.getId());
        }
    }
}
