package com.movem.backend.trip.entities;

import com.movem.backend.authentication.entities.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_bookmarks", indexes = {@Index(name = "idx_trip_bookmark_user", columnList = "user_id")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "google_place_id")
    String googlePlaceId;

    @Column(name = "location_name")
    String locationName;

    @Column(name = "location_address")
    String locationAddress;
    BigDecimal lat;
    BigDecimal lng;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
