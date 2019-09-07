package ru.vasic2000.myweatherapp;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Weather_Data_Loader {
    private static final String OPEN_WEATHER_MAP_API = "https://api.openweathermap.org/";
    private static final String OPEN_WEATHER_METHOD = "data/2.5/weather";
    private static final String PARAM = "q";
    private static final String KEY = "APPID";
    private static final String MY_KEY = "07795d846f9c55c418379de9d14962e7";
    private static final String NEW_LINE = "\n";

    static JSONObject getJSONData(String city) {
        URL uri = generateURL(city);
        try {
            JSONObject jsonObject = new JSONObject(getResponseFromURL(uri));
            return jsonObject;
        } catch (IOException e) {
            return null;
        } catch (JSONException j) {
            return null;
        }
    }


    public static URL generateURL(String city) {
        URL url = null;
        Uri builtUri = Uri.parse(OPEN_WEATHER_MAP_API + OPEN_WEATHER_METHOD)
                .buildUpon()
                .appendQueryParameter(PARAM, city)
                .appendQueryParameter(KEY, MY_KEY)
                .build();
        try {
            String appi = builtUri.toString();
            url = new URL(appi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;
        while((tempVariable = br.readLine()) != null) {
            rawData.append(tempVariable + NEW_LINE);
        }
        br.close();
        return rawData.toString();
    }

}
