package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.StockRepository;
import com.vinava.pofo.dto.request.StockMovementRequest;
import com.vinava.pofo.dto.request.StockRequest;
import com.vinava.pofo.dto.response.ProductResponse;
import com.vinava.pofo.dto.response.StockMovementResponse;
import com.vinava.pofo.dto.response.StockResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.Stock;
import com.vinava.pofo.service.ProductService;
import com.vinava.pofo.service.StockMovementService;
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

    @Autowired
    private ProductService productService;

    @Autowired
    private StockMovementService stockMovementService;

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
        validateProduct(stockRequest.getProductId(), clientId);
        Stock stock = stockRequest.from(clientId);
        stock = stockRepository.save(stock);
        if (stock.getQuantity() != null) {
            StockMovementRequest stockMovementRequest = stock.createOpeningStockMovement();
            log.debug("Creating stock movement record request: {}", stockMovementRequest);
            StockMovementResponse response = stockMovementService.createStockMovement(stockMovementRequest, clientId, false);
            log.debug("Stock movement created, response: {}", response);
        }
        log.debug("Returning from createStock with response: {}, for clientId: {}", stock, clientId);
        return StockResponse.from(stock, productService);
    }

    @Override
    public StockResponse updateStock(StockRequest stockRequest, long id, long clientId) {
        log.debug("Updating stock with request: {}, id: {}, for clientId: {}", stockRequest, id, clientId);
        Optional<Stock> optionalStock = stockRepository.findByIdAndClientId(id, clientId);
        if (!optionalStock.isPresent()) {
            log.error("Stock not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Stock updation", "Invalid stock ID");
        }
        validateProduct(stockRequest.getProductId(), clientId);
        Stock stock = stockRequest.from(clientId);
        stock.setId(id);
        stock = stockRepository.save(stock);
        log.debug("Updated stock with id: {} and clientId: {}. Response: {}", id, clientId, stock);
        return StockResponse.from(stock, productService);
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
        return StockResponse.from(stock, productService);
    }

    @Override
    public StockResponse getStockByStoreIdAndProductId(long storeId, long productId, long clientId) {
        log.debug("Fetching stock with storeId: {}, productId: {} for clientId: {}", storeId, productId, clientId);
        Optional<Stock> optionalStock = stockRepository.findByProductIdAndStoreIdAndClientId(productId, storeId, clientId);
        if (!optionalStock.isPresent()) {
            log.error("Stock not present with storeId: {}, productId: {} and clientId: {}", storeId, productId, clientId);
            return null;
        }
        Stock stock = optionalStock.get();
        log.debug("Fetched stock with storeId: {}, productId: {} and clientId: {}. Response: {}", storeId, productId, clientId, stock);
        return StockResponse.from(stock, productService);
    }

    @Override
    public ResponseEntity<List<StockResponse>> getAllStocks(long clientId, Integer pageNumber, long storeId,
                                                            boolean forSale, Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllStocks with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                "storeId: {}, clientId: {}, forSale: {}", pageNumber, pageSize, sortBy, order, storeId, clientId, forSale);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Stock> stocks = forSale ?
                stockRepository.findAllByStoreIdAndClientIdAndForSale(storeId, clientId, true, pageable) :
                stockRepository.findAllByStoreIdAndClientId(storeId, clientId, pageable);
        log.debug("Returning from getAllStocks for storeId: {}, clientId: {}, forSale: {} with response: {}",
                storeId, clientId, forSale, stocks);
        return StockResponse.getResponseEntityFrom(stocks, productService);
    }

    private void validateProduct(long productId, long clientId) {
        ProductResponse productResponse = productService.getProductById(productId, clientId);
    }

}
