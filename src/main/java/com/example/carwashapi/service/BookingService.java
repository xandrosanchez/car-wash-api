package com.example.carwashapi.service;

import com.example.carwashapi.controller.BookingRequest;
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

@Slf4j
@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerService customerService;
    private final ServiceService serviceService;

    public BookingService(BookingRepository bookingRepository, CustomerService customerService, ServiceService serviceService) {
        this.bookingRepository = bookingRepository;
        this.customerService = customerService;
        this.serviceService = serviceService;
    }

    public List<com.example.carwashapi.model.Service> getAllServices() {
        log.info("Запрос всех услуг");
        return serviceService.getAllServices();
    }

    public com.example.carwashapi.model.Service getServiceById(Long serviceId) throws ServiceNotFoundException {
        log.info("Запрос услуги по ID: {}", serviceId);
        return serviceService.getServiceById(serviceId);
    }

    public List<Timeslot> getAvailableTimeSlotsForService(com.example.carwashapi.model.Service service) {
        log.info("Запрос доступных временных слотов для услуги: {}", service.getName());
        return service.getTimeslots().stream()
                .filter(Timeslot::isAvailable)
                .collect(Collectors.toList());
    }

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

    public List<Booking> getAllBookings() {
        log.info("Запрос всех бронирований");
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long bookingId) {
        log.info("Запрос бронирования по ID: {}", bookingId);
        return bookingRepository.findById(bookingId)
                .orElse(null);
    }

    public void deleteBooking(Long bookingId) {
        log.info("Удаление бронирования с ID: {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }

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

    private boolean isTimeSlotAvailable(LocalDateTime startTime, LocalDateTime endTime, Long bookingId) {
        long overlappingBookings = bookingRepository.countOverlappingBookingsWithId(startTime, endTime, bookingId);
        return overlappingBookings == 0;
    }

    private boolean isTimeSlotAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        long overlappingBookings = bookingRepository.countOverlappingBookings(startTime, endTime);
        return overlappingBookings == 0;
    }
}
