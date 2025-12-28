package com.example.stockexchangelld.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Builder.Default
    private String userId= UUID.randomUUID().toString();

    @NotBlank(message="UserName is required")
    private String userName;

    @NotBlank(message="Invalid email address")
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;
}
