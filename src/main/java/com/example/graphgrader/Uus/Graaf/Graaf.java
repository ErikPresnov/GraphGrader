package com.example.graphgrader.Uus.Graaf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Graaf {
    public List<Tipp> tipud;
    public boolean kaalutud;

    public Graaf(String failitee, boolean suunatud) throws IOException {
        List<String> graaf = loeFail(failitee);

        List<Tipp> tipud = new ArrayList<>();

        String[] esimene = graaf.get(0).split(" ");

        for (int i = 1; i <= Integer.parseInt(esimene[2]); i++) {
            Tipp tipp = new Tipp(String.valueOf(i));
            tipud.add(tipp);
        }

        for (int i = 1; i < graaf.size(); i++) {
            String[] osad = graaf.get(i).split(" ");
            int alg = Integer.parseInt(osad[1]);
            int lopp = Integer.parseInt(osad[2]);
            Tipp algus = tipud.get(alg - 1);
            Tipp loppT = tipud.get(lopp - 1);
            algus.lisaAlluv(loppT);
            if (!suunatud) loppT.lisaAlluv(algus);
            if (osad.length == 4) {
                algus.kaared.add(new Kaar(algus, loppT, Integer.parseInt(osad[3])));
                if (!suunatud)
                    loppT.kaared.add(new Kaar(loppT, algus, Integer.parseInt(osad[3])));
            }
            else {
                algus.kaared.add(new Kaar(algus, loppT));
                if (!suunatud)
                    loppT.kaared.add(new Kaar(loppT, algus));
            }

        }

        this.tipud = tipud;
        this.kaalutud = tipud.get(0).kaared.get(0).kaal != 0;
    }

    private static List<String> loeFail(String failitee) throws IOException {
        List<String> read = Files.readAllLines(Path.of(failitee));
        List<String> tagastus = new ArrayList<>();

        for (String s : read) {
            if (!Objects.equals(s.split(" ")[0], "c")) tagastus.add(s);
        }

        return tagastus;
    }
}
