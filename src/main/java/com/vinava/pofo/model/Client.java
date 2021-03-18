package com.vinava.pofo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinava.pofo.dto.Address;
import com.vinava.pofo.dto.ContactDetail;
import com.vinava.pofo.enumeration.ClientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "pofo_clients")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdOn", "updatedOn"}, allowGetters = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(length = 15, nullable = false, unique = true)
    private String slug;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 2048)
    private String logoImage;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active;

    private Date subscriptionEndDate;

    @Embedded
    private Address address;

    @Embedded
    private ContactDetail contactDetail;

    @Column(length = 15)
    private String gstin;

    private Date activationDate;

    @Enumerated(value = EnumType.STRING)
    private ClientType clientType;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedOn;

}
