package com.example.weatherhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import user.User;
import user.UserFunctions;

public class MainMenuActivity extends AppCompatActivity {

    UserFunctions userFunctions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.main_menu);
        //Cierra la sesión del usuario
        Button logout = findViewById(R.id.button3);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFunctions.logout();
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login);
            }
        });
    }
}