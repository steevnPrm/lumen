package steevnPrm.lumen.visual;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedVisualTypeException extends RuntimeException {

    public UnsupportedVisualTypeException(String contentType) {
        super("Unsupported content type: " + contentType + " (only image/* is accepted)");
    }
}
