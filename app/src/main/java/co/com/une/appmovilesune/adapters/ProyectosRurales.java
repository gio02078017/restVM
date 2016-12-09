package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

/**
 * Created by Gospina on 18/11/2016.
 */

public class ProyectosRurales implements Serializable{
    String Proyecto;
    String Descripcion;
    String Cobertura;

    public ProyectosRurales(String proyecto, String descripcion, String cobertura) {
        Proyecto = proyecto;
        Descripcion = descripcion;
        Cobertura = cobertura;
    }

    public String getProyecto() {
        return Proyecto;
    }

    public void setProyecto(String proyecto) {
        Proyecto = proyecto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCobertura() {
        return Cobertura;
    }

    public void setCobertura(String cobertura) {
        Cobertura = cobertura;
    }
}
