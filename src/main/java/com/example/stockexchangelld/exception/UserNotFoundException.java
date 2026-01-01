package com.example.stockexchangelld.exception;

public class UserNotFoundException extends TradingException{

    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
    }
}
