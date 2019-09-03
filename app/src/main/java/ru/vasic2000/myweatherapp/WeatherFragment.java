package ru.vasic2000.myweatherapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

public class WeatherFragment extends Fragment {

    private static final String LOG_TAG = "WeatherFragment";
    private static final String FONT_FILENAME = "fonts/weathericons_regular_webfont.ttf";

    private final Handler handler = new Handler();

    private Typeface weatherFont;
    private TextView cityTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity weatherActivity = (MainActivity) getActivity();
        weatherFont = Typeface.createFromAsset(weatherActivity.getAssets(), FONT_FILENAME);
        updateWeatherData(new CityPreference(weatherActivity).getUserPrefereces());
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = Weather_Data_Loader.getJSONData(getActivity(), city);
                if(json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        Log.d(LOG_TAG, "json " + json.toString());
        try {
            cityTextView.setText(json.getString("name").toUpperCase() + "," +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");



        } catch (Exception e) {
            Log.d(LOG_TAG, "One or several data missing");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityTextView = rootView.findViewById(R.id.city_field);
        detailsTextView = rootView.findViewById(R.id.details_field);
        currentTemperatureTextView = rootView.findViewById(R.id.temperature_field);
        weatherIcon = rootView.findViewById(R.id.weather_icon_field);
        return rootView;
    }
}
