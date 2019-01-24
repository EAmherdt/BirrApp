package com.ripani.perren.amherdt.birrapp.modelo;

import java.util.Objects;

public class Estilo {


    private Integer id;
    private String nombre;
    //private Double ibu; // amargor
    //private Double abv; // alcohol


    public Estilo(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
       // this.ibu = ibu;
       // this.abv = abv;
    }

    public Estilo() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estilo estilo = (Estilo) o;
        return Objects.equals(id, estilo.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

   /* public Double getIbu() {
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

*/


}
