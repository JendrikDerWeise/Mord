package com.example.jendrik.moerder.FCM;

import android.util.Log;

import com.example.jendrik.moerder.GUI.LittleHelpers.SuspectionHelpers.Suspection;
import com.example.jendrik.moerder.GUI.OnGamingClasses.GameIsRunningCallback;
import com.example.jendrik.moerder.GameObjekts.Player;
import com.example.jendrik.moerder.GameObjekts.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FCMRunningGameListener {

    private String gameName;
    private GameIsRunningCallback callback;
    private DatabaseReference roomReference;
    private DatabaseReference playerReference;
    private ValueEventListener roomListener;
    private ValueEventListener playerListener;
    private DatabaseReference activePlayerReference;
    private ValueEventListener activePlayerListener;
    private DatabaseReference pauseReference;
    private ValueEventListener pauseListener;
    private DatabaseReference prosecutionNotifyReference;
    private ValueEventListener prosecutionNotifyListener;
    private DatabaseReference suspectionNotifyReference;
    private ValueEventListener suspectionNotifyListener;
    private DatabaseReference suspectionObjectReference;
    private ValueEventListener suspectionObjectListener;

    public FCMRunningGameListener(String gameName, GameIsRunningCallback callback){
        this.gameName = gameName;
        this.callback = callback;
    }

    public void roomListListener(){
        roomListener = makeRoomListener();
        roomReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        roomReference.child("roomManager").child("roomList").addValueEventListener(roomListener);
    }

    private ValueEventListener makeRoomListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Room>> t = new GenericTypeIndicator<List<Room>>() {};
                List<Room> roomList = dataSnapshot.getValue(t);
                callback.roomListChanged(roomList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    public void playerListListener(){
        playerListener = makePlayerListener();
        playerReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        playerReference.child("playerManager").child("playerList").addValueEventListener(playerListener);
    }

    private ValueEventListener makePlayerListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Player>> t = new GenericTypeIndicator<List<Player>>() {};
                List<Player> playerList = null;
                try{
                    playerList = dataSnapshot.getValue(t);
                }catch (NullPointerException e){

                }
                callback.playerListChanged(playerList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    public void pauseListener(){
        pauseListener = makePauseListener();
        pauseReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        pauseReference.child("paused").addValueEventListener(pauseListener);
    }

    private ValueEventListener makePauseListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean pause = false;
                try {
                    pause = dataSnapshot.getValue(Boolean.class);
                }catch (NullPointerException e){

                }
                callback.pauseIsPressed(pause);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    public void activePlayerListener(){
        activePlayerListener = makeAktivePlayerListener();
        activePlayerReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        activePlayerReference.child("playerManager").child("aktivePlayer").addValueEventListener(activePlayerListener);
    }

    private ValueEventListener makeAktivePlayerListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double aktivePlayer = 0.0;
                try {
                    aktivePlayer = dataSnapshot.getValue(Double.class);
                }catch (NullPointerException e) {
                }
                callback.aktivePlayerChanged(aktivePlayer);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    public void prosecutionNotifyListener(){
        prosecutionNotifyListener = makeProsecutionNotifyListener();
        prosecutionNotifyReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        prosecutionNotifyReference.child("prosecutionNotify").addValueEventListener(prosecutionNotifyListener);
    }

    private ValueEventListener makeProsecutionNotifyListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean prosecutionNotify = false;
                try {
                    prosecutionNotify = dataSnapshot.getValue(Boolean.class);
                }catch (NullPointerException e){

                }
                if(prosecutionNotify)
                    callback.prosecutionNotify();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    public void suspectionNotifyListener(){
        suspectionNotifyListener = makeSuspectionNotifyListener();
        suspectionNotifyReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        suspectionNotifyReference.child("suspectionNotify").addValueEventListener(suspectionNotifyListener);
    }

    private ValueEventListener makeSuspectionNotifyListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean suspectionNotify = false;
                try {
                    suspectionNotify = dataSnapshot.getValue(Boolean.class);
                }catch (NullPointerException e){

                }
                if(suspectionNotify)
                    suspectionObjectListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    public void suspectionObjectListener(){
        suspectionObjectListener = makeSuspectionObjectListener();
        suspectionObjectReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        suspectionObjectReference.child("suspectionObject").addValueEventListener(suspectionObjectListener);
    }

    private ValueEventListener makeSuspectionObjectListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Suspection suspection = null;
                try {
                    suspection = dataSnapshot.getValue(Suspection.class);
                }catch (NullPointerException e){
                    Log.e("susp", "problem!" );
                    e.printStackTrace();
                }
                if(suspection!=null)
                    callback.suspectionNotify(suspection);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    public void unbindListeners(){
        roomReference.removeEventListener(roomListener);
        playerReference.removeEventListener(playerListener);
        pauseReference.removeEventListener(pauseListener);
        activePlayerReference.removeEventListener(activePlayerListener);
        prosecutionNotifyReference.removeEventListener(prosecutionNotifyListener);
        suspectionNotifyReference.removeEventListener(suspectionNotifyListener);
    }

    public void unbindSuspectionListeners(){
        suspectionObjectReference.removeEventListener(suspectionObjectListener);
    }
}
