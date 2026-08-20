package steevnPrm.lumen.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserProfileRequest(
    @NotBlank @Size(max = 50) String username,
    @NotBlank @Size(max = 100) String firstname,
    @NotBlank @Size(max = 100) String lastname
) {
}
