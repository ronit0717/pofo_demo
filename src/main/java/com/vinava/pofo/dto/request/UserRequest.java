package com.vinava.pofo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.User;
import com.vinava.pofo.service.helper.ValidationService;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

    @JsonIgnore
    private ValidationService validationService;

    private String authId;

    @NotBlank
    @Size(max = 15)
    private String userName;

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 200)
    private String email;

    @Size(max = 15)
    private String mobile;

    public User from(ValidationService validationService) {
        this.validationService = validationService;
        validateUserRequest();
        return User.builder()
                .authId(authId)
                .name(this.name)
                .email(this.email)
                .mobile(this.mobile)
                .build();
    }

    private void validateUserRequest() {
        if ((!StringUtils.isEmpty(this.email)) && (!validationService.validateEmail(this.email))) {
            throw new ProcessException("User request validation", "Invalid email");
        }
    }

}
