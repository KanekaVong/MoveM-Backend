package com.movem.backend.Service.TripServices.TripRouteServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces.GoogleRouteResponse;
import com.movem.backend.Entity.Trip.TripStop;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TripRoutingService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${google.maps.api-key}")
    private String apiKey;

    @Value("${google.maps.routes-url}")
    private String routesUrl;


    public GoogleRouteResponse calculateRoute(
            TripStop origin,
            TripStop destination,
            String travelMode
    ) {

        String googleTravelMode =
                mapToGoogleTravelMode(travelMode);


        Map<String, Object> requestBody = Map.of(

                "origin", Map.of(
                        "location", Map.of(
                                "latLng", Map.of(
                                        "latitude",
                                        origin.getLat(),

                                        "longitude",
                                        origin.getLng()
                                )
                        )
                ),

                "destination", Map.of(
                        "location", Map.of(
                                "latLng", Map.of(
                                        "latitude",
                                        destination.getLat(),

                                        "longitude",
                                        destination.getLng()
                                )
                        )
                ),

                "travelMode",
                googleTravelMode
        );

        ResponseEntity<String> response =
                restClient
                        .post()
                        .uri(routesUrl)

                        .header(
                                "X-Goog-Api-Key",
                                apiKey
                        )

                        .header(
                                "X-Goog-FieldMask",
                                "*"
                        )

                        .contentType(
                                MediaType.APPLICATION_JSON
                        )

                        .body(requestBody)

                        .retrieve()

                        .onStatus(
                                status -> status.isError(),

                                (request, responseBody) -> {

                                    String errorBody;

                                    try {

                                        errorBody =
                                                new String(
                                                        responseBody
                                                                .getBody()
                                                                .readAllBytes()
                                                );

                                    } catch (Exception e) {

                                        errorBody =
                                                "Unable to read Google error body.";
                                    }

                                    throw new IllegalStateException(
                                            "Google Routes API error: "
                                                    + errorBody
                                    );
                                }
                        )

                        .toEntity(String.class);

        String responseBody =
                response.getBody();

        if (responseBody == null
                || responseBody.isBlank()
                || responseBody.trim().equals("{}")) {

            GoogleRouteResponse emptyResponse =
                    new GoogleRouteResponse();

            emptyResponse.setRoutes(
                    java.util.Collections.emptyList()
            );

            return emptyResponse;
        }

        try {

            GoogleRouteResponse result =
                    objectMapper.readValue(
                            responseBody,
                            GoogleRouteResponse.class
                    );

            if (result == null) {

                GoogleRouteResponse emptyResponse =
                        new GoogleRouteResponse();

                emptyResponse.setRoutes(
                        java.util.Collections.emptyList()
                );

                return emptyResponse;
            }


            if (result.getRoutes() == null) {

                result.setRoutes(
                        java.util.Collections.emptyList()
                );
            }


            return result;


        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to parse Google Routes response: "
                            + responseBody,
                    e
            );
        }
    }

    public GoogleRouteResponse calculateMultiStopRoute(
            java.util.List<TripStop> stops,
            String travelMode
    ) {

        if (stops == null || stops.size() < 2) {
            throw new IllegalArgumentException(
                    "At least two stops are required."
            );
        }

        String googleTravelMode =
                mapToGoogleTravelMode(travelMode);

        TripStop origin = stops.get(0);

        TripStop destination =
                stops.get(stops.size() - 1);

        Map<String, Object> originLocation =
                Map.of(
                        "location",
                        Map.of(
                                "latLng",
                                Map.of(
                                        "latitude",
                                        origin.getLat(),
                                        "longitude",
                                        origin.getLng()
                                )
                        )
                );

        Map<String, Object> destinationLocation =
                Map.of(
                        "location",
                        Map.of(
                                "latLng",
                                Map.of(
                                        "latitude",
                                        destination.getLat(),
                                        "longitude",
                                        destination.getLng()
                                )
                        )
                );

        java.util.List<Map<String, Object>> intermediates =
                new java.util.ArrayList<>();

        for (int i = 1; i < stops.size() - 1; i++) {

            TripStop stop = stops.get(i);

            intermediates.add(
                    Map.of(
                            "location",
                            Map.of(
                                    "latLng",
                                    Map.of(
                                            "latitude",
                                            stop.getLat(),
                                            "longitude",
                                            stop.getLng()
                                    )
                            )
                    )
            );
        }

        Map<String, Object> requestBody =
                new java.util.HashMap<>();

        requestBody.put(
                "origin",
                originLocation
        );

        requestBody.put(
                "destination",
                destinationLocation
        );

        requestBody.put(
                "travelMode",
                googleTravelMode
        );

        if (!intermediates.isEmpty()) {

            requestBody.put(
                    "intermediates",
                    intermediates
            );
        }

        ResponseEntity<String> response =
                restClient
                        .post()
                        .uri(routesUrl)

                        .header(
                                "X-Goog-Api-Key",
                                apiKey
                        )

                        .header(
                                "X-Goog-FieldMask",
                                "routes.distanceMeters,"
                                        + "routes.duration,"
                                        + "routes.staticDuration,"
                                        + "routes.polyline.encodedPolyline,"
                                        + "routes.legs.distanceMeters,"
                                        + "routes.legs.duration,"
                                        + "routes.legs.staticDuration,"
                                        + "routes.legs.polyline.encodedPolyline"
                        )

                        .contentType(
                                MediaType.APPLICATION_JSON
                        )

                        .body(requestBody)

                        .retrieve()

                        .onStatus(
                                status -> status.isError(),

                                (request, responseBody) -> {

                                    String errorBody;

                                    try {

                                        errorBody =
                                                new String(
                                                        responseBody
                                                                .getBody()
                                                                .readAllBytes()
                                                );

                                    } catch (Exception e) {

                                        errorBody =
                                                "Unable to read Google error body.";
                                    }

                                    throw new IllegalStateException(
                                            "Google Routes API error: "
                                                    + errorBody
                                    );
                                }
                        )

                        .toEntity(String.class);

        String responseBody =
                response.getBody();

        if (responseBody == null
                || responseBody.isBlank()
                || responseBody.trim().equals("{}")) {

            GoogleRouteResponse emptyResponse =
                    new GoogleRouteResponse();

            emptyResponse.setRoutes(
                    java.util.Collections.emptyList()
            );

            return emptyResponse;
        }

        try {

            GoogleRouteResponse result =
                    objectMapper.readValue(
                            responseBody,
                            GoogleRouteResponse.class
                    );

            if (result == null) {

                GoogleRouteResponse emptyResponse =
                        new GoogleRouteResponse();

                emptyResponse.setRoutes(
                        java.util.Collections.emptyList()
                );

                return emptyResponse;
            }

            if (result.getRoutes() == null) {

                result.setRoutes(
                        java.util.Collections.emptyList()
                );
            }

            return result;

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to parse Google multi-stop Routes response: "
                            + responseBody,
                    e
            );
        }
    }

    private String mapToGoogleTravelMode(
            String travelMode
    ) {

        if (travelMode == null
                || travelMode.isBlank()) {

            throw new IllegalArgumentException(
                    "Travel mode is required."
            );
        }


        return switch (
                travelMode.trim().toUpperCase()
                ) {

            case "WALKING" ->
                    "WALK";

            case "DRIVING" ->
                    "DRIVE";

            case "CYCLING" ->
                    "BICYCLE";

            case "RIDING" ->
                    "TWO_WHEELER";

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported travel mode: "
                                    + travelMode
                    );
        };
    }
}