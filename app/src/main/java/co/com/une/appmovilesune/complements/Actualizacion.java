package co.com.une.appmovilesune.complements;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;

import android.R.integer;
import android.os.AsyncTask;

public class Actualizacion extends AsyncTask<String, integer, ArrayList<Object>> implements Subject {

    Observer obser;

    public Actualizacion() {
        // System.out.println("Actualizacion");
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<Object> doInBackground(String... arg0) {
        // TODO Auto-generated method stub
        boolean resultado = false;

        if (arg0[0].equals("Ciudades")) {
            resultado = MainActivity.config.obtenerCiudades();
        } else if (arg0[0].equals("Listas Generales")) {
            resultado = MainActivity.config.obtenerListasGenerales();
        } else if (arg0[0].equals("Listas Estratos")) {
            resultado = MainActivity.config.obtenerListasEstratos();
        } else if (arg0[0].equals("Listas Valores")) {
            resultado = MainActivity.config.obteneListasValores();
        } else if (arg0[0].equals("Adicionales")) {
            resultado = MainActivity.config.obtenerAdicionales();
        } else if (arg0[0].equals("Productos")) {
            resultado = MainActivity.config.obtenerProductos();
        } else if (arg0[0].equals("Precios")) {
            resultado = MainActivity.config.obtenerPrecios();
        } else if (arg0[0].equals("Reglas")) {
            resultado = MainActivity.config.obtenerReglas();
        } else if (arg0[0].equals("Promociones")) {
            resultado = MainActivity.config.obtenerPromociones();
        } else if (arg0[0].equals("Impuestos")) {
            resultado = MainActivity.config.obtenerImpuestos();
        } else if (arg0[0].equals("ExtensionesHFC")) {
            resultado = MainActivity.config.obtenerExtensionesHFC();
        } else if (arg0[0].equals("Barrios")) {
            resultado = MainActivity.config.obtenerBarrios();
        } else if (arg0[0].equals("Promociones Movilidad")) {
            resultado = MainActivity.config.obtenerPromocionesMovilidad();
        } else if (arg0[0].equals("Gpon")) {
            resultado = MainActivity.config.obtenerGpon();
        } else if (arg0[0].equals("Condiciones")) {
            resultado = MainActivity.config.obtenerCondiciones();
        } else if (arg0[0].equals("CondicionesxReglas")) {
            resultado = MainActivity.config.obtenerCondicionesxReglas();
        } else if (arg0[0].equals("Decos")) {
            resultado = MainActivity.config.obtenerDecos();
        } else if (arg0[0].equals("CondicionesxDecos")) {
            resultado = MainActivity.config.obtenerCondicionesxDecos();
        } else {
            resultado = false;
        }

        ArrayList<Object> res = new ArrayList<Object>();
        res.add(arg0[0]);
        res.add(resultado);

        return res;

    }

    @Override
    protected void onPostExecute(ArrayList<Object> result) {
        obser.update(result);
    }

    @Override
    public void addObserver(Observer o) {
        // TODO Auto-generated method stub
        obser = o;
    }

    @Override
    public void removeObserver(Observer o) {
        // TODO Auto-generated method stub
        obser = null;
    }

    @Override
    public void notifyObserver() {
        // TODO Auto-generated method stub

    }

}
