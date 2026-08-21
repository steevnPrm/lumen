package steevnPrm.lumen.visual;

import org.springframework.data.jpa.repository.JpaRepository;
import steevnPrm.lumen.user.User;

import java.util.List;
import java.util.Optional;

public interface UserVisualRepository extends JpaRepository<UserVisual, Long> {

    List<UserVisual> findByUserOrderByCreatedAtDesc(User user);

    // Scoping the lookup by user (not just id) is what prevents a user from
    // reading/deleting another user's visual by guessing its id.
    Optional<UserVisual> findByIdAndUser(Long id, User user);
}
