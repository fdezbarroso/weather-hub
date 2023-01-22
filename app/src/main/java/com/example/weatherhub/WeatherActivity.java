package com.example.weatherhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    int weatherCode;
    String city;
    String country;
    String date;
    Double maxWindSpeed;
    int windDirection;
    Double maxTemp;
    Double minTemp;
    ArrayList<Double> hourlyTemps;

    ImageView iv_weather;
    TextView tv_loc, tv_date, tv_maxWindSpeed, tv_windDirection, tv_tempMin, tv_tempMax;
    ListView lv_tempList;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_weather);

        weatherCode = (int) getIntent().getSerializableExtra("weathercode");
        city = (String) getIntent().getSerializableExtra("city");
        country = (String) getIntent().getSerializableExtra("country");
        date = (String) getIntent().getSerializableExtra("date");
        maxWindSpeed = (Double) getIntent().getSerializableExtra("maxWindSpeed");
        windDirection = (int) getIntent().getSerializableExtra("windDirection");
        maxTemp = (Double) getIntent().getSerializableExtra("maxTemp");
        minTemp = (Double) getIntent().getSerializableExtra("minTemp");
        hourlyTemps = (ArrayList<Double>) getIntent().getSerializableExtra("hourlyTemps");

        iv_weather = findViewById(R.id.iv_weather2);
        tv_loc = findViewById(R.id.tv_loc2);
        tv_date = findViewById(R.id.tv_date2);
        tv_maxWindSpeed = findViewById(R.id.tv_maxWindSpeed2);
        tv_windDirection = findViewById(R.id.tv_windDirection);
        tv_tempMin = findViewById(R.id.tv_tempMin);
        tv_tempMax = findViewById(R.id.tv_tempMax);
        lv_tempList = findViewById(R.id.lv_tempList);
        lv_tempList.setClickable(false);

        if (weatherCode == 0) {
            iv_weather.setImageResource(R.drawable.sun);
        } else if (weatherCode == 1 || weatherCode == 2 || weatherCode == 3 || weatherCode == 45 || weatherCode == 48) {
            iv_weather.setImageResource(R.drawable.clouds);
        } else if (weatherCode == 51 || weatherCode == 53 || weatherCode == 55 || weatherCode == 56 || weatherCode == 57 || weatherCode == 61 || weatherCode == 63 || weatherCode == 65 || weatherCode == 66 || weatherCode == 67 || weatherCode == 80 || weatherCode == 81 || weatherCode == 82) {
            iv_weather.setImageResource(R.drawable.rain);
        } else if (weatherCode == 71 || weatherCode == 73 || weatherCode == 75 || weatherCode == 77 || weatherCode == 85 || weatherCode == 86) {
            iv_weather.setImageResource(R.drawable.snow);
        } else if (weatherCode == 95 || weatherCode == 96 || weatherCode == 99) {
            iv_weather.setImageResource(R.drawable.thunderstorm);
        }

        if (city != "empty")
            tv_loc.setText(city + "\n" + country);
        else
            tv_loc.setText(country);

        tv_date.setText(date);
        tv_maxWindSpeed.setText("Max Wind Speed\n" + maxWindSpeed + "Km/h");
        tv_windDirection.setText("Wind Direction\n" + windDirection + "º");
        tv_tempMin.setText("T. min\n" + minTemp + "ºC");
        tv_tempMax.setText("T. max\n" + maxTemp + "ºC");

        ArrayList<String> hourlyTempsText = new ArrayList<>();
        for (int i = 0; i < hourlyTemps.size(); i++) {
            if (i < 10)
                hourlyTempsText.add("0" + i + ":00  - " + hourlyTemps.get(i) + "ºC");
            else
                hourlyTempsText.add(i + ":00 - " + hourlyTemps.get(i) + "ºC");
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hourlyTempsText);
        lv_tempList.setAdapter(arrayAdapter);
    }
}