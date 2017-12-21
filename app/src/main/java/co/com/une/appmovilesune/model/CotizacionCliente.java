package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gospina on 27/10/2016.
 */

public class CotizacionCliente implements Serializable {
    private String oferta;
    private String control;
    private GotaBa gotaba;

    private ArrayList<ProductoCotizador> productoCotizador;

    private double totalIndividual;
    private double totalEmpaquetado;
    private int contadorProductos;
    private String estrato;

    private String totalPagoAntCargoFijo, totalPagoConexion, totalPagoParcialConexion, descuentoConexion;

    private String totalCotizacionIndDescuento, totalCotizacionEmpDescuento;

    private String tipoOferta;
    private String ofertaCotizacion;

    private boolean clienteNuevo;
    private String codigoClienteNuevo;

    private String codigoPagoAnticipado;

    public String getOferta() {
        return oferta;
    }

    public void setOferta(String oferta) {
        this.oferta = oferta;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public GotaBa getGotaba() {
        return gotaba;
    }

    public void setGotaba(GotaBa gotaba) {
        this.gotaba = gotaba;
    }

    public ArrayList<ProductoCotizador> getProductoCotizador() {
        return productoCotizador;
    }

    public void setProductoCotizador(ArrayList<ProductoCotizador> productoCotizador) {
        this.productoCotizador = productoCotizador;
    }

    public double getTotalIndividual() {
        return totalIndividual;
    }

    public void setTotalIndividual(double totalIndividual) {
        this.totalIndividual = totalIndividual;
    }

    public double getTotalEmpaquetado() {
        return totalEmpaquetado;
    }

    public void setTotalEmpaquetado(double totalEmpaquetado) {
        this.totalEmpaquetado = totalEmpaquetado;
    }

    public int getContadorProductos() {
        return contadorProductos;
    }

    public void setContadorProductos(int contadorProductos) {
        this.contadorProductos = contadorProductos;
    }

    public String getEstrato() {
        return estrato;
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public String getTotalPagoAntCargoFijo() {
        return totalPagoAntCargoFijo;
    }

    public void setTotalPagoAntCargoFijo(String totalPagoAntCargoFijo) {
        this.totalPagoAntCargoFijo = totalPagoAntCargoFijo;
    }

    public String getTotalPagoConexion() {
        return totalPagoConexion;
    }

    public void setTotalPagoConexion(String totalPagoConexion) {
        this.totalPagoConexion = totalPagoConexion;
    }

    public String getTotalPagoParcialConexion() {
        return totalPagoParcialConexion;
    }

    public void setTotalPagoParcialConexion(String totalPagoParcialConexion) {
        this.totalPagoParcialConexion = totalPagoParcialConexion;
    }

    public String getDescuentoConexion() {
        return descuentoConexion;
    }

    public void setDescuentoConexion(String descuentoConexion) {
        this.descuentoConexion = descuentoConexion;
    }

    public String getTotalCotizacionIndDescuento() {
        return totalCotizacionIndDescuento;
    }

    public void setTotalCotizacionIndDescuento(String totalCotizacionIndDescuento) {
        this.totalCotizacionIndDescuento = totalCotizacionIndDescuento;
    }

    public String getTotalCotizacionEmpDescuento() {
        return totalCotizacionEmpDescuento;
    }

    public void setTotalCotizacionEmpDescuento(String totalCotizacionEmpDescuento) {
        this.totalCotizacionEmpDescuento = totalCotizacionEmpDescuento;
    }

    public String getTipoOferta() {
        return tipoOferta;
    }

    public void setTipoOferta(String tipoOferta) {
        this.tipoOferta = tipoOferta;
    }

    public String getOfertaCotizacion() {
        return ofertaCotizacion;
    }

    public void setOfertaCotizacion(String ofertaCotizacion) {
        this.ofertaCotizacion = ofertaCotizacion;
    }

    public boolean isClienteNuevo() {
        return clienteNuevo;
    }

    public void setClienteNuevo(boolean clienteNuevo) {
        this.clienteNuevo = clienteNuevo;
    }

    public String getCodigoClienteNuevo() {
        return codigoClienteNuevo;
    }

    public void setCodigoClienteNuevo(String codigoClienteNuevo) {
        this.codigoClienteNuevo = codigoClienteNuevo;
    }

    public String getCodigoPagoAnticipado() {
        return codigoPagoAnticipado;
    }

    public void setCodigoPagoAnticipado(String codigoPagoAnticipado) {
        this.codigoPagoAnticipado = codigoPagoAnticipado;
    }
}
