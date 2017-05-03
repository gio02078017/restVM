package co.com.une.appmovilesune.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.adapters.ListaDefaultAdapter;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.complements.Pdf;
import co.com.une.appmovilesune.complements.Validaciones;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Asesoria;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Competencia;
import co.com.une.appmovilesune.model.Configuracion;
import co.com.une.appmovilesune.model.Prospecto;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.Venta;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

public class ControlResumen extends Activity implements Observer {

    private TituloPrincipal tp;
    private String lugar = "Cliente";
    private Asesoria asesoria;

    private TabHost.TabSpec spec;

    private ListView lsvCliente, lsvCompetencia, lsvPeticion, lsvObsequio, lsvVenta, lsvProspecto, lsvBlindaje,
            lsvDocumentacion;
    private LinearLayout linearVenta, linearDocumentacion;

    ArrayList<ListaDefault> listVenta = new ArrayList<ListaDefault>();
    ArrayList<ListaDefault> listDocumentacion = new ArrayList<ListaDefault>();

    TabHost tabs;

    private Observer observador;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewresumen);

        lsvCliente = (ListView) findViewById(R.id.lsvCliente);
        lsvCompetencia = (ListView) findViewById(R.id.lsvCompetencia);
        lsvPeticion = (ListView) findViewById(R.id.lsvPeticion);
        lsvObsequio = (ListView) findViewById(R.id.lsvObsequio);
        lsvVenta = (ListView) findViewById(R.id.lsvVenta);
        lsvDocumentacion = (ListView) findViewById(R.id.lsvDocumentacion);
        lsvProspecto = (ListView) findViewById(R.id.lsvProspecto);
        lsvBlindaje = (ListView) findViewById(R.id.lsvBlindaje);
        linearVenta = (LinearLayout) findViewById(R.id.linearVenta);
        linearDocumentacion = (LinearLayout) findViewById(R.id.linearDocumentacion);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("Resumen Cliente");

        Resources res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            asesoria = (Asesoria) reicieveParams.getSerializable("asesoria");
            if (asesoria.cliente != null) {
                spec = tabs.newTabSpec("Cliente");
                spec.setContent(R.id.rsnCliente);
                spec.setIndicator("Cliente", res.getDrawable(R.drawable.cliente));
                tabs.addTab(spec);
                llenarCliente();
            } else {
                // System.out.println("No Cliente");
            }
            if (asesoria.competencia != null) {
                spec = tabs.newTabSpec("Gerencia De Ruta");
                spec.setContent(R.id.rsnCompetencia);
                spec.setIndicator("Gerencia De Ruta", res.getDrawable(R.drawable.asesoria));
                tabs.addTab(spec);
                llenarCompetencia();
            } else {
                // System.out.println("No Competencia");
            }
            if (asesoria.peticion != null) {
                spec = tabs.newTabSpec("Peticion");
                spec.setContent(R.id.rsnPeticion);
                spec.setIndicator("Peticion", res.getDrawable(R.drawable.peticion));
                tabs.addTab(spec);
                llenarPeticion();
            } else {
                // System.out.println("No Peticion");
            }
            if (asesoria.obsequio != null) {
                spec = tabs.newTabSpec("Obsequio");
                spec.setContent(R.id.rsnObsequio);
                spec.setIndicator("Obsequio", res.getDrawable(R.drawable.obsequio));
                tabs.addTab(spec);
            } else {
                // System.out.println("No Obsequio");
            }
            if (asesoria.venta != null && asesoria.prospecto == null) {
                spec = tabs.newTabSpec("Venta");
                spec.setContent(R.id.rsnVenta);
                spec.setIndicator("Venta", res.getDrawable(R.drawable.tarificador));
                tabs.addTab(spec);
                // System.out.println();
                String[] Internet = asesoria.venta.getInternet();
                for (int i = 0; i < Internet.length; i++) {
                    System.out.println("Internet " + Internet[i]);
                }
                onpintar(asesoria.venta);
            } else {

            }

            if (asesoria.prospecto != null) {
                spec = tabs.newTabSpec("Prospecto");
                spec.setContent(R.id.rsnProspecto);
                spec.setIndicator("Prospecto", res.getDrawable(R.drawable.icon_prospecto));
                tabs.addTab(spec);

                llenarProspecto();
            } else {

            }

            if (asesoria.blindaje != null) {
                spec = tabs.newTabSpec("Blindaje");
                spec.setContent(R.id.rsnBlindaje);
                spec.setIndicator("Blindaje", res.getDrawable(R.drawable.blindaje));
                tabs.addTab(spec);

                llenarBlindaje();
            } else {

            }

            tabs.setCurrentTab(0);
            tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
                    .setBackgroundColor(getResources().getColor(R.color.yellow));
            TextView tv = (TextView) tabs.getCurrentTabView().findViewById(android.R.id.title); // for
            // Selected
            // Tab
            tv.setTextColor(getResources().getColor(R.color.darkTab));

            tabs.setOnTabChangedListener(new OnTabChangeListener() {
                public void onTabChanged(String tabId) {
                    tp.setTitulo("Resumen " + tabId);
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
                    lugar = tabId;
                }
            });
        } else {
            // System.out.println("no asesoria");
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
	protected void onResume() {
		super.onResume();
		if(asesoria.cliente.getDomiciliacion().equalsIgnoreCase("SI")){
			validarDebitoAutomaticoExistente();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (MainActivity.seguimiento) {
			EasyTracker.getInstance(this).activityStop(this);
		}
	}

    public void llenarCliente() {

        ArrayList<ListaDefault> contenido = new ArrayList<ListaDefault>();
        contenido.add(new ListaDefault(1, "Nombre", asesoria.cliente.getNombre()));
        contenido.add(new ListaDefault(2, "Tipo Documento", asesoria.cliente.getTipoDocumento()));
        contenido.add(new ListaDefault(1, "Documento", asesoria.cliente.getCedula()));
        contenido.add(
                new ListaDefault(2, getResources().getString(R.string.fecha_exped), asesoria.cliente.getExpedicion()));
        contenido.add(new ListaDefault(1, getResources().getString(R.string.lugar_exped),
                asesoria.cliente.getLugarExpedicion()));
        contenido.add(new ListaDefault(2, "Fecha Nacimiento", asesoria.cliente.getFechaNacimiento()));
        contenido.add(
                new ListaDefault(1, getResources().getString(R.string.direccion), asesoria.cliente.getDireccion()));
        contenido.add(
                new ListaDefault(2, getResources().getString(R.string.paginacion), asesoria.cliente.getPaginacion()));
        contenido.add(new ListaDefault(1, "Barrio", asesoria.cliente.getBarrio()));
        contenido.add(new ListaDefault(2, getResources().getString(R.string.telefono_servicio),
                asesoria.cliente.getTelefono()));
        contenido.add(new ListaDefault(1, getResources().getString(R.string.telefono_second),
                asesoria.cliente.getTelefono2()));
        contenido.add(new ListaDefault(2, "Celular", asesoria.cliente.getCelular()));
        contenido.add(new ListaDefault(1, getResources().getString(R.string.email), asesoria.cliente.getCorreo()));

        contenido.add(new ListaDefault(2, "Login", asesoria.cliente.getLogin()));
        contenido.add(new ListaDefault(2, "Pin Hot Pack", asesoria.cliente.getPinhp()));
        if (Utilidades.excluirMunicipal("habilitarCampos", "domiciliacion", asesoria.cliente.getCiudad())) {
            contenido.add(new ListaDefault(1, getResources().getString(R.string.domiciliacion),
                    asesoria.cliente.getDomiciliacion()));
        }
        contenido.add(new ListaDefault(1, getResources().getString(R.string.observaciones),
                asesoria.cliente.getObservacion()));

        contenido.add(new ListaDefault(2, "Nombre Contacto",
                asesoria.cliente.getContacto1().getNombres() + " " + asesoria.cliente.getContacto1().getApellidos()));
        contenido.add(new ListaDefault(1, getResources().getString(R.string.telefono_contacto),
                asesoria.cliente.getContacto1().getTelefono()));
        contenido.add(new ListaDefault(2, "Celular Contacto", asesoria.cliente.getContacto1().getCelular()));
        contenido.add(new ListaDefault(1, "Parentesco Contacto", asesoria.cliente.getContacto1().getParentesco()));
        contenido.add(new ListaDefault(2, "Nombre Contacto 2",
                asesoria.cliente.getContacto2().getNombres() + " " + asesoria.cliente.getContacto2().getApellidos()));
        contenido.add(new ListaDefault(1, getResources().getString(R.string.telefono_contacto) + " 2",
                asesoria.cliente.getContacto2().getTelefono()));
        contenido.add(new ListaDefault(2, "Celular Contacto 2", asesoria.cliente.getContacto2().getCelular()));
        contenido.add(new ListaDefault(1, "Parentesco Contacto 2", asesoria.cliente.getContacto2().getParentesco()));

        contenido.add(new ListaDefault(2, "Nivel Estudios", asesoria.cliente.getNivelEstudio()));
        contenido.add(new ListaDefault(1, getResources().getString(R.string.sec_profesion_string),
                asesoria.cliente.getProfesion()));
        contenido.add(new ListaDefault(2, getResources().getString(R.string.sec_ocupacion_string),
                asesoria.cliente.getOcupacion()));
        contenido.add(new ListaDefault(1, "Cargo", asesoria.cliente.getCargo()));
        contenido.add(new ListaDefault(2, "Nivel Ingresos", asesoria.cliente.getNivelIngresos()));
        contenido.add(new ListaDefault(1, "Estado Civil", asesoria.cliente.getEstadoCivil()));
        contenido.add(new ListaDefault(2, "Genero", asesoria.cliente.getGenero()));
        contenido.add(new ListaDefault(1, "Personas Cargo", asesoria.cliente.getPersonasCargo()));
        contenido.add(new ListaDefault(2, "Tipo Vivienda", asesoria.cliente.getTipoVivienda()));
        contenido.add(new ListaDefault(1, "Tipo Predio", asesoria.cliente.getTipoPredio()));
        contenido.add(new ListaDefault(2, "Nombre Predio", asesoria.cliente.getNombrePredio()));

        contenido.add(new ListaDefault(1, getResources().getString(R.string.fac_departamento_string),
                asesoria.cliente.getFacturacion().getDepartamento()));
        contenido.add(new ListaDefault(2, getResources().getString(R.string.fac_municipio_string),
                asesoria.cliente.getFacturacion().getMunicipio()));
        contenido.add(new ListaDefault(1, getResources().getString(R.string.fac_direccion_string),
                asesoria.cliente.getFacturacion().getDireccion()));
        contenido.add(new ListaDefault(2, getResources().getString(R.string.fac_direccion_string),
                asesoria.cliente.getFacturacion().getBarrio()));

        ListaDefaultAdapter adaptador = new ListaDefaultAdapter(this, contenido, this);
        lsvCliente.setAdapter(adaptador);

    }

    public void llenarCompetencia() {

        ArrayList<ListaDefault> contenido = new ArrayList<ListaDefault>();
        /*
		 * contenido.add(new ListaDefault(1, "Competencia", asesoria.competencia
		 * .getCompetencia())); contenido.add(new ListaDefault(2,
		 * "Otra Competencia", asesoria.competencia.getOtrasCompetencias()));
		 * contenido.add(new ListaDefault(1, "Productos", asesoria.competencia
		 * .getProductos()));
		 */
        contenido.add(new ListaDefault(2, "Empaquetado", asesoria.competencia.getEmpaquetado()));
		/*
		 * contenido.add(new ListaDefault(1, "Vigencia del Contrato",
		 * asesoria.competencia.getVigenciaContrato())); contenido.add(new
		 * ListaDefault(2, "Pago Mensual", asesoria.competencia
		 * .getPagoMensual()));
		 */
        contenido.add(new ListaDefault(1, "Motivo No Compra Con Une", asesoria.competencia.getNoUne()));
        contenido.add(new ListaDefault(2, "Observaciones", asesoria.competencia.getObservaciones()));

        ListaDefaultAdapter adaptador = new ListaDefaultAdapter(this, contenido, this);
        lsvCompetencia.setAdapter(adaptador);

    }

    public void llenarPeticion() {

        ArrayList<ListaDefault> contenido = new ArrayList<ListaDefault>();
        contenido.add(new ListaDefault(1, "Productos", asesoria.peticion.getProductos()));
        contenido.add(new ListaDefault(2, getResources().getString(R.string.transaccion),
                asesoria.peticion.getTransaccion()));
        contenido.add(
                new ListaDefault(1, getResources().getString(R.string.otra_transaccion), asesoria.peticion.getCual()));
        contenido.add(new ListaDefault(2, "Evento", asesoria.peticion.getEvento()));
        contenido.add(new ListaDefault(1, "Pedido", asesoria.peticion.getPedido()));
        contenido.add(new ListaDefault(2, "Cun", asesoria.peticion.getCun()));

        ListaDefaultAdapter adaptador = new ListaDefaultAdapter(this, contenido, this);
        lsvPeticion.setAdapter(adaptador);

    }

    public void llenarProspecto() {

        ArrayList<ListaDefault> contenido = new ArrayList<ListaDefault>();
        contenido.add(new ListaDefault(1, "Distancia", asesoria.prospecto.getDistancia()));
        contenido.add(new ListaDefault(2, "Productos", asesoria.prospecto.getProductos()));
        contenido.add(new ListaDefault(1, "Motivo", asesoria.prospecto.getMotivo()));
        contenido.add(new ListaDefault(2, "Observaciones", asesoria.prospecto.getObservaciones()));

        ListaDefaultAdapter adaptador = new ListaDefaultAdapter(this, contenido, this);
        lsvProspecto.setAdapter(adaptador);

    }

    public void llenarBlindaje() {

        ArrayList<ListaDefault> contenido = new ArrayList<ListaDefault>();

        System.out.println(asesoria.blindaje);
        if (asesoria.blindaje.resumen != null) {
            contenido.add(new ListaDefault(1, "Regalo Inicial", asesoria.blindaje.resumen[0]));

            contenido.add(new ListaDefault(1, "Oferta 1", asesoria.blindaje.resumen[1]));
            contenido.add(new ListaDefault(2, "Regalo Oferta 1", asesoria.blindaje.resumen[2]));
            contenido.add(new ListaDefault(2, "Precio Oferta", asesoria.blindaje.resumen[3]));
            contenido.add(new ListaDefault(2, "Valor Factura", asesoria.blindaje.resumen[4]));

            contenido.add(new ListaDefault(1, "Oferta 2", asesoria.blindaje.resumen[5]));
            contenido.add(new ListaDefault(2, "Regalo Oferta 2", asesoria.blindaje.resumen[6]));
            contenido.add(new ListaDefault(2, "Precio Oferta", asesoria.blindaje.resumen[7]));
            contenido.add(new ListaDefault(2, "Valor Factura", asesoria.blindaje.resumen[8]));

            ListaDefaultAdapter adaptador = new ListaDefaultAdapter(this, contenido, this);
            lsvBlindaje.setAdapter(adaptador);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == MainActivity.REQUEST_CODE) {
                if (lugar.equals("Cliente")) {
                    asesoria.cliente = (Cliente) data.getSerializableExtra("Cliente");
                    llenarCliente();
                } else if (lugar.equals("Competencia")) {
                    asesoria.competencia = (Competencia) data.getSerializableExtra("competencia");
                    llenarCompetencia();
                } else if (lugar.equals("Prospecto")) {
                    asesoria.prospecto = (Prospecto) data.getSerializableExtra("prospecto");
                    llenarProspecto();
                }
            }
        } catch (Exception e) {
            Log.w("Error", "Mensaje " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.resumen, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Enviar")) {

            String consolidar = asesoria.consolidarAsesoria();

            System.out.println("Consolidado de la asesoria => " + consolidar);

            if (Validaciones.validarDatosVenta(consolidar)) {

                if (MainActivity.debug) {
                    try {
                        File ruta_sd = Environment.getExternalStorageDirectory();

                        System.out.println(ruta_sd.getAbsolutePath());

                        File f = new File(ruta_sd.getAbsolutePath(), "/prueba_sd.txt");

                        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));

                        fout.write(consolidar);
                        fout.close();
                    } catch (Exception ex) {
                        Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                    }

                }

                try {
                    byte ptext[] = consolidar.getBytes();
                    String consolidarEnv = new String(ptext, "UTF-8");

                    ArrayList<String[]> parametros = new ArrayList<String[]>();
                    parametros.add(new String[]{"Parametros", consolidarEnv});

                    ArrayList<Object> params = new ArrayList<Object>();
                    params.add("Consolidar");
                    params.add(parametros);

                    MainActivity.crearConexion();

                    MainActivity.conexion.setManual(this);
                    MainActivity.conexion.addObserver(this);
                    MainActivity.conexion.execute(params);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Log.w("Error", "Inconvenientes " + "Generando Asesoria");
                Utilidades.MensajesToast("Inconvenientes " + "Generando Asesoria Intente Nuevamente", this);
            }

        }
        return true;
    }

    public void onpintar(Venta venta) {

        pintarDocumentacion();
        listVenta.clear();
        listVenta = Utilidades.getVenta(venta);
        ListaDefaultAdapter adaptador = new ListaDefaultAdapter(this, listVenta, this);

        lsvVenta.setAdapter(adaptador);
        lsvVenta.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (listVenta.get(arg2).getTitulo().equalsIgnoreCase(getResources().getString(R.string.documentacion))
                        && listDocumentacion.size() > 0) {
                    MostrarDocumentacion();
                }
            }
        });
    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub
        boolean valida = true;

        if (asesoria.venta != null) {
            // System.out.println("Tiene Venta");
        } else {
            // System.out.println("Sin venta");
        }

        String venMensaje = "";
        String prosMensaje = "";
        String CompMensaje = "";
        String BlinMensaje = "";
        String CUN = "";

        ArrayList<Object> resultado = (ArrayList<Object>) value;
        String res = resultado.get(1).toString();
        System.out.println("Respuesta cr 478 => " + res);
        try {
            JSONObject jop = new JSONObject(res);
            String data = jop.get("data").toString();
            if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                System.out.println("data => " + data);
                JSONObject respuesta = new JSONObject(data);

				if(resultado != null && resultado.get(0).equals("validarDebitoAutomaticoExistente")){
					tratarValidacionDebitoAutomatico(resultado.get(1).toString());
				}else {
					if (!respuesta.isNull("error")) {
						JSONObject error = new JSONObject(respuesta.get("error").toString());
						JSONObject rollback = new JSONObject(respuesta.get("rollback").toString());
						// System.out.println("Error en el servidor");
						Toast.makeText(getApplicationContext(), "Se ha producido un error en el servidor",
								Toast.LENGTH_SHORT).show();
					}
					if (!respuesta.isNull("asesoria")) {
						JSONObject venta = new JSONObject(respuesta.get("venta").toString());

						JSONObject competencia = new JSONObject(respuesta.get("competencia").toString());
						JSONObject asesoria = new JSONObject(respuesta.get("asesoria").toString());
						JSONObject prospecto = new JSONObject(respuesta.get("prospecto").toString());

					/*
					 * JSONObject resBlindaje = new JSONObject(respuesta
					 * .get("resBlindaje").toString());
					 */

					/*
					 * System.out.println("Control Resumen 360 => " +
					 * venta.get("mensaje") + venta.getString("identificador"));
					 */

						if (venta.get("mensaje").equals("Venta Agregada")) {
							venMensaje = "Su codigo de Venta es: " + venta.getString("identificador");
							MainActivity.basedatos.eliminar("ventas", "id_asesoria = ?",
									new String[] { this.asesoria.getId() });
						} else if (venta.get("mensaje").equals("La venta Ya existe")) {
							venMensaje = "La venta Ya existe";
						} else if (venta.get("mensaje").equals("No venta")) {
							valida = true;
						} else {
							valida = false;
						}

						// System.out.println(respuesta);

						if (MainActivity.seguimiento) {
							GoogleAnalytics mga = GoogleAnalytics.getInstance(this);
							Tracker mtraker = mga.getTracker("UA-44799901-1");
							mga.getLogger().setLogLevel(LogLevel.VERBOSE);
							mtraker.set("Respuesta", respuesta.toString());
							mtraker.send(MapBuilder.createEvent("SEND_ASESORIA", MainActivity.config.getCodigo(),
									respuesta.toString(), null).build());
						}

						if (competencia.get("mensaje").equals("Gestion Agregada")
								|| competencia.get("mensaje").equals("Gestion Existente")) {
							MainActivity.basedatos.eliminar("competencias", "id_asesoria = ?",
									new String[] { this.asesoria.getId() });
							CompMensaje = competencia.get("mensaje") + " id " + competencia.get("identificador");
						} else if (competencia.getString("mensaje").contains("Gestion Agregada")) {
							MainActivity.basedatos.eliminar("competencias", "id_asesoria = ?",
									new String[] { this.asesoria.getId() });
							CompMensaje = competencia.get("mensaje") + " id " + competencia.get("identificador");
						} else if (competencia.get("mensaje").equals("No Competencia")) {
							valida = true;

						} else {
							valida = false;
						}

						if (prospecto.get("respuesta").equals("1")) {
							MainActivity.basedatos.eliminar("prospecto", "id_asesoria = ?",
									new String[] { this.asesoria.getId() });
							// prosMensaje =
							// "Su codigo de Venta es:
							// "+venta.getString("identificador");
							prosMensaje = prospecto.get("mensaje") + " id " + prospecto.get("identificador");
						} else if (prospecto.get("respuesta").equals("0")) {
							prosMensaje = (String) prospecto.get("mensaje");
						/*
						 * System.out.println("Devuelve 0  Prospecto=>" +
						 * prospecto.get("mensaje"));
						 */
							// if(prospecto.get("mensaje").)
							valida = true;
						} else {
							valida = false;
						}

						if (respuesta.has("blindaje")) {
							JSONObject blindaje = new JSONObject(respuesta.get("blindaje").toString());
							System.out.println(blindaje.get("mensaje"));
							if (blindaje.get("mensaje").equals("Blindaje Agregado")) {

								String cun = blindaje.getString("cun");

							/*
							 * if(respuesta.has("resBlindaje")){ JSONObject
							 * resBlindaje = new JSONObject(respuesta.get(
							 * "resBlindaje").toString());
							 * if(resBlindaje.has("Blindaje")){ JSONObject
							 * resBlind = new JSONObject(resBlindaje.get(
							 * "Blindaje").toString()); if(resBlind.has("CUN")){
							 * JSONObject jscun = new JSONObject(resBlind.get(
							 * "CUN").toString()); cun =
							 * jscun.getString("IdCun"); }
							 *
							 * } }
							 */

								System.out.println("Cun " + cun);

								if (cun.equals("0")) {
									BlinMensaje = blindaje.get("mensaje") + " id " + blindaje.get("identificador")
											+ ", id amigo cuentas " + blindaje.get("idSimulador");
								} else {
									BlinMensaje = blindaje.get("mensaje") + " id " + blindaje.get("identificador")
											+ ", id amigo cuentas " + blindaje.get("amgID") + ", CUN " + cun;
								}

								valida = true;
							} else if (blindaje.get("mensaje").equals("No Blindaje")) {
								valida = true;
							} else {
								valida = false;
							}
						} /*
						 * else if (respuesta.has("resBlindaje")) {
						 *
						 * JSONObject resBlindaje = new
						 * JSONObject(respuesta.get( "resBlindaje").toString());
						 * System.out.println("Tiene res");
						 *
						 * if (resBlindaje.getString("codigoMensaje")
						 * .equalsIgnoreCase("00")) { System.out.println(
						 * "Es igual a 00"); if (resBlindaje.has("Blindaje")) {
						 *
						 * JSONObject Blindaje_Sim = new JSONObject(
						 * resBlindaje.getString("Blindaje") .toString());
						 *
						 * if (Blindaje_Sim.has("codigoMensaje")) { System.out
						 * .println("tinee blindaje dentro de res " +
						 * Blindaje_Sim .getString("codigoMensaje")); if
						 * (!Blindaje_Sim .getString("codigoMensaje").equals(
						 * "00")) { Intent intent = new Intent();
						 *
						 * if (Blindaje_Sim.getString(
						 * "codigoMensaje").equals("02") ||
						 * Blindaje_Sim.getString( "codigoMensaje")
						 * .equals("01")) { BlinMensaje = "Blindaje " +
						 * Blindaje_Sim .getString("resumen") +
						 * " id amigo cuentas " + Blindaje_Sim .getString("id");
						 * intent.putExtra("Borrar", "SI");
						 *
						 * } else if (Blindaje_Sim.getString(
						 * "codigoMensaje").equals("03")) { BlinMensaje =
						 * Blindaje_Sim .getString("descripcionMensaje");
						 * intent.putExtra("Borrar", "SI"); } else if
						 * (Blindaje_Sim.getString(
						 * "codigoMensaje").equals("04")) { BlinMensaje =
						 * Blindaje_Sim .getString("descripcionMensaje");
						 * intent.putExtra("Borrar", "NO"); }
						 *
						 * intent.putExtra("Mensaje", BlinMensaje);
						 *
						 * setResult(MainActivity.OK_RESULT_CODE, intent);
						 *
						 * valida = true; finish(); } else { valida = true; } }
						 *
						 * } }else { BlinMensaje = "Blindaje " + resBlindaje
						 * .getString("descripcionMensaje"); valida = false; }
						 *
						 * }
						 */

						if (respuesta.has("CreacionCUN")) {
							JSONObject resCUN = new JSONObject(respuesta.get("CreacionCUN").toString());
							if (resCUN.getString("CodigoMensaje").equalsIgnoreCase("000")) {
								CUN = resCUN.getString("IdCun");
							}
						}

						if (asesoria.get("respuesta").equals("1") && valida) {
							MainActivity.basedatos.eliminar("clientes", "id_asesoria = ?",
									new String[] { this.asesoria.getId() });
							MainActivity.basedatos.eliminar("asesorias", "id = ?", new String[] { this.asesoria.getId() });
							MainActivity.basedatos.eliminar("ventas", "id = ?", new String[] { this.asesoria.getId() });
							MainActivity.basedatos.eliminar("competencias", "id = ?",
									new String[] { this.asesoria.getId() });
							MainActivity.basedatos.eliminar("prospecto", "id = ?", new String[] { this.asesoria.getId() });
							Intent intent = new Intent();

							if (prosMensaje.equalsIgnoreCase("No hay prospecto.")) {
								prosMensaje = "";
							}

							String Complemento = "";

							System.out.println("despues de Complemento " + BlinMensaje);

							if (!venMensaje.equalsIgnoreCase("")) {
								if (!CUN.equalsIgnoreCase("")) {
									Complemento = venMensaje + ", Con Cun # " + CUN;
								} else {
									Complemento = venMensaje;
								}
							} else if (!prosMensaje.equalsIgnoreCase("")) {
								Complemento = prosMensaje;
							} else if (!CompMensaje.equalsIgnoreCase("")) {
								Complemento = CompMensaje;
							} else if (!BlinMensaje.equalsIgnoreCase("")) {
								Complemento = BlinMensaje;
							}

							if (!Complemento.equalsIgnoreCase("")) {
								intent.putExtra("Mensaje", "Asesoria Creada Correctamente. " + Complemento);
							} else {
								intent.putExtra("Mensaje", "Asesoria Creada Correctamente.");
							}

							intent.putExtra("Borrar", "SI");
							setResult(MainActivity.OK_RESULT_CODE, intent);

						/* Parte para crear PDF y enviarlo por correo */

							if (venta.get("mensaje").equals("Venta Agregada")
									|| venta.get("mensaje").equals("Venta Existente")) {
								try {
									// System.out.println("Entrando a PDF");
									Pdf pdf = new Pdf();
									pdf.pdf(venta.getString("identificador"), this.asesoria.cliente, this.asesoria.venta);
									if (MainActivity.seguimiento) {
										GoogleAnalytics mga = GoogleAnalytics.getInstance(this);
										Tracker mtraker = mga.getTracker("UA-44799901-1");
										mga.getLogger().setLogLevel(LogLevel.VERBOSE);
										mtraker.set("Respuesta", "PDF Correcto");
										mtraker.send(MapBuilder.createEvent("PDF", "Enviado", "Correcto", null).build());
									}
								} catch (Exception e) {
									if (MainActivity.seguimiento) {
										GoogleAnalytics mga = GoogleAnalytics.getInstance(this);
										Tracker mtraker = mga.getTracker("UA-44799901-1");
										mga.getLogger().setLogLevel(LogLevel.VERBOSE);
										mtraker.set("Respuesta", e.getMessage());
										mtraker.send(MapBuilder.createEvent("PDF", "Enviado Fail", e.getMessage(), null)
												.build());
									}
								}
							}

							finish();

						} else {
							Intent intent = new Intent();
							intent.putExtra("Mensaje", "Asesoria Sin Guardar");
							intent.putExtra("Borrar", "NO");
							setResult(MainActivity.OK_RESULT_CODE, intent);
							finish();
						}
					}

				}



			} else {
				// System.out.println("Respuesta del envio de asesoria
				// erronea");
			}

        } catch (JSONException e) {
            // Log.w("Error", e.getMessage());
            e.printStackTrace();
            if (MainActivity.seguimiento) {
                GoogleAnalytics mga = GoogleAnalytics.getInstance(this);
                Tracker mtraker = mga.getTracker("UA-44799901-1");
                mga.getLogger().setLogLevel(LogLevel.VERBOSE);
                mtraker.set("Respuesta", e.getMessage());
                mtraker.send(MapBuilder.createEvent("SEND_ASESORIA", "VACIA", e.getMessage(), null).build());
            }
        }

        // ResultadoIVR(value.toString());
    }

    public void pintarDocumentacion() {

        listDocumentacion.clear();
        listDocumentacion = asesoria.venta.getListDocumentacion();

        if (listDocumentacion != null && listDocumentacion.size() > 0) {
            ListaDefaultAdapter adaptador = new ListaDefaultAdapter(this, listDocumentacion, this);

            lsvDocumentacion.setAdapter(adaptador);
            lsvDocumentacion.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (listDocumentacion.get(arg2).getTitulo()
                            .equalsIgnoreCase(getResources().getString(R.string.documentacion))) {
                        Regresar();
                    }
                }
            });
        }
    }

    public void MostrarDocumentacion() {

        Animation entrada = AnimationUtils.loadAnimation(this, R.anim.entrada);
        linearVenta.setVisibility(View.GONE);
        linearDocumentacion.setVisibility(View.VISIBLE);
        linearDocumentacion.setAnimation(entrada);

    }

    public void Regresar() {
        Animation entrada = AnimationUtils.loadAnimation(this, R.anim.entrada);
        Animation salida = AnimationUtils.loadAnimation(this, R.anim.salida);
        linearVenta.setVisibility(View.VISIBLE);
        linearDocumentacion.setVisibility(View.GONE);
        salida.reset();
        linearDocumentacion.setAnimation(salida);
        entrada.reset();
        linearVenta.setAnimation(entrada);
    }

	private void validarDebitoAutomaticoExistente(){
		JSONObject data = new JSONObject();

		try {

			data.put("Cedula", asesoria.cliente.getCedula());
			data.put("Ciudad", asesoria.cliente.getCiudadDane());
			data.put("Direccion", asesoria.cliente.getDireccion());

		} catch (JSONException e) {
			Log.w("Error", e.getMessage());
		}

		Log.d("Domiciliacion", data.toString());
		Log.d("Domiciliacion",Utilidades.camposUnicosCiudad("CRMCarrusel", asesoria.cliente.getCiudad()));

		ArrayList<String> parametros = new ArrayList<String>();
		parametros.add(Utilidades.camposUnicosCiudad("CRMCarrusel", asesoria.cliente.getCiudad()));
		parametros.add(data.toString());


		ArrayList<Object> params = new ArrayList<Object>();
		params.add(MainActivity.config.getCodigo());
		params.add("validarDebitoAutomaticoExistente");
		params.add(parametros);

		Simulador simulador = new Simulador();
		simulador.setManual(this);
		simulador.addObserver(this);

		simulador.execute(params);

	}

	private void tratarValidacionDebitoAutomatico(String respuesta){

		Log.d("Domiciliacion", respuesta);

		boolean bloqueo = false;

		try {
			JSONObject json = new JSONObject(respuesta);

			if (Configuracion.validarIntegridad(json.getString("data"), json.getString("crc"))) {
				String data = new String(Base64.decode(json.getString("data")));

				Log.d("Domiciliacion",data);

				json = new JSONObject(data);

				if(json.getString("codigoMensaje").equals("00")){
					if(json.getJSONObject("data").getString("debitoAutomatico").equalsIgnoreCase("S")){
						asesoria.cliente.setDomiciliacion("NO");
						Dialogo dialogo = null;
						if(json.getJSONObject("data").getString("entidad").contains("BANCOLOMBIA")){
							if(json.getJSONObject("data").getString("tipoDebitoAutomatico").equalsIgnoreCase("DB")){
								dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,getResources().getString(R.string.mensajedebitoautomaticodebito));
							}else{
								dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,getResources().getString(R.string.mensajedebitoautomaticredito));
							}
						}else {
							dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,getResources().getString(R.string.mensajedebitoautomaticredito));
						}

						dialogo.dialogo.show();

						bloqueo = true;


					}
				}

			} else {
				Toast.makeText(this, getResources().getString(R.string.mensajedebitoautomaticoexistente), Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			Toast.makeText(this, getResources().getString(R.string.mensajedebitoautomaticoexistente), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

}
