package amirka.u5w2d4.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorListDTO(String message, LocalDateTime timestamp, List<String> errorsList) {
}
