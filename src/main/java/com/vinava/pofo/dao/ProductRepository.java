package com.vinava.pofo.dao;

import com.vinava.pofo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCategoryIdAndBrandIdAndNameAndClientId(long categoryId, long brandId, String name, long clientId);
    Optional<Product> findByCategoryIdAndNameAndClientId(long categoryId, String name, long clientId);
    Optional<Product> findByBrandIdAndNameAndClientId(long brandId, String name, long clientId);
    Optional<Product> findByIdAndClientId(long id, long clientId);
    List<Product> findAllByClientIdAndCategoryId(long clientId, long categoryId, Pageable pageable);
    List<Product> findAllByClientId(long clientId, Pageable pageable);
}
