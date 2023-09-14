package com.example.carwashapi.service;

import com.example.carwashapi.dto.CustomerRequest;
import com.example.carwashapi.exception.CustomerNotFoundException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Customer;

import java.util.List;

public interface CustomerService {
    public Customer getCustomerById(Long customerId) throws NotFoundException;
    public long getRemainingTimeUntilNextBooking(Customer customer);
    public Customer getCustomerByPhoneNumber(String phoneNumber) throws CustomerNotFoundException;
    public Customer createCustomer(CustomerRequest customerRequest);
    public List<Customer> getAllCustomers();
    public Customer updateCustomer(Long customerId, CustomerRequest customerRequest) throws CustomerNotFoundException;
    public void deleteCustomer(Long customerId);
}
