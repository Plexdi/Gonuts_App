package com.gonuts.gonutsbackend.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;

#

public class StripeService {

    public StripeService(){
        Stripe.apiKey = System.getenv("STRIPE_SECRET");
    }

        public String createCheckOutSession(double amount, String currency, String successUrl, String cancelUrl) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(successUrl)
        .setCancelUrl(cancelUrl)
        .addLineItem(
            SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(currency)
                        .setUnitAmount((long) (amount * 100)) // Convert to cents
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("GoNuts Order")
                                .build()
                        )
                        .build()
                )
                .build()
        )
        .build();

    Session session = Session.create(params);
    return session.getUrl(); // Redirect user to Stripe Checkout
    }
}
