package co.com.une.appmovilesune.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Gospina on 05/02/2018.
 */

public class ProductoPortafolioUNE implements Serializable {

    public static final String TELEFONIA = "TO";
    public static final String TELEVISION = "TELEV";
    public static final String INTERNET = "INTER";

    private String tecnologia;
    private String tipoFactura;
    private String producto;
    private String planId;
    private String decosHD;
    private String plan;
    private String velocidadOriginal;
    private String clienteId;
    private String gota;
    private String extensionesHFC;
    private String estadoServicio;
    private String identificador;
    private String smartPromo;
    private String adicionales;
    private String velocidadGota;

    public ProductoPortafolioUNE(){

    }

    public ProductoPortafolioUNE(String tecnologia, String tipoFactura, String producto, String planId, String plan, String clienteId, String estadoServicio, String identificador, String smartPromo) {
        this.tecnologia = tecnologia;
        this.tipoFactura = tipoFactura;
        this.producto = producto;
        this.planId = planId;
        this.plan = plan;
        this.clienteId = clienteId;
        this.estadoServicio = estadoServicio;
        this.identificador = identificador;
        this.smartPromo = smartPromo;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getDecosHD() {
        return decosHD;
    }

    public void setDecosHD(String decosHD) {
        this.decosHD = decosHD;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getVelocidadOriginal() {
        return velocidadOriginal;
    }

    public void setVelocidadOriginal(String velocidadOriginal) {
        this.velocidadOriginal = velocidadOriginal;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getGota() {
        return gota;
    }

    public void setGota(String gota) {
        this.gota = gota;
    }

    public String getExtensionesHFC() {
        return extensionesHFC;
    }

    public void setExtensionesHFC(String extensionesHFC) {
        this.extensionesHFC = extensionesHFC;
    }

    public String getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(String estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getSmartPromo() {
        return smartPromo;
    }

    public void setSmartPromo(String smartPromo) {
        this.smartPromo = smartPromo;
    }

    public String getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(String adicionales) {
        this.adicionales = adicionales;
    }

    public String getVelocidadGota() {
        return velocidadGota;
    }

    public void setVelocidadGota(String velocidadGota) {
        this.velocidadGota = velocidadGota;
    }

    public int tipoProducto(){
        if(producto.equalsIgnoreCase("TO")){
            return 0;
        }else if(producto.equalsIgnoreCase("TELEV")){
            return 1;
        }else if(producto.equalsIgnoreCase("INTER")){
            return 2;
        }
        return 0;
    }

    public void imprimir(){

        System.out.println("ProductoPortafolioUNE => Inicio");
        System.out.println("ProductoPortafolioUNE => tecnologia " + tecnologia );
        System.out.println("ProductoPortafolioUNE => tipoFactura " + tipoFactura );
        System.out.println("ProductoPortafolioUNE => producto " + producto );
        System.out.println("ProductoPortafolioUNE => planId " + planId );
        System.out.println("ProductoPortafolioUNE => decosHD " + decosHD );
        System.out.println("ProductoPortafolioUNE => plan " + plan );
        System.out.println("ProductoPortafolioUNE => velocidadOriginal " + velocidadOriginal );
        System.out.println("ProductoPortafolioUNE => clienteId " + clienteId );
        System.out.println("ProductoPortafolioUNE => gota " + gota );
        System.out.println("ProductoPortafolioUNE => extensionesHFC " + extensionesHFC );
        System.out.println("ProductoPortafolioUNE => estadoServicio " + estadoServicio );
        System.out.println("ProductoPortafolioUNE => identificador " + identificador );
        System.out.println("ProductoPortafolioUNE => smartPromo " + smartPromo );
        System.out.println("ProductoPortafolioUNE => adicionales " + adicionales );
        System.out.println("ProductoPortafolioUNE => velocidadGota " + velocidadGota );
        System.out.println("ProductoPortafolioUNE => Fin");
    }
}
