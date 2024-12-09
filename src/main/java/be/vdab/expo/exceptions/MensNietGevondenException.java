package be.vdab.expo.exceptions;

public class MensNietGevondenException extends RuntimeException{

    private final int id ;

    public MensNietGevondenException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
