package amirka.u5w3d1.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
public class Booking {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "request_date")
    private LocalDate requestDate;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    private String notes;

    public Booking() {
    }

    public Booking(LocalDate requestDate, Employee employee, Trip trip, String notes) {
        this.requestDate = requestDate;
        this.employee = employee;
        this.trip = trip;
        this.notes = notes;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", requestDate=" + requestDate +
                ", employee=" + employee +
                ", trip=" + trip +
                ", notes='" + notes + '\'' +
                '}';
    }
}
