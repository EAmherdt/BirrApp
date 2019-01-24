package com.ripani.perren.amherdt.birrapp.dao;




import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.Estilo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class CervezaRepositorio {

    public static List<Cerveza> LISTA_CERVEZA = new ArrayList<>();
    public static List<Estilo> LISTA_ESTILOS = new ArrayList<>();

    public CervezaRepositorio() {
    }

    public List<Cerveza> getLista() {
        return LISTA_CERVEZA;
    }

    public List<Estilo> getCategorias() {
        return LISTA_ESTILOS;
    }

    public Cerveza buscarCervezaPorId(Integer id) {
        for (Cerveza p : LISTA_CERVEZA) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public static Estilo buscarEstiloPorId(Integer id) {
        for (Estilo p : LISTA_ESTILOS) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public List<Cerveza> buscarPorEstilo(Estilo estilo) {
        List<Cerveza> resultado = new ArrayList<>();
        for (Cerveza p : LISTA_CERVEZA) {
            if (p.getEstilo().getId().equals(estilo.getId())) resultado.add(p);
        }
        return resultado;
    }
}

