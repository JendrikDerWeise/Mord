package com.example.jendrik.moerder.GUI.Host;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jendrik.moerder.GUI.Host.AdapterClasses.RoomAdapterClass;
import com.example.jendrik.moerder.GUI.TextFieldHelper;
import com.example.jendrik.moerder.R;

import java.util.ArrayList;
import java.util.List;


public class RoomNameList extends Activity {
  //  public static ArrayList ROOM_LIST = "room list";
    public static List<EditText> roomNames;
    private Bundle extras;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvadapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    /**
     * Die Klasse wurde erstellt, als ich die entsprechende Listenfunktion noch nicht kannte.
     * Daher werden erst die Eingabefelder erstellt und alle nicht benötigten unsichtbar geschaltet.
     * Abhängig davon, wieviele Räume der Spieler eingestellt hat.
     */
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.roomlist_activity);

       extras = getIntent().getExtras();
       makeList();

       final Activity fA = this;
       recyclerView = (RecyclerView) findViewById(R.id.recyclerview_rooms);
       rvLayoutManager = new LinearLayoutManager(fA);
       recyclerView.setLayoutManager(rvLayoutManager);

       rvadapter = new RoomAdapterClass();
       recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
       recyclerView.setFocusable(false);
       recyclerView.setAdapter(rvadapter);

   }

    private void makeList(){
        roomNames = new ArrayList<>();
        for(int i=0; i<extras.getInt(CreateGame.ROOM_COUNT);i++){
            roomNames.add(new EditText(this));
                //roomNames.get(i).setText("DUMMY ROOM " + (i+1)); //TODO löschen bei Endversion
        }
    }

    public void onClickNextButtonR(View button){
        final ArrayList<String> roomList = new ArrayList<>();
        boolean noEmptyFields = true;
        for(int i=0; i < extras.getInt(CreateGame.ROOM_COUNT); i++) {
            if(TextFieldHelper.stringIsEmpty(roomNames.get(i).getText().toString())){
                roomNames.get(i).setError( getText(R.string.error_empty_single_textfield ));
                noEmptyFields = false;
                break;
            }
        }
        boolean noDoubles = true;
        if(noEmptyFields){
            for(int i=0; i < extras.getInt(CreateGame.ROOM_COUNT); i++) {
                roomList.add(roomNames.get(i).getText().toString());
            }
            for(int i=0; i < roomList.size(); i++){
                for(int j=0; j < roomList.size(); j++){
                    if(i != j){
                        if(roomList.get(i).equals(roomList.get(j))&& i < j){
                            roomNames.get(j).setError(getText(R.string.error_popup_room));
                            noDoubles = false;
                            break;
                        }
                    }
                }
            }
        }

        if(noEmptyFields&& noDoubles){
            final Intent intent = new Intent(this, WeaponNameList.class);
            intent.putExtras(extras);
            intent.putExtra("room list", roomList);

            startActivity(intent);
        }

    }


}
