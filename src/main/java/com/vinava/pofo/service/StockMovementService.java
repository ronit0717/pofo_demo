package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.StockMovementRequest;
import com.vinava.pofo.dto.response.StockMovementResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StockMovementService {
    StockMovementResponse createStockMovement(StockMovementRequest StockMovementRequest, long clientId, boolean updateStockQuantity);
    StockMovementResponse updateStockMovement(StockMovementRequest StockMovementRequest, long id, long clientId);
    boolean deleteStockMovement(long id, long clientId);
    StockMovementResponse getStockMovementById(long id, long clientId);
    ResponseEntity<List<StockMovementResponse>> getAllStockMovements(long clientId, long stockId, Integer pageNumber,
                                                                     Integer pageSize, String sortBy, String order);
}
