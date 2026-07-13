package amirka.u5w3d1.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record BookingDTO(
        @NotNull(message = "Request date is required")
        LocalDate requestDate,

        @NotNull(message = "Employee ID is required, or is this a ghost?")
        UUID employeeId,

        @NotNull(message = "Travel ID is required, otherwise where do you think you're going")
        UUID tripId,

        String notes
) {
}
