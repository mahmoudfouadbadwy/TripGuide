package com.iti.intake40.tripguide.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setViews();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UpComingFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_upComming);
        }
        if (userIntent != null) {
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UpComingFragment()).commit();
                break;
            case R.id.nav_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
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

    private  void logOut()
    {
        FirebaseAuth.getInstance().signOut();
        loginIntent = new Intent(Home.this, Login.class);
        startActivity(loginIntent);
        finish();
    }

}
