package com.vinava.pofo.dao;

import com.vinava.pofo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCategoryIdAndNameAndClientId(long productCategoryId, String name, long clientId);
    Optional<Product> findByIdAndClientId(long id, long clientId);
    List<Product> findAllByClientIdAndProductCategoryId(long clientId, long productCategoryId, Pageable pageable);
    List<Product> findAllByClientId(long clientId, Pageable pageable);
}
