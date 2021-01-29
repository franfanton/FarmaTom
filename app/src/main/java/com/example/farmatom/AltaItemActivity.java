package com.example.farmatom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.farmatom.Model.Medicamento;
import com.example.farmatom.Room.Medicamento.AppDatabase;

public class AltaItemActivity  extends AppCompatActivity {
    private EditText tituloMedicamento,descripcionMedicamento,precioMedicamento,miligramosMedicamento;
    private Double precioDouble;
    public static int CODIGO_ACTIVIDAD = 0;

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
            @Override
            public void onClick(View view) {
                // Intent i = new Intent(CrearItemActivity.this, PruebaActivity.this);
                precioDouble = Double.parseDouble(precioMedicamento.getText().toString());
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
                    Toast.makeText(getApplicationContext(), "Medicamento Guardado!", Toast.LENGTH_SHORT).show();
                    String titulo = tituloMedicamento.getText().toString();
                    String descripcion = descripcionMedicamento.getText().toString();
                    String miligramos = miligramosMedicamento.getText().toString();

                    Medicamento nuevoMedicamento = new Medicamento(0,titulo,descripcion,precioDouble,miligramos,null);
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "medicamento-db").allowMainThreadQueries().build();
                    db.medicamentoDao().insertar(nuevoMedicamento);

                    // INTENT
                    Intent i = new Intent(AltaItemActivity.this, ListaMedicamentosActivity.class);
                    startActivity(i);
                }
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch(item.getItemId()){
            case R.id.itemCrear:
                Toast.makeText(this, "Selecciono Crear Item", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaItemActivity.this, AltaItemActivity.class);
                startActivity(i);
                break;

            case R.id.itemListar:
                Toast.makeText(this, "Selecciono ver Lista de Items", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaItemActivity.this, ListaMedicamentosActivity.class);
                startActivity(i);
                break;

            case R.id.altaPedido:
                Toast.makeText(this, "Selecciono Realizar Pedido", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaItemActivity.this, AltaOrdenActivity.class);
                startActivity(i);
                break;

            case R.id.cerrarSesion:
                Toast.makeText(this, "Que vuelvas pronto.", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaItemActivity.this, InicioSesionActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
