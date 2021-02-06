package com.example.farmatom.Model;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmatom.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class AdapterListaMedicamentos extends RecyclerView.Adapter<AdapterListaMedicamentos.PruebaViewHolder> implements View.OnClickListener {
    private final List<ListaMedicamentos> listaPrueba;
    private final int CODIGO_ACTIVIDAD;
    private View.OnClickListener listener;
    private String unidades;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public AdapterListaMedicamentos(List<ListaMedicamentos> listaPrueba, int CODIGO_ACTIVIDAD){
        this.listaPrueba = listaPrueba;
        this.CODIGO_ACTIVIDAD = CODIGO_ACTIVIDAD;
    }

    @NonNull
    @Override
    public AdapterListaMedicamentos.PruebaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_card_view2, viewGroup,false);
        v.setOnClickListener(this);
        return new PruebaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PruebaViewHolder pruebaViewHolder, final int i) {
        if(listaPrueba.get(i).getImagen() == R.drawable.medicamento_nuevo) {
            StorageReference gsReference = storage.getReferenceFromUrl("gs://farmatom.appspot.com/images/" + listaPrueba.get(i).getTitulo() + ".jpeg");

            final long MEGABYTES = 3 * 1024 * 1024;
            gsReference.getBytes(MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Exito
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    pruebaViewHolder.ivImagen.setImageBitmap(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Error - Cargar una imagen por defecto
                    pruebaViewHolder.ivImagen.setImageResource(listaPrueba.get(i).getImagen());
                    Log.d("DEBUG", "Error Failure" + exception.getMessage());
                }
            });
        }
        else
            pruebaViewHolder.ivImagen.setImageResource(listaPrueba.get(i).getImagen());
        pruebaViewHolder.tvTitulo.setText(listaPrueba.get(i).getTitulo());
        pruebaViewHolder.tvPrecio.setText(listaPrueba.get(i).getPrecio());
        pruebaViewHolder.tvMiligramos.setText(listaPrueba.get(i).getMiligramos());
        pruebaViewHolder.tvDescripcion.setText(listaPrueba.get(i).getDescripcion());
        if (CODIGO_ACTIVIDAD == 1) {
            pruebaViewHolder.etUnidades.setVisibility(View.VISIBLE);
            pruebaViewHolder.tvUnidades.setVisibility(View.VISIBLE);
            pruebaViewHolder.etUnidades.setText(listaPrueba.get(i).getUnidades());
            pruebaViewHolder.etUnidades.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    pruebaViewHolder.tvAgregarMedicamento.setEnabled(!(pruebaViewHolder.etUnidades.getText().toString().equals("0") || pruebaViewHolder.etUnidades.getText().toString().isEmpty()));
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!(pruebaViewHolder.etUnidades.getText().toString().equals("0") || pruebaViewHolder.etUnidades.getText().toString().isEmpty())) {
                        unidades = pruebaViewHolder.etUnidades.getText().toString();
                        listaPrueba.get(i).setUnidades(unidades);
                        pruebaViewHolder.tvAgregarMedicamento.setVisibility(View.VISIBLE);
                    }
                    else {
                        pruebaViewHolder.tvAgregarMedicamento.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        else {
            pruebaViewHolder.etUnidades.setVisibility(View.INVISIBLE);
            pruebaViewHolder.tvUnidades.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listaPrueba.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public static class PruebaViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen;
        TextView tvTitulo,tvPrecio,tvMiligramos,tvDescripcion,tvUnidades, tvAgregarMedicamento;
        EditText etUnidades;

        public PruebaViewHolder(@NonNull final View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.imagen);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvMiligramos = itemView.findViewById(R.id.tvMiligramos);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvUnidades = itemView.findViewById(R.id.tvUnidades);
            etUnidades = itemView.findViewById(R.id.etUnidades);
            tvAgregarMedicamento = itemView.findViewById(R.id.tvAgregarMedicamento);

        }
    }

}
