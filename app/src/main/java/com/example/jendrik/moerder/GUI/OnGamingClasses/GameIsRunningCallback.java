package com.example.jendrik.moerder.GUI.OnGamingClasses;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;

import com.example.jendrik.moerder.GUI.LittleHelpers.SuspectionHelpers.Suspection;
import com.example.jendrik.moerder.GameObjekts.Player;
import com.example.jendrik.moerder.GameObjekts.Room;

import java.util.ArrayList;
import java.util.List;

public interface GameIsRunningCallback {
    void playerListChanged(List<Player> playerList);
    void roomListChanged(List<Room> roomList);
    void pauseIsPressed(boolean paused);
    void aktivePlayerChanged(double aktivePlayer);

    void onProsecutionPositive(DialogFragment dialog);
    void onIndictNegative(DialogFragment dialog);

    void onBackPositive(DialogFragment dialog);
    void onBackNegative(DialogFragment dialog);

    void prosecutionNotify();
    void startScanForGrpRoom(DialogFragment dialog);

    void suspectionNotify(Suspection suspection);
    void callPlayer(DialogFragment dialog);
    void suspectionNextPlayer();
    void informPlayerWhoHasClue(ArrayList<String> existendClues);
    void shownClueToSuspectionObject(DialogFragment dialog, String clueName);
    void showSuspectionResultBroadcast();
    void informSuspector();
    void endTurn();
    void stopTimer();
    void setTimerOnPause(String status);
}
