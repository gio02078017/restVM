package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.zip.CRC32;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.R.bool;
import android.R.integer;
import android.R.string;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Calendario;

public class Configuracion implements Serializable {

    public String Version, srvVersion;

    private String Departamento;
    private String Ciudad;
    private String Canal;
    private String CiudadFenix;
    private String Codigo_Giis;
    private String Identificador;

    private String codigo_asesor;
    private String documento_asesor;
    private String celular_asesor;
    private String nombre_asesor;
    private String[] departamentos_asesor;

    private Context context;

    public Configuracion(Context context) {
        // obtenerVersion();
        Departamento = "";
        Ciudad = "";
        Canal = "";
        activarWiFi();
    }

    public void precargarConfiguraciones() {
        Ciudad = MainActivity.preferencias.getString("ciudad", "--Seleccione Ciudad--");
        Departamento = MainActivity.preferencias.getString("departamento", "--Seleccione Departamento--");
        Identificador = MainActivity.preferencias.getString("indicativo", "");
        if (!MainActivity.preferencias.getBoolean("cfgCiudades", false)) {
            obtenerCiudades();
        } else {
            // System.out.println("Ciudades ");
        }
    }

    public void guardarConfiguraciones() {
        Editor editor = MainActivity.preferencias.edit();
        editor.putString("departamento", Departamento);
        editor.putString("ciudad", Ciudad);
        editor.putString("indicativo", Identificador);
        editor.commit();
    }

    public void reinicializarEstados() {
        Editor editor = MainActivity.preferencias.edit();
        editor.putBoolean("cfgGlobales", false);
        editor.putBoolean("cfgCiudades", false);
        editor.putBoolean("cfgProductos", false);
        editor.putBoolean("cfgPrecios", false);
        editor.putBoolean("cfgDescuentos", false);
        editor.putBoolean("cfgPromociones", false);
        editor.putBoolean("cfgReglas", false);
        // editor.putBoolean("cfgOtrasPromociones", false);
        editor.putBoolean("cfgAdicionales", false);
        editor.putBoolean("cfgExtensiones", false);
        editor.putBoolean("cfgBarrios", false);
        editor.putBoolean("cfgImpuestos", false);
        editor.commit();
    }

    public Configuracion(String Departamento, String Ciudad, String Codigo, String Canal) {
        reconfigurar(Departamento, Ciudad, Codigo, Canal);
    }

    public void reconfigurar(String Departamento, String Ciudad, String Codigo, String Canal) {
        this.Departamento = Departamento;
        this.Ciudad = Ciudad;
        codigo_asesor = Codigo;
        this.Canal = Canal;
    }

