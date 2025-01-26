package com.gonuts.gonutsbackend.Config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    public FirebaseConfig() throws IOException {
        // Load the Firebase service account key from resources
        FileInputStream serviceAccount =
                new FileInputStream(getClass().getClassLoader().getResource("firebase-service-account.json").getFile());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://gonuts-database-default-rtdb.firebaseio.com/")
                .build();

        // Initialise Firebase App
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
