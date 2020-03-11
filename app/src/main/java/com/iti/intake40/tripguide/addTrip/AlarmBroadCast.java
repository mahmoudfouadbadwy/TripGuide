package com.iti.intake40.tripguide.addTrip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iti.intake40.tripguide.home.Home;

public class AlarmBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, Home.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("flag","notification");
        context.startActivity(i);
    }


    }

