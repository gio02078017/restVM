package co.com.une.appmovilesune.model;

import android.util.Log;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;

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
    private String planFacturacionInd;
    private String planFacturacionEmp;
    private String velocidad;
    private double cargoBasicoInd;
    private double cargoBasicoEmp;
    private double descuentoCargobasico;
    private int duracionDescuento;
    private double pagoAnticipado;
    private double pagoParcial;
    private double pagoParcialDescuento;
    private double totalPagoParcial;

    private boolean aplicaPA;
    private boolean clienteNuevo;

    public ProductoCotizador(String tipoPeticion, int tipo, String plan, double cargoBasicoInd, double cargoBasicoEmp, double descuentoCargobasico, int duracionDescuento, String planFacturacionInd, String planFacturacionEmp, String velocidad) {
        this.tipoPeticion = tipoPeticion;
        this.tipo = tipo;
        this.plan = plan;
        this.planFacturacionInd = planFacturacionInd;
        this.planFacturacionEmp = planFacturacionEmp;
        this.velocidad = velocidad;
        this.cargoBasicoInd = cargoBasicoInd;
        this.cargoBasicoEmp = cargoBasicoEmp;
        this.descuentoCargobasico = descuentoCargobasico;
        this.duracionDescuento = duracionDescuento;
        obtenerValorPagoParcialAnticipado();
    }

    private void obtenerValorPagoParcialAnticipado() {
        String clausula = "producto=?";
        String[] valores = new String[]{traducirProducto().toUpperCase()};

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "pagoparcialanticipado", new String[]{"pagoparcial", "descuento", "pagoanticipado"}, clausula,
                valores, null, null, null);

        Log.i("PAGO PARCIAL","respuesta "+respuesta);
        Log.i("PAGO PARCIAL","clenteNuevo "+clienteNuevo);

        if (respuesta != null) {
            if (tipoPeticion.equals("N")) {
                if(clienteNuevo){
                    totalPagoParcial = calcularDescuento(respuesta.get(0));
                    if (aplicaPA) {
                        pagoAnticipado = Double.parseDouble(respuesta.get(0).get(2));
                    } else {
                        pagoAnticipado = 0;
                    }
                }else {
                    totalPagoParcial = 0;
                    pagoAnticipado = 0;
                }


            } else {
                totalPagoParcial = 0;
                pagoAnticipado = 0;
            }

        } else {
            totalPagoParcial = 0;
            pagoAnticipado = 0;
        }
    }

    private double calcularDescuento(ArrayList<String> valores) {
        pagoParcial = Double.parseDouble(valores.get(0));
        pagoParcialDescuento = Double.parseDouble(valores.get(1));

        double total = pagoParcial - (pagoParcial * pagoParcialDescuento) / 100;

        return total;

    }

    public void aplciarDescuentoTrio() {

        //totalPagoParcial = 0;
        totalPagoParcial = totalPagoParcial * 0.33333;
    }

    public void aplicarDescuentoDuo(){
        totalPagoParcial = totalPagoParcial * 0.5;
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

    public String getPlanFacturacionInd() {
        return planFacturacionInd;
    }

    public void setPlanFacturacionInd(String planFacturacionInd) {
        this.planFacturacionInd = planFacturacionInd;
    }

    public String getPlanFacturacionEmp() {
        return planFacturacionEmp;
    }

    public void setPlanFacturacionEmp(String planFacturacionEmp) {
        this.planFacturacionEmp = planFacturacionEmp;
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

    public double getTotalPagoParcial() {
        return totalPagoParcial;
    }

    public void setTotalPagoParcial(double totalPagoParcial) {
        this.totalPagoParcial = totalPagoParcial;
    }


    public String traducirProducto(){

        String productoAbreviacion = "";

        switch (tipo) {
            case 0:
                productoAbreviacion = "to";
                break;
            case 1:
                productoAbreviacion = "tv";
                break;
            case 2:
                productoAbreviacion = "ba";
                break;
        }

        return productoAbreviacion;

    }

    public void setAplicaPA(boolean aplica) {
        aplicaPA = aplica;
        obtenerValorPagoParcialAnticipado();
    }

    public void setClienteNuevo(boolean clienteNuevo){
        this.clienteNuevo = clienteNuevo;
        obtenerValorPagoParcialAnticipado();
    }
}
