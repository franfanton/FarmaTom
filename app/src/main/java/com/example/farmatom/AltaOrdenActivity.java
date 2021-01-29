package com.example.farmatom;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.example.farmatom.Model.Medicamento;
import com.example.farmatom.Model.Orden;
import com.example.farmatom.Room.Orden.AppDatabase;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class AltaOrdenActivity extends AppCompatActivity {
    // NOTIFICACION
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    // FIN NOTIFICACION
    private EditText correoPedidoNuevo,direccionPedidoNuevo;
    private RadioButton botonEnvioPedido,botonTakeawayPedido;
    private Button botonUbicacion,listadoPlatos,botonGuardarOrden;
    private TextView nombreMedicamento,totalNuevoPedido;
    private final int CODIGO_ACTIVIDAD = 1;
    private ProgressBar progressBar1;

    @SuppressLint("SetTextI18n")
    //se agregaron las annotation para que deje de mostrar warnings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alta_orden);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        final Button botonAgregarUbicacion = findViewById(R.id.botonUbicacion);
        final Button botonAgregarMedicamento = findViewById(R.id.listadoMedicamentos);
        final Button botonGuardarOrden = findViewById(R.id.botonGuardarOrden);

        correoPedidoNuevo = findViewById(R.id.correoPedidoNuevo);
        direccionPedidoNuevo = findViewById(R.id.direccionPedidoNuevo);
        botonEnvioPedido = (RadioButton) findViewById(R.id.botonEnvioPedido);
        botonTakeawayPedido = (RadioButton) findViewById(R.id.botonTakeawayPedido);
        nombreMedicamento = (TextView) findViewById(R.id.nombreMedicamento);
        totalNuevoPedido = (TextView) findViewById(R.id.totalNuevoPedido);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);

        botonEnvioPedido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                botonAgregarUbicacion.setEnabled(botonEnvioPedido.isChecked());
            }
        });

        botonTakeawayPedido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                direccionPedidoNuevo.setEnabled(true);
                direccionPedidoNuevo.setText("");
            }
        });

        botonAgregarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AltaOrdenActivity.this, MapActivity.class);
                LatLng ubicacion = new LatLng(0,0);
                i.putExtra("ubicacion",ubicacion);
                startActivityForResult(i, CODIGO_ACTIVIDAD);
            }
        });

        botonAgregarMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(AltaOrdenActivity.this, ListaMedicamentosActivity.class);
                i.putExtra("CODIGO_ACTIVIDAD", CODIGO_ACTIVIDAD);
                startActivityForResult(i,CODIGO_ACTIVIDAD);
            }
        });

        botonGuardarOrden.setOnClickListener(new View.OnClickListener(){
            String emailPattern = getString(R.string.mailCorrecto);
            String tipoEnvio;
            @Override
            public void onClick(View view) {
                if (correoPedidoNuevo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "El campo de correo electronico esta vacío.", Toast.LENGTH_SHORT).show();
                }else if(!(correoPedidoNuevo.getText().toString().trim().matches(emailPattern))) {
                    Toast.makeText(getApplicationContext(),"Ingrese un correo valido",Toast.LENGTH_SHORT).show();
                }else if (direccionPedidoNuevo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "El campo direccion esta vacío.", Toast.LENGTH_SHORT).show();
                }else if(!(botonEnvioPedido.isChecked() || botonTakeawayPedido.isChecked())){
                    Toast.makeText(getApplicationContext(), "Seleccione el tipo de envio.", Toast.LENGTH_SHORT).show();
                }
                else if(nombreMedicamento.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar un medicamento del menú.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String correo = correoPedidoNuevo.getText().toString();
                    String direccion = direccionPedidoNuevo.getText().toString();
                    if(botonEnvioPedido.isChecked()){
                        String tipoEnvio = botonEnvioPedido.getText().toString();
                    }
                    else{
                        String tipoEnvio = botonTakeawayPedido.getText().toString();
                    }

                    Orden nuevaOrden = new Orden(correo, direccion, tipoEnvio);

                    new Task().execute();
                    // ALTA DE NOTIFICACION
                    int delay = 8;
                    String tittle = "FarmaTom";
                    String content = "Orden cargada con exito!";

                    AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "orden-db").allowMainThreadQueries().build();
                    db.ordenDao().insertar(nuevaOrden);

                    scheduleNotification(getNotification(content, tittle), delay);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == CODIGO_ACTIVIDAD) {
            assert data != null;
            if (data.hasExtra("titulo")) {
                String tituloPlato = "- "+data.getStringExtra("titulo")+" x"+data.getStringExtra("unidades");
                String precioPlato = data.getStringExtra("precio");
                int unidad, costo, total;
                unidad = Integer.parseInt(Objects.requireNonNull(data.getStringExtra("unidades")));
                costo = Integer.parseInt(Objects.requireNonNull(data.getStringExtra("precio")));
                total = unidad*costo;
                nombreMedicamento.setText(tituloPlato);
                totalNuevoPedido.setText(total+"");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    class Task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressBar1.setVisibility(View.VISIBLE);
            botonEnvioPedido.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(5000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "salida";
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar1.setVisibility(View.INVISIBLE);
            botonEnvioPedido.setEnabled(true);
        }

    }
    // FUNCIONES NOTIFICACION

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content, String tittle) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle(!tittle.isEmpty() ? tittle : "Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
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
                i = new Intent(AltaOrdenActivity.this, AltaItemActivity.class);
                startActivity(i);
                break;

            case R.id.itemListar:
                Toast.makeText(this, "Selecciono ver Lista de Items", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaOrdenActivity.this, ListaMedicamentosActivity.class);
                startActivity(i);
                break;

            case R.id.altaPedido:
                Toast.makeText(this, "Selecciono Realizar Pedido", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaOrdenActivity.this, AltaOrdenActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
