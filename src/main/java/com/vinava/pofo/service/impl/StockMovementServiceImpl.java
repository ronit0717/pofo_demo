package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.StockMovementRepository;
import com.vinava.pofo.dto.request.StockMovementRequest;
import com.vinava.pofo.dto.request.StockRequest;
import com.vinava.pofo.dto.response.StockMovementResponse;
import com.vinava.pofo.dto.response.StockResponse;
import com.vinava.pofo.enumeration.StockMovementType;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.StockMovement;
import com.vinava.pofo.service.StockMovementService;
import com.vinava.pofo.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StockMovementServiceImpl implements StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private StockService stockService;

    @Override
    public StockMovementResponse createStockMovement(StockMovementRequest stockMovementRequest, long clientId, boolean updateStockQuantity) {
        log.debug("Starting createStockMovement with request: {}, for clientId: {}", stockMovementRequest, clientId);
        validateStock(stockMovementRequest.getStockId(), clientId);
        StockMovement stockMovement = stockMovementRequest.from(clientId);
        stockMovement = stockMovementRepository.save(stockMovement);
        if (updateStockQuantity) {
            updateStockQuantity(stockMovement);
        }
        log.debug("Returning from createStockMovement with response: {}, for clientId: {}", stockMovement, clientId);
        return StockMovementResponse.from(stockMovement);
    }

    private void validateStock(long stockId, long clientId) {
        stockService.getStockById(stockId, clientId);
    }

    private void updateStockQuantity(StockMovement stockMovement) {
        StockResponse stockResponse = stockService.getStockById(stockMovement.getStockId(), stockMovement.getClientId());
        if (stockResponse.getQuantity() == null) {
            log.debug("Stock quantity update skipped");
            return;
        }
        BigDecimal newQuantity;
        if (StockMovementType.IN.equals(stockMovement.getStockMovementType())) {
            newQuantity = stockResponse.getQuantity().add(stockMovement.getQuantity());
        } else {
            newQuantity = (stockResponse.getQuantity().compareTo(stockMovement.getQuantity()) > 0) ?
                    (stockResponse.getQuantity().subtract(stockMovement.getQuantity())) : BigDecimal.ZERO;
        }
        StockRequest request = stockResponse.from(newQuantity);
        stockResponse = stockService.updateStock(request, stockResponse.getId(), stockMovement.getClientId());
        log.debug("Stock quantity updated, stockResponse: {}", stockResponse);
    }

    @Override
    public StockMovementResponse updateStockMovement(StockMovementRequest stockMovementRequest, long id, long clientId) {
        log.debug("Updating stockMovement with request: {}, id: {}, for clientId: {}", stockMovementRequest, id, clientId);
        Optional<StockMovement> optionalStockMovement = stockMovementRepository.findByIdAndClientId(id, clientId);
        if (!optionalStockMovement.isPresent()) {
            log.error("StockMovement not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("StockMovement updation", "Invalid stockMovement ID");
        }
        StockMovement stockMovement = stockMovementRequest.from(clientId);
        stockMovement.setId(id);
        stockMovement = stockMovementRepository.save(stockMovement);
        log.debug("Updated stockMovement with id: {} and clientId: {}. Response: {}", id, clientId, stockMovement);
        return StockMovementResponse.from(stockMovement);
    }

    @Override
    public boolean deleteStockMovement(long id, long clientId) {
        log.debug("Deleting stockMovement with id: {}, for clientId: {}", id, clientId);
        Optional<StockMovement> optionalStockMovement = stockMovementRepository.findByIdAndClientId(id, clientId);
        if (!optionalStockMovement.isPresent()) {
            log.error("StockMovement not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("StockMovement deletion", "Invalid stockMovement ID");
        }
        StockMovement stockMovement = optionalStockMovement.get();
        stockMovementRepository.delete(stockMovement);
        log.debug("Deleted stockMovement with id: {} and clientId: {}", id, clientId);
        return true;
    }

    @Override
    public StockMovementResponse getStockMovementById(long id, long clientId) {
        log.debug("Fetching stockMovement with id: {}, for clientId: {}", id, clientId);
        Optional<StockMovement> optionalStockMovement = stockMovementRepository.findByIdAndClientId(id, clientId);
        if (!optionalStockMovement.isPresent()) {
            log.error("StockMovement not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Get stockMovement by id", "Invalid stockMovement ID");
        }
        StockMovement stockMovement = optionalStockMovement.get();
        log.debug("Fetched stockMovement with id: {} and clientId: {}. Response: {}", id, clientId, stockMovement);
        return StockMovementResponse.from(stockMovement);
    }

    @Override
    public ResponseEntity<List<StockMovementResponse>> getAllStockMovements(long clientId, long stockId, Integer pageNumber,
                                                                            Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllStockMovements with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                ", stockId: {}, clientId: {}", pageNumber, pageSize, sortBy, order, stockId, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<StockMovement> stockMovements;
        if (0L == stockId) {
            stockMovements = stockMovementRepository.findAllByClientId(clientId, pageable);
        } else {
            stockMovements = stockMovementRepository.findAllByStockIdAndClientId(stockId, clientId, pageable);
        }
        log.debug("Returning from getAllStockMovements for clientId: {}, stockId: {} with response: {}",
                clientId, stockId, stockMovements);
        return StockMovementResponse.getResponseEntityFrom(stockMovements);
    }

}
