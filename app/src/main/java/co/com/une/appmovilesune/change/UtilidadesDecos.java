package co.com.une.appmovilesune.change;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Log;
import com.google.tagmanager.TagManager;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ItemKeyValue;
import co.com.une.appmovilesune.adapters.ItemKeyValue2;
import co.com.une.appmovilesune.model.Decodificadores;

public class UtilidadesDecos {

    private static ArrayList<String> listEtiquetas =  new ArrayList<String> ();
    private static String EtiquetasDecos = null;

    public static Decodificadores consolidado (ArrayList<ItemDecodificador> decosExistentes,
                                               String planTV){
        Decodificadores decodificadores =  new Decodificadores ();

        decodificadores.setInfoConfigDecos(infoConfigDecosPorPlan(planTV));

        decodificadores.setItemDecodificadors(logicaDecos(decodificadores.getInfoConfigDecos(),decosExistentes));

        return decodificadores;
    }

    public static ArrayList<ArrayList<String>> infoConfigDecosPorPlan(String planTV) {

        JSONObject json = new JSONObject();

        try {
            json.put("filtro", "");
            json.put("oferta", "N/A");
            json.put("nodoDigital", "N/A");
            json.put("planTV", planTV);
        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        ArrayList<ArrayList<String>> infoConfigDecos = UtilidadesDecos.queryConfigDecos(json.toString());

        System.out.println("infoConfigDecosPorPlan "+infoConfigDecos);

       return infoConfigDecos;

    }

    public static String filtroDecodificador (ArrayList<ArrayList<String>> infoConfigDecos, String filtro){

        String dato = "";

        if (infoConfigDecos != null && infoConfigDecos.size() > 0) {
            for (int i = 0; i < infoConfigDecos.size(); i++) {

                if (infoConfigDecos.get(i).get(1).equalsIgnoreCase(filtro)) {
                    dato = infoConfigDecos.get(i).get(2);
                }

            }
        }

        return dato;

    }

    public static ArrayList<ItemDecodificador> logicaDecos(ArrayList<ArrayList<String>> infoConfigDecos, ArrayList<ItemDecodificador> decodificadores) {

        JSONObject json = new JSONObject();

        ArrayList<ItemDecodificador> consolidado = null;

        ArrayList<ItemDecodificador> decosExitentes = new ArrayList<ItemDecodificador>();

        if (decodificadores != null) {
            for (int i = 0; i < decodificadores.size(); i++) {
                decosExitentes.add(decodificadores.get(i).clone());
            }
        }

        String incluidos = filtroDecodificador (infoConfigDecos, "incluidos");
        String precioDecos = filtroDecodificador (infoConfigDecos, "precio");
        String incluidos_fideliza = filtroDecodificador (infoConfigDecos, "incluidos_fideliza");
        String etiquetas = filtroDecodificador (infoConfigDecos, "etiquetas");

        listEtiquetas.clear();
        setEtiquetasDecos(etiquetas);
        listEtiquetas = getEtiquetasDecos(etiquetas);

        // System.out.println("Fideliza incluidos "+incluidos);
        //
        // System.out.println("Fideliza incluidos_fideliza
        // "+incluidos_fideliza);

        if (!incluidos.equalsIgnoreCase("")) {

            consolidado = UtilidadesDecos.integrarDecodificadores(decosExitentes,
                    UtilidadesDecos.partirStringDecosIncluidos(incluidos, "AL"),
                    UtilidadesDecos.partirStringDecosIncluidos(incluidos_fideliza, "FI"), precioDecos);

            if (consolidado != null && consolidado.size() > 0) {
                return consolidado;
            }
        } else if (!incluidos_fideliza.equalsIgnoreCase("")) {

            consolidado = UtilidadesDecos.integrarDecodificadores(decosExitentes,
                    UtilidadesDecos.partirStringDecosIncluidos(incluidos, "AL"),
                    UtilidadesDecos.partirStringDecosIncluidos(incluidos_fideliza, "FI"), precioDecos);

            if (consolidado != null && consolidado.size() > 0) {
                return consolidado;
            }
        }

        return decodificadores;

    }

    public static ArrayList<ItemDecodificador> logicaDecos(ArrayList<ItemDecodificador> decodificadores,
                                                           String planTV) {

        JSONObject json = new JSONObject();

        ArrayList<ItemDecodificador> consolidado = null;

        // imprimirDecos("logicaDecos decodificadores", decodificadores);

        ArrayList<ItemDecodificador> decosExitentes = new ArrayList<ItemDecodificador>();

        if (decodificadores != null) {
            for (int i = 0; i < decodificadores.size(); i++) {
                decosExitentes.add(decodificadores.get(i).clone());
            }
        }

        // json.put("filtro", "incluidos");
        try {
            json.put("filtro", "");
            json.put("oferta", "N/A");
            json.put("nodoDigital", "N/A");
            json.put("planTV", planTV);
        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        String incluidos = "";
        String precioDecos = "";
        String incluidos_fideliza = "";
        String etiquetas = "";

        ArrayList<ArrayList<String>> infoConfigDecos = UtilidadesDecos.queryConfigDecos(json.toString());

        if (infoConfigDecos != null && infoConfigDecos.size() > 0) {
            for (int i = 0; i < infoConfigDecos.size(); i++) {
                System.out.println("decodificadores infoConfigDecos " + infoConfigDecos.get(i));

                System.out.println("decodificadores posi 1 " + infoConfigDecos.get(i).get(1));
                if (infoConfigDecos.get(i).get(1).equalsIgnoreCase("incluidos")) {
                    incluidos = infoConfigDecos.get(i).get(2);
                }

                if (infoConfigDecos.get(i).get(1).equalsIgnoreCase("precio")) {
                    precioDecos = infoConfigDecos.get(i).get(2);
                }

                if (infoConfigDecos.get(i).get(1).equalsIgnoreCase("incluidos_fideliza")) {
                    incluidos_fideliza = infoConfigDecos.get(i).get(2);
                }

                if (infoConfigDecos.get(i).get(1).equalsIgnoreCase("etiquetas")) {
                    etiquetas = infoConfigDecos.get(i).get(2);
                }
            }
        }

        listEtiquetas.clear();
        setEtiquetasDecos(etiquetas);
        listEtiquetas = getEtiquetasDecos(etiquetas);

        // System.out.println("Fideliza incluidos "+incluidos);
        //
        // System.out.println("Fideliza incluidos_fideliza
        // "+incluidos_fideliza);

        if (!incluidos.equalsIgnoreCase("")) {

            consolidado = UtilidadesDecos.integrarDecodificadores(decosExitentes,
                    UtilidadesDecos.partirStringDecosIncluidos(incluidos, "AL"),
                    UtilidadesDecos.partirStringDecosIncluidos(incluidos_fideliza, "FI"), precioDecos);

            if (consolidado != null && consolidado.size() > 0) {
                return consolidado;
            }
        } else if (!incluidos_fideliza.equalsIgnoreCase("")) {

            consolidado = UtilidadesDecos.integrarDecodificadores(decosExitentes,
                    UtilidadesDecos.partirStringDecosIncluidos(incluidos, "AL"),
                    UtilidadesDecos.partirStringDecosIncluidos(incluidos_fideliza, "FI"), precioDecos);

            if (consolidado != null && consolidado.size() > 0) {
                return consolidado;
            }
        }

        return decodificadores;

    }

    public static double consultarPrecio(String planTV, String deco) {
        JSONObject json = new JSONObject();

        // json.put("filtro", "incluidos");
        try {
            json.put("filtro", "");
            json.put("oferta", "N/A");
            json.put("nodoDigital", "N/A");
            json.put("planTV", planTV);
        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        String incluidos = "";
        String precioDecos = "";

        ArrayList<ArrayList<String>> infoConfigDecos = UtilidadesDecos.queryConfigDecos(json.toString());

        if (infoConfigDecos != null && infoConfigDecos.size() > 0) {
            for (int i = 0; i < infoConfigDecos.size(); i++) {
                System.out.println("decodificadores infoConfigDecos " + infoConfigDecos.get(i));

                System.out.println("decodificadores posi 1 " + infoConfigDecos.get(i).get(1));
                if (infoConfigDecos.get(i).get(1).equalsIgnoreCase("incluidos")) {
                    incluidos = infoConfigDecos.get(i).get(2);
                }

                if (infoConfigDecos.get(i).get(1).equalsIgnoreCase("precio")) {
                    precioDecos = infoConfigDecos.get(i).get(2);
                }
            }
        }

        if (!precioDecos.equals("")) {
            return (double) precioDecosCobro(precioDecos, deco);
        } else {
            return 0;
        }
    }

    public static ArrayList<ItemDecodificador> decodificadoresExistentes(String infoCliente) {

        // System.out.println("decodificadores infoCliente " + infoCliente);

        ArrayList<ItemDecodificador> decodificadores = new ArrayList<ItemDecodificador>();

        ItemDecodificador itemDecodificador;

        JSONObject cliente;
        try {
            cliente = new JSONObject(infoCliente);

            if (cliente.has("actualesDecos") && cliente.has("infoActualesDecos")) {

                String actualesDecos = cliente.getString("actualesDecos");
                String infoActualesDecos = cliente.getString("infoActualesDecos");

                if (!infoActualesDecos.equalsIgnoreCase("") && !infoActualesDecos.equalsIgnoreCase("NINGUNO")
                        && !infoActualesDecos.equalsIgnoreCase("N/A") && !infoActualesDecos.equalsIgnoreCase("NA")) {
                    if (infoActualesDecos.contains(",")) {

                        String[] decos = infoActualesDecos.split(",");

                        for (int i = 0; i < decos.length; i++) {

                            System.out.println("decodificadores  deco antes" + decos[i]);

                            itemDecodificador = partirStringDecos(actualesDecos, decos[i], infoActualesDecos);
                            if (itemDecodificador != null) {
                                decodificadores.add(itemDecodificador);
                            }
                        }

                    } else {
                        itemDecodificador = partirStringDecos(actualesDecos, infoActualesDecos, infoActualesDecos);
                        if (itemDecodificador != null) {
                            decodificadores.add(itemDecodificador);
                        }
                    }
                }

            }
        } catch (JSONException e) {
            Log.w("error " + e);
        }

        // imprimirDecos("Existentes ", decodificadores);

        return decodificadores;

    }

    public static ArrayList<ItemKeyValue2> partirStringDecos(String decos) {

        ArrayList<ItemKeyValue2> arrayDecos = new ArrayList<ItemKeyValue2>();

        System.out.println("NGTV decos "+decos);

        String[] datosDecos = decos.split("-");

        int decoPosicion1=0;//int sd = 0;
        int decoPosicion2=0;//int hd = 0;
        int decoPosicion3=0;//int dvr = 0;
        int decoPosicion4=0;//int ext = 0;

        for (int i = 0; i < datosDecos.length; i++) {
            switch (i) {
                case 0:
                    decoPosicion1 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    arrayDecos.add(new ItemKeyValue2(listEtiquetas.get(0), decoPosicion1));
                    break;
                case 1:
                    decoPosicion2 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    arrayDecos.add(new ItemKeyValue2(listEtiquetas.get(1), decoPosicion2));
                    break;
                case 2:
                    decoPosicion3 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    arrayDecos.add(new ItemKeyValue2(listEtiquetas.get(2), decoPosicion3));
                    break;
                case 3:
                    decoPosicion4 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    arrayDecos.add(new ItemKeyValue2(listEtiquetas.get(3), decoPosicion4));
                    break;
            }
        }

        System.out.println("NGTV arrayDecos "+arrayDecos);
        return arrayDecos;

    }

    public static ArrayList<ItemDecodificador> partirStringDecosIncluidos(String decosIncluidos, String tipoAlquiler) {

        ItemDecodificador itemDecodificador = null;

        ArrayList<ItemDecodificador> decodificadores = new ArrayList<ItemDecodificador>();

        String[] datosDecos = decosIncluidos.split("-");

        System.out.println("partirStringDecosIncluidos  "+decosIncluidos);

        for (int i = 0; i < datosDecos.length; i++) {
            System.out.println("partirStringDecosIncluidos datosDecos ["+i+"] = "+datosDecos[i]);
        }

        System.out.println("NGTV partirStringDecosIncluidos datosDecos "+datosDecos);

        int decoPosicion1=0;//int sd = 0;
        int decoPosicion2=0;//int hd = 0;
        int decoPosicion3=0;//int dvr = 0;
        int decoPosicion4=0;//int ext = 0;

        boolean fideliza = false;
        boolean fidelizaIncluido = false;
        String tipoFideliza = "N/A";

        if (tipoAlquiler.equals("FI")) {
            fideliza = true;
            tipoFideliza = "CP";
            fidelizaIncluido = true;
        }

        for (int i = 0; i < datosDecos.length; i++) {
            switch (i) {
                case 0:
                    decoPosicion1 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    break;
                case 1:
                    decoPosicion2 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    break;
                case 2:
                    decoPosicion3 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    break;
                case 3:
                    decoPosicion4 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    break;
            }
        }

        if (decoPosicion1 > 0) {
            for (int i = 0; i < decoPosicion1; i++) {
                itemDecodificador = new ItemDecodificador("0|SD-0|HD-0|Ext-0|PVR-", tipoAlquiler, fideliza,
                        tipoFideliza, listEtiquetas.get(0), "0", "Nuevo", true, true, fidelizaIncluido);
                decodificadores.add(itemDecodificador);
            }
        }

        if (decoPosicion2 > 0) {
            for (int i = 0; i < decoPosicion2; i++) {
                itemDecodificador = new ItemDecodificador("0|SD-0|HD-0|Ext-0|PVR-", tipoAlquiler, fideliza,
                        tipoFideliza, listEtiquetas.get(1), "0", "Nuevo", true, true, fidelizaIncluido);
                decodificadores.add(itemDecodificador);
            }
        }

        if (decoPosicion4 > 0) {
            for (int i = 0; i < decoPosicion4; i++) {
                itemDecodificador = new ItemDecodificador("0|SD-0|HD-0|Ext-0|PVR-", tipoAlquiler, fideliza,
                        tipoFideliza, listEtiquetas.get(3), "0", "Nuevo", true, true, fidelizaIncluido);
                decodificadores.add(itemDecodificador);
            }
        }

        if (decoPosicion3 > 0) {
            for (int i = 0; i < decoPosicion3; i++) {
                itemDecodificador = new ItemDecodificador("0|SD-0|HD-0|Ext-0|PVR-", tipoAlquiler, fideliza,
                        tipoFideliza, listEtiquetas.get(2), "0", "Nuevo", true, true, fidelizaIncluido);
                decodificadores.add(itemDecodificador);
            }
        }

        imprimirDecos("partir ", decodificadores);

        System.out.println("NGTV partirStringDecosIncluidos decodificadores "+decodificadores);

        return decodificadores;

    }

    public static ArrayList<ItemDecodificador> integrarDecodificadores(ArrayList<ItemDecodificador> decosExistente,
                                                                       ArrayList<ItemDecodificador> decosIncluidos, ArrayList<ItemDecodificador> decosIncluidosFideliza,
                                                                       String precios) {

        ArrayList<ItemDecodificador> integrar = new ArrayList<ItemDecodificador>();

        decosIncluidos = decostvNueva(decosIncluidos, decosIncluidosFideliza);

        // imprimirDecos("ver decos decosIncluidos ", decosIncluidos);
        //
        // imprimirDecos("ver decos decosExistente ", decosExistente);

        if (decosExistente == null || decosExistente.size() == 0) {

            // System.out.println("solo decos nuevos");

            return decosIncluidos;

        } else {

            for (int i = 0; i < decosExistente.size(); i++) {
                decosExistente.get(i).setProcesado(false);
            }

            for (int i = 0; i < decosIncluidos.size(); i++) {
                decosIncluidos.get(i).setProcesado(false);
            }

            // System.out.println("tiene decos existentes");
            int inicioJ = 0;

            for (int i = 0; i < decosIncluidos.size(); i++) {
                for (int j = inicioJ; j < decosExistente.size(); j++) {
                    if (decosIncluidos.get(i).getOriginal().equals(decosExistente.get(j).getOriginal())) {
                        // System.out.println("Fideliza ingresa a permanece");
                        // System.out.println("Fideliza permanece
                        // "+decosExistente.get(j).getTipoAlquiler());
                        if (!decosExistente.get(j).getTipoAlquiler().equalsIgnoreCase("FI")) {
                            // System.out.println("Fideliza pasa condicion a
                            // permanece");
                            inicioJ++;
                            integrar.add(decosExistente.get(j));
                            integrar.get((integrar.size() - 1)).setDestino("-");
                            integrar.get((integrar.size() - 1)).setTipoTransaccion("Permanece");
                            integrar.get((integrar.size() - 1)).setPrecio("0");
                            if (!decosIncluidos.get(i).isProcesado() && decosIncluidos.get(i).isFideliza()) {
                                integrar.get((integrar.size() - 1)).setFideliza(true);
                                integrar.get((integrar.size() - 1)).setTipoAlquiler("FI");
                                integrar.get((integrar.size() - 1)).setTipoFideliza("CP");
                                integrar.get((integrar.size() - 1)).setTipoTransaccion("Fideliza");
                                integrar.get((integrar.size() - 1)).setFidelizaIncluido(true);
                            }
                            decosIncluidos.get(i).setProcesado(true);
                            decosExistente.get(j).setProcesado(true);
                            break;
                        }
                    }
                }

            }

            for (int i = 0; i < decosIncluidos.size(); i++) {
                // System.out.println("Fideliza ingresa a cambio");
                if (!decosIncluidos.get(i).isProcesado()) {
                    boolean localizado = false;
                    for (int j = 0; j < decosExistente.size(); j++) {
                        if (!decosExistente.get(j).isProcesado()) {
                            if (!decosExistente.get(j).getTipoAlquiler().equalsIgnoreCase("FI")) {
                                integrar.add(decosExistente.get(j));
                                integrar.get((integrar.size() - 1)).setDestino(decosIncluidos.get(i).getOriginal());
                                integrar.get((integrar.size() - 1)).setTipoTransaccion("Cambio");
                                integrar.get((integrar.size() - 1)).setPrecio("0");
                                if (!decosIncluidos.get(i).isProcesado() && decosIncluidos.get(i).isFideliza()) {
                                    integrar.get((integrar.size() - 1)).setFideliza(true);
                                    integrar.get((integrar.size() - 1)).setTipoAlquiler("FI");
                                    integrar.get((integrar.size() - 1)).setTipoFideliza("CP");
                                    integrar.get((integrar.size() - 1)).setFidelizaIncluido(true);
                                }
                                decosIncluidos.get(i).setProcesado(true);
                                decosExistente.get(j).setProcesado(true);
                                localizado = true;
                                break;
                            }
                        }
                    }

                    if (!localizado) {
                        integrar.add(decosIncluidos.get(i));
                        integrar.get((integrar.size() - 1)).setTipoTransaccion("Nuevo");
                        integrar.get((integrar.size() - 1)).setPrecio("0");
                        decosIncluidos.get(i).setProcesado(true);
                    }
                }
            }

            for (int i = 0; i < decosExistente.size(); i++) {
                if (!decosExistente.get(i).isProcesado()) {
                    integrar.add(decosExistente.get(i));
                    if (!decosExistente.get(i).getTipoAlquiler().equalsIgnoreCase("FI")) {
                        integrar.get((integrar.size() - 1)).setTipoTransaccion("Retiro");
                        integrar.get((integrar.size() - 1))
                                .setPrecio("" + precioDecosCobro(precios, decosExistente.get(i).getOriginal()));
                    } else {
                        integrar.get((integrar.size() - 1)).setTipoTransaccion("Permanece");
                        integrar.get((integrar.size() - 1)).setDestino("-");
                        integrar.get((integrar.size() - 1)).setPrecio("0");
                        integrar.get((integrar.size() - 1)).setExistentesFideliza(true);
                    }
                    decosExistente.get(i).setProcesado(true);
                }

            }

        }

        // System.out.println("consolidar tamaño " + integrar.size());

        // imprimirDecos("Fideliza consolidar ", integrar);

        return integrar;

    }

    public static ArrayList<ItemDecodificador> decostvNueva(ArrayList<ItemDecodificador> decosIncluidos,
                                                            ArrayList<ItemDecodificador> decosIncluidosFideliza) {

        ArrayList<ItemDecodificador> incluidos = new ArrayList<ItemDecodificador>();

        incluidos.addAll((Collection<? extends ItemDecodificador>) decosIncluidos.clone());
        incluidos.addAll((Collection<? extends ItemDecodificador>) decosIncluidosFideliza.clone());

        // imprimirDecos("clone decosIncluidosFideliza ", incluidos);

        return incluidos;

    }

    public static ItemDecodificador partirStringDecos(String decosActuales, String infoDecos,
                                                      String infoActualesDecos) {

        ItemDecodificador itemDecodificador = null;

        String tipo = "";
        String productoId = "";
        String marca = "";
        String serial = "";
        String referencia = "";
        String tipoAlquiler = "";
        String tipoFideliza = "";
        boolean fideliza = false;
        String descuento = "";
        String tiempo = "";
        String planPromocional = "";
        String equipoId = "";
        String destino = "";
        String original = "";
        String precio = "";
        String capacidadReal = "";

        String[] datosDecos = infoDecos.split(Pattern.quote("|"));

        for (int i = 0; i < datosDecos.length; i++) {

            switch (i) {
                case 0:
                    original = datosDecos[i];
                    break;
                case 1:
                    equipoId = datosDecos[i];
                    break;
                case 2:
                    productoId = datosDecos[i];
                    break;
                case 3:
                    tipo = datosDecos[i];
                    break;
                case 4:
                    marca = datosDecos[i];
                    break;
                case 5:
                    serial = datosDecos[i];
                    break;
                case 6:
                    referencia = datosDecos[i];
                    break;
                case 7:
                    capacidadReal = datosDecos[i];
                    break;
                case 8:
                    tipoAlquiler = datosDecos[i];

                    if (tipoAlquiler.equalsIgnoreCase("FI")) {
                        fideliza = true;
                    }
                    break;
                case 9:
                    tipoFideliza = datosDecos[i];
                    break;
                default:
                    break;
            }
        }

        if (datosDecos.length > 0) {
            itemDecodificador = new ItemDecodificador(tipo, productoId, marca, serial, referencia, tipoAlquiler,
                    fideliza, tipoFideliza, capacidadReal, descuento, tiempo, planPromocional, equipoId, destino,
                    original, precio, decosActuales, infoActualesDecos, false);
        }

        return itemDecodificador;

    }

    public static void imprimirDecos(String preFijo, ArrayList<ItemDecodificador> decodificadores) {
        if (MainActivity.debug) {
            if (decodificadores != null) {
                for (int i = 0; i < decodificadores.size(); i++) {

                    System.out.println(preFijo + " (" + i + ") " + " Inicio ");
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores tipo "
                            + decodificadores.get(i).getTipoEquipo());
                    System.out.println(
                            preFijo + " (" + i + ") " + " decodificadores marca " + decodificadores.get(i).getMarca());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores productoId "
                            + decodificadores.get(i).getProductoId());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores serial "
                            + decodificadores.get(i).getSerial());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores referencia "
                            + decodificadores.get(i).getReferencia());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores tipoAlquiler "
                            + decodificadores.get(i).getTipoAlquiler());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores fideliza "
                            + decodificadores.get(i).isFideliza());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores tipoFideliza "
                            + decodificadores.get(i).getTipoFideliza());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores capacidadReal "
                            + decodificadores.get(i).getCapacidadReal());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores descuento "
                            + decodificadores.get(i).getDescuento());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores tiempo "
                            + decodificadores.get(i).getTiempo());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores planPromocional "
                            + decodificadores.get(i).getPlanPromocional());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores equipoId "
                            + decodificadores.get(i).getEquipoId());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores original "
                            + decodificadores.get(i).getOriginal());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores destino "
                            + decodificadores.get(i).getDestino());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores precio "
                            + decodificadores.get(i).getPrecio());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores decosActuales "
                            + decodificadores.get(i).getDecosActuales());
                    System.out.println(preFijo + " (" + i + ") " + " decodificadores infoDecosActuales "
                            + decodificadores.get(i).getInfoActualesDecos());

                    System.out.println(
                            preFijo + " (" + i + ") " + " decodificadores nuevo " + decodificadores.get(i).isNuevo());

                    System.out.println(preFijo + " (" + i + ") " + " decodificadores tipoTransacion "
                            + decodificadores.get(i).getTipoTransaccion());

                    System.out.println(preFijo + " (" + i + ") " + " decodificadores Procesado "
                            + decodificadores.get(i).isProcesado());

                    System.out.println(preFijo + " (" + i + ") " + " decodificadores Existentes Fideliza "
                            + decodificadores.get(i).isExistentesFideliza());

                    System.out.println(preFijo + " (" + i + ") " + " Fin ");
                }
            }else{
                System.out.println(preFijo + "lista null");
            }
        }
    }

    public static int precioDecosCobro(String precios, String deco) {

        int precioDeco = -1;

        String[] datosDecos = precios.split("-");

        int decoPosicion1=0;//int sd = 0;
        int decoPosicion2=0;//int hd = 0;
        int decoPosicion3=0;//int dvr = 0;
        int decoPosicion4=0;//int ext = 0;

        for (int i = 0; i < datosDecos.length; i++) {
            switch (i) {
                case 0:
                    decoPosicion1 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    break;
                case 1:
                    decoPosicion2 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    break;
                case 2:
                    decoPosicion3 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    break;
                case 3:
                    decoPosicion4 = Utilidades.convertirNumericos(datosDecos[i], datosDecos[i]);
                    break;
            }
        }

        if (deco.equalsIgnoreCase(listEtiquetas.get(0))) {
            return decoPosicion1;
        } else if (deco.equalsIgnoreCase(listEtiquetas.get(1))) {
            return decoPosicion2;
        } else if (deco.equalsIgnoreCase(listEtiquetas.get(2))) {
            return decoPosicion3;
        }else if (deco.equalsIgnoreCase(listEtiquetas.get(3))) {
            return decoPosicion4;
        }

        return precioDeco;

    }

    public static ArrayList<ArrayList<String>> queryConfigDecos(String jsonDatos) {

        String oferta = "N/A";
        String nodoDigital = "N/A";
        String planTV = "N/A";
        String filtro = "";

        try {
            JSONObject jsonObject = new JSONObject(jsonDatos);

            if (jsonObject.has("oferta")) {
                oferta = jsonObject.getString("oferta");
            }

            if (jsonObject.has("nodoDigital")) {
                nodoDigital = jsonObject.getString("nodoDigital");
            }

            if (jsonObject.has("planTV")) {
                planTV = jsonObject.getString("planTV");
                if(planTV.contains("_")){
                    planTV = planTV.replaceAll("_","|_");
                }
            }

            if (jsonObject.has("filtro")) {
                filtro = jsonObject.getString("filtro");

                if (!filtro.equalsIgnoreCase("")) {
                    filtro = "and dec.caracteristica = '" + filtro + "'";
                }
            }

        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        String query = "select dec.id_confdeco,dec.caracteristica,dec.configuracion from decos dec "
                + "inner join condicionesxdecos  cxd on cxd.id_decos = dec.id_confdeco "
                + "inner join condiciones con on  con.id = cxd.id_condicion " + "where cxd.id_condicion in ("
                + "SELECT DISTINCT(c1.id) FROM condiciones c1 " + "INNER JOIN condiciones c2 ON c1.id=c2.id "
                + "INNER JOIN condiciones c3 ON c1.id=c3.id " + "WHERE c1.clave='Oferta' AND c1.valor = '" + oferta
                + "' " + "AND c2.clave='NodoDigital' AND c2.valor = '" + nodoDigital + "' "
                + "AND c3.Clave='PlanTV' AND c3.valor like '%" + planTV + "%' ESCAPE '|') " + filtro
                + " group by dec.id_confdeco,dec.caracteristica,dec.configuracion";

		//System.out.println("decodificadores queryConfigDecos " + query);

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar2(query);

        //System.out.println("decodificadores respuesta queryConfigDecos" + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        return respuesta;
    }

    public static JSONObject datosValidarDecos(Decodificadores decodificador) {

        int decoPosicion1=0;//int sd = 0;
        int decoPosicion2=0;//int hd = 0;
        int decoPosicion3=0;//int ext = 0;
        int decoPosicion4=0;//int dvr = 0;
        int fideliza = 0;
        int decoPosicion1Fi=0;//int sdFi = 0;
        int decoPosicion2Fi=0;//int hdFi = 0;
        int decoPosicion3Fi=0;//int dvr = 0;
        int decoPosicion4Fi=0;//int dvrFi = 0;
        int totalDecos = 0;
        int totalTelevisores = 0;

        JSONObject datosValidarDecos = new JSONObject();

        ArrayList<ItemDecodificador> itemDecodificadors = decodificador.getItemDecodificadors();
        ArrayList<String> listEtiquetas = UtilidadesDecos.getEtiquetasDecos(UtilidadesDecos.filtroDecodificador(decodificador.getInfoConfigDecos(),"etiquetas"));

        System.out.println("datosValidarDecos + listEtiquetas "+listEtiquetas);

        if (itemDecodificadors != null && itemDecodificadors.size() > 0) {

            for (int i = 0; i < itemDecodificadors.size(); i++) {

                if (itemDecodificadors.get(i).getDestino() != null
                        && !itemDecodificadors.get(i).getDestino().equalsIgnoreCase("-")) {
                    if (itemDecodificadors.get(i).getDestino().equalsIgnoreCase(listEtiquetas.get(0))
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        decoPosicion1++;
                        totalDecos++;
                    } else if (itemDecodificadors.get(i).getDestino().equalsIgnoreCase(listEtiquetas.get(1))
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        decoPosicion2++;
                        totalDecos++;
                    } else if (itemDecodificadors.get(i).getDestino().equalsIgnoreCase(listEtiquetas.get(2))
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        decoPosicion3++;
                        totalDecos++;
                    }else if (itemDecodificadors.get(i).getDestino().equalsIgnoreCase(listEtiquetas.get(3))
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        decoPosicion4++;
                        totalDecos++;
                    }
                } else if (itemDecodificadors.get(i).getOriginal() != null) {
                    if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase(listEtiquetas.get(0))
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        decoPosicion1++;
                        totalDecos++;
                    } else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase(listEtiquetas.get(1))
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        decoPosicion2++;
                        totalDecos++;
                    } else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase(listEtiquetas.get(2))
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        decoPosicion3++;
                        totalDecos++;
                    }else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase(listEtiquetas.get(3))
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        decoPosicion4++;
                        totalDecos++;
                    }
                }

                if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase(listEtiquetas.get(0))
                        && itemDecodificadors.get(i).isFideliza()) {
                    decoPosicion1Fi++;
                    fideliza++;
                } else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase(listEtiquetas.get(1))
                        && itemDecodificadors.get(i).isFideliza()) {
                    decoPosicion2Fi++;
                    fideliza++;
                } else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase(listEtiquetas.get(2))
                        && itemDecodificadors.get(i).isFideliza()) {
                    decoPosicion3Fi++;
                    fideliza++;
                }else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase(listEtiquetas.get(3))
                        && itemDecodificadors.get(i).isFideliza()) {
                    decoPosicion4Fi++;
                    fideliza++;
                }

            }

        }

        totalTelevisores = totalDecos;

        try {
            datosValidarDecos.put("decos", decoPosicion1 + "-" + decoPosicion2 + "-" + decoPosicion3 + "-" + decoPosicion4);
            datosValidarDecos.put("decosFideliza", decoPosicion1Fi + "-" + decoPosicion2Fi + "-" + decoPosicion3Fi + "-" + decoPosicion4Fi);
            datosValidarDecos.put("totalFideliza", fideliza);
            datosValidarDecos.put("totalDecos", totalDecos);
            datosValidarDecos.put("totalTelevisores", totalTelevisores);
            datosValidarDecos.put("decoPosicion1", decoPosicion1);
            datosValidarDecos.put("decoPosicion2", decoPosicion2);
            datosValidarDecos.put("decoPosicion3", decoPosicion3);
            datosValidarDecos.put("decoPosicion4", decoPosicion4);
            datosValidarDecos.put("etiquetas", UtilidadesDecos.filtroDecodificador(decodificador.getInfoConfigDecos(),"etiquetas"));
        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        System.out.println("estructura " + datosValidarDecos);

        return datosValidarDecos;
    }


    public static JSONObject datosValidarDecos(ArrayList<ItemDecodificador> itemDecodificadors, int ext) {

        int sd = 0;
        int hd = 0;
        int dvr = 0;
        int fideliza = 0;
        int sdFi = 0;
        int hdFi = 0;
        int dvrFi = 0;
        int totalDecos = 0;
        int totalTelevisores = 0;

        JSONObject datosValidarDecos = new JSONObject();

        if (itemDecodificadors != null && itemDecodificadors.size() > 0) {

            for (int i = 0; i < itemDecodificadors.size(); i++) {

                if (itemDecodificadors.get(i).getDestino() != null
                        && !itemDecodificadors.get(i).getDestino().equalsIgnoreCase("-")) {
                    if (itemDecodificadors.get(i).getDestino().equalsIgnoreCase("SD")
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        sd++;
                        totalDecos++;
                    } else if (itemDecodificadors.get(i).getDestino().equalsIgnoreCase("HD")
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        hd++;
                        totalDecos++;
                    } else if (itemDecodificadors.get(i).getDestino().equalsIgnoreCase("DVR")
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        dvr++;
                        totalDecos++;
                    }
                } else if (itemDecodificadors.get(i).getOriginal() != null) {
                    if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase("SD")
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        sd++;
                        totalDecos++;
                    } else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase("HD")
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        hd++;
                        totalDecos++;
                    } else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase("DVR")
                            && !itemDecodificadors.get(i).getTipoTransaccion().equalsIgnoreCase("Retiro")) {
                        dvr++;
                        totalDecos++;
                    }
                }

                if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase("SD")
                        && itemDecodificadors.get(i).isFideliza()) {
                    sdFi++;
                    fideliza++;
                } else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase("HD")
                        && itemDecodificadors.get(i).isFideliza()) {
                    hdFi++;
                    fideliza++;
                } else if (itemDecodificadors.get(i).getOriginal().equalsIgnoreCase("DVR")
                        && itemDecodificadors.get(i).isFideliza()) {
                    dvrFi++;
                    fideliza++;
                }

            }

        }

        totalTelevisores = ext + totalDecos;

        try {
            datosValidarDecos.put("decos", sd + "-" + hd + "-" + ext + "-" + dvr);
            datosValidarDecos.put("decosFideliza", sdFi + "-" + hdFi + "-" + ext + "-" + dvrFi);
            datosValidarDecos.put("totalFideliza", fideliza);
            datosValidarDecos.put("totalDecos", totalDecos);
            datosValidarDecos.put("totalTelevisores", totalTelevisores);
            datosValidarDecos.put("decoPosicion1", sd);
            datosValidarDecos.put("decoPosicion2", hd);
            datosValidarDecos.put("decoPosicion3", ext);
            datosValidarDecos.put("decoPosicion4", dvr);
            datosValidarDecos.put("etiquetas", "SD-HD-EXT-DVR");
            /*datosValidarDecos.put("decos_sd", sd);
            datosValidarDecos.put("decos_hd", hd);
            datosValidarDecos.put("decos_pvr", dvr);
            datosValidarDecos.put("ext", ext);*/
        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        System.out.println("estructura " + datosValidarDecos);

        return datosValidarDecos;
    }

    public static boolean validarDecos(JSONObject datosDecos, String planTV) {

        JSONObject jsonEnvio = new JSONObject();

        try {

            jsonEnvio.put("oferta", "N/A");
            jsonEnvio.put("nodoDigital", "N/A");
            jsonEnvio.put("planTV", planTV);
            jsonEnvio.put("parametro", datosDecos.getString("decos"));
            jsonEnvio.put("general", true);

            if (!valiConfigDecos(jsonEnvio.toString())) {
                System.out.println("validarDecos general no cumple");
                return false;
            }

            jsonEnvio.remove("general");
            jsonEnvio.put("filtro", "prohibidos");

            if (!valiConfigDecos(jsonEnvio.toString())) {
                System.out.println("validarDecos prohibidos no cumple");
                return false;
            }

            jsonEnvio.put("filtro", "maximo_fideliza");
            jsonEnvio.put("parametro", datosDecos.getString("decosFideliza"));

            if (!valiConfigDecos(jsonEnvio.toString())) {
                System.out.println("validarDecos maximo_fideliza no cumple");
                return false;
            }

            jsonEnvio.put("filtro", "total_fideliza");
            jsonEnvio.put("parametro", datosDecos.getString("totalFideliza"));

            if (!valiConfigDecos(jsonEnvio.toString())) {
                System.out.println("validarDecos total_fideliza no cumple");
                return false;
            }

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        return true;
    }

    public static boolean validarDecosMinimos(JSONObject datosDecos, String planTV) {

        JSONObject jsonEnvio = new JSONObject();

        try {

            jsonEnvio.put("oferta", "N/A");
            jsonEnvio.put("nodoDigital", "N/A");
            jsonEnvio.put("planTV", planTV);
            jsonEnvio.put("filtro", "minimo");
            jsonEnvio.put("parametro", "");

            if (!valiConfigDecos(jsonEnvio.toString())) {
                return false;
            }

        } catch (JSONException e) {
            Log.w("error " + e.getMessage());
        }

        return true;
    }

    public static boolean valiConfigDecos(String jsonDatos) {

        String oferta = "N/A";
        String nodoDigital = "N/A";
        String planTV = "N/A";
        String filtro = "";
        String parametro = "";
        boolean general = false;

        try {
            JSONObject jsonObject = new JSONObject(jsonDatos);

            System.out.println("jsonObject " + jsonObject);

            if (jsonObject.has("oferta")) {
                oferta = jsonObject.getString("oferta");
            }

            if (jsonObject.has("nodoDigital")) {
                nodoDigital = jsonObject.getString("nodoDigital");
            }

            if (jsonObject.has("planTV")) {
                planTV = jsonObject.getString("planTV");
                if(planTV.contains("_")){
                    planTV = planTV.replaceAll("_","|_");
                }
            }

            if (jsonObject.has("filtro")) {
                filtro = jsonObject.getString("filtro");

                if (!filtro.equalsIgnoreCase("")) {
                    filtro = " and dec.caracteristica = '" + filtro + "'";
                }
            }

            if (jsonObject.has("parametro")) {
                parametro = jsonObject.getString("parametro");

                if (!parametro.equalsIgnoreCase("")) {
                    parametro = " and dec.configuracion like '%" + parametro + "%'";
                }
            }

            if (jsonObject.has("general")) {
                general = true;
            }

        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        String query = "";

        if (general) {
            query = "select * from decos dec " + "where dec.id_confdeco = 0 " + parametro;
        } else {
            query = "select dec.id_confdeco,dec.caracteristica,dec.configuracion from decos dec "
                    + "inner join condicionesxdecos  cxd on cxd.id_decos = dec.id_confdeco "
                    + "inner join condiciones con on  con.id = cxd.id_condicion " + "where cxd.id_condicion in ("
                    + "SELECT DISTINCT(c1.id) FROM condiciones c1 " + "INNER JOIN condiciones c2 ON c1.id=c2.id "
                    + "INNER JOIN condiciones c3 ON c1.id=c3.id " + "WHERE c1.clave='Oferta' AND c1.valor = '" + oferta
                    + "' " + "AND c2.clave='NodoDigital' AND c2.valor = '" + nodoDigital + "' "
                    + "AND c3.Clave='PlanTV' AND c3.valor like '%" + planTV + "%' ESCAPE '|') " + filtro + parametro
                    + " group by dec.id_confdeco,dec.caracteristica,dec.configuracion";
        }

        System.out.println("decodificadores query + parametro [" +parametro+"] "  + query);

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar2(query);

        System.out.println("decodificadores respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            return false;
        }

        return true;
    }

    public static boolean valiConfigDecosMinimo(String jsonDatos) {

        String oferta = "N/A";
        String nodoDigital = "N/A";
        String planTV = "N/A";
        String filtro = "";
        String parametro = "";
        boolean general = false;

        try {
            JSONObject jsonObject = new JSONObject(jsonDatos);

            System.out.println("jsonObject " + jsonObject);

            if (jsonObject.has("oferta")) {
                oferta = jsonObject.getString("oferta");
            }

            if (jsonObject.has("nodoDigital")) {
                nodoDigital = jsonObject.getString("nodoDigital");
            }

            if (jsonObject.has("planTV")) {
                planTV = jsonObject.getString("planTV");
                if(planTV.contains("_")){
                    planTV = planTV.replaceAll("_","|_");
                }
            }

            if (jsonObject.has("filtro")) {
                filtro = jsonObject.getString("filtro");

                if (!filtro.equalsIgnoreCase("")) {
                    filtro = " and dec.caracteristica = '" + filtro + "'";
                }
            }

            if (jsonObject.has("parametro")) {
                parametro = jsonObject.getString("parametro");

                if (!parametro.equalsIgnoreCase("")) {
                    parametro = " and dec.configuracion like '%" + parametro + "%'";
                }
            }

            if (jsonObject.has("general")) {
                general = true;
            }

        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        String query = "";

        if (general) {
            query = "select * from decos dec " + "where dec.id_confdeco = 0 " + parametro;
        } else {
            query = "select dec.id_confdeco,dec.caracteristica,dec.configuracion from decos dec "
                    + "inner join condicionesxdecos  cxd on cxd.id_decos = dec.id_confdeco "
                    + "inner join condiciones con on  con.id = cxd.id_condicion " + "where cxd.id_condicion in ("
                    + "SELECT DISTINCT(c1.id) FROM condiciones c1 " + "INNER JOIN condiciones c2 ON c1.id=c2.id "
                    + "INNER JOIN condiciones c3 ON c1.id=c3.id " + "WHERE c1.clave='Oferta' AND c1.valor = '" + oferta
                    + "' " + "AND c2.clave='NodoDigital' AND c2.valor = '" + nodoDigital + "' "
                    + "AND c3.Clave='PlanTV' AND c3.valor like '%" + planTV + "%' ESCAPE '|') " + filtro + parametro
                    + " group by dec.id_confdeco,dec.caracteristica,dec.configuracion";
        }

        // System.out.println("decodificadores query minimo " + query);

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar2(query);

        // System.out.println("decodificadores respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            return false;
        }

        return true;
    }

    public static ArrayList<String> setEtiquetasDecos(String etiquetas){

        System.out.println("decos etiquetas "+etiquetas);
        ArrayList<String> listEtiquetas = new ArrayList<String>();
        listEtiquetas.clear();

        String [] vector = etiquetas.split("-");

        for (int i = 0; i < vector.length ; i++) {
            listEtiquetas.add(vector[i]);
        }

        return listEtiquetas;

    }

    public static ArrayList<String> getEtiquetasDecos(String etiquetas){

        System.out.println("decos etiquetas "+etiquetas);
        ArrayList<String> listEtiquetas = new ArrayList<String>();
        listEtiquetas.clear();

        String [] vector = etiquetas.split("-");

        for (int i = 0; i < vector.length ; i++) {
            listEtiquetas.add(vector[i]);
        }

        return listEtiquetas;

    }


}
