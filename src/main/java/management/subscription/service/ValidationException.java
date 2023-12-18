package management.subscription.service;



import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter


public class ValidationException extends RuntimeException {
    private List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        super("Validation failed with the following Fields: " + String.join(", ", errorMessages));
        this.errorMessages = errorMessages;
    }
}
    