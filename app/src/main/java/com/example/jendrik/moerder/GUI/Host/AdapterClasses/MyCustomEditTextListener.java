package com.example.jendrik.moerder.GUI.Host.AdapterClasses;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.List;


public class MyCustomEditTextListener extends Activity implements TextWatcher {
    private int position;
    private List<EditText> mDataset;

    public void updatePosition(int position, List<EditText> dataset) {
        this.position = position;
        mDataset = dataset;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        // no op
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        mDataset.get(position).setText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // no op
    }
}