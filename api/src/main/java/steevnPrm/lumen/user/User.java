package steevnPrm.lumen.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String firstname;

    private String lastname;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    // Populated from the Auth0 JWT "sub" claim, not an actual email address; identifies the account, never editable via the profile endpoint.
    @Column(nullable = false, unique = true)
    private String email;

    public User(String email) {
        this.email = email;
        this.createdAt = Instant.now();
    }
}
