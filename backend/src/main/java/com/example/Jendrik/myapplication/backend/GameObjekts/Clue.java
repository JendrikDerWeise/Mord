package com.example.Jendrik.myapplication.backend.GameObjekts;

import java.io.Serializable;

/**
 * Created by Jendrik on 21.02.2016.
 */
public class Clue implements Serializable {

    private String name;
    private int id; //0=player, 1=room, 2=weapon --> für Bildzuweisung

    public Clue(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int id() {
        return id;
    }

    public String getName() {
        return name;
    }
}
