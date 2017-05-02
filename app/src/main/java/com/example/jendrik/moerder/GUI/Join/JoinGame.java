package com.example.jendrik.moerder.GUI.Join;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jendrik.moerder.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class JoinGame extends Activity {
    private FirebaseDatabase database;
    private ListView lv;
    private List<String> games;
    private boolean isSecret;
    private boolean isRunning;
    private DatabaseReference myRef;
    private ChildEventListener el;
    private DatabaseReference runningChecker;
    private ValueEventListener ve;
    private ArrayAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        runningChecker = FirebaseDatabase.getInstance().getReference();
        lv = (ListView)findViewById(R.id.JoinListView);
        games = new ArrayList();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("games");
       // el = addEventListener();
       // myRef.addChildEventListener(el);
        ve = addVEventListener();
        myRef.addValueEventListener(ve);
        adapter = new ArrayAdapter(JoinGame.this,android.R.layout.simple_list_item_1,games);
        lv.setAdapter(adapter);

        setTouchListener();
    }

    private ChildEventListener addEventListener(){
        ChildEventListener eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) { getUpdate(dataSnapshot); }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { getUpdate(dataSnapshot);}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        return eventListener;
    }

    private ValueEventListener addVEventListener(){
        ValueEventListener ve = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        return ve;
    }

    private void  getUpdate(DataSnapshot snapshot){
        games.clear();
        for (DataSnapshot ds : snapshot.getChildren()){
            String gameName = ds.getKey();
            games.add(gameName);
        }

        for(String gameName : games){
            checkRunning(gameName);
        }

        /*if(games.size()>0){
            ArrayAdapter adapter = new ArrayAdapter(JoinGame.this,android.R.layout.simple_list_item_1,games);
            adapter.notifyDataSetChanged();
            lv.setAdapter(adapter);
        }
        else
            Toast.makeText(this,"There are no games on the list!",Toast.LENGTH_SHORT);*/
    }


    private void setTouchListener(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String gameName = games.get(position);

                database.getReference().child("games").child(gameName).child("isSecret").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        isSecret = dataSnapshot.getValue(Boolean.class);
                        startNextActivity(gameName);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                myRef.removeEventListener(ve);
                finish();
            }
        });
    }

    private void startNextActivity(String gameName){
        final Intent intent;
        if(isSecret) {
            intent = new Intent(JoinGame.this, PopUpEnterPassword.class);
        }else{
            intent = new Intent(JoinGame.this, PopUpEnterName.class);
        }
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("host",false);
        intent.putExtra("gameName", gameName);

        startActivity(intent);
    }

    private void checkRunning(String gameName) {
        final String lGameName = gameName;

        runningChecker.child("games").child(gameName).child("running").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isRunning = dataSnapshot.getValue(Boolean.class);
                if(isRunning) {
                    games.remove(lGameName);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
}


