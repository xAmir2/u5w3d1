package amirka.u5w3d1.entities;

import amirka.u5w3d1.enums.TripStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
public class Trip {
    @Id
    @GeneratedValue
    private UUID id;
    private String destination;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private TripStatus status = TripStatus.SCHEDULED;

    public Trip() {
    }

    public Trip(String destination, LocalDate date) {
        this.destination = destination;
        this.date = date;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", destination='" + destination + '\'' +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
