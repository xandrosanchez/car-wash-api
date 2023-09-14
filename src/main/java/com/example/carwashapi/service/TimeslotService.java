package com.example.carwashapi.service;

import com.example.carwashapi.dto.TimeslotRequest;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.exception.TimeslotNotFoundException;
import com.example.carwashapi.model.Timeslot;

import java.util.List;

public interface TimeslotService {
    public List<Timeslot> getAllTimeslots();
    public Timeslot getTimeslotById(Long timeslotId) throws TimeslotNotFoundException;
    public Timeslot addTimeslot(TimeslotRequest timeslotRequest) throws ServiceNotFoundException;
    public void deleteTimeslot(Long timeslotId) throws TimeslotNotFoundException;
    public Timeslot updateTimeslot(Long timeslotId, TimeslotRequest updatedTimeslotRequest) throws TimeslotNotFoundException, ServiceNotFoundException;
}
