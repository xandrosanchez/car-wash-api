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

/**
 * Сервис для управления временными слотами.
 */
@Service
public class TimeslotService {
    private final TimeslotRepository repository;
    private final ServiceService serviceService;
    private final Logger logger = LoggerFactory.getLogger(TimeslotService.class);

    public TimeslotService(TimeslotRepository repository, ServiceService serviceService) {
        this.repository = repository;
        this.serviceService = serviceService;
    }

    /**
     * Получает список всех доступных временных слотов.
     *
     * @return Список всех временных слотов.
     */
    public List<Timeslot> getAllTimeslots() {
        logger.info("Запрос всех Timeslot");
        return repository.findAll();
    }

    /**
     * Получает временный слот по его идентификатору.
     *
     * @param timeslotId Идентификатор временного слота.
     * @return Временный слот с указанным идентификатором.
     * @throws TimeslotNotFoundException если временный слот не найден.
     */
    public Timeslot getTimeslotById(Long timeslotId) throws TimeslotNotFoundException {
        logger.info("Запрос Timeslot по ID: {}", timeslotId);
        return repository.findById(timeslotId)
                .orElseThrow(() -> {
                    logger.error("Timeslot с ID {} не найден", timeslotId);
                    return new TimeslotNotFoundException("Timeslot not found");
                });
    }

    /**
     * Добавляет новый временный слот.
     *
     * @param timeslotRequest Данные для создания нового временного слота.
     * @return Созданный временный слот.
     * @throws ServiceNotFoundException если услуга не найдена.
     */
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

    /**
     * Удаляет временный слот по его идентификатору.
     *
     * @param timeslotId Идентификатор временного слота, который требуется удалить.
     * @throws TimeslotNotFoundException если временный слот не найден.
     */
    public void deleteTimeslot(Long timeslotId) throws TimeslotNotFoundException {
        logger.info("Удаление Timeslot по ID: {}", timeslotId);
        if (!repository.existsById(timeslotId)) {
            logger.error("Timeslot с ID {} не найден и не может быть удален", timeslotId);
            throw new TimeslotNotFoundException("Timeslot not found");
        }
        repository.deleteById(timeslotId);
    }

    /**
     * Обновляет информацию о временном слоте по его идентификатору.
     *
     * @param timeslotId            Идентификатор временного слота, который требуется обновить.
     * @param updatedTimeslotRequest Данные для обновления временного слота.
     * @return Обновленный временный слот.
     * @throws TimeslotNotFoundException если временный слот не найден.
     * @throws ServiceNotFoundException  если услуга не найдена.
     */
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