package com.example.weatherhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import user.User;
import user.UserFunctions;
import user.exceptions.IncorrectEmailConfirmationException;

public class ConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.confirmation);

        //Envía a la pestaña de registro e inicia la actividad register
        Button back = findViewById(R.id.button3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(register);
            }
        });
        //Finaliza el proceso de registro al comprobar que es el usuario
        Button register = findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText code = findViewById(R.id.editTextPersonName);
                    User u = (User) getIntent().getSerializableExtra("user");
                    UserFunctions.checkEmail(u, Integer.parseInt(code.getText().toString().trim()));
                    Intent register = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(register);
                } catch (IncorrectEmailConfirmationException e) {
                    Toast.makeText(getApplicationContext(), "Código incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
