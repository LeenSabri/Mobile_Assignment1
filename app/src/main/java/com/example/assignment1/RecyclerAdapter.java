package com.example.assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Trip> trips;
    private OnTripClickListener listener;

    public interface OnTripClickListener {
        void onTripClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public RecyclerAdapter(ArrayList<Trip> trips, OnTripClickListener listener) {
        this.trips = trips;
        this.listener = listener;
    }

    public void updateList(ArrayList<Trip> newTrips) {
        this.trips = newTrips;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trip trip = trips.get(position);
        CardView cardView = holder.cardView;

        ImageView imageView = cardView.findViewById(R.id.image);
        imageView.setImageResource(trip.getImageID());

        TextView txt = cardView.findViewById(R.id.textView);
        txt.setText(trip.getLocation());

        TextView tvDate = cardView.findViewById(R.id.date);
        tvDate.setText("Date: " + trip.getDate());

        TextView tvPrice = cardView.findViewById(R.id.price);
        tvPrice.setText("Price: " + trip.getPrice() + "$");

        ImageView btnEdit = cardView.findViewById(R.id.btnEdit);
        ImageView btnDelete = cardView.findViewById(R.id.btnDelete);

        cardView.setOnClickListener(v -> {
            if (listener != null) listener.onTripClick(position);
        });

        btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(position);
        });

        btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(position);
        });

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
