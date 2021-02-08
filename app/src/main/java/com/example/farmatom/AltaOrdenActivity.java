package com.example.farmatom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.farmatom.Model.Orden;
import com.example.farmatom.Room.Orden.AppDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AltaOrdenActivity extends AppCompatActivity {
    // NOTIFICACION
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    // FIN NOTIFICACION
    private EditText correoPedidoNuevo,direccionPedidoNuevo;
    private RadioButton botonEnvioPedido,botonTakeawayPedido;
    private TextView nombreMedicamento,totalNuevoPedido;
    private final int CODIGO_ACTIVIDAD = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
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

        pedirPermisos();

        botonEnvioPedido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                direccionPedidoNuevo.setEnabled(botonEnvioPedido.isChecked());
                botonAgregarUbicacion.setEnabled(botonEnvioPedido.isChecked());
            }
        });

        botonTakeawayPedido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            final String emailPattern = getString(R.string.mailCorrecto);
            String tipoEnvio;
            @Override
            public void onClick(View view) {
                if (correoPedidoNuevo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "El campo de correo electronico esta vacío.", Toast.LENGTH_SHORT).show();
                }else if(!(correoPedidoNuevo.getText().toString().trim().matches(emailPattern))) {
                    Toast.makeText(getApplicationContext(),"Ingrese un correo valido",Toast.LENGTH_SHORT).show();
                }else if (botonEnvioPedido.isChecked() && direccionPedidoNuevo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "El campo direccion esta vacío.", Toast.LENGTH_SHORT).show();
                }else if(!(botonEnvioPedido.isChecked() || botonTakeawayPedido.isChecked())){
                    Toast.makeText(getApplicationContext(), "Seleccione el tipo de envio.", Toast.LENGTH_SHORT).show();
                }
                else if(nombreMedicamento.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar un medicamento del menú.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String correo = correoPedidoNuevo.getText().toString();
                    String direccion = null;
                    String tipoEnvio = null;
                    if(botonEnvioPedido.isChecked()){
                        tipoEnvio = "Envio";
                        direccion = direccionPedidoNuevo.getText().toString();
                    }
                    else{
                        tipoEnvio = "Take Away";
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

    private void pedirPermisos() {
        //Solicitud de permiso para la ubicación
        //Comprueba si el permiso ya fue dado
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permisos de Ubicación ya otorgados. Gracias.", Toast.LENGTH_SHORT).show();
        } else {
            // Si entra al else, no se dió permiso aún.
            /* Al obtener el permiso denegado se llamara al metodo que esta dentro del if
            para explicar al usuario porque necesita estos permisos. */
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                AlertDialog.Builder builder = new AlertDialog.Builder(AltaOrdenActivity.this);
                builder.setTitle("SOLICITUD DE PERMISOS").setMessage("Se necesitan los permisos de localización, ¿Acepta concederlos?")
                        .setPositiveButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ActivityCompat.requestPermissions(AltaOrdenActivity.this,
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                LOCATION_PERMISSION_REQUEST_CODE);
                                    }
                                })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "No se dieron permisos para conocer su ubicacion", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
            }
            else {
                //Solicita los permisos con el cuadro de dialogo de sistema.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @SuppressLint("SetTextI18n")
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
            }  else if (data.hasExtra("ubicacion")){
                LatLng ubicacion = data.getParcelableExtra("ubicacion");
                if (ubicacion.longitude != 0 && ubicacion.latitude != 0){
                    //direccionPedidoNuevo.setText(ubicacion.toString());
                    direccionPedidoNuevo.setText("Ubicación agregada a través de Google Maps.");
                    direccionPedidoNuevo.setEnabled(false);
                }
                else {
                    Toast.makeText(this, "No se agregó ninguna ubicación, intente nuevamente.", Toast.LENGTH_LONG).show();
                    direccionPedidoNuevo.setText("");
                }
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
    @SuppressLint("NonConstantResourceId")
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
                break;

            case R.id.cerrarSesion:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut();
                Toast.makeText(this, "Que vuelvas pronto.", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaOrdenActivity.this, InicioSesionActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
