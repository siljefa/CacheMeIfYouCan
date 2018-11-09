package no.hiof.stianad.cachemeifyoucan;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class WeatherFragment extends Fragment {

    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;

    Handler handler;

    public WeatherFragment(){
        handler = new Handler();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);

        cityField = rootView.findViewById(R.id.city_field);
        updatedField = rootView.findViewById(R.id.updated_field);
        detailsField = rootView.findViewById(R.id.details_field);
        currentTemperatureField = rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = rootView.findViewById(R.id.weather_icon);

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/weatherIcons.ttf");
        Bundle args = getArguments();
        if((args == null) || args.isEmpty())
        {
            return;
        }
        LatLng latLng = new LatLng(args.getDouble("latitude"),args.getDouble("longitude"));
        updateWeatherData(latLng);
    }

    private void updateWeatherData(final LatLng latLng){
        new Thread(){
            public void run(){
                final JSONObject json = FetchFromOpenweathermap.getJSON(getActivity(), latLng);
                if(json == null){
                    handler.post(() -> Toast.makeText(getActivity(),
                            Objects.requireNonNull(getActivity()).getString(R.string.place_not_found),
                            Toast.LENGTH_LONG).show());
                } else {
                    handler.post(() -> renderWeather(json));
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            cityField.setText(
                    String.format("%s, %s",
                            json.getString("name").toUpperCase(Locale.getDefault()),
                            json.getJSONObject("sys").getString("country"))
            );

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");

            detailsField.setText(
                    String.format("%s \nHumidity: %s%% \nPressure: %s hPa",
                            details.getString("description").toUpperCase(Locale.getDefault()),
                            main.getString("humidity"),
                            main.getString("pressure"))
            );

            currentTemperatureField.setText(
                    String.format(Locale.getDefault(), "%.2f℃",
                            main.getDouble("temp")));

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText(String.format("Last update: %s", updatedOn));

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("renderWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_sunny);
            } else {
                icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_thunder);
                    break;
                case 3 : icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_foggy);
                    break;
                case 8 : icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_snowy);
                    break;
                case 5 : icon = Objects.requireNonNull(getActivity()).getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

}
