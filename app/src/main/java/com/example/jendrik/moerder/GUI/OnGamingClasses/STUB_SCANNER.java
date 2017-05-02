package com.example.jendrik.moerder.GUI.OnGamingClasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.jendrik.moerder.R;


public class STUB_SCANNER extends Activity {
    public static String RESULT = "scan result";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stub_scanner); //XML zuweisen

    }

    public void onClickGescannt(View button){
        final EditText et_integer = (EditText) findViewById(R.id.et_stub_scanner_int); //Texteingabefeld zuweisen
        final String no = et_integer.getText().toString(); //wildes casting
        final int integer = Integer.parseInt(no); //immernoch casting...

        Intent resultIntent = new Intent(); //Übergabedatei (Intent) bereitmachen
        resultIntent.putExtra(RESULT,integer);//Intent mit Daten füllen
        setResult(Activity.RESULT_OK, resultIntent);//RESULT festlegen, Intent mit dazu...

        finish();//Activity beenden
    }

    @Override
    public void onBackPressed(){

    }
}
