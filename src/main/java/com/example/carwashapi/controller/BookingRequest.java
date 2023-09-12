package com.example.carwashapi.controller;

import com.example.carwashapi.model.Customer;
import com.example.carwashapi.model.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Getter
@AllArgsConstructor
public class BookingRequest {
    private Long customerId; // Идентификатор клиента
    private Long serviceId;  // Идентификатор выбранной услуги
    private LocalDateTime startTime; // Время начала записи
    private LocalDateTime endTime;   // Время окончания записи
}

