package com.example.carwashapi.service;

import com.example.carwashapi.controller.CustomerRequest;
import com.example.carwashapi.exception.CustomerNotFoundException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    public long getRemainingTimeUntilNextBooking(Customer customer) {
        if (customer.getBookings().isEmpty()) {
            return -1; // Если у клиента нет броней, возвращаем -1, как индикатор отсутствия ближайших броней.
        }

        LocalDateTime currentTime = LocalDateTime.now();
        long remainingMinutes = Long.MAX_VALUE;

        for (Booking booking : customer.getBookings()) {
            LocalDateTime bookingStartTime = booking.getStartTime();

            if (bookingStartTime.isAfter(currentTime)) {
                long minutesUntilBooking = ChronoUnit.MINUTES.between(currentTime, bookingStartTime);
                remainingMinutes = Math.min(remainingMinutes, minutesUntilBooking);
            }
        }

        return remainingMinutes == Long.MAX_VALUE ? -1 : remainingMinutes;
    }


    public Customer getCustomerByPhoneNumber(String phoneNumber) throws CustomerNotFoundException {
        return customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with phone number: " + phoneNumber));
    }

    public Customer createCustomer(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long customerId, CustomerRequest customerRequest) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setName(customerRequest.getName());
            existingCustomer.setPhoneNumber(customerRequest.getPhoneNumber());
            return customerRepository.save(existingCustomer);
        } else {
            throw new CustomerNotFoundException("Customer not found");
        }
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
