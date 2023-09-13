package com.example.carwashapi.controller;

import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/admin/services")
@Validated
public class ServiceAdminController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceAdminController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Добавляет новую услугу.
     *
     * @param serviceRequest Данные для добавления услуги.
     * @return Созданная услуга.
     */
    @Operation(summary = "Добавляет новую услугу")
    @PostMapping("/add")
    public ResponseEntity<Service> addService(
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для добавления услуги")
            @Valid @RequestBody ServiceRequest serviceRequest) {
        Service createdService = serviceService.addService(serviceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdService);
    }

    /**
     * Удаляет услугу по её идентификатору.
     *
     * @param serviceId Идентификатор услуги, которую требуется удалить.
     * @return ResponseEntity без содержимого (No Content) в случае успешного удаления.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
    @Operation(summary = "Удаляет услугу по ID")
    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<Void> deleteService(
            @Parameter(in = ParameterIn.PATH, name = "serviceId", description = "ID услуги")
            @Positive(message = "serviceId должен быть положительным числом")
            @PathVariable Long serviceId) throws ServiceNotFoundException {
        serviceService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Обновляет услугу по её идентификатору.
     *
     * @param serviceId       Идентификатор услуги, которую требуется обновить.
     * @param serviceRequest  Данные для обновления услуги.
     * @return Обновленная услуга.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
    @Operation(summary = "Обновляет услугу по ID")
    @PutMapping("/update/{serviceId}")
    public ResponseEntity<Service> updateService(
            @Parameter(in = ParameterIn.PATH, name = "serviceId", description = "ID услуги")
            @Positive(message = "serviceId должен быть положительным числом")
            @PathVariable Long serviceId,
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для обновления услуги")
            @Valid @RequestBody ServiceRequest serviceRequest) throws ServiceNotFoundException {
        Service updatedService = serviceService.updateService(serviceId, serviceRequest);
        return ResponseEntity.ok(updatedService);
    }
}
