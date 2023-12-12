package hu.cubix.logistics.patrik.exception;

public class TransportPlanNotFoundException extends RuntimeException {

    private static final String MESSAGE = "TransportPlan does not exists!";

    public TransportPlanNotFoundException() {
        super(MESSAGE);
    }
}
