package com.example.jendrik.moerder.GameObjekts;

import android.media.Image;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@IgnoreExtraProperties
public class Clue implements Serializable {

    private String name;
    private Double id; //0=player, 1=room, 2=weapon --> für Bildzuweisung

    public Clue(String name, Double id) {
        this.name = name;
        this.id = id;
    }

}
