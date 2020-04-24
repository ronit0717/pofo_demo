package com.vinava.pofo.dao;

import com.vinava.pofo.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByClientIdAndEmail(long clientId, String email);
    Optional<User> findByClientIdAndMobile(long clientId, String mobile);
    Optional<User> findByClientIdAndUserName(long clientId, String userName);
    Optional<User> findByClientIdAndId(long clientId, long id);
    List<User> findAllByClientId(long clientId, Pageable pageable);
}
