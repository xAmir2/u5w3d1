package amirka.u5w3d1.controllers;

import amirka.u5w3d1.entities.Booking;
import amirka.u5w3d1.exceptions.ValidationEx;
import amirka.u5w3d1.payloads.BookingDTO;
import amirka.u5w3d1.payloads.BookingResponseDTO;
import amirka.u5w3d1.services.BookingService;
import amirka.u5w3d1.services.EmployeeService;
import amirka.u5w3d1.services.TripService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final EmployeeService employeeService;
    private final TripService tripService;

    public BookingController(BookingService bookingService, EmployeeService employeeService, TripService tripService) {
        this.bookingService = bookingService;
        this.employeeService = employeeService;
        this.tripService = tripService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDTO saveBooking(@RequestBody @Validated BookingDTO body, BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationEx(errorsList);
        }
        Booking saved = this.bookingService.save(body);
        return new BookingResponseDTO(saved.getId());
    }

    @GetMapping
    public Page<Booking> getTrips(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "requestDate") String orderBy) {
        return bookingService.getAll(page, size, orderBy);
    }

    @GetMapping("/{bookingId}")
    public Booking getById(@PathVariable UUID bookingId) {
        return bookingService.findById(bookingId);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingById(@PathVariable UUID bookingId) {
        bookingService.findByIdAndDelete(bookingId);
    }
}
