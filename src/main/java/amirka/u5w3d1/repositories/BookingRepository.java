package amirka.u5w3d1.repositories;

import amirka.u5w3d1.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    boolean existsByEmployee_IdAndRequestDate(UUID employeeId, LocalDate requestDate);

    void deleteAllByEmployee_Id(UUID employeeId);

    void deleteAllByTrip_Id(UUID tripId);
}
