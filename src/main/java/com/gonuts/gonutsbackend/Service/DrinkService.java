package com.gonuts.gonutsbackend.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.gonuts.gonutsbackend.model.Drink;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Service
public class DrinkService {

    private final DatabaseReference drinkRef; 

    public DrinkService(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.drinkRef = database.getReference("menu");
    }
    
    public ApiFuture<Void> addDrink(Drink drink){
        String category = drink.getCategory();
        String id = drinkRef.child(category).push().getKey();
        drink.setId(id);
        return drinkRef.child(category).child(id).setValueAsync(drink);
    }

    public List<Drink> getCategory(String category) {
        List<Drink> drinks = new ArrayList<>();
        try {
            DataSnapshot dataSnapshot = fetchDataSnapshot().get(); // Fetch data from Firebase
            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                if (categorySnapshot.getKey().equalsIgnoreCase(category)) { // Check if the category matches
                    for (DataSnapshot drinkSnapshot : categorySnapshot.getChildren()) {
                        // Extract drink details
                        String drinkNames = drinkSnapshot.child("name").getValue(String.class);
                        String priceString = drinkSnapshot.child("price").getValue(String.class); // Get price as a String
                        String drinkDescriptions = drinkSnapshot.child("description").getValue(String.class);
                        String drinkCategory = drinkSnapshot.child("category").getValue(String.class);
                        String drinkId = drinkSnapshot.child("id").getValue(String.class);
    
                        // Create a Drink object and add it to the list
                        drinks.add(new Drink( drinkNames, drinkDescriptions, priceString, drinkCategory, drinkId));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching category data", e); // Handle exceptions
        } 
        return drinks; // Return the list of drinks (empty if no matches found)
    }
    
    

    public Drink getDrinkByName(String name) {
        try {
            // Fetch the DataSnapshot asynchronously
            DataSnapshot dataSnapshot = fetchDataSnapshot().get();

            // Iterate over children of the "menu" node
            for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) { // Iterate through categories
                for (DataSnapshot drinkSnapshot : categorySnapshot.getChildren()) { // Iterate through drinks
                    String drinkName = drinkSnapshot.child("name").getValue(String.class);
                    if (drinkName != null && drinkName.equalsIgnoreCase(name)) { // Check if the drink name matches
                        return drinkSnapshot.getValue(Drink.class); // Map to the Drink object
                    }
                }
            }
        
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching drink data", e);
        }

        // Return null if the drink is not found
        return null;
    }

    public CompletableFuture<DataSnapshot> fetchDataSnapshot() {
        CompletableFuture<DataSnapshot> futureSnapshot = new CompletableFuture<>();

        // Add a single-value event listener to fetch the entire "menu" node
        drinkRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
