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
import user.exceptions.MissmatchPwdException;

public class RecoveryPwdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.password_recovery);

        //Envía a la pestaña de registro e inicia la actividad register
        Button back = findViewById(R.id.button3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(), UserForPwdRecoveryActivity.class);
                startActivity(register);
            }
        });
        //Finaliza el cambio de contraseña del usuario
        Button pwdRecover = findViewById(R.id.button2);
        pwdRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    EditText code = findViewById(R.id.editTextPersonName);
                    EditText pwd = findViewById(R.id.editTextPassword);
                    EditText confirmPwd = findViewById(R.id.editTextPassword2);
                    String  u = getIntent().getStringExtra("user");
                    UserFunctions.pwdRecovery(u, Integer.parseInt(code.getText().toString().trim()), pwd.getText().toString().trim(), confirmPwd.getText().toString().trim());
                    Intent register = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(register);
                } catch (IncorrectEmailConfirmationException e) {
                    Toast.makeText(getApplicationContext(), "Código incorrecto", Toast.LENGTH_SHORT).show();
                } catch (MissmatchPwdException e) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
