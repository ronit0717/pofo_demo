package com.vinava.pofo.dao;

import com.vinava.pofo.model.Brand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByNameAndClientId(String name, long clientId);
    Optional<Brand> findByIdAndClientId(long id, long clientId);
    List<Brand> findAllByClientId(long clientId, Pageable pageable);
}
