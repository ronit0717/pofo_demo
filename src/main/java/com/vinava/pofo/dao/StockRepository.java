package com.vinava.pofo.dao;

import com.vinava.pofo.model.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductIdAndStoreIdAndClientId(long productId, long storeId, long clientId);
    Optional<Stock> findByIdAndClientId(long id, long clientId);
    List<Stock> findAllByStoreIdAndClientId(long storeId, long clientId, Pageable pageable);
    List<Stock> findAllByStoreIdAndClientIdAndForSale(long storeId, long clientId, boolean forSale, Pageable pageable);
}
