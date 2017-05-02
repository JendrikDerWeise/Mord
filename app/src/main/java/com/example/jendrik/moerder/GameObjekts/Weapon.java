package com.example.jendrik.moerder.GameObjekts;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IgnoreExtraProperties
public class Weapon implements Serializable {

    private String name;
    private double qrCode;

    public Weapon(String name, int qrCode){
        this.name=name;
        this.qrCode=qrCode;
    }
}
