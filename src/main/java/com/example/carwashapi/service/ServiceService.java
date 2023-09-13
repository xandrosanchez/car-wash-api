package com.example.carwashapi.service;

import com.example.carwashapi.controller.ServiceRequest;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Сервис для управления услугами.
 */
@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository repository;
    private final Logger logger = LoggerFactory.getLogger(ServiceService.class);

    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    /**
     * Получает список всех доступных услуг.
     *
     * @return Список всех услуг.
     */
    public List<Service> getAllServices() {
        logger.info("Запрос всех услуг");
        return repository.findAll();
    }

    /**
     * Получает услугу по её идентификатору.
     *
     * @param serviceId Идентификатор услуги.
     * @return Услуга с указанным идентификатором.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
    public Service getServiceById(Long serviceId) throws ServiceNotFoundException {
        logger.info("Запрос услуги по ID: {}", serviceId);
        return repository.findById(serviceId)
                .orElseThrow(() -> {
                    logger.error("Услуга с ID {} не найдена", serviceId);
                    return new ServiceNotFoundException("Service not found");
                });
    }

    /**
     * Добавляет новую услугу.
     *
     * @param serviceRequest Данные для создания новой услуги.
     * @return Созданная услуга.
     */
    public Service addService(ServiceRequest serviceRequest) {
        logger.info("Добавление новой услуги: {}", serviceRequest.getName());
        Service service = new Service();
        service.setName(serviceRequest.getName());
        service.setPrice(serviceRequest.getPrice());
        return repository.save(service);
    }

    /**
     * Удаляет услугу по её идентификатору.
     *
     * @param serviceId Идентификатор услуги, которую требуется удалить.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
    public void deleteService(Long serviceId) throws ServiceNotFoundException {
        logger.info("Удаление услуги по ID: {}", serviceId);
        if (!repository.existsById(serviceId)) {
            logger.error("Услуга с ID {} не найдена и не может быть удалена", serviceId);
            throw new ServiceNotFoundException("Service not found");
        }
        repository.deleteById(serviceId);
    }

    /**
     * Обновляет информацию о услуге по её идентификатору.
     *
     * @param serviceId           Идентификатор услуги, которую требуется обновить.
     * @param updatedServiceRequest Данные для обновления услуги.
     * @return Обновленная услуга.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
    public Service updateService(Long serviceId, ServiceRequest updatedServiceRequest) throws ServiceNotFoundException {
        logger.info("Обновление услуги по ID: {}", serviceId);
        if (!repository.existsById(serviceId)) {
            logger.error("Услуга с ID {} не найдена и не может быть обновлена", serviceId);
            throw new ServiceNotFoundException("Service not found");
        }
        Service service = repository.findById(serviceId).get();
        service.setName(updatedServiceRequest.getName());
        service.setPrice(updatedServiceRequest.getPrice());
        return repository.save(service);
    }
}
