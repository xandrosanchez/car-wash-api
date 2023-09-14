package com.example.carwashapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private HttpStatus status;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
}