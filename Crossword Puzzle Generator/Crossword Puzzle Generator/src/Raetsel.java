/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Praktikant55103
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Raetsel {

    String[] antwort;
    char[][] gitter;
    int buchstaben;     // Gesamtanzahl der Buchstaben
    int[][] ausr;
    int dopplungscounter = 0;
    char[][] anfangende;
    int[][] positionen;
    String[] fragen;
    ArrayList<Integer> horizontal;
    ArrayList<Integer> vertikal;
    private String[] antwortArray;
    private String[] fragenArray;
    char[][] bearbeitungsgitter;

    /**
     * Sortiert die Woerter im Array antwort absteigend der Laenge nach
     */
    public void sort() {
        for (int i = 0; i < antwort.length; i++) {
            for (int j = 0; j < antwort.length; j++) {
                if (antwort[j].length() < antwort[i].length()) {
                    String temp1 = antwort[i];
                    String temp2 = fragen[i];
                    antwort[i] = antwort[j];
                    fragen[i] = fragen[j];
                    antwort[j] = temp1;
                    fragen[j] = temp2;
                }
            }
        }
    }

    /**
     * Erstellt ein Gitter der Groesse sqrt(buchstaben)+modifikator (abgerundet)
     *
     * @param modifikator
     */
    public void buildgitter(int modifikator) {      // Erstellen des Gitters mit zusaetzlicher Groesse modifikator,
        buchstaben = 0;
        for (int i = 0; i < antwort.length; i++) {      // Feststellen der Buchstabenanzahl
            buchstaben += antwort[i].length();
        }
        gitter = new char[(int) Math.sqrt(buchstaben) + modifikator][(int) Math.sqrt(buchstaben) + modifikator];
        for (int x = 0; x < gitter.length; x++) {       // Fuellt gitter mit Leerzeichen
            for (int y = 0; y < gitter[x].length; y++) {
                gitter[x][y] = ' ';
            }
        }
        ausr = new int[(int) Math.sqrt(buchstaben) + modifikator][(int) Math.sqrt(buchstaben) + modifikator];
        for (int x = 0; x < ausr.length; x++) {
            java.util.Arrays.fill(ausr[x], -1);
        }
        anfangende = new char[(int) Math.sqrt(buchstaben) + modifikator][(int) Math.sqrt(buchstaben) + modifikator];
        for (int x = 0; x < gitter.length; x++) {       // Fuellt gitter mit Leerzeichen
            for (int y = 0; y < gitter[x].length; y++) {
                gitter[x][y] = ' ';
            }
        }
        positionen = new int[antwort.length][3];
        bearbeitungsgitter = new char[gitter.length][gitter.length];
        for (int x = 0; x < bearbeitungsgitter.length; x++) {       // Fuellt gitter mit Leerzeichen
            for (int y = 0; y < bearbeitungsgitter[x].length; y++) {
                bearbeitungsgitter[x][y] = ' ';
            }
        }
    }

    /**
     * Platziert den String wort in gitter mit Ausrichtung ausrichtung (false =
     * vertikal, true = horizontal) beginnend bei gitter[x][y]
     *
     * @param wort Wort, das man einsetzen will
     * @param x x-Koordinate, an der man wort einsetzen will
     * @param y y-Koordinate, an der man wort einsetzen will
     * @param ausrichtung Ausrichtung, mit der man wort einsetzen will
     */
    public void place(String wort, int x, int y, boolean ausrichtung) {  // false = vertikal, true = horizontal
        if (ausrichtung) {        // Horizontales Platzieren
            for (int i = 0; i < wort.length(); i++) {
                if (i > 0 && i + 1 < wort.length()) {
                    gitter[x][y + i] = wort.charAt(i);
                    if (ausr[x][y + i] == -1) {
                        ausr[x][y + i] = 1;
                    } else {
                        ausr[x][y + i] = 2;
                    }
                    anfangende[x][y + i] = '-';
                }
                if (i == 0) {
                    gitter[x][y + i] = wort.charAt(i);
                    if (ausr[x][y + i] == -1) {
                        ausr[x][y + i] = 1;
                    } else {
                        ausr[x][y + i] = 2;
                    }
                    anfangende[x][y + i] = '0';

                }
                if (i + 1 == wort.length()) {
                    gitter[x][y + i] = wort.charAt(i);
                    if (ausr[x][y + i] == -1) {
                        ausr[x][y + i] = 1;
                    } else {
                        ausr[x][y + i] = 2;
                    }
                    anfangende[x][y + i] = '1';
                }
            }
        } else {       // Vertikales Platzieren
            for (int i = 0; i < wort.length(); i++) {
                if (i > 0 && i + 1 < wort.length()) {
                    gitter[x + i][y] = wort.charAt(i);
                    if (ausr[x + i][y] == -1) {
                        ausr[x + i][y] = 0;
                    } else {
                        ausr[x + i][y] = 2;
                    }
                    anfangende[x + i][y] = 'I';
                }
                if (i == 0) {
                    gitter[x + i][y] = wort.charAt(i);
                    if (ausr[x + i][y] == -1) {
                        ausr[x + i][y] = 0;
                    } else {
                        ausr[x + i][y] = 2;
                    }
                    anfangende[x + i][y] = '0';
                }
                if (i + 1 == wort.length()) {
                    gitter[x + i][y] = wort.charAt(i);
                    if (ausr[x + i][y] == -1) {
                        ausr[x + i][y] = 0;
                    } else {
                        ausr[x + i][y] = 2;
                    }
                    anfangende[x + i][y] = '1';
                }
            }
        }
    }

    /**
     * Sucht nach Stellen in gitter, an denen man wort einsetzen kann, sodass es
     * sich mit einem anderen Buchstaben im Feld ueberschneidet.
     *
     * @param wort Wort, das man einsetzen will
     * @return Gibt ein int[n][4] Array zurueck, wenn es n Ueberschneidungen
     * gibt. Fuer die k-te Ueberschneidung gilt: int[k][0] = x-Koordinate, bei
     * der man wort einsetzen muss, um die Ueberschneidung herbeizufuehren
     * int[k][1] = y-Koordinate, bei der man wort einsetzen muss, um die
     * Ueberschneidung herbeizufuehren int[k][2] = Ausrichtung, mit der man wort
     * einsetzen muss (1 = horizontal, 0 = vertikal) int[k][3] = Menge der
     * Ueberschneidungen, die man bei Einsetzen insgesamt herbeifuehrt
     */
    public int[][] check(String wort) {
        ArrayList<int[]> liste = new ArrayList<>();
        boolean wortvertraeglich;

        for (int x = 0; x < gitter.length; x++) {
            for (int y = 0; y < gitter.length; y++) {
                if (wort.contains(Character.toString(gitter[x][y]))) {        // Pruefen auf gleiche Buchstaben
                    if (x + wort.length() - wort.indexOf(gitter[x][y]) < gitter.length && x - wort.indexOf(gitter[x][y]) >= 0) {     // Passt wort vertikal?
                        wortvertraeglich = true;
                        for (int i = 0; i < wort.length(); i++) {       // Pruefen ob beim Schreiben des Worts andere Woerter ueberschrieben wuerden
                            if (!(wort.charAt(i) == gitter[x - wort.indexOf(gitter[x][y]) + i][y] || gitter[x - wort.indexOf(gitter[x][y]) + i][y] == ' ') || ausr[x - wort.indexOf(gitter[x][y]) + i][y] == 0 || ausr[x - wort.indexOf(gitter[x][y]) + i][y] == 2 || gitter[x - wort.indexOf(gitter[x][y]) + i][y] == '?') {
                                wortvertraeglich = false;
                            }
                        }
                        if (wortvertraeglich) {       // Anhaengen des neuen Fundes an ausgabe
                            liste.add(new int[]{x - wort.indexOf(gitter[x][y]), y, 0, 0});

                        }
                    }
                    if (y + wort.length() - wort.indexOf(gitter[x][y]) < gitter.length && y - wort.indexOf(gitter[x][y]) >= 0) {     // Passt wort horizontal?
                        wortvertraeglich = true;
                        for (int i = 0; i < wort.length(); i++) {       // Pruefen ob beim Schreiben des Worts andere Woerter ueberschrieben wuerden
                            if (!(wort.charAt(i) == gitter[x][y - wort.indexOf(gitter[x][y]) + i] || gitter[x][y - wort.indexOf(gitter[x][y]) + i] == ' ') || ausr[x][y - wort.indexOf(gitter[x][y]) + i] == 1 || ausr[x][y - wort.indexOf(gitter[x][y]) + i] == 2 || gitter[x][y - wort.indexOf(gitter[x][y]) + i] == '?') {
                                wortvertraeglich = false;
                            }
                        }

                        if (wortvertraeglich) {       // Anhaengen des neuen Fundes an ausgabe
                            liste.add(new int[]{x, y - wort.indexOf(gitter[x][y]), 1, 0});
                        }
                    }
                }
            }
        }
        int[][] ausgabe = new int[liste.size()][4];
        // Uebertragen der Liste in Array
        ausgabe = new int[liste.size()][4];
        for (int i = 0; i < liste.size(); i++) {
            ausgabe[i] = liste.get(i);
        }

        for (int i = 0; i < ausgabe.length; i++) {      // Festlegen der Menge der Ueberschneidungen
            ausgabe[i][3] = count(wort, ausgabe[i][0], ausgabe[i][1], (ausgabe[i][2] != 0));
        }

        return ausgabe;
    }

    /**
     * Zaehlt die Menge der Buchstabenueberschneidungen, wenn man eingabe an
     * Stelle (x,y) mit Ausrichtung ausrichtung einsetzt.
     *
     * @param eingabe Wort, das man einsetzen will
     * @param x x-Koordinate
     * @param y y-Koordinate
     * @param ausrichtung Anzahl der Ueberschneidungen bei Einsetzen in (x,y)
     * mit Ausrichtung ausrichtung
     * @return
     */
    public int count(String eingabe, int x, int y, boolean ausrichtung) {
        int ausgabe = 0;

        if (ausrichtung) {        // Horizontales Zaehlen
            for (int i = 0; i < eingabe.length(); i++) {
                if (gitter[x][y + i] == eingabe.charAt(i)) {
                    ausgabe += 1;
                }
            }
        } else {       // Vertikales Zaehlen
            for (int i = 0; i < eingabe.length(); i++) {
                if (gitter[x + i][y] == eingabe.charAt(i)) {
                    ausgabe += 1;
                }
            }
        }

        return ausgabe;
    }

    /**
     * Findet Maximum der Eingabe in der zweiten Koordinate an der Stelle index,
     * d.h. eingabe[ausgabe][index] >= eingabe[h][index] fuer alle h
     *
     * @param eingabe
     * @param index
     * @return
     */
    public static int getMaximum(int[][] eingabe, int index) {
        int ausgabe = 0;

        if (eingabe.length != 0) {
            for (int i = 0; i < eingabe.length; i++) {
                if (eingabe[i][index] > eingabe[ausgabe][index]) {
                    ausgabe = i;
                }
            }
        } else {
            ausgabe = -1;
        }

        return ausgabe;
    }

    /**
     * Mischt die Eintraege in antwort
     */
    public void randomise() {
        for (int i = 0; i < antwort.length; i++) {
            int rand = (int) (Math.random() * antwort.length);
            String temp1 = antwort[i];
            String temp2 = fragen[i];
            antwort[i] = antwort[rand];
            fragen[i] = fragen[rand];
            antwort[rand] = temp1;
            fragen[rand] = temp2;
        }
    }

    /**
     * Findet eine leere Stelle im Gitter, an der man wort einsetzen kann (wird
     * aufgerufen, wenn check keine passenden Stellen gefunden hat)
     *
     * @param wort
     * @return Ist von der Form (x,y,ausr) wobei x und y die Koordinaten sind
     * und ausr die Ausrichtung (1 = horizontal, 0 = vertikal). Wurde keine
     * passende Stelle gefunden, so gibt es (-1,-1,-1) zurueck.
     */
    public int[] simpleCheck(String wort) {
        int[] ausgabe = {-1, -1, -1};
        boolean foundV = true;          // Platz zum Einsetzen gefunden oder nicht (Vertikal)
        boolean foundH = true;          // Platz zum Einsetzen gefunden oder nicht (horizontal)
        boolean skip = false; // wir haben einen Platz zumEinsetzen gefunden

        for (int x = 0; x < gitter.length; x++) {
            for (int y = 0; y < gitter.length; y++) {
                for (int i = 0; i < wort.length(); i++) {

                    if (foundH && (y + wort.length() > gitter.length || gitter[x][y + i] != ' ')) {      // Horizontal
                        foundH = false;
                    }
                    if (foundV && (x + wort.length() > gitter.length || gitter[x + i][y] != ' ')) {      // Vertikal
                        foundV = false;
                    }
                    if (!foundH && !foundH) {
                        break;
                    }
                }
                if (foundH || foundV) {
                    ausgabe[0] = x;
                    ausgabe[1] = y;
                    if (foundH) {
                        ausgabe[2] = 1;
                    } else {
                        ausgabe[2] = 0;
                    }
                    skip = true;
                    break;
                }
            }
            if (skip) {
                break;
            }
        }
        return ausgabe;
    }

    /**
     * Setzt nach und nach die Woerter aus antwort in ein Spielfeld ein, unter
     * Maximierung der Ueberschneidungen und Minimierung der Groesse des
     * Spielfeldes.
     *
     * @param modmax Ist der maximale Modifikator, der beim Aufruf von
     * buildgitter uebergeben werden kann.
     */
    public void setField(int modmax) {
        int platziert = 1;
        for (int mod = 0; mod < modmax; mod++) {
            dopplungscounter = 0;
            buildgitter(mod);
            if ((gitter.length / 2) + antwort[0].length() <= gitter.length) {
                place(antwort[0], gitter.length / 2, gitter.length / 2, true);
                positionen[0] = new int[]{gitter.length / 2, gitter.length / 2, 1};

                for (int i = 1; i < antwort.length; i++) {
                    int[][] kandidaten = check(antwort[i]);
                    if (kandidaten.length != 0) {
                        int max = getMaximum(kandidaten, 3);
                        int x = kandidaten[max][0];
                        int y = kandidaten[max][1];
                        boolean ausrichtung = (kandidaten[max][2] != 0);

                        place(antwort[i], x, y, ausrichtung);
                        positionen[i][0] = x;
                        positionen[i][1] = y;
                        positionen[i][2] = kandidaten[max][2];
                        dopplungscounter += kandidaten[max][3];
                        platziert += 1;
                    } else {
                        int[] platz = simpleCheck(antwort[i]);
                        if (platz[0] != -1) {
                            int x = platz[0];
                            int y = platz[1];
                            boolean ausrichtung = (platz[2] != 0);
                            place(antwort[i], x, y, ausrichtung);
                            positionen[i][0] = platz[0];
                            positionen[i][1] = platz[1];
                            positionen[i][2] = platz[2];
                            platziert += 1;
                        } else {
                            break;
                        }
                    }
                }
                if (platziert == antwort.length) {
                    break;
                }
            }
        }
    }

    /**
     * Stellt die geordnete Frageliste des Raetsels her, die der User sieht
     * (aufgeteilt nach horizontal und vertikal). Die Listen enthalten dabei die
     * Indizes, unter denen die Fragen im Array fragen gespeichert sind.
     *
     * @param ausrichtung
     */
    public void fragenaufteilen() {
        horizontal = new ArrayList<Integer>();
        vertikal = new ArrayList<Integer>();

        for (int i = 0; i < positionen.length; i++) {                                 // Fuellen der horizontalen Liste
            if (positionen[i][2] != 0) {
                horizontal.add(i);
            }
        }
        for (int i = 0; i < horizontal.size(); i++) {                                 // Entsprechendes Fuellen der vertikalen Liste mit Woertern, die in gleichen Kaestchen anfangen
            boolean flag = false;                                               // Es wurde ein Wort gefunden, das im gleichen Kaestchen anfaengt
            for (int j = 0; j < positionen.length; j++) {
                if (horizontal.get(i) != j && positionen[horizontal.get(i)][0] == positionen[j][0] && positionen[horizontal.get(i)][1] == positionen[j][1]) {
                    vertikal.add(j);
                    flag = true;
                    break;
                }
            }
            if (!flag) {                                                          // Falls kein Wort gefunden wurde, das im gleichen Kaestchen anfaengt
                vertikal.add(-1);
            }
        }
        for (int i = 0; i < positionen.length; i++) {
            if (positionen[i][2] == 0 && !vertikal.contains(i)) {                 // Falls ein Wort vertikal ist und noch nicht in Liste vertikal steht
                
                    vertikal.add(i);
                
            }
        }

    }

    public void dateiEinlesen(String url) {

        ArrayList<String> tempEingelesen = new ArrayList<>();
        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(url);
            br = new BufferedReader(fr);
            String textline = br.readLine();
            while (textline != null) {
                tempEingelesen.add(textline);
                textline = br.readLine();
            }
        } catch (IOException ex) {
            System.err.println("Fehler beim Einlesen.");
        } finally {
            try {
                fr.close();
                br.close();
            } catch (IOException ex) {
                System.err.println("Fehler beim Schliessen.");
            }
        }

        if (tempEingelesen.size() % 2 == 1) {
            // Anstatt null muss Objekt Hauptfenster Ã¼bergeben werden, damit dort der Fehler angezeigt wird.
            JOptionPane.showMessageDialog(null, "Anzahl von Fragen und Antworten stimmt nicht ueberein. \nBitte die Textdatei überarbeiten oder eine andere auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
         return;
        }
            if (tempEingelesen.size() > 200) {
            // Anstatt null muss Objekt Hauptfenster Ã¼bergeben werden, damit dort der Fehler angezeigt wird.
            JOptionPane.showMessageDialog(null, "Zu viele Fragen und Anworten (>100). \nBitte die Textdatei überarbeiten oder eine andere auswählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        fragenArray = new String[(tempEingelesen.size() / 2)];
        antwortArray = new String[(tempEingelesen.size() / 2)];

        Iterator<String> it = tempEingelesen.iterator();

        int i = 0;
        int indexzaehler = 0;
        while (it.hasNext()) {
            if (i == 0) {
                fragenArray[indexzaehler] = it.next();
                i = 1;
            } else {
                antwortArray[indexzaehler] = it.next().toUpperCase();
                i = 0;
                indexzaehler++;
            }
        }
        /*for (int n = 0; n < indexzaehler; n++) {
            System.out.println("Frage " + (n + 1) + ":");
            System.out.println(fragenArray[n]);
            System.out.println("Antwort " + (n + 1) + ":");
            System.out.println(antwortArray[n]);
        }*/

    }

    public String url() {
        JFileChooser chooser = new JFileChooser();
        // Dialog zum Oeffnen von Dateien anzeigen
        chooser.showOpenDialog(null);
        return chooser.getSelectedFile().getAbsolutePath();
    }

    public Raetsel raetselErstellen(String url) {

        Raetsel raetsel = new Raetsel();                                           // Erstellen eines Raetsels, hier wird das Feld in sortierter
        raetsel.dateiEinlesen(url);
        raetsel.fragen = new String[raetsel.fragenArray.length];
        raetsel.antwort = new String[raetsel.antwortArray.length];
        //raetsel.antwort = raetsel.antwortArray.clone();    
        for(int i=0; i<raetsel.antwortArray.length; i++){
            raetsel.antwort[i] = "?"+raetsel.antwortArray[i];
        }
        raetsel.fragen = raetsel.fragenArray.clone();
        raetsel.sort();
        raetsel.setField(20);
        int modmax = raetsel.gitter.length - (int) Math.sqrt(raetsel.buchstaben);

        for (int k = 1; k < 500; k++) {                                         // Erstellen von 500 Raetseln mit zufaellig gemischter Liste
            Raetsel tempraetsel = new Raetsel();
            tempraetsel.antwort = raetsel.antwort.clone();
            tempraetsel.fragen = raetsel.fragen.clone();
            tempraetsel.randomise();
            tempraetsel.setField(modmax);

            
            if (tempraetsel.dopplungscounter > raetsel.dopplungscounter  && /*tempraetsel.gitter.length <= raetsel.gitter.length && */ tempraetsel.gitter[0][0] != '?'  ) {        // Falls es mehr Ueberschneidungen gibt als mit dem urspruenglichen
                raetsel = tempraetsel;                                            // Raetsel, wird dieses mit dem neuen Raetsel ueberschrieben // durch die Bedingung tempraetsel.gitter[0][0]!= '?' werden Rätsel mit isolierten Wörtern aussortiert
                                                                                  
        }
        }

        raetsel.fragenaufteilen();                                               // Bilden der Vertikal- und Horizontalfrageliste

        /*for (int x = 0; x < raetsel.gitter.length; x++) {                        // Darstellen des fertigen Raetsels
            for (int y = 0; y < raetsel.gitter.length; y++) {
                if (y < raetsel.gitter.length - 1) {
                    System.out.print(raetsel.gitter[x][y] + " ");
                } else {
                    System.out.print(raetsel.gitter[x][y]);
                    System.out.print(System.getProperty("line.separator"));
                }
            }
        }
        
        System.out.println(raetsel.dopplungscounter);                            // Anzahl der Ueberschneidungen im Raetsel
        System.out.println(raetsel.buchstaben);                                  // Anzahl der Buchstaben
        System.out.println(raetsel.gitter.length);                               // Seitenlaenge des Gitters
        System.out.println(raetsel.gitter.length - (int) Math.sqrt(raetsel.buchstaben));   // Groesse des Modifikators, der bei buildgitter verwendet wurde
/*
        for (int i = 0; i < raetsel.antwort.length; i++) {
            System.out.println(i + " " + raetsel.fragen[i] + " -- " + raetsel.antwort[i] + " " + raetsel.positionen[i][0] + "," + raetsel.positionen[i][1] + "," + raetsel.positionen[i][2]);
        }

        System.out.println("Horizontal:");
        for (int i = 0; i < raetsel.horizontal.size(); i++) {
            System.out.println(i + " " + raetsel.fragen[raetsel.horizontal.get(i)]);
        }
        System.out.println("Vertikal:");
        for (int i = 0; i < raetsel.vertikal.size(); i++) {
            if (raetsel.vertikal.get(i) != -1) {
                System.out.println(i + " " + raetsel.fragen[raetsel.vertikal.get(i)]);
            } else {
                System.out.println(-1);
            }
        }
        */
        return raetsel;
    }

    public static void main(String[] args) {

        
    }

}
