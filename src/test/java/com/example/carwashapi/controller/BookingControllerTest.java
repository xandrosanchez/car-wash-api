package com.example.carwashapi.controller;

import com.example.carwashapi.controller.BookingController;
import com.example.carwashapi.controller.BookingRequest;
import com.example.carwashapi.exception.BookingConflictException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.model.Timeslot;
import com.example.carwashapi.service.BookingService;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllServices() {
        // Arrange
        List<Service> services = new ArrayList<>();
        when(bookingService.getAllServices()).thenReturn(services);

        // Act
        List<Service> result = bookingController.getAllServices();

        // Assert
        assertNotNull(result);
        assertEquals(services, result);
    }

    @Test
    public void testGetAvailabilityForService() throws NotFoundException, ServiceNotFoundException {
        // Arrange
        Long serviceId = 1L;
        List<Timeslot> timeslots = new ArrayList<>();
        when(bookingService.getServiceById(eq(serviceId))).thenReturn(new Service());

        // Act
        List<Timeslot> result = bookingController.getAvailabilityForService(serviceId);

        // Assert
        assertNotNull(result);
        assertEquals(timeslots, result);
    }

    @Test
    public void testCreateBooking() throws BookingConflictException, NotFoundException, ServiceNotFoundException {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        Booking createdBooking = new Booking();
        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(createdBooking);

        // Act
        ResponseEntity<Booking> result = bookingController.createBooking(bookingRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(createdBooking, result.getBody());
    }

    @Test
    public void testGetBookingById() throws NotFoundException {
        // Arrange
        Long bookingId = 1L;
        Booking booking = new Booking();
        when(bookingService.getBookingById(bookingId)).thenReturn(booking);

        // Act
        ResponseEntity<Booking> result = bookingController.getBookingById(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(booking, result.getBody());
    }

    @Test
    public void testUpdateBooking() throws BookingConflictException, NotFoundException {
        // Arrange
        Long bookingId = 1L;
        BookingRequest bookingRequest = new BookingRequest();
        Booking updatedBooking = new Booking();
        when(bookingService.updateBooking(eq(bookingId), any(BookingRequest.class))).thenReturn(updatedBooking);

        // Act
        ResponseEntity<Booking> result = bookingController.updateBooking(bookingId, bookingRequest);

        // Assert
        assertNotNull(result);
        assertEquals(updatedBooking, result.getBody());
    }

    @Test
    public void testDeleteBookingById() {
        // Arrange
        Long bookingId = 1L;

        // Act
        ResponseEntity<Void> result = bookingController.deleteBookingById(bookingId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    public void testGetAllBookings() {
        // Arrange
        List<Booking> bookings = new ArrayList<>();
        when(bookingService.getAllBookings()).thenReturn(bookings);

        // Act
        List<Booking> result = bookingController.getAllBookings();

        // Assert
        assertNotNull(result);
        assertEquals(bookings, result);
    }
}
