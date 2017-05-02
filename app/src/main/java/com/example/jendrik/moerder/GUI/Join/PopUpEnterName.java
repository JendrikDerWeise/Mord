package com.example.jendrik.moerder.GUI.Join;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.GUI.Host.WaitForPlayers;
import com.example.jendrik.moerder.GUI.Startscreen;
import com.example.jendrik.moerder.GUI.TextFieldHelper;
import com.example.jendrik.moerder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PopUpEnterName extends Activity {
    public static final String PNAME = "playerName";
    private String pName;
    List<String> connectedPlayers;
    SendToDatabase sendToDatabase;
    DatabaseReference ref;
    boolean nameOK;
    private ValueEventListener vl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.popup_enter_name);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));


        connectedPlayers=new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String gameName = getIntent().getExtras().getString("gameName");
        sendToDatabase = new SendToDatabase(gameName);

        ref = database.getReference().child("games").child(gameName).child("connectedPlayers");

        vl = setListener();
        ref.addListenerForSingleValueEvent(vl);
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.mordTheme);
        builder.setMessage(R.string.popup_back_message);
        builder.setCancelable(true);

        builder.setNegativeButton(
                R.string.popup_back_negative_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton(
                R.string.popup_back_positive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(PopUpEnterName.this, Startscreen.class));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkNameOK(){
        if (connectedPlayers.contains(pName))
            nameOK= false;
        else
            nameOK =true;
    }


    public void onClickOK(View button){
        EditText et = (EditText) findViewById(R.id.enterPlayerName);
        pName = et.getText().toString();

        checkNameOK();
        boolean allcorrect = true;
        if(!nameOK) {
            et.setError(getText(R.string.error_popup_name));
            allcorrect = false;
        }
        if(TextFieldHelper.stringIsEmpty(pName)) {
            et.setError(getText(R.string.error_empty_single_textfield));
            allcorrect = false;
        }
        if(allcorrect){
            boolean host = getIntent().getExtras().getBoolean("host");
            Intent intent;

            if(host) {
                intent = new Intent(this, WaitForPlayers.class);
                connectedPlayers.clear();
            }else
                intent = new Intent(this, WaitForServer.class);

            connectedPlayers.add(pName);

            ref.setValue(connectedPlayers);

            Bundle extras = getIntent().getExtras();
            intent.putExtras(extras);
            intent.putExtra("pName",pName);
            ref.removeEventListener(vl);
            startActivity(intent);
        }

    }

    private ValueEventListener setListener(){

        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot sn:dataSnapshot.getChildren()) {
                    String name = sn.getValue(String.class);
                    connectedPlayers.add(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }
}