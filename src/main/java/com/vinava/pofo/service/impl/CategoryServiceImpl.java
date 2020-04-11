package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.CategoryRepository;
import com.vinava.pofo.dto.request.CategoryRequest;
import com.vinava.pofo.dto.response.CategoryResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.Category;
import com.vinava.pofo.service.CategoryService;
import com.vinava.pofo.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public CategoryResponse create(CategoryRequest categoryRequest, long clientId) {
        log.debug("Creating category for request: {}", categoryRequest);
        validateClient(clientId);
        Category category = categoryRequest.from(clientId);
        checkValidParentCategoryId(category.getParentCategoryId(), category.getClientId());
        category = categoryRepository.save(category);
        log.debug("Returning from create category with response: {}", category);
        return CategoryResponse.from(category);
    }

    @Override
    public CategoryResponse update(CategoryRequest categoryRequest, long clientId) {
        log.debug("Updating category with request: {}", categoryRequest);
        validateClient(clientId);
        Optional<Category> optionalCategory = categoryRepository.
                findByIdAndClientId(categoryRequest.getId(), clientId);
        if (!optionalCategory.isPresent()) {
            log.error("Category not found for update with id: {}", categoryRequest.getId());
            throw new ProcessException("Update category", "Invalid category id");
        }
        checkValidParentCategoryId(categoryRequest.getParentCategoryId(), clientId);
        Category category = categoryRequest.from(clientId);
        category = categoryRepository.save(category);
        log.debug("Category updated to : {}", category);
        return CategoryResponse.from(category);
    }

    @Override
    public boolean delete(long id, long clientId) {
        log.debug("Deleting category wit id : {} for clientId: {}", id, clientId);
        Optional<Category> optionalCategory = categoryRepository.findByIdAndClientId(id, clientId);
        if (!optionalCategory.isPresent()) {
            log.error("Category ID: {} not found for clientId: {}", id, clientId);
            throw new ProcessException("Delete category", "Invalid ID for the client ID passed in request");
        }
        categoryRepository.delete(optionalCategory.get());
        return true;
    }

    //Validations

    private void checkValidParentCategoryId(Long parentCategoryId, long clientId) {
        if (parentCategoryId == null) {
            return;
        }
        Optional<Category> optionalCategory = categoryRepository.findByIdAndClientId(parentCategoryId, clientId);
        if (!optionalCategory.isPresent()) {
            log.error("Parent category not found with id: {} and clientId: {}", parentCategoryId, clientId);
            throw new ProcessException("Create category", "Invalid parent category id");
        }
    }

    private void validateClient(long clientId) {
        if (!clientService.isClientActive(clientId)) {
            log.error("Client is not active for the id: {}", clientId);
            throw new ProcessException("Update client", "Inactive or invalid client id");
        }
    }


}
