package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.CategoryRequest;
import com.vinava.pofo.dto.response.CategoryResponse;
import com.vinava.pofo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    private CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request,
                                            @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return categoryService.create(request, clientId);
    }

    @PutMapping("{id}")
    private CategoryResponse updateCategory(@NotNull @PathVariable Long id,
                                            @Valid @RequestBody CategoryRequest request,
                                            @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return categoryService.update(id, request, clientId);
    }

    @DeleteMapping("{id}")
    private boolean deleteCategory(@NotNull @PathVariable(value = "id") Long id,
                                 @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return categoryService.delete(id, clientId);
    }

    @GetMapping("{id}")
    private CategoryResponse getCategoryById(@NotNull @PathVariable(value = "id") Long id,
                                             @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return categoryService.getById(id, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                                @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                                @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                                @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                                @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return categoryService.getAllCategories(clientId, pageNumber, pageSize, sortBy, order);
    }

}
