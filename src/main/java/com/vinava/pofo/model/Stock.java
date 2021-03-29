package com.vinava.pofo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinava.pofo.dto.request.StockMovementRequest;
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
@Table(name = "stocks")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdOn", "updatedOn"}, allowGetters = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long clientId;

    @Column(nullable = false)
    private long productId;

    @Column(nullable = false)
    private long storeId;

    private BigDecimal price;

    private BigDecimal discountPercentage;

    private BigDecimal quantity;

    private BigDecimal refillLevel;

    private boolean forSale;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdOn;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedOn;

    public StockMovementRequest createOpeningStockMovement() {
        if (this.quantity == null) {
            return null;
        }
        return StockMovementRequest.builder()
                .stockId(this.id)
                .storeId(this.storeId)
                .quantity(this.quantity)
                .stockMovementType(StockMovementType.IN)
                .stockMovementReferenceType(StockMovementReferenceType.OPENING_STOCK)
                .comment("New stock created")
                .build();
    }

}
