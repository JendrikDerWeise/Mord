package com.example.jendrik.moerder.FCM;

import android.util.Log;

import com.example.jendrik.moerder.GUI.Join.GameStartedCallback;
import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.GameObjekts.Solution;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FCMListeners {
    private String gameName;
    private Game game=new Game();

    private DatabaseReference gameReference;
    private GameStartedCallback callback;
    private ValueEventListener gameListener;
    private double number;
    private Solution solution;
    private ArrayList<String> rooms=new ArrayList<>();
    private ArrayList<String> weapons=new ArrayList<>();
    private ArrayList<String> players=new ArrayList<>();
    private double min;
    private double sec;

    private DatabaseReference roomListReference;
    private ValueEventListener veRoomList;
    private DatabaseReference weaponListReference;
    private ValueEventListener veWeaponList;
    private DatabaseReference playerListReference;
    private ValueEventListener vePlayerList;

    public FCMListeners(String gameName, GameStartedCallback callback){

        this.gameName = gameName;
        this.callback = callback;
        gameReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        gameListener = makeGameListener();
        gameReference.addListenerForSingleValueEvent(gameListener);
    }



    public ValueEventListener makeGameListener(){
        ValueEventListener ve =
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        updateGame(dataSnapshot);
                        callback.startGameAfterReceivingInformation();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
        return ve;
    }

    private void updateGame(DataSnapshot snapshot){
        Game game=snapshot.getValue(Game.class);
        this.game=game;
    }

    public Game getGame(){
        return game;
    }


    /*public FCMListeners(String gameName, GameStartedCallback callback){
        this.gameName=gameName;
        database = FirebaseDatabase.getInstance();
        /*stringListListener("roomList");
        stringListListener("weaponlist");
        stringListListener("connectedPlayers");
        solutionListener();
        doubleListener("min");
        doubleListener("sec");

        this.callback = callback;

        roomListReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName).child("roomList");
        veRoomList = stringListListener("roomList");
        roomListReference.addListenerForSingleValueEvent(veRoomList);

        weaponListReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName).child("weaponlist");
        veWeaponList = stringListListener("weaponlist");
        weaponListReference.addListenerForSingleValueEvent(veWeaponList);

        playerListReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName).child("connectedPlayers");
        vePlayerList = stringListListener("connectedPlayers");
        playerListReference.addListenerForSingleValueEvent(vePlayerList);

        solutionListener();
        doubleListener("min");
        doubleListener("sec");
    }*/





    private ValueEventListener stringListListener(String list){
        final String listName=list;
        ValueEventListener ve =
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("fuellen","listen listener"+dataSnapshot.getKey());
                        fillList(listName,dataSnapshot);
                        callback.startGameAfterReceivingInformation();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
        return ve;
    }

    private void doubleListener(final String doubleName){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("games").child(gameName).child(doubleName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("fuellen","double listener"+dataSnapshot.getKey());
                        fillDouble(doubleName, dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void fillDouble(String doubleName, DataSnapshot dataSnapshot){
        switch (doubleName){
            case "min":
                min = dataSnapshot.getValue(Double.class);
                break;
            case "sec":
                sec = dataSnapshot.getValue(Double.class);
                break;
        }
    }

    public void  solutionListener(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("games").child(gameName).child("solution").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void fillList(String listName, DataSnapshot dataSnapshot){
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            final String name = ds.getValue(String.class);
            Log.d("fuellen","fill list: " + listName + " "+name);
            switch (listName) {
                case "roomList":
                    rooms.add(name);
                    break;
                case "weaponlist":
                    weapons.add(name);
                    break;
                case "connectedPlayers":
                    players.add(name);
                    break;
            }
        }
    }
}
