package com.example.Jendrik.myapplication.backend.Manager;



import com.example.Jendrik.myapplication.backend.GameObjekts.Clue;
import com.example.Jendrik.myapplication.backend.GameObjekts.Player;
import com.example.Jendrik.myapplication.backend.GameObjekts.Room;
import com.example.Jendrik.myapplication.backend.GameObjekts.Weapon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jendrik on 21.02.2016.
 */
public class PlayerManager implements Serializable {



    private ArrayList<Player> playerList;

    public PlayerManager(){
        playerList = new ArrayList<>();
    }

    public void addPlayer(String name, int numberOfThings){
        int qrCode = playerList.size()+1;
        playerList.add(new Player(name,qrCode, numberOfThings, playerList.size()));
    }

    public void setActive(String name) {
        for (Player p : playerList) {
            if (p.getName().equals(name))
                p.setActive(true);
            else
                p.setActive(false);
        }
    }

    public void giveClue(Clue clue, int playerNo){ //ich bin mir unsicher wo die Karten verteilt werden. Da muss jedenfalls eine Rechnung hin, damit alle Spieler die gleiche Menge Karten bekommen
        playerList.get(playerNo).setGivenClues(clue);
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public String getNameByNumber(int qrnr){
        for(Player p:playerList){
            if(p.getQrCode() == qrnr){ return p.getName();}
        }
        return "error";
    }

    public void setSuspectList(ArrayList<Room> rooms, ArrayList<Weapon> weapons){

        for(Player p: playerList){
            int i=0;
            for(Player x: playerList){
                p.suspectOnList(i,'n');
                i++;
            }
            for(Room r : rooms){
                p.suspectOnList(i,'n');
                i++;
            }
            for(Weapon w : weapons){
                p.suspectOnList(i,'n');
                i++;
            }
        }
    }

    public void updatePlayer(Player player){
        for(int i = 0; i  < playerList.size(); i++){
            if(playerList.get(i).getQrCode() == player.getQrCode()){
                playerList.remove(i);
                playerList.set(i, player); //TODO Stimmt die reihenfolge?
            }
        }
    }
}
