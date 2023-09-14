package com.example.carwashapi.service;

import com.example.carwashapi.dto.TimeslotRequest;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.exception.TimeslotNotFoundException;
import com.example.carwashapi.model.Service;
import com.example.carwashapi.model.Timeslot;
import com.example.carwashapi.repository.TimeslotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TimeslotServiceTest {

    @Mock
    private TimeslotRepository timeslotRepository;

    @Mock
    private ServiceService serviceService;

    @InjectMocks
    private TimeslotService timeslotService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllTimeslots() {
        // Arrange
        List<Timeslot> timeslots = new ArrayList<>();
        when(timeslotRepository.findAll()).thenReturn(timeslots);

        // Act
        List<Timeslot> result = timeslotService.getAllTimeslots();

        // Assert
        assertNotNull(result);
        assertEquals(timeslots, result);
    }

    @Test
    public void testGetTimeslotById() throws TimeslotNotFoundException {
        // Arrange
        Long timeslotId = 1L;
        Timeslot timeslot = new Timeslot();
        timeslot.setId(timeslotId);
        when(timeslotRepository.findById(timeslotId)).thenReturn(Optional.of(timeslot));

        // Act
        Timeslot result = timeslotService.getTimeslotById(timeslotId);

        // Assert
        assertNotNull(result);
        assertEquals(timeslot, result);
    }

    @Test
    public void testGetTimeslotById_ThrowsTimeslotNotFoundException() {
        // Arrange
        Long timeslotId = 1L;
        when(timeslotRepository.findById(timeslotId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(TimeslotNotFoundException.class, () -> timeslotService.getTimeslotById(timeslotId));
    }

    @Test
    public void testAddTimeslot() throws ServiceNotFoundException {
        // Arrange
        TimeslotRequest timeslotRequest = new TimeslotRequest();
        timeslotRequest.setServiceId(1L);
        timeslotRequest.setStartTime(LocalDateTime.parse("2023-09-15T10:00:00"));
        timeslotRequest.setEndTime(LocalDateTime.parse("2023-09-15T11:00:00"));
        timeslotRequest.setAvailable(true);

        Service service = new Service();
        service.setId(1L);
        when(serviceService.getServiceById(timeslotRequest.getServiceId())).thenReturn(service);

        Timeslot newTimeslot = new Timeslot();
        newTimeslot.setService(service);
        newTimeslot.setStartTime(timeslotRequest.getStartTime());
        newTimeslot.setEndTime(timeslotRequest.getEndTime());
        newTimeslot.setAvailable(timeslotRequest.isAvailable());
        when(timeslotRepository.save(any(Timeslot.class))).thenReturn(newTimeslot);

        // Act
        Timeslot result = timeslotService.addTimeslot(timeslotRequest);

        // Assert
        assertNotNull(result);
        assertEquals(newTimeslot, result);
    }

    @Test
    public void testDeleteTimeslot() {
        // Arrange
        Long timeslotId = 1L;
        when(timeslotRepository.existsById(timeslotId)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> timeslotService.deleteTimeslot(timeslotId));

        // Assert
        verify(timeslotRepository, times(1)).deleteById(timeslotId);
    }

    @Test
    public void testDeleteTimeslot_ThrowsTimeslotNotFoundException() {
        // Arrange
        Long timeslotId = 1L;
        when(timeslotRepository.existsById(timeslotId)).thenReturn(false);

        // Act and Assert
        assertThrows(TimeslotNotFoundException.class, () -> timeslotService.deleteTimeslot(timeslotId));
    }

    @Test
    public void testUpdateTimeslot() throws ServiceNotFoundException, TimeslotNotFoundException {
        // Arrange
        Long timeslotId = 1L;
        TimeslotRequest updatedTimeslotRequest = new TimeslotRequest();
        updatedTimeslotRequest.setServiceId(1L);
        updatedTimeslotRequest.setStartTime(LocalDateTime.parse("2023-09-15T14:00:00"));
        updatedTimeslotRequest.setEndTime(LocalDateTime.parse("2023-09-15T15:00:00"));
        updatedTimeslotRequest.setAvailable(false);

        Timeslot existingTimeslot = new Timeslot();
        existingTimeslot.setId(timeslotId);
        when(timeslotRepository.existsById(timeslotId)).thenReturn(true);
        when(timeslotRepository.findById(timeslotId)).thenReturn(Optional.of(existingTimeslot));

        Service service = new Service();
        service.setId(1L);
        when(serviceService.getServiceById(updatedTimeslotRequest.getServiceId())).thenReturn(service);

        existingTimeslot.setService(service);
        existingTimeslot.setStartTime(updatedTimeslotRequest.getStartTime());
        existingTimeslot.setEndTime(updatedTimeslotRequest.getEndTime());
        existingTimeslot.setAvailable(updatedTimeslotRequest.isAvailable());
        when(timeslotRepository.save(any(Timeslot.class))).thenReturn(existingTimeslot);

        // Act
        Timeslot result = timeslotService.updateTimeslot(timeslotId, updatedTimeslotRequest);

        // Assert
        assertNotNull(result);
        assertEquals(timeslotId, result.getId());
        assertEquals(updatedTimeslotRequest.getServiceId(), result.getService().getId());
        assertEquals(updatedTimeslotRequest.getStartTime(), result.getStartTime());
        assertEquals(updatedTimeslotRequest.getEndTime(), result.getEndTime());
        assertEquals(updatedTimeslotRequest.isAvailable(), result.isAvailable());
    }

    @Test
    public void testUpdateTimeslot_ThrowsTimeslotNotFoundException() {
        // Arrange
        Long timeslotId = 1L;
        TimeslotRequest updatedTimeslotRequest = new TimeslotRequest();
        updatedTimeslotRequest.setServiceId(1L);
        updatedTimeslotRequest.setStartTime(LocalDateTime.parse("2023-09-15T14:00:00"));
        updatedTimeslotRequest.setEndTime(LocalDateTime.parse("2023-09-15T15:00:00"));
        updatedTimeslotRequest.setAvailable(false);

        when(timeslotRepository.existsById(timeslotId)).thenReturn(false);

        // Act and Assert
        assertThrows(TimeslotNotFoundException.class, () -> timeslotService.updateTimeslot(timeslotId, updatedTimeslotRequest));
    }

    @Test
    public void testUpdateTimeslot_ThrowsServiceNotFoundException() throws ServiceNotFoundException {
        // Arrange
        Long timeslotId = 1L;
        TimeslotRequest updatedTimeslotRequest = new TimeslotRequest();
        updatedTimeslotRequest.setServiceId(1L);
        updatedTimeslotRequest.setStartTime(LocalDateTime.parse("2023-09-15T14:00:00"));
        updatedTimeslotRequest.setEndTime(LocalDateTime.parse("2023-09-15T15:00:00"));
        updatedTimeslotRequest.setAvailable(false);

        Timeslot existingTimeslot = new Timeslot();
        existingTimeslot.setId(timeslotId);
        when(timeslotRepository.existsById(timeslotId)).thenReturn(true);
        when(timeslotRepository.findById(timeslotId)).thenReturn(Optional.of(existingTimeslot));

        when(serviceService.getServiceById(updatedTimeslotRequest.getServiceId())).thenThrow(ServiceNotFoundException.class);

        // Act and Assert
        assertThrows(ServiceNotFoundException.class, () -> timeslotService.updateTimeslot(timeslotId, updatedTimeslotRequest));
    }
}