package com.example.farmatom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AltaUsuarioActivity extends AppCompatActivity {
    private EditText nombre,contrasenia1,contrasenia2,correo;
    private CheckBox terminosycondiciones;

    @SuppressLint("SetTextI18n")
    //se agregaron las annotation para que deje de mostrar warnings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alta_usuario);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        final Button boton = findViewById(R.id.button2);
        nombre = (EditText) findViewById(R.id.nombre);
        contrasenia1 = (EditText) findViewById(R.id.contrasenia1);
        contrasenia2 = (EditText) findViewById(R.id.contrasenia2);
        correo = (EditText) findViewById(R.id.correo);
        terminosycondiciones = (CheckBox) findViewById(R.id.terminosycondiciones);

        terminosycondiciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    boton.setEnabled(true);
                } else {
                    boton.setEnabled(false);
                }

            }
        });

        boton.setOnClickListener(new View.OnClickListener(){
        public void onClick(View view) {
            String emailPattern = getString(R.string.mailCorrecto);
            if(nombre.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "El campo nombre esta vacio.", Toast.LENGTH_SHORT).show();
            }
            else if(contrasenia1.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "El campo contraseña esta vacio.", Toast.LENGTH_SHORT).show();
            }
            else if((!(contrasenia1.getText().toString().equals(contrasenia2.getText().toString()))) ||
                    contrasenia2.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            }
            else if(correo.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "El campo correo esta vacio.", Toast.LENGTH_SHORT).show();
            }
            else if (!(correo.getText().toString().trim().matches(emailPattern))) {
                Toast.makeText(getApplicationContext(),"Ingrese un correo valido",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Registro Exitoso!", Toast.LENGTH_SHORT).show();
            }
        }
        });
    }
}
