package management.subscription.exceptionhandler;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestControllerAdvice
public class Exceptionhandle {
   
   @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Map<String, String> handleValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        

        List<ParameterValidationResult> validationResults = ex.getAllValidationResults();
        for (ParameterValidationResult validationResult : validationResults) {
           
            
            List<MessageSourceResolvable> resolvableErrors = validationResult.getResolvableErrors();
            StringBuilder errorMessage = new StringBuilder();

            for (MessageSourceResolvable resolvableError : resolvableErrors) {
                errorMessage.append(resolvableError.getDefaultMessage()).append("; ");
            }

            // Remove the trailing "; " if there are any errors
            if (errorMessage.length() > 0) {
                errorMessage.setLength(errorMessage.length() - 2);
            }

            errorMap.put("FieldsMissing" ,errorMessage.toString());
        }

        return errorMap;
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
