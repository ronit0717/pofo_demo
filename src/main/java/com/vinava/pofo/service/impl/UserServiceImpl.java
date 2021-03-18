package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.UserRepository;
import com.vinava.pofo.dto.request.UserRequest;
import com.vinava.pofo.dto.response.UserResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.User;
import com.vinava.pofo.service.UserService;
import com.vinava.pofo.service.helper.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.debug("Creating user for userRequest: {}", userRequest);
        User user = userRequest.from(validationService);
        userRepository.save(user);
        log.debug("New user created: {}", user);
        return UserResponse.from(user);
    }

    @Override
    public UserResponse updateUser(long id, UserRequest userRequest) {
        log.debug("Updating user with request: {}, id: {}", userRequest, id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            log.error("User not present with id: {}", id);
            throw new ProcessException("User updation", "Invalid user ID");
        }
        User user = userRequest.from(validationService);
        user.setId(id);
        user = userRepository.save(user);
        log.debug("Updated user with id: {}. Response: {}", id, user);
        return UserResponse.from(user);
    }

    @Override
    public boolean deleteUser(long id) {
        log.debug("Deleting user with id: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            log.error("User not present with id: {}", id);
            throw new ProcessException("User deletion", "Invalid user ID");
        }
        User user = optionalUser.get();
        userRepository.delete(user);
        log.debug("Deleted user with id: {}", id);
        return true;
    }

    @Override
    public UserResponse getUserById(long id) {
        log.debug("Fetching user with id: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            log.error("User not present with id: {}", id);
            throw new ProcessException("Get User by id", "Invalid user ID");
        }
        User user = optionalUser.get();
        log.debug("Fetched user with id: {}. Response: {}", id, user);
        return UserResponse.from(user);
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers(Integer pageNumber,
                                                          Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllUsers with pageNumber: {}, pageSize: {}, sortBy: {}, order: {}",
                    pageNumber, pageSize, sortBy, order);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        Page<User> users = userRepository.findAll(pageable);
        log.debug("Returning from getAllUsers, with response: {}", users);
        return UserResponse.getResponseEntityFrom(users);
    }
}
