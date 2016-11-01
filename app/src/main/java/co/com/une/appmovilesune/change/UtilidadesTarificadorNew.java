package co.com.une.appmovilesune.change;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.une.appmovilesune.model.Cliente;

/**
 * Created by Gospina on 28/10/2016.
 */

public class UtilidadesTarificadorNew {

    public UtilidadesTarificadorNew(){

    }

    public static ArrayList<String> aplicarDescuentos(String promocion,String duracion) {

      ArrayList<String> descuento = new ArrayList<String>();

        if(promocion.equalsIgnoreCase("0") || promocion.equalsIgnoreCase("0.0")){
            descuento.add("Sin Promocion");
            descuento.add("N/A");
        }else{
            descuento.add(promocion + "%");
            if(duracion.equalsIgnoreCase("1")){
                descuento.add(promocion + " Mes ");
            }else{
                descuento.add(promocion + " Meses ");
            }
        }

        return descuento;
    }

    public static boolean validarSegundaTelefonia(Cliente cliente) {

        boolean segundaTO = false;

        if (cliente.getPortafolio() != null) {
            try {
                JSONObject portafolio = new JSONObject(cliente.getPortafolio());
                if (portafolio.has("TO")) {

                    if (portafolio.get("TO").getClass().getSimpleName().equals("JSONArray")) {
                        JSONArray array = portafolio.getJSONArray("TO");
                        // Portafolio.add(new ListaDefault(0, "TO", "Titulo"));
                        for (int i = 0; i < array.length(); i++) {
                            if (array.getJSONObject(i).getString("Estado_Servicio").equalsIgnoreCase("ACTI")
                                    || array.getJSONObject(i).getString("Estado_Servicio").equalsIgnoreCase("SXFP")) {
                                segundaTO = true;
                            }
                        }

                    } else if (portafolio.get("TO").getClass().getSimpleName().equals("JSONObject")) {

                        JSONObject array = portafolio.getJSONObject("TO");

                        if (array.getString("Estado_Servicio").equalsIgnoreCase("ACTI")) {
                            segundaTO = true;
                        }
                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.w("Error", e.getMessage());
                // mensaje = "Problemas procesando el portafolio del cliente";
            }
        }

        return segundaTO;
    }
}
