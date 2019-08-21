package com.example.camera.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.camera.holders.AllElement;

import java.util.List;

@Dao
public interface AllElementsDao {

    @Query("SELECT * FROM AllElement")
    List<AllElement> getAll();

    @Insert
    void insertAll(AllElement... allElements);

    @Query("SELECT * FROM AllElement WHERE name = :first")
    AllElement findByName(String first);


    @Delete
    void delete(AllElement user);

}
