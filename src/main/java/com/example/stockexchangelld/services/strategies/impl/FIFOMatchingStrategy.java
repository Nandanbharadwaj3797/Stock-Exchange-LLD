package com.example.stockexchangelld.services.strategies.impl;

import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.models.Trade;
import com.example.stockexchangelld.services.strategies.OrderMatchingStrategy;

import java.util.List;

public class FIFOMatchingStrategy implements OrderMatchingStrategy {

    @Override
    public String getStrategyName() {
        return "FIFO";
    }

    @Override
    public List<Trade> matchOrders(Order newOrder, List<Order> existingOrders) {
        return List.of();
    }
}
