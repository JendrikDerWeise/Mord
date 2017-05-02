package com.example.jendrik.moerder.Manager;

import com.example.jendrik.moerder.GameObjekts.Room;
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
public class RoomManager implements Serializable{
    private List<Room> roomList;
    private Room grpRoom;

    public RoomManager(boolean bool){
        roomList = new ArrayList<>();
        grpRoom = new Room("grp_room", 29);
    }

    @Exclude
    public void createRoom(String name){
        int qrCode=roomList.size() + 20;
        roomList.add(new Room(name,qrCode));
    }

    @Exclude
    public String getNameByNumber(int qrnr){
        for(Room r: roomList){
            if(r.getQrCode() == qrnr){ return r.getName(); }
        }
        return "error";
    }

    @Exclude
    public int getNumberByName(String name){
        for(Room r: roomList){
            if(r.getName() == name){ return (int)r.getQrCode(); }
        }
        return 666;
    }

    public List<Room> getRoomList(){
        return this.roomList;
    }

}
