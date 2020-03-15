package com.iti.intake40.tripguide.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.login.Login;
import com.iti.intake40.tripguide.model.RealTime;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Intent userIntent;
    private TextView emailDrawer;
    private View header;
    private Intent loginIntent;
    private Intent notificationIntent;
    private UpComingFragment upComingFragment;
    private HistoryFragment historyFragment;
    // notification
    private final String CHANNEL_ID = "personal_notification";
    private final int NOTIFICATION_ID = 001;
    boolean flag = false;
    Notification notification;
    NotificationManager notificationManager;
    Ringtone ringtone;
    Uri alarmUri;
    AlertDialog alertDialog;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setViews();
        // set Selected Fragment
        if (savedInstanceState == null) {
            upComingFragment = new UpComingFragment();
            upComingFragment.set_context(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    upComingFragment).commit();
            navigationView.setCheckedItem(R.id.nav_upComming);
            setTitle("Upcoming Trips");
        }
        // setEmail
        if (userIntent != null) {
            if (userIntent.hasExtra("Email"))
                emailDrawer.setText(userIntent.getExtras().getString("Email"));
        }

        // notification
        if (notificationIntent != null) {
            if (notificationIntent.hasExtra("tripKey")) {
                setAlarmConfiguration();
                setAlarmDialog(notificationIntent.getExtras().getString("tripName"),
                        notificationIntent.getExtras().getString("from"),
                        notificationIntent.getExtras().getString("to"),
                        notificationIntent.getExtras().getString("tripKey"));

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_upComming:
                upComingFragment = new UpComingFragment();
                upComingFragment.set_context(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        upComingFragment).commit();
                setTitle("Upcoming Trips");
                break;
            case R.id.nav_history:
                historyFragment = new HistoryFragment();
                historyFragment.set_context(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        historyFragment).commit();
                setTitle("History");
                break;
            case R.id.nav_logOut:
                logOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        userIntent = getIntent();
        header = navigationView.getHeaderView(0);
        emailDrawer = header.findViewById(R.id.drawer_mail);
        notificationIntent = getIntent();

    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        loginIntent = new Intent(Home.this, Login.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // notifications
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
        alertDialogBuilder.setMessage(tripName + "  Coming ");
        alertDialogBuilder.setNeutralButton("Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                stopRing();
                notification();
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopRing();
                flag = true;
                notification();
                new RealTime().makeDone(tripKey);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + from + "&daddr=" +
                        to));
                startActivity(mapIntent);
            }
        });

        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(Home.this, "Trip Canceled", Toast.LENGTH_LONG).show();
                stopRing();
                new RealTime().cancelTrip(tripKey);
                flag = true;
                notification();

            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    // check versions .
    public void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            show_Notification();
        else
            showOldNotification();
    }

    public void stopRing() {
        ringtone.stop();
    }

    // version less than 8 .
    public void showOldNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.clock)
                .setContentTitle("Trip Guide")
                .setVibrate(new long[]{1000, 1000})
                .setContentText(" Return to Your Trip Again :) ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true).setOngoing(true);
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("reopen", "reopen");
        intent.putExtra("tripKey", notificationIntent.getExtras().getString("tripKey"));
        intent.putExtra("tripName", notificationIntent.getExtras().getString("tripName"));
        intent.putExtra("from", notificationIntent.getExtras().getString("from"));
        intent.putExtra("to", notificationIntent.getExtras().getString("to"));
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
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtra("reopen", "reopen");
        intent.putExtra("tripKey", notificationIntent.getExtras().getString("tripKey"));
        intent.putExtra("tripName", notificationIntent.getExtras().getString("tripName"));
        intent.putExtra("from", notificationIntent.getExtras().getString("from"));
        intent.putExtra("to", notificationIntent.getExtras().getString("to"));
        String CHANNEL_ID = "MYCHANNEL";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentText(" Return to Your Trip Again :) ")
                .setContentTitle("Trip Guide")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setVibrate(new long[]{1000, 1000})
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

}
