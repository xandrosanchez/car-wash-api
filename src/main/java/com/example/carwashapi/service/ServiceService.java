package com.example.carwashapi.service;

import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {
    private final ServiceRepository repository;

    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    public List<com.example.carwashapi.model.Service> getAllServices() {
        return repository.findAll();
    }

    public com.example.carwashapi.model.Service getServiceById(Long serviceId) {
        try {
            return repository.findById(serviceId).orElseThrow(() ->
                    new ServiceNotFoundException("Service doesn't exists"));
        } catch (ServiceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
