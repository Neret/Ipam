package com.example.ipam_backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IndirizzoIpRepository extends JpaRepository<IndirizzoIp,Long> {
    boolean existsByIp(String ip);

    Optional<IndirizzoIp> findByIp(String ip);
}
