package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.CategoryRequest;
import com.vinava.pofo.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse create(CategoryRequest categoryRequest, long clientId);
    CategoryResponse update(CategoryRequest categoryRequest, long clientId);
    boolean delete(long id, long clientId);
}
