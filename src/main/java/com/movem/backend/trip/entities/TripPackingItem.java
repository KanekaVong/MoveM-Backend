package com.movem.backend.trip.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_packing_items", indexes = {@Index(name = "idx_packing_trip", columnList = "trip_activity_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripPackingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id", nullable = false)
    Trip trip;
    @Column(name = "item_name", nullable = false)
    String itemName;
    @Column(name = "is_packed")
    Boolean isPacked = false;
    @Column(name = "created_at")
    LocalDateTime createdAt;
}
