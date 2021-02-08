package com.example.farmatom.Room.Orden;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.farmatom.Model.Orden;


@Database(entities = {Orden.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract OrdenDao ordenDao();
}
