package com.example.jendrik.moerder.GUI.LittleHelpers.ProsecutionHelpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer;
import com.example.jendrik.moerder.GameObjekts.Player;
import com.example.jendrik.moerder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProsecutionWaitingForPlayers extends Activity {
    public static String RESULT = "SCAN_RESULT";
    private DatabaseReference playerReference;
    private ValueEventListener playerListener;
    private String gameName;
    private int playersInGrpRoom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prosecution_waiting_for_players_fragment);

        gameName = MenuDrawer.game.getGameName();
        playersInGrpRoom = 0;

        playerListListener();
    }

    private void checkPlayersInGrpRoom(){
        playersInGrpRoom = 0;

        for(Player p : MenuDrawer.game.getPlayerManager().getPlayerList()){
            if(p.getActualRoom().getName().equals("grp_room"))
                playersInGrpRoom ++;
        }

        if(playersInGrpRoom == MenuDrawer.game.getPlayerManager().getPlayerList().size()) {
            playerReference.removeEventListener(playerListener);
            boolean allPlayersAtGrpRoom = true;
            Intent resultBool = new Intent(); //Übergabedatei (Intent) bereitmachen
            resultBool.putExtra(RESULT,allPlayersAtGrpRoom);//Intent mit Daten füllen
            setResult(Activity.RESULT_OK, resultBool);//RESULT festlegen, Intent mit dazu...

            finish();//Activity beenden
        }
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
                checkPlayersInGrpRoom();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }
}
