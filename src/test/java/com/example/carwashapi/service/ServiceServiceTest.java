package com.example.carwashapi.service;

import com.example.carwashapi.controller.ServiceRequest;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllServices() {
        // Arrange
        List<Service> services = new ArrayList<>();
        when(serviceRepository.findAll()).thenReturn(services);

        // Act
        List<Service> result = serviceService.getAllServices();

        // Assert
        assertNotNull(result);
        assertEquals(services, result);
    }

    @Test
    public void testGetServiceById() throws ServiceNotFoundException {
        // Arrange
        Long serviceId = 1L;
        Service service = new Service();
        service.setId(serviceId);
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        // Act
        Service result = serviceService.getServiceById(serviceId);

        // Assert
        assertNotNull(result);
        assertEquals(service, result);
    }

    @Test
    public void testGetServiceById_ThrowsServiceNotFoundException() {
        // Arrange
        Long serviceId = 1L;
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ServiceNotFoundException.class, () -> serviceService.getServiceById(serviceId));
    }

    @Test
    public void testAddService() {
        // Arrange
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setName("Test Service");
        serviceRequest.setPrice(10.0);
        Service newService = new Service();
        newService.setName(serviceRequest.getName());
        newService.setPrice(serviceRequest.getPrice());
        when(serviceRepository.save(any(Service.class))).thenReturn(newService);

        // Act
        Service result = serviceService.addService(serviceRequest);

        // Assert
        assertNotNull(result);
        assertEquals(newService.getName(), result.getName());
        assertEquals(newService.getPrice(), result.getPrice());
    }

    @Test
    public void testDeleteService() {
        // Arrange
        Long serviceId = 1L;
        when(serviceRepository.existsById(serviceId)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> serviceService.deleteService(serviceId));

        // Assert
        verify(serviceRepository, times(1)).deleteById(serviceId);
    }

    @Test
    public void testDeleteService_ThrowsServiceNotFoundException() {
        // Arrange
        Long serviceId = 1L;
        when(serviceRepository.existsById(serviceId)).thenReturn(false);

        // Act and Assert
        assertThrows(ServiceNotFoundException.class, () -> serviceService.deleteService(serviceId));
    }

    @Test
    public void testUpdateService() throws ServiceNotFoundException {
        // Arrange
        Long serviceId = 1L;
        ServiceRequest updatedServiceRequest = new ServiceRequest();
        updatedServiceRequest.setName("Updated Service");
        updatedServiceRequest.setPrice(20.0);
        Service existingService = new Service();
        existingService.setId(serviceId);
        when(serviceRepository.existsById(serviceId)).thenReturn(true);
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(existingService));
        when(serviceRepository.save(any(Service.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Service result = serviceService.updateService(serviceId, updatedServiceRequest);

        // Assert
        assertNotNull(result);
        assertEquals(serviceId, result.getId());
        assertEquals(updatedServiceRequest.getName(), result.getName());
        assertEquals(updatedServiceRequest.getPrice(), result.getPrice());
    }

    @Test
    public void testUpdateService_ThrowsServiceNotFoundException() {
        // Arrange
        Long serviceId = 1L;
        ServiceRequest updatedServiceRequest = new ServiceRequest();
        updatedServiceRequest.setName("Updated Service");
        updatedServiceRequest.setPrice(20.0);
        when(serviceRepository.existsById(serviceId)).thenReturn(false);

        // Act and Assert
        assertThrows(ServiceNotFoundException.class, () -> serviceService.updateService(serviceId, updatedServiceRequest));
    }
}
