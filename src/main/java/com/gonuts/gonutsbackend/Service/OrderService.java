package com.gonuts.gonutsbackend.Service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.gonuts.gonutsbackend.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Service
public class OrderService {
    DatabaseReference orderRef;

    

    public OrderService(){
        this.orderRef = FirebaseDatabase.getInstance().getReference("Orders");
    }

        public boolean validateDate(Order order){
            String createdAt = order.getCreatedAt();
            if (createdAt == null){
                LocalDateTime orderPlaced = LocalDateTime.now();
                String orderPlacedString = orderPlaced.toString();
                order.setOrderDate(orderPlacedString);
            }
            return true;
    }

}
