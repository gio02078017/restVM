package co.com.une.appmovilesune.controller;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Busqueda;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.AmigoCuentas;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Simulador;

import com.google.analytics.tracking.android.EasyTracker;

public class ControlBusquedaAmigoCuentas extends Activity implements Observer, Serializable {

	private TituloPrincipal tp;
	private Busqueda buscar;
	private ListView listaDirecciones;
	private Button btnSeleccion;
	private ProgressBar pgbProgreso;
	private TextView txtMensaje;
	private Dialogo dialogo;

	private Cliente cliente;
	private AmigoCuentas ac;

	private String tipoBusqueda;

	private RadioButton rdbIdentificador, rdbTelefono, rbdCodigoHogar, rbdCedula, rbdDireccion;

	private TextView txtTelefono, txtCodigoHogar, txtCedula, txtDireccion;

	private Button btnNuevo;

	private String codigoHogar;

	private ProgressDialog pg;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewbusquedaamigocuentas);

		tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
		tp.setTitulo("Busqueda Amigo Cuentas");

		buscar = (Busqueda) findViewById(R.id.busqueda);
		listaDirecciones = (ListView) findViewById(R.id.lstDirecciones);
		btnSeleccion = (Button) findViewById(R.id.btnSeleccionar);
		pgbProgreso = (ProgressBar) findViewById(R.id.pgbProgreso);
		txtMensaje = (TextView) findViewById(R.id.txtMensajeBusqueda);
		rdbIdentificador = (RadioButton) findViewById(R.id.radio_identificador);
		rdbTelefono = (RadioButton) findViewById(R.id.radio_telefono);
		rbdCodigoHogar = (RadioButton) findViewById(R.id.radio_codigo_hogar);
		rbdCedula = (RadioButton) findViewById(R.id.radio_Cedula);
		rbdDireccion = (RadioButton) findViewById(R.id.radio_direccion);

		txtTelefono = (TextView) findViewById(R.id.txtTelefono);
		txtCodigoHogar = (TextView) findViewById(R.id.txtCodigoHogar);
		txtCedula = (TextView) findViewById(R.id.txtCedula);
		txtDireccion = (TextView) findViewById(R.id.txtDireccion);

		btnNuevo = (Button) findViewById(R.id.btnNuevo);

		Bundle reicieveParams = getIntent().getExtras();
		if (reicieveParams != null) {
			cliente = (Cliente) reicieveParams.getSerializable("cliente");
			ac = cliente.ac;

			txtTelefono.setText(cliente.getTelefono());
			txtCodigoHogar.setText(cliente.getCodigoHogar());
			txtCedula.setText(cliente.getCedula());
			txtDireccion.setText(cliente.getDireccion());

			/*
			 * if(ac == null){ btnSeleccion.setVisibility(View.VISIBLE); }else
			 * if(ac.tipo.equals("Selector")){
			 * btnSeleccion.setVisibility(View.VISIBLE); }
			 */

			if (Utilidades.habilitar(cliente.getCiudad(), "habilitarOfertaDigital")) {
				btnNuevo.setVisibility(View.VISIBLE);
			} else {
				btnNuevo.setVisibility(View.GONE);
			}

		}

		buscar.boton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// Resultado_Cartera();
				hacerConsulta();
			}
		});

		OnItemClickListener eventoLista = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				for (int i = 0; i < parent.getChildCount(); i++) {
					CheckedTextView txt = (CheckedTextView) parent.getChildAt(i);
					txt.setChecked(false);
				}

				CheckedTextView txt = (CheckedTextView) view;
				txt.setChecked(true);
				System.out.println(ac.direcciones.get(position)[1]);
				System.out.println(ac.direcciones.get(position)[0]);
				codigoHogar = ac.direcciones.get(position)[0];

				btnSeleccion.setVisibility(View.VISIBLE);

			}

		};

		RadioButton.OnClickListener eventoRadios = new RadioButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.radio_identificador:
					tipoBusqueda = "identificador";
					buscar.setVisibility(View.VISIBLE);
					break;
				case R.id.radio_telefono:
					tipoBusqueda = "identificador";
					if (cliente.getTelefono().equals("")) {
						buscar.setVisibility(View.VISIBLE);
						btnSeleccion.setVisibility(View.GONE);
					} else {
						buscar.setVisibility(View.GONE);
						btnSeleccion.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.radio_codigo_hogar:
					tipoBusqueda = "codigoHogar";
					if (cliente.getCodigoHogar().equals("")) {
						buscar.setVisibility(View.VISIBLE);
						btnSeleccion.setVisibility(View.GONE);
					} else {
						buscar.setVisibility(View.GONE);
						btnSeleccion.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.radio_Cedula:
					tipoBusqueda = "cedula";
					if (cliente.getCedula().equals("")) {
						buscar.setVisibility(View.VISIBLE);
						btnSeleccion.setVisibility(View.GONE);
					} else {
						buscar.setVisibility(View.GONE);
						btnSeleccion.setVisibility(View.VISIBLE);
					}
					break;
				case R.id.radio_direccion:
					tipoBusqueda = "direccion";
					if (cliente.getDireccion().equals("")) {
						buscar.setVisibility(View.VISIBLE);
						btnSeleccion.setVisibility(View.GONE);
					} else {
						buscar.setVisibility(View.GONE);
						btnSeleccion.setVisibility(View.VISIBLE);
					}
					break;
				default:
					break;
				}
				System.out.println(tipoBusqueda);
			}

		};

		rdbIdentificador.setOnClickListener(eventoRadios);
		rdbTelefono.setOnClickListener(eventoRadios);
		rbdCodigoHogar.setOnClickListener(eventoRadios);
		rbdCedula.setOnClickListener(eventoRadios);
		rbdDireccion.setOnClickListener(eventoRadios);

		listaDirecciones.setOnItemClickListener(eventoLista);

		Utilidades.DialogoMensaje(this, cliente.getCiudad());

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

	public void hacerConsulta() {
		System.out.println("Busqueda de " + tipoBusqueda + " = " + buscar.getBusqueda());
		pgbProgreso.setVisibility(View.VISIBLE);
		// pg.setTitle("Consultando");
		txtMensaje.setVisibility(View.VISIBLE);
		if (!buscar.getBusqueda().equals("")) {
			lanzarAmigoCuentas(buscar.getBusqueda(), tipoBusqueda, cliente.getCiudadFenix());
		} else {
			Toast.makeText(this, getResources().getString(R.string.introducir_parametros_busqueda), Toast.LENGTH_LONG);
		}

	}

	public void hacerConsulta(View v) {
		pg = new ProgressDialog(this);
		pg.setTitle("Consultando");
		pg.setMessage("Obteniendo datos");
		pg.show();
		if (tipoBusqueda.equals("codigoHogar")) {
			lanzarAmigoCuentas(codigoHogar, tipoBusqueda, cliente.getCiudadFenix());
		} else if (tipoBusqueda.equals("identificador")) {
			lanzarAmigoCuentas(cliente.getTelefono(), tipoBusqueda, cliente.getCiudadFenix());
		} else if (tipoBusqueda.equals("direccion")) {
			lanzarAmigoCuentas(cliente.getDireccion(), tipoBusqueda, cliente.getCiudadFenix());
		} else if (tipoBusqueda.equals("cedula")) {
			lanzarAmigoCuentas(cliente.getCedula(), tipoBusqueda, cliente.getCiudadFenix());
		}

	}

	public void lanzarAmigoCuentas(String valor, String tipo, String municipio) {
		System.out.println("lanzarAmigoCuentas");
		ac = new AmigoCuentas();
		ac.setManual();

		ArrayList<String> parametros = new ArrayList<String>();
		parametros = new ArrayList<String>();
		parametros.add(valor);
		parametros.add(tipo);
		parametros.add(municipio);

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(parametros);
		params.add(this);
		ac.execute(params);

	}

	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		if (pg != null) {
			pg.dismiss();
		}
		pgbProgreso.setVisibility(View.GONE);
		if (value != null) {

			System.out.println("value amc " + value);

			if (value.equals("Oferta")) {
				cliente.ac = ac;

				System.out.println("cliente.ac " + cliente.ac);

				if (cliente.ac != null) {

					System.out.println("cliente.ac.municipio "+cliente.ac.municipio);
					if(!Utilidades.excluir("siebelMunicipios", cliente.ac.municipio)){
						if (cliente.ac.productosPortafolio != null) {
							if (!cliente.ac.productosPortafolio.isEmpty()) {
								Intent intent = new Intent();
								intent.putExtra("cliente", cliente);
								setResult(MainActivity.OK_RESULT_CODE, intent);
								finish();
							} else {
								txtMensaje.setVisibility(View.VISIBLE);
								txtMensaje.setText("La Consulta no arrojo resultados");
							}
						} else {
							txtMensaje.setVisibility(View.VISIBLE);
							txtMensaje.setText("La Consulta no arrojo resultados");
						}
					}else{
						txtMensaje.setVisibility(View.VISIBLE);
						txtMensaje.setText("Para el muncipio de "+cliente.ac.municipio +" no es posible la venta por este modulo");
					}
				} else {
					txtMensaje.setVisibility(View.VISIBLE);
					txtMensaje.setText("La Consulta no arrojo resultados");
				}

			} else if (value.equals("Vacio")) {
				txtMensaje.setVisibility(View.VISIBLE);
				txtMensaje.setText("La Consulta no arrojo resultados");
			} else if (value.equals("Selector")) {
				tipoBusqueda = "codigoHogar";

				txtMensaje.setText("La consulta arroja multiples resultados");
				txtMensaje.setVisibility(View.VISIBLE);

				/*
				 * txtMensaje.setVisibility(View.GONE); ArrayList<String>
				 * direcciones = new ArrayList<String>(); for (int i = 0; i <
				 * ac.direcciones.size(); i++) {
				 * direcciones.add(ac.direcciones.get(i)[1]); }
				 * ArrayAdapter<String> adaptador = new
				 * ArrayAdapter<String>(getApplicationContext(),
				 * R.layout.simple_list_item, direcciones);
				 * listaDirecciones.setAdapter(adaptador);
				 * setListViewHeightBasedOnChildren(listaDirecciones);
				 * listaDirecciones.setVisibility(View.VISIBLE);
				 */
			} else {
				ArrayList<Object> resultado = (ArrayList<Object>) value;

				System.out.println("resultado " + resultado);

				if (resultado != null && resultado.get(0).equals("TipoHogar")) {
					cliente.setlogSmartPromoRes(resultado.get(1).toString());
					validarSmartPromo(resultado.get(1).toString());
					mostrarOfertaDigital();
				}
			}
		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int spacefinalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			// System.out.println("listItem.getMeasuredHeight() =>
			// "+listItem.getMeasuredHeight());
			totalHeight += listItem.getMeasuredHeight();
			spacefinalHeight = listItem.getMeasuredHeight();
			// System.out.println("totalHeight => "+totalHeight);
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + spacefinalHeight;
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public void mostrarOfertaDigital(View v) {
		if (Utilidades.excluir("ConsultarSmartPromo", cliente.getCiudad())) {
			lanzarTipoHogar();
		} else {
			mostrarOfertaDigital();
		}
	}

	public void mostrarOfertaDigital() {
		if(!cliente.getTecnologia().equals(Utilidades.inicial_opcion)){
			if (cliente.isControlNormalizada() || cliente.isControlCerca()) {
				 if(!cliente
							.getEstrato().equalsIgnoreCase(
									Utilidades.inicial_estrato)){	

					if (cliente.getCobertura() != null
							|| Utilidades.excluir("deshabilitarCobertura",
									cliente.getCiudad()) || cliente.isControlCerca()) {
						try {

							String hfc = "";
							if (Utilidades.excluir("deshabilitarCobertura", cliente.getCiudad())) {
								hfc = "SI";
							} else {
								
								if(cliente.isControlCerca()){
									hfc = cliente.getTecnologia();
								}else{
									JSONObject jo = new JSONObject(cliente.getCobertura());
									JSONObject cobertura = jo.getJSONObject("Cobertura");
									hfc = cobertura.get("COBERTURA_HFC").toString();
									String direccionaldiad = cobertura.get(
											"DIRECCIONALIDAD").toString();
								}
							}

							if (hfc.equalsIgnoreCase("SI")) {
								Intent intent = new Intent();
								intent.putExtra("cliente", cliente);
								intent.putExtra("destino", "nuevo");
								setResult(MainActivity.OK_RESULT_CODE, intent);
								finish();
							} else {
								Intent intent = new Intent();
								intent.putExtra("cliente", cliente);
								intent.putExtra("destino", "nuevo");
								setResult(MainActivity.OK_RESULT_CODE, intent);
								finish();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (Utilidades.excluir("deshabilitarCobertura", cliente.getCiudad())) {
								Intent intent = new Intent();
								intent.putExtra("cliente", cliente);
								intent.putExtra("destino", "nuevo");
								setResult(MainActivity.OK_RESULT_CODE, intent);
								finish();
							} else {
								dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
										"Problemas con la consulta de cobertura. Contactar Administrador.");
								dialogo.dialogo.show();
							}

						}
						/*
						*/
					} else {
						if (Utilidades.excluir("deshabilitarCobertura", cliente.getCiudad())) {
							Intent intent = new Intent();
							intent.putExtra("cliente", cliente);
							intent.putExtra("destino", "nuevo");
							setResult(MainActivity.OK_RESULT_CODE, intent);
							finish();
						} else {
							dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
									"Debe consultar la cobertura del cliente");
							dialogo.dialogo.show();
						}
					}

					System.out.println(cliente);
				} else {
					dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
							"Si El Estrato No Es Seleccionado Con La Normalización Seleccione Manual, En Infocliente");
					dialogo.dialogo.show();
				}

			} else {
				dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA, "Debe normalizar la direccion");
				dialogo.dialogo.show();
			}
		} else {
			Toast.makeText(this, "Debe seleccionar la tecnología del cliente.", Toast.LENGTH_SHORT).show();
		}

	}

	private void lanzarTipoHogar() {

		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);

		JSONObject data = new JSONObject();

		try {
			data.put("paginacion", cliente.getPaginacion());
			data.put("direccion", cliente.getDireccion());
			data.put("direccionNormalizada", cliente.getDireccionNormalizada());
			data.put("medio", "ATC");
			data.put("medioCotizador", "AMC");
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}

		ArrayList<String> parametros = new ArrayList<String>();
		cliente.setlogSmartPromoEnv(data.toString());
		parametros.add(data.toString());

		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("TipoHogar");
		params.add(parametros);

		simulador.execute(params);

	}
	

	public void validarSmartPromo(String resultado) {
		try {
			JSONObject smart = new JSONObject(resultado);

			if (smart.has("CodigoMensaje") && smart.getString("CodigoMensaje").equalsIgnoreCase("00")) {
				if (smart.has("tipoHogar") && smart.getString("tipoHogar").equalsIgnoreCase("S")) {
					Utilidades.MensajesToast("Aplica Smart Promo", this);
					cliente.setSmartPromo("1");

				} else {
					Utilidades.MensajesToast("No Aplica Smart Promo", this);
					cliente.setSmartPromo("0");
				}

			}
		} catch (JSONException e) {
			Log.w("Errror SmarPromo", e.getMessage());
		}
	}

}
