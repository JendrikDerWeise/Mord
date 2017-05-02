package com.example.jendrik.moerder.GUI.OnGamingClasses;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jendrik.moerder.GUI.LittleHelpers.MapAdapterClass;
import com.example.jendrik.moerder.GameObjekts.Room;
import com.example.jendrik.moerder.GameObjekts.Weapon;
import com.example.jendrik.moerder.R;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class MapOverview extends Fragment{
    private View fragLayoutV;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvadapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    public static ArrayList<String> roomNames;
    public static ArrayList<String> weaponNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Diese Activity stellt nur das Geruest fuer die Spielkarte her. Die eigentliche Arbeit uebernehmen die Adapter
     * siehe LittleHelpers->MapAdapterClass
     * roomNames und weaponNames muessen statisch sein, damit die Adapter auf diese zugreifen und darstellen koennen.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragLayoutV = inflater.inflate(R.layout.fragment_map, container, false);
        update();
        return fragLayoutV;
    }

    public void update(){

        roomNames = new ArrayList<>();
        weaponNames = new ArrayList<>();

        //Jeder Raum braucht seine eigene Waffenliste.
        //Leider funktioniert es nicht mit einzelnen Waffennamen, daher ist ein Element in der Waffenliste
        //mit einer neuen Zeile vom naechsten WAffen-Element getrennt. (String-Liste)
        //Beispiel: Raum1 enthaelt -> Waffe1\nWaffe2\n
        //          Raum2 enthaelt -> \n
        //          Raum3 enthaelt -> Waffe3\n
        //alles nach "->" ist ein Listeneintrag in der Arrayliste.
        for(Room r: com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getRooms()) {
            roomNames.add(r.getName());
            String str_weaponNames="";
            if(r.getWeaponList() != null) {
                for (Weapon w : r.getWeaponList())
                    str_weaponNames += w.getName() + "\n";
            }
            weaponNames.add(str_weaponNames);
        }

        final Activity fA = getActivity();
        recyclerView = (RecyclerView) fragLayoutV.findViewById(R.id.recyclerview);

        rvLayoutManager = new LinearLayoutManager(fA);
        recyclerView.setLayoutManager(rvLayoutManager);

        rvadapter = new MapAdapterClass();
        recyclerView.setAdapter(rvadapter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}