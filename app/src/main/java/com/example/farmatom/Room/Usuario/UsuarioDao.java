package com.example.farmatom.Room.Usuario;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.farmatom.Model.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {
    @Insert
    void insertar(Usuario usuario);

    @Delete
    void borrar(Usuario usuario);

    @Update
    void actualizar(Usuario usuario);

    @Query("SELECT * FROM usuario WHERE id = :id LIMIT 1")
    Usuario buscar(String id);

    @Query("SELECT * FROM usuario")
    List<Usuario> buscarTodos();

}
