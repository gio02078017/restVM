package co.com.une.appmovilesune.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;

import co.com.une.appmovilesune.change.UtilidadesFacturacionAnticipada;

/**
 * Created by Gospina on 26/03/2018.
 */

public class PaqueteUNE implements Serializable{
    private String idPaquete;
    private ArrayList<ProductoPortafolioUNE> productoPortafolioUNEArrayList = new ArrayList<ProductoPortafolioUNE>();
    private String jsonPaquete;
    private ArrayList<String> clientes = new ArrayList<String>();

    public PaqueteUNE (JSONObject jsonPaquete ){
        productoPortafolioUNEArrayList.clear();
        clientes.clear();
        System.out.println("jsonPaquete "+jsonPaquete);
        try {
            idPaquete = jsonPaquete.getString("IdPaquete");
            this.jsonPaquete = jsonPaquete.toString();
            if(jsonPaquete.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONArray")){
                JSONArray arrayPortafolio = jsonPaquete.getJSONArray("ListaDatosIdentificador");
                System.out.println(" arrayPortafolio "+arrayPortafolio);
                for (int i = 0; i < arrayPortafolio.length(); i++) {
                    productoPortafolioUNEArrayList.add(UtilidadesFacturacionAnticipada.productoPortafolioUNE(arrayPortafolio.getJSONObject(i)));
                    clientes.add(UtilidadesFacturacionAnticipada.idCliente(arrayPortafolio.getJSONObject(i)));
                }
            }else if(jsonPaquete.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONObject")){
                productoPortafolioUNEArrayList.add(UtilidadesFacturacionAnticipada.productoPortafolioUNE(jsonPaquete.getJSONObject("ListaDatosIdentificador")));
                clientes.add(UtilidadesFacturacionAnticipada.idCliente(jsonPaquete.getJSONObject("ListaDatosIdentificador")));
            }
        }catch(JSONException e){
            Log.w("Error ",e.getMessage());
        }
    }

    public String getIdPaquete() {
        return idPaquete;
    }

    public void setIdPaquete(String idPaquete) {
        this.idPaquete = idPaquete;
    }

    public ArrayList<ProductoPortafolioUNE> getProductoPortafolioUNEArrayList() {
        return productoPortafolioUNEArrayList;
    }

    public void setProductoPortafolioUNEArrayList(ArrayList<ProductoPortafolioUNE> productoPortafolioUNEArrayList) {
        this.productoPortafolioUNEArrayList = productoPortafolioUNEArrayList;
    }

    public String getJsonPaquete() {
        return jsonPaquete;
    }

    public void setJsonPaquete(String jsonPaquete) {
        this.jsonPaquete = jsonPaquete;
    }

    public ArrayList<String> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<String> clientes) {
        this.clientes = clientes;
    }


    public void imprimir(String preFijo){
        System.out.println(preFijo+" PaqueteUNE => Inicio");
        System.out.println(preFijo+" PaqueteUNE => idPaquete " + idPaquete );
        System.out.println(preFijo+" PaqueteUNE => productoPortafolioUNEArrayList " + productoPortafolioUNEArrayList );
        System.out.println(preFijo+" PaqueteUNE => jsonPaquete " + jsonPaquete );
        System.out.println(preFijo+" PaqueteUNE => clientes " + clientes );
        System.out.println(preFijo+" ProductoPortafolioUNE => Fin");
    }

    public void imprimirProductosPaquete(String preFijo) {
        System.out.println(preFijo +" imprimirProductosPaquete "+productoPortafolioUNEArrayList.size());
        for (int i = 0; i < productoPortafolioUNEArrayList.size(); i++) {
            productoPortafolioUNEArrayList.get(i).imprimir();
        }
    }
}
