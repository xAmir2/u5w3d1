package amirka.u5w3d1.services;

import amirka.u5w3d1.entities.Booking;
import amirka.u5w3d1.entities.Employee;
import amirka.u5w3d1.entities.Trip;
import amirka.u5w3d1.exceptions.BadRequestEx;
import amirka.u5w3d1.exceptions.NotFoundEx;
import amirka.u5w3d1.payloads.BookingDTO;
import amirka.u5w3d1.repositories.BookingRepository;
import amirka.u5w3d1.repositories.EmployeeRepository;
import amirka.u5w3d1.repositories.TripRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EmployeeRepository employeeRepository;
    private final TripRepository tripRepository;

    public BookingService(BookingRepository bookingRepository, EmployeeRepository employeeRepository, TripRepository tripRepository) {
        this.bookingRepository = bookingRepository;
        this.employeeRepository = employeeRepository;
        this.tripRepository = tripRepository;
    }

    public Booking save(BookingDTO bookingDTO) {
        if (bookingRepository.existsByEmployee_IdAndRequestDate(
                bookingDTO.employeeId(),
                bookingDTO.requestDate())) {
            throw new BadRequestEx(
                    "Employee already has a booking on " + bookingDTO.requestDate());
        }

        Employee employee = employeeRepository.findById(bookingDTO.employeeId())
                .orElseThrow(() -> new NotFoundEx(bookingDTO.employeeId()));
        Trip trip = tripRepository.findById(bookingDTO.tripId())
                .orElseThrow(() -> new NotFoundEx(bookingDTO.tripId()));
        Booking booking = new Booking(bookingDTO.requestDate(), employee, trip, bookingDTO.notes());
        return bookingRepository.save(booking);
    }

    public Page<Booking> getAll(int page, int size, String orderBy) {
        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));

        return bookingRepository.findAll(pageable);
    }

    public Booking findById(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundEx(id));
    }

    @Transactional
    public void findByIdAndDelete(UUID id) {
        Booking found = this.findById(id);
        this.bookingRepository.delete(found);
    }
}

