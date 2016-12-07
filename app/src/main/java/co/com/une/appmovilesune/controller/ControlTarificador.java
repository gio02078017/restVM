package co.com.une.appmovilesune.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.params.ClientParamBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.kobjects.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.BloqueoCobertura;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ItemKeyValue2;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ItemTarificador;
import co.com.une.appmovilesune.adapters.ListaChecked;
import co.com.une.appmovilesune.adapters.ListaCheckedAdapter;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesDecos;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.complements.Mensajes;
import co.com.une.appmovilesune.complements.Validaciones;
import co.com.une.appmovilesune.components.CompDecos;
import co.com.une.appmovilesune.components.ResumenTarificador;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.Tarificador;

import com.google.analytics.tracking.android.EasyTracker;

public class ControlTarificador extends Activity implements Subject, Observer {

	private Tarificador tarificador;

	private TabHost.TabSpec spec;

	private TituloPrincipal tp;

	private Cotizacion cotizacion;

	private Cliente cliente;
	private Scooring scooring;

	private String idAsesoria;

	Spinner sltEstrato, sltTo, sltPlanTo, sltTv, sltPlanTv, sltBa, sltPlanBa, slt3G, slt4G;
	Spinner sltDesTo, sltDesTv, sltDesBa, sltDes3G, sltDes4G;
	TextView txtAdicionales, txtEstrato, txtLocalizacion;
	CheckBox chktvAnaloga;

	private ImageButton btnDescuentos;

	ListView lsvAdicionales;
	ResumenTarificador rt;

	ArrayList<ItemTarificador> resumen = new ArrayList<ItemTarificador>();
	ArrayList<ItemTarificador> cotizacion_venta = new ArrayList<ItemTarificador>();
	ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();
	ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionalesGratis = new ArrayList<ItemPromocionesAdicionales>();
	private Observer observador;

	private boolean Validar = false;
	private boolean cambio = false;
	boolean aplicarAd = false;
	String aplicarAnaloga = "N/A";

	TabHost tabs;

	public Dialogo dialogo;

	private CheckBox chkAplicarAd;

	private String estratoSeleccionado = "";

	private ArrayList<ItemKeyValue2> datosValidacion = new ArrayList<ItemKeyValue2>();
	private ArrayList<ItemKeyValue2> countValidacion = new ArrayList<ItemKeyValue2>();
	private ArrayList<String> countValidacion2 = new ArrayList<String>();

	private boolean validarAdicionales = true;
	private boolean validarEstandarizacion = true;
	private boolean validarTelefonoServicio = true;
	private boolean cambioMedioTV = false;

	private BloqueoCobertura bloqueoCobertura;

	private String toteccr = "N/A", tvteccr = "N/A", bateccr = "N/A";

	private boolean controlDescuentosAd = false;
	private double preciosConDescuentoAd = 0.0;
	private double precioDescuentosAd = 0.0;

	private CompDecos cmpDecos;

	private ArrayList<ItemDecodificador> itemDecodificadores;

	private boolean consultoDecos = false;

	private String filtroDependencia = "";
	private boolean dependenciaTV = false;

