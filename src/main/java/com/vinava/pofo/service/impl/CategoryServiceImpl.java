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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
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
        HashSet<Long> categoryChainSet = new HashSet<>();
        int level = validateAndGetCategoryLevel(categoryChainSet, category.getParentCategoryId(), category.getClientId());
        category.setLevel(level);
        category = categoryRepository.save(category);
        log.debug("Returning from create category with response: {}", category);
        return CategoryResponse.from(category);
    }

    @Override
    public CategoryResponse update(long categoryId, CategoryRequest categoryRequest, long clientId) {
        log.debug("Updating category with request: {} and categoryId: {}", categoryRequest, categoryId);
        validateClient(clientId);
        Optional<Category> optionalCategory = categoryRepository.
                findByIdAndClientId(categoryId, clientId);
        if (!optionalCategory.isPresent()) {
            log.error("Category not found for update with id: {}", categoryId);
            throw new ProcessException("Update category", "Invalid category id");
        }
        HashSet<Long> categoryChainSet = new HashSet<>();
        categoryChainSet.add(categoryId);
        int level = validateAndGetCategoryLevel(categoryChainSet, categoryRequest.getParentCategoryId(), clientId);
        Category category = categoryRequest.from(clientId);
        category.setLevel(level);
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
        log.debug("Checking for category which has dependency on category with id: {}", id);
        List<Category> childCategories = categoryRepository.findAllByParentCategoryIdAndClientId(id, clientId);
        if (childCategories != null && childCategories.size() > 0) {
            log.error("Child categories found for category with id: {} and clientId: {}", id, clientId);
            throw new ProcessException("Category deletion",
                    "Child categories found. Delete child categories before deleting this category");
        }
        categoryRepository.delete(optionalCategory.get());
        return true;
    }

    @Override
    public CategoryResponse getById(long categoryId, long clientId) {
        log.debug("Fetching category details for categoryId: {} and clientId: {}", categoryId, clientId);
        Optional<Category> optionalCategory = categoryRepository.findByIdAndClientId(categoryId, clientId);
        if (!optionalCategory.isPresent()) {
            log.debug("Category not found with id: {} and clientId: {}", categoryId, clientId);
            return null;
        }
        return CategoryResponse.from(optionalCategory.get());
    }

    @Override
    public ResponseEntity<List<CategoryResponse>> getAllCategories(Long clientId, Integer pageNumber, Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllCategories with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} and clientId: {}",
                pageNumber, pageSize, sortBy, order, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Category> categories = categoryRepository.findAllByClientId(clientId, pageable);
        log.debug("Returning from getAllCategories for clientId: {} with response: {}", clientId, categories);
        return CategoryResponse.getResponseEntityFrom(categories);
    }

    //Validations
    private int validateAndGetCategoryLevel(HashSet<Long> categoryChainSet,
                                            Long parentCategoryId, long clientId) {
        if (categoryChainSet.contains(parentCategoryId)) {
            log.error("Circular category detected");
            throw new ProcessException("Category operation",
                    "Circular parent catrgory detected for categoryId: " + parentCategoryId);
        }
        if (parentCategoryId == null) {
            return 0;
        }
        Optional<Category> optionalCategory = categoryRepository.findByIdAndClientId(parentCategoryId, clientId);
        if (!optionalCategory.isPresent()) {
            log.error("Parent category not found with id: {} and clientId: {}", parentCategoryId, clientId);
            throw new ProcessException("Category operation", "Invalid parent category id");
        }
        Category category = optionalCategory.get();
        categoryChainSet.add(category.getId());
        validateAndGetCategoryLevel(categoryChainSet, category.getParentCategoryId(), clientId);
        return  (category.getLevel() + 1);
    }

    private void validateClient(long clientId) {
        if (!clientService.isClientActive(clientId)) {
            log.error("Client is not active for the id: {}", clientId);
            throw new ProcessException("Client Validation", "Inactive or invalid client id");
        }
    }


}
