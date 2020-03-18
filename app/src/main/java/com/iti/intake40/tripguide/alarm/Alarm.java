package com.iti.intake40.tripguide.alarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.floatingIcon.ShowMap;
import com.iti.intake40.tripguide.home.Home;
import com.iti.intake40.tripguide.model.RealTime;

public class Alarm extends AppCompatActivity {
    Ringtone ringtone;
    Uri alarmUri;
    AlertDialog alertDialog;
    Intent AlarmIntent;
    boolean flag;
    Notification notification;
    NotificationManager notificationManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_alarm);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY|
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        AlarmIntent = getIntent();
        if (AlarmIntent != null) {
            if (AlarmIntent.hasExtra("tripKey")) {
                setAlarmConfiguration();
                setAlarmDialog(AlarmIntent.getExtras().getString("tripName"),
                        AlarmIntent.getExtras().getString("from"),
                        AlarmIntent.getExtras().getString("to"),
                        AlarmIntent.getExtras().getString("tripKey"));

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAlarmConfiguration() {
        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(true);
        }
        ringtone.play();
    }


    private void setAlarmDialog(String tripName, final String from, final String to, final String tripKey) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(tripName + "  Coming ");
        alertDialogBuilder.setNeutralButton("Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stopRing();
                getNotification();
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopRing();
                flag = true;
                getNotification();
                new RealTime().makeDone(tripKey);
                Intent mapIntent = new Intent(Alarm.this, ShowMap.class);
                mapIntent.putExtra("startPoint" , from);
                mapIntent.putExtra("endpoint",to);
                mapIntent.putExtra("key",tripKey);
                startActivity(mapIntent);
                finish();

            }
        });

        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(Alarm.this, "Trip Canceled", Toast.LENGTH_LONG).show();
                stopRing();
                new RealTime().cancelTrip(tripKey);
                flag = true;
                getNotification();
                finish();

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    public void stopRing() {
        ringtone.stop();
    }

    // check versions .
    public void getNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            show_Notification();
        else
            showOldNotification();
    }


    // version less than 8 .
    public void showOldNotification() {
        int NOTIFICATION_ID = AlarmIntent.getExtras().getInt("alarmKey");
        String CHANNEL_ID = AlarmIntent.getExtras().getString("tripKey");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.clock)
                .setContentTitle("Trip Guide")
                .setContentText(" Return to "+AlarmIntent.getExtras().getString("tripName")+" Again :) ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true).setOngoing(true);
        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("tripKey", AlarmIntent.getExtras().getString("tripKey"));
        intent.putExtra("tripName", AlarmIntent.getExtras().getString("tripName"));
        intent.putExtra("from", AlarmIntent.getExtras().getString("from"));
        intent.putExtra("to", AlarmIntent.getExtras().getString("to"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (flag == false) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    // version more than 8 .
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void show_Notification() {
        int NOTIFICATION_ID = AlarmIntent.getExtras().getInt("alarmKey");
        Intent intent = new Intent(getApplicationContext(), Alarm.class);
        intent.putExtra("tripKey", AlarmIntent.getExtras().getString("tripKey"));
        intent.putExtra("tripName", AlarmIntent.getExtras().getString("tripName"));
        intent.putExtra("from", AlarmIntent.getExtras().getString("from"));
        intent.putExtra("to", AlarmIntent.getExtras().getString("to"));
        String CHANNEL_ID =  AlarmIntent.getExtras().getString("tripKey");
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "notification", NotificationManager.IMPORTANCE_HIGH);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification = new android.app.Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentText(" Return to "+AlarmIntent.getExtras().getString("tripName")+" Again :) ")
                .setContentTitle("Trip Guide")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(R.drawable.clock)
                .setOngoing(true).build();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        if (flag == false) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRing();
        getNotification();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
