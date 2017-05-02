package com.example.jendrik.moerder.GUI.Host.AdapterClasses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jendrik.moerder.GUI.Host.RoomNameList;
import com.example.jendrik.moerder.R;

public class RoomAdapterClass extends RecyclerView.Adapter<RoomAdapterClass.ViewHolderClass>{
    private int i;
    private int counter=0;

    public class ViewHolderClass extends RecyclerView.ViewHolder{

        public EditText roomName;
        public MyCustomEditTextListener myCustomEditTextListener;

        public ViewHolderClass(View itemView) {
            super(itemView);
            roomName = (EditText) itemView.findViewById(R.id.room_name);
            myCustomEditTextListener = new MyCustomEditTextListener();
            roomName.addTextChangedListener(myCustomEditTextListener);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.roomlist_list, null);

        return new ViewHolderClass(itemView);
    }

    public void onViewAttachedToWindow(ViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
        holder.myCustomEditTextListener.updatePosition(i,RoomNameList.roomNames);

        if(counter != RoomNameList.roomNames.size()) {
            holder.roomName.setText(RoomNameList.roomNames.get(holder.getAdapterPosition()).getText());
            counter++;
        }
        holder.roomName.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBindViewHolder(ViewHolderClass viewHolderClass, final int i) {

        this.i=i;
        viewHolderClass.roomName.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return RoomNameList.roomNames.size();
    }



}