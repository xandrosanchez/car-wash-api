package com.example.carwashapi.service;

import com.example.carwashapi.dto.BookingRequest;
import com.example.carwashapi.exception.BookingConflictException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.model.Timeslot;

import java.util.List;

public interface BookingService {
    public List<Service> getAllServices();
    public Service getServiceById(Long serviceId) throws ServiceNotFoundException;
    public List<Timeslot> getAvailableTimeSlotsForService(Service service);
    public List<Booking> getAllBookings();
    public Booking getBookingById(Long bookingId);
    public void deleteBooking(Long bookingId);
    public Booking updateBooking(Long bookingId, BookingRequest bookingRequest) throws BookingConflictException, NotFoundException;
    public Booking createBooking(BookingRequest bookingRequest) throws BookingConflictException, NotFoundException, ServiceNotFoundException;

}
