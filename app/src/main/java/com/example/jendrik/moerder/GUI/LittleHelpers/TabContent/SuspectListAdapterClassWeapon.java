package com.example.jendrik.moerder.GUI.LittleHelpers.TabContent;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer;
import com.example.jendrik.moerder.R;


public class SuspectListAdapterClassWeapon extends RecyclerView.Adapter<SuspectListAdapterClassWeapon.ViewHolderClass>{
    private int i;

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        private TextView colorField;
        private TextView nameOfThing;

        public ViewHolderClass(View itemView) {
            super(itemView);
            colorField = (TextView) itemView.findViewById(R.id.txt_color_field);
            nameOfThing = (TextView) itemView.findViewById(R.id.txt_name_of_thing);
        }
    }


    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.suspect_list_row, null);


        return new ViewHolderClass(itemView);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolderClass viewHolderClass) {
        super.onViewAttachedToWindow(viewHolderClass);

        int playerCount = com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getPlayers().size();
        int roomCount = com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getRooms().size();

        viewHolderClass.nameOfThing.setText(PersonList.namesOfThings.get(i + playerCount + roomCount));
        setSuspectColor(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getActivePlayer().getSuspectOnList(i + playerCount + roomCount), viewHolderClass.colorField);


        viewHolderClass.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getActivePlayer().getSuspectOnList(i)) {
                    case "y":
                        com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getActivePlayer().suspectOnList(i, "n");
                        setSuspectColor(MenuDrawer.game.getActivePlayer().getSuspectOnList(i), (TextView) v.findViewById(R.id.txt_color_field));
                        break;
                    case "n":
                        com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getActivePlayer().suspectOnList(i, "m");
                        setSuspectColor(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getActivePlayer().getSuspectOnList(i), (TextView) v.findViewById(R.id.txt_color_field));
                        break;
                    case "m":
                        com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getActivePlayer().suspectOnList(i, "y");
                        setSuspectColor(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getActivePlayer().getSuspectOnList(i), (TextView) v.findViewById(R.id.txt_color_field));
                        break;
                }
                String pNumberString = "" + com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.whoAmI;
                SendToDatabase stb = new SendToDatabase(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getGameName(),pNumberString);
                stb.updateData("playerList", com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getPlayerManager().getPlayerList().get(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.whoAmI));
            }
        });
        viewHolderClass.nameOfThing.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBindViewHolder(ViewHolderClass viewHolderClass, final int i) {
        this.i=i;
        viewHolderClass.nameOfThing.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getWeapons().size();
    }

    private void setSuspectColor(String s, TextView tv){
        switch (s){
            case "y":
                tv.setBackgroundColor(ContextCompat.getColor(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.cont, R.color.suspect_yes));
                break;
            case "n":
                tv.setBackgroundColor(ContextCompat.getColor(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.cont, R.color.suspect_no));
                break;
            case "m":
                tv.setBackgroundColor(ContextCompat.getColor(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.cont, R.color.suspect_maybe));
                break;
        }
    }
}