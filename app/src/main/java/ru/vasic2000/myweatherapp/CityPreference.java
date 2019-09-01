package ru.vasic2000.myweatherapp;

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {
    private static final String KEY = "city";
    private static final String MOSCOW = "Moscow";
    private SharedPreferences userPrefereces; //Спец класс для длительного хранения

    CityPreference(Activity activity) {
        userPrefereces = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    String getUserPrefereces() {
        return userPrefereces.getString(KEY, MOSCOW);
    }

    void setCity(String city) {
        userPrefereces.edit().putString(KEY, city).apply();
    }
}


