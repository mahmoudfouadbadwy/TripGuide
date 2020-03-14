package com.iti.intake40.tripguide.home;

import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.model.Trip;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private Context _context;
    private RecyclerView historyRecycle;
    private RecyclerView.LayoutManager manager;
    private View view;
    private ArrayList<Trip> trips;
    private DatabaseReference mTripReference;
    private Trip trip;
    private ValueEventListener tripListener;
    private LinearLayout noTrips;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_history,container,false);
        setViews();
        getReference();
        loadTrips();
        return view;
    }

    public Context get_context() {
        return _context;
    }

    public void set_context(Context _context) {
        this._context = _context;
    }

    @Override
    public void onStart() {
        super.onStart();
        configureVisibility();
    }

    private  void setViews()
    {
        trips = new ArrayList<>();
        historyRecycle = view.findViewById(R.id.history_recycle);
        manager = new LinearLayoutManager(get_context());
        historyRecycle.setLayoutManager(manager);
        historyRecycle.setAdapter(new HistoryAdapter(get_context(),trips));
        noTrips = view.findViewById(R.id.noHistoryTrips);
    }

    private void getReference()
    {
        mTripReference = FirebaseDatabase.getInstance().getReference("TripGuide")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
                        if(!trip.getStatus().equalsIgnoreCase("UpComing")) {
                            trip.setKey(data.getKey());
                            trips.add(trip);
                        }
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
            historyRecycle.setAdapter(new HistoryAdapter(getContext(),trips));
            noTrips.setVisibility(View.INVISIBLE);
            historyRecycle.setVisibility(View.VISIBLE);
        }
        else {
            historyRecycle.setVisibility(View.INVISIBLE);
            noTrips.setVisibility(View.VISIBLE);
        }
    }

}
