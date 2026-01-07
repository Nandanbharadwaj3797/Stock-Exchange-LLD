package com.example.stockexchangelld.services.strategies;

import com.example.stockexchangelld.models.Order;

public interface OrderExpiryStrategy {
    boolean checkExpiry(Order order);
}
