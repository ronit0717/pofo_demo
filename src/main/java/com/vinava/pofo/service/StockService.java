package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.StockRequest;
import com.vinava.pofo.dto.response.StockResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StockService {
    StockResponse createStock(StockRequest StockRequest, long clientId);
    StockResponse updateStock(StockRequest StockRequest, long id, long clientId);
    boolean deleteStock(long id, long clientId);
    StockResponse getStockById(long id, long clientId);
    ResponseEntity<List<StockResponse>> getAllStocks(long clientId, Integer pageNumber,
                                                     Integer pageSize, String sortBy, String order);
}
