package com.example.farmatom.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.farmatom.Model.Medicamento;

import java.util.List;

@Dao
public interface MedicamentoDao {
    @Insert
    void insertar(Medicamento medicamento);

    @Delete
    void borrar(Medicamento medicamento);

    @Update
    void actualizar(Medicamento medicamento);

    @Query("SELECT * FROM medicamento WHERE id = :id LIMIT 1")
    Medicamento buscar(String id);

    @Query("SELECT * FROM medicamento")
    List<Medicamento> buscarTodos();

}

