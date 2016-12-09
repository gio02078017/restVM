package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import co.com.une.appmovilesune.adapters.ProyectosRurales;

/**
 * Created by Gospina on 28/11/2016.
 */

public class CoberturaRural implements Serializable{

    private String barrioConsulta;
    private boolean coberturaRural;
    private ArrayList<ProyectosRurales> arrayProyectosRurales;
    private String proyectoSeleccionado;
    private String codigoProyecto;
    private String coberturaSeleccionada;

    public CoberturaRural(){

    }

    public void limpiar(){
        if(arrayProyectosRurales != null){
            arrayProyectosRurales.clear();
        }

        barrioConsulta = "";
        coberturaRural = false;
        proyectoSeleccionado = "";
        codigoProyecto = "";
        coberturaSeleccionada = "";
    }

    public String getBarrioConsulta() {
        return barrioConsulta;
    }

    public void setBarrioConsulta(String barrioConsulta) {
        this.barrioConsulta = barrioConsulta;
    }

    public boolean isCoberturaRural() {
        return coberturaRural;
    }

    public void setCoberturaRural(boolean coberturaRural) {
        this.coberturaRural = coberturaRural;
    }

    public ArrayList<ProyectosRurales> getArrayProyectosRurales() {
        return arrayProyectosRurales;
    }

    public void setArrayProyectosRurales(ArrayList<ProyectosRurales> arrayProyectosRurales) {
        this.arrayProyectosRurales = arrayProyectosRurales;
    }

    public String getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(String proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
    }

    public String getCodigoProyecto() {
        return codigoProyecto;
    }

    public void setCodigoProyecto(String codigoProyecto) {
        this.codigoProyecto = codigoProyecto;
    }

    public String getCoberturaSeleccionada() {
        return coberturaSeleccionada;
    }

    public void setCoberturaSeleccionada(String coberturaSeleccionada) {
        this.coberturaSeleccionada = coberturaSeleccionada;
    }
}
