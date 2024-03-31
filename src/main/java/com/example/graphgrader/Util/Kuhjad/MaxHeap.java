package com.example.graphgrader.Util.Kuhjad;

import com.example.graphgrader.Graaf.Kaar;
import com.example.graphgrader.Graaf.Tipp;

import java.util.ArrayList;
import java.util.List;

public class MaxHeap {
    public boolean tipud;
    public List<Tipp> heap;
    public List<Kaar> heap1;

    public MaxHeap() {
        this.heap = new ArrayList<>();
        this.heap1 = new ArrayList<>();
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
        if (vasak != -1 && heap.get(vasak).kaal > heap.get(min).kaal) min = vasak;
        if (parem != -1 && heap.get(parem).kaal > heap.get(min).kaal) min = parem;

        if (min != i) {
            swap(i, min);
            alla(min);
        }
    }

    private void yles(int i) {
        int vanem = vanem(i);
        if (vanem < 0) return;
        if (heap.get(vanem).kaal < heap.get(i).kaal) {
            swap(vanem, i);
            yles(vanem);
        }
    }

    private void swap(int i, int j) {
        Tipp t = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, t);
    }

    public Tipp max() {
        if (heap.size() == 0) return null;
        Tipp tagastus = heap.remove(0);
        if (heap.size() == 0) return tagastus;
        heap.add(0, heap.remove(heap.size() - 1));
        alla(0);
        return tagastus;
    }

    public boolean tühi() {return heap.size() == 0;}
}
