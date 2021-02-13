package com.example.farmatom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.farmatom.Model.Medicamento;
import com.example.farmatom.Room.Medicamento.AppDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class AltaItemActivity  extends AppCompatActivity {
    private EditText tituloMedicamento,descripcionMedicamento,precioMedicamento,miligramosMedicamento;
    private TextView tvContador, tvLimite;
    private String precio;
    private ImageView fotoMedicamento;
    public static int CODIGO_ACTIVIDAD = 0;
    static final int CAMARA_REQUEST = 1;
    static final int GALERIA_REQUEST = 2;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @SuppressLint("SetTextI18n")
    //se agregaron las annotation para que deje de mostrar warnings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alta_medicamento);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        Button camara = findViewById(R.id.botonSacarFoto);
        Button galeria = findViewById(R.id.botonBuscarGaleria);
        fotoMedicamento = findViewById(R.id.imageViewMedicamento);

        final Button guardar = findViewById(R.id.botonGuardarMedicamento);

        tituloMedicamento = (EditText) findViewById(R.id.tituloMedicamento);
        descripcionMedicamento = (EditText) findViewById(R.id.descripcionMedicamento);
        precioMedicamento = (EditText) findViewById(R.id.precioMedicamento);
        miligramosMedicamento = (EditText) findViewById(R.id.miligramosMedicamento);

        tvContador = findViewById(R.id.tvContador);
        tvLimite = findViewById(R.id.tvLimite);

        Bundle extras = getIntent().getExtras();
        final String mailUsuario = extras.getString("mail");

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tituloMedicamento.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Primero debe cargar el nombre del plato.", Toast.LENGTH_SHORT).show();
                } else lanzarCamara();
            }
        });

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tituloMedicamento.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Primero debe cargar el nombre del plato.", Toast.LENGTH_SHORT).show();
                } else abrirGaleria();
            }
        });

        descripcionMedicamento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvContador.setText(String.valueOf(charSequence.length())+" / 35");
                if (String.valueOf(charSequence.length()).equals("35")){
                    tvLimite.setVisibility(View.VISIBLE);
                    tvContador.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    tvLimite.setVisibility(View.INVISIBLE);
                    tvContador.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        guardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Intent i = new Intent(CrearItemActivity.this, PruebaActivity.this);
                precio = precioMedicamento.getText().toString();
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

                    Medicamento nuevoMedicamento = new Medicamento(R.drawable.medicamento_nuevo,titulo,descripcion,precio,miligramos,"0");
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "medicamento-db").allowMainThreadQueries().build();
                    db.medicamentoDao().insertar(nuevoMedicamento);
                    // INTENT
                    Intent i = new Intent(AltaItemActivity.this, ListaMedicamentosActivity.class);
                    i.putExtra("mail", mailUsuario);
                    startActivity(i);
                }
            }
        });
    }

    private void lanzarCamara() {
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camaraIntent, CAMARA_REQUEST);
    }

    private void abrirGaleria() {
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeriaIntent, GALERIA_REQUEST);
    }

    @SuppressLint({"SetTextI18n", "MissingSuperCall"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && (requestCode == CAMARA_REQUEST || requestCode == GALERIA_REQUEST)){
            assert data != null;
            Bundle extras = data.getExtras();
            if (requestCode == CAMARA_REQUEST) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytes = baos.toByteArray(); // Imagen en arreglo de bytes
                fotoMedicamento.setImageBitmap(imageBitmap);

                StorageReference storageRef = storage.getReference();

                // Creamos una referencia a 'images/medicamento_id.jpeg'
                final StorageReference medicamentoImagesRef = storageRef.child("images/"+tituloMedicamento.getText().toString()+".jpeg");

                UploadTask uploadTask = medicamentoImagesRef.putBytes(bytes);

                // Registramos un listener para saber el resultado de la operación
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continuamos con la tarea para obtener la URL
                        return medicamentoImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            // URL de descarga del archivo
                            Uri downloadUri = task.getResult();
                        } else {
                            // Fallo
                            Toast.makeText(getApplicationContext(),"FALLÓ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Uri imageUri = data.getData();
                fotoMedicamento.setImageURI(imageUri);

                StorageReference storageRef = storage.getReference();

                // Creamos una referencia a 'images/medicamento_id.jpeg'
                final StorageReference medicamentoImagesRef = storageRef.child("images/"+tituloMedicamento.getText().toString()+".jpeg");

                UploadTask uploadTask = medicamentoImagesRef.putFile(imageUri);

                // Registramos un listener para saber el resultado de la operación
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continuamos con la tarea para obtener la URL
                        return medicamentoImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            // URL de descarga del archivo
                            Uri downloadUri = task.getResult();
                        } else {
                            // Fallo
                            Toast.makeText(getApplicationContext(),"FALLÓ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            fotoMedicamento.setTag("MedicamentoNuevo");
            fotoMedicamento.setVisibility(View.VISIBLE);
        }
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
        Bundle extras = getIntent().getExtras();
        String mailUsuario = extras.getString("mail");
        String mailAdmin = "admin@farmatom.com";
        switch(item.getItemId()){
            case R.id.itemCrear:
//                if (mailUsuario.equals(mailAdmin)) {
//                    Toast.makeText(this, "Selecciono Crear Item", Toast.LENGTH_SHORT).show();
//                    i = new Intent(AltaItemActivity.this, AltaItemActivity.class);
//                    i.putExtra("mail", mailUsuario);
//                    startActivity(i);
//                } else {
//                    Toast.makeText(this, "Disculpe. Usted no puede agregar medicamentos.", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(this, "Usted ya está dando de alta un medicamento.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.itemListar:
                Toast.makeText(this, "Selecciono ver Lista de Items", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaItemActivity.this, ListaMedicamentosActivity.class);
                i.putExtra("mail", mailUsuario);
                startActivity(i);
                break;

            case R.id.altaPedido:
                Toast.makeText(this, "Selecciono Realizar Pedido", Toast.LENGTH_SHORT).show();
                i = new Intent(AltaItemActivity.this, AltaOrdenActivity.class);
                i.putExtra("mail", mailUsuario);
                startActivity(i);
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
                i = new Intent(AltaItemActivity.this, InicioSesionActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
