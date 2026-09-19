package com.movem.backend.Controller.AttachmentController;

import com.movem.backend.Service.Implement.Attachment.GcsFileStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
public class ProfilePictureUploadController {
    private final GcsFileStorageService fileStorageService;

    public ProfilePictureUploadController(GcsFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/profile-pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfilePic(@RequestParam("file") MultipartFile file) throws IOException {
        String url = fileStorageService.upload("profile-pics", file);
        return ResponseEntity.ok(url);
    }
}