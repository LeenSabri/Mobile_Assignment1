package com.example.assignment1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TripDetailsActivity extends AppCompatActivity {

    ImageView tripImage;
    TextView tripTitle, tvOverview, tvLocation, tvStartingPoint, tvCoordinator,
            tvDate, tvAllowed, tvRegistered, tvRemaining, tripType, tripLunch, tvFamily, tvPrice;

    Button btnBookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_details);

        tripImage = findViewById(R.id.tripImage);
        tripTitle = findViewById(R.id.tripTitle);
        tvOverview = findViewById(R.id.tvOverview);
        tvLocation = findViewById(R.id.tvLocation);
        tvStartingPoint = findViewById(R.id.tvStartingPoint);
        tvCoordinator = findViewById(R.id.tvCoordinator);
        tvDate = findViewById(R.id.tvDate);
        tvAllowed = findViewById(R.id.tvAllowed);
        tvRegistered = findViewById(R.id.tvRegistered);
        tvRemaining = findViewById(R.id.tvRemaining);
        tripType = findViewById(R.id.tripType);
        tripLunch = findViewById(R.id.tripLunch);
        tvFamily = findViewById(R.id.tvFamily);
        tvPrice = findViewById(R.id.tvPrice);
        btnBookNow = findViewById(R.id.btnBookNow);

        int position = getIntent().getIntExtra("index", -1);
        if (position != -1) {
            ArrayList<Trip> trips = Shared_pref_trip.loadTrips(this);
            Trip trip = trips.get(position);

            tripTitle.setText(trip.getLocation());
            tvOverview.setText(trip.getOverview());
            tvLocation.setText("Location: " + trip.getLocation());
            tvStartingPoint.setText("Starting Point: " + trip.getStarting_point());
            tvCoordinator.setText("Coordinator: " + trip.getCoordinator());
            tvDate.setText("Date: " + trip.getDate());
            tvAllowed.setText("Allowed: " + trip.getAllowed_num());
            tvRegistered.setText("Registered: " + trip.getRegistered_num());
            tvRemaining.setText("Remaining: " + trip.getRemaining_num());
            tripType.setText("Trip Type: " + trip.getTripType());
            tripLunch.setText("Include Lunch: " + (trip.isIncludeLunch() ? "Yes" : "No"));
            tvFamily.setText("Family Friendly: " + (trip.isFamilyFriendly() ? "Yes" : "No"));
            tvPrice.setText("Price: " + trip.getPrice() + " NIS");

            String path = trip.getImageUri();

            if (path != null && !path.trim().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    tripImage.setImageBitmap(bitmap);
                } else {
                    if (trip.getImageID() != 0) {
                        tripImage.setImageResource(trip.getImageID());
                    }
                }
            } else if (trip.getImageID() != 0) {
                tripImage.setImageResource(trip.getImageID());
            }
        }
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        btnBookNow.setOnClickListener(v -> {
            ArrayList<Trip> trips = Shared_pref_trip.loadTrips(this);
            int index = getIntent().getIntExtra("index", -1);

            if (index != -1 && trips != null && index < trips.size()) {
                Trip trip = trips.get(index);

                int registered = trip.getRegistered_num();
                int allowed = trip.getAllowed_num();

                if (registered < allowed) {
                    trip.setRegistered_num(registered + 1);
                    trip.setRemaining_num(allowed - (registered + 1));

                    trips.set(index, trip);
                    Shared_pref_trip.saveTrips(this, trips);

                    Toast.makeText(this, "You have successfully booked this trip!", Toast.LENGTH_SHORT).show();

                    finish();

                } else {
                    Toast.makeText(this, "Sorry, this trip is already full!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error processing booking!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
