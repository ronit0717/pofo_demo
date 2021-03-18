package com.vinava.pofo.dao;

import com.vinava.pofo.model.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByNameAndClientId(String name, long clientId);
    Optional<Location> findByIdAndClientId(long id, long clientId);
    List<Location> findAllByClientId(long clientId, Pageable pageable);
}
