package com.example.jendrik.moerder.GUI.OnGamingClasses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.GameObjekts.Room;
import com.example.jendrik.moerder.GameObjekts.Weapon;
import com.example.jendrik.moerder.R;

import java.lang.reflect.Field;

public class ChangeWeapon extends Fragment{
    public static String SCAN_WEAPON = "weapon";
    private static final int VALUE = 503;
    private Game game;
    private String actualRoom;
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

        View fragLayoutV = inflater.inflate(R.layout.stub_activity, null);
        game = com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game;
        actualRoom = com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getActivePlayer().getActualRoom().getName();

        String pNumberString = "" + com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.whoAmI;
        stb = new SendToDatabase(game.getGameName(),pNumberString);

        return fragLayoutV;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        startWeaponScan();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //hier kommen die Daten des Scanners an. Methode gehört zur Super-Klasse, Name somit fest vorgegeben
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == VALUE){
                Room room = new Room("",88);
                if (resultCode == Activity.RESULT_OK) { //wenn Activity korrekt zuende geführt wurde
                    //bei QR CodeScanner einkommentieren
                    int qrCode = Integer.parseInt(data.getStringExtra("SCAN_RESULT"));
                    //String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                    //bei qr scanner auskommentieren
                    //int qrCode = data.getIntExtra(STUB_SCANNER.RESULT, 0); //Übergabe des Intents (data), dort ist unter dem String RESULT der INT gespeichert... klingt unsinnig, läuft aber so. Die 0 ist Unsinn
                    if(qrCode>9 && qrCode<20){
                        for(Room r : MenuDrawer.game.getRooms()){
                            if(r.getName().equals(actualRoom)) {
                                if(game.getPlayerManager().getPlayerList().get(MenuDrawer.whoAmI).getActualWeapon()!= null)
                                    r.addWeapon(game.getPlayerManager().getPlayerList().get(MenuDrawer.whoAmI).getActualWeapon());
                                room = r;
                            }
                        }
                        //MenuDrawer.game.getActivePlayer().getActualRoom().addWeapon(MenuDrawer.game.getActivePlayer().getActualWeapon());

                        String weapon;
                        weapon = MenuDrawer.game.getNameByNumber(qrCode);
                        for (Weapon w : MenuDrawer.game.getWeapons()){
                            if(w.getName().equals(weapon)) {
                                MenuDrawer.game.getActivePlayer().setActualWeapon(w);
                                stb.updateData("playerList", game.getPlayerManager().getPlayerList().get(MenuDrawer.whoAmI));
                                room.removeWeapon(w);
                            }
                        }
                        endTurn();

                    }else{
                        int duration = Toast.LENGTH_SHORT;
                        CharSequence cs = getText(R.string.popup_wrong_qrcode).toString();
                        Toast toast = Toast.makeText(getActivity(), cs , duration);
                        toast.show();
                        startWeaponScan();
                    }

                }

        }
    }

    private void startWeaponScan(){
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

    private void endTurn(){
        getActivity().getIntent().putExtra("myTurn", false);
        getActivity().getIntent().putExtra("GAME", com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game);

        callback.stopTimer();
        callback.endTurn();
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