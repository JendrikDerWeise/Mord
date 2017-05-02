package com.example.jendrik.moerder.GUI.Host;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jendrik.moerder.GUI.TextFieldHelper;
import com.example.jendrik.moerder.R;


public class CreateGame extends Activity {

    public static final String NAME = "gameName";
    public static final String SECRET_CHECKED = "checked";
    public static final String PASS = "password";
    public static final String PLAYER_COUNT = "players";
    public static final String ROOM_COUNT = "rooms";
    public static final String WEAPON_COUNT = "weapons";
    public static final String COUNTER_MIN = "minutes";
    public static final String COUNTER_SEC = "seconds";
    public static final String SENDTODATABASE = "sentToDatabase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategame);
        final EditText et = (EditText) findViewById(R.id.editText);
        //et.setText("TESTSPIEL");

    }

    /**
     * die Methode "sammelt" die Spieleinstellungen ein, hängt sie in einen Intent, der dann an die nächste Activity übergeben wird.
     * @param button erwartet einen View vom Typ Button - normalerweise der, der geklickt wird. Wird automatisch übergeben, wenn man dem Button sagt, welche Methode er onClick ausführen soll
     */
    public void onClickNextScreen(View button){

        boolean nameDone = false;

        final EditText et = (EditText) findViewById(R.id.editText);
        if( et.getText().toString().length() == 0 || TextFieldHelper.stringIsEmpty(et.getText().toString()))
            et.setError( getText(R.string.error_empty_gamename) );
        else
            nameDone = true;

        final String gameName = et.getText().toString();


        final CheckBox cb = (CheckBox) findViewById(R.id.cb_password);
        final boolean isSecret = cb.isChecked();

        final Spinner spinner=(Spinner) findViewById(R.id.spinner_player);
        final int pos = spinner.getSelectedItemPosition();
        final int[] cPlayer = getResources().getIntArray(R.array.players);
        final int countP = cPlayer[pos];


        final Spinner spinnerR=(Spinner) findViewById(R.id.spinner_room); //Spinner sind immer Array. Können über XML festgelegt werden (hier so geschehen)
        final int p = spinnerR.getSelectedItemPosition();
        final int[] cRooms = getResources().getIntArray(R.array.rooms);
        final int countR = cRooms[p];

        final Spinner spinnerW=(Spinner) findViewById(R.id.spinner_weapon);
        final int posw = spinnerW.getSelectedItemPosition();
        final int[] cWeapon = getResources().getIntArray(R.array.weapons);
        final int countW = cWeapon[posw];

        final EditText etMin = (EditText) findViewById(R.id.et_minutes); //EditText Objekte können nur Strings, daher muss ein Int geparst werden
        final String str_min = etMin.getText().toString();
        final int min = Integer.parseInt(str_min);

        final EditText etSec = (EditText) findViewById(R.id.et_seconds);
        final String str_sec= etSec.getText().toString();
        final int sec = Integer.parseInt(str_sec);

        final Intent intent = new Intent(this, RoomNameList.class);
        intent.putExtra(NAME, gameName);
        intent.putExtra(SECRET_CHECKED, isSecret);
        boolean passwordDone = true;
        if(isSecret){
            final EditText et2 = (EditText) findViewById(R.id.editText2);
            if(TextFieldHelper.stringIsEmpty(et2.getText().toString())){
                et2.setError(getText(R.string.error_empty_single_textfield));
                passwordDone = false;
            }else{
                final String passw = et2.getText().toString();
                intent.putExtra(PASS, passw);
            }

        }
        intent.putExtras(getIntent().getExtras());
        intent.putExtra(PLAYER_COUNT, countP);
        intent.putExtra(ROOM_COUNT, countR);
        intent.putExtra(WEAPON_COUNT,countW);
        intent.putExtra(COUNTER_MIN, min);
        intent.putExtra(COUNTER_SEC, sec);

        if(nameDone&& passwordDone)
            startActivity(intent);
    }


}
