package com.example.carwashapi.repository;

import com.example.carwashapi.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
}
