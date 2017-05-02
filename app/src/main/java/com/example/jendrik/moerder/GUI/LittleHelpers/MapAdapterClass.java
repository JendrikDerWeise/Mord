package com.example.jendrik.moerder.GUI.LittleHelpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jendrik.moerder.GUI.OnGamingClasses.MapOverview;
import com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer;
import com.example.jendrik.moerder.R;

/**
 * Dies ist das "Futter" fuer die ListView.
 * Die Klasse legt den Inhalt der entsprechenden Rows fest (Text und Bild)
 * Die Inner-Class befuellt lediglich die Variablen..
 */
public class MapAdapterClass extends RecyclerView.Adapter<MapAdapterClass.ViewHolderClass>{

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView tvRoomName;
        TextView tvWeaponName;
        TextView tvRoomName2;
        TextView tvWeaponName2;


        public ViewHolderClass(View itemView) {
            super(itemView);
            tvRoomName = (TextView) itemView.findViewById(R.id.txt_roomname_map);
            tvWeaponName = (TextView) itemView.findViewById(R.id.txt_weapons_in_room_map);
            tvRoomName2 = (TextView) itemView.findViewById(R.id.txt_roomname_map2);
            tvWeaponName2 = (TextView) itemView.findViewById(R.id.txt_weapons_in_room_map2);
        }
    }

    int i;
    int count;
    int pos;

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.map_row, null);

        if(!MenuDrawer.myTurn){
            itemView.findViewById(R.id.txt_weapons_in_room_map).setBackgroundResource(R.color.colorOtherTurnLight);
            itemView.findViewById(R.id.txt_weapons_in_room_map2).setBackgroundResource(R.color.colorOtherTurnLight);
        }else {
            itemView.findViewById(R.id.txt_weapons_in_room_map).setBackgroundResource(R.color.colorPrimaryDark);
            itemView.findViewById(R.id.txt_weapons_in_room_map2).setBackgroundResource(R.color.colorPrimaryDark);
        }

        return new ViewHolderClass(itemView);
    }

    private void theWhatEverMethod(ViewHolderClass holder){
        if(holder instanceof ViewHolderClass && pos <= count-1){
            holder.tvRoomName.setVisibility(View.VISIBLE);
            holder.tvRoomName2.setVisibility(View.VISIBLE);
            holder.tvWeaponName.setVisibility(View.VISIBLE);
            holder.tvWeaponName2.setVisibility(View.VISIBLE);

            holder.tvRoomName.setText(MapOverview.roomNames.get(pos));
            holder.tvWeaponName.setText(MapOverview.weaponNames.get(pos));
            if (pos < count - 1) {//rechter Raum in der Spalte
                holder.tvRoomName2.setText(MapOverview.roomNames.get(pos + 1));
                holder.tvWeaponName2.setText(MapOverview.weaponNames.get(pos + 1));
            }else {//wenn ungerade Raumanzahl und i = letzter Raum in Liste
                setInvisibleSecond(holder);
            }
        }
        checkEmptyRooms(holder);
    }


    /**
     * Um zwei Raeume in einer Row anzuzeigen, wird hier ein wenig getrickst.
     * Auch das Ausblenden des letzten Raums bei ungerader Raumanzahl geschieht hier.
     *
     * @param viewHolderClass
     * @param i entsprechnde Position in RoomArrayList
     */
    @Override
    public void onBindViewHolder(ViewHolderClass viewHolderClass, final int i) {
        this.i = i;

        this.count = MapOverview.roomNames.size();
        this.pos = i*2;

        theWhatEverMethod(viewHolderClass);
    }

    private void checkEmptyRooms(ViewHolderClass holder){
        //verhindert die entstehenden "leeren" Raeume die warum auch immer auftauchen...
        //
        if(count%2==0) {
            if (i >= count / 2) {
                setInvisibleFirst(holder);
                setInvisibleSecond(holder);
            }
        }else{
            if (i >= (count / 2) + 1) {
                setInvisibleFirst(holder);
                setInvisibleSecond(holder);
            }
        }
    }

    private void setInvisibleFirst(ViewHolderClass viewHolderClass){
        viewHolderClass.tvRoomName.setVisibility(View.INVISIBLE);
        viewHolderClass.tvWeaponName.setVisibility(View.INVISIBLE);
    }

    private void setInvisibleSecond(ViewHolderClass viewHolderClass){
        viewHolderClass.tvRoomName2.setVisibility(View.INVISIBLE);
        viewHolderClass.tvWeaponName2.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return MapOverview.roomNames.size();
    }
}
