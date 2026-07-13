package amirka.u5w3d1.services;

import amirka.u5w3d1.entities.Trip;
import amirka.u5w3d1.enums.TripStatus;
import amirka.u5w3d1.exceptions.NotFoundEx;
import amirka.u5w3d1.payloads.TripDTO;
import amirka.u5w3d1.repositories.BookingRepository;
import amirka.u5w3d1.repositories.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final BookingRepository bookingRepository;

    public TripService(TripRepository tripRepository, BookingRepository bookingRepository) {
        this.tripRepository = tripRepository;
        this.bookingRepository = bookingRepository;
    }

    public Trip save(TripDTO tripDTO) {
        Trip newTrip = new Trip(tripDTO.destination(), tripDTO.date());
        return tripRepository.save(newTrip);
    }

    public Trip findById(UUID id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new NotFoundEx(id));
    }

    public Page<Trip> getAll(int page, int size, String orderBy) {
        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));

        return tripRepository.findAll(pageable);
    }

    public Trip findByIdAndUpdate(UUID id, TripDTO tripDTO) {
        Trip found = this.findById(id);

        found.setDestination(tripDTO.destination());
        found.setDate(tripDTO.date());
        return tripRepository.save(found);
    }

    @Transactional
    public void findByIdAndDelete(UUID id) {
        Trip found = this.findById(id);

        bookingRepository.deleteAllByTrip_Id(id);

        this.tripRepository.delete(found);
    }

    public Trip updateStatus(UUID id, TripStatus tripStatus) {
        Trip found = this.findById(id);
        found.setStatus(tripStatus);
        return this.tripRepository.save(found);
    }
}
