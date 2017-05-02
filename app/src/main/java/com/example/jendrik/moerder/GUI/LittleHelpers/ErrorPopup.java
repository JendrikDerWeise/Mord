package com.example.jendrik.moerder.GUI.LittleHelpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.jendrik.moerder.R;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class ErrorPopup  extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.popup_error);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int) (height*0.6));

        setText();
    }

    private void setText(){
        Bundle extras = getIntent().getExtras();
        String typeOfError = extras.getString("typeOfError");
        TextView tv = (TextView) findViewById(R.id.txt_error_popup);

        switch(typeOfError){
            case "password":
                tv.setText(R.string.error_popup_password);
                break;

            case "gameName":
                tv.setText(R.string.error_popup_gamename);
                break;

            case "pname":
                tv.setText(R.string.error_popup_name);
                break;
        }

    }

    public void onClickOK(View button){
        finish();
    }
}
