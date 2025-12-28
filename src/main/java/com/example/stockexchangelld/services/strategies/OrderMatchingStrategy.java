package com.example.stockexchangelld.services.strategies;

import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.models.Trade;

import java.util.List;

public interface OrderMatchingStrategy {

    String getStrategyName();

    List<Trade> matchOrders(Order newOrder, List<Order> existingOrders);
}
