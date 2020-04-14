package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.CategoryRequest;
import com.vinava.pofo.dto.response.CategoryResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest categoryRequest, long clientId);
    CategoryResponse update(long categoryId, CategoryRequest categoryRequest, long clientId);
    boolean delete(long id, long clientId);
    CategoryResponse getById(long categoryId, long clientId);
    ResponseEntity<List<CategoryResponse>> getAllCategories(Long clientId, Integer pageNumber,
                                                            Integer pageSize, String sortBy, String order);
}
