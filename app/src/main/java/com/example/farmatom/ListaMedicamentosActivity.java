package com.example.farmatom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmatom.Model.AdapterListaMedicamentos;
import com.example.farmatom.Model.ListaMedicamentos;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

import static androidx.recyclerview.widget.RecyclerView.*;

public class ListaMedicamentosActivity extends AppCompatActivity{
    //PRUEBA
    List<ListaMedicamentos> listaPruebas;
    private int CODIGO_ACTIVIDAD = -1;
    String titulo, precio, unidades;

    // FIN PRUEBA
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_card_view);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        // PRUEBA
        final RecyclerView rvPruebas = findViewById(R.id.rvPruebas);
        listaPruebas = new ArrayList<>();
        listaPruebas.add(new ListaMedicamentos(R.drawable.aspirina,"Aspirina","Remedio para dolor de cabeza.","350","750", "0"));
        listaPruebas.add(new ListaMedicamentos(R.drawable.actron,"Actron","Remedio para dolores musculares.","300","600", "0"));
        listaPruebas.add(new ListaMedicamentos(R.drawable.rivotril,"Clonacepan","El clonazepam es un fármaco perteneciente al grupo de las benzodiazepinas que actúa sobre el sistema nervioso central, con propiedades ansiolíticas, anticonvulsionantes, miorrelajantes, sedantes, hipnóticas y estabilizadoras del estado de ánimo.","480","2", "0"));
        listaPruebas.add(new ListaMedicamentos(R.drawable.escitalopram,"Escitalopram","El escitalopram es un fármaco antidepresivo perteneciente al grupo de los inhibidores de la recaptación de serotonina, por lo que está emparentado con otros fármacos de este grupo con los que comparte muchas características, tales como la fluoxetina y la paroxetina. Se trata del enantiómero S del citalopram racémico.","275","10", "0"));
        listaPruebas.add(new ListaMedicamentos(R.drawable.cocaina,"Cocaina","Es un alcaloide tropano y fuerte estimulante utilizado sobre todo como droga recreativa.","230","740", "0"));
        listaPruebas.add(new ListaMedicamentos(R.drawable.marihuana,"Marihuana","El cannabis, \u200B\u200B es una droga depresora del sistema nervioso.\u200BContiene dentro de sus compuestos la molécula de THC o tetrahidrocannabinol.","750","100", "0"));
        listaPruebas.add(new ListaMedicamentos(R.drawable.lcd,"LCD","La dietilamida de ácido lisérgico, \u200B LSD-25 o simplemente LSD, también llamada lisérgida y comúnmente conocida como ácido, es una sustancia psicodélica semisintética que se obtiene de la ergolina y de la familia de las triptaminas y que produce efectos psicotrópicos.","525","2", "0"));

        rvPruebas.setHasFixedSize(true);
        LayoutManager lManager = new LinearLayoutManager(this);
        rvPruebas.setLayoutManager(lManager);
        // FIN PRUEBA

        Bundle extras = getIntent().getExtras();
        final AdapterListaMedicamentos adapter;
        if(extras != null) {
            CODIGO_ACTIVIDAD = extras.getInt("CODIGO_ACTIVIDAD");
            adapter = new AdapterListaMedicamentos(listaPruebas, CODIGO_ACTIVIDAD);
            rvPruebas.setAdapter(adapter);
            getActivity(extras, CODIGO_ACTIVIDAD, adapter, rvPruebas);
        }
        else{
            adapter = new AdapterListaMedicamentos(listaPruebas, CODIGO_ACTIVIDAD);
            rvPruebas.setAdapter(adapter);
        }
    }

    @Override
    public void finish() {
        Intent datos = new Intent();
        datos.putExtra("titulo",getTitulo());
        datos.putExtra("precio",getPrecio());
        datos.putExtra("unidades",getUnidades());
        setResult(RESULT_OK, datos);
        super.finish();
    }

    public void getActivity(Bundle extras, final int CODIGO_ACTIVIDAD, final AdapterListaMedicamentos adapter, final RecyclerView rvPruebas){
        switch (CODIGO_ACTIVIDAD){
            case 0:
                String datoTitulo = extras.getString("titulo");
                String datoDescripcion = extras.getString("descripcion");
                double datoPrecio = extras.getDouble("precio");
                String datoMiligramos = extras.getString("miligramos");
                listaPruebas.add(new ListaMedicamentos(R.drawable.medicamento,datoTitulo,datoDescripcion, Double.toString(datoPrecio),datoMiligramos,null));
                break;

            case 1:
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setTitulo(listaPruebas.get(rvPruebas.getChildAdapterPosition(view)).getTitulo());
                        setPrecio(listaPruebas.get(rvPruebas.getChildAdapterPosition(view)).getPrecio());
                        setUnidades(listaPruebas.get(rvPruebas.getChildAdapterPosition(view)).getUnidades());
                        if (unidades.equals("0")){
                            Toast.makeText(getApplicationContext(),"Debe encargar al menos una unidad. Unidades: "+unidades,Toast.LENGTH_SHORT).show();
                        }
                        else {
                            finish();
                        }
                    }
                });
                break;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch(item.getItemId()){
            case R.id.itemRegistrar:
                Toast.makeText(this, "Selecciono Registrarse", Toast.LENGTH_SHORT).show();
                i = new Intent(ListaMedicamentosActivity.this, AltaUsuarioActivity.class);
                startActivity(i);
                break;

            case R.id.itemCrear:
                Toast.makeText(this, "Selecciono Crear Item", Toast.LENGTH_SHORT).show();
                i = new Intent(ListaMedicamentosActivity.this, AltaItemActivity.class);
                startActivity(i);
                break;

            case R.id.itemListar:
                Toast.makeText(this, "Selecciono ver Lista de Items", Toast.LENGTH_SHORT).show();
                i = new Intent(ListaMedicamentosActivity.this, ListaMedicamentosActivity.class);
                //startActivity(i);
                break;

            case R.id.altaPedido:
                Toast.makeText(this, "Selecciono Realizar Pedido", Toast.LENGTH_SHORT).show();
                i = new Intent(ListaMedicamentosActivity.this, AltaOrdenActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}

