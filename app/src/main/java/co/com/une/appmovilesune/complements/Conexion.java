package co.com.une.appmovilesune.complements;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.components.BotonPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;

public class Conexion extends AsyncTask<ArrayList<Object>, Integer, ArrayList<Object>> implements Subject {

	public static final int GLOBAL = 0;
	public static final int SIMULADOR = 1;

	public static final String SERVIDOR_DESARROLLO = "http://dgdr.une.net.co";
	public static final String SERVIDOR_DESARROLLO_INTERNA = "http://unevm-dmap.epmtelco.com.co";
	public static final String SERVIDOR_PRUEBAS = "http://200.13.249.56";
	public static final String SERVIDOR_PRODUCCION = "https://ws.une.com.co";
	public static final String SERVIDOR_PRODUCCION_INTERNA = "http://unevm-pmap.epmtelco.com.co";
	public static final String SERVIDOR_DAVID = "http://10.65.78.113";
	public static final String SERVIDOR_DAVID_INTERNA = "http://10.65.68.128";
	public static final String SERVIDOR_DAVID_EXTRNA = "http://192.168.1.7:8888";
	public static final String SERVIDOR_GIOVANNY = "http://10.65.68.118";

	//private static final String RUTA_WS = "/wsVentaMovil/wsVentaMovil/";
	private static final String RUTA_WS = "/wsVentaMovil/pre-produccion/";
	//private static final String RUTA_WS = "/wsVentaMovil/production/";
	//private static final String RUTA_WS = "/wsVentaMovil/facturacionAnticipada/";
	// private static final String WS_GLOBAL = "ServerVentaMovil.php";
	private static final String WS_GLOBAL = "ServerVentaMovilNew.php";
	private static final String WS_SIMULADOR = "serverSimulador.php";

	private String namespace, url, servidor;

	private Observer observer;
	private Object resultado;

	public HttpTransportSE canal;
	private SoapSerializationEnvelope paquete;
	private SoapObject cliente;

	private BotonPrincipal btnPricipal;

	private String mensaje;
	private String lugar = "";
	private Context context;

	public ProgressDialog pd;

	private String nuevaAccion="";
	private boolean cambiarAccion;

	public Conexion(String servidor) {
		this.servidor = servidor;
		generarDirecciones(GLOBAL);
		canal = new HttpTransportSE(url);
	}

	public Conexion(String servidor, BotonPrincipal btnPricipal) {
		this.servidor = servidor;
		generarDirecciones(GLOBAL);
		canal = new HttpTransportSE(url);
		this.btnPricipal = btnPricipal;
	}

	@Override
	protected void onPreExecute() {
		// System.out.println("Conexion 76 => " + context);
		if (context == null) {
			MainActivity.btnCliente.setVisible();
		} else if (context.getClass().getSimpleName().equals("ControlVenta")) {
			MainActivity.btnCliente.setInvisible();
		} else if (context.getClass().getSimpleName().equals("ControlResumen")) {
			MainActivity.btnCliente.setInvisible();
		} else {
			MainActivity.btnCliente.setVisible();
		}

		if (lugar.equals("Manual")) {
			pd = ProgressDialog.show(context, context.getResources().getString(R.string.consultando),
					context.getResources().getString(R.string.obteniendoinformacion));
		}
	}

