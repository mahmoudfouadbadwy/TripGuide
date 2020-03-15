package com.iti.intake40.tripguide.floatingIcon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.intake40.tripguide.R;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    List<String> list;
    Context context;
    LayoutInflater inflater;


    public RecycleAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapter.ViewHolder holder, int position) {
        System.out.println("note  "+list.get(position));
        holder.getTitleText().setText(list.get(position));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.floating_note, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;


        public TextView getTitleText() {
            if (titleText == null)
                titleText = itemView.findViewById(R.id.note_floating_text);
            return titleText;
        }





        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}
