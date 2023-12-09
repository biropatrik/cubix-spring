package hu.cubix.logistics.patrik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Objects;

@Entity
public class Section {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private Milestone start;

    @OneToOne
    private Milestone end;

    @PositiveOrZero
    private short orderOfSection;

    @ManyToOne
    private TransportPlan transportPlan;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Milestone getStart() {
        return start;
    }

    public void setStart(Milestone start) {
        this.start = start;
    }

    public Milestone getEnd() {
        return end;
    }

    public void setEnd(Milestone end) {
        this.end = end;
    }

    public short getOrderOfSection() {
        return orderOfSection;
    }

    public void setOrderOfSection(short orderOfSection) {
        this.orderOfSection = orderOfSection;
    }

    public TransportPlan getTransportPlan() {
        return transportPlan;
    }

    public void setTransportPlan(TransportPlan transportPlan) {
        this.transportPlan = transportPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id == section.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
