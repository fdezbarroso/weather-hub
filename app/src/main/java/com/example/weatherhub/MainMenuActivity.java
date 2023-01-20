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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity implements LocationListener{
    TextView city, country;
    private final static int REQUEST_CODE = 100;
    LocationManager locationManager;
    NotificationHandler notificationHandler;
    FrameLayout glFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.main_menu);
        glFrame=(FrameLayout) findViewById(R.id.fragment_loading);
        glFrame.bringToFront();
        city = findViewById(R.id.textView1);
        country = findViewById(R.id.textView14);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, this);
        else
            ActivityCompat.requestPermissions(MainMenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

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
            if (addresses.size() > 0) {
                city.setText(addresses.get(0).getLocality() + ", ");
                country.setText(addresses.get(0).getCountryName());
                glFrame.setVisibility(View.INVISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        notificationHandler = new NotificationHandler(this);
        notificationHandler.createChannels();
        Notification.Builder mBuilder = notificationHandler.createNotification("My notification",
                city.getText().toString() + country.getText().toString(), false);
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