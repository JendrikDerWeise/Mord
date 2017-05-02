package com.example.jendrik.moerder.GUI.LittleHelpers.SuspectionHelpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import com.example.jendrik.moerder.GUI.OnGamingClasses.GameIsRunningCallback;
import com.example.jendrik.moerder.R;

public class PopUpShowSuspectorTheResult extends DialogFragment {

    private GameIsRunningCallback callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.mordTheme));
        Bundle args = getArguments();
        String title = args.getString("title");
        String clueOwner = args.getString("clueOwner");
        String clue = args.getString("clue");

        makeCallback();

        builder.setTitle(clueOwner + " " + title)
                .setMessage(clue)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        callback.endTurn();
                    }
                });

        return builder.create();
    }

    private void makeCallback(){
        Activity activity = getActivity();

        try {
            this.callback = (GameIsRunningCallback)activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemSelected");
        }
    }
}
