package com.ripani.perren.amherdt.birrapp.modelo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface LocalDao {
    @Query("SELECT * FROM Local")
    List<Local> getAll();

    /*@Query("SELECT * FROM Local")
    List<Local> getByTipo(String pTipo);*/

    @Query("SELECT * FROM Local WHERE id = :idLocal")
    Local getById(int idLocal);

    @Insert
    long insert(Local l);

    @Insert
    void update(Local l);

    @Delete
    void delete(Local l);
}
