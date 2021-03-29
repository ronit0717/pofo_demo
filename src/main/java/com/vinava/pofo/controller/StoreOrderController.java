package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.StoreOrderRequest;
import com.vinava.pofo.dto.response.StoreOrderResponse;
import com.vinava.pofo.enumeration.OrderStatus;
import com.vinava.pofo.service.StoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/storeOrder")
public class StoreOrderController {

    @Autowired
    private StoreOrderService storeOrderService;

    @PostMapping()
    private StoreOrderResponse createStoreOrder(@Valid @RequestBody StoreOrderRequest request,
                                                @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeOrderService.createStoreOrder(request, clientId);
    }

    @PutMapping("{id}")
    private StoreOrderResponse updateStoreOrder(@NotNull @PathVariable Long id,
                                                @Valid @RequestBody StoreOrderRequest request,
                                                @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeOrderService.updateStoreOrder(request, id, clientId);
    }

    @PutMapping("status/{orderStatus}/{id}")
    private StoreOrderResponse updateStoreOrderStatus(@NotNull @PathVariable Long id,
                                                      @NotNull @PathVariable OrderStatus orderStatus,
                                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeOrderService.updateStoreOrderStatus(id, orderStatus, clientId);
    }

    @DeleteMapping("{id}")
    private boolean deleteStoreOrder(@NotNull @PathVariable(value = "id") Long id,
                                     @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeOrderService.deleteStoreOrder(id, clientId);
    }

    @GetMapping("{id}")
    private StoreOrderResponse getStoreOrderById(@NotNull @PathVariable(value = "id") Long id,
                                                 @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeOrderService.getStoreOrderById(id, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<StoreOrderResponse>> getAllStoreOrders(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                                      @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                                      @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                                      @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                                      @RequestParam(value = "storeId", defaultValue = "0") long storeId,
                                                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeOrderService.getAllStoreOrders(clientId, storeId, pageNumber, pageSize, sortBy, order);
    }

}
