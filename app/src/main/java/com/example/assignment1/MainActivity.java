package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

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

    private ArrayList<Trip> filteredList;

    private EditText edtSearch;
    private ImageButton btnAddTrip;

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

        recycler   = findViewById(R.id.recyclerTrips);
        edtSearch  = findViewById(R.id.edtSearch);
        btnAddTrip = findViewById(R.id.btnAddTrip);

        tripsList = Shared_pref_trip.loadTrips(this);

        if (tripsList == null || tripsList.isEmpty()) {
            tripsList = DefaultTrips.seedTrips();
            Shared_pref_trip.saveTrips(this, tripsList);
        }

        filteredList = new ArrayList<>(tripsList);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(filteredList, this);
        recycler.setAdapter(adapter);


        btnAddTrip.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddTrip.class);
            startActivity(i);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTrips(s.toString());
            }

            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        tripsList = Shared_pref_trip.loadTrips(this);
        if (tripsList == null) tripsList = new ArrayList<>();

        filterTrips(edtSearch.getText().toString());
    }

    private void filterTrips(String text) {
        filteredList.clear();

        if (text == null || text.trim().isEmpty()) {
            filteredList.addAll(tripsList);
        } else {
            String query = text.toLowerCase().trim();

            for (Trip t : tripsList) {
                if (t.getLocation() != null &&
                        t.getLocation().toLowerCase().contains(query)) {
                    filteredList.add(t);
                }

                // if (t.getCoordinator() != null && t.getCoordinator().toLowerCase().contains(query)) { ... }
            }
        }

        adapter.updateList(filteredList);
    }

    @Override
    public void onTripClick(int position) {
        Trip clickedTrip = filteredList.get(position);

        int realIndex = tripsList.indexOf(clickedTrip);

        Intent i = new Intent(this, TripDetailsActivity.class);
        i.putExtra("index", realIndex);
        startActivity(i);
    }

    @Override
    public void onEditClick(int position) {
        Trip clickedTrip = filteredList.get(position);

        int realIndex = tripsList.indexOf(clickedTrip);
        if (realIndex == -1) return;

        Intent i = new Intent(this, EditTripActivity.class);
        i.putExtra("index", realIndex);
        startActivity(i);
    }

    @Override
    public void onDeleteClick(int position) {

        Trip clickedTrip = filteredList.get(position);
        int realIndex = tripsList.indexOf(clickedTrip);

        if (realIndex == -1) return;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this trip?")
                .setPositiveButton("Delete", (dialog, which) -> {

                    tripsList.remove(realIndex);
                    Shared_pref_trip.saveTrips(this, tripsList);

                    filterTrips(edtSearch.getText().toString());

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
