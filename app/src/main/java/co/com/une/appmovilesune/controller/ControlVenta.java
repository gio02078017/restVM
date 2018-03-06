package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.kobjects.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.CrackleExistente;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ListaAdicionales;
import co.com.une.appmovilesune.adapters.ListaAgendamiento;
import co.com.une.appmovilesune.adapters.ListaAgendamientoAdapter;
import co.com.une.appmovilesune.adapters.ListaChecked;
import co.com.une.appmovilesune.adapters.ListaCheckedAdapter;
import co.com.une.appmovilesune.change.Interprete;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesDecos;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.complements.Validaciones;
import co.com.une.appmovilesune.components.ResumenInternet;
import co.com.une.appmovilesune.components.ResumenTelefonia;
import co.com.une.appmovilesune.components.ResumenTelevision;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.AdicionalCotizador;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.CotizacionCliente;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.Venta;

import com.google.analytics.tracking.android.EasyTracker;

public class ControlVenta extends Activity implements Subject, Observer, TextWatcher {

    ResumenTelefonia rto;
    ResumenTelevision rtv;
    ResumenInternet rba;
    Spinner sltEmpaquetado;
    TextView lblTotal, lblAgendamiento, lblTotalDescuento;
    LinearLayout llyBarrio, llyTotalDescuento, llyAgendaDomingo;
    AutoCompleteTextView txtBarrio;
    EditText txtObservaciones;
    CheckBox chkAgendaDomingo;

    public ImageButton btnSiguiente;

    private TituloPrincipal tp;

    private TabHost.TabSpec spec;

    private ListView lsvOtrasPromociones;
    private ListView lstAgendamineto;

    private Cotizacion cotizacion;
    private CotizacionCliente cotizacionCliente;
    private Venta venta;
    private Cliente cliente;
    private Simulador simulador;

    public static Context context;

    public Dialogo dialogo, dialogoA;

    private String fecha = "";
    private String franja = "";
    private String cuposAM = "";
    private String cuposPM = "";
    private String tipoDia = "";
    private String dia = "";
    private String agendaCompletaSiebel = "";
    private JSONObject agendaSiebel = null;
    private String idAgenda = "";
    private String idOferta = "";
    private String agendableDomingo = "0";
    private boolean agendaDomingo = false;

    private boolean validoIVR = false;

    public String id = "";

    public int codigo;

    private boolean Validar = false;

    private boolean segundaLinea = false;

    private boolean consultaNormalizada = false;

    private Observer observador;

    private String Departamento, Municipio, Barrio, tipoPredio, tipoPropiedad;

    private boolean aplicarDescuentos = false;

    private int tipoLlamada;

    TabHost tabs;

    ArrayList<ItemPromocionesAdicionales> promocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();
    ArrayList<ItemPromocionesAdicionales> promocionesAdicionalesInternet = new ArrayList<ItemPromocionesAdicionales>();


    ArrayList<ListaAdicionales> listaAdicionalesTo = new ArrayList<ListaAdicionales>();
    ArrayList<ListaAdicionales> listaAdicionales = new ArrayList<ListaAdicionales>();

    ArrayList<JSONObject> listObjectAdicionalesTo = new ArrayList<JSONObject>();
    ArrayList<JSONObject> listObjectAdicionalesBa = new ArrayList<JSONObject>();
    ArrayList<JSONObject> listObjectAdicionales = new ArrayList<JSONObject>();

    private int agendableSiebel = 2;

    private String equipos1 = "";

    private String equipos2 = "";

    private String medioIngreso;

    private String microZona = "";

    //private String Mail = "NO";

    private ArrayList<ItemDecodificador> itemDecodificadors;

    public void onBackPressed() {
        dialogo = new Dialogo(this, Dialogo.DIALOGO_CONFIRMACION, "Desea volver de la venta?");
        dialogo.dialogo.setOnDismissListener(dls);
        dialogo.dialogo.show();
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewventa);

        MainActivity.obsrVenta = this;

