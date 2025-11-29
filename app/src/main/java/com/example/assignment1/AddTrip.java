package com.example.assignment1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTrip extends AppCompatActivity {

    private EditText edtLocation, edtStartingPoint, edtCoordinator, edtDate, edtPrice;
    private NumberPicker npAllowed;
    private RadioGroup rgTripType;
    private CheckBox chkLunch;
    private Switch swFamily;
    private Button btnSaveTrip, btnChooseImage;
    private ImageView imgTripPreview;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_form);

        edtLocation = findViewById(R.id.edtLocation);
        edtStartingPoint = findViewById(R.id.edtStartingPoint);
        edtCoordinator = findViewById(R.id.edtCoordinator);
        edtDate = findViewById(R.id.edtDate);
        edtPrice = findViewById(R.id.edtPrice);
        npAllowed = findViewById(R.id.npAllowed);
        rgTripType = findViewById(R.id.rgTripType);
        chkLunch = findViewById(R.id.chkLunch);
        swFamily = findViewById(R.id.swFamily);
        btnSaveTrip = findViewById(R.id.btnSaveTrip);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        imgTripPreview = findViewById(R.id.imgTripPreview);

        npAllowed.setMinValue(1);
        npAllowed.setMaxValue(100);


        edtDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH);
            int d = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
                    edtDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year), y, m, d);
            dialog.show();
        });

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSaveTrip.setOnClickListener(v -> {
            String location = edtLocation.getText().toString().trim();
            String start = edtStartingPoint.getText().toString().trim();
            String coord = edtCoordinator.getText().toString().trim();
            String date = edtDate.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();

            if (location.isEmpty() || date.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int allowed = npAllowed.getValue();
            double price = Double.parseDouble(priceStr);

            Trip trip = new Trip(location, start, coord, date, allowed, 0, allowed, price);

            int selectedRadioId = rgTripType.getCheckedRadioButtonId();
            if (selectedRadioId != -1) {
                RadioButton selected = findViewById(selectedRadioId);
                trip.setTripType(selected.getText().toString());
            }

            trip.setIncludeLunch(chkLunch.isChecked());
            trip.setFamilyFriendly(swFamily.isChecked());

            String storedImagePath = null;

            if (selectedImageUri != null) {
                storedImagePath = saveImageToInternalStorage(selectedImageUri);
            }


            if (storedImagePath != null) {
                trip.setImageUri(storedImagePath);
            }

//            if (selectedImageUri != null) {
//                trip.setImageUri(selectedImageUri.toString());
//            }

            ArrayList<Trip> trips = Shared_pref_trip.loadTrips(this);
            if (trips == null) trips = new ArrayList<>();
            trips.add(trip);
            Shared_pref_trip.saveTrips(this, trips);

            Toast.makeText(this, "Trip added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            selectedImageUri = data.getData();

            // 1) Show preview (optional but fine)
            imgTripPreview.setImageURI(selectedImageUri);

            // 2) Copy the image into app internal storage
            String storedImagePath = saveImageToInternalStorage(selectedImageUri);

            if (storedImagePath != null) {
                Toast.makeText(this, "Image saved to app storage ✅", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save image ❌", Toast.LENGTH_SHORT).show();
            }

            // You can now pass `storedImagePath` later when saving the Trip object
        }
    }

    private String saveImageToInternalStorage(Uri sourceUri) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = getContentResolver().openInputStream(sourceUri);

            // 2) أنشئي فولدر داخلي لحفظ الصور: /data/data/your.package.name/files/images
            File imagesDir = new File(getFilesDir(), "images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }


            String fileName = "trip_" + System.currentTimeMillis() + ".jpg";
            File imageFile = new File(imagesDir, fileName);


            out = new FileOutputStream(imageFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            return imageFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
