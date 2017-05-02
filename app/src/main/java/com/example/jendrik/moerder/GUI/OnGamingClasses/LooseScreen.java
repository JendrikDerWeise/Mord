package com.example.jendrik.moerder.GUI.OnGamingClasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.GUI.Startscreen;
import com.example.jendrik.moerder.R;


public class LooseScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_looses);
        Log.d("pros","loosescreen");
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, Startscreen.class);
        startActivity(intent);
        this.finish();
    }
}
