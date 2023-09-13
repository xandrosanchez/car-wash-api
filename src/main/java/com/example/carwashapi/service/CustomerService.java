package com.example.carwashapi.service;

import com.example.carwashapi.controller.CustomerRequest;
import com.example.carwashapi.exception.CustomerNotFoundException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Booking;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления клиентами и их бронированиями.
 */
@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Получает клиента по его идентификатору.
     *
     * @param customerId Идентификатор клиента.
     * @return Клиент с указанным идентификатором.
     * @throws NotFoundException если клиент не найден.
     */
    public Customer getCustomerById(Long customerId) throws NotFoundException {
        log.info("Поиск клиента по ID: {}", customerId);
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));
    }

    /**
     * Получает оставшееся время до следующего бронирования клиента.
     *
     * @param customer Клиент, для которого нужно определить время до бронирования.
     * @return Оставшееся время до ближайшего бронирования клиента в минутах, или -1, если у клиента нет броней.
     */
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

    /**
     * Получает клиента по номеру телефона.
     *
     * @param phoneNumber Номер телефона клиента.
     * @return Клиент с указанным номером телефона.
     * @throws CustomerNotFoundException если клиент не найден.
     */
    public Customer getCustomerByPhoneNumber(String phoneNumber) throws CustomerNotFoundException {
        log.info("Поиск клиента по номеру телефона: {}", phoneNumber);
        return customerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with phone number: " + phoneNumber));
    }

    /**
     * Создает нового клиента.
     *
     * @param customerRequest Данные для создания клиента.
     * @return Созданный клиент.
     */
    public Customer createCustomer(CustomerRequest customerRequest) {
        log.info("Создание нового клиента: {}", customerRequest.getName());
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setPhoneNumber(customerRequest.getPhoneNumber());
        return customerRepository.save(customer);
    }

    /**
     * Получает список всех клиентов.
     *
     * @return Список всех клиентов.
     */
    public List<Customer> getAllCustomers() {
        log.info("Получение списка всех клиентов");
        return customerRepository.findAll();
    }

    /**
     * Обновляет информацию о клиенте по его идентификатору.
     *
     * @param customerId      Идентификатор клиента, который требуется обновить.
     * @param customerRequest Данные для обновления клиента.
     * @return Обновленный клиент.
     * @throws CustomerNotFoundException если клиент не найден.
     */
    public Customer updateCustomer(Long customerId, CustomerRequest customerRequest) throws CustomerNotFoundException {
        log.info("Обновление клиента с ID: {}", customerId);
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

    /**
     * Удаляет клиента по его идентификатору.
     *
     * @param customerId Идентификатор клиента, который требуется удалить.
     */
    public void deleteCustomer(Long customerId) {
        log.info("Удаление клиента с ID: {}", customerId);
        customerRepository.deleteById(customerId);
    }
}
