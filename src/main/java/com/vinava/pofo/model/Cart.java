package com.vinava.pofo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vinava.pofo.enumeration.CartStatus;
import com.vinava.pofo.model.embed.CartEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdOn", "updatedOn"}, allowGetters = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long clientId;

    private Long userId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "cart_entities", joinColumns = @JoinColumn(name = "cart_id"))
    private List<CartEntity> cartEntities = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private CartStatus cartStatus;

}
