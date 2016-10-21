package co.com.une.appmovilesune.model;

import java.util.ArrayList;

/**
 * Created by davids on 18/10/16.
 */

public class ProductoCotizador {

    public static final int TELEFONIA = 0;
    public static final int TELEVISION = 1;
    public static final int INTERNET = 2;

    private String tipoPeticion;
    private int tipo;
    private String plan;
    private String planFacturacion;
    private String velocidad;
    private double cargoBasicoInd;
    private double cargoBasicoEmp;
    private double descuentoCargobasico;
    private int duracionDescuento;
    private double pagoAnticipado;
    private double pagoParcial;
    private double pagoParcialDescuento;

    public ProductoCotizador(String tipoPeticion, int tipo, String plan, String planFacturacion, String velocidad, double cargoBasicoInd, double cargoBasicoEmp, double descuentoCargobasico, int duracionDescuento) {
        this.tipoPeticion = tipoPeticion;
        this.tipo = tipo;
        this.plan = plan;
        this.planFacturacion = planFacturacion;
        this.velocidad = velocidad;
        this.cargoBasicoInd = cargoBasicoInd;
        this.cargoBasicoEmp = cargoBasicoEmp;
        this.descuentoCargobasico = descuentoCargobasico;
        this.duracionDescuento = duracionDescuento;
    }

    public static int getTELEFONIA() {
        return TELEFONIA;
    }

    public static int getTELEVISION() {
        return TELEVISION;
    }

    public static int getINTERNET() {
        return INTERNET;
    }

    public String getTipoPeticion() {
        return tipoPeticion;
    }

    public void setTipoPeticion(String tipoPeticion) {
        this.tipoPeticion = tipoPeticion;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getPlanFacturacion() {
        return planFacturacion;
    }

    public void setPlanFacturacion(String planFacturacion) {
        this.planFacturacion = planFacturacion;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public double getCargoBasicoInd() {
        return cargoBasicoInd;
    }

    public void setCargoBasicoInd(double cargoBasicoInd) {
        this.cargoBasicoInd = cargoBasicoInd;
    }

    public double getCargoBasicoEmp() {
        return cargoBasicoEmp;
    }

    public void setCargoBasicoEmp(double cargoBasicoEmp) {
        this.cargoBasicoEmp = cargoBasicoEmp;
    }

    public double getDescuentoCargobasico() {
        return descuentoCargobasico;
    }

    public void setDescuentoCargobasico(double descuentoCargobasico) {
        this.descuentoCargobasico = descuentoCargobasico;
    }

    public int getDuracionDescuento() {
        return duracionDescuento;
    }

    public void setDuracionDescuento(int duracionDescuento) {
        this.duracionDescuento = duracionDescuento;
    }

    public double getPagoAnticipado() {
        return pagoAnticipado;
    }

    public void setPagoAnticipado(double pagoAnticipado) {
        this.pagoAnticipado = pagoAnticipado;
    }

    public double getPagoParcial() {
        return pagoParcial;
    }

    public void setPagoParcial(double pagoParcial) {
        this.pagoParcial = pagoParcial;
    }

    public double getPagoParcialDescuento() {
        return pagoParcialDescuento;
    }

    public void setPagoParcialDescuento(double pagoParcialDescuento) {
        this.pagoParcialDescuento = pagoParcialDescuento;
    }
}
