package amirka.u5w2d4.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationEx extends RuntimeException {
    private List<String> errorsList;

    public ValidationEx(List<String> errorsList) {
        super("Errori di validazione");
        this.errorsList = errorsList;
    }

    public ValidationEx(String message) {
        super(message);
    }
}