    public JSONObject consolidarConfiguracion() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("departamento", Departamento);
            jo.put("ciudad", Ciudad);
            jo.put("codigo", codigo_asesor);
            jo.put("canal", Canal);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jo;
    }

    public boolean autenticarBaseDatos(String codigo, String documento) {
        boolean respuesta = false;
        ArrayList<ArrayList<String>> result = MainActivity.basedatos.consultar(false, "usuario", null,
                "Codigo_Asesor=? and Documento=?", new String[]{codigo, documento}, null, null, null);

        if (result != null) {

            try {
                // System.out.println("CAprichos de Jorge => " + result.get(0));
                codigo_asesor = result.get(0).get(0);
                documento_asesor = result.get(0).get(1);
                celular_asesor = result.get(0).get(2);

                Canal = result.get(0).get(3);
                nombre_asesor = result.get(0).get(4);
                // System.out.println("nombre_asesor => " + nombre_asesor);
                departamentos_asesor = result.get(0).get(5).split(",");
                // System.out.println("Departamento antes" + Departamento);
                Departamento = new String(departamentos_asesor[0].getBytes("LATIN1"), "UTF-8");
                // System.out.println("Departamento Despues" + Departamento);
                Editor editor = MainActivity.preferencias.edit();
                editor.putString("departamento", new String(departamentos_asesor[0].getBytes("LATIN1"), "UTF-8"));
                editor.commit();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // inicializarCiudad(Departamento);
            respuesta = true;
        }
        return respuesta;
    }

    public boolean autenticarWebService(String codigo, String documento) {
        boolean respuesta = false;
        JSONArray ja;
        String dispositivo = "";
        String marca = "";
        String referencia = "";
        try {
            if (documento.equalsIgnoreCase("4321") || documento.equalsIgnoreCase("1234")) {
                dispositivo = "prueba";
            } else {
                dispositivo = MainActivity.conexion.obtenerMAC();
            }

            marca = Build.MANUFACTURER;
            referencia = Build.MODEL;

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "autenticar"});
            parametros.add(new String[]{"Tabla", "usuario"});
            parametros.add(new String[]{"Parametros",
                    "{\"codigo\":\"" + codigo + "\",\"documento\":\"'" + documento + "'\",\"dispositivo\":\"'"
                            + dispositivo + "'\",\"marca\":\"'" + marca + "'\",\"referencia\":\"'" + referencia
                            + "'\"}"});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();
            // System.out.println("Configuracion => "+data);
            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("Configuracion => "+data);
                ja = new JSONArray(data);
                ContentValues cv = new ContentValues();
                JSONObject jo = ja.getJSONObject(0);
                celular_asesor = jo.getString("Celular");
                codigo_asesor = codigo;
                documento_asesor = documento;
                nombre_asesor = jo.getString("nombre");
                Canal = jo.getString("Codigo_Canal");

                departamentos_asesor = jo.getString("Departamento").split(",");
                // System.out.println("Departamento "+Departamento);
                // System.out.println("departamentos_asesor[0]
                // "+departamentos_asesor[0]);
                if (Departamento.equals(departamentos_asesor[0])) {
                    // System.out.println("Aca");
                    Departamento = departamentos_asesor[0];
                } else {
                    // System.out.println("Alla");
                    Departamento = departamentos_asesor[0];
                    Ciudad = "--Seleccione Ciudad--";
                    Identificador = "";
                }

                cv.put("Codigo_Asesor", codigo);
                cv.put("Documento", documento);
                cv.put("Departamento", departamentos_asesor[0]);
                cv.put("Nombre_Asesor", jo.getString("nombre"));
                cv.put("Celular", celular_asesor);
                cv.put("Codigo_Canal", jo.getString("Codigo_Canal"));

                cv.put("Fecha", Calendario.getFecha());
                cv.put("Hora", Calendario.getHora());
                Editor editor = MainActivity.preferencias.edit();
                editor.putString("departamento", departamentos_asesor[0]);
                editor.commit();
                // inicializarCiudad(jo.getString("Departamento"));
                // *respuesta = MainActivity.basedatos.insertar("usuario", cv);
                respuesta = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return respuesta;
    }

    public void inicializarCiudad(String departamento) {
        // System.out.println("Departamento => " + departamento);
        if (!departamento.equalsIgnoreCase("Bogota")) {
            ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                    new String[]{"ciudad"}, "departamento = ?", new String[]{departamento}, "departamento", null,
                    null);
            if (resultado != null) {
                Ciudad = resultado.get(0).get(0);
                // System.out.println("Configuracion 198 => "+Ciudad);
            } else {
                Ciudad = null;
            }
            // System.out.println("Cliente ciudad => " + Ciudad);
            // System.out.println("Cliente departamento => " + departamento);
            Editor editor = MainActivity.preferencias.edit();
            editor.putString("ciudad", Ciudad);
            editor.commit();
        } else {
            ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Departamentos",
                    new String[]{"ciudad"}, "departamento = ?", new String[]{departamento}, "departamento", null,
                    null);
            if (resultado != null) {
                Ciudad = resultado.get(0).get(0);
                Departamento = Ciudad;
                // System.out.println("Configuracion 198 => "+Ciudad);
            } else {
                Ciudad = null;
            }
            // System.out.println("Cliente ciudad => " + Ciudad);
            // System.out.println("Cliente departamento => " + departamento);
            Editor editor = MainActivity.preferencias.edit();
            editor.putString("ciudad", Ciudad);
            editor.putString("departamento", Departamento);
            editor.commit();
        }

        consultarConfiguracionCiudad(Ciudad);

    }

    public void consultarConfiguracionCiudad(String ciudad) {
        // System.out.println("Configuracion 196 =>" + ciudad);
        String[] campos = new String[]{"CiudadFenix", "Codigo_Giis", "Identificador"};
        String[] argumentos = new String[]{ciudad};
        ArrayList<ArrayList<String>> result = MainActivity.basedatos.consultar(false, "Departamentos", campos,
                "Ciudad=?", argumentos, null, null, null);
        if (result != null) {
            // System.out.println("Cliente 202 => " + result);
            CiudadFenix = result.get(0).get(0);
            Codigo_Giis = result.get(0).get(1);
            Identificador = "(" + result.get(0).get(2) + ")";
        } else {
            CiudadFenix = null;
            Codigo_Giis = null;
            Identificador = null;
        }
        Editor editor = MainActivity.preferencias.edit();
        editor.putString("indicativo", Identificador);
        editor.commit();
    }

    public boolean validarConfiguraciones() {
        ArrayList<Boolean> estadoConfiguraciones = new ArrayList<Boolean>();
        estadoConfiguraciones.add(MainActivity.preferencias.getBoolean("cfgPrecios", false));
        estadoConfiguraciones.add(MainActivity.preferencias.getBoolean("cfgProductos", false));
        estadoConfiguraciones.add(MainActivity.preferencias.getBoolean("cfgReglas", false));
        // estadoConfiguraciones.add(MainActivity.preferencias.getBoolean(
        // "cfgPromociones", false));
        estadoConfiguraciones.add(MainActivity.preferencias.getBoolean("cfgAdicionales", false));
        estadoConfiguraciones.add(MainActivity.preferencias.getBoolean("cfgCiudades", false));
        estadoConfiguraciones.add(MainActivity.preferencias.getBoolean("cfgBarrios", false));
        estadoConfiguraciones.add(MainActivity.preferencias.getBoolean("cfgImpuestos", false));

        return !estadoConfiguraciones.contains(false);

    }

    public static boolean validarIntegridad(String data, String checksum) {
        boolean res = false;
        CRC32 crc = new CRC32();
        crc.update(data.getBytes());
        String hex = Long.toHexString(crc.getValue()).toString();
        int tamanio = Long.toHexString(crc.getValue()).toString().length();
        int diferencia = 8 - tamanio;
        String cadena = "";
        if (diferencia > 0) {
            for (int i = 0; i < diferencia; i++) {
                cadena += "0";
            }
        }
        cadena += hex;

        if (cadena.equals(checksum)) {
            res = true;
        } else {
            res = false;
        }
        /*
		 * System.out.println("Validacion Integridad => " + res + "|||" + cadena
		 * + "<=>" + checksum);
		 */
        return res;
    }

    public boolean obtenerPrecios() {
        MainActivity.basedatos.eliminar("Precios", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "tarifas"});
            parametros.add(new String[]{"Departamentos", Departamento});
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                if (Departamento.equalsIgnoreCase("Santander") || Departamento.equalsIgnoreCase("Norte de Santander")) {
                    cv.put("departamento", Departamento);
                } else {
                    cv.put("departamento", jo.getString("Departamento"));
                }
                cv.put("tipo_producto", jo.getString("Tipo_Producto"));
                cv.put("Producto", jo.getString("Producto"));
                cv.put("estrato", jo.getString("Estrato"));
                cv.put("tipo_paquete", jo.getString("Tipo_Paquete"));
                cv.put("individual", jo.getString("Individual"));
                cv.put("individual_iva", jo.getString("Individual_Iva"));
                cv.put("empaquetado", jo.getString("Empaquetado"));
                cv.put("empaquetado_iva", jo.getString("Empaquetado_Iva"));
                cv.put("ProductoHomologado", jo.getString("ProductoHomologado"));
                cv.put("Nuevo", jo.getString("Nuevo"));
                cv.put("Oferta", jo.getString("Oferta"));
                cv.put("cantidadproductos", jo.getString("Cantidad_Productos"));
                cv.put("homoPrimeraLinea", jo.getString("homoPrimeraLinea"));
                cv.put("homoSegundaLinea", jo.getString("homoSegundaLinea"));
                cv.put("valorGota", jo.getString("valorGota"));
                cv.put("valorGotaIva", jo.getString("valorGotaIva"));
                cv.put("velocidadGota", jo.getString("velocidadGota"));
                cv.put("tecnologia", jo.getString("tecnologia"));
                reg.add(MainActivity.basedatos.insertar("Precios", cv));
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgPrecios", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerReglas() {
        MainActivity.basedatos.eliminar("Reglas", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "reglas"});
            parametros.add(new String[]{"Departamentos", Departamento});
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                cv.put("regla", jo.getString("Regla"));
                if (jo.getString("EsNuevo").equalsIgnoreCase("null")) {
                    cv.put("nuevo", "");
                } else {
                    cv.put("nuevo", jo.getString("EsNuevo"));
                }
                cv.put("Tipo_Producto", jo.getString("Tipo_Producto"));
                cv.put("Plan", jo.getString("Plan"));
                cv.put("Descuento", Utilidades.limpiarDecimales(jo.getString("Descuento")));
                cv.put("Meses", jo.getString("Meses"));
                cv.put("Cantidad", jo.getString("Cantidad"));
                reg.add(MainActivity.basedatos.insertar("Reglas", cv));
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgReglas", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerCondiciones() {
        MainActivity.basedatos.eliminar("condiciones", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "condiciones"});
            parametros.add(new String[]{"Departamentos", Departamento});
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                cv.put("id", jo.getString("Condicion"));
                cv.put("clave", jo.getString("clave"));
                cv.put("valor", jo.getString("valor"));
                cv.put("tipo", jo.getString("tipo"));
                reg.add(MainActivity.basedatos.insertar("condiciones", cv));
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgCondiciones", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerCondicionesxReglas() {
        MainActivity.basedatos.eliminar("condicionesxreglas", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "condicionesxreglas"});
            parametros.add(new String[]{"Departamentos", Departamento});
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                cv.put("id_regla", jo.getString("regla"));
                cv.put("id_condicion", jo.getString("condicion"));
                cv.put("prioridad", jo.getString("prioridad"));
                cv.put("smartPromo", jo.getString("aplica_sp"));
                reg.add(MainActivity.basedatos.insertar("condicionesxreglas", cv));
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgCondicionesxReglas", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerDecos() {
        MainActivity.basedatos.eliminar("decos", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "decos"});
            parametros.add(new String[]{"Departamentos", Departamento});
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                cv.put("id_confdeco", jo.getString("id_confdeco"));
                cv.put("caracteristica", jo.getString("caracteristica"));
                cv.put("configuracion", jo.getString("configuracion"));
                reg.add(MainActivity.basedatos.insertar("decos", cv));
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgDecos", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerCondicionesxDecos() {
        MainActivity.basedatos.eliminar("condicionesxdecos", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "condicionesxdecos"});
            parametros.add(new String[]{"Departamentos", Departamento});
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                cv.put("id_condicion", jo.getString("id_condicion"));
                cv.put("id_decos", jo.getString("confdeco"));
                reg.add(MainActivity.basedatos.insertar("condicionesxdecos", cv));
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgCondicionesxDecos", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

	/*
	 * public boolean obtenerProductos() {
	 * MainActivity.basedatos.eliminar("Productos", null, null);
	 * ArrayList<Boolean> reg = new ArrayList<Boolean>(); try { JSONObject jo =
	 * new JSONObject(); ArrayList<String[]> parametros = new
	 * ArrayList<String[]>(); parametros.add(new String[] { "Tipo", "productos"
	 * }); parametros.add(new String[] { "Departamentos", Departamento });
	 * JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap(
	 * "Tarifas", parametros)); for (int i = 0; i < ja.length(); i++) {
	 * ContentValues cv = new ContentValues(); jo = ja.getJSONObject(i); //
	 * cv.put("departamento", jo.getString("Departamento")); String departamento
	 * = ""; if (!Departamento .equalsIgnoreCase("Distrito Capital de Bogota"))
	 * { if (Departamento.equalsIgnoreCase(jo .getString("Departamento"))) { //
	 * cv.put("departamento", jo.getString("Departamento")); departamento =
	 * jo.getString("Departamento"); } else { // cv.put("departamento",
	 * Departamento); departamento = Departamento; } } else {
	 * 
	 * // cv.put("departamento", jo.getString("Departamento")); departamento =
	 * jo.getString("Departamento"); }
	 * 
	 * String tipoProducto = jo.getString("Tipo_Producto"); String producto =
	 * jo.getString("Producto"); String productoHomologado =
	 * jo.getString("ProductoHomologado"); String nuevo = jo.getString("Nuevo");
	 * String oferta = jo.getString("Oferta");
	 * 
	 * /* System.out.println("Departamento " + Departamento + " " + producto); /
	 * 
	 * if (Departamento.equalsIgnoreCase("Distrito Capital de Bogota") &&
	 * producto.contains("Existente")) { // System.out.println("entro ");
	 * cv.put("departamento", "BogotaHFC"); cv.put("tipo_producto",
	 * tipoProducto); cv.put("Producto", producto); cv.put("ProductoHomologado",
	 * productoHomologado); cv.put("Nuevo", nuevo); cv.put("Oferta", oferta);
	 * reg.add(MainActivity.basedatos.insertar("Productos", cv));
	 * cv.put("departamento", "BogotaREDCO"); cv.put("tipo_producto",
	 * tipoProducto); cv.put("Producto", producto); cv.put("ProductoHomologado",
	 * productoHomologado); cv.put("Nuevo", nuevo); cv.put("Oferta", oferta);
	 * reg.add(MainActivity.basedatos.insertar("Productos", cv)); } else {
	 * cv.put("departamento", departamento); cv.put("tipo_producto",
	 * tipoProducto); cv.put("Producto", producto); cv.put("ProductoHomologado",
	 * productoHomologado); cv.put("Nuevo", nuevo); cv.put("Oferta", oferta);
	 * reg.add(MainActivity.basedatos.insertar("Productos", cv)); }
	 * 
	 * } } catch (JSONException e) { reg.add(false); // TODO Auto-generated
	 * catch block e.printStackTrace(); } Editor editor =
	 * MainActivity.preferencias.edit(); if (!reg.contains(false)) {
	 * editor.putBoolean("cfgProductos", true); editor.commit(); return true; }
	 * else { return false; } }
	 */

    public boolean obtenerProductos() {
        MainActivity.basedatos.eliminar("Productos", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "productos"});
            parametros.add(new String[]{"Departamentos", Departamento});
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                // cv.put("departamento", jo.getString("Departamento"));
                String departamento = "";
                if (!Departamento.equalsIgnoreCase("Distrito Capital De Bogota")) {
                    if (Departamento.equalsIgnoreCase(jo.getString("Departamento"))) {
                        // cv.put("departamento", jo.getString("Departamento"));
                        departamento = jo.getString("Departamento");
                    } else {
                        // cv.put("departamento", Departamento);
                        departamento = Departamento;
                    }
                } else {

                    // cv.put("departamento", jo.getString("Departamento"));
                    departamento = jo.getString("Departamento");
                }

                String tipoProducto = jo.getString("Tipo_Producto");
                String producto = jo.getString("Producto");
                String productoHomologado = jo.getString("ProductoHomologado");
                String nuevo = jo.getString("Nuevo");
                String oferta = jo.getString("Oferta");
                String fechaCarga = jo.getString("fechaCarga");
                String estrato = jo.getString("Estrato");
                String tecnologia = jo.getString("tecnologia");

                if (Departamento.equalsIgnoreCase("Distrito Capital De Bogota") && producto.contains("Existente")) {
                    System.out.println("entro ");
                    cv.put("departamento", "BogotaHFC");
                    cv.put("tipo_producto", tipoProducto);
                    cv.put("Producto", producto);
                    cv.put("ProductoHomologado", productoHomologado);
                    cv.put("Nuevo", nuevo);
                    cv.put("Oferta", oferta);
                    cv.put("FechaCarga", fechaCarga);
                    cv.put("Estrato", estrato);
                    cv.put("Tecnologia", tecnologia);

                    // jo.getString("fechaCarga");
                    reg.add(MainActivity.basedatos.insertar("Productos", cv));
                    cv.put("departamento", "BogotaREDCO");
                    cv.put("tipo_producto", tipoProducto);
                    cv.put("Producto", producto);
                    cv.put("ProductoHomologado", productoHomologado);
                    cv.put("Nuevo", nuevo);
                    cv.put("Oferta", oferta);
                    cv.put("FechaCarga", fechaCarga);
                    cv.put("Estrato", estrato);
                    cv.put("Tecnologia", tecnologia);
                    // jo.getString("fechaCarga");
                    reg.add(MainActivity.basedatos.insertar("Productos", cv));
                } else {
                    cv.put("departamento", departamento);
                    cv.put("tipo_producto", tipoProducto);
                    cv.put("Producto", producto);
                    cv.put("ProductoHomologado", productoHomologado);
                    cv.put("Nuevo", nuevo);
                    cv.put("Oferta", oferta);
                    cv.put("FechaCarga", fechaCarga);
                    cv.put("Estrato", estrato);
                    cv.put("Tecnologia", tecnologia);
                    // jo.getString("fechaCarga");
                    reg.add(MainActivity.basedatos.insertar("Productos", cv));
                }

            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgProductos", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerPromociones() {
        MainActivity.basedatos.eliminar("Promociones", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "promociones"});
            parametros.add(new String[]{"Departamentos", Departamento});
			/*
			 * if(Departamento.equalsIgnoreCase("BogotaREDCO") ||
			 * Departamento.equalsIgnoreCase("BogotaHFC")){ parametros.add(new
			 * String[] { "Departamentos", "Cundinamarca" }); }else{
			 * parametros.add(new String[] { "Departamentos", Departamento }); }
			 */
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                cv.put("departamento", jo.getString("Departamento"));
                cv.put("nombre", jo.getString("Nombre"));
                cv.put("descripcion", jo.getString("Descripcion"));
                cv.put("inicia", jo.getString("Inicia"));
                cv.put("termina", jo.getString("Termina"));
                cv.put("aplica", jo.getString("Aplica"));
                cv.put("activa ", jo.getString("Activa"));
                reg.add(MainActivity.basedatos.insertar("Promociones", cv));
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgPromociones", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean consultarSeguimiento() {
        boolean seguimiento = false;
        ArrayList<String[]> parametros = new ArrayList<String[]>();
        parametros.add(new String[]{"Accion", "consultar"});
        parametros.add(new String[]{"Tabla", "seguimiento"});
        parametros.add(new String[]{"Parametros", ""});
        try {
            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();
            // System.out.println("Configuracion sesion => " + data);
            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                JSONArray ja = new JSONArray(data);
                JSONObject jo = ja.getJSONObject(0);
                seguimiento = jo.getBoolean("Datos");
            } else {
				/*
				 * System.out .println(
				 * "La Configuracion de tiempo de sesion no fue descargada correctamente"
				 * );
				 */
            }
        } catch (JSONException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return seguimiento;
    }

    public float consultarTiempoSesion() {
        float tiempoSesion = 24;
        ArrayList<String[]> parametros = new ArrayList<String[]>();
        parametros.add(new String[]{"Accion", "consultar"});
        parametros.add(new String[]{"Tabla", "sesion"});
        parametros.add(new String[]{"Parametros", ""});
        try {
            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();
            // System.out.println("Configuracion sesion => " + data);
            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                JSONArray ja = new JSONArray(data);
                JSONObject jo = ja.getJSONObject(0);
                tiempoSesion = jo.getLong("Datos");
            } else {
				/*
				 * System.out .println(
				 * "La Configuracion de tiempo de sesion no fue descargada correctamente"
				 * );
				 */
            }
        } catch (JSONException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tiempoSesion;
    }

    public boolean obtenerAdicionales() {
        MainActivity.basedatos.eliminar("Adicionales", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        try {
            JSONObject jo = new JSONObject();
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "adicionales"});
            parametros.add(new String[]{"Departamentos", Departamento});
            JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap("Tarifas", parametros));
            for (int i = 0; i < ja.length(); i++) {
                ContentValues cv = new ContentValues();
                jo = ja.getJSONObject(i);
                if (Departamento.equalsIgnoreCase("Santander") || Departamento.equalsIgnoreCase("Norte de Santander")) {
                    cv.put("departamento", Departamento);
                } else {
                    cv.put("departamento", jo.getString("departamento"));
                }
                cv.put("tipoProducto", jo.getString("tipoProducto"));
                cv.put("producto", jo.getString("producto"));
                cv.put("adicional", jo.getString("adicional"));
                cv.put("tarifa", jo.getString("tarifa"));
                cv.put("tarifaIva", jo.getString("tarifaIva"));
                cv.put("estrato", jo.getString("estrato"));
                cv.put("oferta", jo.getString("oferta"));
                reg.add(MainActivity.basedatos.insertar("Adicionales", cv));
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgAdicionales", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerCiudades() {
        // System.out.println("obtenerCiudades");
        MainActivity.basedatos.eliminar("Departamentos", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        ArrayList<String[]> parametros = new ArrayList<String[]>();
        parametros.add(new String[]{"Accion", "consultar"});
        parametros.add(new String[]{"Tabla", "ciudades"});
        parametros.add(new String[]{"Parametros", ""});
        try {
            String res = MainActivity.conexion.ejecutarSoap("Ejecutar", parametros);
            // System.out.println("res" + res);
            if (!res.equals("004")) {
                JSONObject jop = new JSONObject(res);
                String data = jop.get("data").toString();
                if (validarIntegridad(data, jop.get("crc").toString())) {
                    data = new String(Base64.decode(data));
                    JSONArray ja = new JSONArray(data);
                    for (int i = 0; i < ja.length(); i++) {
                        ContentValues cv = new ContentValues();
                        JSONObject jo = ja.getJSONObject(i);
                        cv.put("Departamento", jo.getString("Departamento"));
                        cv.put("DepartamentoFenix", jo.getString("DepartamentoFenix"));
                        cv.put("Ciudad", jo.getString("Ciudad"));
                        cv.put("CiudadFenix", jo.getString("CiudadFenix"));
                        cv.put("Codigo_Giis", jo.getString("Codigo_Giis"));
                        cv.put("Codigo_Amc", jo.getString("Codigo_Amc"));
                        cv.put("Identificador", jo.getString("Identificador"));
                        cv.put("CodigoDaneMunicipio", jo.getString("codigoDaneMunicipio"));
                        cv.put("Estado", jo.getString("Estado"));
                        cv.put("visible", jo.getString("visible"));

                        reg.add(MainActivity.basedatos.insertar("Departamentos", cv));
                    }
                } else {
                    reg.add(false);
					/*
					 * System.out .println(
					 * "La Configuracion de ciudades no fue descargada correctamente"
					 * );
					 */
                }
            } else {
                Toast.makeText(context, "MOD-004-" + res, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            reg.add(false);
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgCiudades", true);
            editor.putString("indicativo", this.Identificador);
            editor.commit();
            return true;
        } else {
            return false;
        }

    }

    public boolean obtenerBarrios() {
        MainActivity.basedatos.eliminar("barrios", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        try {
            JSONObject param = new JSONObject();

            param.put("departamento", "'" + Departamento + "'");

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "consultar"});
            parametros.add(new String[]{"Tabla", "barrios"});
            parametros.add(new String[]{"Parametros", "{\"departamento\":\"'" + Departamento + "'\"}"});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();

            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println(data);
                JSONArray ja = new JSONArray(data);
                for (int i = 0; i < ja.length(); i++) {
                    ContentValues cv = new ContentValues();
                    JSONObject jo = ja.getJSONObject(i);
                    cv.put("id", jo.getString("id"));
                    String ciudad = jo.getString("ciudad");
                    if (ciudad.equalsIgnoreCase("BogotaREDCO") || ciudad.equalsIgnoreCase("BogotaHFC")) {
                        cv.put("ciudad", "Bogota");
                    } else {
                        cv.put("ciudad", jo.getString("ciudad"));
                    }
                    cv.put("barrio", jo.getString("barrio"));
                    cv.put("codigo", jo.getString("codigo"));
                    cv.put("zona", jo.getString("zona"));
                    cv.put("activo", jo.getString("activo"));
                    reg.add(MainActivity.basedatos.insertar("barrios", cv));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Los barrios no fueron descargados correctamente");
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgBarrios", true);
            editor.commit();
            return true;
        } else {
            return false;
        }

    }

    public boolean obtenerPermisos() {
        MainActivity.basedatos.eliminar("permisos", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        try {
            JSONObject param = new JSONObject();

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"asesor", codigo_asesor});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("obtenerPermisos", parametros));
            String data = jop.get("data").toString();

            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println(data);
                JSONArray ja = new JSONArray(data);
                for (int i = 0; i < ja.length(); i++) {
                    ContentValues cv = new ContentValues();
                    JSONObject jo = ja.getJSONObject(i);
                    cv.put("rol", jo.getString("rls_nombre"));
                    cv.put("accion", jo.getString("pms_accion"));
                    reg.add(MainActivity.basedatos.insertar("permisos", cv));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Los barrios no fueron descargados correctamente");
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfPermisos", true);
            editor.commit();
            return true;
        } else {
            return false;
        }

    }

    public boolean obtenerImpuestos() {
        MainActivity.basedatos.eliminar("impuestos", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        if (MainActivity.config.getDepartamento().equals("Antioquia")) {

            try {

                ArrayList<String[]> parametros = new ArrayList<String[]>();
                parametros.add(new String[]{"Accion", "consultar"});
                parametros.add(new String[]{"Tabla", "impuestos"});
                parametros.add(new String[]{"Parametros",
                        "{\"Departamento\":\"'" + MainActivity.config.Departamento + "'\"}"});

                JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
                String data = jop.get("data").toString();

                if (validarIntegridad(data, jop.get("crc").toString())) {
                    data = new String(Base64.decode(data));
                    // System.out.println(data);
                    JSONArray ja = new JSONArray(data);
                    for (int i = 0; i < ja.length(); i++) {
                        ContentValues cv = new ContentValues();
                        JSONObject jo = ja.getJSONObject(i);
                        cv.put("id", jo.getString("id"));
                        cv.put("departamento", jo.getString("departamento"));
                        cv.put("municipio", jo.getString("municipio"));
                        cv.put("estrato", jo.getString("estrato"));
                        cv.put("valor", jo.getString("valor"));
                        reg.add(MainActivity.basedatos.insertar("impuestos", cv));
                    }
                } else {
                    reg.add(false);
					/*
					 * System.out .println(
					 * "Los impuestos no fueron descargados correctamente" );
					 */
                }
            } catch (JSONException e) {
                reg.add(false);
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgImpuestos", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerExtensionesHFC() {
        MainActivity.basedatos.eliminar("Extensiones_HFC", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();
        ArrayList<String[]> parametros = new ArrayList<String[]>();
        parametros.add(new String[]{"Accion", "consultar"});
        parametros.add(new String[]{"Tabla", "extensioneshfc"});
        parametros.add(
                new String[]{"Parametros", "{\"departamento\":\"'" + MainActivity.config.Departamento + "'\"}"});
        try {
            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();
            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                JSONArray ja = new JSONArray(data);
                for (int i = 0; i < ja.length(); i++) {
                    ContentValues cv = new ContentValues();
                    JSONObject jo = ja.getJSONObject(i);
                    cv.put("Departamento", jo.getString("dpt_nombre"));
                    cv.put("hfc", "HFC");
                    cv.put("Producto", jo.getString("hfc_producto"));
                    reg.add(MainActivity.basedatos.insertar("Extensiones_HFC", cv));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Las extensiones HFC no fueron descargadas correctamente");
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Editor editor = MainActivity.preferencias.edit();
        if (!reg.contains(false)) {
            editor.putBoolean("cfgExtensiones", true);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerConfiguraciones() {
        // MainActivity.basedatos.eliminar("configuracion", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        Editor editor = MainActivity.preferencias.edit();
        try {

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "obtener"});
            parametros.add(new String[]{"Tabla", "listavalor"});
            parametros.add(new String[]{"Parametros",
                    "{\"municipio\":\"'" + MainActivity.config.Ciudad + "'\",\"lista\":\"'configuraciones'\"}"});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();

            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("Descarga de Configuraciones => "+data);
                JSONArray ja = new JSONArray(data);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    editor.putString(jo.getString("lvi_clave"), jo.getString("lvi_valor"));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Las Configuraciones globales no fueron descargadas correctamente"
				 * );
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!reg.contains(false)) {
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obteneListasValores() {
        MainActivity.basedatos.eliminar("listasvalores", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        Editor editor = MainActivity.preferencias.edit();
        try {
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "obtener"});
            parametros.add(new String[]{"Tabla", "listasvalores"});
            // parametros.add(new String[] { "Parametros", "{\"municipio\":\"'"+
            // MainActivity.config.Ciudad + "'\"}"});
            parametros.add(new String[]{"Parametros", "{\"municipio\":\"'" + MainActivity.config.Ciudad
                    + "'\",\"departamento\":\"'" + MainActivity.config.getDepartamento() + "'\"}"});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();

            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("Descarga Listas Generales => "+data);
                JSONArray ja = new JSONArray(data);
                ContentValues cv = new ContentValues();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    // System.out.println(jo.toString());
                    cv.put("lst_departamento", jo.getString("dpt_nombre"));
                    cv.put("lst_ciudad", jo.getString("cdd_nombre"));
                    cv.put("lst_nombre", jo.getString("lst_nombre"));
                    cv.put("lst_clave", jo.getString("lvi_clave"));
                    cv.put("lst_valor", jo.getString("lvi_valor"));
                    reg.add(MainActivity.basedatos.insertar("listasvalores", cv));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Las Listas de valores no fueron descargadas correctamente");
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!reg.contains(false)) {
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerListasEstratos() {
        MainActivity.basedatos.eliminar("listasestratos", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        Editor editor = MainActivity.preferencias.edit();
        try {

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "obtener"});
            parametros.add(new String[]{"Tabla", "listasestratos"});
            // parametros.add(new String[] { "Parametros", "{\"municipio\":\"'"+
            // MainActivity.config.Ciudad + "'\"}"});
            parametros.add(new String[]{"Parametros", "{\"municipio\":\"'" + MainActivity.config.Ciudad
                    + "'\",\"departamento\":\"'" + MainActivity.config.getDepartamento() + "'\"}"});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();

            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("Descarga Listas Generales => "+data);
                JSONArray ja = new JSONArray(data);
                ContentValues cv = new ContentValues();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    cv.put("lst_departamento", jo.getString("dpt_nombre"));
                    cv.put("lst_ciudad", jo.getString("cdd_nombre"));

                    cv.put("lst_nombre", jo.getString("lst_nombre"));
                    cv.put("lst_estrato", jo.getInt("lti_estrato"));
                    cv.put("lst_item", jo.getString("lti_item"));
                    reg.add(MainActivity.basedatos.insertar("listasestratos", cv));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Las Configuraciones globales no fueron descargadas correctamente"
				 * );
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!reg.contains(false)) {
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerListasGenerales() {
        MainActivity.basedatos.eliminar("listasgenerales", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        Editor editor = MainActivity.preferencias.edit();
        try {

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "obtener"});
            parametros.add(new String[]{"Tabla", "listasgenerales"});
            // parametros.add(new String[] { "Parametros", "{\"municipio\":\"'"+
            // MainActivity.config.Ciudad + "'\"}"});
            parametros.add(new String[]{"Parametros", "{\"municipio\":\"'" + MainActivity.config.Ciudad
                    + "'\",\"departamento\":\"'" + MainActivity.config.getDepartamento() + "'\"}"});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();

            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("Descarga Listas Generales => "+data);
                JSONArray ja = new JSONArray(data);
                ContentValues cv = new ContentValues();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    cv.put("lst_departamento", jo.getString("dpt_nombre"));
                    cv.put("lst_ciudad", jo.getString("cdd_nombre"));

                    cv.put("lst_nombre", jo.getString("lst_nombre"));
                    cv.put("lst_item", jo.getString("lgi_item"));
                    reg.add(MainActivity.basedatos.insertar("listasgenerales", cv));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Las Configuraciones globales no fueron descargadas correctamente"
				 * );
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!reg.contains(false)) {
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerPromocionesMovilidad() {
        MainActivity.basedatos.eliminar("promocionesmovilidad", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        Editor editor = MainActivity.preferencias.edit();
        try {

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "obtener"});
            parametros.add(new String[]{"Tabla", "promociones_movilidad"});
            parametros.add(new String[]{"Parametros", ""});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();

            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("Descarga Promociones Movilidad =>
                // "+data);
                JSONArray ja = new JSONArray(data);
                ContentValues cv = new ContentValues();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    cv.put("prm_planid", jo.getString("prm_planid"));
                    cv.put("prm_nombre", jo.getString("prm_nombre"));
                    cv.put("prm_homologado", jo.getString("prm_homologado"));
                    reg.add(MainActivity.basedatos.insertar("promocionesmovilidad", cv));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Las Promociones de Movilidad no fueron descargadas correctamente"
				 * );
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!reg.contains(false)) {
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public boolean obtenerGpon() {
        MainActivity.basedatos.eliminar("gpon", null, null);
        ArrayList<Boolean> reg = new ArrayList<Boolean>();

        Editor editor = MainActivity.preferencias.edit();
        try {

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "consultar"});
            parametros.add(new String[]{"Tabla", "gpon"});
            parametros.add(new String[]{"Parametros", "{\"departamento\":\"'" + Departamento + "'\"}"});

            JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
            String data = jop.get("data").toString();

            if (validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("Descarga Promociones Movilidad =>
                // "+data);
                JSONArray ja = new JSONArray(data);
                ContentValues cv = new ContentValues();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    cv.put("gpn_ciudad", jo.getString("ciudad"));
                    cv.put("gpn_inicial", jo.getString("inicial"));
                    cv.put("gpn_final", jo.getString("final"));
                    reg.add(MainActivity.basedatos.insertar("gpon", cv));
                }
            } else {
                reg.add(false);
				/*
				 * System.out .println(
				 * "Las Promociones de Movilidad no fueron descargadas correctamente"
				 * );
				 */
            }
        } catch (JSONException e) {
            reg.add(false);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!reg.contains(false)) {
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    public void activarWiFi() {
        try {
            WifiManager wifiManager = (WifiManager) MainActivity.context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        } catch (Exception e) {
            // TODO: handle exception
            // System.out.println("Problemas al habilitar el wifi");
        }

    }

    public boolean obtenerVersion() {
        boolean actualizado;
        try {
            actualizado = false;
            PackageInfo pInfo = MainActivity.context.getPackageManager()
                    .getPackageInfo(MainActivity.context.getPackageName(), 0);
            Version = pInfo.versionName;
            // System.out.println("Version => " + Version);

            srvVersion = MainActivity.preferencias.getString("version", null);

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

		/*
		 * System.out.println("version 909 => " + Version); System.out.println(
		 * "version 910 => " + srvVersion);
		 */

        if (srvVersion != null) {
            if (Version.toString().equals(srvVersion.toString())) {
                actualizado = true;
            } else {
                actualizado = false;
            }
        } else {
            actualizado = false;
        }

        return actualizado;
    }

    public boolean validarConectividad() {
        boolean conectividad = false;
        try {
            if (MainActivity.conexion.isConect(MainActivity.context)) {
                ArrayList<String[]> parametros = new ArrayList<String[]>();
                parametros.add(new String[]{"Accion", "consultar"});
                parametros.add(new String[]{"Tabla", "Conectividad"});
                parametros.add(new String[]{"Parametros", ""});

                JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
                String data = jop.get("data").toString();
                if (validarIntegridad(data, jop.get("crc").toString())) {
                    data = new String(Base64.decode(data));
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = ja.getJSONObject(0);
                    if (jo.getString("Datos").equals("conectado")) {
                        conectividad = true;
                    }
                } else {
					/*
					 * System.out .println(
					 * "No se ha podido establecer la conexion con el servidor"
					 * );
					 */
                }
            } else {

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return conectividad;
    }

    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String departamento) {
        Departamento = departamento;
    }

    public String[] getDepartamentos() {
        return departamentos_asesor;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getCodigo_asesor() {
        return codigo_asesor;
    }

    public void setCodigo_asesor(String codigo_asesor) {
        this.codigo_asesor = codigo_asesor;
    }

    public String getDocumento_asesor() {
        return documento_asesor;
    }

    public void setDocumento_asesor(String documento_asesor) {
        this.documento_asesor = documento_asesor;
    }

    public String getNombre_asesor() {
        return nombre_asesor;
    }

    public void setNombre_asesor(String nombre_asesor) {
        this.nombre_asesor = nombre_asesor;
    }

    public String getCiudadFenix() {
        consultarConfiguracionCiudad(Ciudad);
        return CiudadFenix;
    }

    public String getCodigo() {
        return codigo_asesor;
    }

    public void setCodigo(String codigo) {
        codigo_asesor = codigo;
    }

    public String getCelular_asesor() {
        return celular_asesor;
    }

    public void setCelular_asesor(String celular_asesor) {
        this.celular_asesor = celular_asesor;
    }

    public String getCanal() {
        return Canal;
    }

    public void setCanal(String canal) {
        Canal = canal;
    }

    public String getIdentificador() {
        return Identificador;
    }

    public void setIdentificador(String identificador) {
        Identificador = identificador;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public void setContext(Context context) {
        this.context = context;
    }

	/*
	 * public boolean isActualizado() { return actualizado; }
	 * 
	 * public void setActualizado(boolean actualizado) { this.actualizado =
	 * actualizado; }
	 */
}