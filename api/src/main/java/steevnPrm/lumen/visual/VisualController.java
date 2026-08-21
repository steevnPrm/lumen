package steevnPrm.lumen.visual;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import steevnPrm.lumen.user.User;
import steevnPrm.lumen.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/visuals")
public class VisualController {

    private final VisualService visualService;
    private final UserService userService;

    public VisualController(VisualService visualService, UserService userService) {
        this.visualService = visualService;
        this.userService = userService;
    }

    @GetMapping
    public List<VisualResponse> list(@AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreate(jwt.getSubject());
        return visualService.listForUser(user).stream()
            .map(visual -> VisualResponse.from(visual, visualService.urlFor(visual)))
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisualResponse upload(@AuthenticationPrincipal Jwt jwt, @RequestParam("file") MultipartFile file) {
        User user = userService.getOrCreate(jwt.getSubject());
        UserVisual visual = visualService.upload(user, file);
        return VisualResponse.from(visual, visualService.urlFor(visual));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        User user = userService.getOrCreate(jwt.getSubject());
        visualService.delete(user, id);
    }
}
