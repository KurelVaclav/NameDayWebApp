package fg.forrest.namedayapp.nameday.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = { FileParsingException.class })
    public ResponseEntity<Object> handleNotFoundException(FileParsingException ex) {
        String errorMessage = ex.getMessage().toString();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}