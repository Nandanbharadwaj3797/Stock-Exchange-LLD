package com.example.stockexchangelld.exception;

import java.lang.classfile.Superclass;

public class InvalidOrderException extends TradingException {

    public InvalidOrderException(String message) {
        super("Invalid order: "+message);
    }
}
