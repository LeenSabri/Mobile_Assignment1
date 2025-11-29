package com.example.assignment1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Trip> tripList;
    private OnTripClickListener listener;

    public interface OnTripClickListener {
        void onTripClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public RecyclerAdapter(ArrayList<Trip> tripList, OnTripClickListener listener) {
        this.tripList = tripList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, btnEdit, btnDelete;
        TextView textView, date, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.textView);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }


        public void bind(Trip trip, int position, OnTripClickListener listener) {
            textView.setText(trip.getLocation());
            date.setText("Date: " + trip.getDate());
            price.setText("Price: " + trip.getPrice() + " NIS");

            String path = trip.getImageUri();

            if (path != null && !path.trim().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    image.setImageBitmap(bitmap);
                } else {
                    if (trip.getImageID() != 0) {
                        image.setImageResource(trip.getImageID());
                    }
                }
            } else if (trip.getImageID() != 0) {
                image.setImageResource(trip.getImageID());
            }

            itemView.setOnClickListener(v -> listener.onTripClick(position));
            btnEdit.setOnClickListener(v -> listener.onEditClick(position));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(position));
        }

    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.bind(tripList.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void updateList(ArrayList<Trip> newList) {
        this.tripList = newList;
        notifyDataSetChanged();
    }
}
