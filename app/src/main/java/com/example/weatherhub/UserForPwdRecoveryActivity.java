package com.example.weatherhub;

import android.app.ProgressDialog;
import android.content.Context;
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
import user.exceptions.UnregisteredUserException;

public class UserForPwdRecoveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.user_pwd_recovery);

        //Envía a la pestaña de registro e inicia la actividad register
        Button back = findViewById(R.id.button3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(register);
            }
        });
        //Finaliza el proceso de registro al comprobar que es el usuario
        Button register = findViewById(R.id.button2);
        Context context = this;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText userField = findViewById(R.id.editTextPersonName);
                    String username = userField.getText().toString().trim();
                    UserFunctions.confirmIdentityPwdRecover(username);
                    ProgressDialog pdialog = ProgressDialog.show(context, "", "Enviando correo...", true);
                    Intent pwdRecover = new Intent(getApplicationContext(), RecoveryPwdActivity.class);
                    pwdRecover.putExtra("user", username);
                    startActivity(pwdRecover);
                } catch (UnregisteredUserException e) {
                    Toast.makeText(getApplicationContext(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
