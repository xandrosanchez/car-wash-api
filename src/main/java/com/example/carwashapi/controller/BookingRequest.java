package com.example.carwashapi.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
public class BookingRequest {
    @NotNull(message = "customerId не может быть null")
    @Positive(message = "customerId должен быть положительным числом")
    private Long customerId; // Идентификатор клиента

    @NotNull(message = "serviceId не может быть null")
    @Positive(message = "serviceId должен быть положительным числом")
    private Long serviceId;  // Идентификатор выбранной услуги

    @NotNull(message = "startTime не может быть null")
    private LocalDateTime startTime; // Время начала записи

    @NotNull(message = "endTime не может быть null")
    private LocalDateTime endTime;   // Время окончания записи
}

