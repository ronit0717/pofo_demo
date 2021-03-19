package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.StoreOrderRequest;
import com.vinava.pofo.dto.response.StoreOrderResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StoreOrderService {
    StoreOrderResponse createStoreOrder(StoreOrderRequest StoreOrderRequest, long clientId);
    StoreOrderResponse updateStoreOrder(StoreOrderRequest StoreOrderRequest, long id, long clientId);
    boolean deleteStoreOrder(long id, long clientId);
    StoreOrderResponse getStoreOrderById(long id, long clientId);
    ResponseEntity<List<StoreOrderResponse>> getAllStoreOrders(long clientId, Integer pageNumber,
                                                               Integer pageSize, String sortBy, String order);
}
