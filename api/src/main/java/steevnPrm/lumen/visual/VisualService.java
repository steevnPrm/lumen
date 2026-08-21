package steevnPrm.lumen.visual;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import steevnPrm.lumen.storage.StorageService;
import steevnPrm.lumen.user.User;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VisualService {

    private final UserVisualRepository userVisualRepository;
    private final StorageService storageService;

    public VisualService(UserVisualRepository userVisualRepository, StorageService storageService) {
        this.userVisualRepository = userVisualRepository;
        this.storageService = storageService;
    }

    public UserVisual upload(User user, MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new UnsupportedVisualTypeException(contentType);
        }

        // users/{id}/{uuid}.{ext}: the "users/{id}/" prefix is the per-user namespace
        // that ownership checks and cleanup rely on; the uuid avoids collisions between
        // uploads sharing the same original filename.
        String objectKey = "users/%d/%s%s".formatted(user.getId(), UUID.randomUUID(), extensionOf(file.getOriginalFilename()));
        try {
            storageService.upload(objectKey, file.getInputStream(), file.getSize(), contentType);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read uploaded file", e);
        }

        UserVisual visual = new UserVisual(user, objectKey, file.getOriginalFilename(), contentType, file.getSize());
        return userVisualRepository.save(visual);
    }

    public List<UserVisual> listForUser(User user) {
        return userVisualRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public String urlFor(UserVisual visual) {
        return storageService.presignedGetUrl(visual.getObjectKey());
    }

    public void delete(User user, Long visualId) {
        UserVisual visual = userVisualRepository.findByIdAndUser(visualId, user)
            .orElseThrow(() -> new VisualNotFoundException(visualId));
        storageService.remove(visual.getObjectKey());
        userVisualRepository.delete(visual);
    }

    private String extensionOf(String filename) {
        if (filename == null) {
            return "";
        }
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : "";
    }
}
