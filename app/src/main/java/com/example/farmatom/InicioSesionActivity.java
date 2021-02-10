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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.farmatom.Model.ListaMedicamentos;
import com.example.farmatom.Model.Usuario;
import com.example.farmatom.Room.Usuario.AppDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;
import static android.content.ContentValues.TAG;

public class InicioSesionActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private EditText usuario,contrasenia;
    GoogleSignInClient mGoogleSignInClient;
    private final String TAG = "InicioSesionActivity";

    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    //se agregaron las annotation para que deje de mostrar warnings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        //Traemos al activity el boton del layout
        SignInButton botonGoogle = findViewById(R.id.botonGoogle);
        // Inicializar Firebase Auth
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        // Iniciar Session como usuario anónimo
        signInAnonymously();

        //Configuramos Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Iniciar sesion con cuenta google
        botonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

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
                        // Iniciar Session como usuario anónimo de firebase
                        signInAnonymously();
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

    private void signInGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> tareaCompleta){
        try{
            GoogleSignInAccount miCuenta = tareaCompleta.getResult(ApiException.class);
            Log.d(TAG, "Inicio de sesion con Google exitoso!");
            FirebaseGoogleAuth(miCuenta);
        }
        catch (ApiException e){
            Log.d(TAG, "ERROR!!! Inicio de sesion con Google falló!!");
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount cuenta){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(cuenta.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"Tarea completada con exito!");
                    FirebaseUser usuario = mAuth.getCurrentUser();
                    updateUI(usuario);
                } else {
                    Log.d(TAG, "ERROR!!! Falló al realizar la tarea!!");
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            String nombre = account.getDisplayName();
            String mail = account.getEmail();

            Log.d(TAG, "Logueo con Google exitoso.");
            Intent j = new Intent(InicioSesionActivity.this, HomeActivity.class);
            startActivity(j);
            Toast.makeText(InicioSesionActivity.this, "Bienvenido "+nombre, Toast.LENGTH_SHORT).show();
            Toast.makeText(InicioSesionActivity.this, "Ingreso correctamente con el mail: "+mail, Toast.LENGTH_SHORT).show();
        }
    }
}