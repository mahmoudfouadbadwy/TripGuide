package com.iti.intake40.tripguide.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private String google_map_url="https://maps.googleapis.com/maps/api/staticmap?size=500x700";
    private String google_map_key="AIzaSyDIJ9XX2ZvRKCJcFRrl-lRanEtFUow4piM";

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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_map:
                showMapDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private String getGoogleMapTrips(List<Trip> trips)
    {
        StringBuilder url = new StringBuilder(google_map_url);
        for (int i=0;i<trips.size();i++)
        {
            String color = Integer.toHexString(new Random().nextInt(16777215));
            url.append("&markers=color:green")
                    .append("|label:S|")
                    .append(trips.get(i).getStartPoint())
                    .append("&markers=color:red")
                    .append("|label:E|")
                    .append(trips.get(i).getEndPoint())
                    .append("&path=color:0x")
                    .append(color)
                    .append("|weight:5|")
                    .append(trips.get(i).getStartPoint())
                    .append("|")
                    .append(trips.get(i).getEndPoint());
        }
        url.append("&key=").append(google_map_key);
        return url.toString();
    }

    private void showMapDialog()
    {
       // if(_context != null) {
            final Dialog dialog = new Dialog(getActivity());
            ViewGroup viewGroup_Dialog = view.findViewById(R.id.drawer_layout);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.map_dialog, viewGroup_Dialog, false);
            ImageView mapImage = v.findViewById(R.id.map_image_dialog);
            Glide.with(getActivity()).load(getGoogleMapTrips(trips)).into(mapImage);
            v.findViewById(R.id.close_map_dialog).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(v);
            dialog.setFeatureDrawableResource(0,R.drawable.map);
            dialog.setTitle("Map");
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        //}
    }



}
