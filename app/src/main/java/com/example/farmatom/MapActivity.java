package com.example.farmatom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.Random;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView tvMap;
    Button btnMap;
    private GoogleMap myMap;
    LatLng ubicacion, miUbicacion;
    // Agrego marcador a una posicion random
    Random marcadorRandom = new Random();
    // Una direccion aleatoria de 0 a 359 grados
    int direccionRandomEnGrados = marcadorRandom.nextInt(360);

    // Una distancia aleatoria de 100 a 1000 metros
    int distanciaMinima = 100;
    int distanciaMaxima = 1000;
    int distanciaRandomEnMetros = marcadorRandom.nextInt(distanciaMaxima - distanciaMinima) + distanciaMinima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        tvMap = findViewById(R.id.tvMap);
        btnMap = findViewById(R.id.btnMap);
        mapFragment.getMapAsync(this);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void finish() {
        Intent datos = new Intent();
        datos.putExtra("ubicacion", getUbicacion());
        setResult(RESULT_OK, datos);
        super.finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        if (myMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No se han otorgado los permisos de ubicación");
                builder.setMessage("Para utilizar esta funcion, debe conceder los permisos que se le solicitan.");
                builder.setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(MapActivity.this, AltaOrdenActivity.class);
                        startActivity(i);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            myMap.setMyLocationEnabled(true);

            // Mover el mapa a la posición actual.
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            if(location!=null){
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                miUbicacion = new LatLng(latitude, longitude);
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 18));
            }

            dibujarMapa();

        }

        Bundle extras = getIntent().getExtras();
        final MarkerOptions[] nuevoMarcador = {null};
        ubicacion = extras.getParcelable("ubicacion");
        myMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (nuevoMarcador[0] == null) {
                    nuevoMarcador[0] = new MarkerOptions().position(latLng).title(latLng.toString());
                    myMap.addMarker(nuevoMarcador[0]);
                    ubicacion = latLng;
                    tvMap.setVisibility(View.VISIBLE);
                    tvMap.setText("Ubicacion seleccionada: "+latLng.latitude+","+latLng.longitude);
                    btnMap.setVisibility(View.VISIBLE);
                } else {
                    myMap.clear();
                    dibujarMapa();
                    nuevoMarcador[0] = new MarkerOptions().position(latLng).title(latLng.toString());
                    myMap.addMarker(nuevoMarcador[0]);
                    ubicacion = latLng;
                    tvMap.setText("Ubicacion seleccionada: "+latLng.latitude+","+latLng.longitude);
                }
            }
        });
    }

    private void dibujarMapa() {

        LatLng nuevaPosicion = SphericalUtil.computeOffset(
                miUbicacion,
                distanciaRandomEnMetros,
                direccionRandomEnGrados
        );

        //Marcador del local de comidas
        myMap.addMarker(new MarkerOptions().position(nuevaPosicion).title("Farmacia FarmaTom"));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(0x66FF0000);

        // Agregar ambos puntos
        polylineOptions.add(miUbicacion);
        polylineOptions.add(nuevaPosicion);

        myMap.addPolyline(polylineOptions);


    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

}