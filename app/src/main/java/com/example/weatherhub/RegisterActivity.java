package com.example.weatherhub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import user.User;
import user.UserFunctions;
import user.exceptions.MissingArgumentsException;
import user.exceptions.MissmatchPwdException;
import user.exceptions.UserAlreadyExistException;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.register);
        //Vuelve al login
        Button login = findViewById(R.id.button3);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login);
            }
        });

        //Inicia el registro
        Button register = findViewById(R.id.button2);
        Context context = this;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText username = findViewById(R.id.editTextTextPersonName);
                    EditText pwd = findViewById(R.id.editTextTextPassword);
                    EditText pwd2 = findViewById(R.id.editTextTextPassword2);
                    EditText email = findViewById(R.id.editTextTextEmailAddress);
                    User user = UserFunctions.newUser(username.getText().toString().trim(), pwd.getText().toString().trim()
                            , pwd2.getText().toString().trim()
                            , email.getText().toString().trim());
                    ProgressDialog pdialog = ProgressDialog.show(context, "", "Enviando correo...", true);
                    Intent confirm = new Intent(getApplicationContext(), ConfirmActivity.class);
                    confirm.putExtra("user",user);
                    startActivity(confirm);
                } catch (UserAlreadyExistException e) {
                    Toast.makeText(getApplicationContext(), "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                } catch (MissingArgumentsException e) {
                    Toast.makeText(getApplicationContext(), "Rellene todos los campos por favor", Toast.LENGTH_SHORT).show();
                } catch (MissmatchPwdException e) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
