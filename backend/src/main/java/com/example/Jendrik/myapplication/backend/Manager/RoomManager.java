package com.example.Jendrik.myapplication.backend.Manager;


import com.example.Jendrik.myapplication.backend.GameObjekts.Room;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jendrik on 21.02.2016.
 */
public class RoomManager implements Serializable {
    private ArrayList<Room> roomList;
    private Room grpRoom;

    public RoomManager(){
        roomList = new ArrayList<>();
        grpRoom = new Room("grp_room", 29);
    }

    public void createRoom(String name){
        int qrCode=roomList.size() + 20;
        roomList.add(new Room(name,qrCode));
    }

    public ArrayList<Room> showMap(){
        return roomList;
    }

    public String getNameByNumber(int qrnr){
        for(Room r: roomList){
            if(r.getQrCode() == qrnr){ return r.getName(); }
        }
        return "error";
    }

    public Room getGrpRoom(){
        return grpRoom;
    }

}
