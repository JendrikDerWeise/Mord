package com.example.Jendrik.myapplication.backend.GameObjekts;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Jendrik on 21.02.2016.
 */
public class Player implements Serializable{
    private String name;
    private boolean active;
    private boolean dead;
    private int qrCode;
    private Weapon actualWeapon;
    private Room actualRoom;
    private ArrayList<Clue> givenClues;
    private char[] suspectList;
    private int pNumber;

    public Player(String name, int qrCode, int numberOfThings, int pNumber){
        active=false;
        this.qrCode=qrCode;
        this.name=name;
        this.pNumber = pNumber;
        suspectList = new char[numberOfThings];
        givenClues = new ArrayList<>();
    }

    public Player(){}

    public String getName(){
        return name;
    }

    private void clear(){ //oder doch public? wer fuehrt das spaeter aus?
        actualRoom = null;
        actualWeapon = null;
    }

    public int getQrCode() {
        return qrCode;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public Weapon getActualWeapon() {
        return actualWeapon;
    }

    public void setActualWeapon(Weapon actualWeapon) {
        this.actualWeapon = actualWeapon;
    }
    //TODO ggf removeWeapon() einbauen weapon = null

    public Room getActualRoom() {
        return actualRoom;
    }

    public void setActualRoom(Room room){
        this.actualRoom=room;
    }

    public ArrayList<Clue> getGivenClues() {
        return givenClues;
    }

    public void setGivenClues(Clue clue){ givenClues.add(clue); }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getpNumber(){ return pNumber; }

    public void suspectOnList(int position, char character){ // zu deutsch verdaechtigen auf der Liste
        //different to suspect, as this is only the internal list for that human
        if(position < suspectList.length && (character == 'n' || character == 'y' || character == 'm')) {
            //n = no, y = yes, m = maybe
            suspectList[position] = character;
        }
    }

    public char getSuspectOnList(int position){
        return suspectList[position];
    }


}
