package com.example.assignment1;

import java.util.ArrayList;

public class DefaultTrips {
    public static ArrayList<Trip> seedTrips() {
        ArrayList<Trip> trips = new ArrayList<>();

        // رحلة 1 — القدس
        Trip t1 = new Trip(
                "Al-Quds",
                "Ramallah",
                "Abdullah Hammad",
                "15/11/2025",
                50,         // allowed_num
                10,         // registered_num
                40,         // remaining_num
                120.0       // price
        );
        t1.setImageID(R.drawable.al_quds);
        t1.setOverview("A cultural and historical trip to the city of Al-Quds, visiting Al-Aqsa Mosque and the Old City.");

        // رحلة 2 — بتّير
        Trip t2 = new Trip(
                "Bitteer",
                "Bethlehem",
                "Sara Khalil",
                "20/12/2025",
                40,
                18,
                22,
                90.0
        );
        t2.setImageID(R.drawable.bitteer);
        t2.setOverview("A relaxing nature trip to the village of Battir, exploring its terraces and ancient irrigation system.");

        // رحلة 3 — أم الصفّا
        Trip t3 = new Trip(
                "Umm Al-Safa",
                "Nablus",
                "Ahmad Barghouthi",
                "05/01/2026",
                30,
                8,
                22,
                65.0
        );
        t3.setImageID(R.drawable.umm_al_safa);
        t3.setOverview("A hiking journey in Umm Al-Safa reserve with beautiful landscapes and wildlife.");

        // رحلة 4 — وادي قانا
        Trip t4 = new Trip(
                "Wadi Qana",
                "Qalqilya",
                "Lina Jaber",
                "11/02/2026",
                35,
                12,
                23,
                80.0
        );
        t4.setImageID(R.drawable.wadi_qana);
        t4.setOverview("An adventurous trail through Wadi Qana, known for its freshwater springs and natural beauty.");

        trips.add(t1);
        trips.add(t2);
        trips.add(t3);
        trips.add(t4);

        return trips;
    }
}
