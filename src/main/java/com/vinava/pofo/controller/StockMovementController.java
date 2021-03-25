package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.StockMovementRequest;
import com.vinava.pofo.dto.response.StockMovementResponse;
import com.vinava.pofo.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/stockMovement")
public class StockMovementController {

    @Autowired
    private StockMovementService stockMovementService;

    @PostMapping()
    private StockMovementResponse createStockMovement(@Valid @RequestBody StockMovementRequest request,
                                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockMovementService.createStockMovement(request, clientId, true);
    }

    @PutMapping("{id}")
    private StockMovementResponse updateStockMovement(@NotNull @PathVariable Long id,
                                                      @Valid @RequestBody StockMovementRequest request,
                                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockMovementService.updateStockMovement(request, id, clientId);
    }

    @DeleteMapping("{id}")
    private boolean deleteStockMovement(@NotNull @PathVariable(value = "id") Long id,
                                        @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockMovementService.deleteStockMovement(id, clientId);
    }

    @GetMapping("{id}")
    private StockMovementResponse getStockMovementById(@NotNull @PathVariable(value = "id") Long id,
                                                       @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockMovementService.getStockMovementById(id, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<StockMovementResponse>> getAllBrands(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                                    @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                                    @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                                    @RequestParam(value = "stockId", defaultValue = "0") long stockId,
                                                                    @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return stockMovementService.getAllStockMovements(clientId, stockId, pageNumber, pageSize, sortBy, order);
    }

}
