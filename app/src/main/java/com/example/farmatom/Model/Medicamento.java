package com.example.farmatom.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Medicamento {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String titulo;
    private String descripcion;
    private String precio;
    private String miligramos;
    private int imagen;
    private String unidades;

    public Medicamento(int imagen, String titulo, String descripcion, String precio, String miligramos, String unidades) {

        this.imagen = imagen;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.miligramos = miligramos;
        this.unidades = unidades;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getMiligramos() {
        return miligramos;
    }

    public void setMiligramos(String miligramos) {
        this.miligramos = miligramos;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }
}
