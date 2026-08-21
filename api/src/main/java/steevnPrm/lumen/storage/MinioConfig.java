package steevnPrm.lumen.storage;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    // MinioClient.builder().build() only wires up an HTTP client, it never contacts the
    // MinIO server, so app startup doesn't depend on MinIO being reachable (same reasoning
    // as the JwtDecoder bean in SecurityConfig). The bucket itself is provisioned by
    // docker-compose (see the minio-init service), not by the application.
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build();
    }
}
