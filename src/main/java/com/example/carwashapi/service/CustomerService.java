package com.example.carwashapi.service;

import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerById(Long customerId) throws NotFoundException {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));
    }
}
