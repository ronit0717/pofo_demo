package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.UserRequest;
import com.vinava.pofo.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(long id, UserRequest userRequest);
    boolean deleteUser(long id);
    UserResponse getUserById(long id);
    ResponseEntity<List<UserResponse>> getAllUsers(Integer pageNumber,
                                                   Integer pageSize, String sortBy, String order);
}
