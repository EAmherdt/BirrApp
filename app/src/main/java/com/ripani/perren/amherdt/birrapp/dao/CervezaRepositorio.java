package com.ripani.perren.amherdt.birrapp.dao;




import com.ripani.perren.amherdt.birrapp.modelo.Cerveza;
import com.ripani.perren.amherdt.birrapp.modelo.Estilo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class CervezaRepositorio {

    private static List<Cerveza> LISTA_PRODUCTOS = new ArrayList<>();
    private static List<Estilo> CATEGORIAS_PRODUCTOS = new ArrayList<>();
    private static boolean FLAG_INICIALIZADO = false;

    private static void inicializar() {
        int id = 0;
        Random rand = new Random();
        CATEGORIAS_PRODUCTOS.add(new Estilo(1, "IPA"));
        CATEGORIAS_PRODUCTOS.add(new Estilo(2, "APA"));
        CATEGORIAS_PRODUCTOS.add(new Estilo(3, "UPA"));
        CATEGORIAS_PRODUCTOS.add(new Estilo(4, "OPA"));
        for (Estilo cat : CATEGORIAS_PRODUCTOS) {
            for (int i = 0; i < 25; i++) {
                LISTA_PRODUCTOS.add(new Cerveza(id++,cat.getNombre(),cat,7.6,5.3));
            }
        }
        FLAG_INICIALIZADO = true;
    }

    public CervezaRepositorio() {
        if (!FLAG_INICIALIZADO) inicializar();
    }

    public List<Cerveza> getLista() {
        return LISTA_PRODUCTOS;
    }

    public List<Estilo> getCategorias() {
        return CATEGORIAS_PRODUCTOS;
    }

    public Cerveza buscarPorId(Integer id) {
        for (Cerveza p : LISTA_PRODUCTOS) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    public List<Cerveza> buscarPorCategoria(Estilo cat) {
        List<Cerveza> resultado = new ArrayList<>();
        for (Cerveza p : LISTA_PRODUCTOS) {
            if (p.getTipo().getId().equals(cat.getId())) resultado.add(p);
        }
        return resultado;
    }
}

