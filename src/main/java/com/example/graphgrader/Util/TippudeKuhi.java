package com.example.graphgrader.Util;

import com.example.graphgrader.Graaf.Tipp;

import java.util.ArrayList;
import java.util.List;

public class TippudeKuhi {

    public List<Tipp> kuhi;

    public TippudeKuhi() {
        this.kuhi = new ArrayList<>();
    }

    public Tipp min() {
        if (kuhi.isEmpty()) return null;
        Tipp min = kuhi.remove(0);
        if (!kuhi.isEmpty()) {
            kuhi.add(0, kuhi.remove(kuhi.size() - 1));
            viiAlla(0);
        }

        return min;
    }

    public void lisa(Tipp t) {
        kuhi.add(t);
        viiYles(kuhi.size() - 1);
    }

    public void viiAlla(int i) {
        int vahim = i, vasak = 2 * i + 1, parem = 2 * i + 2;
        if (vasak < kuhi.size() && kuhi.get(vasak).kaal < kuhi.get(vahim).kaal) vahim = vasak;
        if (parem < kuhi.size() && kuhi.get(parem).kaal < kuhi.get(vahim).kaal) vahim = parem;

        if (vahim != i) {
            vaheta(vahim, i);
            viiAlla(vahim);
        }
    }

    public void viiYles(int i) {
        int ylemine = (i - 1)/2;
        while (kuhi.get(ylemine).kaal > kuhi.get(i).kaal) {
            vaheta(i, ylemine);
            i = ylemine;
            ylemine = (i - 1)/2;
        }
    }

    public void vaheta(int i, int j) {
        Tipp ajutine = kuhi.get(i);
        kuhi.set(i, kuhi.get(j));
        kuhi.set(j, ajutine);
    }

    public boolean onTyhi() {
        return kuhi.isEmpty();
    }

    public void kuhjasta() {
        for (int i = kuhi.size() / 2 - 1; i >= 0; i--) viiAlla(i);
    }
}
