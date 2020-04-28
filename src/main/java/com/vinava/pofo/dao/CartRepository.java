package com.vinava.pofo.dao;

import com.vinava.pofo.enumeration.CartStatus;
import com.vinava.pofo.model.Cart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByClientIdAndUserIdAndCartStatus(long clientId, long userId, CartStatus cartStatus);
    List<Cart> findAllByClientId(long clientId, Pageable pageable);
    Optional<Cart> findByClientIdAndId(long clientId, long id);
}
