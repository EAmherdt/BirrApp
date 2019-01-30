package com.ripani.perren.amherdt.birrapp.modelo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Arrays;
import java.util.List;

@Entity
public class Local {

    public enum Marca {
        PALOHUESO("Palo & Hueso", Arrays.asList(Estilo.IPA,Estilo.GOLDEN)),
        PATAGONIA("Patagonia", Arrays.asList(Estilo.WEISSE)),
        YACARE("Yacare", Arrays.asList(Estilo.IPA,Estilo.GOLDEN,Estilo.REDALE,Estilo.WEISSE));

        private String nombreMarca;
        private List<Estilo> estilos;

        public String getNombreMarca() {
            return nombreMarca;
        }

        public List<Estilo> getEstilos() {
            return estilos;
        }

        private Marca (String nombreMarca, List<Estilo> estilos){
            this.nombreMarca = nombreMarca;
            this.estilos = estilos;
        }
    }
    public enum Estilo {
        IPA("IPA"),GOLDEN("GOLDEN"), REDALE("RED ALE"),WEISSE("WEISSE"),;

        private String nombreMarca;

        private Estilo (String nombreEstilo){
            this.nombreMarca = nombreEstilo;
        }
    }

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String nombre;
    private String horaApertura;
    private String horaCierre;
    private Double latitud;
    private Double longitud;
    @TypeConverters({CervezaConverter.class})
    private List<Cerveza> cervezas;
    private Boolean reservas;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura) {
        this.horaApertura = horaApertura;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getReservas() {
        return reservas;
    }

    public void setReservas(Boolean reservas) {
        this.reservas = reservas;
    }

    public List<Cerveza> getCervezas() {
        return cervezas;
    }

    public void setCervezas(List<Cerveza> cervezas) {
        this.cervezas = cervezas;
    }

    @Override
    public String toString() {
        return this.nombre;

    }
}
