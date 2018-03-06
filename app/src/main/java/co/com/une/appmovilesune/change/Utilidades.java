package co.com.une.appmovilesune.change;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.*;

import android.R.string;
import android.content.Context;
import android.content.Intent;
import android.util.*;

import com.google.analytics.tracking.android.Log;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.xml.simpleparser.NewLineHandler;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.BloqueoCobertura;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ItemTarificador;
import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.adapters.ListaIpDinamica;
import co.com.une.appmovilesune.complements.Calendario;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.model.AdicionalCotizador;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.Decodificadores;
import co.com.une.appmovilesune.model.Localizacion;
import co.com.une.appmovilesune.model.Producto;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.Venta;

import android.widget.Toast;

public class Utilidades {

    public static String Bogota = "Distrito Capital De Bogota";
    public static String inicial = "--Seleccione Producto--";
    public static String inicial_guion = "-";
    public static String inicial_vacio = "";
    public static String inicial_des = "--Seleccione Descuento--";
    public static String inicial_sindes = "--Sin Descuentos--";
    public static String inicial_estrato = "--Seleccione Estrato--";
    public static String inicial_plan = "--Seleccione Tipo--";

    public static String inicial_opcion = "-- Seleccione Opción --";
    public static String inicial_dominio = "-- Seleccione Dominio --";
    public static String inicial_sub_motivo = "-- Seleccione SubMotivo --";

    public static String seleccioneDepartamento = "--Seleccione Departamento--";
    public static String seleccioneEstrato = "--Seleccione Estrato--";
    public static String seleccioneMunicipio = "--Seleccione Municipio--";
    public static String iniDepartamento = "--Sin Departamento--";
    public static String iniMunicipio = "--Sin Municipio--";

    public static String[] inicialMunicipio = {iniMunicipio};

    public static String tipo_producto_to = "to", tipo_producto_ba = "ba", tipo_producto_tv = "tv",
            tipo_producto_3g = "im", tipo_producto_4g = "4G";
    public static String tipo_producto_to_d = "TO", tipo_producto_ba_d = "BA", tipo_producto_tv_d = "TV",
            tipo_producto_3g_d = "3G", tipo_producto_4g_d = "4G", tipo_producto_otros = "OTROS",
            tipo_producto_ad = "AD", tipo_producto_gota = "gota", tipo_planes = "PlanesFacturacion";
    public static String[] estrato = {"--Seleccione Estrato--", "1", "2", "3", "4", "5", "6"};
    public static String[] SI_NO = {"SI", "NO"};
    public static String[] tipoUsuario = {"Asesor", "Supervisor"};
    public static String[] plan = {"-", "N", "C", "E"};

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    // private static final String PATTERN_EMAIL_2 =
    // "^[a-zA-Z0-9_-.]{2,15}@[a-zA-Z0-9_-]{2,15}.[a-zA-Z]{2,4}(.[a-zA-Z]{2,4})?$";

    // Pattern p = Pattern.compile("");

    private static final String PATTERN_LOGIN = "^[_A-Za-z0-9-\\.]";

    public static String iniDireccion = "--Sin Direccion--";
    public static String seleccioneDireccion = "--Seleccione Direccion--";
    public static String[] inicialDireccion = {iniDireccion};
    public static String iniTelevisores = "-- Seleccione Televisores --";

    public static String paqueteNuevo = "Nuevo";
    public static String paqueteEliminar = "Eliminar";
    public static String nombreHBOGO = "HBO GO";
    public static String nombreCrackleBa = "Crackle Sony Ba";
    public static double precioCero = 0;
    public static String tipoAdicionalBa = "AD_BA";

    public static String id_ivr = "841";
    public static String nombre_ivr_venta = "finalizacion_venta";
    public static String nombre_ivr_scoring = "scoring";
    public static int tipoIvrScoring = 1;
    public static int tipoIvrVenta = 5;
    public static int tipoIvrRepetirCodigo = 6;

    public Utilidades() {

    }

    public static String OfertaProductos(String producto, String tipo) {

        String oferta = "";
        String tabla = "";
        String campo = "";
        String filtro = "";

        if (tipo.equalsIgnoreCase("prod")) {
            tabla = "Productos";
            campo = "Oferta";
            filtro = "Producto=?";
        } else if (tipo.equalsIgnoreCase("adic")) {
            tabla = "Adicionales";
            campo = "oferta";
            filtro = "adicional=?";
        }

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, tabla, new String[]{campo},
                filtro, new String[]{producto}, null, null, null);

        System.out.println("respuesta " + respuesta);

        if (respuesta != null) {
            try {
                oferta = respuesta.get(0).get(0);
            } catch (Exception e) {
                oferta = "";
            }
        }

