package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ListaDecosIncluido;

public class Decodificadores implements Serializable {

    public Decodificadores(){

    }

    ArrayList<ArrayList<String>> infoConfigDecos = null;

    String etiquetas =  null;
    String ciudad = null;
    String plan =  null;
    Boolean permisoFideliza = null;

    ArrayList<ItemDecodificador> itemDecodificadors = null;
    ArrayList<ListaDecosIncluido> listaDecosIncluido = null;


    public ArrayList<ArrayList<String>> getInfoConfigDecos() {
        return infoConfigDecos;
    }

    public void setInfoConfigDecos(ArrayList<ArrayList<String>> infoConfigDecos) {
        this.infoConfigDecos = infoConfigDecos;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public ArrayList<ItemDecodificador> getItemDecodificadors() {
        return itemDecodificadors;
    }

    public void setItemDecodificadors(ArrayList<ItemDecodificador> itemDecodificadors) {
        this.itemDecodificadors = itemDecodificadors;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Boolean getPermisoFideliza() {
        return permisoFideliza;
    }

    public void setPermisoFideliza(Boolean permisoFideliza) {
        this.permisoFideliza = permisoFideliza;
    }

    public ArrayList<ListaDecosIncluido> getListaDecosIncluido() {
        return listaDecosIncluido;
    }

    public void setListaDecosIncluido(ArrayList<ListaDecosIncluido> listaDecosIncluido) {
        this.listaDecosIncluido = listaDecosIncluido;
    }
}
