package co.com.une.appmovilesune.controller;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.kobjects.util.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemKeyValue;
import co.com.une.appmovilesune.adapters.ProyectosRurales;
import co.com.une.appmovilesune.change.Interprete;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.SelectorFecha;
import co.com.une.appmovilesune.components.SelectorHora;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.CoberturaRural;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.Venta;

import com.google.analytics.tracking.android.EasyTracker;

public class ControlCliente extends Activity implements Observer, TextWatcher {

	private Cliente cliente, manual;
	private Venta venta;
	private Context context;
	private Dialogo dialogo;
	private Dialogo dialogoHora;

    private TituloPrincipal tp;
    private SelectorFecha slfCliente, sltFechaNacimiento;
    private EditText txtDocumento;
    private EditText txtNombre;
    private EditText txtApellido;

    private AutoCompleteTextView txtLugarExpedicion;
    private EditText txtDireccion;
    private EditText txtDireccionNormalizada;
    private TableRow trTablaClienteDireccionNormalizada;
    private EditText txtPaginacion;
    private AutoCompleteTextView txtBarrio;
    private AutoCompleteTextView txtProyectosRurales;
    private EditText txtPuntoReferencia;
    private EditText txtTelefonoServicio;
    private EditText txtTelefonoDestino;
    private EditText txtTelefonoSecundario;
    private EditText txtCelular;
    private EditText txtCorreo;
    private EditText txtLogin;
    private EditText txtPinHotPack;
    private EditText txtObservaciones;
    private EditText txtContracto;
    private EditText txtCodigoHogar;

    private EditText txtNombrePredio;

    private EditText txtNombreContacto1;
    private EditText txtApellidoContacto1;
    private EditText txtTelefonoContacto1;
    private EditText txtCelularContacto1;
    private EditText txtParentescoContacto1;
    private EditText txtNombreContacto2;
    private EditText txtApellidoContacto2;
    private EditText txtTelefonoContacto2;
    private EditText txtCelularContacto2;
    private EditText txtParentescoContacto2;

    private EditText txtDireccionFacturacion;
    private EditText txtBarrioFacturacion;
    private Spinner sltDepartamentoFacturacion, sltMunicipioFacturacion, spnDominio, spnTecnologia, spnTipoZona, spnDomiciliacion;

    private TabHost tabs;

    private CheckBox chkValidar;

    public Spinner sltTipoDocumento, spnTipoServicio, sltPersonasCargo, sltEstrato, sltNivelEstudio, sltProfession,
            sltOcupacion, sltCargo, sltNivelIngresos, sltEstadoCivil, sltGenero, sltTipoVivienda, sltTipoPredio,
            sltTipoPropiedad, sltMunicipioSSC, sltParentescoContacto1, sltParentescoContacto2, sltTipoConstruccion,
            sltMigrar4G;

    private TableRow rowTecnologia;
    private TableRow rowTipoZona;
    private TableRow rowNivelEstudio;
    private TableRow rowProfesion;
    private TableRow rowOcupacion;
    private TableRow rowCargo;
    private TableRow rowNivelIngresos;
    private TableRow rowEstadoCivil;
    private TableRow rowPersonasCargo;
    private TableRow rowTipoVivienda;
    private TableRow rowTipoPredio;
    private TableRow rowTipoPropiedad;
    private TableRow rowNombrePredio;
    private TableRow rowTipoConstruccion;
    private TableRow rowMunicipioREDCO;
    private TableRow rowTablaMigrar4G;
    private TableRow rowFechaNacimiento;
    private TableRow rowGenero;
    private TableRow rowCodigoHogar;
    private TableRow trTablaClienteBotonPrueba;
    private TableRow rowTituloContacto2;
    private TableRow rowTCNContacto2Nombre;
    private TableRow rowTCNContacto2Apellido;
    private TableRow rowTCNContacto2Telefono;
    private TableRow rowTCPContacto2Parentesco;

    private TableRow rowDomiciliacion;
    private TableRow rowClientePaginacion;
    private TableRow rowClienteContrato;
    private TableRow rowClienteLogin;
    private TableRow rowClientePinHotPack;

    private TableRow rowProyectosRurales;

    public SelectorFecha sfp;
	private TableRow rowClienteHoraDomiciliacion;

	public SelectorHora shp;

    private ArrayList<ItemKeyValue> claveValorLugarExp = new ArrayList<ItemKeyValue>();
    private ArrayList<ProyectosRurales> arrayProyectosRurales = new ArrayList<ProyectosRurales>();

