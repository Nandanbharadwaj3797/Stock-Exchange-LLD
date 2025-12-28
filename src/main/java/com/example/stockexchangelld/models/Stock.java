package com.example.stockexchangelld.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Builder.Default
    private String stockId= UUID.randomUUID().toString();

    @NotBlank(message="Stock name is required")
    private String stockName;

    @NotBlank(message="Stock symbol is required")
    private String stockSymbol;

    @NotBlank(message="Stock price is required")
    private double stockPrice;


}
