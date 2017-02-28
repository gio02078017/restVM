package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.complements.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Peticion implements Serializable {

    private String id;
    private String id_asesoria;
    private String Transaccion;
    private String Cual;
    private String Productos;
    private String Evento;
    private String Pedido;
    private String Cun;

    public Peticion() {
        Transaccion = "";
        Cual = "";
        Productos = "";
        Evento = "";
        Pedido = "";
        Cun = "";
    }

    public Peticion(String Transaccion, String Cual, String Productos, String Evento, String Pedido, String Cun) {
        this.Transaccion = Transaccion;
        this.Cual = Cual;
        this.Productos = Productos;
        this.Evento = Evento;
        this.Pedido = Pedido;
        this.Cun = Cun;
    }

    public String[] obtenerListaTransaccion() {
        String transacciones = "";
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "Configuracion",
                new String[]{"Datos"}, "tipo=? and campo=?", new String[]{"Peticiones", "Transacción"}, null,
                null, null);
        if (resultado != null) {
            transacciones = resultado.get(0).get(0);
        } else {

        }
        transacciones = "-- Seleccione Transacción --," + transacciones;
        String[] StTransacciones = transacciones.split(",");

        return StTransacciones;
    }

    public void guardarPeticion(String id_asesoria) {
        ContentValues cv = new ContentValues();
        cv.put("id_asesoria", id_asesoria);
        cv.put("Transaccion", Transaccion);
        cv.put("Cual", Cual);
        cv.put("Productos", Productos);
        cv.put("Evento", Evento);
        cv.put("Pedido", Pedido);
        cv.put("Cun", Cun);

        if (MainActivity.basedatos.insertar("peticiones", cv)) {
            ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "peticiones",
                    new String[]{"id"}, "id_asesoria=?", new String[]{id_asesoria}, null, null, null);
            if (resultado != null) {
                id = resultado.get(0).get(0);
            } else {
                id = null;
            }
        } else {
            id = null;
        }
    }

    public JSONObject consolidarPeticion() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("Transaccion", Transaccion);
            jo.put("Cual", Cual);
            jo.put("Productos", Productos);
            jo.put("Evento", Evento);
            jo.put("Pedido", Pedido);
            jo.put("Cun", Cun);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jo;
    }

    public String getTransaccion() {
        return Transaccion;
    }

    public void setTransaccion(String transaccion) {
        Transaccion = transaccion;
    }

    public String getCual() {
        return Cual;
    }

    public void setCual(String cual) {
        Cual = cual;
    }

    public String getProductos() {
        return Productos;
    }

    public void setProductos(String productos) {
        Productos = productos;
    }

    public String getEvento() {
        return Evento;
    }

    public void setEvento(String evento) {
        Evento = evento;
    }

    public String getPedido() {
        return Pedido;
    }

    public void setPedido(String pedido) {
        Pedido = pedido;
    }

    public String getCun() {
        return Cun;
    }

    public void setCun(String cun) {
        Cun = cun;
    }

}
