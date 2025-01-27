package com.gonuts.gonutsbackend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonuts.gonutsbackend.Service.UserService;
import com.gonuts.gonutsbackend.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final DatabaseReference userRef;

    public UserController(UserService userService) {
        this.userService = userService;
        this.userRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            String userId = userRef.push().getKey(); // Generate a unique ID
            userRef.child(userId).setValueAsync(user);
            return ResponseEntity.ok("User added successfully with ID: " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding user");
        }
    }

    @GetMapping("/getUser/LoyaltyPoints/{userId}")
    public ResponseEntity<?> getLoyaltyPoint(@PathVariable String userId){
        String loyaltyPoints = userService.getLoyaltyPoints(userId);
        if (loyaltyPoints != null){
            return ResponseEntity.ok("Loyalty Points: " + loyaltyPoints);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}

