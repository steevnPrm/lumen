package steevnPrm.lumen.user;

import java.time.Instant;

// Deliberately excludes the email/subject identifier: it is sensitive and not editable via this endpoint.
public record UserProfileResponse(
    String username,
    String firstname,
    String lastname,
    Instant createdAt
) {

    static UserProfileResponse from(User user) {
        return new UserProfileResponse(user.getUsername(), user.getFirstname(), user.getLastname(), user.getCreatedAt());
    }
}
