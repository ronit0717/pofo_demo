package com.vinava.pofo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinava.pofo.enumeration.UserType;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.User;
import com.vinava.pofo.service.helper.ValidationService;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

    @JsonIgnore
    private ValidationService validationService;

    @NotBlank
    @Size(max = 15)
    private String userName;

    @NotBlank
    @Size(max = 200)
    private String fullName;

    @Size(max = 200)
    private String email;

    @Size(max = 15)
    private String mobile;

    @NotNull
    private UserType userType;

    private String companyName;

    private String designation;

    public User from(long clientId, ValidationService validationService) {
        this.validationService = validationService;
        validateUserRequest();
        return User.builder()
                .clientId(clientId)
                .userName(this.userName)
                .fullName(this.fullName)
                .email(this.email)
                .mobile(this.mobile)
                .userType(this.userType)
                .companyName(this.companyName)
                .designation(this.designation)
                .build();
    }

    private void validateUserRequest() {
        if ((!StringUtils.isEmpty(this.email)) && (!validationService.validateEmail(this.email))) {
            throw new ProcessException("User request validation", "Invalid email");
        }
        if (UserType.CORPORATE.equals(this.userType)) {
            if (StringUtils.isEmpty(this.companyName) || StringUtils.isEmpty((this.designation))) {
                throw new ProcessException("User request validation",
                        "For corporate users, company name and designation is mandatory");
            }
        }
    }

}
