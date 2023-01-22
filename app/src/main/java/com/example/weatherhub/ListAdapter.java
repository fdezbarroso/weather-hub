package com.example.weatherhub;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<JSONObject> {

    Activity context;
    ArrayList<JSONObject> weatherObjects;

    public ListAdapter(Activity context, ArrayList<JSONObject> objectArrayList) {
        super(context, R.layout.list_item, objectArrayList);

        this.context = context;
        this.weatherObjects = objectArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.list_item, null, true);

        ImageView weatherIcon = rowView.findViewById(R.id.iv_weather);
        TextView temperature = rowView.findViewById(R.id.tv_temp);
        TextView date = rowView.findViewById(R.id.tv_date);
        TextView maxWindSpeed = rowView.findViewById(R.id.tv_maxWindSpeed);

        try {
            JSONObject dailyWeather = weatherObjects.get(position).getJSONObject("daily");

            int weatherCode = dailyWeather.getJSONArray("weathercode").getInt(position);

            if (weatherCode == 0) {
                weatherIcon.setImageResource(R.drawable.sun);
            } else if (weatherCode == 1 || weatherCode == 2 || weatherCode == 3 || weatherCode == 45 || weatherCode == 48) {
                weatherIcon.setImageResource(R.drawable.clouds);
            } else if (weatherCode == 51 || weatherCode == 53 || weatherCode == 55 || weatherCode == 56 || weatherCode == 57 || weatherCode == 61 || weatherCode == 63 || weatherCode == 65 || weatherCode == 66 || weatherCode == 67 || weatherCode == 80 || weatherCode == 81 || weatherCode == 82) {
                weatherIcon.setImageResource(R.drawable.rain);
            } else if (weatherCode == 71 || weatherCode == 73 || weatherCode == 75 || weatherCode == 77 || weatherCode == 85 || weatherCode == 86) {
                weatherIcon.setImageResource(R.drawable.snow);
            } else if (weatherCode == 95 || weatherCode == 96 || weatherCode == 99) {
                weatherIcon.setImageResource(R.drawable.thunderstorm);
            }

            int dayTemp = (dailyWeather.getJSONArray("temperature_2m_max").getInt(position) + dailyWeather.getJSONArray("temperature_2m_min").getInt(position)) / 2;
            temperature.setText(dayTemp + "ºC");

            date.setText(dailyWeather.getJSONArray("time").getString(position));
            maxWindSpeed.setText("Max Wind Speed: " + dailyWeather.getJSONArray("windspeed_10m_max").getInt(position) + "Km/h");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }
}
