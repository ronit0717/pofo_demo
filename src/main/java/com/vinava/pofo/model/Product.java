package com.vinava.pofo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinava.pofo.enumeration.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdOn", "updatedOn"}, allowGetters = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long clientId;

    private Long productCategoryId;

    @Enumerated(value = EnumType.STRING)
    private ProductType productType;

    private Double price;

    private Double discountPercentage;

    //private List<Long> productImageIds;

    @Column(length = 2048)
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
