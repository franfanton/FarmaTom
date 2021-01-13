package com.example.farmatom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AltaItemActivity  extends AppCompatActivity {
    private EditText tituloMedicamento,descripcionMedicamento,precioMedicamento,miligramosMedicamento;

    @SuppressLint("SetTextI18n")
    //se agregaron las annotation para que deje de mostrar warnings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alta_medicamento);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        final Button boton = findViewById(R.id.botonGuardarMedicamento);

        tituloMedicamento = (EditText) findViewById(R.id.tituloMedicamento);
        descripcionMedicamento = (EditText) findViewById(R.id.descripcionMedicamento);
        precioMedicamento = (EditText) findViewById(R.id.precioMedicamento);
        miligramosMedicamento = (EditText) findViewById(R.id.miligramosMedicamento);

        boton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if(tituloMedicamento.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El campo del nombre del medicamento esta vacio.", Toast.LENGTH_SHORT).show();
                }
                else if(descripcionMedicamento.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El campo de la descripción esta vacio.", Toast.LENGTH_SHORT).show();
                }
                else if(precioMedicamento.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El campo precio esta vacio.", Toast.LENGTH_SHORT).show();
                }
                else if(miligramosMedicamento.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "El campo de miligramos esta vacio.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Registro Alta Medicamento Exitoso!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
