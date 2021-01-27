package com.example.farmatom.Room.Medicamento;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.farmatom.Model.Medicamento;


@Database(entities = {Medicamento.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MedicamentoDao medicamentoDao();
}