        context = this;
        dialogo = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_FRANJA, "");
        dialogo.setCancelable(false);
        dialogo.dialogo.setOnDismissListener(dl);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);

        Resources res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        spec = tabs.newTabSpec("Productos");
        spec.setContent(R.id.llyProductos);
        spec.setIndicator("Productos", res.getDrawable(R.drawable.producto));
        tabs.addTab(spec);

		/*
         * spec = tabs.newTabSpec("Leyes"); spec.setContent(R.id.llyPreventa);
		 * spec.setIndicator("Leyes", res.getDrawable(R.drawable.leyes));
		 * tabs.addTab(spec);
		 */

        spec = tabs.newTabSpec("Agendamiento");
        spec.setContent(R.id.llyAgendamiento);
        spec.setIndicator("Agendamiento", res.getDrawable(R.drawable.leyes));
        tabs.addTab(spec);

        // spec = tabs.newTabSpec("Otras Promociones");
        // spec.setContent(R.id.llyOtrasPromociones);
        // spec.setIndicator("Otras Promociones",
        // res.getDrawable(R.drawable.otraspromo));
        // tabs.addTab(spec);

        tabs.setCurrentTab(0);
        tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
                .setBackgroundColor(getResources().getColor(R.color.yellow));
        TextView tv = (TextView) tabs.getCurrentTabView().findViewById(android.R.id.title); // for
        // Selected
        // Tab
        tv.setTextColor(getResources().getColor(R.color.darkTab));

        tabs.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                tp.setTitulo("Venta " + tabId);
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
                Barrio = txtBarrio.getText().toString();
                if (tabId.equals("Agendamiento")) {
                    if (Utilidades.excluir("excluirAgenda", Municipio)) {

                        System.out.println("hola agenda Elite");
                        lanzarAgendaRedco("Elite");
                    }
                }

            }
        });

        rto = (ResumenTelefonia) findViewById(R.id.rsnTelefonia);
        rtv = (ResumenTelevision) findViewById(R.id.rsnTelevision);
        rba = (ResumenInternet) findViewById(R.id.rsnInternet);

        sltEmpaquetado = (Spinner) findViewById(R.id.sltEmpaquetado);

        llyBarrio = (LinearLayout) findViewById(R.id.llyBarrio);
        llyAgendaDomingo = (LinearLayout) findViewById(R.id.llyAgendaDomingo);
        txtBarrio = (AutoCompleteTextView) findViewById(R.id.txtBarrio);
        chkAgendaDomingo = (CheckBox) findViewById(R.id.chkAgendaDomingo);
        chkAgendaDomingo.setOnCheckedChangeListener(eventoAgendaDomingo);

        ArrayList<ArrayList<String>> barr = MainActivity.basedatos.consultar(false, "barrios",
                new String[]{"barrio"}, null, null, null, null, null);
        if (barr != null) {
            ArrayList<String> dat = new ArrayList<String>();
            for (int i = 0; i < barr.size(); i++) {
                dat.add(barr.get(i).get(0));
            }

            ArrayAdapter<String> adaptadorBarrios = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    dat);
            txtBarrio.setAdapter(adaptadorBarrios);
            txtBarrio.setTextColor(getResources().getColor(R.color.black));
        }

        lblAgendamiento = (TextView) findViewById(R.id.lblAgendamiento);

        lsvOtrasPromociones = (ListView) findViewById(R.id.lsvOtrasPromociones);
        lstAgendamineto = (ListView) findViewById(R.id.lstAgendamiento);

        lblTotal = (TextView) findViewById(R.id.lblTotal);
        lblTotalDescuento = (TextView) findViewById(R.id.lblTotalDescuento);

        llyTotalDescuento = (LinearLayout) findViewById(R.id.llyTotalDescuento);

        llyTotalDescuento.setVisibility(View.GONE);

        txtObservaciones = (EditText) findViewById(R.id.txtObservaciones);

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            Departamento = reicieveParams.getString("departamento");
            if (Departamento.equals("BogotaHFC")) {
                Departamento = "Cundinamarca";
            } else if (Departamento.equals("Valle")) {
                Departamento = "Valle del Cauca";
            }
            Municipio = reicieveParams.getString("municipio");
            if (Municipio.equals("Bogota HFC")) {
                Municipio = "Bogota D.C.";
            }

            Barrio = reicieveParams.getString("barrio");

            tipoPredio = reicieveParams.getString("tipoPredio");

            tipoPropiedad = reicieveParams.getString("tipoPropiedad");

            consultaNormalizada = reicieveParams.getBoolean("consultaNormalizada");

            System.out.println("tipoPredio " + tipoPredio);
            System.out.println("tipoPropiedad " + tipoPropiedad);
            System.out.println("consultaNormalizada " + consultaNormalizada);

            cotizacion = (Cotizacion) reicieveParams.getSerializable("cotizacion");
            cotizacionCliente = (CotizacionCliente) reicieveParams.getSerializable("cotizacionCliente");
            venta = (Venta) reicieveParams.getSerializable("venta");

            cliente = (Cliente) reicieveParams.getSerializable("cliente");

            System.out.println("Despues smartPromo " + cliente.getSmartPromo());

            System.out.println("MainActivity.config.srvVersion " + MainActivity.config.srvVersion);

            id = String.valueOf(cliente.getIdIVR());
            System.out.println("id IVR " + id + " cliente id ivr " + cliente.getIdIVR());

            /*if (!cliente.getCorreo().equalsIgnoreCase("") && !cliente.getCorreo().contains("@sincorreo.com")) {
                Mail = "SI";
            }*/

            if (!tipoPropiedad.equalsIgnoreCase("Conjunto Residencial") && Utilidades
                    .excluirMunicipal("habilitarAgendaDomingo", "habilitarAgendaDomingo", cliente.getCiudad())) {
                llyAgendaDomingo.setVisibility(View.VISIBLE);
            }

            simulador = (Simulador) reicieveParams.getSerializable("simulador");

            System.out.println("cliente.getIdDireccionGis() " + cliente.getIdDireccionGis());
            System.out.println("cliente.getEstandarizarSiebel() " + cliente.getEstandarizarSiebel());
            System.out.println("cliente.getCobertura() " + cliente.getCobertura());

            if (cotizacion != null) {

                if (Utilidades.excluir(cotizacion.getOferta(), cliente.getCiudad())) {
                    aplicarDescuentos = true;
                }

                Validar = true;
                validoIVR = cotizacion.isValidoIVR();
                Empaquetamiento();
                medioIngreso = cotizacion.medioIngreso;

                System.out.println("Segunda TO " + cotizacion.isSegundaTelefonia());

                if ((Boolean) cotizacion.isSegundaTelefonia() == null) {
                    segundaLinea = false;
                } else if ((Boolean) cotizacion.isSegundaTelefonia()) {
                    segundaLinea = true;
                }
                // adicionales();

                if (Municipio.equals("Medellin")) {
                    if (Barrio.equals("")) {
                        llyBarrio.setVisibility(llyBarrio.VISIBLE);
                    } else {
                        txtBarrio.setText(Barrio);
                        // llenarAgendamiento();
                    }
                } else {
                    // llenarAgendamiento();
                }

            } else if (cotizacionCliente != null) {
                if (Utilidades.excluir(cotizacionCliente.getOferta(), cliente.getCiudad())) {
                    cotizacionCliente.setAplicarDescuentos(true);
                }
                Empaquetamiento();

            }

        }

    }

    private void lanzarAgendaRedco(String Systema) {
        Intent intent = new Intent(MainActivity.MODULO_AGENDABOGOTA);
        intent.putExtra("Systema", Systema);
        startActivityForResult(intent, MainActivity.REQUEST_CODE);
    }

    public void segundaConsultaScooring() {
        System.out.println("Aplica segunda consulta");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tarificador, menu);
        return true;
    }

    public void llenarVenta() {
        // llenarDocumentacion();
        llenarHoraAtencion();
        llenarObservaciones();
        llenarTelefonia();
        llenarTelevision();
    }

    public void llenarTelefonia() {
        if (venta.getTelefonia()[7] != null) {
            // rto.setDuracion(venta.getTelefonia()[7]);
        }

        if (venta.getTelefonia()[2] != null) {
            rto.setLinea(venta.getTelefonia()[2]);
        }

        if (venta.getTelefonia()[3] != null) {
            rto.setPagoLinea(venta.getTelefonia()[3]);
        }

        if (venta.getTelefonia()[4] != null) {
            rto.setMigracion(venta.getTelefonia()[4]);
        }

        if (venta.getTelefonia()[5] != null) {
            rto.setTipoMigracion(venta.getTelefonia()[5]);
        }

    }

    public void llenarTelevision() {

        if (venta.getTelevision()[4] != null) {
            // rtv.setDuracion(venta.getTelevision()[4]);
        }

        if (venta.getTelevision()[2] != null) {
            rtv.setExtensiones(venta.getTelevision()[2]);
        }
        if (venta.getTelevision()[9] != null) {
            rtv.setMigracion(venta.getTelevision()[9]);
        }

        if (venta.getTelevision()[10] != null) {
            rtv.setTipoMigracion(venta.getTelevision()[10]);
        }

        if (venta.getTelevision()[6] != null) {
            String[] adic = venta.getTelevision()[6].split(",");
            String[] preAdic = venta.getTelevision()[7].split(",");
            String[][] adicionales = new String[adic.length][2];
            for (int i = 0; i < adic.length; i++) {
                adicionales[i][0] = adic[i];
                adicionales[i][1] = preAdic[i];
            }
            rtv.setAdicionales(adicionales);
            //rtv.setPrecioAdicionales(venta.getTelevision()[8]);
            rtv.llenarAdicionales();
        }

    }

    public void llenarBandaAncha() {

        if (venta.getInternet()[6] != null) {
            rba.setDuracion(venta.getInternet()[6]);
        }

        if (venta.getInternet()[3] != null) {
            rba.setMigracion(venta.getInternet()[3]);
        }

        if (venta.getInternet()[4] != null) {
            rba.setTipoMigracion(venta.getInternet()[4]);
        }

        if (venta.getInternet()[2] != null) {
            rba.setWifi(venta.getInternet()[2]);
        }

    }

    public void llenarHoraAtencion() {
        lblAgendamiento.setText(venta.getHorarioAtencion());
    }

    public void llenarObservaciones() {
        txtObservaciones.setText(venta.getObservaciones());
    }

    public ArrayList<JSONObject> arraylistAdicionalesTo() {
        ArrayList<JSONObject> arraylistAdicionalesTo = new ArrayList<JSONObject>();
        listaAdicionalesTo.clear();
        String[][] adicionales = cotizacion.getAdicionalesTo();

        System.out.println("Adicionales To -> " + adicionales);

        /*if (adicionales == null) {
            adicionales = new String[1][2];
            adicionales[0][0] = "Impuesto Telefonico";
            adicionales[0][1] = String.valueOf(UtilidadesTarificador.ImpuestoTelefonico(cliente.getCiudad(),
                    cliente.getDepartamento(), cliente.getEstrato()));
        }*/

        if (adicionales != null) {
            for (int i = 0; i < adicionales.length; i++) {
                listaAdicionalesTo.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], "-", "0 Meses", true));
            }
        }

        listaAdicionalesTo.add(new ListaAdicionales("Impuesto Telefonico", String.valueOf(UtilidadesTarificador.ImpuestoTelefonico(cliente.getCiudad(),
                cliente.getDepartamento(), cliente.getEstrato())), "-", "0 Meses", true));

        for (int i = 0; i < listaAdicionalesTo.size(); i++) {

            JSONObject adicional = new JSONObject();
            try {
                if (listaAdicionalesTo.get(i).getAdicional().equalsIgnoreCase("Impuesto Telefonico")) {
                    adicional.put("tipo", "IMP");
                } else {
                    adicional.put("tipo", "ADTO");
                }

                adicional.put("producto", Utilidades.Quitar_Solo_Tildes(listaAdicionalesTo.get(i).getAdicional()));
                adicional.put("precio", listaAdicionalesTo.get(i).getPrecio());
                adicional.put("descuento", listaAdicionalesTo.get(i).getDescuento());
                adicional.put("duracion", listaAdicionalesTo.get(i).getDuracion());
                adicional.put("permanencia", "0");

                System.out
                        .println("listaAdicionalesTo.get(i).getDuracion() " + listaAdicionalesTo.get(i).getDescuento());
                System.out
                        .println("listaAdicionaleTos.get(i).getDuracion() " + listaAdicionalesTo.get(i).getDuracion());
                /*
                 * adicional.put("demo",
				 * Utilidades.claveValor(cotizacion.getOferta(),
				 * listaAdicionales.get(i).getAdicional()));
				 */
                adicional.put("demo", demosAd(listaAdicionalesTo.get(i).getAdicional()));
                arraylistAdicionalesTo.add(adicional);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return arraylistAdicionalesTo;

    }

    public ArrayList<JSONObject> arraylistAdicionales() {
        ArrayList<JSONObject> arraylistAdicionales = new ArrayList<JSONObject>();
        listaAdicionales.clear();
        String[][] adicionales = cotizacion.getAdicionales();
        promocionesAdicionales = cotizacion.getItemPromocionesAdicionales();

        ArrayList<String[]> promoAdIndividual = null;

        for (int i = 0; i < adicionales.length; i++) {

            String descuento = "-";
            String duracion = "0 Meses";

            System.out.println("adicionales[i][0] " + adicionales[i][0]);

            System.out.println("promocionesAdicionales " + promocionesAdicionales.size());
            System.out.println("promocionesAdicionales " + promocionesAdicionales);

            if (promocionesAdicionales.size() > 0) {
                for (int j = 0; j < promocionesAdicionales.size(); j++) {

                    System.out.println("promocionesAdicionales.get(j) " + promocionesAdicionales.get(j));
                    System.out.println("promocionesAdicionales.get(j) " + promocionesAdicionales.get(j).getAdicional());
                    if (promocionesAdicionales.get(j).getAdicional().equalsIgnoreCase(adicionales[i][0])) {

                        descuento = promocionesAdicionales.get(j).getDescuento() + "%";
                        duracion = promocionesAdicionales.get(j).getMeses();
                    }
                }

                if (descuento.equalsIgnoreCase("0") || descuento.equalsIgnoreCase("0%")) {
                    descuento = "-";
                }

                System.out.println("adicionales[i][0] " + adicionales[i][0]);
                System.out.println("descuento " + descuento);
                System.out.println("duracion " + duracion);

                listaAdicionales.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], descuento, duracion, true));

            } else {
                listaAdicionales.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], "-", "0 Meses", true));
            }

            promoAdIndividual = promocionesAdIndividuales(cotizacion.getTelevision(),
                    (String) sltEmpaquetado.getSelectedItem(), adicionales[i][0], cotizacion.getEstrato());

            if (promoAdIndividual.size() > 0) {
                for (int j = 0; j < listaAdicionales.size(); j++) {
                    if (listaAdicionales.get(j).getAdicional().equalsIgnoreCase(adicionales[i][0])) {
                        listaAdicionales.remove(j);
                        listaAdicionales.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1],
                                promoAdIndividual.get(0)[0], promoAdIndividual.get(0)[1], true));
                    }
                }
                // System.out.println("posicion
                // "+listaAdicionales.indexOf(adicionales[i][0]));
                //
                // listaAdicionales.add(new ListaAdicionales(adicionales[i][0],
                // adicionales[i][1], promoAdIndividual.get(0)[0],
                // promoAdIndividual.get(0)[1]));
            }
        }

        for (int i = 0; i < listaAdicionales.size(); i++) {

            JSONObject adicional = new JSONObject();
            try {
                adicional.put("tipo", "ADTV");
                adicional.put("producto", listaAdicionales.get(i).getAdicional());
                adicional.put("precio", listaAdicionales.get(i).getPrecio());
                adicional.put("descuento", listaAdicionales.get(i).getDescuento());
                adicional.put("duracion", listaAdicionales.get(i).getDuracion());
                adicional.put("permanencia", "0");

                System.out.println("listaAdicionales.get(i).getDuracion() " + listaAdicionales.get(i).getDescuento());
                System.out.println("listaAdicionales.get(i).getDuracion() " + listaAdicionales.get(i).getDuracion());
                /*
                 * adicional.put("demo",
				 * Utilidades.claveValor(cotizacion.getOferta(),
				 * listaAdicionales.get(i).getAdicional()));
				 */
                adicional.put("demo", demosAd(listaAdicionales.get(i).getAdicional()));
                arraylistAdicionales.add(adicional);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return arraylistAdicionales;

    }

    public ArrayList<JSONObject> arraylistAdicionalesBaGota(String nombreGota, String valorGota, String valorGotaSinIva,
                                                            String velocidadInicial, String velocidadFinal) {
        ArrayList<JSONObject> arraylistAdicionales = new ArrayList<JSONObject>();

        JSONObject adicional = new JSONObject();
        try {
            adicional.put("tipo", "ADBA");
            adicional.put("producto", nombreGota);
            adicional.put("precio", valorGota);
            adicional.put("precioSinIva", valorGotaSinIva);
            adicional.put("velocidadInicial", velocidadInicial);
            adicional.put("velocidadFinal", velocidadFinal);
            adicional.put("descuento", "-");
            adicional.put("duracion", "0 Meses");
            adicional.put("permanencia", "0");
            adicional.put("demo", "0");
            adicional.put("tiposolicitud", "Nuevo");


            arraylistAdicionales.add(adicional);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arraylistAdicionales;

    }

    public ArrayList<JSONObject> arraylistAdicionalesBa() {
        ArrayList<JSONObject> arraylistAdicionales = new ArrayList<JSONObject>();

        promocionesAdicionalesInternet = cotizacion.getItemPromocionesAdicionalesInternet();

        listaAdicionales.clear();
        String[][] adicionales = cotizacion.getAdicionalesBa();

        for (int i = 0; i < adicionales.length; i++) {

            String descuento = "-";
            String duracion = "0 Meses";
            String tipoSolicitud = "N/A";

            if (promocionesAdicionalesInternet.size() > 0) {
                for (int j = 0; j < promocionesAdicionalesInternet.size(); j++) {

                    System.out.println("promocionesAdicionalesInternet.get(j) " + promocionesAdicionalesInternet.get(j));
                    System.out.println("promocionesAdicionalesInternet.get(j) " + promocionesAdicionalesInternet.get(j).getAdicional());
                    if (promocionesAdicionalesInternet.get(j).getAdicional().equalsIgnoreCase(adicionales[i][0])) {

                        descuento = promocionesAdicionalesInternet.get(j).getDescuento() + "%";
                        duracion = promocionesAdicionalesInternet.get(j).getMeses();
                    }
                }

                if (descuento.equalsIgnoreCase("0") || descuento.equalsIgnoreCase("0%")) {
                    descuento = "-";
                }

                System.out.println("adicionales[i][0] " + adicionales[i][0]);
                System.out.println("descuento " + descuento);
                System.out.println("duracion " + duracion);

                listaAdicionales.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], descuento, duracion, true));

            } else {
                listaAdicionales.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], "-", "0 Meses", true));
            }
        }

        for (int i = 0; i < listaAdicionales.size(); i++) {

            JSONObject adicional = new JSONObject();
            try {
                adicional.put("tipo", "ADBA");
                adicional.put("producto", listaAdicionales.get(i).getAdicional());
                adicional.put("precio", listaAdicionales.get(i).getPrecio());
                adicional.put("descuento", listaAdicionales.get(i).getDescuento());
                adicional.put("duracion", listaAdicionales.get(i).getDuracion());
                adicional.put("permanencia", "0");

                System.out.println("listaAdicionales.get(i).getDuracion() " + listaAdicionales.get(i).getDescuento());
                System.out.println("listaAdicionales.get(i).getDuracion() " + listaAdicionales.get(i).getDuracion());

                adicional.put("demo", demosAd(listaAdicionales.get(i).getAdicional()));

                System.out.println("retirar HBO GO no cumple jsonAdicionalesBa listaAdicionales.get(i).getAdicional() " + listaAdicionales.get(i).getAdicional());

                if (listaAdicionales.get(i).getAdicional().equalsIgnoreCase("HBO GO")) {
                    Log.d("cotizacion TV ", cotizacion.getTelevision());
                    System.out.println("retirar HBO GO no cumple if hbo go ");
                    System.out.println("retirar HBO GO no cumple cotizacion.isRetiroHBOGO() 1 " + cotizacion.isRetiroHBOGO());
                        /*if (!cotizacion.getTelevision().equalsIgnoreCase(Utilidades.inicial) && !cotizacion.getTelevision().equalsIgnoreCase(Utilidades.inicial_guion)) {
                            //if(UtilidadesTarificadorNew.validarHBOGOPortafolioElite(cliente.getPortafolioElite(), cliente.getCedula(), cotizacion.getTipoOferta())){
                            System.out.println("retirar HBO GO no cumple cotizacion.isRetiroHBOGO() 2 " + cotizacion.isRetiroHBOGO());
                            if (cotizacion.isRetiroHBOGO()) {
                                adicional.put("tiposolicitud", "Eliminar");
                                System.out.println("retirar HBO GO no tiposolicitud Eliminar " + cotizacion.isRetiroHBOGO());
                            } else {
                                adicional.put("tiposolicitud", "Nuevo");
                                System.out.println("retirar HBO GO no cumple tiposolicitud nuevo  " + cotizacion.isRetiroHBOGO());
                            }
                        } else {
                            adicional.put("tiposolicitud", "Nuevo");
                            System.out.println("retirar HBO GO no cumple tiposolicitud nuevo  " + cotizacion.isRetiroHBOGO());
                        }*/
                    if (cotizacion.isRetiroHBOGO()) {
                        adicional.put("tiposolicitud", "Eliminar");
                        System.out.println("retirar HBO GO no tiposolicitud Eliminar " + cotizacion.isRetiroHBOGO());
                    } else {
                        adicional.put("tiposolicitud", "Nuevo");
                        System.out.println("retirar HBO GO no cumple tiposolicitud nuevo  " + cotizacion.isRetiroHBOGO());
                    }
                } else if (listaAdicionales.get(i).getAdicional().equalsIgnoreCase("Crackle Sony Ba")) {
                    Log.d("cotizacion TV ", cotizacion.getTelevision());
                    System.out.println("if hbo go ");
                    //CrackleExistente crackleExistente = UtilidadesTarificadorNew.validarCracklePortafolioElite(cliente.getPortafolioElite(), cliente.getCedula(), cotizacion.getTipoOferta());
                    if (cotizacion.isRetiroCrackleBa()) {
                        adicional.put("tiposolicitud", "Eliminar");
                    } else {
                        adicional.put("tiposolicitud", "Nuevo");
                    }
                    adicional.put("producto", "Crackle Sony");
                } else {
                    adicional.put("tiposolicitud", "Nuevo");
                    System.out.println("else hbo go ");
                }

                System.out.println("jretirar HBO GO no cumple sonAdicionalesBa adicional " + adicional);

                arraylistAdicionales.add(adicional);

                System.out.println("retirar HBO GO no cumple jsonAdicionalesBa arraylistAdicionales" + arraylistAdicionales);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        System.out.println("adba " + arraylistAdicionales.toString());

        return arraylistAdicionales;

    }

    public void Empaquetamiento() {
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                Utilidades.SI_NO);
        sltEmpaquetado.setAdapter(adaptador);

        System.out.println("Empaquetamiento oferta " + cotizacionCliente.getOferta());

        if (cotizacionCliente.getOferta() != null) {
            if (!cotizacionCliente.getOferta().equalsIgnoreCase("")) {
                sltEmpaquetado.setEnabled(false);
            }
        } else {
            sltEmpaquetado.setEnabled(false);
        }

        try {
            int contador = cotizacionCliente.getContadorProductos();
            if (contador > 1) {

                System.out.println("Empaquetamiento contador " + contador);

                if (Utilidades.validarNacionalValor("ofertaIndividual", cotizacionCliente.getOferta())) {
                    System.out.println("Empaquetamiento ofertaIndividual ");
                    sltEmpaquetado.setSelection(1);
                    Venta_Individual();
                } else {
                    System.out.println("Empaquetamiento paquete ");
                    sltEmpaquetado.setSelection(0);
                    Venta_Empaquetada();
                }

            } else {
                sltEmpaquetado.setSelection(1);
                sltEmpaquetado.setEnabled(false);
                Venta_Individual();
            }
        } catch (NumberFormatException e) {
            Log.w("Error ", e.getMessage());
        }

        sltEmpaquetado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                String empaquetamiento = parent.getSelectedItem().toString();

                if (empaquetamiento.equals("SI")) {
                    Venta_Empaquetada();
                } else if (empaquetamiento.equals("NO")) {
                    Venta_Individual();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // lblMensaje.setText("");
            }
        });

    }

    public void Venta_Individual() {

        cotizacionCliente.setVentaEmpaquetada(false);

        System.out.println("llenadoVenta Venta_Empaquetada");

        if (!Utilidades.validarVacioProducto(UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getTELEFONIA()).getPlan())) {
            rto.Telefonia(this, this, cotizacionCliente, cliente);
        } else {
            rto.setVisibility(View.GONE);
        }

        if (!Utilidades.validarVacioProducto(UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getTELEVISION()).getPlan())) {
            rtv.Television(this, this, cotizacionCliente, cliente);
        } else {
            rtv.setVisibility(View.GONE);
        }

        if (!Utilidades.validarVacioProducto(UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getINTERNET()).getPlan())) {
            rba.Internet(this, this, cotizacionCliente, cliente);
        } else {
            rba.setVisibility(View.GONE);
        }

        lblTotal.setText(String.valueOf(cotizacionCliente.getTotalIndividual()));

        if (venta != null) {
            lblAgendamiento.setText(venta.getHorarioAtencion());
            txtObservaciones.setText(venta.getObservaciones());
        }
    }

    public void Venta_Empaquetada() {

        cotizacionCliente.setVentaEmpaquetada(true);

        System.out.println("llenadoVenta Venta_Empaquetada");

        if (!Utilidades.validarVacioProducto(UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getTELEFONIA()).getPlan())) {
            rto.Telefonia(this, this, cotizacionCliente, cliente);
        } else {
            rto.setVisibility(View.GONE);
        }

        if (!Utilidades.validarVacioProducto(UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getTELEVISION()).getPlan())) {
            rtv.Television(this, this, cotizacionCliente, cliente);
        } else {
            rtv.setVisibility(View.GONE);
        }

        if (!Utilidades.validarVacioProducto(UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getINTERNET()).getPlan())) {
            rba.Internet(this, this, cotizacionCliente, cliente);
        } else {
            rba.setVisibility(View.GONE);
        }

        lblTotal.setText(String.valueOf(cotizacionCliente.getTotalEmpaquetado()));

        if (venta != null) {
            txtObservaciones.setText(venta.getObservaciones());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Repetir Codigo")) {
            // System.out.println("id" + id);
            if (!id.equals("")) {
                ArrayList<String[]> parametros = new ArrayList<String[]>();
                parametros.add(new String[]{"Accion", "confirmar"});
                parametros.add(new String[]{"Tabla", "ivr"});
                parametros.add(new String[]{"Parametros", "{\"id\":\"" + id + "\"}"});
                try {
                    JSONObject jop = new JSONObject(MainActivity.conexion.ejecutarSoap("Ejecutar", parametros));
                    String data = jop.get("data").toString();
                    System.out.println("Data Llamada => " + data);
                    if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                        data = new String(Base64.decode(data));
                        System.out.println(data);
                        JSONArray ja = new JSONArray(data);
                        JSONObject jo = ja.getJSONObject(0);
                        if (jo.getString("completo").equals("0")) {
                            Toast.makeText(this, "Complete el ivr", Toast.LENGTH_SHORT).show();
                        } else {
                            LanzarLLamadaPorLlamadasMasivasRepetirCodigo(id);
                        }
                    } else {
                        /*
                         * System.out .println(
						 * "Los datos no se descargaron correctamente" );
						 */
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Complete el ivr", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getTitle().equals("Repetir Llamada")) {
            //LanzarLLamada(id, "5", Mail, venta.getCobroDomingo());
        } else if (item.getTitle().equals("Grabacion")) {
            System.out.println("id" + id);
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "4"});
            parametros.add(new String[]{"Id", id});
            parametros.add(new String[]{"Mail", Utilidades.cumpleMail(cliente)});
            parametros.add(new String[]{"CobroDomingo", Utilidades.cambioCobroDomingo(venta.getCobroDomingo())});
            String llamada = MainActivity.conexion.ejecutarSoap("LanzarLlamada", parametros);
            System.out.println(llamada);
        } else if (item.getTitle().equals("Ingresar Codigo")) {
            mostrarDialgo();
        }
        return true;
    }

    public void procesarVenta(View v) {

        for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
            if (!Utilidades.validarVacioProducto(cotizacionCliente.getProductoCotizador().get(i).getPlan())) {
                if (cotizacionCliente.getProductoCotizador().get(i).getTipo() == ProductoCotizador.getTELEFONIA()) {
                    cotizacionCliente.getProductoCotizador().set(i, rto.getProductoCotizador());
                } else if (cotizacionCliente.getProductoCotizador().get(i).getTipo() == ProductoCotizador.getTELEVISION()) {
                    cotizacionCliente.getProductoCotizador().set(i, rtv.getProductoCotizador());
                } else if (cotizacionCliente.getProductoCotizador().get(i).getTipo() == ProductoCotizador.getINTERNET()) {
                    cotizacionCliente.getProductoCotizador().set(i, rba.getProductoCotizador());
                }
            }
        }

        //if(cotizacionCliente.getProductoCotizador().get)
        cotizacionCliente.consolidarCotizacionCliente(null);
        btnSiguiente = (ImageButton) v;

        boolean compatibilidad = true;
        boolean nuevos = nuevos(cotizacionCliente);
        ;
        int totalNuevos = totalProductosNuevos(cotizacionCliente);

        venta = new Venta();

        venta.medioIngreso = medioIngreso;

        venta.setCotizacionCliente(cotizacionCliente);

        if (!nuevos) {
            cliente.getScooringune().setPasaScooring(true);
        }

         venta.setDocumentacion(id);
        venta.setTotal(lblTotal.getText().toString().replace(",", "."));
        cotizacionCliente.setTotalCotizacionConsolidado(lblTotal.getText().toString().replace(",", "."));

        venta.setTotalNuevos("" + totalNuevos);

        System.out.println("venta.getTotal() " + venta.getTotal());

        System.out.println("cliente.getScooringune().isPasaScooring() " + cliente.getScooringune().isPasaScooring());

        System.out.println(
                "cliente.getScooringune().isValidarScooring() " + cliente.getScooringune().isValidarScooring());

        System.out.println("isNoAplicaScooring() " + cliente.getScooringune().isNoAplicaScooring());

        if (cliente.getTipoCuenta().equalsIgnoreCase("Scooring")) {

            // if (Utilidades.excluir("habilitarScooring", cliente.getCiudad()))
            // {
            if (!cliente.getScooringune().isNoAplicaScooring()) {
                if (cliente.getScooringune().isValidarScooring() && cliente.getScooringune().isPasaScooring()) {

                    int maximaVenta = Utilidades.convertirNumericos(cliente.getScooringune().getTotalMaxima(),
                            "getTotalMaxima()");

                    System.out.println("totalNuevos " + totalNuevos);
                    System.out.println("maximaVenta " + maximaVenta);

                    if (maximaVenta > 0) {
                        System.out.println("entro a condicion scooring");
                        if (totalNuevos > maximaVenta) {
                            System.out.println("entro a condicion scooring");
                            venta.setValidarOfertaScooring(false);
                            venta.setMensajeScore(
                                    "El Cupo Aprobado es de " + maximaVenta + " y  la oferta seleccionada es "
                                            + totalNuevos + " , por lo tanto no es posible ingresar la venta.");
                        }
                    } else {
                        venta.setValidarOfertaScooring(false);
                        venta.setMensajeScore(
                                "El Cupo Aprobado es invalido, de esta forma no es posible ingresar la venta.");
                    }
                } else if (!cliente.getScooringune().isValidarScooring()
                        && cliente.getScooringune().getEstado().equalsIgnoreCase("ESPERANDO RESPUESTA")) {
                    segundaConsultaScooring();
                }
            }
        }

        System.out.println("venta.getTotalMaxima() " + cliente.getScooringune().getTotalMaxima());

        venta.setEmpaquetamiento((String) sltEmpaquetado.getSelectedItem());

        venta.setDependencia(cotizacionCliente.getOferta());

        venta.setOtrasPromociones("");
        // System.out.println("venta.getOtrasPromociones()
        // "+venta.getOtrasPromociones());
        venta.setScooring("scooring");
        venta.setObservaciones(txtObservaciones.getText().toString());

        venta.setHorarioAtencion(lblAgendamiento.getText().toString());

        venta.setMicroZona(microZona);

        if (agendaDomingo) {
            venta.setCobroDomingo("1");
        } else {
            venta.setCobroDomingo("0");
        }

        venta.setValidarTelevisores(rtv.isValidarTelevisores());
        System.out.println("rtv.isValidarDecosPago() " + rtv.isValidarDecosPago());
        venta.setValidarDecosPago(rtv.isValidarDecosPago());

        venta.setMedioIngreso("Cotizador");

        venta.setOferta(cotizacionCliente.getOferta());

        venta.setDepartamento(Departamento);
        venta.setMunicipio(Municipio);

        if (Utilidades.excluir("excluirAgenda", Municipio)) {
            venta.setValidarAgenda(false);
        } else {
            venta.setValidarAgenda(true);
        }

        System.out.println("agendableSiebel " + agendableSiebel);

        venta.setAgendaSiebel("");
        venta.setIdOferta("");
        venta.setAgendableSiebel(2);

        System.out.println("no aplica validacion");


        if (compatibilidad) {

            Intent intent = new Intent(MainActivity.MODULO_RESUMEN_PREVENTA);
            intent.putExtra("venta", venta);
            intent.putExtra("cliente", cliente);
            startActivityForResult(intent, MainActivity.REQUEST_CODE);
        } else {
            Toast.makeText(this, "No Se Puede Pasar Venta Incompatible.", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean nuevos(CotizacionCliente cotizacionCliente) {
        boolean nuevos = false;

        for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
            if (!Utilidades.validarVacioProducto(cotizacionCliente.getProductoCotizador().get(i).getPlan())) {
                if (cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNumerico().equalsIgnoreCase("1")) {
                    nuevos = true;
                }
            }
        }

        return nuevos;
    }

    public int totalProductosNuevos(CotizacionCliente cotizacionCliente) {
        int totalNuevos = 0;

        for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
            if (!Utilidades.validarVacioProducto(cotizacionCliente.getProductoCotizador().get(i).getPlan())) {
                if (cotizacionCliente.getProductoCotizador().get(i).getTipoPeticionNumerico().equalsIgnoreCase("1")) {
                    totalNuevos += Utilidades.convertirNumericos(cotizacionCliente.getProductoCotizador().get(i).getPrecio(), "get(i).getPrecio()");
                }
            }
        }

        return totalNuevos;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_CODE) {
            // cogemos el valor devuelto por la otra actividad

            try {
                if (data.getStringExtra("result") != null) {
                    String result = data.getStringExtra("result");
                    if (result.equals("Acepto")) {
                        procesarVenta();
                        // InsertarConfirmacion();
                    } else if (result.equals("Cancelo")) {
                        // System.out.println(result);
                    }
                }

                if (data.getStringExtra("Agenda") != null) {
                    System.out.println("Agenda " + data.getStringExtra("Agenda"));
                    lblAgendamiento.setText(data.getStringExtra("Agenda"));
                    tabs.setCurrentTab(0);
                } else {
					/*
					 * System.out .println(
					 * "NullPointerException (@ControlVenta:onActivityResult:882) {main} =>"
					 * + data.getStringExtra("result"));
					 */
                }
            } catch (Exception e) {
                // Log.w("Error ", e.getStackTrace());
                e.printStackTrace();
            }

        }
    }

    private void procesarVenta() {

        System.out.println("Validaciones.validarVenta(venta) " + Validaciones.validarVenta(venta));

        if (Validaciones.validarVenta(venta)) {
            if (!validoIVR) {
                // LanzarLLamada(id,"5");
                InsertarConfirmacion();
            } else {
                cotizacion.setValidoIVR(true);
                MainActivity.btnTarificador.setOK();
                Intent intent = new Intent();
                intent.putExtra("Venta", venta);
                intent.putExtra("estrato", cotizacion.getEstrato());
                if (txtBarrio.getText().toString().equals("")) {
					/*
					 * System.out.println("BArrio 708 => " +
					 * txtBarrio.getText().toString() + " <=");
					 */
                    intent.putExtra("barrio", Barrio);
                }

                setResult(MainActivity.OK_RESULT_CODE, intent);
                finish();
            }
        } else {
            Toast.makeText(context, "Venta Incompleta", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.MODULO_MENSAJES);
            intent.putExtra("mensajes", Validaciones.getMensajes().toString());
            startActivityForResult(intent, MainActivity.REQUEST_CODE);
        }
    }

    private void InsertarConfirmacion() {

        addObserver(MainActivity.obsrMainActivity);
        notifyObserver();
        tipoLlamada = 0;

        ArrayList<String> consolidado = MainActivity.consolidarIVR();

        System.out.println("consolidado " + consolidado);

        if (consolidado.get(2).length() >= 7) {
            if (id.equals("0") || id.equals("")) {
                codigo = (int) (Math.random() * (9999 - 1000 + 1) + 1000);

                consolidado.add(String.valueOf(codigo));

                JSONObject IVR = new JSONObject();
                JSONArray cotizacion = new JSONArray();
                JSONObject jsonPlan = new JSONObject();
                JSONObject jsonAdicional = new JSONObject();
                JSONObject jsonDecos = new JSONObject();

                try {
                    for (int i = 0; i < venta.getCotizacionCliente().getProductoCotizador().size(); i++) {
                        ProductoCotizador producto = venta.getCotizacionCliente().getProductoCotizador().get(i);
                        if (!Utilidades.validarVacioProducto(producto.getPlan())) {
                            jsonPlan = Utilidades.jsonProductos(producto.traducirProducto().toUpperCase(), UtilidadesTarificadorNew.cambiarPlan(producto.getPlan()), producto.getPrecio(), String.valueOf(producto.getDescuentoCargobasico()),
                                    String.valueOf(producto.getDuracionDescuento()), "0");
                            cotizacion.put(jsonPlan);
                            if (producto.getTipo() == ProductoCotizador.getTELEVISION()) {
                                if (producto.getObjectDecodificador().getItemDecodificadors() != null) {
                                    for (int j = 0; j < producto.getObjectDecodificador().getItemDecodificadors().size(); j++) {

                                        String precio = producto.getObjectDecodificador().getItemDecodificadors().get(j).getPrecio();
                                        String tipoAlquiler = producto.getObjectDecodificador().getItemDecodificadors().get(j).getTipoAlquiler();
                                        String tipoDeco = UtilidadesTarificadorNew.cambiarPlan(producto.getObjectDecodificador().getItemDecodificadors().get(j).getOriginal());

                                        if (tipoAlquiler.equalsIgnoreCase("AL") && !precio.equals("0") && !precio.equals("0.0")) {
                                            System.out.println("itemDecodificadors.get(i) entro ");
                                            cotizacion.put(Utilidades.jsonProductos("ADTV", "Decodificador " + tipoDeco + " (Adicional)", precio, "0", "0", "0"));
                                        }

                                    }
                                }
                            }

                            if (producto.getAdicionalesCotizador() != null) {
                                for (int j = 0; j < producto.getAdicionalesCotizador().size(); j++) {
                                    AdicionalCotizador adicional = producto.getAdicionalesCotizador().get(j);
                                    jsonAdicional = Utilidades.jsonProductos("AD" + producto.traducirProducto().toUpperCase(), adicional.getNombreAdicional(), String.valueOf(adicional.getPrecioAdicional()), adicional.getDescuento(),
                                            adicional.getDuracionDescuento(), "0");
                                    cotizacion.put(jsonAdicional);
                                }
                            }

                        }
                    }

                    IVR.put("cotizacionCliente", cotizacionCliente.consolidarCotizacionIVR(null).getJSONObject("oferta"));

                    if (cliente.getIdIVR() == 0) {
                        IVR.put("tipo", "insert");
                        IVR.put("codigoasesor", consolidado.get(0));
                        IVR.put("telefono", consolidado.get(2));
                        IVR.put("codigo", consolidado.get(5));
                        IVR.put("tipocliente", consolidado.get(3));
                        IVR.put("cotizacion", cotizacion);
                        IVR.put("documento", consolidado.get(4));
                        IVR.put("scoring", "NO");
                        tipoLlamada = 1;
                    } else {
                        IVR.put("tipo", "update");
                        IVR.put("id", cliente.getIdIVR());
                        IVR.put("codigo", String.valueOf(codigo));
                        IVR.put("cotizacion", cotizacion);
                        IVR.put("scoring", "SI");
                        tipoLlamada = 2;
                    }

                    if (Utilidades.excluir("excluirAgenda", Municipio)) {
                        IVR.put("fecha", "");
                        IVR.put("franja", "");
                    } else {
                        IVR.put("fecha", fecha);
                        IVR.put("franja", franja);
                    }
                    IVR.put("Scooring", 0);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                System.out.println("datos del IVR " + IVR);

                ArrayList<String> parametros = new ArrayList<String>();
                parametros.add(IVR.toString());
                parametros.add(Utilidades.generarJsonVerificacionServicios());
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(MainActivity.config.getCodigo());
                params.add("InsertarConfirmacion");
                params.add(parametros);
                Simulador simulador = new Simulador();
                simulador.setManual(this);
                simulador.addObserver(this);
                simulador.execute(params);
            } else {
                //LanzarLLamada(id, "5", Mail, venta.getCobroDomingo());
            }
        } else {
            Toast.makeText(this, "No ha Ingresado Telefono de destino", Toast.LENGTH_SHORT).show();
        }
     }

    private void LanzarLLamada(String id, String tipo, String mail, String CobroDomingo) {

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(tipo);
        parametros.add(id);
        parametros.add(mail);
        parametros.add(Utilidades.cambioCobroDomingo(CobroDomingo));
        parametros.add(cotizacionCliente.getTotalPagoConexion());
        parametros.add(cotizacionCliente.getDescuentoConexion());
        parametros.add(cotizacionCliente.getTotalPagoParcialConexion());
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("LanzarLlamada");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

    }

    private void LanzarLLamadaPorLlamadasMasivas(String id) {

        JSONObject datosExtras = new JSONObject();
        try {
            datosExtras.put("idIVR", Utilidades.id_ivr);
            datosExtras.put("nombreIVR", Utilidades.nombre_ivr_venta);
            datosExtras.put("tipoIVR", Utilidades.tipoIvrVenta);
            datosExtras.put("idConfirmacion", id);
            datosExtras.put("codigoConfirmacion", codigo);
            datosExtras.put("tipoLLamada", tipoLlamada);
            datosExtras.put("mail", Utilidades.cumpleMail(cliente));
            datosExtras.put("codigoAsesor",MainActivity.config.getCodigo_asesor());
            datosExtras.put("scoring","NO");
            if(tipoLlamada == 2){
                datosExtras.put("scoring","SI");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String datosIVR = cotizacionCliente.consolidarCotizacionIVR(datosExtras).toString();

        System.out.println("datosIVR " + datosIVR);

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(datosIVR);
        parametros.add(MainActivity.config.getCodigo());
        parametros.add(id);
        parametros.add(Utilidades.id_ivr);
        parametros.add(cliente.getCedula());
        parametros.add(cliente.getNombre() + " " + cliente.getApellido());
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("LanzarLLamadaPorLlamadasMasivas");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

    }

    private void LanzarLLamadaPorLlamadasMasivasRepetirCodigo(String id) {

        JSONObject datosExtras = new JSONObject();
        try {
            datosExtras.put("idIVR", Utilidades.id_ivr);
            datosExtras.put("nombreIVR", Utilidades.nombre_ivr_venta);
            datosExtras.put("tipoIVR", Utilidades.tipoIvrRepetirCodigo);
            datosExtras.put("idConfirmacion", id);
            datosExtras.put("codigoConfirmacion", codigo);
            datosExtras.put("tipoLLamada", tipoLlamada);
            datosExtras.put("mail", Utilidades.cumpleMail(cliente));
            datosExtras.put("codigoAsesor",MainActivity.config.getCodigo_asesor());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(datosExtras.toString());
        parametros.add(MainActivity.config.getCodigo());
        parametros.add(id);
        parametros.add(Utilidades.id_ivr);
        parametros.add(cliente.getCedula());
        parametros.add(cliente.getNombre() + " " + cliente.getApellido());
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("LanzarLLamadaPorLlamadasMasivas");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

    }

    private void mostrarDialgo() {
        System.out.println("Codigo => " + codigo);
        AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle(getResources().getString(R.string.confirmacioncodigo));
        promptDialog.setCancelable(false);
        final EditText input = new EditText(this);
        final Context context = this;
        promptDialog.setView(input);

        promptDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                if (value.toCharArray().length == 4 || value.equals("20130806")) {
                    if (value.equals(String.valueOf(codigo)) || (!Utilidades.excluirGeneral("codigosPrueba", MainActivity.config.getCodigo_asesor()) && value.equals("20130806"))) {

                        cotizacionCliente.setValidoIVR(true);
                        venta.setDocumentacion(id);
                        MainActivity.btnTarificador.setOK();
                        Intent intent = new Intent();
                        intent.putExtra("Venta", venta);
                        intent.putExtra("estrato", cotizacionCliente.getEstrato());
                        intent.putExtra("barrio", Barrio);
                        setResult(MainActivity.OK_RESULT_CODE, intent);
                        finish();
                    } else {
                        Toast.makeText(context, "No Concuerdan los codigos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.tamanocodigoincorrecto),
                            Toast.LENGTH_SHORT).show();
                    return;
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

    private void Limpiar_Otras_Promociones() {
        ArrayList<ListaChecked> contenido = new ArrayList<ListaChecked>();
        contenido.clear();
        ListaCheckedAdapter adaptador = new ListaCheckedAdapter(this, contenido, this);
        lsvOtrasPromociones.setAdapter(adaptador);
    }

    public String demosAd(String adicional) {

        String demo = "0";

        if (Utilidades.claveValor("dpAdicionales", adicional).equalsIgnoreCase("true")) {
            for (int i = 0; i < listaAdicionales.size(); i++) {

                String dataDemo = Utilidades.claveValor(adicional, listaAdicionales.get(i).getAdicional());
                if (!dataDemo.equalsIgnoreCase("")) {
                    demo = dataDemo;
                    break;
                }
            }
        } else {
            String dataDemo = Utilidades.claveValor("demo", adicional);
            if (!dataDemo.equalsIgnoreCase("")) {
                demo = dataDemo;
            }
        }

        return demo;
    }

    public String consolidarOtrasPromociones(String otrasPromociones) {
        String promociones = "";

        // promocionesAdicionales.clear();
        promocionesAdicionales = cotizacion.getItemPromocionesAdicionales();

        int contador = 0;

        if (promocionesAdicionales != null) {

            for (int i = 0; i < promocionesAdicionales.size(); i++) {

                if (!promocionesAdicionales.get(i).getAdicional().equalsIgnoreCase("Vacia")) {
                    if (!promocionesAdicionales.get(i).getMeses().equalsIgnoreCase("0 Meses")
                            && cotizacion.isAplicarAd()) {
                        if (contador == 0) {
                            promociones = promociones + promocionesAdicionales.get(i).getAdicional() + ","
                                    + promocionesAdicionales.get(i).getMeses() + " al "
                                    + promocionesAdicionales.get(i).getDescuento() + "%" + ",N/A";
                            contador++;
                        } else {
                            promociones = promociones + "|" + promocionesAdicionales.get(i).getAdicional() + ","
                                    + promocionesAdicionales.get(i).getMeses() + " al "
                                    + promocionesAdicionales.get(i).getDescuento() + "%" + ",N/A";
                            contador++;
                        }
                    }

                }

            }
        }

		/*
		 * System.out.println("promociones " + promociones + "contador " +
		 * contador + " otrasPromociones " + otrasPromociones);
		 */

        if (!otrasPromociones.equalsIgnoreCase("")) {
            if (contador > 0) {
                promociones = promociones + "|" + otrasPromociones;
            } else {
                promociones = otrasPromociones;
            }
        }

        return promociones;
    }

    OnDismissListener dl = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                franja = dialogo.getFranja();

                System.out.println("franja " + franja);
                System.out.println("tipoDia " + tipoDia);
                System.out.println("dia " + dia);

                // System.out.println("fecha "+fecha +"franja "+franja
                // +" cuposAM "+cuposAM +" cuposPM "+cuposPM );
                if (franja.equalsIgnoreCase("AM") && Utilidades.parserDisponibilidad(cuposAM) > 0) {
                    lblAgendamiento.setText(fecha + " " + franja);
                    AgendaSiebel(franja);
                    validarAgendaDomingo();

                } else if (franja.equalsIgnoreCase("PM") && Utilidades.parserDisponibilidad(cuposPM) > 0) {
                    lblAgendamiento.setText(fecha + " " + franja);
                    AgendaSiebel(franja);
                    validarAgendaDomingo();

                } else {
                    lblAgendamiento.setText("");
                    Toast.makeText(context, "En la franja " + franja + " No hay Cupos", Toast.LENGTH_SHORT).show();
                }
                tabs.setCurrentTab(0);
            } else {
                // System.out.println("cancelado");
            }

        }
    };

    public void validarAgendaDomingo() {
        if (agendableDomingo.equalsIgnoreCase("1") && tipoDia.equalsIgnoreCase("D")) {
            agendaDomingo = true;
        }
    }

    public void AgendaSiebel(String Franja) {
        System.out.println("agendaSiebel " + agendaSiebel);
        String agenSiebel = "";
        JSONObject jsonObjec = new JSONObject();
        if (!agendaCompletaSiebel.equalsIgnoreCase("")) {
            try {
                JSONArray agendas = new JSONArray(agendaCompletaSiebel);
                for (int i = 0; i < agendas.length(); i++) {
                    JSONObject siebel = agendas.getJSONObject(i);
                    if (siebel.getString("tipoFranja").equalsIgnoreCase(Franja)) {
                        agenSiebel = siebel.toString();
                        agendaSiebel = new JSONObject();
                        agendaSiebel.put("idAgenda", idAgenda);
                        agendaSiebel.put("idOferta", idOferta);
                        agendaSiebel.put("agenda", siebel);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    OnDismissListener dls = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                finish();
            }
            dialogo.setSeleccion(false);
        }
    };

    @Override
    public void addObserver(Observer o) {
        // TODO Auto-generated method stub
        // System.out.println("Agregar Observador Control Venta");
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
        // System.out.println(observador);
        observador.update("Llamada");
    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub
        ArrayList<Object> resultado = (ArrayList<Object>) value;

        System.out.println("******" + resultado + "********");

        if (resultado.get(0).equals("InsertarConfirmacion")) {
            JSONObject jop;

            try {
                jop = new JSONObject(resultado.get(1).toString());

                System.out.println("**** jop ***" + jop);

                String data = jop.get("data").toString();

                if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                    data = new String(Base64.decode(data));
                    // Confirmacion(data);
                    JSONObject confirmacion = new JSONObject(data);

                    if (confirmacion.has("id")) {
                        id = confirmacion.getString("id");

                        if (!id.equalsIgnoreCase("")) {
                            // blindaje.idIVR = id;
                            //LanzarLLamada(id, "5", Mail, venta.getCobroDomingo());
                            LanzarLLamadaPorLlamadasMasivas(id);
                            // LLamadaUneMas = true;
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
        } else if (resultado.get(0).equals("LanzarLLamadaPorLlamadasMasivas")) {
            System.out.println("data LanzarLLamadaPorLlamadasMasivas" + resultado.get(1));

            try {
                JSONObject jop = new JSONObject(resultado.get(1).toString());
                if (jop.has("codigoMensaje") && jop.getString("codigoMensaje").equalsIgnoreCase("00")) {
                    mostrarDialgo();
                } else {
                    Toast.makeText(this, "EL lanzamiento de la llamada fallo", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

                        //LanzarLLamada(id, "5", Mail, venta.getCobroDomingo());

                    }
                } else {
                    System.out.println("Los datos no se descargaron correctamente");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (resultado.get(0).equals("ConsultarAgenda") || resultado.get(0).equals("ConsultarAgendaFenix")) {
            JSONObject jop;

            System.out.println("resultado ConsultarAgenda o  ConsultarAgendaFenix" + resultado.get(1).toString());

            try {

                microZona = "";

                final ArrayList<ListaAgendamiento> agenda = new ArrayList<ListaAgendamiento>();

                JSONArray ja = new JSONArray(resultado.get(1).toString());
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (!jo.getString("cuposam").equals("0") || !jo.getString("cupospm").equals("0")) {

                        if (!jo.getString("tipoDia").equalsIgnoreCase("D")) {
                            agenda.add(new ListaAgendamiento(jo.getString("fecha"), jo.getString("cuposam"),
                                    jo.getString("porcentageocuam"), jo.getString("cupospm"),
                                    jo.getString("porcentageocupm"), jo.getString("tipoDia"), jo.getString("dia")));
                        } else if (jo.getString("tipoDia").equalsIgnoreCase("D")
                                && !tipoPropiedad.equalsIgnoreCase("Conjunto Residencial")) {
                            agenda.add(new ListaAgendamiento(jo.getString("fecha"), jo.getString("cuposam"),
                                    jo.getString("porcentageocuam"), jo.getString("cupospm"),
                                    jo.getString("porcentageocupm"), jo.getString("tipoDia"), jo.getString("dia")));
                        }

                        if (jo.has("microZona")) {
                            microZona = jo.getString("microZona");
                        } else {
                            microZona = "";
                        }
                    }

                }

                ListaAgendamientoAdapter adaptador = new ListaAgendamientoAdapter(this, agenda, this);
                lstAgendamineto.setAdapter(adaptador);
                lstAgendamineto.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // ListaAgendamiento item = agenda.get(arg2);
                        fecha = agenda.get(arg2).getFecha();
                        cuposAM = agenda.get(arg2).getDisManana();
                        cuposPM = agenda.get(arg2).getDisTarde();
                        tipoDia = agenda.get(arg2).getTipoDia();
                        dia = agenda.get(arg2).getDia();
                        dialogo.dialogo.show();
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                Log.w("Error ", e.getMessage());
                microZona = "";
            }
        } else if (resultado.get(0).equals("ConsultarAgendaSiebel")) {
            JSONObject jop;

            boolean franjasPermitidas = false;

            try {
                final ArrayList<ListaAgendamiento> agenda = new ArrayList<ListaAgendamiento>();
                jop = new JSONObject(resultado.get(1).toString());

                if (jop.has("CodigoRespuesta")) {
                    if (jop.getString("CodigoRespuesta").equalsIgnoreCase("00")) {

                        System.out.println("jop agenda siebel " + jop);
                        idAgenda = jop.getString("idAgenda");
                        idOferta = jop.getString("idOferta");
                        JSONArray fechas = new JSONArray(jop.getString("fechas"));
                        for (int i = 0; i < fechas.length(); i++) {
                            JSONObject jo = fechas.getJSONObject(i);
                            String fecha = jo.getString("fecha");
                            JSONObject data = jo.getJSONObject("data");

                            if (data.has("Franja")) {
                                JSONArray franja = new JSONArray(data.getString("Franja"));
                                String cuposam = "0";
                                String porcentajeam = "100";
                                String cupospm = "0";
                                String porcentajepm = "100";
                                for (int j = 0; j < franja.length(); j++) {
                                    JSONObject jof = franja.getJSONObject(j);
                                    System.out.println("jof " + jof);
                                    if (jof.getString("tipoFranja").equalsIgnoreCase("AM")) {
                                        cuposam = "10";
                                        porcentajeam = "50";
                                        franjasPermitidas = true;
                                    } else if (jof.getString("tipoFranja").equalsIgnoreCase("PM")) {
                                        cupospm = "10";
                                        porcentajepm = "50";
                                        franjasPermitidas = true;
                                    }
                                }
                                agenda.add(new ListaAgendamiento(fecha, cuposam, porcentajeam, cupospm, porcentajepm,
                                        franja.toString()));
                            }
                        }

                        ListaAgendamientoAdapter adaptador = new ListaAgendamientoAdapter(this, agenda, this);
                        lstAgendamineto.setAdapter(adaptador);
                        lstAgendamineto.setOnItemClickListener(new OnItemClickListener() {
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                fecha = agenda.get(arg2).getFecha();
                                cuposAM = agenda.get(arg2).getDisManana();
                                cuposPM = agenda.get(arg2).getDisTarde();
                                agendaCompletaSiebel = agenda.get(arg2).getAgendaSiebel();
                                dialogo.dialogo.show();
                            }
                        });
                        if (!franjasPermitidas) {
                            Utilidades.MensajesToast("El aplicativo no arroja cupos en las franjas permitidas",
                                    context);
                        }

                    } else if (jop.getString("CodigoRespuesta").equalsIgnoreCase("01")) {

                        Utilidades.MensajesToast("Sin Agendas", context);
                        limpiarAgenda();

                    } else if (jop.getString("CodigoRespuesta").equalsIgnoreCase("-1")) {

                        Utilidades.MensajesToast("Problemas Con la consulta intente mas tardes", context);
                        limpiarAgenda();

                    } else {
                        Utilidades.MensajesToast("Problemas Procesando el Resultado", context);
                        limpiarAgenda();
                    }
                } else {
                    Utilidades.MensajesToast("Problemas Procesando el Resultado", context);
                    limpiarAgenda();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (resultado != null && resultado.get(0).equals("ValidacionConfiguracionMovil")) {

            try {

                JSONObject jop = new JSONObject(resultado.get(1).toString());
                String data = jop.get("data").toString();

                if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {

                    data = new String(Base64.decode(data));
                    // Confirmacion(data);
                    JSONObject validacion = new JSONObject(data);
                    Toast.makeText(MainActivity.context, "Datos Invalidos", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.MODULO_MENSAJES);
                    intent.putExtra("mensajes", Interprete.mensajesConfiguracion(validacion).toString());
                    startActivityForResult(intent, MainActivity.REQUEST_CODE);

                }
            } catch (JSONException e) {
                Log.w("Error JSONException ", e.getMessage());
            }

        }
    }

    public void limpiarAgenda() {
        ArrayList<ListaAgendamiento> limpiar = new ArrayList<ListaAgendamiento>();
        limpiar.clear();
        ListaAgendamientoAdapter adaptador = new ListaAgendamientoAdapter(this, limpiar, this);
        lstAgendamineto.setAdapter(adaptador);
    }

    public ArrayList<String[]> promocionesAdIndividuales(String producto, String empaquetamiento, String adicional,
                                                         String estrato) {

        ArrayList<String[]> arrayPromo = new ArrayList<String[]>();
        arrayPromo.clear();

        String desAdicional = Utilidades.claveValor(producto, empaquetamiento);

        System.out.println(desAdicional);

        if (desAdicional.equalsIgnoreCase(adicional)) {

            String promoTV = Utilidades.listaEstratos(desAdicional, estrato);

            System.out.println(promoTV);

            if (!promoTV.equalsIgnoreCase("")) {
                arrayPromo = promoTV(promoTV);
            }
        }

        return arrayPromo;

    }

    public ArrayList<String[]> promoTV(String promoTV) {

        ArrayList<String[]> arrayPromo = new ArrayList<String[]>();
        arrayPromo.clear();

        String tipoAdicional = null;
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_clave,lst_valor"}, "lst_nombre=?", new String[]{promoTV}, null, null, null);

        System.out.println(respuesta);

        if (respuesta != null) {
            String porcentaje = respuesta.get(0).get(0);
            String tiempo = respuesta.get(0).get(1);
            System.out.println("porcentaje " + porcentaje + " tiempo " + tiempo);
            arrayPromo.add(new String[]{porcentaje, tiempo});
        }

        return arrayPromo;

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    OnCheckedChangeListener eventoAgendaDomingo = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                agendableDomingo = "1";
            } else {
                agendableDomingo = "0";
                lblAgendamiento.setText("");
                agendaDomingo = false;
            }
        }
    };
}