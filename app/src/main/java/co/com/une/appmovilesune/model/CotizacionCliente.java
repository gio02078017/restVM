package co.com.une.appmovilesune.model;

import java.util.ArrayList;

/**
 * Created by Gospina on 27/10/2016.
 */

public class CotizacionCliente {
    private String oferta;
    private String control;
    private GotaBa gotaba;
    private ArrayList<ProductoCotizador> productoCotizador;
    private double totalIndividual;
    private double totalEmpaquetado;
    private int contadorProductos;
    private String estrato;

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
}
