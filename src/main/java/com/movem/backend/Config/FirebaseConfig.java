package com.movem.backend.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() throws IOException {

        if (FirebaseApp.getApps().isEmpty()) {

            ClassPathResource resource =
                    new ClassPathResource("firebase-service-account.json");

            FirebaseOptions options =
                    FirebaseOptions.builder()
                            .setCredentials(
                                    GoogleCredentials.fromStream(
                                            resource.getInputStream()
                                    )
                            )
                            .build();

            FirebaseApp.initializeApp(options);
        }
    }
}