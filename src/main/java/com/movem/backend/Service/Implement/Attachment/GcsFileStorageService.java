package com.movem.backend.Service.Implement.Attachment;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GcsFileStorageService {

    private final Storage storage;

    @Value("${gcs.bucket-name}")
    private String bucketName;

    public String upload(String folder, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            originalFileName = "file";
        }

        String safeFileName = originalFileName.replaceAll("[\\\\/]", "_").replaceAll("[^a-zA-Z0-9._-]", "_");
        String objectName = folder + "/" + UUID.randomUUID() + "-" + safeFileName;

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream").build();

        storage.create(blobInfo, file.getBytes());

        return objectName;
    }

    public byte[] download(String objectName) {
        Blob blob = storage.get(BlobId.of(bucketName, objectName));
        if (blob == null) {
            throw new RuntimeException("File not found in Google Cloud Storage: " + objectName);
        }

        return blob.getContent();
    }

    public URL signedUrl(String objectName) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();
        return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES);
    }

    public void delete(String objectName) {
        storage.delete(BlobId.of(bucketName, objectName));
    }

    public String publicUrl(String objectName) {
        return "https://storage.googleapis.com/" + bucketName + "/" + objectName;
    }

}