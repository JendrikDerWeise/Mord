package com.example.Jendrik.myapplication.backend.GameObjekts;

import java.io.Serializable;

/**
 * Created by Jendrik on 21.02.2016.
 */
public class Weapon implements Serializable {

    private String name;
    private int qrCode;

    public Weapon(String name, int qrCode){
        this.name=name;
        this.qrCode=qrCode;
    }

    public String getName(){
        return name;
    }

    public int getQrCode() {
        return qrCode;
    }

}
