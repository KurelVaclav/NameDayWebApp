package fg.forrest.namedayapp.nameday.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class FileParsingException extends RuntimeException {
    public FileParsingException(String message, Throwable cause) {
        super(message, cause);
    }
    public FileParsingException(String message){
        super(message);
    }
}
