package com.example.graphgrader.Hindaja;

import com.example.graphgrader.Graaf.Graaf;

import java.util.List;

public class Hindaja {

    public List<Tegevus> õiged;
    public List<Tegevus> tehtud;

    public Hindaja(Algoritm a, List<Tegevus> tehtud, Graaf g) {
        this.tehtud = tehtud;
        this.õiged = Lahendaja.lahenda(a, g);
    }

    public double hinda() {
        int valed = 0;
        for (int i = 0; i < Math.min(tehtud.size(), õiged.size()); i++) {
            if (!õiged.get(i).eq(tehtud.get(i))) {
                valed++;
            }
        }

        return 1.0 - (1.0 * valed / õiged.size());
    }
}
