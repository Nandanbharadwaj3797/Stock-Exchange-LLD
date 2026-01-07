package com.example.stockexchangelld.services.strategies.impl;

import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.models.OrderStatus;
import com.example.stockexchangelld.models.OrderType;
import com.example.stockexchangelld.models.Trade;
import com.example.stockexchangelld.services.strategies.OrderMatchingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FIFOMatchingStrategy implements OrderMatchingStrategy {

    @Override
    public String getStrategyName() {
        return "FIFO";
    }

    @Override
    public List<Trade> matchOrders(Order newOrder, List<Order> existingOrders) {

        log.info("Matching orders: {} - {}", newOrder.getOrderId(), newOrder.getOrderType());

        if(newOrder.getOrderType() == OrderType.BUY) {
            return  matchBuyOrder(newOrder, existingOrders);
        }
        else{
            return  matchSellOrder(newOrder, existingOrders);
        }
    }

    private List<Trade>matchBuyOrder(Order buyOrder, List<Order> existingOrders) {
        List<Trade> trades = new ArrayList<>();

        Comparator<Order> sellOrderComparator =
                Comparator.comparing(Order::getPrice)
                        .thenComparing(Order::getOrderAcceptedTimeStamp);

        List<Order> matchingSellOrders = existingOrders.stream()
                .filter(order -> order.getOrderType() == OrderType.SELL)
                .filter(order -> order.getOrderStatus() == OrderStatus.ACCEPTED)
                .filter(order -> order.getStockSymbol().equals(buyOrder.getStockSymbol()))
                .filter(order -> order.getPrice() <= buyOrder.getPrice())
                .sorted(sellOrderComparator)
                .collect(Collectors.toList());

        int remainingQuantity = buyOrder.getRemainingQuantity();

        for (Order sellOrder : matchingSellOrders) {
            if(remainingQuantity <= 0) {
                break; // we have matched all the buyers
            }
            int tradeQuantity=Math.min(remainingQuantity, sellOrder.getRemainingQuantity());
            Double tradePrice=sellOrder.getPrice();

            Trade trade= Trade.builder()
                    .buyerOrderId(buyOrder.getOrderId())
                    .sellerOrderId(sellOrder.getOrderId())
                    .stockSymbol(buyOrder.getStockSymbol())
                    .quantity(tradeQuantity)
                    .price(tradePrice)
                    .build();

            trades.add(trade);

            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQuantity);
            buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity() - tradeQuantity);

            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);
            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQuantity);

            remainingQuantity -= tradeQuantity;

            log.info("Trade : {} - {} - {} {}",trade.getTradeId(),trade.getBuyerOrderId(),trade.getSellerOrderId(),trade.getQuantity());

        }

        return trades;

    }

    private List<Trade> matchSellOrder(Order sellOrder, List<Order> existingOrders) {
        List<Trade> trades = new ArrayList<>();

        List<Order> matchingBuyOrders = existingOrders.stream()
                .filter(order -> order.getOrderType() == OrderType.BUY)
                .filter(order -> order.getStockSymbol().equals(sellOrder.getStockSymbol()))
                .filter(order -> order.getPrice() >= sellOrder.getPrice())
                .filter(order -> order.getOrderStatus() == OrderStatus.ACCEPTED)
                .sorted(Comparator.comparing(Order::getPrice).thenComparing(Order::getOrderAcceptedTimeStamp))
                .collect(Collectors.toList());


        log.info("Matching buy orders: {}", matchingBuyOrders.size());


        int remainingQuantity = sellOrder.getRemainingQuantity();




        for(Order buyOrder : matchingBuyOrders) {
            if(remainingQuantity <= 0) break; // we have matched all the buys

            int tradeQuantity = Math.min(remainingQuantity, buyOrder.getRemainingQuantity());

            Double tradePrice = sellOrder.getPrice();

            Trade trade = Trade.builder()
                    .buyerOrderId(buyOrder.getOrderId())
                    .sellerOrderId(sellOrder.getOrderId())
                    .stockSymbol(sellOrder.getStockSymbol())
                    .quantity(tradeQuantity)
                    .price(tradePrice)
                    .build();

            trades.add(trade);



            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQuantity);
            buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity() - tradeQuantity);

            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);
            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQuantity);

            remainingQuantity -= tradeQuantity;

            log.info("Trade: {} - {} - {} - {} - {} - {} - {} - {}", trade.getTradeId(), trade.getBuyerOrderId(), trade.getSellerOrderId(), trade.getStockSymbol(), trade.getQuantity(), trade.getPrice(), buyOrder.getFilledQuantity(), buyOrder.getRemainingQuantity(), sellOrder.getFilledQuantity(), sellOrder.getRemainingQuantity());

        }

        return trades;


    }
}
