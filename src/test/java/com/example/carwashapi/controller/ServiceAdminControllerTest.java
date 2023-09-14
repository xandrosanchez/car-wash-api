package com.example.carwashapi.controller;

import com.example.carwashapi.dto.ServiceRequest;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceAdminControllerTest {

    @InjectMocks
    private ServiceAdminController serviceAdminController;

    @Mock
    private ServiceService serviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddService_ValidInput_ReturnsCreatedService() {
        // Arrange
        ServiceRequest serviceRequest = new ServiceRequest();
        Service createdService = new Service();

        when(serviceService.addService(serviceRequest)).thenReturn(createdService);

        // Act
        ResponseEntity<Service> response = serviceAdminController.addService(serviceRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(createdService, response.getBody());

        verify(serviceService, times(1)).addService(serviceRequest);
    }

    @Test
    public void testDeleteService_ExistingServiceId_ReturnsNoContent() throws ServiceNotFoundException {
        // Arrange
        Long serviceId = 1L;

        // Act
        ResponseEntity<Void> response = serviceAdminController.deleteService(serviceId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(serviceService, times(1)).deleteService(serviceId);
    }

    @Test
    public void testDeleteService_NonExistingServiceId_ThrowsServiceNotFoundException() throws ServiceNotFoundException {
        // Arrange
        Long serviceId = 1L;

        // Мокируем поведение serviceService для ситуации, когда услуга не найдена
        doThrow(new ServiceNotFoundException()).when(serviceService).deleteService(serviceId);

        // Act & Assert
        assertThrows(ServiceNotFoundException.class, () -> {
            serviceAdminController.deleteService(serviceId);
        });

        verify(serviceService, times(1)).deleteService(serviceId);
    }

    @Test
    public void testUpdateService_ExistingServiceId_ReturnsUpdatedService() throws ServiceNotFoundException {
        // Arrange
        Long serviceId = 1L;
        ServiceRequest serviceRequest = new ServiceRequest();
        Service updatedService = new Service();

        when(serviceService.updateService(serviceId, serviceRequest)).thenReturn(updatedService);

        // Act
        ResponseEntity<Service> response = serviceAdminController.updateService(serviceId, serviceRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(updatedService, response.getBody());

        verify(serviceService, times(1)).updateService(serviceId, serviceRequest);
    }

    @Test
    public void testUpdateService_NonExistingServiceId_ThrowsServiceNotFoundException() throws ServiceNotFoundException {
        // Arrange
        Long serviceId = 1L;
        ServiceRequest serviceRequest = new ServiceRequest();

        // Мокируем поведение serviceService для ситуации, когда услуга не найдена
        doThrow(new ServiceNotFoundException()).when(serviceService).updateService(serviceId, serviceRequest);

        // Act & Assert
        assertThrows(ServiceNotFoundException.class, () -> {
            serviceAdminController.updateService(serviceId, serviceRequest);
        });

        verify(serviceService, times(1)).updateService(serviceId, serviceRequest);
    }
}
