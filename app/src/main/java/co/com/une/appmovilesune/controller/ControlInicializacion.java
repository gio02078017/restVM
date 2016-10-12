package co.com.une.appmovilesune.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.CRC32;

import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import com.google.analytics.tracking.android.EasyTracker;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.complements.Inicializar;
import co.com.une.appmovilesune.interfaces.Observer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ControlInicializacion extends Activity implements Observer {

	public static TextView lblMensajes;
	public ProgressBar pgbInicializar;
	public ImageButton btnDownload;
	public NetworkInfo mWifi;
	public NetworkInfo mOther;
	public Dialogo dialogo;
	public Inicializar inicial;
	public Observer observador;

	public void onBackPressed() {
		return;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewinicializacion);

		// Chequeo conectividad
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		mOther = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// Escondo boton de descargar si hay internet (Solo daria para
		// descargar)
		btnDownload = (ImageButton) findViewById(R.id.btnDownload);
		if (mOther != null) {
			if (mWifi.isConnected() || mOther.isConnected()) {
				btnDownload.setVisibility(View.GONE);
			}
		} else if (mOther == null) {
			if (mWifi.isConnected()) {
				btnDownload.setVisibility(View.GONE);
			}
		}

		pgbInicializar = (ProgressBar) findViewById(R.id.pgbInicializar);

		lblMensajes = (TextView) findViewById(R.id.lblMensajes);
		lblMensajes.setText("Iniciando y Configurando Aplicativo...");

		observador = this;

		inicial = new Inicializar();
		if (MainActivity.config.getCiudad().equals("--Seleccione Ciudad--")) {
			MainActivity.config.setCiudad("Medellin");
		}
		inicial.addObserver(this);
		inicial.execute("Inicla");
	}

	@Override
	public void onStart() {
		super.onStart();
		if (MainActivity.seguimiento) {
			EasyTracker.getInstance(this).activityStart(this);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (MainActivity.seguimiento) {
			EasyTracker.getInstance(this).activityStop(this);
		}
	}

	private void validarCiudades() {
		ArrayList<ArrayList<String>> res = MainActivity.basedatos.consultar(false, "Departamentos",
				new String[] { "*" }, null, null, null, null, null);
		String json = "[";
		for (ArrayList<String> arrayList : res) {
			json = json + "{\"Departamento\":\"" + arrayList.get(1) + "\",\"DepartamentoFenix\":\"" + arrayList.get(2)
					+ "\",\"Ciudad\":\"" + arrayList.get(3) + "\",\"CiudadFenix\":\"" + arrayList.get(4)
					+ "\",\"Codigo_Giis\":\"" + arrayList.get(5) + "\",\"Identificador\":\"" + arrayList.get(6)
					+ "\"},";
		}
		json = json + ']';
		json = json.replace(",]", "]");

		ArrayList<String[]> parametros = new ArrayList<String[]>();
		parametros.add(new String[] { "Configuracion", "ciudades" });
		parametros.add(new String[] { "Parametros", "" });
		parametros.add(new String[] { "Comparacion", generarCodigo(json) });

		/*
		 * System.out.println(MainActivity.conexion.ejecutarSoap(
		 * "ValidarConfiguracion", parametros));
		 */
	}

	private void validarGlobales() {
		ArrayList<ArrayList<String>> res = MainActivity.basedatos.consultar(false, "Configuracion",
				new String[] { "*" }, null, null, null, null, null);
		String json = "[";
		for (ArrayList<String> arrayList : res) {
			json = json + "{\"Departamento\":\"" + arrayList.get(1) + "\",\"Estrato\":\"" + arrayList.get(2)
					+ "\",\"Tipo\":\"" + arrayList.get(3) + "\",\"Campo\":\"" + arrayList.get(4) + "\",\"Datos\":\""
					+ arrayList.get(5) + "\",\"Evento\":\"" + arrayList.get(6) + "\"},";
		}
		json = json + ']';
		json = json.replace(",]", "]");

		ArrayList<String[]> parametros = new ArrayList<String[]>();
		parametros.add(new String[] { "Configuracion", "ciudades" });
		parametros.add(new String[] { "Parametros", "" });
		parametros.add(new String[] { "Comparacion", generarCodigo(json) });

		/*
		 * System.out.println(MainActivity.conexion.ejecutarSoap(
		 * "ValidarConfiguracion", parametros));
		 */
	}

	private String generarCodigo(String json) {

		String codigo = "-1";

		try {
			String jsonutf8 = new String(json.getBytes("UTF-8"));
			String base = Base64.encode(jsonutf8.getBytes());
			base = base.replace("\r\n", "");

			// System.out.println(jsonutf8);

			CRC32 crc = new CRC32();
			crc.update(base.getBytes());

			Editor editor = MainActivity.preferencias.edit();
			editor.putString("json", json);
			editor.putString("jsonutf8", jsonutf8);
			editor.putString("base", base);
			editor.putString("crc", Long.toHexString(crc.getValue()).toString());
			editor.commit();

			codigo = Long.toHexString(crc.getValue()).toString();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return codigo;
	}

	public void downloadVersion(View v) {

		String urlppal = "http://goo.gl/sRW5hu";
		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "configuracion",
				new String[] { "datos" }, "tipo = ? and campo = ?", new String[] { "variables", "urlDownload" }, null,
				null, null);
		if (resultado != null) {
			urlppal = resultado.get(0).get(0);
		}
		// System.out.println("URL De descarga" + urlppal);
		Uri uri = Uri.parse(urlppal);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	public void reloadVersion(View v) {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		mOther = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		pgbInicializar = (ProgressBar) findViewById(R.id.pgbInicializar);
		lblMensajes = (TextView) findViewById(R.id.lblMensajes);
		lblMensajes.setText("Intentando de nuevo...");

		Inicializar inicial = new Inicializar();
		inicial.addObserver(this);
		inicial.execute("Inicla");
	}

	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		// System.out.println("Update ci => " + value);
		if (value.equals("Siga")) {
			Intent intent = new Intent();
			setResult(MainActivity.OK_RESULT_CODE, intent);
			finish();
		} else if (value.equals("No Actualizado")) {
		}
		if (mOther != null) {
			if (mWifi.isConnected() || mOther.isConnected()) {
				btnDownload.setVisibility(View.GONE);
				lblMensajes.setText(getResources().getString(R.string.ustedestaversion) + MainActivity.config.Version
						+ ", necesita actualizar a la version " + MainActivity.config.srvVersion
						+ " para poder continuar.");
				btnDownload.setVisibility(View.VISIBLE);
				pgbInicializar.setVisibility(ProgressBar.GONE);
			} else {
				lblMensajes.setText("No hay conexion a internet.");
				btnDownload.setVisibility(View.GONE);
				pgbInicializar.setVisibility(ProgressBar.GONE);
			}
		} else if (mOther == null) {
			if (mWifi.isConnected()) {
				btnDownload.setVisibility(View.GONE);
				lblMensajes.setText(getResources().getString(R.string.ustedestaversion) + MainActivity.config.Version
						+ ", necesita actualizar a la version " + MainActivity.config.srvVersion
						+ " para poder continuar.");
				btnDownload.setVisibility(View.VISIBLE);
				pgbInicializar.setVisibility(ProgressBar.GONE);
			} else {
				lblMensajes.setText("No hay conexion a internet.");
				btnDownload.setVisibility(View.GONE);
				pgbInicializar.setVisibility(ProgressBar.GONE);
			}
		}

	}

}
