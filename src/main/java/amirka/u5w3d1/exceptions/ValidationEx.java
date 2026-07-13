package amirka.u5w3d1.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationEx extends RuntimeException {
    private final List<String> errorMessages;

    public ValidationEx(List<String> errorMessages) {
        super("Validation failed with multiple errors.");
        this.errorMessages = errorMessages;
    }
}
