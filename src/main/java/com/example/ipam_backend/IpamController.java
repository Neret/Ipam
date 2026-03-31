package com.example.ipam_backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
public class IpamController {

    private final IpamService ipamService;

    public IpamController(IpamService ipamService) {
        this.ipamService = ipamService;
    }


    @GetMapping("/")
    public String dashboard(Model model) {
        List<IndirizzoIp> listaIp = ipamService.ottieniTuttiGliIp();
        model.addAttribute("indirizzi", listaIp != null ? listaIp : new ArrayList<>());

        model.addAttribute("reti", ipamService.ottieniTutteLeReti());
        model.addAttribute("storico", ipamService.ottieniStoricoCompleto());
        return "index";
    }


    @PostMapping("/rete/genera")
    public String generaRete(@RequestParam String cidr,
                             @RequestParam String descrizione,
                             RedirectAttributes ra) {
        try {
            ipamService.generaRete(cidr, descrizione);
            ra.addFlashAttribute("successo", "Rete " + cidr + " generata con successo!");
        } catch (Exception e) {
            ra.addFlashAttribute("errore", e.getMessage());
        }
        return "redirect:/";
    }


    @PostMapping("/ip/assegna")
    public String assegnaIp(@RequestParam String ip,
                            @RequestParam String mac,
                            @RequestParam String nome,
                            RedirectAttributes ra) {
        try {
            ipamService.assegnaIp(ip, mac, nome);
            ra.addFlashAttribute("successo", "IP " + ip + " assegnato correttamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errore", e.getMessage());
        }
        return "redirect:/";
    }


    @PostMapping("/ip/libera")
    public String liberaIp(@RequestParam String ip, RedirectAttributes ra) {
        try {
            ipamService.liberaIp(ip);
            ra.addFlashAttribute("successo", "IP " + ip + " tornato libero.");
        } catch (Exception e) {
            ra.addFlashAttribute("errore", e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/rete/elimina")
    public String eliminaRete(@RequestParam String cidr, RedirectAttributes ra) {
        try {
            ipamService.eliminaRete(cidr);
            ra.addFlashAttribute("successo", "Rete eliminata insieme a tutti i suoi IP.");
        } catch (Exception e) {
            ra.addFlashAttribute("errore", e.getMessage());
        }
        return "redirect:/";
    }
}