	@Override
	protected ArrayList<Object> doInBackground(ArrayList<Object>... params) {

		// System.out.println("Ejecutando");
        nuevaAccion = "";
		cambiarAccion = false;
		String accion = params[0].get(0).toString();
		ArrayList<String[]> parametros = (ArrayList<String[]>) params[0].get(1);

		ArrayList<Object> resultados = new ArrayList<Object>();
		resultados.add(accion);
		resultados.add(MainActivity.conexion.ejecutarSoap(accion, parametros));

		System.out.println("Conexion ejecutarSoap cambiarAccion => " + cambiarAccion);
		System.out.println("Conexion ejecutarSoap => nuevaAccion" + nuevaAccion);

		if(cambiarAccion) {
			System.out.println("Conexion ejecutarSoap  validacion "+Utilidades.validacionConfig(resultados));
			ArrayList<Object> validados = Utilidades.validacionConfig(resultados);
			if((Boolean) validados.get(0)){
				//resultados.add(1,validados.get(1));
				resultados.set(1,validados.get(1));
			}else{
				//resultados.add(0,MainActivity.conexion.getNuevaAccion());
				resultados.set(0,nuevaAccion);
			}
		}

		System.out.println("Conexion ejecutarSoap Ejecutado => " + resultados);

		return resultados;
	}

	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		System.out.println("result.get(0) => " + result.get(0));
		if (result.get(0).equals("Cliente") || result.get(0).equals("ClienteREDCO")
				|| result.get(0).equals("ConsolidadoSiebel")) {
			MainActivity.btnCliente.setInvisible();
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")
					|| result.get(1).equals("Ha superado el limite de consultas por dia")
					|| result.get(1).equals("Consulta Hecha fuera del horario establecido")) {
				MainActivity.btnCliente.setWRONG();
				resultado = result;
			} else {
				MainActivity.btnCliente.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("Consolidar")) {
			MainActivity.btnCliente.setOK();
			resultado = result;
			notifyObserver();
		} else if (result.get(0).equals("ConsultarAgenda")) {
			MainActivity.btnCliente.setOK();
			resultado = result;
			notifyObserver();
		} else if (result.get(0).equals("ConsultarBlindaje")) {
			MainActivity.btnCliente.setInvisible();
			resultado = result;
			notifyObserver();
		} else if (result.get(0).equals("ValidacionConfiguracionMovil")) {
			MainActivity.btnCliente.setInvisible();
			resultado = result;
			notifyObserver();
		}

