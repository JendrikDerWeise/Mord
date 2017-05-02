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

import java.util.List;

public class PopUpSuspectionInformPlayerWhoHasClue extends DialogFragment {
    private GameIsRunningCallback mListener;
    private List<String> existentClues;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.mordTheme));
        existentClues = getArguments().getStringArrayList("existendClues");

        CharSequence[] cs = existentClues.toArray(new CharSequence[existentClues.size()]);
        Bundle args = getArguments();
        String message = args.getString("message");
        String title = args.getString("title");

        builder//.setMessage( message + " " + MenuDrawer.roomForCalling)
                .setTitle( title)
                .setItems(cs, makeOnClickListener());

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (GameIsRunningCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement GameIsRunnungCallback");
        }
    }

    private DialogInterface.OnClickListener makeOnClickListener(){
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.shownClueToSuspectionObject(PopUpSuspectionInformPlayerWhoHasClue.this, existentClues.get(which));
                dismiss();
            }
        };

        return clickListener;
    }
}