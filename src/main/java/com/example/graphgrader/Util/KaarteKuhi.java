package com.example.graphgrader.Util;

import com.example.graphgrader.Graaf.Kaar;

import java.util.ArrayList;
import java.util.List;

public class KaarteKuhi {

    public List<Kaar> kuhi;

    public KaarteKuhi() {
        this.kuhi = new ArrayList<>();
    }

    public Kaar min() {
        if (kuhi.isEmpty()) return null;
        Kaar min = kuhi.remove(0);
        if (!kuhi.isEmpty()) {
            kuhi.add(0, kuhi.remove(kuhi.size() - 1));
            viiAlla(0);
        }

        return min;
    }

    public void lisa(Kaar k) {
        kuhi.add(k);
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
        Kaar ajutine = kuhi.get(i);
        kuhi.set(i, kuhi.get(j));
        kuhi.set(j, ajutine);
    }

    public boolean onTyhi() {
        return kuhi.isEmpty();
    }

    public boolean sisaldab(Kaar k) {
        return kuhi.contains(k);
    }
}
