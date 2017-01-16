package co.com.une.appmovilesune;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import com.google.analytics.tracking.android.EasyTracker;
import com.itextpdf.text.log.SysoCounter;

import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.change.Interprete;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.BaseDatos;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.complements.Validaciones;
import co.com.une.appmovilesune.components.BotonPrincipal;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.controller.ControlConfiguracion;
import co.com.une.appmovilesune.controller.ControlPrecision;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Asesoria;
import co.com.une.appmovilesune.model.Blindaje;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Competencia;
import co.com.une.appmovilesune.model.Configuracion;
import co.com.une.appmovilesune.model.Contacto;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.Facturacion;
import co.com.une.appmovilesune.model.Localizacion;
import co.com.une.appmovilesune.model.Peticion;
import co.com.une.appmovilesune.model.Prospecto;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.UneMas;
import co.com.une.appmovilesune.model.Venta;
import co.com.une.appmovilesune.threads.Enviar;
import co.com.une.appmovilesune.R;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import static co.com.une.appmovilesune.MainActivity.MODULO_COTIZADOR;

public class MainActivity extends Activity implements Observer {

    public static final int REQUEST_CODE = 10;
    public static final int OK_RESULT_CODE = 1;

    private static final String PREFERENCIAS = "VentaMovilPreferencias";

	/*
     * Definimos los modulos que vienen a ser los controladores generalmente
	 * para poderlos trabajar
	 */

	/*
	 * proyecto integrado produccion direccionamiento IP y bronze 2, silver
	 * premium y carrusel
	 */

    public static final String MODULO_COMPETENCIA = "co.com.une.appmovilesune.ControlCompetencia";
    public static final String MODULO_CLIENTE = "co.com.une.appmovilesune.ControlCliente";
    public static final String MODULO_PETICION = "co.com.une.appmovilesune.ControlPeticion";
    public static final String MODULO_OBSEQUIO = "co.com.une.appmovilesune.ControlObsequio";
    public static final String MODULO_TARIFICADOR = "co.com.une.appmovilesune.ControlTarificador";
    public static final String MODULO_VENTA = "co.com.une.appmovilesune.ControlVenta";
    public static final String MODULO_AUTENTICACION = "co.com.une.appmovilesune.Autenticacion";
    public static final String MODULO_RESUMEN = "co.com.une.appmovilesune.ControlResumen";
    public static final String MODULO_ACTUALIZACION = "co.com.une.appmovilesune.ControlActualizacion";
    public static final String MODULO_PEDIDO = "co.com.une.appmovilesune.ControlPedido";
    public static final String MODULO_SIMULADOR = "co.com.une.appmovilesune.Simulador";
    public static final String MODULO_INICIALIZACION = "co.com.une.appmovilesune.ControlInicializacion";
    public static final String MODULO_PROSPECTO = "co.com.une.appmovilesune.ControlProspecto";
    public static final String MODULO_VALIDADOR = "co.com.une.appmovilesune.ControlValidador";
    public static final String MODULO_DIRECCIONES = "co.com.une.appmovilesune.Direcciones";
    public static final String MODULO_RESUMEN_PREVENTA = "co.com.une.appmovilesune.Resumen";
    public static final String MODULO_LISTAS = "co.com.une.appmovilesune.Listas";
    public static final String MODULO_PRECISION = "co.com.une.appmovilesune.ControlPrecision";
    public static final String MODULO_BLINDAJE = "co.com.une.appmovilesune.ControlBlindaje";
    public static final String MODULO_DETALLE_BLINDAJE = "co.com.une.appmovilesune.ControlDetalleBlindaje";
    public static final String MODULO_MENSAJES = "co.com.une.appmovilesune.Mensajes";
    public static final String MODULO_UNEMAS = "co.com.une.appmovilesune.ControlUneMas";
    public static final String MODULO_BUSQUEDA_AMC = "co.com.une.appmovilesune.ControlBusquedaAmigoCuentas";
    public static final String MODULO_AMIGOCUENTAS = "co.com.une.appmovilesune.ControlAmigoCuentas";
    public static final String MODULO_AGENDABOGOTA = "co.com.une.appmovilesune.AgendaBogota";
    public static final String MODULO_DETALLE_PRODUCTO = "co.com.une.appmovilesune.ControlDetalleProducto";
    public static final String MODULO_AMC_DIGITAL = "co.com.une.appmovilesune.ControlAmigoCuentasDigital";
    public static final String MODULO_CARRUSEL = "co.com.une.appmovilesune.ControlCarrusel";
    public static final String MODULO_COTIZADOR = "co.com.une.appmovilesune.ControlCotizador";

    /* Servidor de conexion */
    public static boolean debug = true;
    public static String servidor = Conexion.SERVIDOR_DESARROLLO;//
    public static boolean seguimiento;

	/* Para ponerles los titulos en el component */

    private TituloPrincipal tp;

    public static SharedPreferences preferencias;
    public static Conexion conexion = new Conexion(servidor);
    public static Configuracion config;
    public static BaseDatos basedatos;
    public static ControlConfiguracion ccfg;
    public static Context context;
    private Enviar enviar;

    public static String lanzador;
    public static Observer obsrMainActivity, obsrTarifas, obsrVenta, obsrCliente, obsrBlindaje, obsrUneMas,
            obrsProspecto;

    private Asesoria asesoria;
    private Dialogo dialogo;
    private String modulo;
    public String IVR = "";

    public static ArrayList<String> ivr;

    /* Cada uno de los botones que encontramos en el menu principal */
    public static BotonPrincipal btnCliente, btnCompetencia, btnPeticion, btnTarificador, btnPortafolio, btnCobertura,
            btnCarteraUne, btnProspecto, btnPrecision, btnBlindaje, btnUneMas, btnAmigoCuentas, btnCarrusel;
    /* Para los mensajes por medio de dialogos */
    public static ProgressDialog pd;

    public static ArrayList<ListaDefault> listDocumentacion = new ArrayList<ListaDefault>();
    public static boolean permanecia = false;

    private LocationManager locManager;
    private LocationListener locListenerGPS;
    private LocationListener locListenerRed;

    private ArrayList<String[]> localizaciones;

    /*
     * Cuando el boton de
     * "volver atras se presione... se muestra el siguiente dialogo"
     */
    public void onBackPressed() {
        dialogo = new Dialogo(this, Dialogo.DIALOGO_CONFIRMACION, getResources().getString(R.string.cerraraplicacion));
        dialogo.dialogo.setOnDismissListener(dls);
        dialogo.dialogo.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/*
		 * El primer layout que ponemos es el del menu con los modulos posibles
		 * a ejecutar
		 */
        setContentView(R.layout.activity_main);

        context = this;
        obsrMainActivity = this;
        preferencias = getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);

        conexion.addObserver(this);

		/* Establecemos el titulo del layout activity_main */

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo(getResources().getString(R.string.ventamovil));

        btnCliente = (BotonPrincipal) findViewById(R.id.btpCliente);
        btnCompetencia = (BotonPrincipal) findViewById(R.id.btpCompetencia);
        btnTarificador = (BotonPrincipal) findViewById(R.id.btpTarificador);
        btnPortafolio = (BotonPrincipal) findViewById(R.id.btpPortafolio);
        btnCobertura = (BotonPrincipal) findViewById(R.id.btpCobertura);
        btnCarteraUne = (BotonPrincipal) findViewById(R.id.btpCuentasUne);
        btnProspecto = (BotonPrincipal) findViewById(R.id.btpProspecto);
        btnPrecision = (BotonPrincipal) findViewById(R.id.btpPrecision);
        btnBlindaje = (BotonPrincipal) findViewById(R.id.btpBlindaje);
        btnUneMas = (BotonPrincipal) findViewById(R.id.btpUneMas);
        btnAmigoCuentas = (BotonPrincipal) findViewById(R.id.btpAmigoCuentas);
        btnCarrusel = (BotonPrincipal) findViewById(R.id.btpCarrusel);

