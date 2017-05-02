package com.example.jendrik.moerder.GUI.OnGamingClasses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.GameObjekts.Room;
import com.example.jendrik.moerder.R;

import java.lang.reflect.Field;


public class ChangeRoom extends Fragment {
    private View fragLayoutV;
    public static String SCAN_ROOM = "room";
    private static final int VALUE = 503;
    private Game game;
    private SendToDatabase stb;
    private GameIsRunningCallback callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();

        try {
            this.callback = (GameIsRunningCallback)activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragLayoutV = inflater.inflate(R.layout.fragment_change_room, null);
        return fragLayoutV;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        game = com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game;
        String pNumberString = "" + com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.whoAmI;
        stb = new SendToDatabase(game.getGameName(),pNumberString);
        startRoomScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //hier kommen die Daten des Scanners an. Methode gehört zur Super-Klasse, Name somit fest vorgegeben
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VALUE){
                if (resultCode == Activity.RESULT_OK) { //wenn Activity korrekt zuende geführt wurde
                    //bei QR CodeScanner einkommentieren
                    int qrCode = Integer.parseInt(data.getStringExtra("SCAN_RESULT"));
                    String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                    //bei qr scanner auskommentieren
                    //int qrCode = data.getIntExtra(STUB_SCANNER.RESULT, 0); //Übergabe des Intents (data), dort ist unter dem String RESULT der INT gespeichert... klingt unsinnig, läuft aber so. Die 0 ist Unsinn
                    if(qrCode>19 && qrCode<29){
                        for(Room r : game.getRooms()){
                            if(r.getName().equals(game.getNameByNumber(qrCode))) {
                                removePlayerFromRoom("normalRoom");
                                movePlayerToRoom(r);
                                TextView textView = (TextView)getActivity().findViewById(R.id.change_room_name);
                                textView.setText(r.getName());
                                callback.stopTimer();
                                callback.endTurn();
                            }
                        }
                    }
                    else if(qrCode==29){
                        removePlayerFromRoom("grpRoom");
                        movePlayerToRoom(game.getRoomManager().getGrpRoom());
                        TextView textView = (TextView)getActivity().findViewById(R.id.change_room_name);
                        textView.setText(getActivity().getResources().getText(R.string.grp_room));
                        callback.stopTimer();
                        callback.endTurn();

                    }else{
                        int duration = Toast.LENGTH_SHORT;
                        CharSequence cs = getText(R.string.popup_wrong_qrcode).toString();
                        Toast toast = Toast.makeText(getActivity(), cs , duration);
                        toast.show();
                        startRoomScan();
                    }

                }

        }
    }

    private void startRoomScan(){
        String gameMode = getActivity().getIntent().getStringExtra("mode");
        switch(gameMode){
            case "qr":
                startQrScan();
                break;
            case "nfc":
                startNfcScan();
                break;
        }
    }

    private void startQrScan(){
        //bei QR CodeScanner auskommentieren
        //final Intent intent = new Intent(getActivity(), STUB_SCANNER.class); //Vorbereitung der neuen Activity, STUB SCANNER ist der "QR-Code Leser"
        //final int kindOfObject = 0;
        //intent.putExtra(SCAN_WEAPON,kindOfObject);
        //bei QR CodeScanner einkommentieren
        final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
        final Intent intent = new Intent(ACTION_SCAN);
        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(intent, VALUE); //Starten der Activity. Methodenaufruf "...ForResult" impliziert, das die Activity etwas zurück liefert
        }else{
            String appAddressInMarket = "play.google.com/store/apps/details?id=com.google.zxing.client.android";
            Intent intent_googlePlay = new Intent(Intent.ACTION_VIEW, Uri.parse(appAddressInMarket));
            startActivity(intent_googlePlay);
        }
    }

    private void startNfcScan(){
        final Intent intent = new Intent(getActivity(), RfidScanner.class);
        startActivityForResult(intent, VALUE);
    }

    private void removePlayerFromRoom(String kindOfRoom){
        Room rOldPlayer = game.getActivePlayer().getActualRoom();
        switch (kindOfRoom){
            case "normalRoom":
                for(Room r : game.getRoomManager().getRoomList()){
                    if(r.equals(rOldPlayer))
                        r.getPlayerList().remove(game.getActivePlayer().getName());
                }
                break;
            case "grpRoom":
                game.getGrpRoom().getPlayerList().remove(game.getActivePlayer().getName());
                break;
        }
    }

    private void movePlayerToRoom(Room room){
        game.getActivePlayer().setActualRoom(room);

        if(room.getName().equals("grp_room")) {
            game.getGrpRoom().getPlayerList().add(game.getActivePlayer().getName());

        }else {
            room.getPlayerList().add(game.getActivePlayer().getName());

        }
        stb.updateData("playerList", game.getPlayerManager().getPlayerList().get(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.whoAmI));
        stb.updateData("grpRoom", game.getRoomManager().getGrpRoom());
        stb.updateData("roomList", game.getRoomManager().getRoomList());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



}