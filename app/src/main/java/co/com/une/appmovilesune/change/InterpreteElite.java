package co.com.une.appmovilesune.change;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemDirecciones;
import co.com.une.appmovilesune.adapters.ListaDefault;

public class InterpreteElite {

    public static String Validacion_Cifin = "";
    public static String Datos_Cifin = "";

    private static String Validacion_Cartera = "", Mensaje_Cartera = "";

    private static ArrayList<String[]> datos_cliente = new ArrayList<String[]>();

    private static ArrayList<Ofertas> ofertas = new ArrayList<Ofertas>();

    private static ArrayList<ListaDefault> Portafolio = new ArrayList<ListaDefault>();
    String Direccion = "", Paginacion = "", Estrato = "", Telefono = "", Cedula = "";

    public InterpreteElite() {

    }

    public static ArrayList<ListaDefault> Mensajes(String interpretar) {

        ArrayList<ListaDefault> mensajes = new ArrayList<ListaDefault>();

        try {

            System.out.println("Interprete Mensajes -" + interpretar);

            JSONObject jsonObject = new JSONObject(interpretar);

            String Titulo = jsonObject.getString("Titulo");

            mensajes.add(new ListaDefault(0, Titulo, "Titulo"));

            JSONArray array = jsonObject.getJSONArray("Mensajes");

            for (int i = 0; i < array.length(); i++) {
                String titulo = array.getJSONObject(i).getString("titulo");
                String datos = array.getJSONObject(i).getString("datos");
                mensajes.add(new ListaDefault(2, titulo, datos));
            }

        } catch (JSONException e) {
            Log.w("Error", "Mensaje " + e.getMessage());

        }

        return mensajes;

    }

    public static ArrayList<ListaDefault> MensajesVacio() {

        ArrayList<ListaDefault> mensajes = new ArrayList<ListaDefault>();

        mensajes.add(new ListaDefault(0, "Error", "Titulo"));
        mensajes.add(new ListaDefault(2, "Respuesta", "Vacia"));

        mensajes.add(new ListaDefault(2, "Recomendación",
                "Intente Nueva Mente y de continuar el inconveniente Informe al analista del sistema"));

        return mensajes;

    }

    public static ArrayList<ListaDefault> PortafolioNuevo(String portafolio) {
        System.out.println("portafolio PortafolioNuevo Elite"+portafolio);
        Portafolio.clear();
        boolean to = false;
        boolean tv = false;
        boolean ba = false;
        try {

            JSONObject datosPortafolio = new JSONObject(portafolio);
            System.out.println("portafolio PortafolioNuevo datosPortafolio "+datosPortafolio);
                    if (datosPortafolio.has("ListaDatosIdentificador")) {
                        if (datosPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONArray")) {
                            JSONArray productosInstalados = datosPortafolio.getJSONArray("ListaDatosIdentificador");
                            System.out.println("portafolio Elite productosInstalados "+productosInstalados);
                            Portafolio.add(0, new ListaDefault(1, "SI", "Datos"));
                            if (productosInstalados != null) {
                                for (int i = 0; i < productosInstalados.length(); i++) {
                                    JSONObject producto = productosInstalados.getJSONObject(i);
                                    portafolioProductos(producto);
                                }
                            }
                        } else if (datosPortafolio.get("ListaDatosIdentificador").getClass().getSimpleName().equals("JSONObject")) {
                            JSONObject producto = datosPortafolio.getJSONObject("ListaDatosIdentificador");
                            System.out.println("portafolio Elite producto "+producto);
                            Portafolio.add(0, new ListaDefault(1, "SI", "Datos"));
                            portafolioProductos(producto);
                        }
                    }
        } catch (JSONException e) {
            Portafolio.clear();
            Portafolio.add(new ListaDefault(1, "NO", e.getMessage()));
            Log.w("Error PortafolioNuevo ", e.getMessage());
        }
        return Portafolio;
    }

    public static void portafolioProductos(JSONObject producto) {
        boolean tipoProductoBa = false;
        boolean tipoProductoTv = false;
        boolean tipoProductoTo = false;
        try {
            if (producto.getString("Producto").equalsIgnoreCase("TO")) {
                 Portafolio.add(new ListaDefault(0, "TO", "Titulo"));
            }else if (producto.getString("Producto").equalsIgnoreCase("TV") || producto.getString("Producto").equalsIgnoreCase("TELEV")) {
                Portafolio.add(new ListaDefault(0, "TV", "Titulo"));
            }else if (producto.getString("Producto").equalsIgnoreCase("BA") || producto.getString("Producto").equalsIgnoreCase("INTER")) {
                Portafolio.add(new ListaDefault(0, "BA", "Titulo"));
                tipoProductoBa = true;
            }

            Portafolio.add(new ListaDefault(2, "Cliente Id", producto.getString("clienteId")));
            Portafolio.add(new ListaDefault(2, "Identificador", producto.getString("Identificador")));
            Portafolio.add(new ListaDefault(2, "Plan Id", producto.getString("PlanId")));
            Portafolio.add(new ListaDefault(2, "Nombre Plan", producto.getString("Plan")));
            Portafolio.add(new ListaDefault(2, "Tecnologia", producto.getString("Tecnologia")));
            Portafolio.add(new ListaDefault(2, "Tipo Factura", producto.getString("TipoFactura")));

            if(tipoProductoBa){
                Portafolio.add(new ListaDefault(2, "Velocidad Original", producto.getString("VelocidadOriginal")));
                if(!producto.getString("Gota").equalsIgnoreCase("") && !producto.getString("Gota").equalsIgnoreCase("null")) {
                    Portafolio.add(new ListaDefault(2, "Gota", producto.getString("Gota")));
                    Portafolio.add(new ListaDefault(2, "Velocidad Gota", producto.getString("Velocidad_Gota")));
                }
            }

            Portafolio.add(new ListaDefault(2, "Adicionales", producto.getString("Adicionales")));

        } catch (JSONException e) {
            //e.printStackTrace();
            Log.w("Error portafolioProductos ", e.getMessage());
        }
    }
}
