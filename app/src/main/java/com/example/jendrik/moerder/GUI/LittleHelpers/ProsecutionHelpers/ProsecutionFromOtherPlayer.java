package com.example.jendrik.moerder.GUI.LittleHelpers.ProsecutionHelpers;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.GUI.OnGamingClasses.LooseScreen;
import com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer;
import com.example.jendrik.moerder.GUI.OnGamingClasses.WinScreen;
import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProsecutionFromOtherPlayer extends Fragment {
    private View fragLayoutV;
    private Game game;
    private LayoutInflater inflater;
    private ViewGroup container;
    private String gameName;
    private Context context;

    private DatabaseReference prosecutionIsPlacedReference;
    private DatabaseReference playerWinsReference;
    private ValueEventListener prosecutionIsPlacedListener;
    private ValueEventListener playerWinsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        fragLayoutV = inflater.inflate(R.layout.prosecution_waiting_for_placement, container, false);

        game = MenuDrawer.game;
        this.gameName = game.getGameName();

        prosecutionIsPlacedListener();

        return fragLayoutV;
    }

    public void prosecutionIsPlacedListener(){
        prosecutionIsPlacedListener = prosecutionListener();
        prosecutionIsPlacedReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        prosecutionIsPlacedReference.child("prosecutionIsPlaced").addValueEventListener(prosecutionIsPlacedListener);
    }

    private ValueEventListener prosecutionListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean prosecutionIsPlaced = dataSnapshot.getValue(Boolean.class);
                if(prosecutionIsPlaced)
                    playerWinsListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        return ve;
    }

    public void playerWinsListener(){
        playerWinsListener = makePlayerWinsListener();
        playerWinsReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameName);
        playerWinsReference.child("playerWins").addValueEventListener(playerWinsListener);
    }

    private boolean preventDoubleFrag;

    private ValueEventListener makePlayerWinsListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean playerWins = dataSnapshot.getValue(Boolean.class);
                if(!preventDoubleFrag) {
                    if (playerWins) {
                        Intent intent = new Intent(context, LooseScreen.class);
                        startActivity(intent);
                        //TODO callback für unbind listeners um spiel zu beenden
                        getActivity().finish();
                    } else {
                        if (game.getPlayerManager().ckeckForWon()) {
                            Intent intent = new Intent(context, WinScreen.class);
                            context.startActivity(intent);
                            //TODO callback für unbind listeners um spiel zu beenden
                            getActivity().finish();
                        } else {
                            dialog();
                        }
                    }
                    unbindListeners();
                    preventDoubleFrag = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return ve;
    }

    private void unbindListeners() {
        prosecutionIsPlacedReference.removeEventListener(prosecutionIsPlacedListener);
        playerWinsReference.removeEventListener(playerWinsListener);
        SendToDatabase stb = new SendToDatabase(game.getGameName());
        stb.updateData("prosecutionIsPlaced", false);
    }

    private void dialog(){
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.mordTheme))
                .setMessage("spieler stirbt")
                .setTitle("spieler failed")

            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        })
        .show();
        //removeFragment(ProsecutionFromOtherPlayer.this);
    }

    private void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
    }
}
