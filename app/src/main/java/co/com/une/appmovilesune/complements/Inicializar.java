package co.com.une.appmovilesune.complements;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.controller.ControlConfiguracion;
import co.com.une.appmovilesune.controller.ControlInicializacion;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Configuracion;

import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

public class Inicializar extends AsyncTask<String, Integer, Boolean> implements Subject {

    Observer obsr;

    boolean actualizado;

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... params) {
        // TODO Auto-generated method stub
        ArrayList<Boolean> recolector = new ArrayList<Boolean>();
        if (MainActivity.conexion.isConect(MainActivity.context)) {
            crearBaseDatos();
            inicializarConfiguracion();
            obtenerVersion();

			/*
             * crearBaseDatos(); inicializarConfiguracion(); obtenerVersion();
			 * seguimiento(); tiempoSesion();
			 */

        } else {
            // System.out.println("Habilite las interfaces de comunicacion");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        // System.out.println("Termino Ejecucion");
        notifyObserver();
    }

    public Inicializar() {
        crearBaseDatos();
        MainActivity.config = new Configuracion(MainActivity.context);
        MainActivity.config.precargarConfiguraciones();

    }

    private void crearBaseDatos() {
        MainActivity.basedatos = new BaseDatos(MainActivity.context, BaseDatos.NOMBRE, null, BaseDatos.VERSION);
    }

    private void inicializarConfiguracion() {
        MainActivity.config.obtenerConfiguraciones();

    }

    private void obtenerVersion() {
        actualizado = MainActivity.config.obtenerVersion();
        // System.out.println("");
    }

	/*
	 * 
	 * private void seguimiento() { MainActivity.seguimiento =
	 * MainActivity.config.consultarSeguimiento(); System.out.println(
	 * "El seguimiento de la aplicacion esta activo? => " +
	 * MainActivity.seguimiento); }
	 * 
	 * private void tiempoSesion() { float tiempoSesion =
	 * MainActivity.config.consultarTiempoSesion(); System.out.println(
	 * "inicializar 50 : Tiempo Sesion => " + tiempoSesion); Editor editor =
	 * MainActivity.preferencias.edit(); editor.putFloat("SessionTime",
	 * tiempoSesion); editor.commit();
	 * 
	 * long time = MainActivity.preferencias.getLong("time", 0); if (time > 0) {
	 * long ahora = Calendario.getTimestamp();
	 * 
	 * System.out.println("Time => " + time); System.out.println("Ahora => " +
	 * ahora);
	 * 
	 * long diff = ahora - time;
	 * 
	 * System.out.println("diff => " + diff);
	 * 
	 * double semanas = Math.floor(Double.valueOf(diff) / 86400000 / 7); double
	 * dias = Math.floor(diff / 86400000 % 7); double horas = Math.floor(diff /
	 * 1000 / 3600 % 24); double minutos = Math.floor(diff / 60000 % 60); double
	 * segundos = Math.floor(diff % 60000 / 1000);
	 * 
	 * System.out.println("Semanas => " + semanas); System.out.println(
	 * "Dias => " + dias); System.out.println("Horas => " + horas);
	 * System.out.println("Minutos => " + minutos); System.out.println(
	 * "Segundos => " + segundos);
	 * 
	 * if (tiempoSesion <= dias) { System.out.println(
	 * "Sobre paso el limite de tiempo de sesion");
	 * System.out.println(MainActivity.basedatos.eliminar("usuario", null,
	 * null)); } } }
	 */

    @Override
    public void addObserver(Observer o) {
        // TODO Auto-generated method stub
        obsr = o;
    }

    @Override
    public void removeObserver(Observer o) {
        // TODO Auto-generated method stub
        obsr = null;
    }

    @Override
    public void notifyObserver() {
        // TODO Auto-generated method stub
        // System.out.println("Actualizado => " + actualizado);
        if (actualizado) {
            obsr.update("Siga");
        } else {
            obsr.update("No Actualizado");
        }
    }

}
