package com.example.jendrik.moerder.GUI.Host;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.jendrik.moerder.FCM.SendToDatabase;
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

import android.databinding.ObservableArrayList;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WaitForPlayers extends Activity {

    private Bundle extras;
    private static Game game;
    private ObservableList.OnListChangedCallback<ObservableList<String>> onListChangedCallback;
    public static ObservableArrayList<String> pNameList = new ObservableArrayList();
    public static TableLayout table;

    private ListView lv;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ValueEventListener el;
    private List<String> playerNames;
    private String gameName;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitscreen);

        extras = getIntent().getExtras();
        game = (Game) extras.get("GAME");
        TextView tvGameName = (TextView) findViewById(R.id.game_name);
        tvGameName.setText(game.getGameName());

        //String ownName = extras.getString(PopUpEnterName.PNAME);
        //pNameList.add(ownName);

       // platzhalterSpielerListeFuellen();

        /*
        Begin neue Version
         */
        gameName = getIntent().getExtras().getString("gameName");
        lv = (ListView) findViewById(R.id.lv_player_wait);
        playerNames = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("games").child(gameName).child("connectedPlayers");
        el = setListener();
        myRef.addValueEventListener(el);



        //makeActivity();
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
                        startActivity(new Intent(WaitForPlayers.this, Startscreen.class));
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

    private void  getUpdate(){

        if(playerNames.size()>0){
            ArrayList<String> lines = new ArrayList<String>();
            for(int i = 0; i < playerNames.size(); i++){
                String line = "QR-Code "+ (i+1) + ": " + playerNames.get(i);
                lines.add(i,line);
            }
            ArrayAdapter adapter = new ArrayAdapter(WaitForPlayers.this,android.R.layout.simple_list_item_1,lines);
            lv.setAdapter(adapter);
        }
        else
            Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT);
    }

    private void platzhalterSpielerListeFuellen(){
        /*
        Erzeugung von DUMMY-Playern --> TODO löschen! UND makePlayerObjects(); in BTN AKTIVIEREN!!!
         */
        ArrayList<String> playerList = new ArrayList<>();

       // for(int i=0; i < extras.getInt(CreateGame.PLAYER_COUNT); i++)
         //   playerList.add("Dummy " + i);

        game.startGame(playerList);

        /*
        Ende der Platzhalterfunktion
         */

        TableLayout table = (TableLayout)findViewById(R.id.player_table);
        fillTable(game.getPlayers(), table);
    }


    private void fillTable(List<Player> players, TableLayout table){
        for(int i=0; i<players.size(); i++){
            TableRow row = new TableRow(this);
            TextView tv = new TextView(this);
            String txt = getResources().getString(R.string.txt_player);
            txt =txt+ " "+(i+1) + ": " + players.get(i).getName();
            tv.setText(txt);
            row.addView(tv);
            table.addView(row);
        }
    }

    /**
     * Fügt dem WaitScreen einen neuen Spieler hinzu
     * @param pName
     */
    public static void addPlayer(String pName){
        pNameList.add(pName);
    }

    /**
     * Gibt dem Gameobject den Auftrag, die Spielerobjekte zu erstellen.
     */
    private void makePlayerObjects(){
        game.startGame(playerNames); }

    private void sendGameStuffToDB(){

        SendToDatabase sendToDatabase = new SendToDatabase(gameName);

        sendToDatabase.sendGame(game);
        /*sendToDatabase.createList("rooms",game.getRooms());
        sendToDatabase.createList("weapons",game.getWeapons());
        sendToDatabase.createList("players",game.getPlayers());
        sendToDatabase.createList("solution",game.getSolution());
        sendToDatabase.createList("clues",game.getClueList());
        sendToDatabase.sendData("isRunning",true);*/
    }



    public void onClickStartGame(View button){
        //TODO folgende Zeile auskommentieren und bool aus if abfrage loeschen oder auf false setzen
        boolean demo = true;
        if(playerNames.size() != game.getPlayerAmount() && !demo){
            int duration = Toast.LENGTH_SHORT;
            CharSequence cs = getText(R.string.popup_not_enough_players).toString();
            Toast toast = Toast.makeText(this, cs , duration);
            toast.show();
        }else {

            makePlayerObjects();
            game.setGameIsRunning(true);
            sendGameStuffToDB();
            myRef.removeEventListener(el);

            //Clients informieren, Spiel zu starten
            final Intent intent = new Intent(this, MenuDrawer.class);
            intent.putExtra("mode",getIntent().getStringExtra("mode"));
            intent.putExtra("gameName", gameName);
            intent.putExtra("GAME", game);
            intent.putExtra("whoAmI", 0);
            intent.putExtra("myTurn", true);

            startActivity(intent);
        }
    }
}
