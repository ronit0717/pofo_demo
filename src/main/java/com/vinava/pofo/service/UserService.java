package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.UserRequest;
import com.vinava.pofo.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest, long clientId);
    UserResponse updateUser(long id, long clientId, UserRequest userRequest);
    boolean deleteUser(long id, long clientId);
    UserResponse getUserById(long id, long clientId);
    UserResponse getUserByUserName(String userName, long clientId);
    ResponseEntity<List<UserResponse>> getAllUsers(long clientId, Integer pageNumber,
                                                   Integer pageSize, String sortBy, String order);
}
