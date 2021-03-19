package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.BrandRepository;
import com.vinava.pofo.dto.request.BrandRequest;
import com.vinava.pofo.dto.response.BrandResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.Brand;
import com.vinava.pofo.service.BrandService;
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
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public BrandResponse createBrand(BrandRequest brandRequest, long clientId) {
        log.debug("Starting createBrand with request: {}, for clientId: {}", brandRequest, clientId);
        Optional<Brand> optionalBrand = brandRepository.findByNameAndClientId(
                brandRequest.getName(), clientId);
        if (optionalBrand.isPresent()) {
            log.error("Brand already exists with name: {} for clientId: {}",
                    brandRequest.getName(), clientId);
            throw new ProcessException("Brand creation", "Brand already exists with same name");
        }
        Brand brand = brandRequest.from(clientId);
        brand = brandRepository.save(brand);
        log.debug("Returning from createBrand with response: {}, for clientId: {}", brand, clientId);
        return BrandResponse.from(brand);
    }

    @Override
    public BrandResponse updateBrand(BrandRequest brandRequest, long id, long clientId) {
        log.debug("Updating brand with request: {}, id: {}, for clientId: {}", brandRequest, id, clientId);
        Optional<Brand> optionalBrand = brandRepository.findByIdAndClientId(id, clientId);
        if (!optionalBrand.isPresent()) {
            log.error("Brand not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Brand updation", "Invalid brand ID");
        }
        Brand brand = brandRequest.from(clientId);
        brand.setId(id);
        brand = brandRepository.save(brand);
        log.debug("Updated brand with id: {} and clientId: {}. Response: {}", id, clientId, brand);
        return BrandResponse.from(brand);
    }

    @Override
    public boolean deleteBrand(long id, long clientId) {
        log.debug("Deleting brand with id: {}, for clientId: {}", id, clientId);
        Optional<Brand> optionalBrand = brandRepository.findByIdAndClientId(id, clientId);
        if (!optionalBrand.isPresent()) {
            log.error("Brand not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Brand deletion", "Invalid brand ID");
        }
        Brand brand = optionalBrand.get();
        brandRepository.delete(brand);
        log.debug("Deleted brand with id: {} and clientId: {}", id, clientId);
        return true;
    }

    @Override
    public BrandResponse getBrandById(long id, long clientId) {
        log.debug("Fetching brand with id: {}, for clientId: {}", id, clientId);
        Optional<Brand> optionalBrand = brandRepository.findByIdAndClientId(id, clientId);
        if (!optionalBrand.isPresent()) {
            log.error("Brand not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Get brand by id", "Invalid brand ID");
        }
        Brand brand = optionalBrand.get();
        log.debug("Fetched brand with id: {} and clientId: {}. Response: {}", id, clientId, brand);
        return BrandResponse.from(brand);
    }

    @Override
    public ResponseEntity<List<BrandResponse>> getAllBrands(long clientId, Integer pageNumber,
                                                            Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllBrands with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                ", clientId: {}", pageNumber, pageSize, sortBy, order, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Brand> brands = brandRepository.findAllByClientId(clientId, pageable);
        log.debug("Returning from getAllBrands for clientId: {} with response: {}",
                clientId, brands);
        return BrandResponse.getResponseEntityFrom(brands);
    }

}
