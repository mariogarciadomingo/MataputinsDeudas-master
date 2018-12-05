package com.example.mario.mataputinsdeudas;

/**
 * Created by mario on 25/01/2018.
 */

public class deuda {

    String ImProducte;


    String titol, preu, marca, fecha, id;

    public deuda(String id, String imProducte, String titol, String preu, String marca, String fecha) {
        this.id = id;
        ImProducte = imProducte;
        this.titol = titol;
        this.preu = preu;

        this.marca = marca;
        this.fecha = fecha;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImProducte() {
        return ImProducte;
    }

    public void setImProducte(String imProducte) {
        ImProducte = imProducte;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getPreu() {
        return preu;
    }

    public void setPreu(String preu) {
        this.preu = preu;
    }


}
