package com.example.jendrik.moerder.GUI.Host.AdapterClasses;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jendrik.moerder.GUI.Host.GivenQrCodes;
import com.example.jendrik.moerder.R;

public class QRCodeAdapterClass extends RecyclerView.Adapter<QRCodeAdapterClass.ViewHolderClass> {
    private int i;

    public class ViewHolderClass extends RecyclerView.ViewHolder {

        public TextView objectName;
        public TextView qrCode;

        public ViewHolderClass(View itemView) {
            super(itemView);
            objectName = (TextView) itemView.findViewById(R.id.txt_qr_for);
            qrCode = (TextView) itemView.findViewById(R.id.txt_qr);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.qr_codelist_row, null);
        return new ViewHolderClass(itemView);
    }

    private String unHashMe(String kind){
        String str;
        switch (kind){
            case "qr":
                str = GivenQrCodes.qrList.get(i).get("qrCode").getText().toString();
                return str;
            case "object":
                str = GivenQrCodes.qrList.get(i).get("objectName").getText().toString();
                return str;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolderClass viewHolderClass, final int i) {
        this.i = i;
        viewHolderClass.qrCode.setText(unHashMe("qr"));
        viewHolderClass.objectName.setText(unHashMe("object"));

        viewHolderClass.qrCode.setVisibility(View.VISIBLE);
        viewHolderClass.objectName.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return GivenQrCodes.qrList.size();
    }
}