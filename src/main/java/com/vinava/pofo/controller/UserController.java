package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.UserRequest;
import com.vinava.pofo.dto.response.UserResponse;
import com.vinava.pofo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    private UserResponse createUser(@Valid @RequestBody UserRequest request,
                                    @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return userService.createUser(request, clientId);
    }

    @PutMapping("{id}")
    private UserResponse updateUser(@NotNull @PathVariable Long id,
                                    @Valid @RequestBody UserRequest request,
                                    @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return userService.updateUser(id, clientId, request);
    }

    @DeleteMapping("{id}")
    private boolean deleteUser(@NotNull @PathVariable(value = "id") Long id,
                               @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return userService.deleteUser(id, clientId);
    }

    @GetMapping("{id}")
    private UserResponse getUserById(@NotNull @PathVariable(value = "id") Long id,
                                     @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return userService.getUserById(id, clientId);
    }

    @GetMapping("username/{username}")
    private UserResponse getClientByUserName(@NotBlank @PathVariable(value = "username") String userName,
                                             @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return userService.getUserByUserName(userName, clientId);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                          @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                          @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                          @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return userService.getAllUsers(clientId, pageNumber, pageSize, sortBy, order);
    }

}

