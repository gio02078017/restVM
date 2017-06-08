package co.com.une.appmovilesune.change;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ItemDependencias;
import co.com.une.appmovilesune.adapters.ItemKeyValue;
import co.com.une.appmovilesune.adapters.ItemKeyValue2;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.complements.Observador;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.ProductoAMG;
import co.com.une.appmovilesune.model.Simulador;

public class UtilidadesTarificador {

    private static ArrayList<ItemKeyValue2> datosValidacion = new ArrayList<ItemKeyValue2>();
    private static ArrayList<ItemKeyValue2> countValidacion = new ArrayList<ItemKeyValue2>();
    private static ArrayList<String> countValidacion2 = new ArrayList<String>();

    static ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();
    static ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionalesGratis = new ArrayList<ItemPromocionesAdicionales>();

    private static boolean validarAdicionales = true;

    private static String estrato;
    private static String ciudadCliente;

    private static boolean dependencia = false;
    private static boolean exception = true;
    private static boolean control = true;
    private static String Oferta = "";

    private static int Contador_productos = 0;
    private static int contadorFija = 0;

    private static String dependenciaTO, dependenciaTV, dependenciaBA;

    private static int cantidadTO = 0, cantidadTV = 0, cantidadBA = 0;

    private static ArrayList<ItemDependencias> itemDependencias = new ArrayList<ItemDependencias>();

    private static int Contador = 0;

    private static String[][] Descripcion_Ad;

    private Context context;

    public UtilidadesTarificador() {

    }

    public static ArrayList<ArrayList<String>> llenarAdicionales(String tipoAdicional, String departamento,
                                                                 String estrato) {
        String tipo = "", tipoProducto = "tv";

        System.out.println(
                "tipoAdicional -> " + tipoAdicional + " departamento -> " + departamento + " estrato -> " + estrato);

        if (tipoAdicional.contains("HFC")) {
            tipo = "HFC";
        } else if (tipoAdicional.contains("IPTV")) {
            tipo = "IPTV";
        } else if (tipoAdicional.contains("DIGITAL")) {
            tipo = "HFC";
        } else {
            tipo = "IPTV";
        }

        System.out.println("tipo -> " + tipo);

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Adicionales",
                new String[]{"adicional", "tarifa"},
                "departamento like ? and producto like ? and tipoProducto = ? and estrato like ?"
                        + "and adicional like ? ",
                new String[]{"%" + departamento + "%", "%" + tipo + "%", tipoProducto, "%" + estrato + "%",
                        "%Decodificador%"},
                null, "producto,adicional ASC", null);

