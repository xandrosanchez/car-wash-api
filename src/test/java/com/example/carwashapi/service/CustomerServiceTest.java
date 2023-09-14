package com.example.carwashapi.service;

import com.example.carwashapi.dto.CustomerRequest;
import com.example.carwashapi.exception.CustomerNotFoundException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCustomerById_Success() throws NotFoundException {
        // Arrange
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(1L);
        expectedCustomer.setName("John Doe");
        expectedCustomer.setPhoneNumber("1234567890");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(expectedCustomer));

        // Act
        Customer actualCustomer = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer, actualCustomer);
    }

    @Test(expected = NotFoundException.class)
    public void testGetCustomerById_CustomerNotFound() throws NotFoundException {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        customerService.getCustomerById(1L);
    }

    @Test
    public void testGetRemainingTimeUntilNextBooking_WithBookings() {
        // Arrange
        Customer customer = new Customer();
        Booking booking1 = new Booking();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);
        booking1.setStartTime(oneHourLater);
        Booking booking2 = new Booking();
        LocalDateTime twoHoursLater = now.plusHours(2);
        booking2.setStartTime(twoHoursLater);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        customer.setBookings(bookings);

        // Act
        long remainingMinutes = customerService.getRemainingTimeUntilNextBooking(customer);

        // Assert
        assertEquals(60, remainingMinutes);
    }

    @Test
    public void testGetRemainingTimeUntilNextBooking_NoBookings() {
        // Arrange
        Customer customer = new Customer();

        // Act
        long remainingMinutes = customerService.getRemainingTimeUntilNextBooking(customer);

        // Assert
        assertEquals(-1, remainingMinutes);
    }

    @Test
    public void testGetCustomerByPhoneNumber_Success() throws CustomerNotFoundException {
        // Arrange
        String phoneNumber = "1234567890";
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(1L);
        expectedCustomer.setName("John Doe");
        expectedCustomer.setPhoneNumber(phoneNumber);

        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(expectedCustomer));

        // Act
        Customer actualCustomer = customerService.getCustomerByPhoneNumber(phoneNumber);

        // Assert
        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer, actualCustomer);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testGetCustomerByPhoneNumber_CustomerNotFound() throws CustomerNotFoundException {
        // Arrange
        String phoneNumber = "1234567890";
        when(customerRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        // Act
        customerService.getCustomerByPhoneNumber(phoneNumber);
    }

    @Test
    public void testCreateCustomer_Success() {
        // Arrange
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe");
        customerRequest.setPhoneNumber("1234567890");

        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(1L);
        expectedCustomer.setName(customerRequest.getName());
        expectedCustomer.setPhoneNumber(customerRequest.getPhoneNumber());

        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

        // Act
        Customer actualCustomer = customerService.createCustomer(customerRequest);

        // Assert
        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer, actualCustomer);
    }

    @Test
    public void testGetAllCustomers() {
        // Arrange
        List<Customer> expectedCustomers = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");
        customer1.setPhoneNumber("1234567890");
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");
        customer2.setPhoneNumber("9876543210");
        expectedCustomers.add(customer1);
        expectedCustomers.add(customer2);

        when(customerRepository.findAll()).thenReturn(expectedCustomers);

        // Act
        List<Customer> actualCustomers = customerService.getAllCustomers();

        // Assert
        assertEquals(expectedCustomers, actualCustomers);
    }

    @Test
    public void testUpdateCustomer_Success() throws CustomerNotFoundException {
        // Arrange
        Long customerId = 1L;
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Updated Name");
        customerRequest.setPhoneNumber("9876543210");

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setName("John Doe");
        existingCustomer.setPhoneNumber("1234567890");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Customer updatedCustomer = customerService.updateCustomer(customerId, customerRequest);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals(customerId, updatedCustomer.getId());
        assertEquals(customerRequest.getName(), updatedCustomer.getName());
        assertEquals(customerRequest.getPhoneNumber(), updatedCustomer.getPhoneNumber());
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testUpdateCustomer_CustomerNotFound() throws CustomerNotFoundException {
        // Arrange
        Long customerId = 1L;
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Updated Name");
        customerRequest.setPhoneNumber("9876543210");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act
        customerService.updateCustomer(customerId, customerRequest);
    }

    @Test
    public void testDeleteCustomer_Success() {
        // Arrange
        Long customerId = 1L;

        // Act
        customerService.deleteCustomer(customerId);

        // Assert
        verify(customerRepository, times(1)).deleteById(customerId);
    }
}
