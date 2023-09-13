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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Получает список всех услуг")
    @GetMapping("/services")
    public List<Service> getAllServices() {
        logger.info("Запрос списка всех услуг");
        return bookingService.getAllServices();
    }

    @Operation(summary = "Получает доступное время для услуги")
    @GetMapping("/availability/{serviceId}")
    public List<Timeslot> getAvailabilityForService(
            @Parameter(in = ParameterIn.PATH, name = "serviceId", description = "ID услуги")
            @PathVariable Long serviceId) throws NotFoundException, ServiceNotFoundException {
        logger.info("Запрос доступного времени для услуги с ID: {}", serviceId);
        Service service = bookingService.getServiceById(serviceId);
        if (service == null) {
            logger.error("Услуга с ID {} не найдена", serviceId);
            throw new NotFoundException("Service not found");
        }
        return bookingService.getAvailableTimeSlotsForService(service);
    }

    @Operation(summary = "Создает бронирование")
    @PostMapping("/create")
    public ResponseEntity<Booking> createBooking(
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для создания бронирования")
            @Valid @RequestBody BookingRequest bookingRequest) throws BookingConflictException, NotFoundException, ServiceNotFoundException {
        logger.info("Создание нового бронирования");
        Booking createdBooking = bookingService.createBooking(bookingRequest);
        logger.info("Бронирование создано с ID: {}", createdBooking.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    @Operation(summary = "Получает бронирование по ID")
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingById(
            @Parameter(in = ParameterIn.PATH, name = "bookingId", description = "ID бронирования")
            @PathVariable Long bookingId) throws NotFoundException {
        logger.info("Запрос бронирования по ID: {}", bookingId);
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            logger.error("Бронирование с ID {} не найдено", bookingId);
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
        logger.info("Обновление бронирования по ID: {}", bookingId);
        Booking updatedBooking = bookingService.updateBooking(bookingId, bookingRequest);
        logger.info("Бронирование с ID {} обновлено", bookingId);
        return ResponseEntity.ok(updatedBooking);
    }

    @Operation(summary = "Удаляет бронирование по ID")
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBookingById(
            @Parameter(in = ParameterIn.PATH, name = "bookingId", description = "ID бронирования")
            @PathVariable Long bookingId) {
        logger.info("Удаление бронирования по ID: {}", bookingId);
        bookingService.deleteBooking(bookingId);
        logger.info("Бронирование с ID {} удалено", bookingId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получает список всех бронирований")
    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        logger.info("Запрос списка всех бронирований");
        return bookingService.getAllBookings();
    }
}