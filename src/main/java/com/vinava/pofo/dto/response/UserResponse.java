package com.vinava.pofo.dto.response;

import com.vinava.pofo.model.User;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private String authId;
    private String name;
    private String email;
    private String mobile;
    private Date createdOn;
    private Date updatedOn;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .authId(user.getAuthId())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .createdOn(user.getCreatedOn())
                .updatedOn(user.getUpdatedOn())
                .build();
    }

    private static List<UserResponse> from(Page<User> users) {
        List<UserResponse> userResponses = new LinkedList<>();
        for (User user: users) {
            userResponses.add(from(user));
        }
        return userResponses;
    }

    public static ResponseEntity<List<UserResponse>> getResponseEntityFrom(Page<User> users) {
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
