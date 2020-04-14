package com.vinava.pofo.dao;

import com.vinava.pofo.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndClientId(long id, long clientId);
    List<Category> findAllByParentCategoryIdAndClientId(long id, long clientId);
    List<Category> findAllByClientId(long clientId, Pageable pageable);
}
