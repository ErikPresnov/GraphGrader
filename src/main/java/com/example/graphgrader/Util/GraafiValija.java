package com.example.graphgrader.Util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class GraafiValija {
    public static String valiSuvaline(String kaust) {
        Path kaustaTee = Path.of(kaust);
        if (!Files.isDirectory(kaustaTee)) return null;
        try {
            List<Path> p = Files.list(kaustaTee).toList();
            Random r = new Random();
            return p.get(r.nextInt(p.size())).toString();
        } catch (Exception ignored) {}
        return null;
    }
}
