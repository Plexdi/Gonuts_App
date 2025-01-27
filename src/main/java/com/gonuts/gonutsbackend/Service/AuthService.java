package com.gonuts.gonutsbackend.Service;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

@Service
public class AuthService {
    public String authenticate(String token) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return decodedToken.getUid(); // This is the userId from the token
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}
