package com.example.jendrik.moerder.GUI.Join;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jendrik.moerder.FCM.FCMListeners;
import com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer;
import com.example.jendrik.moerder.GUI.Startscreen;
import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.GameObjekts.Player;
import com.example.jendrik.moerder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WaitForServer extends Activity implements GameStartedCallback{

    private String gameName;
    private ListView lv;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference gamestarter;
    private ValueEventListener el;
    private ValueEventListener elGame;
    private List<String> playerNames;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitscreen);
        Button startBtn = (Button) findViewById(R.id.button5);
        startBtn.setVisibility(View.INVISIBLE);

        TextView tvGameName = (TextView) findViewById(R.id.game_name);

        gameName = getIntent().getExtras().getString("gameName");
        tvGameName.setText(gameName);
        lv = (ListView) findViewById(R.id.lv_player_wait);
        playerNames = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("games").child(gameName).child("connectedPlayers");
        el = setListener();
        myRef.addValueEventListener(el);

        gamestarter = FirebaseDatabase.getInstance().getReference().child("games").child(gameName).child("running");
        elGame = setGameStartedListener();
        gamestarter.addValueEventListener(elGame);

    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.mordTheme));
        builder.setMessage(R.string.popup_back_message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.popup_back_positive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(
                R.string.popup_back_negative_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(WaitForServer.this, Startscreen.class));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private ValueEventListener setListener(){

        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                playerNames.clear();
                for(DataSnapshot sn:dataSnapshot.getChildren()) {
                    String name = sn.getValue(String.class);
                    playerNames.add(name);
                }
                getUpdate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    private ValueEventListener setGameStartedListener(){

        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO irgendeine wait-Schleife bauen, damit es nicht zu überschneidungen kommt
                playerNames.clear();
                boolean isRunning = dataSnapshot.getValue(Boolean.class);
                if(isRunning)
                    startGame();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }


    private void getUpdate(){

        if(playerNames.size()>0){
            ArrayList<String> lines = new ArrayList<String>();
            for(int i = 0; i < playerNames.size(); i++){
                String line = "QR-Code "+ (i+1) + ": " + playerNames.get(i);
                lines.add(i,line);
            }
            ArrayAdapter adapter = new ArrayAdapter(WaitForServer.this,android.R.layout.simple_list_item_1,lines);
            lv.setAdapter(adapter);
        }
        else
            Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT);
    }

    private FCMListeners fcmListeners;

    private void startGame(){
        myRef.removeEventListener(el);
        gamestarter.removeEventListener(elGame);

        fcmListeners = new FCMListeners(gameName, this);
    }

    public void startGameAfterReceivingInformation(){
        Intent intent = new Intent(this,MenuDrawer.class);
        intent.putExtra("mode",getIntent().getStringExtra("mode"));
        intent.putExtra("gameName", gameName);
        intent.putExtra("myTurn", false);

        Game game = fcmListeners.getGame();
        intent.putExtra("whoAmI", checkForPlayerNumber(game));
        intent.putExtra("GAME", game);

        startActivity(intent);
    }

    private int checkForPlayerNumber(Game game) {
        String pName = getIntent().getExtras().getString("pName");
        int whoAmI=0;
        for(Player p : game.getPlayerManager().getPlayerList()){
            if(p.getName().equals(pName))
                whoAmI = (int)p.getPNumber();
        }

        Log.d("aktivePlayerChanged", "WaitForServer pNumber: "+whoAmI);

        return whoAmI;
    }
}
