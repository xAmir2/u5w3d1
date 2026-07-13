package amirka.u5w3d1.controllers;

import amirka.u5w3d1.entities.Trip;
import amirka.u5w3d1.exceptions.ValidationEx;
import amirka.u5w3d1.payloads.TripDTO;
import amirka.u5w3d1.payloads.TripResponseDTO;
import amirka.u5w3d1.payloads.TripStatusUpdateDTO;
import amirka.u5w3d1.services.TripService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripResponseDTO saveTrip(@RequestBody @Validated TripDTO body, BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationEx(errorsList);
        }
        Trip saved = this.tripService.save(body);
        return new TripResponseDTO(saved.getId());
    }

    @GetMapping
    public Page<Trip> getTrips(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "destination") String orderBy) {
        return tripService.getAll(page, size, orderBy);
    }

    @GetMapping("/{tripId}")
    public Trip getById(@PathVariable UUID tripId) {
        return tripService.findById(tripId);
    }

    @PutMapping("/{tripId}")
    public Trip updateTrip(@PathVariable UUID tripId, @RequestBody @Validated TripDTO body, BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationEx(errorsList);
        }
        return tripService.findByIdAndUpdate(tripId, body);
    }

    @PatchMapping("/{tripId}/status")
    public Trip updateStatus(@PathVariable UUID tripId, @RequestBody @Validated TripStatusUpdateDTO body, BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationEx(errorsList);
        }
        return tripService.updateStatus(tripId, body.status());
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable UUID tripId) {

        tripService.findByIdAndDelete(tripId);
    }
}
