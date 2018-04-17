package co.com.une.appmovilesune.change;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.kobjects.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Config;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemCoberturaGpon;
import co.com.une.appmovilesune.adapters.ItemDirecciones;
import co.com.une.appmovilesune.adapters.ListaCoberturaGponAdapter;
import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.adapters.ListaDefaultAdapter;
import co.com.une.appmovilesune.complements.Calendario;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Domicilio;
import co.com.une.appmovilesune.model.Paquete;
import co.com.une.appmovilesune.model.PortafolioUNE;
import co.com.une.appmovilesune.model.Producto;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.Simulador;

import com.google.analytics.tracking.android.EasyTracker;

//INICIO CLASE ACTIVITY OBSEQUIOS
public class ControlSimulador extends Activity implements Observer, TextWatcher {
    /**
     * Called when the activity is first created.
     */

    // INICIO DE LAS VARIBLES DE LA CLASE ACTUALIZAR DATOS
    private Button Configurar;

    private ListaDefaultAdapter adapterCobertura;
    private ListaDefaultAdapter adapterPortafolio;
    private ListaDefaultAdapter adapterFactura;
    private ListaDefaultAdapter adapterBlindaje;
    private ListaDefaultAdapter adapterScooring;
    // private ListaDefaultAdapter adapterCobertura;
    ArrayList<ListaDefault> cobertura = new ArrayList<ListaDefault>();
    ArrayList<ListaDefault> cartera = new ArrayList<ListaDefault>();
    ArrayList<ListaDefault> portafolio = new ArrayList<ListaDefault>();
    ArrayList<ListaDefault> factura = new ArrayList<ListaDefault>();
    ArrayList<ItemDirecciones> direcciones = new ArrayList<ItemDirecciones>();
    ArrayList<Ofertas> ofertas = new ArrayList<Ofertas>();
    ArrayList<Ofertas> selfofertas = new ArrayList<Ofertas>();
    ArrayList<ListaDefault> blindaje = new ArrayList<ListaDefault>();
    ArrayList<ListaDefault> listascooring = new ArrayList<ListaDefault>();

    ListView lvc, lvp, lvped, lvf, lvcifin, lvblin, lvscooring;

    // se declaran los objetos webservices,consulta,almacenar y enviar
    // alamcenado

    protected static final int REQUEST_CODE = 10;
    private static final int OK_RESULT_CODE = 1;

    private boolean control_portafolio = false;

    private String Canal = "", Codigo_usuario = "", Departamento = "", Ciudad = "", Telefono = "", Celular = "",
            Direccion = "", Cedula = "", Codigo_Ciudad = "", CiudadxCedula = "", Codigo_Giis = "", Nombre = "",
            Estrato = "", Paginacion = "", Identificador = "", Fuente = "", Individual = "", Empaquetado = "";

    public TextView viewTelefonoCobertura, viewDireccionCobertura, viewCiudadCobertura, view_Coberturatitulo,
            viewTelefonoPortafolio, viewDireccionPortafolio, viewCiudadPortafolio, view_Portafoliotitulo,
            viewbusquedaBlindaje;
    private RadioButton radio_direccion_Cobertura, radio_telefono_Cobertura, radio_cedula_Cobertura,
            radio_direccion_Portafolio, radio_telefono_Portafolio, radio_cedula_Portafolio, radio_gpon_cobertura;
    private TableRow TableCedulaCobertura, TableDireccionesCobertura, TableTelefonoCobertura, TableDireccionCobertura,
            TableCedulaPortafolio, TableDireccionesPortafolio, TableTelefonoPortafolio, TableDireccionPortafolio,
            TableNodos, TableGpon;

    private TableLayout TableCartera;

    private EditText Cedula_Simulador;
    private Spinner Direcciones_Cobertura, Direcciones_Portafolio;

    private TextView Vigencia, ViewNombre, Estado, Accion, txtOrigen;

    private Item Avanzar;

    static final int DATE_DIALOG_ID = 0;

    private static final int DIALOGO_ALERTA = 1;

    private String Mensaje;

    private String Control_AsyncTask = "";

    ArrayList<String> Datos = new ArrayList<String>();

    private ProgressDialog pd = null;

    String[] ArrayDirecciones = null;

    private String Fecha_I, Hora_I;

    private TituloPrincipal tp;
    private Busqueda buscar, buscob, buspor, busfac, busnod, busblin;

    private TabHost tabs;

    private String posicion;
    private Context context;

    private String Validacion_Cartera = "", Mensaje_Cartera = "";

    LinearLayout LayoutFactura, LayoutBlindajeAntioquia;

    private Cliente cliente;
    private Scooring scooring;
    private String tipoBusquedaBlindaje = "";

    private ArrayList<Producto> productos;
    private ArrayList<Paquete> paquetes;
    private ArrayList<Domicilio> domicilios;
    private boolean validacionCliente = false;
    private boolean visibleBlindaje = false;

    private RadioGroup groupBlindaje;

    private AutoCompleteTextView txtGpon;

    private boolean validarScooring = false;
    private boolean apruebaScooring = false;
    private boolean regresar = true;
    private String idScooring = "";

    private long fecha1 = 0;

    private String idIVR = "";
    private int codigoIVR;
    private int confirmacionCifin = 0;

    private boolean bloqueo = false;

