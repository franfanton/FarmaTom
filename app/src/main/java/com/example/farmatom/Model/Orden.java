package com.example.farmatom.Model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Orden {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String correo;
    private String direccion;
    private String tipoEnvio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orden(String correo, String direccion, String tipoEnvio) {
        this.correo = correo;
        this.direccion = direccion;
        this.tipoEnvio = tipoEnvio;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }
}

