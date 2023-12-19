package management.subscription.service;



import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Getter
@Setter
@Slf4j

public class ValidationException extends RuntimeException {
    private List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        super("Validation failed with the following Fields: " + String.join(", ", errorMessages));
        this.errorMessages = errorMessages;
        log.warn(errorMessages.toString());
    }
}
    