package com.gonuts.gonutsbackend.Service;

import java.util.HashMap;
import java.util.Map;
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

    public Integer getLoyaltyPoints(String userId){
        try {
            DataSnapshot dataSnapshot = fetchDataSnapshot().get();
            for (DataSnapshot userSnapShot : dataSnapshot.getChildren()){ // Loop through all users
                if (userSnapShot.getKey().equalsIgnoreCase(userId)){ // Check if the user ID matches
                    Integer loyaltyPoints = userSnapShot.child("loyaltyPoints").getValue(Integer.class);
                    return loyaltyPoints;
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


    public String addLoyaltyPoint(String userId, Integer amount) {
        Integer currentLoyaltyPoints = getLoyaltyPoints(userId); 
        Integer newLoyaltyPoints = currentLoyaltyPoints + amount;
        
        // Update the points in Firebase
        Map<String, Object> updates = new HashMap<>();
        updates.put("loyaltyPoints", newLoyaltyPoints);
        userReference.child(userId).updateChildrenAsync(updates);
    
        // Construct response message
        StringBuilder responseMessage = new StringBuilder();
        responseMessage.append("You now have ").append(newLoyaltyPoints).append(" loyalty points.");
    
        // Check eligibility for rewards
        if (newLoyaltyPoints >= 10 && newLoyaltyPoints < 20) {
            responseMessage.append(" Congratulations! You are eligible for a free medium-sized boba drink.");
        } else if (newLoyaltyPoints >= 20) {
            responseMessage.append(" Congratulations! You are eligible for a free large-sized boba drink.");
        }
    
        // Check if user is close to a reward and tell them how many points are left
        if (newLoyaltyPoints < 10) {
            int pointsRemaining = 10 - newLoyaltyPoints;
            responseMessage.append(" You need ").append(pointsRemaining)
                .append(" more points to earn a free medium-sized boba drink.");
        } else if (newLoyaltyPoints < 20) {
            int pointsRemaining = 20 - newLoyaltyPoints;
            responseMessage.append(" You need ").append(pointsRemaining)
                .append(" more points to earn a free large-sized boba drink.");
        }
    
        return responseMessage.toString();
    }

    public String redeemLoyaltyPoints(String userId, String drinkSize) {
        Integer currentLoyaltyPoints = getLoyaltyPoints(userId);
        int pointsRequired = drinkSize.equalsIgnoreCase("medium") ? 10 : 20; 
    
        // Check if the user has enough points
        if (currentLoyaltyPoints < pointsRequired) {
            int pointsNeeded = pointsRequired - currentLoyaltyPoints;
            return "You do not have enough loyalty points to redeem a " + drinkSize +
                   "-sized boba drink. You need " + pointsNeeded + " more points.";
        }
    
        // Deduct points from user's loyalty balance
        int newLoyaltyPoints = currentLoyaltyPoints - pointsRequired;
        Map<String, Object> updates = new HashMap<>();
        updates.put("loyaltyPoints", newLoyaltyPoints);
        userReference.child(userId).updateChildrenAsync(updates);
    
        return "You have successfully redeemed your " + drinkSize +
               "-sized boba drink. Your remaining loyalty points: " + newLoyaltyPoints;
    }
}
