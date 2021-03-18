package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.UserRequest;
import com.vinava.pofo.dto.response.UserResponse;
import com.vinava.pofo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    private UserResponse createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("{id}")
    private UserResponse updateUser(@NotNull @PathVariable Long id,
                                    @Valid @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("{id}")
    private boolean deleteUser(@NotNull @PathVariable(value = "id") Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("{id}")
    private UserResponse getUserById(@NotNull @PathVariable(value = "id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                          @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                          @RequestParam(value = "_order", defaultValue = "DESC") String order) {
        return userService.getAllUsers(pageNumber, pageSize, sortBy, order);
    }

}

