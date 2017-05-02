package com.example.Jendrik.myapplication.backend.GameObjekts;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jendrik on 21.02.2016.
 */
public class Room implements Serializable {
    private String name;
    private int qrCode;
    private ArrayList<Player> playerList;
    private ArrayList<Weapon> weaponList;

    public Room(String name, int qrCode) {
        this.name = name;
        this.qrCode = qrCode;
        weaponList = new ArrayList<>();
        playerList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getQrCode() {
        return qrCode;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public ArrayList<Weapon> getWeaponList() {
        return weaponList;
    }

    public void addWeapon(Weapon weapon) {
        weaponList.add(weapon);
    }

    public void removeWeapon(Weapon weapon){
        for(int i = 0; i<weaponList.size();i++){
            if(weaponList.get(i).getName().equals(weapon.getName()))
                weaponList.remove(i);

        }
    }

}
