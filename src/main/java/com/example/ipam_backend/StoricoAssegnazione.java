package com.example.ipam_backend;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "storico_assegnazioni")
public class StoricoAssegnazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ip;

    @Column(name = "mac_address", nullable = false)
    private String macAddress;

    @Column(nullable = false)
    private String azione;

    @Column(name = "data_azione", nullable = false)
    private LocalDateTime dataAzione;
}