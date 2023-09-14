package com.example.carwashapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
}
