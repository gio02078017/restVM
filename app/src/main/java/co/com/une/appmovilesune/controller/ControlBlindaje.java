package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ListaAgendamiento;
import co.com.une.appmovilesune.adapters.ListaAgendamientoAdapter;
import co.com.une.appmovilesune.change.Busqueda;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.ResumenBlindaje;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Blindaje;
import co.com.une.appmovilesune.model.Cliente;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ControlBlindaje extends Activity implements Observer {

	private static final String DOC = "documento";
	private static final String TEL = "telefono";

	private TituloPrincipal tp;
	private Busqueda buscar;

	private RadioButton radio_doc_blindaje, radio_tel_blindaje;
	private LinearLayout llyDirecciones;

	private String tipo;

	private ArrayList<Blindaje> listaBlindaje;

	private JSONArray agendamiento;

	private Cliente cliente;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * El primer layout que ponemos es el del menu con los modulos posibles
		 * a ejecutar
		 */
		setContentView(R.layout.viewblindaje);

		tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);

		llyDirecciones = (LinearLayout) findViewById(R.id.llyDirecciones);

		radio_doc_blindaje = (RadioButton) findViewById(R.id.radio_doc_blindaje);
		radio_tel_blindaje = (RadioButton) findViewById(R.id.radio_tel_blindaje);

		buscar = (Busqueda) findViewById(R.id.busquedaBlindaje);
		buscar.boton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				consultarCliente();
			}
		});

		RadioButton.OnClickListener eventoRadios = new RadioButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (radio_doc_blindaje.isChecked()) {
					tipo = DOC;
					if (!cliente.getCedula().equals("")) {
						buscar.setBusqueda(cliente.getCedula());
					} else {
						buscar.setBusqueda("");
					}
				} else if (radio_tel_blindaje.isChecked()) {
					tipo = TEL;
					if (!cliente.getTelefono().equals("")) {
						String telefono = cliente.getTelefono();
						if (telefono.contains("(") && telefono.contains(")")) {
							telefono = telefono.substring(3, telefono.length());
							telefono = telefono.trim();
						}
						buscar.setBusqueda(telefono);
					} else {
						buscar.setBusqueda("");
					}
				}
				// System.out.println(tipo);
			}

		};

		radio_doc_blindaje.setOnClickListener(eventoRadios);
		radio_tel_blindaje.setOnClickListener(eventoRadios);

		Bundle reicieveParams = getIntent().getExtras();
		if (reicieveParams != null) {
			cliente = (Cliente) reicieveParams.getSerializable("cliente");
			if (!cliente.getTelefono().equals("") && cliente.getCedula().equals("")) {
				String telefono = cliente.getTelefono();
				if (telefono.contains("(") && telefono.contains(")")) {
					telefono = telefono.substring(3, telefono.length());
					telefono = telefono.trim();
				}
				buscar.setBusqueda(telefono);
				radio_tel_blindaje.setChecked(true);
				tipo = TEL;
			} else if (!cliente.getCedula().equals("") && cliente.getTelefono().equals("")) {
				buscar.setBusqueda(cliente.getCedula());
				radio_doc_blindaje.setChecked(true);
				tipo = DOC;
			} else if (!cliente.getCedula().equals("") && !cliente.getTelefono().equals("")) {
				buscar.setBusqueda(cliente.getCedula());
				radio_doc_blindaje.setChecked(true);
				tipo = DOC;
			}
		} else {
			// System.out.println("no cliente");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		tp.setTitulo("Blindaje Manizales");

	}

	// Metodo encargado de hacer la peticion al ws para traer la informacion de
	// los clientes creando un hilo.
	private void consultarCliente() {
		if (tipo != null) {
			if (!buscar.getBusqueda().equals("")) {
				ArrayList<String[]> parametros = new ArrayList<String[]>();
				parametros.add(new String[] { "tipo", tipo });
				parametros.add(new String[] { "identificador", buscar.getBusqueda() });

				ArrayList<Object> params = new ArrayList<Object>();
				params.add("ConsultarBlindaje");
				params.add(parametros);

				MainActivity.crearConexion();
				MainActivity.conexion.setManual(this);
				MainActivity.conexion.addObserver(this);
				MainActivity.conexion.execute(params);
			}

		} else {
			Toast.makeText(this, "Debe seleccionar el tipo de la consulta.", Toast.LENGTH_SHORT).show();
		}

	}

	public void agregarResumen(Blindaje blindaje) {
		// System.out.println("agregarResumen");
		final ResumenBlindaje rb = new ResumenBlindaje(this, blindaje);
		LayoutInflater li = LayoutInflater.from(rb.getContext());
		LinearLayout componente = (LinearLayout) li.inflate(R.layout.resumenblindaje, null);
		componente.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (!rb.blindaje.tieneOferta) {
					Intent intent = new Intent(MainActivity.MODULO_DETALLE_BLINDAJE);
					intent.putExtra("blindaje", rb.blindaje);
					if (agendamiento != null && rb.blindaje.agenda) {
						intent.putExtra("agenda", agendamiento.toString());
					}
					if (rb.blindaje.tieneRegalo) {
						Toast.makeText(rb.getContext(), "La Dirección Ya Tienen Un Regalo", Toast.LENGTH_SHORT).show();
					}
					startActivityForResult(intent, MainActivity.REQUEST_CODE);
				} else {
					Toast.makeText(rb.getContext(), "La Dirección Ya Tienen Una Oferta", Toast.LENGTH_SHORT).show();
				}
			}
		});
		llyDirecciones.addView(componente);

		TextView txtClienteDireccion = (TextView) componente.findViewById(R.id.txtDireccionCliente);
		TextView txtBarrioCliente = (TextView) componente.findViewById(R.id.txtBarrioCliente);
		TextView txtEstratoCliente = (TextView) componente.findViewById(R.id.txtEstratoCliente);
		TextView txtCiudadCliente = (TextView) componente.findViewById(R.id.txtCiudadCliente);
		TextView txtProductosCliente = (TextView) componente.findViewById(R.id.txtProductosCliente);
		TextView txtRegaloCliente = (TextView) componente.findViewById(R.id.txtRegaloCliente);
		TextView txtOferta1Cliente = (TextView) componente.findViewById(R.id.txtOferta1Cliente);
		TextView txtOferta2Cliente = (TextView) componente.findViewById(R.id.txtOferta2Cliente);

		txtClienteDireccion.setText(blindaje.direccion);
		txtBarrioCliente.setText(blindaje.barrio);
		txtEstratoCliente.setText(blindaje.estrato);
		txtCiudadCliente.setText(blindaje.ciudad);
		txtProductosCliente.setText(blindaje.paquete);
		txtRegaloCliente.setText(blindaje.Regalo_Inicial);
		txtOferta1Cliente.setText(blindaje.Oferta_1);
		txtOferta2Cliente.setText(blindaje.Oferta_2);

	}

	// procesa la informacion retornada por el ws, y genera los diferentes
	// blindajes que se ven para la busqueda
	private void procesarRespuesta(String respuesta) {
		// System.out.println("resultado blindaje "+respuesta);
		try {
			JSONObject jo = new JSONObject(respuesta);
			if (MainActivity.config.validarIntegridad(jo.getString("data"), jo.getString("crc"))) {
				String data = new String(Base64.decode(jo.getString("data")));
				System.out.println("data " + data);
				JSONObject jod = new JSONObject(data);
				System.out.println("jod " + jod);
				System.out.println("jod.getString(codigoRespuesta) " + jod.getString("codigoRespuesta"));
				if (jod.getString("codigoRespuesta").equalsIgnoreCase("00")) {
					JSONArray ja = jod.getJSONArray("datos");

					Editor editor = MainActivity.preferencias.edit();
					editor.putString("respuestaBlindaje", ja.toString());
					editor.commit();

					System.out.println("ja " + ja + "tamaño " + ja.length());
					System.out.println("tamaño " + ja.length());
					agendamiento = jod.getJSONArray("Agenda");
					System.out.println("agenda " + agendamiento.toString());
					listaBlindaje = new ArrayList<Blindaje>();
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jor = ja.getJSONObject(i);
						JSONArray jac = jor.getJSONArray("cliente");
						ArrayList<String[]> cliente = new ArrayList<String[]>();
						ArrayList<Boolean> validar = validarData(jor.getJSONObject("validacion"));
						System.out.println("validacion " + jor.getJSONObject("validacion") + " validar " + validar);

						for (int j = 0; j < jac.length(); j++) {
							JSONObject joc = jac.getJSONObject(j);
							cliente.add(new String[] { joc.get("tipoDocumento").toString(),
									joc.get("documento").toString(), joc.get("nombre").toString() });
						}
						Blindaje blindaje = new Blindaje(jor.getString("ma_id"), jor.getString("ciudad"),
								jor.getString("direccion"), jor.getString("estrato"), jor.getString("barrio"),
								jor.getString("comuna"), jor.getString("urbanizacion"), jor.getString("paquete"),
								jor.getString("plan_de_to"), jor.getString("to_coniva"),
								jor.getString("plancerrado_coniva"), jor.getString("plantv"),
								jor.getString("tv_masiptvconiva"), jor.getString("vel_fact"),
								jor.getString("ba_coniva"), jor.getString("gotica_coniva"),
								jor.getString("totalconiva_subsidios"), jor.getString("regalo_inicial1"),
								jor.getString("regalo_inicial2"), jor.getString("oferta1_final"),
								jor.getString("regalocompra_oferta1"), jor.getString("oferta1_coniva"),
								jor.getString("totalconiva_subsidios2"), jor.getString("oferta2"),
								jor.getString("regalocompra_oferta2"), jor.getString("oferta2_coniva"),
								jor.getString("totalivasubsidios_oferta2"), jor.getBoolean("agenda"),
								jor.getString("migracionred_regaloinicial"), jor.getString("migracionred_oferta1"),
								jor.getString("migraacionred_oferta2"), jor.getString("tipo_instalacion_oferta1"),
								jor.getString("tipo_instalacion_oferta2"), jor.getString("cambioequipo_ba"),
								jor.getString("incluyente_excluyente_regaloinicial1ofer1amg"),
								jor.getString("incluyente_excluyente_regaloinicial2afer1amg"),
								jor.getString("incluyente_excluyente_regaloinicial1of2"), cliente, validar.get(0),
								validar.get(1));
						listaBlindaje.add(blindaje);
					}
				} else {
					Dialogo dial = new Dialogo(this, Dialogo.DIALOGO_ALERTA, jod.getString("descripcionRespuesta"));
					dial.dialogo.show();
				}
			} else {
				// System.out.println("La informacion llego corrupta favor
				// consultar de nuevo.");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void listarResumenes() {
		// System.out.println("listarResumenes");
		llyDirecciones.removeAllViews();
		if (listaBlindaje != null) {
			for (int i = 0; i < listaBlindaje.size(); i++) {
				agregarResumen(listaBlindaje.get(i));
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MainActivity.REQUEST_CODE) {
			if (data != null) {
				if (data.hasExtra("blindaje")) {
					Blindaje blindaje = (Blindaje) data.getSerializableExtra("blindaje");
					Intent intent = new Intent();
					intent.putExtra("blindaje", blindaje);
					setResult(MainActivity.OK_RESULT_CODE, intent);
					finish();
				}
			}
		}
	}

	public ArrayList<Boolean> validarData(JSONObject validar) {
		ArrayList<Boolean> respuesta = new ArrayList<Boolean>();

		try {
			JSONObject oferta = validar.getJSONObject("oferta");
			JSONObject regalo = validar.getJSONObject("regalo");
			if (oferta.getString("codigoRespuesta").equalsIgnoreCase("00")) {
				respuesta.add(false);
			} else if (oferta.getString("codigoRespuesta").equalsIgnoreCase("01")) {
				respuesta.add(true);
			} else {
				respuesta.add(false);
			}

			if (regalo.getString("codigoRespuesta").equalsIgnoreCase("00")) {
				respuesta.add(false);
			} else if (regalo.getString("codigoRespuesta").equalsIgnoreCase("01")) {
				respuesta.add(true);
			} else {
				respuesta.add(false);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			respuesta.clear();
			respuesta.add(false);
			respuesta.add(false);
		}
		return respuesta;

	}

	// Metodo que recibe la respuesta del hilo que me consulta la info del
	// cliente
	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		ArrayList<Object> resultado = (ArrayList<Object>) value;
		System.out.println("resultado " + resultado);
		procesarRespuesta(resultado.get(1).toString());
		listarResumenes();
	}

}
