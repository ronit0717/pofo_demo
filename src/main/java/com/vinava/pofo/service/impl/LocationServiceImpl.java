package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.LocationRepository;
import com.vinava.pofo.dto.request.LocationRequest;
import com.vinava.pofo.dto.response.LocationResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.Location;
import com.vinava.pofo.service.LocationService;
import com.vinava.pofo.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private StoreService storeService;

    @Override
    public LocationResponse createLocation(LocationRequest locationRequest, long clientId) {
        log.debug("Starting createLocation with request: {}, for clientId: {}", locationRequest, clientId);
        Optional<Location> optionalLocation = locationRepository.findByNameAndClientId(
                locationRequest.getName(), clientId);
        if (optionalLocation.isPresent()) {
            log.error("Location already exists with name: {} for clientId: {}",
                    locationRequest.getName(), clientId);
            throw new ProcessException("Location creation", "Location already exists with same name");
        }
        Location location = locationRequest.from(clientId);
        location = locationRepository.save(location);
        log.debug("Returning from createLocation with response: {}, for clientId: {}", location, clientId);
        return LocationResponse.from(location, storeService);
    }

    @Override
    public LocationResponse updateLocation(LocationRequest locationRequest, long id, long clientId) {
        log.debug("Updating location with request: {}, id: {}, for clientId: {}", locationRequest, id, clientId);
        Optional<Location> optionalLocation = locationRepository.findByIdAndClientId(id, clientId);
        if (!optionalLocation.isPresent()) {
            log.error("Location not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Location updation", "Invalid location ID");
        }
        Location location = locationRequest.from(clientId);
        location.setId(id);
        location = locationRepository.save(location);
        log.debug("Updated location with id: {} and clientId: {}. Response: {}", id, clientId, location);
        return LocationResponse.from(location, storeService);
    }

    @Override
    public boolean deleteLocation(long id, long clientId) {
        log.debug("Deleting location with id: {}, for clientId: {}", id, clientId);
        Optional<Location> optionalLocation = locationRepository.findByIdAndClientId(id, clientId);
        if (!optionalLocation.isPresent()) {
            log.error("Location not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Location deletion", "Invalid location ID");
        }
        Location location = optionalLocation.get();
        locationRepository.delete(location);
        log.debug("Deleted location with id: {} and clientId: {}", id, clientId);
        return true;
    }

    @Override
    public LocationResponse getLocationById(long id, long clientId) {
        log.debug("Fetching location with id: {}, for clientId: {}", id, clientId);
        Optional<Location> optionalLocation = locationRepository.findByIdAndClientId(id, clientId);
        if (!optionalLocation.isPresent()) {
            log.error("Location not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Get location by id", "Invalid location ID");
        }
        Location location = optionalLocation.get();
        log.debug("Fetched location with id: {} and clientId: {}. Response: {}", id, clientId, location);
        return LocationResponse.from(location, storeService);
    }

    @Override
    public ResponseEntity<List<LocationResponse>> getAllLocations(long clientId, Integer pageNumber,
                                                                  Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllLocations with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                        ", clientId: {}", pageNumber, pageSize, sortBy, order, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Location> locations = locationRepository.findAllByClientId(clientId, pageable);
        log.debug("Returning from getAllLocations for clientId: {} with response: {}",
                clientId, locations);
        return LocationResponse.getResponseEntityFrom(locations, storeService);
    }

}
