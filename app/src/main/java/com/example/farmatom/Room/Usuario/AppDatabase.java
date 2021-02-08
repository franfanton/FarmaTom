package com.example.farmatom.Room.Usuario;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.farmatom.Model.Usuario;


@Database(entities = {Usuario.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
}
