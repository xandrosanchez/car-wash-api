package com.example.carwashapi.controller;

import com.example.carwashapi.dto.BookingRequest;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для управления бронированиями.
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Получает список всех доступных услуг.
     *
     * @return Список всех доступных услуг.
     */
    @Operation(summary = "Получает список всех услуг")
    @GetMapping("/services")
    public List<Service> getAllServices() {
        logger.info("Запрос списка всех услуг");
        return bookingService.getAllServices();
    }

    /**
     * Получает доступное время для указанной услуги.
     *
     * @param serviceId Идентификатор услуги.
     * @return Список доступных временных слотов для указанной услуги.
     * @throws NotFoundException       если услуга не найдена.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
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

    /**
     * Создает новое бронирование.
     *
     * @param bookingRequest Данные для создания бронирования.
     * @return Созданное бронирование.
     * @throws BookingConflictException если есть конфликт в расписании.
     * @throws NotFoundException        если клиент или услуга не найдены.
     * @throws ServiceNotFoundException  если услуга не найдена.
     */
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

    /**
     * Получает бронирование по его идентификатору.
     *
     * @param bookingId Идентификатор бронирования.
     * @return Бронирование с указанным идентификатором.
     * @throws NotFoundException если бронирование не найдено.
     */
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

    /**
     * Обновляет бронирование по его идентификатору.
     *
     * @param bookingId       Идентификатор бронирования, которое требуется обновить.
     * @param bookingRequest  Данные для обновления бронирования.
     * @return Обновленное бронирование.
     * @throws BookingConflictException если есть конфликт в расписании.
     * @throws NotFoundException        если бронирование не найдено.
     */
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

    /**
     * Удаляет бронирование по его идентификатору.
     *
     * @param bookingId Идентификатор бронирования, которое требуется удалить.
     * @return ResponseEntity без содержимого (No Content) в случае успешного удаления.
     */
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

    /**
     * Получает список всех бронирований.
     *
     * @return Список всех бронирований.
     */
    @Operation(summary = "Получает список всех бронирований")
    @GetMapping("/all")
    public List<Booking> getAllBookings() {
        logger.info("Запрос списка всех бронирований");
        return bookingService.getAllBookings();
    }
}