package com.gonuts.gonutsbackend;

import java.io.FileInputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@SpringBootApplication
public class GonutsBackendApplication {
		public static void main(String[] args) throws Exception {
			if (FirebaseApp.getApps().isEmpty()) { // Ensure FirebaseApp is initialized only once
				FileInputStream serviceAccount = new FileInputStream("src\\main\\resources\\firebase-service-account.json");
	
				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setDatabaseUrl("https://gonuts-database-default-rtdb.firebaseio.com/") 
						.build();
	
				FirebaseApp.initializeApp(options);
			}
	
			SpringApplication.run(GonutsBackendApplication.class, args);
		}
}
