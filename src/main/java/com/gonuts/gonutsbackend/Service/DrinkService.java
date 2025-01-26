package com.gonuts.gonutsbackend.Service;
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

    public Drink getDrinkByName(String name) {
        try {
            // Fetch the DataSnapshot asynchronously
            DataSnapshot dataSnapshot = fetchDataSnapshot().get();

            // Iterate over children of the "menu" node
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String drinkName = snapshot.child("name").getValue(String.class);
                if (drinkName != null && drinkName.equalsIgnoreCase(name)) {
                    // Map the snapshot to a Drink object if found
                    return snapshot.getValue(Drink.class);
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
