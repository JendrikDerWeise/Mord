package com.example.jendrik.moerder.GUI.LittleHelpers;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.jendrik.moerder.R;

public class PopUpPlayerShowsCard extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();


        setContentView(R.layout.popup_shown_card);

        Bundle extras = getIntent().getExtras();
       //fill the content
        TextView pN = (TextView) findViewById(R.id.player_name);
        pN.setText(extras.getString("PLAYER_NAME"));
        TextView cN = (TextView) findViewById(R.id.card_name);
        cN.setText(extras.getString("CARD"));


        //Size of popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int) (height*0.6));
    }

    public void onClickOK(View button){
        finish();
    }
}
