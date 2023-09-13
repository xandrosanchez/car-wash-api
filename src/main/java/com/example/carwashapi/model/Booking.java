package com.example.carwashapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonIgnore
    private Service service;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @Column(nullable = false)
    @NotNull(message = "Start time cannot be null")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @NotNull(message = "End time cannot be null")
    private LocalDateTime endTime;


}
