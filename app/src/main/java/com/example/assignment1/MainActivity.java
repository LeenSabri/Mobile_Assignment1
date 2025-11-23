package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnTripClickListener {

    private RecyclerView recycler;
    private RecyclerAdapter adapter;
    private ArrayList<Trip> tripsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recycler = findViewById(R.id.recyclerTrips);

        tripsList = Shared_pref_trip.loadTrips(this);

        if (tripsList == null) {
            tripsList = DefaultTrips.seedTrips();
            Shared_pref_trip.saveTrips(this, tripsList);
        }

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(tripsList, this);
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tripsList = Shared_pref_trip.loadTrips(this);
        adapter.updateList(tripsList);
    }

    @Override
    public void onTripClick(int position) {
        Intent i = new Intent(this, TripDetailsActivity.class);
        i.putExtra("index", position);
        startActivity(i);
    }

    @Override
    public void onEditClick(int position) {
//        Intent i = new Intent(this, EditTripActivity.class);
//        i.putExtra("index", position);
//        startActivity(i);
    }

    @Override
    public void onDeleteClick(int position) {

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this trip?")
                .setPositiveButton("Delete", (dialog, which) -> {

                    tripsList.remove(position);
                    Shared_pref_trip.saveTrips(this, tripsList);
                    adapter.notifyItemRemoved(position);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
