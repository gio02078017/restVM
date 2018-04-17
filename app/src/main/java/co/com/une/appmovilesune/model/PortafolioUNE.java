package co.com.une.appmovilesune.model;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import co.com.une.appmovilesune.change.UtilidadesFacturacionAnticipada;
/**
 * Created by Gospina on 05/02/2018.
 */

public class PortafolioUNE implements Serializable {

    private ArrayList<ProductoPortafolioUNE> productoPortafolioUNEArrayList = new ArrayList<ProductoPortafolioUNE>();
    private ArrayList<PaqueteUNE> paqueteUNEArrayList = new ArrayList<PaqueteUNE>();

    private String contrato;
    private String smartPromoCiudad;
    private String tipoFacturacionCiudad;
    private String estandarizado;

    public PaqueteUNE paqueteSeleccionado;

    public boolean validarPortafolio = false;

    public boolean saltarValidacionClienteNuevo = false;

    public PortafolioUNE() {

    }

    public PortafolioUNE(ArrayList<ProductoPortafolioUNE> portafolioUNEArrayList, String contrato, String smartPromoCiudad, String tipoFacturacionCiudad, String estandarizado) {
        this.productoPortafolioUNEArrayList = portafolioUNEArrayList;
        this.contrato = contrato;
        this.smartPromoCiudad = smartPromoCiudad;
        this.tipoFacturacionCiudad = tipoFacturacionCiudad;
        this.estandarizado = estandarizado;
    }

    public ArrayList<ProductoPortafolioUNE> getProductoPortafolioUNEArrayList() {
        return productoPortafolioUNEArrayList;
    }

    public void setProductoPortafolioUNEArrayList(ArrayList<ProductoPortafolioUNE> portafolioUNEArrayList) {
        this.productoPortafolioUNEArrayList = portafolioUNEArrayList;
    }

    public ArrayList<PaqueteUNE> getPaqueteUNEArrayList() {
        return paqueteUNEArrayList;
    }

    public void setPaqueteUNEArrayList(ArrayList<PaqueteUNE> paqueteUNEArrayList) {
        this.paqueteUNEArrayList = paqueteUNEArrayList;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getSmartPromoCiudad() {
        return smartPromoCiudad;
    }

    public void setSmartPromoCiudad(String smartPromoCiudad) {
        this.smartPromoCiudad = smartPromoCiudad;
    }

    public String getTipoFacturacionCiudad() {
        return tipoFacturacionCiudad;
    }

    public void setTipoFacturacionCiudad(String tipoFacturacionCiudad) {
        this.tipoFacturacionCiudad = tipoFacturacionCiudad;
    }

    public String getEstandarizado() {
        return estandarizado;
    }

    public void setEstandarizado(String estandarizado) {
        this.estandarizado = estandarizado;
    }

    public ArrayList<PaqueteUNE> paqueteUNEArrayList(JSONArray jsonArrayPortafolio) {
        paqueteUNEArrayList.clear();
        System.out.println("portafolio paqueteUNEArrayList " + jsonArrayPortafolio);
        try {
            for (int i = 0; i < jsonArrayPortafolio.length(); i++) {
               // System.out.println("portafolio paqueteItemArrayList [i = " + i + "] = " + jsonArrayPortafolio.getJSONObject(i));
                paqueteUNEArrayList.add(new PaqueteUNE(jsonArrayPortafolio.getJSONObject(i)));
                validarPortafolio = true;
            }
        } catch (JSONException e) {
            Log.w("Exception portafolio ", e.getMessage());
        }

        return paqueteUNEArrayList;
    }

    public ArrayList<PaqueteUNE> paqueteUNEObject(JSONObject paquete) {
        paqueteUNEArrayList.clear();
        System.out.println("portafolio paqueteUNEObject " + paquete);

        paqueteUNEArrayList.add(new PaqueteUNE(paquete));
        validarPortafolio = true;

        return paqueteUNEArrayList;
    }

    public ArrayList<ProductoPortafolioUNE> productoPortafolioUNEArrayList(JSONObject jsonPortafolio) {
        productoPortafolioUNEArrayList.clear();
        System.out.println("portafolio " + jsonPortafolio);
        try {

            if (jsonPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONArray")) {
                JSONArray arrayPortafolio = jsonPortafolio.getJSONArray("ListaDatosIdentificador");
                for (int i = 0; i < arrayPortafolio.length(); i++) {
                    productoPortafolioUNEArrayList.add(UtilidadesFacturacionAnticipada.productoPortafolioUNE(arrayPortafolio.getJSONObject(i)));

                }
            } else if (jsonPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONObject")) {
                JSONObject objectoPortafolio = jsonPortafolio.getJSONObject("ListaDatosIdentificador");
                productoPortafolioUNEArrayList.add(UtilidadesFacturacionAnticipada.productoPortafolioUNE(objectoPortafolio));

            }
        } catch (JSONException e) {
            Log.w("Exception portafolio ", e.getMessage());
        }

        System.out.println("productoPortafolioUNEArrayList tamaño " + productoPortafolioUNEArrayList.size());

        return productoPortafolioUNEArrayList;
    }

    public ProductoPortafolioUNE buscarProductoPorTipoProducto(String tipoProducto) {
        ProductoPortafolioUNE productoPortafolioUNE = null;
        System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto " + tipoProducto);
        for (int i = 0; i < productoPortafolioUNEArrayList.size(); i++) {
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto i " + i);
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto productoPortafolioUNEArrayList.get(i).getPlan() " + productoPortafolioUNEArrayList.get(i).getPlan());
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto " + tipoProducto);
            if (productoPortafolioUNEArrayList.get(i).getProducto().equalsIgnoreCase(tipoProducto)) {
                productoPortafolioUNE = productoPortafolioUNEArrayList.get(i);
            }
        }
        return productoPortafolioUNE;
    }

    public ProductoPortafolioUNE buscarProductoPorTipoProductoEnPaqueteSeleccionado(String tipoProducto) {
        ProductoPortafolioUNE productoPortafolioUNE = null;
        System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto " + tipoProducto);
        for (int i = 0; i < paqueteSeleccionado.getProductoPortafolioUNEArrayList().size(); i++) {
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto i " + i);
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto productoPortafolioUNEArrayList.get(i).getPlan() " + paqueteSeleccionado.getProductoPortafolioUNEArrayList().get(i).getPlan());
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto " + tipoProducto);
            if (paqueteSeleccionado.getProductoPortafolioUNEArrayList().get(i).getProducto().equalsIgnoreCase(tipoProducto)) {
                productoPortafolioUNE = paqueteSeleccionado.getProductoPortafolioUNEArrayList().get(i);
            }
        }
        return productoPortafolioUNE;
    }

    public PaqueteUNE getPaqueteSeleccionado() {
        return paqueteSeleccionado;
    }

    public void setPaqueteSeleccionado(PaqueteUNE paqueteSeleccionado) {
        this.paqueteSeleccionado = paqueteSeleccionado;
    }

    public boolean isValidarPortafolio() {
        return validarPortafolio;
    }

    public void setValidarPortafolio(boolean validarPortafolio) {
        this.validarPortafolio = validarPortafolio;
    }

    public boolean isSaltarValidacionClienteNuevo() {
        return saltarValidacionClienteNuevo;
    }

    public void setSaltarValidacionClienteNuevo(boolean saltarValidacionClienteNuevo) {
        this.saltarValidacionClienteNuevo = saltarValidacionClienteNuevo;
    }
}
