package amirka.u5w2d4.exceptions;

import java.util.UUID;

public class NotFoundEx extends RuntimeException {
    public NotFoundEx(UUID id) {
        super("Nothing was found with the following id: " + id);
    }
}