		if (lugar.equals("Manual")) {
			pd.dismiss();
		}
	}

	public void generarDirecciones(int webservice) {

		switch (webservice) {
		case 0:
			namespace = servidor + RUTA_WS + WS_GLOBAL;
			url = servidor + RUTA_WS + WS_GLOBAL + "?wsdl";
			break;
		case 1:
			namespace = servidor + RUTA_WS + WS_SIMULADOR;
			url = servidor + RUTA_WS + WS_SIMULADOR + "?wsdl";
			break;
		default:
			namespace = servidor + RUTA_WS + WS_GLOBAL;
			url = servidor + RUTA_WS + WS_GLOBAL + "?wsdl";
			break;
		}
		// System.out.println("Conexion 134 => " + url);
	}

	public String ejecutarSoap(String accion, ArrayList<String[]> parametros) {
		String respuesta = "Respuesta Vacia";
		HttpTransportSE canal = new HttpTransportSE(url, 70000);
		// HttpTransportSE canal = new HttpTransportSE(url, 120000);
		try {

			String accionDinamica = "";
			boolean dependencia = false;
			SoapObject cliente;

			System.out.println("Conecion ejecutarSoap accion "+accion);

			ArrayList<String> excluir= new ArrayList<String>();
			excluir.add("Ejecutar");
			excluir.add("Tarifas");
			excluir.add("obtenerPermisos");

			if(!excluir.contains(accion)){
				accionDinamica = "ValidacionConfiguracionMovil";
				cliente = new SoapObject(namespace, "ValidacionConfiguracionMovil");
				SoapObject parametrosAccion = new SoapObject(namespace, "parametrosAccion");

				for (int i = 0; i < parametros.size(); i++) {

					parametrosAccion.addProperty("ref",parametros.get(i)[1]);

				}

				cliente.addSoapObject(parametrosAccion);
				cliente.addProperty("parametrosAutenticacion", Utilidades.generarJsonVerificacionServicios());
				cliente.addProperty("accion", accion);
				cliente.addProperty("log", true);
				dependencia = true;
			}else {
				accionDinamica = accion;
				cliente = new SoapObject(namespace, accion);
				for (int i = 0; i < parametros.size(); i++) {
					cliente.addProperty(parametros.get(i)[0], parametros.get(i)[1]);
				}

				dependencia = false;

			}


			SoapSerializationEnvelope paquete = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			paquete.setOutputSoapObject(cliente);
			//System.out.println("Conexion 149 => " + cliente);
			System.out.println("Conexion ejecutarSoap cliente "+cliente);
			canal.debug = true;
			//canal.call(accion, paquete);
			canal.call(accionDinamica, paquete);
			canal.getConnection().disconnect();

			System.out.println("Conexion ejecutarSoap Log servicios "+canal.requestDump);
			//System.out.println("Log servicios " + canal.requestDump);
			Editor editor = MainActivity.preferencias.edit();
			editor.putString("xml", canal.requestDump);
			editor.commit();
			if(!dependencia) {
				respuesta = paquete.getResponse().toString();
				System.out.println("No dependiente");
				System.out.println("Conexion ejecutarSoap dependencia "+"No dependiente");
			}else{
				respuesta = paquete.getResponse().toString();
				System.out.println("Conexion ejecutarSoap dependencia "+"dependiente");
				cambiarAccion = true;
				nuevaAccion = accionDinamica;
			}

			System.out.println("Conexion ejecutarSoap respuesta "+respuesta);
			System.out.println("Conexion ejecutarSoap 170 => " + respuesta);

		} catch (IOException e) {
			e.printStackTrace();
			Log.w("problemas conexion", e.getMessage() + "Parametros " + parametros);
			System.out.println(respuesta);
			System.out.println("Log servicios IOException" + canal.requestDump);
			Editor editor = MainActivity.preferencias.edit();
			editor.putString("xml", canal.requestDump);

			editor.commit();
			// Toast.LENGTH_SHORT).show();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			Log.w("Error", e.getMessage() + "Parametros " + parametros);
			System.out.println("Log servicios XmlPullParserException" + canal.requestDump);
			Editor editor = MainActivity.preferencias.edit();
			editor.putString("xml", canal.requestDump);
			editor.commit();
			// Toast.makeText(context,"Problemas Leyendo los Datos",
			// Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Excepcion" + e.getMessage());
			System.out.println("Log servicios Exception" + canal.requestDump);
			Editor editor = MainActivity.preferencias.edit();
			editor.putString("xml", canal.requestDump);
			editor.commit();
		}
		return respuesta;
	}

	public String ejecutarNuSoap(String accion, SoapObject parametros) {
		String respuesta = "Respuesta Vacia";
		try {
			SoapObject cliente = new SoapObject(namespace, accion);
			cliente.addProperty(accion, parametros);
			SoapSerializationEnvelope paquete = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			paquete.setOutputSoapObject(cliente);
			canal.call(accion, paquete);
			respuesta = paquete.getResponse().toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return respuesta;

	}

	public boolean isConect(Context context) {

		boolean conectado = false;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo[] redes = manager.getAllNetworkInfo();
		for (int i = 0; i < redes.length; i++) {
			if (redes[i].isAvailable()) {
				conectado = true;
			}

		}
		return conectado;
	}

	public String obtenerMAC() {
		WifiManager wifimanager = (WifiManager) MainActivity.context
				.getSystemService(MainActivity.context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifimanager.getConnectionInfo();
		return wifiinfo.getMacAddress();
	}

	public void setManual(Context context) {
		this.lugar = "Manual";
		this.context = context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getNamespace() {
		return namespace;
	}

	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub
		observer = o;
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observer = null;

	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub
		observer.update(this.resultado);
	}

	public String getNuevaAccion() {
		return nuevaAccion;
	}

	public void setNuevaAccion(String nuevaAccion) {
		this.nuevaAccion = nuevaAccion;
	}

	public boolean isCambiarAccion() {
		return cambiarAccion;
	}

	public void setCambiarAccion(boolean cambiarAccion) {
		this.cambiarAccion = cambiarAccion;
	}
}
