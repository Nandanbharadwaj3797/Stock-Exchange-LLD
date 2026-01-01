package com.example.stockexchangelld.exception;

public class OrderNotFoundException extends TradingException{

    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
    }
}
