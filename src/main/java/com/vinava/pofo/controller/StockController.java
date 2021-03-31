package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.StockRequest;
import com.vinava.pofo.dto.response.StockResponse;
import com.vinava.pofo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping()
    private StockResponse createStock(@Valid @RequestBody StockRequest request,
                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockService.createStock(request, clientId);
    }

    @PutMapping("{id}")
    private StockResponse updateStock(@NotNull @PathVariable Long id,
                                      @Valid @RequestBody StockRequest request,
                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockService.updateStock(request, id, clientId);
    }

    @DeleteMapping("{id}")
    private boolean deleteStock(@NotNull @PathVariable(value = "id") Long id,
                                @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockService.deleteStock(id, clientId);
    }

    @GetMapping("{id}")
    private StockResponse getStockById(@NotNull @PathVariable(value = "id") Long id,
                                       @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockService.getStockById(id, clientId);
    }

    @GetMapping("store")
    private StockResponse getStockById(@RequestParam(value = "store_id", defaultValue = "0") long storeId,
                                       @RequestParam(value = "product_id", defaultValue = "0") long productId,
                                       @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockService.getStockByStoreIdAndProductId(storeId, productId, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<StockResponse>> getAllStocks(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                            @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                            @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                            @RequestParam(value = "store_id", defaultValue = "0") long storeId,
                                                            @RequestParam(value = "for_sale", defaultValue = "true") boolean forSale,
                                                            @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockService.getAllStocks(clientId, pageNumber, storeId, forSale, pageSize, sortBy, order);
    }

}
