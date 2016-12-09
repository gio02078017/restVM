package co.com.une.appmovilesune.change;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.ProductoCotizador;

/**
 * Created by Gospina on 28/10/2016.
 */

public class UtilidadesTarificadorNew {

    public UtilidadesTarificadorNew() {

    }

    public static ArrayList<String> aplicarDescuentos(String promocion, String duracion) {

        ArrayList<String> descuento = new ArrayList<String>();

        if (promocion.equalsIgnoreCase("0") || promocion.equalsIgnoreCase("0.0")) {
            descuento.add("Sin Promocion");
            descuento.add("N/A");
        } else {
            descuento.add(promocion + "%");
            if (duracion.equalsIgnoreCase("1")) {
                descuento.add(duracion + " Mes ");
            } else {
                descuento.add(duracion + " Meses ");
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

    public static void imprimirProductosCotizacion(ArrayList<ProductoCotizador> productos) {

        System.out.println("************imprimirProductosCotizacion**********Inicio********");
        for (int i = 0; i < productos.size(); i++) {

            System.out.println("************Producto**********Posicion(" + i + ")********");
            System.out.println("************tipo**********" + productos.get(i).getTipo() + "********");
            System.out.println("************tipoPeticion**********" + productos.get(i).getTipoPeticion() + "********");
            System.out.println("************plan**********" + productos.get(i).getPlan() + "********");
            System.out.println("************planFacturacionInd**********" + productos.get(i).getPlanFacturacionInd() + "********");
            System.out.println("************planFacturacionEmp**********" + productos.get(i).getPlanFacturacionEmp() + "********");
            System.out.println("************cargoBasicoInd**********" + productos.get(i).getCargoBasicoInd() + "********");
            System.out.println("************cargoBasicoEmp**********" + productos.get(i).getCargoBasicoEmp() + "********");
            System.out.println("************cargoBasicoInd**********" + productos.get(i).getCargoBasicoInd() + "********");
            System.out.println("************descuentoCargobasico**********" + productos.get(i).getDescuentoCargobasico() + "********");
            System.out.println("************duracionDescuento**********" + productos.get(i).getDuracionDescuento() + "********");
            System.out.println("************velocidad**********" + productos.get(i).getVelocidad() + "********");
        }

        System.out.println("************imprimirProductosCotizacion**********Fin********");
    }

    public static void imprimirDescuentos(ArrayList<ArrayList<String>> descuentos) {
        if (descuentos != null) {
            for (int i = 0; i < descuentos.size(); i++) {
                System.out.println("impresion descuentos tipoProducto " + descuentos.get(i).get(0));
                System.out.println("impresion descuentos descuento " + descuentos.get(i).get(1));
                System.out.println("impresion descuentos tiempo " + descuentos.get(i).get(2));
            }
        }
    }

    public static boolean isTrioNuevo(ArrayList<ProductoCotizador> productos) {

        boolean trioNuevo = false;

        ArrayList<Boolean> nuevos = new ArrayList<Boolean>();

        for (ProductoCotizador producto : productos) {
            if (producto.getTipoPeticion().equals("N")) {
                nuevos.add(true);
            }
        }

        if (nuevos.size() == 3) {
            trioNuevo = true;
        }

        return trioNuevo;
    }

    public static boolean validarEstandarizacion(Cotizacion cotizacion, Cliente cliente, Context contex) {

        boolean validarEstandarizacion = true;
        boolean validarTelefonoServicio = true;


        if (cotizacion.getTipoTo().equalsIgnoreCase("N") && !cotizacion.getTelefonia().equalsIgnoreCase(Utilidades.inicial_guion)) {
            if (Utilidades.visible("portafolioATC", cliente.getCiudad())
			/* && cliente.getPortafolio() == null */) {
                if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
                    // System.out.println("cliente.isConsultaNormalizada() "
                    // + cliente.isConsultaNormalizada());
                    // System.out.println("cliente.isControlNormalizada() "
                    // + cliente.isControlNormalizada());

                    if (!cliente.isControlCerca() && !Utilidades.CoberturaRural(cliente)) {
                        if (!cliente.isControlNormalizada()) {
                            validarEstandarizacion = false;
                            // ja.put(Utilidades.jsonMensajes("Direccion",
                            // "Debe Normalizar la Direcci�n"));
                            Toast.makeText(contex, "Debe Normalizar la Dirección", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cliente.getPortafolio() == null/*
																 * cliente.
																 * isConsultaNormalizada
																 * ()
																 */) {
                                //lanzarSimulador("direccion", "Portafolio");
                                ;
                            }
                        }
                    }

                }

                String telefono = Utilidades.limpiarTelefono(cliente.getTelefono());
                if (!telefono.equals("")) {
                    System.out.println("trlefono " + telefono);
                    if (telefono.length() != 7) {
                        validarTelefonoServicio = false;
                        Toast.makeText(contex, "Telefono De Servicio, Debe Estar Compuesto Por 7 Digitos",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
            if (!cliente.isControlNormalizada() && !Utilidades.CoberturaRural(cliente)) {
                validarEstandarizacion = false;
                Toast.makeText(contex, contex.getResources().getString(R.string.normailizardireccion), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        return validarEstandarizacion;

    }
}
