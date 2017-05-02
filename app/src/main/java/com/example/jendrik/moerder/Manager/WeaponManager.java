package com.example.jendrik.moerder.Manager;

import com.example.jendrik.moerder.GameObjekts.Weapon;
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
public class WeaponManager implements Serializable {

    public List<Weapon> weaponList;

    public WeaponManager(boolean bool){
        weaponList = new ArrayList<>();
    }

    @Exclude
    public void createWeapon(String name){
        int qrCode = weaponList.size() + 10;
        weaponList.add(new Weapon(name,qrCode));
    }

    @Exclude
    public String getNameByNumber(int qrnr){
        for(Weapon w: weaponList){
            if(w.getQrCode() == qrnr){ return w.getName();}
        }
        return "error";
    }

    @Exclude
    public int getNumberByName(String name){
        for(Weapon r: weaponList){
            if(r.getName() == name){ return (int)r.getQrCode(); }
        }
        return 666;
    }

    public List<Weapon> getWeaponList(){
        return this.weaponList;
    }
}
