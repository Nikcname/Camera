package com.example.camera.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.camera.holders.AllElement;

@Database(entities = {AllElement.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AllElementsDao allElementsDao();
}
