package com.movem.backend.Entity.Trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_packing_items", indexes = {
        @Index(name = "idx_packing_trip", columnList = "trip_activity_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripPackingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id", nullable = false)
    private Trip trip;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "is_packed")
    private Boolean isPacked = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
