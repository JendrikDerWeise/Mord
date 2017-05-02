package com.example.jendrik.moerder.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jendrik.moerder.GUI.Host.CreateGame;
import com.example.jendrik.moerder.GUI.Join.JoinGame;
import com.example.jendrik.moerder.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileOutputStream;


/**
 * First screen of game
 */
public class Startscreen extends Activity {

    Firebase myFirebaseRef = new Firebase("https://project-3462685560901461074.firebaseio.com/");
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private NfcAdapter nfcAdapter;
    private boolean modeChosen;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide(); //macht das Ding oben weg
        setContentView(R.layout.activity_startscreen); //legt das Layout fest
        Typeface specialTypeface = Typeface.createFromAsset(getAssets(), "fonts/wcRoughTrad.ttf");
        TextView specialTextView = (TextView)findViewById(R.id.appName);
        specialTextView.setTypeface(specialTypeface);
//        Log.d("Firebase Token ", FirebaseInstanceId.getInstance().getToken());
        mAuth = FirebaseAuth.getInstance();
        extras = new Bundle();

        firebaseBindAuthStateListener();

        checkForNfc();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        signInAnonymously();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onClickCreateGame(View button){
        final Intent intent = new Intent(this, CreateGame.class); //legt nächste, zu startende Activity fest
        if(modeChosen) {
            intent.putExtras(extras);
            startActivity(intent); //startet nächste Activity
        }else
            checkForNfc();
    }

    public void onClickJoin(View button){
        final Intent intent = new Intent(this, JoinGame.class);
        if (modeChosen) {
            intent.putExtras(extras);
            startActivity(intent);
        }else
            checkForNfc();
    }

    /**
     * Actually useless method. Just for preparation if I want to integrate a log-in
     */
    private void firebaseBindAuthStateListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    /**
     * Actually there is no reason to make a log-in and save the user.
     * For that reason auth. stays anonymously
     */
    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInAnonymously", task.getException());
                            Toast.makeText(Startscreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
    }

    /**
     * Check if NFC is availavle.
     * If not, game can be started with QR-Code scanner.
     */
    private void checkForNfc(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this,
                    "NFC NOT supported on this device!",
                    Toast.LENGTH_LONG).show();
            extras.putString("mode", "qr");
            modeChosen = true;
        }else if(!nfcAdapter.isEnabled()){
            Toast.makeText(this,
                    "To play with NFC-Tags switch on NFC!",
                    Toast.LENGTH_LONG).show();
            extras.putString("mode", "qr");
            modeChosen = true;
        }else if(nfcAdapter.isEnabled())
            askForGameMode();
    }

    /**
     * If NFC is avaiable player can choose if he wants to play with that.
     */
    private void askForGameMode(){
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.mordTheme))
                .setMessage(getResources().getString(R.string.txt_game_mode))
                .setTitle(getResources().getString(R.string.ask_playMode))

                .setPositiveButton("QR-Code Scanner", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        extras.putString("mode", "qr");
                        modeChosen = true;
                    }
                })

                .setNegativeButton("NFC-Scanner", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        extras.putString("mode", "nfc");
                        modeChosen = true;
                    }
                })
                .show();
    }

}
