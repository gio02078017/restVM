package co.com.une.appmovilesune;

import co.com.une.appmovilesune.complements.Calendario;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

//CLASE SERVICIO ENCARGADA DE SOLICITAR, DESTRUIR Y ALMACENAR LOS REGISTROS DEL GPS 
public class PlayerGPS extends Service {

	GeoUpdateHandler_GPS Localizacion_GPS;
	GeoUpdateHandler_Red Localizacion_Red;

	private static LocationManager locationManager;
	private static String latitud;
	private static String longitud;
	/*
	 * private Almacenar almacenar; private Fecha fecha = new Fecha();
	 */
	private String Codigo;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// METODO PRINCIPAL DE LA CLASE
	@Override
	public void onCreate() {
		super.onCreate();
		// almacenar = new Almacenar(this);
		// System.out.println("Servicio onCreate");
		_startService();

	}

	// METODO QUE SE LLAMA CUANDO SE CIERRA EL APLICATIVO PARA QUE DETENGA LOS
	// SERVICIOS CORRIENDO DEL GPS
	@Override
	public void onDestroy() {
		// Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
		// System.out.println("Servicio onDestroy");
		locationManager.removeUpdates(Localizacion_GPS);
		locationManager.removeUpdates(Localizacion_Red);
		// locationManager.removeUpdates(Localizacion_Criteria);
	}

	private void _startService() {
		// System.out.println("Servicio _startService");
	}

	@Override
	public void onStart(Intent intent, int startid) {
		// Bundle b = getIntent().getExtras();
		Bundle Datos = intent.getExtras();
		// String Codigo = Datos.getString("Codigo");
		String Codigo = MainActivity.config.getCodigo();
		this.Codigo = Codigo;
		// System.out.println("Codigo " + Codigo);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 1,
				Localizacion_GPS = new GeoUpdateHandler_GPS());
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 9000, 1,
				Localizacion_Red = new GeoUpdateHandler_Red());

	}

	public class GeoUpdateHandler_GPS implements LocationListener {

		String Fecha;
		String Hora;

		public void onLocationChanged(Location location) {
			Double lat = location.getLatitude();
			latitud = lat.toString();
			// System.out.println("latitud " + latitud);
			Double longi = location.getLongitude();
			longitud = longi.toString();
			// System.out.println("longitud " + longitud);
			Mensaje("latitud " + latitud + " y " + "longitud " + longitud);
			String Localizacion = latitud + "," + longitud;
			GPS(Codigo, Localizacion, "GPS");

		}

		public void onProviderDisabled(String provider) {
			// System.out.println("Hola Disabled Gps Satelite");
			Control_GPS(Codigo, "Disabled", "GPS");
		}

		public void onProviderEnabled(String provider) {
			// System.out.println("Hola Enabled Gps Satelite");
			Control_GPS(Codigo, "Enabled", "GPS");
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public class GeoUpdateHandler_Red implements LocationListener {
		String Fecha;
		String Hora;

		public void onLocationChanged(Location location) {
			Double lat = location.getLatitude();
			latitud = lat.toString();
			Double longi = location.getLongitude();
			longitud = longi.toString();
			Mensaje("latitud " + latitud + " y " + "longitud " + longitud);
			String Localizacion = latitud + "," + longitud;
			GPS(Codigo, Localizacion, "Red");
		}

		public void onProviderDisabled(String provider) {
			// System.out.println("Hola Disabled Gps Red");
			Control_GPS(Codigo, "Disabled", "Red");
		}

		public void onProviderEnabled(String provider) {
			// System.out.println("Hola Enabled Gps Red");
			Control_GPS(Codigo, "Enabled", "Red");
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}

	public void GPS() {

	}

	public void Mensaje(String Mensaje) {
		// Toast.makeText(this, Mensaje, Toast.LENGTH_SHORT).show();
	}

	public void Control_GPS(String Codigo, String Estado, String Medio) {
		ContentValues cv = new ContentValues();
		cv.put("Codigo_Asesor", MainActivity.config.getCodigo());
		cv.put("Fecha", Calendario.getFecha());
		cv.put("Hora", Calendario.getHora());
		cv.put("Activo", Estado);
		cv.put("Tipo", Medio);
		MainActivity.basedatos.insertar("Control_GPS", cv);
	}

	public void GPS(String Codigo, String Localizacion, String Medio) {
		ContentValues cv = new ContentValues();
		cv.put("Codigo_Asesor", MainActivity.config.getCodigo());
		cv.put("Fecha", Calendario.getFecha());
		cv.put("Hora", Calendario.getHora());
		cv.put("Localizacion", Localizacion);
		cv.put("Tipo", Medio);
		MainActivity.basedatos.insertar("GPS", cv);
	}
}
