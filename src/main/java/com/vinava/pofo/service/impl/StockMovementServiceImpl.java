package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.StockMovementRepository;
import com.vinava.pofo.dto.request.StockMovementRequest;
import com.vinava.pofo.dto.response.StockMovementResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.StockMovement;
import com.vinava.pofo.service.StockMovementService;
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
public class StockMovementServiceImpl implements StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Override
    public StockMovementResponse createStockMovement(StockMovementRequest stockMovementRequest, long clientId) {
        log.debug("Starting createStockMovement with request: {}, for clientId: {}", stockMovementRequest, clientId);
        StockMovement stockMovement = stockMovementRequest.from(clientId);
        stockMovement = stockMovementRepository.save(stockMovement);
        log.debug("Returning from createStockMovement with response: {}, for clientId: {}", stockMovement, clientId);
        return StockMovementResponse.from(stockMovement);
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
    public ResponseEntity<List<StockMovementResponse>> getAllStockMovements(long clientId, Integer pageNumber,
                                                                            Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllStockMovements with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                ", clientId: {}", pageNumber, pageSize, sortBy, order, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<StockMovement> stockMovements = stockMovementRepository.findAllByClientId(clientId, pageable);
        log.debug("Returning from getAllStockMovements for clientId: {} with response: {}",
                clientId, stockMovements);
        return StockMovementResponse.getResponseEntityFrom(stockMovements);
    }

}
