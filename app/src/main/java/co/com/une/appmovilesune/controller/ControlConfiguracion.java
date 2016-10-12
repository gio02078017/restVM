package co.com.une.appmovilesune.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.complements.Calendario;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ControlConfiguracion extends AsyncTask<String[], Integer, Boolean> implements Subject {

	Observer obsr;
	ProgressDialog pd;

	boolean autenticado = false;

	private Dialogo dialogo;
	private Spinner sltDepartamento, sltCiudad;
	private String lanzador;

	Context context;

	@Override
	protected void onPreExecute() {
		pd = ProgressDialog.show(context, "Consultando", "Autenticando Usuario....");
	}

	@Override
	protected Boolean doInBackground(String[]... params) {
		// TODO Auto-generated method stub
		// System.out.println("params "+params);
		return autenticar(params[0][0], params[0][1]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		autenticado = result;
		System.out.println(autenticado);
		pd.dismiss();
		notifyObserver();
	}

	public ControlConfiguracion() {
		if (MainActivity.basedatos
				.consultar(false, "Departamentos", new String[] { "count(*)" }, null, null, null, null, null).get(0)
				.get(0).equals("0")) {
			// System.out.println("Reinicio ciudades");
			Editor editor = MainActivity.preferencias.edit();
			editor.putBoolean("cfgCiudades", false);
			editor.commit();
		}

		MainActivity.config.precargarConfiguraciones();
	}

	public boolean autenticar(String codigo, String documento) {
		boolean respuesta = false;
		Editor editor = MainActivity.preferencias.edit();
		/*
		 * if (MainActivity.config.autenticarBaseDatos(codigo, documento)) {
		 * //System.out.println("Autenticado por base de datos");
		 * editor.putBoolean("session", true); // System.out.println(
		 * "Ciudad CCF  42 => "+MainActivity.config.getCiudad());
		 * MainActivity.config .consultarConfiguracionCiudad(MainActivity.config
		 * .getCiudad()); // editor.putLong("time", Calendario.getTimestamp());
		 * respuesta = true; } else
		 */ 
		if (MainActivity.config.autenticarWebService(codigo, documento)) {
			// System.out.println("Autenticado por webservice");
			editor.putBoolean("session", true);
			// System.out.println("Ciudad CCF 42 =>
			// "+MainActivity.config.getCiudad());
			MainActivity.config.consultarConfiguracionCiudad(MainActivity.config.getCiudad());
			editor.putLong("time", Calendario.getTimestamp());
			
			if(MainActivity.config.obtenerPermisos()){
				respuesta = true;
				System.out.println("Validar permiso "+Utilidades.validarPermiso("carrusel"));
			}else{
				editor.putBoolean("session", false);
			}
			
		} else {
			// System.out.println("No Autenticado");
			editor.putBoolean("session", false);
		}
		editor.commit();
		return respuesta;
	}

	public void mostrarDialogoConfiguracion(Context context) {
		dialogo = new Dialogo(context, Dialogo.DIALOGO_CONFIGURACION, "");
		dialogo.dialogo.setOnDismissListener(dl);
		dialogo.dialogo.show();

		sltDepartamento = (Spinner) dialogo.dialogo.findViewById(R.id.sltDepartamento);
		sltCiudad = (Spinner) dialogo.dialogo.findViewById(R.id.sltCiudad);
		/*
		 * System.out.println("MainActivity.config.getDepartamentos() " +
		 * MainActivity.config.getDepartamentos());
		 */
		if (MainActivity.config.getDepartamentos() != null) {
			ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
					MainActivity.config.getDepartamentos());
			sltDepartamento.setAdapter(adaptador);
			// System.out.println(MainActivity.config.getDepartamento());
			sltDepartamento.setSelection(adaptador.getPosition(MainActivity.config.getDepartamento()));
			sltDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
					// System.out.println("Seleccion de departamento =>
					// "+parent.getSelectedItem().toString());
					llenarCiudades(parent.getSelectedItem().toString());
				}

				public void onNothingSelected(AdapterView<?> parent) {
					// lblMensaje.setText("");
				}
			});
		}
	}

	public void llenarCiudades(String departamento) {
		// System.out.println("Deparamenteo => " +departamento);
		ArrayAdapter<String> adaptador = dialogo.fc.obtenerCiudades(departamento);
		sltCiudad.setAdapter(adaptador);
		sltCiudad.setSelection(adaptador.getPosition(MainActivity.config.getCiudad()));

	}

	OnDismissListener dl = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {
			// TODO Auto-generated method stub
			if (dialogo.isSeleccion()) {
				MainActivity.config.setDepartamento(dialogo.getConfiguracion()[0]);
				MainActivity.config.setCiudad(dialogo.getConfiguracion()[1]);
				MainActivity.config.consultarConfiguracionCiudad(dialogo.getConfiguracion()[1]);
				MainActivity.config.guardarConfiguraciones();
				MainActivity.config.precargarConfiguraciones();
			}
			dialogo.setSeleccion(false);
		}
	};

	public String getLanzador() {
		return lanzador;
	}

	public void setLanzador(String lanzador) {
		this.lanzador = lanzador;
	}

	public void setContext(Context context) {
		this.context = context;
	}

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
		obsr.update(autenticado);
	}

}
