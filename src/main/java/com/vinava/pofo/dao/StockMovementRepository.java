package com.vinava.pofo.dao;

import com.vinava.pofo.model.StockMovement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    Optional<StockMovement> findByIdAndClientId(long id, long clientId);
    List<StockMovement> findAllByClientId(long clientId, Pageable pageable);
    List<StockMovement> findAllByStockIdAndClientId(long stockId, long clientId, Pageable pageable);
}
