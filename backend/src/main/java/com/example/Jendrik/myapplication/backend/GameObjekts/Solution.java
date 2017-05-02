package com.example.Jendrik.myapplication.backend.GameObjekts;

import java.io.Serializable;

/**
 * Created by Jendrik on 22.02.2016.
 */
public class Solution implements Serializable {

    private String murderer, weapon, room;

    public Solution(String m, String r, String w){
        murderer=m;
        weapon=w;
        room=r;
    }

    public String getMurderer() {
        return murderer;
    }

    public String getWeapon() {
        return weapon;
    }

    public String getRoom() {
        return room;
    }
}
