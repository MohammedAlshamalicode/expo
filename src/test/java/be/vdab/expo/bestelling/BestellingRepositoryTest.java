
package be.vdab.expo.bestelling;

import be.vdab.expo.bestellingen.Bestelling;
import be.vdab.expo.bestellingen.BestellingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(BestellingRepository.class)
@Sql({"/bestellingen.sql"})
class BestellingRepositoryTest {

    private static final String BESTELLINGEN_TABLE = "bestellingen";
    private final BestellingRepository bestellingRepository;
    private final JdbcClient jdbcClient;

    BestellingRepositoryTest(BestellingRepository bestellingRepository, JdbcClient jdbcClient) {
        this.bestellingRepository = bestellingRepository;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void findAll_ReturnsAllBestellingen() {
        // Act
        var bestellingen = bestellingRepository.findAll();

        // Assert
        int rowCount = JdbcTestUtils.countRowsInTable(jdbcClient, BESTELLINGEN_TABLE);
        assertThat(bestellingen).hasSize(rowCount);
    }

    @Test
    void findById_ReturnsBestelling_WhenIdExists() {
        // Act
        Optional<Bestelling> bestelling = bestellingRepository.findById(1);

        // Assert
        assertThat(bestelling).isPresent();
        assertThat(bestelling.get().getNaam()).isEqualTo("test1");
        assertThat(bestelling.get().getTicketType()).isEqualTo(1);
    }

    @Test
    void findById_ReturnsEmptyOptional_WhenIdDoesNotExist() {
        // Act
        Optional<Bestelling> bestelling = bestellingRepository.findById(9999);

        // Assert
        assertThat(bestelling).isNotPresent();
    }

    @Test
    void create_AddsNewBestelling() {
        // Arrange
        int initialCount = JdbcTestUtils.countRowsInTable(jdbcClient, BESTELLINGEN_TABLE);
        Bestelling bestelling = new Bestelling(0, "John Doe", 2);

        // Act
        int id = bestellingRepository.create(bestelling);

        // Assert
        int finalCount = JdbcTestUtils.countRowsInTable(jdbcClient, BESTELLINGEN_TABLE);
        assertThat(finalCount).isEqualTo(initialCount + 1);
        assertThat(id).isGreaterThan(0);
    }

    @Test
    void deleteById_RemovesBestelling() {
        // Arrange
        int initialCount = JdbcTestUtils.countRowsInTable(jdbcClient, BESTELLINGEN_TABLE);

        // Act
        bestellingRepository.deleteById(1);

        // Assert
        int finalCount = JdbcTestUtils.countRowsInTable(jdbcClient, BESTELLINGEN_TABLE);
        assertThat(finalCount).isEqualTo(initialCount - 1);
    }

    @Test
    void findAantal_ReturnsCorrectCount() {
        // Act
        long aantal = bestellingRepository.findAantal();

        // Assert
        int rowCount = JdbcTestUtils.countRowsInTable(jdbcClient, BESTELLINGEN_TABLE);
        assertThat(aantal).isEqualTo(rowCount);
    }
}
//********************************************************************************************

//package be.vdab.expo.bestelling;
//
//import be.vdab.expo.bestellingen.Bestelling;
//import be.vdab.expo.bestellingen.BestellingRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.jdbc.core.simple.JdbcClient;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class BestellingRepositoryTest {
//
//    @Mock
//    private Ti
//    @Mock
//    private BestellingRepository bestellingRepository;
//
//    @BeforeEach
//    void setUp() {
//        jdbcClient = mock(JdbcClient.class); // Mock JdbcClient
//        bestellingRepository = new BestellingRepository(jdbcClient); // Use mocked JdbcClient
//    }
//
//    @Test
//    void findAll_ReturnsListOfBestellingen() {
//        // Arrange
//        var bestelling1 = new Bestelling(1, "John Doe", 1);
//        var bestelling2 = new Bestelling(2, "Jane Doe", 2);
//        when(jdbcClient.sql(anyString()).query(Bestelling.class).list())
//                .thenReturn(List.of(bestelling1, bestelling2));
//
//        // Act
//        List<Bestelling> bestellingen = bestellingRepository.findAll();
//
//        // Assert
//        assertThat(bestellingen).hasSize(2);
//        assertThat(bestellingen.get(0).getNaam()).isEqualTo("John Doe");
//        assertThat(bestellingen.get(1).getNaam()).isEqualTo("Jane Doe");
//        verify(jdbcClient).sql(anyString());
//    }
//
//    @Test
//    void findById_ReturnsBestelling_WhenIdExists() {
//        // Arrange
//        var bestelling = new Bestelling(1, "John Doe", 1);
//        when(jdbcClient.sql(anyString()).param(anyInt()).query(Bestelling.class).optional())
//                .thenReturn(Optional.of(bestelling));
//
//        // Act
//        Optional<Bestelling> result = bestellingRepository.findById(1);
//
//        // Assert
//        assertThat(result).isPresent();
//        assertThat(result.get().getNaam()).isEqualTo("John Doe");
//        verify(jdbcClient).sql(anyString());
//    }
//
//    @Test
//    void findById_ReturnsEmptyOptional_WhenIdDoesNotExist() {
//        // Arrange
//        when(jdbcClient.sql(anyString()).param(anyInt()).query(Bestelling.class).optional())
//                .thenReturn(Optional.empty());
//
//        // Act
//        Optional<Bestelling> result = bestellingRepository.findById(999);
//
//        // Assert
//        assertThat(result).isNotPresent();
//        verify(jdbcClient).sql(anyString());
//    }
//
//    @Test
//    void create_AddsNewBestelling() {
//        // Arrange
//        var bestelling = new Bestelling(0, "John Doe", 1);
//        ArgumentCaptor<Object[]> captor = ArgumentCaptor.forClass(Object[].class);
//        when(jdbcClient.sql(anyString()).params(captor.capture()).update())
//                .thenReturn(1);
//
//        // Act
//        int result = bestellingRepository.create(bestelling);
//
//        // Assert
//        assertThat(result).isGreaterThan(0);
//        verify(jdbcClient).sql(anyString());
//        assertThat(captor.getValue()).containsExactly("John Doe", 1);
//    }
//
//    @Test
//    void deleteById_RemovesBestelling() {
//        // Arrange
//        when(jdbcClient.sql(anyString()).param(anyInt()).update())
//                .thenReturn(1);
//
//        // Act
//        bestellingRepository.deleteById(1);
//
//        // Assert
//        verify(jdbcClient).sql(anyString());
//    }
//
//    @Test
//    void findAantal_ReturnsCorrectCount() {
//        // Arrange
//        when(jdbcClient.sql(anyString()).query(Long.class).single())
//                .thenReturn(10L);
//
//        // Act
//        long aantal = bestellingRepository.findAantal();
//
//        // Assert
//        assertThat(aantal).isEqualTo(10);
//        verify(jdbcClient).sql(anyString());
//    }
//}
