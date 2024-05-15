package com.example.graphgrader.Util;

import com.example.graphgrader.Graaf.Graaf;
import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Logija {

    public static void logi(List<String> vead, Graaf g, List<String> sammud, String algo, boolean kaarteKaalud, boolean tipuKaalud) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(algo + "_logi.txt"))) {
            bw.write("p edge %d %d%s%n".formatted(g.tipud.size(), g.tipud.stream().mapToInt(e -> e.alluvad.size()).sum(), (tipuKaalud ? tippudeKaalud(g) : "")));
            for (Tipp t : g.tipud)
                for (Kaar k : t.kaared)
                    bw.write("e " + t.tähis + " " + k.lopp.tähis + (kaarteKaalud ? (" " + k.kaal) : "") + "\n");

            bw.write("\n");
            int idx = 0;
            for (String s : sammud) {
                bw.write(s + "\n");
                if (s.endsWith("VIGA")) bw.write(vead.get(idx++) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String tippudeKaalud(Graaf g) {
        return String.join(" ", g.tipud.stream().map(e -> String.valueOf(e.kaal)).toList());
    }

}
