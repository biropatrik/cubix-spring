package hu.cubix.logistics.patrik.exception;

public class DifferentAddressIdsException extends RuntimeException {

    private static final String MESSAGE = "The path id and the body id must match!";

    public DifferentAddressIdsException() {
        super(MESSAGE);
    }
}
