package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TripDetailsActivity extends AppCompatActivity {

    private ImageView tripImage;
    private TextView tripTitle, tvOverview, tvLocation, tvStartingPoint, tvCoordinator,
            tvDate, tvAllowed, tvRegistered, tvRemaining, tvPrice;
    private ImageButton backButton;
    private Button btnBookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_details); // اسم ملف xml تبعك

        // 1) أربطي الـ Views
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
        tvPrice = findViewById(R.id.tvPrice);
        backButton = findViewById(R.id.backButton);
        btnBookNow = findViewById(R.id.btnBookNow);

        // 2) استقبلي الـ index
        int index = getIntent().getIntExtra("index", -1);

        if (index == -1) {
            finish(); // لو صار اشي غلط
            return;
        }

        // 3) حمّلي الليست من SharedPreferences
        ArrayList<Trip> trips = Shared_pref_trip.loadTrips(this);
        Trip trip = trips.get(index);

        // 4) اعرضي البيانات
        tripImage.setImageResource(trip.getImageID());
        tripTitle.setText(trip.getLocation());

        // خليه يضيف تفاصيل إذا عنده (لو اضافتي overview لاحقاً بصير إلك مكان تحطيه)
        tvOverview.setText(trip.getOverview() != null ? trip.getOverview() : "No overview available");

        tvLocation.setText("Location: " + trip.getLocation());
        tvStartingPoint.setText("Starting Point: " + trip.getStarting_point());
        tvCoordinator.setText("Coordinator: " + trip.getCoordinator());
        tvDate.setText("Date: " + trip.getDate());
        tvAllowed.setText("Allowed: " + trip.getAllowed_num());
        tvRegistered.setText("Registered: " + trip.getRegistered_num());
        tvRemaining.setText("Remaining: " + trip.getRemaining_num());
        tvPrice.setText("Price: " + trip.getPrice() + " NIS");

        // 5) رجوع
        backButton.setOnClickListener(v -> finish());

//        // 6) فتح شاشة الحجز
//        btnBookNow.setOnClickListener(v -> {
//            Intent i = new Intent(this, BookingActivity.class);
//            i.putExtra("index", index);
//            startActivity(i);
//        });
    }
}
