package co.com.une.appmovilesune.model;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gospina on 05/02/2018.
 */

public class PortafolioUNE implements Serializable{

    private ArrayList<ProductoPortafolioUNE> productoPortafolioUNEArrayList= new ArrayList<ProductoPortafolioUNE>();

    private String contrato;
    private String smartPromoCiudad;
    private String tipoFacturacionCiudad;
    private String estandarizado;

    public PortafolioUNE(){

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

    public ArrayList<ProductoPortafolioUNE> productoPortafolioUNEArrayList (JSONObject jsonPortafolio){
        productoPortafolioUNEArrayList.clear();
        System.out.println("portafolio "+jsonPortafolio);
        try {

            if (jsonPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONArray")) {
                JSONArray arrayPortafolio = jsonPortafolio.getJSONArray("ListaDatosIdentificador");
                for (int i = 0; i < arrayPortafolio.length(); i++) {
                    productoPortafolioUNEArrayList.add(productoPortafolioUNE(arrayPortafolio.getJSONObject(i)));
                }
            }else if (jsonPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONObject")) {
                JSONObject objectoPortafolio = jsonPortafolio.getJSONObject("ListaDatosIdentificador");
                productoPortafolioUNEArrayList.add(productoPortafolioUNE(objectoPortafolio));

            }
        }catch(JSONException e){
            Log.w("Exception portafolio ", e.getMessage());
        }

        System.out.println("productoPortafolioUNEArrayList tamaño "+productoPortafolioUNEArrayList.size());

        return productoPortafolioUNEArrayList;
    }


    public ProductoPortafolioUNE productoPortafolioUNE (JSONObject producto){
        ProductoPortafolioUNE productoPortafolioUNE = new ProductoPortafolioUNE();

        try {
            if(producto.has("Tecnologia"))
                productoPortafolioUNE.setTecnologia(producto.getString("Tecnologia"));

            if(producto.has("TipoFactura"))
                productoPortafolioUNE.setTipoFactura(producto.getString("TipoFactura"));

            if(producto.has("Producto"))
                productoPortafolioUNE.setProducto(producto.getString("Producto"));

            if(producto.has("PlanId"))
                productoPortafolioUNE.setPlanId(producto.getString("PlanId"));

            if(producto.has("Plan"))
                productoPortafolioUNE.setPlan(producto.getString("Plan"));

            if(producto.has("Estado_Servicio"))
                productoPortafolioUNE.setEstadoServicio(producto.getString("Estado_Servicio"));

            if(producto.has("clienteId"))
                productoPortafolioUNE.setEstadoServicio(producto.getString("clienteId"));

            if(producto.has("SmartPromo"))
                productoPortafolioUNE.setSmartPromo(producto.getString("SmartPromo"));

            if(producto.has("Adicionales"))
                productoPortafolioUNE.setAdicionales(producto.getString("Adicionales"));

        }catch (Exception e){
            Log.w("Excep productosElite", e.getMessage());
        }


        return  productoPortafolioUNE;
    }

    public ProductoPortafolioUNE buscarProductoPorTipoProducto (String tipoProducto){
        ProductoPortafolioUNE productoPortafolioUNE = null;
        System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto "+tipoProducto);
        for (int i = 0; i < productoPortafolioUNEArrayList.size(); i++) {
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto i "+i);
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto productoPortafolioUNEArrayList.get(i).getPlan() "+productoPortafolioUNEArrayList.get(i).getPlan());
            System.out.println("ProductoPortafolioUNE buscarProductoPorTipoProducto "+tipoProducto);
            if (productoPortafolioUNEArrayList.get(i).getProducto().equalsIgnoreCase(tipoProducto)){
               productoPortafolioUNE = productoPortafolioUNEArrayList.get(i);
           }
        }
        return productoPortafolioUNE;
    }
}
