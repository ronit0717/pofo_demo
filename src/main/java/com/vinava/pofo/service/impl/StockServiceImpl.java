package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.StockRepository;
import com.vinava.pofo.dto.request.StockRequest;
import com.vinava.pofo.dto.response.StockResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.Stock;
import com.vinava.pofo.service.StockService;
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
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public StockResponse createStock(StockRequest stockRequest, long clientId) {
        log.debug("Starting createStock with request: {}, for clientId: {}", stockRequest, clientId);
        Optional<Stock> optionalStock = stockRepository.findByProductIdAndStoreIdAndClientId(
                stockRequest.getProductId(), stockRequest.getStoreId(), clientId);
        if (optionalStock.isPresent()) {
            log.error("Stock already exists with productId: {} storeId: {} for clientId: {}",
                    stockRequest.getProductId(), stockRequest.getStoreId(), clientId);
            throw new ProcessException("Stock creation", "Stock already exists with same productId-StoreId");
        }
        Stock stock = stockRequest.from(clientId);
        stock = stockRepository.save(stock);
        log.debug("Returning from createStock with response: {}, for clientId: {}", stock, clientId);
        return StockResponse.from(stock);
    }

    @Override
    public StockResponse updateStock(StockRequest stockRequest, long id, long clientId) {
        log.debug("Updating stock with request: {}, id: {}, for clientId: {}", stockRequest, id, clientId);
        Optional<Stock> optionalStock = stockRepository.findByIdAndClientId(id, clientId);
        if (!optionalStock.isPresent()) {
            log.error("Stock not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Stock updation", "Invalid stock ID");
        }
        Stock stock = stockRequest.from(clientId);
        stock.setId(id);
        stock = stockRepository.save(stock);
        log.debug("Updated stock with id: {} and clientId: {}. Response: {}", id, clientId, stock);
        return StockResponse.from(stock);
    }

    @Override
    public boolean deleteStock(long id, long clientId) {
        log.debug("Deleting stock with id: {}, for clientId: {}", id, clientId);
        Optional<Stock> optionalStock = stockRepository.findByIdAndClientId(id, clientId);
        if (!optionalStock.isPresent()) {
            log.error("Stock not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Stock deletion", "Invalid stock ID");
        }
        Stock stock = optionalStock.get();
        stockRepository.delete(stock);
        log.debug("Deleted stock with id: {} and clientId: {}", id, clientId);
        return true;
    }

    @Override
    public StockResponse getStockById(long id, long clientId) {
        log.debug("Fetching stock with id: {}, for clientId: {}", id, clientId);
        Optional<Stock> optionalStock = stockRepository.findByIdAndClientId(id, clientId);
        if (!optionalStock.isPresent()) {
            log.error("Stock not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Get stock by id", "Invalid stock ID");
        }
        Stock stock = optionalStock.get();
        log.debug("Fetched stock with id: {} and clientId: {}. Response: {}", id, clientId, stock);
        return StockResponse.from(stock);
    }

    @Override
    public ResponseEntity<List<StockResponse>> getAllStocks(long clientId, Integer pageNumber,
                                                            Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllStocks with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                ", clientId: {}", pageNumber, pageSize, sortBy, order, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Stock> stocks = stockRepository.findAllByClientId(clientId, pageable);
        log.debug("Returning from getAllStocks for clientId: {} with response: {}",
                clientId, stocks);
        return StockResponse.getResponseEntityFrom(stocks);
    }

}
