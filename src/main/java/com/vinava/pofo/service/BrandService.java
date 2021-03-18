package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.BrandRequest;
import com.vinava.pofo.dto.response.BrandResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrandService {
    BrandResponse createBrand(BrandRequest BrandRequest, long clientId);
    BrandResponse updateBrand(BrandRequest BrandRequest, long id, long clientId);
    boolean deleteBrand(long id, long clientId);
    BrandResponse getBrandById(long id, long clientId);
    ResponseEntity<List<BrandResponse>> getAllBrands(long clientId, Integer pageNumber,
                                                     Integer pageSize, String sortBy, String order);
}
