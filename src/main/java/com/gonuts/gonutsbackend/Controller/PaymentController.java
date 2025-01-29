package com.gonuts.gonutsbackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gonuts.gonutsbackend.Service.StripeService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-checkout")
    public ResponseEntity<String> createCheckout(@RequestParam double amount) {
        try {
            String sessionUrl = stripeService.createCheckOutSession(amount, "usd", "https://frontend.com/success", "https://frontend.com/cancel");
            return ResponseEntity.ok(sessionUrl);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Error creating checkout session: " + e.getMessage());
        }
    }
}
