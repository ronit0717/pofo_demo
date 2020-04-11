package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.CategoryRequest;
import com.vinava.pofo.dto.response.CategoryResponse;
import com.vinava.pofo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("create")
    private CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request,
                                            @RequestHeader(value = "pofo_client_id") long clientId) {
        return categoryService.create(request, clientId);
    }

    @PostMapping("update")
    private CategoryResponse updateCateoory(@Valid @RequestBody CategoryRequest request,
                                            @RequestHeader(value = "pofo_client_id") long clientId) {
        return categoryService.update(request, clientId);
    }

    @DeleteMapping("delete/{id}/{clientId}")
    private boolean deleteClient(@PathParam(value = "id") long id,
                                 @RequestHeader(value = "pofo_client_id") long clientId) {
        return categoryService.delete(id, clientId);
    }

}
