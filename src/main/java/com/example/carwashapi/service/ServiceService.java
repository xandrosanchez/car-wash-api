package com.example.carwashapi.service;

import com.example.carwashapi.dto.ServiceRequest;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Service;

import java.util.List;

public interface ServiceService {
    public List<Service> getAllServices();
    public Service getServiceById(Long serviceId) throws ServiceNotFoundException;
    public Service addService(ServiceRequest serviceRequest);
    public void deleteService(Long serviceId) throws ServiceNotFoundException;
    public Service updateService(Long serviceId, ServiceRequest updatedServiceRequest) throws ServiceNotFoundException;
}
