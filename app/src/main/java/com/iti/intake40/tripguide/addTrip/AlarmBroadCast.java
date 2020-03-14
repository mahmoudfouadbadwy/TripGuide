package com.iti.intake40.tripguide.addTrip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.iti.intake40.tripguide.home.Home;

public class AlarmBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("BroadCast setup");

        Intent i = new Intent(context, Home.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("tripKey",intent.getExtras().getString("key"));
        i.putExtra("tripName",intent.getExtras().getString("tripName"));
        i.putExtra("from",intent.getExtras().getString("from"));
        i.putExtra("to",intent.getExtras().getString("to"));
        context.startActivity(i);

    }


    }

