package com.gonuts.gonutsbackend.Util;

import java.security.SecureRandom;

public class OrderIdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateShortOrderId(int length) {
        StringBuilder orderId = new StringBuilder();
        for (int i = 0; i < length; i++) {
            orderId.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return orderId.toString();
    }
}
