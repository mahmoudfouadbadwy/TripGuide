package com.iti.intake40.tripguide.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.addTrip.AddTrip;
import com.iti.intake40.tripguide.model.Trip;
import java.util.ArrayList;


public class UpComingFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private LinearLayout noTrips;
    private View view;
    private FloatingActionButton floatingActionButton;
    private Context _context;
    private Intent addTripIntent;
    private DatabaseReference mTripReference;
    private ArrayList<Trip> trips;
    private Trip trip;
    private ValueEventListener tripListener;
    UpComingFragment(Context _context){
        this._context = _context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_upcoming,container,false);
         setViews();
         getReference();
         loadTrips();
         return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.floatingActionButton:goToAddTrip();
        }
    }

    private void goToAddTrip()
    {
        addTripIntent = new Intent(_context, AddTrip.class);
        startActivity(addTripIntent);
    }

    private void setViews()
    {
        trips = new ArrayList<>();
        recyclerView = view.findViewById(R.id.Trips_RecycleView);
        manager = new  LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new RecycleAdapter(getContext(),trips));
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        noTrips = view.findViewById(R.id.No_Trips_Layout);
    }
    private void getReference()
    {
        mTripReference =FirebaseDatabase.getInstance().getReference("TripGuide")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
    @Override
    public void onStart() {
        super.onStart();
        configureVisibility();
    }
    private void loadTrips()
    {
         tripListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren()!=null) {
                    trips.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        trip = data.getValue(Trip.class);
                        trip.setKey(data.getKey());
                        trips.add(trip);

                    }
                }
                configureVisibility();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mTripReference.addValueEventListener(tripListener);
    }
    private void configureVisibility()
    {
        if (trips.size()>0)
        {
            recyclerView.setAdapter(new RecycleAdapter(getContext(),trips));
            noTrips.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.INVISIBLE);
            noTrips.setVisibility(View.VISIBLE);
        }
    }
}
