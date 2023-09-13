package com.example.carwashapi.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
    @NotNull(message = "name не может быть null")
    private String name; // Название услуги

    @NotNull(message = "price не может быть null")
    @Positive(message = "price должен быть положительным числом")
    private Double price; // Цена услуги
}
