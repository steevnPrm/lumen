package steevnPrm.lumen.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreate(String subject) {
        return userRepository.findByEmail(subject)
            .orElseGet(() -> userRepository.save(new User(subject)));
    }

    public User updateProfile(String subject, UserProfileRequest request) {
        User user = userRepository.findByEmail(subject).orElseGet(() -> new User(subject));
        user.setUsername(request.username());
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        return userRepository.save(user);
    }
}
