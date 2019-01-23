package com.ripani.perren.amherdt.birrapp.modelo;

public class Cerveza {

    private Integer id;
    private String marca;
    private Estilo tipo; //ipa, apa
    private Double ibu; // amargor
    private Double abv; // alcohol


    public Cerveza(Integer id, String marca, Estilo tipo, Double ibu, Double abv) {
        this.id = id;
        this.tipo = tipo;
        this.marca = marca;
        this.ibu = ibu;
        this.abv = abv;
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

    public Estilo getTipo() {
        return tipo;
    }

    public void setTipo(Estilo tipo) {
        this.tipo = tipo;
    }

    public Double getIbu() {
        return ibu;
    }

    public void setIbu(Double ibu) {
        this.ibu = ibu;
    }

    public Double getAbv() {
        return abv;
    }

    public void setAbv(Double abv) {
        this.abv = abv;
    }


}