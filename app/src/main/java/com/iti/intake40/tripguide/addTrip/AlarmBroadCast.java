package com.iti.intake40.tripguide.addTrip;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.iti.intake40.tripguide.alarm.Alarm;


public class AlarmBroadCast extends BroadcastReceiver {
    Intent i;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("broad cast setup");
        i = new Intent(context, Alarm.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("tripKey",intent.getExtras().getString("key"));
        i.putExtra("tripName",intent.getExtras().getString("tripName"));
        i.putExtra("from",intent.getExtras().getString("from"));
        i.putExtra("to",intent.getExtras().getString("to"));
        context.startActivity(i);

    }


    }

