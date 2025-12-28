package com.example.stockexchangelld.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Builder.Default
    private String orderId= UUID.randomUUID().toString();

    @NotBlank(message="User Id is required")
    private String userId;

    @NotNull(message="Order Type is required")
    private OrderType orderType;

    @NotNull(message="Stock ID is required")
    private String stockId;

    @NotNull(message="Quantity is required")
    private int quantity;

    @NotNull(message="price is required")
    private double price;

    @Builder.Default
    private OrderStatus orderStatus= OrderStatus.ACCEPTED;

    @Builder.Default
    private int filledQuantity=0;

    @Builder.Default
    private int remainingQuantity=0;

    @Builder.Default
    private LocalDateTime orderAcceptedTimeStamp=LocalDateTime.now();


}
