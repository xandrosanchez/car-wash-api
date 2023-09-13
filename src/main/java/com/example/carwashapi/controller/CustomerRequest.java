package com.example.carwashapi.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@Getter
@AllArgsConstructor
public class CustomerRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}
