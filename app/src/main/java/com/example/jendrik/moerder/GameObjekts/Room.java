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
public class Room implements Serializable {
    private String name;
    private double qrCode;
    private List<String> playerList;
    private List<Weapon> weaponList;


    public Room(String name, int qrCode) {
        this.name = name;
        this.qrCode = qrCode;
        weaponList = new ArrayList<>();
        playerList = new ArrayList<>();
        playerList.add(" ");
    }


    @Exclude
    public void addWeapon(Weapon weapon) {
        weaponList.add(weapon);
    }

    @Exclude
    public void removeWeapon(Weapon weapon){
        for(int i = 0; i<weaponList.size();i++){
            if(weaponList.get(i).getName().equals(weapon.getName()))
                weaponList.remove(i);
        }
    }


}
