package com.example.jendrik.moerder.GameObjekts;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@IgnoreExtraProperties
public class Player implements Serializable{
    private String name;
    private boolean active;
    private boolean dead;
    private double qrCode;
    private Weapon actualWeapon;
    private Room actualRoom;
    private List<Clue> givenClues;
    private List<String> suspectList;
    private double pNumber;

    public Player(String name, int qrCode, int numberOfThings, int pNumber){
        this.active=false;
        this.qrCode=qrCode;
        this.name=name;
        this.pNumber = pNumber;
        this.suspectList = new ArrayList<>();
        this.givenClues = new ArrayList<>();
        this.dead = false;

        for(int i=0; i<numberOfThings;i++)
            suspectList.add(i,"n");
    }

    private void clear(){ //oder doch public? wer fuehrt das spaeter aus?
        actualRoom = null;
        actualWeapon = null;
    }



    //TODO ggf removeWeapon() einbauen weapon = null

    @Exclude
    public void giveGivenClues(Clue clue){ givenClues.add(clue); }

    @Exclude
    public void suspectOnList(int position, String string){ // zu deutsch verdaechtigen auf der Liste
        //different to suspect, as this is only the internal list for that human
        if(position < suspectList.size() && (string == "n" || string == "y" || string == "m")) {
            //n = no, y = yes, m = maybe
            suspectList.remove(position);
            suspectList.add(position,string);
        }
    }

    @Exclude
    public String getSuspectOnList(int position){
        return suspectList.get(position);
    }


}
