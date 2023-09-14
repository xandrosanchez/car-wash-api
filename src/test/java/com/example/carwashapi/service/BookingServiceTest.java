package com.example.carwashapi.service;

import com.example.carwashapi.dto.BookingRequest;
import com.example.carwashapi.exception.BookingConflictException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.*;
import com.example.carwashapi.repository.BookingRepository;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ServiceService serviceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllServices() {
        // Arrange
        com.example.carwashapi.model.Service service1 = new com.example.carwashapi.model.Service();
        service1.setId(1L);
        service1.setName("Service 1");

        com.example.carwashapi.model.Service service2 = new com.example.carwashapi.model.Service();
        service2.setId(2L);
        service2.setName("Service 2");

        List<com.example.carwashapi.model.Service> expectedServices = Arrays.asList(service1, service2);

        when(serviceService.getAllServices()).thenReturn(expectedServices);

        // Act
        List<com.example.carwashapi.model.Service> actualServices = bookingService.getAllServices();

        // Assert
        assertEquals(expectedServices, actualServices);
    }

    @Test
    public void testGetServiceById() throws ServiceNotFoundException {
        // Arrange
        com.example.carwashapi.model.Service expectedService = new com.example.carwashapi.model.Service();
        expectedService.setId(1L);
        expectedService.setName("Service 1");

        when(serviceService.getServiceById(1L)).thenReturn(expectedService);

        // Act
        com.example.carwashapi.model.Service actualService = bookingService.getServiceById(1L);

        // Assert
        assertEquals(expectedService, actualService);
    }

    @SneakyThrows
    @Test
    public void testGetAvailableTimeSlotsForService() {
        // Arrange
        com.example.carwashapi.model.Service service = new com.example.carwashapi.model.Service();
        service.setName("Service 1");

        Timeslot timeslot1 = new Timeslot();
        timeslot1.setAvailable(true);

        Timeslot timeslot2 = new Timeslot();
        timeslot2.setAvailable(false);

        Timeslot timeslot3 = new Timeslot();
        timeslot3.setAvailable(true);

        service.setTimeslots(Arrays.asList(timeslot1, timeslot2, timeslot3));

        List<Timeslot> expectedTimeSlots = service.getTimeslots().stream()
                .filter(Timeslot::isAvailable)
                .collect(Collectors.toList());

        when(serviceService.getServiceById(1L)).thenReturn(service);

        // Act
        List<Timeslot> actualTimeSlots = bookingService.getAvailableTimeSlotsForService(service);

        // Assert
        assertEquals(expectedTimeSlots, actualTimeSlots);
    }

    @Test
    public void testCreateBooking_Success() throws BookingConflictException, NotFoundException, ServiceNotFoundException {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1L);
        bookingRequest.setServiceId(2L);
        bookingRequest.setStartTime(LocalDateTime.now());
        bookingRequest.setEndTime(LocalDateTime.now().plusHours(1));

        Customer customer = new Customer();
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        com.example.carwashapi.model.Service service = new com.example.carwashapi.model.Service();
        when(serviceService.getServiceById(2L)).thenReturn(service);

        when(bookingRepository.countOverlappingBookings(any(), any())).thenReturn(0L);

        Booking booking = new Booking();
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        Booking createdBooking = bookingService.createBooking(bookingRequest);

        // Assert
        assertEquals(booking, createdBooking);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test(expected = BookingConflictException.class)
    public void testCreateBooking_Conflict() throws BookingConflictException, NotFoundException, ServiceNotFoundException {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1L);
        bookingRequest.setServiceId(2L);
        bookingRequest.setStartTime(LocalDateTime.now());
        bookingRequest.setEndTime(LocalDateTime.now().plusHours(1));

        when(bookingRepository.countOverlappingBookings(any(), any())).thenReturn(1L);

        // Act
        bookingService.createBooking(bookingRequest);
    }

    @Test(expected = NotFoundException.class)
    public void testCreateBooking_CustomerNotFound() throws BookingConflictException, NotFoundException, ServiceNotFoundException {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1L);
        bookingRequest.setServiceId(2L);
        bookingRequest.setStartTime(LocalDateTime.now());
        bookingRequest.setEndTime(LocalDateTime.now().plusHours(1));

        when(customerService.getCustomerById(1L)).thenReturn(null);

        // Act
        bookingService.createBooking(bookingRequest);
    }

    @Test(expected = NotFoundException.class)
    public void testCreateBooking_ServiceNotFound() throws BookingConflictException, NotFoundException, ServiceNotFoundException {
        // Arrange
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setCustomerId(1L);
        bookingRequest.setServiceId(2L);
        bookingRequest.setStartTime(LocalDateTime.now());
        bookingRequest.setEndTime(LocalDateTime.now().plusHours(1));

        when(customerService.getCustomerById(1L)).thenReturn(new Customer());
        when(serviceService.getServiceById(2L)).thenReturn(null);

        // Act
        bookingService.createBooking(bookingRequest);
    }
}
