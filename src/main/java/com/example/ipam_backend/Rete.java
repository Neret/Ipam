package com.example.ipam_backend;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "reti")
public class Rete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cidr;

    @Column(name = "descrizione")
    private String descrizione;

    @OneToMany(mappedBy = "rete", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IndirizzoIp> indirizziIp;
}
