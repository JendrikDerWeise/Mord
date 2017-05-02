package com.example.jendrik.moerder.GUI.Host;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.jendrik.moerder.GUI.Host.AdapterClasses.QRCodeAdapterClass;
import com.example.jendrik.moerder.GUI.Host.AdapterClasses.RoomAdapterClass;
import com.example.jendrik.moerder.GUI.Join.PopUpEnterName;
import com.example.jendrik.moerder.Game;
import com.example.jendrik.moerder.GameObjekts.Room;
import com.example.jendrik.moerder.GameObjekts.Weapon;
import com.example.jendrik.moerder.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GivenQrCodes extends Activity {
    private Game game;
    public static List<HashMap<String, TextView>> qrList;
    public static List<Drawable> iconList;
    private Bundle extras;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvadapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_codelist_activity);
        extras = getIntent().getExtras();
        game = (Game)extras.get("GAME");
        makeList();

        final Activity fA = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_qrnumber);
        rvLayoutManager = new LinearLayoutManager(fA);
        recyclerView.setLayoutManager(rvLayoutManager);

        rvadapter = new QRCodeAdapterClass();

        recyclerView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(rvadapter);

        //TODO Gruppenraum ausbauen/wirklichen Raum als gruppenraum

        startActivity(new Intent(GivenQrCodes.this, popupGivenQrCodes.class));
    }

    private void makeList(){
        qrList = new ArrayList<>();
        iconList = new ArrayList<>();
        for(Room r : game.getRooms()){
            hashMe(r.getName(),"" + r.getQrCode());

            for(Weapon w : r.getWeaponList()){
                hashMe(w.getName(),"" + w.getQrCode());

            }
        }
        hashMe(""+getResources().getText(R.string.grp_room),"" + 29);

    }

    private void hashMe(String objectName, String qr){
        HashMap<String,TextView> hashMap = new HashMap<>();
        TextView tvObject = new TextView(this);
        tvObject.setText(objectName);
        TextView tvQRCode = new TextView(this);
        tvQRCode.setText("" + qr);
        hashMap.put("objectName", tvObject);
        hashMap.put("qrCode", tvQRCode);
        qrList.add(hashMap);
    }

    public void startWaitForPlayers(View v){
        final Intent intent = new Intent(this, PopUpEnterName.class);
        intent.putExtras(extras);
        intent.putExtra("GAME", game);
        startActivity(intent);
    }

}
