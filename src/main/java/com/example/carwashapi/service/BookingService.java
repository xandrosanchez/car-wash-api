package com.example.carwashapi.service;

import com.example.carwashapi.controller.BookingRequest;
import com.example.carwashapi.exception.BookingConflictException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.model.TimeSlot;
import com.example.carwashapi.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        return serviceService.getAllServices();
    }

    public com.example.carwashapi.model.Service getServiceById(Long serviceId) {
        return serviceService.getServiceById(serviceId);
    }

    public List<TimeSlot> getAvailableTimeSlotsForService(com.example.carwashapi.model.Service service) {
        return service.getTimeSlots().stream()
                .filter(TimeSlot::isAvailable)
                .collect(Collectors.toList());
    }

    public Booking createBooking(BookingRequest bookingRequest) throws BookingConflictException, NotFoundException {
        LocalDateTime startTime = bookingRequest.getStartTime();
        LocalDateTime endTime = bookingRequest.getEndTime();
        if (!isTimeSlotAvailable(startTime, endTime)) {
            throw new BookingConflictException("Time slot is not available");
        }
        Customer customer = customerService.getCustomerById(bookingRequest.getCustomerId());
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        com.example.carwashapi.model.Service service = serviceService.getServiceById(bookingRequest.getServiceId());
        if (service == null) {
            throw new NotFoundException("Service not found");
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setService(service);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);

        return bookingRepository.save(booking);
    }

    private boolean isTimeSlotAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        long overlappingBookings = bookingRepository.countOverlappingBookings(startTime, endTime);
        return overlappingBookings == 0;
    }
}
