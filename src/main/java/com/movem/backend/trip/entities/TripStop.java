package com.movem.backend.trip.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_stops", indexes = {@Index(name = "idx_trip_stop_trip", columnList = "trip_activity_id"), @Index(name = "idx_trip_stop_sequence", columnList = "sequence_order")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id", nullable = false)
    Trip trip;
    @Column(name = "location_name")
    String locationName;
    @Column(name = "sequence_order", nullable = false)
    Integer sequenceOrder;
    @Column(name = "arrival_time")
    LocalDateTime arrivalTime;
    @Column(name = "departure_time")
    LocalDateTime departureTime;
    @Column(name = "location_address")
    String locationAddress;
    BigDecimal lat;
    BigDecimal lng;
    @Column(name = "google_place_id")
    String googlePlaceId;
    @Column(name = "coordinates")
    String coordinates;
    @Column(name = "is_completed")
    Boolean isCompleted = false;
}
