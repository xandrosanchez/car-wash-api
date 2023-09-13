package com.example.carwashapi.exception;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String customerNotFound) {
        super(customerNotFound);
    }
}
