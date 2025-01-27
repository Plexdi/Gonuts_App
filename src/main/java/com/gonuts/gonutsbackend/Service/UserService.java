package com.gonuts.gonutsbackend.Service;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Service
public class UserService {

    private final DatabaseReference userReference;

    public UserService() {
        this.userReference = FirebaseDatabase.getInstance().getReference("users");
    }

    public String getLoyaltyPoints(String userId){
        try {
            DataSnapshot dataSnapshot = fetchDataSnapshot().get();

            for (DataSnapshot userSnapShot : dataSnapshot.getChildren()){ // Loop through all users
                if (userSnapShot.getKey().equalsIgnoreCase(userId)){ // Check if the user ID matches
                    Integer loyaltyPoints = userSnapShot.child("loyaltyPoints").getValue(Integer.class);
                    System.out.println("Loyalty Points: " + loyaltyPoints);
                    return loyaltyPoints != null ? loyaltyPoints.toString() : null; 
                } else {
                    System.out.println("User not found");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user data", e);
        }
        return null;
    }

        public CompletableFuture<DataSnapshot> fetchDataSnapshot() {
        CompletableFuture<DataSnapshot> futureSnapshot = new CompletableFuture<>();

        // Add a single-value event listener to fetch the entire "menu" node
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                futureSnapshot.complete(dataSnapshot); // Complete the future with the DataSnapshot
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the database error and complete the future exceptionally
                futureSnapshot.completeExceptionally(databaseError.toException());
            }
        });

        return futureSnapshot;
    }
}
