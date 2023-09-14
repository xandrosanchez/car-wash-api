package com.example.carwashapi.service;

import com.example.carwashapi.dto.BookingRequest;
import com.example.carwashapi.exception.BookingConflictException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.model.Timeslot;
import com.example.carwashapi.repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления бронированиями услуг.
 */
@Slf4j
@Service
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    private final CustomerServiceImpl customerService;
    private final ServiceServiceImpl serviceService;

    public BookingServiceImpl(BookingRepository bookingRepository, CustomerServiceImpl customerService, ServiceServiceImpl serviceService) {
        this.bookingRepository = bookingRepository;
        this.customerService = customerService;
        this.serviceService = serviceService;
    }

    /**
     * Получает список всех доступных услуг.
     *
     * @return Список всех доступных услуг.
     */
    public List<com.example.carwashapi.model.Service> getAllServices() {
        log.info("Запрос всех услуг");
        return serviceService.getAllServices();
    }

    /**
     * Получает услугу по ее идентификатору.
     *
     * @param serviceId Идентификатор услуги.
     * @return Услуга с указанным идентификатором.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
    public com.example.carwashapi.model.Service getServiceById(Long serviceId) throws ServiceNotFoundException {
        log.info("Запрос услуги по ID: {}", serviceId);
        return serviceService.getServiceById(serviceId);
    }

    /**
     * Получает список доступных временных слотов для услуги.
     *
     * @param service Услуга, для которой запрашиваются временные слоты.
     * @return Список доступных временных слотов.
     */
    public List<Timeslot> getAvailableTimeSlotsForService(com.example.carwashapi.model.Service service) {
        log.info("Запрос доступных временных слотов для услуги: {}", service.getName());
        return service.getTimeslots().stream()
                .filter(Timeslot::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Создает новое бронирование на указанное время.
     *
     * @param bookingRequest Запрос на создание бронирования.
     * @return Созданное бронирование.
     * @throws BookingConflictException если временной слот уже занят другим бронированием.
     * @throws NotFoundException        если клиент или услуга не найдены.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
    public Booking createBooking(BookingRequest bookingRequest) throws BookingConflictException, NotFoundException, ServiceNotFoundException {
        LocalDateTime startTime = bookingRequest.getStartTime();
        LocalDateTime endTime = bookingRequest.getEndTime();

        log.info("Создание бронирования начато: startTime={}, endTime={}", startTime, endTime);

        if (!isTimeSlotAvailable(startTime, endTime)) {
            log.error("Ошибка при создании бронирования: Time slot is not available");
            throw new BookingConflictException("Time slot is not available");
        }
        Customer customer = customerService.getCustomerById(bookingRequest.getCustomerId());
        if (customer == null) {
            log.error("Ошибка при создании бронирования: Customer not found");
            throw new NotFoundException("Customer not found");
        }
        com.example.carwashapi.model.Service service = serviceService.getServiceById(bookingRequest.getServiceId());
        if (service == null) {
            log.error("Ошибка при создании бронирования: Service not found");
            throw new NotFoundException("Service not found");
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setService(service);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);

        log.info("Создание бронирования завершено");
        return bookingRepository.save(booking);
    }

    /**
     * Получает список всех бронирований.
     *
     * @return Список всех бронирований.
     */
    public List<Booking> getAllBookings() {
        log.info("Запрос всех бронирований");
        return bookingRepository.findAll();
    }

    /**
     * Получает бронирование по его идентификатору.
     *
     * @param bookingId Идентификатор бронирования.
     * @return Бронирование с указанным идентификатором.
     */
    public Booking getBookingById(Long bookingId) {
        log.info("Запрос бронирования по ID: {}", bookingId);
        return bookingRepository.findById(bookingId)
                .orElse(null);
    }

    /**
     * Удаляет бронирование по его идентификатору.
     *
     * @param bookingId Идентификатор бронирования.
     */
    public void deleteBooking(Long bookingId) {
        log.info("Удаление бронирования с ID: {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }

    /**
     * Обновляет информацию о бронировании на указанное время.
     *
     * @param bookingId      Идентификатор бронирования, которое требуется обновить.
     * @param bookingRequest Запрос на обновление бронирования.
     * @return Обновленное бронирование.
     * @throws BookingConflictException если временной слот уже занят другим бронированием.
     * @throws NotFoundException        если бронирование не найдено.
     */
    public Booking updateBooking(Long bookingId, BookingRequest bookingRequest) throws BookingConflictException, NotFoundException {
        log.info("Обновление бронирования с ID: {}", bookingId);
        Booking existingBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        LocalDateTime startTime = bookingRequest.getStartTime();
        LocalDateTime endTime = bookingRequest.getEndTime();

        if (!isTimeSlotAvailable(startTime, endTime, bookingId)) {
            log.error("Ошибка при обновлении бронирования: Time slot is not available");
            throw new BookingConflictException("Time slot is not available");
        }

        existingBooking.setStartTime(startTime);
        existingBooking.setEndTime(endTime);

        return bookingRepository.save(existingBooking);
    }

    /**
     * Проверяет доступность временного слота для бронирования.
     *
     * @param startTime Начальное время бронирования.
     * @param endTime   Конечное время бронирования.
     * @return true, если временной слот доступен для бронирования, в противном случае - false.
     */
    private boolean isTimeSlotAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        long overlappingBookings = bookingRepository.countOverlappingBookings(startTime, endTime);
        return overlappingBookings == 0;
    }

    /**
     * Проверяет доступность временного слота для бронирования с исключением указанного бронирования.
     *
     * @param startTime  Начальное время бронирования.
     * @param endTime    Конечное время бронирования.
     * @param bookingId  Идентификатор бронирования, которое нужно исключить из проверки на пересечение.
     * @return true, если временной слот доступен для бронирования, в противном случае - false.
     */
    private boolean isTimeSlotAvailable(LocalDateTime startTime, LocalDateTime endTime, Long bookingId) {
        long overlappingBookings = bookingRepository.countOverlappingBookingsWithId(startTime, endTime, bookingId);
        return overlappingBookings == 0;
    }
}
