package be.vdab.expo.tickets;

public class Ticket {
    private int juniorDag;
    private int seniorDag;

    public Ticket(int juniorDag, int seniorDag) {
        this.juniorDag = juniorDag;
        this.seniorDag = seniorDag;
    }

    public int getJuniorDag() {
        return juniorDag;
    }

    public int getSeniorDag() {
        return seniorDag;
    }
}
