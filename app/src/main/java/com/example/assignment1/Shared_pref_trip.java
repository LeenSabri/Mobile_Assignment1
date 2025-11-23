package com.example.assignment1;
import android.content.Context;
import android.content.SharedPreferences;


import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Shared_pref_trip extends AppCompatActivity {

    private List<Trip> trips = new ArrayList<>();
    public  static  final String PREF_FILE_NAME ="PREF_FILE";
    public static final String KEY_TRIPS = "TRIPS";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public  static void saveTrips(Context context, ArrayList<Trip> trips){
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(trips);

        editor.putString(KEY_TRIPS, json);
        editor.apply();
    }

    public static ArrayList<Trip> loadTrips(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_TRIPS, null);

        if (json == null) return null;

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Trip>>(){}.getType();
        return gson.fromJson(json, type);
    }


}
