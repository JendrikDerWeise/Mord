package com.example.jendrik.moerder.FCM;

import android.util.Log;
import android.widget.EditText;

import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.GameHandler;
import com.example.jendrik.moerder.GameObjekts.Solution;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Setter;


@Setter
public class SendToDatabase<T> {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    private String gameName;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String pNumber;

    public SendToDatabase(String gameName) {
        this.gameName = gameName;
    }

    public SendToDatabase(String gameName, String pNumber) {
        this.gameName = gameName;
        this.pNumber = pNumber;
    }


    public void sendData(String typOfObject, T object) {
        DatabaseReference myRef = database.getReference();
        myRef.child("games").child(gameName).child(typOfObject).push();

        Map<String, Object>update = new HashMap<>();
        update.put("games/"+gameName+"/"+typOfObject, object);
        myRef.updateChildren(update);
    }

    public void updateData(String typOfObject, T object){
        DatabaseReference myRef = database.getReference();
        //myRef.child("games").child(gameName);

        switch (typOfObject){
            case "roomList":
                myRef.child("games").child(gameName).child("roomManager").child(typOfObject).setValue(object);
                break;
            case "playerList":
                myRef.child("games").child(gameName).child("playerManager").child("playerList").child(pNumber).setValue(object);
                break;
            case "completePlayerList":
                myRef.child("games").child(gameName).child("playerManager").child("playerList").setValue(object);
                break;
            case "paused":
                myRef.child("games").child(gameName).child(typOfObject).setValue(object);
                break;
            case "aktivePlayer":
                myRef.child("games").child(gameName).child("playerManager").child(typOfObject).setValue(object);
                break;
            case "playerManager":
                myRef.child("games").child(gameName).child(typOfObject).setValue(object);
                break;
            case "grpRoom":
                myRef.child("games").child(gameName).child("roomManager").child("grpRoom").setValue(object);
                break;
            case "prosecutionNotify":
                myRef.child("games").child(gameName).child(typOfObject).setValue(object);
                break;
            case "prosecutionIsPlaced":
                myRef.child("games").child(gameName).child(typOfObject).setValue(object);
                break;
            case "suspectionNotify":
                myRef.child("games").child(gameName).child(typOfObject).setValue(object);
                break;
            case "suspectionObject":
                myRef.child("games").child(gameName).child(typOfObject).setValue(object);
                break;
            case "playerCalled":
                myRef.child("games").child(gameName).child("suspectionObject").child(typOfObject).setValue(object);
                break;
            case "suspectionNextPlayer":
                myRef.child("games").child(gameName).child("suspectionObject").child(typOfObject).setValue(object);
                break;
        }
    }

    public void sendGame(Game game){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("games").child(gameName).setValue(game);
    }

    public void createGame(){
        DatabaseReference myRef = database.getReference();
        myRef.child("games").child(gameName).push();
    }

    public void deleteGame(){
        DatabaseReference myRef = database.getReference();
        myRef.child("games").child(gameName).removeValue();
    }
}
