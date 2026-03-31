package com.example.ipam_backend;

import java.util.ArrayList;
import java.util.List;

public class CidrUtils {

    public static List<String> calcolaIpAssegnabili(String cidr) {
        String[] parti = cidr.split("/");
        String indirizzoRete = parti[0];
        int prefisso = Integer.parseInt(parti[1]);

        int ipBinario = ipAIntero(indirizzoRete);

        int mascheraBinaria = 0xFFFFFFFF << (32 - prefisso);

        int reteBinaria = ipBinario & mascheraBinaria;
        int broadcastBinario = reteBinaria | ~mascheraBinaria;

        List<String> listaIp = new ArrayList<>();

        for (int i = reteBinaria + 1; i < broadcastBinario; i++) {
            listaIp.add(interoAIp(i));
        }

        return listaIp;
    }

    private static int ipAIntero(String ip) {
        String[] ottetti = ip.split("\\.");
        return (Integer.parseInt(ottetti[0]) << 24) |
                (Integer.parseInt(ottetti[1]) << 16) |
                (Integer.parseInt(ottetti[2]) << 8) |
                Integer.parseInt(ottetti[3]);
    }

    private static String interoAIp(int ip) {
        return ((ip >> 24) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                (ip & 0xFF);
    }
}
