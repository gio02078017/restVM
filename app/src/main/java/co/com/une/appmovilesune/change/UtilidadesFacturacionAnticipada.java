package co.com.une.appmovilesune.change;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ListaCotizacion;
import co.com.une.appmovilesune.model.CotizacionCliente;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.ProductoPortafolioUNE;
import co.com.une.appmovilesune.model.PortafolioUNE;


/**
 * Created by Gospina on 28/10/2016.
 */

public class UtilidadesFacturacionAnticipada {

    public UtilidadesFacturacionAnticipada(){

    }

    public static ProductoPortafolioUNE productoPortafolioUNE(JSONObject producto) {
        ProductoPortafolioUNE productoPortafolioUNE = new ProductoPortafolioUNE();

        try {
            if (producto.has("Tecnologia"))
                productoPortafolioUNE.setTecnologia(producto.getString("Tecnologia"));

            if (producto.has("TipoFactura"))
                productoPortafolioUNE.setTipoFactura(producto.getString("TipoFactura"));

            if (producto.has("Producto"))
                productoPortafolioUNE.setProducto(producto.getString("Producto"));

            if (producto.has("PlanId"))
                productoPortafolioUNE.setPlanId(producto.getString("PlanId"));

            if (producto.has("Plan"))
                productoPortafolioUNE.setPlan(producto.getString("Plan"));

            if (producto.has("Estado_Servicio"))
                productoPortafolioUNE.setEstadoServicio(producto.getString("Estado_Servicio"));

            if (producto.has("clienteId"))
                productoPortafolioUNE.setEstadoServicio(producto.getString("clienteId"));

            if (producto.has("SmartPromo"))
                productoPortafolioUNE.setSmartPromo(producto.getString("SmartPromo"));

            if (producto.has("Adicionales"))
                productoPortafolioUNE.setAdicionales(producto.getString("Adicionales"));

        } catch (Exception e) {
            Log.w("Excep productosElite", e.getMessage());
        }

        productoPortafolioUNE.imprimir();

        return productoPortafolioUNE;
    }

    public static String idCliente(JSONObject producto) {
        String clienteId = null;
        try {
            if (producto.has("clienteId"))
                clienteId = producto.getString("clienteId");

        } catch (Exception e) {
            Log.w("Excep productosElite", e.getMessage());
        }

        return clienteId;
    }

    public static void imprimirPortafolio(PortafolioUNE portafolioUNE){
        System.out.println(portafolioUNE.getPaqueteUNEArrayList().size());
        if(portafolioUNE.getPaqueteUNEArrayList().size() > 0){
            for (int i = 0; i < portafolioUNE.getPaqueteUNEArrayList().size(); i++) {
                System.out.println("Inicio paqute i"+i);
                portafolioUNE.getPaqueteUNEArrayList().get(i).imprimir();
                portafolioUNE.getPaqueteUNEArrayList().get(i).imprimirProductosPaquete();
                System.out.println("Fin paqute i"+i);
            }
        }
    }
}
