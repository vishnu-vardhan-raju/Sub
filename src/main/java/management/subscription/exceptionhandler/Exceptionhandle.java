package management.subscription.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import management.subscription.service.ValidationException;


import java.util.List;




@RestControllerAdvice
public class Exceptionhandle {
   
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<String>> handleValidationException(ValidationException ex){
        return ResponseEntity.badRequest().body(ex.getErrorMessages());
    }

     @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = ex.getMessage();

        // Finding the position of key parts in the error message
        int enumStartIndex = errorMessage.indexOf("`") + 1;
        int enumEndIndex = errorMessage.indexOf("`", enumStartIndex);
        int valuesStartIndex = errorMessage.indexOf("[") + 1;
        int valuesEndIndex = errorMessage.indexOf("]");

        if (enumStartIndex > 0 && enumEndIndex > 0 && valuesStartIndex > 0 && valuesEndIndex > 0) {
            // Extracting the enum name and values
            String enumName = errorMessage.substring(enumStartIndex, enumEndIndex);
            String enumValues = errorMessage.substring(valuesStartIndex, valuesEndIndex);

            String response = String.format("%s : select %s", enumName, enumValues);
            return ResponseEntity.badRequest().body(response);
        } else {
            // Fallback response if the expected parts are not found
            return ResponseEntity.badRequest().body("Error handling enum parsing.");
        }
    }
}
