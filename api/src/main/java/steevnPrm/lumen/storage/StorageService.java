package steevnPrm.lumen.storage;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

// Thin wrapper around the MinIO client: object storage concerns only (bucket, keys,
// presigned URLs). No knowledge of what an object key represents — that convention
// belongs to callers, e.g. steevnPrm.lumen.visual.
@Service
public class StorageService {

    private final MinioClient minioClient;
    private final String bucket;
    private final int presignedUrlExpirySeconds;

    public StorageService(
        MinioClient minioClient,
        @Value("${minio.bucket}") String bucket,
        @Value("${minio.presigned-url-expiry-seconds}") int presignedUrlExpirySeconds
    ) {
        this.minioClient = minioClient;
        this.bucket = bucket;
        this.presignedUrlExpirySeconds = presignedUrlExpirySeconds;
    }

    public void upload(String objectKey, InputStream content, long size, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .stream(content, size, -1L)
                .contentType(contentType)
                .build());
        } catch (Exception e) {
            throw new StorageException("Failed to upload object " + objectKey, e);
        }
    }

    // Bucket is private; this is the only way for a client to read an object. The
    // expiry keeps a leaked URL from granting indefinite access.
    public String presignedGetUrl(String objectKey) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(objectKey)
                .expiry(presignedUrlExpirySeconds)
                .build());
        } catch (Exception e) {
            throw new StorageException("Failed to generate URL for object " + objectKey, e);
        }
    }

    public void remove(String objectKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(objectKey)
                .build());
        } catch (Exception e) {
            throw new StorageException("Failed to remove object " + objectKey, e);
        }
    }
}
