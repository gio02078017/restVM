package co.com.une.appmovilesune.model;

import java.io.Serializable;

/**
 * Created by Gospina on 18/12/2017.
 */

public class AdicionalCotizador implements Serializable {


    private String tipoAdicional;
    private String nombreAdicional;
    private double precioAdicional;
    private String descuento;
    private String duracionDescuento;
    private String tiposolicitud;
    private int permanencia;
    private int demo;
    private boolean gota;
    private String velocidadFinal;
    private String velocidadInicial;
    private String precioSinIva;

    public AdicionalCotizador(String tipoAdicional, String nombreAdicional, double precioAdicional, String descuento, String duracionDescuento, String tiposolicitud, int permanencia, int demo, boolean gota, String velocidadFinal, String velocidadInicial, String precioSinIva) {
        this.tipoAdicional = tipoAdicional;
        this.nombreAdicional = nombreAdicional;
        this.precioAdicional = precioAdicional;
        this.descuento = descuento;
        this.duracionDescuento = duracionDescuento;
        this.tiposolicitud = tiposolicitud;
        this.permanencia = permanencia;
        this.demo = demo;
        this.gota = gota;
        this.velocidadFinal = velocidadFinal;
        this.velocidadInicial = velocidadInicial;
        this.precioSinIva = precioSinIva;
    }

    public AdicionalCotizador(String tipoAdicional, String nombreAdicional, double precioAdicional, String descuento, String duracionDescuento, String tiposolicitud, int permanencia, int demo) {
        this.tipoAdicional = tipoAdicional;
        this.nombreAdicional = nombreAdicional;
        this.precioAdicional = precioAdicional;
        this.descuento = descuento;
        this.duracionDescuento = duracionDescuento;
        this.tiposolicitud = tiposolicitud;
        this.permanencia = permanencia;
        this.demo = demo;
    }

    public AdicionalCotizador(String tipoAdicional, String nombreAdicional, double precioAdicional, String descuento, String duracionDescuento, String tiposolicitud) {
        this.tipoAdicional = tipoAdicional;
        this.nombreAdicional = nombreAdicional;
        this.precioAdicional = precioAdicional;
        this.descuento = descuento;
        this.duracionDescuento = duracionDescuento;
        this.tiposolicitud = tiposolicitud;
    }

    public AdicionalCotizador(String nombreAdicional, double precioAdicional,String tipoAdicional,String tiposolicitud) {
        this.nombreAdicional = nombreAdicional;
        this.precioAdicional = precioAdicional;
        this.tipoAdicional = tipoAdicional;
        this.tiposolicitud = tiposolicitud;
        this.descuento = "-";
        this.duracionDescuento = "0 meses";
    }

    public String getTipoAdicional() {
        return tipoAdicional;
    }

    public void setTipoAdicional(String tipoAdicional) {
        this.tipoAdicional = tipoAdicional;
    }

    public String getNombreAdicional() {
        return nombreAdicional;
    }

    public void setNombreAdicional(String nombreAdicional) {
        this.nombreAdicional = nombreAdicional;
    }

    public double getPrecioAdicional() {
        return precioAdicional;
    }

    public void setPrecioAdicional(double precioAdicional) {
        this.precioAdicional = precioAdicional;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getDuracionDescuento() {
        return duracionDescuento;
    }

    public void setDuracionDescuento(String duracionDescuento) {
        this.duracionDescuento = duracionDescuento;
    }

    public String getTiposolicitud() {
        return tiposolicitud;
    }

    public void setTiposolicitud(String tiposolicitud) {
        this.tiposolicitud = tiposolicitud;
    }

    public int getPermanencia() {
        return permanencia;
    }

    public void setPermanencia(int permanencia) {
        this.permanencia = permanencia;
    }

    public int getDemo() {
        return demo;
    }

    public void setDemo(int demo) {
        this.demo = demo;
    }

    public boolean isGota() {
        return gota;
    }

    public void setGota(boolean gota) {
        this.gota = gota;
    }

    public String getVelocidadFinal() {
        return velocidadFinal;
    }

    public void setVelocidadFinal(String velocidadFinal) {
        this.velocidadFinal = velocidadFinal;
    }

    public String getVelocidadInicial() {
        return velocidadInicial;
    }

    public void setVelocidadInicial(String velocidadInicial) {
        this.velocidadInicial = velocidadInicial;
    }

    public String getPrecioSinIva() {
        return precioSinIva;
    }

    public void setPrecioSinIva(String precioSinIva) {
        this.precioSinIva = precioSinIva;
    }
}
