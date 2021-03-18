package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.StoreRequest;
import com.vinava.pofo.dto.response.StoreResponse;
import com.vinava.pofo.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping()
    private StoreResponse createStore(@Valid @RequestBody StoreRequest request,
                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeService.createStore(request, clientId);
    }

    @PutMapping("{id}")
    private StoreResponse updateStore(@NotNull @PathVariable Long id,
                                      @Valid @RequestBody StoreRequest request,
                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeService.updateStore(request, id, clientId);
    }

    @DeleteMapping("{id}")
    private boolean deleteStore(@NotNull @PathVariable(value = "id") Long id,
                                  @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeService.deleteStore(id, clientId);
    }

    @GetMapping("{id}")
    private StoreResponse getStoreById(@NotNull @PathVariable(value = "id") Long id,
                                           @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeService.getStoreById(id, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<StoreResponse>> getAllStores(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                                @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                                @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                                @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                                @RequestParam(value = "location_id", defaultValue = "0") long locationId,
                                                                @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return storeService.getAllStores(clientId, locationId, pageNumber, pageSize, sortBy, order);
    }

}
