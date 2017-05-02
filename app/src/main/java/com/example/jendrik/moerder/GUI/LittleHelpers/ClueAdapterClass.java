package com.example.jendrik.moerder.GUI.LittleHelpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jendrik.moerder.GUI.OnGamingClasses.ShowClues;
import com.example.jendrik.moerder.R;

/**
 * Dies ist das "Futter" fuer die ListView.
 * Die Klasse legt den Inhalt der entsprechenden Rows fest (Text und Bild)
 * Die Inner-Class befuellt lediglich die Variablen.p
 */
public class ClueAdapterClass extends RecyclerView.Adapter<ClueAdapterClass.ViewHolderClass>{

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        TextView clueName;
        ImageView itemImageView;

        public ViewHolderClass(View itemView) {
            super(itemView);
            clueName = (TextView) itemView.findViewById(R.id.txt_clue_name);
            itemImageView = (ImageView) itemView.findViewById(R.id.img_kind_of_clue);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clue_row, null);

        return new ViewHolderClass(itemView);
    }

    public void onViewAttachedToWindow(ViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
        holder.clueName.setVisibility(View.VISIBLE);
        holder.itemImageView.setVisibility(View.VISIBLE);
    }

    /**
     * Entscheidung welches Bild fuer den Clue verwendet wird.
     * 0=player, 1=room, 2=weapon
     *
     * @param viewHolderClass
     * @param i entsprechender Inhalt der clues-ArrayList
     */
    @Override
    public void onBindViewHolder(ViewHolderClass viewHolderClass, final int i) {

        viewHolderClass.clueName.setText(ShowClues.clues.get(i).getName());
        int value=ShowClues.clues.get(i).getId().intValue();
        switch (value){
            case 0: //player
                viewHolderClass.itemImageView.setImageResource(R.drawable.person);
                break;
            case 1: //room
                viewHolderClass.itemImageView.setImageResource(R.drawable.map);
                break;
            case 2: //weapon
                viewHolderClass.itemImageView.setImageResource(R.drawable.pistol);
                break;
        }
        viewHolderClass.clueName.setVisibility(View.VISIBLE);
        viewHolderClass.itemImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return ShowClues.clues.size();
    }


}