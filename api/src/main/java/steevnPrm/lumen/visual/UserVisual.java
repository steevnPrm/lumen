package steevnPrm.lumen.visual;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import steevnPrm.lumen.user.User;

import java.time.Instant;

@Entity
@Table(name = "user_visuals")
@Getter
@Setter
@NoArgsConstructor
public class UserVisual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // MinIO object key, e.g. "users/42/3f2c...-avatar.png" — see VisualService.
    @Column(nullable = false, unique = true)
    private String objectKey;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public UserVisual(User user, String objectKey, String originalFilename, String contentType, long size) {
        this.user = user;
        this.objectKey = objectKey;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.size = size;
        this.createdAt = Instant.now();
    }
}
