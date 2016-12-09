package co.com.une.appmovilesune.threads;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Conexion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.util.Log;

//clase encargada de enviar datos almacenados cada Hora
public class Enviar {

    int i = 0;

    private String Codigo, Canal;
    private Context contexto;

    LocationManager locationManager;

    Hilo_Enviar hilo_enviar = new Hilo_Enviar();

    // contructor de la clase
    public Enviar(Context contexto) {
        this.contexto = contexto;
        // usdbh = new DBSQLiteHelper(contexto, "DBAPPMoviles", null, 1, null);
        // Se Inicia el Hilo
        // System.out.println("enviar");
        hilo_enviar.start();

        // se crea el objeto enviar almacenado
        // enviar_almacenado = new Enviar_Almacenado(contexto);
    }

    // Metodo encargado de Terminal el hilo
    public void Terminar_Hilo() {
        hilo_enviar.setTerminar(true);
    }

    // se realiza una consulta del usuario de acuerdo al codigo del asesor
    public void Consultar() {
        Codigo = "";
        Canal = "";
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "usuario",
                new String[]{"Codigo_Asesor,Codigo_Canal"}, null, null, null, null, null);
        if (respuesta != null) {
            Codigo = respuesta.get(0).get(0);
            Canal = respuesta.get(0).get(1);
            // System.out.println("Codigo " + Codigo + " Canal " + Canal);
        }

    }

    // metodo encargado de enviar los datos
    public void Enviar() {
        // System.out.println("Metodo Enviar ");

        if (!registroTarificador().equalsIgnoreCase("[]") && !registroTarificador().equalsIgnoreCase("{}")) {

            ArrayList<String[]> parametrosrt = new ArrayList<String[]>();
            parametrosrt.add(new String[]{"Accion", "registro"});
            parametrosrt.add(new String[]{"Tabla", "tarificador"});
            parametrosrt.add(new String[]{"Parametros", registroTarificador()});

            // System.out.println("registroTarificador " +
            // registroTarificador());

			/*
             * System.out.println("tarificador " +
			 * MainActivity.conexion.ejecutarSoap("Servicios", parametrosrt));
			 */

            // borrarServicios("tarificador",
            // MainActivity.conexion.ejecutarSoap("Servicios", parametrosrt));

        }

        if (!GPS().equalsIgnoreCase("[]") && !GPS().equalsIgnoreCase("{}")) {

            ArrayList<String[]> parametrosrg = new ArrayList<String[]>();
            parametrosrg.add(new String[]{"Accion", "registro"});
            parametrosrg.add(new String[]{"Tabla", "gps"});
            parametrosrg.add(new String[]{"Parametros", GPS()});

            // System.out.println("gps " + GPS());

			/*
			 * System.out.println("Resultado GPS localizacion " +
			 * MainActivity.conexion.ejecutarSoap("Servicios", parametrosrg));
			 */

            // borrarServicios("localizacion",
            // MainActivity.conexion.ejecutarSoap("Servicios", parametrosrg));
        }

        if (!controlGPS().equalsIgnoreCase("[]") && !controlGPS().equalsIgnoreCase("{}")) {

            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Accion", "control"});
            parametros.add(new String[]{"Tabla", "gps"});
            parametros.add(new String[]{"Parametros", controlGPS()});

            // System.out.println("Control gps " + controlGPS());

			/*
			 * System.out.println("Resultado GPS localizacion " +
			 * MainActivity.conexion.ejecutarSoap("Servicios", parametros));
			 */

            // borrarServicios("controlGPS",
            // MainActivity.conexion.ejecutarSoap("Servicios", parametros));
        }

        // System.out.println("registroTarificador() "+registroTarificador());
        // System.out.println("controlGPS() "+controlGPS());
        // System.out.println("GPS() "+GPS());
        // Consultar();
    }

    private void borrarServicios(String Servicio, String respuesta) {

        try {
            JSONArray ja = new JSONArray(respuesta);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = new JSONObject(ja.get(i).toString());

                if (Servicio.equals("controlGPS")) {
                    MainActivity.basedatos.eliminar("Control_GPS", "Id = ?",
                            new String[]{jo.get("identificador").toString()});
                } else if (Servicio.equals("localizacion")) {
                    MainActivity.basedatos.eliminar("GPS", "Id = ?",
                            new String[]{jo.get("identificador").toString()});
                } else if (Servicio.equals("tarificador")) {
                    MainActivity.basedatos.eliminar("Registro_Tarificador", "Id = ?",
                            new String[]{jo.get("identificador").toString()});
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String registroTarificador() {

        JSONArray array = new JSONArray();
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Registro_Tarificador",
                new String[]{"*"}, null, null, null, null, null);

        if (respuesta != null) {

            try {
                for (int i = 0; i < respuesta.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", respuesta.get(i).get(0).toString());
                    jo.put("Codigo", respuesta.get(i).get(1).toString());
                    jo.put("Fecha", respuesta.get(i).get(2).toString());
                    jo.put("Hora", respuesta.get(i).get(3).toString());
                    array.put(jo);
                }

            } catch (JSONException e) {
                Log.w("Error", e.getMessage());
            }
        }

        // System.out.println(array.toString());
        return array.toString();

    }

    public String controlGPS() {

        JSONArray array = new JSONArray();
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Control_GPS",
                new String[]{"*"}, null, null, null, null, null);

        if (respuesta != null) {

            try {
                for (int i = 0; i < respuesta.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", respuesta.get(i).get(0));
                    jo.put("Canal", MainActivity.config.getCanal());
                    jo.put("Codigo", respuesta.get(i).get(1).toString());
                    jo.put("Fecha", respuesta.get(i).get(2).toString());
                    jo.put("Hora", respuesta.get(i).get(3).toString());
                    jo.put("Activo", respuesta.get(i).get(4).toString());
                    jo.put("Tipo", respuesta.get(i).get(5).toString());
                    array.put(jo);
                }

            } catch (JSONException e) {
                Log.w("Error", e.getMessage());
            }
        }

        return array.toString();

    }

    public String GPS() {

        JSONArray array = new JSONArray();
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "GPS", new String[]{"*"},
                null, null, null, null, null);

        if (respuesta != null) {

            try {
                for (int i = 0; i < respuesta.size(); i++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", respuesta.get(i).get(0).toString());
                    jo.put("Canal", MainActivity.config.getCanal());
                    jo.put("Codigo_Asesor", respuesta.get(i).get(1).toString());
                    jo.put("Fecha", respuesta.get(i).get(2).toString());
                    jo.put("Hora", respuesta.get(i).get(3).toString());
                    jo.put("Localizacion", respuesta.get(i).get(4).toString());
                    jo.put("Tipo", respuesta.get(i).get(5).toString());
                    array.put(jo);
                }

            } catch (JSONException e) {
                Log.w("Error", e.getMessage());
            }
        }

        return array.toString();
    }

    // clase Interna tipo Hilo encargada de enviar cada hora los datos que esten
    // almacendos
    class Hilo_Enviar extends Thread {

        private boolean terminar = false;

        public void setTerminar(boolean terminar) {
            this.terminar = terminar;
        }

        @Override
        public void run() {

            while (!terminar) {
                try {
                    Enviar();
                } catch (Exception e) {
                    Log.w("Error", "Mensage " + e.getMessage());
                }
                dormir();
            }
        }

        public void dormir() {
            try {
                // System.out.println("Hilo Enviar Durmiendo");
                // Thread.sleep(3600000); // El tiempo en milisegundos
                // Thread.sleep(600000);
                Thread.sleep(60000);
            } catch (Exception e) {
                Log.w("Error", "Mensage " + e.getMessage());
            }
        }
    }

    ;

}
