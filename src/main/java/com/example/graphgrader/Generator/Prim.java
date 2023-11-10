package com.example.graphgrader.Generator;

import com.example.graphgrader.Graaf.Graaf;
import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;

import java.util.ArrayList;
import java.util.List;

public class Prim {

    public static Graaf genereeri(int p) {
        int algne = p;
        List<Tipp> tipud = new ArrayList<>();
        List<Kaar> kaared = new ArrayList<>();

        int pool = p/2;
        for (int i = 0; i < p; i++)
            tipud.add(new Tipp(String.valueOf(i).charAt(0)));

        for (int i = 0; i < p - 1; i++) {
            Kaar k = new Kaar(tipud.get(i), tipud.get(i + 1));
            kaared.add(k);
            tipud.get(i).lisaAlluv(tipud.get(i + 1));
            tipud.get(i).kaared.add(k);
        }

        while (p != 0) {
            int e = (int)(Math.random()*(algne));
            int t = (int)(Math.random()*(algne));
            while (e == t) t = (int)(Math.random()*(algne));
            Kaar k = new Kaar(tipud.get(e), tipud.get(t));
            kaared.add(k);
            tipud.get(e).lisaAlluv(tipud.get(t));
            tipud.get(e).kaared.add(k);
            p--;
        }


        return new Graaf(tipud, kaared);
    }
}
