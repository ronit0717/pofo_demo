package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.ProductRequest;
import com.vinava.pofo.dto.response.ProductResponse;
import com.vinava.pofo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping()
    private ProductResponse createProduct(@Valid @RequestBody ProductRequest request,
                                           @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return productService.createProduct(request, clientId);
    }

    @PutMapping("{id}")
    private ProductResponse updateProduct(@NotNull @PathVariable Long id,
                                            @Valid @RequestBody ProductRequest request,
                                            @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return productService.updateProduct(request, id, clientId);
    }

    @DeleteMapping("{id}")
    private boolean deleteProduct(@NotNull @PathVariable(value = "id") Long id,
                                   @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return productService.deleteProduct(id, clientId);
    }

    @GetMapping("{id}")
    private ProductResponse getProductById(@NotNull @PathVariable(value = "id") Long id,
                                             @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return productService.getProductById(id, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                                @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                                @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                                @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                                @RequestParam(value = "category_id", defaultValue = "0") long categoryId,
                                                                @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return productService.getAllProducts(clientId, categoryId, pageNumber, pageSize, sortBy, order);
    }

}
