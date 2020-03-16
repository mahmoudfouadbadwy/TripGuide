package com.iti.intake40.tripguide.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.login.Login;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Intent userIntent;
    private TextView emailDrawer;
    private View header;
    private Intent loginIntent;
    private UpComingFragment upComingFragment;
    private HistoryFragment historyFragment;

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


}
