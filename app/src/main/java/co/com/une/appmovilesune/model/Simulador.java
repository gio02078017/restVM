package co.com.une.appmovilesune.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class Simulador extends AsyncTask<ArrayList<Object>, Integer, ArrayList<Object>> implements Subject {

	private String accion, tipo, lugar = "";
	private ArrayList<String> parametros;
	private Observer observer;
	private Object resultado;
	private Context context;
	ProgressDialog pd;

	@Override
	protected void onPreExecute() {
		accion = "";
		if (lugar.equals("Manual")) {
			pd = null;
			pd = ProgressDialog.show(context, "Consultando",
					context.getResources().getString(R.string.obteniendoinformacion));
		} else {
			MainActivity.btnPortafolio.setVisible();
			MainActivity.btnCobertura.setVisible();
			MainActivity.btnCarteraUne.setVisible();
			if (!MainActivity.config.getCiudad().equalsIgnoreCase("BogotaREDCO")) {
				MainActivity.btnCobertura.setVisible();
			}
		}

	}

	@Override
	protected ArrayList<Object> doInBackground(ArrayList<Object>... params) {
		// TODO Auto-generated method stub
		ArrayList<Object> resultados = new ArrayList<Object>();

		// System.out.println("Simulador 48 => " + params[0].get(1).toString());

		accion = params[0].get(1).toString();
		tipo = params[0].get(2).toString();

		resultados.add(accion);

		if (accion.equals("Portafolio")) {
			parametros = (ArrayList<String>) params[0].get(3);
			resultados.add(consultarPortafolio(tipo, parametros));
		} else if (accion.equals("ConsolidarSiebel")) {
			parametros = (ArrayList<String>) params[0].get(3);
			String salida = params[0].get(4).toString();
			resultados.add(consultarConsolidarSiebel(tipo, parametros, salida));
		} else if (accion.equals("Cobertura")) {
			parametros = (ArrayList<String>) params[0].get(3);
			resultados.add(consultarCobertura(tipo, parametros));
		} else if (accion.equals("CoberturaNew")) {
			parametros = (ArrayList<String>) params[0].get(3);
			resultados.add(consultarCobertura(tipo, parametros));
		} else if (accion.equals("Cartera_UNE")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(consultarCuentasUne(parametros));
		} else if (accion.equals("EstadoCuenta")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(consultarEstadoCuenta(parametros));
		} else if (accion.equals("DatosScooring")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(datosScooring(parametros));
		} else if (accion.equals("ResultScooring")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(resultadoScooring(parametros));
		} else if (accion.equals("Identificadores")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(Identificadores(parametros));
		} else if (accion.equals("EstadoPedido")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(EstadoPedido(parametros));
		} else if (accion.equals("EstadoOferta")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(EstadoOferta(parametros));
		} else if (accion.equals("Validador")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(Validador(parametros));
		} else if (accion.equals("Direcciones")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(consultarDirecciones(parametros));
		} else if (accion.equals("ConsultaNodos")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(ConsultaNodos(parametros));
		} else if (accion.equals("ResultadoIVR")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(ResultadoIVR(parametros));
		} else if (accion.equals("EstadoVenta")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(EstadoVenta(parametros));
		} else if (accion.equals("EstadoGerencia")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(EstadoGerencia(parametros));
		} else if (accion.equals("EstadoBlindaje")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(EstadoBlindaje(parametros));
		} else if (accion.equals("BuscarBlindaje")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(BuscarBlindaje(parametros));
		} else if (accion.equals("ConsultaCodigoHogar")) {
			parametros = (ArrayList<String>) params[0].get(2);
			System.out.println("parametros " + parametros);
			resultados.add(ConsultaCodigoHogar(parametros));
		} else if (accion.equals("CrearUneMas")) {
			parametros = (ArrayList<String>) params[0].get(2);
			System.out.println("parametros " + parametros);
			resultados.add(CrearUneMas(parametros));
		} else if (accion.equals("InsertarConfirmacion")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(InsertarConfirmacion(parametros));
		} else if (accion.equals("LanzarLlamada")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(LanzarLlamada(parametros));
		} else if (accion.equals("ConsultarRespuesta")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(consultarRespuesta(parametros));
		} else if (accion.equals("actualizarBandera")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(actualizarBandera(parametros));
		} else if (accion.equals("ValidarLlamada")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(validarLlamada(parametros));
		} else if (accion.equals("RepetirCodigo")) {
			System.out.println("parametros " + parametros);
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(RepetirCodigo(parametros));
		} else if (accion.equals("ConsultaCodigos")) {
			System.out.println("parametros " + parametros);
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(ConsultaCodigos(parametros));
		} else if (accion.equals("PortafolioREDCO")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(consultarPortafolioREDCO(parametros));
		} else if (accion.equals("ValidarLogin")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(validarLogin(parametros));
		} else if (accion.equals("VerificarDireccion")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(verificarDireccion(parametros));
		} else if (accion.equals("ConsultarAgenda")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(ConsultarAgenda(parametros));
		} else if (accion.equals("ConsultarAgendaSiebel")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(ConsultarAgendaSiebel(parametros));
		} else if (accion.equals("TipoHogar")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(ConsultarTipoHogar(parametros));
		} else if (accion.equals("ConsultarVentas")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(consultarVentas(parametros));
		} else if (accion.equals("validarPermiso")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(validarPermiso(parametros));
		}else if (accion.equals("ProyectosRurales")) {
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(proyectosRurales(parametros));
		} else if(accion.equals("consultarPagoParcialAnticipado")){
			parametros = (ArrayList<String>) params[0].get(2);
			resultados.add(consultarPagoParcialAnticipado(parametros));
		}

		return resultados;
	}

	@Override
	protected void onPostExecute(ArrayList<Object> result) {
		if (lugar.equals("Manual")) {
			pd.dismiss();
			lugar = "";
		}

		if(pd != null){
			pd.dismiss();
			pd = null;
		}

		if (result.get(0).equals("Portafolio")) {
			MainActivity.btnPortafolio.setInvisible();
			System.out.println("Simulador 88 => " + result.get(1));
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")
					|| result.get(1).equals("Ha superado el limite de consultas por dia")
					|| result.get(1).equals("Consulta Hecha fuera del horario establecido")) {
				MainActivity.btnPortafolio.setWRONG();
				resultado = result;
			} else {
				MainActivity.btnPortafolio.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("CoberturaNew")) {
			MainActivity.btnCobertura.setInvisible();
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")
					|| result.get(1).equals("Ha superado el limite de consultas por dia")
					|| result.get(1).equals("Consulta Hecha fuera del horario establecido")) {
				MainActivity.btnCobertura.setWRONG();
				resultado = result;
			} else {
				MainActivity.btnCobertura.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("Cartera_UNE")) {
			MainActivity.btnCarteraUne.setInvisible();
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")
					|| result.get(1).equals("Ha superado el limite de consultas por dia")
					|| result.get(1).equals("Consulta Hecha fuera del horario establecido")) {
				MainActivity.btnCarteraUne.setWRONG();
				resultado = result;
			} else {
				MainActivity.btnCarteraUne.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("DatosScooring")) {
			// MainActivity.btnCarteraUne.setInvisible();
			System.out.println("Scooring 000 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("EstadoCuenta")) {
			// MainActivity.btnCarteraUne.setInvisible();
			System.out.println("EstadoCuenta 000 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("ResultScooring")) {
			// MainActivity.btnCarteraUne.setInvisible();
			System.out.println("Scooring 000 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("Identificadores")) {
			// MainActivity.btnCarteraUne.setInvisible();
			System.out.println(result);
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")) {
				// MainActivity.btnCarteraUne.setWRONG();
				resultado = result;
			} else {
				// MainActivity.btnCarteraUne.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("EstadoPedido")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")) {
				// MainActivity.btnCarteraUne.setWRONG();
				resultado = result;
			} else {
				// MainActivity.btnCarteraUne.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("EstadoOferta")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")) {
				// MainActivity.btnCarteraUne.setWRONG();
				resultado = result;
			} else {
				// MainActivity.btnCarteraUne.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("EstadoBlindaje")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 EstadoMovilidad => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("Validador")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")) {
				// MainActivity.btnCarteraUne.setWRONG();
				resultado = result;
			} else {
				// MainActivity.btnCarteraUne.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("Direcciones")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			// System.out.println("Simulador 161 => " + observer);
			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")
					|| result.get(1).equals("Ha superado el limite de consultas por dia")
					|| result.get(1).equals("Consulta Hecha fuera del horario establecido")) {
				// MainActivity.btnCarteraUne.setWRONG();
				resultado = result;
			} else {
				// MainActivity.btnCarteraUne.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("ConsultaNodos")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			// System.out.println("Simulador 162 => " +
			// result.get(1).toString());

			if (result.get(1).equals("0") || result.get(1).equals("-1") || result.get(1).equals("Respuesta Vacia")
					|| result.get(1).equals("Ha superado el limite de consultas por dia")
					|| result.get(1).equals("Consulta Hecha fuera del horario establecido")) {
				// MainActivity.btnCarteraUne.setWRONG();
				resultado = result;
			} else {
				// MainActivity.btnCarteraUne.setOK();
				resultado = result;
			}
			notifyObserver();
		} else if (result.get(0).equals("ResultadoIVR")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			/*
			 * System.out.println("Simulador 161 => " + observer);
			 * System.out.println("result " + result);
			 */
			resultado = result;
			// System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("EstadoVenta")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			/*
			 * System.out.println("Simulador 161 => " + observer);
			 * System.out.println("result " + result);
			 */
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("EstadoGerencia")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			/*
			 * System.out.println("Simulador 161 => " + observer);
			 * System.out.println("result " + result);
			 */
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("BuscarBlindaje")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 BuscarBlindaje => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("ConsultaCodigoHogar")) {
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("CrearUneMas")) {
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("InsertarConfirmacion")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 InsertarConfirmacion => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("LanzarLlamada")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 LanzarLlamada => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("ConsultarRespuesta")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 Consultar Respuesta => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("actualizarBandera")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 Consultar Respuesta => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (accion.equals("ValidarLlamada")) {
			System.out.println("Simulador 161 Consultar Respuesta => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("RepetirCodigo")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 RepetirCodigo => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("ConsultaCodigos")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 ConsultaCodigos => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("PortafolioREDCO")) {
			// MainActivity.btnCarteraUne.setInvisible();
			// System.out.println(result);
			System.out.println("Simulador 161 PortafolioRedco => " + observer);
			System.out.println("result " + result);
			resultado = result;

			notifyObserver();
		} else if (result.get(0).equals("ValidarLogin")) {
			// MainActivity.btnPortafolio.setInvisible();
			System.out.println("Simulador 88 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("VerificarDireccion")) {
			// MainActivity.btnPortafolio.setInvisible();
			System.out.println("Simulador 88 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("ConsultarAgenda")) {
			// MainActivity.btnPortafolio.setInvisible();
			System.out.println("Simulador 88 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("ConsultarAgendaSiebel")) {
			System.out.println("Simulador 88 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("ConsolidarSiebel")) {
			System.out.println("Simulador 88 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (result.get(0).equals("TipoHogar")) {
			System.out.println("Simulador 88 => " + result.get(1));
			resultado = result;
			System.out.println("resultado " + resultado);
			notifyObserver();
		} else if (accion.equals("ConsultarVentas")) {
			System.out.println("Simulador 161 Consultar Respuesta => " + observer);
			System.out.println("result " + result);
			resultado = result;
			notifyObserver();
		} else if (accion.equals("validarPermiso")) {
			System.out.println("Simulador 161 Consultar Respuesta => " + observer);
			System.out.println("result " + result);
			resultado = result;
			notifyObserver();
		}else if (accion.equals("ProyectosRurales")) {
			System.out.println("Simulador Consultar Respuesta ProyectosRurales=> " + observer);
			System.out.println("result " + result);
			resultado = result;
			notifyObserver();
		}else if(accion.equals("consultarPagoParcialAnticipado")){
			System.out.println("Simulador 161 Consultar Respuesta => " + observer);
			System.out.println("result " + result);
			resultado = result;
			notifyObserver();
		}

	}

	public String CrearUneMas(ArrayList<String> parametros) {

		System.out.println("parametros " + parametros);
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "parametros", parametros.get(0) });

		return MainActivity.conexion.ejecutarSoap("CrearUneMas", param);

	}

	public String ConsultaCodigoHogar(ArrayList<String> parametros) {

		System.out.println("parametros " + parametros);
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "codHogar", parametros.get(0) });

		return MainActivity.conexion.ejecutarSoap("ConsultaCodHogar", param);

	}

	public String BuscarBlindaje(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "tipo", parametros.get(0) });
		param.add(new String[] { "busqueda", parametros.get(1) });
		param.add(new String[] { "codAsesor", parametros.get(2) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add("BlindajeNacional");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("BlindajeNacional", param);

	}

	public String consultarPortafolioREDCO(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Tipo", parametros.get(1) });
		param.add(new String[] { "Parametros", parametros.get(0) });

		return MainActivity.conexion.ejecutarSoap("PortafolioREDCO", param);

	}

	public String consultarPortafolio(String tipo, ArrayList<String> parametros) {

		JSONObject jo = new JSONObject();
		try {
			jo.put("strIdentificador", parametros.get(0));
			jo.put("strDireccion", parametros.get(1));
			jo.put("strCiudad", parametros.get(2));
			jo.put("strCedula", parametros.get(3));

		} catch (JSONException e) {
			Log.w("Error", e.getMessage());

		}

		// System.out.println("Simulador portafolio 186 => " + parametros);

		ArrayList<String[]> param = new ArrayList<String[]>();
		if (tipo.equalsIgnoreCase("identificador") && parametros.get(0) != null && !parametros.get(0).equals("")
				&& parametros.get(0).length() > 3) {
			param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
			param.add(new String[] { "Tipo", "identificador" });
			param.add(new String[] { "Parametros", jo.toString() });
		} else if (tipo.equalsIgnoreCase("direccion") && parametros.get(1) != null && !parametros.get(1).equals("")) {
			param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
			param.add(new String[] { "Tipo", "direccion" });
			param.add(new String[] { "Parametros", jo.toString() });
		} else if (tipo.equalsIgnoreCase("cedula") && parametros.get(3) != null && !parametros.get(3).equals("")) {
			param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
			param.add(new String[] { "Tipo", "cedula" });
			param.add(new String[] { "Parametros", jo.toString() });
		}
		return MainActivity.conexion.ejecutarSoap("Portafolio", param);

	}

	public String consultarConsolidarSiebel(String tipo, ArrayList<String> parametros, String salida) {
		JSONArray datos = new JSONArray(parametros);
		System.out.println(" datos " + datos);
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Tipo", tipo });
		try {
			param.add(new String[] { "Parametros", datos.get(0).toString() });
		} catch (JSONException e) {
			Log.w("error", e.getMessage());
		}
		param.add(new String[] { "Salida", salida });
		return MainActivity.conexion.ejecutarSoap("ConsolidadoSiebel", param);
	}

	public String consultarCobertura(String tipo, ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();

		System.out.println("tipo " + tipo);

		try {
			jo.put("strIdentificador", parametros.get(0));
			jo.put("strDireccion", parametros.get(1));
			jo.put("strCiudad", parametros.get(2));
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());

		}

		ArrayList<String[]> param = new ArrayList<String[]>();

		/*
		 * psoble problema al momento de consultar la cobertura, debido a que se
		 * da prioridad al identificador y si este lo encuentra sobre este busca
		 */

		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Tipo", tipo });
		param.add(new String[] { "Parametros", jo.toString() });
		param.add(new String[] { "Salida", "Cobertura" });

		// if (parametros.get(0) != null && !parametros.get(0).equals("")
		// && parametros.get(0).length() > 3) {
		// param.add(new String[] { "CodigoAsesor",
		// MainActivity.config.getCodigo() });
		// param.add(new String[] { "Tipo", "identificador" });
		// param.add(new String[] { "Parametros", jo.toString() });
		// param.add(new String[] { "Salida", "" });
		// } else if (parametros.get(1) != null &&
		// !parametros.get(1).equals("")) {
		// param.add(new String[] { "CodigoAsesor",
		// MainActivity.config.getCodigo() });
		// param.add(new String[] { "Tipo", "direccion" });
		// param.add(new String[] { "Parametros", jo.toString() });
		// param.add(new String[] { "Salida", "" });
		// }

		return MainActivity.conexion.ejecutarSoap("CoberturaNew", param);

	}

	public String consultarCuentasUne(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("strCodigoAsesor", parametros.get(0));
			jo.put("strCedula", parametros.get(1));
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Parametros", jo.toString() });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("Cartera_UNE");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("Cartera_UNE", param);

	}

	public String consultarEstadoCuenta(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Parametros", parametros.get(1) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("EstadoCuenta");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("EstadoCuenta", param);

	}

	public String datosScooring(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Parametros", parametros.get(1) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("DatosScooring");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("DatosScooring", param);

	}

	public String resultadoScooring(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "idScooring", parametros.get(1) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("ResultScooring");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("ResultScooring", param);

	}

	public String EstadoPedido(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("strPedido", parametros.get(0));
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "Parametros", jo.toString() });

		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
				new String[] { "lst_valor" }, "lst_clave = ?", new String[] { "Estado Pedido" }, null, null, null);

		System.out.println("resultado consulta " + resultado);
		if (resultado != null) {
			System.out.println("resultado.get(0).get(0) " + resultado.get(0).get(0));
			if (resultado.get(0).get(0).equalsIgnoreCase("NewEstadoPedido")) {
				System.out.println("NewEstadoPedido");
				return MainActivity.conexion.ejecutarSoap("NewEstadoPedido", param);
			} else {
				System.out.println("EstadoPedido");
				return MainActivity.conexion.ejecutarSoap("EstadoPedido", param);
			}
		} else {
			return MainActivity.conexion.ejecutarSoap("EstadoPedido", param);
		}

	}

	public String EstadoOferta(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("strIdOferta", parametros.get(0).toUpperCase());
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "Parametros", jo.toString() });
		return MainActivity.conexion.ejecutarSoap("EstadoOferta", param);

	}

	public String EstadoBlindaje(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "id", parametros.get(0) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("EstadoBlindaje");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("EstadoBlindaje", param);

	}

	public String EstadoVenta(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "codigo", MainActivity.config.getCodigo() });
		param.add(new String[] { "id", parametros.get(0) });
		param.add(new String[] { "login", parametros.get(1) });
		param.add(new String[] { "password", parametros.get(2) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("EstadoVenta");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("EstadoVenta", param);

	}

	public String EstadoGerencia(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "codigo", MainActivity.config.getCodigo() });
		param.add(new String[] { "id", parametros.get(0) });
		param.add(new String[] { "login", parametros.get(1) });
		param.add(new String[] { "password", parametros.get(2) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("EstadoGerencia");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("EstadoGerencia", param);

	}

	public String ResultadoIVR(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();
		String id = parametros.get(0);

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "Id", id });

		return MainActivity.conexion.ejecutarSoap("ResultadoIVR", param);

	}

	public String Validador(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("strCodigoAsesor", parametros.get(0));
			jo.put("strTipoIdentificacion", parametros.get(1));
			jo.put("strNumeroIdentificacion", parametros.get(2));
			jo.put("strCanal", "VM");
			// jo.put("strIp", "127.0.0.1");
			jo.put("strIp", Utilidades.getIPAddress(true));
			// jo.put("strUsuario", "gospinal@.com.co");
			jo.put("strUsuario", "asesor_" + parametros.get(0) + "@.com.co");
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}

		// System.out.println("parmaetros a enviar " + jo.toString());

		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "Parametros", jo.toString() });

		return MainActivity.conexion.ejecutarSoap("Validador", param);

	}

	public String Identificadores(ArrayList<String> parametros) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("strIdentificador", parametros.get(0));
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "Parametros", jo.toString() });

		return MainActivity.conexion.ejecutarSoap("Identificadores", param);

	}

	public String consultarDirecciones(ArrayList<String> parametros) {
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Cedula", parametros.get(0) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("Direcciones");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("Direcciones", param);

	}

	public String proyectosRurales(ArrayList<String> parametros) {
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "parametros", parametros.get(0) });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("ProyectosRurales");
		params.add(parametros);
		return MainActivity.conexion.ejecutarSoap("ProyectosRurales", param);

	}

	public String ConsultaNodos(ArrayList<String> parametros) {
		ArrayList<String[]> param = new ArrayList<String[]>();

		String departamento = MainActivity.config.getDepartamento();
		String municipio = MainActivity.config.getCiudad();

		if (departamento.equalsIgnoreCase("Bogota")) {
			departamento = "Cundinamarca";
		}

		if (municipio.equalsIgnoreCase("BogotaHFC")) {
			municipio = "Bogota D.C.";
		}

		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Nodo", parametros.get(0) });
		param.add(new String[] { "Departamento", departamento });
		param.add(new String[] { "Municipio", municipio });

		ArrayList<Object> params = new ArrayList<Object>();
		params.add("ConsultaNodos");

		// System.out.println("params -> "+param);
		return MainActivity.conexion.ejecutarSoap("ConsultaNodos", param);

	}

	public String InsertarConfirmacion(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "Parametros", parametros.get(0) });

		return MainActivity.conexion.ejecutarSoap("InsertarConfirmacion", param);

	}

	public String LanzarLlamada(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "Tipo", parametros.get(0) });
		param.add(new String[] { "Id", parametros.get(1) });
		param.add(new String[] { "Mail", parametros.get(2) });
		param.add(new String[] { "CobroDomingo", parametros.get(3) });
		param.add(new String[] { "ValorCargoConexion", parametros.get(4) });
		param.add(new String[] { "DescuentoCargoConexion", parametros.get(5) });
		param.add(new String[] { "PagoParcial", parametros.get(6) });

		return MainActivity.conexion.ejecutarSoap("LanzarLlamada", param);

	}

	public String consultarRespuesta(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "Accion", "consultar" });
		param.add(new String[] { "Tabla", "cifin" });
		param.add(new String[] { "Parametros", "{\"id\":\"" + parametros.get(0) + "\"}" });

		return MainActivity.conexion.ejecutarSoap("Ejecutar", param);
	}

	public String actualizarBandera(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "Accion", "actualizar" });
		param.add(new String[] { "Tabla", "bandera" });
		param.add(new String[] { "Parametros", "{\"id\":\"" + parametros.get(0) + "\"}" });

		return MainActivity.conexion.ejecutarSoap("Ejecutar", param);
	}

	public String consultarVentas(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "Accion", "consultar" });
		param.add(new String[] { "Tabla", "ventas" });
		param.add(new String[] { "Parametros",
				"{\"asesor\":\"" + parametros.get(0) + "\",\"telefono\":\"'" + parametros.get(1) + "'\"}" });

		return MainActivity.conexion.ejecutarSoap("Ejecutar", param);
	}

	public String validarLlamada(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "Accion", "validar" });
		param.add(new String[] { "Tabla", "tiempo_confirmacion" });
		param.add(new String[] { "Parametros",
				"{\"asesor\":\"" + parametros.get(0) + "\",\"telefono\":\"'" + parametros.get(1) + "'\"}" });

		return MainActivity.conexion.ejecutarSoap("Ejecutar", param);
	}

	public String RepetirCodigo(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "Accion", "confirmar" });
		param.add(new String[] { "Tabla", "ivr" });
		param.add(new String[] { "Parametros", "{\"id\":\"" + parametros.get(0) + "\"}" });
		return MainActivity.conexion.ejecutarSoap("Ejecutar", param);

	}

	public String ConsultaCodigos(ArrayList<String> parametros) {

		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "busqueda", parametros.get(0) });
		param.add(new String[] { "municipio", parametros.get(1) });
		param.add(new String[] { "tipo", parametros.get(2) });

		return MainActivity.conexion.ejecutarSoap("ConsultarCodigoHogar", param);

	}

	public String validarPermiso(ArrayList<String> parametros) {
		ArrayList<String[]> param = new ArrayList<String[]>();

		param.add(new String[] { "asesor", parametros.get(0) });
		param.add(new String[] { "accion", parametros.get(1) });

		return MainActivity.conexion.ejecutarSoap("validarPermisos", param);
	}

	public String validarLogin(ArrayList<String> parametros) {

		System.out.println("parametros " + parametros);
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "Parametros", parametros.get(0) });

		return MainActivity.conexion.ejecutarSoap("ValidarLogin", param);

	}

	public String verificarDireccion(ArrayList<String> parametros) {

		System.out.println("parametros " + parametros);
		ArrayList<String[]> param = new ArrayList<String[]>();
		param.add(new String[] { "Parametros", parametros.get(0) });

		return MainActivity.conexion.ejecutarSoap("VerificarDireccion", param);

	}

	public String ConsultarAgenda(ArrayList<String> parametros) {

		System.out.println("parametros " + parametros);
		ArrayList<String[]> param = new ArrayList<String[]>();
		// param.add(new String[] { "Parametros", parametros.get(0)[0] });
		for (int i = 0; i < parametros.size(); i++) {
			System.out.println("parametros " + parametros.get(i));

		}

		param.add(new String[] { "Departamento", parametros.get(0) });

		param.add(new String[] { "Municipio", parametros.get(1) });

		param.add(new String[] { "Barrio", parametros.get(2) });

		param.add(new String[] { "Estrato", parametros.get(3) });

		param.add(new String[] { "Productos", parametros.get(4) });

		param.add(new String[] { "Urbanizacion", parametros.get(5) });

		param.add(new String[] { "Cobertura", parametros.get(6) });

		param.add(new String[] { "Direccionalidad", parametros.get(7) });

		param.add(new String[] { "TipoCliente", parametros.get(8) });

		param.add(new String[] { "Paginacion", parametros.get(9) });

		param.add(new String[] { "AgendaDomingo", parametros.get(10) });

		// return "";
		return MainActivity.conexion.ejecutarSoap("ConsultarAgenda", param);

	}

	public String ConsultarAgendaSiebel(ArrayList<String> parametros) {

		System.out.println("parametros " + parametros);
		ArrayList<String[]> param = new ArrayList<String[]>();
		// param.add(new String[] { "Parametros", parametros.get(0)[0] });
		for (int i = 0; i < parametros.size(); i++) {
			System.out.println("parametros " + parametros.get(i));

		}

		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Parametros", parametros.get(0) });

		// return "";
		return MainActivity.conexion.ejecutarSoap("ConsultarAgendaSiebel", param);

	}

	public String ConsultarTipoHogar(ArrayList<String> parametros) {

		System.out.println("parametros " + parametros);
		ArrayList<String[]> param = new ArrayList<String[]>();
		// param.add(new String[] { "Parametros", parametros.get(0)[0] });
		for (int i = 0; i < parametros.size(); i++) {
			System.out.println("parametros " + parametros.get(i));

		}

		param.add(new String[] { "CodigoAsesor", MainActivity.config.getCodigo() });
		param.add(new String[] { "Parametros", parametros.get(0) });

		// return "";
		return MainActivity.conexion.ejecutarSoap("TipoHogar", param);

	}

	public String consultarPagoParcialAnticipado(ArrayList<String> parametros){

		System.out.println("parametros " + parametros);
		ArrayList<String[]> param = new ArrayList<String[]>();
		// param.add(new String[] { "Parametros", parametros.get(0)[0] });
		for (int i = 0; i < parametros.size(); i++) {
			System.out.println("parametros " + parametros.get(i));

		}

		param.add(new String[] { "parametros", parametros.get(0)});
		param.add(new String[] { "debug", "false" });
		param.add(new String[] { "profundidad", "0" });

		// return "";
		return MainActivity.conexion.ejecutarSoap("consultarPagoParcialAnticipado", param);

	}

	public void setManual(Context context) {
		this.lugar = "Manual";
		this.context = context;
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
		observer.update(resultado);
	}

}
