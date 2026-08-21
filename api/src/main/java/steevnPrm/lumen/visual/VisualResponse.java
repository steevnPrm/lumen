package steevnPrm.lumen.visual;

import java.time.Instant;

public record VisualResponse(
    Long id,
    String url,
    String contentType,
    long size,
    Instant createdAt
) {

    static VisualResponse from(UserVisual visual, String url) {
        return new VisualResponse(visual.getId(), url, visual.getContentType(), visual.getSize(), visual.getCreatedAt());
    }
}