        return oferta;

    }

    public static String limpiarTelefono(String numero) {

        System.out.println("numero Antes " + numero);
        if (numero.contains("(") && numero.contains(")")) {
            numero = numero.substring(3, numero.length());
            numero = numero.trim();
        }
        System.out.println("numero Despues " + numero);
        return numero;
    }

    public static String limpiarTelefonoSiebel(String numero) {
        System.out.println("numero Antes " + numero);
        if (numero.contains("(") && numero.contains(")")) {
            String indicativo = numero.substring(1, 2);
            numero = numero.substring(3, numero.length());
            numero = indicativo + "" + numero;
            System.out.println("numero " + numero);
        }
        System.out.println("numero Despues " + numero);

        return numero;
    }

    public static String Parser_Fecha(String Dato) {

        String Resultado = "";
        String[] Fecha = Dato.split("T");

        String[] Hora = Fecha[1].split("-");

        Resultado = Fecha[0] + " " + Hora[0];

        return Resultado;
    }

    public static int parserDisponibilidad(String Dato) {
        int numero = 0;
        try {
            numero = Integer.parseInt(Dato);
            if (numero <= 0) {
                numero = 0;
            }
        } catch (NumberFormatException e) {
            numero = 0;
        }
        return numero;
    }

    public static boolean validateEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static boolean validateLogin(String input) {
        boolean valido = true;

        char login[] = input.toCharArray();

        if (!Character.isLetter(login[0])) {
            return false;
        }
        // String input = "www.?regular.com";
        // comprueba que no empieze por punto o @
        // Pattern p = Pattern.compile("^.|^@");
        Pattern p = Pattern.compile("^.");
        Matcher m = p.matcher(input);
        if (m.find())
            System.err.println("Las direcciones email no empiezan por punto o @");

        // comprueba que no empieze por www.
        p = Pattern.compile("^www.");
        m = p.matcher(input);
        if (m.find()) {
            System.out.println("Los emails no empiezan por www");
            valido = false;
        }

        // comprueba que contenga @
        /*
         * p = Pattern.compile("@"); m = p.matcher(input); if (!m.find())
		 * System.out.println("La cadena no tiene arroba");
		 */

        // comprueba que no contenga caracteres prohibidos
        // p = Pattern.compile("[^A-Za-z0-9.@_-~#]+");
        p = Pattern.compile("[^A-Za-z0-9._-]+");
        m = p.matcher(input);
        StringBuffer sb = new StringBuffer();
        boolean resultado = m.find();
        boolean caracteresIlegales = false;

        while (resultado) {
            caracteresIlegales = true;
            m.appendReplacement(sb, "");
            resultado = m.find();
        }

        m.appendTail(sb);

        input = sb.toString();

        if (caracteresIlegales) {
            System.out.println("La cadena contiene caracteres ilegales");
            valido = false;
        }

        return valido;

    }

    public static JSONObject jsonLocalizacion(String latitud, String longitud, String tipo) {

        int tipoLocalizacion = 0;

        if (tipo.equalsIgnoreCase("Default")) {
            tipoLocalizacion = 0;
        } else if (tipo.equalsIgnoreCase("GPS")) {
            tipoLocalizacion = 1;
        } else {
            tipoLocalizacion = 2;
        }

        JSONObject localizacion = new JSONObject();
        try {
            localizacion.put("latitud", latitud);
            localizacion.put("longitud", longitud);
            localizacion.put("tipo", tipoLocalizacion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return localizacion;
    }

    public static String homologado(String producto) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Productos",
                new String[]{"ProductoHomologado"}, "Producto = ?", new String[]{producto}, null, null, null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "";
        }
    }

    public static JSONObject jsonMensajes(String titulo, String datos) {

        JSONObject mensajes = new JSONObject();
        try {
            mensajes.put("titulo", titulo);
            mensajes.put("datos", datos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mensajes;
    }

    public static JSONObject jsonProductos(String tipoProducto, String producto, String precio, String descuento,
                                           String duracion, String permanencia) {
        JSONObject productos = new JSONObject();
        try {
            productos.put("tipo", tipoProducto);
            productos.put("producto", producto);
            productos.put("precio", precio);
            productos.put("descuento", descuento);
            productos.put("duracion", duracion);
            productos.put("permanencia", permanencia);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productos;
    }

    public static JSONObject jsonTelefonos(String tipo, String numero) {
        JSONObject telefonos = new JSONObject();

        try {
            telefonos.put("tipo", tipo);
            telefonos.put("numero", numero);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return telefonos;
    }

    public static JSONObject jsonDirecciones(String municipio, String departamento, String barrio, String codigoBarrio,
                                             String tipo, String direccion, String direccionNormalizada, String direccionConsulta, String contratoepm,
                                             String paginacion, String paginacionAsignacion, String puntoreferencia) {
        JSONObject direcciones = new JSONObject();
        try {
            direcciones.put("municipio", municipio);
            direcciones.put("departamento", departamento);
            direcciones.put("barrio", barrio);
            direcciones.put("codigoBarrio", codigoBarrio);
            direcciones.put("tipo", tipo);

            if (tipo.equalsIgnoreCase("Servicio") && Utilidades.visible("portafolioATC", municipio)) {
                if (!direccionNormalizada.equalsIgnoreCase("")) {
                    direcciones.put("direccion", direccionNormalizada);
                } else {
                    direcciones.put("direccion", direccion);
                }
            } else {
                direcciones.put("direccion", direccion);
            }

            direcciones.put("direccionCliente", direccion);
            direcciones.put("direccionConsulta", direccionConsulta);

            direcciones.put("contratoepm", contratoepm);
            direcciones.put("paginacion", paginacion);
            direcciones.put("paginacion", paginacion);
            if (paginacionAsignacion == null) {
                paginacionAsignacion = "";
            }
            direcciones.put("paginaAsignacion", paginacionAsignacion);
            direcciones.put("puntoreferencia", puntoreferencia);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return direcciones;
    }

    public static JSONObject jsonContactos(String nombres, String apellidos, String telefono, String celular,
                                           String parentesco, int numero) {

        JSONObject contactos = new JSONObject();
        try {
            contactos.put("nombres", nombres);
            contactos.put("apellidos", apellidos);
            contactos.put("telefono", telefono);
            contactos.put("celular", celular);
            System.out.println("parentesco" + parentesco);
            if (parentesco == null || parentesco.equalsIgnoreCase("")) {
                contactos.put("parentesco", "");
            } else {
                contactos.put("parentesco", parentesco);
            }
            contactos.put("numero", numero);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contactos;
    }

    public static JSONObject jsonProductosVenta(String nombre, String tipo, String cambioPlan, String planAnterior,
                                                String precio, String descuento, String duracion, String extensiones, String wifi, String linea,
                                                String marca, String referencia, String capacidad, String adicionales, String planFacturacion,
                                                String tecnologia, String decos, String tipoCotizacion, String identificador, String tecnologiacr,
                                                Decodificadores decodificador, String ipDinamica, String pagoAntCargoFijo, String pagoParcialConexion) {

        System.out.println("jsonProductosVenta->tecnologiacr " + tecnologiacr);
        JSONArray arrayAdicionales = null;

        JSONArray arrayEquipos = new JSONArray();
        JSONObject mejorasDecos = new JSONObject();

        System.out.println("adicionales " + adicionales);
        try {

            System.out.println("adicionales " + adicionales);

            arrayAdicionales = new JSONArray(adicionales);

        } catch (JSONException e1) {
            // e1.printStackTrace();
            Log.w("error " + e1.getMessage());
            arrayAdicionales = new JSONArray();
        }

        if (cambioPlan.equalsIgnoreCase("Nueva")) {
            cambioPlan = "0";
        } else {
            cambioPlan = "1";
        }

        if (!wifi.equalsIgnoreCase("null")) {
            if (wifi.equalsIgnoreCase("SI")) {
                wifi = "1";
            } else {
                wifi = "0";
            }
        }

        JSONObject productos = new JSONObject();
        try {
            productos.put("nombre", nombre);
            productos.put("tipoCotizacion", tipoCotizacion);
            productos.put("tipo", tipo);
            productos.put("cambioPlan", cambioPlan);
            productos.put("planAnterior", planAnterior);
            productos.put("precio", precio);
            productos.put("pagoAntCargoFijo", pagoAntCargoFijo);
            productos.put("pagoParcialConexion", pagoParcialConexion);
            productos.put("descuento", descuento);
            productos.put("duracion", duracion);
            productos.put("extensiones", extensiones);
            productos.put("linea", linea);
            productos.put("wifi", wifi);
            productos.put("marca", marca);
            productos.put("referencia", referencia);
            productos.put("capacidad", capacidad);
            productos.put("homologado", "");
            productos.put("planfacturacion", "");
            productos.put("identificador", identificador);
            productos.put("ipDinamica", ipDinamica);
            productos.put("adicionales", arrayAdicionales);
            productos.put("planFacturacion", planFacturacion);
            if (!tecnologia.equalsIgnoreCase("IPTV")) {
                productos.put("tecnologia", tecnologia);
            } else {
                productos.put("tecnologia", "REDCO");
            }
            productos.put("tecnologiacr", tecnologiacr);

            System.out.println("decos " + decos);

            if (!decos.equalsIgnoreCase("") && !decos.equalsIgnoreCase("N/A")) {

                productos.put("decos", new JSONObject(decos));

                mejorasDecos.put("decoPosicion1", new JSONObject(decos).get("decoPosicion1"));
                mejorasDecos.put("decoPosicion2", new JSONObject(decos).get("decoPosicion2"));
                mejorasDecos.put("decoPosicion3", new JSONObject(decos).get("decoPosicion3"));
                mejorasDecos.put("decoPosicion4", new JSONObject(decos).get("decoPosicion4"));

            }

            ArrayList<ItemDecodificador> itemDecodificadors = null;

            System.out.println("decodificador tipo ["+tipo+"] "+decodificador);

            if(decodificador != null){
                itemDecodificadors = decodificador.getItemDecodificadors();
            }

            UtilidadesDecos.imprimirDecos("itemDecodificadors jsonProductosVenta tipo ["+tipo+"] ",itemDecodificadors);

            if (itemDecodificadors != null && itemDecodificadors.size() > 0) {
                UtilidadesDecos.imprimirDecos("Consolidar ", itemDecodificadors);
                ArrayList<JSONObject> objectEquipos = new ArrayList<JSONObject>();

                String decosActuales = "";
                String infoDecosActuales = "";

                for (int i = 0; i < itemDecodificadors.size(); i++) {

                    System.out.println("itemDecodificadors.get(i).getInfoActualesDecos() "
                            + itemDecodificadors.get(i).getInfoActualesDecos());
                    System.out.println("itemDecodificadors.get(i).isNuevo() " + itemDecodificadors.get(i).isNuevo());

                    if (!itemDecodificadors.get(i).isNuevo()) {
                        decosActuales = itemDecodificadors.get(i).getDecosActuales();
                        infoDecosActuales = itemDecodificadors.get(i).getInfoActualesDecos();
                        break;
                    }
                }

                System.out.println("decosActuales A " + decosActuales);
                System.out.println("infoDecosActuales A " + infoDecosActuales);

                if (decosActuales.equalsIgnoreCase("") && infoDecosActuales.equalsIgnoreCase("")) {
                    for (int i = 0; i < itemDecodificadors.size(); i++) {
                        if (itemDecodificadors.get(i).isIncluido()) {
                            decosActuales = itemDecodificadors.get(i).getDecosActuales();
                            infoDecosActuales = itemDecodificadors.get(i).getInfoActualesDecos();
                            break;
                        }
                    }
                }

                System.out.println("decosActuales B " + decosActuales);
                System.out.println("infoDecosActuales B " + infoDecosActuales);

                for (int i = 0; i < itemDecodificadors.size(); i++) {
                    objectEquipos.add(
                            jsonEquipos(itemDecodificadors.get(i), decosActuales, infoDecosActuales, tipoCotizacion));
                }

                for (int i = 0; i < objectEquipos.size(); i++) {
                    arrayEquipos.put(objectEquipos.get(i));
                    System.out.println("arrayEquipos.get(" + i + ") " + arrayEquipos.get(i).toString());
                }

                System.out.println("arrayEquipos " + arrayEquipos);

                mejorasDecos.put("mejoraDecos", "SI");
                mejorasDecos.put("equipos", arrayEquipos);
                //mejorasDecos.put("decos_sd", new JSONObject(decos).get("decos_sd"));

            }

            productos.put("decos", mejorasDecos);

            if (tipo.equalsIgnoreCase("BA")) {
                productos.put("homologado", Utilidades.homologado(nombre));
            } else {
                productos.put("homologado", "");
            }

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        System.out.println("productos tipo ["+tipo+"] "+productos);
        return productos;
    }

    public static JSONObject jsonEquipos(ItemDecodificador itemDecodificador, String decosActuales,
                                         String infoDecosActuales, String tipoCotizacion) {

        JSONObject equipos = new JSONObject();

        try {

            // // if (itemDecodificador.getDecosActuales() != null) {
            // // equipos.put("actuales", itemDecodificador.getDecosActuales());
            // // } else {
            // // equipos.put("actuales", "");
            // // }
            //
            // System.out.println("enviar venta decos tipoCotizacion
            // "+tipoCotizacion);
            // System.out.println("enviar venta decos
            // itemDecodificador.getTipoTransaccion()
            // "+itemDecodificador.getTipoTransaccion());
            //

            equipos.put("actuales", decosActuales);

            if (decosActuales != null) {
                equipos.put("actuales", decosActuales);
            } else {
                equipos.put("actuales", "");
            }
            // equipos.put("infoActuales", infoDecosActuales);
            if (infoDecosActuales != null) {
                equipos.put("infoDecosActuales", infoDecosActuales);
            } else {
                equipos.put("infoDecosActuales", "");
            }

            if (itemDecodificador.getTipoTransaccion() != null) {
                if (itemDecodificador.getTipoTransaccion().equalsIgnoreCase("Permanece")
                        && tipoCotizacion.equalsIgnoreCase("1")) {
                    equipos.put("accion", "Nuevo");
                } else {
                    equipos.put("accion", itemDecodificador.getTipoTransaccion());
                }
            } else {
                equipos.put("accion", "");
            }

            if (itemDecodificador.getOriginal() != null) {
                equipos.put("original", itemDecodificador.getOriginal());
            } else {
                equipos.put("original", "");
            }

            if (itemDecodificador.getDestino() != null && !itemDecodificador.getDestino().equals("-")) {
                equipos.put("destino", itemDecodificador.getDestino());
            } else {
                equipos.put("destino", "");
            }

            if (itemDecodificador.getEquipoId() != null) {
                equipos.put("equipoid", itemDecodificador.getEquipoId());
            } else {
                equipos.put("equipoid", "");
            }

            if (itemDecodificador.getTipoEquipo() != null) {
                equipos.put("tipo", itemDecodificador.getTipoEquipo());
            } else {
                equipos.put("tipo", "");
            }

            if (itemDecodificador.getMarca() != null) {
                equipos.put("marca", itemDecodificador.getMarca());
            } else {
                equipos.put("marca", "");
            }

            if (itemDecodificador.getSerial() != null) {
                equipos.put("serial", itemDecodificador.getSerial());
            } else {
                equipos.put("serial", "");
            }

            if (itemDecodificador.getReferencia() != null) {
                equipos.put("referencia", itemDecodificador.getReferencia());
            } else {
                equipos.put("referencia", "");
            }

            if (itemDecodificador.getTipoAlquiler() != null) {
                equipos.put("tipoAlquiler", itemDecodificador.getTipoAlquiler());
            } else {
                equipos.put("tipoAlquiler", "");
            }

            if (itemDecodificador.getTipoFideliza() != null) {
                equipos.put("fideliza", itemDecodificador.getTipoFideliza());
            } else {
                equipos.put("fideliza", "");
            }

            if (itemDecodificador.getPrecio() != null) {
                equipos.put("precio", itemDecodificador.getPrecio());
            } else {
                equipos.put("precio", "");
            }

            if (itemDecodificador.getDescuento() != null) {
                equipos.put("descuento", itemDecodificador.getDescuento());
            } else {
                equipos.put("descuento", "");
            }

            if (itemDecodificador.getTiempo() != null) {
                equipos.put("tiempo", itemDecodificador.getTiempo());
            } else {
                equipos.put("tiempo", "");
            }

            if (itemDecodificador.getPlanPromocional() != null) {
                equipos.put("planPromo", itemDecodificador.getPlanPromocional());
            } else {
                equipos.put("planPromo", "");
            }

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        return equipos;
    }

    public static String tocapitalCase(String Dato) {
        String capital = "";

        Dato = Dato.toLowerCase();
        String[] Vector = Dato.split(" ");

        for (int i = 0; i < Vector.length; i++) {
            char[] Arrarchar = Vector[i].toCharArray();

            String temp = "" + Arrarchar[0];

            temp = temp.toUpperCase();

            Arrarchar[0] = (char) temp.charAt(0);

            Vector[i] = String.valueOf(Arrarchar);

            capital += " " + Vector[i];

        }

        return capital.trim();
    }

    public static String ciudadFenix(String ciudad, String departamento) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"CiudadFenix"}, "Ciudad = ? and Departamento= ?", new String[]{ciudad, departamento},
                null, null, null);

        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String departamentoFenix(String departamento) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"DepartamentoFenix"}, "Departamento= ?", new String[]{departamento}, null, null,
                null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String traducirCiudad(String ciudad) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"CiudadFenix"}, "Ciudad = ?", new String[]{ciudad}, null, null, null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String traducirCiudadFenix(String ciudadFenix) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"Ciudad"}, "CiudadFenix = ?", new String[]{ciudadFenix}, null, null, null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String traducirCiudadDane(String ciudad, String departamento) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"CodigoDaneMunicipio"}, "Ciudad = ? and Departamento = ?", new String[]{ciudad, departamento}, null, null, null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String traducirDepartamentoDane(String departamento) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(true, "Departamentos",
                new String[]{"CodigoDaneDepartamento"}, "Departamento = ?", new String[]{departamento}, null, null, null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String traducirCiudadGIIS(String ciudad) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"Codigo_Giis"}, "Ciudad = ?", new String[]{ciudad}, null, null, null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String traducirCiudadAmc(String ciudad) {
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"Codigo_Amc"}, "Ciudad = ?", new String[]{ciudad}, null, null, null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String traducirCodigoBarrio(String barrio) {

        System.out.println("Barrio " + barrio);

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "barrios",
                new String[]{"codigo"}, "barrio = ?", new String[]{barrio}, null, null, null);
        if (resultado != null) {
            return resultado.get(0).get(0);
        } else {
            return "Ninguna";
        }
    }

    public static String quitaEspacios(String texto) {
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
        texto = "";
        while (tokens.hasMoreTokens()) {
            texto += " " + tokens.nextToken();
        }
        texto = texto.toString();
        texto = texto.trim();
        return texto;
    }

    public static String limpiar_opcion(String dato) {
        String result = dato;
        if (result.equalsIgnoreCase("-- Seleccione Opción --")) {
            result = "-- Seleccione Opcion --";
        }
        return result;
    }

    private static Map<Character, Character> MAP_NORM;

    public static String Quitar_Tildes2(String value) {

        if (MAP_NORM == null || MAP_NORM.size() == 0) {

            MAP_NORM = new HashMap<Character, Character>();
            MAP_NORM.put('À', 'A');
            MAP_NORM.put('Á', 'A');
            MAP_NORM.put('Â', 'A');
            MAP_NORM.put('Ã', 'A');
            MAP_NORM.put('Ä', 'A');
            MAP_NORM.put('È', 'E');
            MAP_NORM.put('É', 'E');
            MAP_NORM.put('Ê', 'E');
            MAP_NORM.put('Ë', 'E');
            MAP_NORM.put('Í', 'I');
            MAP_NORM.put('Ì', 'I');
            MAP_NORM.put('Î', 'I');
            MAP_NORM.put('Ï', 'I');
            MAP_NORM.put('Ù', 'U');
            MAP_NORM.put('Ú', 'U');
            MAP_NORM.put('Û', 'U');
            MAP_NORM.put('Ü', 'U');
            MAP_NORM.put('Ò', 'O');
            MAP_NORM.put('Ó', 'O');
            MAP_NORM.put('Ô', 'O');
            MAP_NORM.put('Õ', 'O');
            MAP_NORM.put('Ö', 'O');
            MAP_NORM.put('Ñ', 'N');
            MAP_NORM.put('Ç', 'C');
            MAP_NORM.put('ª', 'A');
            MAP_NORM.put('º', 'O');
            MAP_NORM.put('§', 'S');
            MAP_NORM.put('³', '3');
            MAP_NORM.put('²', '2');
            MAP_NORM.put('¹', '1');
            MAP_NORM.put('à', 'a');
            MAP_NORM.put('á', 'a');
            MAP_NORM.put('â', 'a');
            MAP_NORM.put('ã', 'a');
            MAP_NORM.put('ä', 'a');
            MAP_NORM.put('è', 'e');
            MAP_NORM.put('é', 'e');
            MAP_NORM.put('ê', 'e');
            MAP_NORM.put('ë', 'e');
            MAP_NORM.put('í', 'i');
            MAP_NORM.put('ì', 'i');
            MAP_NORM.put('î', 'i');
            MAP_NORM.put('ï', 'i');
            MAP_NORM.put('ù', 'u');
            MAP_NORM.put('ú', 'u');
            MAP_NORM.put('û', 'u');
            MAP_NORM.put('ü', 'u');
            MAP_NORM.put('ò', 'o');
            MAP_NORM.put('ó', 'o');
            MAP_NORM.put('ô', 'o');
            MAP_NORM.put('õ', 'o');
            MAP_NORM.put('ö', 'o');
            MAP_NORM.put('ñ', 'n');
            MAP_NORM.put('ç', 'c');
        }

        if (value == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(value);

        for (int i = 0; i < value.length(); i++) {
            Character c = MAP_NORM.get(sb.charAt(i));
            if (c != null) {
                sb.setCharAt(i, c.charValue());
            }
        }

        return sb.toString();

    }

    // METODO QUE SIRVE PARA QUITAR TILDES Y CONVERTIR EL RESULTADO A MAYUSCULAS

    public static String Quitar_Tildes(String Dato) {

        String tustring = Dato;
        tustring = tustring.replace('á', 'a');
        tustring = tustring.replace('é', 'e');
        tustring = tustring.replace('í', 'i');
        tustring = tustring.replace('ó', 'o');
        tustring = tustring.replace('ú', 'u');
        tustring = tustring.replace('Á', 'A');
        tustring = tustring.replace('É', 'E');
        tustring = tustring.replace('Í', 'I');
        tustring = tustring.replace('Ó', 'O');
        tustring = tustring.replace('Ú', 'U');
        tustring = tustring.replace('ñ', 'n');
        tustring = tustring.replace('Ñ', 'N');
        return tustring.toUpperCase();

    }

    // METODO QUE SIRVE PARA QUITAR TILDES
    public static String Quitar_Solo_Tildes(String Dato) {

        String tustring = Dato;
        tustring = tustring.replace('á', 'a');
        tustring = tustring.replace('é', 'e');
        tustring = tustring.replace('í', 'i');
        tustring = tustring.replace('ó', 'o');
        tustring = tustring.replace('ú', 'u');
        tustring = tustring.replace('Á', 'A');
        tustring = tustring.replace('É', 'E');
        tustring = tustring.replace('Í', 'I');
        tustring = tustring.replace('Ó', 'O');
        tustring = tustring.replace('Ú', 'U');
        tustring = tustring.replace('ñ', 'n');
        tustring = tustring.replace('Ñ', 'N');
        return tustring;

    }

    public static String Identificador(String Identificador) {
        String Respuesta = "";
        if (Identificador.equalsIgnoreCase("CEDULA")) {
            Respuesta = "1";
        } else if (Identificador.equalsIgnoreCase("CEDULA DE EXTANJERIA")) {
            Respuesta = "3";
        } else if (Identificador.equalsIgnoreCase("NIT")) {
            Respuesta = "2";
        }

        return Respuesta;

    }

    public static String Tipo_Producto(String Tipo_Producto) {
        String Respuesta = "";

        Tipo_Producto = Tipo_Producto.toLowerCase();

        if (Tipo_Producto.equalsIgnoreCase("to")) {
            Respuesta = "TO";
        } else if (Tipo_Producto.equalsIgnoreCase("tv")) {
            Respuesta = "TV";
        } else if (Tipo_Producto.equalsIgnoreCase("ba")) {
            Respuesta = "BA";
        } else if (Tipo_Producto.equalsIgnoreCase("im")) {
            Respuesta = "3G";
        } else if (Tipo_Producto.equalsIgnoreCase("4g")) {
            Respuesta = "4G";
        }

        return Respuesta;
    }

    public static String ponerIndicativo(String telefono) {
        String tel = telefono;
        if (tel.startsWith("(")) {
            String indicativo = tel.substring(1, 2);
            String telef = tel.substring(3);
            tel = "05" + indicativo + telef;
        }
        return tel;
    }

    public static String[] St_Identificador = {"CEDULA", "CEDULA DE EXTANJERIA", "NIT"};
    public static String Version_Android = "Venta Movil 1.1 Android";
    public static String[] StElemento_A = {"Seleccione", "AVDA", "CL", "AC", "AK", "CR", "CQ", "DIAG", "TRAN", "MZ",
            "CVR"};
    public static String[] StElemento_D = {"----", "NORTE", "SUR", "ESTE", "OESTE", "BIS"};
    public static String[] StLetras = {"----", "A", "AA", "AAA", "AB", "AC", "AD", "AE", "AF", "AG", "B", "BA", "BB",
            "BBB", "BC", "BD", "BE", "BF", "BG", "C", "CA", "CB", "CC", "CCC", "CD", "CE", "CF", "CG", "D", "DA", "DB",
            "DC", "DD", "DDD", "DE", "DF", "DG", "E", "EA", "EB", "EC", "ED", "EE", "EEE", "EF", "EG", "F", "FA", "FB",
            "FC", "FD", "FE", "FF", "FFF", "FG", "G", "GA", "GB", "GC", "GD", "GE", "GF", "GG", "GGG", "H", "HA", "HB",
            "HC", "HD", "HE", "HF", "HG", "I", "IA", "IB", "IC", "ID", "IE", "IF", "IG", "J", "JA", "JB", "JC", "JD",
            "JE", "JF", "JG", "", "K", "KA", "KB", "KC", "KD", "KE", "KF", "KG", "L", "LA", "LB", "LC", "LD", "LE",
            "LF", "LG", "M", "MA", "MB", "MC", "MD", "ME", "MF", "MG", "N", "NA", "NB", "NC", "ND", "NE", "NF", "NG",
            "O", "OA", "OB", "OC", "OD", "OE", "OF", "OG", "P", "PA", "PB", "PC", "PD", "PE", "PF", "PG", "Q", "QA",
            "QB", "QC", "QD", "QE", "QF", "QG", "R", "RA", "RB", "RC", "RD", "RE", "RF", "RG", "S", "SA", "SB", "SC",
            "SD", "SE", "SF", "SG", "T", "TA", "TB", "TC", "TD", "TE", "TF", "TG", "", "U", "UA", "UB", "UC", "UD",
            "UE", "UF", "UG", "V", "VA", "VB", "VC", "VD", "VE", "VF", "VG", "W", "WA", "WB", "WC", "WD", "WE", "WF",
            "WG", "X", "XA", "XB", "XC", "XD", "XE", "XF", "XG", "Y", "YA", "YB", "YC", "YD", "YE", "YF", "YG", "Z",
            "ZA", "ZC", "ZD", "ZE", "ZF", "ZG"};

    public static String[] StLetras_Inicial = {"----", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};

    public static String[] Campos_Ventas = {"Codigo Asesor", "Departamento", "Ciudad", "Estrato", "Nombre Cliente",
            "Cedula", "Expedicion", "Direccion", "Paginacion", "Unidad_Residencial", "Barrio", "Telefono", "Telefono_2",
            "Celular", "Email", "Login", "Nombre_Referencia", "Telefono_Referencia", "Nombre_Referencia_2",
            "Telefono_Referencia_2", "Observaciones", "Documentacion", "Telefonia", "Precio_Telefonia",
            "Nuevo_Migracion_To", "Promo_To", "Linea", "Television", "Precio_Television", "Extensiones",
            "Adiccional_Tv", "Precio_Adiccional_Tv", "Promo_Tv", "Nuevo_Migracion", "Internet", "Precio_Internet",
            "Promo_Ba", "Wifi", "Internet_3G", "Precio_Internet_3G", "Nuevo_Migracion_3G", "Promo_3G", "Moden",
            "Internet_4G", "Precio_Internet_4G", "Promo_4G", "Otras_Promociones", "Total", "Empaquetamiento"};

    public static String[] STVenta = {"-"};

    public static String[] franjas = {"AM", "PM"};

    public static String[] asesorias;

    public static String[] tipo_asesoria = { /* "Prospecto", */
            "Gerencia De Ruta", "Venta"};

    public static void setAsesorias(ArrayList<ArrayList<String>> valores) {
        asesorias = new String[valores.size()];
        for (int i = 0; i < valores.size(); i++) {
            asesorias[i] = valores.get(i).get(0) + " - " + valores.get(i).get(1);
        }
    }

    public static boolean ListaNegra(String Valor, String Clave) {

        boolean respuesta = false;
        ArrayList<String[]> listanegra = new ArrayList<String[]>();

        listanegra.add(new String[]{"Precio_Telefonia", "Telefonia"});
        listanegra.add(new String[]{"Nuevo_Migracion_To", "Telefonia"});
        listanegra.add(new String[]{"Promo_To", "Telefonia"});
        listanegra.add(new String[]{"Linea", "Telefonia"});
        listanegra.add(new String[]{"Precio_Television", "Television"});
        listanegra.add(new String[]{"Extensiones", "Television"});
        listanegra.add(new String[]{"Promo_Tv", "Television"});
        listanegra.add(new String[]{"Precio_Adiccional_Tv", "Adiccional_Tv"});
        listanegra.add(new String[]{"Nuevo_Migracion", "Internet"});
        listanegra.add(new String[]{"Precio_Internet", "Internet"});
        listanegra.add(new String[]{"Promo_Ba", "Internet"});
        listanegra.add(new String[]{"Wifi", "Internet"});
        listanegra.add(new String[]{"Precio_Internet_Movil", "Internet_Movil"});
        listanegra.add(new String[]{"Nuevo_Migracion_Im", "Internet_Movil"});
        listanegra.add(new String[]{"Promo_Im", "Internet_Movil"});
        listanegra.add(new String[]{"Moden", "Internet_Movil"});
        listanegra.add(new String[]{"Precio_Internet_4G", "Internet_4G"});
        listanegra.add(new String[]{"Promo_4G", "Internet_Movil"});
        listanegra.add(new String[]{"Precio_Internet_4G", "Internet_4G"});
        listanegra.add(new String[]{"Ciudad", "Localizacion"});
        listanegra.add(new String[]{"Estrato", "Localizacion"});
        listanegra.add(new String[]{"Cedula", "Cliente"});
        listanegra.add(new String[]{"Expedicion", "Cliente"});
        listanegra.add(new String[]{"Direccion", "Cliente"});
        listanegra.add(new String[]{"Paginacion", "Cliente"});
        listanegra.add(new String[]{"Unidad_Residencial", "Cliente"});
        listanegra.add(new String[]{"Barrio", "Cliente"});
        listanegra.add(new String[]{"Telefono", "Cliente"});
        listanegra.add(new String[]{"Telefono_2", "Cliente"});
        listanegra.add(new String[]{"Email", "Cliente"});
        listanegra.add(new String[]{"Login", "Cliente"});
        listanegra.add(new String[]{"Nombre_Referencia", "Cliente"});
        listanegra.add(new String[]{"Telefono_Referencia", "Cliente"});
        listanegra.add(new String[]{"Nombre_Referencia_2", "Cliente"});
        listanegra.add(new String[]{"Telefono_Referencia_2", "Cliente"});
        listanegra.add(new String[]{"Observaciones", "Cliente"});
        listanegra.add(new String[]{"Documentacion", "Cliente"});

        for (int i = 0; i < listanegra.size(); i++) {
            if (listanegra.get(i)[0].equalsIgnoreCase(Valor) && listanegra.get(i)[1].equalsIgnoreCase(Clave)) {
                respuesta = true;
            }
        }

        return respuesta;

    }

    public static boolean ListaNegraTelefonos(String telefono) {

        boolean respuesta = false;
        ArrayList<String> listanegra = new ArrayList<String>();

        listanegra.add("9999999");
        listanegra.add("99999999");
        listanegra.add("88888888");
        listanegra.add("0000000");
        listanegra.add("573143981698");
        listanegra.add("0000000000");
        listanegra.add("0300000000");
        listanegra.add("000 513436");
        listanegra.add("0300000000");
        listanegra.add("7999999");
        listanegra.add("2323232323");
        listanegra.add("2330000000");
        listanegra.add("3232323232");
        listanegra.add("3222222554");
        listanegra.add("93423800");
        listanegra.add("5723130331");
        listanegra.add("99999");
        listanegra.add("2000000");
        listanegra.add("999999999");
        listanegra.add("9999999999");
        listanegra.add("7777777777");
        listanegra.add("2222222222");
        listanegra.add("4444444444");
        listanegra.add("3000000000");
        listanegra.add("0300000");
        listanegra.add("0101010");
        listanegra.add("0158966");
        listanegra.add("(5)6450");
        listanegra.add("  00 00");
        listanegra.add(" ()0000");
        listanegra.add(" (0)000");
        listanegra.add(" 0 0000");
        listanegra.add("000000");
        listanegra.add("2 00000");
        listanegra.add("(0)0000");
        listanegra.add("(0) 000");
        listanegra.add("7570000");
        listanegra.add("2222222");
        listanegra.add("3099999999");
        listanegra.add("7034 (000)");
        listanegra.add("49 0000");
        listanegra.add("4 00000");
        listanegra.add("4  0000");
        listanegra.add("316 534 22");
        listanegra.add("314 880065");
        listanegra.add("314 854206");
        listanegra.add("313 765052");
        listanegra.add("311336 193");
        listanegra.add("310 468626");
        listanegra.add("310 341 86");
        listanegra.add("300 808905");
        listanegra.add("300 324448");
        listanegra.add("25 0000");
        listanegra.add("2 00000");
        listanegra.add(" 00 000");
        listanegra.add(" 0 0000");
        listanegra.add(" (0)000");
        listanegra.add(" ()0000");
        listanegra.add("  00 00");
        listanegra.add("300000000");
        listanegra.add("4000000000");
        listanegra.add("555555555");
        listanegra.add("333333333");
        listanegra.add("9899999999");
        listanegra.add("9699999999");
        listanegra.add("8899999999");
        listanegra.add("8110461395");
        listanegra.add("6666666666");
        listanegra.add("5444444444");
        listanegra.add("4744444444");
        listanegra.add("4444444445");
        listanegra.add("4444444444");
        listanegra.add("4000000000");
        listanegra.add("3999999999");
        listanegra.add("6065555000");
        listanegra.add("6666666666");
        listanegra.add("6565454545");
        listanegra.add("6450000000");
        listanegra.add("6173802486");
        listanegra.add("6156446932");
        listanegra.add("9999999999");
        listanegra.add("9899999999");
        listanegra.add("9699999999");
        listanegra.add("8899999999");
        listanegra.add("8110461395");
        listanegra.add("7777777777");
        listanegra.add("6666666666");
        listanegra.add("6565454545");
        listanegra.add("6450000000");
        listanegra.add("6173802486");
        listanegra.add("6156446932");
        listanegra.add("6065555000");
        listanegra.add("5555555555");
        listanegra.add("5531654101");
        listanegra.add("5465464564");
        listanegra.add("5444444444");
        listanegra.add("5113457314");
        listanegra.add("4931419000");
        listanegra.add("4844848484");
        listanegra.add("4744444444");
        listanegra.add("4715960471");
        listanegra.add("4545646456");
        listanegra.add("4510736451");
        listanegra.add("4509032297");
        listanegra.add("4444444445");
        listanegra.add("4444444444");
        listanegra.add("4310412479");
        listanegra.add("4256430125");
        listanegra.add("4234234234");
        listanegra.add("4000000000");
        listanegra.add("2931999679");
        listanegra.add("2857311011");
        listanegra.add("2333333333");
        listanegra.add("2222222222");
        listanegra.add("2127120835");
        listanegra.add("2125514121");
        listanegra.add("2122222222");
        listanegra.add("2111111111");
        listanegra.add("2001866216");
        listanegra.add("2001793007");
        listanegra.add("2001561366");
        listanegra.add("5555555");
        listanegra.add("7777777");
        listanegra.add("7000000");
        listanegra.add("6666666");
        listanegra.add("3333333333");
        listanegra.add("4999999");
        listanegra.add("4075207011");
        listanegra.add("4206468687");
        listanegra.add("4440127 30");
        listanegra.add("4496432200");
        listanegra.add("4551530300");
        listanegra.add("4874878787");
        listanegra.add("4999999999");
        listanegra.add("5116768000");
        listanegra.add("5454544544");
        listanegra.add("5454654665");
        listanegra.add("5538412424");
        listanegra.add("5679217222");
        listanegra.add("6808963323");
        listanegra.add("7666666666");

        for (int i = 0; i < listanegra.size(); i++) {
            if (listanegra.get(i).equalsIgnoreCase(telefono)) {
                System.out.println("listanegra.get(i) " + listanegra.get(i) + " telefono " + telefono);
                respuesta = true;
            }
        }

        return respuesta;

    }

    public static boolean ListaNegraCedula(String cedula) {

        boolean respuesta = false;
        ArrayList<String> listanegra = new ArrayList<String>();

        listanegra.add("0000");
        listanegra.add("1111");
        listanegra.add("2222");
        listanegra.add("3333");
        listanegra.add("4444");
        listanegra.add("5555");
        listanegra.add("6666");
        listanegra.add("7777");
        listanegra.add("8888");
        listanegra.add("9999");
        listanegra.add("00000");
        listanegra.add("11111");
        listanegra.add("22222");
        listanegra.add("33333");
        listanegra.add("44444");
        listanegra.add("55555");
        listanegra.add("66666");
        listanegra.add("77777");
        listanegra.add("88888");
        listanegra.add("99999");
        listanegra.add("000000");
        listanegra.add("111111");
        listanegra.add("222222");
        listanegra.add("333333");
        listanegra.add("444444");
        listanegra.add("555555");
        listanegra.add("666666");
        listanegra.add("777777");
        listanegra.add("888888");
        listanegra.add("999999");
        listanegra.add("0000000");
        listanegra.add("1111111");
        listanegra.add("2222222");
        listanegra.add("3333333");
        listanegra.add("4444444");
        listanegra.add("5555555");
        listanegra.add("6666666");
        listanegra.add("7777777");
        listanegra.add("8888888");
        listanegra.add("9999999");
        listanegra.add("00000000");
        listanegra.add("11111111");
        listanegra.add("22222222");
        listanegra.add("33333333");
        listanegra.add("44444444");
        listanegra.add("55555555");
        listanegra.add("66666666");
        listanegra.add("77777777");
        listanegra.add("88888888");
        listanegra.add("99999999");
        listanegra.add("0000000000");
        listanegra.add("1111111111");
        listanegra.add("2222222222");
        listanegra.add("3333333333");
        listanegra.add("4444444444");
        listanegra.add("5555555555");
        listanegra.add("6666666666");
        listanegra.add("7777777777");
        listanegra.add("8888888888");
        listanegra.add("9999999999");

        for (int i = 0; i < listanegra.size(); i++) {
            if (listanegra.get(i).equalsIgnoreCase(cedula)) {
                System.out.println("listanegra.get(i) " + listanegra.get(i) + " cedula " + cedula);
                respuesta = true;
            }
        }

        return respuesta;

    }

    public static ArrayList<String> getCotizacion(ArrayList<ItemTarificador> Cotizacion, String Tipo_Producto) {
        ArrayList<String> producto = new ArrayList<String>();
        if (Cotizacion.size() > 0) {

            for (int i = 0; i < Cotizacion.size(); i++) {

                if (Cotizacion.get(i).getTipo_producto().equalsIgnoreCase(Tipo_Producto)) {
                    producto.add(Cotizacion.get(i).getDato());
                }
            }
        }

        return producto;
    }

    public static ArrayList<ListaDefault> getVenta(Venta venta) {
        ArrayList<ListaDefault> listVenta = new ArrayList<ListaDefault>();

        for (int i = 0; i < venta.getCotizacionCliente().getProductoCotizador().size(); i++) {
            ProductoCotizador producto= venta.getCotizacionCliente().getProductoCotizador().get(i);
            if(!Utilidades.validarVacioProducto(producto.getPlan())){
                if(producto.getTipo() == ProductoCotizador.getTELEFONIA()){
                    listVenta.add(new ListaDefault(0, "Productos Telefonia", "Titulo"));
                    listVenta.add(new ListaDefault(1, "Telefonía",producto.getPlan()));
                    listVenta.add(new ListaDefault(1, "Precio", producto.getPrecio()));

                    /*listVenta.add(new ListaDefault(2, "Nuevo o Migración",
                            "Migración: " + Telefonia[4] + " - Tipo Migración: " + Telefonia[5]));*/
                    listVenta.add(new ListaDefault(2, "Linea", "Linea: " + producto.getLinea()/*Telefonia[2]*/ + " - Pago: " + producto.getLinea()/*Telefonia[3]*/));
                    listVenta.add(new ListaDefault(2, "Promociones",
                            "Promoción: " + producto.getDescuentoCargobasico() + " - Duración: " + producto.getDuracionDescuento() + " - Precio: " +"precio con descuento"/*+ Telefonia[8]*/));
                    /*if (!Telefonia[13].equalsIgnoreCase("") || !Telefonia[13].equalsIgnoreCase("-")) {
                        listVenta.add(new ListaDefault(0, "Servicios Especiales TO", "Titulo"));
                        listVenta.add(new ListaDefault(1, "Adicionales", Telefonia[13]));
                        listVenta.add(new ListaDefault(2, "Precio", Telefonia[14]));
                    }*/
                }else if(producto.getTipo() == ProductoCotizador.getTELEVISION()){
                    listVenta.add(new ListaDefault(0, "Productos Television", "Titulo"));
                    listVenta.add(new ListaDefault(1, "Televisión",producto.getPlan()));
                    listVenta.add(new ListaDefault(1, "Precio", producto.getPrecio()));

                    /*listVenta.add(new ListaDefault(2, "Extensiones", "No. de Extensiones: " + Television[2]));
                    listVenta.add(new ListaDefault(2, "Promociones",
                            "Promoción: " + Television[3] + " - Duración: " + Television[4] + " - Precio: " + Television[5]));*/

                    listVenta.add(new ListaDefault(2, "Promociones",
                            "Promoción: " + producto.getDescuentoCargobasico() + " - Duración: " + producto.getDuracionDescuento() + " - Precio: " +"precio con descuento"/*+ Telefonia[8]*/));
                    /*if (!Television[6].equalsIgnoreCase("") || !Television[6].equalsIgnoreCase("-")) {
                        listVenta.add(new ListaDefault(0, "Adicionales de Television", "Titulo"));
                        listVenta.add(new ListaDefault(1, "Adicionales", Television[6]));
                        listVenta.add(new ListaDefault(2, "Precio", Television[7]));
                    }*/
                }else if(producto.getTipo() == ProductoCotizador.getINTERNET()){
                    listVenta.add(new ListaDefault(0, "Productos Internet", "Titulo"));
                    listVenta.add(new ListaDefault(1, "Internet",producto.getPlan()));
                    listVenta.add(new ListaDefault(1, "Precio", producto.getPrecio()));
                    /*listVenta.add(new ListaDefault(2, "Nuevo o Migración",
                            "Migración: " + Internet[3] + " - Tipo Migración: " + Internet[4]));*/
                    listVenta.add(new ListaDefault(2, "Promociones",
                            "Promoción: " + producto.getDescuentoCargobasico() + " - Duración: " + producto.getDuracionDescuento() + " - Precio: " +"precio con descuento"/*+ Telefonia[8]*/));
                    listVenta.add(new ListaDefault(2, "Wifi", producto.getWifi()));
                }
            }
        }

        /*String[] Telefonia = venta.getTelefonia();
        if (!Telefonia[0].equalsIgnoreCase("-")) {
            listVenta.add(new ListaDefault(0, "Productos Telefonia", "Titulo"));
            listVenta.add(new ListaDefault(1, "Telefonía", Telefonia[0]));
            listVenta.add(new ListaDefault(1, "Precio", Telefonia[1]));

            listVenta.add(new ListaDefault(2, "Nuevo o Migración",
                    "Migración: " + Telefonia[4] + " - Tipo Migración: " + Telefonia[5]));
            listVenta.add(new ListaDefault(2, "Linea", "Linea: " + Telefonia[2] + " - Pago: " + Telefonia[3]));
            listVenta.add(new ListaDefault(2, "Promociones",
                    "Promoción: " + Telefonia[6] + " - Duración: " + Telefonia[7] + " - Precio: " + Telefonia[8]));
            if (!Telefonia[13].equalsIgnoreCase("") || !Telefonia[13].equalsIgnoreCase("-")) {
                listVenta.add(new ListaDefault(0, "Servicios Especiales TO", "Titulo"));
                listVenta.add(new ListaDefault(1, "Adicionales", Telefonia[13]));
                listVenta.add(new ListaDefault(2, "Precio", Telefonia[14]));
            }

        }*/

        /*String[] Television = venta.getTelevision();
        if (!Television[0].equalsIgnoreCase("-")) {
            listVenta.add(new ListaDefault(0, "Productos Television", "Titulo"));
            listVenta.add(new ListaDefault(1, "Televisión", Television[0]));
            listVenta.add(new ListaDefault(1, "Precio", Television[1]));

            listVenta.add(new ListaDefault(2, "Extensiones", "No. de Extensiones: " + Television[2]));
            listVenta.add(new ListaDefault(2, "Promociones",
                    "Promoción: " + Television[3] + " - Duración: " + Television[4] + " - Precio: " + Television[5]));
            if (!Television[6].equalsIgnoreCase("") || !Television[6].equalsIgnoreCase("-")) {
                listVenta.add(new ListaDefault(0, "Adicionales de Television", "Titulo"));
                listVenta.add(new ListaDefault(1, "Adicionales", Television[6]));
                listVenta.add(new ListaDefault(2, "Precio", Television[7]));
            }

        }*/

       /* String[] Internet = venta.getInternet();
        if (!Internet[0].equalsIgnoreCase("-")) {
            listVenta.add(new ListaDefault(0, "Productos Internet", "Titulo"));
            listVenta.add(new ListaDefault(1, "Internet", Internet[0]));
            listVenta.add(new ListaDefault(1, "Precio", Internet[1]));
            listVenta.add(new ListaDefault(2, "Nuevo o Migración",
                    "Migración: " + Internet[3] + " - Tipo Migración: " + Internet[4]));
            listVenta.add(new ListaDefault(2, "Promociones",
                    "Promoción: " + Internet[5] + " - Duración: " + Internet[6] + " - Precio: " + Internet[7]));
            listVenta.add(new ListaDefault(2, "Wifi", Internet[2]));

        }*/


        String[] Documentacion = venta.getDocumentacion();
        listVenta.add(new ListaDefault(0, "Documentación", "Titulo"));
        listVenta.add(new ListaDefault(1, "Identificador Validación IVR", Documentacion[0]));
        listVenta.add(new ListaDefault(1, "Horario Atención", venta.getHorarioAtencion()));
        listVenta.add(new ListaDefault(1, "Observacion", venta.getObservaciones()));

        String otrasPromociones = venta.getOtrasPromociones().trim();
        if (!otrasPromociones.equalsIgnoreCase("-") && !otrasPromociones.equalsIgnoreCase("")) {
            listVenta.add(new ListaDefault(0, "Otras Promociones", "Titulo"));
            listVenta.add(new ListaDefault(1, "Promociones", otrasPromociones));
        }

        listVenta.add(new ListaDefault(0, "Datos Finales", "Titulo"));
        listVenta.add(new ListaDefault(1, "Scooring", venta.getScooring()));
        listVenta.add(new ListaDefault(1, "Empaquetamiento", venta.getEmpaquetamiento()));
        listVenta.add(new ListaDefault(1, "Total Plena", venta.getTotal()));

        return listVenta;
    }

    public static String planNumerico(String plan) {
        String Respuesta = "";

        if (plan.equalsIgnoreCase("N")) {
            Respuesta = "1";
        } else if (plan.equalsIgnoreCase("C")) {
            Respuesta = "0";
        } else if (plan.equalsIgnoreCase("E")) {
            Respuesta = "3";
        }

        return Respuesta;
    }

    public static String Existente(String departamento) {
        String result = "";
        String lista = "Comodines", clave = "TV Existente";
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?", new String[]{lista, clave}, null,
                null, null);

        if (resultado != null) {
            result = resultado.get(0).get(0);
        }
        return result;
    }

    public static boolean visible(String clave, String ciudad) {

        boolean visible = false;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_clave=? and Upper(lst_ciudad)=?",
                new String[]{clave, ciudad.toUpperCase()}, null, null, null);

        System.out.println("respuesta  Visible " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        if (data.equalsIgnoreCase("visible")) {
            visible = true;
        }

        return visible;

    }

    public static boolean visibleDepartamento(String clave, String departamento) {

        boolean visible = false;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_clave=? and lst_departamento=?",
                new String[]{clave, departamento}, null, null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        if (data.equalsIgnoreCase("visible")) {
            visible = true;
        }

        return visible;

    }

    public static boolean excluirNacional(String nombre, String clave) {

        boolean visible = false;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?", new String[]{nombre, clave}, null,
                null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        if (data.equalsIgnoreCase("true")) {
            visible = true;
        }

        return visible;

    }

    public static boolean excluirNacionalCompleto(String nombre, String clave , String valor) {

        boolean excluir = false;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=? and lst_valor=?", new String[]{nombre, clave, valor}, null,
                null, null);

        System.out.println("respuesta excluirNacionalCompleto" + respuesta);

        if (respuesta != null) {
            excluir = true;
        }


        return excluir;

    }

    public static boolean excluirMunicipal(String nombre, String clave, String ciudad) {

        System.out.println("excluirMunicipal nombre " + nombre);
        System.out.println("excluirMunicipal clave " + clave);
        System.out.println("excluirMunicipal ciudad " + ciudad);
        boolean visible = false;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=? and lst_ciudad=?",
                new String[]{nombre, clave, ciudad}, null, null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        if (data.equalsIgnoreCase("true")) {
            visible = true;
        }

        return visible;

    }

    // public String []
    // titulosTo={"plan","precio","linea","pagoLinea","migracion","tipoMigracion,"promocion,"duracion,"precioDescuento"};

    public static boolean excluir(String clave, String ciudad) {

        boolean excluir = false;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_clave=? and lst_ciudad=?", new String[]{clave, ciudad}, null,
                null, null);

        System.out.println("----Carrusel--- respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        System.out.println("----Carrusel--- data " + data);

        if (data.equalsIgnoreCase("true")) {
            excluir = true;
        }

        System.out.println("----Carrusel--- excluir " + excluir);

        return excluir;

    }

    public static String claveValor(String nombre, String clave) {
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?", new String[]{nombre, clave}, null,
                null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        return data;
    }

    public static String listaEstratos(String nombre, String estrato) {

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasestratos",
                new String[]{"lst_item"}, "lst_nombre= ? and lst_estrato=?", new String[]{nombre, estrato}, null,
                null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        return data;
    }

    public static String excluirAdicional(String municipio, String tipo, String nombre) {

        String adicional = null;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_ciudad=? and lst_nombre=?", new String[]{municipio, nombre}, null,
                null, null);

        System.out.println("respuesta " + respuesta);

        if (respuesta != null) {
            try {
                // adicional = respuesta.get(0).get(0);
                for (int i = 0; i < respuesta.size(); i++) {
                    if (i == 0) {
                        adicional = "'" + respuesta.get(i).get(0) + "'";
                    } else {
                        adicional += ",'" + respuesta.get(i).get(0) + "'";
                    }
                    // adicional+=""
                }
            } catch (Exception e) {
                // adicional = "";
                Log.w("error " + e.getMessage());
            }
        }

        return adicional;
    }

    public static String adicionalesGratis(String nombre, String clave) {
        String adicional = null;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?", new String[]{nombre, clave}, null,
                null, null);

        System.out.println("respuesta " + respuesta);

        if (respuesta != null) {
            try {
                adicional = respuesta.get(0).get(0);
            } catch (Exception e) {
                // adicional = "";
                Log.w("error " + e.getMessage());
            }
        }

        return adicional;
    }

    public static String decosxtipotv(String ciudad, String television) {

        String decosxtipotv = "";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_ciudad=? and lst_nombre=? and lst_clave=?",
                new String[]{ciudad, "decosxtipotv", television}, null, null, null);

        System.out.println("respuesta " + respuesta);

        if (respuesta != null) {
            try {
                decosxtipotv = respuesta.get(0).get(0);
            } catch (Exception e) {
                // adicional = "";
                Log.w("error " + e.getMessage());
            }
        }

        return decosxtipotv;
    }

    public static int planIntegerNumerico(String Plan) {
        int plan = -1;
        try {
            plan = Integer.parseInt(Utilidades.planNumerico(Plan));
        } catch (NumberFormatException e) {
            Log.w("Error " + e.getMessage());
            plan = -1;
        }
        return plan;
    }

    public static boolean excluirGeneral(String nombre, String item) {

        boolean excluir = false;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre=? and lst_item=?", new String[]{nombre, item}, null, null,
                null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            excluir = false;
        } else {
            excluir = true;
        }

        return excluir;

    }

    public static boolean validarNacionalValor(String nombre, String clave) {

        boolean validar = false;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?", new String[]{nombre, clave}, null,
                null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        System.out.println("data " + data);

        if (data.equalsIgnoreCase("true")) {
            validar = true;
        }

        return validar;

    }

    public static boolean validarNacionalEstrato(String nombre, String estrato) {

        boolean validar = false;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasestratos",
                new String[]{"lst_item"}, "lst_nombre=? and lst_estrato=?", new String[]{nombre, estrato}, null,
                null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        System.out.println("data " + data);

        if (data.equalsIgnoreCase("true")) {
            validar = true;
        }

        return validar;

    }

    public static String homologadoNacionalValor(String nombre, String clave) {

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?", new String[]{nombre, clave}, null,
                null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        if (data.equalsIgnoreCase("Atlantico")) {
            data = "Atlántico";
            byte ptext[] = data.getBytes();
            try {
                data = new String(ptext, "iso-8859-1");
            } catch (UnsupportedEncodingException e) {
                Log.w("Error " + e.getMessage());
            }
        } else if (data.equalsIgnoreCase("Cordoba")) {
            data = "Córdoba";
            byte ptext[] = data.getBytes();
            try {
                data = new String(ptext, "iso-8859-1");
            } catch (UnsupportedEncodingException e) {
                Log.w("Error " + e.getMessage());
            }
        } else if (data.equalsIgnoreCase("Distrito Capital De Bogota")) {
            data = "Bogotá D.C.";
            byte ptext[] = data.getBytes();
            try {
                data = new String(ptext, "iso-8859-1");
            } catch (UnsupportedEncodingException e) {
                Log.w("Error " + e.getMessage());
            }
        }

        return data;

    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port
                                // suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public static String agendaSiebel(Cliente cliente, Cotizacion cotizacion, String tiempoAtencion, String equipos) {

        JSONObject direccion = new JSONObject();
        JSONObject clientes = new JSONObject();
        JSONObject contactos = new JSONObject();
        JSONArray productos = new JSONArray();
        JSONObject agenda = new JSONObject();
        JSONObject agendas = new JSONObject();

        boolean agendarTO = false;
        boolean agendarTV = false;
        boolean agendarBA = false;

        String equipo = "0";

        boolean agendable = false;

        try {
            // direccion.put("idCliente", cliente.getCedula());
            direccion.put("idCliente", cliente.getCedula());
            direccion.put("IdDireccionServicio", cliente.getIdDireccionGis());
            direccion.put("NombreDireccionServicio", cliente.getDireccion());
            direccion.put("CoordenadaXDireccionServicio", cortarString(cliente.getLatitud(), 15));
            direccion.put("CoordenadaYDireccionServicio", cortarString(cliente.getLongitud(), 15));

            if (!Utilidades.homologadoNacionalValor("homologar", cliente.getCiudad()).equalsIgnoreCase("")) {
                direccion.put("CiudadDireccionServicio",
                        Utilidades.homologadoNacionalValor("homologar", cliente.getCiudad()));
            } else {
                direccion.put("CiudadDireccionServicio", cliente.getCiudad());
            }

            if (!Utilidades.homologadoNacionalValor("homologar", cliente.getDepartamento()).equalsIgnoreCase("")) {
                direccion.put("DepartamentoDireccionServicio",
                        Utilidades.homologadoNacionalValor("homologar", cliente.getDepartamento()));
            } else {
                direccion.put("DepartamentoDireccionServicio", cliente.getDepartamento());
            }

            JSONObject equipos1 = new JSONObject(equipos);

            clientes.put("idCliente", cliente.getCedula());
            clientes.put("nombreCliente", Utilidades.Quitar_Tildes2(cliente.getNombre()));
            clientes.put("apellidosCliente", Utilidades.Quitar_Tildes2(cliente.getApellido()));
            clientes.put("nombresContacto", Utilidades.Quitar_Tildes2(cliente.getNombre()) + " "
                    + Utilidades.Quitar_Tildes2(cliente.getApellido()));

            if (!Utilidades.limpiarTelefono(cliente.getTelefono()).equalsIgnoreCase("")) {
                clientes.put("telefonoContacto", cliente.getTelefono());
            } else {
                clientes.put("telefonoContacto", cliente.getTelefono2());
            }

            clientes.put("celular", cliente.getCelular());

            contactos.put("idCliente", cliente.getCedula());
            contactos.put("nombreContacto", Utilidades.Quitar_Tildes2(cliente.getContacto1().getNombres()));
            contactos.put("apellidosContacto", Utilidades.Quitar_Tildes2(cliente.getContacto1().getApellidos()));
            contactos.put("nombresContacto", Utilidades.Quitar_Tildes2(cliente.getContacto1().getNombres()) + " "
                    + Utilidades.Quitar_Tildes2(cliente.getContacto1().getApellidos()));
            contactos.put("telefonoContacto", cliente.getContacto1().getTelefono());
            contactos.put("celular", "");

            if (!cotizacion.getTelefonia().equalsIgnoreCase("-")
                    && !cotizacion.getTipoCotizacionTo().equalsIgnoreCase("3")) {

                if (cotizacion.getTipoCotizacionTo().equalsIgnoreCase("1")) {
                    agendable = true;
                    agendarTO = true;
                }

                equipo = "0";

                if (equipos1.has("Telefonia")) {
                    equipo = equipos1.getString("Telefonia");
                }

                if (agendarTO) {
                    productos.put(productosAgenda("TO", cotizacion.getTipoCotizacionTo(), (String) equipo, "HFC",
                            tiempoAtencion));
                }
            }

            if (!cotizacion.getTelevision().equalsIgnoreCase("-")
                    && !cotizacion.getTipoCotizacionTv().equalsIgnoreCase("3")) {

                equipo = "0";

                if (equipos1.has("Television")) {
                    equipo = equipos1.getString("Television");
                }

                if (cotizacion.getTipoCotizacionTv().equalsIgnoreCase("1")) {
                    agendable = true;
                    agendarTV = true;
                } else if (cotizacion.getTipoCotizacionTv().equalsIgnoreCase("0")) {
                    int eq = 0;
                    try {
                        eq = Integer.parseInt(equipo);
                    } catch (NumberFormatException e) {
                        android.util.Log.w("Error ", e.getMessage());
                        eq = 0;
                    }

                    if (eq > 0) {
                        agendable = true;
                        agendarTV = true;
                    }
                }

                if (agendarTV) {
                    productos.put(
                            productosAgenda("TV", cotizacion.getTipoCotizacionTv(), equipo, "HFC", tiempoAtencion));
                }
            } else if (!cotizacion.getTelevision().equalsIgnoreCase("-")
                    && cotizacion.getTipoCotizacionTv().equalsIgnoreCase("3")) {
                int equipotv = 0;

                if (cotizacion.getAdicionales().length > 0) {
                    equipotv = equipoExistenteTV(cotizacion.getAdicionales());
                }

                if (equipotv > 0) {
                    productos.put(productosAgenda("TV", "0", "" + equipotv, "HFC", tiempoAtencion));
                    agendable = true;
                    agendarTV = true;
                }
            }

            if (!cotizacion.getInternet().equalsIgnoreCase("-")
                    && !cotizacion.getTipoCotizacionBa().equalsIgnoreCase("3")) {

                equipo = "0";

                if (cotizacion.getTipoCotizacionBa().equalsIgnoreCase("1")) {
                    agendable = true;
                    agendarBA = true;
                    equipo = "1";
                } else if (cotizacion.getTipoCotizacionBa().equalsIgnoreCase("0")) {

                    if (equipos1.has("Internet")) {
                        equipo = equipos1.getString("Internet");

                        if (equipo.equalsIgnoreCase("1")) {
                            agendable = true;
                            agendarBA = true;
                        }
                    }
                }

                if (agendarBA = true) {
                    productos.put(
                            productosAgenda("BA", cotizacion.getTipoCotizacionBa(), equipo, "HFC", tiempoAtencion));
                }
            }

            agenda.put("Cliente", clientes);
            agenda.put("Contactos", contactos);
            agenda.put("Productos", productos);
            agenda.put("Direccion", direccion);

            agendas.put("agenda", agenda);
            System.out.println("agendable " + agendable);
            agendas.put("agendable", agendable);
            agendas.put("Salida", "");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return agendas.toString();
    }

    public static int equipoExistenteTV(String adicionales[][]) {
        int equipos = 0;

        if (adicionales != null) {
            for (int i = 0; i < adicionales.length; i++) {

                String decos_pago = UtilidadesTarificador.decos_pago(adicionales[i][0]);
                System.out.println("decos_pago " + decos_pago);
                if (decos_pago.equalsIgnoreCase("HD")) {
                    equipos++;
                } else if (decos_pago.equalsIgnoreCase("SD")) {
                    equipos++;
                }
            }

        }

        return equipos;
    }

    public static JSONObject productosAgenda(String tipo, String transaccion, String equipos, String tecnologia,
                                             String tiempoAtencion) {
        JSONObject producto = new JSONObject();
        try {
            producto.put("tipo", tipo);
            producto.put("transaccion", transaccion);
            producto.put("equipos", equipos);
            producto.put("tecnologia", tecnologia);
            producto.put("TMAP", tiempoAtencion);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return producto;
    }

    public static JSONObject productosAgendaFenix(String tipo, String transaccion) {
        JSONObject producto = new JSONObject();
        try {
            producto.put("producto", tipo);
            switch (Integer.parseInt(transaccion)) {
                case 0:
                    producto.put("tipoSolicitud", "Cambio Plan");
                    break;
                case 1:
                    producto.put("tipoSolicitud", "Nuevo Producto");
                    break;
                case 3:
                    producto.put("tipoSolicitud", "Existente");
                    break;

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return producto;
    }

    public static String portaFolioSiebel() {

        // String portafolio =
        // "{'ListaDatosProductosVent':[{'Estado_Servicio':'ACTI','visible':1,'Producto':'TO','Identificador':'1-J9J7YM','PaqueteId':'1-JA51WP','Uso':'Residencial','clienteId':'63558368','NombrePlan':'Plan
        // Hogares UNE ilimitado','NombrePlanLimpio':'Plan Hogares UNE
        // ilimitado','PlanId':'1043','Adicionales':['Identificador de llamadas
        // por pantalla','Programador','Marcaci\u00f3n Abreviada','Directorio

        // Telef\u00f3nico','Llamada en espera','Discado Local','Transferencia
        // // // // //
        // //
        // de
        // //
        // llamadas','Conversaci\u00f3n
        // //
        // entre
        // // // tres'],'AdicionalesLimpio':['Identificador
        // de
        // llamadas
        // por
        // pantalla','Programador','Marcacion
        // Abreviada','Directorio
        // Telefonico','Llamada
        // en
        // espera','Discado
        // Local','Transferencia
        // de
        // llamadas','Conversacion
        // entre
        // tres']},{'Estado_Servicio':'ACTI','visible':1,'Producto':'INTER','Identificador':'1-J9J7W9','PaqueteId':'1-JA51WP','Uso':'Residencial','clienteId':'63558368','NombrePlan':'Plan
        // 5MB
        // Agosto
        // 2013','NombrePlanLimpio':'Plan
        // 5MB
        // Agosto
        // 2013','PlanId':'1064','Velocidad':5000},{'Estado_Servicio':'ACTI','visible':1,'PaqueteId':'1-JA51WP','Producto':'TELEV','Identificador':'1-J9J7XC','Uso':'Residencial','clienteId':'63558368','NombrePlan':'Plan
        // B\u00e1sico
        // Full
        // -
        // BIDI','NombrePlanLimpio':'Plan
        // Basico
        // Full
        // -
        // BIDI','PlanId':'1046','CantidadSD':0,'CantidadHD':0}],'ListaDatosPaquetesVent':[{'1-JA51WP':{'Estado_Servicio':'ACTI','visible':1,'IdPaquete':'1-JA51WP','Uso':'Residencial','clienteId':'63558368'}}],'ListaDatosClienteVent':[{'Direccion':'CL
        // 103 A # 40 A -
        // 29','DepartamentoId':'Santander','Municipio':'Floridablanca','Codigo_Hogar':'*476928760','Estrato':'2','IdDepartamentoGis':'68','IdMunicipioGis':'68276000','IdDireccionGis':'943366819','Latitud':'7.09567','Longitud':'-73.097936','Cliente_id':'63558368','Apellidos':'GONZALEZ
        // AMADO','Nombres':'SLENDY YADIRA','NombreCompletoCliente':'SLENDY
        // YADIRA GONZALEZ
        // AMADO','TipoDocumento':'CC'}],'ListaLocalizacionClienteVent':{'CL 103
        // A # 40 A - 29':{'Direccion':'CL 103 A # 40 A -
        // 29','DepartamentoId':'Santander','Municipio':'Floridablanca','Codigo_Hogar':'*476928760','Estrato':'2','IdDepartamentoGis':'68','IdMunicipioGis':'68276000','IdDireccionGis':'943366819','Latitud':'7.09567','Longitud':'-73.097936'}},'ListaResulInfraVent':[{'CodigoRespuesta':'00','DescripcionMensaje':'Cobertura
        // Exitosa, Paginacion
        // Exitosa','georreferenciarCR':null,'paginaNlectura':{'CodigoRespuesta':'00','DescripcionMensaje':'EXITOSO','PaginaInstalacion':'900680002763021319','DireccionEstandarNLectura':'CL
        // 103 A # 40 A -
        // 29','EstadoPagina':'2'},'factibilidadServicios':{'CodigoRespuesta':'00','DescripcionMensaje':'EXITOSO','CodigoSolicitud':'111111','CodigoDireccion':'943366819','ZonaAltoRiesgo':'NO','CoberturaHFC':{'Cobertura':'SI','Prioridad':1,'NombreNodo':'CLUSTER
        // SAN
        // JUAN','TipoNodo':'BIDI','TVDigital':'SI','CapacidadToip':'SI'}}}],'Mensaje':'OK','CodigoMensaje':'00','ListaDatosInfraestructuraVent':[{'COBERTURA_HFC':'SI','DIRECCIONALIDAD':'B','HFC_DIGITAL':'SI','COBERTURA_INALA':'NO','COBERTURA_GPON':'NO','NODO':null,'DISTANCIA':null,'ANCHO_BANDA_SUBIDA':null,'ANCHO_BANDA_BAJADA':null}]}";

        String portafolio = "{'ListaResulFactVent':{'CodigoMensaje':'00','DescripcionMensaje':'Exitoso','Factura':[{'identificador':'76928760','producto':'TO','productoNombre':'TELEFONIA','plan':'ILIMITADO LOCAL','cargoBasico':15588,'velocidad':'','descuentosComerciales':[],'totalCargoBasico':15588,'iva':{'IVA CARGO BASICO':507.68},'adicionales':[],'otrosAdicionales':[],'otrosFinancieros':{'SUBSIDIO':-1429},'acceso':null,'totalProducto':14666.68,'mostrarIndependientes':{'Larga Distancia':0,'MINUTOS ADICIONALES':0},'aplicaPromocion':[],'detalles':{'CONSUMO DEL':'04 Enero Al 03 Febrero','DIAS DE CONSUMO':'31.00','Minutos Consumidos':'0.00','Minutos Adicionales':'0.00','AGOSTO':'0.00','SEPTIEMBRE':'0.00','OCTUBRE':'1189.00','NOVIEMBRE':'319.00','DICIEMBRE':'0.00','ENERO':'0.00'},'estadoServicio':'ACTI','clienteId':'','planFacturacion':'','aplica':true,'demo':3,'planFactura':'ILIMITADO LOCAL'},{'identificador':'1-J9J7XC','producto':'TV','productoNombre':'TELEVISION','plan':'TELEVISION 1SD y 1HD','cargoBasico':21552,'velocidad':'','descuentosComerciales':[],'totalCargoBasico':21552,'iva':{'IVA CARGO BASICO':3448.32},'adicionales':[],'otrosAdicionales':[],'otrosFinancieros':{'SUBSIDIO':0},'acceso':null,'totalProducto':25000.32,'mostrarIndependientes':{'Larga Distancia':0,'MINUTOS ADICIONALES':0},'aplicaPromocion':[],'detalles':{'CONSUMO DEL':'03 Enero Al 02 Febrero','DIAS DE CONSUMO':'31.00'},'estadoServicio':'ACTI','clienteId':'','planFacturacion':'','aplica':true,'demo':3,'planFactura':'TELEVISION 1SD y 1HD'},{'identificador':'1-J9J7W9','producto':'BA','productoNombre':'BANDA ANCHA','plan':'NUEVA BANDA ANCHA 1M RESIDENCIAL','cargoBasico':24300,'velocidad':'','descuentosComerciales':[],'totalCargoBasico':24300,'iva':{'IVA CARGO BASICO':0},'adicionales':[],'otrosAdicionales':[],'otrosFinancieros':{'SUBSIDIO':0},'acceso':null,'totalProducto':24300,'mostrarIndependientes':{'Larga Distancia':0,'MINUTOS ADICIONALES':0},'aplicaPromocion':[],'detalles':{'CONSUMO DEL':'03 Enero Al 02 Febrero','DIAS DE CONSUMO':'31.00'},'estadoServicio':'ACTI','clienteId':'','planFacturacion':'','aplica':true,'demo':3,'planFactura':'NUEVA BANDA ANCHA 1M RESIDENCIAL'}]},'ListaDatosProductosVent':[{'Estado_Servicio':'ACTI','visible':1,'Producto':'TO','Identificador':'76928760','IdServicio':'1-J9J7YM','PaqueteId':'1-JA51WP','Uso':'Residencial','clienteId':'63558368','cliente':'63558368 SLENDY YADIRA GONZALEZ AMADO','NombrePlan':'Plan Hogares UNE ilimitado','NombrePlanLimpio':'Plan Hogares UNE ilimitado','PlanId':'1043','Adicionales':['Identificador de llamadas por pantalla','Programador','Marcaci\u00f3n Abreviada','Directorio Telef\u00f3nico','Llamada en espera','Discado Local','Transferencia de llamadas','Conversaci\u00f3n entre tres'],'AdicionalesLimpio':['Identificador de llamadas por pantalla','Programador','Marcacion Abreviada','Directorio Telefonico','Llamada en espera','Discado Local','Transferencia de llamadas','Conversacion entre tres']},{'Estado_Servicio':'ACTI','visible':1,'Producto':'INTER','Identificador':'1-J9J7W9','IdServicio':'1-J9J7W9','PaqueteId':'1-JA51WP','Uso':'Residencial','clienteId':'63558368','cliente':'63558368 SLENDY YADIRA GONZALEZ AMADO','NombrePlan':'Plan 5MB Agosto 2013','NombrePlanLimpio':'Plan 5MB Agosto 2013','PlanId':'1064','Velocidad':5000},{'Estado_Servicio':'ACTI','visible':1,'PaqueteId':'1-JA51WP','Producto':'TELEV','Identificador':'1-J9J7XC','IdServicio':'1-J9J7XC','Uso':'Residencial','clienteId':'63558368','cliente':'63558368 SLENDY YADIRA GONZALEZ AMADO','NombrePlan':'Plan B\u00e1sico Full - BIDI','NombrePlanLimpio':'Plan Basico Full - BIDI','PlanId':'1046','CantidadSD':0,'CantidadHD':0}],'ListaDatosPaquetesVent':[{'Estado_Servicio':'ACTI','visible':1,'IdPaquete':'1-JA51WP','Identificador':'1-J9J7V3','IdServicio':'1-J9J7V3','Uso':'Residencial','clienteId':'63558368','cliente':'63558368 SLENDY YADIRA GONZALEZ AMADO'}],'ListaDatosClienteVent':[{'Direccion':'CL 103 A # 40 A - 29','Departamento':'Santander','DepartamentoId':'','Municipio':'Floridablanca','MunicipioId':'','Codigo_Hogar':'*476928760','Estrato':'2','IdDepartamentoGis':'68','IdMunicipioGis':'68276000','IdDireccionGis':'943366819','Latitud':'7.09567','Longitud':'-73.097936','Cliente_id':'63558368','Apellidos':'GONZALEZ AMADO','Nombres':'SLENDY YADIRA','NombreCompletoCliente':'SLENDY YADIRA GONZALEZ AMADO','TipoDocumento':'CC'}],'ListaLocalizacionClienteVent':{'Direccion':'CL 103 A # 40 A - 29','Departamento':'Santander','DepartamentoId':'','Municipio':'Floridablanca','MunicipioId':'','Codigo_Hogar':'*476928760','Estrato':'2','IdDepartamentoGis':'68','IdMunicipioGis':'68276000','IdDireccionGis':'943366819','Latitud':'7.09567','Longitud':'-73.097936'},'ListaResulInfraVent':[{'CodigoRespuesta':'01','DescripcionMensaje':'Cobertura Exitosa, Sin Exitosa','georreferenciarCR':null,'paginaNlectura':{'CodigoRespuesta':'02','DescripcionMensaje':'Sin Datos'},'factibilidadServicios':{'CodigoRespuesta':'00','DescripcionMensaje':'EXITOSO','CodigoSolicitud':'111111','CodigoDireccion':'943366819','ZonaAltoRiesgo':'NO','CoberturaHFC':{'Cobertura':'SI','Prioridad':1,'NombreNodo':'CLUSTER SAN JUAN','TipoNodo':'BIDI','TVDigital':'SI','CapacidadToip':'SI'}}}],'Mensaje':'OK','CodigoMensaje':'00','ListaDatosInfraestructuraVent':[{'COBERTURA_HFC':'SI','DIRECCIONALIDAD':'B','HFC_DIGITAL':'SI','COBERTURA_INALA':'NO','COBERTURA_GPON':'NO','NODO':null,'DISTANCIA':null,'ANCHO_BANDA_SUBIDA':null,'ANCHO_BANDA_BAJADA':null,'Mensaje':'Vender Cobertura HFC Digital Bidireccional'}]}";

        portafolio = portafolio.replace((char) 39, (char) 34);

        return portafolio;
    }

    public static String Existente() {

        // String portafolio =
        // "{'ListaDatosProductosVent':[{'Estado_Servicio':'ACTI','visible':1,'Producto':'TO','Identificador':'1-J9J7YM','PaqueteId':'1-JA51WP','Uso':'Residencial','clienteId':'63558368','NombrePlan':'Plan
        // Hogares UNE ilimitado','NombrePlanLimpio':'Plan Hogares UNE
        // ilimitado','PlanId':'1043','Adicionales':['Identificador de llamadas
        // por pantalla','Programador','Marcaci\u00f3n Abreviada','Directorio
        // Telef\u00f3nico','Llamada en espera','Discado Local','Transferencia
        // // // // //
        // //
        // de
        // //
        // llamadas','Conversaci\u00f3n
        // //
        // entre
        // // // tres'],'AdicionalesLimpio':['Identificador
        // de
        // llamadas
        // por
        // pantalla','Programador','Marcacion
        // Abreviada','Directorio
        // Telefonico','Llamada
        // en
        // espera','Discado
        // Local','Transferencia
        // de
        // llamadas','Conversacion
        // entre
        // tres']},{'Estado_Servicio':'ACTI','visible':1,'Producto':'INTER','Identificador':'1-J9J7W9','PaqueteId':'1-JA51WP','Uso':'Residencial','clienteId':'63558368','NombrePlan':'Plan
        // 5MB
        // Agosto
        // 2013','NombrePlanLimpio':'Plan
        // 5MB
        // Agosto
        // 2013','PlanId':'1064','Velocidad':5000},{'Estado_Servicio':'ACTI','visible':1,'PaqueteId':'1-JA51WP','Producto':'TELEV','Identificador':'1-J9J7XC','Uso':'Residencial','clienteId':'63558368','NombrePlan':'Plan
        // B\u00e1sico
        // Full
        // -
        // BIDI','NombrePlanLimpio':'Plan
        // Basico
        // Full
        // -
        // BIDI','PlanId':'1046','CantidadSD':0,'CantidadHD':0}],'ListaDatosPaquetesVent':[{'1-JA51WP':{'Estado_Servicio':'ACTI','visible':1,'IdPaquete':'1-JA51WP','Uso':'Residencial','clienteId':'63558368'}}],'ListaDatosClienteVent':[{'Direccion':'CL
        // 103 A # 40 A -
        // 29','DepartamentoId':'Santander','Municipio':'Floridablanca','Codigo_Hogar':'*476928760','Estrato':'2','IdDepartamentoGis':'68','IdMunicipioGis':'68276000','IdDireccionGis':'943366819','Latitud':'7.09567','Longitud':'-73.097936','Cliente_id':'63558368','Apellidos':'GONZALEZ
        // AMADO','Nombres':'SLENDY YADIRA','NombreCompletoCliente':'SLENDY
        // YADIRA GONZALEZ
        // AMADO','TipoDocumento':'CC'}],'ListaLocalizacionClienteVent':{'CL 103
        // A # 40 A - 29':{'Direccion':'CL 103 A # 40 A -
        // 29','DepartamentoId':'Santander','Municipio':'Floridablanca','Codigo_Hogar':'*476928760','Estrato':'2','IdDepartamentoGis':'68','IdMunicipioGis':'68276000','IdDireccionGis':'943366819','Latitud':'7.09567','Longitud':'-73.097936'}},'ListaResulInfraVent':[{'CodigoRespuesta':'00','DescripcionMensaje':'Cobertura
        // Exitosa, Paginacion
        // Exitosa','georreferenciarCR':null,'paginaNlectura':{'CodigoRespuesta':'00','DescripcionMensaje':'EXITOSO','PaginaInstalacion':'900680002763021319','DireccionEstandarNLectura':'CL
        // 103 A # 40 A -
        // 29','EstadoPagina':'2'},'factibilidadServicios':{'CodigoRespuesta':'00','DescripcionMensaje':'EXITOSO','CodigoSolicitud':'111111','CodigoDireccion':'943366819','ZonaAltoRiesgo':'NO','CoberturaHFC':{'Cobertura':'SI','Prioridad':1,'NombreNodo':'CLUSTER
        // SAN
        // JUAN','TipoNodo':'BIDI','TVDigital':'SI','CapacidadToip':'SI'}}}],'Mensaje':'OK','CodigoMensaje':'00','ListaDatosInfraestructuraVent':[{'COBERTURA_HFC':'SI','DIRECCIONALIDAD':'B','HFC_DIGITAL':'SI','COBERTURA_INALA':'NO','COBERTURA_GPON':'NO','NODO':null,'DISTANCIA':null,'ANCHO_BANDA_SUBIDA':null,'ANCHO_BANDA_BAJADA':null}]}";

        String validador = "{'codigoRespuesta':0,'mensajeRespuesta':'OP exitosa','cuerpoRespuesta':{'parametros':{'codigoInformacion':'153','motivoConsulta':'24','tipoIdentificacion':'1','numeroIdentificacion':'71224160'},'tercero':{'identificacion':'000000071224160','tipoIdentificacion':'01','fechaExpedicion':'11-11-1998','lugarExpedicion':null,'departamento':null,'ciudad':null,'telefono':null,'numeroIntentosFallidos':null,'operacion':1,'historicoConfronta':null,'historicoValidacion':null,'nombre':'OSPINA          ALZATE        GIOVANNY','departamento_codigo':'5','municipio_codigo':'88'},'codigoCalificacion':1,'codigoEstado':1,'descripcionEstado':'VIGENTE'}}";

        validador = validador.replace((char) 39, (char) 34);

        return validador;
    }

    public static String TerceroNoEXiste() {

        String validador = "{'codigoRespuesta':0,'mensajeRespuesta':'OP exitosa','cuerpoRespuesta':{'parametros':{'codigoInformacion':'153','motivoConsulta':'24','tipoIdentificacion':'1','numeroIdentificacion':'1111114555'},'tercero':{'identificacion':'000001111114555','tipoIdentificacion':'01','fechaExpedicion':null,'lugarExpedicion':null,'departamento':null,'ciudad':null,'telefono':null,'numeroIntentosFallidos':1,'operacion':null,'historicoConfronta':null,'historicoValidacion':{'estado':'0','descripcion_estado':'TERCERO NO EXISTE EN CIFIN','canal':'VM','fecha':'2015-03-03 10:37:00'},'nombre':null,'departamento_codigo':'','municipio_codigo':''},'codigoCalificacion':0,'codigoEstado':0,'descripcionEstado':'TERCERO NO EXISTE EN CIFIN'}}";

        validador = validador.replace((char) 39, (char) 34);

        return validador;
    }

    public static String PositivaScooring() {

        String validador = "{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'APROBADO','razonEstadoFinal':'SCORING','maximaCotizacion':'270000'}";

        validador = validador.replace((char) 39, (char) 34);

        return validador;
    }

    public static boolean habilitarValidador(String municipio, String lista, String clave) {
        boolean respuesta = false;

        ArrayList<ArrayList<String>> result = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=? and lst_ciudad=?",
                new String[]{lista, clave, municipio}, null, null, null);

        if (result != null) {
            respuesta = Boolean.valueOf(result.get(0).get(0));
        }

        return respuesta;
    }

    public static void MensajesToast(String mensaje, Context context) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
    }

    public static void pintarBotones(String Resultados) {

        try {
            JSONObject consolidado = new JSONObject(Resultados);

            if (consolidado.has("ListaDatosProductosVent")) {
                MainActivity.btnPortafolio.setOK();
            } else {
                MainActivity.btnPortafolio.setWRONG();
            }

            if (consolidado.has("ListaResulInfraVent")) {
                MainActivity.btnCobertura.setOK();
            } else {
                MainActivity.btnCobertura.setWRONG();
            }

            if (consolidado.has("ListaDatosClienteVent")) {
                MainActivity.btnCliente.setOK();
            } else {
                MainActivity.btnCliente.setWRONG();
            }
            // if()
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	/*
     * Utilidades para amigo cuentas digital
	 */

    public static boolean mostrarTVDigital(String clave) {

        boolean mostrar = false;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?",
                new String[]{"planesTVDigitalVisible", clave}, null, null, null);

        System.out.println("respuesta " + respuesta.get(0).get(0));

        if (respuesta != null) {
            if (respuesta.get(0).get(0).equalsIgnoreCase("true")) {
                mostrar = true;
            }
        }

        return mostrar;

    }

    public static ArrayList<String> mostrarTVDigital(String nombre, String ciudad) {

        System.out.println("nombre " + nombre);
        System.out.println("ciudad " + ciudad);

        ArrayList<String> planes = new ArrayList<String>();

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave,lst_valor"}, "lst_nombre=? and lst_ciudad=?",
                new String[]{nombre, ciudad}, null, null, null);

        System.out.println("respuesta mostrarTVDigital" + respuesta);

        if (respuesta != null) {
            for (int i = 0; i < respuesta.size(); i++) {
                if (respuesta.get(i).get(1).equalsIgnoreCase("true")) {
                    planes.add(respuesta.get(i).get(0));
                }

            }
        }

        System.out.println("planes " + planes);

        return planes;

    }

    public static String obtenerPlanTVDigital(String oferta, String tecnologia) {

        String plan = "";

        System.out.println("lst_clave " + oferta);
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?", new String[]{"ofertaDigital", oferta},
                null, null, null);

        System.out.println("respuesta obtenerPlanTVDigital" + respuesta);

        if (respuesta != null) {

            for (int i = 0; i < respuesta.size(); i++) {
                if (tecnologia.equals("HFC")) {
                    if (!respuesta.get(i).get(0).contains("IPTV")) {
                        plan = respuesta.get(i).get(0);
                    }
                } else {
                    if (respuesta.get(i).get(0).contains("IPTV")) {
                        plan = respuesta.get(i).get(0);
                    }
                }

            }

        }

        return plan;
    }

    public static String obtenerTipoPlanTVDigital(String oferta) {

        String plan = "";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?", new String[]{"ofertaDigital", oferta},
                null, null, null);

        System.out.println("respuesta obtenerTipoPlanTVDigital" + respuesta);

        if (respuesta != null) {
            plan = "HFC";

        }

        return plan;
    }

    public static String obtenerPlanTODigital() {

        String plan = "";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre =?", new String[]{"ofertaDigitalTO"}, null, null, null);

        if (respuesta != null) {
            plan = respuesta.get(0).get(0);
        }

        return plan;
    }

    public static String obtenerPlanTODigitalBronze() {

        String plan = "";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave"}, "lst_nombre=?", new String[]{"toBronze"}, null, null, null);

        if (respuesta != null) {
            plan = respuesta.get(0).get(0);
        }

        return plan;
    }

    public static ArrayList<String> obtenerPlanTVDigitalBronze() {

        ArrayList<String> plan = null;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave"}, "lst_nombre=?", new String[]{"tipoTvBronze"}, null, null, null);

        if (respuesta != null) {
            plan = respuesta.get(0);
        }

        return plan;
    }

    public static String traducirPlanOfertaDigital(String plan) {

        System.out.println("PLan => " + plan);

        String oferta = "";

        ArrayList<ArrayList<String>> respuesta = null;

        if (plan != null) {
            respuesta = MainActivity.basedatos.consultar(true, "listasvalores", new String[]{"lst_clave"},
                    "lst_nombre=? and lst_valor=?", new String[]{"ofertaDigital", plan}, null, null, null);

            System.out.println("respuesta -> " + respuesta);
        }

        if (respuesta != null) {
            oferta = respuesta.get(0).get(0);
        }

        System.out.println("Oferta Traducida -> " + oferta);

        return oferta;
    }

    public static boolean habilitar(String municipio, String lista) {
        boolean respuesta = false;

        ArrayList<ArrayList<String>> result = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?",
                new String[]{"habilitarOfertaDigital", lista}, null, null, null);

        if (result != null) {
            respuesta = Boolean.valueOf(result.get(0).get(0));
        }

        return respuesta;
    }

    public static String obtenerCobertura(String cobertura, Context context) {
        String respuesta = "";

        try {
            if (cobertura != null) {
                JSONObject jo = new JSONObject(cobertura);
                JSONObject cober = jo.getJSONObject("Cobertura");
                String direccionaldiad = cober.get("DIRECCIONALIDAD").toString();

                String gpon = "";
                String hfc = "";
                String redco = "";

                if (cober.has("COBERTURA_GPON")) {
                    gpon = cober.get("COBERTURA_GPON").toString();
                }

                if (cober.has("COBERTURA_HFC")) {
                    hfc = cober.get("COBERTURA_HFC").toString();
                }

                if (cober.has("COBERTURA_REDCO")) {
                    redco = cober.get("COBERTURA_REDCO").toString();
                }

                if (gpon.equalsIgnoreCase("SI")) {
                    respuesta = "GPON";
                } else if (hfc.equalsIgnoreCase("SI")) {
                    respuesta = "HFC";
                } else if (redco.equalsIgnoreCase("SI")) {
                    respuesta = "REDCO";
                } else {
                    respuesta = "N/D";
                }
            }

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
            respuesta = "N/D";
        }

        return respuesta;
    }

    public static boolean tratarCobertura(String cobertura, Context context) {
        boolean respuesta = false;

        try {
            if (cobertura != null) {
                JSONObject jo = new JSONObject(cobertura);
                JSONObject cober = jo.getJSONObject("Cobertura");
                String hfc = cober.get("COBERTURA_HFC").toString();
                String direccionaldiad = cober.get("DIRECCIONALIDAD").toString();

                String gpon = "";

                if (cober.has("COBERTURA_GPON")) {
                    gpon = cober.get("COBERTURA_GPON").toString();
                }

                if (gpon.equalsIgnoreCase("SI")) {
                    respuesta = true;
                    /*
                     * if(cober.has("DISPONIBILIDAD_GPON")){
					 * if(cober.getString("DISPONIBILIDAD_GPON").
					 * equalsIgnoreCase("SI")){ respuesta = true; }else{
					 * MensajesToast("Cobertura GPON Sin Disponibilidad"
					 * ,context); } }else{ respuesta = true; }
					 */
                }

                if (hfc.equalsIgnoreCase("SI")) {
                    respuesta = true;
					/*
					 * if(cober.has("DISPONIBILIDAD_HFC")){
					 * if(cober.getString("DISPONIBILIDAD_HFC").equalsIgnoreCase
					 * ("SI")){ respuesta = true; }else{ MensajesToast(
					 * "Cobertura HFC Sin Disponibilidad",context); } }else{
					 * respuesta = true; }
					 */
                }
            }

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());

        }

        return respuesta;
    }

    public static BloqueoCobertura tratarCoberturaBloqueo(String cobertura, Context context) {

        BloqueoCobertura bloqueo = new BloqueoCobertura();

        boolean respuesta = false;
        boolean controlDisponibilidadHFC = false;
        boolean controlDisponibilidadREDCO = false;
        bloqueo.setHFCDigital(false);

        try {
            if (cobertura != null) {
                JSONObject jo = new JSONObject(cobertura);

                JSONObject cober = jo.getJSONObject("Cobertura");

                System.out.println("cober " + cober);

                String hfc = cober.get("COBERTURA_HFC").toString();
                String direccionaldiad = cober.get("DIRECCIONALIDAD").toString();

                String gpon = "";
                String redco = "";

                if (cober.has("COBERTURA_GPON")) {
                    gpon = cober.get("COBERTURA_GPON").toString();
                }

                if (cober.has("COBERTURA_REDCO")) {
                    redco = cober.get("COBERTURA_REDCO").toString();
                }

                System.out.println("redco " + redco);

                if (gpon.equalsIgnoreCase("SI")) {
                    if (cober.has("DISPONIBILIDAD_GPON")) {
                        // if(cober.getString("DISPONIBILIDAD_GPON").equalsIgnoreCase("SI")
                        // && !controlDisponibilidadHFC){
                        // //respuesta = true;
                        // bloqueo.setDisponibilidadHFC(true);;
                        // controlDisponibilidadHFC = true;
                        // }else{
                        // MensajesToast("Cobertura GPON Sin
                        // Disponibilidad",context);
                        // }
                    }

                    bloqueo.setCoberturaHFC(true);
                    bloqueo.setTipoCobertura("GPON");
                    // control = true;
                }

                if (hfc.equalsIgnoreCase("SI")) {
                    // respuesta = true;
                    // if(cober.has("DISPONIBILIDAD_HFC")){
                    // if(cober.getString("DISPONIBILIDAD_HFC").equalsIgnoreCase("SI")
                    // && !controlDisponibilidadHFC){
                    // bloqueo.setDisponibilidadHFC(true);
                    // controlDisponibilidadHFC = true;
                    // }else{
                    // MensajesToast("Cobertura HFC Sin
                    // Disponibilidad",context);
                    // }
                    // }

                    bloqueo.setCoberturaHFC(true);
                    bloqueo.setTipoCobertura("HFC");
                }

                if (redco.equalsIgnoreCase("SI")) {
                    // respuesta = true;
                    // if(cober.has("DISPONIBILIDAD_REDCO")){
                    // if(cober.getString("DISPONIBILIDAD_REDCO").equalsIgnoreCase("SI")){
                    // bloqueo.setDisponibilidadREDCO(true);
                    // }else{
                    // MensajesToast("Cobertura Redco Sin
                    // Disponibilidad",context);
                    // }
                    // }
                    bloqueo.setCoberturaREDCO(true);
                    bloqueo.setTipoCobertura("IPTV");

                    System.out.println("cobertura redco SI");
                }

                System.out.println("Nodos Digitales Antes");
                if (cober.has("HFC_DIGITAL")) {
                    if (cober.getString("HFC_DIGITAL").equalsIgnoreCase("S")) {
                        bloqueo.setHFCDigital(true);
                        System.out.println("Nodos Digitales Este es un nodo digital");
                    }
                }
            }

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());

        }

        return bloqueo;
    }

    public static boolean validarCliente(String municipio, String tipoAccion, String estadoValidador, Context context) {
        boolean validacion = true;
        if (habilitarValidador(municipio, "habilitarValidador", "habilitarValidador")) {
            if (tipoAccion.equalsIgnoreCase("Verificar Cifin")) {
                if (estadoValidador != null) {
                    if (estadoValidador.equalsIgnoreCase("TERCERO NO EXISTE EN CIFIN")) {
                        validacion = false;
                        Toast.makeText(context, "Se Debe Prospectar Porque El Cliente Aparece Como Tercero No Existe",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    validacion = false;
                    Toast.makeText(context, "Se Debe Verificar El Cliente En El Validador", Toast.LENGTH_SHORT).show();
                }
            }
        }

        return validacion;
    }

    public static String pruebaValidadorCifin() {

        String resultado = "";
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre=?", new String[]{"pruebaValidador"}, null, null, null);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        if (data.equalsIgnoreCase("tercero")) {
            resultado = TerceroNoEXiste();
        } else if (data.equalsIgnoreCase("vigente")) {
            resultado = Existente();
        }

        return resultado;
    }

    public static String camposUnicos(String nombre) {
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre=?", new String[]{nombre}, null, null, null);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        Log.e("data");

        return data;
    }

    public static String camposUnicosCiudad(String nombre, String ciudad) {
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre=? and lst_ciudad =?", new String[]{nombre, ciudad}, null,
                null, null);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        Log.e("data");

        return data;
    }

    public static String homologarPlanFacturacion(String departamento, String producto, String estrato) {

        boolean validar = false;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios",
                new String[]{"homoPrimeraLinea"}, "Departamento=? COLLATE NOCASE and Producto=? and estrato like ?",
                new String[]{departamento, producto, "%" + estrato + "%"}, null, null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        return data;
    }

    public static String nombrePlan(String departamento, String plan, String estrato) {

        String nombrePlan = "";

        nombrePlan = nombrePlanHomoPrimeraLinea(departamento, plan, estrato);

        if (nombrePlan.trim().equalsIgnoreCase("")) {
            nombrePlan = nombrePlanHomoSegundaLinea(departamento, plan, estrato);
        }

        return nombrePlan;
    }

    public static String nombrePlanHomoPrimeraLinea(String departamento, String plan, String estrato) {

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios",
                new String[]{"Producto"}, "Departamento=? and homoPrimeraLinea=? and estrato like ?",
                new String[]{departamento, plan, "%" + estrato + "%"}, null, null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        return data;
    }

    public static String nombrePlanHomoSegundaLinea(String departamento, String plan, String estrato) {

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios",
                new String[]{"Producto"}, "Departamento=? and homoSegundaLinea=? and estrato like ?",
                new String[]{departamento, plan, "%" + estrato + "%"}, null, null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        return data;
    }

    public static boolean validarFechaConsuta(long fechaInicio, long fechaConsulta, Context context) {
        boolean validar = true;
        long resta = 0;

        // System.out.println("fechaInicio " + fechaInicio);
        //
        // System.out.println("fechaConsulta " + fechaConsulta);

        resta = (fechaConsulta - fechaInicio) / 1000;

        // System.out.println("resta en segundos " + resta);

        if (resta < tiempoScooring()) {
            MensajesToast("Debe Esperar minimo " + tiempoScooring() + " Segundos y lleva " + resta
                    + " Segundos para realizar la consulta del resultado", context);
            validar = false;
        }

        return validar;

    }

    public static int tiempoScooring() {

        String tiempo = Utilidades.camposUnicos("tiempoScooring");

        int tiempoScooring = 0;

        tiempoScooring = convertirNumericos(tiempo, "tiempoScooring");

        return tiempoScooring;

    }

    public static int convertirNumericos(String Valor, String Campo) {

        int numero = 0;

        try {

            numero = Integer.parseInt(Valor);

        } catch (NumberFormatException e) {
            Log.w("Error Campo " + Campo + " Descripcion error " + e.getMessage());
        }

        return numero;
    }

    public static double convertirDouble(String Valor, String Campo) {

        double numero = 0.0;

        try {

            numero = Double.parseDouble(Valor);

        } catch (NumberFormatException e) {
            Log.w("Error Campo " + Campo + " Descripcion error " + e.getMessage());
        }

        return numero;
    }

    public static String pruebasScooring(String documento) {

        String prueba = "";

        if (documento.equalsIgnoreCase("1111241111")) {
            prueba = "{'Medio':'Scooring','Respuesta':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','idScooring':'0011','dupli':'1','dataDupli':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'APROBADO','razonEstadoFinal':'SCORING','maximaCotizacion':'400000'}},'Documento':'111124111'}";
        } else if (documento.equalsIgnoreCase("1111241112")) {
            prueba = "{'Medio':'Scooring','Respuesta':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','idScooring':'0011','dupli':'1','dataDupli':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'ANULADO','razonEstadoFinal':'POR SCORE'}},'Documento':'111124112'}";
        } else if (documento.equalsIgnoreCase("1111241113")) {
            prueba = "{'Medio':'Scooring','Respuesta':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','idScooring':'0011','dupli':'1','dataDupli':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'ANULADO','razonEstadoFinal':'TERCERO NO EXISTE'}},'Documento':'111124113'}";
        }else if (documento.equalsIgnoreCase("1111241114")) {
            prueba = "{'Medio':'Scooring','Respuesta':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','idScooring':'0011','dupli':'1','dataDupli':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'ANULADO','razonEstadoFinal':'POR SCORE'}},'Documento':'111124114'}";
        }else if (documento.equalsIgnoreCase("1111241115")) {
            prueba = "{'Medio':'Scooring','Respuesta':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','idScooring':'0011','dupli':'1','dataDupli':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'ANULADO','razonEstadoFinal':'INCUMPLIMIENTO DE POLITICAS'}},'Documento':'111124115'}";
        }else if (documento.equalsIgnoreCase("4317614")) {
            prueba = "{'Medio':'Scooring','Respuesta':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','idScooring':'0011','dupli':'1','dataDupli':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'APROBADO','razonEstadoFinal':'SCORING','maximaCotizacion':'400000'}},'Documento':'4317614'}";
        }else if (documento.equalsIgnoreCase("7549110")) {
            prueba = "{'Medio':'Scooring','Respuesta':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','idScooring':'0011','dupli':'1','dataDupli':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'APROBADO','razonEstadoFinal':'SCORING','maximaCotizacion':'400000'}},'Documento':'7549110'}";
        }else if (documento.equalsIgnoreCase("24440257")) {
            prueba = "{'Medio':'Scooring','Respuesta':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','idScooring':'0011','dupli':'1','dataDupli':{'codigoMensaje':'00','descripcionMensaje':'Exitoso','estado':'EN PROCESO','estadoFinal':'APROBADO','razonEstadoFinal':'SCORING','maximaCotizacion':'400000'}},'Documento':'24440257'}";
        }

        if (!prueba.equalsIgnoreCase("")) {
            prueba = prueba.replace((char) 39, (char) 34);
        }

        System.out.println("prueba scooring " + prueba);

        return prueba;
    }

    public static boolean validarTelefono(String telefono) {

        boolean validacion = false;

        if (!telefono.equalsIgnoreCase("")) {
            if (telefono.length() == 7 || telefono.length() == 10) {
                validacion = true;
            }
        }

        return validacion;
    }

    public static void DialogoMensaje(Context context, String ciudad) {

        String mensaje = Utilidades.camposUnicosCiudad("promoMes", ciudad);

        System.out.println(" mensaje promo " + mensaje);

        if (!mensaje.equalsIgnoreCase("")) {
            Utilidades.MensajesToast(mensaje, context);
            Dialogo dialogo = new Dialogo(context, Dialogo.DIALOGO_ALERTA, mensaje);
            // dialogo.dialogo.setOnDismissListener(dlr);
            dialogo.dialogo.show();
        }
    }

    public static boolean pintarDigital(String plan, String tecnologia, String ciudad) {
        boolean pintar = false;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave"}, "lst_nombre=? and lst_clave=? and lst_valor=? and lst_ciudad=?",
                new String[]{"filtroDigital", plan, tecnologia, ciudad}, null, null, null);

        System.out.println("Respuesta pintarDigital " + respuesta);

        if (respuesta != null) {
            // oferta = respuesta.get(0).get(0);
            pintar = true;
        }

        return pintar;
    }

    public static String TVExistente(String plan) {

        String existente = "";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?", new String[]{"tvExistente", plan},
                null, null, null);

        System.out.println("Respuesta tvExistente " + respuesta);

        if (respuesta != null) {
            existente = respuesta.get(0).get(0);
            // pintar = true;
        }

        return existente;
        // return pintar;
    }

    public static boolean validarEstratoBronze(String estrato, String ciudad) {

        boolean aplica = false;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasestratos",
                new String[]{"lst_item"}, "lst_nombre=? and lst_estrato=? and lst_ciudad = ?",
                new String[]{"aplicaBronze", estrato, ciudad}, null, null, null);

        if (respuesta != null) {
            aplica = Boolean.valueOf(respuesta.get(0).get(0));
            // pintar = true;
        }

        System.out.println("aplicaBronze " + aplica);

        return aplica;

    }

    public static String localizacionDefault(String ciudad) {

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave,lst_valor"}, "lst_nombre=? and lst_ciudad=?",
                new String[]{"localizacionDefault", ciudad}, null, null, null);

        System.out.println("respuesta localizacionDefaul" + respuesta);

        JSONObject localizacionDefault = null;

        try {
            localizacionDefault = new JSONObject();

            if (respuesta != null) {

                for (int i = 0; i < respuesta.size(); i++) {
                    if (respuesta.get(i).get(0).equalsIgnoreCase("latitud")) {
                        localizacionDefault.put("latitud", respuesta.get(i).get(1));

                    } else if (respuesta.get(i).get(0).equalsIgnoreCase("longitud")) {
                        localizacionDefault.put("longitud", respuesta.get(i).get(1));

                    }
                }

                // pintar = true;
            } else {
                localizacionDefault.put("latitud", "0");
                localizacionDefault.put("longitud", "0");

            }

        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
            try {
                localizacionDefault.put("latitud", "0");
                localizacionDefault.put("longitud", "0");
            } catch (JSONException e1) {
                Log.w("Error " + e1.getMessage());
            }

        }

        return localizacionDefault.toString();
    }

    public static boolean tamanoCedula(String cedula, String tipoDocumento) {

        boolean tamano = true;

        if (tipoDocumento.equals("CC")) {

            if (cedula.length() == 4 || cedula.length() == 5 || cedula.length() == 6 || cedula.length() == 7
                    || cedula.length() == 8 || cedula.length() == 10) {
                tamano = true;
            } else {
                tamano = false;
            }

        } else if (tipoDocumento.equals("CE")) {

            if (cedula.length() == 4 || cedula.length() == 5 || cedula.length() == 6) {
                tamano = true;
            } else {
                tamano = false;
            }

        }

        return tamano;

    }

    public static boolean TOIP_Default(String cobertura, String tecnologiaCR) {

        boolean toip = false;

        System.out.println("TOIP_Default tecnologiaCR " + tecnologiaCR);
        System.out.println("TOIP_Default cobertura " + cobertura);

        if (tecnologiaCR != null && !tecnologiaCR.equalsIgnoreCase("") && !tecnologiaCR.equalsIgnoreCase(null)) {
            if (tecnologiaCR.equalsIgnoreCase("HFC") || tecnologiaCR.equalsIgnoreCase("GPON")) {
                System.out.println("TOIP_Default cobertura primera validacion");
                return true;
            }
        }

        try {
            JSONObject json = new JSONObject(cobertura);

            if (json.has("CodigoMensaje") && json.getString("CodigoMensaje").equalsIgnoreCase("00")) {

                System.out.println("TOIP_Default cobertura segunda validacion");

                if (json.has("Cobertura")) {

                    JSONObject cober = json.getJSONObject("Cobertura");

                    if (cober.has("COBERTURA_HFC") && cober.getString("COBERTURA_HFC").equalsIgnoreCase("SI")) {
                        // if(cober.has("DISPONIBILIDAD_HFC") &&
                        // cober.getString("DISPONIBILIDAD_HFC").equalsIgnoreCase("SI")){
                        // toip = true;
                        // }
                        toip = true;
                    }

                    if (cober.has("COBERTURA_GPON") && cober.getString("COBERTURA_GPON").equalsIgnoreCase("SI")) {
                        // if(cober.has("DISPONIBILIDAD_GPON") &&
                        // cober.getString("DISPONIBILIDAD_GPON").equalsIgnoreCase("SI")){
                        // toip = true;
                        // }
                        toip = true;

                    }
                }
            }

        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        return toip;

    }

    public static ArrayList<Object> precioDescuento(String descuento, String precioProducto) {

        ArrayList<Object> listDescuentos = new ArrayList<Object>();

        System.out.println("descuento " + descuento);

        if (descuento.contains("%")) {

            String vector[] = descuento.split("%");

            System.out.println("vector " + vector);

            for (int i = 0; i < vector.length; i++) {
                System.out.println("vector [" + i + "] " + vector[i]);
            }

            if (!vector[0].contentEquals("") && !vector[0].contentEquals("0")) {

                Double porcentaje = Utilidades.convertirDouble(vector[0], "vector[0]");

                System.out.println("porcentaje " + porcentaje);
                int precio = Utilidades.convertirNumericos(precioProducto, "precioProducto");

                System.out.println("porcentaje " + porcentaje);

                System.out.println("precio " + precio);

                listDescuentos.add(true);

                double valorDescuento = (precio * porcentaje / 100);

                double precioDescuento = precio - valorDescuento;

                listDescuentos.add(valorDescuento);

                listDescuentos.add(precioDescuento);

                System.out.println("listDescuentos " + listDescuentos);

                System.out.println("precio " + precio);

            }
        }

        if (listDescuentos.size() == 0) {
            listDescuentos.add(false);
        }

        System.out.println("listDescuentos " + listDescuentos);

        return listDescuentos;
    }

    public static ArrayList<String> excluirEstadosDomiciliacion() {
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre=?", new String[]{"estadosExcluidos"}, null, null, null);

        ArrayList<String> estados = new ArrayList<String>();

        if (respuesta != null) {
            for (int i = 0; i < respuesta.size(); i++) {
                System.out.println("respuesta.get(i).get(0) " + respuesta.get(i).get(0));
                estados.add(respuesta.get(i).get(0).toUpperCase());
            }

        }

        Log.e("estados" + estados);

        return estados;
    }

    public static ArrayList<String> excluirEstadosPagoAnticipado() {
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre=?", new String[]{"estadosExcluidosPagoAnticipado"}, null, null, null);

        ArrayList<String> estados = new ArrayList<String>();

        if (respuesta != null) {
            for (int i = 0; i < respuesta.size(); i++) {
                System.out.println("respuesta.get(i).get(0) " + respuesta.get(i).get(0));
                estados.add(respuesta.get(i).get(0).toUpperCase());
            }

        }

        Log.e("BanderaPA estados" + estados);

        return estados;
    }

    public static String cambioCobroDomingo(String cobroDomingo) {

        String data = "NO";

        if (cobroDomingo.equalsIgnoreCase("1")) {
            data = "SI";
        }

        return data;
    }

    public static String queryExternoPromociones(String queryInterno, String jsonDatos) {

        String departamento = "N/A";
        String ciudad = "N/A";
        String estrato = "N/A";
        String tecnologia = "N/A";
        String hogarNuevo = "N/A";
        String tvAnaloga = "N/A";

        try {
            JSONObject jsonObject = new JSONObject(jsonDatos);

            if (jsonObject.has("departamento")) {
                departamento = jsonObject.getString("departamento");
            }

            if (jsonObject.has("ciudad")) {
                ciudad = jsonObject.getString("ciudad");
            }

            if (jsonObject.has("estrato")) {
                estrato = jsonObject.getString("estrato");
            }

            if (jsonObject.has("tecnologia")) {
                tecnologia = jsonObject.getString("tecnologia");
            }

            if (jsonObject.has("hogarNuevo")) {
                hogarNuevo = jsonObject.getString("hogarNuevo");
            }

            if (jsonObject.has("tvAnaloga")) {
                tvAnaloga = jsonObject.getString("tvAnaloga");
            }

        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        String queryFinal = "SELECT p.tipo_producto, p.descuento, p.meses,max(prioridad),id_regla"
                + " FROM condicionesxreglas cxr" + " INNER JOIN reglas p ON p.regla=cxr.id_regla"
                + " WHERE cxr.id_condicion IN (" + " SELECT DISTINCT(c1.id) FROM condiciones c1"
                + " INNER JOIN condiciones c2 ON c1.id=c2.id" + " INNER JOIN condiciones c3 ON c1.id=c3.id"
                + " INNER JOIN condiciones c4 ON c1.id=c4.id" + " INNER JOIN condiciones c5 ON c1.id=c5.id"
                + " INNER JOIN condiciones c6 ON c1.id=c6.id" + " WHERE c1.clave='Departamento' AND (c1.valor LIKE '%"
                + departamento + "%' OR c1.valor = 'N/A')" + " AND c2.clave='Ciudad' AND (c2.valor LIKE '%" + ciudad
                + "%' OR c2.valor = 'N/A')" + " AND c3.clave='Estrato' AND (c3.valor LIKE '%" + estrato
                + "%' OR c3.valor = 'N/A')" + " AND c4.clave='Tecnologia' AND (c4.valor LIKE '%" + tecnologia
                + "%' OR c4.valor = 'N/A')" + " AND c5.clave='HogarNuevo' AND (c5.valor = '" + hogarNuevo
                + "' OR c5.valor = 'N/A')" + " AND c6.clave='TvAnaloga' AND (c6.valor = '" + tvAnaloga
                + "' OR c6.valor = 'N/A')" + " AND c1.tipo='Promocion'" + ")" + "and id_regla IN (" + queryInterno
                + ") " + " Group by p.tipo_producto" + " ORDER BY prioridad DESC";

        return queryFinal;
    }

    public static String queryExternoPromocionesAdicionales(String queryInterno, String jsonDatos) {

        String departamento = "N/A";
        String ciudad = "N/A";
        String estrato = "N/A";
        String tecnologia = "N/A";
        String hogarNuevo = "N/A";

        try {
            JSONObject jsonObject = new JSONObject(jsonDatos);

            if (jsonObject.has("departamento")) {
                departamento = jsonObject.getString("departamento");
            }

            if (jsonObject.has("ciudad")) {
                ciudad = jsonObject.getString("ciudad");
            }

            if (jsonObject.has("estrato")) {
                estrato = jsonObject.getString("estrato");
            }

            if (jsonObject.has("tecnologia")) {
                tecnologia = jsonObject.getString("tecnologia");
            }

            if (jsonObject.has("hogarNuevo")) {
                hogarNuevo = jsonObject.getString("hogarNuevo");
            }

        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        String queryFinal = "SELECT p.plan, p.descuento, p.meses, p.tipo_producto, prioridad, id_regla"
                + " FROM condicionesxreglas cxr" + " INNER JOIN reglas p ON p.regla=cxr.id_regla"
                + " WHERE cxr.id_condicion IN (" + " SELECT DISTINCT(c1.id) FROM condiciones c1"
                + " INNER JOIN condiciones c2 ON c1.id=c2.id" + " INNER JOIN condiciones c3 ON c1.id=c3.id"
                + " INNER JOIN condiciones c4 ON c1.id=c4.id" + " INNER JOIN condiciones c5 ON c1.id=c5.id"
                + " WHERE c1.clave='Departamento' AND (c1.valor LIKE '%" + departamento + "%' OR c1.valor = 'N/A')"
                + " AND c2.clave='Ciudad' AND (c2.valor LIKE '%" + ciudad + "%' OR c2.valor = 'N/A')"
                + " AND c3.clave='Estrato' AND (c3.valor LIKE '%" + estrato + "%' OR c3.valor = 'N/A')"
                + " AND c4.clave='Tecnologia' AND (c4.valor LIKE '%" + tecnologia + "%' OR c4.valor = 'N/A')"
                + " AND c5.clave='HogarNuevo' AND (c5.valor = '" + hogarNuevo + "' OR c5.valor = 'N/A')"
                + " AND c1.tipo='Promocion'" + ")" + "and id_regla IN (" + queryInterno + ")" + " group by p.plan"
                + " ORDER BY prioridad DESC";

        return queryFinal;
    }

    public static String limpiarDecimales(String txtNumero) {

        String numero = "0";

        if (txtNumero.contains(".")) {

            String txtSplit = txtNumero.replace(".", "-");

            String[] arrayDecimal = txtSplit.split("-");

            if (arrayDecimal.length > 1) {
                int decimal = Integer.parseInt(arrayDecimal[1]);

                if (decimal == 0) {
                    numero = arrayDecimal[0];

                } else {
                    numero = txtNumero;
                }
            } else {
                numero = txtNumero;
            }

        }

        return numero;

    }

    public static String limpiarDecimales2(String valor) {

        double numero = Double.parseDouble(valor);

        String patron = "###.####";
        String salida = "";

        DecimalFormat formato = new DecimalFormat(patron);
        salida = formato.format(numero);

        return salida;

    }

    public static String cortarString(String data, int tamano) {

        String cortado = "";

        if (data != null && !data.equalsIgnoreCase("")) {
            if (data.length() > tamano) {
                cortado = data.substring(0, tamano);
            } else {
                cortado = data;
            }
        }
        return cortado;
    }

    public static String dependenciaOferta(String plan) {

        String dependencia = "";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Productos",
                new String[]{"Oferta"}, "Producto=?", new String[]{plan}, null, null, null);

        if (respuesta != null) {
            dependencia = respuesta.get(0).get(0);
            // pintar = true;
        }

        System.out.println("Dependencia " + dependencia);

        return dependencia;

    }

    public static boolean validarTelefonos(Cliente cliente, Context context) {

        boolean validado = true;
        int count = 0;

        String telefonoServicio = Utilidades.limpiarTelefono(cliente.getTelefono());

        if (!telefonoServicio.equals("")) {
            if (telefonoServicio.length() == 7) {
                if (!Utilidades.ListaNegraTelefonos(telefonoServicio)) {
                    count++;
                } else {
                    Toast.makeText(context, "El telefono de servicio ingresado no es valido", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(context, "El telefono de servicio ingresado no es valido", Toast.LENGTH_LONG).show();
                return false;
            }

        }

        if (!cliente.getTelefono2().equals("")) {
            if (cliente.getTelefono2().length() == 7 || cliente.getTelefono2().length() == 10) {
                if (!Utilidades.ListaNegraTelefonos(cliente.getTelefono2())) {
                    count++;
                } else {
                    Toast.makeText(context, "El telefono secundario ingresado no es valido", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(context, "El telefono secundario debe estar compuesto por 7 o 10 Digitos",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (!cliente.getCelular().equals("") && cliente.getCelular().length() == 10) {
            if (!Utilidades.ListaNegraTelefonos(cliente.getCelular())) {
                count++;
            } else {
                Toast.makeText(context, "El celular ingresado no es valido", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(context, "Numero celular sin diligenciar, debe estar compuesto por 10 Digitos",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (count <= 1) {
            Toast.makeText(context,
                    "Recuerda que al menos uno de los telefonos, telefono de servicio o telefono secundario, debe estar diligenciado.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (!telefonoServicio.equals("")) {
            if (!cliente.getTelefono2().equals("")) {
                if (telefonoServicio.equals(cliente.getTelefono2())) {
                    Toast.makeText(context,
                            "El telefono de servicio y el telefono secundario son iguales, debes modificar uno de ellos para continuar con la venta",
                            Toast.LENGTH_LONG).show();
                    return false;
                }

                if (telefonoServicio.equals(cliente.getCelular())) {
                    Toast.makeText(context,
                            "El telefono de servicio y celular son iguales, debes modificar uno de ellos para continuar con la venta",
                            Toast.LENGTH_LONG).show();
                    return false;
                }

                if (cliente.getTelefono2().equals(cliente.getCelular())) {
                    Toast.makeText(context,
                            "El telefono secundario y celular son iguales, debes modificar uno de ellos para continuar con la venta",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                if (telefonoServicio.equals(cliente.getCelular())) {
                    Toast.makeText(context,
                            "El teléfono de servicio y celular son iguales, debes modificar uno de ellos para continuar con la venta",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            }

        } else if (!cliente.getTelefono2().equalsIgnoreCase("")) {
            if (cliente.getTelefono2().equals(cliente.getCelular())) {
                Toast.makeText(context,
                        "El telefono secundario y celular son iguales, debes modificar uno de ellos para continuar con la venta",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return validado;
    }

    public static boolean validarDireccionesCerca(String direccion) {

        boolean esCerca = false;

        if (direccion.toUpperCase().startsWith("CERCA A")) {
            esCerca = true;
        }

        return esCerca;

    }

    public static boolean validarEtiquetaJson(JSONObject json, String name, String nameInterno) {
        boolean validar = true;

        try {
            if (json.has(name)) {
                if (json.get(name).getClass().getSimpleName().equals("JSONArray")) {

                    JSONArray array = json.getJSONArray(name);

                    if (array.length() > 0) {
                        if (!array.getJSONObject(0).has(nameInterno)) {
                            validar = false;
                            System.out.println("Fallo " + name + "Array");
                        }
                    } else {
                        System.out.println("Fallo " + name + " Array Vacio");
                        validar = false;
                    }

                } else if (json.get(name).getClass().getSimpleName().equals("JSONObject")) {
                    if (!json.getJSONObject(name).has(nameInterno)) {
                        validar = false;
                        System.out.println("Fallo " + name + " object");
                    }
                } else {
                    validar = false;
                    System.out.println("fallo " + name + " string");
                }
            } else {
                validar = false;
                System.out.println("fallo " + name + "");
            }
        } catch (JSONException e) {
            validar = false;
            System.out.println("Excepcion " + e.getMessage());
        }

        return validar;
    }

    public static ArrayList<ListaIpDinamica> direccionamientoIP(String infoCliente) {

        JSONObject cliente;

        boolean internet = false;

        ArrayList<String> idInternet = new ArrayList<String>();

        ArrayList<ListaIpDinamica> ipDinamica = new ArrayList<ListaIpDinamica>();

        try {

            cliente = new JSONObject(infoCliente);

            System.out.println("cliente portafolio" + cliente);

            if (cliente.has("productosInstalados")) {
                if (cliente.getJSONObject("productosInstalados").has("INTER")) {
                    idInternet.add(cliente.getJSONObject("productosInstalados").getString("INTER"));
                    internet = true;
                }
            } else if (cliente.has("productosInstalados2")) {

                System.out.println(
                        "cliente.getJSONObject(productosInstalados2) " + cliente.getJSONObject("productosInstalados2"));
                if (cliente.getJSONObject("productosInstalados2").has("INTER")) {

                    JSONArray arrayBa = cliente.getJSONObject("productosInstalados2").getJSONArray("INTER");
                    for (int i = 0; i < arrayBa.length(); i++) {
                        idInternet.add(arrayBa.getString(i));
                        internet = true;
                    }
                }
            }

            System.out.println("ip dinamica idInternet " + idInternet);

            if (cliente.has("datosProductosInstalados") && internet) {
                System.out.println(
                        "ip dinamica datosProductosInstalados " + cliente.getJSONObject("datosProductosInstalados"));
                for (int i = 0; i < idInternet.size(); i++) {

                    System.out.println("ip dinamica idInternet for" + idInternet.get(i));

                    if (cliente.getJSONObject("datosProductosInstalados").has(idInternet.get(i))) {
                        if (cliente.getJSONObject("datosProductosInstalados").getJSONObject(idInternet.get(i))
                                .has("ipDinamica")) {
                            // ipDinamica =
                            // cliente.getJSONObject("datosProductosInstalados").getJSONObject(idinternet).getString("ipDinamica");
                            ipDinamica.add(new ListaIpDinamica(idInternet.get(i),
                                    cliente.getJSONObject("datosProductosInstalados").getJSONObject(idInternet.get(i))
                                            .getString("ipDinamica")));
                        }
                    }
                }

            }

            System.out.println("ip dinamica result " + ipDinamica);

            if (ipDinamica.size() > 0) {
                for (int i = 0; i < ipDinamica.size(); i++) {
                    System.out.println("ip dinamica id " + ipDinamica.get(i).getIdentificador());
                    System.out.println("ip dinamica ip " + ipDinamica.get(i).getIpDinamica());
                }
            }

        } catch (JSONException e) {
            Log.w("error " + e);
        }

        return ipDinamica;

    }

    public static String coberturaProyecto(String cobertura) {

        String coberDefault = "{'CodigoMensaje':'01','DescripcionMensaje':'Exitoso','Cobertura':{'COBERTURA_REDCO':'NO','DISPONIBILIDAD_REDCO':null,'COBERTURA_HFC':'NO','DISPONIBILIDAD_HFC':null,'COBERTURA_INALA':'NO','DISPONIBILIDAD_INALA':'NO','COBERTURA_GPON':'NO','DISPONIBILIDAD_GPON':null,'DIRECCIONALIDAD':'','DISTANCIA':'','HFC_DIGITAL':'N','NODO_REDCO':'BUENOS AIRES','ID_NODO_REDCO':null,'ANCHO_BANDA_SUBIDA':null,'ANCHO_BANDA_BAJADA':null,'NODO_HFC':null,'ID_NODO_HFC':null,'NODO_GPON':null,'ID_NODO_GPON':null,'Mensaje':''}}";

        coberDefault = coberDefault.replace((char) 39, (char) 34);

        JSONObject cober = null;

        try {
            cober = new JSONObject(coberDefault);

            JSONObject data = cober.getJSONObject("Cobertura");

            String homologarCobertura = Utilidades.claveValor("homologarTecnologia", cobertura);

            System.out.println("homologarCobertura " + homologarCobertura);

            if (homologarCobertura.equals("GPON")) {
                data.put("COBERTURA_GPON", "SI");
            } else if (homologarCobertura.equals("HFC")) {
                data.put("COBERTURA_HFC", "SI");
                data.put("DIRECCIONALIDAD", "B");
            } else if (homologarCobertura.equals("REDCO")) {
                data.put("COBERTURA_REDCO", "SI");
                data.put("DISTANCIA", "100");
            }

            cober.put("CodigoMensaje", "00");

            System.out.println("Cobertura Proyecto " + cober.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("cober proyecto rural " + cober);

        return cober.toString();
    }

    public static boolean validarPermiso(String permiso) {
        boolean valido = false;

        ArrayList<ArrayList<String>> permisos = MainActivity.basedatos.consultar(false, "permisos", null, "accion = ?", new String[]{permiso}, null, null, null);
        System.out.println("----Carrusel--- permisos carrusel "+permisos);
        if (permisos != null) {
            valido = true;
        }

        return valido;
    }

    public static boolean CoberturaRural(Cliente cliente) {
        boolean cobertura = false;

        if (cliente.coberturaRural != null) {
            if (cliente.coberturaRural.isCoberturaRural()) {
                cobertura = true;
            }
        }

        return cobertura;
    }

    public static String generarJsonVerificacionServicios() {

        JSONObject datos = new JSONObject();

        try {

            datos.put("codigoAsesor", MainActivity.config.getCodigo());
            datos.put("versionAplicacion", MainActivity.config.srvVersion);
            datos.put("versionDB", MainActivity.config.getVersionDB());

        } catch (JSONException e) {
            android.util.Log.e("Error Generando JSON ", e.getMessage());
        }

        return datos.toString();
    }

    public static boolean respuestaConfigMovil(JSONObject respuesta) {

        System.out.println("jsonRespuesta "+respuesta);
        try {
            if(respuesta.has("validacion") && !respuesta.getBoolean("validacion")){
              return false;
            }
        } catch (JSONException e) {
            Log.e("Error json respuesta "+e.getMessage());
        }
        return true;
    }

    public static Object limpiarValidacionConfiguracion(JSONObject respuesta) {

        System.out.println("jsonRespuesta "+respuesta);
        try {
            if(respuesta.has("validacion") && !respuesta.getBoolean("validacion")){
                return false;
            }
        } catch (JSONException e) {
            Log.e("Error json respuesta "+e.getMessage());
        }
        return true;
    }

    public static String pruebasAutenticacion() {

        String prueba = "{'codigoAsesor':'1234545','versionAplicacion':'2.0.1d'}";

        if (!prueba.equalsIgnoreCase("")) {
            prueba = prueba.replace((char) 39, (char) 34);
        }

        System.out.println("pruebasAutenticacion" + prueba);

        return prueba;
    }

    public static ArrayList<Object> validacionConfig (ArrayList<Object> result){

        ArrayList<Object> validacion = new ArrayList<Object>();
        validacion.clear();

        try {

            JSONObject jop = new JSONObject(String.valueOf(result.get(1)));

            String data = jop.get("data").toString();

            if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(org.kobjects.base64.Base64.decode(data));
                // Confirmacion(data);
                JSONObject informacion = new JSONObject(data);

                System.out.println("validacionConfig validacion " + Utilidades.respuestaConfigMovil(informacion));

                boolean valido = Utilidades.respuestaConfigMovil(informacion);

                validacion.add(valido);

                //System.out.println("validacionConfig Data "+informacion.getJSONObject("respuesta"));

                if(valido) {
                    if(informacion.get("respuesta").getClass().getSimpleName().equals("JSONArray")) {
                        validacion.add(informacion.getJSONArray("respuesta"));
                    }else if(informacion.get("respuesta").getClass().getSimpleName().equals("JSONObject")){
                        validacion.add(informacion.getJSONObject("respuesta"));
                    }else{
                        validacion.add(informacion.getString("respuesta"));
                    }
            }

            }
        } catch (JSONException e) {
            validacion.clear();
            validacion.add(false);
            validacion.add("");
        }

        System.out.println("validacionConfig resultadoValidacion "+validacion);

        return validacion;
    }

    public static String homologarMunicipio (String ciudad){

        if (ciudad.equalsIgnoreCase("BogotaREDCO")
                || ciudad.equalsIgnoreCase("BogotaHFC")) {
            ciudad = "Bogota";
        }

        return ciudad;
    }

    public static boolean validarVacioProducto(String producto){
        boolean vacio = false;

        if(producto != null && producto.equalsIgnoreCase(inicial_vacio) || producto.equalsIgnoreCase(inicial_guion)){
            vacio = true;
        }

        System.out.println("----Carrusel--- vacio " + vacio);

        return vacio;

    }

    public static boolean validarIgualdad(String campo1, String campo2){
        boolean igualdad = false;

        if(campo1.equalsIgnoreCase(campo2)){
            igualdad = true;
        }

        return igualdad;
    }

    public static JSONObject jsonProductosCotizacion(ProductoCotizador productoCotizador) {

        //System.out.println("jsonProductosVenta->tecnologiacr " + tecnologiacr);
        JSONArray arrayAdicionales = null;

        JSONArray arrayEquipos = new JSONArray();
        JSONObject mejorasDecos = new JSONObject();
        int wifi;
        String cambioPlan = "";

        if (productoCotizador.getCambioPlan().equalsIgnoreCase("Nueva")) {
            cambioPlan = "0";
        } else {
            cambioPlan = "1";
        }

        if (productoCotizador.getWifi() != null) {
            if (productoCotizador.getWifi().equalsIgnoreCase("SI")) {
                wifi = 1;
            } else {
                wifi = 0;
            }
        }else{
            wifi = -1;
        }


        ArrayList<ItemDecodificador> itemDecodificadors = null;

        if(productoCotizador.getDecodificadores() != null){
            itemDecodificadors = productoCotizador.getDecodificadores();
        }

        JSONObject productos = new JSONObject();
        try {
            productos.put("nombre", productoCotizador.getPlan());
            productos.put("tipoCotizacion", productoCotizador.getTipoPeticionNumerico());
            productos.put("tipo", productoCotizador.traducirProducto().toUpperCase());
            productos.put("cambioPlan", cambioPlan);
            productos.put("planAnterior", "");
            productos.put("precio", productoCotizador.getPrecio());
            productos.put("pagoAntCargoFijo", productoCotizador.getPagoAnticipado());
            productos.put("pagoParcialConexion", productoCotizador.getPagoParcial());
            productos.put("descuento", productoCotizador.getDescuentoCargobasico());
            productos.put("duracion", productoCotizador.getDuracionDescuento());
            productos.put("extensiones", "");
            productos.put("linea", productoCotizador.getLinea());
            productos.put("wifi", wifi);
            productos.put("marca", "");
            productos.put("referencia", "");
            productos.put("capacidad", "");
            productos.put("homologado", "");
            productos.put("planfacturacion", "");
            productos.put("identificador", "");
            productos.put("ipDinamica", "");
            productos.put("adicionales", jsonArrayAdicionales(productoCotizador.getAdicionalesCotizador(),itemDecodificadors ));
            productos.put("planFacturacion", productoCotizador.getPlanFacturacion());
            productos.put("tecnologia", productoCotizador.getTecnologia());
            productos.put("tecnologiacr", "N/A");
            productos.put("activacion",productoCotizador.getActivacion());
            productos.put("tipoTransaccion",productoCotizador.getTipoTransaccion());
            productos.put("inicioFacturacion",productoCotizador.getInicioFacturacion());
            productos.put("tipoFacturacion",productoCotizador.getTipoFacturacion());
            productos.put("smartPromo",productoCotizador.getSmartPromo());


            /*if (!productoCotizador.getTecnologia().equalsIgnoreCase("IPTV")) {
                productos.put("tecnologia", productoCotizador.getTecnologia());
            } else {
                productos.put("tecnologia", "REDCO");
            }*/

            /*productos.put("tecnologiacr", tecnologiacr);*/

            /*System.out.println("decos productoCotizador.getJsonDecos()" + productoCotizador.getJsonDecos());

            if (productoCotizador.getJsonDecos() != null && productoCotizador.getJsonDecos().equalsIgnoreCase("") && !productoCotizador.getJsonDecos().equalsIgnoreCase("N/A")) {

                productos.put("decos", new JSONObject(productoCotizador.getJsonDecos()));

                mejorasDecos.put("decoPosicion1", new JSONObject(productoCotizador.getJsonDecos()).get("decoPosicion1"));
                mejorasDecos.put("decoPosicion2", new JSONObject(productoCotizador.getJsonDecos()).get("decoPosicion2"));
                mejorasDecos.put("decoPosicion3", new JSONObject(productoCotizador.getJsonDecos()).get("decoPosicion3"));
                mejorasDecos.put("decoPosicion4", new JSONObject(productoCotizador.getJsonDecos()).get("decoPosicion4"));

            }*/


            //UtilidadesDecos.imprimirDecos("itemDecodificadors jsonProductosVenta tipo ["+productoCotizador.getTipo()+"] ",itemDecodificadors);

            if (itemDecodificadors != null && itemDecodificadors.size() > 0) {
                //UtilidadesDecos.imprimirDecos("Consolidar ", itemDecodificadors);
                ArrayList<JSONObject> objectEquipos = new ArrayList<JSONObject>();

                String decosActuales = "";
                String infoDecosActuales = "";

                for (int i = 0; i < itemDecodificadors.size(); i++) {

                    /*System.out.println("itemDecodificadors.get(i).getInfoActualesDecos() "
                            + itemDecodificadors.get(i).getInfoActualesDecos());
                    System.out.println("itemDecodificadors.get(i).isNuevo() " + itemDecodificadors.get(i).isNuevo());*/

                    if (!itemDecodificadors.get(i).isNuevo()) {
                        decosActuales = itemDecodificadors.get(i).getDecosActuales();
                        infoDecosActuales = itemDecodificadors.get(i).getInfoActualesDecos();
                        break;
                    }
                }

               /* System.out.println("decosActuales A " + decosActuales);
                System.out.println("infoDecosActuales A " + infoDecosActuales);*/

                if (decosActuales.equalsIgnoreCase("") && infoDecosActuales.equalsIgnoreCase("")) {
                    for (int i = 0; i < itemDecodificadors.size(); i++) {
                        if (itemDecodificadors.get(i).isIncluido()) {
                            decosActuales = itemDecodificadors.get(i).getDecosActuales();
                            infoDecosActuales = itemDecodificadors.get(i).getInfoActualesDecos();
                            break;
                        }
                    }
                }

                /*System.out.println("decosActuales B " + decosActuales);
                System.out.println("infoDecosActuales B " + infoDecosActuales);*/

                for (int i = 0; i < itemDecodificadors.size(); i++) {
                    objectEquipos.add(
                            jsonEquipos(itemDecodificadors.get(i), decosActuales, infoDecosActuales, productoCotizador.getTipoPeticion()));
                }

                for (int i = 0; i < objectEquipos.size(); i++) {
                    arrayEquipos.put(objectEquipos.get(i));
                    System.out.println("arrayEquipos.get(" + i + ") " + arrayEquipos.get(i).toString());
                }

                System.out.println("arrayEquipos " + arrayEquipos);

                mejorasDecos.put("mejoraDecos", "SI");
                mejorasDecos.put("equipos", arrayEquipos);
                //mejorasDecos.put("decos_sd", new JSONObject(decos).get("decos_sd"));
                System.out.println("decos productoCotizador.getJsonDecos()" + productoCotizador.getJsonDecos());

                if (productoCotizador.getJsonDecos() != null && !productoCotizador.getJsonDecos().equalsIgnoreCase("") && !productoCotizador.getJsonDecos().equalsIgnoreCase("N/A")) {

                    productos.put("infoDecos", new JSONObject(productoCotizador.getJsonDecos()));

                    mejorasDecos.put("decoPosicion1", new JSONObject(productoCotizador.getJsonDecos()).get("decoPosicion1"));
                    mejorasDecos.put("decoPosicion2", new JSONObject(productoCotizador.getJsonDecos()).get("decoPosicion2"));
                    mejorasDecos.put("decoPosicion3", new JSONObject(productoCotizador.getJsonDecos()).get("decoPosicion3"));
                    mejorasDecos.put("decoPosicion4", new JSONObject(productoCotizador.getJsonDecos()).get("decoPosicion4"));

                }

                System.out.println("***** mejorasDecos ****"+mejorasDecos);
                System.out.println("***** productos ****"+productos);


            }

            productos.put("decos", mejorasDecos);

            if (productoCotizador.traducirProducto().equalsIgnoreCase("BA")) {
                productos.put("homologado", Utilidades.homologado(productoCotizador.getPlan()));
            } else {
                productos.put("homologado", "");
            }

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        //System.out.println("productos tipo ["+productoCotizador.traducirProducto()+"] "+productos);
        return productos;
    }

    public static JSONArray jsonArrayAdicionales(ArrayList<AdicionalCotizador> adicionalesCotizador, ArrayList<ItemDecodificador> itemDecodificadors) {

        JSONArray arrayAdicionales = new JSONArray();

        if(adicionalesCotizador != null) {
            for (int i = 0; i < adicionalesCotizador.size(); i++) {
                arrayAdicionales.put(jsonObjectAdicional(adicionalesCotizador.get(i)));
            }
        }

        /*if(itemDecodificadors != null){
            for (int i = 0; i < itemDecodificadors.size(); i++) {
                arrayAdicionales.put(jsonObjectAdicionalDecos(itemDecodificadors.get(i)));
            }
        }*/

        return arrayAdicionales;

    }

    public static JSONObject jsonObjectAdicional(AdicionalCotizador adicionalesCotizador) {

        JSONObject adicional = new JSONObject();

        try {
            adicional.put("solicitud", adicionalesCotizador.getTiposolicitud());
            adicional.put("tipo", adicionalesCotizador.getTipoAdicional());
            adicional.put("producto", adicionalesCotizador.getNombreAdicional());
            adicional.put("precio", String.valueOf(adicionalesCotizador.getPrecioAdicional()));
            adicional.put("precioSinIva", adicionalesCotizador.getPrecioSinIva());
            adicional.put("descuento", adicionalesCotizador.getDescuento());
            adicional.put("duracion", adicionalesCotizador.getDuracionDescuento());
            adicional.put("permanencia", adicionalesCotizador.getPermanencia());
            adicional.put("demo", adicionalesCotizador.getDemo());
        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        return adicional;
    }

    public static JSONObject jsonObjectAdicionalDecos(ItemDecodificador itemDecodificador) {

        JSONObject adicional = new JSONObject();

        try {
            adicional.put("tipo", "Equipo");
            adicional.put("solicitud", itemDecodificador.getTipoTransaccion());
            adicional.put("nombre", itemDecodificador.getOriginal());
            adicional.put("precio", String.valueOf(itemDecodificador.getPrecio()));
            adicional.put("precioSinIva", null);
            adicional.put("descuento", itemDecodificador.getDescuento());
            adicional.put("duracion", itemDecodificador.getTiempo());
            adicional.put("permanencia", 0);
            adicional.put("demo", 0);
        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        return adicional;
    }

    public static JSONObject jsonProductosCotizacionIVR(ProductoCotizador productoCotizador) {

        //System.out.println("jsonProductosVenta->tecnologiacr " + tecnologiacr);
        JSONArray arrayAdicionales = null;

        JSONArray arrayEquipos = new JSONArray();
        JSONObject mejorasDecos = new JSONObject();
        int wifi;
        String cambioPlan = "";

        if (productoCotizador.getCambioPlan().equalsIgnoreCase("Nueva")) {
            cambioPlan = "0";
        } else {
            cambioPlan = "1";
        }

        if (productoCotizador.getWifi() != null) {
            if (productoCotizador.getWifi().equalsIgnoreCase("SI")) {
                wifi = 1;
            } else {
                wifi = 0;
            }
        }else{
            wifi = -1;
        }


        ArrayList<ItemDecodificador> itemDecodificadors = null;

            /*System.out.println("decodificador tipo ["+tipo+"] "+decodificador);*/

        if(productoCotizador.getDecodificadores() != null){
            itemDecodificadors = productoCotizador.getDecodificadores();
        }

        JSONObject productos = new JSONObject();
        try {
            productos.put("idProducto", "");
            productos.put("tipoProducto", productoCotizador.traducirProducto().toUpperCase());
            productos.put("nombre", ConcatenarProducto(productoCotizador.traducirProducto().toUpperCase(), UtilidadesTarificadorNew.cambiarPlan(productoCotizador.getPlan())));
            productos.put("id", "");
            productos.put("valor", productoCotizador.getPrecio());
            productos.put("porcentajeDescuento", productoCotizador.getDescuentoCargobasico());
            productos.put("duracionDescuento", productoCotizador.getDuracionDescuento());
            productos.put("permanencia", false);

            if(productoCotizador.getTipoPeticion().equalsIgnoreCase("N")){
                productos.put("permanencia", true);
            }

            productos.put("accion", productoCotizador.getTipoPeticionNombreCompeto());
            productos.put("pagoAntCargoFijo", productoCotizador.getPagoAnticipado());
            productos.put("pagoParcialConexion", productoCotizador.getPagoParcial());
            productos.put("adicionales", jsonArrayAdicionalesIVR(productoCotizador.getAdicionalesCotizador(),itemDecodificadors ));
            productos.put("activacion",productoCotizador.getActivacion());
            productos.put("tipoTransaccion",productoCotizador.getTipoTransaccion());
            productos.put("inicioFacturacion",productoCotizador.getInicioFacturacion());
            productos.put("tipoFacturacion",productoCotizador.getTipoFacturacion());

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        //System.out.println("productos tipo ["+productoCotizador.traducirProducto()+"] "+productos);
        return productos;
    }

    public static String ConcatenarProducto(String tipoProducto, String producto){

        String productoHomologado = producto;

        if(tipoProducto.equalsIgnoreCase("TO")){
            if(!producto.contains("Telefonia")){
                productoHomologado = "Telefonia "+producto;
            }
        }else if(tipoProducto.equalsIgnoreCase("TV")){
            if(!producto.contains("Television")){
                productoHomologado = "Television "+producto;
            }
        }else if(tipoProducto.equalsIgnoreCase("BA")){
            if(!producto.contains("Internet")){
                productoHomologado = "Internet "+producto;
            }
        }

        return productoHomologado;
    }

    public static JSONArray jsonArrayAdicionalesIVR(ArrayList<AdicionalCotizador> adicionalesCotizador, ArrayList<ItemDecodificador> itemDecodificadors) {

        JSONArray arrayAdicionales = new JSONArray();

        if(adicionalesCotizador != null) {
            for (int i = 0; i < adicionalesCotizador.size(); i++) {
                if(!adicionalesCotizador.get(i).getTipoAdicional().equalsIgnoreCase("IMP")) {
                    arrayAdicionales.put(jsonObjectAdicionalIVR(adicionalesCotizador.get(i)));
                }
            }
        }

        if(itemDecodificadors != null){
            for (int i = 0; i < itemDecodificadors.size(); i++) {
                if(!itemDecodificadors.get(i).isIncluido() && !itemDecodificadors.get(i).isFidelizaIncluido()) {
                    arrayAdicionales.put(jsonObjectAdicionalDecosIVR(itemDecodificadors.get(i)));
                }
            }
        }

        return arrayAdicionales;

    }

    public static JSONObject jsonObjectAdicionalIVR(AdicionalCotizador adicionalesCotizador) {

        JSONObject adicional = new JSONObject();

        try {
            adicional.put("idProducto", "");
            adicional.put("tipoProducto", adicionalesCotizador.getTipoAdicional());
            adicional.put("nombre", adicionalesCotizador.getNombreAdicional());
            adicional.put("id", "");
            adicional.put("valor", String.valueOf(adicionalesCotizador.getPrecioAdicional()));
            adicional.put("porcentajeDescuento", adicionalesCotizador.getDescuento());
            adicional.put("duracionDescuento", adicionalesCotizador.getDuracionDescuento());
            adicional.put("permanencia", false);
            adicional.put("adicionales",new JSONArray());
            adicional.put("cargos",new JSONArray());
            adicional.put("accion", adicionalesCotizador.getTiposolicitud());
        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        return adicional;
    }

    public static JSONObject jsonObjectAdicionalDecosIVR(ItemDecodificador itemDecodificador) {

        JSONObject adicional = new JSONObject();

        try {
                adicional.put("idProducto", "");
                adicional.put("tipoProducto", "Equipo");
                adicional.put("nombre", UtilidadesTarificadorNew.homologarPlanDecosIVR(itemDecodificador.getOriginal()));
                adicional.put("id", "");
                adicional.put("valor", String.valueOf(itemDecodificador.getPrecio()));
                adicional.put("porcentajeDescuento", itemDecodificador.getDescuento());
                adicional.put("duracionDescuento", itemDecodificador.getTiempo());
                adicional.put("permanencia", false);
                adicional.put("adicionales", new JSONArray());
                adicional.put("cargos", new JSONArray());
                adicional.put("accion", itemDecodificador.getTipoTransaccion());

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        return adicional;
    }

    public static Boolean validarNullyVacio(String parametro) {

        boolean datoNullyVacio = false;

        if(parametro == null || parametro.equalsIgnoreCase("")){
            datoNullyVacio = true;
        }

        return datoNullyVacio;
    }

    public static String cumpleMail(Cliente cliente){

        String mail = "NO";

        if (!cliente.getCorreo().equalsIgnoreCase("") && !cliente.getCorreo().contains("@sincorreo.com")) {
            mail = "SI";
        }

        return mail;
    }

}
