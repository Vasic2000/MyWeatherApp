package ru.vasic2000.myweatherapp;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String POSITIVE_BUTTON_TEXT = "Go!";
    private static final String WEATHER_FRAGMENT_TAG = "Ggggoo!";

    private CityPreference cityPreference;

    // При создании Активити
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityPreference = new CityPreference(this);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container_for_fragment,
                    new WeatherFragment(), WEATHER_FRAGMENT_TAG).commit();
        }
    }

    // Создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    void showInputDialog(){

    }

}
