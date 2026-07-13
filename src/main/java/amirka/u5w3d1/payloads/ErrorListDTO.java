package amirka.u5w3d1.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorListDTO(String message, LocalDateTime timestamp, List<String> errorsList) {
}