        lanzarInicializador();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (seguimiento) {
            EasyTracker.getInstance(this).activityStart(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (seguimiento) {
            EasyTracker.getInstance(this).activityStop(this);
        }
    }

    /*
     * Funcion que lanza el dialogo que contiene la ciudad en la cual se va a
     * crear la asesoria
     */
    private void crearAsesoria(String modulo) {
        dialogo = new Dialogo(this, Dialogo.DIALOGO_ASESORIA, "");
        dialogo.dialogo.setOnDismissListener(dl);
        dialogo.dialogo.show();
        Spinner sltCiudad = (Spinner) dialogo.dialogo.findViewById(R.id.sltCiudad);
        ArrayAdapter<String> adaptador = dialogo.fr.obtenerCiudades(config.getDepartamento());
        sltCiudad.setAdapter(adaptador);
        sltCiudad.setSelection(adaptador.getPosition(MainActivity.config.getCiudad()));
    }

    /* Funcion que retorna si hay asesoria o no lo hay, retorna true o false */
    private boolean validarAsesoria() {
        boolean existe = false;
        if (asesoria != null) {
            existe = true;
        }
        return existe;
    }

    /*
     * Al dar click en el boton principal de TARIFICADOR, lanzamos esta funcion
     */
    public void mostrarTarificador(View v) {
		/* Primero validamos si hay o no asesoria */

//        Intent intent = new Intent(MODULO_COTIZADOR);
//        System.out.println("cliente " + asesoria.cliente);
//        intent.putExtra("cliente", asesoria.cliente);
//        startActivity(intent);
//        modulo = MODULO_COTIZADOR;

        if (validarAsesoria()) {

            if (asesoria.cotizacion != null && asesoria.venta != null) {
                // En caso de que exista asesoria guardada y sea cargada
                dialogo = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_RECUPERADOR, "Por donde quiere seguir.");
                dialogo.dialogo.setOnDismissListener(dlr);
                dialogo.dialogo.show();
            } else {

                System.out.println("ingreso tarificador");

                if (!asesoria.cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)) {
                    if (asesoria.cliente.getCobertura() != null
                            || Utilidades.excluir("siebelMunicipios",
                            asesoria.cliente.getCiudad())
                            || Utilidades.excluir("eliteMunicipios",
                            asesoria.cliente.getCiudad()) || asesoria.cliente.isControlCerca()) {
                        System.out.println("asesoria.cliente.getTecnologia() " + asesoria.cliente.getTecnologia());
                        if (asesoria.cliente.getTecnologia() != null) {
                            if (!asesoria.cliente.getTecnologia().equals(Utilidades.inicial_opcion)) {
                                /*lanzarModulo(MODULO_TARIFICADOR);
                                modulo = MODULO_TARIFICADOR;
                                ccfg.setLanzador("tarificador");*/
                                lanzarModulo(MODULO_COTIZADOR);
                                modulo = MODULO_COTIZADOR;

                            } else {
                                        Toast.makeText(this, getResources().getString(R.string.seleccionartecnologia),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.seleccionartecnologia),
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Debe consultar la cobertura del cliente.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Debe Seleccionar el Estrato.", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            System.out.println("ingreso asesoria tarificador");

            lanzarModulo(MODULO_TARIFICADOR);
            modulo = MODULO_TARIFICADOR;
            ccfg.setLanzador("tarificador");

        }

    }

    public boolean ValidarCartera() {
        boolean validarCartera = true;

        if (asesoria.scooring != null) {
            if (asesoria.scooring.isValidarCartera()) {
                if (asesoria.cliente.getTipoCliente() == 2) {
                    Toast.makeText(this, getResources().getString(R.string.verificarestadocliente), Toast.LENGTH_SHORT)
                            .show();
                    validarCartera = false;
                }
            }
        } else {
            Toast.makeText(this,
                    "Debe verificar el estado del cliente en Estado Cuenta. El cliente no fue consultado exitosamente.",
                    Toast.LENGTH_SHORT).show();
            validarCartera = false;
        }

        return validarCartera;
    }

    public boolean validarScooring() {

        System.out.println(asesoria.cliente.getScooringune().getEstado());

        boolean pasa = false;

        if (Utilidades.excluir("habilitarScooring", asesoria.cliente.getCiudad())) {

            if (!asesoria.cliente.getScooringune().isValidarScooring()) {
                pasa = true;
            } else {

                System.out.println("asesoria.cliente.getCedula() " + asesoria.cliente.getCedula());

                System.out.println("asesoria.cliente.getScooringune().getDocumentoScooring() "
                        + asesoria.cliente.getScooringune().getDocumentoScooring());

                if (asesoria.cliente.getCedula()
                        .equalsIgnoreCase(asesoria.cliente.getScooringune().getDocumentoScooring())) {
                    if (asesoria.cliente.getScooringune().isPasaScooring()) {
                        // asesoria.cliente.setTipoCliente(1);
                        pasa = true;

                        if (asesoria.scooring == null) {
                            asesoria.scooring = new Scooring();
                        }

                        asesoria.scooring.setCarteraUNE("", "", "", "", asesoria.cliente.getCedula());

                        asesoria.cliente.setTipoCliente(1);

                    } else {

                        if (!asesoria.cliente.getScooringune().getIdScooring().equalsIgnoreCase("")) {
                            Utilidades.MensajesToast("El usuario no paso el Scooring", this);
                        }
                        pasa = false;
                    }
                } else {
                    Utilidades.MensajesToast("No Se Puede cambiar el documento de Consulta, Consulte Nuevamente", this);

                    asesoria.cliente.getScooringune().setIdScooring("");
                    asesoria.cliente.getScooringune().setPasaScooring(false);
                    asesoria.cliente.getScooringune().setValidarScooring(true);

                }
            }

        }

        System.out.println("pasa scooring " + pasa);

        return pasa;
    }

    public void mostrarCliente(View v) {
        lanzarModulo(MODULO_CLIENTE);
        modulo = MODULO_CLIENTE;
        ccfg.setLanzador("cliente");
    }

    public void mostrarCompetencia(View v) {
        lanzarModulo(MODULO_COMPETENCIA);
        modulo = MODULO_COMPETENCIA;
        ccfg.setLanzador("competencia");
    }

    public void mostrarPeticion(View v) {
        lanzarModulo(MODULO_PETICION);
        modulo = MODULO_PETICION;
        ccfg.setLanzador("peticion");
    }

    public void mostrarProspecto(View v) {
        lanzarModulo(MODULO_PROSPECTO);
        modulo = MODULO_PROSPECTO;
        ccfg.setLanzador("prospecto");
    }

    public void mostrarPortafolio(View v) {
        lanzarModulo(MODULO_SIMULADOR);
        modulo = MODULO_SIMULADOR;
        ccfg.setLanzador("Portafolio");
    }

    public void mostrarCobertura(View v) {
        lanzarModulo(MODULO_SIMULADOR);
        modulo = MODULO_SIMULADOR;
        ccfg.setLanzador("Cobertura");
    }

    public void mostrarPrecision(View v) {
        Intent intent = new Intent(context, ControlPrecision.class);
        startActivity(intent);
        modulo = MODULO_PRECISION;
    }

    public void mostrarUneMas(View v) {
        lanzarModulo(MODULO_UNEMAS);
        modulo = MODULO_UNEMAS;
        ccfg.setLanzador("UneMas");

    }

    public void mostrarBlindaje(View v) {
        lanzarModulo(MODULO_BLINDAJE);
        modulo = MODULO_BLINDAJE;
    }

    public void mostrarCarrusel(View v) {
        lanzarModulo(MODULO_CARRUSEL);
        modulo = MODULO_CARRUSEL;
    }

    public void mostrarAmigoCuentas(View v) {

        if (validarAsesoria()) {
            if (asesoria.cliente != null) {
                if (!asesoria.cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)) {
                    if (asesoria.cliente.getTecnologia() != null) {
                        if (asesoria.cliente.ac != null) {
                            if (asesoria.cliente.ac.tipo != null) {
                                if (asesoria.cliente.ac.tipo.equals("Oferta")) {
                                    if (!asesoria.cliente.ac.productosPortafolio.isEmpty()) {
                                        if (asesoria.cliente.ac.modulo.equals("digital")) {
                                            lanzarModulo(MODULO_AMC_DIGITAL);
                                            modulo = MODULO_AMC_DIGITAL;
                                        } else {
                                            lanzarModulo(MODULO_AMIGOCUENTAS);
                                            modulo = MODULO_AMIGOCUENTAS;
                                        }
                                    } else {
                                        lanzarModulo(MODULO_BUSQUEDA_AMC);
                                        modulo = MODULO_BUSQUEDA_AMC;
                                    }
                                } else {
                                    lanzarModulo(MODULO_BUSQUEDA_AMC);
                                    modulo = MODULO_BUSQUEDA_AMC;
                                }
                            } else {
                                lanzarModulo(MODULO_BUSQUEDA_AMC);
                                modulo = MODULO_BUSQUEDA_AMC;
                            }

                        } else {
                            lanzarModulo(MODULO_BUSQUEDA_AMC);
                            modulo = MODULO_BUSQUEDA_AMC;
                        }
                    } else {
                        Toast.makeText(this, "Debe Seleccionar la tecnologia en info cliente.", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    Toast.makeText(this, "Debe Seleccionar el Estrato.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            lanzarModulo(MODULO_AMIGOCUENTAS);
            modulo = MODULO_AMIGOCUENTAS;
        }
    }

    public void lanzarAmigoCuentas() {
        lanzarModulo(MODULO_AMIGOCUENTAS);
        modulo = MODULO_AMIGOCUENTAS;
    }

    public void lanzarAmigoCuentaDigital() {
        lanzarModulo(MODULO_AMC_DIGITAL);
        modulo = MODULO_AMC_DIGITAL;
    }

    public void mostrarCuentasUNE(View v) {
        lanzarModulo(MODULO_SIMULADOR);
        modulo = MODULO_SIMULADOR;
        ccfg.setLanzador("CuentasUNE");
    }

    public void mostrarEstadoCuenta() {
        lanzarModulo(MODULO_SIMULADOR);
        modulo = MODULO_SIMULADOR;
        ccfg.setLanzador("CuentasUNE");
    }

    public void mostrarPedido(View v) {
        Intent intent = new Intent(MODULO_PEDIDO);
        // intent.putExtra("cliente", asesoria.cliente);
        startActivityForResult(intent, REQUEST_CODE);
        modulo = MODULO_PEDIDO;
		/*
		 * lanzarModulo(MODULO_PEDIDO); modulo = MODULO_PEDIDO;
		 * ccfg.setLanzador("CuentasUNE");
		 */
    }

    public void lanzarInicializador() {
        Intent intent = new Intent(MODULO_INICIALIZACION);
        startActivityForResult(intent, REQUEST_CODE);
        modulo = MODULO_INICIALIZACION;
    }

    public void mostrarValidador(View v) {

        try {

            if (asesoria != null && asesoria.scooring != null) {
                // if(asesoria.scooring.getAccion().equalsIgnoreCase(string))

                if (asesoria.cliente.getCedula().equalsIgnoreCase(asesoria.scooring.getIdCliente())) {

                    if (asesoria.scooring.getAccion().equalsIgnoreCase("Verificar Cifin")) {
                        Intent intent = new Intent(MODULO_VALIDADOR);
                        intent.putExtra("cliente", asesoria.cliente);
                        intent.putExtra("scooring", asesoria.scooring);
                        startActivityForResult(intent, REQUEST_CODE);
                        modulo = MODULO_VALIDADOR;
                    } else {
                        Toast.makeText(this, "Solo se pueden Verificar Clientes Nuevos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Documento del cliente vs Documento Consulta Cuentas Diferente",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Debe verificar el estado del cliente en Estado Cuenta", Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (NullPointerException e) {
            Log.w("Error ", e.getMessage());

        }

    }

    private void lanzarAutenticacion() {
        Intent intent = new Intent(MODULO_AUTENTICACION);
        startActivityForResult(intent, REQUEST_CODE);
        modulo = MODULO_AUTENTICACION;
    }

    private void lanzarVenta() {
        lanzarModulo(MODULO_VENTA);
        modulo = MODULO_VENTA;
    }

    private void lanzarCompetencia() {
        lanzarModulo(MODULO_COMPETENCIA);
        modulo = MODULO_COMPETENCIA;
    }

    private void lanzarCarrusel() {
        lanzarModulo(MODULO_CARRUSEL);
        modulo = MODULO_CARRUSEL;
    }

    public void mostrarElearning(View v) {
        Uri uri = Uri.parse("http://aprendernosune.une.net.co/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void lanzarActualizacion() {
        Intent intent = new Intent(MODULO_ACTUALIZACION);
        startActivityForResult(intent, REQUEST_CODE);
        modulo = MODULO_ACTUALIZACION;
    }

    private void lanzarModulo(String modulo) {
        if (!config.getCiudad().equals("--Seleccione Ciudad--")) {
            if (config.validarConfiguraciones() || servidor.equals(Conexion.SERVIDOR_PRUEBAS)) {
                if (!validarAsesoria()) {
                    crearAsesoria(modulo);
                } else {
                    Intent intent = new Intent(modulo);
                    if (modulo.equals(MODULO_CLIENTE)) {
                        System.out.println("Lanzando Clinete " + asesoria.cliente);
                        intent.putExtra("cliente", asesoria.cliente);
                        intent.putExtra("venta", asesoria.venta);
                    } else if (modulo.equals(MODULO_COMPETENCIA)) {
                        intent.putExtra("competencia", asesoria.competencia);
                        intent.putExtra("cliente", asesoria.cliente);
                    } else if (modulo.equals(MODULO_PETICION)) {
                        intent.putExtra("peticion", asesoria.peticion);
                    } else if (modulo.equals(MODULO_SIMULADOR)) {
                        intent.putExtra("cliente", asesoria.cliente);
                        intent.putExtra("scooring", asesoria.scooring);
                    } else if (modulo.equals(MODULO_PEDIDO)) {
                        intent.putExtra("cliente", asesoria.cliente);
                        // 1intent.putExtra("scooring", asesoria.scooring);
                    } else if (modulo.equals(MODULO_VENTA)) {
                        IVR = "";
                        intent.putExtra("departamento", asesoria.cliente.getDepartamento());
                        intent.putExtra("municipio", asesoria.cliente.getCiudad());
                        intent.putExtra("barrio", asesoria.cliente.getBarrio());
                        intent.putExtra("tipoPredio", asesoria.cliente.getTipoPredio());
                        intent.putExtra("tipoPropiedad", asesoria.cliente.getTipoPropiedad());
                        intent.putExtra("consultaNormalizada", asesoria.cliente.isConsultaNormalizada());
                        intent.putExtra("cotizacion", asesoria.cotizacion);
                        intent.putExtra("venta", asesoria.venta);
                        intent.putExtra("cliente", asesoria.cliente);
                    } else if (modulo.equals(MODULO_TARIFICADOR)) {

                        if (!asesoria.cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)) {

                            intent.putExtra("id_asesoria", asesoria.getId());
                            intent.putExtra("cotizacion", asesoria.cotizacion);
                            intent.putExtra("cliente", asesoria.cliente);
                            intent.putExtra("scooring", asesoria.scooring);

                        } else {
                            Toast.makeText(this, "Debe Seleccionar el Estrato.", Toast.LENGTH_SHORT).show();
                        }

                    }else if (modulo.equals(MODULO_COTIZADOR)) {

                        if (!asesoria.cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)) {

                            intent.putExtra("id_asesoria", asesoria.getId());
                            intent.putExtra("cotizacion", asesoria.cotizacion);
                            intent.putExtra("cliente", asesoria.cliente);
                            intent.putExtra("scooring", asesoria.scooring);

                        } else {
                            Toast.makeText(this, "Debe Seleccionar el Estrato.", Toast.LENGTH_SHORT).show();
                        }

                    } else if (modulo.equals(MODULO_PROSPECTO)) {
                        intent.putExtra("cliente", asesoria.cliente);
                        intent.putExtra("prospecto", asesoria.prospecto);
                    } else if (modulo.equals(MODULO_BLINDAJE)) {
                        intent.putExtra("cliente", asesoria.cliente);
                    } else if (modulo.equals(MODULO_UNEMAS)) {
                        intent.putExtra("cliente", asesoria.cliente);
                        intent.putExtra("unemas", asesoria.unemas);
                        intent.putExtra("IVR", IVR);
                    } else if (modulo.equals(MODULO_AMIGOCUENTAS)) {

                        if (!asesoria.cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)) {
                            intent.putExtra("cliente", asesoria.cliente);
                            intent.putExtra("scooring", asesoria.scooring);
                        } else {
                            Toast.makeText(this,
                                    "Debe verificar el estado del cliente en Estado Cuenta. El cliente no fue consultado exitosamente.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else if (modulo.equals(MODULO_BUSQUEDA_AMC)) {
                        System.out.println("Cliente");
                        intent.putExtra("cliente", asesoria.cliente);

                    } else if (modulo.equals(MODULO_AMC_DIGITAL)) {
                        if (!asesoria.cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)) {
                            intent.putExtra("cliente", asesoria.cliente);
                            intent.putExtra("scooring", asesoria.scooring);

                        } else {
                            Toast.makeText(this,
                                    "Debe verificar el estado del cliente en Estado Cuenta. El cliente no fue consultado exitosamente.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else if (modulo.equals(MODULO_CARRUSEL)) {
                        intent.putExtra("cliente", asesoria.cliente);
                    }
                    startActivityForResult(intent, REQUEST_CODE);
                }
            } else {
                Toast.makeText(this, "Actualizar Base de datos", Toast.LENGTH_SHORT).show();
            }
        } else {
            ccfg.mostrarDialogoConfiguracion(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE) {

                System.out.println("modulo onActivityResult "+modulo);

                if (modulo.equals(MODULO_CLIENTE)) {
                    asesoria.cliente = (Cliente) data.getSerializableExtra("Cliente");
                    System.out.println("asesoria.cliente.getPortafolio() " + asesoria.cliente.getPortafolio());
                } else if (modulo.equals(MODULO_COMPETENCIA)) {
                    asesoria.competencia = (Competencia) data.getSerializableExtra("competencia");
                    asesoria.cliente = (Cliente) data.getSerializableExtra("cliente");
                    asesoria.competencia.guardarCompetencia(asesoria.getId());
                    btnCompetencia.setOK();
                } else if (modulo.equals(MODULO_PETICION)) {
                    asesoria.peticion = (Peticion) data.getSerializableExtra("peticion");
                    asesoria.peticion.guardarPeticion(asesoria.getId());
                    btnPeticion.setOK();
                } else if (modulo.equals(MODULO_PROSPECTO)) {
                    asesoria.prospecto = (Prospecto) data.getSerializableExtra("prospecto");
					/*
					 * System.out.println("productos " +
					 * asesoria.prospecto.getProductos());
					 */
                    asesoria.prospecto.guardarProspecto(asesoria.getId());
                    btnProspecto.setOK();
                } else if (modulo.equals(MODULO_AUTENTICACION)) {
                    String result = data.getStringExtra("result");
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                    if (result.equalsIgnoreCase("Bienvenido")) {
						/*
						 * enviar = new Enviar(this); IniciarServicio();
						 */
                    }
                    if (Utilidades.visibleDepartamento("BlindajeManizales", MainActivity.config.getDepartamento())) {
                        btnBlindaje.setVisibility(View.VISIBLE);
                    }
                    if (Utilidades.visible("AmigoCuentas", config.getCiudad())) {
                        btnAmigoCuentas.setVisibility(View.VISIBLE);
                    }

                    // config.obtenerConfiguracionesGlobales();
                } else if (modulo.equals(MODULO_SIMULADOR)) {
                    asesoria.cliente = (Cliente) data.getSerializableExtra("Cliente");
                    asesoria.scooring = (Scooring) data.getSerializableExtra("Scooring");
					/*
					 * System.out.println("tipo Cliente " +
					 * asesoria.cliente.getTipoCliente());
					 */

                    System.out.println("asesoria.scooring " + asesoria.scooring);
                    System.out.println("idIVR " + asesoria.cliente.getIdIVR());

                    if (asesoria.scooring != null) {
                        System.out.println(asesoria.scooring.getCarteraUNE());
                        if (asesoria.scooring.getCarteraUNE()[1].equalsIgnoreCase("No Existe")) {
                            // btnPrecision.setVisibility(View.VISIBLE);
                            asesoria.cliente.setTipoCliente(0);

                        } else if (!asesoria.scooring.getCarteraUNE()[1].equalsIgnoreCase("No Existe")) {
                            asesoria.cliente.setTipoCliente(1);
                        }
                    }

                    System.out.println("asesoria.cliente.getScooringune().getDocumentoScooring() "
                            + asesoria.cliente.getScooringune().getDocumentoScooring());

                    System.out.println("asesoria.cliente.getScooringune().getEstado() "
                            + asesoria.cliente.getScooringune().getEstado());

                    asesoria.cliente.guardarCliente(asesoria.getId());
					/*
					 * System.out.println("tipo Cliente " +
					 * asesoria.cliente.getTipoCliente());
					 */
                } else if (modulo.equals(MODULO_VALIDADOR)) {
                    asesoria.cliente = (Cliente) data.getSerializableExtra("Cliente");
                    asesoria.scooring = (Scooring) data.getSerializableExtra("Scooring");
					/*
					 * System.out.println("tipo Cliente " +
					 * asesoria.cliente.getTipoCliente());
					 */
                    if (asesoria.scooring != null) {
                        System.out.println(
                                "asesoria.scooring.getEstadoValidador() " + asesoria.scooring.getEstadoValidador());
                        System.out.println("asesoria.scooring.getIdCliente() " + asesoria.scooring.getIdCliente());
                        System.out.println("asesoria.scooring.getAccion() " + asesoria.scooring.getAccion());
                    }

                    asesoria.cliente.guardarCliente(asesoria.getId());
					/*
					 * System.out.println("tipo Cliente " +
					 * asesoria.cliente.getTipoCliente());
					 */
                } else if (modulo.equals(MODULO_TARIFICADOR) || modulo.equals(MODULO_COTIZADOR)) {
                    asesoria.cotizacion = (Cotizacion) data.getSerializableExtra("cotizacion");
                    asesoria.cliente = (Cliente) data.getSerializableExtra("cliente");

                    String tipoIngreso = data.getStringExtra("tipoIngreso");

                    if (tipoIngreso.equalsIgnoreCase("venta")) {
                        lanzarVenta();
                    } else if (tipoIngreso.equalsIgnoreCase("estado cuenta")) {
                        mostrarEstadoCuenta();
                    } else if (tipoIngreso.equalsIgnoreCase("gerencia de ruta")) {
                        lanzarCompetencia();
                    } else if (tipoIngreso.equalsIgnoreCase("carrusel")) {
                        lanzarCarrusel();
                    }
                } else if (modulo.equals(MODULO_VENTA)) {
                    asesoria.venta = (Venta) data.getSerializableExtra("Venta");
                    asesoria.cliente.setEstrato(data.getSerializableExtra("estrato").toString());
                    if (asesoria.cliente.getBarrio().equals("")) {
                        asesoria.cliente.setBarrio(data.getStringExtra("barrio"));
                    }
                    // asesoria.venta.guardarVenta(asesoria.getId());

                    try {
                        // IVR("1");
                        IVR(asesoria.venta.getDocumentacion()[0]);
                    } catch (Exception e) {
                        ArrayList<ListaDefault> IVR = new ArrayList<ListaDefault>();
                        IVR.add(new ListaDefault(1, "NO", "Problemas de Conectividad"));
                    }

                    System.out.println("asesoria.venta.getDocumentacion()[0]) " + asesoria.venta.getDocumentacion()[0]);

                    try {

                        asesoria.cliente.setIdIVR(Integer.parseInt(asesoria.venta.getDocumentacion()[0]));
                    } catch (NumberFormatException e) {
                        asesoria.cliente.setIdIVR(0);
                        Log.e("Error ", e.getMessage());
                    }

                    System.out.println("asesoria.cliente.getIdIVR() " + asesoria.cliente.getIdIVR());

                } else if (modulo.equals(MODULO_RESUMEN)) {
                    String mensaje = (String) data.getSerializableExtra("Mensaje");
                    String borrar = (String) data.getSerializableExtra("Borrar");
                    dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA, mensaje);
                    dialogo.dialogo.show();
                    if (mensaje.contains("Asesoria Creada Correctamente.") || borrar.equalsIgnoreCase("SI")) {
                        asesoria = null;
                        btnCliente.restart();
                        btnCarteraUne.restart();
                        btnCobertura.restart();
                        btnCompetencia.restart();
                        // btnPeticion.restart();
                        btnPortafolio.restart();
                        btnTarificador.restart();
                        btnProspecto.restart();
                        btnPrecision.setVisibility(View.GONE);
                        btnBlindaje.restart();
                        btnUneMas.restart();
                        btnAmigoCuentas.restart();
                    }
                } else if (modulo.equals(MODULO_UNEMAS)) {
                    asesoria.unemas = (UneMas) data.getSerializableExtra("unemas");
                    asesoria.cliente = (Cliente) data.getSerializableExtra("cliente");
                    btnUneMas.setOK();
                } else if (modulo.equals(MODULO_INICIALIZACION)) {
                    lanzarAutenticacion();
                } else if (modulo.equals(MODULO_ACTUALIZACION)) {
                    if (Utilidades.visibleDepartamento("BlindajeManizales", MainActivity.config.getDepartamento())) {
                        btnBlindaje.setVisibility(View.VISIBLE);
                    } else {
                        btnBlindaje.setVisibility(View.GONE);
                    }

                    if (Utilidades.visible("AmigoCuentas", config.getCiudad())) {
                        btnAmigoCuentas.setVisibility(View.VISIBLE);
                    } else {
                        btnAmigoCuentas.setVisibility(View.GONE);
                    }
                } else if (modulo.equals(MODULO_BLINDAJE)) {
                    asesoria.blindaje = (Blindaje) data.getSerializableExtra("blindaje");

                    btnBlindaje.setOK();

                    IVR(asesoria.blindaje.idIVR);

                    if (!asesoria.blindaje.nombreCliente.equals("Otro")) {
                        String[] nombre = asesoria.blindaje.nombreCliente.split(" ");
                        if (nombre.length == 2) {
                            asesoria.cliente.setNombre(nombre[0]);
                            asesoria.cliente.setApellido(nombre[1]);
                        } else if (nombre.length == 3) {
                            asesoria.cliente.setNombre(nombre[0]);
                            asesoria.cliente.setApellido(nombre[1] + " " + nombre[2]);
                        } else if (nombre.length == 4) {
                            asesoria.cliente.setNombre(nombre[0] + " " + nombre[1]);
                            asesoria.cliente.setApellido(nombre[2] + " " + nombre[3]);
                        } else {
                            asesoria.cliente.setNombre("");
                            asesoria.cliente.setApellido("");
                        }
                        asesoria.cliente.setTipoDocumento(asesoria.blindaje.tipoCedulaCliente);
                        asesoria.cliente.setCedula(asesoria.blindaje.cedulaCliente);
                        asesoria.cliente.setDireccion(asesoria.blindaje.direccion);
                        asesoria.cliente.setEstrato(asesoria.blindaje.estrato);
                        asesoria.cliente.setDepartamento(MainActivity.config.getDepartamento());
                        asesoria.cliente.setCiudad(asesoria.blindaje.ciudad);
                        asesoria.cliente.setBarrio(asesoria.blindaje.barrio);
                        Facturacion fact = new Facturacion(asesoria.blindaje.cedulaCliente,
                                MainActivity.config.getDepartamento(), asesoria.blindaje.ciudad,
                                asesoria.blindaje.direccion, asesoria.blindaje.barrio);
                        asesoria.cliente.setFacturacion(fact);
                    } else {
                        asesoria.cliente.setNombre(asesoria.blindaje.formulario[0]);
                        asesoria.cliente.setApellido(asesoria.blindaje.formulario[1]);
                        asesoria.cliente.setTipoDocumento(asesoria.blindaje.formulario[2]);
                        asesoria.cliente.setCedula(asesoria.blindaje.formulario[3]);
                        asesoria.cliente.setExpedicion(asesoria.blindaje.formulario[4]);
                        asesoria.cliente.setLugarExpedicion(asesoria.blindaje.formulario[5]);
                        asesoria.cliente.setTipoServicio(asesoria.blindaje.formulario[6]);
                        asesoria.cliente.setDireccion(asesoria.blindaje.direccion);
                        asesoria.cliente.setEstrato(asesoria.blindaje.estrato);
                        asesoria.cliente.setDepartamento(MainActivity.config.getDepartamento());
                        asesoria.cliente.setCiudad(asesoria.blindaje.ciudad);
                        asesoria.cliente.setBarrio(asesoria.blindaje.barrio);
                        Facturacion fact = new Facturacion(asesoria.blindaje.formulario[3],
                                MainActivity.config.getDepartamento(), asesoria.blindaje.ciudad,
                                asesoria.blindaje.direccion, asesoria.blindaje.barrio);
                        asesoria.cliente.setFacturacion(fact);
                        Contacto con1 = new Contacto(asesoria.blindaje.formulario[7], asesoria.blindaje.formulario[8],
                                asesoria.blindaje.formulario[9], "", "");
                        asesoria.cliente.setContacto1(con1);
                        Contacto con2 = new Contacto(asesoria.blindaje.formulario[10], asesoria.blindaje.formulario[11],
                                asesoria.blindaje.formulario[12], "", "");
                        asesoria.cliente.setContacto2(con2);
                    }
                } else if (modulo.equals(MODULO_BUSQUEDA_AMC)) {
                    asesoria.cliente = (Cliente) data.getSerializableExtra("cliente");
                    if (data.getStringExtra("destino").equalsIgnoreCase("nuevo")) {
                        lanzarAmigoCuentaDigital();
                    } else if (data.getStringExtra("destino").equalsIgnoreCase("tarificador")) {
                        lanzarModulo(MODULO_TARIFICADOR);
                    } else {
                        lanzarAmigoCuentas();
                    }

                } else if (modulo.equals(MODULO_AMIGOCUENTAS)) {
                    asesoria.cotizacion = (Cotizacion) data.getSerializableExtra("cotizacion");
                    asesoria.cliente = (Cliente) data.getSerializableExtra("cliente");

                    String tipoIngreso = data.getStringExtra("tipoIngreso");

                    if (tipoIngreso.equalsIgnoreCase("venta")) {
                        lanzarVenta();
                    } else if (tipoIngreso.equalsIgnoreCase("estado cuenta")) {
                        mostrarEstadoCuenta();
                    }
                } else if (modulo.equals(MODULO_AMC_DIGITAL)) {
                    asesoria.cotizacion = (Cotizacion) data.getSerializableExtra("cotizacion");
                    asesoria.cliente = (Cliente) data.getSerializableExtra("cliente");

                    String tipoIngreso = data.getStringExtra("tipoIngreso");

                    if (tipoIngreso.equalsIgnoreCase("venta")) {
                        lanzarVenta();
                    } else if (tipoIngreso.equalsIgnoreCase("estado cuenta")) {
                        mostrarEstadoCuenta();
                    } else if (tipoIngreso.equalsIgnoreCase("gerencia de ruta")) {
                        lanzarCompetencia();
                    } else if (tipoIngreso.equalsIgnoreCase("carrusel")) {
                        lanzarCarrusel();
                    }
                } else if (modulo.equals(MODULO_CARRUSEL)) {

                    System.out.println("entro a resulta carrusel");

                    asesoria.cliente = (Cliente) data.getSerializableExtra("cliente");

                    System.out.println(asesoria.cliente.getArraycarrusel());

                }
            }
        } catch (Exception e) {

            Log.w("Error", "Mensaje " + e.getMessage());
        }
    }

    OnDismissListener dl = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                String[] asesoriaResumida = dialogo.getAsesoria();
                if (!asesoriaResumida[0].equals("") || !asesoriaResumida[1].equals("")
                        || !asesoriaResumida[2].equals("")) {
                    asesoria = new Asesoria();
                    asesoria.Configuracion = config;
                    asesoria.guardarAsesoria();
                    asesoria.cliente = new Cliente(asesoriaResumida[0], asesoriaResumida[1], asesoriaResumida[2],
                            asesoriaResumida[3], asesoria.getId());

                    asesoria.localizacion = new Localizacion();
                    iniciarGPS();
                    if (!modulo.equals(MODULO_CLIENTE) && !modulo.equals(MODULO_BLINDAJE)) {
                        // if (asesoria.cliente.getCiudad().equalsIgnoreCase(
                        // "BogotaREDCO")) {
                        // asesoria.cliente.llenarClienteREDCO(context,
                        // asesoria.cliente.getTelefono(),
                        // "identificador", "Automatico");
                        // } else {
                        if (Utilidades.excluir("siebelMunicipios", asesoria.cliente.getCiudad())) {
                            asesoria.cliente.llenarConsolidadoSiebel(context, asesoria.cliente.getTelefono(),
                                    "identificador", "");
                        } else {
                            asesoria.cliente.llenarCliente();
                        }
                        // }
                    }

                    System.out.println("lanzar modulo " + modulo);
                    System.out.println("lanzar modulo estrato " + asesoria.cliente.getEstrato());

                    if (!modulo.equals(MODULO_TARIFICADOR)) {
                        lanzarModulo(modulo);
                    } else {
                        if (!asesoria.cliente.getEstrato().equalsIgnoreCase(Utilidades.inicial_estrato)
                                && !asesoria.cliente.getEstrato().equalsIgnoreCase("")) {
                            lanzarModulo(modulo);
                        } else {
                            Toast.makeText(MainActivity.context, "Debe Seleccionar el Estrato.", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }

                } else {
                    Toast.makeText(MainActivity.context, "No Datos Para Generar Asesoria", Toast.LENGTH_SHORT).show();
                }

            }
            dialogo.setSeleccion(false);
        }
    };

    OnDismissListener dll = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                asesoria = null;
                btnCliente.restart();
                btnCarteraUne.restart();
                btnCobertura.restart();
                btnCompetencia.restart();
                // btnPeticion.restart();
                btnPortafolio.restart();
                btnProspecto.restart();
                btnTarificador.restart();
                btnPrecision.setVisibility(View.GONE);
                btnBlindaje.restart();
                btnUneMas.restart();
                btnAmigoCuentas.restart();
            }
            dialogo.setSeleccion(false);
        }
    };

    OnDismissListener dls = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                if (asesoria != null) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.enviarantesdesalir),
                            Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
            dialogo.setSeleccion(false);
        }
    };

    OnDismissListener dlc = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                asesoria = new Asesoria();
                asesoria.cargarAsesoria(dialogo.getIdAsesoria());
            }
            dialogo.setSeleccion(false);
        }
    };

    OnDismissListener dlr = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
            if (dialogo.getLanzador().equals("confirmacionTarificador")) {
                //lanzarModulo(MODULO_TARIFICADOR);
                //modulo = MODULO_TARIFICADOR;
                //ccfg.setLanzador("tarificador");
                lanzarModulo(MODULO_COTIZADOR);
                modulo = MODULO_COTIZADOR;
                ccfg.setLanzador("tarificador");
            } else if (dialogo.getLanzador().equals("confirmacionVenta")) {
                lanzarVenta();
            }
            dialogo.setSeleccion(false);
        }
    };

