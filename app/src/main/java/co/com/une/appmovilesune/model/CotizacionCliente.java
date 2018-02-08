package co.com.une.appmovilesune.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.util.Util;

import java.io.Serializable;
import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;

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

    private String totalCotizacionConsolidado, totalPagoAntCargoFijo, totalPagoConexion, totalPagoParcialConexion, descuentoConexion;

    private String totalCotizacionIndDescuento, totalCotizacionEmpDescuento;

    private String tipoOferta;
    private String ofertaCotizacion;

    private boolean clienteNuevo;
    private String codigoClienteNuevo;

    private String codigoPagoAnticipado;

    private boolean ventaEmpaquetada;

    private boolean aplicarDescuentos;

    private boolean validoIVR;

    private String lecturaxProducto;
    private String ofertaTipoFacturacion;
    private String ofertaInicioFacturacion;
    private String smartPromo;

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

    public String getTotalCotizacionConsolidado() {
        return totalCotizacionConsolidado;
    }

    public void setTotalCotizacionConsolidado(String totalCotizacionConsolidado) {
        this.totalCotizacionConsolidado = totalCotizacionConsolidado;
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

    public boolean isVentaEmpaquetada() {
        return ventaEmpaquetada;
    }

    public void setVentaEmpaquetada(boolean ventaEmpaquetada) {
        this.ventaEmpaquetada = ventaEmpaquetada;
    }

    public boolean isAplicarDescuentos() {
        return aplicarDescuentos;
    }

    public void setAplicarDescuentos(boolean aplicarDescuentos) {
        this.aplicarDescuentos = aplicarDescuentos;
    }

    public boolean isValidoIVR() {
        return validoIVR;
    }

    public void setValidoIVR(boolean validoIVR) {
        this.validoIVR = validoIVR;
    }

    public String getLecturaxProducto() {
        if(lecturaxProducto == null || lecturaxProducto.equalsIgnoreCase("")){
            lecturaxProducto  = "N/A";
        }
        return lecturaxProducto;
    }

    public void setLecturaxProducto(String lecturaxProducto) {
        this.lecturaxProducto = lecturaxProducto;
    }

    public String getOfertaTipoFacturacion() {
        if(ofertaTipoFacturacion == null || ofertaTipoFacturacion.equalsIgnoreCase("")){
            ofertaTipoFacturacion  = "N/A";
        }
        return ofertaTipoFacturacion;
    }

    public void setOfertaTipoFacturacion(String ofertaTipoFacturacion) {
        this.ofertaTipoFacturacion = ofertaTipoFacturacion;
    }

    public String getOfertaInicioFacturacion() {
        if(ofertaInicioFacturacion == null || ofertaInicioFacturacion.equalsIgnoreCase("")){
            ofertaInicioFacturacion  = "N/A";
        }
        return ofertaInicioFacturacion;
    }

    public void setOfertaInicioFacturacion(String ofertaInicioFacturacion) {
        this.ofertaInicioFacturacion = ofertaInicioFacturacion;
    }

    public String getSmartPromo() {
        return smartPromo;
    }

    public void setSmartPromo(String smartPromo) {
        this.smartPromo = smartPromo;
    }

    public JSONObject consolidarCotizacionCliente(Venta venta) {

        JSONObject cotizacionCliente = new JSONObject();
        JSONObject cotizacion = new JSONObject();

        JSONObject productos = new JSONObject();
        JSONArray arrayProductos = new JSONArray();


        try {


            if (productoCotizador != null && productoCotizador.size() > 0) {

                for (int i = 0; i < productoCotizador.size(); i++) {
                    if (!Utilidades.validarVacioProducto(productoCotizador.get(i).getPlan())) {
                        //objectProductos.add(Utilidades.jsonProductosCotizacion(productoCotizador.get(i)));
                        arrayProductos.put(Utilidades.jsonProductosCotizacion(productoCotizador.get(i)));
                    }
                }
            }

            cotizacion.put("productos", arrayProductos);
            cotizacion.put("total", totalCotizacionConsolidado);
            cotizacion.put("totalPagoAntCargoFijo", totalPagoAntCargoFijo);
            cotizacion.put("totalPagoConexion", totalPagoConexion);
            cotizacion.put("totalPagoParcialConexion", totalPagoParcialConexion);
            cotizacion.put("descuentoConexion", descuentoConexion);
            cotizacion.put("lecturaxProducto",lecturaxProducto);
            cotizacion.put("ofertaTipoFacturacion",ofertaTipoFacturacion);
            cotizacion.put("ofertaInicioFacturacion",ofertaInicioFacturacion);
            cotizacion.put("smartPromo",smartPromo);
            cotizacion.put("permanencia", "N/A" /*actualizar este dato*/);

            if (venta != null) {

                cotizacion.put("totalNuevos", venta.getTotalNuevos());
                cotizacion.put("fecha", venta.getFecha() + " " + venta.getHora());

                if (venta.getEmpaquetamiento().equalsIgnoreCase("SI")) {
                    cotizacion.put("empaquetada", "1");
                } else {
                    cotizacion.put("empaquetada", "0");
                }


                cotizacion.put("empaquetadaNombre", venta.getEmpaquetamiento());
                cotizacion.put("dependencias", venta.getDependencia());

                cotizacion.put("observaciones", venta.getObservaciones().replace(";", ".,"));
                cotizacion.put("horarioAtencion", venta.getHorarioAtencion());
                cotizacion.put("microZona", venta.getMicroZona());
                cotizacion.put("cobroDomingo", venta.getCobroDomingo());
                cotizacion.put("tipoServicio", venta.getTipoServicio());
                cotizacion.put("estrato", estrato);
                cotizacion.put("municipio", venta.getMunicipio());
                cotizacion.put("departamento", venta.getDepartamento());

                cotizacion.put("municipiossc", venta.getMunicipioSSC());

                cotizacion.put("agendaSiebel", "");
                cotizacion.put("idOferta", "");

                cotizacion.put("agendableSiebel", 0);
                cotizacion.put("oferta", oferta);

                if (Utilidades.excluirNacional("conceptoConstructor", venta.getTipoConstruccion())) {
                    cotizacion.put("constructores", 1);
                    cotizacion.put("conceptoConstructor", venta.getTipoConstruccion());
                } else {
                    cotizacion.put("constructores", 0);
                }
                if (venta.getMedioIngreso() != null) {
                    cotizacion.put("medioIngreso", venta.getMedioIngreso());
                } else {
                    cotizacion.put("medioIngreso", "n/a");
                }
                cotizacion.put("scooring", venta.getScooring());
            }

            cotizacionCliente.put("cotizacionCliente", cotizacion);

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }
        return cotizacionCliente;
    }

    public JSONObject consolidarCotizacionIVR(JSONObject datosExtras) {

        JSONObject cotizacionCliente = new JSONObject();
        JSONObject cotizacion = new JSONObject();

        JSONObject productos = new JSONObject();
        JSONArray arrayProductos = new JSONArray();


        try {


            if (productoCotizador != null && productoCotizador.size() > 0) {

                for (int i = 0; i < productoCotizador.size(); i++) {
                    if (!Utilidades.validarVacioProducto(productoCotizador.get(i).getPlan())) {
                        //objectProductos.add(Utilidades.jsonProductosCotizacion(productoCotizador.get(i)));
                        arrayProductos.put(Utilidades.jsonProductosCotizacionIVR(productoCotizador.get(i)));
                    }
                }
            }

            cotizacion.put("productos", arrayProductos);
            cotizacion.put("valorTotal", totalCotizacionConsolidado);
            cotizacion.put("totalPagoAntCargoFijo", totalPagoAntCargoFijo);
            cotizacion.put("totalPagoConexion", totalPagoConexion);
            cotizacion.put("totalPagoParcialConexion", totalPagoParcialConexion);
            cotizacion.put("descuentoConexion", descuentoConexion);
            cotizacion.put("lecturaxProducto",lecturaxProducto);
            cotizacion.put("ofertaTipoFacturacion",ofertaTipoFacturacion);
            cotizacion.put("ofertaInicioFacturacion",ofertaInicioFacturacion);
            cotizacion.put("sourceCRM","Venta Movil");

            cotizacionCliente.put("oferta", cotizacion);
            System.out.println("datosExtras " +datosExtras);
            if(datosExtras != null){
                cotizacionCliente.put("idIVR", datosExtras.getString("idIVR"));
                cotizacionCliente.put("nombreIVR", datosExtras.getString("nombreIVR"));
                cotizacionCliente.put("tipoIVR", datosExtras.getString("tipoIVR"));
                cotizacionCliente.put("idConfirmacion", datosExtras.getString("idConfirmacion"));
                cotizacionCliente.put("codigoConfirmacion", datosExtras.getString("codigoConfirmacion"));
                cotizacionCliente.put("mail", datosExtras.getString("mail"));
                cotizacionCliente.put("codigoAsesor", datosExtras.getString("codigoAsesor"));
            }

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }
        return cotizacionCliente;
    }

}
