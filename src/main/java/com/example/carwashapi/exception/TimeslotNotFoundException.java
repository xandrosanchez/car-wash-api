package com.example.carwashapi.exception;

public class TimeslotNotFoundException extends Exception {
    public TimeslotNotFoundException(String timeslotNotFound) {
        super(timeslotNotFound);
    }
}
