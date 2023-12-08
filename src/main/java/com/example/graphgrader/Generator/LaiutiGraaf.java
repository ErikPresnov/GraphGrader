package com.example.graphgrader.Generator;

import com.example.graphgrader.Graaf.Graaf;
import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;

import java.util.ArrayList;
import java.util.List;

public class LaiutiGraaf {

    // SISENDI GENEREERIMINE LAIUTI LÄBIMISELE
    // p = ??
    public static Graaf genereeri(int n) {
        List<Tipp> tipud = new ArrayList<>();
        List<Kaar> kaared = new ArrayList<>();

        int pool = n/2;
        for (int i = 0; i < pool; i++)
            tipud.add(new Tipp(String.valueOf(i).charAt(0)));

        for (int i = 0; i < pool - 1; i++) {
            Kaar k = new Kaar(tipud.get(i), tipud.get(i + 1));
            kaared.add(k);
            tipud.get(i).lisaAlluv(tipud.get(i + 1));
            tipud.get(i).kaared.add(k);
        }

        for (int i = 0; i < n - pool; i++) {
            int e = (int)(Math.random()*(pool));
            int t = (int)(Math.random()*(pool));
            while (e == t) t = (int)(Math.random()*(pool));
            Kaar k = new Kaar(tipud.get(e), tipud.get(t));
            kaared.add(k);
            tipud.get(e).lisaAlluv(tipud.get(t));
            tipud.get(e).kaared.add(k);
        }

        return new Graaf(tipud);
    }
}
