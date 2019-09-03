package ru.vasic2000.myweatherapp;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather_Data_Loader {
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int ALL_GOOD = 200;

    static JSONObject getJSONData(Context context, String city) {
        try {
            URL uri = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.addRequestProperty(KEY, context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;
            while((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable + NEW_LINE);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if(jsonObject.getInt(RESPONSE) != ALL_GOOD) {
                return null;
            }
            return jsonObject;
        } catch (Exception e) {
            return null;
        }

    }


}
