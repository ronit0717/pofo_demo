package com.vinava.pofo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContactDetail {

    @Size(max = 100)
    @Column(length = 100)
    private String contactName;

    @Size(max = 100)
    @Column(length = 100)
    private String email;

    @Size(max = 15)
    @NotBlank
    @Column(length = 15, nullable = false)
    private String phoneNumber;

}
