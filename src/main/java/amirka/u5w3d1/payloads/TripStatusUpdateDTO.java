package amirka.u5w3d1.payloads;

import amirka.u5w3d1.enums.TripStatus;
import jakarta.validation.constraints.NotNull;

public record TripStatusUpdateDTO(
        @NotNull(message = "Status is required")
        TripStatus status
) {
}
