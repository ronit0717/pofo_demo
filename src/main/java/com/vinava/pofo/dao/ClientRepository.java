package com.vinava.pofo.dao;

import com.vinava.pofo.model.Client;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public Optional<Client> findBySlug(String slug);
    public List<Client> findAllByName(String clientName, Pageable pageable);
    public Optional<Client> findByNameAndAddressPincode(String name, String pincode);
}
