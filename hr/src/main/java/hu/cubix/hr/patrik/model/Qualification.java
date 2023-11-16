package hu.cubix.hr.patrik.model;

public enum Qualification {

    NONE("None"),
    HIGH_SCHOOL("High school"),
    COLLEGE("College"),
    UNIVERSITY("University"),
    PHD("PhD");

    private final String name;

    Qualification(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
