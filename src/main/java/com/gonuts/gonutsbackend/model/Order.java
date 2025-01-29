package com.gonuts.gonutsbackend.model;

import java.util.List;


public class Order {
    private String orderId;
    private String userId;
    private List<String> items; 
    private Double totalAmount;
    private String paymentId;
    private String paymentStatus;
    private String createdAt;

    //constructor
    public Order(){
        this.orderId = java.util.UUID.randomUUID().toString(); // Auto-generate ID
        this.createdAt = null;
        this.paymentStatus = "PLACED"; // Default order status
        this.paymentStatus = "PENDING"; // Default payment status
    }

    public String getId(){
        return orderId;
    }

    public void setId(String orderId){
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getItems() {
        return items;  // Getter should return the existing value
    }
    
    public void setItems(List<String> items) {
        this.items = items;  // Setter should assign the value
    }

    public double getAmount(){
        return totalAmount;
    }

    public void setAmount(double totalAmount){
        this.totalAmount = totalAmount;
    }

    public String getPaymentId(){
        return paymentId;
    }

    public void setPaymentId(String paymentId){
        this.paymentId = paymentId;
    }

    public String getPaymentStatus(){
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus){
        this.paymentStatus = paymentStatus;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    public void setOrderDate(String createdAt){
        this.createdAt = createdAt;
    }

}
