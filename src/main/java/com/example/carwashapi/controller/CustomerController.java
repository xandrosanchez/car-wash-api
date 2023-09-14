package com.example.carwashapi.controller;

import com.example.carwashapi.dto.CustomerRequest;
import com.example.carwashapi.exception.CustomerNotFoundException;
import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.model.Customer;
import com.example.carwashapi.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@Validated
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Получает оставшееся время до записи по номеру клиента.
     *
     * @param phoneNumber Номер клиента.
     * @return Оставшееся время до бронирования клиента.
     * @throws CustomerNotFoundException если клиент не найден.
     */
    @Operation(summary = "Получает оставшееся время до записи по номеру клиента")
    @GetMapping("/remaining-time/{phoneNumber}")
    public ResponseEntity<Long> getRemainingTimeToBooking(
            @Parameter(in = ParameterIn.PATH, name = "phoneNumber", description = "номер клиента")
            @PathVariable String phoneNumber) throws CustomerNotFoundException {
        log.info("Запрос оставшегося времени до бронирования для клиента с номером: {}", phoneNumber);

        Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.getCustomerByPhoneNumber(phoneNumber));

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            long remainingMinutes = customerService.getRemainingTimeUntilNextBooking(customer);

            if (remainingMinutes != -1) {
                log.info("Оставшееся время до бронирования для клиента с номером {} составляет {} минут", phoneNumber, remainingMinutes);
                return ResponseEntity.ok(remainingMinutes);
            }
        }

        log.info("Оставшееся время до бронирования для клиента с номером {} не найдено", phoneNumber);
        return ResponseEntity.notFound().build();
    }

    /**
     * Создает нового клиента.
     *
     * @param customerRequest Данные для создания клиента.
     * @return Созданный клиент.
     */
    @Operation(summary = "Создает нового Customer")
    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для создания Customer")
            @Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Создание нового клиента: {}", customerRequest.getName());

        Customer createdCustomer = customerService.createCustomer(customerRequest);
        log.info("Клиент {} успешно создан с ID: {}", createdCustomer.getName(), createdCustomer.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    /**
     * Получает список всех клиентов.
     *
     * @return Список всех клиентов.
     */
    @Operation(summary = "Получает список всех Customers")
    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Получает клиента по его идентификатору.
     *
     * @param customerId Идентификатор клиента.
     * @return Клиент с указанным идентификатором.
     * @throws NotFoundException если клиент не найден.
     */
    @Operation(summary = "Получает Customer по ID")
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(
            @Parameter(in = ParameterIn.PATH, name = "customerId", description = "ID Customer")
            @Positive(message = "customerId должен быть положительным числом")
            @PathVariable Long customerId) throws NotFoundException {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    /**
     * Обновляет данные клиента по его идентификатору.
     *
     * @param customerId      Идентификатор клиента, данные которого требуется обновить.
     * @param customerRequest Данные для обновления клиента.
     * @return Обновленный клиент.
     * @throws CustomerNotFoundException если клиент не найден.
     */
    @Operation(summary = "Обновляет Customer по ID")
    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(
            @Parameter(in = ParameterIn.PATH, name = "customerId", description = "ID Customer")
            @Positive(message = "customerId должен быть положительным числом")
            @PathVariable Long customerId,
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для обновления Customer")
            @Valid @RequestBody CustomerRequest customerRequest) throws CustomerNotFoundException {
        Customer customer = customerService.updateCustomer(customerId, customerRequest);
        return ResponseEntity.ok(customer);
    }

    /**
     * Удаляет клиента по его идентификатору.
     *
     * @param customerId Идентификатор клиента, которого требуется удалить.
     * @return ResponseEntity без содержимого (No Content) в случае успешного удаления.
     */
    @Operation(summary = "Удаляет Customer по ID")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomerById(
            @Parameter(in = ParameterIn.PATH, name = "customerId", description = "ID Customer")
            @Positive(message = "customerId должен быть положительным числом")
            @PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
