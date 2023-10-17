package com.example.graphgrader.Hindaja;

import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;

import java.util.ArrayList;
import java.util.List;

public class Kuhi {
    public List<Kaar> kuhi;

    public Kuhi() {
        this.kuhi = new ArrayList<>();
    }

    public Kuhi(List<Kaar> kuhi) {
        this.kuhi = kuhi;
        kuhjasta();
    }

    public void kuhjasta() {
        int n = kuhi.size()/2 - 1;

        for (int i = n; i >= 0; i--)
            kuhjasta(i);
    }

    public void kuhjasta(int n) {
        int vahim = n;
        int vasak = 2 * n + 1;
        int parem = 2 * n + 2;

        if (vasak < kuhi.size() && kuhi.get(vasak).kaal < kuhi.get(vahim).kaal)
            vahim = vasak;

        if (parem < kuhi.size() && kuhi.get(parem).kaal < kuhi.get(vahim).kaal)
            vahim = parem;

        if (vahim == n) return;

        swap(n, vahim);
        kuhjasta(vahim);
    }

    public void yles(int i) {
        int ylemaI = (i - 1)/2;
        if (kuhi.get(i).kaal < kuhi.get(ylemaI).kaal){
            swap(i, ylemaI);
            yles(ylemaI);
        }
    }

    public void alla(int i) {
        int alluvaIndeks1 = 2 * i + 1;
        int alluvaIndeks2 = 2 * i + 2;

        int vahim = i;
        if (kuhi.get(alluvaIndeks1).kaal < kuhi.get(vahim).kaal)
            vahim = alluvaIndeks1;

        if (kuhi.get(alluvaIndeks2).kaal < kuhi.get(vahim).kaal)
            vahim = alluvaIndeks2;

        if (vahim == i) return;

        swap(i, vahim);
        alla(vahim);
    }

    public void lisa(Kaar el) {
        kuhi.add(el);
        yles(kuhi.size() - 1);
    }

    public Tipp eemalda() {
        Tipp min = kuhi.remove(0).lopp;
        kuhi.add(0, kuhi.remove(kuhi.size() - 1));
        alla(0);
        return min;
    }

    public void swap(int esimene, int teine) {
        Kaar esimeneV = kuhi.remove(esimene);
        Kaar teineV = kuhi.remove(teine - 1);

        kuhi.add(esimene - 1, teineV);
        kuhi.add(teine, esimeneV);
    }
}
