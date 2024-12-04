package com.example.lab4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import db.Trak;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private List<Trak> trackList;

    public TrackAdapter(List<Trak> trackList) {
        this.trackList = trackList;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        Trak track = trackList.get(position);
        holder.executorTextView.setText(track.getExecutor());
        holder.titleTextView.setText(track.getTitle());
        holder.dateTextView.setText(track.getDate());
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {

        public TextView executorTextView;
        public TextView titleTextView;
        public TextView dateTextView;

        public TrackViewHolder(View itemView) {
            super(itemView);
            executorTextView = itemView.findViewById(R.id.tvFullName);
            titleTextView = itemView.findViewById(R.id.tvTtitle);
            dateTextView = itemView.findViewById(R.id.tvDate);
        }
    }
}
