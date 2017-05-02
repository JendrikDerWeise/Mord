package com.example.jendrik.moerder.GUI.Host;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.GUI.Host.AdapterClasses.WeaponAdapterClass;
import com.example.jendrik.moerder.GUI.TextFieldHelper;
import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.R;

import java.util.ArrayList;
import java.util.List;

public class WeaponNameList extends Activity {

    public static final String WEAPON_LIST = "weapon list";
    public static List<EditText> weaponNames;
    private Bundle extras;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvadapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    /**
     * Es gilt das gleiche wie bei RoomNameList
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weaponlist_activity);

        extras = getIntent().getExtras();

        makeList();
        final Activity fA = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_weapons);
        rvLayoutManager = new LinearLayoutManager(fA);
        recyclerView.setLayoutManager(rvLayoutManager);

        rvadapter = new WeaponAdapterClass();
        recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(rvadapter);

    }

    private void makeList() {
        weaponNames = new ArrayList<>();
        for (int i = 0; i < extras.getInt(CreateGame.WEAPON_COUNT); i++) {
            weaponNames.add(new EditText(this));
            //weaponNames.get(i).setText("DUMMY WEAPON " + (i+1));
        }
    }

    public void onClickNextButtonW(View button){
        String gameName = extras.getString(CreateGame.NAME, "Game 1");
        String pass = extras.getString(CreateGame.PASS, "none");
        final ArrayList<String> weaponList = new ArrayList<>();

        boolean noEmptyFields = true;
        for(int i=0; i < extras.getInt(CreateGame.WEAPON_COUNT); i++){
            if(TextFieldHelper.stringIsEmpty(weaponNames.get(i).getText().toString())){
                weaponNames.get(i).setError(getText(R.string.error_empty_single_textfield));
                noEmptyFields = false;
                break;
            }
        }
        boolean noDoubles = true;

        if(noEmptyFields){
            for(int i=0; i < extras.getInt(CreateGame.WEAPON_COUNT); i++) {
                weaponList.add(weaponNames.get(i).getText().toString());
            }
            for(int i=0; i < weaponList.size(); i++){
                for(int j=0; j < weaponList.size(); j++){
                    if(i != j){
                        if(weaponList.get(i).equals(weaponList.get(j)) && i < j){
                            weaponNames.get(j).setError(getText(R.string.error_popup_weapon));
                            noDoubles = false;
                            break;
                        }
                    }
                }
            }
        }

        if(noEmptyFields&& noDoubles){

            final Intent intent = new Intent(this, GivenQrCodes.class);
            intent.putExtras(extras);

            int min = extras.getInt(CreateGame.COUNTER_MIN);
            int sec = extras.getInt(CreateGame.COUNTER_SEC);
            int playerAmount = extras.getInt(CreateGame.PLAYER_COUNT);
            ArrayList<String> roomList = extras.getStringArrayList("room list");
            Game game = new Game(gameName, pass, roomList, weaponList, min, sec, playerAmount);
            intent.putExtra("GAME", game);

            boolean isSecret = extras.getBoolean(CreateGame.SECRET_CHECKED);
            SendToDatabase sendToDatabase = new SendToDatabase(gameName);
            sendToDatabase.createGame();
            sendToDatabase.sendData("isSecret", isSecret);
            sendToDatabase.sendData("countP",game.getPlayerAmount());
            sendToDatabase.sendData("countR", (double)game.getRooms().size());
            sendToDatabase.sendData("min",game.getMin());
            sendToDatabase.sendData("sec",game.getSec());
            if(isSecret)
                sendToDatabase.sendData("pass", game.getPwd());

            sendToDatabase.sendData("roomList", extras.getStringArrayList("room list"));
            sendToDatabase.sendData("weaponlist",weaponList);
            List<String> connectedPlayers = new ArrayList<>();
            connectedPlayers.add("DUMMY");
            sendToDatabase.sendData("connectedPlayers", connectedPlayers);
            sendToDatabase.sendData("running", false);

            intent.putExtra("host", true);
            //GameHandler.saveGame(game);
            startActivity(intent);

        }
    }
}
