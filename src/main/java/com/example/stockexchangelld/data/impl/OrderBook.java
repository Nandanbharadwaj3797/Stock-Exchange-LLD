package com.example.stockexchangelld.data.impl;

import com.example.stockexchangelld.data.IOrderBook;
import com.example.stockexchangelld.models.Order;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class OrderBook implements IOrderBook {

    private final ConcurrentHashMap<String, List<Order>>orderBook = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ReadWriteLock> symbolLocks = new ConcurrentHashMap<>();

    @Override
    public void addOrder(Order order) {
        // 1.figure out the stock symbol
        String stockSymbol= order.getStockSymbol();

        ReadWriteLock lock = getOrCreateLock(stockSymbol);

        lock.writeLock().lock();

        try{
            orderBook.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(order);
            log.info("Order added to OrderBook: {} - {} - {} - {} - {} " + order.getOrderId(),order.getUserId(), order.getStockSymbol(), order.getQuantity(), order.getPrice());
        }finally {
            lock.writeLock().unlock();
        }
    }


    @Override
    public boolean removeOrder(String orderId, String stockSymbol) {

        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.writeLock().lock();
        try{
            List<Order> orders = orderBook.get(stockSymbol);

            if (orders != null) {
                boolean removed= orders.removeIf(order -> order.getOrderId().equals(orderId));
                if (removed) {
                    log.info("order removed from orderBook:");
                }
                else{
                    log.info("order not found in orderBook:");
                }
                return removed;
            }
            return false;
        }
        finally {
            lock.writeLock().unlock();
        }

    }


    @Override
    public boolean updateOrder(Order updatedOrder) {

        String  stockSymbol= updatedOrder.getStockSymbol();

        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.writeLock().lock();

        try{
            List<Order> orders = orderBook.get(updatedOrder.getStockSymbol());
            if (orders != null) {
                for(int i=0;i<orders.size();i++){
                    if(orders.get(i).getOrderId().equals(updatedOrder.getOrderId())){
                        orders.set(i,updatedOrder);
                        log.info("order updated from orderBook:");
                        return true;
                    }
                }
            }
            return false;
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Order> getOrders(String stockSymbol) {
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.readLock().lock();
        try{
            return orderBook.get(stockSymbol);
        }finally{
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<Order> getOrderBySymbol(String symbol) {
        ReadWriteLock lock = getOrCreateLock(symbol);
        lock.readLock().lock();
        try{
            return orderBook.get(symbol).stream().findFirst();
        }finally{
            lock.readLock().unlock();
        }
    }

    private ReadWriteLock getOrCreateLock(String stockSymbol) {
        return  symbolLocks.computeIfAbsent(stockSymbol, (k) -> new ReentrantReadWriteLock());
    }
}
