package co.com.une.appmovilesune.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;

import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;
import co.com.une.appmovilesune.model.AdicionalCotizador;

/**
 * Created by davids on 18/10/16.
 */

public class ProductoCotizador implements Serializable {

    public static final int TELEFONIA = 0;
    public static final int TELEVISION = 1;
    public static final int INTERNET = 2;

    private String tipoPeticion;
    private int tipo;
    private String cambioPlan;
    private String plan;
    private String precio;
    private String planFacturacion;
    private String wifi;
    private String linea;
    private String tecnologia;
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

    private String planFacturacionAnterior;
    private String planFacturacionActual;

    private ArrayList<AdicionalCotizador> adicionalesCotizador;
    private double totalAdicionales;

    private ArrayList<ItemDecodificador> itemDecodificadores;
    private Decodificadores objectDecodificador;
    private ArrayList<ItemDecodificador> decodificadores;
    private String jsonDecos = null;
    private double totalDecos = 0.0;

    private String activacion;
    private String tipoTransaccion;
    private String inicioFacturacion;
    private String tipoFacturacion;
    private boolean smartPromo;

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
                    //totalPagoParcial = calcularDescuento(respuesta.get(0));
                    if (aplicaPA) {
                        pagoAnticipado = Double.parseDouble(respuesta.get(0).get(2));
                    } else {
                        pagoAnticipado = 0;
                    }
                }else {
                   // totalPagoParcial = 0;
                    pagoAnticipado = 0;
                }


            } else {
                //totalPagoParcial = 0;
                pagoAnticipado = 0;
            }

        } else {
            //totalPagoParcial = 0;
            pagoAnticipado = 0;
        }
    }

    public double calcularDescuento() {

        double total = pagoParcial - (pagoParcial * pagoParcialDescuento) / 100;
        total = (double)Math.round(total * 100d) / 100d;

        return total;

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

    public String getCambioPlan() {
        return cambioPlan;
    }

    public void setCambioPlan(String cambioPlan) {
        this.cambioPlan = cambioPlan;
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

    public boolean isAplicaPA() {
        return aplicaPA;
    }

    public void setClienteNuevo(boolean clienteNuevo){
        this.clienteNuevo = clienteNuevo;
        obtenerValorPagoParcialAnticipado();
    }

    public boolean isClienteNuevo() {
        return clienteNuevo;
    }

    public String getPlanFacturacionAnterior() {
        return planFacturacionAnterior;
    }

    public void setPlanFacturacionAnterior(String planFacturacionAnterior) {
        this.planFacturacionAnterior = planFacturacionAnterior;
    }

    public String getPlanFacturacionActual() {
        return planFacturacionActual;
    }

    public void setPlanFacturacionActual(String planFacturacionActual) {
        this.planFacturacionActual = planFacturacionActual;
    }

    public String getActivacion() {
        if(activacion == null || activacion.equalsIgnoreCase("")){
            activacion  = "N/A";
        }
        return activacion;
    }

    public void setActivacion(String activacion) {
        this.activacion = activacion;
    }

    public String getTipoTransaccion() {
        if(tipoTransaccion == null || tipoTransaccion.equalsIgnoreCase("")){
            tipoTransaccion  = "N/A";
        }
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getInicioFacturacion() {
        if(inicioFacturacion == null || inicioFacturacion.equalsIgnoreCase("")){
            inicioFacturacion  = "N/A";
        }
        return inicioFacturacion;
    }

    public void setInicioFacturacion(String inicioFacturacion) {
        this.inicioFacturacion = inicioFacturacion;
    }

    public String getTipoFacturacion() {
        if(tipoFacturacion == null || tipoFacturacion.equalsIgnoreCase("")){
            tipoFacturacion  = "N/A";
        }
        return tipoFacturacion;
    }

    public void setTipoFacturacion(String tipoFacturacion) {
        this.tipoFacturacion = tipoFacturacion;
    }

    public boolean isSmartPromo() {
        return smartPromo;
    }

    public void setSmartPromo(boolean smartPromo) {
        this.smartPromo = smartPromo;
    }

    public ArrayList<AdicionalCotizador> getAdicionalesCotizador() {
        return adicionalesCotizador;
    }

    public void setAdicionalesCotizador(ArrayList<AdicionalCotizador> adicionalesCotizador) {
        this.adicionalesCotizador = adicionalesCotizador;
    }

    public void llenarAdicional(AdicionalCotizador adicionalCotizador){
       adicionalesCotizador.add(adicionalCotizador);
    }

    public double getTotalAdicionales() {
        return totalAdicionales;
    }

    public void setTotalAdicionales(double totalAdicionales) {
        this.totalAdicionales = totalAdicionales;
    }

    public ArrayList<ItemDecodificador> getItemDecodificadores() {
        return itemDecodificadores;
    }

    public void setItemDecodificadores(ArrayList<ItemDecodificador> itemDecodificadores) {
        this.itemDecodificadores = itemDecodificadores;
    }

    public Decodificadores getObjectDecodificador() {
        return objectDecodificador;
    }

    public void setObjectDecodificador(Decodificadores objectDecodificador) {
        this.objectDecodificador = objectDecodificador;
    }

    public ArrayList<ItemDecodificador> getDecodificadores() {
        return decodificadores;
    }

    public void setDecodificadores(ArrayList<ItemDecodificador> decodificadores) {
        this.decodificadores = decodificadores;
    }

    public double getTotalDecos() {
        return totalDecos;
    }

    public void setTotalDecos(double totalDecos) {
        this.totalDecos = totalDecos;
    }

    public String getTipoPeticionNumerico() {
        String Respuesta = "";

        if (tipoPeticion.equalsIgnoreCase("N")) {
            Respuesta = "1";
        } else if (tipoPeticion.equalsIgnoreCase("C")) {
            Respuesta = "0";
        } else if (tipoPeticion.equalsIgnoreCase("E")) {
            Respuesta = "3";
        }

        return Respuesta;
    }

    public String getTipoPeticionNombreCompeto() {
        String Respuesta = "";

        if (tipoPeticion.equalsIgnoreCase("N")) {
            Respuesta = "Nuevo";
        } else if (tipoPeticion.equalsIgnoreCase("C")) {
            Respuesta = "Cambio";
        } else if (tipoPeticion.equalsIgnoreCase("E")) {
            Respuesta = "Existente";
        }

        return Respuesta;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getPlanFacturacion() {
        if(planFacturacion == null){
            planFacturacion = "";
        }
        return planFacturacion;
    }

    public void setPlanFacturacion(String planFacturacion) {
        this.planFacturacion = planFacturacion;
    }

    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public String getTecnologia() {
        if(tecnologia == null){
            tecnologia = "";
        }
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public String getLinea() {

        if(linea == null){
            linea = "";
        }

        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public void imprimir (){
                System.out.println("************tipo**********"+tipo+"********");
                System.out.println("************tipoPeticion**********"+tipoPeticion+"********");
                System.out.println("************plan**********"+plan+"********");
                System.out.println("************planFacturacionInd**********"+planFacturacionInd+"********");
                System.out.println("************planFacturacionEmp**********"+planFacturacionEmp+"********");
                System.out.println("************cargoBasicoInd**********"+cargoBasicoInd+"********");
                System.out.println("************cargoBasicoEmp**********"+cargoBasicoEmp+"********");
                System.out.println("************cargoBasicoInd**********"+cargoBasicoInd+"********");
                System.out.println("************descuentoCargobasico**********"+descuentoCargobasico+"********");
                System.out.println("************duracionDescuento**********"+duracionDescuento+"********");
                System.out.println("************velocidad**********"+velocidad+"********");
                System.out.println("************pagoParcial**********"+pagoParcial+"********");
                System.out.println("************pagoParcialDescuento**********"+pagoParcialDescuento+"********");
                System.out.println("************totalPagoParcial**********"+totalPagoParcial+"********");
                System.out.println("************linea**********"+linea+"********");
                System.out.println("************tecnologia**********"+tecnologia+"********");
                if(adicionalesCotizador != null){
                    UtilidadesTarificadorNew.imprimirAdicionalesCotizacion(adicionalesCotizador);
                }
    }

    public String descuentoConcatenado(){
        String meses = "Meses";
        if(duracionDescuento == 1){
            meses = "Mes";
        }

        return descuentoCargobasico +" % "+duracionDescuento +" "+meses;
    }

    public String getJsonDecos() {
        return jsonDecos;
    }

    public void setJsonDecos(String jsonDecos) {
        this.jsonDecos = jsonDecos;
    }
}
