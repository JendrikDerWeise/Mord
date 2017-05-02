package com.example.jendrik.moerder.GUI.LittleHelpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;

import com.example.jendrik.moerder.GUI.OnGamingClasses.MenuDrawer;
import com.example.jendrik.moerder.R;


public class NotificationBuilder {
    private NotificationCompat.Builder mBuilder;
    private Context mContext;
    private Bundle mExtras;

    public NotificationBuilder(Context context, Bundle extras){
        mBuilder = new NotificationCompat.Builder(context);
        mContext = context;
        mExtras = extras;
    }

    public void setBuilder(String title, String txt){
        mBuilder.setSmallIcon(R.drawable.pistol);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(txt);
        mBuilder.setVibrate(new long[]{500,500});
        mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setAutoCancel(true);
        Intent intent = new Intent(mContext, MenuDrawer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_FROM_BACKGROUND);
        intent.putExtras(mExtras);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);
    }

    public NotificationCompat.Builder getBuilder(){
        return mBuilder;
    }
}
