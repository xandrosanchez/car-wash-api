package com.example.carwashapi.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(nullable = false)
    @Positive(message = "Price must be a positive value")
    private double price;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Timeslot> timeslots;
}
