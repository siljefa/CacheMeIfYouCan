package no.hiof.stianad.cachemeifyoucan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

public class FetchFromOpenweathermap {

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&APPID=%s";

    public static JSONObject getJSON(Context context, LatLng Latlng){
        try {
            URL url = new URL(
                    String.format(OPEN_WEATHER_MAP_API,
                            Latlng.latitude,
                            Latlng.longitude,
                            context.getString(R.string.openWeather_appId)
                    ));

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder json = new StringBuilder(1024);
            String i;
            while((i=reader.readLine())!=null)
                json.append(i).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
}