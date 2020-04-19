package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.ProductRequest;
import com.vinava.pofo.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest, long clientId);
    ProductResponse updateProduct(ProductRequest productRequest, long id, long clientId);
    boolean deleteProduct(long id, long clientId);
    ProductResponse getProductById(long id, long clientId);
    ResponseEntity<List<ProductResponse>> getAllProducts(long clientId, Long categoryId, Integer pageNumber,
                                                         Integer pageSize, String sortBy, String order);
}
