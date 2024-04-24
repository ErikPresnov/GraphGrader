package com.example.graphgrader.Vana.Util.Kuhjad;

import com.example.graphgrader.Uus.Graaf.Kaar;

import java.util.ArrayList;
import java.util.List;

public class MinHeapKaared {
    public List<Kaar> heap;

    public MinHeapKaared() {
        this.heap = new ArrayList<>();
    }

    private int vanem(int idx) {
        return (idx - 1) / 2;
    }

    private int vasak(int idx) {
        if (2 * idx + 1 >= heap.size()) return -1;
        return 2 * idx + 1;
    }

    private int parem(int idx) {
        if (2 * idx + 2 >= heap.size()) return -1;
        return 2 * idx + 2;
    }

    private void alla(int i) {
        int min = i;
        int vasak = vasak(i);
        int parem = parem(i);
        if (vasak != -1 && heap.get(vasak).kaal < heap.get(min).kaal) min = vasak;
        if (parem != -1 && heap.get(parem).kaal < heap.get(min).kaal) min = parem;

        if (min != i) {
            swap(i, min);
            alla(min);
        }
    }

    private void yles(int i) {
        int vanem = vanem(i);
        if (vanem < 0) return;
        if (heap.get(vanem).kaal > heap.get(i).kaal) {
            swap(vanem, i);
            yles(vanem);
        }
    }

    private void swap(int i, int j) {
        Kaar t = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, t);
    }

    public Kaar min() {
        if (heap.size() == 0) return null;
        Kaar tagastus = heap.remove(0);
        if (heap.size() == 0) return tagastus;
        heap.add(0, heap.remove(heap.size() - 1));
        alla(0);
        return tagastus;
    }

    public void lisa(Kaar t) {
        heap.add(t);
        yles(heap.size() - 1);
    }

    public boolean tühi() {return heap.size() == 0;}

    public boolean olemas(Kaar t) {
        for (Kaar k : heap) if (t.algus == k.algus && t.lopp == k.lopp) return true;
        return false;
    }
}
