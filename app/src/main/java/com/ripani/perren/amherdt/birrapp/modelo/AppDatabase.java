package com.ripani.perren.amherdt.birrapp.modelo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Local.class},version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocalDao localDao();
}
