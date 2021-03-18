package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.StoreRepository;
import com.vinava.pofo.dto.request.StoreRequest;
import com.vinava.pofo.dto.response.LocationResponse;
import com.vinava.pofo.dto.response.StoreResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.Store;
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
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private LocationService locationService;

    @Override
    public StoreResponse createStore(StoreRequest storeRequest, long clientId) {
        log.debug("Starting createStore with request: {}, for clientId: {}", storeRequest, clientId);
        Optional<Store> optionalStore = storeRepository.findByNameAndClientIdAndLocationId(
                storeRequest.getName(), clientId, storeRequest.getLocationId());
        if (optionalStore.isPresent()) {
            log.error("Store already exists with name: {}, and locationId: {} for clientId: {}",
                    storeRequest.getName(), storeRequest.getLocationId(), clientId);
            throw new ProcessException("Store creation", "Store already exists in this location with same name");
        }
        LocationResponse locationResponse = locationService.getLocationById(storeRequest.getLocationId(), clientId);
        if (locationResponse == null) {
            log.error("Location not found with id: {}, clientId: {}", storeRequest.getLocationId(), clientId);
            throw new ProcessException("Create Store", "Invalid location ID");
        }
        Store store = storeRequest.from(clientId);
        store = storeRepository.save(store);
        log.debug("Returning from createStore with response: {}, for clientId: {}", store, clientId);
        return StoreResponse.from(store);
    }

    @Override
    public StoreResponse updateStore(StoreRequest storeRequest, long id, long clientId) {
        log.debug("Updating store with request: {}, id: {}, for clientId: {}", storeRequest, id, clientId);
        Optional<Store> optionalStore = storeRepository.findByIdAndClientId(id, clientId);
        if (!optionalStore.isPresent()) {
            log.error("Store not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Store updation", "Invalid store ID");
        }
        Store store = storeRequest.from(clientId);
        store.setId(id);
        store = storeRepository.save(store);
        log.debug("Updated store with id: {} and clientId: {}. Response: {}", id, clientId, store);
        return StoreResponse.from(store);
    }

    @Override
    public boolean deleteStore(long id, long clientId) {
        log.debug("Deleting store with id: {}, for clientId: {}", id, clientId);
        Optional<Store> optionalStore = storeRepository.findByIdAndClientId(id, clientId);
        if (!optionalStore.isPresent()) {
            log.error("Store not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Store deletion", "Invalid store ID");
        }
        Store store = optionalStore.get();
        storeRepository.delete(store);
        log.debug("Deleted store with id: {} and clientId: {}", id, clientId);
        return true;
    }

    @Override
    public StoreResponse getStoreById(long id, long clientId) {
        log.debug("Fetching store with id: {}, for clientId: {}", id, clientId);
        Optional<Store> optionalStore = storeRepository.findByIdAndClientId(id, clientId);
        if (!optionalStore.isPresent()) {
            log.error("Store not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Get store by id", "Invalid store ID");
        }
        Store store = optionalStore.get();
        log.debug("Fetched store with id: {} and clientId: {}. Response: {}", id, clientId, store);
        return StoreResponse.from(store);
    }

    @Override
    public ResponseEntity<List<StoreResponse>> getAllStores(long clientId, Long locationId, Integer pageNumber,
                                                                Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllStores with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                        ", clientId: {} and locationId: {}",
                pageNumber, pageSize, sortBy, order, clientId, locationId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Store> stores = (locationId == null || locationId == 0L) ?
                storeRepository.findAllByClientId(clientId, pageable)
                : storeRepository.findAllByClientIdAndLocationId(clientId, locationId, pageable);
        log.debug("Returning from getAllStores for clientId: {}, locationid: {} with response: {}",
                clientId, locationId, stores);
        return StoreResponse.getResponseEntityFrom(stores);
    }

}
