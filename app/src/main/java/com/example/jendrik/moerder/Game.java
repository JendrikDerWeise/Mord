package com.example.jendrik.moerder;

import android.util.Log;

import com.example.jendrik.moerder.GameObjekts.Clue;
import com.example.jendrik.moerder.GameObjekts.Player;
import com.example.jendrik.moerder.GameObjekts.Room;
import com.example.jendrik.moerder.GameObjekts.Solution;
import com.example.jendrik.moerder.GameObjekts.Weapon;
import com.example.jendrik.moerder.Manager.PlayerManager;
import com.example.jendrik.moerder.Manager.RoomManager;
import com.example.jendrik.moerder.Manager.WeaponManager;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Jendrik on 22.02.2016.
 *
 * Hier landet die Spielmechannik. Klassen für hosten und suchen kommen extra
 *
 */
@Getter @Setter
@NoArgsConstructor
@IgnoreExtraProperties
public class Game implements Serializable {

    private Solution solution;
    private RoomManager roomManager;
    private WeaponManager weaponManager;
    private PlayerManager playerManager;
    private List<Clue> clueList;
    private double numberOfThings;
    private String gameName;
    private String pwd;
    private double min;
    private double sec;
    private double justScannedQR;
    private double playerAmount;
    private boolean gameOver;
    private boolean isRunning;
    private boolean paused;
    private boolean prosecutionNotify;
    private boolean suspectionNotify;

    public Game(String gameName, String pwd, ArrayList<String> rooms, ArrayList<String> weapons, int min, int sec, int playerAmount){
        this.gameName = gameName;
        this.pwd = pwd;
        this.min = min;
        this.sec = sec;
        this.solution = null;
        this.clueList = new ArrayList<>();
        this.playerManager = new PlayerManager(false);
        this.weaponManager = new WeaponManager(false);
        this.roomManager = new RoomManager(false);
        createRooms(rooms);
        createWeapons(weapons);
        this.numberOfThings = rooms.size() + weapons.size();
        this.justScannedQR = 0;
        this.playerAmount = playerAmount;
        this.gameOver = false;
        this.paused = false;
        this.prosecutionNotify = false;
        this.suspectionNotify = false;

    }

    /**
    *Methode soll prüfen ob die Anklage des Spielers korrekt ist.
    *Gibt true zurück, wenn dies der Fall ist.
     */
    @Exclude
    public boolean compareSolution(String murderer, String room, String weapon){
        return solution != null && solution.getMurderer().equals(murderer) && solution.getRoom().equals(room) && solution.getWeapon().equals(weapon);
    }

    @Exclude
    public Player getActivePlayer(){
        Player player = new Player();
        for(Player p : playerManager.getPlayerList()){
            if(p.isActive())
                player = p;
        }
        return player;
    }

    /**
    *Setzt eine Liste aus Clue-Objekten zusammen.
     */
    @Exclude
    private void createClues(){
        for (Player p:playerManager.getPlayerList())
            clueList.add(new Clue(p.getName(), 0.0));

        for (Room r:roomManager.getRoomList())
            clueList.add(new Clue(r.getName(),1.0));

        for (Weapon w:weaponManager.getWeaponList())
            clueList.add(new Clue(w.getName(), 2.0));

        createSolution();
    }

    /**
    *Füllt ein String-Array mit den Namen aller bisher angemeldeten Spieler und setzt sie in den Gruppenraum (Startposition).
    *Zum Ende wird Spieler 0 als aktiv markiert.
     */
    @Exclude
    private void createPlayer(List<String> players){
        numberOfThings += players.size();
        for(String s:players) {
            playerManager.addPlayer(s,(int) numberOfThings);
            playerManager.getPlayerList().get(playerManager.getPlayerList().size()-1).setActualRoom(roomManager.getGrpRoom());
            //roomManager.getGrpRoom().getPlayerList().add(playerManager.getPlayerList().get(playerManager.getPlayerList().size()-1));
        }
        playerManager.getPlayerList().get(0).setActive(true);  //first Player has to be active for starting the game

    }

    /**
    *Hat Sophia geschrieben
     */
    @Exclude
    private void createSolution(){
        int playerCount = playerManager.getPlayerList().size();
        int roomCount = roomManager.getRoomList().size();
        int weaponCount = weaponManager.getWeaponList().size();
        Random random = new Random();
        String pName = playerManager.getPlayerList().get(random.nextInt(playerCount)).getName();
        String rName = roomManager.getRoomList().get(random.nextInt(roomCount)).getName();
        String wName = weaponManager.getWeaponList().get(random.nextInt(weaponCount)).getName();
        solution = new Solution(pName, rName,wName);
        Log.e("SOLUTION", solution.getMurderer() + solution.getRoom() + solution.getWeapon());
    }

    @Exclude
    private void createRooms(ArrayList<String> rooms){
        for (String s:rooms)
            roomManager.createRoom(s);
    }

