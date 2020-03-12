package com.iti.intake40.tripguide.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.intake40.tripguide.R;
import com.iti.intake40.tripguide.addTrip.AddTrip;
import com.iti.intake40.tripguide.model.RealTime;
import com.iti.intake40.tripguide.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> implements PopupMenu.OnMenuItemClickListener {
    View view;
    Context _context;
    List<Trip> trips;
    PopupMenu popup;
    MenuInflater inflater;
    int _position;
    RealTime model;
    Dialog noteDialog;
    LayoutInflater layoutInflater;
    View dialogView;
    ViewGroup viewGroup_Dialog;
    TextView tripName_note;
    EditText note_content;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    Intent editIntent;

    RecycleAdapter(Context _context, List<Trip> trips) {
        this._context = _context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(_context).inflate(R.layout.custom_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.getTitle().setText(trips.get(position).getTripName());
        holder.getDate().setText(trips.get(position).getDay());
        holder.getTime().setText(trips.get(position).getTime());
        holder.getStatus().setText(trips.get(position).getStatus());
        holder.getFrom().setText("From : " + trips.get(position).getStartPoint());
        holder.getTo().setText("To : " + trips.get(position).getEndPoint());
        holder.getStart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + holder.getFrom().getText() + "&daddr=" +
                        holder.getTo().getText()));
                _context.startActivity(mapIntent);
            }
        });
        holder.getOptions().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
                _position = position;
            }
        });

        holder.getNote().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _position = position;
                model = new RealTime(trips.get(_position).getKey(), RecycleAdapter.this);
                model.getNotes();
            }
        });

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void showMenu(View v) {
        popup = new PopupMenu(_context, v);
        popup.setOnMenuItemClickListener(this);// to implement on click event on items of menu
        inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.options_menu, popup.getMenu());
        popup.show();
    }

    // show add notes dialog .
    private void showDialog() {
        noteDialog = new Dialog(_context);
        layoutInflater = (LayoutInflater) _context.getSystemService(_context.LAYOUT_INFLATER_SERVICE);
        viewGroup_Dialog = view.findViewById(R.id.drawer_layout);
        dialogView = layoutInflater.inflate(R.layout.add_notes_dialog, viewGroup_Dialog, false);
        // trip title
        tripName_note = dialogView.findViewById(R.id.trip_name_addnotes);
        tripName_note.setText(trips.get(_position).getTripName());
        // note content
        note_content = dialogView.findViewById(R.id.note_content);
        // save note
        dialogView.findViewById(R.id.save_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!note_content.getText().toString().trim().isEmpty()) {
                    model.addNote(note_content.getText().toString(), trips.get(_position).getKey());
                    noteDialog.dismiss();
                    Toast.makeText(_context, "Note Added Successfully", Toast.LENGTH_LONG).show();

                }
            }
        });
        // cancel
        dialogView.findViewById(R.id.cancel_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteDialog.dismiss();
            }
        });
        noteDialog.setTitle("Add Note");
        noteDialog.setContentView(dialogView);
        noteDialog.setCanceledOnTouchOutside(false);
        noteDialog.show();
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

    public void showAlertDialog(String msg) {
        builder = new AlertDialog.Builder(_context);
        builder.setMessage(msg);
        builder.setTitle("Attention");
        builder.setIcon(R.drawable.error);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model = new RealTime();
                model.deleteTrip(trips.get(_position).getKey());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void editTrip() {
        editIntent = new Intent(_context, AddTrip.class);
        editIntent.putExtra("tripName",trips.get(_position).getTripName());
        editIntent.putExtra("tripDate",trips.get(_position).getDay());
        editIntent.putExtra("tripTime",trips.get(_position).getTime());
        editIntent.putExtra("from",trips.get(_position).getStartPoint());
        editIntent.putExtra("to",trips.get(_position).getEndPoint());
        editIntent.putExtra("direction",trips.get(_position).getDirection());
        editIntent.putExtra("repeat",trips.get(_position).getRepeating());
        editIntent.putExtra("key",trips.get(_position).getKey());
        _context.startActivity(editIntent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (model == null) {
            model = new RealTime();
        }

        switch (item.getItemId()) {
            case R.id.addNotes:
                showDialog();
                break;
            case R.id.done_menu:
                model.makeDone(trips.get(_position).getKey());
                break;
            case R.id.edit:
                   editTrip();
                break;
            case R.id.cancel_menu:
                model.cancelTrip(trips.get(_position).getKey());
                break;
            case R.id.delete_menu:
                showAlertDialog("Are You Sure You Want To Delete Trip ?");

                break;
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;
        TextView time;
        TextView status;
        TextView from;
        TextView to;
        Button start;
        ImageView note;
        ImageView options;
        CardView cardView;

        public ImageView getOptions() {
            if (options == null)
                options = view.findViewById(R.id.optionMenu);

            return options;
        }

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
            if (to == null)
                to = view.findViewById(R.id.To);
            return to;
        }


        public Button getStart() {
            if (start == null)
                start = view.findViewById(R.id.Start);
            return start;
        }

        public ImageView getNote() {
            if (note == null)
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
