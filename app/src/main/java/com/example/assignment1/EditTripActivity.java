package com.example.assignment1;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class EditTripActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView imgTripPreview;
    private Button btnChooseImage, btnSaveTrip;
    private EditText edtLocation, edtStartingPoint, edtCoordinator, edtDate, edtPrice;
    private NumberPicker npAllowed;
    private RadioGroup rgTripType;
    private RadioButton rbAdventure, rbCulture;
    private CheckBox chkLunch;
    private Switch swFamily;
    private TextView txtFormTitle;

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final int PERMISSION_REQUEST_READ_IMAGES = 201;

    private Uri selectedImageUri = null;
    private ArrayList<Trip> trips;
    private int tripIndex = -1;
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_form);

        backButton      = findViewById(R.id.backButton);
        imgTripPreview  = findViewById(R.id.imgTripPreview);
        btnChooseImage  = findViewById(R.id.btnChooseImage);
        btnSaveTrip     = findViewById(R.id.btnSaveTrip);
        txtFormTitle    = findViewById(R.id.txtFormTitle);

        edtLocation     = findViewById(R.id.edtLocation);
        edtStartingPoint= findViewById(R.id.edtStartingPoint);
        edtCoordinator  = findViewById(R.id.edtCoordinator);
        edtDate         = findViewById(R.id.edtDate);
        edtPrice        = findViewById(R.id.edtPrice);

        npAllowed       = findViewById(R.id.npAllowed);
        rgTripType      = findViewById(R.id.rgTripType);
        rbAdventure     = findViewById(R.id.rbAdventure);
        rbCulture       = findViewById(R.id.rbCulture);
        chkLunch        = findViewById(R.id.chkLunch);
        swFamily        = findViewById(R.id.swFamily);

        txtFormTitle.setText("Edit Trip");
        btnSaveTrip.setText("Update Trip");

        npAllowed.setMinValue(1);
        npAllowed.setMaxValue(100);

        tripIndex = getIntent().getIntExtra("index", -1);
        trips = Shared_pref_trip.loadTrips(this);

        if (trips == null || tripIndex < 0 || tripIndex >= trips.size()) {
            Toast.makeText(this, "Invalid trip data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentTrip = trips.get(tripIndex);

        fillFormWithTripData(currentTrip);

        backButton.setOnClickListener(v -> finish());

        edtDate.setOnClickListener(v -> showDatePicker());

        btnChooseImage.setOnClickListener(v -> checkPermissionAndOpenGallery());

        btnSaveTrip.setOnClickListener(v -> updateTrip());
    }

    private void fillFormWithTripData(Trip trip) {
        edtLocation.setText(trip.getLocation());
        edtStartingPoint.setText(trip.getStarting_point());
        edtCoordinator.setText(trip.getCoordinator());
        edtDate.setText(trip.getDate());
        edtPrice.setText(String.valueOf(trip.getPrice()));

        npAllowed.setValue(trip.getAllowed_num());

        String type = trip.getTripType();
        if ("Adventure".equalsIgnoreCase(type)) {
            rbAdventure.setChecked(true);
        } else if ("Culture".equalsIgnoreCase(type)) {
            rbCulture.setChecked(true);
        } else {
            rgTripType.clearCheck();
        }

        chkLunch.setChecked(trip.isIncludeLunch());
        swFamily.setChecked(trip.isFamilyFriendly());

        try {
            if (trip.getImageUri() != null) {
                Uri uri = Uri.parse(trip.getImageUri());
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgTripPreview.setImageBitmap(bitmap);
            } else {
                imgTripPreview.setImageResource(trip.getImageID());
            }
        } catch (Exception e) {
            e.printStackTrace();
            imgTripPreview.setImageResource(R.drawable.image_placeholder);
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);      // 0-based
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    String dateStr = String.format("%02d/%02d/%04d", d, (m + 1), y);
                    edtDate.setText(dateStr);
                },
                year, month, day
        );
        dialog.show();
    }

    private void checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_READ_IMAGES
                );
            } else {
                openGallery();
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_READ_IMAGES) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this,
                        "Permission denied. Cannot open gallery.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            selectedImageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgTripPreview.setImageBitmap(bitmap);
                imgTripPreview.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateTrip() {
        String location      = edtLocation.getText().toString().trim();
        String startingPoint = edtStartingPoint.getText().toString().trim();
        String coordinator   = edtCoordinator.getText().toString().trim();
        String date          = edtDate.getText().toString().trim();
        String priceStr      = edtPrice.getText().toString().trim();

        if (location.isEmpty() || startingPoint.isEmpty()
                || coordinator.isEmpty() || date.isEmpty()
                || priceStr.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Invalid price value",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int allowed     = npAllowed.getValue();
        int registered  = currentTrip.getRegistered_num();
        int remaining   = allowed - registered;

        String tripType;
        int checkedId = rgTripType.getCheckedRadioButtonId();
        if (checkedId == R.id.rbAdventure) {
            tripType = "Adventure";
        } else if (checkedId == R.id.rbCulture) {
            tripType = "Culture";
        } else {
            tripType = "General";
        }

        boolean includeLunch   = chkLunch.isChecked();
        boolean familyFriendly = swFamily.isChecked();

        currentTrip.setLocation(location);
        currentTrip.setStarting_point(startingPoint);
        currentTrip.setCoordinator(coordinator);
        currentTrip.setDate(date);
        currentTrip.setPrice(price);
        currentTrip.setAllowed_num(allowed);
        currentTrip.setRemaining_num(remaining);
        currentTrip.setTripType(tripType);
        currentTrip.setIncludeLunch(includeLunch);
        currentTrip.setFamilyFriendly(familyFriendly);

        if (selectedImageUri != null) {
            currentTrip.setImageUri(selectedImageUri.toString());
        }

        trips.set(tripIndex, currentTrip);
        Shared_pref_trip.saveTrips(this, trips);

        Toast.makeText(this,
                "Trip updated successfully",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}
