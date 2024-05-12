package com.example.graphgrader.Kontrollerid;

import com.example.graphgrader.Util.Teavitaja;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;

public class MainController {

    public Tab Laiuti;
    public String laiuti = "Laiuti läbimine\n\nKasutatav andmestruktuur: järjekord\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Töötle selles järglased (väljuvad kaared) ning siis märgi praegune tipp töödelduks (klikates sellele).";
    public Tab Sygavuti1;
    public String syg1 = "Sügavuti läbimine (eesjärjestuses)\n\nKasutatav andmestruktuur: magasin\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Märgi praegune tipp töödelduks ning seejärel töötle tema järglased (väljuvad kaared).";
    public Tab Sygavuti2;
    public String syg2 = "Sügavuti läbimine (lõppjärjestuses)\n\nKasutavadad andmestruktuurid: magasinid\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Töötle praeguse tipu järglased (väljuvad kaared) ning seejärel märgi praegune tipp töödelduks.";
    public Tab Prim;
    public String prim = "Minimaalse kaaluga toespuu leidmine (Prim)\n\nKasutatav andmestruktuur: eelistusjärjekord (kuhi - automaatne)\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Lisa kõik praegu töödeldava tipuga intsidentsed kaared eelistusjärjekorda, seejärel märgi tipp töödelduks ning võta eelistusjärjekorrast uus tipp.";
    public Tab Kruskal;
    public String kruskal = "Minimaalse kaaluga toespuu leidmine (Kruskal)\n\nKasutatav andmestruktuur: kuhi (automaatne)\n\nKõik tipud on kohe rohelised (ühetipuliste puude mets). Hakka eelistusjärjekorrast kaari võtma ning iga kaare kohta otsusta, kas ta kuulub toesesse või mitte.";
    public Tab Dijkstra;
    public String dijkstra = "Kauguste leidmine algtipust kõikidesse teistesse tippudesse\n\nKasutatav andmestruktuur: kuhi (automaatne)\n\nPunaseks värvitud tipp on praegu töödeldav tipp. Töötle praeguse tipu järglased (väljuvad kaared). Selleks vajuta kaare peale, ning sisesta järglase (uus)kaal. Seejärel märgi praegune tipp töödelduks.\n\nLäbimängu lõpetamine: kui kõik tipud on töödeldud ja kuhi on saanud tühjaks vajuta \"Võta\" nuppu.";
    public Tab FW;
    public String fw = "Kauguste leidmine igast tipust igasse tippu\n\nKasutatav andmestruktuur: maatriks\n\nPunaseks värvitud tipp (üks rida ja üks veerg maatriksis) on fikseeritud. Vajuta halli nuppu maatriksis ning sisesta (uus)kaugus. Kui kogu maatriks (va punane rida ja veerg) on töödeldud siis märgi praegune tipp töödelduks.";
    public Tab BF;
    public String bf = "Kauguste leidmine algtipust kõikidesse teistesse tippudesse\n\nKasutatav andmestruktuur: järjekord\n\nVõta järjekorrast serv ning töötle see, see tähendab sisesta lõpptipu (uus) kaugus. Järjekorras tähistab | uue ringi algust. Järjekorrast püstkriipsu võtmisel küsitakse kas peaks algoritmi töö lõpetama.";
    public Tab Kahn;
    public String kahn = "Topoloogilise järjestuse leidmine\n\nKasutatavad andmestruktuurid: paisktabel ja järjekord\n\nMäära tabelis igale tipule tema sisendaste ning vajuta nuppu \"Kontrolli\". Kui sisendastmed on korrektsed siis kaob nupp ära. Seejärel lisa tipud sisendastmega 0 järjekorda. Võta järjekorrast tipp, töötle väljuvad kaared (vähenda järglaste sisendastmeid), lisa sisendastmega 0 tipud järjekorda, määra tipp töödelduks. Korda kuni kõik tipud on töödeldud.";
    public Tab Eeldus;
    public String eeldus = "Graafi eeldusanalüüs\n\nTipus kujutatud info vasakult paremale, ülevalt alla on tähis, tipu aeg, varaseim lõpp, hiliseim algus\n\nSisesta topoloogiline järjestus(tipu tähised, komadega eraldatud), seejärel määra igale tipule varaseim lõpuaeg, seejärel määra kogu projekti (graafi) varaseim lõpuaeg, seejärel määra igale tipule hiliseim algusaeg, seejärel sisesta kriitilised tipud.";

    @FXML
    public void initialize() {
        Laiuti.setOnSelectionChanged(e -> {
            if (Laiuti.isSelected()) Teavitaja.teavita(laiuti, Alert.AlertType.INFORMATION);
        });
        Sygavuti1.setOnSelectionChanged(e -> {
            if (Sygavuti1.isSelected()) Teavitaja.teavita(syg1, Alert.AlertType.INFORMATION);
        });
        Sygavuti2.setOnSelectionChanged(e -> {
            if (Sygavuti2.isSelected()) Teavitaja.teavita(syg2, Alert.AlertType.INFORMATION);
        });
        Prim.setOnSelectionChanged(e -> {
            if (Prim.isSelected()) Teavitaja.teavita(prim, Alert.AlertType.INFORMATION);
        });
        Kruskal.setOnSelectionChanged(e -> {
            if (Kruskal.isSelected()) Teavitaja.teavita(kruskal, Alert.AlertType.INFORMATION);
        });
        Dijkstra.setOnSelectionChanged(e -> {
            if (Dijkstra.isSelected()) Teavitaja.teavita(dijkstra, Alert.AlertType.INFORMATION);
        });
        FW.setOnSelectionChanged(e -> {
            if (FW.isSelected()) Teavitaja.teavita(fw, Alert.AlertType.INFORMATION);
        });
        BF.setOnSelectionChanged(e -> {
            if (BF.isSelected()) Teavitaja.teavita(bf, Alert.AlertType.INFORMATION);
        });
        Kahn.setOnSelectionChanged(e -> {
            if (Kahn.isSelected()) Teavitaja.teavita(kahn, Alert.AlertType.INFORMATION);
        });
        Eeldus.setOnSelectionChanged(e -> {
            if (Eeldus.isSelected()) Teavitaja.teavita(eeldus, Alert.AlertType.INFORMATION);
        });
    }
}
