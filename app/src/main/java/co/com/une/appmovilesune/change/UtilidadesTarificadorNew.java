package co.com.une.appmovilesune.change;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.ProductoCotizador;

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

    public static void imprimirProductosCotizacion(ArrayList<ProductoCotizador> productos){

        System.out.println("************imprimirProductosCotizacion**********Inicio********");
        for (int i = 0; i < productos.size(); i++) {

            System.out.println("************Producto**********Posicion("+i+")********");
            System.out.println("************tipo**********"+productos.get(i).getTipo()+"********");
            System.out.println("************tipoPeticion**********"+productos.get(i).getTipoPeticion()+"********");
            System.out.println("************plan**********"+productos.get(i).getPlan()+"********");
            System.out.println("************planFacturacionInd**********"+productos.get(i).getPlanFacturacionInd()+"********");
            System.out.println("************planFacturacionEmp**********"+productos.get(i).getPlanFacturacionEmp()+"********");
            System.out.println("************cargoBasicoInd**********"+productos.get(i).getCargoBasicoInd()+"********");
            System.out.println("************cargoBasicoEmp**********"+productos.get(i).getCargoBasicoEmp()+"********");
            System.out.println("************cargoBasicoInd**********"+productos.get(i).getCargoBasicoInd()+"********");
            System.out.println("************descuentoCargobasico**********"+productos.get(i).getDescuentoCargobasico()+"********");
            System.out.println("************duracionDescuento**********"+productos.get(i).getDuracionDescuento()+"********");
            System.out.println("************velocidad**********"+productos.get(i).getVelocidad()+"********");
        }

        System.out.println("************imprimirProductosCotizacion**********Fin********");
    }
}
