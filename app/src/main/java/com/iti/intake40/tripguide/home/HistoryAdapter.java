package com.iti.intake40.tripguide.home;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.model.RealTime;
import com.iti.intake40.tripguide.model.Trip;

import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    View view;
    Context _context;
    Boolean animationFlag = false;
    List<Trip> trips;
    Animation fadeIn;
    Animation fadeOut;

    HistoryAdapter(Context _context, List<Trip> trips) {
        this._context = _context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryAdapter.ViewHolder(LayoutInflater.from(_context).inflate(R.layout.history_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.getTripTitle().setText(trips.get(position).getTripName());
        holder.getTripStatus().setText(trips.get(position).getStatus());
        holder.getTripDate().setText(trips.get(position).getDay());
        holder.getTripTime().setText(trips.get(position).getTime());
        holder.getTripStart().setText(trips.get(position).getStartPoint());
        holder.getTripEnd().setText(trips.get(position).getEndPoint());

        // animation
        fadeIn = AnimationUtils.loadAnimation(_context, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(_context, R.anim.fade_out);
        // card action
        holder.getHistoryCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animationFlag == false) {
                    animationFlag = true;
                    holder.getDetails().setVisibility(View.VISIBLE);
                    holder.getDetails().startAnimation(fadeIn);
                } else {
                    animationFlag = false;
                    holder.getDetails().setVisibility(View.GONE);
                    holder.getDetails().startAnimation(fadeOut);
                }
            }
        });

        // delete
        holder.getDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RealTime().deleteTrip(trips.get(position).getKey());
            }
        });

        // show notes
        holder.getNote().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RealTime(trips.get(position).getKey(),HistoryAdapter.this).getNotes();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    // show notes dialog
    public void showNotes(ArrayList<String> notes) {
        CharSequence[] myNoteArray = new CharSequence[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            myNoteArray[i] = notes.get(i);
        }

        if (myNoteArray.length >0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(_context);
            builder.setTitle("Notes");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setItems(myNoteArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }
        else
        {
            Toast.makeText(_context,"No Notes Available",Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tripTitle;
        TextView tripStatus;
        ImageView preview;
        TextView tripDate;
        TextView tripTime;
        TextView tripStart;
        TextView tripEnd;
        Button note;
        Button delete;
        CardView historyCard;
        ConstraintLayout details;

        public ConstraintLayout getDetails() {
            if (details == null)
                details = view.findViewById(R.id.details);
            return details;
        }

        public TextView getTripTitle() {
            if (tripTitle == null)
                tripTitle = view.findViewById(R.id.trip_title);
            return tripTitle;
        }

        public TextView getTripStatus() {
            if (tripStatus == null)
                tripStatus = view.findViewById(R.id.trip_status);
            return tripStatus;
        }

        public ImageView getPreview() {
            if (preview == null)
                preview = view.findViewById(R.id.preview);
            return preview;
        }

        public TextView getTripDate() {
            if (tripDate == null)
                tripDate = view.findViewById(R.id.trip_date);
            return tripDate;
        }

        public TextView getTripTime() {
            if (tripTime == null)
                tripTime = view.findViewById(R.id.trip_time);
            return tripTime;
        }

        public TextView getTripStart() {
            if (tripStart == null)
                tripStart = view.findViewById(R.id.trip_start);
            return tripStart;
        }

        public TextView getTripEnd() {
            if (tripEnd == null)
                tripEnd = view.findViewById(R.id.trip_end);
            return tripEnd;
        }

        public Button getNote() {
            if (note == null)
                note = view.findViewById(R.id.trip_note);
            return note;
        }

        public Button getDelete() {
            if (delete == null)
                delete = view.findViewById(R.id.deleteTrip);
            return delete;
        }

        public CardView getHistoryCard() {
            if (historyCard == null)
                historyCard = view.findViewById(R.id.history_card);
            return historyCard;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
