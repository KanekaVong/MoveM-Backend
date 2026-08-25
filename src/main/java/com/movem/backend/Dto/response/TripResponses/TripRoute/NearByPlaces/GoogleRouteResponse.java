package com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleRouteResponse {

    @JsonProperty("routes")
    private List<GoogleRoute> routes;


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