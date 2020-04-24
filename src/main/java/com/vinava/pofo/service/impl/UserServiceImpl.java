package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.UserRepository;
import com.vinava.pofo.dto.request.UserRequest;
import com.vinava.pofo.dto.response.UserResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.User;
import com.vinava.pofo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public UserResponse createUser(UserRequest userRequest, long clientId) {
        log.debug("Creating user for clientId: {} and userRequest: {}", clientId, userRequest);
        User userByEmail = getUserByEmail(clientId, userRequest.getEmail());
        User userByMobile = getUserByMobile(clientId, userRequest.getMobile());
        User userByUserName = getUserByUserName(clientId, userRequest.getUserName());
        if (userByEmail != null || userByMobile != null || userByUserName != null) {
            log.error("User already present with email: {} or mobile: {} or userName: {} for clientId: {}",
                    userRequest.getEmail(), userRequest.getMobile(), userRequest.getUserName(), clientId);
            log.error("UserByEmail : {}", userByEmail);
            log.error("UserByMobile : {}", userByMobile);
            log.error("UserByUserName: {}", userByUserName);
            if ((userByEmail.getId() != userByMobile.getId()) || (userByEmail.getId() != userByUserName.getId())) {
                log.error("Conflict detected");
                throw new ProcessException("User creation", "User already present with email/mobile/userName in request");
            }
            String errorMessage = String.format("User already present with email %s and/or mobile %s " +
                            "and/or userName %s for clientId %s",
                    userRequest.getEmail(), userRequest.getMobile(), userRequest.getUserName(), clientId);
            throw new ProcessException("User creation", errorMessage);
        }
        log.debug("User not found with email: {} or mobile: {} for clientId: {}",
                userRequest.getEmail(), userRequest.getMobile(), clientId);
        User user = userRequest.from(clientId);
        userRepository.save(user);
        log.debug("New user created: {}", user);
        return UserResponse.from(user);
    }

    @Override
    public UserResponse updateUser(long id, long clientId, UserRequest userRequest) {
        log.debug("Updating user with request: {}, id: {}, for clientId: {}", userRequest, id, clientId);
        Optional<User> optionalUser = userRepository.findByClientIdAndId(clientId, id);
        if (!optionalUser.isPresent()) {
            log.error("User not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("User updation", "Invalid user ID");
        }
        User user = userRequest.from(clientId);
        user.setId(id);
        user = userRepository.save(user);
        log.debug("Updated user with id: {} and clientId: {}. Response: {}", id, clientId, user);
        return UserResponse.from(user);
    }

    @Override
    public boolean deleteUser(long id, long clientId) {
        log.debug("Deleting user with id: {}, for clientId: {}", id, clientId);
        Optional<User> optionalUser = userRepository.findByClientIdAndId(clientId, id);
        if (!optionalUser.isPresent()) {
            log.error("User not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("User deletion", "Invalid user ID");
        }
        User user = optionalUser.get();
        userRepository.delete(user);
        log.debug("Deleted user with id: {} and clientId: {}", id, clientId);
        return true;
    }

    @Override
    public UserResponse getUserById(long id, long clientId) {
        log.debug("Fetching user with id: {}, for clientId: {}", id, clientId);
        Optional<User> optionalUser = userRepository.findByClientIdAndId(clientId, id);
        if (!optionalUser.isPresent()) {
            log.error("User not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Get User by id", "Invalid user ID");
        }
        User user = optionalUser.get();
        log.debug("Fetched user with id: {} and clientId: {}. Response: {}", id, clientId, user);
        return UserResponse.from(user);
    }

    @Override
    public UserResponse getUserByUserName(String userName, long clientId) {
        log.debug("Fetching user with userName: {}, for clientId: {}", userName, clientId);
        Optional<User> optionalUser = userRepository.findByClientIdAndUserName(clientId, userName);
        if (!optionalUser.isPresent()) {
            log.error("User not present with userName: {}, and clientId: {}", userName, clientId);
            throw new ProcessException("Get User by userName", "Invalid userName");
        }
        User user = optionalUser.get();
        log.debug("Fetched user with userName: {} and clientId: {}. Response: {}", userName, clientId, user);
        return UserResponse.from(user);
    }

    @Override
    public ResponseEntity<List<UserResponse>> getAllUsers(long clientId, Integer pageNumber,
                                                          Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllUsers with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} and clientId: {} ",
                    pageNumber, pageSize, sortBy, order, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<User> users = userRepository.findAllByClientId(clientId, pageable);
        log.debug("Returning from getAllUsers for clientId: {},  with response: {}",
                clientId, users);
        return UserResponse.getResponseEntityFrom(users);
    }

    private User getUserByEmail(long clientId, String email) {
        log.debug("Getting user with email: {} and clientId: {}", email, clientId);
        if (email == null) {
            return null;
        }
        Optional<User> optionalUser = userRepository.findByClientIdAndEmail(clientId, email);
        if (optionalUser.isPresent()) {
            log.debug("User found with email : {} for clientId: {}", email, clientId);
            return optionalUser.get();
        }
        return null;
    }

    private User getUserByMobile(long clientId, String mobile) {
        log.debug("Getting user with mobile: {} and clientId: {}", mobile, clientId);
        if (mobile == null) {
            return null;
        }
        Optional<User> optionalUser = userRepository.findByClientIdAndMobile(clientId, mobile);
        if (optionalUser.isPresent()) {
            log.debug("User found with mobile : {} for clientId: {}", mobile, clientId);
            return optionalUser.get();
        }
        return null;
    }

    private User getUserByUserName(long clientId, String userName) {
        log.debug("Getting user with userName: {} and clientId: {}", userName, clientId);
        Optional<User> optionalUser = userRepository.findByClientIdAndUserName(clientId, userName);
        if (optionalUser.isPresent()) {
            log.debug("User found with userName : {} for clientId: {}", userName, clientId);
            return optionalUser.get();
        }
        return null;
    }

}
