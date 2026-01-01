package com.example.stockexchangelld.data.impl;

import com.example.stockexchangelld.data.IOrderBook;
import com.example.stockexchangelld.models.Order;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;

public class OrderBook implements IOrderBook {

    private final ConcurrentHashMap<String, List<Order>>orderBook = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ReadWriteLock> symbolLocks = new ConcurrentHashMap<>();
}
