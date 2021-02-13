package com.example.farmatom;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vista_principal);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

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
                if (mailUsuario.equals(mailAdmin)) {
                    Toast.makeText(this, "Selecciono Crear Item", Toast.LENGTH_SHORT).show();
                    i = new Intent(HomeActivity.this, AltaItemActivity.class);
                    i.putExtra("mail", mailUsuario);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Disculpe. Usted no puede agregar medicamentos.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.itemListar:
                Toast.makeText(this, "Selecciono ver Lista de Items", Toast.LENGTH_SHORT).show();
                i = new Intent(HomeActivity.this, ListaMedicamentosActivity.class);
                i.putExtra("mail", mailUsuario);
                startActivity(i);
                break;

            case R.id.altaPedido:
                Toast.makeText(this, "Selecciono Realizar Pedido", Toast.LENGTH_SHORT).show();
                i = new Intent(HomeActivity.this, AltaOrdenActivity.class);
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
                i = new Intent(HomeActivity.this, InicioSesionActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}
