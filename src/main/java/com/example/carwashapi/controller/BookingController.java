package com.example.carwashapi.controller;

import com.example.carwashapi.exception.BookingConflictException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.model.TimeSlot;
import com.example.carwashapi.repository.BookingRepository;
import com.example.carwashapi.service.BookingService;
import com.example.carwashapi.service.CustomerService;
import com.example.carwashapi.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/services")
    public List<Service> getAllServices() {
        return bookingService.getAllServices();
    }

    @GetMapping("/availability/{serviceId}")
    public List<TimeSlot> getAvailabilityForService(@PathVariable Long serviceId) throws NotFoundException {
        Service service = bookingService.getServiceById(serviceId);
        if (service == null) {
            throw new NotFoundException("Service not found");
        }
        return bookingService.getAvailableTimeSlotsForService(service);
    }

    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) throws BookingConflictException, NotFoundException {
        Booking createdBooking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }
}
