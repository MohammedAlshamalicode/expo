package be.vdab.expo.exceptions;

public class TicketNotAvailableException extends RuntimeException {
    public TicketNotAvailableException(String message) {
        super(message);
    }
}