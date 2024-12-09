
package be.vdab.expo.bestellingen;

public class Bestelling {
    private final int id;
    private final String naam;
    private final int ticketType;

    public Bestelling(int id, String naam, int ticketType) {
        this.id = id;
        this.naam = naam;
        this.ticketType = ticketType;
    }

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public int getTicketType() {
        return ticketType;
    }
}
