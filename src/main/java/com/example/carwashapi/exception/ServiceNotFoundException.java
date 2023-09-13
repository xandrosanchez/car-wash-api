package com.example.carwashapi.exception;

public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException(){
        super();
    }
    public ServiceNotFoundException(String s) {
        super(s);
    }
}
