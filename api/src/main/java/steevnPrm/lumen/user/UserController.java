package steevnPrm.lumen.user;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserProfileResponse getProfile(@AuthenticationPrincipal Jwt jwt) {
        return UserProfileResponse.from(userService.getOrCreate(jwt.getSubject()));
    }

    @PutMapping
    public UserProfileResponse updateProfile(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UserProfileRequest request) {
        return UserProfileResponse.from(userService.updateProfile(jwt.getSubject(), request));
    }
}
