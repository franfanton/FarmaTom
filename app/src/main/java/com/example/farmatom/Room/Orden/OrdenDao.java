package com.example.farmatom.Room.Orden;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.farmatom.Model.Orden;

import java.util.List;

@Dao
public interface OrdenDao {
    @Insert
    void insertar(Orden orden);

    @Delete
    void borrar(Orden orden);

    @Update
    void actualizar(Orden orden);

    @Query("SELECT * FROM orden WHERE id = :id LIMIT 1")
    Orden buscar(String id);

    @Query("SELECT * FROM orden")
    List<Orden> buscarTodos();

}
