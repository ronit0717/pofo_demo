package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.BrandRequest;
import com.vinava.pofo.dto.response.BrandResponse;
import com.vinava.pofo.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping()
    private BrandResponse createBrand(@Valid @RequestBody BrandRequest request,
                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return brandService.createBrand(request, clientId);
    }

    @PutMapping("{id}")
    private BrandResponse updateBrand(@NotNull @PathVariable Long id,
                                      @Valid @RequestBody BrandRequest request,
                                      @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return brandService.updateBrand(request, id, clientId);
    }

    @DeleteMapping("{id}")
    private boolean deleteBrand(@NotNull @PathVariable(value = "id") Long id,
                                @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return brandService.deleteBrand(id, clientId);
    }

    @GetMapping("{id}")
    private BrandResponse getBrandById(@NotNull @PathVariable(value = "id") Long id,
                                       @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return brandService.getBrandById(id, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<BrandResponse>> getAllBrands(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                            @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                            @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                            @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                            @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return brandService.getAllBrands(clientId, pageNumber, pageSize, sortBy, order);
    }

}
