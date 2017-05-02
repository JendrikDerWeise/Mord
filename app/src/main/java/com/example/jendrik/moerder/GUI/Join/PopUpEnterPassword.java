package com.example.jendrik.moerder.GUI.Join;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jendrik.moerder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUpEnterPassword extends Activity {
    private String typedPass;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.popup_enter_name);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        TextView tv = (TextView)findViewById(R.id.tv_pass_name);
        tv.setText("Please enter password...");

        Button btn = (Button)findViewById(R.id.OKbuttonNamePass);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOKPassword(v);
            }
        });
    }

    public void onClickOKPassword(View button){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final String gameName = getIntent().getExtras().getString("gameName");
        final EditText et = (EditText)findViewById(R.id.enterPlayerName);
        typedPass = et.getText().toString();
        database.getReference().child("games").child(gameName).child("pass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().equals(typedPass)){
                    Intent intent = new Intent(PopUpEnterPassword.this, PopUpEnterName.class);
                    intent.putExtra("gameName", gameName);
                    Bundle extras = getIntent().getExtras();
                    intent.putExtras(extras);
                    startActivity(intent);
                    finish();
                }
                else
                    et.setError("Wrong password!");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
