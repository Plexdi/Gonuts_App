package com.gonuts.gonutsbackend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonuts.gonutsbackend.Service.OrderService;
import com.gonuts.gonutsbackend.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final DatabaseReference orderRef;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
        this.orderRef = FirebaseDatabase.getInstance().getReference("orders");
    }

    @PostMapping("/placeOrder/{userId}")
    public ResponseEntity<String> placeOrder(@RequestBody Order order){
        String orderId = order.getId();
        String date = order.getCreatedAt();
        if (date == null){
            orderService.validateDate(order);
        }
        try {
            Map<String, Object> orderData = new HashMap();
            orderData.put("userId", order.getUserId());
            orderData.put("items", order.getItems());
            orderData.put("totalAmount", order.getAmount());
            orderData.put("paymentStatus", order.getPaymentStatus());
            orderData.put("createdAt", order.getCreatedAt().toString());

            orderRef.child(orderId).setValueAsync(orderData);
            return ResponseEntity.ok("Order placed successfully! Order ID: " + orderId);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error placing order: " + e.getMessage());
        }
    }

}