    OnDismissListener dla = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
			/*
			 * System.out.println("Lanzador " + dialogo.getLanzador());
			 * System.out.println("Seleccion " + dialogo.isSeleccion());
			 */
            if (dialogo.isSeleccion()) {
				/*
				 * System.out.println("MainActivity " +
				 * dialogo.getSelectTipoAsesoria());
				 */
                if (dialogo.getSelectTipoAsesoria().equalsIgnoreCase("Prospecto")) {
                    lanzarModulo(MODULO_PROSPECTO);
                    modulo = MODULO_PROSPECTO;
                    ccfg.setLanzador("prospecto");
                } else if (dialogo.getSelectTipoAsesoria().equalsIgnoreCase("Gerencia De Ruta")) {
                    lanzarModulo(MODULO_COMPETENCIA);
                    modulo = MODULO_COMPETENCIA;
                    ccfg.setLanzador("competencia");
                } else if (dialogo.getSelectTipoAsesoria().equalsIgnoreCase("Venta")) {
                    mostrarTarificador(MainActivity.btnTarificador);
                }

            }
            dialogo.setSeleccion(false);
        }
    };

    public static void crearConexion() {
        conexion = new Conexion(servidor);
    }

    public void asignarObservador() {
        conexion.addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getResources().getString(R.string.menu_settings))) {
            if (preferencias.getBoolean("cfgCiudades", false)) {
                ccfg.mostrarDialogoConfiguracion(this);
            } else {
                Toast.makeText(this, getResources().getString(R.string.actualizar_base_datos), Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (item.getTitle().equals("Cerrar")) {

            if (asesoria != null) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.enviar_info_antes_cerrar),
                        Toast.LENGTH_SHORT).show();
            } else {
                Editor editor = preferencias.edit();
                editor.putBoolean("session", false);
                editor.commit();
                lanzarAutenticacion();
            }
        } else if (item.getTitle().equals("Terminar")) {
            ArrayList<Boolean> reg = new ArrayList<Boolean>();
            boolean validacion = false;
            if (asesoria != null) {
                if (asesoria.venta != null) {
                    if (Validaciones.validarClienteVenta(asesoria.cliente, asesoria.venta, this)) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                        Toast.makeText(this, getResources().getString(R.string.complete_formulario_cliente),
                                Toast.LENGTH_SHORT).show();
                        System.out.println("Validaciones.mensaje " + Validaciones.getMensajes());
                        // String mensajes
                        Intent intent = new Intent(MainActivity.MODULO_MENSAJES);
                        intent.putExtra("mensajes", Validaciones.getMensajes().toString());
                        startActivityForResult(intent, MainActivity.REQUEST_CODE);

                    }
                    validacion = true;
                }

                if (asesoria.competencia != null) {
                    if (Validaciones.validarCompetencia(asesoria.cliente, asesoria.competencia, this)) {
                        reg.add(true);
                    } else {
                        reg.add(false);
						/*
						 * Toast.makeText( this,
						 * "Complete el formulario de Gesti�n de Visitas para continuar"
						 * , Toast.LENGTH_SHORT).show();
						 */
                        Intent intent = new Intent(MainActivity.MODULO_MENSAJES);
                        intent.putExtra("mensajes", Validaciones.getMensajes().toString());
                        startActivityForResult(intent, MainActivity.REQUEST_CODE);
                    }
                    validacion = true;
                }

                if (asesoria.prospecto != null) {
                    if (/*
						 * Validaciones.validarProspecto(asesoria.prospecto) &&
						 */Validaciones.validarClienteProspecto(asesoria.cliente, asesoria.prospecto)) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                        Toast.makeText(this, getResources().getString(R.string.complete_formulario_prospecto),
                                Toast.LENGTH_SHORT).show();
                    }
                    validacion = true;
                }

                System.out.println("blindaje => " + asesoria.blindaje);
                if (asesoria.blindaje != null) {
                    if (asesoria.unemas == null) {
                        asesoria.blindaje.unemasCodigo = "0";
                        asesoria.blindaje.unemasMensaje = "Usuario No Consultado En Une Mas";
                    } else {
                        asesoria.blindaje.unemasCodigo = asesoria.unemas.codigo;
                        asesoria.blindaje.unemasMensaje = asesoria.unemas.mensaje;
                    }

                    if (Validaciones.validarBlindaje(asesoria.blindaje)) {
                        reg.add(true);
                    } else {
                        reg.add(false);
                        Toast.makeText(this, getResources().getString(R.string.complete_formulario_cliente),
                                Toast.LENGTH_SHORT).show();
                    }
                    validacion = true;
                }

                if (validacion) {
                    if (!reg.contains(false)) {
                        modulo = MODULO_RESUMEN;
                        Intent intent = new Intent(MainActivity.MODULO_RESUMEN);
                        intent.putExtra("asesoria", asesoria);
                        startActivityForResult(intent, MainActivity.REQUEST_CODE);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.error_pasar_resumen), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
					/*
					 * Toast.makeText( this, 
					 * "Debes diligenciar la informacion de competencia, prospecto o venta antes de continuar."
					 * =======
					 * "Debes diligenciar la informaci�n de competencia, prospecto o venta antes de continuar.",
					 * Toast.LENGTH_SHORT).show();
					 */
                    Toast.makeText(this, getResources().getString(R.string.diligenciar_informacion), Toast.LENGTH_SHORT)
                            .show();
                    tipoAsesoria();
                }

            } else {
                Toast.makeText(this, getResources().getString(R.string.no_asesoria), Toast.LENGTH_SHORT).show();
            }

        } else if (item.getTitle().equals("Actualizar")) {
            if (!config.getCiudad().equals("--Seleccione Ciudad--")) {
                ArrayList<String> parametros = new ArrayList<String>();
                // MainActivity.config.execute(parametros);
                lanzarActualizacion();
            } else {
                ccfg.mostrarDialogoConfiguracion(this);
            }
        } else if (item.getTitle().equals("Limpiar")) {
            dialogo = new Dialogo(this, Dialogo.DIALOGO_CONFIRMACION,
                    getResources().getString(R.string.desea_limpiar_asesoria));
            dialogo.dialogo.setOnDismissListener(dll);
            dialogo.dialogo.show();
        } /*
			 * else if (item.getTitle().equals("Cargar")) { //
			 * mostrarAsesoriasGuardadas(); }
			 */
        return true;
    }

    @Override
    protected void onDestroy() {
        Editor editor = preferencias.edit();
        editor.putBoolean("session", false);
        editor.commit();
        Salir();
        super.onDestroy();
    }

    public void Salir() {
        try {
            enviar.Terminar_Hilo();
        } catch (Exception e) {
            Log.w("Error", "Mensaje " + e.getMessage());
        }
        try {
            PararServicio();
        } catch (Exception e) {
            Log.w("Error", "Mensaje " + e.getMessage());
        }
    }

    public void PararServicio() {
        System.out.println("Parar Gps");
        stopService(new Intent(this, PlayerGPS.class));
    }

    @Override
    public void update(Object value) {
        if (asesoria != null) {
            // TODO Auto-generated method stub
            if (value.equals("Llamada")) {
                agrupar();
                obsrVenta.update(ivr);
            } else if (value.equals("LlamadaBlindaje")) {
                agrupar();
                System.out.println("ivr " + ivr);
                obsrBlindaje.update(ivr);
            } else if (value.equals("LlamadaUneMas")) {
                agrupar();
                System.out.println("ivr " + ivr);
                obsrUneMas.update(ivr);
            } else if (value.equals("LlamadaProspecto")) {
                agrupar();
                System.out.println("ivr " + ivr);
                obrsProspecto.update(ivr);
            } else if (value.equals("Estrato")) {
                obsrTarifas.update(asesoria.cliente.getEstrato());
            } else {
                ArrayList<Object> resultado = (ArrayList<Object>) value;
                System.out.println("resultado " + resultado);
                if (resultado.get(0).equals("Cliente")) {
                    asesoria.cliente.reorganizarCliente(resultado.get(1).toString());
                    asesoria.cliente.guardarCliente(asesoria.getId());
                    System.out.println("Observador " + obsrCliente);
                    if (obsrCliente != null) {
                        obsrCliente.update(resultado);
                    }

                    if (!asesoria.cliente.getEstrato().equals("--Seleccione Estrato--") && obsrTarifas != null) {
                        obsrTarifas.update(asesoria.cliente.getEstrato());
                    }
                } else if (resultado.get(0).equals("ClienteREDCO")) {

                    try {

                        System.out.println("cliente Redco " + resultado.get(1));

                        JSONObject jo = new JSONObject(resultado.get(1).toString());

                        if (jo.has("codigoMensaje")) {
                            if (jo.getString("codigoMensaje").equalsIgnoreCase("00")) {
                                asesoria.cliente.reorganizarCliente(resultado.get(1).toString());
                                asesoria.cliente.guardarCliente(asesoria.getId());
                                System.out.println("Observador " + obsrCliente);
                                if (obsrCliente != null) {
                                    obsrCliente.update(resultado);
                                }

                                if (!asesoria.cliente.getEstrato().equals("--Seleccione Estrato--")
                                        && obsrTarifas != null) {
                                    obsrTarifas.update(asesoria.cliente.getEstrato());
                                }
                            } else {
                                Toast.makeText(MainActivity.this, jo.getString("descripcionMensaje"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "El Mensaje No Se Puede Interpretar", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "El Mensaje No Se Puede Interpretar", Toast.LENGTH_SHORT)
                                .show();
                        Log.w("Error", e.getMessage());
                    }
                } else if (resultado.get(0).equals("ConsolidadoSiebel")) {
                    btnCobertura.setInvisible();
                    btnPortafolio.setInvisible();
                    System.out.println("PortafolioSiebel " + resultado.get(1));
                    try {
                        System.out.println("cliente Redco " + resultado.get(1));
                        JSONObject jo = new JSONObject(resultado.get(1).toString());
                        if (jo.has("CodigoMensaje")) {
                            if (jo.getString("CodigoMensaje").equalsIgnoreCase("00")) {
                                System.out.println("portafolio " + jo.getString("ListaDatosProductosVent"));
                                asesoria.cliente.reorganizarClienteSiebel(resultado.get(1).toString());
                                asesoria.cliente.guardarCliente(asesoria.getId());
                                System.out.println("Observador " + obsrCliente);
                                if (obsrCliente != null) {
                                    obsrCliente.update(resultado);
                                }
                                btnCobertura.setOK();
                                btnPortafolio.setOK();
                            } else {
                                Toast.makeText(MainActivity.this, jo.getString("descripcionMensaje"),
                                        Toast.LENGTH_SHORT).show();
                                btnCobertura.setWRONG();
                                btnPortafolio.setWRONG();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "El Mensaje No Se Puede Interpretar", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "El Mensaje No Se Puede Interpretar", Toast.LENGTH_SHORT)
                                .show();
                        Log.w("Error", e.getMessage());
                    }
                } else if (resultado.get(0).equals("ResultadoIVR")) {
                    ResultadoIVR(resultado.get(1).toString());
                }
            }
        }
    }

    public void IniciarServicio() {
        System.out.println("Inicio Gps");
        Intent intent = new Intent(this, PlayerGPS.class);
        // intent.putExtra("Codigo", asesoria.Configuracion.getCodigo());
        startService(intent);
    }

    public void agrupar() {
        ivr = new ArrayList<String>();
        ivr.add(config.getCodigo());
        ivr.add(config.getCelular_asesor());
        ivr.add(Utilidades.ponerIndicativo(asesoria.cliente.getTelefonoDestino()));
        ivr.add("" + asesoria.cliente.getTipoCliente());
        ivr.add(asesoria.cliente.getCedula());
    }

    public static ArrayList<String> consolidarIVR() {
        System.out.println("consolidarIVR");
        return ivr;
    }

    public void mostrarAsesoriasGuardadas() {
        String sql = "SELECT asesorias.id, clientes.nombre FROM asesorias INNER JOIN clientes ON asesorias.id = clientes.id";
        ArrayList<ArrayList<String>> lista = MainActivity.basedatos.consultarAvanzado(sql, null);
        if (lista != null) {
            Utilidades.setAsesorias(MainActivity.basedatos.consultarAvanzado(sql, null));
            dialogo = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_ASESORIA, "");
            dialogo.dialogo.setOnDismissListener(dlc);
            dialogo.dialogo.show();
        } else {
            Toast.makeText(this, "No hay asesorias guardadas.", Toast.LENGTH_SHORT).show();
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

    public void ResultadoIVR(String Resultado) {
        JSONObject jop;
        try {
            jop = new JSONObject(Resultado);

            String data = jop.get("data").toString();
            if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                System.out.println("data => " + data);
                IVR = data;
                if (asesoria.venta != null) {
                    validarPermanecia(data);
                    pintarDocumentacion(data);
                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pintarDocumentacion(String documentacion) {

        String Cifin = "0";
        listDocumentacion.clear();
        listDocumentacion = Interprete.IVR(documentacion);
        if (listDocumentacion.get(0).getTitulo().equalsIgnoreCase("SI")) {
            listDocumentacion.remove(0);
            for (int i = 0; i < listDocumentacion.size(); i++) {
                if (listDocumentacion.get(i).getTitulo().equalsIgnoreCase("Autoriza Cifin")) {
                    // btnPrecision.setVisibility(View.VISIBLE);
                    int cifin = asesoria.cliente.getTipoCliente();
                    if (listDocumentacion.get(i).getDato().equalsIgnoreCase("SI") && cifin == 0) {
                        // btnPrecision.setVisibility(View.VISIBLE);
                    }
                }
            }
            asesoria.venta.setListDocumentacion(listDocumentacion);

        } else if (listDocumentacion.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, listDocumentacion.get(0).getDato(), Toast.LENGTH_SHORT).show();
        }
    }

    private void validarPermanecia(String resultado){

        try{

            JSONArray ja = new JSONArray(resultado);
            JSONObject jsonObject = ja.getJSONObject(0);

            if(jsonObject.getString("cnf_permanencia").equals("0")
                    || jsonObject.getString("cnf_permanencia").equals("null")){
                asesoria.venta.quitarPagosParciales();
            }

        }catch (JSONException e){
            e.printStackTrace();
        }


    }

    public void tipoAsesoria() {
        dialogo = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_TIPO_ASESORIA, "Por donde quiere seguir.");
        dialogo.dialogo.setOnDismissListener(dla);
        dialogo.dialogo.show();
    }

    public Asesoria getAsesoria() {
        return asesoria;
    }

    private void comenzarLocalizacionRed() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // mostrarPosicion(loc,"RED");

        locListenerRed = new LocationListener() {
            public void onLocationChanged(Location location) {
                mostrarPosicion(location, "RED");
                locManager.removeUpdates(locListenerRed);
            }

            public void onProviderDisabled(String provider) {
                System.out.println("Disabled " + provider);
            }

            public void onProviderEnabled(String provider) {
                System.out.println("Enabled " + provider);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("", "Provider Status: " + status);
            }
        };

        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 0, locListenerRed);
    }

    private void comenzarLocalizacionGPS() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // mostrarPosicion(loc,"GPS");

        locListenerGPS = new LocationListener() {
            public void onLocationChanged(Location location) {
                mostrarPosicion(location, "GPS");
                locManager.removeUpdates(locListenerGPS);
            }

            public void onProviderDisabled(String provider) {
                System.out.println("Disable " + provider);
            }

            public void onProviderEnabled(String provider) {
                System.out.println("Enabled " + provider);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("", "Provider Status: " + status);
            }
        };

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListenerGPS);
    }

    private void mostrarPosicion(Location loc, String tipo) {
        if (loc != null) {

			/*
			 * System.out.println("Localizacion obtenida "+"Latitud: " +
			 * String.valueOf(loc.getLatitude())+" "+"Longitud: " +
			 * String.valueOf(loc.getLongitude())+" "+"Precision: " +
			 * String.valueOf(loc.getAccuracy())+" Tipo "+tipo);
			 */

            Toast.makeText(this, "Latitud: " + String.valueOf(loc.getLatitude()) + " Longitud "
                    + String.valueOf(loc.getLongitude()) + "Tipo " + tipo, Toast.LENGTH_SHORT).show();
            try {
                asesoria.localizacion.almacenar(String.valueOf(loc.getLatitude()), String.valueOf(loc.getLongitude()),
                        tipo);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Localizacion obtenida nula");
        }
    }

    public void terminarGPS() {
        try {
            locManager.removeUpdates(locListenerGPS);
            locManager.removeUpdates(locListenerRed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciarGPS() {
        // System.out.println("Iniciar GPS");
        try {
            comenzarLocalizacionGPS();
            comenzarLocalizacionRed();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Problemas con el GPS");
        }
    }
}