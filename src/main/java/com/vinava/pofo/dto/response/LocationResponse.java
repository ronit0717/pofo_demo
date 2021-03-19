package com.vinava.pofo.dto.response;

import com.vinava.pofo.model.Location;
import com.vinava.pofo.service.StoreService;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Data
@Builder
@Slf4j
public class LocationResponse {

    private long id;
    private long clientId;
    private String name;
    private String description;
    private String pincode;
    private List<StoreResponse> stores;
    private Date createdOn;
    private Date updatedOn;

    public static LocationResponse from(Location location, StoreService storeService) {
        ResponseEntity<List<StoreResponse>> storeResponses = storeService.getAllStores
                (location.getClientId(), location.getId(), 0, 50, "id", "DESC");
        List<StoreResponse> stores = storeResponses.getBody();
        return LocationResponse.builder()
                .id(location.getId())
                .clientId(location.getClientId())
                .name(location.getName())
                .description(location.getDescription())
                .pincode(location.getPincode())
                .stores(stores)
                .createdOn(location.getCreatedOn())
                .updatedOn(location.getUpdatedOn())
                .build();
    }

    private static List<LocationResponse> from(List<Location> locations, StoreService storeService) {
        List<LocationResponse> locationResponses = new LinkedList<>();
        for (Location location : locations) {
            locationResponses.add(from(location, storeService));
        }
        return locationResponses;
    }

    public static ResponseEntity<List<LocationResponse>> getResponseEntityFrom(List<Location> locations, StoreService storeService) {
        try {
            List<LocationResponse> locationResponses = from(locations, storeService);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(locationResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(locationResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of locations: {}", locations, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
