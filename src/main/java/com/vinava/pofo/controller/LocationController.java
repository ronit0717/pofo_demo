package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.LocationRequest;
import com.vinava.pofo.dto.response.LocationResponse;
import com.vinava.pofo.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping()
    private LocationResponse createLocation(@Valid @RequestBody LocationRequest request,
                                            @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return locationService.createLocation(request, clientId);
    }

    @PutMapping("{id}")
    private LocationResponse updateLocation(@NotNull @PathVariable Long id,
                                            @Valid @RequestBody LocationRequest request,
                                            @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return locationService.updateLocation(request, id, clientId);
    }

    @DeleteMapping("{id}")
    private boolean deleteLocation(@NotNull @PathVariable(value = "id") Long id,
                                   @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return locationService.deleteLocation(id, clientId);
    }

    @GetMapping("{id}")
    private LocationResponse getLocationById(@NotNull @PathVariable(value = "id") Long id,
                                            @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return locationService.getLocationById(id, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<LocationResponse>> getAllBrands(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                                 @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                                 @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                                 @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                                 @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return locationService.getAllLocations(clientId, pageNumber, pageSize, sortBy, order);
    }

}