	private boolean precargarTv = false;
	private boolean precargarTo = false;
	private boolean precargarBa = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewtarificador);

		tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);

		Bundle reicieveParams = getIntent().getExtras();
		if (reicieveParams != null) {
			cotizacion = (Cotizacion) reicieveParams.getSerializable("cotizacion");
			cliente = (Cliente) reicieveParams.getSerializable("cliente");
			scooring = (Scooring) reicieveParams.getSerializable("scooring");
			if (cotizacion != null) {
				Validar = true;

				System.out.println("CambiarCotizacion " + cotizacion.isCambiarCotizacion());
				if (cotizacion.isCambiarCotizacion()) {
					precargarBa = true;
					precargarTo = true;
					precargarTv = true;
				}

				System.out.println("CambiarCotizacion precargarBa" + precargarBa);
				System.out.println("CambiarCotizacion precargarTo" + precargarTo);
				System.out.println("CambiarCotizacion precargarTv" + precargarTv);

			}
		}

		if (Utilidades.excluirMunicipal("bloquearCobertura", "bloquearCobertura", cliente.getCiudad())) {

			bloqueoCobertura = Utilidades.tratarCoberturaBloqueo(cliente.getCobertura(), this);

		}

		MainActivity.obsrTarifas = this;

		tarificador = new Tarificador();

		sltEstrato = (Spinner) findViewById(R.id.sltEstrato);
		sltTo = (Spinner) findViewById(R.id.sltTelefonia);
		sltPlanTo = (Spinner) findViewById(R.id.sltPlanTelefonia);
		sltTv = (Spinner) findViewById(R.id.sltTelevision);
		sltPlanTv = (Spinner) findViewById(R.id.sltPlanTelevision);
		sltBa = (Spinner) findViewById(R.id.sltInternet);
		sltPlanBa = (Spinner) findViewById(R.id.sltPlanInternet);
		slt3G = (Spinner) findViewById(R.id.slt3G);
		slt4G = (Spinner) findViewById(R.id.slt4G);

		sltDesTo = (Spinner) findViewById(R.id.sltDesTelefonia);
		sltDesTv = (Spinner) findViewById(R.id.sltDesTelevision);
		sltDesBa = (Spinner) findViewById(R.id.sltDesInternet);
		sltDes3G = (Spinner) findViewById(R.id.sltDes3G);
		sltDes4G = (Spinner) findViewById(R.id.sltDes4G);
		btnDescuentos = (ImageButton) findViewById(R.id.btnDescuentos);

		chktvAnaloga = (CheckBox) findViewById(R.id.chktvAnaloga);
		chktvAnaloga.setOnCheckedChangeListener(aplicartvAnaloga);
		chktvAnaloga.setEnabled(false);

		chkAplicarAd = (CheckBox) findViewById(R.id.chkAplicarAd);
		chkAplicarAd.setOnCheckedChangeListener(aplicarAdicional);

		txtEstrato = (TextView) findViewById(R.id.txtEstrato);
		// txtAdicionales = (TextView) findViewById(R.id.txtAdicionales);
		txtLocalizacion = (TextView) findViewById(R.id.txtLocalizacion);

		lsvAdicionales = (ListView) findViewById(R.id.lsvAdicionales);

		rt = (ResumenTarificador) findViewById(R.id.rstResumen);
		rt.mostrar();

		cmpDecos = (CompDecos) findViewById(R.id.cmpDecosTarificador);
		cmpDecos.setCiudad(cliente.getCiudad());
		cmpDecos.setOferta("tarificador");
		cmpDecos.addObserver(this);

		validarPermisosDecos();

		Resources res = getResources();

		tabs = (TabHost) findViewById(android.R.id.tabhost);
		tabs.setup();

		spec = tabs.newTabSpec("Productos");
		spec.setContent(R.id.llyProductos);
		spec.setIndicator("Productos", res.getDrawable(R.drawable.producto));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("Descuentos");
		spec.setContent(R.id.llyDescuentos);
		spec.setIndicator("Promociones", res.getDrawable(R.drawable.descuentos));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("Adicionales");
		spec.setContent(R.id.llyAdicionales);
		spec.setIndicator("Adicionales", res.getDrawable(R.drawable.adicionales));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("Decos");
		spec.setContent(R.id.llyDecos);
		spec.setIndicator("Decos", res.getDrawable(R.drawable.adicionales));
		tabs.addTab(spec);

		spec = tabs.newTabSpec("Resultado");
		spec.setContent(R.id.llyResultado);
		spec.setIndicator("Resultado", res.getDrawable(R.drawable.resultado));
		tabs.addTab(spec);

		tabs.setCurrentTab(0);
		tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
				.setBackgroundColor(getResources().getColor(R.color.yellow));
		TextView tv = (TextView) tabs.getCurrentTabView().findViewById(android.R.id.title); // for
																							// Selected
																							// Tab
		tv.setTextColor(getResources().getColor(R.color.darkTab));

		tabs.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				tp.setTitulo(getResources().getString(R.string.cotizacion) + tabId);

				if (tabId.equalsIgnoreCase("Productos")) {
					if (cambioMedioTV) {
						sltTv.setSelection(0);
						sltPlanTv.setSelection(0);
						cambioMedioTV = false;
					}
				} else if (tabId.equalsIgnoreCase("Resultado")) {
					if (consultoDecos) {
						consultar();
					} else {
						Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.consultardecodificadores), Toast.LENGTH_LONG).show();
						tabs.setCurrentTab(3);
					}

				} else if (tabId.equalsIgnoreCase("Descuentos")) {
					// llenarDescuentos(consultarDescuentos());
					llenarDescuentos(consultarDescuentos2());
				} else if (tabId.equalsIgnoreCase("Decos")) {
					if (itemDecodificadores != null && itemDecodificadores.size() > 0) {
						itemDecodificadores.clear();
					}
					itemDecodificadores = UtilidadesDecos.logicaDecos(itemDecodificadores,
							(String) sltTv.getSelectedItem());
					cmpDecos.setPlan((String) sltTv.getSelectedItem());
					cmpDecos.setDecos(itemDecodificadores);
					consultoDecos = true;
				}

				for (int i = 0; i < tabs.getTabWidget().getChildCount(); i++) {
					tabs.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.darkTab));
					TextView tv = (TextView) tabs.getTabWidget().getChildAt(i).findViewById(android.R.id.title); // Unselected
																													// Tabs
					tv.setTextColor(getResources().getColor(R.color.lightTab));
				}
				tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
						.setBackgroundColor(getResources().getColor(R.color.yellow));
				TextView tv = (TextView) tabs.getCurrentTabView().findViewById(android.R.id.title); // for
																									// Selected
																									// Tab
				tv.setTextColor(getResources().getColor(R.color.darkTab));

			}
		});

		ArrayAdapter<String> adaptador = null;

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.estrato);
		sltEstrato.setAdapter(adaptador);

		llenarEstrato();

		Iniciar_Descuentos();
		llenarProductos();
		llenarProductosPlan();

		sltEstrato.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

				if (sltEstrato.getSelectedItem().toString().equals(Utilidades.seleccioneEstrato)) {
					sltTo.setSelection(0);
					sltTv.setSelection(0);
					sltBa.setSelection(0);
					slt3G.setSelection(0);
					slt4G.setSelection(0);
				} else if (!estratoSeleccionado.equalsIgnoreCase((String) sltEstrato.getSelectedItem())) {

					sltTo.setSelection(0);
					sltTv.setSelection(0);
					sltBa.setSelection(0);
					slt3G.setSelection(0);
					slt4G.setSelection(0);
					estratoSeleccionado = (String) sltEstrato.getSelectedItem();

				} else {
					estratoSeleccionado = (String) sltEstrato.getSelectedItem();
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		sltPlanTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				llenarTo();
				cambio = true;
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		sltTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				cambio = true;
				if (!parent.getSelectedItem().toString().equals(Utilidades.inicial)) {
					/*
					 * sltDesTo.setAdapter(llenarDescuentos(sltEstrato
					 * .getSelectedItem().toString(),
					 * Utilidades.tipo_producto_to_d, parent
					 * .getSelectedItem().toString()));
					 * 
					 * llenarCotizacionDescuentos(Utilidades.tipo_producto_to_d)
					 * ;
					 */

				} else {
					sltDesTo.setAdapter(vaciarDescuento());
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		sltPlanTv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				llenarTv();
				cambio = true;
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		sltTv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				cambio = true;
				if (!parent.getSelectedItem().toString().equals(Utilidades.inicial)) {
					/*
					 * sltDesTv.setAdapter(llenarDescuentos(sltEstrato
					 * .getSelectedItem().toString(),
					 * Utilidades.tipo_producto_tv_d, parent
					 * .getSelectedItem().toString()));
					 */
					llenarAdicionales(parent.getSelectedItem().toString());
					if (!filtroDependencia.equalsIgnoreCase("")
							|| filtroDependencia.equalsIgnoreCase(Utilidades.inicial)) {
						if (!filtroDependencia.equalsIgnoreCase((String) parent.getSelectedItem())) {

							if (precargarTo) {
								precargarTo = false;
							} else {
								sltPlanTo.setSelection(0);
							}

							if (precargarBa) {
								precargarBa = false;
							} else {
								sltPlanBa.setSelection(0);
							}

							System.out.println("CambiarCotizacion precargarBa" + precargarBa);
							System.out.println("CambiarCotizacion precargarTo" + precargarTo);
							System.out.println("CambiarCotizacion precargarTv" + precargarTv);
						}
						filtroDependencia = Utilidades.dependenciaOferta((String) parent.getSelectedItem());
					} else {
						filtroDependencia = Utilidades.dependenciaOferta((String) parent.getSelectedItem());

						if (precargarTo) {
							precargarTo = false;
						} else {
							sltPlanTo.setSelection(0);
						}

						if (precargarBa) {
							precargarBa = false;
						} else {
							sltPlanBa.setSelection(0);
						}

						System.out.println("CambiarCotizacion precargarBa" + precargarBa);
						System.out.println("CambiarCotizacion precargarTo" + precargarTo);
						System.out.println("CambiarCotizacion precargarTv" + precargarTv);
					}

					dependenciaTV = true;

					// llenarCotizacionDescuentos(Utilidades.tipo_producto_tv_d);

				} else {
					Limpiar_Adicionales();
					sltDesTv.setAdapter(vaciarDescuento());
					dependenciaTV = false;
					System.out.println("filtroDependencia Antes " + filtroDependencia);
					System.out.println("filtroDependencia Ahora " + (String) parent.getSelectedItem());
					if (!filtroDependencia.equalsIgnoreCase((String) parent.getSelectedItem())) {
						sltPlanTo.setSelection(0);
						sltPlanBa.setSelection(0);
					}

					filtroDependencia = Utilidades.dependenciaOferta((String) parent.getSelectedItem());
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		sltBa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				cambio = true;
				if (!parent.getSelectedItem().toString().equals(Utilidades.inicial)) {
					/*
					 * sltDesBa.setAdapter(llenarDescuentos(sltEstrato
					 * .getSelectedItem().toString(),
					 * Utilidades.tipo_producto_ba_d, parent
					 * .getSelectedItem().toString()));
					 */

					// llenarCotizacionDescuentos(Utilidades.tipo_producto_ba_d);

				} else {
					sltDesBa.setAdapter(vaciarDescuento());
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		sltPlanBa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				llenarBa();
				cambio = true;
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		slt3G.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				if (!parent.getSelectedItem().toString().equals(Utilidades.inicial)) {
					sltDes3G.setAdapter(llenarDescuentos(sltEstrato.getSelectedItem().toString(),
							Utilidades.tipo_producto_3g_d, parent.getSelectedItem().toString()));

					llenarCotizacionDescuentos(Utilidades.tipo_producto_3g_d);
				} else {

				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		slt4G.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				if (!parent.getSelectedItem().toString().equals(Utilidades.inicial)) {
					sltDes4G.setAdapter(llenarDescuentos(sltEstrato.getSelectedItem().toString(),
							Utilidades.tipo_producto_4g_d, parent.getSelectedItem().toString()));

					llenarCotizacionDescuentos(Utilidades.tipo_producto_4g_d);

				} else {
					sltDes4G.setAdapter(vaciarDescuento());
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});

		llenarProductosPlan();
		MainActivity.context = this;

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Utilidades.excluir("ConsultarSmartPromo", cliente.getCiudad())) {
			lanzarTipoHogar();
		}
	}

	public void Iniciar_Descuentos() {
		sltDesTo.setAdapter(vaciarDescuento());
		sltDesTv.setAdapter(vaciarDescuento());
		sltDesBa.setAdapter(vaciarDescuento());
		sltDes3G.setAdapter(vaciarDescuento());
		sltDes4G.setAdapter(vaciarDescuento());
	}

	private void validarPermisosDecos() {
		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(MainActivity.config.getCodigo_asesor());
		parametros.add("Fideliza");
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("validarPermiso");
		params.add(parametros);
		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);
		simulador.execute(params);
	}

	public void llenarEstrato() {

		ArrayAdapter<String> adaptador = null;

		adaptador = (ArrayAdapter<String>) sltEstrato.getAdapter();

		String estrato = (String) sltEstrato.getSelectedItem();

		System.out.println("estrato " + estrato);

		sltEstrato.setSelection(adaptador.getPosition((String) cliente.getEstrato()));

		sltEstrato.setEnabled(false);

	}

	public void llenarCotizacionDescuentos(String Tipo) {

		ArrayAdapter<String> adaptador = null;
		if (Validar) {

			if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_to_d)) {
				adaptador = (ArrayAdapter<String>) sltDesTo.getAdapter();
				sltDesTo.setSelection(adaptador.getPosition(cotizacion.getPromoTo()));
			} else if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_tv_d)) {
				adaptador = (ArrayAdapter<String>) sltDesTv.getAdapter();
				sltDesTv.setSelection(adaptador.getPosition(cotizacion.getPromoTv()));
			} else if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_ba_d)) {
				adaptador = (ArrayAdapter<String>) sltDesBa.getAdapter();
				sltDesBa.setSelection(adaptador.getPosition(cotizacion.getPromoBa()));
			} else if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_3g_d)) {
				adaptador = (ArrayAdapter<String>) sltDes3G.getAdapter();
				sltDes3G.setSelection(adaptador.getPosition(cotizacion.getPromo3g()));
			} else if (Tipo.equalsIgnoreCase(Utilidades.tipo_producto_4g_d)) {
				adaptador = (ArrayAdapter<String>) sltDes4G.getAdapter();
				sltDes4G.setSelection(adaptador.getPosition(cotizacion.getPromo4g()));
			}
		}

	}

	private void llenarProductosPlan() {

		ArrayAdapter<String> adaptador = null;
		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.plan);

		sltPlanTo.setAdapter(adaptador);
		sltPlanTv.setAdapter(adaptador);
		sltPlanBa.setAdapter(adaptador);

		if (Validar) {

			adaptador = (ArrayAdapter<String>) sltEstrato.getAdapter();
			// sltEstrato.setSelection(adaptador.getPosition(cotizacion.getEstrato()));

			sltEstrato.setSelection(adaptador.getPosition(cliente.getEstrato()));

			adaptador = (ArrayAdapter<String>) sltPlanTo.getAdapter();
			sltPlanTo.setSelection(adaptador.getPosition(cotizacion.getTipoTo()));

			adaptador = (ArrayAdapter<String>) sltPlanTv.getAdapter();
			sltPlanTv.setSelection(adaptador.getPosition(cotizacion.getTipoTv()));
			adaptador = (ArrayAdapter<String>) sltPlanBa.getAdapter();
			sltPlanBa.setSelection(adaptador.getPosition(cotizacion.getTipoBa()));
		}
	}

	private void llenarTo() {
		ArrayAdapter<String> adaptador = null;
		ArrayList<ArrayList<String>> respuesta;

		String planTo = Utilidades.planNumerico((String) sltPlanTo.getSelectedItem());

		String estrato = (String) sltEstrato.getSelectedItem();

		if (planTo.equals("0") || planTo.equals("1")) {

			String clausula = "";
			String[] valores = null;

			if (filtroDependencia.equalsIgnoreCase("") && !dependenciaTV) {
				clausula = "Departamento=? and Tipo_Producto=? and Nuevo IN(?,?) and Estrato like ? and Tecnologia like ?";
				valores = new String[] { Departamento(), Utilidades.tipo_producto_to, planTo, "2", "%" + estrato + "%",
						"%" + cliente.getTecnologia() + "%" };
			} else {

				if (filtroDependencia.equalsIgnoreCase("null")) {
					filtroDependencia = "";
				}

				clausula = "Departamento=? and Tipo_Producto=? and Nuevo IN(?,?) and Estrato like ? and Tecnologia like ?  and Oferta =? ";
				valores = new String[] { Departamento(), Utilidades.tipo_producto_to, planTo, "2", "%" + estrato + "%",
						"%" + cliente.getTecnologia() + "%", filtroDependencia };
			}

			// if (!Utilidades.excluir("deshabilitarCobertura",
			// cliente.getCiudad())) {
			// if (!Utilidades.tratarCobertura(cliente.getCobertura())) {
			// clausula += "and Producto not like ?";
			// valores = new String[] { Departamento(),
			// Utilidades.tipo_producto_to, planTo, "2",
			// "%" + estrato + "%", "%Digital%" };
			// }
			// }

			respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" }, clausula,
					valores, null, null, null);
		} else {

			String clausula = "Departamento=? and Tipo_Producto=? and Nuevo=?  and Estrato like ? and Tecnologia like ?";
			String[] valores = new String[] { Departamento(), Utilidades.tipo_producto_to, planTo, "%" + estrato + "%",
					"%" + cliente.getTecnologia() + "%" };

			// if (!Utilidades.excluir("deshabilitarCobertura",
			// cliente.getCiudad())) {
			// if (!Utilidades.tratarCobertura(cliente.getCobertura())) {
			// clausula += "and Producto not like ?";
			// valores = new String[] { Departamento(),
			// Utilidades.tipo_producto_to, planTo,
			// "%" + estrato + "%", "%Digital%" };
			// }
			// }

			respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" }, clausula,
					valores, null, null, null);
		}

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

		adaptador.add(Utilidades.inicial);

		if (respuesta != null) {
			for (ArrayList<String> arrayList : respuesta) {
				adaptador.add(arrayList.get(0));
			}
		}

		sltTo.setAdapter(adaptador);

		if (cotizacion != null) {
			adaptador = (ArrayAdapter<String>) sltTo.getAdapter();
			sltTo.setSelection(adaptador.getPosition(cotizacion.getTelefonia()));
		}
	}

	private void llenarTv() {
		ArrayAdapter<String> adaptador = null;
		ArrayList<ArrayList<String>> respuesta = null;

		String planTv = Utilidades.planNumerico((String) sltPlanTv.getSelectedItem());
		String estrato = (String) sltEstrato.getSelectedItem();

		if (planTv.equalsIgnoreCase("0")) {
			chktvAnaloga.setEnabled(true);
			aplicarAnaloga = "0";
		} else {
			chktvAnaloga.setEnabled(false);
			chktvAnaloga.setChecked(false);
			aplicarAnaloga = "N/A";
		}

		if (planTv.equals("0") || planTv.equals("1")) {

			String clausula = "Departamento=? and Tipo_Producto=? and Nuevo IN(?,?) and Estrato like ?  and Tecnologia like ?";
			String[] valores = new String[] { Departamento(), Utilidades.tipo_producto_tv, planTv, "2",
					"%" + estrato + "%", "%" + cliente.getTecnologia() + "%" };

			// if (!Utilidades.excluir("deshabilitarCobertura",
			// cliente.getCiudad())) {
			// if (!Utilidades.tratarCobertura(cliente.getCobertura())) {
			// clausula += "and Producto not like ?";
			// valores = new String[] { Departamento(),
			// Utilidades.tipo_producto_tv, planTv, "2",
			// "%" + estrato + "%", "%DIGITAL%" };
			// }
			// }

			respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" }, clausula,
					valores, null, null, null);
		} else {
			String existente = Utilidades.Existente(Departamento());
			/*
			 * System.out.println("existente " + existente + "Departamento() " +
			 * Departamento());
			 */
			if (existente.equalsIgnoreCase("Todas") || existente.equalsIgnoreCase("")) {

				String clausula = "Departamento=? and Tipo_Producto=? and Nuevo=? and Estrato like ?  and Tecnologia like ?";
				String[] valores = new String[] { Departamento(), Utilidades.tipo_producto_tv, planTv,
						"%" + estrato + "%", "%" + cliente.getTecnologia() + "%" };

				respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" }, clausula,
						valores, null, null, null);
			} else {

				String clausula = "Departamento=? and Tipo_Producto=? and Nuevo=? and Producto=? and Estrato like ?  and Tecnologia like ?";
				String[] valores = new String[] { Departamento(), Utilidades.tipo_producto_tv, planTv, existente,
						"%" + estrato + "%", "%" + cliente.getTecnologia() + "%" };

				respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" }, clausula,
						valores, null, null, null);
			}

		}
		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adaptador.add(Utilidades.inicial);
		if (respuesta != null) {

			System.out.println("respuesta consulta TV " + respuesta);
			for (ArrayList<String> arrayList : respuesta) {

				System.out.println("antes consulta filtrarTV " + arrayList.get(0));

				if (!Utilidades.excluirMunicipal("filtrarTV", arrayList.get(0), cliente.getCiudad())) {

					System.out.println("antes consulta cobertura " + arrayList.get(0));

					if (!Utilidades.excluir("deshabilitarCobertura", cliente.getCiudad())) {

						System.out.println("antes consulta Hfc" + arrayList.get(0));

						if (Utilidades.excluirMunicipal("bloquearCobertura", "bloquearCobertura",
								cliente.getCiudad())) {

							System.out.println("bloqueoCobertura " + bloqueoCobertura);

							System.out.println(
									"bloqueoCobertura.getDefaulCobertura() " + bloqueoCobertura.getDefaulCobertura());

							System.out.println(
									"bloqueoCobertura.isCoberturaREDCO() " + bloqueoCobertura.isCoberturaREDCO());

							if (bloqueoCobertura.isCoberturaREDCO() || bloqueoCobertura.isCoberturaHFC()
									|| cliente.isControlCerca()) {

								System.out.println("bloqueoCobertura.getMedioTV() " + bloqueoCobertura.getMedioTV());

								System.out.println("bloqueoCobertura.getConsultaMedioTV() "
										+ bloqueoCobertura.getConsultaMedioTV());

								if (bloqueoCobertura.getConsultaMedioTV() != null
										&& !bloqueoCobertura.getConsultaMedioTV().equalsIgnoreCase("")) {
									if (Utilidades.pintarDigital(arrayList.get(0),
											bloqueoCobertura.getConsultaMedioTV(), cliente.getCiudad())) {
										adaptador.add(arrayList.get(0));
										bloqueoCobertura.setMedioTV(bloqueoCobertura.getConsultaMedioTV());
									}

								} else {
									if (bloqueoCobertura.isCoberturaHFC()
											|| (cliente.isControlCerca() && !cliente.getTecnologia().equals("REDCO"))) {
										if (Utilidades.pintarDigital(arrayList.get(0), "HFC", cliente.getCiudad())) {
											adaptador.add(arrayList.get(0));
											bloqueoCobertura.setMedioTV("HFC");
										}
									} else if (bloqueoCobertura.isCoberturaREDCO()
											|| (cliente.isControlCerca() && cliente.getTecnologia().equals("REDCO"))) {
										if (Utilidades.pintarDigital(arrayList.get(0), "IPTV", cliente.getCiudad())) {
											adaptador.add(arrayList.get(0));
											bloqueoCobertura.setMedioTV("IPTV");
										}
									}
								}
							}

						} else {
							if (Utilidades.tratarCobertura(cliente.getCobertura(), this)) {

								System.out.println("despues consulta Hfc" + arrayList.get(0));

								if (Utilidades.pintarDigital(arrayList.get(0), "HFC", cliente.getCiudad())) {
									adaptador.add(arrayList.get(0));
								}
							} else {
								System.out.println("antes filtro redco " + arrayList.get(0));

								if (Utilidades.pintarDigital(arrayList.get(0), "IPTV", cliente.getCiudad())) {
									adaptador.add(arrayList.get(0));
								}
							}
						}

					} else {
						adaptador.add(arrayList.get(0));
					}
				}
			}
		}

		sltTv.setAdapter(adaptador);

		if (cotizacion != null) {
			adaptador = (ArrayAdapter<String>) sltTv.getAdapter();
			sltTv.setSelection(adaptador.getPosition(cotizacion.getTelevision()));
		}
	}

	private void llenarBa() {
		ArrayAdapter<String> adaptador = null;
		ArrayList<ArrayList<String>> respuesta = null;
		String planBa = Utilidades.planNumerico((String) sltPlanBa.getSelectedItem());

		String estrato = (String) sltEstrato.getSelectedItem();

		if (planBa.equals("0") || planBa.equals("1")) {

			if (filtroDependencia.equalsIgnoreCase("") && !dependenciaTV) {

				respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" },
						"Departamento=? and Tipo_Producto=? and Nuevo IN(?,?) and Estrato like ?  and Tecnologia like ?",
						new String[] { Departamento(), Utilidades.tipo_producto_ba, planBa, "2", "%" + estrato + "%",
								"%" + cliente.getTecnologia() + "%" },
						null, null, null);
			} else {

				if (filtroDependencia.equalsIgnoreCase("null")) {
					filtroDependencia = "";
				}

				respuesta = MainActivity.basedatos
						.consultar(false, "Productos", new String[] { "Producto" },
								"Departamento=? and Tipo_Producto=? and Nuevo IN(?,?) and Estrato like ?  and Tecnologia like ? and Oferta=?",
								new String[] { Departamento(), Utilidades.tipo_producto_ba, planBa, "2",
										"%" + estrato + "%", "%" + cliente.getTecnologia() + "%", filtroDependencia },
								null, null, null);
			}

		} else {

			respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" },
					"Departamento=? and Tipo_Producto=? and Nuevo=? and Estrato like ?  and Tecnologia like ?",
					new String[] {

							Departamento(), Utilidades.tipo_producto_ba, planBa, "%" + estrato + "%",
							"%" + cliente.getTecnologia() + "%" },
					null, null, null);

		}

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adaptador.add(Utilidades.inicial);

		if (respuesta != null) {
			for (ArrayList<String> arrayList : respuesta) {
				// adaptador.add(arrayList.get(0));
				if (!cliente.getCiudad().equalsIgnoreCase("BogotaREDCO")) {

					if (filtrarProducto(arrayList.get(0))) {
						if (pasarFiltro(arrayList.get(0))) {
							adaptador.add(arrayList.get(0));
						}
					} else {
						adaptador.add(arrayList.get(0));
					}

				} else {
					if (!arrayList.get(0).equalsIgnoreCase("Segunda Banda Ancha")) {
						adaptador.add(arrayList.get(0));
					}
				}
			}
		}

		sltBa.setAdapter(adaptador);

		if (cotizacion != null) {
			adaptador = (ArrayAdapter<String>) sltBa.getAdapter();
			sltBa.setSelection(adaptador.getPosition(cotizacion.getInternet()));
		}
	}

	public boolean pasarFiltro(String Producto) {
		boolean pasarFiltro = false;
		System.out.println("Producto " + Producto);
		System.out.println("cliente.getCiudad() " + cliente.getCiudad());
		pasarFiltro = Utilidades.excluir(Producto, cliente.getCiudad());
		System.out.println("pasarFiltro " + pasarFiltro);
		return pasarFiltro;

	}

	public boolean filtrarProducto(String Producto) {
		boolean filtar = false;
		filtar = Utilidades.excluirNacional("filtrar", Producto);
		return filtar;

	}

	private void llenarProductos() {

		ArrayAdapter<String> adaptador = null;
		ArrayList<ArrayList<String>> respuesta = null;

		respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" },
				"Departamento=? and Tipo_Producto=?", new String[] { Departamento(), Utilidades.tipo_producto_3g },
				null, null, null);
		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adaptador.add(Utilidades.inicial);
		if (respuesta != null) {
			for (ArrayList<String> arrayList : respuesta) {
				adaptador.add(arrayList.get(0));
			}
		}

		slt3G.setAdapter(adaptador);

		respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" },
				"Departamento=? and Tipo_Producto=?", new String[] { Departamento(), Utilidades.tipo_producto_4g },
				null, null, null);
		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adaptador.add(Utilidades.inicial);
		if (respuesta != null) {
			for (ArrayList<String> arrayList : respuesta) {
				adaptador.add(arrayList.get(0));
			}
		}

		slt4G.setAdapter(adaptador);

	}

	private ArrayAdapter<String> llenarDescuentos(String estrato, String tipoProducto, String producto) {

		ArrayAdapter<String> adaptador = null;
		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Descuentos",
				new String[] { "Descuentos" },
				"(Ciudad like ? or Ciudad='-') and Tipo_Producto = ? and (Producto like ? or Producto='-') and (Estrato like ? or Estrato = '-')",
				new String[] { "%" + cliente.getCiudad() + "%", tipoProducto, "%" + producto + "%",
						"%" + estrato + "%" },
				null, null, null);

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adaptador.add(Utilidades.inicial_des);
		if (respuesta != null) {
			for (ArrayList<String> arrayList : respuesta) {
				adaptador.add(arrayList.get(0));
			}
		}

		return adaptador;
	}

	private ArrayAdapter<String> llenarDescuentosNew(String Descuento, String tipoProducto) {

		ArrayAdapter<String> adaptador = null;

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adaptador.add(Utilidades.inicial_des);
		adaptador.add(Descuento);
		return adaptador;
	}

	private ArrayAdapter<String> vaciarDescuento() {
		ArrayAdapter<String> adaptador = null;

		adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		adaptador.add(Utilidades.inicial_sindes);

		return adaptador;
	}

	private void llenarAdicionales(String tipoAdicional) {
		String tipo = "", tipoProducto = "tv";
		String estrato = (String) sltEstrato.getSelectedItem();

		if (tipoAdicional.contains("HFC")) {
			tipo = "HFC";
		} else if (tipoAdicional.contains("IPTV")) {
			tipo = "IPTV";
		} else if (tipoAdicional.contains("DIGITAL")) {
			tipo = "HFC";
		} else if (Utilidades.pintarDigital(tipoAdicional, "HFC", cliente.getCiudad())) {
			tipo = "HFC";
		} else {
			tipo = "IPTV";
		}

		String excluirAdicional = Utilidades.excluirAdicional(cliente.getCiudad(), tipo, "excluirAdicional");

		String Oferta = Utilidades.OfertaProductos(tipoAdicional, "prod");

		ArrayList<ArrayList<String>> respuesta = null;

		System.out.println("excluirAdicional " + excluirAdicional);

		if (excluirAdicional == null) {
			respuesta = MainActivity.basedatos.consultar(false, "Adicionales",
					new String[] { "adicional", "tarifaIva" },
					"departamento like ? and producto like ? and tipoProducto = ? and estrato like ? and (oferta=? or oferta=?)",
					new String[] { "%" + Departamento() + "%", "%" + tipo + "%", tipoProducto, "%" + estrato + "%",
							Oferta, "" },
					null, "producto,adicional ASC", null);
		} else {

			String query = "Select adicional, tarifaIva from Adicionales where " + "departamento like '%"
					+ Departamento() + "%' and producto like '%" + tipo + "%' and estrato like '%" + estrato
					+ "%'and tipoProducto='" + tipoProducto + "' and oferta in ('" + Oferta
					+ "','') and adicional not in(" + excluirAdicional + ")";

			respuesta = MainActivity.basedatos.consultar2(query);
		}

		ArrayList<ListaChecked> contenido = new ArrayList<ListaChecked>();
		int i = 2;

		if (respuesta != null) {
			for (ArrayList<String> arrayList : respuesta) {
				if (i == 2) {
					i = 1;
				} else {
					i = 2;
				}
				if (cotizacion != null) {
					contenido.add(new ListaChecked(i, arrayList.get(1), arrayList.get(0), cotizacion.getAdicionales()));
				} else {
					if (UtilidadesTarificador.adicionalDependiente(tipoAdicional) != null) {
						contenido.add(new ListaChecked(i, arrayList.get(1), arrayList.get(0),
								UtilidadesTarificador.adicionalDependiente(tipoAdicional)));
					} else {
						contenido.add(new ListaChecked(i, arrayList.get(1), arrayList.get(0), null));
					}
				}

			}
			ListaCheckedAdapter adaptador = new ListaCheckedAdapter(this, contenido, this);
			lsvAdicionales.setAdapter(adaptador);
		} else {
			Limpiar_Adicionales();
		}
	}

	public void tvDependiente() {

	}

	private void Limpiar_Adicionales() {
		ArrayList<ListaChecked> contenido = new ArrayList<ListaChecked>();
		contenido.clear();
		ListaCheckedAdapter adaptador = new ListaCheckedAdapter(this, contenido, this);
		lsvAdicionales.setAdapter(adaptador);
	}

	private Integer obtenerValorAdicionales() {
		int valor = 0;
		ListaCheckedAdapter lca = (ListaCheckedAdapter) lsvAdicionales.getAdapter();
		if (lca != null) {
			valor = lca.calcularValor();
		}
		return valor;

	}

	private String[][] obtenerAdicionales() {
		String[][] adicionales = null;
		ListaCheckedAdapter lca = (ListaCheckedAdapter) lsvAdicionales.getAdapter();
		if (lca != null) {
			adicionales = lca.Adicionales();
		}
		return adicionales;

	}

	public void consultar() {

		validarAdicionales = true;

		String estrato = (String) sltEstrato.getSelectedItem();

		System.out.println("estrato cotizacion " + estrato);
		String[][] adicionales = obtenerAdicionales();
		btnDescuentos.setVisibility(View.GONE);
		chkAplicarAd.setVisibility(View.GONE);

		/*
		 * System.out.println("obtenerValorAdicionales " +
		 * obtenerValorAdicionales());
		 */

		System.out.println("sltEstrato.getSelectedItem().toString() " + sltEstrato.getSelectedItem().toString());
		System.out.println("cliente.getTecnologia() " + cliente.getTecnologia());

		if (!sltEstrato.getSelectedItem().toString().equalsIgnoreCase(Utilidades.inicial_estrato)
				&& !cliente.getTecnologia().equals(Utilidades.inicial_opcion)) {

			validarTvAnaloga();

			tarificador.Consulta_Tarifaz((String) sltTo.getSelectedItem(), (String) sltTv.getSelectedItem(),
					(String) sltBa.getSelectedItem(), (String) slt3G.getSelectedItem(),
					(String) slt4G.getSelectedItem(), (String) sltDesTo.getSelectedItem(),
					(String) sltDesTv.getSelectedItem(), (String) sltDesBa.getSelectedItem(),
					(String) sltDes3G.getSelectedItem(), (String) sltDes4G.getSelectedItem(), obtenerAdicionales(),
					obtenerValorAdicionales(), (String) sltEstrato.getSelectedItem(), isNuevo(), cliente.getCiudad(),
					UtilidadesTarificador.jsonDatos(cliente, cliente.getSmartPromo(), "N/A", aplicarAnaloga), this,
					cmpDecos.obtenerTotalDecos());

			if (!((String) sltTv.getSelectedItem()).equals("-")) {

				// UtilidadesDecos.imprimirDecos("consolidar",
				// itemDecodificadores);
			}

			resumen = tarificador.Array_Cotizacion();

			if (resumen.get(0).getTipo().equalsIgnoreCase("0")) {

				String mensaje = resumen.get(0).getDato();
				dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA, mensaje);
				dialogo.dialogo.show();
				resumen = tarificador.Array_Cotizacion_Limpio();
				rt.resumen(resumen);

			} else {

				resumen.remove(0);
				rt.resumen(resumen);
				txtEstrato.setText(estrato);
				String adicional = "";
				for (int i = 0; i < adicionales.length; i++) {
					if (adicionales[i][0] != null) {
						adicional += adicionales[i][0] + ",";
					}
				}

				// txtAdicionales.setText(adicional);
				txtLocalizacion.setText(cliente.getCiudad() + " - " + MainActivity.config.getDepartamento());

				itemPromocionesAdicionales = tarificador.itemPromocionesAdicionales;

				countAdicionales();

				boolean mostrarAplicar = false;

				for (int i = 0; i < itemPromocionesAdicionales.size(); i++) {

					if (itemPromocionesAdicionales.get(0).getAdicional().equalsIgnoreCase("Vacia")) {
						// System.out.println("Sin respuesta");
						btnDescuentos.setVisibility(View.GONE);
						chkAplicarAd.setVisibility(View.GONE);
						break;
					} else {
						btnDescuentos.setVisibility(View.VISIBLE);

						if (!itemPromocionesAdicionales.get(i).getMeses().equalsIgnoreCase("0 Meses")) {
							mostrarAplicar = true;
							aplicarAd = true;
						}
					}
				}

				if (mostrarAplicar) {
					chkAplicarAd.setVisibility(View.VISIBLE);
					if (aplicarAd) {
						chkAplicarAd.setChecked(true);
					}
				}

				validarPortafolio(resumen);

			}
		}

		else {
			resumen = tarificador.Array_Cotizacion_Limpio();
			rt.resumen(resumen);
		}
	}

	public boolean isNuevo() {
		boolean nuevo = true;
		if (!sltPlanTo.getSelectedItem().equals("-") && !sltTo.getSelectedItem().equals("--Seleccione Producto--")) {
			if (!sltPlanTo.getSelectedItem().equals("N")) {
				nuevo = false;
			}
		}

		if (!sltPlanTv.getSelectedItem().equals("-") && !sltTv.getSelectedItem().equals("--Seleccione Producto--")) {
			if (!sltPlanTv.getSelectedItem().equals("N")) {
				nuevo = false;
			}
		}

		if (!sltPlanBa.getSelectedItem().equals("-") && !sltBa.getSelectedItem().equals("--Seleccione Producto--")) {
			if (!sltPlanBa.getSelectedItem().equals("N")) {
				nuevo = false;
			}
		}

		// System.out.println("Nuevo " + nuevo);
		return nuevo;
	}

	public void llenarDescuentos(ArrayList<ArrayList<String>> promociones) {
		ArrayList<String> promocion;
		boolean controlTo = false;
		boolean controlBa = false;
		boolean controlTv = false;

		if (promociones != null) {
			for (int i = 0; i < promociones.size(); i++) {
				promocion = promociones.get(i);

				if (Utilidades.tipo_producto_to.equalsIgnoreCase(promocion.get(0))
						&& !promocion.get(1).equalsIgnoreCase("0")) {
					sltDesTo.setAdapter(llenarDescuentosNew(promocion.get(1) + "%", promocion.get(0)));
					controlTo = true;
				} else if (Utilidades.tipo_producto_tv.equalsIgnoreCase(promocion.get(0))
						&& !promocion.get(1).equalsIgnoreCase("0")) {
					sltDesTv.setAdapter(llenarDescuentosNew(promocion.get(1) + "%", promocion.get(0)));
					controlTv = true;
				} else if (Utilidades.tipo_producto_ba.equalsIgnoreCase(promocion.get(0))
						&& !promocion.get(1).equalsIgnoreCase("0")) {
					sltDesBa.setAdapter(llenarDescuentosNew(promocion.get(1) + "%", promocion.get(0)));
					controlBa = true;
				}
			}
		}

		if (!controlTo) {
			sltDesTo.setAdapter(vaciarDescuento());
		}

		if (!controlTv) {
			sltDesTv.setAdapter(vaciarDescuento());
		}

		if (!controlBa) {
			sltDesBa.setAdapter(vaciarDescuento());
		}
	}

	public ArrayList<ArrayList<String>> consultarDescuentos() {

		ArrayList<ArrayList<String>> promociones = null;

		if (!sltEstrato.getSelectedItem().toString().equalsIgnoreCase(Utilidades.inicial_estrato) && cambio) {
			promociones = tarificador.Descuentos((String) sltPlanTo.getSelectedItem(), (String) sltTo.getSelectedItem(),
					(String) sltPlanTv.getSelectedItem(), (String) sltTv.getSelectedItem(),
					(String) sltPlanBa.getSelectedItem(), (String) sltBa.getSelectedItem());
		}

		return promociones;
	}

	public ArrayList<ArrayList<String>> consultarDescuentos2() {

		ArrayList<ArrayList<String>> promociones = null;

		if (!sltEstrato.getSelectedItem().toString().equalsIgnoreCase(Utilidades.inicial_estrato) && cambio) {

			validarTvAnaloga();

			promociones = tarificador.Descuentos2((String) sltPlanTo.getSelectedItem(),
					(String) sltTo.getSelectedItem(), (String) sltPlanTv.getSelectedItem(),
					(String) sltTv.getSelectedItem(), (String) sltPlanBa.getSelectedItem(),
					(String) sltBa.getSelectedItem(),
					UtilidadesTarificador.jsonDatos(cliente, cliente.getSmartPromo(), "N/A", aplicarAnaloga));
		}

		return promociones;
	}

	public ArrayList<String> aplicarDescuentos(ArrayList<ArrayList<String>> promociones, String Tipo) {

		/*
		 * System.out.println("promociones " + promociones); System.out.println(
		 * "Tipo " + Tipo);
		 */

		ArrayList<String> promocion = null;
		ArrayList<String> descuento = new ArrayList<String>();

		if (promociones != null) {
			for (int i = 0; i < promociones.size(); i++) {
				promocion = promociones.get(i);
				if (Tipo.equalsIgnoreCase(promocion.get(0)) && !promocion.get(1).equalsIgnoreCase("0")) {
					descuento.add(promocion.get(1) + "%");
					String Duracion;
					if (promocion.get(2).equalsIgnoreCase("1")) {
						Duracion = promocion.get(2) + " Mes ";
					} else {
						Duracion = promocion.get(2) + " Meses ";
					}
					descuento.add(Duracion);
				}
			}
		}

		// System.out.println("descuento " + descuento);

		if (descuento.size() == 0) {
			descuento.add("Sin Promocion");
			descuento.add("N/A");
		}

		return descuento;
	}

	public void procesarCotizacion(View v) {

		boolean trioNuevo = false;
		controlDescuentosAd = false;
		preciosConDescuentoAd = 0.0;
		precioDescuentosAd = 0.0;

		String tv = (String) sltPlanTv.getSelectedItem();
		String to = (String) sltPlanTo.getSelectedItem();
		String ba = (String) sltPlanBa.getSelectedItem();

		if (!sltEstrato.getSelectedItem().equals("--Seleccione Estrato--")
				&& (!sltTo.getSelectedItem().equals("--Seleccione Producto--")
						|| !sltTv.getSelectedItem().equals("--Seleccione Producto--")
						|| !sltBa.getSelectedItem().equals("--Seleccione Producto--")
						|| !slt3G.getSelectedItem().equals("--Seleccione Producto--")
						|| !slt4G.getSelectedItem().equals("--Seleccione Producto--"))) {

			if (cotizacion == null) {
				cotizacion = new Cotizacion();
			}

			cotizacion_venta = tarificador.Array_Cotizacion_Venta();

			ArrayList<String> telefonia = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_to_d);
			ArrayList<String> television = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_tv_d);
			ArrayList<String> adicionales = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_ad);
			ArrayList<String> gota = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_gota);
			ArrayList<String> internet = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_ba_d);
			ArrayList<String> internet_3d = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_3g_d);
			ArrayList<String> internet_4d = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_4g_d);
			ArrayList<String> otros = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_otros);

			System.out.println("otros " + otros);

			// ArrayList<ArrayList<String>> descuentos = consultarDescuentos();
			ArrayList<ArrayList<String>> descuentos = consultarDescuentos2();
			System.out.println("descuentos " + descuentos);

			ArrayList<String> planesFacturacion = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_planes);

			System.out.println("planesFacturacion " + planesFacturacion);

			// cotizacion.setAdicionales(obtenerAdicionales());

			if (otros.get(2).equalsIgnoreCase("3")) {

				if (tv.equalsIgnoreCase("N") && to.equalsIgnoreCase("N") && ba.equalsIgnoreCase("N")) {
					trioNuevo = true;
				}
			}

			if (television != null && television.size() > 0) {
				System.out.println("television " + television);
				ArrayList<String> descuentoTv = aplicarDescuentos(descuentos, Utilidades.tipo_producto_tv);
				cotizacion.setAdicionales(agregarAdicionalesGratis(planesFacturacion.get(1), planesFacturacion.get(4),
						obtenerAdicionales(), television.get(0), tv, otros.get(2), trioNuevo,
						descuentoTv.get(0).toString(), descuentoTv.get(1).toString()));

				cotizacion.setDecodificadores(cmpDecos.getDecos());
				cotizacion.setTotalDecos(cmpDecos.obtenerTotalDecos());

			}
			cotizacion.setTotalAdicionales(obtenerValorAdicionales().toString());

			// agregarAdicionalesGratis(planesFacturacion.get(1),obtenerAdicionales());

			ArrayList<String> ofertas = Utilidades.getCotizacion(cotizacion_venta, "Oferta");

			System.out.println("ofertas " + ofertas);

			cotizacion.setOferta(ofertas.get(0));

			ArrayList<String> controlCotizacion = Utilidades.getCotizacion(cotizacion_venta, "Control");

			System.out.println("controlCotizacion " + controlCotizacion);

			int contProductos = 0;

			if (telefonia != null && telefonia.size() > 0) {

				ArrayList<String> descuentoTo = aplicarDescuentos(descuentos, Utilidades.tipo_producto_to);

				// System.out.println("descuentoTo " + descuentoTo);
				cotizacion.Telefonia((String) sltPlanTo.getSelectedItem(), telefonia.get(0), telefonia.get(1),
						telefonia.get(2), "0", "0", descuentoTo.get(0).toString(), descuentoTo.get(1).toString(), "",
						"", getTipoTecnologiaExistente("TO"));

				cotizacion.setPlanFacturacionTo_I(planesFacturacion.get(0));
				cotizacion.setPlanFacturacionTo_P(planesFacturacion.get(3));

				String tipoCotizacion = Utilidades.planNumerico((String) sltPlanTo.getSelectedItem());
				cotizacion.setTipoCotizacionTo(tipoCotizacion);

				if (tipoCotizacion.equals("1")) {
					System.out.println("Validar segunta TO");
					validarSegundaTelefonia();
				}

				contProductos++;

			}

			if (television != null && television.size() > 0) {
				ArrayList<String> descuentoTv = aplicarDescuentos(descuentos, Utilidades.tipo_producto_tv);
				cotizacion.Television((String) sltPlanTv.getSelectedItem(), television.get(0), television.get(1),
						television.get(2), "0", "0", descuentoTv.get(0).toString(), descuentoTv.get(1).toString(), "",
						"", getTipoTecnologiaExistente("TV"));
				cotizacion.setItemPromocionesAdicionales(itemPromocionesAdicionales);
				cotizacion.setAplicarAd(aplicarAd);

				cotizacion.setPlanFacturacionTv_I(planesFacturacion.get(1));
				cotizacion.setPlanFacturacionTv_P(planesFacturacion.get(4));

				cotizacion.setTipoCotizacionTv(Utilidades.planNumerico((String) sltPlanTv.getSelectedItem()));
				contProductos++;

			}

			/*
			 * if (adicionales != null && adicionales.size() > 0) {
			 * cotizacion.Adicionales(adicionales.get(0), adicionales.get(1),
			 * adicionales.get(2)); }
			 */

			if (internet != null && internet.size() > 0) {
				ArrayList<String> descuentoBa = aplicarDescuentos(descuentos, Utilidades.tipo_producto_ba);
				cotizacion.Internet((String) sltPlanBa.getSelectedItem(), internet.get(0), internet.get(1),
						internet.get(2), "0", "0", descuentoBa.get(0).toString(), descuentoBa.get(1).toString(), "", "",
						getTipoTecnologiaExistente("BA"));

				cotizacion.setPlanFacturacionBa_I(planesFacturacion.get(2));
				cotizacion.setPlanFacturacionBa_P(planesFacturacion.get(5));

				System.out.println("(String)sltPlanBa.getSelectedItem() " + (String) sltPlanBa.getSelectedItem());
				cotizacion.setTipoCotizacionBa(Utilidades.planNumerico((String) sltPlanBa.getSelectedItem()));
				System.out.println("TipoCotizacionBa " + cotizacion.getTipoCotizacionBa());
				contProductos++;

				boolean controlGota = Boolean.parseBoolean(gota.get(0).toString());

				System.out.println("controlGota " + controlGota);

				System.out.println("gota.get(0).toString() " + gota.get(0).toString());

				if (controlGota) {

					String nombreGota = "Gota de " + gota.get(3).toString() + " a " + gota.get(4).toString();

					cotizacion.setGota(controlGota, (int)Math.round(Double.parseDouble(gota.get(1).toString())),
							(int)Math.round(Double.parseDouble(gota.get(2).toString())), gota.get(3).toString(), gota.get(4).toString(),
							nombreGota);

				} else {
					cotizacion.setControlGota(false);
				}
			}

			if (internet_3d != null && internet_3d.size() > 0) {
				cotizacion.Internet_3G(internet_3d.get(0), internet_3d.get(1), internet_3d.get(2), internet_3d.get(3),
						internet_3d.get(4), internet_3d.get(5));
			}

			if (internet_4d != null && internet_4d.size() > 0) {
				cotizacion.Internet_4G(internet_4d.get(0), internet_4d.get(1), internet_4d.get(2), internet_4d.get(3),
						internet_4d.get(4), internet_4d.get(5));
			}

			if (otros != null && otros.size() > 0) {
				cotizacion.Otros(otros.get(0), otros.get(1), otros.get(2), otros.get(3));
			}

			cotizacion.setMedioIngreso("Venta");

			// cotizacion.actualizarCotizacion(idAsesoria);

			// aplicarSmarPromo();

			System.out.println("cliente.getSmartPromo() " + cliente.getSmartPromo());

			if (cliente.getSmartPromo().equalsIgnoreCase("1")) {

				System.out.println("cotizacion.getOferta()" + cotizacion.getOferta());

				if (!Utilidades.excluir(cotizacion.getOferta(), cliente.getCiudad())) {
					if (!Utilidades.excluir("SmarPromo_2.0", cliente.getCiudad())) {
						cotizacion.setPromoTo("-");
						cotizacion.setTiempoPromoTo("0 Meses");
						cotizacion.setPromoTv("-");
						cotizacion.setTiempoPromoTv("0 Meses");
						cotizacion.setPromoBa("-");
						cotizacion.setTiempoPromoBa("0 Meses");
					}
				} else {

					preciosAdDescuento(cotizacion.getAdicionales(), cotizacion.getTotalAdicionales(),
							cotizacion.getItemPromocionesAdicionales());

					if (controlDescuentosAd) {
						cotizacion.setDescuentosAd(controlDescuentosAd, preciosConDescuentoAd, precioDescuentosAd);
					}

					System.out.println("precioDescuentoAD invocacion " + precioDescuentosAd);

					String totalDescuentoEmp = totalDescuentos(cotizacion.getToEmp(), cotizacion.getPromoTo(),
							cotizacion.getTvEmp(), cotizacion.getPromoTv(), cotizacion.getBaEmp(),
							cotizacion.getPromoBa(), cotizacion.getTotalEmp(), precioDescuentosAd);

					if (!totalDescuentoEmp.equalsIgnoreCase("0") && !totalDescuentoEmp.equalsIgnoreCase("0.0")) {
						cotizacion.setTotalEmpDescuento(totalDescuentoEmp);
					}

					String totalDescuentoInd = totalDescuentos(cotizacion.getToInd(), cotizacion.getPromoTo(),
							cotizacion.getTvInd(), cotizacion.getPromoTv(), cotizacion.getBaInd(),
							cotizacion.getPromoBa(), cotizacion.getTotalInd(), precioDescuentosAd);

					System.out.println("totalDescuentoInd " + totalDescuentoInd);

					if (!totalDescuentoInd.equalsIgnoreCase("0") && !totalDescuentoInd.equalsIgnoreCase("0.0")) {
						cotizacion.setTotalIndDescuento(totalDescuentoInd);
					}
				}
			} else {
				if (Utilidades.excluir(cotizacion.getOferta(), cliente.getCiudad())) {

					preciosAdDescuento(cotizacion.getAdicionales(), cotizacion.getTotalAdicionales(),
							cotizacion.getItemPromocionesAdicionales());

					if (controlDescuentosAd) {
						cotizacion.setDescuentosAd(controlDescuentosAd, preciosConDescuentoAd, precioDescuentosAd);
					}

					System.out.println("precioDescuentoAD invocacion " + precioDescuentosAd);

					String totalDescuentoEmp = totalDescuentos(cotizacion.getToEmp(), cotizacion.getPromoTo(),
							cotizacion.getTvEmp(), cotizacion.getPromoTv(), cotizacion.getBaEmp(),
							cotizacion.getPromoBa(), cotizacion.getTotalEmp(), precioDescuentosAd);

					if (!totalDescuentoEmp.equalsIgnoreCase("0") && !totalDescuentoEmp.equalsIgnoreCase("0.0")) {
						cotizacion.setTotalEmpDescuento(totalDescuentoEmp);
					}

					String totalDescuentoInd = totalDescuentos(cotizacion.getToInd(), cotizacion.getPromoTo(),
							cotizacion.getTvInd(), cotizacion.getPromoTv(), cotizacion.getBaInd(),
							cotizacion.getPromoBa(), cotizacion.getTotalInd(), precioDescuentosAd);

					System.out.println("totalDescuentoInd 2 " + totalDescuentoInd);

					if (!totalDescuentoInd.equalsIgnoreCase("0") && !totalDescuentoInd.equalsIgnoreCase("0.0")) {
						cotizacion.setTotalIndDescuento(totalDescuentoInd);
					}
				}
			}

			System.out.println("antes aplicaDescuentoTo() ");

			if (aplicaDescuentoTo()) {

				cotizacion.setPromoTo("100%");
				cotizacion.setTiempoPromoTo("12 Meses");
				cotizacion.setPromoTo_12_100(true);

			} else {
				cotizacion.setPromoTo_12_100(false);
			}

			System.out.println("antes aplicaDescuentoTo() ");

			if (!UtilidadesTarificador.validarBronze(cotizacion, cliente.getCiudad())) {
				controlCotizacion.set(0, "01");
			}

			cotizacion.setCambiarCotizacion(true);

			if (validarCobertura() || cliente.isControlCerca()) {

				if (controlCotizacion.get(0).equalsIgnoreCase("00")) {
					if (UtilidadesTarificador.validarDependencias(cotizacion.getTelevision(),
							cotizacion.getAdicionales(), this)
							&& UtilidadesTarificador.validarDecosMinimos(cotizacion.getTelevision(),
									cotizacion.getDecodificadores(), this)) {

						System.out.println("cliente.isControlEstadoCuenta() " + cliente.isControlEstadoCuenta());

						if (cliente.isControlEstadoCuenta()) {
							if (validarCarrusel()) {
								if (validarDigital()) {
									if (validarTelefonoServicio) {
										if (Utilidades.validarTelefonos(cliente, this)) {
											if (validarEstandarizacion || cliente.isControlCerca() || cliente.isCoberRural()) {

												System.out.println("direccion " + cliente.getDireccion());
												System.out.println("tipoDocumento " + cliente.getTipoDocumento());
												System.out.println("Documento " + cliente.getCedula());

												if ((!cliente.getDireccion().equalsIgnoreCase("") || cliente.isControlCerca())
														&& !cliente.getCedula().equalsIgnoreCase("")
														&& !cliente.getTipoDocumento()
																.equalsIgnoreCase(Utilidades.inicial_opcion)) {
													if (!cliente.getTelefonoDestino().equalsIgnoreCase("")
															&& cliente.getTelefonoDestino().length() >= 7) {
														if (validarAgendaSiebel()) {
															if ((cliente.getTipoPropiedad() != null
																	&& !cliente.getTipoPropiedad()
																			.equalsIgnoreCase(Utilidades.inicial_opcion)
																	&& !cliente.getTipoPropiedad()
																			.equalsIgnoreCase("N/A"))
																	|| Utilidades.excluir("excluirAgenda",
																			cliente.getCiudad())) {
																if(cliente.isRealizoConfronta()){
																	if(cliente.isConfronta()){
																		if (validarCarteraUNE()) {
																			if (validarScooring()) {
																				if (Utilidades.validarCliente(
																						cliente.getCiudad(),
																						scooring.getAccion(),
																						scooring.getEstadoValidador(), this)) {

																					if (validarAdicionales) {
																						if (sltBa.getSelectedItem().equals(
																								"Segunda Banda Ancha")) {
																							if (procesarSegundaBA()) {
																								resultCotizador("venta");
																							}
																						} else {
																							resultCotizador("venta");
																						}
																					} else {
																						Toast.makeText(this,
																								"Verifique hay incompatibilidad en los adicionales",
																								Toast.LENGTH_SHORT).show();
																					}
																				}
																			}
																		} else {
																			Toast.makeText(this,
																					"Cliente con calificacion negativa, prospectar por el modulo Gestor de Visitas",
																					Toast.LENGTH_SHORT).show();
																		}
																	}else{
																		Toast.makeText(this,
																				getResources().getText(R.string.mensajeconfrontanoaprobado),
																				Toast.LENGTH_SHORT).show();
																	}
																}else{
																	Toast.makeText(this,
																			getResources().getText(R.string.mensajeconfrontanorealizado),
																			Toast.LENGTH_SHORT).show();
																	resultCotizador("estado cuenta");
																}
															} else {
																Toast.makeText(this, "No ha Ingresado TipoPropiedad",
																		Toast.LENGTH_SHORT).show();
															}
														} else {
															try {
																Toast.makeText(this,
																		"Faltan Datos Necesarios Para Agendar En Siebel",
																		Toast.LENGTH_SHORT).show();
																Intent intent = new Intent(
																		MainActivity.MODULO_MENSAJES);
																intent.putExtra("mensajes",
																		Validaciones.getMensajes().toString());
																startActivityForResult(intent,
																		MainActivity.REQUEST_CODE);
															} catch (Exception e) {
																//Log.w("Error Validacion Agenda ", e.getMessage());
															}
														}
													} else {
														Toast.makeText(this, "No ha Ingresado Telefono de destino",
																Toast.LENGTH_SHORT).show();
													}
												} else {
													Toast.makeText(this,
															"Los Campos Tipo Documento, Documento Y Dirección Siempre Debe Estar Diligenciados Antes De Consolidar La Venta",
															Toast.LENGTH_SHORT).show();
												}
											} else {
												Toast.makeText(this,
														getResources().getString(
																R.string.campostipodocumentodireccionsiemprediligenciados),
														Toast.LENGTH_SHORT).show();
											}
										}
									} else {
										Toast.makeText(this,
												getResources().getString(R.string.telefonoservicioinvalido),
												Toast.LENGTH_SHORT).show();
									}
								}
							} else {
								Toast.makeText(this, getResources().getString(R.string.mensajeCarrusel),
										Toast.LENGTH_SHORT).show();
								cliente.setCarrusel(true);
                                cliente.setProductosCarrusel(UtilidadesTarificador.productosCotizacionCarrusel(cotizacion));
								resultCotizador("gerencia de ruta");
							}
						} else {
							Toast.makeText(this,
									"Recuerda consultar el estado de cuenta del cliente para continuar con la venta",
									Toast.LENGTH_SHORT).show();
							resultCotizador("estado cuenta");
						}
					}
				} else {
					Toast.makeText(this, getResources().getString(R.string.cotizacioninvalidanocontinua),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, getResources().getString(R.string.sincoberturaprospecte), Toast.LENGTH_SHORT)
						.show();
			}

		} else {
			Toast.makeText(this, "Seleccione productos para cotizar", Toast.LENGTH_SHORT).show();
			tabs.setCurrentTab(0);
		}

	}


	public boolean validarCobertura() {

		boolean pasa = false;

		if (Utilidades.excluirMunicipal("bloquearCobertura", "bloquearCobertura", cliente.getCiudad())) {

			if (productosNuevos()) {
				if (!cotizacion.getTipoTv().equalsIgnoreCase("-") && !cotizacion.getTelevision().equalsIgnoreCase("")) {

					if(bloqueoCobertura.isHFCDigital()){
					  cotizacion.setHFCDigital(true);	
					}else{
						cotizacion.setHFCDigital(false);		
					}
					
					System.out.println("bloqueoCobertura.getMedioTV() validacion " + bloqueoCobertura.getMedioTV());

					if (bloqueoCobertura.getMedioTV().equalsIgnoreCase("HFC")) {
						if (bloqueoCobertura
								.isCoberturaHFC()/*
													 * && bloqueoCobertura.
													 * isDisponibilidadHFC()
													 */) {
							pasa = true;
						} else if (bloqueoCobertura
								.isCoberturaREDCO()/*
													 * && bloqueoCobertura.
													 * isDisponibilidadREDCO()
													 */) {
							bloqueoCobertura.setConsultaMedioTV("IPTV");
							Utilidades.MensajesToast(getResources().getString(R.string.cambiomedioinstalaciontviptv),
									this);
							cambioMedioTV = true;
						}

					} else if (bloqueoCobertura.getMedioTV().equalsIgnoreCase("IPTV")) {
						if (bloqueoCobertura
								.isCoberturaREDCO()/*
													 * && bloqueoCobertura.
													 * isDisponibilidadREDCO()
													 */) {
							pasa = true;
						} else if (bloqueoCobertura
								.isCoberturaHFC()/*
													 * && bloqueoCobertura.
													 * isDisponibilidadHFC()
													 */) {
							bloqueoCobertura.setConsultaMedioTV("HFC");
							Utilidades.MensajesToast(getResources().getString(R.string.cambiomedioinstalaciontvhfc),
									this);
							cambioMedioTV = true;
						}
					}

				} else {
					if (bloqueoCobertura
							.isCoberturaHFC()/*
												 * && bloqueoCobertura.
												 * isDisponibilidadHFC()
												 */) {
						pasa = true;
					} else if (bloqueoCobertura
							.isCoberturaREDCO()/*
												 * && bloqueoCobertura.
												 * isDisponibilidadREDCO()
												 */) {
						pasa = true;
					}
				}
			} else {
				pasa = true;
			}

		} else {
			pasa = true;
		}

		return pasa;
	}

	public void aplicarSmarPromo() {
		boolean trioDigitalNuevo = true;

		if (cotizacion.getContadorProductos().equalsIgnoreCase("3")) {

			trioDigitalNuevo = UtilidadesTarificador.validarTrioDigitalNuevo(cotizacion.getTipoTo(),
					cotizacion.getTelefonia(), cotizacion.getTipoTv(), cotizacion.getTelevision(),
					cotizacion.getTipoBa(), cotizacion.getInternet(), this);

			System.out.println("lista Negra tO digital " + trioDigitalNuevo);

		} else {
			trioDigitalNuevo = false;
		}

	}

	public boolean aplicaDescuentoTo() {

		boolean aplica = false;
		boolean trioDigitalNuevo = true;

		if (cotizacion.getContadorProductos().equalsIgnoreCase("3")) {

			trioDigitalNuevo = UtilidadesTarificador.validarTrioDigitalNuevo(cotizacion.getTipoTo(),
					cotizacion.getTelefonia(), cotizacion.getTipoTv(), cotizacion.getTelevision(),
					cotizacion.getTipoBa(), cotizacion.getInternet(), this);

			System.out.println("lista Negra tO digital " + trioDigitalNuevo);

		} else {
			trioDigitalNuevo = false;
		}

		if (trioDigitalNuevo) {
			if (Utilidades.validarNacionalEstrato("EstratoPromoTO", cotizacion.getEstrato())) {
				// aplica = true;
				if (Utilidades.validarNacionalValor("DigitalPromoTO", cotizacion.getTelevision())) {
					if (Utilidades.validarNacionalValor("InternetPromoTO", cotizacion.getInternet())) {
						aplica = true;
					} else {
						System.out.println("no es la BA valida " + cotizacion.getInternet());
					}
				} else {
					System.out.println("no es la digital valida " + cotizacion.getTelevision());
				}
			} else {
				System.out.println("no cumple con el estrato " + cotizacion.getEstrato());
			}
		} else {
			System.out.println("no es trio digital " + trioDigitalNuevo);
		}

		System.out.println("aplica promo to" + aplica);

		return aplica;
	}

	public void resultCotizador(String tipoIngreso) {
		Intent intent = new Intent();
		intent.putExtra("cotizacion", cotizacion);
		intent.putExtra("cliente", cliente);
		intent.putExtra("tipoIngreso", tipoIngreso);
		setResult(MainActivity.OK_RESULT_CODE, intent);
		finish();
	}

	public boolean validarDigital() {
		boolean validar = true;
		if (cotizacion.getContadorProductos().equalsIgnoreCase("3")) {

			validar = UtilidadesTarificador.validarTrioDigital(cotizacion.getTipoTo(), cotizacion.getTelefonia(),
					cotizacion.getTipoTv(), cotizacion.getTelevision(), cotizacion.getTipoBa(),
					cotizacion.getInternet(), this);

			System.out.println("lista Negra tO digital " + validar);
		}

		return validar;
	}

	public boolean validarCarrusel() {
		boolean validar = true;

		if(Utilidades.validarPermiso("carrusel") || cliente.isControlCerca()){
			return true;
		}
		
		System.out.println("carrusel cliente.getArraycarrusel() " + cliente.getArraycarrusel());
		if (Utilidades.excluir("MunicipiosCarrusel", cliente.getCiudad())) {
			if (cliente.isControlCarrusel()) {
				System.out.println("carrusel cliente.getCedula() " + cliente.getCedula());
				System.out.println("carrusel cliente.getDocumentoCarrusel() " + cliente.getDocumentoCarrusel());
				System.out.println("carrusel cliente.getDireccion() " + cliente.getDireccion());
				System.out.println("carrusel cliente.getDireccionCarrusel() " + cliente.getDireccionCarrusel());

				if (cliente.getCodigoCarrusel().equals("02") || cliente.getCodigoCarrusel().equals("-1")) {
					System.out.println("Error cliente.getCodigoCarrusel() " + cliente.getCodigoCarrusel());
				} else if (cliente.getCedula().equalsIgnoreCase(cliente.getDocumentoCarrusel())
						&& cliente.getDireccion().equalsIgnoreCase(cliente.getDireccionCarrusel())) {

					if (cliente.getCodigoCarrusel().equals("00") && cliente.getArraycarrusel() != null) {
						System.out.println(
								"carrusel cliente.getArraycarrusel().size() " + cliente.getArraycarrusel().size());
						if (cliente.getArraycarrusel().size() > 0) {
							for (int i = 0; i < cliente.getArraycarrusel().size(); i++) {
								System.out.println("carrusel cliente.getArraycarrusel().get(i).getProducto() "
										+ cliente.getArraycarrusel().get(i).getProducto());
								if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TO")) {
									if (!UtilidadesTarificador.validarCarruselProductos(
											cotizacion.getTipoCotizacionTo(), cotizacion.getTelefonia(), this)) {
										validar = false;
										System.out.println("carrusel to");
										break;
									}
								} else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TV")) {
									if (!UtilidadesTarificador.validarCarruselProductos(
											cotizacion.getTipoCotizacionTv(), cotizacion.getTelevision(), this)) {
										validar = false;
										System.out.println("carrusel tv");
										break;
									}
								} else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("BA")) {
									if (!UtilidadesTarificador.validarCarruselProductos(
											cotizacion.getTipoCotizacionBa(), cotizacion.getInternet(), this)) {
										validar = false;
										System.out.println("carrusel ba");
										break;
									}
								}
							}
						}
					} else if (cliente.getCodigoCarrusel().equals("01")) {
						System.out.println("Sin carrusel cliente.getCodigoCarrusel() " + cliente.getCodigoCarrusel());
						;
					}
				} else {
					Toast.makeText(this, getResources().getString(R.string.mensajeRetiros), Toast.LENGTH_SHORT).show();
					resultCotizador("carrusel");
				}
			} else {
				if (productosNuevos()) {
					if (cliente.getCodigoCarrusel() == null) {
						Toast.makeText(this, getResources().getString(R.string.mensajeRetiros), Toast.LENGTH_SHORT)
								.show();
						resultCotizador("carrusel");
					}
				}
			}
		}

		return validar;
	}

	public boolean validarCarteraUNE() {

		boolean valid = true;

		if (scooring.isValidarCartera()) {

			if (scooring.getIdCliente().equalsIgnoreCase(cliente.getCedula())) {

				if (Utilidades.excluir("habilitarBloqueoScooring", cliente.getCiudad())) {
					if (scooring.getAccion().equalsIgnoreCase("No Vender")) {

						valid = false;

					}
				}

			} else {
				Toast.makeText(this, "Documento del cliente vs Documento de Consulta en Estado Cuenta no coincide",
						Toast.LENGTH_SHORT).show();
				resultCotizador("estado cuenta");
				valid = false;
			}

		}

		return valid;
	}

	public boolean validarScooring() {

		boolean valid = true;

		System.out.println();
		if (!scooring.isValidarCartera()) {
			if (cliente.getScooringune() != null) {
				if (cliente.getScooringune().getDocumentoScooring().equalsIgnoreCase(cliente.getCedula())) {
					if (cliente.getScooringune().isValidarScooring()) {
						System.out.println("cliente.getScooringune().isPasaScooring() "
								+ cliente.getScooringune().isPasaScooring());
						if (!cliente.getScooringune().isPasaScooring()) {
							if (productosNuevos()) {
								System.out.println("cliente.getDomiciliacion() " + cliente.getDomiciliacion());
								System.out.println("cliente.getScooringune().getRazonScooring() "
										+ cliente.getScooringune().getRazonScooring());
								if (cliente.getDomiciliacion().equals("SI")) {
									if (Utilidades.excluirEstadosDomiciliacion()
											.contains(cliente.getScooringune().getRazonScooring())) {
										// cliente.getScooringune().setNoAplicaScooring(true);
										valid = true;
										cliente.setBloqueoDomiciliacion("1");
										System.out.println("isNoAplicaScooring() "
												+ cliente.getScooringune().isNoAplicaScooring());
									} else {
										valid = false;
										Utilidades.MensajesToast(
												"No se puede vender ya que el cliente no cumple con el scoring", this);
									}
								} else {
									valid = false;
									Utilidades.MensajesToast(
											"No se puede vender ya que el cliente no cumple con el scoring", this);
								}

							} else {
								cliente.setDomiciliacion("NO");
								cliente.getScooringune().setNoAplicaScooring(true);
								System.out.println(
										"isNoAplicaScooring() " + cliente.getScooringune().isNoAplicaScooring());
							}
						} else {
							if (!productosNuevos()) {
								cliente.setDomiciliacion("NO");
								cliente.getScooringune().setNoAplicaScooring(true);
								System.out.println(
										"isNoAplicaScooring() " + cliente.getScooringune().isNoAplicaScooring());

							}
						}
					} else {
						if (cliente.getScooringune().isPendienteRespuesta()) {
							if (!productosNuevos()) {
								cliente.setDomiciliacion("NO");
								cliente.getScooringune().setNoAplicaScooring(true);
							}
						}
					}
				} else {
					Toast.makeText(this, "Documento del cliente vs Documento de Consulta en Estado Cuenta no coincide",
							Toast.LENGTH_SHORT).show();
					resultCotizador("estado cuenta");
					valid = false;
				}
			}
		}

		return valid;
	}

	public boolean productosNuevos() {

		boolean nuevos = false;

		System.out.println("nuevos " + nuevos);

		if (!cotizacion.getTelefonia().equalsIgnoreCase(Utilidades.inicial_guion)
				&& !cotizacion.getTelefonia().equalsIgnoreCase("")) {

			System.out.println("cotizacion.getTipoCotizacionTo() " + cotizacion.getTipoCotizacionTo());
			if (cotizacion.getTipoCotizacionTo().equalsIgnoreCase("1")) {
				nuevos = true;
			}
		}

		if (!cotizacion.getInternet().equalsIgnoreCase(Utilidades.inicial_guion)
				&& !cotizacion.getInternet().equalsIgnoreCase("")) {

			System.out.println("cotizacion.getTipoCotizacionBa() " + cotizacion.getTipoCotizacionBa());

			if (cotizacion.getTipoCotizacionBa().equalsIgnoreCase("1")) {
				nuevos = true;
			}
		}

		if (!cotizacion.getTelevision().equalsIgnoreCase(Utilidades.inicial_guion)
				&& !cotizacion.getTelevision().equalsIgnoreCase("")) {

			System.out.println("cotizacion.getTipoCotizacionTv() " + cotizacion.getTipoCotizacionTv());

			if (cotizacion.getTipoCotizacionTv().equalsIgnoreCase("1")) {
				nuevos = true;
			}
		}

		// if (cotizacion.getAdicionales().length > 0) {
		// for (int i = 0; i < cotizacion.getAdicionales().length; i++) {
		// if (!Utilidades.excluirGeneral("adicionalesScooring",
		// cotizacion.getAdicionales()[i][0])) {
		// nuevos = true;
		// break;
		// }
		// }
		// }

		System.out.println("nuevos " + nuevos);

		return nuevos;
	}

	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub
		observador = o;
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub
		observador = null;
	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub
		observador.update("Tarificador");
	}

	@Override
	public void update(Object value) {
		try {
			ArrayList<Object> resultado = (ArrayList<Object>) value;
			System.out.println("resultado " + resultado);
			if (resultado.get(0).equals("validarPermiso")) {
				mostarComponenteDecos(resultado.get(1).toString());
			} else if (resultado.get(0).equals("decos")) {
				// if (resultado != null &&
				// resultado.get(0).equals("TipoHogar")) {
				// System.out.println("TipoHogar " + resultado.get(1));
				// // cliente.setPortafolio(resultado.get(1).toString());
				// validarSmartPromo(resultado.get(1).toString());
				//
				// }
			} else if (resultado != null && resultado.get(0).equals("TipoHogar")) {
				System.out.println("TipoHogar " + resultado.get(1));
				// cliente.setPortafolio(resultado.get(1).toString());
				cliente.setlogSmartPromoRes(resultado.get(1).toString());
				validarSmartPromo(resultado.get(1).toString());

			} else if (resultado != null && resultado.get(0).equals("Portafolio")) {
				System.out.println("Portafolio " + resultado.get(1));
				cliente.setPortafolio(resultado.get(1).toString());
			}

		} catch (Exception e) {
			Log.w("error update", e.getMessage());
		}

	}

	public void validarSmartPromo(String resultado) {
		System.out.println("validarSmartPromo");
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

		System.out.println("cliente.getSmartPromo Resultado " + cliente.getSmartPromo());
	}

	OnCheckedChangeListener aplicarAdicional = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			// System.out.println("evento Boton ");
			if (isChecked) {
				aplicarAd = true;
			} else {
				aplicarAd = false;
			}
		}
	};

	OnCheckedChangeListener aplicartvAnaloga = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			System.out.println("evento Boton " + aplicarAnaloga);
			if (isChecked) {
				aplicarAnaloga = "1";
			} else {
				aplicarAnaloga = "0";
			}
		}
	};

	public String Departamento() {
		String depto = "";
		/*
		 * System.out.println("departamento " +
		 * MainActivity.config.getDepartamento());
		 */
		if (MainActivity.config.getDepartamento().equalsIgnoreCase("Distrito Capital de Bogota")) {
			depto = cliente.getCiudad();
		} else {
			depto = MainActivity.config.getDepartamento();
		}

		return depto;
	}

	public void descuentosAdicionales(View v) {
		Intent intent = new Intent(MainActivity.MODULO_LISTAS);
		intent.putExtra("promocionesAdicionales", itemPromocionesAdicionales);
		startActivityForResult(intent, MainActivity.REQUEST_CODE);
	}

	private boolean procesarSegundaBA() {
		String mensaje = "";
		System.out.println("Utilidades.excluir(segundaBA, cliente.getCiudad()) => "
				+ Utilidades.excluir("segundaBA", cliente.getCiudad()));
		if (!Utilidades.excluir("excluirSegundaBA", cliente.getCiudad())) {
			if (cliente.getPortafolio() != null) {
				try {
					JSONObject portafolio = new JSONObject(cliente.getPortafolio());
					if (portafolio.has("INTER")) {
						JSONObject inter = null;
						if (portafolio.getString("INTER").startsWith("[")) {
							System.out.println("cliente.getDireccion() => " + cliente.getDireccion());
							if (!cliente.getDireccion().equals("")) {
								for (int i = 0; i < portafolio.getJSONArray("INTER").length(); i++) {
									inter = portafolio.getJSONArray("INTER").getJSONObject(i);
									if (cliente.getDireccion().equalsIgnoreCase(inter.getString("Direccion"))) {
										break;
									} else {
										inter = null;
									}
								}
							} else {
								mensaje = "Diligencia la direccion";
							}
						} else {
							inter = portafolio.getJSONObject("INTER");
							if (!cliente.getDireccion().equalsIgnoreCase(inter.getString("Direccion"))) {
								inter = null;
							}
						}

						if (inter != null) {
							if (inter.has("Datos_cliente")) {
								String[] datosCliente = inter.get("Datos_cliente").toString().split(" ");
								if (datosCliente[0].equalsIgnoreCase(cliente.getCedula())) {
									if (Utilidades.excluirGeneral("Excluir2BA",
											inter.get("Plan_Factura2").toString())) {
										if (inter.has("Tipo_Tecnologia")) {
											if (inter.get("Tipo_Tecnologia").equals("HFC")) {
												Toast.makeText(this, "Se puede Vender", Toast.LENGTH_LONG).show();
												return true;
											} else {
												Toast.makeText(this, "Tecnologia No soportada", Toast.LENGTH_LONG)
														.show();
												return false;
											}
										} else {
											mensaje = "No se puede validar el tipo de tecnologia";
										}
									} else {
										mensaje = "Producto de portafolio no soportado";
									}
								} else {
									mensaje = getResources().getString(R.string.clientenoduenoba);
								}

							} else {
								mensaje = "No se puede validar el cliente";
							}

						} else {
							mensaje = getResources().getString(R.string.nobaendireccion);
						}
					} else {
						mensaje = "El cliente no posee banda ancha";
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					mensaje = "Problemas procesando el portafolio del cliente";
				}
			} else {
				mensaje = "El cliente no posee el portafolio";
			}
		} else {
			return true;
		}

		if (!mensaje.equals("")) {
			Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}

	}

	public void validarSegundaTelefonia() {

		boolean segundaTO = false;

		if (cliente.getPortafolio() != null) {
			try {
				JSONObject portafolio = new JSONObject(cliente.getPortafolio());
				if (portafolio.has("TO")) {

					if (portafolio.get("TO").getClass().getSimpleName().equals("JSONArray")) {
						JSONArray array = portafolio.getJSONArray("TO");
						// Portafolio.add(new ListaDefault(0, "TO", "Titulo"));
						for (int i = 0; i < array.length(); i++) {
							if (array.getJSONObject(i).getString("Estado_Servicio").equalsIgnoreCase("ACTI")
									|| array.getJSONObject(i).getString("Estado_Servicio").equalsIgnoreCase("SXFP")) {
								segundaTO = true;
							}
						}

					} else if (portafolio.get("TO").getClass().getSimpleName().equals("JSONObject")) {

						JSONObject array = portafolio.getJSONObject("TO");

						if (array.getString("Estado_Servicio").equalsIgnoreCase("ACTI")) {
							segundaTO = true;
						}
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.w("Error", e.getMessage());
				// mensaje = "Problemas procesando el portafolio del cliente";
			}
		}

		cotizacion.setSegundaTelefonia(segundaTO);
	}

	public void validacionAdicionales() {

		datosValidacion.clear();
		int value;

		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
				new String[] { "lst_clave,lst_valor" }, "lst_nombre=?", new String[] { "validacionAdicionalesTV" },
				null, null, null);

		System.out.println("respuesta " + respuesta);

		if (respuesta != null) {
			for (int i = 0; i < respuesta.size(); i++) {

				String key = respuesta.get(i).get(0);
				try {
					value = Integer.parseInt(respuesta.get(i).get(1));
					datosValidacion.add(new ItemKeyValue2(key, value));
				} catch (NumberFormatException e) {
					value = 0;
				}

			}

		}

		System.out.println("datosValidacion " + datosValidacion);

		for (int i = 0; i < datosValidacion.size(); i++) {
			System.out.println("Key " + datosValidacion.get(i).getKey());
			System.out.println("Value " + datosValidacion.get(i).getValues());

		}
	}

	public void countAdicionales() {
		countValidacion.clear();

		countValidacion2.clear();

		for (int i = 0; i < itemPromocionesAdicionales.size(); i++) {
			System.out.println("itemPromocionesAdicionales.get(i).getAdicional() "
					+ itemPromocionesAdicionales.get(i).getAdicional());

			String tipoAdicional = tipoAdicional(itemPromocionesAdicionales.get(i).getAdicional());

			if (tipoAdicional != null) {
				// countValidacion.add(new ItemKeyValue2(tipoAdicional, 1));
				countValidacion2.add(tipoAdicional);
			}
		}

		if (countValidacion2.size() > 0) {
			validacionAdicionales();
		}

		// for (int i = 0; i < countValidacion.size(); i++) {
		// System.out.println("Key "+countValidacion.get(i).getKey());
		// System.out.println("Value "+countValidacion.get(i).getValues());
		//
		// }

		for (int i = 0; i < countValidacion2.size(); i++) {
			System.out.println("datos " + countValidacion2.get(i));
		}

		Set<String> quipu = new HashSet<String>(countValidacion2);
		for (String key : quipu) {
			// System.out.println(key + " : " +
			// Collections.frequency(countValidacion2, key));
			countValidacion.add(new ItemKeyValue2(key, Collections.frequency(countValidacion2, key)));
		}

		for (int i = 0; i < countValidacion.size(); i++) {
			System.out.println("Key " + countValidacion.get(i).getKey());
			System.out.println("Value " + countValidacion.get(i).getValues());
		}

		validarAdicionales();

	}

	public String tipoAdicional(String Adicional) {
		String tipoAdicional = null;
		ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
				new String[] { "lst_valor" }, "lst_nombre=? and lst_clave=?",
				new String[] { "tipoAdicionalTV", Adicional }, null, null, null);

		if (respuesta != null) {
			tipoAdicional = respuesta.get(0).get(0);
		}

		return tipoAdicional;

	}

	public void validarAdicionales() {

		// validar=true;

		System.out.println("validar " + validarAdicionales);

		if (countValidacion.size() > 0) {
			for (int i = 0; i < countValidacion.size(); i++) {
				for (int j = 0; j < datosValidacion.size(); j++) {
					System.out.println("countValidacion.get(i) " + countValidacion.get(i));
					System.out.println("datosValidacion.get(j) " + datosValidacion.get(j));
					if (countValidacion.get(i).getKey().equals(datosValidacion.get(j).getKey())) {
						if (countValidacion.get(i).getValues() <= datosValidacion.get(j).getValues()) {
							;
						} else {
							validarAdicionales = false;
						}
					}

				}

			}
		}

		System.out.println("validar " + validarAdicionales);

		// return validarAdicionales;

	}

	public String[][] agregarAdicionalesGratis(String planFactura, String planFacturaEmp, String[][] adicionales,
			String Television, String planTv, String cantidaProductos, boolean trioNuevo, String promoTv,
			String duracionTv) {

		String[][] adicionales2 = null;

		boolean miniHD = false;
		boolean expecialHD = false;

		int nProductos = 0;

		String precioAdicional = "";
		String promocion = "";
		String duracion = "";

		try {
			nProductos = Integer.parseInt(cantidaProductos);

		} catch (Exception e) {
			Log.w("Error", e.getMessage());
		}

		itemPromocionesAdicionalesGratis.clear();

		// otrosAdicionales=

		for (int i = 0; i < adicionales.length; i++) {
			System.out.println("adicionales " + adicionales[i][0] + " - precio " + adicionales[i][1]);
		}

		System.out.println("planFactura " + planFactura);
		String adicional = Utilidades.adicionalesGratis("adicionalesGratis", planFactura);

		System.out.println("Adiciolan gratis " + adicional);

		if (adicional != null) {
			if (countValidacion2.contains("HD")) {
				String cambiar = Utilidades.adicionalesGratis("cambiar", adicional);

				if (cambiar != null && cambiar.equalsIgnoreCase("SI")) {
					adicional = Utilidades.adicionalesGratis("cambiarAdicional", adicional);
				}
			}

			System.out.println("promoTv " + promoTv);

			if (promoTv.contains("%")) {
				String[] promo = promoTv.split("%");
				promoTv = promo[0];
			} else if (promoTv.equalsIgnoreCase("Sin Promocion")) {
				promoTv = "0";
			}

			promocion = promoTv;
			duracion = duracionTv;
		}

		for (int i = 0; i < adicionales.length; i++) {
			// System.out.println("adicionales " + adicionales[i][0]
			// + " - precio " + adicionales[i][1]);

			if (adicionales[i][0].equalsIgnoreCase("Mini HD Estratos 1-2-3-4")
					&& Utilidades.excluirNacional("Mini HD Estratos 1-2-3-4 - A", planFacturaEmp) && nProductos == 3) {
				if (trioNuevo) {
					itemPromocionesAdicionales
							.add(new ItemPromocionesAdicionales(adicionales[i][0], "100", "AD", "12 Meses"));
				}

				miniHD = true;
			}

			System.out.println("adicionales[i][0] " + adicionales[i][0]);
			System.out.println("planFacturaEmp " + planFacturaEmp);
			System.out.println("nProductos " + nProductos);

			if (adicionales[i][0].equalsIgnoreCase("HD Edicion Especial")
					&& Utilidades.excluirNacional("HD Edicion Especial - A", planFacturaEmp) && nProductos == 1) {
				itemPromocionesAdicionales
						.add(new ItemPromocionesAdicionales(adicionales[i][0], "100", "AD", "12 Meses"));
				expecialHD = true;
			}
		}

		System.out.println("miniHD " + miniHD);
		System.out.println("planFacturaEmp " + planFacturaEmp);
		System.out.println("nProductos " + nProductos);
		System.out.println("planTv " + planTv);

		if (Utilidades.excluirNacional("Mini HD Estratos 1-2-3-4 - B", planFacturaEmp) && nProductos == 3 && !miniHD) {
			// miniHD = false;
			if (!countValidacion2.contains("HD")) {
				if (trioNuevo) {
					adicional = "Mini HD Estratos 1-2-3-4";
					promocion = "100";
					duracion = "12 Meses";
				}
			}
		} else if (Utilidades.excluirNacional("HD Edicion Especial - B", planFacturaEmp) && nProductos == 1) {
			// expecialHD = false;
			if (!countValidacion2.contains("HD")) {
				if (planTv.equalsIgnoreCase("N")) {
					adicional = "HD Edicion Especial";
					promocion = "100";
					duracion = "12 Meses";
				}
			}
		}

		if (adicional != null) {
			precioAdicional = UtilidadesTarificador.precioAdicional(adicional, Departamento(),
					(String) sltEstrato.getSelectedItem());
		}

		System.out.println("adicional " + adicional);
		System.out.println("precio " + precioAdicional);

		if (adicionales.length > 0 && adicional != null) {

			int tamano = adicionales.length + 1;
			System.out.println("tamano " + tamano);

			adicionales2 = new String[tamano][2];

			for (int i = 0; i < tamano; i++) {

				if (i == tamano - 1) {
					adicionales2[i][0] = adicional;
					adicionales2[i][1] = precioAdicional;

					itemPromocionesAdicionales
							.add(new ItemPromocionesAdicionales(adicional, promocion, "AD", duracion));
				} else {
					adicionales2[i][0] = adicionales[i][0];
					adicionales2[i][1] = adicionales[i][1];
				}
			}

		} else if (adicional != null) {
			adicionales2 = new String[1][2];
			adicionales2[0][0] = adicional;
			adicionales2[0][1] = precioAdicional;
			// String[][] adicionales3 = {{adicional},{"0"}};
			// adicionales2 = adicionales3;
			itemPromocionesAdicionales.add(new ItemPromocionesAdicionales(adicional, promocion, "AD", duracion));
		} else {
			adicionales2 = adicionales;
		}

		System.out.println("countValidacion2 " + countValidacion2);

		System.out.println("adicionales2 " + adicionales2.length);

		for (int i = 0; i < adicionales2.length; i++) {
			System.out.println("adicionales " + adicionales2[i][0] + " - precio " + adicionales2[i][1]);
		}

		return adicionales2;

	}

	public void validarPortafolio(ArrayList<ItemTarificador> Resumen) {

		validarEstandarizacion = true;
		validarTelefonoServicio = true;

		// System.out.println("Resumen " + Resumen);
		// System.out.println("Resumen " + Resumen.get(0).getDato());
		String planTO = (String) sltPlanTo.getSelectedItem();
		// System.out.println("planTO " + planTO);

		if (planTO.equalsIgnoreCase("N") && !Resumen.get(0).getDato().equalsIgnoreCase(Utilidades.inicial_guion)) {
			if (Utilidades.visible("portafolioATC", cliente.getCiudad())
			/* && cliente.getPortafolio() == null */) {
				if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
					// System.out.println("cliente.isConsultaNormalizada() "
					// + cliente.isConsultaNormalizada());
					// System.out.println("cliente.isControlNormalizada() "
					// + cliente.isControlNormalizada());

					if (!cliente.isControlCerca()) {
						if (!cliente.isControlNormalizada()) {
							validarEstandarizacion = false;
							// ja.put(Utilidades.jsonMensajes("Direccion",
							// "Debe Normalizar la Direcci�n"));
							Toast.makeText(this, "Debe Normalizar la Dirección", Toast.LENGTH_SHORT).show();
						} else {
							if (cliente.getPortafolio() == null/*
																 * cliente.
																 * isConsultaNormalizada
																 * ()
																 */) {
								lanzarSimulador("direccion", "Portafolio");
							}
						}
					}

				}

				String telefono = Utilidades.limpiarTelefono(cliente.getTelefono());
				if (!telefono.equals("")) {
					System.out.println("trlefono " + telefono);
					if (telefono.length() != 7) {
						validarTelefonoServicio = false;
						Toast.makeText(this, "Telefono De Servicio, Debe Estar Compuesto Por 7 Digitos",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		} else if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
			if (!cliente.isControlNormalizada()) {
				validarEstandarizacion = false;
				Toast.makeText(this, getResources().getString(R.string.normailizardireccion), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public boolean validarAgendaSiebel() {
		boolean agenda = true;
		System.out.println("cliente.getEstandarizarSiebel()  " + cliente.getEstandarizarSiebel());
		if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
			if (cliente.getEstandarizarSiebel() == 1) {
				if (Utilidades.excluir("agendaSantander", cliente.getCiudad())) {
					if (!Validaciones.validarAgendaSiebel(cliente)) {
						agenda = false;
					}
				}
			} else if (cliente.getEstandarizarSiebel() == 2) {
				Toast.makeText(this, getResources().getString(R.string.estandarizardireccion), Toast.LENGTH_SHORT)
						.show();
				// agenda = false;
				if (!Validaciones.validarAgendaSiebel(cliente)) {
					agenda = false;
				} else {
					agenda = false;
				}
			}
		}
		return agenda;
	}

	public static boolean validarCliente() {
		boolean validacion = true;
		return validacion;
	}

	private void lanzarSimulador(String tipoDato, String tipo) {

		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);
		// simulador.execute(params);

		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(cliente.getTelefono());
		parametros.add(cliente.getDireccion());
		parametros.add(cliente.getCiudadFenix());
		parametros.add(cliente.getDireccion());

		if (tipo.equalsIgnoreCase("Portafolio")) {

			ArrayList<Object> params = new ArrayList<Object>();
			params.add(MainActivity.config.getCodigo());
			params.add("Portafolio");
			params.add(tipoDato);
			params.add(parametros);

			simulador.execute(params);

		}

	}

	private void lanzarTipoHogar() {

		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);
		// simulador.execute(params);

		JSONObject data = new JSONObject();

		cliente.setSmartPromo("");

		cliente.setSmartPromo("");

		boolean consultar = true;

		try {

			data.put("paginacion", cliente.getPaginacion());
			data.put("direccion", cliente.getDireccion());
			data.put("direccionNormalizada", cliente.getDireccionNormalizada());
			data.put("medioCotizador", "Tarificador");
			if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
				data.put("medio", "Siebel");
				if (cliente.getIdDireccionGis() != null && !cliente.getIdDireccionGis().equalsIgnoreCase("")) {
					data.put("IdDireccionGis", cliente.getIdDireccionGis());
				} else if (cliente.getIdDireccionGisEx() != null
						&& !cliente.getIdDireccionGisEx().equalsIgnoreCase("")) {
					data.put("IdDireccionGis", cliente.getIdDireccionGisEx());
				} else {
					consultar = false;
				}
			} else {
				data.put("medio", "ATC");
				if (cliente.getPaginacion().equalsIgnoreCase("")) {
					consultar = false;
				}
			}
		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}

		if (consultar) {
			ArrayList<String> parametros = new ArrayList<String>();
			parametros.add(data.toString());
			cliente.setlogSmartPromoEnv(data.toString());

			ArrayList<Object> params = new ArrayList<Object>();
			params.add(MainActivity.config.getCodigo());
			params.add("TipoHogar");
			params.add(parametros);

			simulador.execute(params);
		}

	}

	private String getTipoTecnologiaExistente(String producto) {

		String respuesta = "";

		if (!Utilidades.excluir("tecnologiaProducto", cliente.getCiudad())) {
			if (cliente.getPortafolio() != null) {
				if (!cliente.getPortafolio().equalsIgnoreCase("")) {
					if (producto.equalsIgnoreCase("TO")) {
						try {
							JSONObject jo = new JSONObject(cliente.getPortafolio());
							if (jo.has("TO")) {
								JSONObject joto = jo.getJSONObject("TO");
								if (joto.has("Tipo_Tecnologia")) {
									respuesta = joto.getString("Tipo_Tecnologia");
								} else {
									respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
								}
							} else {
								respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
						}
					} else if (producto.equalsIgnoreCase("TV")) {
						try {
							JSONObject jo = new JSONObject(cliente.getPortafolio());
							if (jo.has("TELEV")) {
								JSONObject jotv = jo.getJSONObject("TELEV");
								if (jotv.has("Tipo_Tecnologia")) {
									respuesta = jotv.getString("Tipo_Tecnologia");
								} else {
									respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
								}
							} else {
								respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
						}
					} else if (producto.equalsIgnoreCase("BA")) {
						try {
							JSONObject jo = new JSONObject(cliente.getPortafolio());
							if (jo.has("INTER")) {
								JSONObject joba = jo.getJSONObject("INTER");
								if (joba.has("Tipo_Tecnologia")) {
									respuesta = joba.getString("Tipo_Tecnologia");
								} else {
									respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
								}
							} else {
								respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
						}
					}
				} else {
					respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
				}
			} else {
				respuesta = Utilidades.obtenerCobertura(cliente.getCobertura(), this);
			}

		} else {
			respuesta = "N/A";
		}

		return respuesta;

	}

	public String totalDescuentos(String precioTo, String descuentoTo, String precioTv, String descuentoTv,
			String precioBa, String descuentoBa, String total, double descuentoAd) {

		double totalDescuento = Utilidades.convertirNumericos(total, "total");

		if (descuentoTo.contains("%")) {

			ArrayList<Object> listDescuentos = Utilidades.precioDescuento(descuentoTo, precioTo);

			if ((Boolean) listDescuentos.get(0)) {

				if (!listDescuentos.get(1).equals(0.0) && !listDescuentos.get(1).equals(0)) {

					double resta = Utilidades.convertirDouble(listDescuentos.get(1).toString(),
							"listDescuentos.get(1)");

					System.out.println("resta " + resta);

					System.out.println("totalDescuento " + totalDescuento);

					totalDescuento = totalDescuento - resta;

					System.out.println("totalDescuento " + totalDescuento);
				}
			}

		}

		if (descuentoBa.contains("%")) {

			ArrayList<Object> listDescuentos = Utilidades.precioDescuento(descuentoBa, precioBa);

			if ((Boolean) listDescuentos.get(0)) {

				if (!listDescuentos.get(1).equals(0.0) && !listDescuentos.get(1).equals(0)) {

					double resta = Utilidades.convertirDouble(listDescuentos.get(1).toString(),
							"listDescuentos.get(1)");

					System.out.println("resta " + resta);

					System.out.println("totalDescuento " + totalDescuento);

					totalDescuento = totalDescuento - resta;

					System.out.println("totalDescuento " + totalDescuento);
				}
			}

		}

		if (descuentoTv.contains("%")) {

			ArrayList<Object> listDescuentos = Utilidades.precioDescuento(descuentoTv, precioTv);

			if ((Boolean) listDescuentos.get(0)) {

				if (!listDescuentos.get(1).equals(0.0) && !listDescuentos.get(1).equals(0)) {
					double resta = Utilidades.convertirDouble(listDescuentos.get(1).toString(),
							"listDescuentos.get(1)");

					System.out.println("resta " + resta);

					System.out.println("totalDescuento " + totalDescuento);

					totalDescuento = totalDescuento - resta;

					System.out.println("totalDescuento " + totalDescuento);
				}
			}

		}

		System.out.println("totalDescuento " + totalDescuento);

		if (descuentoAd != 0 && descuentoAd != 00) {
			totalDescuento = totalDescuento - descuentoAd;
		}

		System.out.println("totalDescuento " + totalDescuento);

		return "" + totalDescuento;
	}

	public void preciosAdDescuento(String[][] adicionales, String totalAdicionales,
			ArrayList<ItemPromocionesAdicionales> promocioneAd) {

		double precioDescuentoAD = 0;
		controlDescuentosAd = false;
		preciosConDescuentoAd = 0.0;
		precioDescuentosAd = 0.0;

		for (int i = 0; i < adicionales.length; i++) {
			System.out.println("preciosAdDescuento nombre " + adicionales[i][0]);
			System.out.println("preciosAdDescuento precio " + adicionales[i][1]);

			double precioAd = Utilidades.convertirDouble(adicionales[i][1], "adicionales[" + i + "][1]");

			for (int j = 0; j < cotizacion.getItemPromocionesAdicionales().size(); j++) {

				if (adicionales[i][0]
						.equalsIgnoreCase(cotizacion.getItemPromocionesAdicionales().get(j).getAdicional())) {

					System.out.println("preciosAdDescuento precioAd " + precioAd);

					if (!cotizacion.getItemPromocionesAdicionales().get(j).getDescuento().equals("0")) {

						int descuento = Utilidades.convertirNumericos(
								cotizacion.getItemPromocionesAdicionales().get(j).getDescuento(),
								"cotizacion.getItemPromocionesAdicionales().get(" + i + ").getDescuento()");

						precioDescuentosAd += (precioAd * descuento) / 100;

						precioAd = precioAd - (precioAd * descuento) / 100;

						System.out.println("preciosAdDescuento precioAd " + precioAd + " "
								+ cotizacion.getItemPromocionesAdicionales().get(j).getAdicional());
						controlDescuentosAd = true;
					}

					System.out.println("preciosAdDescuento precioAd " + precioAd + " "
							+ cotizacion.getItemPromocionesAdicionales().get(j).getAdicional());
				}

				System.out.println("preciosAdDescuento precioAd " + precioAd);

			}

			precioDescuentoAD += precioAd;

			System.out.println("preciosAdDescuento precioDescuentoAD " + precioDescuentoAD);

		}

		System.out.println("precioDescuentoAD Antes" + precioDescuentoAD);

		if (precioDescuentoAD != 0 && precioDescuentoAD != 0.0) {
			preciosConDescuentoAd = precioDescuentoAD;
		}
	}

	private void mostarComponenteDecos(String respuesta) {
		System.out.println("validarPermiso => " + respuesta);
		try {
			JSONObject json = new JSONObject(respuesta);
			if (json.getString("code").equals("00")) {
				if (MainActivity.config.validarIntegridad(json.getString("data"), json.get("crc").toString())) {
					String data = new String(Base64.decode(json.getString("data")));
					// Confirmacion(data);
					JSONArray permisos = new JSONArray(data);

					if (permisos.length() > 0) {
						cmpDecos.setPermisoFideliza(true);

					} else {
						cmpDecos.setPermisoFideliza(false);
					}

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String validarTvAnaloga() {
		System.out.println("aplicarAnaloga antes " + aplicarAnaloga);

		if (aplicarAnaloga.equals("0") || aplicarAnaloga.equals("1")) {
			if (((String) sltTv.getSelectedItem()).equals(Utilidades.inicial)
					|| ((String) sltTv.getSelectedItem()).equals(Utilidades.inicial)) {
				aplicarAnaloga = "N/A";
			}
		}

		System.out.println("aplicarAnaloga despues " + aplicarAnaloga);

		return aplicarAnaloga;
	}
}
