package ru.vasic2000.myweatherapp;

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

import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {

    private static final String LOG_TAG = "WeatherFragment";
    private static final String FONT_FILENAME = "font/weathericons.ttf";

    private final Handler handler = new Handler();

    private TextView cityTextView;
    private TextView detailsTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity weatherActivity = (MainActivity) getActivity();
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
            cityTextView.setText(json.getString("name").toUpperCase(Locale.US) + "," +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            String st1 = details.getString("description").toUpperCase(Locale.US);
            String st2 = main.getString("humidity");
            String st3 = main.getString("pressure");


            detailsTextView.setText(st1 + "\n" +
                    "Humidity: " + st2 + "%\n" + st3 + "hpa");

            Double temp = main.getDouble("temp") - 273.15;

            currentTemperatureTextView.setText(String.format("%.2f", temp) + " °C");

            int actualId = details.getInt("id");
            long sunrise = json.getJSONObject("sys").getLong("sunrise")* 1000;
            long sunset = json.getJSONObject("sys").getLong("sunset") * 1000;

            setWeatherIcon(actualId, sunrise, sunset);

        } catch (Exception e) {
            Log.d(LOG_TAG, "One or several data missing");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if(actualId == 800) {
            long currentTime = new Date().getTime();
            if(currentTime > sunrise && currentTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2:
                    icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.weather_drizzly);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rainy);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 7:
                    icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                default:
                    break;
            }
        }
        weatherIcon.setText(icon);
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

    public void changeCity(String city) {
        updateWeatherData(city);
    }
}
