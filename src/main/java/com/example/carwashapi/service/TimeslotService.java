package com.example.carwashapi.service;

import com.example.carwashapi.controller.TimeslotRequest;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.exception.TimeslotNotFoundException;
import com.example.carwashapi.model.Timeslot;
import com.example.carwashapi.repository.TimeslotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeslotService {
    private final TimeslotRepository repository;
    private final ServiceService serviceService;
    private final Logger logger = LoggerFactory.getLogger(TimeslotService.class);

    public TimeslotService(TimeslotRepository repository, ServiceService serviceService) {
        this.repository = repository;
        this.serviceService = serviceService;
    }

    public List<Timeslot> getAllTimeslots() {
        logger.info("Запрос всех Timeslot");
        return repository.findAll();
    }

    public Timeslot getTimeslotById(Long timeslotId) throws TimeslotNotFoundException {
        logger.info("Запрос Timeslot по ID: {}", timeslotId);
        return repository.findById(timeslotId)
                .orElseThrow(() -> {
                    logger.error("Timeslot с ID {} не найден", timeslotId);
                    return new TimeslotNotFoundException("Timeslot not found");
                });
    }

    public Timeslot addTimeslot(TimeslotRequest timeslotRequest) throws ServiceNotFoundException {
        logger.info("Добавление нового Timeslot");
        com.example.carwashapi.model.Service service = serviceService.getServiceById(timeslotRequest.getServiceId());
        Timeslot timeslot = new Timeslot();
        timeslot.setService(service);
        timeslot.setStartTime(timeslotRequest.getStartTime());
        timeslot.setEndTime(timeslotRequest.getEndTime());
        timeslot.setAvailable(timeslotRequest.isAvailable());
        return repository.save(timeslot);
    }

    public void deleteTimeslot(Long timeslotId) throws TimeslotNotFoundException {
        logger.info("Удаление Timeslot по ID: {}", timeslotId);
        if (!repository.existsById(timeslotId)) {
            logger.error("Timeslot с ID {} не найден и не может быть удален", timeslotId);
            throw new TimeslotNotFoundException("Timeslot not found");
        }
        repository.deleteById(timeslotId);
    }

    public Timeslot updateTimeslot(Long timeslotId, TimeslotRequest updatedTimeslotRequest)
            throws TimeslotNotFoundException, ServiceNotFoundException {
        logger.info("Обновление Timeslot по ID: {}", timeslotId);
        if (!repository.existsById(timeslotId)) {
            logger.error("Timeslot с ID {} не найден и не может быть обновлен", timeslotId);
            throw new TimeslotNotFoundException("Timeslot not found");
        }
        com.example.carwashapi.model.Service service = serviceService.getServiceById(updatedTimeslotRequest.getServiceId());
        Timeslot timeslot = repository.findById(timeslotId).get();
        timeslot.setService(service);
        timeslot.setStartTime(updatedTimeslotRequest.getStartTime());
        timeslot.setEndTime(updatedTimeslotRequest.getEndTime());
        timeslot.setAvailable(updatedTimeslotRequest.isAvailable());
        return repository.save(timeslot);
    }
}