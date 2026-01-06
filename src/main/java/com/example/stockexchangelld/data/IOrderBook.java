package com.example.stockexchangelld.data;

import com.example.stockexchangelld.models.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderBook {

    void addOrder(Order order);

    boolean removeOrder(String orderId,String stockSymbol);

    boolean updateOrder(Order updatedOrder);

    List<Order> getOrders(String stockSymbol);

    Optional<Order> getOrderBySymbol(String symbol);

    Optional<Order> getOrderByOrderId(String orderId);


}
