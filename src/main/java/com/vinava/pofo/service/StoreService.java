package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.StoreRequest;
import com.vinava.pofo.dto.response.StoreResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StoreService {
    StoreResponse createStore(StoreRequest StoreRequest, long clientId);
    StoreResponse updateStore(StoreRequest StoreRequest, long id, long clientId);
    boolean deleteStore(long id, long clientId);
    StoreResponse getStoreById(long id, long clientId);
    ResponseEntity<List<StoreResponse>> getAllStores(long clientId, Long locationId, Integer pageNumber,
                                                     Integer pageSize, String sortBy, String order);
}
