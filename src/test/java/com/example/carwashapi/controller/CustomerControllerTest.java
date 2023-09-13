package com.example.carwashapi.controller;

import com.example.carwashapi.exception.CustomerNotFoundException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRemainingTimeToBooking() throws CustomerNotFoundException {
        // Подготавливаем данные для теста
        String phoneNumber = "1234567890";
        Customer customer = new Customer();
        customer.setId(1L);

        // Мокируем поведение customerService
        when(customerService.getCustomerByPhoneNumber(eq(phoneNumber))).thenReturn(customer);
        when(customerService.getRemainingTimeUntilNextBooking(eq(customer))).thenReturn(30L); // Оставшееся время

        // Вызываем метод контроллера
        ResponseEntity<Long> response = customerController.getRemainingTimeToBooking(phoneNumber);

        // Проверяем результаты
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(30L, response.getBody().longValue());

        // Проверяем вызовы методов в customerService
        verify(customerService, times(1)).getCustomerByPhoneNumber(eq(phoneNumber));
        verify(customerService, times(1)).getRemainingTimeUntilNextBooking(eq(customer));
    }

    @Test
    public void testGetRemainingTimeToBooking_CustomerNotFound() throws CustomerNotFoundException {
        // Подготавливаем данные для теста
        String phoneNumber = "1234567890";

        // Мокируем поведение customerService для ситуации, когда клиент не найден
        when(customerService.getCustomerByPhoneNumber(eq(phoneNumber))).thenReturn(null);

        // Вызываем метод контроллера
        ResponseEntity<Long> response = customerController.getRemainingTimeToBooking(phoneNumber);

        // Проверяем результаты
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Проверяем вызовы методов в customerService
        verify(customerService, times(1)).getCustomerByPhoneNumber(eq(phoneNumber));
        verify(customerService, never()).getRemainingTimeUntilNextBooking(any());
    }

    @Test
    public void testCreateCustomer() {
        // Подготавливаем данные для теста
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("John Doe");

        Customer createdCustomer = new Customer();
        createdCustomer.setId(1L);
        createdCustomer.setName("John Doe");

        // Мокируем поведение customerService
        when(customerService.createCustomer(eq(customerRequest))).thenReturn(createdCustomer);

        // Вызываем метод контроллера
        ResponseEntity<Customer> response = customerController.createCustomer(customerRequest);

        // Проверяем результаты
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCustomer, response.getBody());

        // Проверяем вызовы методов в customerService
        verify(customerService, times(1)).createCustomer(eq(customerRequest));
    }

    @Test
    public void testGetAllCustomers() {
        // Подготавливаем данные для теста
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John Doe",null,null));
        customers.add(new Customer(2L, "Jane Smith",null,null));

        // Мокируем поведение customerService
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Вызываем метод контроллера
        List<Customer> response = customerController.getAllCustomers();

        // Проверяем результаты
        assertEquals(customers, response);

        // Проверяем вызовы методов в customerService
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    public void testGetCustomerById() throws CustomerNotFoundException, NotFoundException {
        // Подготавливаем данные для теста
        long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");

        // Мокируем поведение customerService
        when(customerService.getCustomerById(eq(customerId))).thenReturn(customer);

        // Вызываем метод контроллера
        ResponseEntity<Customer> response = customerController.getCustomerById(customerId);

        // Проверяем результаты
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());

        // Проверяем вызовы методов в customerService
        verify(customerService, times(1)).getCustomerById(eq(customerId));
    }

    @Test
    public void testUpdateCustomer() throws CustomerNotFoundException {
        long customerId = 1L;
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setName("Updated Name");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customerId);
        updatedCustomer.setName("Updated Name");


        // Мокируем поведение customerService
        when(customerService.updateCustomer(eq(customerId), eq(customerRequest))).thenReturn(updatedCustomer);

        // Вызываем метод контроллера
        ResponseEntity<Customer> response = customerController.updateCustomer(customerId, customerRequest);

        // Проверяем результаты
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCustomer, response.getBody());

        // Проверяем вызовы методов в customerService
        verify(customerService, times(1)).updateCustomer(eq(customerId), eq(customerRequest));
    }

    @Test
    public void testDeleteCustomerById() {
        // Подготавливаем данные для теста
        long customerId = 1L;

        // Вызываем метод контроллера
        ResponseEntity<Void> response = customerController.deleteCustomerById(customerId);

        // Проверяем результаты
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Проверяем вызовы методов в customerService
        verify(customerService, times(1)).deleteCustomer(eq(customerId));
    }
}
