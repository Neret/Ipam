package com.example.ipam_backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReteRepository extends JpaRepository<Rete,Long> {
    boolean existsByCidr(String cidr);

    Optional<Rete> findByCidr(String cidr);
}
