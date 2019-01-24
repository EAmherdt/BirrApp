package com.ripani.perren.amherdt.birrapp.modelo;

import java.util.Objects;

public class Cerveza {

    private Integer id;
    private String marca;
    private Estilo estilo; //ipa, apa



    public Cerveza(Integer id, String marca, Estilo tipo) {
        this.id = id;
        this.estilo = tipo;
        this.marca = marca;
    }

    public Cerveza() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Estilo getEstilo() {
        return estilo;
    }

    public void setEstilo(Estilo tipo) {
        this.estilo = tipo;
    }


    @Override
    public String toString() {
        return marca+ "( $" + estilo +")";

    }

}