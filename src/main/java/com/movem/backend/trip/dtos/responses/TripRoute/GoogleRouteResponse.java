package com.movem.backend.trip.dtos.responses.TripRoute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleRouteResponse {
    @JsonProperty("routes")
    List<GoogleRoute> routes;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoogleRoute {

        @JsonProperty("distanceMeters")
        private Integer distanceMeters;

        @JsonProperty("duration")
        private String duration;

        @JsonProperty("staticDuration")
        private String staticDuration;

        @JsonProperty("polyline")
        private Polyline polyline;

        @JsonProperty("legs")
        private List<GoogleLeg> legs;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoogleLeg {

        @JsonProperty("distanceMeters")
        private Integer distanceMeters;

        @JsonProperty("duration")
        private String duration;

        @JsonProperty("staticDuration")
        private String staticDuration;

        @JsonProperty("polyline")
        private Polyline polyline;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Polyline {

        @JsonProperty("encodedPolyline")
        private String encodedPolyline;
    }
}