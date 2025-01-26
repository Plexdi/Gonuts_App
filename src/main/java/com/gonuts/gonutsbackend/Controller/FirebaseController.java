package com.gonuts.gonutsbackend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@RestController
public class FirebaseController {

    @GetMapping("/test-firebase")
    public String testFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("test-node");

        Map<String, Object> data = new HashMap<>();
        data.put("message", "Hello, Firebase!");
        data.put("timestamp", System.currentTimeMillis());

        ref.setValueAsync(data);
        return "Data written to Firebase!";
    }
}

