package com.example.carwashapi.controller;

import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.exception.TimeslotNotFoundException;
import com.example.carwashapi.model.Timeslot;
import com.example.carwashapi.service.TimeslotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TimeslotAdminControllerTest {

    @InjectMocks
    private TimeslotAdminController timeslotAdminController;

    @Mock
    private TimeslotService timeslotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTimeslot_ValidInput_ReturnsCreatedTimeslot() throws ServiceNotFoundException {
        // Arrange
        TimeslotRequest timeslotRequest = new TimeslotRequest();
        Timeslot createdTimeslot = new Timeslot();

        when(timeslotService.addTimeslot(timeslotRequest)).thenReturn(createdTimeslot);

        // Act
        ResponseEntity<Timeslot> response = timeslotAdminController.addTimeslot(timeslotRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(createdTimeslot, response.getBody());

        verify(timeslotService, times(1)).addTimeslot(timeslotRequest);
    }

    @Test
    public void testAddTimeslot_InvalidInput_ThrowsConstraintViolationException() throws ServiceNotFoundException {
        // Arrange
        TimeslotRequest timeslotRequest = new TimeslotRequest();
        // Мокируем поведение timeslotService для ситуации, когда выбрасывается ConstraintViolationException
        when(timeslotService.addTimeslot(timeslotRequest)).thenThrow(ConstraintViolationException.class);

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            timeslotAdminController.addTimeslot(timeslotRequest);
        });

        verify(timeslotService, times(1)).addTimeslot(timeslotRequest);
    }

    @Test
    public void testDeleteTimeslot_ExistingTimeslotId_ReturnsNoContent() throws TimeslotNotFoundException {
        // Arrange
        Long timeslotId = 1L;

        // Act
        ResponseEntity<Void> response = timeslotAdminController.deleteTimeslot(timeslotId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(timeslotService, times(1)).deleteTimeslot(timeslotId);
    }

    @Test
    public void testDeleteTimeslot_NonExistingTimeslotId_ThrowsTimeslotNotFoundException() throws TimeslotNotFoundException {
        // Arrange
        Long timeslotId = 1L;

        // Мокируем поведение timeslotService для ситуации, когда Timeslot не найден
        doThrow(new TimeslotNotFoundException()).when(timeslotService).deleteTimeslot(timeslotId);

        // Act & Assert
        assertThrows(TimeslotNotFoundException.class, () -> {
            timeslotAdminController.deleteTimeslot(timeslotId);
        });

        verify(timeslotService, times(1)).deleteTimeslot(timeslotId);
    }

    @Test
    public void testUpdateTimeslot_ExistingTimeslotId_ReturnsUpdatedTimeslot() throws NotFoundException, TimeslotNotFoundException, ServiceNotFoundException {
        // Arrange
        Long timeslotId = 1L;
        TimeslotRequest timeslotRequest = new TimeslotRequest();
        Timeslot updatedTimeslot = new Timeslot();

        when(timeslotService.updateTimeslot(timeslotId, timeslotRequest)).thenReturn(updatedTimeslot);

        // Act
        ResponseEntity<Timeslot> response = timeslotAdminController.updateTimeslot(timeslotId, timeslotRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(updatedTimeslot, response.getBody());

        verify(timeslotService, times(1)).updateTimeslot(timeslotId, timeslotRequest);
    }

    @Test
    public void testUpdateTimeslot_InvalidInput_ThrowsConstraintViolationException() throws NotFoundException, TimeslotNotFoundException, ServiceNotFoundException {
        // Arrange
        Long timeslotId = 1L;
        TimeslotRequest timeslotRequest = new TimeslotRequest();
        // Мокируем поведение timeslotService для ситуации, когда выбрасывается ConstraintViolationException
        when(timeslotService.updateTimeslot(timeslotId, timeslotRequest)).thenThrow(ConstraintViolationException.class);

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            timeslotAdminController.updateTimeslot(timeslotId, timeslotRequest);
        });

        verify(timeslotService, times(1)).updateTimeslot(timeslotId, timeslotRequest);
    }

    @Test
    public void testUpdateTimeslot_NonExistingTimeslotId_ThrowsTimeslotNotFoundException() throws NotFoundException, TimeslotNotFoundException, ServiceNotFoundException {
        // Arrange
        Long timeslotId = 1L;
        TimeslotRequest timeslotRequest = new TimeslotRequest();
        // Мокируем поведение timeslotService для ситуации, когда Timeslot не найден
        doThrow(new TimeslotNotFoundException()).when(timeslotService).updateTimeslot(timeslotId, timeslotRequest);

        // Act & Assert
        assertThrows(TimeslotNotFoundException.class, () -> {
            timeslotAdminController.updateTimeslot(timeslotId, timeslotRequest);
        });

        verify(timeslotService, times(1)).updateTimeslot(timeslotId, timeslotRequest);
    }

    @Test
    public void testUpdateTimeslot_ServiceNotFound_ThrowsServiceNotFoundException() throws NotFoundException, TimeslotNotFoundException, ServiceNotFoundException {
        // Arrange
        Long timeslotId = 1L;
        TimeslotRequest timeslotRequest = new TimeslotRequest(); // Замените на реальные данные для обновления Timeslot

        // Мокируем поведение timeslotService для ситуации, когда услуга не найдена
        doThrow(new ServiceNotFoundException()).when(timeslotService).updateTimeslot(timeslotId, timeslotRequest);

        // Act & Assert
        assertThrows(ServiceNotFoundException.class, () -> {
            timeslotAdminController.updateTimeslot(timeslotId, timeslotRequest);
        });

        verify(timeslotService, times(1)).updateTimeslot(timeslotId, timeslotRequest);
    }

    @Test
    public void testGetTimeslotById_ExistingTimeslotId_ReturnsTimeslot() throws TimeslotNotFoundException {
        // Arrange
        Long timeslotId = 1L;
        Timeslot timeslot = new Timeslot(); // Замените на реальный Timeslot

        when(timeslotService.getTimeslotById(timeslotId)).thenReturn(timeslot);

        // Act
        ResponseEntity<Timeslot> response = timeslotAdminController.getTimeslotById(timeslotId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertSame(timeslot, response.getBody());

        verify(timeslotService, times(1)).getTimeslotById(timeslotId);
    }

    @Test
    public void testGetTimeslotById_NonExistingTimeslotId_ThrowsTimeslotNotFoundException() throws TimeslotNotFoundException {
        // Arrange
        Long timeslotId = 1L;

        // Мокируем поведение timeslotService для ситуации, когда Timeslot не найден
        doThrow(new TimeslotNotFoundException()).when(timeslotService).getTimeslotById(timeslotId);

        // Act & Assert
        assertThrows(TimeslotNotFoundException.class, () -> {
            timeslotAdminController.getTimeslotById(timeslotId);
        });

        verify(timeslotService, times(1)).getTimeslotById(timeslotId);
    }
}
