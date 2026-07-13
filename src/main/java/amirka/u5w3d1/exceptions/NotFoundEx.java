package amirka.u5w3d1.exceptions;

import java.util.UUID;

public class NotFoundEx extends RuntimeException {
    public NotFoundEx(UUID id) {
        super("Nothing was found with the following id: " + id);
    }

    public NotFoundEx(String message) {
        super(message);
    }
}
