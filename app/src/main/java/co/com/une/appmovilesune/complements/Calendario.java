package co.com.une.appmovilesune.complements;

import java.sql.Timestamp;
import java.util.Calendar;

import co.com.une.appmovilesune.MainActivity;

public class Calendario {

	private static final long HORA = 3600000;

	private static String Fecha;
	private static String Hora;
	private static int anio;
	private static int mes;
	private static int dia;
	private static long timestamp;

	public Calendario() {

	}

	public static String getFecha() {
		Calendar calendario = Calendar.getInstance();
		Fecha = calendario.get(calendario.YEAR) + "-" + (calendario.get(calendario.MONTH) + 1) + "-"
				+ calendario.get(calendario.DAY_OF_MONTH);
		return Fecha;
	}

	public static String getHora() {
		Calendar calendario = Calendar.getInstance();
		Hora = calendario.get(calendario.HOUR_OF_DAY) + ":" + calendario.get(calendario.MINUTE);
		return Hora;
	}

	public static String getHoraCompleta() {
		Calendar calendario = Calendar.getInstance();
		Hora = calendario.get(calendario.HOUR_OF_DAY) + ":" + calendario.get(calendario.MINUTE) + ":"
				+ calendario.get(calendario.SECOND);
		return Hora;
	}

	public static int getAnio() {
		Calendar calendario = Calendar.getInstance();
		anio = calendario.get(calendario.YEAR);
		return anio;
	}

	public static int getMes() {
		Calendar calendario = Calendar.getInstance();
		mes = calendario.get(calendario.MONTH);
		return mes;
	}

	public static int getDia() {
		Calendar calendario = Calendar.getInstance();
		dia = calendario.get(calendario.DAY_OF_MONTH);
		return dia;
	}

	public static long getTimestamp() {
		Calendar calendario = Calendar.getInstance();
		timestamp = calendario.getTimeInMillis();
		return timestamp;
	}

	public static boolean validarSesion(long ultima, long ahora) {
		boolean respuesta = false;
		long tiempo = ahora - ultima;
		// System.out.println("Tiempo => " + tiempo);
		if (tiempo < (HORA * MainActivity.preferencias.getFloat("SessionTime", 1))) {
			respuesta = true;
		}
		return respuesta;
	}

}
