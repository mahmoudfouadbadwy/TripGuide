package com.iti.intake40.tripguide.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iti.intake40.tripguide.R;

public class UpComingFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    LinearLayout noTrips;
    View view;
    FloatingActionButton floatingActionButton;
    Context _context;
    UpComingFragment()
    {}
    UpComingFragment(Context _context){
        this._context = _context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_upcoming,container,false);
         recyclerView = view.findViewById(R.id.Trips_RecycleView);
         manager = new  LinearLayoutManager(container.getContext());
         recyclerView.setLayoutManager(manager);
         noTrips = view.findViewById(R.id.No_Trips_Layout);
         noTrips.setVisibility(View.INVISIBLE);
         recyclerView.setAdapter(new RecycleAdapter(container.getContext()));
         floatingActionButton = view.findViewById(R.id.floatingActionButton);
         floatingActionButton.setOnClickListener(this);
         return  view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.floatingActionButton: Toast.makeText(_context,"Add Trip",Toast.LENGTH_LONG).show();
        }
    }

}
