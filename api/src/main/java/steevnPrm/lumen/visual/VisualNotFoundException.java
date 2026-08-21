package steevnPrm.lumen.visual;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VisualNotFoundException extends RuntimeException {

    public VisualNotFoundException(Long id) {
        super("No visual found with id " + id + " for the current user");
    }
}
