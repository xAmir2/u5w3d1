package amirka.u5w3d1.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TripDTO(
        @NotEmpty(message = "Destination is required, otherwise where are we going? Nowhere?")
        String destination,

        @NotNull(message = "A date is required, otherwise when will I know when you going on a trip?")
        LocalDate date
) {
}
