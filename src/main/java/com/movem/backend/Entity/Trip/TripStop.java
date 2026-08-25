package com.movem.backend.Entity.Trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_stops", indexes = {
        @Index(name = "idx_trip_stop_trip", columnList = "trip_activity_id"),
        @Index(name = "idx_trip_stop_sequence", columnList = "sequence_order")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id", nullable = false)
    private Trip trip;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "location_address")
    private String locationAddress;

    private BigDecimal lat;

    private BigDecimal lng;

    @Column(name = "google_place_id")
    private String googlePlaceId;

    // Ignore geometry for now — same simplification Activity.coordinates already uses
    @Column(name = "coordinates")
    private String coordinates;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;
}