    // METODO PRINCIPAL DE LA ACTIVIDAD
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simulador);

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {

            cliente = (Cliente) reicieveParams.getSerializable("cliente");
            scooring = (Scooring) reicieveParams.getSerializable("scooring");
            validacionCliente = true;

        } else {
            // System.out.println("no cliente");
        }

        if (scooring == null) {
            scooring = new Scooring();
        }

        TableCedulaCobertura = (TableRow) findViewById(R.id.campoCedulaCobertura);
        TableDireccionesCobertura = (TableRow) findViewById(R.id.campoDireccionesCobertura);
        TableTelefonoCobertura = (TableRow) findViewById(R.id.campoTelefonoCobertura);
        TableDireccionCobertura = (TableRow) findViewById(R.id.campoDireccionCobertura);
        TableCedulaPortafolio = (TableRow) findViewById(R.id.campoCedulaPortafolio);
        TableDireccionesPortafolio = (TableRow) findViewById(R.id.campoDireccionesPortafolio);
        TableTelefonoPortafolio = (TableRow) findViewById(R.id.campoTelefonoPortafolio);
        TableDireccionPortafolio = (TableRow) findViewById(R.id.campoDireccionPortafolio);
        TableNodos = (TableRow) findViewById(R.id.campoNodosCobertura);
        TableGpon = (TableRow) findViewById(R.id.campoGponCobertura);
        viewTelefonoCobertura = (TextView) findViewById(R.id.viewTelefonoCobertura);
        viewDireccionCobertura = (TextView) findViewById(R.id.viewDireccionCobertura);
        viewCiudadCobertura = (TextView) findViewById(R.id.viewCiudadCobertura);
        viewTelefonoPortafolio = (TextView) findViewById(R.id.viewTelefonoPortafolio);
        viewDireccionPortafolio = (TextView) findViewById(R.id.viewDireccionPortafolio);
        viewCiudadPortafolio = (TextView) findViewById(R.id.viewCiudadPortafolio);
        Direcciones_Cobertura = (Spinner) findViewById(R.id.Direcciones_Cobertura);
        Direcciones_Portafolio = (Spinner) findViewById(R.id.Direcciones_Portafolio);
        radio_telefono_Cobertura = (RadioButton) findViewById(R.id.radio_telefono_Cobertura);
        radio_direccion_Cobertura = (RadioButton) findViewById(R.id.radio_direccion_Cobertura);
        radio_cedula_Cobertura = (RadioButton) findViewById(R.id.radio_Cedula_Cobertura);
        radio_telefono_Portafolio = (RadioButton) findViewById(R.id.radio_telefono_Portafolio);
        radio_direccion_Portafolio = (RadioButton) findViewById(R.id.radio_direccion_Portafolio);
        radio_cedula_Portafolio = (RadioButton) findViewById(R.id.radio_Cedula_Portafolio);
        radio_gpon_cobertura = (RadioButton) findViewById(R.id.radio_gpon_Cobertura);

        lvscooring = (ListView) findViewById(R.id.viewScooring);

        TableCartera = (TableLayout) findViewById(R.id.TableCartera);

        // Parte de Blindaje Med
        viewbusquedaBlindaje = (TextView) findViewById(R.id.viewbusquedaBlindaje);
        groupBlindaje = (RadioGroup) findViewById(R.id.radioGroupRespuestasBlindaje);

        if (Utilidades.visible("gpon", cliente.getCiudad())) {
            txtGpon = (AutoCompleteTextView) findViewById(R.id.txtGpon);
            txtGpon.setThreshold(4);
            txtGpon.setAdapter(obtenerAdaptador());
            txtGpon.setTextColor(getResources().getColor(R.color.black));

            radio_gpon_cobertura.setVisibility(View.VISIBLE);
        }

        System.out.println("cliente.getPortafolio() " + cliente.getPortafolio());

        context = this;

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("Factura");

        ViewNombre = (TextView) findViewById(R.id.txtNombre);
        Estado = (TextView) findViewById(R.id.txtResultado);
        Accion = (TextView) findViewById(R.id.txtAccion);
        txtOrigen = (TextView) findViewById(R.id.txtOrigen);

        LayoutFactura = (LinearLayout) findViewById(R.id.Factura);
        LayoutBlindajeAntioquia = (LinearLayout) findViewById(R.id.layoutBlindajeAntioquia);

        buscar = (Busqueda) findViewById(R.id.busquedaCartera);
        buscar.setEditable(false);

        buscar.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) { // Resultado_Cartera();

                idIVR = "";

                int deshabilitarIvr = Utilidades.convertirNumericos(Utilidades.camposUnicos("deshabilitarIvr"),
                        "deshabilitarIvr");

                if (Utilidades.validarTelefono(cliente.getTelefonoDestino())) {
                    if (Utilidades.excluir("habilitarScooring", cliente.getCiudad())
                            && Utilidades.excluirNacional("pruebasScoring", cliente.getCedula())) {
                        scooringPrueba(cliente.getCedula());
                        cliente.setRealizoConfronta(true);
                        cliente.setConfronta(true);
                    } else if (!cliente.getTipoCuenta().equalsIgnoreCase("")) {
                        if(cliente.getScooringune()!= null && !cliente.getScooringune().getIdScooring().equalsIgnoreCase("")){
                            if(idScooring.equalsIgnoreCase("")){
                                if(cliente.getScooringune().getDocumentoScooring().equalsIgnoreCase(cliente.getCedula())){
                                    idScooring = cliente.getScooringune().getIdScooring();
                                    TableCartera.setVisibility(View.GONE);
                                    lvscooring.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        if (!idScooring.equalsIgnoreCase("")) {
                            if (Utilidades.validarFechaConsuta(fecha1, Calendario.getTimestamp(), context)) {
                                ResultadoScooring(idScooring);
                            }
                        } else {
                            IngresarEstadoCuenta(deshabilitarIvr);
                        }
                    }else{
                        IngresarEstadoCuenta(deshabilitarIvr);
                        //IngresarEstadoCuenta(deshabilitarIvr);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.ingresetelefono),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        busfac = (Busqueda) findViewById(R.id.busquedaFactura);
        busfac.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

            }
        });

        buscob = (Busqueda) findViewById(R.id.busquedaCobertura);
        buscob.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (cliente.getCedula() != null && !cliente.getCedula().equals("")) {
                    DireccionxCedula(buscob.getBusqueda());
                } else {
                    cliente.setCedula(buscob.getBusqueda());
                    DireccionxCedula(buscob.getBusqueda());
                }
            }
        });

        buspor = (Busqueda) findViewById(R.id.busquedaPortafolio);
        buspor.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (cliente.getCedula() != null && !cliente.getCedula().equals("")) {
                    DireccionxCedula(buspor.getBusqueda());
                } else {
                    cliente.setCedula(buspor.getBusqueda());
                    DireccionxCedula(buspor.getBusqueda());
                }
            }
        });

        busblin = (Busqueda) findViewById(R.id.busquedaBlindaje);
        busblin.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (!tipoBusquedaBlindaje.equalsIgnoreCase("") && !busblin.getBusqueda().equalsIgnoreCase("")) {
                    BuscarBlindaje(tipoBusquedaBlindaje, busblin.getBusqueda());
                }
            }
        });

        if (MainActivity.config.getDepartamento().equals("Bogota")
                || MainActivity.config.getDepartamento().equals("Atlantico")
                || MainActivity.config.getDepartamento().equals("Valle")
                || MainActivity.config.getDepartamento().equals("Bolivar")) {
            // Encuentro la vista del boton
            busnod = (Busqueda) findViewById(R.id.busquedaNodosCobertura);

            busnod.boton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    ConsultaNodos(busnod.getBusqueda());
                }
            });
        } else {

            TableNodos.setVisibility(View.GONE);
        }

        lvc = (ListView) findViewById(R.id.viewCobertura);
        lvp = (ListView) findViewById(R.id.viewPortafolio);
        lvf = (ListView) findViewById(R.id.viewFactura);
        lvblin = (ListView) findViewById(R.id.viewListBlindaje);

        Resources res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("Cartera");
        spec.setContent(R.id.Cartera);
        spec.setIndicator("Estado Cuenta", res.getDrawable(R.drawable.cartera));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Cobertura");
        spec.setContent(R.id.Cobertura);//
        spec.setIndicator("Cobertura", res.getDrawable(R.drawable.cobertura));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Portafolio");
        spec.setContent(R.id.Portafolio);//
        spec.setIndicator("Portafolio", res.getDrawable(R.drawable.portafolio));
        tabs.addTab(spec);

        if (Utilidades.visible("BlindajeNacional", cliente.getCiudad())) {
            spec = tabs.newTabSpec("Blindaje");
            spec.setContent(R.id.BlindajeAntioquia);//
            spec.setIndicator("Blindaje", res.getDrawable(R.drawable.portafolio));
            tabs.addTab(spec);
            LayoutBlindajeAntioquia.setVisibility(View.VISIBLE);

        }

        LayoutFactura.setVisibility(View.GONE);

        tabs.setCurrentTab(0);

        if (validacionCliente) {
            if (cliente.getCarteraUNE() != null) {
                Resultado_Cartera(cliente.getCarteraUNE());
            }

            if (cliente.getPortafolio() != null) {
                Resultado_Portafolio(cliente.getPortafolio());
            }
            if (cliente.getConsolidado() != null) {
                if (Utilidades.excluir("MunicipiosSiebel", cliente.getCiudad())) {
                    Resultado_Consolidado(cliente.getConsolidado());
                }
            }

            /*if (cliente.getPortafolioElite() != null) {
                if (Utilidades.excluir("eliteMunicipios", cliente.getCiudad())) {
                    //Resultado_ConsolidadoElite(cliente.getPortafolioElite());
                    Resultado_ConsolidadoPortafolioElite(cliente.getPortafolioUNE());
                }
            }*/

            if (cliente.getPortafolioUNE() != null) {
                if (Utilidades.excluir("eliteMunicipios", cliente.getCiudad())) {
                    //Resultado_ConsolidadoElite(cliente.getPortafolioElite());
                    Resultado_ConsolidadoPortafolioElite(cliente.getPortafolioUNE());
                }
            }

            if (cliente.getCobertura() != null) {
                Resultado_Cobertura(cliente.getCobertura());
            }

            if (MainActivity.ccfg.getLanzador().equals("CuentasUNE")) {
                tabs.setCurrentTab(0);
            } else if (MainActivity.ccfg.getLanzador().equals("Cobertura")) {
                tabs.setCurrentTab(1);
            } else if (MainActivity.ccfg.getLanzador().equals("Portafolio")) {
                tabs.setCurrentTab(2);
            }
            tabs.getTabWidget().getChildAt(tabs.getCurrentTab())
                    .setBackgroundColor(getResources().getColor(R.color.yellow));
            TextView tv = (TextView) tabs.getCurrentTabView().findViewById(android.R.id.title); // for
            // Selected
            // Tab
            tv.setTextColor(getResources().getColor(R.color.darkTab));
        }

        tabs.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if (tabId.equals("Factura")) {
                    tp.setTitulo("Factura");
                } else if (tabId.equals("Cartera")) {
                    tp.setTitulo("Cartera");
                } else if (tabId.equals("Cobertura")) {
                    tp.setTitulo("Cobertura");
                } else if (tabId.equals("Portafolio")) {
                    tp.setTitulo("Portafolio");
                } else if (tabId.equals("Blindaje")) {
                    tp.setTitulo("Blindaje Nacional");
                } else {
                    Log.i("AndroidTabsDemo", "Pulsada pestana: " + tabId);
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

        if (cliente.getTelefono().equalsIgnoreCase("")) {
            radio_telefono_Cobertura.setVisibility(View.GONE);
            radio_telefono_Portafolio.setVisibility(View.GONE);
            TableTelefonoCobertura.setVisibility(View.GONE);
            TableTelefonoPortafolio.setVisibility(View.GONE);
        }

        if (cliente.getDireccion().equalsIgnoreCase("")) {
            radio_direccion_Cobertura.setVisibility(View.GONE);
            radio_direccion_Portafolio.setVisibility(View.GONE);
            TableDireccionCobertura.setVisibility(View.GONE);
            TableDireccionPortafolio.setVisibility(View.GONE);
        }

        // System.out.println("Cedula " + Cedula);

        if (!cliente.getCedula().equalsIgnoreCase("")) {
            setLlenarCedula(cliente.getCedula());
        }

        if (!cliente.getTelefono().equalsIgnoreCase("")) {
            setLlenarTelefonia(cliente.getTelefono());
        }

        RadioButton.OnClickListener eventoRadiosCobertura = new RadioButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (radio_telefono_Cobertura.isChecked() || radio_direccion_Cobertura.isChecked()) {
                    TableCedulaCobertura.setVisibility(View.GONE);
                    TableDireccionesCobertura.setVisibility(View.GONE);
                    TableGpon.setVisibility(View.GONE);
                    TableTelefonoCobertura.setVisibility(View.VISIBLE);
                    TableDireccionCobertura.setVisibility(View.VISIBLE);
                    if (!Telefono.equalsIgnoreCase("")) {
                        TableTelefonoCobertura.setVisibility(View.VISIBLE);
                    }
                    if (!Direccion.equalsIgnoreCase("")) {
                        TableDireccionCobertura.setVisibility(View.VISIBLE);
                    }
                } else if (radio_cedula_Cobertura.isChecked()) {
                    TableTelefonoCobertura.setVisibility(View.GONE);
                    TableDireccionCobertura.setVisibility(View.GONE);
                    TableGpon.setVisibility(View.GONE);
                    // if(!MainActivity.config.getDepartamento().equals("BogotaHFC")
                    // ||
                    // !MainActivity.config.getDepartamento().equals("Atlantico")
                    // || !MainActivity.config.getDepartamento().equals("Valle")
                    // ||
                    // !MainActivity.config.getDepartamento().equals("Bolivar")){
                    TableCedulaCobertura.setVisibility(View.VISIBLE);
                    TableDireccionesCobertura.setVisibility(View.VISIBLE);
                    // }
                } else if (radio_gpon_cobertura.isChecked()) {
                    TableCedulaCobertura.setVisibility(View.GONE);
                    TableDireccionesCobertura.setVisibility(View.GONE);
                    TableTelefonoCobertura.setVisibility(View.GONE);
                    TableDireccionCobertura.setVisibility(View.GONE);
                    TableGpon.setVisibility(View.VISIBLE);
                }

            }

        };

        RadioButton.OnClickListener eventoRadiosPortafolio = new RadioButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (radio_telefono_Portafolio.isChecked() || radio_direccion_Portafolio.isChecked()) {
                    TableCedulaPortafolio.setVisibility(View.GONE);
                    TableDireccionesPortafolio.setVisibility(View.GONE);
                    TableTelefonoPortafolio.setVisibility(View.VISIBLE);
                    TableDireccionPortafolio.setVisibility(View.VISIBLE);
                    if (!Telefono.equalsIgnoreCase("")) {
                        TableTelefonoPortafolio.setVisibility(View.VISIBLE);
                    }
                    if (!Direccion.equalsIgnoreCase("")) {
                        TableDireccionPortafolio.setVisibility(View.VISIBLE);
                    }
                } else if (radio_cedula_Portafolio.isChecked()) {
                    TableTelefonoPortafolio.setVisibility(View.GONE);
                    TableDireccionPortafolio.setVisibility(View.GONE);
                    TableCedulaPortafolio.setVisibility(View.VISIBLE);
                    TableDireccionesPortafolio.setVisibility(View.VISIBLE);
                }

            }

        };

        groupBlindaje.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.radio_telefono_Blindaje) {

                    busblin.setBusqueda(cliente.getTelefono());
                    viewbusquedaBlindaje.setText(getResources().getString(R.string.telefono));

                    tipoBusquedaBlindaje = "identificador";
                } else if (checkedId == R.id.radio_codigoHogar_Blindaje) {
                    viewbusquedaBlindaje.setText(getResources().getString(R.string.codigo_hogar));
                    busblin.setBusqueda("");
                    tipoBusquedaBlindaje = "codigoHogar";
                } else if (checkedId == R.id.radio_Cedula_Blindaje) {
                    tipoBusquedaBlindaje = "documento";
                    viewbusquedaBlindaje.setText("Documento");
                    busblin.setBusqueda(cliente.getCedula());
                }

            }

        });

        radio_telefono_Cobertura.setOnClickListener(eventoRadiosCobertura);
        radio_direccion_Cobertura.setOnClickListener(eventoRadiosCobertura);
        radio_cedula_Cobertura.setOnClickListener(eventoRadiosCobertura);
        radio_gpon_cobertura.setOnClickListener(eventoRadiosCobertura);

        radio_telefono_Portafolio.setOnClickListener(eventoRadiosPortafolio);
        radio_direccion_Portafolio.setOnClickListener(eventoRadiosPortafolio);
        radio_cedula_Portafolio.setOnClickListener(eventoRadiosPortafolio);

        if (cliente.getTelefono().equalsIgnoreCase("")) {
            radio_telefono_Cobertura.setEnabled(false);
            radio_telefono_Portafolio.setEnabled(false);
        }

        if (cliente.getDireccion().equalsIgnoreCase("")) {
            radio_direccion_Cobertura.setEnabled(false);
            radio_direccion_Portafolio.setEnabled(false);
        }

        viewTelefonoCobertura.setText(cliente.getTelefono());
        viewDireccionCobertura.setText(cliente.getDireccion());
        viewCiudadCobertura.setText(cliente.getCiudad());

        viewTelefonoPortafolio.setText(cliente.getTelefono());
        viewDireccionPortafolio.setText(cliente.getDireccion());
        viewCiudadPortafolio.setText(cliente.getCiudad());

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

    public ListaCoberturaGponAdapter obtenerAdaptador() {
        ArrayList<ArrayList<String>> direcciones = MainActivity.basedatos.consultar(false, "gpon",
                new String[]{"gpn_inicial", "gpn_final"}, null, null, null, null, null);

        if (direcciones != null) {
            ArrayList<ItemCoberturaGpon> direccionInicial = new ArrayList<ItemCoberturaGpon>();
            for (int i = 0; i < direcciones.size(); i++) {
                direccionInicial.add(new ItemCoberturaGpon(direcciones.get(i).get(0), direcciones.get(i).get(1)));
            }
            System.out.println("direccionInicial " + direccionInicial);

            ListaCoberturaGponAdapter adaptador = new ListaCoberturaGponAdapter(this, R.layout.itemgpon,
                    direccionInicial, this);
            // ArrayAdapter<String> adaptador = new
            // ArrayAdapter<String>(,android.R.layout.simple_spinner_item, dat);
            return adaptador;
        } else {
            return null;
        }
    }

    public void setLlenarCedula(String Cedula) {
        buscar.setBusqueda(Cedula);
        buscob.setBusqueda(Cedula);
        buspor.setBusqueda(Cedula);
    }

    public void setLlenarTelefonia(String Telefono) {
        busfac.setBusqueda(Telefono);
    }

    public void Resultado_Factura(String Resultado) {
        Borrar("Factura");
        factura = Interprete.Factura(Resultado);

        if (factura.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, cartera.get(0).getDato(), Toast.LENGTH_SHORT).show();
        } else if (factura.get(0).getTitulo().equalsIgnoreCase("SI")) {

            factura.remove(0);
            adapterFactura = new ListaDefaultAdapter(this, factura, this);
            lvf.setAdapter(adapterFactura);
            setListViewHeightBasedOnChildren(lvf);
        }
    }

    public void Resultado_Cartera(String Resultado) {

        cartera = Interprete.Cartera(Resultado);

        if (cartera.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, cartera.get(0).getDato(), Toast.LENGTH_SHORT).show();
        } else if (cartera.get(0).getTitulo().equalsIgnoreCase("SI")) {

            ViewNombre.setText(cartera.get(1).getDato());
            Estado.setText(cartera.get(2).getDato());
            Accion.setText(cartera.get(3).getDato());
            txtOrigen.setText(cartera.get(4).getDato());

            Validacion_Cartera = cartera.get(3).getDato();
            Mensaje_Cartera = cartera.get(3).getDato();

            scooring.setCarteraUNE(cartera.get(1).getDato(), cartera.get(2).getDato(), cartera.get(3).getDato(),
                    cartera.get(4).getDato(), cartera.get(6).getDato());

        }
    }

    public void DireccionxCedula(String Cedula) {
        // System.out.println("Control Simulador 479 => "+Cedula);
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(Cedula);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("Direcciones");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void ConsultaNodos(String nodo) {
        // System.out.println("Control Simulador 479 => "+Nodos);
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(nodo);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ConsultaNodos");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void CuentasUNE() {
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(MainActivity.config.getCodigo());
        parametros.add(buscar.getBusqueda());

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("Cartera_UNE");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void scooringPrueba(String documento) {
        ClasificarEstadoCuenta(Utilidades.pruebasScooring(documento));
    }

    public void IngresarEstadoCuenta(int ValidarCliente) {

        boolean validarConsulta = true;
        cliente.setControlEstadoCuenta(false);

        JSONObject jo = new JSONObject();

        try {

            if (!cliente.getTipoDocumento().equalsIgnoreCase("")
                    && !cliente.getTipoDocumento().equalsIgnoreCase(Utilidades.inicial_opcion)
                    && !cliente.getTipoDocumento().equalsIgnoreCase("N/A")
                    && !cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)
                    && !cliente.getEstrato().equalsIgnoreCase("") && !cliente.getCedula().equalsIgnoreCase("")) {
                if (Utilidades.tamanoCedula(cliente.getCedula(), cliente.getTipoDocumento())) {
                    if (!Utilidades.ListaNegraCedula(cliente.getCedula())) {
                        jo.put("strCodigoAsesor", MainActivity.config.getCodigo_asesor());
                        jo.put("strCedula", buscar.getBusqueda());
                        jo.put("strTipoDocumento", cliente.getTipoDocumento());
                        jo.put("strDepartamento", cliente.getDepartamento());
                        jo.put("strCiudad", cliente.getCiudad());
                        jo.put("strEstrato", cliente.getEstrato());
                        jo.put("strDireccion", cliente.getDireccion());
                        jo.put("strValidarCliente", ValidarCliente);
                        jo.put("strMedioIngreso", "VM");

                        EstadoCuenta(jo.toString());

                    } else {

                        Utilidades.MensajesToast(getResources().getString(R.string.listaNegraDocumento), this);
                    }
                } else {
                    Utilidades.MensajesToast(getResources().getString(R.string.tamanoDocumento), this);
                }

            } else {
                Utilidades.MensajesToast(
                        "Los Campos Tipo Documento, Documento Y Estrato Siempre Deben Estar Diligenciados Para Realizar La Consulta.",
                        context);
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public void ResultadoScooring(String id) {
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(MainActivity.config.getCodigo());
        parametros.add(id);

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ResultScooring");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
        // ValidarResultadoScooring(Utilidades.PositivaScooring());
    }

    public void EstadoCuenta(String data) {
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(MainActivity.config.getCodigo());
        parametros.add(data);

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("EstadoCuenta");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void Scooring(String data) {
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(MainActivity.config.getCodigo());
        parametros.add(data);

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("DatosScooring");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void BuscarBlindaje(String Tipo, String Busqueda) {
        System.out.println("Tipo " + Tipo + " Busqueda " + Busqueda);
        System.out.println("Control Simulador 479 => ");
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(Tipo);
        parametros.add(Busqueda);
        parametros.add(MainActivity.config.getCodigo());
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("BuscarBlindaje");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void LLenar_Direcciones(String Resultado) {
        direcciones = Interprete.Direcciones(Resultado);
        if (Resultado.equals("Consulta Hecha fuera del horario establecido")
                || Resultado.equals("Ha superado el limite de consultas por dia")) {
            Toast.makeText(this, Resultado, Toast.LENGTH_SHORT).show();
        } else if (direcciones.get(0).getDireccion().equalsIgnoreCase("NO")) {
            Toast.makeText(this, direcciones.get(0).getDireccion(), Toast.LENGTH_SHORT).show();
            Borrar_Spinner();
        } else if (direcciones.get(0).getDireccion().equalsIgnoreCase("SI")) {
            // System.out.println("Entro a direcciones");
            direcciones.remove(0);

            if (direcciones.size() > 0) {
                ArrayDirecciones = new String[direcciones.size()];
                for (int i = 0; i < ArrayDirecciones.length; i++) {
                    ArrayDirecciones[i] = direcciones.get(i).getDireccion().toString();
                }
            }

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    ArrayDirecciones);
            adaptador.setDropDownViewResource(R.layout.list_radio);

            Direcciones_Cobertura.setAdapter(adaptador);
            Direcciones_Cobertura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                    CiudadxCedula = direcciones.get(position).getCiudad();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            Direcciones_Portafolio.setAdapter(adaptador);
            Direcciones_Portafolio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                    CiudadxCedula = direcciones.get(position).getCiudad();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (tabs.getCurrentTabTag().equals("Cobertura")) {
                buspor.setBusqueda(buscob.getBusqueda());
            } else if (tabs.getCurrentTabTag().equals("Portafolio")) {
                buscob.setBusqueda(buspor.getBusqueda());
            }

        }
    }

    public void Resultado_Consolidado(String Resultado) {
        System.out.println("Consolidado 28-02-2014 " + Resultado);
        Borrar("Portafolio");
        System.out.println("tamano portafolio " + portafolio.size());
        portafolio = Interprete.PortafolioNuevo(Resultado);
        System.out.println("portafolio " + portafolio);
        PintarPortafolioNuevo();
        Borrar("Cobertura");
        System.out.println("tamano  cobertura " + cobertura.size());
        cobertura = Interprete.CoberturaNuevo(Resultado);
        System.out.println("cobertura " + cobertura);
        PintarCoberturaNuevo();
    }

    public void Resultado_ConsolidadoElite(String Resultado) {
        System.out.println("Consolidado Elite 5-12-2017 " + Resultado);
        Borrar("Portafolio");
        if(Resultado != null && !Resultado.equalsIgnoreCase("")) {
            System.out.println("tamano portafolio " + portafolio.size());
            portafolio = InterpreteElite.PortafolioNuevo(Resultado);
            System.out.println("portafolio " + portafolio);
            PintarPortafolioNuevo();
        }
    }

    public void Resultado_ConsolidadoPortafolioElite(PortafolioUNE portafolioUNE) {
        //System.out.println("Consolidado Elite 5-12-2017 " + Resultado);
        Borrar("Portafolio");
        if(portafolioUNE != null) {
            System.out.println("tamano portafolio " + portafolio.size());
            portafolio = InterpreteElite.PortafolioNuevoPaquetes(portafolioUNE);
            System.out.println("portafolio " + portafolio);
            PintarPortafolioNuevo();
        }
    }

    public void reorganizarClienteSiebel(String consolidado) {
        try {
            JSONObject jo = new JSONObject(consolidado);
            System.out.println("jo " + jo);
            System.out.println("Telefono " + Telefono);
            if (jo.has("CodigoMensaje")) {
                System.out.println("jo " + jo);
                if (jo.has("ListaDatosClienteVent")) {
                    JSONArray arrayJson = jo.getJSONArray("ListaDatosClienteVent");
                    MainActivity.btnCobertura.setOK();
                    System.out.println("arrayJson " + arrayJson);
                    if (arrayJson.length() > 0) {
                        JSONObject datacliente = arrayJson.getJSONObject(0);
                        cliente.setDireccion(datacliente.getString("Direccion"));

                        if (datacliente.has("Estrato")) {
                            if (!datacliente.getString("Estrato").equalsIgnoreCase("")
                                    && !datacliente.getString("Estrato").equalsIgnoreCase("null")
                                    && !datacliente.getString("Estrato").equalsIgnoreCase("0")) {

                                if (cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)) {
                                    cliente.setEstrato(datacliente.getString("Estrato"));
                                }

                            }
                        }

                        if (datacliente.has("Cliente_id")) {
                            if (cliente.getCedula().equalsIgnoreCase("")) {
                                cliente.setCedula(datacliente.getString("Cliente_id"));
                            }
                        }

                        if (datacliente.has("Nombres")) {
                            if (cliente.getNombre().equalsIgnoreCase("")) {
                                cliente.setNombre(datacliente.getString("Nombres"));
                            }
                        }

                        if (datacliente.has("Apellidos")) {
                            if (cliente.getApellido().equalsIgnoreCase("")) {
                                cliente.setApellido(datacliente.getString("Apellidos"));
                            }
                        }

                        if (datacliente.has("IdDireccionGis")) {
                            cliente.setIdDireccionGis(datacliente.getString("IdDireccionGis"));

                            if (jo.has("ListaResulInfraVent")) {
                                JSONObject ListaResulInfraVent = jo.getJSONObject("ListaResulInfraVent");

                                if (ListaResulInfraVent.has("georreferenciarCR")) {

                                    JSONObject georreferenciarCR = ListaResulInfraVent
                                            .getJSONObject("georreferenciarCR");

                                    System.out.println("Latitud " + georreferenciarCR.getString("Latitud"));
                                    System.out.println("Longitud " + georreferenciarCR.getString("Longitud"));

                                    if (!georreferenciarCR.getString("Latitud").equals("")) {
                                        cliente.setLatitud(georreferenciarCR.getString("Latitud"));
                                    } else {
                                        // validarIdDireccionGis = false;
                                    }

                                    if (!georreferenciarCR.getString("Longitud").equals("")) {
                                        cliente.setLongitud(georreferenciarCR.getString("Longitud"));
                                    } else {
                                        // validarIdDireccionGis = false;
                                    }

                                }
                            }
                        }

                        if (datacliente.has("TipoDocumento")) {
                            cliente.setTipoDocumento(datacliente.getString("TipoDocumento"));
                        }

                        if (datacliente.has("Codigo_Hogar")) {
                            cliente.setCodigoHogar(datacliente.getString("Codigo_Hogar"));
                        }

                        if (datacliente.has("Municipio")) {
                            if (!cliente.getCiudad().equalsIgnoreCase(datacliente.getString("Municipio"))) {
                                if (!datacliente.getString("Municipio").equalsIgnoreCase("Bogota")) {
                                    cliente.setCiudad(datacliente.getString("Municipio"));
                                }
                            }
                        }

                        if (datacliente.has("PaginaInstalacion")) {
                            cliente.setPaginacion(datacliente.getString("PaginaInstalacion"));
                        }

                        cliente.setControlClienteSiebel(true);
                        cliente.setEstandarizarSiebel(1);
                    } else {
                        cliente.setEstandarizarSiebel(0);
                    }
                } else {
                    cliente.setEstandarizarSiebel(0);
                }
            } else {
                cliente.setEstandarizarSiebel(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void PintarPortafolioNuevo() {
        if (portafolio.size() > 0) {
            if (portafolio.get(0).getTitulo().equalsIgnoreCase("NO")) {
                Toast.makeText(this, portafolio.get(0).getDato(), Toast.LENGTH_SHORT).show();
            } else if (portafolio.get(0).getTitulo().equalsIgnoreCase("SI")) {
                ofertas = Interprete.getOfertas();
                portafolio.remove(0);
                adapterPortafolio = new ListaDefaultAdapter(this, portafolio, this);
                lvp.setAdapter(adapterPortafolio);
                setListViewHeightBasedOnChildren(lvp);
                lvp.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        String Titulo = portafolio.get(arg2).getTitulo();
                        if (portafolio.get(arg2).getDato().equalsIgnoreCase("Oferta")) {
                            Toast.makeText(ControlSimulador.this, "Oferta Seleccionada", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < ofertas.size(); i++) {
                                if (ofertas.get(i).getOferta().equalsIgnoreCase(Titulo)) {
                                    selfofertas.add(
                                            new Ofertas(ofertas.get(i).getTipoProducto(), ofertas.get(i).getProducto(),
                                                    ofertas.get(i).getTarifa(), ofertas.get(i).getOferta()));
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public void PintarCoberturaNuevo() {
        if (cobertura.size() > 0) {
            if (cobertura.get(0).getTitulo().equalsIgnoreCase("NO")) {
                Toast.makeText(this, cobertura.get(0).getDato(), Toast.LENGTH_SHORT).show();
            } else if (cobertura.get(0).getTitulo().equalsIgnoreCase("SI")) {
                cobertura.remove(0);
                adapterCobertura = new ListaDefaultAdapter(this, cobertura, this);
                lvc.setAdapter(adapterCobertura);
                setListViewHeightBasedOnChildren(lvc);
            }
        }
    }

    public void Resultado_Portafolio(String Resultado) {

        try {
            JSONObject datos = new JSONObject(Resultado);
            JSONArray nombre = datos.names();

            if (datos.getString("TIPO").equals("cedula")) {

                ArrayList<String> direcciones = new ArrayList<String>();
                domicilios = new ArrayList<Domicilio>();
                productos = new ArrayList<Producto>();
                paquetes = new ArrayList<Paquete>();

                final ArrayList<ListaDefault> Portafolio = new ArrayList<ListaDefault>();
                if (Resultado.equalsIgnoreCase("-1")) {
                    Portafolio.add(new ListaDefault(1, "NO", "Problemas Con la consulta intente mas tardes"));
                } else if (Resultado.equalsIgnoreCase("0")) {
                    Portafolio.add(new ListaDefault(1, "NO", "La Consulta No arrojo Resultados"));
                } else if (Resultado.equalsIgnoreCase("2")) {
                    Portafolio.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
                } else if (Resultado.equals("Consulta Hecha fuera del horario establecido")
                        || Resultado.equals("Ha superado el limite de consultas por dia")) {
                    Portafolio.add(new ListaDefault(1, "NO", Resultado));
                } else {
					/*
					 * A continuacion todo el proceso para agregar a cada
					 * direccion su respectivos paquetesy a cada paquete sus
					 * respectivos productos, en caso de no estar empaquetados
					 * se agregan comoproductos solos.
					 */
                    for (int i = 0; i < nombre.length(); i++) {
                        if (datos.get(nombre.get(i).toString()).getClass().getSimpleName().equals("JSONArray")) {
                            JSONArray arreglo = new JSONArray(datos.get(nombre.get(i).toString()).toString());
                            for (int j = 0; j < arreglo.length(); j++) {
                                JSONObject obj = new JSONObject(arreglo.get(j).toString());
                                if (!nombre.get(i).toString().equals("PAQUETE")
                                        && !nombre.get(i).toString().equals("TIPO")
                                        && !nombre.get(i).toString().equals("VIVIENDA")) {
                                    if (!direcciones.contains(obj.getString("Direccion"))) {
                                        direcciones.add(obj.getString("Direccion"));
                                    }
                                    if (!obj.isNull("IdEmpaquetamiento")) {
                                        Producto pro = new Producto(nombre.get(i).toString(),
                                                obj.getString("Identificador"), obj.getString("Datos_cliente"),
                                                obj.getString("Direccion"), obj.getString("Plan_Factura"),
                                                obj.getString("IdEmpaquetamiento"));
                                        productos.add(pro);
                                    } else {
                                        Producto pro = new Producto(nombre.get(i).toString(),
                                                obj.getString("Identificador"), obj.getString("Datos_cliente"),
                                                obj.getString("Direccion"), obj.getString("Plan_Factura"), "{}");
                                        productos.add(pro);
                                    }

                                }
                            }

                            if (nombre.get(i).toString().equals("PAQUETE")) {
                                if (datos.get(nombre.get(i).toString()).getClass().getSimpleName()
                                        .equals("JSONArray")) {
                                    JSONArray jarr = new JSONArray(datos.get(nombre.get(i).toString()).toString());
                                    for (int k = 0; k < jarr.length(); k++) {
                                        JSONObject objt = new JSONObject(jarr.getString(k));
                                        Paquete paq = new Paquete(objt.getString("Identificador"),
                                                objt.getString("Datos_cliente"), objt.getString("Tipo_Paquete"));
                                        paquetes.add(paq);
                                    }
                                } else {
                                    JSONObject objt = new JSONObject(datos.get(nombre.get(i).toString()).toString());
                                    Paquete paq = new Paquete(objt.getString("Identificador"),
                                            objt.getString("Datos_cliente"), objt.getString("Tipo_Paquete"));
                                    paquetes.add(paq);
                                }
                            }

                        } else {
                            if (!nombre.get(i).toString().equals("PAQUETE") && !nombre.get(i).toString().equals("TIPO")
                                    && !nombre.get(i).toString().equals("VIVIENDA")) {
                                JSONObject obj = new JSONObject(datos.get(nombre.get(i).toString()).toString());
                                if (!direcciones.contains(obj.getString("Direccion"))) {
                                    direcciones.add(obj.getString("Direccion"));
                                }

                                if (!obj.isNull("IdEmpaquetamiento")) {
                                    Producto pro = new Producto(nombre.get(i).toString(),
                                            obj.getString("Identificador"), obj.getString("Datos_cliente"),
                                            obj.getString("Direccion"), obj.getString("Plan_Factura"),
                                            obj.getString("IdEmpaquetamiento"));
                                    productos.add(pro);
                                } else {
                                    Producto pro = new Producto(nombre.get(i).toString(),
                                            obj.getString("Identificador"), obj.getString("Datos_cliente"),
                                            obj.getString("Direccion"), obj.getString("Plan_Factura"), "{}");
                                    productos.add(pro);
                                }

                            }
                            if (nombre.get(i).toString().equals("PAQUETE")) {
                                if (datos.get(nombre.get(i).toString()).getClass().getSimpleName()
                                        .equals("JSONArray")) {
                                    JSONArray jarr = new JSONArray(datos.get(nombre.get(i).toString()).toString());
                                    for (int k = 0; k < jarr.length(); k++) {
                                        JSONObject objt = new JSONObject(jarr.getString(k));
                                        Paquete paq = new Paquete(objt.getString("Identificador"),
                                                objt.getString("Datos_cliente"), objt.getString("Tipo_Paquete"));
                                        paquetes.add(paq);
                                    }
                                } else {
                                    JSONObject objt = new JSONObject(datos.get(nombre.get(i).toString()).toString());
                                    Paquete paq = new Paquete(objt.getString("Identificador"),
                                            objt.getString("Datos_cliente"), objt.getString("Tipo_Paquete"));
                                    paquetes.add(paq);
                                }
                            }
                        }
                    }

                    for (int i = 0; i < paquetes.size(); i++) {
                        for (int j = 0; j < productos.size(); j++) {
                            if (paquetes.get(i).getId().equals(productos.get(j).getIdPaquete())) {
                                // System.out.println("No productos => j => " +
                                // j);
                                paquetes.get(i).agregarProducto(productos.get(j));
                            }
                        }
                    }

                    for (int i = 0; i < direcciones.size(); i++) {
                        Domicilio dom = new Domicilio(direcciones.get(i));
                        for (int j = 0; j < paquetes.size(); j++) {
                            if (paquetes.get(j).productos.size() > 0) {
                                if (dom.getDireccion().equals(paquetes.get(j).productos.get(0).getDireccion())) {
                                    dom.agregarPaquete(paquetes.get(j));
                                }
                            }
                        }

                        for (int j = 0; j < productos.size(); j++) {
                            if (productos.get(j).getIdPaquete().equals("{}")
                                    || productos.get(j).getIdPaquete() == null) {
                                if (dom.getDireccion().equals(productos.get(j).getDireccion())) {
                                    dom.agregarProducto(productos.get(j));
                                }
                            }

                        }
                        domicilios.add(dom);
                    }
                    // Creo la lista de direcciones y al final se las agrego al
                    // spinner
                    final List<String> ListaDir = new ArrayList<String>();

                    for (int i = 0; i < domicilios.size(); i++) {
                        // Recorro direcciones -> y agrego a la lista
                        ListaDir.add(domicilios.get(i).getDireccion());
                    }
                    // Creo el adaptador
                    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, ListaDir);
                    adaptador.setDropDownViewResource(R.layout.list_radio);
                    // Agrego direcciones al spinner
                    Direcciones_Portafolio.setAdapter(adaptador);

                    Direcciones_Portafolio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                            for (int i = 0; i < domicilios.size(); i++) {
                                if (domicilios.get(i).getDireccion() == Direcciones_Portafolio.getSelectedItem()
                                        .toString()) {
                                    final ArrayList<ListaDefault> Portafolio = new ArrayList<ListaDefault>();
                                    Portafolio.add(new ListaDefault(0, "VIVIENDA", "Titulo"));
                                    Portafolio.add(new ListaDefault(1, getResources().getString(R.string.direccion),
                                            domicilios.get(i).getDireccion()));
                                    if (domicilios.get(i).paquetes.size() > 0) {
                                        Portafolio.add(new ListaDefault(0, "Productos Empaquetados", "Titulo"));
                                    }
                                    for (int j = 0; j < domicilios.get(i).paquetes.size(); j++) {
                                        Portafolio.add(new ListaDefault(1,
                                                "Paquete No. " + j + " - "
                                                        + domicilios.get(i).paquetes.get(j).getTipo(),
                                                domicilios.get(i).paquetes.get(j).getId()));
                                        Portafolio.add(new ListaDefault(2, "Cliente: ",
                                                domicilios.get(i).paquetes.get(j).getDatosCliente()));
                                        for (int k = 0; k < domicilios.get(i).paquetes.get(j).productos.size(); k++) {
                                            Portafolio
                                                    .add(new ListaDefault(0, domicilios.get(i).paquetes.get(j).productos
                                                            .get(k).getTipoProducto(), "Titulo"));
                                            Portafolio.add(new ListaDefault(1, "Identificador",
                                                    domicilios.get(i).paquetes.get(j).productos.get(k)
                                                            .getIdentificador()));
                                            Portafolio.add(new ListaDefault(2, "Datos del cliente",
                                                    domicilios.get(i).paquetes.get(j).productos.get(k)
                                                            .getDatosCliente()));
                                            Portafolio.add(new ListaDefault(2, "Plan Factura",
                                                    domicilios.get(i).paquetes.get(j).productos.get(k).getPlan()));
                                            Portafolio.add(new ListaDefault(2, "Id Empaquetamiento",
                                                    domicilios.get(i).paquetes.get(j).getId()));
                                        }
                                    }
                                    if (domicilios.get(i).productos.size() > 0) {
                                        Portafolio.add(new ListaDefault(0, "Productos No Empaquetados", "Titulo"));
                                    }
                                    for (int j = 0; j < domicilios.get(i).productos.size(); j++) {
                                        Portafolio.add(new ListaDefault(0,
                                                domicilios.get(i).productos.get(j).getTipoProducto(), "Titulo"));
                                        Portafolio.add(new ListaDefault(1, "Identificador",
                                                domicilios.get(i).productos.get(j).getIdentificador()));
                                        Portafolio.add(new ListaDefault(2, "Datos del cliente",
                                                domicilios.get(i).productos.get(j).getDatosCliente()));
                                        Portafolio.add(new ListaDefault(2, "Plan Factura",
                                                domicilios.get(i).productos.get(j).getPlan()));
                                    }
                                    ofertas = Interprete.getOfertas();
                                    adapterPortafolio = new ListaDefaultAdapter(ControlSimulador.this, Portafolio,
                                            ControlSimulador.this);
                                    lvp.setAdapter(adapterPortafolio);
                                    setListViewHeightBasedOnChildren(lvp);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                }
            } else {
                ArrayList<String[]> Datos_Cliente = new ArrayList<String[]>();
                ofertas.clear();
                selfofertas.clear();

                Borrar("Portafolio");

                portafolio = Interprete.Portafolio(Resultado);
                Datos_Cliente = Interprete.getDatos_Cliente();

                if (Datos_Cliente.size() > 0) {
                    LLenar_Datos_Cliente(Datos_Cliente);
                }

                if (portafolio.get(0).getTitulo().equalsIgnoreCase("NO")) {
                    Toast.makeText(this, portafolio.get(0).getDato(), Toast.LENGTH_SHORT).show();
                } else if (portafolio.get(0).getTitulo().equalsIgnoreCase("SI")) {
                    ofertas = Interprete.getOfertas();
                    portafolio.remove(0);
                    adapterPortafolio = new ListaDefaultAdapter(this, portafolio, this);
                    lvp.setAdapter(adapterPortafolio);
                    setListViewHeightBasedOnChildren(lvp);
                    lvp.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            String Titulo = portafolio.get(arg2).getTitulo();
                            // System.out.println("portafolio Titulo "+ Titulo+
                            // " portafolio Dato "+
                            // portafolio.get(arg2).getDato());
                            if (portafolio.get(arg2).getDato().equalsIgnoreCase("Oferta")) {
                                Toast.makeText(ControlSimulador.this, "Oferta Seleccionada", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < ofertas.size(); i++) {
                                    if (ofertas.get(i).getOferta().equalsIgnoreCase(Titulo)) {
                                        selfofertas.add(new Ofertas(ofertas.get(i).getTipoProducto(),
                                                ofertas.get(i).getProducto(), ofertas.get(i).getTarifa(),
                                                ofertas.get(i).getOferta()));
                                    }
                                }
                            }
                        }
                    });
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void LLenar_Datos_Cliente(ArrayList<String[]> Datos_Cliente) {

        String Direccion_Portafolio = "", Paginacion_Portafolio = "", Estrato_Portafolio = "", Telefono_Portafolio = "",
                Cedula_Portafolio = "", Nombre_Portafolio = "";

        for (int i = 0; i < Datos_Cliente.size(); i++) {

            if (Datos_Cliente.get(i)[0].equalsIgnoreCase("Direccion")) {
                Direccion_Portafolio = Datos_Cliente.get(i)[1];
            } else if (Datos_Cliente.get(i)[0].equalsIgnoreCase("Paginacion")) {
                Paginacion_Portafolio = Datos_Cliente.get(i)[1];
            } else if (Datos_Cliente.get(i)[0].equalsIgnoreCase("Estrato")) {
                Estrato_Portafolio = Datos_Cliente.get(i)[1];
                Estrato_Portafolio = Estrato_Portafolio.trim();
            } else if (Datos_Cliente.get(i)[0].equalsIgnoreCase("Telefono")) {
                Telefono_Portafolio = Datos_Cliente.get(i)[1];
            } else if (Datos_Cliente.get(i)[0].equalsIgnoreCase("Cedula")) {
                Cedula_Portafolio = Datos_Cliente.get(i)[1];
            } else if (Datos_Cliente.get(i)[0].equalsIgnoreCase("Nombre")) {
                Nombre_Portafolio = Datos_Cliente.get(i)[1];
            }
        }

        if (!Direccion.equalsIgnoreCase("")) {
            if (Direccion_Portafolio.equalsIgnoreCase("")) {
                if (Paginacion.equalsIgnoreCase("")) {
                    Paginacion = Paginacion_Portafolio;
                }
                if (Estrato.equalsIgnoreCase("")) {
                    Estrato = Estrato_Portafolio;
                }
            }
        } else {
            Paginacion = Paginacion_Portafolio;
            Estrato = Estrato_Portafolio;
            Direccion = Direccion_Portafolio;
        }

        if (Telefono.equalsIgnoreCase("")) {
            Telefono = Telefono_Portafolio;
            setLlenarTelefonia(Telefono);
        }

        if (Cedula.equalsIgnoreCase("")) {
            Cedula = Cedula_Portafolio;
            Nombre = Nombre_Portafolio;
            // setLlenarCedula(Cedula);
        }
    }

    public void PortaFolio() {

        if (radio_telefono_Portafolio.isChecked() == true) {

            if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
                ConsolidadoSiebel("identificador");
            } else {
                ArrayList<String> parametros = new ArrayList<String>();
                parametros.add(cliente.getTelefono());
                parametros.add(cliente.getDireccion());
                parametros.add(cliente.getCiudadFenix());
                parametros.add(cliente.getCedula());

                ArrayList<Object> params = new ArrayList<Object>();
                params.add(MainActivity.config.getCodigo());
                params.add("Portafolio");
                params.add("identificador");
                params.add(parametros);

                Simulador simulador = new Simulador();
                simulador.setManual(this);
                simulador.addObserver(this);
                simulador.execute(params);
            }

        } else if (radio_direccion_Portafolio.isChecked() == true) {

            if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
                ConsolidadoSiebel("direccion");
            } else {
                ArrayList<String> parametros = new ArrayList<String>();
                parametros.add(cliente.getTelefono());
                parametros.add(cliente.getDireccion());
                parametros.add(cliente.getCiudadFenix());
                parametros.add(buspor.getBusqueda());

                ArrayList<Object> params = new ArrayList<Object>();
                params.add(MainActivity.config.getCodigo());
                params.add("Portafolio");
                params.add("direccion");
                params.add(parametros);

                Simulador simulador = new Simulador();
                simulador.setManual(this);
                simulador.addObserver(this);
                simulador.execute(params);
            }

        } else if (radio_cedula_Portafolio.isChecked() == true) {

            if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
                Mensaje(getResources().getString(R.string.opcionnohabilitada));
            } else {
                ArrayList<String> parametros = new ArrayList<String>();
                parametros.add(cliente.getTelefono());
                parametros.add(cliente.getDireccion());
                parametros.add(cliente.getCiudadFenix());
                parametros.add(buspor.getBusqueda());

                ArrayList<Object> params = new ArrayList<Object>();
                params.add(MainActivity.config.getCodigo());
                params.add("Portafolio");
                params.add("cedula");
                params.add(parametros);

                Simulador simulador = new Simulador();
                simulador.setManual(this);
                simulador.addObserver(this);
                simulador.execute(params);
            }

        }

    }

    public void Resultado_Cobertura_Giis(String Resultado) {
        Borrar("Cobertura");
        cobertura = Interprete.Cobertura(Resultado, cliente.getDepartamento());

        if (cobertura.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, cobertura.get(0).getDato(), Toast.LENGTH_SHORT).show();
        } else if (cobertura.get(0).getTitulo().equalsIgnoreCase("SI")) {
            cobertura.remove(0);
            adapterCobertura = new ListaDefaultAdapter(this, cobertura, this);
            lvc.setAdapter(adapterCobertura);
            setListViewHeightBasedOnChildren(lvc);
        }
    }

    public void Resultado_Cobertura(String Resultado) {

        Borrar("Cobertura");

        cobertura = Interprete.Cobertura(Resultado, cliente.getCiudad());

        if (cobertura.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, cobertura.get(0).getDato(), Toast.LENGTH_SHORT).show();
        } else if (cobertura.get(0).getTitulo().equalsIgnoreCase("SI")) {
            cobertura.remove(0);
            adapterCobertura = new ListaDefaultAdapter(this, cobertura, this);
            lvc.setAdapter(adapterCobertura);
            setListViewHeightBasedOnChildren(lvc);
        }

    }

    public void Cobertura() {

        System.out.println("radio_telefono_Cobertura " + radio_telefono_Cobertura.isChecked());
        System.out.println("radio_direccion_Cobertura " + radio_direccion_Cobertura.isChecked());

        if (radio_telefono_Cobertura.isChecked()) {
            if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
                ConsolidadoSiebel("identificador");
            } else {

                ArrayList<String> parametros = new ArrayList<String>();
                parametros.add(cliente.getTelefono());
                parametros.add(cliente.getDireccion());
                parametros.add(cliente.getCiudadFenix());

                ArrayList<Object> params = new ArrayList<Object>();
                params.add(MainActivity.config.getCodigo());
                params.add("CoberturaNew");
                params.add("identificador");
                params.add(parametros);

                Simulador simulador = new Simulador();
                simulador.setManual(this);
                simulador.addObserver(this);
                simulador.execute(params);
            }

        } else if (radio_direccion_Cobertura.isChecked()) {
            if (Utilidades.validarDireccionesCerca(cliente.getDireccion())) {
                Toast.makeText(this, R.string.direccioncercaa, Toast.LENGTH_LONG).show();
            } else {
                if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
                    ConsolidadoSiebel("direccion");
                } else {
                    ArrayList<String> parametros = new ArrayList<String>();
                    parametros.add(cliente.getTelefono());
                    parametros.add(cliente.getDireccion());

                    if (cliente.getCiudadFenix().equalsIgnoreCase("Ninguna")
                            || cliente.getCiudadFenix().equalsIgnoreCase("Ninguna")) {

                        System.out.println("cliente.getCiudad(); " + cliente.getCiudad());

                        parametros.add(Utilidades.traducirCiudad(cliente.getCiudad()));
                    } else {
                        parametros.add(cliente.getCiudadFenix());
                    }

                    // ArrayList<Object> params = new ArrayList<Object>();
                    // params.add(MainActivity.config.getCodigo());
                    // params.add("Cobertura");
                    // params.add("direccion");
                    // params.add(parametros);

                    ArrayList<Object> params = new ArrayList<Object>();
                    params.add(MainActivity.config.getCodigo());
                    params.add("CoberturaNew");
                    params.add("direccion");
                    params.add(parametros);

                    Simulador simulador = new Simulador();
                    simulador.setManual(this);
                    simulador.addObserver(this);
                    simulador.execute(params);
                }
            }

        } else if (radio_cedula_Cobertura.isChecked()) {
            if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
                Mensaje(getResources().getString(R.string.opcionnohabilitada));
            } else if (Direcciones_Cobertura.getCount() > 0) {
                ArrayList<String> parametros = new ArrayList<String>();
                parametros.add(cliente.getTelefono());
                parametros.add(Direcciones_Cobertura.getSelectedItem().toString());
                parametros.add(CiudadxCedula);

                ArrayList<Object> params = new ArrayList<Object>();
                params.add(MainActivity.config.getCodigo());
                params.add("CoberturaNew");
                params.add("direccion");
                params.add(parametros);

                Simulador simulador = new Simulador();
                simulador.setManual(this);
                simulador.addObserver(this);
                simulador.execute(params);
            } else {
                Dialogo dialogo;
                dialogo = new Dialogo(context, Dialogo.DIALOGO_ALERTA,
                        getResources().getString(R.string.alerta_ced_incorrecta));
                dialogo.dialogo.show();
            }

        } else {
            // System.out.println("nada");
        }

    }

    public void ConsolidadoSiebel(String tipo) {
        boolean validarConsulta = true;
        JSONObject jo = new JSONObject();
        try {
            if (tipo.equalsIgnoreCase("identificador")) {
                if (!Utilidades.limpiarTelefono(cliente.getTelefono()).trim().equalsIgnoreCase("")) {
                    jo.put("strIdentificador", Utilidades.limpiarTelefonoSiebel(cliente.getTelefono()));
                } else {
                    validarConsulta = false;
                }
            } else if (tipo.equalsIgnoreCase("direccion")) {
                if (!cliente.getDireccion().trim().equalsIgnoreCase("")) {
                    jo.put("strDireccion", cliente.getDireccion());
                } else {
                    validarConsulta = false;
                }
                jo.put("strBarrio", cliente.getBarrio());
                jo.put("strDepartamento", cliente.getDepartamento());

                if (cliente.getCiudad().equalsIgnoreCase("BogotaHFC")) {
                    jo.put("strMunicipio", "Bogota");
                } else {
                    jo.put("strMunicipio", cliente.getCiudad());
                }

                jo.put("strDaneDepartamento", cliente.getDepartamentoSiebel());
                jo.put("strDaneMunicipio", cliente.getCiudadSiebel());
                jo.put("strDanePais", "57");
                jo.put("strSalida", "");
            }
            if (validarConsulta) {
                VerificarSiebel(jo.toString(), tipo, "");
            } else {
                Mensaje("No Se Puede Consultar Con Datos Nulos");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void VerificarSiebel(String data, String tipo, String salida) {
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(data);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ConsolidarSiebel");
        params.add(tipo);
        params.add(parametros);
        params.add(salida);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void Mensaje(String Mensaje) {
        Toast.makeText(this, Mensaje, Toast.LENGTH_SHORT).show();
    }

    public String Mensajes() {
        return Mensaje;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
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

    // metodo que permite regresar a la actividad anterior
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("keyCode " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (regresar) {

                System.out.println("cliente.getScooringune().getDocumentoScooring() "
                        + cliente.getScooringune().getDocumentoScooring());

                System.out.println("cliente.getScooringune().getEstado() " + cliente.getScooringune().getEstado());

                Intent intent = new Intent();
                intent.putExtra("Cliente", cliente);
                intent.putExtra("Scooring", scooring);
                setResult(MainActivity.OK_RESULT_CODE, intent);
                super.onBackPressed();
                finish();
            } else {
                Mensaje("Boton bloqueado consulte nuevamente el resultado");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("cliente", cliente);
        intent.putExtra("Scooring", scooring);
        setResult(MainActivity.OK_RESULT_CODE, intent);
    }

    // CLASE INTERNA QUE ENVIA LOS DATOS A LA CLASE Webservice Y NOS MUESTRA EL
    // RESULTADO EN PANTALLA
    public void onPintar() {
        // adapter = new ListaDefaultAdapter(this, simulador, this);
        if (tabs.getCurrentTabTag().equals("Cobertura")) {
            lvc.setAdapter(adapterCobertura);
            setListViewHeightBasedOnChildren(lvc);
        } else if (tabs.getCurrentTabTag().equals("Portafolio")) {
            lvp.setAdapter(adapterPortafolio);
            setListViewHeightBasedOnChildren(lvp);
        } else if (tabs.getCurrentTabTag().equals("Factura")) {
            lvf.setAdapter(adapterFactura);
            setListViewHeightBasedOnChildren(lvf);
        }
    }

    public void Borrar(String tipo) {

        if (tipo.equalsIgnoreCase("Cobertura")) {
            cobertura.clear();
            adapterCobertura = new ListaDefaultAdapter(this, cobertura, this);
            lvc.setAdapter(adapterCobertura);
            setListViewHeightBasedOnChildren(lvc);
        } else if (tipo.equalsIgnoreCase("Portafolio")) {
            portafolio.clear();
            adapterPortafolio = new ListaDefaultAdapter(this, portafolio, this);
            lvp.setAdapter(adapterPortafolio);
            setListViewHeightBasedOnChildren(lvp);
        } else if (tipo.equalsIgnoreCase("Factura")) {
            factura.clear();
            adapterFactura = new ListaDefaultAdapter(this, factura, this);
            lvf.setAdapter(adapterFactura);
            setListViewHeightBasedOnChildren(lvf);
        } else if (tipo.equalsIgnoreCase("Scooring")) {
            listascooring.clear();
            adapterScooring = new ListaDefaultAdapter(this, listascooring, this);
            lvscooring.setAdapter(adapterScooring);
            setListViewHeightBasedOnChildren(lvscooring);
        }
    }

    public void Borrar_Spinner() {
        String[] ArrayDirecciones = new String[0];
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                ArrayDirecciones);
        adaptador.setDropDownViewResource(R.layout.list_radio);
        if (tabs.getCurrentTabTag().equals("Cobertura")) {
            Direcciones_Cobertura.setAdapter(adaptador);
        } else if (tabs.getCurrentTabTag().equals("Portafolio")) {
            Direcciones_Portafolio.setAdapter(adaptador);
        }

    }

    // METODO QUE CREA EL MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_simulador, menu);
        if (Fuente.equalsIgnoreCase("Tarificador")) {
            MenuItem item = menu.getItem(1);
            item.setTitle("Venta");
        }
        return true;
    }

    // METODO INDICA QUE COMPONENTE DEL MENU SE PRESIONO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.verificar:
                if (tabs.getCurrentTabTag().equals("Cobertura")) {
                    Cobertura();

                } else if (tabs.getCurrentTabTag().equals("Portafolio")) {
                    // System.out.println("Portafolio");
                    PortaFolio();
                }

                return true;
            case R.id.menu_repeatCode:
                mostrarDialogo();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub
        ArrayList<Object> resultado = (ArrayList<Object>) value;
        if (resultado.get(0).equals("Portafolio")) {
            cliente.setPortafolio(resultado.get(1).toString());
            Resultado_Portafolio(cliente.getPortafolio());
        } else if (resultado.get(0).equals("Cobertura")) {
            cliente.setCobertura(resultado.get(1).toString());
            Resultado_Cobertura(cliente.getCobertura());
        } else if (resultado.get(0).equals("CoberturaNew")) {
            cliente.setCobertura(resultado.get(1).toString());
            String tec = Utilidades.obtenerCobertura(resultado.get(1).toString(), this);
            if (!tec.equalsIgnoreCase("N/D")) {
                cliente.setTecnologia(tec);
                if (Utilidades.camposUnicosCiudad("bloqueoTecnologia", cliente.getCiudad()).equals("true")) {
                    cliente.setBloqueoTecnologia(true);
                }
            } else {
                cliente.setTecnologia(Utilidades.inicial_opcion);
                if (Utilidades.camposUnicosCiudad("bloqueoTecnologia", cliente.getCiudad()).equals("true")) {
                    cliente.setBloqueoTecnologia(true);
                }
            }
            System.out.println("Tecnologia " + cliente.getTecnologia());
            Resultado_Cobertura(cliente.getCobertura());
        } else if (resultado.get(0).equals("Cartera_UNE")) {
            cliente.setCarteraUNE(resultado.get(1).toString());
            Resultado_Cartera(cliente.getCarteraUNE());
        } else if (resultado.get(0).equals("EstadoCuenta")) {
            // cliente.setCarteraUNE(resultado.get(1).toString());
            // Resultado_Cartera(cliente.getCarteraUNE());
            System.out.println("resultado.get(0) " + resultado.get(0));
            System.out.println("resultado.get(1) " + resultado.get(1));
            ClasificarEstadoCuenta(resultado.get(1).toString());
            // ValidarScooringIngreso(resultado.get(1).toString());
        } else if (resultado.get(0).equals("ResultScooring")) {
            // cliente.setCarteraUNE(resultado.get(1).toString());
            // Resultado_Cartera(cliente.getCarteraUNE());
            System.out.println("resultado.get(0) " + resultado.get(0));
            ValidarResultadoScooring(resultado.get(1).toString());
        } else if (resultado.get(0).equals("DatosScooring")) {
            // cliente.setCarteraUNE(resultado.get(1).toString());
            // Resultado_Cartera(cliente.getCarteraUNE());
            System.out.println("resultado.get(0) " + resultado.get(0));
            ValidarScooringIngreso(resultado.get(1).toString());
        } else if (resultado.get(0).equals("Direcciones")) {
            // System.out.println("DireccionesxCedula aaaaaa
            // "+resultado.get(1).toString());
            LLenar_Direcciones(resultado.get(1).toString());
        } else if (resultado.get(0).equals("ConsultaNodos")) {
            // System.out.println("NodosBogota aaaaaa
            // "+resultado.get(1).toString());

            // LLenar_Direcciones(resultado.get(1).toString());
            // pintarNOdos(String documentacion)
            ResultadoNodos(resultado.get(1).toString());

        } else if (resultado.get(0).equals("BuscarBlindaje")) {
            ResultadoBlindaje(resultado.get(1).toString());
        } else if (resultado.get(0).equals("ConsolidarSiebel")) {
            Resultado_Consolidado(resultado.get(1).toString());
            reorganizarClienteSiebel(resultado.get(1).toString());
            cliente.setConsolidado(resultado.get(1).toString());
            Utilidades.pintarBotones(resultado.get(1).toString());
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
                        idIVR = confirmacion.getString("id");
                        cliente.setIdIVR(Utilidades.convertirNumericos(idIVR, "idIVR Control Simulador"));
                        if (!idIVR.equalsIgnoreCase("")) {
                            LanzarLLamada(idIVR, "1", "NO", "0");
                            //LanzarLLamadaPorLlamadasMasivas(idIVR);
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
                    || MainActivity.servidor == Conexion.SERVIDOR_PRUEBAS
                    || MainActivity.servidor == Conexion.SERVIDOR_DAVID_EXTRNA) {
                controlUserProof = true;

            }

            if (resultado.get(1).equals("OK") || controlUserProof) {
                mostrarDialogo();
                if (Utilidades.excluirNacional("bloqueoTelefonoIVR", "llamada")) {
                    cliente.setBloquearTelefonoDestino(true);
                }
            } else if (resultado.get(1).equals("FAIL")) {
                Toast.makeText(this, "EL lanzamiento de la llamada fallo", Toast.LENGTH_SHORT).show();
            }
        }else if (resultado.get(0).equals("LanzarLLamadaPorLlamadasMasivas")) {
            System.out.println("data LanzarLLamadaPorLlamadasMasivas" + resultado.get(1));

            try {
                JSONObject jop = new JSONObject(resultado.get(1).toString());
                if (jop.has("codigoMensaje") && jop.getString("codigoMensaje").equalsIgnoreCase("00")) {
                    mostrarDialogo();
                } else {
                    Toast.makeText(this, "EL lanzamiento de la llamada fallo", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "EL lanzamiento de la llamada fallo", Toast.LENGTH_SHORT).show();
            }

        }  else if (resultado.get(0).equals("ConsultarRespuesta")) {

            System.out.println("data " + resultado.get(1));
            System.out.println(resultado);

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

                    if (jo.getString("confirmacion").equals("1")) {
                        confirmacionCifin = 1;
                        IngresarEstadoCuenta(1);
                    } else {
                        confirmacionCifin = 0;
                        Toast.makeText(this,
                                "Debe aceptar la consulta a Centrales de Riesgo para proseguir con la venta",
                                Toast.LENGTH_LONG);
                    }
                } else {
                    System.out.println("Los datos no se descargaron correctamente");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultado.get(0).equals("actualizarBandera")) {

            System.out.println("data " + resultado.get(1));
            System.out.println(resultado);

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

                } else {
                    System.out.println("Los datos no se descargaron correctamente");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultado.get(0).equals("ValidarLlamada")) {
            System.out.println("data " + resultado.get(1));
            System.out.println(resultado);

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
                    // System.out.println("jo " + jo.getBoolean("valido"));

                    if (jo.getString("valido").equalsIgnoreCase("1")) {
                        InsertarConfirmacion();
                    } else {
                        Toast.makeText(this, "Ya existe una llamada en cola", Toast.LENGTH_LONG).show();
                    }

                } else {
                    System.out.println("Los datos no se descargaron correctamente");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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

                        // LanzarLLamada(id, "6");
                        LanzarLLamada(idIVR, "1", "NO", "0"); // para cuando se
                        // habilite el
                        // codigo en el IVR

                    }
                } else {
                    System.out.println("Los datos no se descargaron correctamente");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else if (resultado.get(0).equals("ValidacionConfiguracionMovil")) {

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
                Log.w("Error JSONException ",e.getMessage());
            }

        }
        // System.out.println(value.toString());
    }

    public void ClasificarEstadoCuenta(String Resultado) {

        try {

            JSONObject jop = new JSONObject(Resultado);

            if (jop.has("Medio")) {
                if (jop.getString("Medio").equalsIgnoreCase("Scooring")) {
                    if (jop.has("Respuesta")) {
                        if (jop.getJSONObject("Respuesta").has("codigoMensaje")) {
                            ValidarScooringIngreso(jop.getJSONObject("Respuesta").toString());

                        }
                    }
                    scooring.setValidarCartera(false);
                } else if (jop.getString("Medio").equalsIgnoreCase("Cartera")) {
                    if (jop.has("Respuesta")) {
                        Resultado_Cartera(jop.getJSONObject("Respuesta").toString());
                    }
                    scooring.setValidarCartera(true);
                } else if (jop.getString("Medio").equalsIgnoreCase("LanzarIVR")) {
					/*
					 * if (jop.has("Respuesta")) {
					 * Resultado_Cartera(jop.getJSONObject("Respuesta")
					 * .toString()); } scooring.setValidarCartera(true);
					 */
                    System.out.println("Lanzar IVR");
                    validarLlamada();

                }
                cliente.setControlEstadoCuenta(true);
                pintadoEstados(jop.getString("Medio"));
                cliente.setTipoCuenta(jop.getString("Medio"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void pintadoEstados(String tipo) {
        if (tipo.equalsIgnoreCase("Cartera")) {
            TableCartera.setVisibility(View.VISIBLE);
            lvscooring.setVisibility(View.GONE);
        } else if (tipo.equalsIgnoreCase("Scooring")) {
            TableCartera.setVisibility(View.GONE);
            lvscooring.setVisibility(View.VISIBLE);
        }
    }

    public void ValidarScooringIngreso(String Resultado) {
        JSONObject jop;
        try {

            jop = new JSONObject(Resultado);

            System.out.println("Resultado ingreso scooring " + Resultado);

            pintarScooring(Resultado, 0);

            if (jop.has("codigoMensaje")) {

                cliente.getScooringune().setOrigenScooring("");

                if (jop.getString("codigoMensaje").equalsIgnoreCase("00")) {

                    idScooring = jop.getString("idScooring");

                    if (jop.has("log")) {
                        if (jop.getString("log").equalsIgnoreCase("1")) {

                            if (jop.has("origen")) {
                                cliente.getScooringune().setOrigenScooring(jop.getString("origen"));
                            }

                            ValidarResultadoScooring(jop.getString("dataAlmacenamiento").toString());

                        }

                    } else if (jop.has("dupli")) {
                        if (jop.getString("dupli").equalsIgnoreCase("1")) {
                            ValidarResultadoScooring(jop.getString("dataDupli").toString());
                        }

                    } else {

                        regresar = false;

                        Mensaje("Primer Intento Realizado, espere " + Utilidades.tiempoScooring()
                                + " Segundos y consulte nuevamente para ver el resultado");
                        fecha1 = Calendario.getTimestamp();

                        System.out.println("fecha1 " + fecha1);

                        cliente.getScooringune().setResultadoScooring(false);
                    }

                    if (Utilidades.excluirNacional("bloqueoTelefonoIVR", "respuesta")) {
                        cliente.setBloquearTelefonoDestino(true);
                    }
                } else if (jop.getString("codigoMensaje").equalsIgnoreCase("01")
                        || jop.getString("codigoMensaje").equalsIgnoreCase("-1")) {

                    String id = "00";

                    if (jop.has("idScooring")) {
                        id = jop.getString("idScooring");
                    }

                    Mensaje("Error Scoring");
                    cliente.getScooringune().setResultado(false, false, false, false, buscar.getBusqueda(), "0",
                            "ERROR SERVICIO", id);

                    cliente.getScooringune().setPendienteRespuesta(true);
                    cliente.setTipoCliente(2);

                }
            } else {
                cliente.getScooringune().setOrigenScooring("");
            }

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }
    }

    public void ValidarResultadoScooring(String Resultado) {
        JSONObject jop;
        try {
            jop = new JSONObject(Resultado);

            pintarScooring(Resultado, 1);

            // pintarScooring(Utilidades.PositivaScooring(), 1);

            System.out.println("jop " + jop);

            if (jop.has("codigoMensaje")) {

                if (jop.getString("codigoMensaje").equalsIgnoreCase("00")) {
                    regresar = true;
                    if (jop.getString("estadoFinal").equalsIgnoreCase("")
                            || jop.getString("estadoFinal").equalsIgnoreCase("NULL")
                            || jop.getString("estadoFinal").equalsIgnoreCase("(NULL)")) {
                        Mensaje("Scooring todavia Pendiente");
                        cliente.getScooringune().setResultado(false, false, false, false, buscar.getBusqueda(), "0",
                                "ESPERANDO RESPUESTA", idScooring);

                        cliente.getScooringune().setPendienteRespuesta(true);
                        cliente.setTipoCliente(2);

                    } else if (jop.getString("estadoFinal").equalsIgnoreCase("PENDIENTE")
                            && jop.getString("razonEstadoFinal").equalsIgnoreCase("COMUNICACION CENTRALES")) {
                        Mensaje("Scooring todavia Pendiente");
                        cliente.getScooringune().setResultado(false, false, false, false, buscar.getBusqueda(), "0",
                                "ESPERANDO RESPUESTA", idScooring);

                        cliente.getScooringune().setPendienteRespuesta(true);
                        cliente.setTipoCliente(2);
                    } else if (jop.getString("estadoFinal").equalsIgnoreCase("APROBADO")) {
                        apruebaScooring = true;
                        cliente.getScooringune().setResultado(true, true, false, true, buscar.getBusqueda(),
                                jop.getString("maximaCotizacion"), jop.getString("estadoFinal"), idScooring);
                    } else {
                        apruebaScooring = false;
                        cliente.getScooringune().setResultado(true, false, true, true, buscar.getBusqueda(), "0",
                                jop.getString("estadoFinal"), idScooring);
                        cliente.getScooringune().setRazonScooring(jop.getString("razonEstadoFinal"));
                    }
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pintarScooring(String Resultado, int tipo) {
        Borrar("Scooring");

        listascooring = Interprete.defaulScooring(Resultado, tipo);

        System.out.println("listascooring " + listascooring);

        System.out.println("cantidad " + listascooring.size());

        if (listascooring.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, blindaje.get(0).getDato(), Toast.LENGTH_SHORT).show();
        } else if (listascooring.get(0).getTitulo().equalsIgnoreCase("SI")) {
            listascooring.remove(0);
            adapterScooring = new ListaDefaultAdapter(this, listascooring, this);
            lvscooring.setAdapter(adapterScooring);
            setListViewHeightBasedOnChildren(lvscooring);
        }
    }

    public void ResultadoBlindaje(String Resultado) {
        JSONObject jop;
        try {
            jop = new JSONObject(Resultado);

            String data = jop.get("data").toString();
            if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("data => " + data);
                PintarBlindaje(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void PintarBlindaje(String Resultado) {

        Borrar("Blindaje");

        System.out.println("PintarBlindaje " + Resultado);

        blindaje = Interprete.BlindajeNacional(Resultado);

        System.out.println("array " + blindaje);

        if (blindaje.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, blindaje.get(0).getDato(), Toast.LENGTH_SHORT).show();
        } else if (blindaje.get(0).getTitulo().equalsIgnoreCase("SI")) {
            blindaje.remove(0);
            adapterBlindaje = new ListaDefaultAdapter(this, blindaje, this);
            lvblin.setAdapter(adapterBlindaje);
            setListViewHeightBasedOnChildren(lvblin);
        }

    }

    public void ResultadoNodos(String Resultado) {
        JSONObject jop;
        try {
            jop = new JSONObject(Resultado);

            String data = jop.get("data").toString();
            if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("data => " + data);
                Resultado_CoberturaNodos(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Resultado_CoberturaNodos(String Resultado) {

        Borrar("Cobertura");

        cobertura = Interprete.CoberturaNodos(Resultado);

        if (cobertura.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, cobertura.get(0).getDato(), Toast.LENGTH_SHORT).show();
        } else if (cobertura.get(0).getTitulo().equalsIgnoreCase("SI")) {
            cobertura.remove(0);
            adapterCobertura = new ListaDefaultAdapter(this, cobertura, this);
            lvc.setAdapter(adapterCobertura);
            setListViewHeightBasedOnChildren(lvc);
        }

    }

    private void validarLlamada() {

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(MainActivity.config.getCodigo_asesor());
        parametros.add(cliente.getTelefonoDestino());
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ValidarLlamada");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

    }

    private void InsertarConfirmacion() {

        if (cliente.getTelefonoDestino().length() >= 7) {
            if (idIVR.equals("")) {
                codigoIVR = (int) (Math.random() * (9999 - 1000 + 1) + 1000);

                JSONObject IVR = new JSONObject();

                try {

                    IVR.put("tipo", "insert");
                    IVR.put("codigoasesor", MainActivity.config.getCodigo());
                    // IVR.put("telefono", TelefonoIVR);
                    IVR.put("telefono", Utilidades.ponerIndicativo(cliente.getTelefonoDestino()));
                    IVR.put("codigo", codigoIVR);
                    IVR.put("documento", cliente.getCedula());
                    IVR.put("tipocliente", 0);
                    IVR.put("fecha", "");
                    IVR.put("franja", "");
                    IVR.put("Scooring", 0);

                    System.out.println(IVR.toString());

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
                //LanzarLLamada(idIVR, "1", "NO", "0");
                LanzarLLamadaPorLlamadasMasivas(idIVR);
            }

        } else {
            // mostrarDialgoTelefono();
        }

        // btnSiguiente.setEnabled(true);
    }

    private void LanzarLLamada(String id, String tipo, String mail, String cobroDomingo) {

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(tipo);
        parametros.add(id);
        parametros.add(mail);
        parametros.add(Utilidades.cambioCobroDomingo(cobroDomingo));
        parametros.add("0");
        parametros.add("0");
        parametros.add("0");
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
            datosExtras.put("nombreIVR", Utilidades.nombre_ivr_scoring);
            datosExtras.put("tipoIVR", Utilidades.tipoIvrScoring);
            datosExtras.put("idConfirmacion", id);
            datosExtras.put("codigoConfirmacion", codigoIVR);
            datosExtras.put("tipoLLamada", 1);
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

    private void consultarRespuesta() {
        if (!idIVR.equalsIgnoreCase("")) {
            ArrayList<String> parametros = new ArrayList<String>();
            parametros.add(idIVR);
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(MainActivity.config.getCodigo());
            params.add("ConsultarRespuesta");
            params.add(parametros);
            Simulador simulador = new Simulador();
            simulador.setManual(this);
            simulador.addObserver(this);
            simulador.execute(params);
        } else {
            Toast.makeText(this, "Problemas Con la consulta, favor volver a realizar la llamada", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void actualizarBandera() {
        if (!idIVR.equalsIgnoreCase("")) {
            ArrayList<String> parametros = new ArrayList<String>();
            parametros.add(idIVR);

            ArrayList<Object> params = new ArrayList<Object>();
            params.add(MainActivity.config.getCodigo());
            params.add("actualizarBandera");
            params.add(parametros);
            Simulador simulador = new Simulador();
            simulador.setManual(this);
            simulador.addObserver(this);
            simulador.execute(params);
        } else {
            Toast.makeText(this, "Problemas Con la consulta, favor volver a realizar la llamada", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void mostrarDialogo() {
        System.out.println("Codigo => " + codigoIVR);
        AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle(getResources().getString(R.string.confirmacioncodigo));
        promptDialog.setCancelable(false);
        final EditText input = new EditText(this);
        final Context context = this;
        promptDialog.setView(input);

        promptDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                System.out.println("promptDialog id" + idIVR);
                if (cliente.getIdIVR() != 0) {

                    idIVR = String.valueOf(cliente.getIdIVR());

                    if (value.toCharArray().length == 4) {
                        if (value.equals(String.valueOf(codigoIVR)) || value.equals("20130806")) {

                            if (Utilidades.excluirNacional("habilitarIVR", "confronta")) {
                                mostrarMensajeConfronta();
                            } else {
                                consultarRespuesta();
                                actualizarBandera();
                            }


                            if (Utilidades.excluirNacional("bloqueoTelefonoIVR", "confirmacion")) {
                                cliente.setBloquearTelefonoDestino(true);
                            }

                        } else {
                            Toast.makeText(context, "No Concuerdan los codigos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.tamanocodigoincorrecto),
                                Toast.LENGTH_SHORT).show();
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

    private void mostrarMensajeConfronta() {
        AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle(getResources().getString(R.string.valdiacionconfronta));
        promptDialog.setCancelable(false);
        final RadioGroup lly = new RadioGroup(this);
        lly.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        lly.setOrientation(LinearLayout.HORIZONTAL);


        final RadioButton exitoso = new RadioButton(this);
        exitoso.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
        exitoso.setText(getResources().getText(R.string.exitoso));
        exitoso.setTextColor(getResources().getColor(R.color.white));

        final RadioButton rechazado = new RadioButton(this);
        rechazado.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
        rechazado.setText(getResources().getText(R.string.rechazado));
        rechazado.setTextColor(getResources().getColor(R.color.white));

        lly.addView(exitoso);
        lly.addView(rechazado);

        final Context context = this;
        promptDialog.setView(lly);

        promptDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                cliente.setRealizoConfronta(true);

                if (lly.getCheckedRadioButtonId() == exitoso.getId()) {
                    consultarRespuesta();
                    actualizarBandera();
                    cliente.setConfronta(true);
                } else if (lly.getCheckedRadioButtonId() == rechazado.getId()) {
                    consultarRespuesta();
                    actualizarBandera();
                    cliente.setConfronta(false);
                    Toast.makeText(context, "El proceso de confronta no fue exitoso, no es posible continuar con el ingreso de la venta", Toast.LENGTH_LONG).show();
                }

            }
        });

        promptDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                cliente.setRealizoConfronta(false);
                Toast.makeText(context, "No se puede continuar", Toast.LENGTH_SHORT).show();
            }
        });

        promptDialog.show();
    }

    @Override
    public void afterTextChanged(Editable arg0) {
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

}
