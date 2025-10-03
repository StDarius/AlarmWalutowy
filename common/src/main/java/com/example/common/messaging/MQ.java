package com.example.common.messaging;

public final class MQ {
    public static final String EXCHANGE = "rates.exchange";
    public static final String ROUTING_KEY = "rates.update";
    public static final String QUEUE = "rate_updates"; // <- PICK THIS and use everywhere
    private MQ() {}
}