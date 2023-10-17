package com.example.graphgrader.Hindaja;

import com.example.graphgrader.Graaf.Graaf;
import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;

import java.util.*;

public class Lahendaja {

    public static List<Tegevus> lahenda(Algoritm a, Graaf g) {
        List<Tegevus> tagastus = new ArrayList<>();
        switch (a) {
            case LAIUTI_LÄBIMINE -> tagastus = laiuti(g);
            case SYGAVUTI_EES -> tagastus = sygavutiEes(g);
        }
        return tagastus;
    }

    public static List<Tegevus> laiuti(Graaf g) {
        List<Tegevus> tegevused = new ArrayList<>();

        Queue<Tipp> ootel = new ArrayDeque<>();
        List<Tipp> toodeldud = new ArrayList<>();

        for (Tipp tipp : g.tipud.get(0).alluvad) {
            ootel.add(tipp);
            tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_JARJEKORDA, tipp));
        }

        toodeldud.add(g.tipud.get(0));
        tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, g.tipud.get(0)));

        while (!ootel.isEmpty()) {
            Tipp praegune = ootel.remove();
            tegevused.add(new Tegevus(Tegevus.Voimalus.EEMALDA_JARJEKORRAST, praegune));
            if (toodeldud.contains(praegune)) continue;
            for (Kaar kaar : praegune.kaared) {
                ootel.add(kaar.lopp);
                tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_JARJEKORDA, kaar.lopp));
            }
            tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, praegune));
            toodeldud.add(praegune);
        }

        return tegevused;
    }

    public static List<Tegevus> sygavutiEes(Graaf g) {
        List<Tegevus> tegevused = new ArrayList<>();

        Deque<Tipp> ootel = new ArrayDeque<>();
        List<Tipp> toodeldud = new ArrayList<>();

        for (int i = g.tipud.get(0).kaared.size() - 1; i >= 0; i--) {
            Tipp tipp = g.tipud.get(0).kaared.get(i).lopp;
            ootel.push(tipp);
            tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_MAGASINI, tipp));
        }

        toodeldud.add(g.tipud.get(0));
        tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, g.tipud.get(0)));

        while (!ootel.isEmpty()) {
            Tipp praegune = ootel.pop();
            tegevused.add(new Tegevus(Tegevus.Voimalus.EEMALDA_MAGASINIST, praegune));
            if (toodeldud.contains(praegune)) continue;

            for (int i = praegune.kaared.size() - 1; i >= 0; i--) {
                Tipp uus = praegune.kaared.get(i).lopp;
                ootel.push(uus);
                tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_MAGASINI, uus));
            }

            toodeldud.add(praegune);
            tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, praegune));
        }

        return tegevused;
    }

    public static List<Tegevus> sygavutiLopp(Graaf g) {
        List<Tegevus> tegevused = new ArrayList<>();

        Deque<Tipp> ootel = new ArrayDeque<>();
        List<Tipp> toodeldud = new ArrayList<>();

        for (int i = 0; i < g.tipud.get(0).kaared.size(); i++) {
            Tipp tipp = g.tipud.get(0).kaared.get(i).lopp;
            ootel.push(tipp);
            tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_MAGASINI, tipp));
        }

        toodeldud.add(g.tipud.get(0));
        tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, g.tipud.get(0)));

        while (!ootel.isEmpty()) {
            Tipp praegune = ootel.pop();
            tegevused.add(new Tegevus(Tegevus.Voimalus.EEMALDA_MAGASINIST, praegune));
            if (toodeldud.contains(praegune)) continue;

            for (int i = 0; i < g.tipud.get(0).kaared.size(); i++) {
                Tipp uus = praegune.kaared.get(i).lopp;
                ootel.push(uus);
                tegevused.add(new Tegevus(Tegevus.Voimalus.LISA_MAGASINI, uus));
            }

            toodeldud.add(praegune);
            tegevused.add(new Tegevus(Tegevus.Voimalus.TOODELDUD, praegune));
        }

        return tegevused;
    }

    // TODO
    public static List<Tegevus> prim(Graaf g) {
        List<Tegevus> tegevused = new ArrayList<>();

        Kuhi kuhi = new Kuhi();
        List<Tipp> toodeldud = new ArrayList<>();


        return null;
    }

    // TODO
    public static List<Tegevus> kruskal(Graaf g) {
        return null;
    }

    // TODO
    public static List<Tegevus> dijkstra(Graaf g) {
        return null;
    }

    // TODO
    public static List<Tegevus> floydWarshall(Graaf g) {
        return null;
    }

    // TODO
    public static List<Tegevus> topSortLopp(Graaf g) {
        return null;
    }

    // TODO
    public static List<Tegevus> kahn(Graaf g) {
        return null;
    }

    // TODO
    public static List<Tegevus> eeldusGAnalyys(Graaf g) {
        return null;
    }

    // TODO
    public static List<Tegevus> kosaraju(Graaf g) {
        return null;
    }
}
