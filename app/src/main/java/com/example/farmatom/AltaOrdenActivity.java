package com.example.farmatom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AltaOrdenActivity extends AppCompatActivity {
    private EditText correoPedidoNuevo,direccionPedidoNuevo;
    private RadioButton botonEnvioPedido,botonTakeawayPedido;
    private Button botonUbicacion,listadoPlatos,botonGuardarOrden;
    private TextView nombreMedicamento,totalNuevoPedido;
    private ProgressBar progressBar1;

    @SuppressLint("SetTextI18n")
    //se agregaron las annotation para que deje de mostrar warnings
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alta_orden);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        final Button botonGuardarOrden = findViewById(R.id.botonGuardarOrden);
        correoPedidoNuevo = findViewById(R.id.correoPedidoNuevo);
        direccionPedidoNuevo = findViewById(R.id.direccionPedidoNuevo);
        botonEnvioPedido = (RadioButton) findViewById(R.id.botonEnvioPedido);
        botonTakeawayPedido = (RadioButton) findViewById(R.id.botonTakeawayPedido);
        nombreMedicamento = (TextView) findViewById(R.id.nombreMedicamento);
        totalNuevoPedido = (TextView) findViewById(R.id.totalNuevoPedido);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);

        botonGuardarOrden.setOnClickListener(new View.OnClickListener(){
            String emailPattern = getString(R.string.mailCorrecto);
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
                /*else if(nombreMedicamento.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar un plato del menú.", Toast.LENGTH_SHORT).show();
                }*/
                else {
                    Toast.makeText(getApplicationContext(), "Pedido Guardado!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
