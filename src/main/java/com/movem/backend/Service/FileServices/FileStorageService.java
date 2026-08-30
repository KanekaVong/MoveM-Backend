package com.movem.backend.Service.FileServices;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileStorageService {

    private final Storage storage;
    private static final String BUCKET_NAME = "movem-cloud-2-uploads";

    public FileStorageService(Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String objectName = folder + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(BUCKET_NAME, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, objectName);
    }

    public byte[] downloadFile(String objectName) {
        Blob blob = storage.get(BlobId.of(BUCKET_NAME, objectName));
        return blob.getContent();
    }

    public void deleteFile(String objectName) {
        storage.delete(BlobId.of(BUCKET_NAME, objectName));
    }
}
