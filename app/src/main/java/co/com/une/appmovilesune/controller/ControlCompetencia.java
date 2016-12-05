package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemCompetencia;
import co.com.une.appmovilesune.adapters.ListaCompetenciaAdapter;
import co.com.une.appmovilesune.change.ControlSimulador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.SelectorFecha;
import co.com.une.appmovilesune.components.SelectorProductos;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Competencia;

import com.google.analytics.tracking.android.EasyTracker;

public class ControlCompetencia extends Activity {

	private Competencia competencia;
	private Cliente cliente;
	private Dialogo dialogo, dialogoFecha, dialogoProductos, dialogoCompetencia;
	private ArrayList<ItemCompetencia> competencias;

	private ListView lstCompetencias;
	private Spinner spnCompetencia, spnProducto, spnMotivoNoUne, spnSubMotivoNoUne, spnAtencion;
	private CheckBox chkEmpaquetado;
	private EditText txtOtraCompetencia, txtPagoMensual, txtObservaciones, txtDireccion, txtNombre, txtApellido,
			txtDocumento, txtBarrio, txtTelefono;
	public static EditText txtVigenciaContrato;
	public static EditText txtFechaExpCompetencia;
	public ImageButton btnSiguiente, btnPickDate;
	private SelectorProductos slpProductos;
	private TableRow trTablaCompetenciaOtraCompetencia;
	private TableRow trTablaCompetenciaSubMotivo;
	private String respuestaEmpaquetamiento = "";

	private SelectorFecha slfCompetenciaFechaExp;

	public Spinner spnTipoDocumento;

	private ImageView imgAgregarCompetencia;
	private LinearLayout llyCompetencias;

	private TituloPrincipal tp;

