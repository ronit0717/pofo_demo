package com.vinava.pofo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinava.pofo.enumeration.ProductPricingType;
import com.vinava.pofo.model.embed.ProductAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdOn", "updatedOn"}, allowGetters = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long clientId;

    @Column(nullable = false, length = 200)
    private String name;

    private long productCategoryId;

    @Enumerated(value = EnumType.STRING)
    private ProductPricingType productPricingType;

    private BigDecimal price;

    private BigDecimal discountPercentage;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_id")
    private Set<Long> productImageIds = new HashSet<>();

    @Column(length = 2048)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_attributes", joinColumns = @JoinColumn(name = "product_id"))
    private Set<ProductAttribute> productAttributes = new HashSet<>();

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedOn;

}
