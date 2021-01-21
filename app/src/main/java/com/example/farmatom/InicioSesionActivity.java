package com.example.farmatom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InicioSesionActivity extends AppCompatActivity {
    private EditText usuario,contrasenia;

    @SuppressLint("SetTextI18n")
    //se agregaron las annotation para que deje de mostrar warnings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        final Button botonInicioSesion = findViewById(R.id.botonInicioSesion);
        final Button botonCrearCuenta = findViewById(R.id.botonCrearCuenta);

        usuario = (EditText) findViewById(R.id.usuario);
        contrasenia = (EditText) findViewById(R.id.contrasenia);

        botonInicioSesion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String emailPattern = getString(R.string.mailCorrecto);
                Intent i;
                if(usuario.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El campo usuario esta vacio.", Toast.LENGTH_SHORT).show();
                }
                else if (!(usuario.getText().toString().trim().matches(emailPattern))) {
                    Toast.makeText(getApplicationContext(),"Ingrese un usuario valido",Toast.LENGTH_SHORT).show();
                }
                else if(contrasenia.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El campo contraseña esta vacio.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Logueo Exitoso!", Toast.LENGTH_SHORT).show();
                    i = new Intent(InicioSesionActivity.this, HomeActivity.class);
                    startActivity(i);
                }
            }
        });

        botonCrearCuenta.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i;
                i = new Intent(InicioSesionActivity.this, AltaUsuarioActivity.class);
                startActivity(i);
            }
        });
    }

}
