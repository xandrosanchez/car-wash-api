package com.example.carwashapi.controller;

import com.example.carwashapi.exception.NotFoundException;
import com.example.carwashapi.exception.ServiceNotFoundException;
import com.example.carwashapi.exception.TimeslotNotFoundException;
import com.example.carwashapi.model.Timeslot;
import com.example.carwashapi.service.TimeslotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/admin/timeslots")
@Validated
public class TimeslotAdminController {

    private final TimeslotService timeslotService;

    @Autowired
    public TimeslotAdminController(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
    }

    @Operation(summary = "Добавляет новый Timeslot")
    @PostMapping("/add")
    public ResponseEntity<Timeslot> addTimeslot(
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для добавления Timeslot")
            @Valid @RequestBody TimeslotRequest timeslotRequest) throws ServiceNotFoundException {
        Timeslot createdTimeslot = timeslotService.addTimeslot(timeslotRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeslot);
    }

    @Operation(summary = "Удаляет Timeslot по ID")
    @DeleteMapping("/delete/{timeslotId}")
    public ResponseEntity<Void> deleteTimeslot(
            @Parameter(in = ParameterIn.PATH, name = "timeslotId", description = "ID Timeslot")
            @Positive(message = "timeslotId должен быть положительным числом")
            @PathVariable Long timeslotId) throws TimeslotNotFoundException {
        timeslotService.deleteTimeslot(timeslotId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновляет Timeslot по ID")
    @PutMapping("/update/{timeslotId}")
    public ResponseEntity<Timeslot> updateTimeslot(
            @Parameter(in = ParameterIn.PATH, name = "timeslotId", description = "ID Timeslot")
            @Positive(message = "timeslotId должен быть положительным числом")
            @PathVariable Long timeslotId,
            @Parameter(in = ParameterIn.DEFAULT, description = "Данные для обновления Timeslot")
            @Valid @RequestBody TimeslotRequest timeslotRequest) throws NotFoundException, TimeslotNotFoundException, ServiceNotFoundException {
        Timeslot updatedTimeslot = timeslotService.updateTimeslot(timeslotId, timeslotRequest);
        return ResponseEntity.ok(updatedTimeslot);
    }

    @Operation(summary = "Получает Timeslot по ID")
    @GetMapping("/{timeslotId}")
    public ResponseEntity<Timeslot> getTimeslotById(
            @Parameter(in = ParameterIn.PATH, name = "timeslotId", description = "ID Timeslot")
            @Positive(message = "timeslotId должен быть положительным числом")
            @PathVariable Long timeslotId) throws TimeslotNotFoundException {
        Timeslot timeslot = timeslotService.getTimeslotById(timeslotId);
        return ResponseEntity.ok(timeslot);
    }
}