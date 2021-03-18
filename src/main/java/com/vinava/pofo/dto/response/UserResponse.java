package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.UserType;
import com.vinava.pofo.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@Slf4j
public class UserResponse {

    private long id;
    private long clientId;
    private String userName;
    private String fullName;
    private String email;
    private String mobile;
    private UserType userType;
    private String companyName;
    private String designation;
    private Date createdOn;
    private Date updatedOn;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .clientId(user.getClientId())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .userType(user.getUserType())
                .companyName(user.getCompanyName())
                .designation(user.getDesignation())
                .createdOn(user.getCreatedOn())
                .updatedOn(user.getUpdatedOn())
                .build();
    }

    private static List<UserResponse> from(List<User> users) {
        List<UserResponse> userResponses = new LinkedList<>();
        for (User user: users) {
            userResponses.add(from(user));
        }
        return userResponses;
    }

    public static ResponseEntity<List<UserResponse>> getResponseEntityFrom(List<User> users) {
        try {
            List<UserResponse> userResponses = from(users);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(userResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(userResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of users: {}", users, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
