package com.example.jendrik.moerder.GUI.LittleHelpers.SuspectionHelpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import com.example.jendrik.moerder.R;


public class PopupSuspectionShowPlayersTheResult extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.mordTheme));
        Bundle args = getArguments();
        String suspector = args.getString("suspector");
        String clueOwner = args.getString("clueOwner");
        String name = args.getString("name");
        String room = args.getString("room");
        String weapon = args.getString("weapon");
        String title = args.getString("title");
        String message = args.getString("message");

        builder.setTitle(suspector + " " +title)
                .setMessage(name + "\n" + room + "\n" + weapon +"\n\n" + clueOwner + " " + message)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }
}