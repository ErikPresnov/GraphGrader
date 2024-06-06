package com.example.graphgrader.Util;

import com.example.graphgrader.Graaf.Graaf;
import com.example.graphgrader.Graaf.Tipp;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class Märgendaja {

    public static void margenda(Graaf g) {
        Queue<Tipp> j = new ArrayDeque<>();

        Tipp esimene = g.tipud.get(0);
        esimene.syg = 0;
        j.add(esimene);

        Set<Tipp> t = new HashSet<>();
        int sygavaim = 0;
        
        while (!j.isEmpty()) {
            Tipp praegune = j.poll();
            sygavaim = Math.max(praegune.syg, sygavaim);
            t.add(praegune);
            for (Tipp tipp : praegune.alluvad) {
                if (t.contains(tipp)) continue;
                tipp.syg = Math.min(praegune.syg + 1, tipp.syg);
                j.add(tipp);
            }
        }
        
        int[] suurus = new int[sygavaim + 1];
        for (Tipp tipp : g.tipud) 
            suurus[tipp.syg]++;
        
        int laius = 0;
        for (int i : suurus) laius = Math.max(laius, i);

        System.out.println(sygavaim);
        System.out.println(laius);
        int x = 800/sygavaim, y = 450/laius;
        for (Tipp tipp : g.tipud) {
            tipp.x = 40 + x * tipp.syg;
            tipp.y = 40 + y * (laius - suurus[tipp.syg]--);
        }
    }

}
