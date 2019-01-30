package com.ripani.perren.amherdt.birrapp.modelo;

import android.arch.persistence.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CervezaConverter {

    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<Cerveza> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Cerveza>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<Cerveza> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Cerveza>>() {
        }.getType();
        List<Cerveza> cervezas = gson.fromJson(optionValuesString, type);
        return cervezas;
    }
}
