package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.LocationRequest;
import com.vinava.pofo.dto.response.LocationResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LocationService {
    LocationResponse createLocation(LocationRequest LocationRequest, long clientId);
    LocationResponse updateLocation(LocationRequest LocationRequest, long id, long clientId);
    boolean deleteLocation(long id, long clientId);
    LocationResponse getLocationById(long id, long clientId);
    ResponseEntity<List<LocationResponse>> getAllLocations(long clientId, Integer pageNumber,
                                                           Integer pageSize, String sortBy, String order);
}
