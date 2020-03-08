package com.iti.intake40.tripguide.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.model.Trip;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    Context _context;
    List<Trip> trips;
    RecycleAdapter(Context _context ,List<Trip>trips)
    {
        this._context = _context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(_context).inflate(R.layout.custom_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.getTitle().setText(trips.get(position).getTripName());
        holder.getDate().setText(trips.get(position).getDay());
        holder.getTime().setText(trips.get(position).getTime());
        holder.getStatus().setText(trips.get(position).getStatus());
        holder.getFrom().setText(trips.get(position).getStartPoint());
        holder.getTo().setText(trips.get(position).getEndPoint());
        holder.getStart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+holder.getFrom().getText()+"&daddr="+
                        holder.getTo().getText()));
                _context.startActivity(mapIntent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return trips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView title;
        TextView date;
        TextView time;
        TextView status;
        TextView from;
        TextView to ;
        Button start;
        ImageView note;
        CardView cardView;

        public TextView getTitle() {
            if (title == null)
                title = view.findViewById(R.id.Trip_Title);
            return title;
        }


        public TextView getDate() {
            if (date == null)
                date = view.findViewById(R.id.Date);
            return date;
        }

        public TextView getTime() {
            if (time == null)
                time = view.findViewById(R.id.time);
            return time;
        }



        public TextView getStatus() {
             if (status == null)
                 status = view.findViewById(R.id.Status);
            return status;
        }


        public TextView getFrom() {
            if (from == null)
                from = view.findViewById(R.id.From);
            return from;
        }


        public TextView getTo() {
            if(to == null)
                to = view.findViewById(R.id.To);
            return to;
        }


        public Button getStart() {
            if (start == null)
                start = view.findViewById(R.id.Start);
            return start;
        }

        public ImageView getNote() {
            if (note ==  null)
                note = view.findViewById(R.id.Card_Note);
            return note;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public CardView getCardView() {
            if (cardView == null)
                cardView = view.findViewById(R.id.card);
            return cardView;
        }


    }
}
