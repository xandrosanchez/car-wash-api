package com.example.carwashapi.service;

import com.example.carwashapi.exception.CustomerNotFoundException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long customerId, Customer updatedCustomer) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
            return customerRepository.save(existingCustomer);
        } else {
            throw new CustomerNotFoundException("Customer not found");
        }
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
