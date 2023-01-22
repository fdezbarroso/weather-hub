package com.example.weatherhub;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity implements LocationListener {
    TextView currentLocation, currentTemp, location;
    FrameLayout glFrame;
    EditText address;
    Button getTemp;
    ListView temps;
    String country;
    String city;

    private final static int REQUEST_CODE = 100;
    LocationManager locationManager;
    NotificationHandler notificationHandler;

    final WeatherDataService weatherDataService = new WeatherDataService(MainMenuActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.main_menu);
        glFrame = (FrameLayout) findViewById(R.id.fragment_loading);
        glFrame.bringToFront();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        currentLocation = findViewById(R.id.tv_currentLoc);
        currentTemp = findViewById(R.id.tv_currentTemp);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0F, this);
            else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0F, this);
            else
                Toast.makeText(MainMenuActivity.this, "No se puede acceder a la ubicación", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(MainMenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        address = findViewById(R.id.et_address);
        getTemp = findViewById(R.id.btn_getTemp);
        location = findViewById(R.id.tv_loc);
        temps = findViewById(R.id.lv_weatherList);

        getTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Geocoder geocoder = new Geocoder(MainMenuActivity.this);
                List<Address> addressList;
                try {
                    addressList = geocoder.getFromLocationName(address.getText().toString(), 1);
                    if (addressList != null) {
                        double latitude = addressList.get(0).getLatitude();
                        double longitude = addressList.get(0).getLongitude();
                        weatherDataService.getTempForecast(latitude, longitude, new WeatherDataService.VolleyResponseListener() {
                            @Override
                            public void onError(String message) {
                                Toast.makeText(MainMenuActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(JSONObject response) {
                                JSONObject weatherResponse = response;
                                Address loc = addressList.get(0);
                                String city = loc.getLocality();
                                String country = loc.getCountryName();
                                if(city != null)
                                    location.setText(loc.getLocality() + "\n" + loc.getCountryName());
                                else
                                    location.setText(loc.getCountryName());
                                ArrayList<JSONObject> weatherObjectList = new ArrayList<>();
                                for (int i = 0; i < 7; i++) {
                                    weatherObjectList.add(response);
                                }

                                ListAdapter listAdapter = new ListAdapter(MainMenuActivity.this, weatherObjectList);
                                temps.setAdapter(listAdapter);

                                temps.setClickable(true);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Cierra la sesión del usuario
        Button logout = findViewById(R.id.button3);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences("my_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("logged", false);
                editor.apply();
                finish();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null) {
                Address currentLoc = addresses.get(0);
                city = currentLoc.getLocality();
                country = currentLoc.getCountryName();
                if (city != null)
                    currentLocation.setText(city + "\n" + country);
                else
                    currentLocation.setText(country);
                weatherDataService.getTempCurrent(currentLoc.getLatitude(), currentLoc.getLongitude(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainMenuActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject weatherResponse = response;
                        try {
                            currentTemp.setText(weatherResponse.getDouble("temperature") + "ºC");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                glFrame.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                currentLocation.setText("");
                currentTemp.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView share = findViewById(R.id.imageView);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToShare;
                if (city != null)
                    textToShare = "La temperatura es de " + currentTemp.getText().toString() + " en la localidad de " + city + " en " + country;
                else
                    textToShare = "La temperatura es de " + currentTemp.getText().toString() + " en " + country;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        String temperature = currentTemp.getText().toString();
        if (!temperature.equals("")) {
            notificationHandler = new NotificationHandler(this);
            notificationHandler.createChannels();
            Notification.Builder mBuilder;
            mBuilder = notificationHandler.createNotification(temperature,
                    city + ", " + country, false);
            notificationHandler.getManager().notify(1, mBuilder.build());
            Intent resultIntent = new Intent(this, MainMenuActivity.class);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, FLAG_IMMUTABLE);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}