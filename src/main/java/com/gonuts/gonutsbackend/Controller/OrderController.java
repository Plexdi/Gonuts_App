package com.gonuts.gonutsbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonuts.gonutsbackend.Service.OrderService;
import com.gonuts.gonutsbackend.model.Order;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping("/placeOrder/{OrderId}")
    public ResponseEntity<String> createOrder(@RequestBody Order order){
        
    }

}
