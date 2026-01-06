package com.example.stockexchangelld.controllers;

import com.example.stockexchangelld.dtos.OrderRequest;
import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.services.TradeService;
import com.example.stockexchangelld.services.TradingServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trading")
public class TradingController {

    private final TradeService tradeService;  //violate dip todo
    private final TradingServices tradingServices;

    @PostMapping("/orders")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order=tradingServices.placeOrder(orderRequest);

        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/orderBook/{symbol}")
    public ResponseEntity<List<Order>> getOrderBook(@PathVariable String symbol) {
        List<Order>orderBook=tradingServices.getOrderBook(symbol);

        return ResponseEntity.ok(orderBook);
    }
}