    @Exclude
    private void createWeapons(ArrayList<String> weapons){
        for (String s:weapons)
            weaponManager.createWeapon(s);

        int room = 0;
        for (int i = 0; i < weaponManager.getWeaponList().size();i++){ //Zuteilung der Waffen auf die Räume. Vorraussetzung:  Anzahl Waffen <=> Anzahl Räume
            if(!(room < roomManager.getRoomList().size())){
                room = 0;
            }
            roomManager.getRoomList().get(room).addWeapon(weaponManager.getWeaponList().get(i));
            room++;
        }
    }

    @Exclude
    public Room getGrpRoom(){
        return roomManager.getGrpRoom(); }

    @Exclude
    public double getPlayerAmount(){
        return this.playerAmount;
    }

    @Exclude
    public List<Room> getRooms(){
        return roomManager.getRoomList();
    }

    @Exclude
    public List<Weapon> getWeapons(){
        return weaponManager.getWeaponList();
    }

    @Exclude
    public List<Player> getPlayers(){
        return playerManager.getPlayerList();
    }

    @Exclude
    public boolean passwordSecured(){
        return pwd != "";
    }

    @Exclude
    public boolean checkPwd(String pwd){
        return this.pwd == pwd;
    }

    /**
    *Methode gibt zu QR-Code zugehörigen Namen zurück. Kann dabei Spieler, Raum oder Waffe sein.
     */
    @Exclude
    public String getNameByNumber(int qrnr){
        if(qrnr > 0  && qrnr < 30){
            if(qrnr < 10) {
                return playerManager.getNameByNumber(qrnr);
            }else if(qrnr < 20){
                return weaponManager.getNameByNumber(qrnr);
            }else {
                return roomManager.getNameByNumber(qrnr);
            }
        }else{
            return "error";
        }
    }

    @Exclude
    public int getNumberByName(String name){
        int qrCode=playerManager.getNumberByName(name);
        if(qrCode == 666){
            qrCode = weaponManager.getNumberByName(name);
                if(qrCode == 666){
                    return roomManager.getNumberByName(name);
                }else{
                    return qrCode;
                }
        }else{
            return qrCode;
        }
    }



    /**
    *Verteilung der Spielkarten (Hinweise) an die Spieler. (Sophia)
     */
    @Exclude
    private void giveCluesToPlayer(){
        ArrayList<Clue> copyClueList = new ArrayList<Clue>();//duplicates Cardlist
        for(int i = 0; i < clueList.size(); i++){
            copyClueList.add(i, clueList.get(i));
            Log.d("COPYCLUELIST", clueList.get(i).getName());
        }

        int playerCount = playerManager.getPlayerList().size();
        Random random = new Random();
        int cluePosition = 0;
        boolean given = false;
        while(!copyClueList.isEmpty()){ //until copied List is not Empty
            //Log.d("ERSTE", "hier");
            for(int i = 0; i < playerCount; i++){
                given = false;
                while(!given) {
                    if(copyClueList.size() !=0)
                        cluePosition = random.nextInt(copyClueList.size());
                    else
                        break;
                    if (copyClueList.get(cluePosition).getName() == solution.getMurderer() ||
                            copyClueList.get(cluePosition).getName() == solution.getWeapon() ||
                            copyClueList.get(cluePosition).getName() == solution.getRoom()) {
                        //when the clue is part of the solution
                        //clue is deleted from Copied List
                        copyClueList.remove(cluePosition);
                        //loop repeats
                    }else if(!copyClueList.isEmpty()) { //if card is not part of solution
                        //clue is given
                        playerManager.giveClue(copyClueList.get(cluePosition), i);
                        given = true;
                        //Clue is removed from copied List
                        copyClueList.remove(cluePosition);

                    }
                }
            }
        }

    }

    @Exclude
    public void killPlayer(int pNumber){
        playerManager.getPlayerList().get(pNumber).setDead(true);
    }

    @Exclude
    public void setJustScannedQR(int qrnr){
        justScannedQR = qrnr;
    }

    @Exclude
    public void setNextActivePlayer(){
        for(Player p : playerManager.getPlayerList()){
            if(p.isActive()) {
                playerManager.setActive(p);
                break;
            }
        }
    }

    /**
    *Methode stupst das Spiel los. Erst wenn alle Daten vorliegen (Spieler, Räume, Waffen) kann die Methode sinnvoll
    *verwendet werden. Die Reihenfolge muss so bestehn bleiben - glaub ich.
     */
    @Exclude
    public void startGame(List<String> players){
        createPlayer(players);
        createClues();
        giveCluesToPlayer();
        playerManager.setSuspectList(getRooms(),getWeapons());

        for(Player p : playerManager.getPlayerList())
            roomManager.getGrpRoom().getPlayerList().add(p.getName());
        //Spiel Speichern über GUI
        //Auslöser zum Senden des Savegames -->gehört in ServerClass
    }

    @Exclude
    public void updatePlayer(Player player){
        playerManager.updatePlayer(player);
    }

    public void setGameIsRunning(boolean gameIsRunning) {
        this.isRunning = gameIsRunning;
    }

    /**
     * Viele Getter fuer Firebase-Gedoens
     */

}
