package com.example.farmatom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.farmatom.Model.ListaMedicamentos;
import com.example.farmatom.Model.Usuario;
import com.example.farmatom.Room.Usuario.AppDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import static android.content.ContentValues.TAG;

public class InicioSesionActivity extends AppCompatActivity {
    private EditText usuario,contrasenia;
    private FirebaseAuth mAuth;
    @SuppressLint("SetTextI18n")
    //se agregaron las annotation para que deje de mostrar warnings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Iniciar Session como usuario anónimo
        signInAnonymously();

        final Button botonInicioSesion = findViewById(R.id.botonInicioSesion);
        final Button botonCrearCuenta = findViewById(R.id.botonCrearCuenta);

        usuario = (EditText) findViewById(R.id.usuario);
        contrasenia = (EditText) findViewById(R.id.contrasenia);

        botonInicioSesion.setOnClickListener(new View.OnClickListener(){
            int bandera = 0;
            public void onClick(View view) {
                String emailPattern = getString(R.string.mailCorrecto);
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
                    // LISTA DE ROOM
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "usuario-db").allowMainThreadQueries().build();
                    List<Usuario> listaUsuarios = db.usuarioDao().buscarTodos();

                    for (int i = 0; i < listaUsuarios.size(); i++) {
                        if (listaUsuarios.get(i).getCorreo().equals(usuario.getText().toString()) && listaUsuarios.get(i).getContrasenia().equals(contrasenia.getText().toString())) {
                            bandera = 1;
                        }
                    }
                    if(bandera == 1){
                        Toast.makeText(getApplicationContext(), "Logueo con exito.", Toast.LENGTH_SHORT).show();
                        Intent j = new Intent(InicioSesionActivity.this, HomeActivity.class);
                        startActivity(j);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "La cuenta y/o la contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                    }
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

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Exito
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // Error
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
