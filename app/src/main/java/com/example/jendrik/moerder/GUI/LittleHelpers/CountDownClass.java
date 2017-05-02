package com.example.jendrik.moerder.GUI.LittleHelpers;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.jendrik.moerder.FCM.SendToDatabase;
import com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class CountDownClass{

    private CounterClass timer;
    private TextView textViewTime;
    private Activity activity;
    private long millisOnPause;
    private long millis;
    private String str;

    /**
     * Verantwortlich fuer das herunterzaehlen einer Rundendauer
     * Klasse rechnet "echte" Minuten und Sekundenangaben intern in Millisekunden um, weil sonst nix geht
     *
     * @param activity this
     * @param min Anzahl Minuten
     * @param sec Anzahl Sekunden
     */
   public CountDownClass(Activity activity, int min, int sec, String str){

       this.activity = activity;
       //textViewTime = (TextView) activity.findViewById(R.id.timer);

       int count = (min*60000) + (sec*1000);
       timer = new CounterClass(count, 1000);
       this.str = str;
    }



    public class CounterClass extends CountDownTimer {


        boolean wasPaused = false;

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            millis = millisUntilFinished;
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            //textViewTime.setText(hms);
            activity.setTitle(str + "          " + hms);
            wasPaused = false;
        }

        @Override
        public void onFinish() {
            com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.setNextActivePlayer();

            SendToDatabase stb = new SendToDatabase(com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getGameName());
            stb.updateData("completePlayerList", com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game.getPlayerManager().getPlayerList());
            stb.updateData("aktivePlayer", MenuDrawer.game.getPlayerManager().getAktivePlayer());

            activity.getIntent().putExtra("GAME", com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer.game);
            activity.getIntent().putExtra("myTurn", false);
            activity.finish();
            activity.startActivity(activity.getIntent());
        }


    }

    public CounterClass getTimer(){
        return timer;
    }

    public void pause(){
        millisOnPause = millis;
        timer.cancel();
    }

    public void resume(){
        timer = new CounterClass(millisOnPause,1000);
        timer.start();
    }
}
