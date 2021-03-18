package com.vinava.pofo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "stores")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdOn", "updatedOn"}, allowGetters = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long clientId;

    @NotBlank
    @Column(length = 30, nullable = false)
    private String name;

    @Column(nullable = false)
    private long locationId;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 15)
    private String gstin;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedOn;

}