	public SelectorFecha sfp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewcompetencia);

		tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
		tp.setTitulo("Gerencia De Ruta");

		competencias = new ArrayList<ItemCompetencia>();

		competencia = new Competencia();
		dialogo = new Dialogo(this, Dialogo.DIALOGO_FECHA, "");
		dialogoFecha = new Dialogo(this, Dialogo.DIALOGO_FECHA, "");

		dialogoProductos = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_PRODUCTO, "");
		dialogoCompetencia = new Dialogo(this, Dialogo.DIALOGO_FORMULARIO_COMPETENCIA, "");
		dialogoCompetencia.dialogo.setOnDismissListener(dlc);
		dialogo.dialogo.setOnDismissListener(dl);
		dialogoFecha.dialogo.setOnDismissListener(dl2);
		dialogoProductos.dialogo.setOnDismissListener(dlp);

		asignarCampos();
		llenarSpiners();

		Bundle reicieveParams = getIntent().getExtras();
		if (reicieveParams != null) {

			competencia = (Competencia) reicieveParams.getSerializable("competencia");
			cliente = (Cliente) reicieveParams.getSerializable("cliente");

			if (competencia != null) {
				llenarCampos();
			}

			if (cliente != null) {
				llenarCamposCliente();
				if (cliente.isCarrusel()) {
					llenarCarrusel();
				}
			}

		} else {
			// System.out.println("no competencia");
		}
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

	private void asignarCampos() {

		spnAtencion = (Spinner) findViewById(R.id.spnAtencion);
		spnCompetencia = (Spinner) findViewById(R.id.spnCompetencia);
		spnMotivoNoUne = (Spinner) findViewById(R.id.spnMotivoNoUne);
		spnSubMotivoNoUne = (Spinner) findViewById(R.id.spnSubMotivo);
		// slpProductos = (SelectorProductos) findViewById(R.id.slpCompetencia);
		lstCompetencias = (ListView) findViewById(R.id.lstCompetencias);
		imgAgregarCompetencia = (ImageView) findViewById(R.id.imgAgregarCompetencia);
		txtOtraCompetencia = (EditText) findViewById(R.id.txtOtraCompetencia);
		trTablaCompetenciaOtraCompetencia = (TableRow) findViewById(R.id.trTablaCompetenciaOtraCompetencia);
		trTablaCompetenciaSubMotivo = (TableRow) findViewById(R.id.trTablaCompetenciaSubMotivo);
		txtVigenciaContrato = (EditText) findViewById(R.id.txtVigenciaContrato);
		txtFechaExpCompetencia = (EditText) findViewById(R.id.txtFechaExpCompetencia);
		txtPagoMensual = (EditText) findViewById(R.id.txtPagoMensual);
		txtObservaciones = (EditText) findViewById(R.id.txtObservaciones);
		txtDireccion = (EditText) findViewById(R.id.txtDireccionCompetencia);
		txtTelefono = (EditText) findViewById(R.id.txtTelefonoCompetencia);
		txtNombre = (EditText) findViewById(R.id.txtNombreCompetencia);
		txtApellido = (EditText) findViewById(R.id.txtApellidoCompetencia);
		txtDocumento = (EditText) findViewById(R.id.txtDocumentoCompetencia);
		txtBarrio = (EditText) findViewById(R.id.txtBarrioCompetencia);
		spnTipoDocumento = (Spinner) findViewById(R.id.spnTipoDocumento);

		// slfCompetenciaFechaExp = (SelectorFecha)
		// findViewById(R.id.slfCompetenciaFechaExp);

		btnSiguiente = (ImageButton) findViewById(R.id.btnSiguiente);
		btnPickDate = (ImageButton) findViewById(R.id.btnPickDate);

	}

	private void llenarSpiners() {

		ArrayAdapter<String> adaptador = null;

		String nombreCompetencia = "";
		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
				new String[] { "lst_item" }, "lst_nombre = ?", new String[] { "Competencia" }, null, null, null);
		ArrayList<String> listCompetencias = new ArrayList<String>();
		if (resultado != null) {
			listCompetencias.add("-- Seleccione Competencia --");

			for (int i = 0; i < resultado.size(); i++) {
				listCompetencias.add(resultado.get(i).get(0));
			}
		} else {
			listCompetencias.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCompetencias);
		// spnCompetencia.setAdapter(adaptador);

		resultado = MainActivity.basedatos.consultar(false, "listasgenerales", new String[] { "lst_item" },
				"lst_nombre = ?", new String[] { "Atencion" }, null, null, null);
		ArrayList<String> listAtencion = new ArrayList<String>();

		if (resultado != null) {
			listAtencion.add("-- Seleccione Atencion --");

			for (int i = 0; i < resultado.size(); i++) {
				listAtencion.add(resultado.get(i).get(0));
			}
		} else {
			listAtencion.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAtencion);
		spnAtencion.setAdapter(adaptador);

		resultado = MainActivity.basedatos.consultar(false, "listasgenerales", new String[] { "lst_item" },
				"lst_nombre = ?", new String[] { "Motivos Gestor Visitas" }, null, null, null);
		ArrayList<String> listMotivo = new ArrayList<String>();
		if (resultado != null) {
			listMotivo.add("-- Seleccione Motivo --");

			for (int i = 0; i < resultado.size(); i++) {
				listMotivo.add(resultado.get(i).get(0));
			}
		}

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMotivo);
		spnMotivoNoUne.setAdapter(adaptador);

		spnMotivoNoUne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

				String motivo = (String) spnMotivoNoUne.getSelectedItem();

				if (motivo != null) {
					if (motivo.equalsIgnoreCase("CREDITO Y CARTERA")) {
						trTablaCompetenciaSubMotivo.setVisibility(View.VISIBLE);
						llenarSpinerSubmotivos("CREDITO Y CARTERA");
						preLLenarSubMotivo();
						spnSubMotivoNoUne.setEnabled(false);
					} else if(motivo.equalsIgnoreCase("MOTIVO DE NO PAGO")){
						trTablaCompetenciaSubMotivo.setVisibility(View.VISIBLE);
						llenarSpinerSubmotivos("MOTIVO DE NO PAGO");
						spnSubMotivoNoUne.setSelection(0);
						spnSubMotivoNoUne.setEnabled(true);
					} else  {
						trTablaCompetenciaSubMotivo.setVisibility(View.GONE);
						spnSubMotivoNoUne.setSelection(0);
					}
				} else {
					trTablaCompetenciaSubMotivo.setVisibility(View.GONE);
					spnSubMotivoNoUne.setSelection(0);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		resultado = MainActivity.basedatos.consultar(false, "listasgenerales", new String[] { "lst_item" },
				"lst_nombre = ?", new String[] { "Tipo Documento" }, null, null, null);

		ArrayList<String> listATipoDocumento = new ArrayList<String>();

		if (resultado != null) {
			listATipoDocumento.add(Utilidades.inicial_opcion);

			for (int i = 0; i < resultado.size(); i++) {
				listATipoDocumento.add(resultado.get(i).get(0));
			}
		} else {
			listATipoDocumento.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listATipoDocumento);
		spnTipoDocumento.setAdapter(adaptador);
	}

	private void llenarSpinerSubmotivos(String motivo){

		ArrayAdapter<String> adaptador = null;

		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores", new String[] { "lst_valor" },
				"lst_nombre = ? and lst_clave = ?", new String[] { "submotivos gerencia visitas", motivo }, null, null, null);
		ArrayList<String> listSubMotivo = new ArrayList<String>();
		if (resultado != null) {
			listSubMotivo.add(Utilidades.inicial_sub_motivo);

			for (int i = 0; i < resultado.size(); i++) {
				listSubMotivo.add(resultado.get(i).get(0));
			}
		}

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSubMotivo);
		spnSubMotivoNoUne.setAdapter(adaptador);
	}

	public void preLLenarSubMotivo() {

		if (cliente.getScooringune() != null) {
			if (cliente.getScooringune().isProspectoScooring()) {
				submotivo(cliente.getScooringune().getRazonScooring());
			}
		}
	}

	private void llenarCampos() {
		ArrayAdapter<String> adaptador;
		motivo();
		submotivo(competencia.getSubMotivo());
		atiende();
		txtObservaciones.setText(competencia.getObservaciones());

	}

	public void submotivo(String dato) {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnSubMotivoNoUne.getAdapter();
		spnSubMotivoNoUne.setSelection(adaptador.getPosition(dato));
	}

	public void motivo() {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnMotivoNoUne.getAdapter();

		spnMotivoNoUne.setSelection(adaptador.getPosition(competencia.getNoUne()));
	}

	public void atiende() {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnAtencion.getAdapter();

		adaptador = (ArrayAdapter<String>) spnAtencion.getAdapter();
		spnAtencion.setSelection(adaptador.getPosition(competencia.getAtencion()));
	}

	private void llenarCamposCliente() {

		txtDireccion.setText(cliente.getDireccion());

		if (!Utilidades.limpiarTelefono(cliente.getTelefono()).equalsIgnoreCase("")) {

			txtTelefono.setText(Utilidades.limpiarTelefono(cliente.getTelefono()));
		} else {
			txtTelefono.setText(cliente.getTelefono2());
		}

		txtNombre.setText(cliente.getNombre());
		txtApellido.setText(cliente.getApellido());
		txtDocumento.setText(cliente.getCedula());
		txtBarrio.setText(cliente.getBarrio());

		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnTipoDocumento.getAdapter();

		spnTipoDocumento.setSelection(adaptador.getPosition(cliente.getTipoDocumento()));

		txtFechaExpCompetencia.setText(cliente.getExpedicion());

	}

	private void llenarCarrusel() {

		ArrayAdapter<String> adaptador;

		adaptador = (ArrayAdapter<String>) spnAtencion.getAdapter();

		spnAtencion.setSelection(adaptador.getPosition("ATIENDE"));

		adaptador = (ArrayAdapter<String>) spnMotivoNoUne.getAdapter();

		spnMotivoNoUne.setSelection(adaptador.getPosition("VALIDACION SEGUNDO NIVEL"));
		spnMotivoNoUne.setEnabled(false);

	}

	public void agregarCompetencia(View v) {
		dialogoCompetencia.dialogo.show();
		spnCompetencia = (Spinner) dialogoCompetencia.dialogo.findViewById(R.id.spnCompetencia);
		spnProducto = (Spinner) dialogoCompetencia.dialogo.findViewById(R.id.spnCompetenciaProducto);
		txtVigenciaContrato = (EditText) dialogoCompetencia.dialogo.findViewById(R.id.txtVigenciaContrato);
		trTablaCompetenciaOtraCompetencia = (TableRow) dialogoCompetencia.dialogo
				.findViewById(R.id.trTablaCompetenciaOtraCompetencia);
		ArrayAdapter<String> adaptadorCompetencias = dialogoCompetencia.icf.obtenerCompetencias();
		ArrayAdapter<String> adaptadorProductos = dialogoCompetencia.icf.obtenerProductosCompetencia();
		spnCompetencia.setAdapter(adaptadorCompetencias);
		spnProducto.setAdapter(adaptadorProductos);

		spnCompetencia.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				mostrarOtraCompetencia();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void mostrarOtraCompetencia() {
		if (spnCompetencia.getSelectedItem().toString().equalsIgnoreCase("OTRA")) {
			trTablaCompetenciaOtraCompetencia.setVisibility(View.VISIBLE);
		} else {
			trTablaCompetenciaOtraCompetencia.setVisibility(View.GONE);
		}

	}

	public void buscarDir(View v) {
		Intent intent = new Intent(MainActivity.MODULO_DIRECCIONES);
		startActivityForResult(intent, MainActivity.REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == MainActivity.REQUEST_CODE) {
				// cogemos el valor devuelto por la otra actividad
				String result = data.getStringExtra("result");
				if (result.equalsIgnoreCase("Direcciones")) {
					txtDireccion.setText(data.getStringExtra("Direccion"));
				}
			}
		} catch (Exception e) {
			Log.w("Error", "Mensaje " + e.getMessage());
		}
	}

	public void procesarCompetencia(View v) {

		competencia = new Competencia((String) spnAtencion.getSelectedItem(), competencias,
				spnMotivoNoUne.getSelectedItem().toString(), txtObservaciones.getText().toString());

		competencia.setSubMotivo((String) spnSubMotivoNoUne.getSelectedItem());

		cliente.setNombre(txtNombre.getText().toString());
		cliente.setApellido(txtApellido.getText().toString());
		cliente.setTelefono2(txtTelefono.getText().toString());
		cliente.setCedula(txtDocumento.getText().toString());
		cliente.setDireccion(txtDireccion.getText().toString());
		cliente.setBarrio(txtBarrio.getText().toString());
		cliente.setCedula(txtDocumento.getText().toString());
		cliente.setExpedicion(txtFechaExpCompetencia.getText().toString());
		cliente.setTipoDocumento((String) spnTipoDocumento.getSelectedItem());

		Intent intent = new Intent();
		intent.putExtra("competencia", competencia);
		intent.putExtra("cliente", cliente);
		setResult(MainActivity.OK_RESULT_CODE, intent);
		finish();
	}

	public void mostrarDialogo(View v) {
		dialogoProductos.dialogo.show();
	}

	public void mostrarSelectorFecha(View v) {
		dialogo.dialogo.show();
	}

	OnDismissListener dl = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {
			// TODO Auto-generated method stub
			if (dialogo.isSeleccion()) {
				txtVigenciaContrato.setText(dialogo.getFechaSeleccion());
			}
			dialogo.setSeleccion(false);
		}
	};

	public void mostrarSelectorFecha2(View v) {
		dialogoFecha.dialogo.show();
	}

	OnDismissListener dl2 = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {
			// TODO Auto-generated method stub
			if (dialogoFecha.isSeleccion()) {
				txtFechaExpCompetencia.setText(dialogoFecha.getFechaSeleccion());
			}
			dialogoFecha.setSeleccion(false);
		}
	};

	OnDismissListener dlp = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {
			// TODO Auto-generated method stub
			if (dialogoProductos.isSeleccion()) {
				slpProductos.setTexto(dialogoProductos.getProductos().toString());
			}

		}
	};

	OnDismissListener dlc = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {
			// TODO Auto-generated method stub
			if (dialogoCompetencia.isSeleccion()) {
				agergarCompetenciaItem(dialogoCompetencia.getCompetencia());
			}
		}
	};

	public void agergarCompetenciaItem(String[] competencia) {
		if (!competencia[0].equalsIgnoreCase("OTRA")) {
			ItemCompetencia ic = new ItemCompetencia(dialogoCompetencia.getCompetencia()[1],
					dialogoCompetencia.getCompetencia()[0], dialogoCompetencia.getCompetencia()[3],
					dialogoCompetencia.getCompetencia()[4]);
			competencias.add(ic);
		} else {
			ItemCompetencia ic = new ItemCompetencia(dialogoCompetencia.getCompetencia()[1],
					dialogoCompetencia.getCompetencia()[2], dialogoCompetencia.getCompetencia()[3],
					dialogoCompetencia.getCompetencia()[4]);
			competencias.add(ic);
		}

		ListaCompetenciaAdapter adapter = new ListaCompetenciaAdapter(this, competencias, this);
		lstCompetencias.setAdapter(adapter);
		ControlSimulador.setListViewHeightBasedOnChildren(lstCompetencias);
		registerForContextMenu(lstCompetencias);
	}

	public void actualizarLista() {
		ListaCompetenciaAdapter adapter = new ListaCompetenciaAdapter(this, competencias, this);
		lstCompetencias.setAdapter(adapter);
		ControlSimulador.setListViewHeightBasedOnChildren(lstCompetencias);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.lstCompetencias) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(competencias.get(info.position).getProducto() + " - "
					+ competencias.get(info.position).getCompetencia());
			String[] menuItems = getResources().getStringArray(R.array.menu);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		competencias.remove(info.position);
		actualizarLista();
		return true;
	}

}
