package com.example.ipam_backend;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class IpamService {

    private final ReteRepository reteRepository;
    private final IndirizzoIpRepository ipRepository;
    private final StoricoAssegnazioneRepository storicoRepository;

    public IpamService(ReteRepository reteRepository,
                       IndirizzoIpRepository ipRepository,
                       StoricoAssegnazioneRepository storicoRepository) {
        this.reteRepository = reteRepository;
        this.ipRepository = ipRepository;
        this.storicoRepository = storicoRepository;
    }


    public void generaRete(String cidr, String descrizione) {
        if (reteRepository.existsByCidr(cidr)) {
            throw new IllegalArgumentException("La rete " + cidr + " è già presente!");
        }

        Rete nuovaRete = new Rete();
        nuovaRete.setCidr(cidr);
        nuovaRete.setDescrizione(descrizione);
        Rete reteSalvata = reteRepository.save(nuovaRete);

        List<String> listaStringheIp = CidrUtils.calcolaIpAssegnabili(cidr);

        List<IndirizzoIp> listaEntitaIp = new ArrayList<>();

        for (String ip : listaStringheIp) {
            IndirizzoIp nuovoIp = new IndirizzoIp();
            nuovoIp.setIp(ip);
            nuovoIp.setStato("LIBERO");
            nuovoIp.setRete(reteSalvata); // Foreign Key impostata qui
            listaEntitaIp.add(nuovoIp);
        }

        ipRepository.saveAll(listaEntitaIp);
    }


    public void assegnaIp(String ip, String macAddress, String nomeHost) {
        IndirizzoIp indirizzo = ipRepository.findByIp(ip)
                .orElseThrow(() -> new IllegalArgumentException("Errore: IP " + ip + " non trovato nel database!"));

        if ("ASSEGNATO".equals(indirizzo.getStato())) {
            throw new IllegalArgumentException("L'IP " + ip + " è già occupato da un altro dispositivo!");
        }

        indirizzo.setStato("ASSEGNATO");
        indirizzo.setMacAddress(macAddress);
        indirizzo.setNome(nomeHost);
        ipRepository.save(indirizzo);

        salvaInStorico(ip, macAddress, "ASSEGNATO");
    }

    public void liberaIp(String ip) {
        IndirizzoIp indirizzo = ipRepository.findByIp(ip)
                .orElseThrow(() -> new IllegalArgumentException("IP non trovato!"));

        String macPrecedente = indirizzo.getMacAddress();

        indirizzo.setStato("LIBERO");
        indirizzo.setMacAddress(null);
        indirizzo.setNome(null);
        ipRepository.save(indirizzo);

        salvaInStorico(ip, macPrecedente, "RIMOSSO");
    }


    public void eliminaRete(String cidr) {
        Rete rete = reteRepository.findByCidr(cidr)
                .orElseThrow(() -> new IllegalArgumentException("Rete non trovata!"));

        reteRepository.delete(rete);
    }


    public List<Rete> ottieniTutteLeReti() {
        return reteRepository.findAll();
    }

    public List<IndirizzoIp> ottieniTuttiGliIp() {
        return ipRepository.findAll();
    }

    public List<StoricoAssegnazione> ottieniStoricoCompleto() {
        return storicoRepository.findAll();
    }

    private void salvaInStorico(String ip, String mac, String azione) {
        StoricoAssegnazione record = new StoricoAssegnazione();
        record.setIp(ip);
        record.setMacAddress(mac);
        record.setAzione(azione);
        record.setDataAzione(LocalDateTime.now());
        storicoRepository.save(record);
    }
    @Transactional
    public void eliminaInteraRete(String cidr) {
        Rete rete = reteRepository.findByCidr(cidr)
                .orElseThrow(() -> new IllegalArgumentException("Impossibile eliminare: la rete " + cidr + " non esiste."));
        reteRepository.delete(rete);
        salvaInStorico(cidr, "N/A", "RETE_ELIMINATA");
    }
}