        return respuesta;

    }

    public static String precioAdicional(String adicional, String departamento, String estrato) {
        String tipo = "", tipoProducto = "tv";

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Adicionales",
                new String[]{"tarifaIva"}, "departamento like ? and estrato like ? and adicional = ?",
                new String[]{"%" + departamento + "%", "%" + estrato + "%", adicional}, null, null, null);

        System.out.println("result precioAdicional " + respuesta);

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

    public static boolean validarAdicionalesAMC(ArrayList<ItemPromocionesAdicionales> adicionales) {

        validarAdicionales = true;

        itemPromocionesAdicionales = adicionales;

        countAdicionales();

        return validarAdicionales;

    }

    public static void countAdicionales() {
        countValidacion.clear();

        countValidacion2.clear();

        for (int i = 0; i < itemPromocionesAdicionales.size(); i++) {
            System.out.println("itemPromocionesAdicionales.get(i).getAdicional() "
                    + itemPromocionesAdicionales.get(i).getAdicional());

            String tipoAdicional = tipoAdicional(itemPromocionesAdicionales.get(i).getAdicional());

            if (tipoAdicional != null) {
                // countValidacion.add(new ItemKeyValue2(tipoAdicional, 1));
                countValidacion2.add(tipoAdicional);
            }
        }

        if (countValidacion2.size() > 0) {
            validacionAdicionales();
        }

        for (int i = 0; i < countValidacion2.size(); i++) {
            System.out.println("datos " + countValidacion2.get(i));
        }

        Set<String> quipu = new HashSet<String>(countValidacion2);
        for (String key : quipu) {

            countValidacion.add(new ItemKeyValue2(key, Collections.frequency(countValidacion2, key)));
        }

        for (int i = 0; i < countValidacion.size(); i++) {
            System.out.println("Key " + countValidacion.get(i).getKey());
            System.out.println("Value " + countValidacion.get(i).getValues());
        }

        validarAdicionales();

    }

    public static String tipoAdicional(String Adicional) {
        String tipoAdicional = null;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?",
                new String[]{"tipoAdicionalTV", Adicional}, null, null, null);

        if (respuesta != null) {
            tipoAdicional = respuesta.get(0).get(0);
        }

        return tipoAdicional;

    }

    public static void validacionAdicionales() {

        datosValidacion.clear();
        int value;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave,lst_valor"}, "lst_nombre=?", new String[]{"validacionAdicionalesTV"},
                null, null, null);

        System.out.println("respuesta " + respuesta);

        if (respuesta != null) {
            for (int i = 0; i < respuesta.size(); i++) {

                String key = respuesta.get(i).get(0);
                try {
                    value = Integer.parseInt(respuesta.get(i).get(1));
                    datosValidacion.add(new ItemKeyValue2(key, value));
                } catch (NumberFormatException e) {
                    value = 0;
                }

            }

        }

    }

    public static void validarAdicionales() {

        System.out.println("validar " + validarAdicionales);

        if (countValidacion.size() > 0) {
            for (int i = 0; i < countValidacion.size(); i++) {
                for (int j = 0; j < datosValidacion.size(); j++) {
                    System.out.println("countValidacion.get(i) " + countValidacion.get(i));
                    System.out.println("datosValidacion.get(j) " + datosValidacion.get(j));
                    if (countValidacion.get(i).getKey().equals(datosValidacion.get(j).getKey())) {
                        if (countValidacion.get(i).getValues() <= datosValidacion.get(j).getValues()) {
                            ;
                        } else {
                            validarAdicionales = false;
                        }
                    }

                }

            }
        }

        System.out.println("validar " + validarAdicionales);

        // return validarAdicionales;

    }

    public static boolean validarOferta(ArrayList<ProductoAMG> oferta, String estratoL) {

        estrato = estratoL;

        for (int i = 0; i < oferta.size(); i++) {
            Consulta(oferta.get(i).plan, oferta.get(i).producto, oferta.size());
        }

        int cantidad = 0;

        String controlDependencia = "";
        itemPromocionesAdicionales.clear();

        System.out.println("exception " + exception);

        if (exception) {
            control = true;
        }

        System.out.println("control " + control);

        System.out.println("Dependencias " + dependencia);

        if (dependencia) {

            System.out.println("hola itemDependencias.sise() " + itemDependencias.size());

            if (itemDependencias.size() > 0) {
                for (int i = 0; i < oferta.size(); i++) {
                    if (oferta.get(i).producto.equalsIgnoreCase("ADICHD")) {
                        contadorFija += ContadoOfertas(oferta.get(i), oferta.size());
                    }
                }

            }

            System.out.println("contadorFija " + contadorFija + " itemDependencias.size() " + itemDependencias.size());

            if (itemDependencias.size() == contadorFija) {
                for (int i = 0; i < itemDependencias.size(); i++) {

                    if (itemDependencias.get(i).getCantidad() != contadorFija) {
                        control = false;
                        System.out.println("Salida por cantidad");
                        break;
                    }

                    if (i == 0) {
                        controlDependencia = itemDependencias.get(i).getDependencia();
                    } else {
                        if (!controlDependencia.equalsIgnoreCase(itemDependencias.get(i).getDependencia())) {
                            control = false;
                            System.out.println("Dependencia");
                            break;
                        }
                    }
                }
            } else {
                control = false;
                System.out.println("Salida por comparacion");
            }
        }

        System.out.println("control " + control + "dependencias " + dependencia);

        if (control && dependencia) {
            Oferta = controlDependencia;
        }

        if (control) {

            return true;
        } else {
            // System.out.println("Error Cotizacion Invalida");
            limpiar();
            return false;
        }

    }

    public static boolean validarOfertaDigital(ArrayList<ArrayList<String>> oferta, String estratoL) {

        estrato = estratoL;

        for (int i = 0; i < oferta.size(); i++) {
            Consulta(oferta.get(i).get(0), oferta.get(i).get(1), oferta.size());
        }

        int cantidad = 0;

        String controlDependencia = "";
        itemPromocionesAdicionales.clear();

        System.out.println("exception " + exception);

        if (exception) {
            control = true;
        }

        System.out.println("control " + control);

        System.out.println("Dependencias " + dependencia);

        if (dependencia) {

            System.out.println("hola itemDependencias.sise() " + itemDependencias.size());

            if (itemDependencias.size() > 0) {
                for (int i = 0; i < oferta.size(); i++) {
                    if (oferta.get(i).get(0).equalsIgnoreCase("ADICHD")) {
                        contadorFija += ContadoOfertasDigital(oferta.get(i), oferta.size());
                    }
                }

            }

            System.out.println("contadorFija " + contadorFija + " itemDependencias.size() " + itemDependencias.size());

            if (itemDependencias.size() == contadorFija) {
                for (int i = 0; i < itemDependencias.size(); i++) {

                    if (itemDependencias.get(i).getCantidad() != contadorFija) {
                        control = false;
                        System.out.println("Salida por cantidad");
                        break;
                    }

                    if (i == 0) {
                        controlDependencia = itemDependencias.get(i).getDependencia();
                    } else {
                        if (!controlDependencia.equalsIgnoreCase(itemDependencias.get(i).getDependencia())) {
                            control = false;
                            System.out.println("Dependencia");
                            break;
                        }
                    }
                }
            } else {
                control = false;
                System.out.println("Salida por comparacion");
            }
        }

        System.out.println("control " + control + "dependencias " + dependencia);

        if (control && dependencia) {
            Oferta = controlDependencia;
        }

        if (control) {

            return true;
        } else {
            // System.out.println("Error Cotizacion Invalida");
            limpiar();
            return false;
        }

    }

    public static void Consulta(String producto, String tipo_producto, int cantidad) {
        int Individual = 0, Individual_Iva = 0, Empaquetado = 0, Empaquetado_Iva = 0;
        int Contador_Paquete = 0;
        String paquete = "";

        Contador_Paquete = cantidad;

        if (Contador_Paquete >= 1 && Contador_Paquete <= 2) {
            paquete = "Duo";
        } else if (Contador_Paquete > 2) {
            paquete = "Trio";
        }

        String depto = "";
        if (MainActivity.config.getDepartamento().equalsIgnoreCase("Distrito Capital de Bogota")) {
            depto = ciudadCliente;
        } else {
            depto = MainActivity.config.getDepartamento();
        }

        try {
            if (producto.contains("Existente")) {
                Precios("0", "0", "0", "0", "", "0", tipo_producto, "", "");
            } else {

                if (tipo_producto.equalsIgnoreCase("4G")) {
                    // System.out.println("internet " + internet);
                    /*
					 * if (internet.equalsIgnoreCase("-")) { paquete =
					 * "pq sin BA"; } else { paquete = "pq con BA"; }
					 */
                    if (producto.contains("MIFI") || (producto.contains("Modem") && producto.contains("4G"))) {
                        ;
                    } else {
                        paquete = "pq sin BA";
                    }
                }

                System.out.println(
                        "select individual, individual_iva, empaquetado, empaquetado_iva, Oferta, cantidadproductos, homoPrimeraLinea, homoSegundaLinea,homoSegundaLinea"
                                + "from Precios where departamento= " + depto + " and tipo_producto= " + tipo_producto
                                + " and Producto= " + producto + " and estrato like '%" + estrato
                                + "%' and tipo_paquete Like '%" + paquete + "%' ");

                if (!tipo_producto.equalsIgnoreCase("ADICHD")) {
                    ArrayList<String> precios = MainActivity.basedatos.consultar(false, "Precios",
                            new String[]{"individual", "individual_iva", "empaquetado", "empaquetado_iva", "Oferta",
                                    "cantidadproductos", "homoPrimeraLinea", "homoSegundaLinea", "homoSegundaLinea"},
                            "departamento=? and tipo_producto=? and Producto=? and estrato like ? and tipo_paquete Like ?",
                            new String[]{depto, tipo_producto.toLowerCase(), producto, "%" + estrato + "%",
                                    "%" + paquete + "%"},
                            null, null, null).get(0);

                    Precios(precios.get(0), precios.get(1), precios.get(2), precios.get(3), precios.get(4),
                            precios.get(5), tipo_producto, precios.get(6), precios.get(7));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Error ", "Muesta por aca " + e.getMessage());
            limpiar();
            dependencia = true;
            exception = false;
            // System.out.println("tipo producto " + tipo_producto);
        }

    }

    public static int ContadoOfertas(ProductoAMG adicional, int fija) {
        int oferta = 0;

        String Oferta = Utilidades.OfertaProductos(Utilidades.Quitar_Solo_Tildes(adicional.plan), "adic");
        System.out.println("Oferta " + Oferta);
        if (!Oferta.equalsIgnoreCase("")) {
            oferta++;
            itemDependencias.add(new ItemDependencias(Oferta, fija, "ADIC"));
        }

        return oferta;
    }

    public static int ContadoOfertasDigital(ArrayList<String> adicional, int fija) {
        int oferta = 0;

        String Oferta = Utilidades.OfertaProductos(Utilidades.Quitar_Solo_Tildes(adicional.get(0)), "adic");
        System.out.println("Oferta " + Oferta);
        if (!Oferta.equalsIgnoreCase("")) {
            oferta++;
            itemDependencias.add(new ItemDependencias(Oferta, fija, "ADIC"));
        }

        return oferta;
    }

    public static void Precios(String individual, String individual_Iva, String empaquetado, String empaquetado_Iva,
                               String dependencias, String CantidadProductos, String Tipo, String homoPrimeraLinea,
                               String homoSegundaLinea) {
        int Individual = 0, Individual_Iva = 0, Empaquetado = 0, Empaquetado_Iva = 0, Cantidad = 0;

        System.out.println("dependencias Precios " + dependencias);
        try {
            Individual = Integer.parseInt(individual);
            Individual_Iva = Integer.parseInt(individual_Iva);
            Empaquetado = Integer.parseInt(empaquetado);
            Empaquetado_Iva = Integer.parseInt(empaquetado_Iva);
            Cantidad = Integer.parseInt(CantidadProductos);
        } catch (NumberFormatException e) {

            Log.w("Error NumberFormatException", "Mensaje " + e.getMessage());
        }

        if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_to)) {

            dependenciaTO = dependencias;
            if (!dependenciaTO.equalsIgnoreCase("")) {
                dependencia = true;
            }
            cantidadTO = Cantidad;
            itemDependencias.add(new ItemDependencias(dependencias, Cantidad, "TO"));
            contadorFija++;
        }
        if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_tv)) {

            dependenciaTV = dependencias;
            if (!dependenciaTV.equalsIgnoreCase("")) {
                dependencia = true;
            }

            cantidadTV = Cantidad;
            itemDependencias.add(new ItemDependencias(dependencias, Cantidad, "TV"));
            contadorFija++;
        }
        if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_ba)) {

            dependenciaBA = dependencias;
            if (!dependenciaBA.equalsIgnoreCase("")) {
                dependencia = true;
            }
            cantidadBA = Cantidad;
            itemDependencias.add(new ItemDependencias(dependencias, Cantidad, "BA"));
            contadorFija++;
        }

    }

    public static void limpiar() {

        Contador = 0;
        Contador_productos = 0;
        contadorFija = 0;
        itemDependencias = new ArrayList<ItemDependencias>();
        cantidadTO = 0;
        cantidadTV = 0;
        cantidadBA = 0;
        dependencia = false;
        exception = true;
        control = true;
        dependenciaTO = null;
        dependenciaTV = null;
        dependenciaBA = null;
        Oferta = "";

    }

    public static String[][] agregarAdicionalesGratis(String planFactura, String[][] adicionales, String departamento,
                                                      String estrato, String promoTv, String duracionTv) {

        String[][] adicionales2 = null;

        boolean miniHD = false;
        boolean expecialHD = false;

        int nProductos = 0;

        String precioAdicional = "";
        String promocion = "";
        String duracion = "";

        itemPromocionesAdicionalesGratis.clear();

        // otrosAdicionales=

        for (int i = 0; i < adicionales.length; i++) {
            System.out.println("adicionales " + adicionales[i][0] + " - precio " + adicionales[i][1]);
        }

        System.out.println("planFactura " + planFactura);
        String adicional = Utilidades.adicionalesGratis("adicionalesGratis", planFactura);

        System.out.println("Adiciolan gratis " + adicional);

        if (adicional != null) {
            if (countValidacion2.contains("HD")) {
                String cambiar = Utilidades.adicionalesGratis("cambiar", adicional);

                if (cambiar != null && cambiar.equalsIgnoreCase("SI")) {
                    adicional = Utilidades.adicionalesGratis("cambiarAdicional", adicional);
                }
            }

            System.out.println("promoTv " + promoTv);

            if (promoTv.contains("%")) {
                String[] promo = promoTv.split("%");
                promoTv = promo[0];
            } else if (promoTv.equalsIgnoreCase("Sin Promocion")) {
                promoTv = "0";
            }

            promocion = promoTv;
            duracion = duracionTv;
        }

        // for (int i = 0; i < adicionales.length; i++) {
        // // System.out.println("adicionales " + adicionales[i][0]
        // // + " - precio " + adicionales[i][1]);
        //
        // if (adicionales[i][0].equalsIgnoreCase("Mini HD Estratos 1-2-3-4")
        // && Utilidades.excluirNacional(
        // "Mini HD Estratos 1-2-3-4 - A", planFacturaEmp)
        // && nProductos == 3) {
        // if (trioNuevo) {
        // itemPromocionesAdicionales
        // .add(new ItemPromocionesAdicionales(
        // adicionales[i][0], "100", "AD", "12 Meses"));
        // }
        //
        // miniHD = true;
        // }
        //
        // System.out.println("adicionales[i][0] " + adicionales[i][0]);
        // System.out.println("planFacturaEmp " + planFacturaEmp);
        // System.out.println("nProductos " + nProductos);
        //
        // if (adicionales[i][0].equalsIgnoreCase("HD Edicion Especial")
        // && Utilidades.excluirNacional("HD Edicion Especial - A",
        // planFacturaEmp) && nProductos == 1) {
        // itemPromocionesAdicionales.add(new ItemPromocionesAdicionales(
        // adicionales[i][0], "100", "AD", "12 Meses"));
        // expecialHD = true;
        // }
        // }

        if (adicional != null) {
            precioAdicional = UtilidadesTarificador.precioAdicional(adicional, departamento, estrato);
        }

        System.out.println("adicional " + adicional);
        System.out.println("precio " + precioAdicional);

        if (adicionales.length > 0 && adicional != null) {

            int tamano = adicionales.length + 1;
            System.out.println("tamano " + tamano);

            adicionales2 = new String[tamano][2];

            for (int i = 0; i < tamano; i++) {

                if (i == tamano - 1) {
                    adicionales2[i][0] = adicional;
                    adicionales2[i][1] = precioAdicional;

                    itemPromocionesAdicionales
                            .add(new ItemPromocionesAdicionales(adicional, promocion, "AD", duracion));
                } else {
                    adicionales2[i][0] = adicionales[i][0];
                    adicionales2[i][1] = adicionales[i][1];
                }
            }

        } else if (adicional != null) {
            adicionales2 = new String[1][2];
            adicionales2[0][0] = adicional;
            adicionales2[0][1] = precioAdicional;
            // String[][] adicionales3 = {{adicional},{"0"}};
            // adicionales2 = adicionales3;
            itemPromocionesAdicionales.add(new ItemPromocionesAdicionales(adicional, promocion, "AD", duracion));
        } else {
            adicionales2 = adicionales;
        }

        System.out.println("countValidacion2 " + countValidacion2);

        System.out.println("adicionales2 " + adicionales2.length);

        for (int i = 0; i < adicionales2.length; i++) {
            System.out.println("adicionales " + adicionales2[i][0] + " - precio " + adicionales2[i][1]);
        }

        return adicionales2;

    }

    public static String[][] agregarAdicionalesGratis(String planFactura, String planFacturaEmp, String[][] adicionales,
                                                      String Television, String planTv, String cantidaProductos, boolean trioNuevo, String promoTv,
                                                      String duracionTv, String departamento, String estrato) {

        String[][] adicionales2 = null;

        boolean miniHD = false;
        boolean expecialHD = false;

        int nProductos = 0;

        String precioAdicional = "";
        String promocion = "";
        String duracion = "";

        try {
            nProductos = Integer.parseInt(cantidaProductos);

        } catch (Exception e) {
            Log.w("Error", e.getMessage());
        }

        itemPromocionesAdicionalesGratis.clear();

        // otrosAdicionales=

        for (int i = 0; i < adicionales.length; i++) {
            System.out.println("adicionales " + adicionales[i][0] + " - precio " + adicionales[i][1]);
        }

        System.out.println("planFactura " + planFactura);
        String adicional = Utilidades.adicionalesGratis("adicionalesGratis", planFactura);

        System.out.println("Adiciolan gratis " + adicional);

        if (adicional != null) {
            if (countValidacion2.contains("HD")) {
                String cambiar = Utilidades.adicionalesGratis("cambiar", adicional);

                if (cambiar != null && cambiar.equalsIgnoreCase("SI")) {
                    adicional = Utilidades.adicionalesGratis("cambiarAdicional", adicional);
                }
            }

            System.out.println("promoTv " + promoTv);

            if (promoTv.contains("%")) {
                String[] promo = promoTv.split("%");
                promoTv = promo[0];
            } else if (promoTv.equalsIgnoreCase("Sin Promocion")) {
                promoTv = "0";
            }

            promocion = promoTv;
            duracion = duracionTv;
        }

        System.out.println("adicionales pintar " + adicionales);

        for (int i = 0; i < adicionales.length; i++) {
            // System.out.println("adicionales " + adicionales[i][0]
            // + " - precio " + adicionales[i][1]);

            System.out.println("adicionales[" + i + "] " + adicionales[i]);

            // if (adicionales[i][0].equalsIgnoreCase("Mini HD Estratos
            // 1-2-3-4")
            // && Utilidades.excluirNacional(
            // "Mini HD Estratos 1-2-3-4 - A", planFacturaEmp)
            // && nProductos == 3) {
            // if (trioNuevo) {
            // itemPromocionesAdicionales
            // .add(new ItemPromocionesAdicionales(
            // adicionales[i][0], "100", "AD", "12 Meses"));
            // }
            //
            // miniHD = true;
            // }
            //
            // System.out.println("adicionales[i][0] " + adicionales[i][0]);
            // System.out.println("planFacturaEmp " + planFacturaEmp);
            // System.out.println("nProductos " + nProductos);
            //
            // if (adicionales[i][0].equalsIgnoreCase("HD Edicion Especial")
            // && Utilidades.excluirNacional("HD Edicion Especial - A",
            // planFacturaEmp) && nProductos == 1) {
            // itemPromocionesAdicionales.add(new ItemPromocionesAdicionales(
            // adicionales[i][0], "100", "AD", "12 Meses"));
            // expecialHD = true;
            // }
        }

        System.out.println("miniHD " + miniHD);
        System.out.println("planFacturaEmp " + planFacturaEmp);
        System.out.println("nProductos " + nProductos);
        System.out.println("planTv " + planTv);

        if (Utilidades.excluirNacional("Mini HD Estratos 1-2-3-4 - B", planFacturaEmp) && nProductos == 3 && !miniHD) {
            // miniHD = false;
            if (!countValidacion2.contains("HD")) {
                if (trioNuevo) {
                    adicional = "Mini HD Estratos 1-2-3-4";
                    promocion = "100";
                    duracion = "12 Meses";
                }
            }
        } else if (Utilidades.excluirNacional("HD Edicion Especial - B", planFacturaEmp) && nProductos == 1) {
            // expecialHD = false;
            if (!countValidacion2.contains("HD")) {
                if (planTv.equalsIgnoreCase("N")) {
                    adicional = "HD Edicion Especial";
                    promocion = "100";
                    duracion = "12 Meses";
                }
            }
        }

        if (adicional != null) {
            precioAdicional = UtilidadesTarificador.precioAdicional(adicional, departamento, estrato);
        }

        System.out.println("adicional " + adicional);
        System.out.println("precio " + precioAdicional);

        if (adicionales.length > 0 && adicional != null) {

            int tamano = adicionales.length + 1;
            System.out.println("tamano " + tamano);

            adicionales2 = new String[tamano][2];

            for (int i = 0; i < tamano; i++) {

                if (i == tamano - 1) {
                    adicionales2[i][0] = adicional;
                    adicionales2[i][1] = precioAdicional;

                    itemPromocionesAdicionales
                            .add(new ItemPromocionesAdicionales(adicional, promocion, "AD", duracion));
                } else {
                    adicionales2[i][0] = adicionales[i][0];
                    adicionales2[i][1] = adicionales[i][1];
                }
            }

        } else if (adicional != null) {
            adicionales2 = new String[1][2];
            adicionales2[0][0] = adicional;
            adicionales2[0][1] = precioAdicional;
            // String[][] adicionales3 = {{adicional},{"0"}};
            // adicionales2 = adicionales3;
            itemPromocionesAdicionales.add(new ItemPromocionesAdicionales(adicional, promocion, "AD", duracion));
        } else {
            adicionales2 = adicionales;
        }

        System.out.println("countValidacion2 " + countValidacion2);

        System.out.println("adicionales2 " + adicionales2.length);

        for (int i = 0; i < adicionales2.length; i++) {
            System.out.println("adicionales " + adicionales2[i][0] + " - precio " + adicionales2[i][1]);
        }

        return adicionales2;

    }

    public boolean validarPortafolio(Cliente cliente, Context context, Observador observer) {

        boolean validarEstandarizacion = true;

        if (Utilidades.visible("portafolioATC", cliente.getCiudad())) {
            if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
                // System.out.println("cliente.isConsultaNormalizada() "
                // + cliente.isConsultaNormalizada());
                // System.out.println("cliente.isControlNormalizada() "
                // + cliente.isControlNormalizada());
                if (!cliente.isControlNormalizada()) {
                    validarEstandarizacion = false;
                    // ja.put(Utilidades.jsonMensajes("Direccion",
                    // "Debe Normalizar la Direccion"));
                } else {
                    if (cliente.getPortafolio() == null) {
                        lanzarSimulador("direccion", "Portafolio", context, observer, cliente);
                    }
                }

            }
        } else {
            validarEstandarizacion = false;
        }
        // if (Utilidades.visible("estandarizarDireccion",
        // cliente.getCiudad())) {
        // if (!cliente.isControlNormalizada()) {
        // validarEstandarizacion = false;
        // }

        return validarEstandarizacion;
    }

    private void lanzarSimulador(String tipoDato, String tipo, Context context, Observador observer, Cliente cliente) {

        Simulador simulador = new Simulador();
        simulador.setManual(context);
        simulador.addObserver(observer);
        // simulador.execute(params);

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(cliente.getTelefono());
        parametros.add(cliente.getDireccion());
        parametros.add(cliente.getCiudadFenix());
        parametros.add(cliente.getDireccion());

        if (tipo.equalsIgnoreCase("Portafolio")) {

            ArrayList<Object> params = new ArrayList<Object>();
            params.add(MainActivity.config.getCodigo());
            params.add("Portafolio");
            params.add(tipoDato);
            params.add(parametros);

            simulador.execute(params);

        }

    }

    public static ArrayList<String> aplicarDescuentos(ArrayList<ArrayList<String>> promociones, String Tipo) {

		/*
		 * System.out.println("promociones " + promociones); System.out.println(
		 * "Tipo " + Tipo);
		 */

        ArrayList<String> promocion = null;
        ArrayList<String> descuento = new ArrayList<String>();

        if (promociones != null) {
            for (int i = 0; i < promociones.size(); i++) {
                promocion = promociones.get(i);
                if (Tipo.equalsIgnoreCase(promocion.get(0)) && !promocion.get(1).equalsIgnoreCase("0")) {
                    descuento.add(promocion.get(1) + "%");
                    String Duracion;
                    if (promocion.get(2).equalsIgnoreCase("1")) {
                        Duracion = promocion.get(2) + " Mes ";
                    } else {
                        Duracion = promocion.get(2) + " Meses ";
                    }
                    descuento.add(Duracion);
                }
            }
        }

        // System.out.println("descuento " + descuento);

        if (descuento.size() == 0) {
            descuento.add("Sin Promocion");
            descuento.add("N/A");
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

    public static boolean validarTelefonoServicio(String telefono) {
        boolean validarTelefonoServicio = true;
        telefono = Utilidades.limpiarTelefono(telefono);
        if (!telefono.equals("")) {
            System.out.println("trlefono " + telefono);
            if (telefono.length() != 7) {
                validarTelefonoServicio = false;
            }
        }

        return validarTelefonoServicio;
    }

    public static String decos_pago(String adicional) {
        String decos_pago = "N/A";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?",
                new String[]{"decos_pago", adicional}, null, null, null);

        if (resultado != null) {
            decos_pago = resultado.get(0).get(0);
            // System.out.println("consulta decos_pago "+decos_pago);
        }

        return decos_pago;
    }

    public static boolean validarTrioDigital(String planTo, String telefonia, String planTv, String television,
                                             String planBa, String internet, Context context) {

        System.out.println("planTo " + planTo);
        System.out.println("planBa " + planBa);
        System.out.println("planTv " + planTv);

        int cantidadProductos = 0;

        boolean televisionDigital = false;
        boolean ncba = false;
        boolean ncto = false;

        boolean validacion = true;

        if (!telefonia.equalsIgnoreCase("-") && !planTo.equalsIgnoreCase("-")) {
            cantidadProductos++;
            if (planTo.equalsIgnoreCase("C") || planTo.equalsIgnoreCase("N")) {
                ncto = true;
            }
        }

        if (!television.equalsIgnoreCase("-") && !planTv.equalsIgnoreCase("-")) {
            cantidadProductos++;
            if (television.contains("DIGITAL")) {
                televisionDigital = true;
            }
        }

        System.out.println("televisionDigital " + televisionDigital);

        if (!internet.equalsIgnoreCase("-") && !planBa.equalsIgnoreCase("-")) {

            if (planBa.equalsIgnoreCase("C") || planBa.equalsIgnoreCase("N")) {
                ncba = true;
            }
            cantidadProductos++;
        }

        if (cantidadProductos == 3) {

            if (televisionDigital && ncto && ncba) {
                System.out.println("Lista Negra TO " + telefonia);
                if (Utilidades.excluirNacional("negraToDigital", telefonia)) {
                    validacion = false;
                    Utilidades.MensajesToast(
                            context.getResources().getString(R.string.cotizaciontvdigitaltoinvalida) + telefonia
                                    + context.getResources().getString(R.string.existeplanespecialtoparatvdigital),
                            context);

                }
            }
        }

        return validacion;
    }

    public static boolean validarCarruselProductos(String plan, String producto, Context context) {

        System.out.println("carrusel plan " + plan);
        System.out.println("carrusel producto " + producto);

        boolean validacion = true;

		if(plan != null){
			if (!plan.equalsIgnoreCase("-") && !producto.equalsIgnoreCase("-")) {
				if (plan.equalsIgnoreCase("1")) {
					validacion = false;
				}
			}
		}


        return validacion;
    }

    public static String productosCotizacionCarrusel(Cotizacion cotizacion) {

        String productosCarrusel = "";

        boolean control = false;
        if (!cotizacion.getTelefonia().equalsIgnoreCase(Utilidades.inicial_guion)
                && !cotizacion.getTelefonia().equalsIgnoreCase("")) {
            productosCarrusel = "TO";
            control = true;
        }

        if (!cotizacion.getInternet().equalsIgnoreCase(Utilidades.inicial_guion)
                && !cotizacion.getInternet().equalsIgnoreCase("")) {

            if (control) {
                productosCarrusel = productosCarrusel + ",BA";
            } else {
                productosCarrusel = "BA";
            }

            control = true;
        }

        if (!cotizacion.getTelevision().equalsIgnoreCase(Utilidades.inicial_guion)
                && !cotizacion.getTelevision().equalsIgnoreCase("")) {

            if (control) {
                productosCarrusel = productosCarrusel + ",TV";
            } else {
                productosCarrusel = "TV";
            }
        }

        return productosCarrusel;
    }

    public static boolean validarTrioDigitalNuevo(String planTo, String telefonia, String planTv, String television,
                                                  String planBa, String internet, Context context) {

        int cantidadProductos = 0;

        System.out.println("planTo " + planTo);
        System.out.println("telefonia " + telefonia);

        System.out.println("planTv " + planTv);
        System.out.println("television " + television);

        System.out.println("planBa " + planBa);
        System.out.println("internet " + internet);

        ArrayList<Boolean> nuevos = new ArrayList<Boolean>();

        boolean validacion = true;

        if (!telefonia.equalsIgnoreCase("-") && !planTo.equalsIgnoreCase("-")) {
            cantidadProductos++;
            if (planTo.equalsIgnoreCase("N")) {
                nuevos.add(true);
            } else {
                nuevos.add(false);
            }
        }

        if (!television.equalsIgnoreCase("-") && !planTv.equalsIgnoreCase("-")) {
            cantidadProductos++;
            if (Utilidades.excluirNacional("planesDigitales", television) && planTv.equalsIgnoreCase("N")) {
                nuevos.add(true);
            } else {
                nuevos.add(false);
            }
        }

        if (!internet.equalsIgnoreCase("-") && !planBa.equalsIgnoreCase("-")) {

            if (planBa.equalsIgnoreCase("N")) {
                nuevos.add(true);
            } else {
                nuevos.add(false);
            }

            cantidadProductos++;
        }

        if (cantidadProductos == 3) {

            if (!nuevos.contains(false)) {
                System.out.println("Lista Negra TO " + telefonia);
                if (Utilidades.excluirNacional("negraToDigital", telefonia)) {
                    validacion = false;
                    // Utilidades.MensajesToast("Cotizacion Invalida No Se Puede
                    // Cotizar Trio, Conteniendo Television Digital Con
                    // Telefonia "+telefonia+" , Existe Plan Especial Para Este
                    // Tipo De Cotizacion", context);
                }
            } else {
                validacion = false;
            }
        } else {
            validacion = false;
        }

        return validacion;
    }

    public static double ImpuestoTelefonico(String municipio, String departamento, String estrato) {

        double impuesto = 0;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasestratos",
                new String[]{"lst_item"},
                "lst_nombre=? and lst_departamento = ? and lst_ciudad = ? and lst_estrato = ?",
                new String[]{"impuestoTelefinico", departamento, municipio, estrato}, null, null, null);

        if (respuesta != null) {
            try {
                impuesto = Double.parseDouble(respuesta.get(0).get(0));
            } catch (Exception e) {
                impuesto = -1;
            }
        }

        return impuesto;

    }

    public static boolean validarBronze(Cotizacion cotizacion, String ciudad) {
        boolean valido = true;
        if (cotizacion.getContadorProductos().equalsIgnoreCase("3")) {
            System.out.println("<<validar bronze>> cotizacion.getTelefonia() " + cotizacion.getTelefonia());
            if (Utilidades.validarNacionalValor("toBronze", cotizacion.getTelefonia())) {
                System.out.println("<<validar bronze>> cotizacion.getTelevision() " + cotizacion.getTelevision());
                if (Utilidades.validarNacionalValor("tipoTvBronze", cotizacion.getTelevision())) {
                    System.out.println("<<validar bronze>> cotizacion.getInternet() " + cotizacion.getInternet());
                    if (Utilidades.validarNacionalValor("tipoBaBronze", cotizacion.getInternet())) {
                        System.out.println("<<validar bronze>> tipoBaBronze  ");
                    } else {
                        System.out.println("<<validar bronze>> No tipoBaBronze  ");
                        valido = false;
                    }
                } else {
                    System.out.println("<<validar bronze>> No tipotvBronze  ");
                    valido = false;
                }
            }

            System.out.println("<<validar bronze>> cotizacion.getTelevision() segunda validacion" + cotizacion.getTelevision());

            if (Utilidades.validarNacionalValor("tipoTvBronze", cotizacion.getTelevision())) {
                System.out.println("<<validar bronze>> cotizacion.getInternet() segunda validacion" + cotizacion.getInternet());
                if (Utilidades.validarNacionalValor("tipoBaBronze", cotizacion.getInternet())) {
                    System.out.println("<<validar bronze>> ciudadesToBronze " + ciudad);
                    if (Utilidades.excluir("ciudadesToBronze", ciudad)) {
                        System.out.println("<<validar bronze>> cotizacion.getTelefonia() segunda validacion" + cotizacion.getTelefonia());
                        if (Utilidades.validarNacionalValor("toValidasBronce", cotizacion.getTelefonia())) {
                            System.out.println("<<validar bronze>> toValidasBronce  ");
                        } else {
                            System.out.println("<<validar bronze>> No toValidasBronce");
                            valido = false;
                        }
                    }
                }
            }
        }

        System.out.println("<<validar bronze>> valido "+valido);

        return valido;
    }

    public static String[][] adicionalDependiente(String television) {

        String[][] adicionalDependiente = null;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave,lst_valor"}, "lst_nombre=? and lst_clave=?",
                new String[]{"dependientesMega", television}, null, null, null);

        if (respuesta != null) {

            int tamano = respuesta.size();

            adicionalDependiente = new String[tamano][2];
            for (int i = 0; i < tamano; i++) {
                adicionalDependiente[i][0] = respuesta.get(i).get(1);
            }
        }

        return adicionalDependiente;
    }

    public static boolean validarDecosMinimos(String planTV, ArrayList<ItemDecodificador> itemDecodificadors,
                                              Context context) {

        boolean valido = true;

        ArrayList<ItemKeyValue2> descosMinimo;
        ArrayList<ItemKeyValue2> descosLogica = null;

        System.out.println("valido " + valido);

        if (!planTV.equalsIgnoreCase("-")) {

            JSONObject datosDecos = UtilidadesDecos.datosValidarDecos(itemDecodificadors, 0);

            try {

                JSONObject json = new JSONObject();

                json.put("filtro", "");
                json.put("oferta", "N/A");
                json.put("nodoDigital", "N/A");
                json.put("planTV", planTV);

                String minimo = "";

                ArrayList<ArrayList<String>> infoConfigDecos = UtilidadesDecos.queryConfigDecos(json.toString());

                System.out.println("infoConfigDecos " + infoConfigDecos);

                if (infoConfigDecos != null && infoConfigDecos.size() > 0) {
                    for (int i = 0; i < infoConfigDecos.size(); i++) {
                        System.out.println("decodificadores infoConfigDecos " + infoConfigDecos.get(i));

                        System.out.println("decodificadores posi 1 " + infoConfigDecos.get(i).get(1));
                        if (infoConfigDecos.get(i).get(1).equalsIgnoreCase("minimo")) {
                            minimo = infoConfigDecos.get(i).get(2);
                        }

                    }
                }

                if (!minimo.equals("")) {

                    descosMinimo = UtilidadesDecos.partirStringDecos(minimo);

                    if (datosDecos.has("decos")) {
                        descosLogica = UtilidadesDecos.partirStringDecos(datosDecos.getString("decos"));

                    }

                    System.out.println("descosMinimo" + descosMinimo);

                    System.out.println("descosLogica" + descosLogica);

                    if (descosMinimo.size() > 0 && descosLogica.size() > 0) {
                        for (int i = 0; i < descosLogica.size(); i++) {
                            for (int j = 0; j < descosMinimo.size(); j++) {
                                System.out.println("descosLogica.get(i).getKey() " + descosLogica.get(i).getKey());
                                System.out.println("descosMinimo.get(j).getKey() " + descosMinimo.get(j).getKey());
                                if (descosLogica.get(i).getKey().equalsIgnoreCase(descosMinimo.get(j).getKey())) {
                                    System.out.println(
                                            "descosLogica.get(i).getValues()  " + descosLogica.get(i).getValues());
                                    System.out.println(
                                            "descosMinimo.get(j).getValues()  " + descosMinimo.get(j).getValues());
                                    if (descosLogica.get(i).getValues() < descosMinimo.get(j).getValues()) {
                                        valido = false;
                                    }
                                }
                            }
                        }
                    }

                }

            } catch (JSONException e) {
                Log.w("error ", e.getMessage());
                valido = false;
            }

        }

        if (!valido) {
            Utilidades.MensajesToast(context.getResources().getString(R.string.minimodecos), context);
            return false;
        }

        return valido;
    }

    public static boolean validarDependencias(String television, String[][] adicionales, Context context) {
        // return true;

        String[][] adicionalesDependientes = UtilidadesTarificador.adicionalDependiente(television);
        ArrayList<Boolean> localizado = new ArrayList<Boolean>();

        String tipoDependencia = Utilidades.claveValor("tipoDependencia", television);

        if (adicionalesDependientes != null) {
            for (int i = 0; i < adicionalesDependientes.length; i++) {

                String adicionalDepen = adicionalesDependientes[i][0];

                System.out.println("adicionalDepen " + adicionalDepen);

                if (adicionales != null) {
                    boolean localizar = false;
                    for (int j = 0; j < adicionales.length; j++) {
                        String adicional = adicionales[j][0];

                        System.out.println("adicional " + adicional);

                        if (adicional.equalsIgnoreCase(adicionalDepen)) {
                            localizar = true;
                        }
                    }

                    if (localizar) {
                        localizado.add(true);
                    } else if (tipoDependencia.equalsIgnoreCase("and")) {
                        localizado.add(false);
                    }
                }
            }

            if (localizado.size() == 0 && tipoDependencia.equalsIgnoreCase("or")) {
                localizado.add(false);
            }

            if (localizado.contains(false)) {
                Utilidades.MensajesToast("Debe seleccionar los Adicionales requeridos del plan", context);
                return false;
            }

            System.out.println("localizado " + localizado);
        }

        return true;
    }

    public static String jsonDatos(Cliente cliente, String hogarNuevo, String tecnologia, String tvAnaloga) {

        JSONObject json = null;

        json = new JSONObject();

        try {
            json.put("departamento", cliente.getDepartamento());
            json.put("ciudad", cliente.getCiudad());
            json.put("estrato", cliente.getEstrato());
            json.put("tecnologia", tecnologia);
            json.put("hogarNuevo", hogarNuevo);
            json.put("tvAnaloga", tvAnaloga);
        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }

        System.out.println("json smart promo 2.0 " + json);

        return json.toString();
    }

    public static boolean buscarPlanes(String departamento, String plan) {

        boolean localizado = false;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Productos",
                new String[]{"Producto"}, "Departamento=? and Producto=?",
                new String[]{departamento, plan}, null, null, null);


        System.out.println("buscarPlanes departamento " + departamento);
        System.out.println("buscarPlanes plan " + plan);
        System.out.println("buscarPlanes respuesta " + respuesta);

        if (respuesta != null) {

            localizado = true;
        }

        return localizado;
    }

}
