package com.example.stockexchangelld.services;

import com.example.stockexchangelld.data.IOrderBook;
import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.models.OrderStatus;
import com.example.stockexchangelld.models.OrderType;
import com.example.stockexchangelld.models.Trade;
import com.example.stockexchangelld.services.strategies.OrderMatchingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradingServices {
    private final IOrderBook orderBook;
    private final OrderMatchingStrategy orderMatchingStrategy;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final TradeService tradeService;
    public Order placeOrder(Order order) {
        //  TODO:validation

        order.setOrderAcceptedTimeStamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setRemainingQuantity(order.getQuantity());

        orderBook.addOrder(order);

        CompletableFuture.runAsync(() -> {
            try{
                executeOrderMatch(order);
            }catch(Exception e){
                log.error("Error executing order match",e);
            }
        },executorService);
        return order;
    }

    private void executeOrderMatch(Order newOrder){
        String StockSymbol = newOrder.getStockSymbol();

        List<Order>existingOrders = orderBook.getOrders(StockSymbol);
        existingOrders.stream().filter(order->!order.getOrderId().equals(newOrder.getOrderId())).collect(Collectors.toList());

        List<Trade>executedTraders=orderMatchingStrategy.matchOrders(newOrder,existingOrders);

        if(!executedTraders.isEmpty()){
            for (Trade trade : executedTraders) {
                // Save trade in the db
                tradeService.saveTrade(trade);
            }
//            orderBook.updateOrder(newOrder);
//
//            for(Trade trade : executedTraders){
//                String otherOrderId=newOrder.getOrderType() == OrderType.BUY ? trade.getSellerOrderId() :  trade.getBuyerOrderId();
//                orderBook.getOrderByOrderId(otherOrderId).ifPresent(orderBook::updateOrder);
//            }
            log.info("Order matched successfully");
        }
    }

}
