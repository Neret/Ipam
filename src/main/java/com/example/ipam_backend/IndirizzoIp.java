package com.example.ipam_backend;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "indirizzi_ip") // Meglio chiamarla indirizzi_ip per non fare confusione
public class IndirizzoIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "indirizzo_ip", unique = true, nullable = false)
    private String ip;

    @Column(name = "stato", nullable = false)
    private String stato = "LIBERO";

    @Column(name = "nome_host")
    private String nome;

    @Column(name = "mac_address")
    private String macAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rete_id", nullable = false) // Questa crea la colonna della Foreign Key nel DB
    private Rete rete;
}