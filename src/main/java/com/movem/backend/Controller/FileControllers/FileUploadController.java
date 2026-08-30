package com.movem.backend.Controller.FileControllers;

import com.movem.backend.Service.FileServices.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/profile-pic")
    public ResponseEntity<String> uploadProfilePic(@RequestParam("file") MultipartFile file) throws IOException {
        String url = fileStorageService.uploadFile(file, "profile-pics");
        return ResponseEntity.ok(url);
    }
}
