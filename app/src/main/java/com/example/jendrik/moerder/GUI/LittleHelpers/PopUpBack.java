package com.example.jendrik.moerder.GUI.LittleHelpers;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import com.example.jendrik.moerder.GUI.OnGamingClasses.GameIsRunningCallback;
import com.example.jendrik.moerder.R;


public class PopUpBack extends DialogFragment {
    GameIsRunningCallback mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.mordTheme));

        builder.setMessage(R.string.popup_back_message)
                .setTitle(R.string.popup_back_title);

        builder.setPositiveButton(R.string.popup_back_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onBackPositive(PopUpBack.this);
            }
        });

        builder.setNegativeButton(R.string.popup_back_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onBackNegative(PopUpBack.this);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (GameIsRunningCallback) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement GameIsRunnungCallback");
        }
    }

}



