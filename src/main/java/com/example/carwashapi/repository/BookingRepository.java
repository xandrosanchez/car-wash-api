package com.example.carwashapi.repository;

import com.example.carwashapi.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.endTime > :startTime AND b.startTime < :endTime")
    long countOverlappingBookings(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE b.id <> :bookingId " +
            "AND ((:startTime BETWEEN b.startTime AND b.endTime) OR " +
            "(:endTime BETWEEN b.startTime AND b.endTime) OR " +
            "(b.startTime BETWEEN :startTime AND :endTime))")
    long countOverlappingBookingsWithId(@Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime,
                                        @Param("bookingId") Long bookingId);
}
