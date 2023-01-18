package com.example.weatherhub;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import user.User;
import user.UserFunctions;
import user.exceptions.IncorrectPwdException;
import user.exceptions.UnregisteredUserException;

public class MainActivity extends AppCompatActivity {

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Usuario maestro
        User master = new User("master", UserFunctions.toHexString(UserFunctions.encodedPwd("1234")), "pmejiaskudelka@gmail.com", null);
        UserFunctions.getUsers().put("master", master);

        //Envía a la pestaña de registro e inicia la actividad register
        settings = getSharedPreferences("my_prefs", MODE_PRIVATE);
        boolean isChecked = settings.getBoolean("logged", false);

        if (isChecked) {
            Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(i);
        }

        Button register = findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(register);
            }
        });

        //Envía a la pestaña de registro e inicia la actividad register
        TextView forgetPwd = findViewById(R.id.textView5);
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pwd = new Intent(getApplicationContext(), UserForPwdRecoveryActivity.class);
                startActivity(pwd);
            }
        });

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save a flag in shared preferences indicating that the user wants to stay logged in
                SharedPreferences.Editor editor = settings.edit();
                if (isChecked)
                    editor.putBoolean("logged", true);
                else
                    editor.putBoolean("logged", false);
                editor.apply();
            }
        });
        //Realiza el login en la app
        Button login = findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText username = findViewById(R.id.editTextTextPersonName);
                    EditText pwd = findViewById(R.id.editTextTextPassword);
                    UserFunctions.login(username.getText().toString().trim(), pwd.getText().toString().trim());
                    Intent log = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(log);
                } catch (UnregisteredUserException e) {
                    Toast.makeText(getApplicationContext(), "Usuario no registradp", Toast.LENGTH_SHORT).show();
                } catch (IncorrectPwdException e) {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}