package com.example.ipam_backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoricoAssegnazioneRepository extends JpaRepository<StoricoAssegnazione,Long> {
}
