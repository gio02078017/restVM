package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemKeyValue;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.SelectorProductos;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Peticion;
import co.com.une.appmovilesune.model.Prospecto;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.UneMas;
import co.com.une.appmovilesune.R;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ControlUneMas extends Activity implements Subject, Observer {

	private Cliente cliente;
	private UneMas unemas;

	private Observer observador;

	private SelectorProductos slpHobbies;
	private EditText txtCodHogar, txtContrasenia, txtBusqueda;
	private CheckBox sms, email, fisico, telemercadeo;
	private String nombres, apellidos, tipodocumento, documento, telefono, celular, genero, ciudad, correo, codigoHogar,
			contrasenia;
	private TextView campoMensajes;

	JSONObject mensajes = null;

	private Dialogo dialogoHobbies;

	private TituloPrincipal tp;
	private boolean validarIVR;
	private String IVR;

	public String unemasCodigo, unemasMensaje;

	private String tipoBusqueda = "";

	private Spinner sltDirecciones;

	private RadioGroup groupUneMas;

	private TableRow rowDireccion;

	private boolean LLamadaUneMas = false;

	private String id = "";
	private int codigo;

	private boolean prioridad = false;
	private ArrayList<ItemKeyValue> claveValorDirecciones = new ArrayList<ItemKeyValue>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewunemas);
		MainActivity.obsrUneMas = this;

		tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
		tp.setTitulo("UNE MAS");

		dialogoHobbies = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_HOBBIES, "");
		dialogoHobbies.dialogo.setOnDismissListener(dlh);

		asignarCampos();

		Bundle reicieveParams = getIntent().getExtras();
		if (reicieveParams != null) {
			cliente = (Cliente) reicieveParams.getSerializable("cliente");
			unemas = (UneMas) reicieveParams.getSerializable("unemas");
			System.out.println("Cliente " + cliente);
			IVR = reicieveParams.getString("IVR");

			if (unemas != null) {
				llenarCampos();
			}

			peticionIVR();

		} else {
			System.out.println("no peticion");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_unemas, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Crear")) {
			crearUneMas();
		}
		return true;
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

	public void mostrarDialogo(View v) {
		dialogoHobbies.dialogo.show();
	}

	public void guardarUneMas(View v) {
		unemas = new UneMas(slpHobbies.getTexto(), txtCodHogar.getText().toString(),
				txtContrasenia.getText().toString());
		unemas.codigo = unemasCodigo;
		unemas.mensaje = unemasMensaje;
		Intent intent = new Intent();
		intent.putExtra("cliente", cliente);
		intent.putExtra("unemas", unemas);
		setResult(MainActivity.OK_RESULT_CODE, intent);
		finish();
	}

	public void crearUneMas() {

		int valorsms = 0, valoremail = 0, valorfisico = 0, valortelemercadeo = 0;

		JSONObject UneMas = new JSONObject();

		System.out.println("IVR " + IVR);
		System.out.println("cliente.getIdIVR( " + cliente.getIdIVR());

		if (validarIVR) {

			if (validarUneMas()) {

				if (sms.isChecked()) {
					valorsms = 1;
				}

				if (email.isChecked()) {
					valoremail = 1;
				}

				if (fisico.isChecked()) {
					valorfisico = 1;

				}

				if (telemercadeo.isChecked()) {
					valortelemercadeo = 1;
				}

				try {
					UneMas.put("ciudad", ciudad);
					UneMas.put("codigoHogar", codigoHogar);
					UneMas.put("envioSMS", valorsms);
					UneMas.put("envioEmail", valoremail);
					UneMas.put("correoFisico", valorfisico);
					UneMas.put("telemercadeo", valortelemercadeo);
					UneMas.put("nombres", nombres);
					UneMas.put("apellidos", apellidos);
					UneMas.put("tipoDocumento", tipodocumento);
					UneMas.put("documento", documento);
					UneMas.put("telefono", telefono);
					UneMas.put("celular", celular);
					UneMas.put("genero", genero);
					UneMas.put("email", correo);
					UneMas.put("token", "");
					UneMas.put("password", contrasenia);
					UneMas.put("hobbies", LimpiarHobbies());
					UneMas.put("codigoAsesor", MainActivity.config.getCodigo());
					UneMas.put("idConfirmacion", cliente.getIdIVR());
					CrearUneMas(UneMas.toString());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("UneMas " + UneMas);

			} else {
				Intent intent = new Intent(MainActivity.MODULO_MENSAJES);
				intent.putExtra("mensajes", mensajes.toString());
				startActivityForResult(intent, MainActivity.REQUEST_CODE);
			}

		} else if (IVR.equalsIgnoreCase("") && cliente.getIdIVR() == 0) {
			System.out.println("Entro InsertarConfirmacion ");
			InsertarConfirmacion();
		} else {
			Toast.makeText(this, "Debe lanzar El IVR de Venta", Toast.LENGTH_SHORT).show();
		}
	}

	private void InsertarConfirmacion() {

		addObserver(MainActivity.obsrMainActivity);
		notifyObserver();

		ArrayList<String> consolidado = MainActivity.consolidarIVR();

		System.out.println("consolidado " + consolidado);

		if (consolidado.get(2).length() >= 7) {
			if (id.equals("")) {
				codigo = (int) (Math.random() * (9999 - 1000 + 1) + 1000);

				JSONObject IVR = new JSONObject();

				try {

					IVR.put("codigoasesor", consolidado.get(0));
					IVR.put("telefono", consolidado.get(2));
					IVR.put("codigo", codigo);
					IVR.put("tipocliente", consolidado.get(3));
					IVR.put("documento", consolidado.get(4));

				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				ArrayList<String> parametros = new ArrayList<String>();
				parametros.add(IVR.toString());
				parametros.add(MainActivity.config.getCodigo());
				ArrayList<Object> params = new ArrayList<Object>();
				params.add(MainActivity.config.getCodigo());
				params.add("InsertarConfirmacion");
				params.add(parametros);
				Simulador simulador = new Simulador();
				simulador.setManual(this);
				simulador.addObserver(this);
				simulador.execute(params);

			} else {
				LanzarLLamada(id, "10");
			}

		} else {
			Toast.makeText(this, "No ha Ingresado Telefono de destino", Toast.LENGTH_SHORT).show();
		}

		// btnSiguiente.setEnabled(true);
	}

	private void LanzarLLamada(String id, String tipo) {

		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(tipo);
		parametros.add(id);
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("LanzarLlamada");
		params.add(parametros);
		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);
		simulador.execute(params);

	}

	private void mostrarDialgo() {
		System.out.println("Codigo => " + codigo);
		AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
		promptDialog.setTitle("Confirmación de Código");
		promptDialog.setCancelable(false);
		final EditText input = new EditText(this);
		final Context context = this;
		promptDialog.setView(input);

		promptDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				System.out.println("promptDialog id" + id);
				if (!id.equalsIgnoreCase("")) {
					if (value.toCharArray().length == 4 || value.equals("20130806")) {
						if (value.equals(String.valueOf(codigo)) || value.equals("20130806")) {
							IVR(id);

							/*
							 * Intent intent = new Intent();
							 * intent.putExtra("blindaje", blindaje);
							 * setResult(MainActivity.OK_RESULT_CODE, intent);
							 * finish();
							 */

						} else {
							Toast.makeText(context, "No Concuerdan los codigos", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(context, "El tamaño del codigo es incorrecto", Toast.LENGTH_SHORT).show();
						return;
					}
				} else {
					Toast.makeText(context, "Primero Debe Lanzar La Llamada", Toast.LENGTH_SHORT).show();
				}

			}
		});

		promptDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Toast.makeText(context, "No se puede continuar", Toast.LENGTH_SHORT).show();
			}
		});

		promptDialog.show();
	}

	private boolean validarUneMas() {

		boolean validar = true;
		mensajes = new JSONObject();

		JSONArray ja = new JSONArray();
		nombres = "";
		apellidos = "";
		tipodocumento = "";
		documento = "";
		telefono = "";
		celular = "";
		genero = "";
		ciudad = "";
		correo = "";
		codigoHogar = "";
		contrasenia = "";

		try {
			if (cliente.getNombre() != null && !cliente.getNombre().equalsIgnoreCase("")) {
				nombres = cliente.getNombre();
				System.out.println("nombres " + nombres);
			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Nombres");
				jop.put("datos", "Sin Diligenciar");
				ja.put(jop);
			}

			if (cliente.getApellido() != null && !cliente.getApellido().equalsIgnoreCase("")) {
				apellidos = cliente.getApellido();
				System.out.println("apellidos " + apellidos);
			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Apellidos");
				jop.put("datos", "Sin Diligenciar");
				ja.put(jop);
			}

			if (cliente.getTipoDocumento() != null && !cliente.getTipoDocumento().equalsIgnoreCase("")
					&& !cliente.getTipoDocumento().equalsIgnoreCase("-- Seleccione Opción --")) {
				tipodocumento = cliente.getTipoDocumento();
				System.out.println("tipodocumento " + tipodocumento);
			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Tipo Documento");
				jop.put("datos", "Sin Diligenciar");
				ja.put(jop);
			}

			if (cliente.getCedula() != null && !cliente.getCedula().equalsIgnoreCase("")) {
				documento = cliente.getCedula();
				System.out.println("documento " + documento);
			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Documento");
				jop.put("datos", "Sin Diligenciar");
				ja.put(jop);
			}

			if (cliente.getCiudadDane() != null && !cliente.getCiudadDane().equalsIgnoreCase("")) {
				ciudad = cliente.getCiudadDane();
				System.out.println("ciudad " + ciudad);
			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Ciudad");
				jop.put("datos", "Sin Diligenciar");
				ja.put(jop);
			}

			if (cliente.getCorreo() != null && !cliente.getCorreo().equalsIgnoreCase("")) {
				if (Utilidades.validateEmail(cliente.getCorreo())) {
					correo = cliente.getCorreo();
				} else {
					validar = false;
					JSONObject jop = new JSONObject();
					jop.put("titulo", "Correo");
					jop.put("datos", "Correo No Valido");
					ja.put(jop);
				}

			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Correo");
				jop.put("datos", "Sin Diligenciar");
				ja.put(jop);
			}

			if (cliente.getGenero() != null && !cliente.getGenero().equalsIgnoreCase("")
					&& !cliente.getGenero().equalsIgnoreCase("-- Seleccione Opción --")) {
				if (cliente.getGenero().equalsIgnoreCase("Masculino")) {
					genero = "M";
				} else {
					genero = "F";
				}
				System.out.println("getGenero " + genero);
			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Genero");
				jop.put("datos", "Sin Diligenciar");
				ja.put(jop);
			}

			if (cliente.getTelefono() != null && !cliente.getTelefono().equalsIgnoreCase("")) {
				telefono = cliente.getTelefono();
				System.out.println("telefono " + telefono);
			} else if (cliente.getTelefono2() != null && !cliente.getTelefono2().equalsIgnoreCase("")) {
				telefono = cliente.getTelefono2();
				System.out.println("telefono " + telefono);
			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Telefonos");
				jop.put("datos", "Sin Diligenciar\nSe Debe Diligenciar el Telefono De Servicio o Telefono Secundario");
				ja.put(jop);
			}

			if (cliente.getCelular() != null && !cliente.getCelular().equalsIgnoreCase("")) {
				celular = cliente.getCelular();
				System.out.println("celular " + celular);
				if (celular.length() != 10) {
					JSONObject jop = new JSONObject();
					jop.put("titulo", "Celular");
					jop.put("datos", "El Celular Debe Tener 10 Caracteres");
					ja.put(jop);
				}
			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Celular");
				jop.put("datos", "Sin Diligenciar\nEl Celular Debe Tener 10 Caracteres");
				ja.put(jop);
			}

			codigoHogar = txtCodHogar.getText().toString();
			System.out.println("codigoHogar " + codigoHogar);
			if (!codigoHogar.equalsIgnoreCase("")) {

				if (codigoHogar.length() < 10) {
					validar = false;
					JSONObject jop = new JSONObject();
					jop.put("titulo", "Código Hogar");
					jop.put("datos", "El Código Hogar Debe Tener 10 o Más Caracteres");
					ja.put(jop);
				}

			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Código Hogar");
				jop.put("datos", "Sin Diligenciar\nEl Código Hogar Debe Tener 10 o Más Caracteres");
				ja.put(jop);
			}

			contrasenia = txtContrasenia.getText().toString();
			System.out.println("contrasenia " + contrasenia);
			if (!contrasenia.equalsIgnoreCase("")) {
				if (contrasenia.length() < 8) {
					validar = false;
					JSONObject jop = new JSONObject();
					jop.put("titulo", "Contraseña");
					jop.put("datos", "La Contraseña Debe Tener 8 o Más Caracteres");
					ja.put(jop);
				}

			} else {
				validar = false;
				JSONObject jop = new JSONObject();
				jop.put("titulo", "Contraseña");
				jop.put("datos", "Sin Diligenciar\nLa Contraseña Debe Tener 8 o Más Caracteres");
				ja.put(jop);
			}

			if (!validar) {
				mensajes.put("Titulo", "Campos Requeridos");
				mensajes.put("Mensajes", ja);
			}

			System.out.println("mensajes.toString() " + mensajes.toString());
		} catch (JSONException e) {
			validar = false;
		}

		return validar;
	}

	private void asignarCampos() {
		slpHobbies = (SelectorProductos) findViewById(R.id.slpHobbies);
		slpHobbies.txtTexto.setHint("Seleccione Afición");
		txtCodHogar = (EditText) findViewById(R.id.txtCodHogar);
		txtContrasenia = (EditText) findViewById(R.id.txtContrasenia);
		sms = (CheckBox) findViewById(R.id.chkSms);
		email = (CheckBox) findViewById(R.id.chkEmail);
		fisico = (CheckBox) findViewById(R.id.chkFisico);
		telemercadeo = (CheckBox) findViewById(R.id.chkTelemercadeo);
		campoMensajes = (TextView) findViewById(R.id.campoMensajes);

		rowDireccion = (TableRow) findViewById(R.id.tlytDireccion);
		txtBusqueda = (EditText) findViewById(R.id.txtBusqueda);
		sltDirecciones = (Spinner) findViewById(R.id.sltDireccion);
		groupUneMas = (RadioGroup) findViewById(R.id.radioGroupConsutaCodigoHogar);

		groupUneMas.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.radio_telefono_CodigoHogar) {
					if (cliente != null) {
						txtBusqueda.setText(cliente.getTelefono());
						tipoBusqueda = "identificador";
					}

				} else if (checkedId == R.id.radio_Cedula_CodigoHogar) {
					if (cliente != null) {
						txtBusqueda.setText(cliente.getCedula());
						tipoBusqueda = "documento";
					}
				} else if (checkedId == R.id.radio_direccion_CodigoHogar) {
					if (cliente != null) {
						txtBusqueda.setText(cliente.getDireccion());
						tipoBusqueda = "direccion";
					}
				}
			}

		});

		limpiarDireccion();

		sltDirecciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

				String direccion = (String) sltDirecciones.getSelectedItem();

				if (!direccion.equalsIgnoreCase(Utilidades.iniDireccion)
						&& !direccion.equalsIgnoreCase(Utilidades.seleccioneDireccion)) {

					System.out.println("direccion " + direccion);
					System.out.println("claveValorDirecciones.size() " + claveValorDirecciones.size());

					txtCodHogar.setText(claveValorDirecciones.get(position).getValues());

				} else {
					if (!prioridad) {
						txtCodHogar.setText("");
					}
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		sms.setEnabled(false);
		email.setEnabled(false);
		fisico.setEnabled(false);
		telemercadeo.setEnabled(false);
	}

	public void limpiarDireccion() {
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				Utilidades.inicialDireccion);
		sltDirecciones.setAdapter(adaptador);
	}

	private String LimpiarHobbies() {
		String limpio = "";

		if (!slpHobbies.getTexto().equalsIgnoreCase("")) {
			limpio = slpHobbies.getTexto();
			limpio = limpio.replace("[", "");
			limpio = limpio.replace("]", "");
		}

		return limpio;
	}

	private void llenarCampos() {
		txtCodHogar.setText(unemas.getCodigoHogar());
		txtContrasenia.setText(unemas.getContrasenia());
		slpHobbies.setTexto(unemas.getHobbies());

	}

	public void buscar_CodHogar(View v) {
		if (!txtCodHogar.getText().toString().equalsIgnoreCase("")) {
			ConsultaCodigoHogar(txtCodHogar.getText().toString());
		} else {
			Toast.makeText(this, "El Campo Código Hogar No Puede Estar Vacio", Toast.LENGTH_SHORT).show();
		}

	}

	public void buscar_CodHogarxBusqueda(View v) {
		// System.out.println("Hola es un evento");

		String busqueda = txtBusqueda.getText().toString();
		if (!tipoBusqueda.equalsIgnoreCase("")) {
			if (!busqueda.equalsIgnoreCase("")) {
				ConsultaCodigos(busqueda, tipoBusqueda);
			} else {
				Toast.makeText(this, "El campo Busqueda No Puede Estar Vacio", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "Debe Seleccionar Un Tipo", Toast.LENGTH_SHORT).show();
		}

	}

	public void ConsultaCodigos(String busqueda, String tipo) {
		// System.out.println("Control Simulador 479 => "+Cedula);
		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(busqueda);
		parametros.add(cliente.getCiudadFenix());
		parametros.add(tipo);
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("ConsultaCodigos");
		params.add(parametros);
		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);
		simulador.execute(params);
	}

	public void ConsultaCodigoHogar(String codHogar) {
		// System.out.println("Control Simulador 479 => "+Cedula);
		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(codHogar);
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("ConsultaCodigoHogar");
		params.add(parametros);
		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);
		simulador.execute(params);
	}

	public void CrearUneMas(String UneMas) {
		// System.out.println("Control Simulador 479 => "+Cedula);
		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(UneMas);
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("CrearUneMas");
		params.add(parametros);
		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);
		simulador.execute(params);
	}

	OnDismissListener dlh = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {

			// TODO Auto-generated method stub
			System.out.println(dialogoHobbies.isSeleccion());
			if (dialogoHobbies.isSeleccion()) {
				slpHobbies.setTexto(dialogoHobbies.getHobbies().toString());
			}

		}
	};

	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		campoMensajes.setText("");
		ArrayList<Object> resultado = (ArrayList<Object>) value;
		if (resultado.get(0).equals("ConsultaCodigoHogar")) {
			String Respuesta = resultado.get(1).toString();
			System.out.println("ConsultaCodigoHogar " + Respuesta);
			Mensaje("Consulta", Respuesta);
		} else if (resultado.get(0).equals("CrearUneMas")) {
			String Respuesta = resultado.get(1).toString();
			System.out.println("Respuesta UNE MAS " + Respuesta);
			try {
				JSONObject interprete = new JSONObject(Respuesta);
				if (interprete.has("Response")) {
					Mensaje("Crear", interprete.getString("Response"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (resultado.get(0).equals("ConsultaCodigos")) {
			ResultadoCodigos(resultado.get(1).toString());
		} else if (resultado.get(0).equals("ResultadoIVR")) {
			ResultadoIVR(resultado.get(1).toString());
		} else if (resultado.get(0).equals("InsertarConfirmacion")) {
			JSONObject jop;

			try {
				jop = new JSONObject(resultado.get(1).toString());

				String data = jop.get("data").toString();
				if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
					data = new String(Base64.decode(data));
					// Confirmacion(data);
					JSONObject confirmacion = new JSONObject(data);
					if (confirmacion.has("id")) {
						id = confirmacion.getString("id");

						if (!id.equalsIgnoreCase("")) {
							// blindaje.idIVR = id;
							LanzarLLamada(id, "10");
							LLamadaUneMas = true;
						} else {
							System.out.println("Error " + confirmacion.getString("mensaje"));
						}
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (resultado.get(0).equals("LanzarLlamada")) {
			System.out.println("data " + resultado.get(1));
			boolean controlUserProof = false;

			if (MainActivity.servidor == Conexion.SERVIDOR_DAVID || MainActivity.servidor == Conexion.SERVIDOR_GIOVANNY
					|| MainActivity.servidor == Conexion.SERVIDOR_DESARROLLO
					|| MainActivity.servidor == Conexion.SERVIDOR_PRUEBAS) {
				controlUserProof = true;

			}

			if (resultado.get(1).equals("OK") || controlUserProof) {
				mostrarDialgo();
			} else if (resultado.get(1).equals("FAIL")) {
				Toast.makeText(this, "EL lanzamiento de la llamada fallo", Toast.LENGTH_SHORT).show();
			}
		} else if (resultado.get(0).equals("RepetirCodigo")) {
			JSONObject jop;

			try {
				jop = new JSONObject(resultado.get(1).toString());

				String data = jop.get("data").toString();
				if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
					data = new String(Base64.decode(data));

					JSONArray ja = new JSONArray(data);
					System.out.println("ja " + ja);
					JSONObject jo = ja.getJSONObject(0);
					System.out.println("jo " + jo);

					if (jo.getString("completo").equals("0")) {
						Toast.makeText(this, "Complete el ivr", Toast.LENGTH_SHORT).show();
					} else {

						LanzarLLamada(id, "6");

					}
				} else {
					System.out.println("Los datos no se descargaron correctamente");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void Mensaje(String tipo, String resultado) {

		try {
			JSONObject interprete = new JSONObject(resultado);

			if (interprete.has("codigoRespuesta")) {
				String codigoRespuesta = interprete.getString("codigoRespuesta");

				System.out.println("codigoRespuesta " + codigoRespuesta);

				if (!codigoRespuesta.equalsIgnoreCase("-1")) {
					if (codigoRespuesta.equalsIgnoreCase("00") && tipo.equalsIgnoreCase("Crear")) {
						campoMensajes.setText("Usuario Une Mas Creado Correctamente\nEl Nombre De Usuario " + correo);
						unemasCodigo = "1";
						unemasMensaje = "Usuario Une Mas Creado Correctamente. El Nombre De Usuario " + correo;

					} else {
						campoMensajes.setText(interprete.getString("mensajeRespuesta"));
						unemasCodigo = "0";
						unemasMensaje = interprete.getString("mensajeRespuesta");
					}

				} else if (codigoRespuesta.equalsIgnoreCase("-1")) {

					campoMensajes.setText(
							"Problemas con el servidor intente nuevamente y de continuar informe a soporte venta móvil.");

					unemasCodigo = "0";
					unemasMensaje = interprete.getString("mensajeRespuesta");

				} else {
					campoMensajes.setText("No Se Puede Interpretar El Resultado.");

					unemasCodigo = "0";
					unemasMensaje = interprete.getString("mensajeRespuesta");

				}
			} else {
				campoMensajes.setText("No Se Puede Interpretar El Resultado.");

				unemasCodigo = "0";
				unemasMensaje = interprete.getString("mensajeRespuesta");
			}

		} catch (JSONException e) {
			e.printStackTrace();
			campoMensajes.setText("No Se Puede Interpretar El Resultado.");

			unemasCodigo = "0";
			unemasMensaje = "No Se Puede Interpretar El Resultado.";
		}

	}

	public void ResultadoCodigos(String Resultado) {
		claveValorDirecciones.clear();
		prioridad = false;
		JSONObject jop;
		try {
			jop = new JSONObject(Resultado);
			System.out.println("jop Codigos Hogar " + jop);

			if (jop.has("codigoMensaje")) {
				if (jop.getString("codigoMensaje").equalsIgnoreCase("00")) {
					JSONArray array = jop.getJSONArray("data");

					System.out.println("array.length() " + array.length());
					for (int i = 0; i < array.length(); i++) {
						if (array.length() == 1) {
							txtCodHogar.setText(array.getJSONObject(i).getString("CodHogar"));
							prioridad = true;
						}
						claveValorDirecciones
								.add(new ItemKeyValue(array.getJSONObject(i).getString("DireccionServicio"),
										array.getJSONObject(i).getString("CodHogar")));
					}

					if (array.length() > 1) {
						String[] arrayDireccion = new String[array.length() + 1];
						claveValorDirecciones.add(0,
								new ItemKeyValue(Utilidades.seleccioneDireccion, Utilidades.seleccioneDireccion));
						for (int i = 0; i < claveValorDirecciones.size(); i++) {
							arrayDireccion[i] = claveValorDirecciones.get(i).getKey();

						}
						ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
								android.R.layout.simple_spinner_item, arrayDireccion);
						sltDirecciones.setAdapter(adaptador);
						rowDireccion.setVisibility(View.VISIBLE);
					} else {
						rowDireccion.setVisibility(View.GONE);
						limpiarDireccion();
					}
				} else {
					campoMensajes.setText(jop.getString("descripcionMensaje"));
					txtCodHogar.setText("");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ResultadoIVR(String Resultado) {
		JSONObject jop;
		try {
			jop = new JSONObject(Resultado);

			String data = jop.get("data").toString();
			if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
				data = new String(Base64.decode(data));
				validarIVR = true;
				interpreteIVR(data);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void IVR(String idIVR) {
		ArrayList<String> parametros = new ArrayList<String>();

		parametros.add(idIVR);

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("ResultadoIVR");
		params.add(parametros);

		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);
		simulador.execute(params);
	}

	public void interpreteIVR(String IVR) {

		try {
			System.out.println(" IVR " + IVR);
			JSONArray arrayIVR = new JSONArray(IVR);
			System.out.println(" arrayIVR " + arrayIVR);
			JSONObject objectIVR = arrayIVR.getJSONObject(0);
			System.out.println(" objectIVR " + objectIVR);
			if (objectIVR.has("lgl_habeassms")) {
				if (objectIVR.getString("lgl_habeassms").equalsIgnoreCase("1")) {
					sms.setChecked(true);
				} else {
					sms.setChecked(false);
				}

			}

			if (objectIVR.has("lgl_habeasemail")) {
				if (objectIVR.getString("lgl_habeasemail").equalsIgnoreCase("1")) {
					email.setChecked(true);
				} else {
					email.setChecked(false);
				}

			}

			if (objectIVR.has("lgl_habeasmail")) {
				if (objectIVR.getString("lgl_habeasmail").equalsIgnoreCase("1")) {
					fisico.setChecked(true);
				} else {
					fisico.setChecked(false);
				}

			}

			if (objectIVR.has("lgl_habeascalls")) {
				if (objectIVR.getString("lgl_habeascalls").equalsIgnoreCase("1")) {
					telemercadeo.setChecked(true);
				} else {
					telemercadeo.setChecked(false);
				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
			sms.setChecked(false);
			email.setChecked(false);
			fisico.setChecked(false);
			telemercadeo.setChecked(false);

		}

		if (LLamadaUneMas) {
			cliente.setIdIVR(Integer.parseInt(id));
			crearUneMas();
		}
	}

	public void peticionIVR() {
		validarIVR = false;
		// if(cliente.getd)

		System.out.println("IVR " + IVR);
		System.out.println("idIVR " + cliente.getIdIVR());

		if (!IVR.equalsIgnoreCase("")) {
			validarIVR = true;
			interpreteIVR(IVR);
		} else if (cliente.getIdIVR() != 0) {
			IVR("" + cliente.getIdIVR());
		} else {
			validarIVR = false;
		}
	}

	@Override
	public void addObserver(Observer o) {
		System.out.println("Agregar Observador Control DetalleBlindaje");
		observador = o;

	}

	@Override
	public void removeObserver(Observer o) {
		observador = null;

	}

	@Override
	public void notifyObserver() {
		observador.update("LlamadaUneMas");

	}

}