    private boolean controlProyecto = false;
    private boolean controlEstandarizar = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcliente);

        dialogo = new Dialogo(this, Dialogo.DIALOGO_FECHA, "");
        dialogo.dialogo.setOnDismissListener(dl);

		dialogoHora = new Dialogo(this, Dialogo.DIALOGO_HORA, "");
		dialogoHora.dialogo.setOnDismissListener(dlh);

		tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
		tp.setTitulo("Cliente");
		MainActivity.obsrCliente = this;
		slfCliente = (SelectorFecha) findViewById(R.id.slfCliente);
		sltFechaNacimiento = (SelectorFecha) findViewById(R.id.sltFechaNacimiento);

        txtDocumento = (EditText) findViewById(R.id.txtDocumento);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtNombre.requestFocus();
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtLugarExpedicion = (AutoCompleteTextView) findViewById(R.id.txtLugarExpedicion);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtDireccionNormalizada = (EditText) findViewById(R.id.txtDireccionNormalizada);
        trTablaClienteDireccionNormalizada = (TableRow) findViewById(R.id.trTablaClienteDireccionNormalizada);

        txtCodigoHogar = (EditText) findViewById(R.id.txtCodigoHogar);
        txtPaginacion = (EditText) findViewById(R.id.txtPaginacion);
        txtContracto = (EditText) findViewById(R.id.txtContrato);
        txtBarrio = (AutoCompleteTextView) findViewById(R.id.txtBarrio);

        txtPuntoReferencia = (EditText) findViewById(R.id.txtPuntoReferencial);
        txtTelefonoServicio = (EditText) findViewById(R.id.txtTelefonoServicio);
        txtTelefonoDestino = (EditText) findViewById(R.id.txtTelefonoDestino);
        txtTelefonoSecundario = (EditText) findViewById(R.id.txtTelefonoSecundario);
        txtCelular = (EditText) findViewById(R.id.txtCelular);
        txtCorreo = (EditText) findViewById(R.id.txtMail);
        spnDominio = (Spinner) findViewById(R.id.spnDominio);
        txtLogin = (EditText) findViewById(R.id.txtLogin);
        txtPinHotPack = (EditText) findViewById(R.id.txtPinHotPack);
        txtObservaciones = (EditText) findViewById(R.id.txtObservaciones);

        sltTipoDocumento = (Spinner) findViewById(R.id.sltTipoDocumento);
        spnTipoServicio = (Spinner) findViewById(R.id.spnTipoServicio);
        sltEstrato = (Spinner) findViewById(R.id.sltEstrato);
        spnTecnologia = (Spinner) findViewById(R.id.spnTecnologia);
        sltNivelEstudio = (Spinner) findViewById(R.id.sltNivelEstudio);
        sltProfession = (Spinner) findViewById(R.id.sltProfession);
        sltOcupacion = (Spinner) findViewById(R.id.sltOcupacion);
        sltCargo = (Spinner) findViewById(R.id.sltCargo);
        sltNivelIngresos = (Spinner) findViewById(R.id.sltNivelIngresos);
        sltEstadoCivil = (Spinner) findViewById(R.id.sltEstadoCivil);
        sltGenero = (Spinner) findViewById(R.id.sltGenero);
        sltTipoVivienda = (Spinner) findViewById(R.id.sltTipoVivienda);
        sltTipoPredio = (Spinner) findViewById(R.id.sltTipoPredio);
        sltTipoPropiedad = (Spinner) findViewById(R.id.sltTipoPropiedad);
        sltPersonasCargo = (Spinner) findViewById(R.id.sltPersonasCargo);
        sltTipoConstruccion = (Spinner) findViewById(R.id.sltTipoConstruccion);
        sltMigrar4G = (Spinner) findViewById(R.id.sltMigrar4G);

		sltMunicipioSSC = (Spinner) findViewById(R.id.sltMunicipioSSC);
		spnDomiciliacion = (Spinner) findViewById(R.id.spnDomiciliacion);
		spnDomiciliacion.setOnItemSelectedListener(ocultarMostarFranja);

		shp = (SelectorHora) findViewById(R.id.slhdomiciliacion);
		/* Objectos Scoring */
		rowMunicipioREDCO = (TableRow) findViewById(R.id.trTablaMunicipioSSC);
		rowTecnologia = (TableRow) findViewById(R.id.trTablaTecnologia);
		rowNivelEstudio = (TableRow) findViewById(R.id.trTablaNivelEstudio);
		rowProfesion = (TableRow) findViewById(R.id.trTablaProfesion);
		rowOcupacion = (TableRow) findViewById(R.id.trTablaOcupacion);
		rowCargo = (TableRow) findViewById(R.id.trTablaCargo);
		rowNivelIngresos = (TableRow) findViewById(R.id.trTablaNivelIngresos);
		rowEstadoCivil = (TableRow) findViewById(R.id.trTablaEstadoCivil);
		rowPersonasCargo = (TableRow) findViewById(R.id.trTablaPersonasCargo);
		rowTipoVivienda = (TableRow) findViewById(R.id.trTablaTipoVivienda);
		rowTipoPredio = (TableRow) findViewById(R.id.trTablaTipoPredio);
		rowTipoPropiedad = (TableRow) findViewById(R.id.trTablaTipoPropiedad);
		rowNombrePredio = (TableRow) findViewById(R.id.trTablaNombrePredio);
		rowTipoConstruccion = (TableRow) findViewById(R.id.trTablaTipoConstruccion);
		rowTablaMigrar4G = (TableRow) findViewById(R.id.trTablaMigrar4G);
		rowFechaNacimiento = (TableRow) findViewById(R.id.trTablaFechaNacimiento);
		rowGenero = (TableRow) findViewById(R.id.trTablaGenero);
		rowCodigoHogar = (TableRow) findViewById(R.id.trTablaCodigoHogar);
		trTablaClienteBotonPrueba = (TableRow) findViewById(R.id.trTablaClienteBotonPrueba);
		rowTituloContacto2 = (TableRow) findViewById(R.id.trTituloContacto2);
		rowTCNContacto2Nombre = (TableRow) findViewById(R.id.trTCNContacto2Nombre);
		rowTCNContacto2Apellido = (TableRow) findViewById(R.id.trTCNContacto2Apellido);
		rowTCNContacto2Telefono = (TableRow) findViewById(R.id.trTCTContacto2Telefono);
		rowTCPContacto2Parentesco = (TableRow) findViewById(R.id.trTCPContacto2Parentesco);
		rowDomiciliacion = (TableRow) findViewById(R.id.trTablaDomiciliacion);

        rowClientePaginacion = (TableRow) findViewById(R.id.trTablaClientePaginacion);
        rowClienteContrato = (TableRow) findViewById(R.id.trTablaClienteContrato);
        rowClienteLogin = (TableRow) findViewById(R.id.trTablaClienteLogin);
        rowClientePinHotPack = (TableRow) findViewById(R.id.trTablaClientePinHotPack);

        rowProyectosRurales = (TableRow) findViewById(R.id.trTablaProyectos);
        rowTipoZona = (TableRow) findViewById(R.id.trTipoZona);
        spnTipoZona = (Spinner) findViewById(R.id.spnTipoZona);
        txtProyectosRurales = (AutoCompleteTextView) findViewById(R.id.txtProyectosRurales);

		rowClienteHoraDomiciliacion = (TableRow) findViewById(R.id.trTablaHoraDomiciliacion);

		txtNombrePredio = (EditText) findViewById(R.id.txtNombrePredio);

        // Campos correspondientes a la clase de contacto
        txtNombreContacto1 = (EditText) findViewById(R.id.txtNombreContacto1);
        txtApellidoContacto1 = (EditText) findViewById(R.id.txtApellidoContacto1);
        txtTelefonoContacto1 = (EditText) findViewById(R.id.txtTelefonoContacto1);
        txtCelularContacto1 = (EditText) findViewById(R.id.txtCelContacto1);
        txtParentescoContacto1 = (EditText) findViewById(R.id.txtParentescoContacto1);
        // sltParentescoContacto1 = (Spinner)
        // findViewById(R.id.spnParentescoContacto1);
        txtNombreContacto2 = (EditText) findViewById(R.id.txtNombreContacto2);
        txtApellidoContacto2 = (EditText) findViewById(R.id.txtApellidoContacto2);
        txtTelefonoContacto2 = (EditText) findViewById(R.id.txtTelefonoContacto2);
        txtCelularContacto2 = (EditText) findViewById(R.id.txtCelContacto2);
        // txtParentescoContacto2 = (EditText)
        // findViewById(R.id.txtParentescoContacto2);
        sltParentescoContacto2 = (Spinner) findViewById(R.id.spnParentescoContacto2);

        // Campos Correspondientes a la clase de facturacion
        sltDepartamentoFacturacion = (Spinner) findViewById(R.id.fac_departamento);
        sltMunicipioFacturacion = (Spinner) findViewById(R.id.fac_municipio);
        txtDireccionFacturacion = (EditText) findViewById(R.id.fac_direccion);
        txtBarrioFacturacion = (EditText) findViewById(R.id.fac_barrio);

        chkValidar = (CheckBox) findViewById(R.id.chkValidar);

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            cliente = (Cliente) reicieveParams.getSerializable("cliente");
            venta = (Venta) reicieveParams.getSerializable("venta");

            System.out.println("venta " + venta);

            System.out.println(cliente);
        } else {
            // System.out.println("no cliente");
        }

        llenarBarrios();
        llenarLugarExpedicion();
        llenarDominios();

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                Utilidades.estrato);
        sltEstrato.setAdapter(adaptador);

		/* Lleno Spinners */
        llenarSpinner("Tipo Servicio", spnTipoServicio);
        llenarSpinner("Tipo Documento", sltTipoDocumento);

        sltTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                String tipoDocumento = (String) sltTipoDocumento.getSelectedItem();

                try {
                    int tamanoDocumento = Integer.parseInt(Utilidades.claveValor("tamanoDocumento", tipoDocumento));

                    if (txtDocumento.getText().toString().length() > tamanoDocumento) {
                        txtDocumento.setText("");
                    }

                    txtDocumento.setFilters(new InputFilter[]{new InputFilter.LengthFilter(tamanoDocumento)});

                } catch (NumberFormatException e) {
                    Log.w("Error ", e.getMessage());
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // lblMensaje.setText("");
            }
        });

        // llenarSpinner("tecnologia", spnTecnologia);
        llenarTecnologia();
        llenarSpinner("sino", spnDomiciliacion);
        llenarSpinner("Estudios Realizados", sltNivelEstudio);
        llenarSpinner("Profesion", sltProfession);
        llenarSpinner("Ocupacion", sltOcupacion);
        llenarSpinner("Cargo", sltCargo);
        llenarSpinner("Nivel Ingresos", sltNivelIngresos);
        llenarSpinner("Estado Civil", sltEstadoCivil);
        llenarSpinner("Generos", sltGenero);
        llenarSpinner("Tipo Vivienda", sltTipoVivienda);
        llenarSpinner("Tipo Predio", sltTipoPredio);
        llenarSpinner("Tipo Propiedad", sltTipoPropiedad);
        llenarSpinner("Personas Cargo", sltPersonasCargo);
        // llenarSpinner("Parentesco", sltParentescoContacto1);
        llenarSpinner("Parentesco", sltParentescoContacto2);
        llenarSpinner("tipoConstruccion", sltTipoConstruccion);
        llenarSpinner("migracion", sltMigrar4G);
        llenarTipoZona();

        llenarDepartamentoFac();
        pintarMunicipioREDCO();

        llenarCampos();

        if (cliente.isBloqueoTecnologia()) {
            spnTecnologia.setEnabled(false);
        } else {
            spnTecnologia.setEnabled(true);
        }

		/* Manejo de TABS */
        Resources res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();
        /*
         * Establezco los tabs que se van a manejar y de acuerdo a estos,
		 * cambian el titulo, texto e img
		 */
        TabHost.TabSpec spec = tabs.newTabSpec("Cliente");
        spec.setContent(R.id.infoCliente);
        spec.setIndicator("Cliente", res.getDrawable(R.drawable.cliente));
        tabs.addTab(spec);

        if (Utilidades.visible("Contactos", cliente.getCiudad())) {
            spec = tabs.newTabSpec("Contacto");
            spec.setContent(R.id.infoContacto);//
            spec.setIndicator("Contacto", res.getDrawable(R.drawable.asesoria));
            tabs.addTab(spec);
        }
        if (Utilidades.visible("SegundoContacto", cliente.getCiudad())) {
            rowTituloContacto2.setVisibility(View.VISIBLE);
            rowTCNContacto2Nombre.setVisibility(View.VISIBLE);
            rowTCNContacto2Apellido.setVisibility(View.VISIBLE);
            rowTCNContacto2Telefono.setVisibility(View.VISIBLE);
            rowTCPContacto2Parentesco.setVisibility(View.VISIBLE);
        }

        System.out.println("Cliente Ciudad => " + cliente.getCiudad());

        if (Utilidades.visible("Scooring", cliente.getCiudad())) {
            spec = tabs.newTabSpec("Socioeconomico");
            spec.setContent(R.id.infoSocioeco);//
            spec.setIndicator("Socioeconomico", res.getDrawable(R.drawable.cartera));
            tabs.addTab(spec);
        }

        if (Utilidades.visible("Elite", cliente.getCiudad())) {
            spec = tabs.newTabSpec("Facturacion");
            spec.setContent(R.id.infoFacturacion);
            spec.setIndicator("Facturación", res.getDrawable(R.drawable.cuentasune));
            tabs.addTab(spec);
            cliente.setControlFacturacion(true);
        }

        if (Utilidades.visible("tipoZona", cliente.getCiudad())) {
            rowTipoZona.setVisibility(View.VISIBLE);
        }

        tabs.setCurrentTab(0);

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

		/*
         * Cada que se cambia el tab, repinto todos y luego pinto de amarillo el
		 * seleccionado
		 */
        tabs.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if (tabId.equals("Contacto")) {
                    tp.setTitulo("Contacto");
                } else if (tabId.equals("Facturacion")) {
                    tp.setTitulo("Facturación");
                } else if (tabId.equals("Socioeconomico")) {
                    tp.setTitulo("Socioeconomico");
                } else if (tabId.equals("Cliente")) {
                    tp.setTitulo("Cliente");
                } else {
                    Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
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

        chkValidar.setOnCheckedChangeListener(validar);

        //txtProyectosRurales.setOnItemSelectedListener(eventoProyectoRural);
        txtProyectosRurales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Utilidades.MensajesToast(txtProyectosRurales.getText().toString(), MainActivity.context);
                validarProyecto(txtProyectosRurales.getText().toString());
            }

        });

        campoVisibles();

		System.out.println("nombre plan "+Utilidades.nombrePlan("Antioquia","0947", "3"));

	}

    public void validarProyecto(String proyecto) {
        boolean control = false;
        for (int i = 0; i < arrayProyectosRurales.size(); i++) {
            if (arrayProyectosRurales.get(i).getDescripcion().equalsIgnoreCase(proyecto)) {

                System.out.println("proyecto Rural descripcion " + arrayProyectosRurales.get(i).getDescripcion());
                System.out.println("proyecto Rural cobertura " + arrayProyectosRurales.get(i).getCobertura());
                System.out.println("proyecto Rural proyecto " + arrayProyectosRurales.get(i).getProyecto());

                validarCoberturaNew(Utilidades.coberturaProyecto(arrayProyectosRurales.get(i).getCobertura()));
                //cliente.setCoberRural(true);
                control = true;
                cliente.coberturaRural.setCoberturaRural(true);
                cliente.coberturaRural.setCoberturaSeleccionada(arrayProyectosRurales.get(i).getCobertura());
                cliente.coberturaRural.setCodigoProyecto(arrayProyectosRurales.get(i).getProyecto());
                cliente.coberturaRural.setProyectoSeleccionado(arrayProyectosRurales.get(i).getDescripcion());
            }
        }

        if (!control) {
            limpiarProyecto();

        }
    }

    public void limpiarProyecto(){
        cliente.coberturaRural.setCoberturaRural(false);
        cliente.coberturaRural.setCoberturaSeleccionada("");
        cliente.coberturaRural.setCodigoProyecto("");
        cliente.coberturaRural.setProyectoSeleccionado("");
        cliente.setControlNormalizada(false);
        cliente.setConsultaNormalizada(false);
        cliente.setCobertura("");
    }

    public void campoVisibles() {
        visibleMunicipio(rowDomiciliacion, "domiciliacion");
        visibleMunicipio(rowTecnologia, "tecnologia");
        visibleMunicipio(rowClientePaginacion, "paginacion");
        visibleMunicipio(rowClienteContrato, "contrato");
        visibleMunicipio(rowClienteLogin, "login");
        visibleMunicipio(rowClientePinHotPack, "pinHotPack");
        visible(rowNivelEstudio, "NivelEstudio");
        visible(rowProfesion, "Profesion");
        visible(rowOcupacion, "Ocupacion");
        visible(rowCargo, "Cargo");
        visible(rowNivelIngresos, "NivelIngresos");
        visible(rowEstadoCivil, "EstadoCivil");
        visible(rowPersonasCargo, "PersonasCargo");
        visible(rowTipoVivienda, "TipoVivienda");

        visible(rowTipoPredio, "TipoPredio");
        visible(rowTipoPropiedad, "TipoPropiedad");
        visible(rowNombrePredio, "NombrePredio");
        visible(rowTipoConstruccion, "TipoConstruccion");

        visible(rowFechaNacimiento, "FechaNacimiento");
        visible(rowGenero, "Genero");
        visible(rowCodigoHogar, "CodigoHogar");
        visible(rowTablaMigrar4G, "Migrar4G");
    }

    public void visible(TableRow row, String Campo) {

        if (Utilidades.visible(Campo, cliente.getCiudad())) {
            row.setVisibility(View.VISIBLE);
        } else {
            row.setVisibility(View.GONE);
        }

    }

    public void visibleMunicipio(TableRow row, String Campo) {

        if (Utilidades.excluirMunicipal("habilitarCampos", Campo, cliente.getCiudad())) {
            row.setVisibility(View.VISIBLE);
        } else {
            row.setVisibility(View.GONE);
        }

    }

    public void llenarBarrios() {
        String mun = "";
        if (MainActivity.config.getDepartamento().equalsIgnoreCase(Utilidades.Bogota)) {
            mun = "Bogota";
        } else {

            System.out.println("cliente " + cliente);
            if (cliente != null) {
                if (cliente.getCiudad() != null) {
                    mun = cliente.getCiudad();
                } else {
                    mun = MainActivity.config.getCiudad();
                }
            } else {
                mun = MainActivity.config.getCiudad();
            }

        }
        System.out.println("Municipio " + mun);

        String[] arg = new String[]{mun};
        // System.out.println("Argumentos => " + mun);
        ArrayList<ArrayList<String>> barr = MainActivity.basedatos.consultar(false, "barrios",
                new String[]{"barrio"}, "ciudad=?", arg, null, null, null);

        if (barr != null) {
            ArrayList<String> dat = new ArrayList<String>();
            for (int i = 0; i < barr.size(); i++) {
                dat.add(barr.get(i).get(0));
                // System.out.println("barrios => " + barr.get(i).get(0));
            }

            ArrayAdapter<String> adaptadorBarrios = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    dat);
            txtBarrio.setAdapter(adaptadorBarrios);
        }
        txtBarrio.setTextColor(getResources().getColor(R.color.black));

    }

    public void llenarLugarExpedicion() {
        boolean bogota = false;

        claveValorLugarExp.clear();
        ArrayList<ArrayList<String>> ciudades = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"Ciudad,DepartamentoFenix,Departamento"}, null, null, null, null, null);

        if (ciudades != null) {
            ArrayList<String> dat = new ArrayList<String>();
            for (int i = 0; i < ciudades.size(); i++) {
                String ciudad = ciudades.get(i).get(0);
                String DeparFenix = ciudades.get(i).get(1);

                if ((ciudad.equalsIgnoreCase("BogotaHFC") || ciudad.equalsIgnoreCase("BogotaREDCO"))) {
                    if (!bogota) {
                        ciudad = "Bogota - CUN";
                        bogota = true;

                        dat.add(ciudad);

                        claveValorLugarExp.add(new ItemKeyValue(ciudad, ciudades.get(i).get(1)));
                    }
                } else {
                    ciudad = ciudad + " - " + DeparFenix + "";
                    dat.add(ciudad);

                    claveValorLugarExp.add(new ItemKeyValue(ciudad, ciudades.get(i).get(2)));
                }

            }

            ArrayAdapter<String> adaptadorCiudades = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, dat);
            txtLugarExpedicion.setAdapter(adaptadorCiudades);
        }

        txtLugarExpedicion.setTextColor(getResources().getColor(R.color.black));
    }

    public void llenarProyectosRurales(ArrayList<ProyectosRurales> proyectos) {

        ArrayList<String> dat = new ArrayList<String>();
        txtProyectosRurales.setText("");

        for (int i = 0; i < proyectos.size(); i++) {
            dat.add(proyectos.get(i).getDescripcion());
            // System.out.println("barrios => " + barr.get(i).get(0));
        }

        ArrayAdapter<String> adaptadorProyectos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                dat);
        txtProyectosRurales.setAdapter(adaptadorProyectos);

        txtProyectosRurales.setTextColor(getResources().getColor(R.color.black));

        if (cliente.coberturaRural.isCoberturaRural()) {
            System.out.println("rural cliente.coberturaRural.getProyectoSeleccionado() " + cliente.coberturaRural.getProyectoSeleccionado());
            txtProyectosRurales.setText(cliente.coberturaRural.getProyectoSeleccionado());
        }

    }

    public void pintarMunicipioREDCO() {

        if (cliente.getCiudad().equalsIgnoreCase("BogotaREDCO")) {
            llenarSpinner("municipiosREDCO", sltMunicipioSSC);
        } else {

            ArrayList<String> arrayListData = new ArrayList<String>();
            arrayListData.add("N/A");

            ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    arrayListData);
            sltMunicipioSSC.setAdapter(adaptador);

            rowMunicipioREDCO.setVisibility(View.INVISIBLE);
        }
    }

    private void llenarTecnologia() {
        ArrayAdapter<String> adaptador = null;
        String datos = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre = ? and lst_ciudad = ?",
                new String[]{"tecnologia", cliente.getCiudad()}, null, null, null);
        String[] StOpt = null;
        ArrayList<String> arrayListData = new ArrayList<String>();
        arrayListData.add(Utilidades.inicial_opcion);
        if (resultado != null) {
            for (int i = 0; i < resultado.size(); i++) {
                arrayListData.add(resultado.get(i).get(0));
            }
        } else {
            arrayListData.add("N/A");
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListData);
        spnTecnologia.setAdapter(adaptador);
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

    private void llenarSpinner(String dataType, Spinner spinner) {
        ArrayAdapter<String> adaptador = null;
        String datos = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre = ?", new String[]{dataType}, null, null, null);
        String[] StOpt = null;
        ArrayList<String> arrayListData = new ArrayList<String>();
        arrayListData.add(Utilidades.inicial_opcion);
        if (resultado != null) {
            for (int i = 0; i < resultado.size(); i++) {
                if (dataType.equalsIgnoreCase("tipoConstruccion")) {
                    if (!resultado.get(i).get(0).equalsIgnoreCase("Existente")) {
                        if (Utilidades.excluirNacional(resultado.get(i).get(0), MainActivity.config.getCanal())) {
                            arrayListData.add(resultado.get(i).get(0));
                        }
                    } else {
                        arrayListData.add(resultado.get(i).get(0));
                    }
                } else {
                    arrayListData.add(resultado.get(i).get(0));
                }
            }
        } else {
            arrayListData.add("N/A");
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListData);
        spinner.setAdapter(adaptador);
    }

    private void llenarTipoZona() {
        ArrayAdapter<String> adaptador = null;

        ArrayList<String> arrayListData = new ArrayList<String>();
        arrayListData.add(Utilidades.inicial_opcion);
        arrayListData.add("Urbano");
        arrayListData.add("Rural");
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListData);
        spnTipoZona.setAdapter(adaptador);

        spnTipoZona.setOnItemSelectedListener(eventoTipoZona);
    }

    AdapterView.OnItemSelectedListener eventoTipoZona = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String tipoZona = (String) spnTipoZona.getSelectedItem();
            String barrio = txtBarrio.getText().toString();

            System.out.println("Zona rural tipoZona" + tipoZona);
            System.out.println("Zona rural barrio" + barrio);
            System.out.println("Zona rural controlProyecto" + controlProyecto);

            if (tipoZona.equalsIgnoreCase("Rural")) {
                txtPaginacion.setText("");
                txtDireccionFacturacion.setText("");
                txtDireccionNormalizada.setText("");
                if(!cliente.coberturaRural.isCoberturaRural()) {
                    spnTecnologia.setSelection(0);
                }
                cliente.setPaginaAsignacion("");
                cliente.setPaginacion("");
                cliente.setDireccionNormalizada("");

                if (!barrio.equalsIgnoreCase("")) {
                    if (!controlProyecto) {
                        ConsultarProyectos();
                        rowProyectosRurales.setVisibility(View.VISIBLE);
                        controlEstandarizar = false;
                    }
                } else {
                    limpiarSpnTipoZona();
                    controlProyecto = false;
                    controlEstandarizar = true;
                    limpiarProyecto();
                }
            } else {
                //limpiar
                arrayProyectosRurales.clear();
                if(cliente.coberturaRural != null) {
                    llenarProyectosRurales(arrayProyectosRurales);
                }
                rowProyectosRurales.setVisibility(View.GONE);
                controlProyecto = false;
                controlEstandarizar = true;
                limpiarProyecto();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener eventoProyectoRural = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//            String tipoZona = (String) spnTipoZona.getSelectedItem();
//            String barrio = txtBarrio.getText().toString();
//
//            System.out.println("Zona rural tipoZona" + tipoZona);
//            System.out.println("Zona rural barrio" + barrio);
//
//            if (tipoZona.equalsIgnoreCase("Rural")) {
//                if (!barrio.equalsIgnoreCase("")) {
//                    ConsultarProyectos();
//                    rowProyectosRurales.setVisibility(View.VISIBLE);
//                } else {
//                    limpiarSpnTipoZona();
//                }
//            } else {
//                //limpiar
//                arrayProyectosRurales.clear();
//                llenarProyectosRurales(arrayProyectosRurales);
//                rowProyectosRurales.setVisibility(View.GONE);
//            }
            Utilidades.MensajesToast(txtProyectosRurales.getText().toString(), MainActivity.context);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void limpiarSpnTipoZona() {

        Utilidades.MensajesToast(getResources().getString(R.string.validacionBarrio), this);

        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnTipoZona.getAdapter();
        spnTipoZona.setSelection(adaptador.getPosition(Utilidades.inicial_opcion));
        arrayProyectosRurales.clear();
        llenarProyectosRurales(arrayProyectosRurales);
        rowProyectosRurales.setVisibility(View.GONE);
    }

    private void llenarDepartamentoFac() {
        ArrayAdapter<String> adaptador = null;

        String datos = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(true, "Departamentos",
                new String[]{"Departamento"}, null, null, null, null, null);

        ArrayList<String> arrayListData = new ArrayList<String>();
        if (resultado != null) {
            arrayListData.add(Utilidades.seleccioneDepartamento);
            for (int i = 0; i < resultado.size(); i++) {
                arrayListData.add(resultado.get(i).get(0));
            }

        } else {
            arrayListData.add(Utilidades.iniDepartamento);
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListData);
        sltDepartamentoFacturacion.setAdapter(adaptador);

        sltDepartamentoFacturacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                String departamento = (String) sltDepartamentoFacturacion.getSelectedItem();

                if (!departamento.equalsIgnoreCase(Utilidades.iniDepartamento)
                        && !departamento.equalsIgnoreCase(Utilidades.seleccioneDepartamento)) {
                    llenarMunicipioFac(departamento);
                } else {
                    limpiarMunicipioFac();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // lblMensaje.setText("");
            }
        });

    }

    public void limpiarMunicipioFac() {
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                Utilidades.inicialMunicipio);
        sltMunicipioFacturacion.setAdapter(adaptador);
    }

    public void llenarMunicipioFac(String departamento) {

        ArrayAdapter<String> adaptador = null;
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(true, "Departamentos",
                new String[]{"Ciudad,CodigoDaneMunicipio"}, "Departamento=?", new String[]{departamento}, null,
                null, null);
        ArrayList<String> arrayListData = new ArrayList<String>();
        if (resultado != null) {

            arrayListData.add(Utilidades.seleccioneMunicipio);
            for (int i = 0; i < resultado.size(); i++) {
                arrayListData.add(resultado.get(i).get(0));
            }

        } else {
            arrayListData.add(Utilidades.iniMunicipio);
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListData);
        sltMunicipioFacturacion.setAdapter(adaptador);
    }

    private void llenarCampos() {

        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        sltFechaNacimiento.setTexto(cliente.getFechaNacimiento());
        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoDocumento.getAdapter();

        sltTipoDocumento.setSelection(adaptador.getPosition(cliente.getTipoDocumento()));
        txtDocumento.setText(cliente.getCedula());
        slfCliente.setTexto(cliente.getExpedicion());

        txtLugarExpedicion.setText(cliente.getLugarExpedicion());

        txtDireccion.setText(cliente.getDireccion());

        if (cliente.getDireccionNormalizada() != null && !cliente.getDireccionNormalizada().equalsIgnoreCase("")) {
            txtDireccionNormalizada.setText(cliente.getDireccionNormalizada());
            trTablaClienteDireccionNormalizada.setVisibility(View.VISIBLE);
        }

        if (cliente.isControlEstrato()) {
            sltEstrato.setEnabled(false);
        }

        txtCodigoHogar.setText(cliente.getCodigoHogar());
        txtPaginacion.setText(cliente.getPaginacion());
        txtContracto.setText(cliente.getContrato());
        txtBarrio.setText(cliente.getBarrio());

        txtPuntoReferencia.setText(cliente.getPuntoReferencia());
        adaptador = (ArrayAdapter<String>) sltEstrato.getAdapter();
        sltEstrato.setSelection(adaptador.getPosition(cliente.getEstrato()));
        adaptador = (ArrayAdapter<String>) spnTipoServicio.getAdapter();
        spnTipoServicio.setSelection(adaptador.getPosition(cliente.getTipoServicio()));

        if (!MainActivity.config.getDepartamento().equals("Antioquia") && cliente.getTelefono().startsWith("(")) {
            txtTelefonoServicio.setText(cliente.getTelefono().substring(3));
        } else {
            txtTelefonoServicio.setText(cliente.getTelefono());
        }

        if (!MainActivity.config.getDepartamento().equals("Antioquia")
                && cliente.getTelefonoDestino().startsWith("(")) {
            txtTelefonoDestino.setText(cliente.getTelefonoDestino().substring(3));
        } else {
            txtTelefonoDestino.setText(cliente.getTelefonoDestino());
        }
        txtTelefonoSecundario.setText(cliente.getTelefono2());
        txtCelular.setText(cliente.getCelular());

        if (!cliente.getUsermail().equals("")) {

        } else {

        }

        if (!cliente.getDominioCorreo().equals("") && !cliente.getDominioCorreo().equals(Utilidades.inicial_dominio)) {
            adaptador = (ArrayAdapter<String>) spnDominio.getAdapter();
            spnDominio.setSelection(adaptador.getPosition(cliente.getDominioCorreo()));
            txtCorreo.setText(cliente.getUsermail());
        } else {
            txtCorreo.setText(cliente.getCorreo());
        }

        if (Utilidades.excluirMunicipal("habilitarCampos", "tecnologia", cliente.getCiudad())) {
            adaptador = (ArrayAdapter<String>) spnTecnologia.getAdapter();
            spnTecnologia.setSelection(adaptador.getPosition(cliente.getTecnologia()));
        }

        txtLogin.setText(cliente.getLogin());
        txtPinHotPack.setText(cliente.getPinhp());
        txtObservaciones.setText(cliente.getObservacion());

        adaptador = (ArrayAdapter<String>) sltNivelEstudio.getAdapter();
        sltNivelEstudio.setSelection(adaptador.getPosition(cliente.getNivelEstudio()));
        adaptador = (ArrayAdapter<String>) sltProfession.getAdapter();
        sltProfession.setSelection(adaptador.getPosition(cliente.getProfesion()));
        adaptador = (ArrayAdapter<String>) sltOcupacion.getAdapter();
        sltOcupacion.setSelection(adaptador.getPosition(cliente.getOcupacion()));
        adaptador = (ArrayAdapter<String>) sltCargo.getAdapter();
        sltCargo.setSelection(adaptador.getPosition(cliente.getCargo()));
        adaptador = (ArrayAdapter<String>) sltNivelIngresos.getAdapter();
        sltNivelIngresos.setSelection(adaptador.getPosition(cliente.getNivelIngresos()));
        adaptador = (ArrayAdapter<String>) sltEstadoCivil.getAdapter();
        sltEstadoCivil.setSelection(adaptador.getPosition(cliente.getEstadoCivil()));
        adaptador = (ArrayAdapter<String>) sltGenero.getAdapter();
        sltGenero.setSelection(adaptador.getPosition(cliente.getGenero()));
        adaptador = (ArrayAdapter<String>) sltTipoVivienda.getAdapter();
        sltTipoVivienda.setSelection(adaptador.getPosition(cliente.getTipoVivienda()));
        adaptador = (ArrayAdapter<String>) sltTipoPredio.getAdapter();
        sltTipoPredio.setSelection(adaptador.getPosition(cliente.getTipoPredio()));

        adaptador = (ArrayAdapter<String>) sltTipoPropiedad.getAdapter();
        sltTipoPropiedad.setSelection(adaptador.getPosition(cliente.getTipoPropiedad()));

        adaptador = (ArrayAdapter<String>) sltTipoConstruccion.getAdapter();
        sltTipoConstruccion.setSelection(adaptador.getPosition(cliente.getTipoConstruccion()));

        adaptador = (ArrayAdapter<String>) sltPersonasCargo.getAdapter();
        sltPersonasCargo.setSelection(adaptador.getPosition(cliente.getPersonasCargo()));

        adaptador = (ArrayAdapter<String>) sltMunicipioSSC.getAdapter();
        sltMunicipioSSC.setSelection(adaptador.getPosition(cliente.getMunicipioSSC()));

        adaptador = (ArrayAdapter<String>) sltMigrar4G.getAdapter();
        sltMigrar4G.setSelection(adaptador.getPosition(cliente.getMigracion4G()));

        adaptador = (ArrayAdapter<String>) spnDomiciliacion.getAdapter();
        spnDomiciliacion.setSelection(adaptador.getPosition(cliente.getDomiciliacion()));

		shp.setTexto(cliente.getHoraDomiciliacion());

        System.out.println("cliente.getContacto2().getParentesco() " + cliente.getContacto2().getParentesco());

        adaptador = (ArrayAdapter<String>) sltParentescoContacto2.getAdapter();
        sltParentescoContacto2.setSelection(adaptador.getPosition(cliente.getContacto2().getParentesco()));

        if (!cliente.getBarrio().equals("")) {
            adaptador = (ArrayAdapter<String>) spnTipoZona.getAdapter();
            spnTipoZona.setSelection(adaptador.getPosition(cliente.getTipoZona()));

            System.out.println("rural cliente.getTipoZona(" + cliente.getTipoZona());

            if (cliente.getTipoZona().equals("Rural")) {
                System.out.println("rural cliente.coberturaRural.getArrayProyectosRurales() " + cliente.coberturaRural.getArrayProyectosRurales());
                if (cliente.coberturaRural.getArrayProyectosRurales().size() > 0) {

                    arrayProyectosRurales = cliente.coberturaRural.getArrayProyectosRurales();
                    llenarProyectosRurales(arrayProyectosRurales);
                    controlProyecto = true;
                    rowProyectosRurales.setVisibility(View.VISIBLE);
                }
            }

            System.out.println("rural txtProyectosRurales.getVisibility() " + txtProyectosRurales.getVisibility());
        }

        txtNombrePredio.setText(cliente.getNombrePredio());

        txtNombreContacto1.setText(cliente.getContacto1().getNombres());
        txtApellidoContacto1.setText(cliente.getContacto1().getApellidos());
        txtTelefonoContacto1.setText(cliente.getContacto1().getTelefono());
        txtCelularContacto1.setText(cliente.getContacto1().getCelular());
        txtParentescoContacto1.setText(cliente.getContacto1().getParentesco());
        txtNombreContacto2.setText(cliente.getContacto2().getNombres());
        txtApellidoContacto2.setText(cliente.getContacto2().getApellidos());
        txtTelefonoContacto2.setText(cliente.getContacto2().getTelefono());
        txtCelularContacto2.setText(cliente.getContacto2().getCelular());

        txtDireccionFacturacion.setText(cliente.getFacturacion().getDireccion());
        txtBarrioFacturacion.setText(cliente.getFacturacion().getBarrio());

        System.out.println("Validar correo " + cliente.isValidarCorreo());
        System.out.println("venta " + venta);
        System.out.println("deshabilitar  "
                + Utilidades.validarNacionalValor("deshabilitarCamposCliente", "deshabilitarCamposCliente"));

        if (cliente.isBloquearTelefonoDestino()) {
            txtTelefonoDestino.setEnabled(false);
        }

        if (venta != null
                && Utilidades.validarNacionalValor("deshabilitarCamposCliente", "deshabilitarCamposCliente")) {
            sltTipoDocumento.setEnabled(false);
            txtDireccion.setEnabled(false);
            txtDocumento.setEnabled(false);

            if (!cliente.getTelefono().equals("")) {
                txtTelefonoServicio.setEnabled(false);
            }

            if (!cliente.getTelefono2().equals("")) {
                txtTelefonoSecundario.setEnabled(false);
            }

            if (!cliente.getTelefono2().equals("")) {
                txtTelefonoSecundario.setEnabled(false);
            }

            if (!cliente.getCelular().equals("")) {
                txtCelular.setEnabled(false);
            }

            txtTelefonoDestino.setEnabled(false);

        }

        if (cliente.isValidarCorreo()) {
            chkValidar.setChecked(true);
        } else {
            chkValidar.setChecked(false);
        }

        if (!Utilidades.excluirGeneral("codigosPrueba", MainActivity.config.getCodigo_asesor())) {
            trTablaClienteBotonPrueba.setVisibility(View.VISIBLE);
        }

        if (cliente.coberturaRural != null) {
            System.out.println("cliente.coberturaRural.getProyectoSeleccionado() " + cliente.coberturaRural.getProyectoSeleccionado());
        }

    }

    private void llenarCamposElite() {

        if(cliente.getEstandarizarElite() == 1) {
            txtDireccion.setText(cliente.getDireccion());
        }
    }

    public void preLLenar(View v) {

        sltFechaNacimiento.setTexto("1971-12-9");

        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoDocumento.getAdapter();

        if (cliente.consolidarCliente() == null || cliente.getNombre().equalsIgnoreCase("")) {
            txtNombre.setText("Pepito");
        }
        if (cliente.consolidarCliente() == null || cliente.getApellido().equalsIgnoreCase("")) {
            txtApellido.setText("Perez");
        }
        if (cliente.consolidarCliente() == null
                || cliente.getTipoDocumento().equalsIgnoreCase(Utilidades.inicial_opcion)) {
            sltTipoDocumento.setSelection(adaptador.getPosition("CC"));
        }

        if (cliente.consolidarCliente() == null || cliente.getCedula().equalsIgnoreCase("")) {
            txtDocumento.setText("1111241111");
        }

        System.out.println("cliente.getCiudad() " + cliente.getCiudad());

        if (cliente.consolidarCliente() == null || cliente.getDireccion().equalsIgnoreCase("")) {

            if (cliente.getCiudad().equalsIgnoreCase("Medellin")) {
                //txtDireccion.setText("CL 18 B CR 88 A -26");
                //txtDireccion.setText("CL 79 C CR 75 -37 (INTERIOR 516 )");
                //txtDireccion.setText("CL 88 A  # 67 - 46 APT 601");
                txtDireccion.setText("CL 88 A # 67 - 46 APT 301");
            } else {
                txtDireccion.setText("CL 25 # 15 - 25");
            }
        } else {
            if (cliente.getCiudad().equalsIgnoreCase("Medellin")
                    || MainActivity.config.getCiudad().equals("Medellin")) {
                txtDireccion.setText("CL 64 E CR 91 B -42");
            } else {
                txtDireccion.setText("CL 25 # 15 - 25");
            }
        }

        if (cliente.consolidarCliente() == null || cliente.getApellido().equalsIgnoreCase(Utilidades.inicial_estrato)) {
            sltEstrato.setSelection(adaptador.getPosition("3"));
            adaptador = (ArrayAdapter<String>) spnTipoServicio.getAdapter();
        }
        slfCliente.setTexto("1971-12-9");

        txtLugarExpedicion.setText("Medellin - ANT");

        // txtDireccion.setText("CL 25 # 15 - 25");

        if (cliente.getDireccionNormalizada() != null && !cliente.getDireccionNormalizada().equalsIgnoreCase("")) {
            txtDireccionNormalizada.setText(cliente.getDireccionNormalizada());
            trTablaClienteDireccionNormalizada.setVisibility(View.VISIBLE);
        }

        if (cliente.isControlEstrato()) {
            sltEstrato.setEnabled(false);
        }

        txtCodigoHogar.setText(cliente.getCodigoHogar());
        txtPaginacion.setText(cliente.getPaginacion());
        txtContracto.setText(cliente.getContrato());
        txtBarrio.setText("BARRIO UNICO");

        txtPuntoReferencia.setText(cliente.getPuntoReferencia());
        adaptador = (ArrayAdapter<String>) sltEstrato.getAdapter();

        sltEstrato.setSelection(adaptador.getPosition("3"));
        adaptador = (ArrayAdapter<String>) spnTipoServicio.getAdapter();
        spnTipoServicio.setSelection(adaptador.getPosition("Hogares"));

        if (!MainActivity.config.getDepartamento().equals("Antioquia") && cliente.getTelefono().startsWith("(")) {
            txtTelefonoServicio.setText("5152510");
        } else {
            txtTelefonoServicio.setText("5152510");
        }

        if (!MainActivity.config.getDepartamento().equals("Antioquia")
                && cliente.getTelefonoDestino().startsWith("(")) {
            if (MainActivity.config.getCodigo_asesor().equals("1053778038")) {
                txtTelefonoDestino.setText("3216466574");
            } else {
                txtTelefonoDestino.setText("");
            }

        } else {
            if (MainActivity.config.getCodigo_asesor().equals("1053778038")) {
                txtTelefonoDestino.setText("3216466574");
            } else {
                txtTelefonoDestino.setText("");
            }
        }

        txtTelefonoSecundario.setText("3042301102");
        txtCelular.setText("3040000011");
        txtCorreo.setText("cliente@sincorreo.com");
        txtLogin.setText("sinlogin123411");
        txtPinHotPack.setText("3042301102");
        txtObservaciones.setText(cliente.getObservacion());

        adaptador = (ArrayAdapter<String>) sltNivelEstudio.getAdapter();
        sltNivelEstudio.setSelection(adaptador.getPosition("Universitario"));
        adaptador = (ArrayAdapter<String>) sltProfession.getAdapter();
        sltProfession.setSelection(adaptador.getPosition("Arquitecto"));
        adaptador = (ArrayAdapter<String>) sltOcupacion.getAdapter();
        sltOcupacion.setSelection(adaptador.getPosition("Empleado"));
        adaptador = (ArrayAdapter<String>) sltCargo.getAdapter();
        sltCargo.setSelection(adaptador.getPosition("Gerente"));
        adaptador = (ArrayAdapter<String>) sltNivelIngresos.getAdapter();
        sltNivelIngresos.setSelection(adaptador.getPosition("Mas de 8 SMLV"));
        adaptador = (ArrayAdapter<String>) sltEstadoCivil.getAdapter();
        sltEstadoCivil.setSelection(adaptador.getPosition("Casado"));
        adaptador = (ArrayAdapter<String>) sltGenero.getAdapter();
        sltGenero.setSelection(adaptador.getPosition("Masculino"));
        adaptador = (ArrayAdapter<String>) sltTipoVivienda.getAdapter();
        sltTipoVivienda.setSelection(adaptador.getPosition("Propia"));
        adaptador = (ArrayAdapter<String>) sltTipoPredio.getAdapter();
        sltTipoPredio.setSelection(adaptador.getPosition("Apartamento"));

        adaptador = (ArrayAdapter<String>) sltTipoPropiedad.getAdapter();
        sltTipoPropiedad.setSelection(adaptador.getPosition("Conjunto Residencial"));

        adaptador = (ArrayAdapter<String>) sltTipoConstruccion.getAdapter();
        sltTipoConstruccion.setSelection(adaptador.getPosition("Existente"));

        adaptador = (ArrayAdapter<String>) sltPersonasCargo.getAdapter();
        sltPersonasCargo.setSelection(adaptador.getPosition("1"));

        adaptador = (ArrayAdapter<String>) sltMunicipioSSC.getAdapter();
        sltMunicipioSSC.setSelection(adaptador.getPosition("N/A"));

        // se prellena el campo domicialiacion con no
        adaptador = (ArrayAdapter<String>) spnDomiciliacion.getAdapter();
        spnDomiciliacion.setSelection(adaptador.getPosition("NO"));

        // se prellena el campo domicialiacion con no
        adaptador = (ArrayAdapter<String>) spnDomiciliacion.getAdapter();
        spnDomiciliacion.setSelection(adaptador.getPosition("NO"));

        txtNombrePredio.setText(cliente.getNombrePredio());

        if (!Utilidades.excluir("excluirContacto1", cliente.getCiudad())) {
            txtNombreContacto1.setText("Pepito");
            txtApellidoContacto1.setText("Perez");
            txtTelefonoContacto1.setText("3117003400");
            txtCelularContacto1.setText(cliente.getContacto1().getCelular());
            txtParentescoContacto1.setText(cliente.getContacto1().getParentesco());
        }
        if (!Utilidades.excluir("excluirContacto2", cliente.getCiudad())) {
            txtNombreContacto2.setText("Pepito");
            txtApellidoContacto2.setText("Perez");
            txtTelefonoContacto2.setText("3117003400");
            txtCelularContacto2.setText("");
            adaptador = (ArrayAdapter<String>) sltParentescoContacto2.getAdapter();
            sltParentescoContacto2.setSelection(adaptador.getPosition("Hermano(a)"));
        }

        txtDireccionFacturacion.setText(cliente.getFacturacion().getDireccion());
        txtBarrioFacturacion.setText(cliente.getFacturacion().getBarrio());
    }

    public void buscar_cliente(View v) {
        // Identificadores();

        if (MainActivity.config.getCiudad().equals("BogotaREDCO")) {
            cliente.llenarClienteREDCO(this, txtCodigoHogar.getText().toString(), "codigoHogar", "Manual");
        }

    }

    public void buscar_datos(View v) {

        if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
            cliente.llenarConsolidadoSiebel(this,
                    MainActivity.config.getIdentificador() + txtTelefonoServicio.getText().toString(), "identificador",
                    "Manual");
        } else if (!MainActivity.config.getDepartamento().equals("Antioquia")) {
            cliente.llenarClienteManual(this,
                    MainActivity.config.getIdentificador() + txtTelefonoServicio.getText().toString());
        } else {
            cliente.llenarClienteManual(this, txtTelefonoServicio.getText().toString());
        }

    }

    public void buscarDir(View v) {

        boolean estandarizar = Utilidades.visible("estandarizarDireccion", cliente.getCiudad());
        System.out.println("estandarizar " + estandarizar);

        System.out.println("ciudad consulta " + cliente.getCiudad());
        System.out.println("Municipio Siebel " + Utilidades.visible("MunicipiosSiebel",
                cliente.getCiudad() + " ciudad consulta " + cliente.getCiudad()));

        if (Utilidades.validarDireccionesCerca(txtDireccion.getText().toString().toUpperCase())) {
            Toast.makeText(this, R.string.direccioncercaa, Toast.LENGTH_LONG).show();
            cliente.setControlCerca(true);
        } else {
            cliente.setControlCerca(false);
            if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad()) && !Utilidades.excluir("eliteMunicipios", cliente.getCiudad())) {
                estandarizarDireccion();
            } else if (Utilidades.excluir("siebelMunicipios", cliente.getCiudad())) {
                estandarizarDireccionSiebel();
            } else if (Utilidades.excluir("eliteMunicipios", cliente.getCiudad())) {
                estandarizarDireccionElite();
            }else {
                Intent intent = new Intent(MainActivity.MODULO_DIRECCIONES);
                startActivityForResult(intent, MainActivity.REQUEST_CODE);
            }
        }

    }

    public void estandarizarDireccion() {

        String municipio = "";
        String tipoEstandar = "";

        if (!txtDireccion.getText().toString().equalsIgnoreCase("")
                && !txtBarrio.getText().toString().equalsIgnoreCase("")) {

            System.out.println("controlEstandarizar "+controlEstandarizar);

            String tipoZona = (String)spnTipoZona.getSelectedItem();

            System.out.println("tipoZona "+tipoZona);

            if (controlEstandarizar && !tipoZona.equalsIgnoreCase("Rural")) {
                JSONObject jo = new JSONObject();

                try {

                    if (cliente.getCiudad().equalsIgnoreCase("BogotaREDCO")
                            || cliente.getCiudad().equalsIgnoreCase("BogotaHFC")) {
                        municipio = "Bogota";
                    } else {
                        municipio = cliente.getCiudad();
                    }

                    if (cliente.getDepartamento().equalsIgnoreCase("Antioquia")) {
                        tipoEstandar = "1";
                    } else {
                        tipoEstandar = "0";
                    }

                    jo.put("Direccion", txtDireccion.getText().toString());
                    jo.put("Municipio", municipio);
                    jo.put("MunicipioFenix", Utilidades.ciudadFenix(cliente.getCiudad(), cliente.getDepartamento()));
                    jo.put("DepartamentoFenix", Utilidades.departamentoFenix(cliente.getDepartamento()));
                    jo.put("CodigoBarrioFenix", Utilidades.traducirCodigoBarrio(txtBarrio.getText().toString()));
                    jo.put("NombreBarrio", txtBarrio.getText().toString());
                    jo.put("tipoEstandar", tipoEstandar);

                    VerificarDireccion(jo.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (!txtBarrio.getText().toString().equalsIgnoreCase("")) {
            Intent intent = new Intent(MainActivity.MODULO_DIRECCIONES);
            startActivityForResult(intent, MainActivity.REQUEST_CODE);
        } else {
            Toast.makeText(this, "El Barrio Deben Estar Diligenciados, Para Realizar La Consulta", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void estandarizarDireccionSiebel() {
        if (!txtDireccion.getText().toString().equalsIgnoreCase("")
                && !txtBarrio.getText().toString().equalsIgnoreCase("")) {
            JSONObject jo = new JSONObject();
            try {

                if (cliente.getCiudad().equalsIgnoreCase("BogotaREDCO")
                        || cliente.getCiudad().equalsIgnoreCase("BogotaHFC")) {
                    jo.put("strMunicipio", "Bogota");
                } else {
                    jo.put("strMunicipio", cliente.getCiudad());
                }

                jo.put("strDireccion", txtDireccion.getText().toString());
                jo.put("strBarrio", txtBarrio.getText().toString());
                jo.put("strDepartamento", cliente.getDepartamento());
                jo.put("strDaneDepartamento", cliente.getDepartamentoSiebel());
                jo.put("strDaneMunicipio", cliente.getCiudadSiebel());
                jo.put("strDanePais", "57");
                jo.put("strSalida", "");
                VerificarDireccionSiebel(jo.toString(), "");

            } catch (JSONException e) {

                e.printStackTrace();
            }
        } else if (!txtBarrio.getText().toString().equalsIgnoreCase("")) {
            Intent intent = new Intent(MainActivity.MODULO_DIRECCIONES);
            startActivityForResult(intent, MainActivity.REQUEST_CODE);
        } else {
            Toast.makeText(this, "El Barrio Deben Estar Diligenciados, Para Realizar La Consulta", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void estandarizarDireccionElite() {
        if (!txtDireccion.getText().toString().equalsIgnoreCase("")/*
                && !txtBarrio.getText().toString().equalsIgnoreCase("")*/) {
            JSONObject jo = new JSONObject();
            try {

                jo.put("daneDepartamento", cliente.getDepartamentoDane());
                jo.put("daneMunicipio", cliente.getCiudadDane());
                jo.put("direccion", txtDireccion.getText().toString());
                VerificarDireccionElite(jo.toString(), "integrada");

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    public void ConsultarProyectos() {

        JSONObject jo = new JSONObject();
        try {

            jo.put("strNombreBarrio", txtBarrio.getText().toString());
            jo.put("strDepartamento", cliente.getDepartamento());
            jo.put("strMunicipio", cliente.getCiudad());
            jo.put("strMunicipioFenix", cliente.getCiudadFenix());
            VerificarProyectos(jo.toString());

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public void buscar_login(View v) {
        // Identificadores();
        /*
         * Intent intent = new Intent(MainActivity.MODULO_DIRECCIONES);
		 * startActivityForResult(intent, MainActivity.REQUEST_CODE);
		 */
        System.out.println("Evento login");

        String login = txtLogin.getText().toString();
        login = login.trim();
        String tipoDocumento = (String) sltTipoDocumento.getSelectedItem();
        String documento = txtDocumento.getText().toString();
        documento = documento.trim();

        if (!documento.equalsIgnoreCase("") && !tipoDocumento.equalsIgnoreCase(Utilidades.inicial_opcion)) {
            if (login.length() > 7) {
                if (Utilidades.validateLogin(txtLogin.getText().toString())) {
                    // cliente.setLogin(txtLogin.getText().toString());
                    System.out.println("Valido " + txtLogin.getText().toString());

                    JSONObject jo = new JSONObject();

                    try {

                        jo.put("idCanal", "fenix");
                        jo.put("login", txtLogin.getText().toString());
                        jo.put("idCliente", txtDocumento.getText().toString());
                        jo.put("tipoDocumento", (String) sltTipoDocumento.getSelectedItem());
                        jo.put("tipoLogin", "une.net.co");
                        jo.put("dominio", "");
                        jo.put("nombre", "");
                        jo.put("apellido", "");
                        jo.put("servicioAsociado", "buzon une");

                        ValidarLogin(jo.toString());

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                } else {
                    // System.out.println("No cumple
                    // "+txtLogin.getText().toString());
                    Toast.makeText(this, "Login No Valido", Toast.LENGTH_SHORT).show();
                    cliente.setValidarLogin(false);
                }

            } else {
                Toast.makeText(this, "El Tamaño Del Login Debe Ser Mínimo 8 Dígitos", Toast.LENGTH_SHORT).show();
                System.out.println("vacio");
                cliente.setValidarLogin(false);
            }
        }
    }

    public void guardarCliente(View v) {

        String departamentoExpedicion = "";

        cliente.setNombre(txtNombre.getText().toString());
        cliente.setApellido(txtApellido.getText().toString());
        cliente.setFechaNacimiento(sltFechaNacimiento.getTexto());

        cliente.setTipoDocumento(sltTipoDocumento.getSelectedItem().toString());
        cliente.setCedula(txtDocumento.getText().toString());
        cliente.setExpedicion(slfCliente.getTexto());

        cliente.setLugarExpedicion(txtLugarExpedicion.getText().toString());

        for (int i = 0; i < claveValorLugarExp.size(); i++) {

            if (claveValorLugarExp.get(i).getKey().equalsIgnoreCase(cliente.getLugarExpedicion())) {
                departamentoExpedicion = claveValorLugarExp.get(i).getValues();
            }
        }

        System.out.println("departamentoExpedicion " + departamentoExpedicion);

        // cliente.setDaneLugarExpedicion(codigoDane);
        cliente.setDepartamentoExpedicion(departamentoExpedicion);

        cliente.setDireccion(txtDireccion.getText().toString());
        cliente.setPaginacion(txtPaginacion.getText().toString());
        cliente.setContrato(txtContracto.getText().toString());

        cliente.setBarrio(txtBarrio.getText().toString());

        cliente.setCodigoBarrio(Utilidades.traducirCodigoBarrio(cliente.getBarrio()));

        System.out.println("codigobARRIO " + cliente.getCodigoBarrio());

        cliente.setPuntoReferencia(txtPuntoReferencia.getText().toString());
        cliente.setEstrato(sltEstrato.getSelectedItem().toString());

        cliente.setTipoServicio("Hogares");

        if (cliente.getTelefono().startsWith("(")) {
            cliente.setTelefono(MainActivity.config.getIdentificador() + txtTelefonoServicio.getText().toString());
        } else {
            cliente.setTelefono(txtTelefonoServicio.getText().toString());
        }

        cliente.setTelefonoDestino(txtTelefonoDestino.getText().toString());
        if (!MainActivity.config.getDepartamento().equals("Antioquia")) {
            if (cliente.getTelefonoDestino().length() == 7) {
                cliente.setTelefonoDestino(
                        MainActivity.config.getIdentificador() + txtTelefonoDestino.getText().toString());
            } else if (cliente.getTelefonoDestino().length() == 10) {
                cliente.setTelefonoDestino(txtTelefonoDestino.getText().toString());
            } else {
                cliente.setTelefonoDestino("");
            }

        } else {
            if (cliente.getTelefonoDestino().length() == 7 || cliente.getTelefonoDestino().length() == 10) {
                cliente.setTelefonoDestino(txtTelefonoDestino.getText().toString());
            } else {
                cliente.setTelefonoDestino("");
            }
        }

		/*
         * System.out.println("Telefono destino => " +
		 * cliente.getTelefonoDestino());
		 */

        cliente.setTelefono2(txtTelefonoSecundario.getText().toString());
        cliente.setCelular(txtCelular.getText().toString());
        String dominio = (String) spnDominio.getSelectedItem();
        if (!dominio.equals(Utilidades.inicial_dominio)) {
            cliente.setCorreo(txtCorreo.getText().toString() + dominio);
            cliente.setUsermail(txtCorreo.getText().toString());
            cliente.setDominioCorreo(dominio);
        } else {
            cliente.setCorreo(txtCorreo.getText().toString());
            cliente.setUsermail("");
            cliente.setDominioCorreo("");
        }

        cliente.setLogin(txtLogin.getText().toString());
        cliente.setPinhp(txtPinHotPack.getText().toString());
        cliente.setObservacion(txtObservaciones.getText().toString());

        cliente.setTecnologia((String) spnTecnologia.getSelectedItem());

        cliente.setDomiciliacion((String) spnDomiciliacion.getSelectedItem());

        cliente.setNivelEstudio((String) sltNivelEstudio.getSelectedItem());
        cliente.setProfesion((String) sltProfession.getSelectedItem());
        cliente.setOcupacion((String) sltOcupacion.getSelectedItem());
        cliente.setCargo((String) sltCargo.getSelectedItem());
        cliente.setNivelIngresos((String) sltNivelIngresos.getSelectedItem());
        cliente.setEstadoCivil((String) sltEstadoCivil.getSelectedItem());
        cliente.setGenero((String) sltGenero.getSelectedItem());
        cliente.setPersonasCargo((String) sltPersonasCargo.getSelectedItem());
        cliente.setTipoVivienda((String) sltTipoVivienda.getSelectedItem());
        cliente.setTipoPredio((String) sltTipoPredio.getSelectedItem());
        cliente.setTipoPropiedad((String) sltTipoPropiedad.getSelectedItem());
        cliente.setTipoConstruccion((String) sltTipoConstruccion.getSelectedItem());
        cliente.setNombrePredio(txtNombrePredio.getText().toString());

        cliente.getContacto1().setNombres(txtNombreContacto1.getText().toString());
        cliente.getContacto1().setApellidos(txtApellidoContacto1.getText().toString());
        cliente.getContacto1().setTelefono(txtTelefonoContacto1.getText().toString());
        cliente.getContacto1().setCelular(txtCelularContacto1.getText().toString());

        // cliente.getContacto1().setParentesco((String)sltParentescoContacto1.getSelectedItem());

        cliente.getContacto1().setParentesco(txtParentescoContacto1.getText().toString());
        cliente.getContacto2().setNombres(txtNombreContacto2.getText().toString());
        cliente.getContacto2().setApellidos(txtApellidoContacto2.getText().toString());
        cliente.getContacto2().setTelefono(txtTelefonoContacto2.getText().toString());
        cliente.getContacto2().setCelular(txtCelularContacto2.getText().toString());

        cliente.getContacto2().setParentesco((String) sltParentescoContacto2.getSelectedItem());

        cliente.getFacturacion().setDepartamento((String) sltDepartamentoFacturacion.getSelectedItem());
        cliente.getFacturacion().setMunicipio((String) sltMunicipioFacturacion.getSelectedItem());
        cliente.getFacturacion().setDireccion(txtDireccionFacturacion.getText().toString());

        cliente.getFacturacion().setBarrio(txtBarrioFacturacion.getText().toString());

        cliente.setCodigoHogar(txtCodigoHogar.getText().toString());

        cliente.setMunicipioSSC((String) sltMunicipioSSC.getSelectedItem());

        cliente.setMigracion4G((String) sltMigrar4G.getSelectedItem());

        cliente.actualizarCliente();

        cliente.setTipoZona((String) spnTipoZona.getSelectedItem());

		cliente.setHoraDomiciliacion(shp.getTexto());

		System.out.println("validacion correo " + Utilidades.validateEmail(cliente.getCorreo()));

        llenadoCamposInvisibles();

        Intent intent = new Intent();
        intent.putExtra("Cliente", cliente);
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();

    }

    public void llenadoCamposInvisibles() {

        if (!Utilidades.visible("NivelEstudio", cliente.getCiudad())) {
            cliente.setNivelEstudio(Utilidades.claveValor("defaulCampos", "NivelEstudio"));
        }

        if (!Utilidades.visible("Profesion", cliente.getCiudad())) {
            cliente.setProfesion(Utilidades.claveValor("defaulCampos", "Profesion"));
        }

        if (!Utilidades.visible("Ocupacion", cliente.getCiudad())) {
            cliente.setOcupacion(Utilidades.claveValor("defaulCampos", "Ocupacion"));
        }

        if (!Utilidades.visible("Cargo", cliente.getCiudad())) {
            cliente.setCargo(Utilidades.claveValor("defaulCampos", "Cargo"));
        }

        if (!Utilidades.visible("NivelIngresos", cliente.getCiudad())) {
            cliente.setNivelIngresos(Utilidades.claveValor("defaulCampos", "NivelIngresos"));
        }

        if (!Utilidades.visible("EstadoCivil", cliente.getCiudad())) {
            cliente.setEstadoCivil(Utilidades.claveValor("defaulCampos", "EstadoCivil"));
        }

        if (!Utilidades.visible("PersonasCargo", cliente.getCiudad())) {
            cliente.setPersonasCargo(Utilidades.claveValor("defaulCampos", "PersonasCargo"));
        }

        if (!Utilidades.visible("TipoVivienda", cliente.getCiudad())) {
            cliente.setTipoVivienda(Utilidades.claveValor("defaulCampos", "TipoVivienda"));
        }

        if (!Utilidades.visible("TipoPredio", cliente.getCiudad())) {
            cliente.setTipoPredio(Utilidades.claveValor("defaulCampos", "TipoPredio"));
        }

        if (!Utilidades.visible("TipoPropiedad", cliente.getCiudad())) {
            cliente.setTipoPropiedad(Utilidades.claveValor("defaulCampos", "TipoPropiedad"));
        }

        if (!Utilidades.visible("NombrePredio", cliente.getCiudad())) {
            cliente.setNombrePredio(Utilidades.claveValor("defaulCampos", "NombrePredio"));
        }

        if (!Utilidades.visible("TipoConstruccion", cliente.getCiudad())) {
            cliente.setTipoConstruccion(Utilidades.claveValor("defaulCampos", "TipoConstruccion"));
        }

        if (!Utilidades.visible("FechaNacimiento", cliente.getCiudad())) {
            cliente.setFechaNacimiento(Utilidades.claveValor("defaulCampos", "FechaNacimiento"));
        }

        if (!Utilidades.visible("Genero", cliente.getCiudad())) {
            cliente.setGenero(Utilidades.claveValor("defaulCampos", "Genero"));
        }

        if (!Utilidades.visible("CodigoHogar", cliente.getCiudad())) {
            cliente.setCodigoHogar(Utilidades.claveValor("defaulCampos", "CodigoHogar"));
        }
    }

    public void mostrarDialogo(View v) {
        sfp = (SelectorFecha) v.getParent().getParent();
        dialogo.dialogo.show();
    }

	public void mostrarDialogoHora(View v){
		shp = (SelectorHora) v.getParent().getParent();
		dialogoHora.dialogo.show();
	}

	OnDismissListener dl = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {

            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                sfp.setTexto(dialogo.getFechaSeleccion());
            }
            dialogo.setSeleccion(false);
        }
    };

	OnDismissListener dlh = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface arg0) {

			// TODO Auto-generated method stub
			if (dialogoHora.isSeleccion()) {
				shp.setTexto(dialogoHora.getHoraSeleccion());
			}
			dialogoHora.setSeleccion(false);
		}
	};


	@Override
	public void update(Object value) {
		// TODO Auto-generated method stub
		// System.out.println("Update ControlCliente => " + value);
		ArrayList<Object> resultado = (ArrayList<Object>) value;
		System.out.println("value Control Cliente" + value);
		if (resultado.get(0).equals("Cliente")) {
			// cliente.setPortafolio(resultado.get(1).toString());
			cliente.setTelefono(txtTelefonoServicio.getText().toString());
			cliente.reorganizarCliente(resultado.get(1).toString());
			llenarCampos();
		} else if (resultado.get(0).equals("ConsolidadoSiebel")) {
			System.out.println("Control Cliente siebel");
			cliente.reorganizarClienteSiebel(resultado.get(1).toString());
			llenarCampos();
			// Resultado_Portafolio(cliente.getPortafolio());
		}else if (resultado.get(0).equals("PortafolioElite")) {
            System.out.println("Control Cliente Elite "+resultado.get(1).toString());
            cliente.reorganizarClienteElite(resultado.get(1).toString());
            llenarCamposElite();
            // Resultado_Portafolio(cliente.getPortafolio());
        } else if (resultado.get(0).equals("ConsolidarSiebel")) {
			validarConsolidar(resultado.get(1).toString());
			Utilidades.pintarBotones(resultado.get(1).toString());
		} else if (resultado.get(0).equals("ValidarLogin")) {

            System.out.println("Login " + resultado.get(1));
            resulValidarLogin(resultado.get(1).toString());

        } else if (resultado.get(0).equals("VerificarDireccion")) {

            System.out.println("Direccion " + resultado.get(1));
            validarEstandarizacion(resultado.get(1).toString());
            // resulValidarLogin(resultado.get(1).toString());

        } else if (resultado.get(0).equals("ProyectosRurales")) {

            System.out.println("ProyectosRurales " + resultado.get(1));
            validarProyectos(resultado.get(1).toString());

        } else if (resultado.get(0).equals("Portafolio")) {

            System.out.println("Portafolio " + resultado.get(1));
            cliente.setPortafolio(resultado.get(1).toString());

        } else if (resultado.get(0).equals("CoberturaNew")) {

            System.out.println("CoberturaNew " + resultado.get(1));
            validarCoberturaNew(resultado.get(1).toString());

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
    }

    public void validarCoberturaNew(String cobertura) {
        String tec = Utilidades.obtenerCobertura(cobertura, this);
        if (!tec.equalsIgnoreCase("N/D")) {
            cliente.setTecnologia(tec);
            if (Utilidades.excluirMunicipal("habilitarCampos", "tecnologia", cliente.getCiudad())) {
                ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnTecnologia.getAdapter();
                if (Utilidades.camposUnicosCiudad("bloqueoTecnologia", cliente.getCiudad()).equals("true")) {
                    cliente.setBloqueoTecnologia(true);
                    spnTecnologia.setEnabled(false);
                }
                if (adaptador != null) {
                    spnTecnologia.setSelection(adaptador.getPosition(tec));
                } else {
                    spnTecnologia.setSelection(0);
                }

            }

        } else {
            cliente.setTecnologia(Utilidades.inicial_opcion);
            if (Utilidades.excluirMunicipal("habilitarCampos", "tecnologia", cliente.getCiudad())) {
                ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnTecnologia.getAdapter();
                if (Utilidades.camposUnicosCiudad("bloqueoTecnologia", cliente.getCiudad()).equals("true")) {
                    cliente.setBloqueoTecnologia(true);
                    spnTecnologia.setEnabled(false);
                }
                if (adaptador != null) {
                    spnTecnologia.setSelection(adaptador.getPosition(Utilidades.inicial_opcion));
                } else {
                    spnTecnologia.setSelection(0);
                }

            }

        }

        System.out.println("Tecnologia " + cliente.getTecnologia());
        cliente.setCobertura(cobertura);
        // validarEstandarizacion(resultado.get(1).toString());
        // resulValidarLogin(resultado.get(1).toString());
    }

    public void validarConsolidar(String resul) {
        System.out.println("shjsjsj " + resul);
        cliente.setConsolidado(resul);

        boolean validarIdDireccionGis = false;

        try {
            JSONObject consolidado = new JSONObject(resul);
            if (consolidado.has("ListaDatosClienteVent")) {
                JSONArray arrayJson = consolidado.getJSONArray("ListaDatosClienteVent");
                System.out.println("arrayJson " + arrayJson);
                if (arrayJson.length() > 0) {
                    JSONObject cli = arrayJson.getJSONObject(0);
                    ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoDocumento.getAdapter();
                    if (cli.has("TipoDocumento")) {
                        sltTipoDocumento.setSelection(adaptador.getPosition(cli.getString("TipoDocumento")));
                    }

                    if (cli.has("Estrato")) {
                        if (!cli.getString("Estrato").equalsIgnoreCase("")
                                && !cli.getString("Estrato").equalsIgnoreCase("null")
                                && !cli.getString("Estrato").equalsIgnoreCase("0")) {
                            adaptador = (ArrayAdapter<String>) sltEstrato.getAdapter();
                            sltEstrato.setSelection(adaptador.getPosition(cli.getString("Estrato")));
                            cliente.setEstratifico(1);
                            cliente.setControlEstrato(true);
                            sltEstrato.setEnabled(false);
                        }
                    }

                    if (cli.has("Direccion")) {
                        if (!cli.getString("Direccion").equalsIgnoreCase("")
                                && !cli.getString("Direccion").equalsIgnoreCase("null")
                                && !cli.getString("Direccion").equalsIgnoreCase("0")) {
                            cliente.setDireccion(cli.getString("Direccion"));
                        }
                    }

                    if (cli.has("Cliente_id")) {
                        txtDocumento.setText(cli.getString("Cliente_id"));
                    }
                    if (cli.has("Nombres")) {
                        txtNombre.setText(cli.getString("Nombres"));
                    }
                    if (cli.has("Apellidos")) {
                        txtApellido.setText(cli.getString("Apellidos"));
                    }

                    if (cli.has("IdDireccionGis")) {
                        cliente.setIdDireccionGis(cli.getString("IdDireccionGis"));
                        validarIdDireccionGis = true;

                        if (consolidado.has("ListaResulInfraVent")) {
                            JSONObject ListaResulInfraVent = consolidado.getJSONObject("ListaResulInfraVent");

                            if (ListaResulInfraVent.has("georreferenciarCR")) {

                                JSONObject georreferenciarCR = ListaResulInfraVent.getJSONObject("georreferenciarCR");

                                System.out.println("Latitud " + georreferenciarCR.getString("Latitud"));
                                System.out.println("Longitud " + georreferenciarCR.getString("Longitud"));

                                if (!georreferenciarCR.getString("Latitud").equals("")) {
                                    cliente.setLatitud(georreferenciarCR.getString("Latitud"));
                                } else {
                                    validarIdDireccionGis = false;
                                }

                                if (!georreferenciarCR.getString("Longitud").equals("")) {
                                    cliente.setLongitud(georreferenciarCR.getString("Longitud"));
                                } else {
                                    validarIdDireccionGis = false;
                                }

                            }
                        }
                    }

                    if (cli.has("IdDireccionGisEx")) {
                        cliente.setIdDireccionGisEx(cli.getString("IdDireccionGisEx"));
                    }

                    if (cli.has("Codigo_Hogar")) {
                        cliente.setCodigoHogar(cli.getString("Codigo_Hogar"));
                    }
                    if (cli.has("Municipio")) {
                        if (!cliente.getCiudad().equalsIgnoreCase(cli.getString("Municipio"))) {
                            if (!cli.getString("Municipio").equalsIgnoreCase("Bogota")) {
                                cliente.setCiudad(cli.getString("Municipio"));
                            }
                        }
                    }

                    if (cli.has("PaginaInstalacion")) {
                        txtPaginacion.setText(cli.getString("PaginaInstalacion"));
                    } else {
                        if (consolidado.has("ListaResulInfraVent")) {
                            JSONObject infraestructura = consolidado.getJSONObject("ListaResulInfraVent");
                            if (infraestructura.has("paginaNlectura")) {
                                System.out.println("infraestructura.get(\"paginaNlectura\").getClass().getSimpleName() "+infraestructura.get("paginaNlectura").getClass().getSimpleName());
                                if(infraestructura.get("paginaNlectura").getClass().getSimpleName().equals("JSONObject")){
                                    JSONObject nlectura = infraestructura.getJSONObject("paginaNlectura");
                                    if (nlectura.has("PaginaInstalacion")) {
                                        txtPaginacion.setText(nlectura.getString("PaginaInstalacion"));
                                    }
                                }
                            }
                        }

                    }

                    if (cli.has("Localizacion")) {
                        if (!cli.getString("Localizacion").equalsIgnoreCase("00")) {
                            Utilidades.MensajesToast(getResources().getString(R.string.georeferenciacionnoprecisa),
                                    this);
                        }
                    }
                }

                if (validarIdDireccionGis) {
                    cliente.setControlClienteSiebel(true);
                    cliente.setEstandarizarSiebel(1);
                } else {
                    cliente.setEstandarizarSiebel(0);
                }

            } else {
                cliente.setEstandarizarSiebel(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("cliente.getEstandarizarSiebel() " + cliente.getEstandarizarSiebel());
    }

    public void validarEstandarizacion(String resul) {
        try {
            JSONObject resultValidacion = new JSONObject(resul);

            System.out.println("resultValidacion " + resultValidacion);

            if (resultValidacion.has("codigoRespuesta")) {

                if (resultValidacion.getString("codigoRespuesta").equalsIgnoreCase("00")) {
                    if (!resultValidacion.getString("direccionNormalizada").equalsIgnoreCase("null")) {
                        cliente.setControlNormalizada(true);

                        // txtDireccion.setText(resultValidacion
                        // .getString("direccionNormalizada"));

                        cliente.setDireccionNormalizada(resultValidacion.getString("direccionNormalizada"));
                        cliente.setDireccionConsulta(txtDireccion.getText().toString());
                        txtDireccionNormalizada.setText(cliente.getDireccionNormalizada());
                        trTablaClienteDireccionNormalizada.setVisibility(View.VISIBLE);
                        // txtDireccionNormalizada.setv

                        if (cliente.getPortafolio() == null) {
                            System.out.println("consultar portafolion");
                            consultarPortafolio(resultValidacion.getString("direccionNormalizada"));
                        }

                        // if (cliente.getCobertura() == null) {
                        System.out.println("consultar cobertura");
                        consultarCobertura(resultValidacion.getString("direccionNormalizada"));
                        // }

                        if (resultValidacion.has("codigoNormalizada")) {

                            if (resultValidacion.getString("paginaInstalacion").equalsIgnoreCase("03")) {
                                txtPaginacion.setText("");
                                cliente.setPaginaAsignacion("OFF");
                            } else {
                                if (resultValidacion.has("paginaInstalacion")) {
                                    if (!resultValidacion.getString("paginaInstalacion").equalsIgnoreCase("null")) {
                                        txtPaginacion.setText(resultValidacion.getString("paginaInstalacion"));
                                    }
                                }

                                if (resultValidacion.has("paginaAsignacion")) {
                                    if (!resultValidacion.getString("paginaAsignacion").equalsIgnoreCase("null")) {
                                        cliente.setPaginaAsignacion(resultValidacion.getString("paginaAsignacion"));
                                    }
                                }
                            }

                        } else {
                            if (resultValidacion.has("paginaInstalacion")) {
                                if (!resultValidacion.getString("paginaInstalacion").equalsIgnoreCase("null")) {
                                    txtPaginacion.setText(resultValidacion.getString("paginaInstalacion"));
                                }
                            }

                            if (resultValidacion.has("paginaAsignacion")) {
                                if (!resultValidacion.getString("paginaAsignacion").equalsIgnoreCase("null")) {
                                    cliente.setPaginaAsignacion(resultValidacion.getString("paginaAsignacion"));
                                }
                            }
                        }

                        if (!resultValidacion.getString("estrato").equalsIgnoreCase("null")
                                && !resultValidacion.getString("estrato").equalsIgnoreCase("[]")) {

                            ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltEstrato.getAdapter();

                            sltEstrato.setSelection(adaptador.getPosition(resultValidacion.getString("estrato")));
                            cliente.setEstratifico(1);
                            cliente.setControlEstrato(true);
                            sltEstrato.setEnabled(false);
                        } else {
                            cliente.setEstratifico(0);
                            cliente.setControlEstrato(false);
                            sltEstrato.setEnabled(true);
                        }

                        cliente.setConsultaNormalizada(true);
                    } else {
                        cliente.setControlNormalizada(true);
                        cliente.setConsultaNormalizada(false);
                        borrarEstandar();
                    }
                } else if (resultValidacion.getString("codigoRespuesta").equalsIgnoreCase("03")) {
                    txtPaginacion.setText("");
                    cliente.setPaginaAsignacion("OFF");
                    cliente.setControlNormalizada(true);
                    cliente.setConsultaNormalizada(false);
                    borrarEstandar();
                } else {
                    // cliente.setPaginaAsignacion("OFF");
                    cliente.setControlNormalizada(true);
                    cliente.setConsultaNormalizada(false);
                    borrarEstandar();
                }
            } else {
                // cliente.setPaginaAsignacion("OFF");
                cliente.setControlNormalizada(true);
                cliente.setConsultaNormalizada(false);
                borrarEstandar();
            }
        } catch (Exception e) {
            cliente.setPaginaAsignacion("OFF");
            Log.w("Error", e.getMessage());
            cliente.setConsultaNormalizada(false);
            cliente.setControlNormalizada(true);
            borrarEstandar();
        }

        System.out.println("cliente.getPaginaAsignacion() " + cliente.getPaginaAsignacion());
        System.out.println("txtPaginacion " + txtPaginacion.getText().toString());

    }

    public void validarProyectos(String result) {
        try {
            JSONObject resultValidacion = new JSONObject(result);

            System.out.println("resultValidacion validarProyectos" + resultValidacion);

            if (resultValidacion.has("CodigoMensaje")) {
                if (resultValidacion.getString("CodigoMensaje").equals("00")) {
                    JSONArray proyectos = resultValidacion.getJSONArray("Proyectos");
                    for (int i = 0; i < proyectos.length(); i++) {
                        JSONObject dataProyecto = proyectos.getJSONObject(i);
                       /* System.out.println("Cobertura " + dataProyecto.getString("Cobertura"));
                        System.out.println("Descripcion " + dataProyecto.getString("Descripcion"));
                        System.out.println("Proyecto " + dataProyecto.getString("Proyecto"));*/

                        arrayProyectosRurales.add(new ProyectosRurales(dataProyecto.getString("Proyecto"), dataProyecto.getString("Descripcion"), dataProyecto.getString("Cobertura")));
                        cliente.coberturaRural.setArrayProyectosRurales(arrayProyectosRurales);
                        cliente.coberturaRural.setBarrioConsulta(cliente.getBarrio());
                    }
                    llenarProyectosRurales(arrayProyectosRurales);
                }
            } else {
                arrayProyectosRurales.clear();
                llenarProyectosRurales(arrayProyectosRurales);
                cliente.coberturaRural.limpiar();
            }


        } catch (Exception e) {
            cliente.setPaginaAsignacion("OFF");
            Log.w("Error", e.getMessage());
            cliente.setConsultaNormalizada(false);
            cliente.setControlNormalizada(true);
            borrarEstandar();
        }
    }

    public void borrarEstandar() {
        cliente.setDireccionNormalizada("");
        cliente.setDireccionConsulta("");
        txtDireccionNormalizada.setText("");
        trTablaClienteDireccionNormalizada.setVisibility(View.GONE);
        cliente.setEstratifico(0);
        cliente.setControlEstrato(false);
        sltEstrato.setEnabled(true);
    }

    public void consultarPortafolio(String direccion) {
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(cliente.getTelefono());
        parametros.add(direccion);
        parametros.add(cliente.getCiudadFenix());
        parametros.add("");

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

    public void consultarCobertura(String direccion) {
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(cliente.getTelefono());
        parametros.add(direccion);
        System.out.println("cliente.getCiudadFenix() " + cliente.getCiudadFenix());
        System.out.println("cliente.getCiudad() " + cliente.getCiudad());
        if (cliente.getCiudadFenix().equalsIgnoreCase("Ninguna")) {
            cliente.setCiudadFenix(Utilidades.ciudadFenix(cliente.getCiudad(), cliente.getDepartamento()));
        }
        parametros.add(cliente.getCiudadFenix());
        parametros.add("");

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

    public void resulValidarLogin(String resul) {

		/*
		 * Toast.makeText(this, "Las Contraseñas No concuerdan",
		 * Toast.LENGTH_SHORT).show();
		 */
        try {
            JSONObject resultValidacion = new JSONObject(resul);

            if (resultValidacion.has("CodigoMensaje")) {

                if (resultValidacion.getString("CodigoMensaje").equalsIgnoreCase("00")) {
                    Toast.makeText(this, "Login Valido", Toast.LENGTH_SHORT).show();
                    cliente.setValidarLogin(true);
                } else {
                    Toast.makeText(this, resultValidacion.getString("DescripcionMensaje"), Toast.LENGTH_SHORT).show();
                    cliente.setValidarLogin(false);
                }

            } else {
                Toast.makeText(this, "No Se Puede Interpretar El Resultado", Toast.LENGTH_SHORT).show();
                cliente.setValidarLogin(false);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == MainActivity.REQUEST_CODE) {
                // cogemos el valor devuelto por la otra actividad
                String result = data.getStringExtra("result");
                if (result.equalsIgnoreCase("Direcciones")) {
                    txtDireccion.setText(data.getStringExtra("Direccion"));
                    if (Utilidades.visible("estandarizarDireccion", cliente.getCiudad())) {
                        estandarizarDireccion();
                    }
                }
            }
        } catch (Exception e) {
            Log.w("Error", "Mensaje " + e.getMessage());
        }
    }

    OnCheckedChangeListener validar = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                System.out.println("quitar");
                cliente.setValidarCorreo(true);
            } else {
                System.out.println("validar");
                cliente.setValidarCorreo(false);
            }
        }

    };

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

    public String onVisible(String tipo, String campo) {

        String visible = "invisible";
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?", new String[]{tipo, campo}, null,
                null, null);

        if (resultado != null) {
            visible = resultado.get(0).get(0);
        }

        return visible;
    }

    public void ValidarLogin(String cliente) {
        // System.out.println("Control Simulador 479 => "+Cedula);
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(cliente);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ValidarLogin");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void VerificarDireccion(String cliente) {
        // System.out.println("Control Simulador 479 => "+Cedula);
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(cliente);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("VerificarDireccion");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void VerificarDireccionSiebel(String cliente, String salida) {
        // System.out.println("Control Simulador 479 => "+Cedula);
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(cliente);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ConsolidarSiebel");
        params.add("direccion");
        params.add(parametros);
        params.add(salida);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void VerificarDireccionElite(String cliente, String tipoConsulta) {
        // System.out.println("Control Simulador 479 => "+Cedula);
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(cliente);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("PortafolioElite");
        params.add(parametros);
        params.add(tipoConsulta);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void VerificarProyectos(String cliente) {
        // System.out.println("Control Simulador 479 => "+Cedula);
        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(cliente);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ProyectosRurales");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void llenarDominios() {

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre=?", new String[]{"dominios"}, null, null, null);

        System.out.println("respuesta " + respuesta);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        adaptador.add(Utilidades.inicial_dominio);

        if (respuesta != null) {
            for (int i = 0; i < respuesta.size(); i++) {
                System.out.println(respuesta.get(i).get(0));
                adaptador.add(respuesta.get(i).get(0));
            }
        }

        spnDominio.setAdapter(adaptador);

	}

	AdapterView.OnItemSelectedListener ocultarMostarFranja = new AdapterView.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if(parent.getSelectedItem().toString().equalsIgnoreCase("SI")){
				rowClienteHoraDomiciliacion.setVisibility(View.VISIBLE);
			} else {
				rowClienteHoraDomiciliacion.setVisibility(View.GONE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};
}
