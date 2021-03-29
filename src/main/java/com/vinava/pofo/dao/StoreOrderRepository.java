package com.vinava.pofo.dao;

import com.vinava.pofo.model.StoreOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {
    Optional<StoreOrder> findByCartIdAndClientId(long cartId, long clientId);
    Optional<StoreOrder> findByOrderSlugAndClientId(String orderSlug, long clientId);
    Optional<StoreOrder> findByIdAndClientId(long id, long clientId);
    List<StoreOrder> findAllByClientId(long clientId, Pageable pageable);
    List<StoreOrder> findAllByClientIdAndStoreId(long clientId, long storeId, Pageable pageable);
}
