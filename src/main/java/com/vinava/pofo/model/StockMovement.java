package com.vinava.pofo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinava.pofo.enumeration.StockMovementReferenceType;
import com.vinava.pofo.enumeration.StockMovementType;
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

@Entity
@Table(name = "stock_movements")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdOn", "updatedOn"}, allowGetters = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long clientId;

    @Column(nullable = false)
    private long stockId;

    @Column(nullable = false)
    private long storeId;

    private BigDecimal quantity;

    @Enumerated(value = EnumType.STRING)
    private StockMovementType stockMovementType;

    @Enumerated(value = EnumType.STRING)
    private StockMovementReferenceType stockMovementReferenceType;

    @Column(length = 50)
    private String referenceId;

    @Column(length = 1024)
    private String comment;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedOn;

}
