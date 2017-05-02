package com.example.jendrik.moerder.GUI.Host.AdapterClasses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jendrik.moerder.GUI.Host.RoomNameList;
import com.example.jendrik.moerder.GUI.Host.WeaponNameList;
import com.example.jendrik.moerder.R;

public class WeaponAdapterClass  extends RecyclerView.Adapter<WeaponAdapterClass.ViewHolderClass>{
    private int i;
    private int counter = 0;

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        public EditText weaponName;
        public MyCustomEditTextListener myCustomEditTextListener;

        public ViewHolderClass(View itemView) {
            super(itemView);
            weaponName = (EditText) itemView.findViewById(R.id.weapon_name);
            myCustomEditTextListener = new MyCustomEditTextListener();
            weaponName.addTextChangedListener(myCustomEditTextListener);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weaponlist_list, null);

        return new ViewHolderClass(itemView);
    }

    public void onViewAttachedToWindow(ViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
        holder.myCustomEditTextListener.updatePosition(i, WeaponNameList.weaponNames);

        if(counter != WeaponNameList.weaponNames.size()) {
            holder.weaponName.setText(WeaponNameList.weaponNames.get(holder.getAdapterPosition()).getText());
            counter++;
        }
        holder.weaponName.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBindViewHolder(ViewHolderClass viewHolderClass, final int i) {

        this.i=i;
        viewHolderClass.weaponName.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return WeaponNameList.weaponNames.size();
    }


}