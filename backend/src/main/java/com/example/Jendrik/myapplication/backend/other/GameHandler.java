package com.example.Jendrik.myapplication.backend.other;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Die Klasse ist zur Speicher- bzw. Ladeverwaltung.
 * @author Jendrik
 *
 */
public class GameHandler implements Serializable{

    /**
     * Methode speichert das Spiel in einem frei wählbaren Dateinamen.
     *
     * @param oos ObjectOutputStream ist das Objekt, das die Datei schreibt.
     */
    public static void saveGame(Game game){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("savegame.sv"));
            oos.writeObject(game);
            oos.close();
        } catch (IOException e) {e.printStackTrace(); System.out.println("Speicherfehler");  }
    }

    /**
     * Methode listet alle Savegames (*.sv) im Stammverzeichnis auf. Anschließend erwartet das Programm eine Zahleneingabe welche Datei geladen werden soll.
     *
     * @param oos ObjectInputStream ist das Objekt, das die Datei liest.
     */
    public static Game loadGame(){
        Game game = new Game();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("savegame.sv"));
            game = (Game) ois.readObject();
            ois.close();
        }
        catch (IOException e) { }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Du Troll, die Zahl steht da nicht!");
            loadGame();
        }
        catch (ClassNotFoundException e) {System.out.println("Lesefehler");}

        return game;
    }

    /**
     * Methode prüft ob eine Datei zum Einlesen vorhanden ist.
     *
     * @return Existenz eines "Savegames"
     */
    public static boolean checkSaved(){
        File file = new File("save.sv");
        if(file.exists())
            return true;
        else
            return false;
    }
}
