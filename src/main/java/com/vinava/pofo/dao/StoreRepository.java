package com.vinava.pofo.dao;

import com.vinava.pofo.model.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByNameAndClientIdAndLocationId(String name, long clientId, long locationId);
    Optional<Store> findByIdAndClientId(long id, long clientId);
    List<Store> findAllByClientIdAndLocationId(long clientId, long locationId, Pageable pageable);
    List<Store> findAllByClientId(long clientId, Pageable pageable);
}
