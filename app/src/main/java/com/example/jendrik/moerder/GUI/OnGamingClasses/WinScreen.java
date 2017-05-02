package com.example.jendrik.moerder.GUI.OnGamingClasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.GUI.Startscreen;
import com.example.jendrik.moerder.R;


public class WinScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_wins);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Startscreen.class);
        startActivity(intent);
        this.finish();
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();

        Bundle extras = getIntent().getExtras();
        String gameName = extras.getString("gameName");
        SendToDatabase stb = new SendToDatabase(gameName);
        stb.deleteGame();
    }*/
}
