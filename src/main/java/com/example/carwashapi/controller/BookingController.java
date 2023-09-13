package com.example.carwashapi.controller;

import com.example.carwashapi.exception.BookingConflictException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.model.Timeslot;
import com.example.carwashapi.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Получает список всех услуг")
    @GetMapping("/services")
    public List<Service> getAllServices() {
        return bookingService.getAllServices();
    }

    @Operation(summary = "Получает доступное время для услуги")
    @GetMapping("/availability/{serviceId}")
    public List<Timeslot> getAvailabilityForService(
            @Parameter(in = ParameterIn.PATH, name = "serviceId", description = "ID услуги")
            @PathVariable Long serviceId) throws NotFoundException, ServiceNotFoundException {
        Service service = bookingService.getServiceById(serviceId);
        if (service == null) {
            throw new NotFoundException("Service not found");
        }
        return bookingService.getAvailableTimeSlotsForService(service);
    }

    @Operation(summary = "Создает бронирование")
    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для создания бронирования")
            @Valid @RequestBody BookingRequest bookingRequest) throws BookingConflictException, NotFoundException, ServiceNotFoundException {
        Booking createdBooking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @Operation(summary = "Получает бронирование по ID")
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingById(
            @Parameter(in = ParameterIn.PATH, name = "bookingId", description = "ID бронирования")
            @PathVariable Long bookingId) throws NotFoundException {
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }
        return ResponseEntity.ok(booking);
    }

    @Operation(summary = "Обновляет бронирование по ID")
    @PutMapping("/{bookingId}")
    public ResponseEntity<Booking> updateBooking(
            @Parameter(in = ParameterIn.PATH, name = "bookingId", description = "ID бронирования")
            @PathVariable Long bookingId,
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для обновления бронирования")
            @RequestBody BookingRequest bookingRequest) throws BookingConflictException, NotFoundException {
        Booking updatedBooking = bookingService.updateBooking(bookingId, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @Operation(summary = "Удаляет бронирование по ID")
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBookingById(
            @Parameter(in = ParameterIn.PATH, name = "bookingId", description = "ID бронирования")
            @PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получает список всех бронирований")
    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
