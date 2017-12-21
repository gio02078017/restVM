package co.com.une.appmovilesune.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import com.google.analytics.tracking.android.EasyTracker;

import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.BloqueoCobertura;
import co.com.une.appmovilesune.adapters.ItemKeyValue2;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ItemTarificador;
import co.com.une.appmovilesune.change.Interprete;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesPagoParcial;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.complements.Validaciones;
import co.com.une.appmovilesune.components.CompAdicional;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.components.CompDecos;
import co.com.une.appmovilesune.components.CompProducto;
import co.com.une.appmovilesune.components.CompTotalCotizador;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.ObserverTotales;
import co.com.une.appmovilesune.interfaces.SubjectTotales;
import co.com.une.appmovilesune.model.AdicionalCotizador;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Configuracion;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.CotizacionCliente;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.TarificadorNew;
import co.com.une.appmovilesune.adapters.*;

/**
 * Created by davids on 18/10/16.
 */

public class ControlCotizador extends Activity implements Observer, SubjectTotales {

    public static final String TAG = "ControlCotizador";

    private TituloPrincipal tlpPrincipal;

    private BloqueoCobertura bloqueoCobertura;
    private boolean cambioMedioTV = false;

    private CheckBox chkHogarNuevo;
    private CheckBox chkAnaloga;
    private Spinner spnestrato;
    private Spinner spntipooferta;
    private Spinner spnoferta;

    private CompProducto cprdTelevision;
    private CompAdicional cadcTelevision;
    private CompDecos cdcsDecodificadores;
    private CompProducto cprdInternet;
    private CompProducto cprdTelefonia;
    private CompAdicional cadcTelefonia;
    private CompAdicional cadcInternet;

    private CompTotalCotizador cttlTotales;

    private ImageButton btnGardarCotizacion;

    private TarificadorNew tarificador;
    private Cliente cliente;
    private Cotizacion cotizacion;
    private Scooring scooring;

    ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();
    ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionalesGratis = new ArrayList<ItemPromocionesAdicionales>();
    ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionalesInternet = new ArrayList<ItemPromocionesAdicionales>();

    private ArrayList<ItemKeyValue2> datosValidacion = new ArrayList<ItemKeyValue2>();
    private ArrayList<ItemKeyValue2> countValidacion = new ArrayList<ItemKeyValue2>();
    private ArrayList<String> countValidacion2 = new ArrayList<String>();

    private ArrayList<String> productosLog;

    private boolean validarAdicionales = true;
    private boolean validarEstandarizacion = true;
    private boolean validarTelefonoServicio = true;

    private boolean controlDescuentosAd = false;
    private double preciosConDescuentoAd = 0.0;
    private double precioDescuentosAd = 0.0;

    private ObserverTotales observerTotales;

    CotizacionCliente cotizacionCliente;

    private String aplicarAnaloga = "N/A";

    private double totalPagoAnticipado = 0;

    private String codigoPP = "";
    private String codigoPA = "";
    private String codigoClienteNuevo = "";

    private boolean clienteNuevo;

    private Dialogo dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tarificador = new TarificadorNew();

        setContentView(R.layout.viewcotizador);

        tlpPrincipal = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tlpPrincipal.setTitulo(getResources().getString(R.string.cotizador));

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            cotizacion = (Cotizacion) reicieveParams.getSerializable("cotizacion");
            cliente = (Cliente) reicieveParams.getSerializable("cliente");
            scooring = (Scooring) reicieveParams.getSerializable("scooring");
        }

        chkHogarNuevo = (CheckBox) findViewById(R.id.chkHogarNuevo);
        chkAnaloga = (CheckBox) findViewById(R.id.chkAnaloga);
        spnestrato = (Spinner) findViewById(R.id.spnestrato);
        spntipooferta = (Spinner) findViewById(R.id.spntipooferta);
        spnoferta = (Spinner) findViewById(R.id.spnoferta);

        cprdTelevision = (CompProducto) findViewById(R.id.cprdTelevision);
        cadcTelevision = (CompAdicional) findViewById(R.id.cadcTelevision);
        cdcsDecodificadores = (CompDecos) findViewById(R.id.cdcsDecodificadores);
        cprdInternet = (CompProducto) findViewById(R.id.cprdInternet);
        cadcInternet = (CompAdicional) findViewById(R.id.cadcInternet);
        cprdTelefonia = (CompProducto) findViewById(R.id.cprdTelefonia);
        cadcTelefonia = (CompAdicional) findViewById(R.id.cadcTelefonia);
        cttlTotales = (CompTotalCotizador) findViewById(R.id.cttlTotales);

        btnGardarCotizacion = (ImageButton) findViewById(R.id.btnGardarCotizacion);


        /*Se Agregan los observadores de adicionales a los producutos que cientan con la
        funcionalidad de adicionales*/
        cprdTelevision.addObserverAdicionales(cadcTelevision);
        cprdTelevision.addObserverDecodificadores(cdcsDecodificadores);
        cprdTelevision.addObserverAdicionalesInternet(cadcInternet);
        cprdTelefonia.addObserverAdicionales(cadcTelefonia);
        cprdInternet.addObserverAdicionales(cadcInternet);

        cdcsDecodificadores.setOferta("tarificador");
        cdcsDecodificadores.setCiudad(cliente.getCiudad());
        cdcsDecodificadores.addObserver(this);


        cprdTelevision.addObserver(this);
        cadcTelevision.addObserver(this);
        cprdInternet.addObserver(this);
        cadcInternet.addObserver(this);
        cprdTelefonia.addObserver(this);
        cadcTelefonia.addObserver(this);

        cadcTelevision.setCliente(cliente);
        cadcTelefonia.setCliente(cliente);
        cadcInternet.setCliente(cliente);

        spnestrato.setOnItemSelectedListener(seleccionarEstrato);
        spntipooferta.setOnItemSelectedListener(seleccionarTipoOferta);
        spnoferta.setOnItemSelectedListener(seleccionarOferta);
        chkAnaloga.setOnCheckedChangeListener(aplicartvAnaloga);
        chkAnaloga.setEnabled(false);

        llenarEstrato();

        if (Utilidades.excluirMunicipal("bloquearCobertura", "bloquearCobertura", cliente.getCiudad())) {

            bloqueoCobertura = Utilidades.tratarCoberturaBloqueo(cliente.getCobertura(), this);

        }

        System.out.println("bloqueoCobertura " + bloqueoCobertura);
        System.out.println("cliente.getCobertura() " + cliente.getCobertura());

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

        System.out.println("smart promo => " + Utilidades.excluir("ConsultarSmartPromo", cliente.getCiudad()));

        if (Utilidades.excluir("ConsultarSmartPromo", cliente.getCiudad())) {
            lanzarTipoHogar();
        }

        MainActivity.basedatos.eliminar("pagoparcialanticipado", null, null);
        MainActivity.basedatos.eliminar("valorconexion", null, null);
        MainActivity.basedatos.eliminar("pagoParcial", null, null);

        if (!Utilidades.excluir("excluirppca", cliente.getCiudad())) {
            obtenerPagoParcialAnticipado();
        }

    }

    private void lanzarTipoHogar() {

        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        // simulador.execute(params);

        JSONObject data = new JSONObject();

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
                    data.put("strDepartamento", cliente.getDepartamento());
                    data.put("strMunicipio", Utilidades.homologarMunicipio(cliente.getCiudad()));
                    data.put("strBarrio", cliente.getBarrio());
                    data.put("strDireccion", cliente.getDireccion());
                } else if (cliente.getIdDireccionGisEx() != null
                        && !cliente.getIdDireccionGisEx().equalsIgnoreCase("")) {
                    data.put("IdDireccionGis", cliente.getIdDireccionGisEx());
                    data.put("strDepartamento", cliente.getIdDireccionGis());
                    data.put("strMunicipio", Utilidades.homologarMunicipio(cliente.getCiudad()));
                    data.put("strBarrio", cliente.getBarrio());
                    data.put("strDireccion", cliente.getDireccion());
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

        System.out.println("smart promo => consultar "+consultar);
        System.out.println("smart promo => dataa "+data);
        if (consultar) {

            System.out.println("smart promo => data "+data);

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

    public void llenarEstrato() {

        ArrayAdapter<String> adaptador = null;

        adaptador = (ArrayAdapter<String>) spnestrato.getAdapter();

        String estrato = (String) spnestrato.getSelectedItem();

        System.out.println("estrato " + estrato);

        spnestrato.setSelection(adaptador.getPosition((String) cliente.getEstrato()));

        spnestrato.setEnabled(false);

    }

    private void llenarOfertas(String estrato, String tipoPaquete) {

        if (estrato.equals("-")) {
            estrato = "";
        }

        if (tipoPaquete.equals("-")) {
            tipoPaquete = "";
        } else if (tipoPaquete.equals("Individual")) {
            tipoPaquete = "Ind";
        }

        String queryOferta = "select distinct Oferta from Precios p " +
                UtilidadesTarificadorNew.innerJoinTarifas+
                " where cxt.id_condicion in ("+ UtilidadesTarificadorNew.queryInternoTarifas(UtilidadesTarificadorNew.homologarDepartamentoCotizacion(cliente.getDepartamento(),cliente.getCiudad()),cliente.getCiudad())+")" +
                " and estrato like '%" + estrato + "%' and tipo_paquete like '%"+tipoPaquete+"%' and Tecnologia like '%"+cliente.getTecnologia()+"%'";

        //System.out.println("queryOferta "+queryOferta);

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar2(queryOferta);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        System.out.println(respuesta);

        if (respuesta != null) {
            for (ArrayList<String> arrayList : respuesta) {
                if (arrayList.get(0).equals("")) {
                    adaptador.add("-");
                } else {
                    adaptador.add(arrayList.get(0));
                }
            }
        } else {
            adaptador.add("-");
        }

        spnoferta.setAdapter(adaptador);
    }

    private void parametrizarComponentes() {
        cprdTelevision.cargarPlanes(cliente, Integer.parseInt((String) spnestrato.getSelectedItem()), (String) spnoferta.getSelectedItem());
        cprdInternet.cargarPlanes(cliente, Integer.parseInt((String) spnestrato.getSelectedItem()), (String) spnoferta.getSelectedItem());
        cprdTelefonia.cargarPlanes(cliente, Integer.parseInt((String) spnestrato.getSelectedItem()), (String) spnoferta.getSelectedItem());
        cadcInternet.setTipoOferta((String) spntipooferta.getSelectedItem());
        if (cotizacion != null) {
            //rellenarCotizacion();
        }
    }

    public void rellenarCotizacion() {

        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spntipooferta.getAdapter();
        spntipooferta.setSelection(adaptador.getPosition(cotizacion.getTipoOferta()));

        adaptador = (ArrayAdapter<String>) spnoferta.getAdapter();
        spnoferta.setSelection(adaptador.getPosition(cotizacion.getOfertaCotizacion()));

        if (!cotizacion.getTipoTv().equals("-") && !cotizacion.getTelevision().equals("-")) {
            cprdTelevision.setActivo(true);
            cprdTelevision.rellenarProducto(cotizacion.getTipoTv(), cotizacion.getTelevision());
        }

        if (!cotizacion.getTipoBa().equals("-") && !cotizacion.getInternet().equals("-")) {
            cprdInternet.setActivo(true);
            cprdInternet.rellenarProducto(cotizacion.getTipoBa(), cotizacion.getInternet());
        }

        if (!cotizacion.getTipoTo().equals("-") && !cotizacion.getTelefonia().equals("-")) {
            cprdTelefonia.setActivo(true);
            cprdTelefonia.rellenarProducto(cotizacion.getTipoTo(), cotizacion.getTelefonia());
        }
    }

    AdapterView.OnItemSelectedListener seleccionarEstrato = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            llenarOfertas((String) parent.getSelectedItem(), (String) spntipooferta.getSelectedItem());
            parametrizarComponentes();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener seleccionarTipoOferta = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            llenarOfertas((String) spnestrato.getSelectedItem(), (String) parent.getSelectedItem());
            parametrizarComponentes();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener seleccionarOferta = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            productosHabilitar((String) parent.getSelectedItem());
            parametrizarComponentes();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    CompoundButton.OnCheckedChangeListener aplicartvAnaloga = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            System.out.println("evento Boton " + aplicarAnaloga);
            if (isChecked) {
                aplicarAnaloga = "1";
            } else {
                aplicarAnaloga = "0";
            }
            cotizar();
        }
    };

    private void productosHabilitar(String oferta) {

        if (oferta.equals("-")) {
            cprdTelevision.habilitarCheckProducto();
            cprdTelevision.setActivo(false);
            cprdInternet.habilitarCheckProducto();
            cprdInternet.setActivo(false);
            cprdTelefonia.habilitarCheckProducto();
            cprdTelefonia.setActivo(false);
        } else {
            String clausula = "Oferta = ?";
            String[] valores = new String[]{oferta};

            ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios", new String[]{"tipo_producto"}, clausula,
                    valores, null, null, null);

            if (respuesta != null) {

                cprdTelevision.deshabilitarCheckProducto();
                cprdTelevision.limpiarTipoPeticion();
                cprdTelevision.setActivo(false);
                cprdInternet.deshabilitarCheckProducto();
                cprdInternet.limpiarTipoPeticion();
                cprdInternet.setActivo(false);
                cprdTelefonia.deshabilitarCheckProducto();
                cprdTelefonia.limpiarTipoPeticion();
                cprdTelefonia.setActivo(false);

                for (ArrayList<String> arrayList : respuesta) {
                    if (arrayList.get(0).equalsIgnoreCase("tv")) {
                        cprdTelevision.setActivo(true);
                        cprdTelevision.deshabilitarCheckProducto();
                    }

                    if (arrayList.get(0).equalsIgnoreCase("ba")) {
                        cprdInternet.setActivo(true);
                        cprdInternet.deshabilitarCheckProducto();
                    }

                    if (arrayList.get(0).equalsIgnoreCase("to")) {
                        cprdTelefonia.setActivo(true);
                        cprdTelefonia.deshabilitarCheckProducto();
                    }
                }
            }
        }

    }


    @Override
    public void update(Object value) {

        try {
            ArrayList<Object> resultado = null;
            if (value != null) {
                resultado = (ArrayList<Object>) value;
                System.out.println("resultado update ControlCotizador" + resultado);
            }


            if (resultado != null && resultado.get(0).equals("TipoHogar")) {
                System.out.println("TipoHogar " + resultado.get(1));
                // cliente.setPortafolio(resultado.get(1).toString());
                cliente.setlogSmartPromoRes(resultado.get(1).toString());
                validarSmartPromo(resultado.get(1).toString());

            } else if (resultado != null && resultado.get(0).equals("Portafolio")) {
                System.out.println("Portafolio " + resultado.get(1));
                cliente.setPortafolio(resultado.get(1).toString());
            } else if (resultado != null && resultado.get(0).equals("validarPermiso")) {
                //mostarComponenteDecos(resultado.get(1).toString());
            } else if (resultado != null && resultado.get(0).equals("consultarPagoParcialAnticipado")) {
                tratarPagoParcialAnticipado(resultado.get(1).toString());
            } else if (resultado != null && resultado.get(0).equals("guardarLogCarruselAutomatico")) {
                Log.d(TAG,"guardarLogCarruselAutomatico "+resultado.get(1).toString());
                tratarLogAutomaticoCarrusel(resultado.get(1).toString());
            } else if (resultado != null && resultado.get(0).equals("decos")) {

            }else  if (resultado != null && resultado.get(0).equals("ValidacionConfiguracionMovil")) {

                try {

                    JSONObject jop = new JSONObject(resultado.get(1).toString());
                    String data = jop.get("data").toString();

                    if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {

                        data = new String(Base64.decode(data));
                        // Confirmacion(data);
                        JSONObject validacion = new JSONObject(data);
                        Toast.makeText(MainActivity.context, "Datos Invalidos", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(MainActivity.MODULO_MENSAJES);
                        intent.putExtra("mensajes", Interprete.mensajesConfiguracion(validacion).toString());
                        startActivityForResult(intent, MainActivity.REQUEST_CODE);*/

                    }
                } catch (JSONException e) {
                    Log.w("Error JSONException ", e.getMessage());
                }

            } else if(resultado != null && resultado.get(0).equals("validarDebitoAutomaticoExistente")){
                tratarValidacionDebitoAutomatico(resultado.get(1).toString());
            }



        } catch (Exception e) {
            Log.w("error update", e.getMessage());
        }

        cotizar();

    }

    public void cotizar() {

        cprdTelefonia.limpiarComp();
        cprdTelevision.limpiarComp();
        cprdInternet.limpiarComp();

        cttlTotales.limpiarTotales();

        String[][] adicionales = new String[0][0];


        double totalPagoParcial = 0;
        totalPagoAnticipado = 0;

        if (cprdTelevision.getPeticionProducto().equalsIgnoreCase("C")) {
            chkAnaloga.setEnabled(true);
        } else {
            chkAnaloga.setEnabled(false);
            chkAnaloga.setChecked(false);
        }

        tarificador.Consulta_Tarifaz(cprdTelefonia.getPeticionProducto(), cprdTelefonia.getPlan(),
                cprdTelevision.getPeticionProducto(), cprdTelevision.getPlan(),
                cprdInternet.getPeticionProducto(), cprdInternet.getPlan(), adicionales,
                0, (String) spnestrato.getSelectedItem(), false, cliente,
                UtilidadesTarificador.jsonDatos(cliente, cliente.getSmartPromo(), cliente.getTecnologia(), aplicarAnaloga), this,
                0,(String)spnoferta.getSelectedItem());

        // ArrayList<ProductoCotizador> productos = tarificador.cotizacionVenta();

        cotizacionCliente = tarificador.cotizacionCliente();

        ArrayList<ProductoCotizador> productos = cotizacionCliente.getProductoCotizador();

        //UtilidadesTarificadorNew.imprimirProductosCotizacion(cotizacionCliente.getProductoCotizador());

        boolean trioDuoNuevo = UtilidadesTarificadorNew.isTrioDuoNuevo(productos);
        boolean duoNuevo = UtilidadesTarificadorNew.isDuoNuevo(productos);
        boolean comportamientoExistentes = UtilidadesTarificadorNew.isCotizacionConExistentes(productos);

        if(cotizacionCliente.getControl().equalsIgnoreCase("00")) {

            //productos = obtenerValorConexionCotizacion(cotizacionCliente);
            productos = UtilidadesPagoParcial.obtenerPagoParcial(cotizacionCliente);if (productos != null) {
            for (int i = 0; i < productos.size(); i++) {



                if(!codigoClienteNuevo.equalsIgnoreCase("00")){
                    if(clienteNuevo && !comportamientoExistentes){
                        productos.get(i).setClienteNuevo(true);
                    } else {
                        productos.get(i).setClienteNuevo(false);
                    }
                }else {
                    if(clienteNuevo){
                        productos.get(i).setClienteNuevo(true);
                    } else {
                        productos.get(i).setClienteNuevo(false);
                    }
                }


                    if (codigoPA.equalsIgnoreCase("00")) {
                        if (clienteNuevo) {
                            if (cliente.getScooringune().isPasaScooring()) {
                                productos.get(i).setAplicaPA(false);
                                cliente.setPagoAnticipado("NO");
                            } else if (Utilidades.excluirEstadosPagoAnticipado()
                                    .contains(cliente.getScooringune().getRazonScooring())) {
                                productos.get(i).setAplicaPA(true);
                                cliente.setPagoAnticipado("SI");
                            } else {

                            }
                        } else {
                            productos.get(i).setClienteNuevo(false);
                            productos.get(i).setAplicaPA(false);
                            cliente.setPagoAnticipado("NO");
                        }

                    } else {
                        productos.get(i).setAplicaPA(false);
                        cliente.setPagoAnticipado("NO");
                    }

                    switch (productos.get(i).getTipo()) {
                        case 0:
                            cprdTelefonia.llenarComp(productos.get(i));
                            break;
                        case 1:
                            cprdTelevision.llenarComp(productos.get(i));
                            break;
                        case 2:
                            cprdInternet.llenarComp(productos.get(i));
                            break;
                    }
                    if (productos.get(i).getTipoPeticion().equals("N")) {
                        totalPagoParcial += productos.get(i).getTotalPagoParcial();

                        if (codigoPA.equalsIgnoreCase("00")) {
                            totalPagoAnticipado += productos.get(i).getPagoAnticipado();
                        } else {
                            totalPagoAnticipado = 0;
                        }

                    }
                }
                totalPagoParcial = Math.ceil(totalPagoParcial);
                cotizacionCliente.setClienteNuevo(clienteNuevo);
                cotizacionCliente.setCodigoClienteNuevo(codigoClienteNuevo);
            }



        String cadenaConexion = UtilidadesPagoParcial.obtenerCadenaValorConexion(productos);
        double valorConexion = UtilidadesPagoParcial.obtenerValorConexion(cadenaConexion);
        double valorDescuentoComercial = valorConexion - totalPagoParcial;
        cttlTotales.llenarTotales(cotizacionCliente.getTotalIndividual(), cotizacionCliente.getTotalEmpaquetado(), cadcTelevision.calcularTotal(), cdcsDecodificadores.obtenerTotalDecos(), cadcTelefonia.calcularTotal(), cadcInternet.calcularTotal(), valorConexion, totalPagoParcial, valorDescuentoComercial, totalPagoAnticipado);


    }
    }

    public void procesarCotizacion(View v) {
        if (cotizacionCliente != null) {
            System.out.println("cotizacionCliente " + cotizacionCliente);
            //procesarCotizacion(cotizacionCliente);
            procesarCotizacion2(cotizacionCliente);
        } else {
            Utilidades.MensajesToast("Debe seleccionar primero la cotización", this);
        }
    }

    public void procesarCotizacion2(CotizacionCliente cotizacionCliente) {
        if (cprdTelevision.isActivo() || cprdInternet.isActivo() || cprdTelefonia.isActivo()) {
            if (!spnestrato.getSelectedItem().equals("--Seleccione Estrato--")
                    && (!cprdTelefonia.getPlan().equals("--Seleccione Producto--")
                    || !cprdTelevision.getPlan().equals("--Seleccione Producto--")
                    || !cprdInternet.getPlan().equals("--Seleccione Producto--"))) {

                cotizacionCliente.setTipoOferta((String) spntipooferta.getSelectedItem());
                cotizacionCliente.setOfertaCotizacion((String) spnoferta.getSelectedItem());

                cotizacionCliente.setTotalPagoAntCargoFijo(String.valueOf(totalPagoAnticipado));
                cotizacionCliente.setTotalPagoConexion(String.valueOf(cttlTotales.getTotalConexion()));
                cotizacionCliente.setTotalPagoParcialConexion(String.valueOf(cttlTotales.getTotalPagoParcial()));
                cotizacionCliente.setDescuentoConexion(String.valueOf(cttlTotales.getValorDescuentoConexion()));
                cotizacionCliente.setCodigoPagoAnticipado(codigoPA);

                if (cotizacionCliente.getProductoCotizador().size() > 0) {
                    for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
                        System.out.println("tipoProducto " + cotizacionCliente.getProductoCotizador().get(i).getTipo());

                        switch (cotizacionCliente.getProductoCotizador().get(i).getTipo()) {
                            case 0:
                                cotizacionCliente.getProductoCotizador().set(i,llenarcotizacionTelefonia(cotizacionCliente.getProductoCotizador().get(i)));
                                break;
                            case 1:
                                cotizacionCliente.getProductoCotizador().set(i,llenarcotizacionTelevision(cotizacionCliente.getProductoCotizador().get(i)));
                                //llenarcotizacionTelevision(cotizacionCliente.getProductoCotizador().get(i), tv, cotizacionCliente.getContadorProductos(), trioNuevo);
                                break;
                            case 2:
                                cotizacionCliente.getProductoCotizador().set(i,llenarcotizacionInternet(cotizacionCliente.getProductoCotizador().get(i)));
                                //llenarcotizacionInternet(cotizacionCliente.getProductoCotizador().get(i), cotizacionCliente, ba, cotizacionCliente.getContadorProductos(), trioNuevo, cadcInternet.isHBOGOExistente(),cadcInternet.getObjectCrackleExistente());
                                break;
                        }
                    }
                }

                ProductoCotizador tele = UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(),ProductoCotizador.getTELEVISION());
                tele.imprimir();

                if(Validaciones.validarCotizacion(cliente,cotizacionCliente,scooring,this)){
                    System.out.println("hola");
                    resultCotizador("venta");

                }else{
                    Toast.makeText(this, "No cumple", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.MODULO_MENSAJES);
                    intent.putExtra("mensajes", Validaciones.getMensajes().toString());
                    startActivityForResult(intent, MainActivity.REQUEST_CODE);
                }

            }

            }
    }


    public void procesarCotizacion(CotizacionCliente cotizacionCliente) {

        boolean trioNuevo = false;
        controlDescuentosAd = false;
        preciosConDescuentoAd = 0.0;
        precioDescuentosAd = 0.0;

        String tv = cprdTelefonia.getPeticionProducto();
        String to = cprdTelevision.getPeticionProducto();
        String ba = cprdInternet.getPeticionProducto();

        if (cprdTelevision.isActivo() || cprdInternet.isActivo() || cprdTelefonia.isActivo()) {
            if (!spnestrato.getSelectedItem().equals("--Seleccione Estrato--")
                    && (!cprdTelefonia.getPlan().equals("--Seleccione Producto--")
                    || !cprdTelevision.getPlan().equals("--Seleccione Producto--")
                    || !cprdInternet.getPlan().equals("--Seleccione Producto--"))) {

                if (cotizacion == null) {
                    cotizacion = new Cotizacion();
                }



                cotizacion.setTipoOferta((String) spntipooferta.getSelectedItem());
                cotizacion.setOfertaCotizacion((String) spnoferta.getSelectedItem());

                itemPromocionesAdicionales = cadcTelevision.itemPromocionesAdicionales();
                itemPromocionesAdicionalesInternet = cadcInternet.itemPromocionesAdicionales();

                cotizacion.setAdicionales(cadcTelevision.arrayAdicionales());

                if (cotizacionCliente.getContadorProductos() == 3) {

                    if (tv.equalsIgnoreCase("N") && to.equalsIgnoreCase("N") && ba.equalsIgnoreCase("N")) {
                        trioNuevo = true;
                    }
                }


                cotizacion.Otros("" + cttlTotales.getTotalIndividualAdicionales(), "" + cttlTotales.getTotalEmpaquetadoAdicionales(), "" + cotizacionCliente.getContadorProductos(), cotizacionCliente.getEstrato());
                cotizacion.setTotalPagoAntCargoFijo(String.valueOf(totalPagoAnticipado));
                cotizacion.setTotalPagoConexion(String.valueOf(cttlTotales.getTotalConexion()));
                cotizacion.setTotalPagoParcialConexion(String.valueOf(cttlTotales.getTotalPagoParcial()));
                cotizacion.setDescuentoConexion(String.valueOf(cttlTotales.getValorDescuentoConexion()));

                //UtilidadesTarificadorNew.imprimirProductosCotizacion(cotizacionCliente.getProductoCotizador());

                if (cotizacionCliente.getProductoCotizador().size() > 0) {
                    for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
                        System.out.println("tipoProducto " + cotizacionCliente.getProductoCotizador().get(i).getTipo());

                        switch (cotizacionCliente.getProductoCotizador().get(i).getTipo()) {
                            case 0:
                                llenarcotizacionTelefonia(cotizacionCliente.getProductoCotizador().get(i), to, cotizacionCliente.getContadorProductos(), trioNuevo);
                                break;
                            case 1:
                                llenarcotizacionTelevision(cotizacionCliente.getProductoCotizador().get(i), tv, cotizacionCliente.getContadorProductos(), trioNuevo);
                                break;
                            case 2:
                                llenarcotizacionInternet(cotizacionCliente.getProductoCotizador().get(i), cotizacionCliente, ba, cotizacionCliente.getContadorProductos(), trioNuevo, cadcInternet.isHBOGOExistente(),cadcInternet.getObjectCrackleExistente());
                                break;
                        }
                    }
                }

                cotizacion.setTotalAdicionales(String.valueOf(cadcTelevision.calcularTotal())); // se debe ingresar el dato

                cotizacion.setOferta(cotizacionCliente.getOferta());

                ArrayList<String> controlCotizacion = new ArrayList<String>();
                controlCotizacion.add(cotizacionCliente.getControl());

                cotizacion.setMedioIngreso("Venta");

                if (cliente.getSmartPromo().equalsIgnoreCase("1")) {

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

                        String totalDescuentoEmp = totalDescuentos(cotizacion.getToEmp(), cotizacion.getPromoTo(),
                                cotizacion.getTvEmp(), cotizacion.getPromoTv(), cotizacion.getBaEmp(),
                                cotizacion.getPromoBa(), cotizacion.getTotalEmp(), precioDescuentosAd);

                        if (!totalDescuentoEmp.equalsIgnoreCase("0") && !totalDescuentoEmp.equalsIgnoreCase("0.0")) {
                            cotizacion.setTotalEmpDescuento(totalDescuentoEmp);
                        }

                        String totalDescuentoInd = totalDescuentos(cotizacion.getToInd(), cotizacion.getPromoTo(),
                                cotizacion.getTvInd(), cotizacion.getPromoTv(), cotizacion.getBaInd(),
                                cotizacion.getPromoBa(), cotizacion.getTotalInd(), precioDescuentosAd);

                        if (!totalDescuentoInd.equalsIgnoreCase("0") && !totalDescuentoInd.equalsIgnoreCase("0.0")) {
                            cotizacion.setTotalIndDescuento(totalDescuentoInd);
                        }
                    }
                }

                if (aplicaDescuentoTo()) {

                    cotizacion.setPromoTo("100%");
                    cotizacion.setTiempoPromoTo("12 Meses");
                    cotizacion.setPromoTo_12_100(true);

                } else {
                    cotizacion.setPromoTo_12_100(false);
                }

                if (!UtilidadesTarificador.validarBronze(cotizacion, cliente.getCiudad())) {
                    controlCotizacion.set(0, "01");
                }

                cotizacion.setCambiarCotizacion(true);

                if (validarCobertura() || cliente.isControlCerca()) {

                    if (controlCotizacion.get(0).equalsIgnoreCase("00")) {
                        if (UtilidadesTarificador.validarDependencias(cotizacion.getTelevision(),
                                cotizacion.getAdicionales(), this)
                                && UtilidadesTarificador.validarDecos(cotizacion.getObjectDecodificador(), this)) {
                            if (cliente.isControlEstadoCuenta()) {
                                if (validarCarrusel()) {
                                    if(validarCotizacionClienteNuevo(cotizacion)){
                                        if (validarDigital()) {
                                            if (validarTelefonoServicio) {
                                                if (Utilidades.validarTelefonos(cliente, this)) {
                                                    if (UtilidadesTarificadorNew.validarEstandarizacion(cotizacion,cliente, this) || cliente.isControlCerca() || Utilidades.CoberturaRural(cliente)) {
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
                                                                        if (cliente.isRealizoConfronta()) {
                                                                            if (cliente.isConfronta()) {
                                                                                if (validarCarteraUNE()) {
                                                                                    if (validarScooring()) {
                                                                                        if (Utilidades.validarCliente(
                                                                                                cliente.getCiudad(),
                                                                                                scooring.getAccion(),
                                                                                                scooring.getEstadoValidador(), this)) {

                                                                                            if (validarAdicionales) {
                                                                                                if (cprdInternet.getPlan().equals(
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
                                                                            } else {
                                                                                Toast.makeText(this,
                                                                                        getResources().getText(R.string.mensajeconfrontanoaprobado),
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
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
                                        Toast.makeText(this,
                                                getResources().getString(R.string.mensajeCotizacioninvalidaClienteNuevo),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    cliente.setCarrusel(true);
                                    cliente.setProductosCarrusel(UtilidadesTarificador.productosCotizacionCarrusel(cotizacion));

                                    enviarLogCarruselAutomatico();
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
            }
        } else {
            Toast.makeText(this, "Seleccione productos para cotizar", Toast.LENGTH_SHORT).show();
        }

    }

    private void enviarLogCarruselAutomatico(){

        try{

            ArrayList<ProductoCotizador> productos = cotizacionCliente.getProductoCotizador();

            JSONObject  dataLogCarrusel = new JSONObject();
            dataLogCarrusel.put("documento",cliente.getCedula());
            dataLogCarrusel.put("tipoDocumento",cliente.getTipoDocumento());
            dataLogCarrusel.put("departamento",cliente.getDepartamento());
            dataLogCarrusel.put("municipio",cliente.getCiudad());
            dataLogCarrusel.put("direccion",cliente.getDireccion());
            dataLogCarrusel.put("codigoAsesor",MainActivity.config.getCodigo_asesor());
            dataLogCarrusel.put("nombreAsesor",MainActivity.config.getNombre_asesor());
            dataLogCarrusel.put("codigoCanal",MainActivity.config.getCanal());
            dataLogCarrusel.put("nombreCanal","");
            dataLogCarrusel.put("motivo","VALIDACION SEGUNDO NIVEL");
            dataLogCarrusel.put("submotivo","Carrusel");
            dataLogCarrusel.put("crm",cliente.getCrmCarrusel());

            JSONArray dataLogCarruselProductos = new JSONArray();

            for (ProductoCotizador producto:productos) {
                JSONObject productoJson = new JSONObject();
                productoJson.put("tipo",producto.traducirProducto().toUpperCase());
                productoJson.put("plan",producto.getPlan());
                if(productosLog.contains(producto.traducirProducto().toUpperCase())){
                    productoJson.put("carrusel","1");
                }else{
                    productoJson.put("carrusel","0");
                }
                dataLogCarruselProductos.put(productoJson);
            }

            dataLogCarrusel.put("productos",dataLogCarruselProductos);

            Log.i(TAG,"JSON Automatico "+dataLogCarrusel.toString());

            Simulador simulador = new Simulador();
            simulador.setManual(this);
            simulador.addObserver(this);

            ArrayList<String> parametros = new ArrayList<String>();
            parametros.add(dataLogCarrusel.toString());

            ArrayList<Object> params = new ArrayList<Object>();
            params.add(MainActivity.config.getCodigo());
            params.add("guardarLogCarruselAutomatico");
            params.add(parametros);

            simulador.execute(params);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public ProductoCotizador  llenarcotizacionTelevision(ProductoCotizador productoCotizador){

        if (!productoCotizador.getPlan().equalsIgnoreCase("-") && !productoCotizador.getTipoPeticion().equalsIgnoreCase("-")) {
            productoCotizador.setAdicionalesCotizador(cadcTelevision.listaAdicionalesConPromocion(cadcTelevision.listaAdicionales()));
            productoCotizador.setTotalAdicionales(cadcTelevision.calcularTotal());
            productoCotizador.setDecodificadores(cdcsDecodificadores.getDecos());
            productoCotizador.setTotalDecos(cdcsDecodificadores.obtenerTotalDecos());
            productoCotizador.setObjectDecodificador(cdcsDecodificadores.getDecodificadores());

        }

        return productoCotizador;
    }

    public void llenarcotizacionTelevision(ProductoCotizador productoCotizador, String tv, int contadorProd, boolean trioNuevo) {

        System.out.println("llenado TV ");

        if (!productoCotizador.getPlan().equalsIgnoreCase("-") && !productoCotizador.getTipoPeticion().equalsIgnoreCase("-")) {

            ArrayList<AdicionalCotizador> adicionalesCotizador = cadcTelevision.listaAdicionales();
            UtilidadesTarificadorNew.imprimirAdicionalesCotizacion(adicionalesCotizador);
            adicionalesCotizador = cadcInternet.listaAdicionalesConPromocion(adicionalesCotizador);
            UtilidadesTarificadorNew.imprimirAdicionalesCotizacion(adicionalesCotizador);

            ArrayList<String> descuentoTv = UtilidadesTarificadorNew.aplicarDescuentos(String.valueOf(productoCotizador.getDescuentoCargobasico()), String.valueOf(productoCotizador.getDuracionDescuento()));
            cotizacion.setAdicionales(agregarAdicionalesGratis(productoCotizador.getPlanFacturacionInd(), productoCotizador.getPlanFacturacionEmp(),
                    cadcTelevision.arrayAdicionales(), productoCotizador.getPlan(), tv, "" + contadorProd, trioNuevo,
                    descuentoTv.get(0).toString(), descuentoTv.get(1).toString()));

            cotizacion.setDecodificadores(cdcsDecodificadores.getDecos());
            cotizacion.setTotalDecos(cdcsDecodificadores.obtenerTotalDecos());
            cotizacion.setObjectDecodificador(cdcsDecodificadores.getDecodificadores());

            String descuentoCadena = Utilidades.limpiarDecimales(String.valueOf(productoCotizador.getDescuentoCargobasico()));

            ArrayList<String> descuento = UtilidadesTarificadorNew.aplicarDescuentos(descuentoCadena, String.valueOf(productoCotizador.getDuracionDescuento()));
            cotizacion.Television(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), String.valueOf(productoCotizador.getCargoBasicoInd()),
                    String.valueOf(productoCotizador.getCargoBasicoEmp()), "0", "0", descuento.get(0).toString(), descuento.get(1).toString(), "",
                    "", getTipoTecnologiaExistente("TV"));

            cotizacion.setItemPromocionesAdicionales(itemPromocionesAdicionales);
            boolean aplicarAd = false;//valor quemado corregir
            cotizacion.setAplicarAd(aplicarAd);

            cotizacion.setPlanFacturacionTv_I(productoCotizador.getPlanFacturacionInd());
            cotizacion.setPlanFacturacionTv_P(productoCotizador.getPlanFacturacionEmp());

            cotizacion.TelevisionCargos(String.valueOf(productoCotizador.getPagoAnticipado()), String.valueOf(productoCotizador.getTotalPagoParcial()));

            cotizacion.setTipoCotizacionTv(Utilidades.planNumerico(productoCotizador.getTipoPeticion()));
            //contProductos++;
        } else {

            cotizacion.Television(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), String.valueOf(productoCotizador.getCargoBasicoInd()),
                    String.valueOf(productoCotizador.getCargoBasicoEmp()));
            cotizacion.setAdicionales(cadcTelevision.arrayAdicionales());
        }
    }

    public ProductoCotizador  llenarcotizacionTelefonia(ProductoCotizador productoCotizador){

        if (!productoCotizador.getPlan().equalsIgnoreCase("-") && !productoCotizador.getTipoPeticion().equalsIgnoreCase("-")) {
            productoCotizador.setAdicionalesCotizador(cadcTelefonia.listaAdicionalesConPromocion(cadcTelefonia.listaAdicionales()));
            productoCotizador.setTotalAdicionales(cadcTelefonia.calcularTotal());

        }

        return productoCotizador;
    }

    public void llenarcotizacionTelefonia(ProductoCotizador productoCotizador, String to, int contadorProd, boolean trioNuevo) {

       if (!productoCotizador.getPlan().equalsIgnoreCase("-") && !productoCotizador.getTipoPeticion().equalsIgnoreCase("-")) {

            String descuentoCadena = Utilidades.limpiarDecimales(String.valueOf(productoCotizador.getDescuentoCargobasico()));

            ArrayList<String> descuento = UtilidadesTarificadorNew.aplicarDescuentos(descuentoCadena, String.valueOf(productoCotizador.getDuracionDescuento()));

            cotizacion.Telefonia(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), "" + productoCotizador.getCargoBasicoInd(),
                    "" + productoCotizador.getCargoBasicoEmp(), "0", "0", descuento.get(0).toString(), descuento.get(1).toString(), "",
                    "", getTipoTecnologiaExistente("TO"));

            cotizacion.setPlanFacturacionTo_I(String.valueOf(productoCotizador.getPlanFacturacionInd()));
            cotizacion.setPlanFacturacionTo_P(String.valueOf(productoCotizador.getPlanFacturacionEmp()));

            cotizacion.TelefoniaCargos(String.valueOf(productoCotizador.getPagoAnticipado()), String.valueOf(productoCotizador.getTotalPagoParcial()));

            String tipoCotizacion = Utilidades.planNumerico(productoCotizador.getTipoPeticion());
            cotizacion.setTipoCotizacionTo(tipoCotizacion);

            if (tipoCotizacion.equals("1")) {

                cotizacion.setSegundaTelefonia(UtilidadesTarificadorNew.validarSegundaTelefonia(cliente));
            }

            cotizacion.setAdicionalesTo(cadcTelefonia.arrayAdicionales());
            cotizacion.setTotalAdicionalesTo(String.valueOf(cadcTelefonia.calcularTotal()));

        } else {
            cotizacion.Telefonia(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), String.valueOf(productoCotizador.getCargoBasicoInd()),
                    String.valueOf(productoCotizador.getCargoBasicoEmp()));
            cotizacion.setAdicionalesTo(cadcTelefonia.arrayAdicionales());
            cotizacion.setTotalAdicionalesTo(String.valueOf(cadcTelefonia.calcularTotal()));
        }

    }

    public ProductoCotizador llenarcotizacionInternet(ProductoCotizador productoCotizador){

        if (!productoCotizador.getPlan().equalsIgnoreCase("-") && !productoCotizador.getTipoPeticion().equalsIgnoreCase("-")) {
            productoCotizador.setAdicionalesCotizador(cadcInternet.listaAdicionalesConPromocion(cadcInternet.listaAdicionales()));
            productoCotizador.setTotalAdicionales(cadcInternet.calcularTotal());

        }

        return productoCotizador;
    }

    public void llenarcotizacionInternet(ProductoCotizador productoCotizador, CotizacionCliente cotizacionCliente, String ba, int contadorProd, boolean trioNuevo, boolean HBOGOExistente, CrackleExistente crackleExistente) {

        String descuentoCadena = Utilidades.limpiarDecimales(String.valueOf(productoCotizador.getDescuentoCargobasico()));

        ArrayList<String> descuento = UtilidadesTarificadorNew.aplicarDescuentos(descuentoCadena, String.valueOf(productoCotizador.getDuracionDescuento()));

        cotizacion.setRetiroHBOGO(false);
        if (!productoCotizador.getPlan().equalsIgnoreCase("-") && !productoCotizador.getTipoPeticion().equalsIgnoreCase("-")) {
            ArrayList<AdicionalCotizador> adicionalesCotizador = cadcInternet.listaAdicionales();
            adicionalesCotizador = cadcInternet.listaAdicionalesConPromocion(adicionalesCotizador);
            // System.out.println("descuentoTo " + descuentoTo);
            cotizacion.Internet(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), "" + productoCotizador.getCargoBasicoInd(),
                    "" + productoCotizador.getCargoBasicoEmp(), "0", "0", descuento.get(0).toString(), descuento.get(1).toString(), "",
                    "", getTipoTecnologiaExistente("BA"));

            cotizacion.setPlanFacturacionBa_I(String.valueOf(productoCotizador.getPlanFacturacionInd()));
            cotizacion.setPlanFacturacionBa_P(String.valueOf(productoCotizador.getPlanFacturacionEmp()));

            cotizacion.InternetCargos(String.valueOf(productoCotizador.getPagoAnticipado()), String.valueOf(productoCotizador.getTotalPagoParcial()));

            String tipoCotizacion = Utilidades.planNumerico(productoCotizador.getTipoPeticion());
            cotizacion.setTipoCotizacionBa(tipoCotizacion);
            cotizacion.setRetiroHBOGO(false);
            UtilidadesTarificadorNew.imprimirAdicionales(cotizacion.getAdicionalesBa(),"Cotizacion.getAdicionalesBa() Ante HBO GO Existente ");
            cotizacion.setAdicionalesBa(cadcInternet.arrayAdicionales());
            if(HBOGOExistente){
                if(!cprdTelevision.getPlan().equalsIgnoreCase(Utilidades.inicial)){
                    cotizacion.setAdicionalesBa(cadcInternet.arrayAdicionalesHBOGO(cotizacion.getAdicionalesBa()));
                    cotizacion.setRetiroHBOGO(true);
                }else {
                    String adicional =  cadcInternet.arrayAdicionalesHBOGO(cotizacion.getAdicionalesBa())[0][0];
                    if(!UtilidadesTarificadorNew.validarVelocidadInternet(productoCotizador.getPlan(),adicional,cliente)){
                        cotizacion.setAdicionalesBa(cadcInternet.arrayAdicionalesHBOGO(cotizacion.getAdicionalesBa()));
                        cotizacion.setRetiroHBOGO(true);
                    } /*else {
                        if(cotizacion.getTipoOferta().equalsIgnoreCase("DUO") || cotizacion.getTipoOferta().equalsIgnoreCase("TRIO")){
                            cotizacion.setAdicionalesBa(cadcInternet.arrayAdicionalesHBOGO(cotizacion.getAdicionalesBa()));
                            cotizacion.setRetiroHBOGO(true);
                        } else {
                            //cotizacion.setAdicionalesBa(cadcInternet.arrayAdicionales());
                        }
                    }*/

                }

            }

            UtilidadesTarificadorNew.imprimirAdicionales(cotizacion.getAdicionalesBa(),"Cotizacion.getAdicionalesBa() Despues HBO GO Existente ");
            cotizacion.setRetiroCrackleBa(false);
            UtilidadesTarificadorNew.imprimirAdicionales(cotizacion.getAdicionalesBa(),"Cotizacion.getAdicionalesBa() Antes Crackle Existente ");
            if(crackleExistente !=null  && crackleExistente.isExistente() && crackleExistente.getTipoProducto().equalsIgnoreCase("INTER")){
                //String adicional =  cadcInternet.arrayAdicionalesCrackle()[0][0];
                if(!UtilidadesTarificadorNew.validarVelocidadInternet(productoCotizador.getPlan(),"Crackle",cliente) && productoCotizador.getTipoPeticion().equalsIgnoreCase("C")){
                    cotizacion.setAdicionalesBa(cadcInternet.arrayAdicionalesCrackle(cotizacion.getAdicionalesBa()));
                    cotizacion.setRetiroCrackleBa(true);
                }
            }

            UtilidadesTarificadorNew.imprimirAdicionales(cotizacion.getAdicionalesBa(),"Cotizacion.getAdicionalesBa() Despues Crackle Existente ");

            cotizacion.setTotalAdicionalesBa(String.valueOf(cadcInternet.calcularTotal()));

            if (cotizacionCliente.getGotaba().isControlGota()) {

                String nombreGota = "Gota de " + cotizacionCliente.getGotaba().getVelocidadInicial() + " a " + cotizacionCliente.getGotaba().getVelocidadFinal();

                cotizacion.setGota(cotizacionCliente.getGotaba().isControlGota(), (int) Math.round(cotizacionCliente.getGotaba().getValorGota()),
                        (int) Math.round(cotizacionCliente.getGotaba().getValorGotaSinIva()), cotizacionCliente.getGotaba().getVelocidadInicial(), cotizacionCliente.getGotaba().getVelocidadFinal(),
                        nombreGota);

            } else {
                cotizacion.setControlGota(false);
            }
        } else {
            if(HBOGOExistente){

                String tipoCotizacion = Utilidades.planNumerico("E");
                cotizacion.setTipoCotizacionBa(tipoCotizacion);

                cotizacion.setPlanFacturacionBa_I("");
                cotizacion.setPlanFacturacionBa_P("");

                cotizacion.Internet("E", "Internet Existente", "0",
                        "0");

                cotizacion.setAdicionalesBa(cadcInternet.arrayAdicionalesHBOGO(cotizacion.getAdicionalesBa()));
                cotizacion.setRetiroHBOGO(true);
            } else {
                cotizacion.Internet(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), String.valueOf(productoCotizador.getCargoBasicoInd()),
                        String.valueOf(productoCotizador.getCargoBasicoEmp()));

                cotizacion.setAdicionalesBa(cadcInternet.arrayAdicionales());
            }



            cotizacion.setTotalAdicionalesBa(String.valueOf(cadcInternet.calcularTotal()));
        }
        //contProductos++;
        cotizacion.setItemPromocionesAdicionalesInternet(itemPromocionesAdicionalesInternet);

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

                    totalDescuento = totalDescuento - resta;

                }
            }

        }

        if (descuentoBa.contains("%")) {

            ArrayList<Object> listDescuentos = Utilidades.precioDescuento(descuentoBa, precioBa);

            if ((Boolean) listDescuentos.get(0)) {

                if (!listDescuentos.get(1).equals(0.0) && !listDescuentos.get(1).equals(0)) {

                    double resta = Utilidades.convertirDouble(listDescuentos.get(1).toString(),
                            "listDescuentos.get(1)");
                    totalDescuento = totalDescuento - resta;
                }
            }

        }

        if (descuentoTv.contains("%")) {

            ArrayList<Object> listDescuentos = Utilidades.precioDescuento(descuentoTv, precioTv);

            if ((Boolean) listDescuentos.get(0)) {

                if (!listDescuentos.get(1).equals(0.0) && !listDescuentos.get(1).equals(0)) {
                    double resta = Utilidades.convertirDouble(listDescuentos.get(1).toString(),
                            "listDescuentos.get(1)");
                    totalDescuento = totalDescuento - resta;

                }
            }

        }

       if (descuentoAd != 0 && descuentoAd != 00) {
            totalDescuento = totalDescuento - descuentoAd;
        }

        return "" + totalDescuento;
    }

    public void preciosAdDescuento(String[][] adicionales, String totalAdicionales,
                                   ArrayList<ItemPromocionesAdicionales> promocioneAd) {

        double precioDescuentoAD = 0;
        controlDescuentosAd = false;
        preciosConDescuentoAd = 0.0;
        precioDescuentosAd = 0.0;

        for (int i = 0; i < adicionales.length; i++) {

            double precioAd = Utilidades.convertirDouble(adicionales[i][1], "adicionales[" + i + "][1]");

            for (int j = 0; j < cotizacion.getItemPromocionesAdicionales().size(); j++) {

                if (adicionales[i][0]
                        .equalsIgnoreCase(cotizacion.getItemPromocionesAdicionales().get(j).getAdicional())) {

                    if (!cotizacion.getItemPromocionesAdicionales().get(j).getDescuento().equals("0")) {

                        int descuento = Utilidades.convertirNumericos(
                                cotizacion.getItemPromocionesAdicionales().get(j).getDescuento(),
                                "cotizacion.getItemPromocionesAdicionales().get(" + i + ").getDescuento()");

                        precioDescuentosAd += (precioAd * descuento) / 100;

                        precioAd = precioAd - (precioAd * descuento) / 100;

                        controlDescuentosAd = true;
                    }
                }


            }

            precioDescuentoAD += precioAd;

        }

        if (precioDescuentoAD != 0 && precioDescuentoAD != 0.0) {
            preciosConDescuentoAd = precioDescuentoAD;
        }
    }

    /*metodos dependientes*/

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

    private String[][] obtenerAdicionales() {
        String[][] adicionales = null;
//        ListaCheckedAdapter lca = (ListaCheckedAdapter) lsvAdicionales.getAdapter();
//        if (lca != null) {
//            adicionales = lca.Adicionales();
//        }
        return adicionales;

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

        System.out.println("nuevos " + nuevos);

        return nuevos;
    }

    public boolean aplicaDescuentoTo() {

        boolean aplica = false;
        boolean trioDigitalNuevo = true;

        if (cotizacion.getContadorProductos().equalsIgnoreCase("3")) {

            trioDigitalNuevo = UtilidadesTarificador.validarTrioDigitalNuevo(cotizacion.getTipoTo(),
                    cotizacion.getTelefonia(), cotizacion.getTipoTv(), cotizacion.getTelevision(),
                    cotizacion.getTipoBa(), cotizacion.getInternet(), this);
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

        return aplica;
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

    /*Metodos Validaciones */

    public boolean validarCobertura() {

        boolean pasa = false;

        if (Utilidades.excluirMunicipal("bloquearCobertura", "bloquearCobertura", cliente.getCiudad())) {

            if (productosNuevos()) {
                if ((cotizacion.getTipoTv() != null && !cotizacion.getTipoTv().equalsIgnoreCase("-")) && !cotizacion.getTelevision().equalsIgnoreCase("")) {

                    if (bloqueoCobertura.isHFCDigital()) {
                        cotizacion.setHFCDigital(true);
                    } else {
                        cotizacion.setHFCDigital(false);
                    }

                    pasa = true;
                } else {
                    if (bloqueoCobertura
                            .isCoberturaHFC()) {
                        pasa = true;
                    } else if (bloqueoCobertura
                            .isCoberturaREDCO()) {
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

    public boolean validarCarrusel() {
        boolean validar = true;

        productosLog = new ArrayList<String>();

        if (Utilidades.validarPermiso("carrusel") || cliente.isControlCerca() || Utilidades.CoberturaRural(cliente)) {
            return true;
        }

        if (Utilidades.excluir("MunicipiosCarrusel", cliente.getCiudad())) {
            if (cliente.isControlCarrusel()) {
                if (cliente.getCodigoCarrusel().equals("02") || cliente.getCodigoCarrusel().equals("-1")) {
                } else if (cliente.getDireccion().equalsIgnoreCase(cliente.getDireccionCarrusel())) {
                    if (cliente.getCodigoCarrusel().equals("00") && cliente.getArraycarrusel() != null) {
                        if (cliente.getArraycarrusel().size() > 0) {
                            for (int i = 0; i < cliente.getArraycarrusel().size(); i++) {
                                if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TO")) {
                                    if(cprdTelefonia.isActivo()){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                cotizacion.getTipoCotizacionTo(), cotizacion.getTelefonia(), this)) {
                                            productosLog.add("TO");
                                        }
                                    }
                                } else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TV")) {
                                    if(cprdTelevision.isActivo()){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                cotizacion.getTipoCotizacionTv(), cotizacion.getTelevision(), this)) {
                                            productosLog.add("TV");
                                        }
                                    }

                                } else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("BA")) {
                                    if(cprdInternet.isActivo()){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                cotizacion.getTipoCotizacionBa(), cotizacion.getInternet(), this)) {
                                            productosLog.add("BA");
                                        }
                                    }
                                }
                            }

                            if(productosLog.size() > 0){
                                validar = false;
                                for (String prod: productosLog) {
                                    Log.i(TAG,"LOG "+prod);
                                }
                            }



                        }
                    } else if (cliente.getCodigoCarrusel().equals("01")) {
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
        if (!scooring.isValidarCartera()) {
            if (cliente.getScooringune() != null) {
                if (cliente.getScooringune().getDocumentoScooring().equalsIgnoreCase(cliente.getCedula())) {
                    if (cliente.getScooringune().isValidarScooring()) {
                        if (!cliente.getScooringune().isPasaScooring()) {
                            if (productosNuevos()) {
                                if (codigoPA.equalsIgnoreCase("00")) {
                                    if (Utilidades.excluirEstadosPagoAnticipado()
                                            .contains(cliente.getScooringune().getRazonScooring())) {
                                        valid = true;
                                        cliente.setPagoAnticipado("SI");
                                    } else {
                                        valid = false;
                                        cliente.setPagoAnticipado("NO");
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
                            }
                        } else {
                            if (!productosNuevos()) {
                                cliente.setDomiciliacion("NO");
                                cliente.getScooringune().setNoAplicaScooring(true);
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

    public boolean validarAgendaSiebel() {
        boolean agenda = true;
        System.out.println("cliente.getEstandarizarSiebel()  "+cliente.getEstandarizarSiebel() );

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
                if (!Validaciones.validarAgendaSiebel(cliente)) {
                    agenda = false;
                } else {
                    agenda = true;
                }
            }
        }
        return agenda;
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

    /*Validar si se necesita*/

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
        String adicional = null;

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
        if(planFactura != null && !planFactura.equalsIgnoreCase("null")) {
            adicional = Utilidades.adicionalesGratis("adicionalesGratis", planFactura);
        }

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

        if (planFacturaEmp != null) {
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
        }

        if (adicional != null) {
            precioAdicional = UtilidadesTarificador.precioAdicional(adicional, Departamento(),
                    (String) spnestrato.getSelectedItem());
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

    /*lanzar actividades*/

    public void resultCotizador(String tipoIngreso) {
        Intent intent = new Intent();
        intent.putExtra("cotizacion", cotizacion);
        intent.putExtra("cotizacionCliente", cotizacionCliente);
        intent.putExtra("cliente", cliente);
        intent.putExtra("tipoIngreso", tipoIngreso);
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
    }

    private void validarDebitoAutomaticoExistente(){
        JSONObject data = new JSONObject();

        try {

            data.put("Cedula", cliente.getCedula());
            data.put("Ciudad", cliente.getCiudadDane());
            data.put("Direccion", cliente.getDireccion());

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }

        Log.d("Domiciliacion", data.toString());
        Log.d("Domiciliacion",Utilidades.camposUnicosCiudad("CRMCarrusel", cliente.getCiudad()));

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(Utilidades.camposUnicosCiudad("CRMCarrusel", cliente.getCiudad()));
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

    private void obtenerPagoParcialAnticipado() {
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);

        JSONObject data = new JSONObject();

        try {

            data.put("Direccion", cliente.getDireccion());
            data.put("Estrato", cliente.getEstrato());
            data.put("CiudadId", cliente.getCiudadAmc());
            data.put("Ciudad", cliente.getCiudad());
            data.put("Departamento", cliente.getDepartamento());
            data.put("Productos", new JSONArray());
            data.put("Origen", "VM");

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(data.toString());

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("consultarPagoParcialAnticipado");
        params.add(parametros);

        try{
            simulador.execute(params);
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private void tratarPagoParcialAnticipado(String respuesta) {

        Log.i("JSon", respuesta);

        try {
            JSONObject json = new JSONObject(respuesta);

            if (Configuracion.validarIntegridad(json.getString("data"), json.getString("crc"))) {
                String data = new String(Base64.decode(json.getString("data")));

                json = new JSONObject(data);

                codigoPP = json.getJSONObject("codigoRespuesta").getString("pagoParcial");
                codigoPA = json.getJSONObject("codigoRespuesta").getString("pagoAnticipado");
                codigoClienteNuevo = json.getJSONObject("codigoRespuesta").getString("clienteNuevo");

                JSONArray jsonProdutos = json.getJSONObject("data").getJSONArray("productos");
                JSONArray jsonValoresConexion = json.getJSONObject("data").getJSONArray("valorConexion");
                JSONArray jsonPagoParcial = json.getJSONObject("data").getJSONArray("pagoParcial");

                if(codigoClienteNuevo.equalsIgnoreCase("00")){
                    clienteNuevo = json.getJSONObject("data").getBoolean("clienteNuevo");
                }else {
                    clienteNuevo = true;
                }

                guardarPagoParcialAnticipado(jsonProdutos);
                UtilidadesPagoParcial.guardarValorConexion(jsonValoresConexion);
                UtilidadesPagoParcial.guardarPagoParcial(jsonPagoParcial);
            } else {
                Toast.makeText(this, getResources().getString(R.string.mensajevalidacionpagoparcial), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(this, getResources().getString(R.string.mensajevalidacionpagoparcial), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private void guardarPagoParcialAnticipado(JSONArray productos) {
        MainActivity.basedatos.eliminar("pagoparcialanticipado", null, null);
        try {
            for (int i = 0; i < productos.length(); i++) {
                ContentValues cv = new ContentValues();
                JSONObject producto = productos.getJSONObject(i);
                cv.put("producto", producto.getString("nombre"));
                cv.put("servicio", producto.getString("servicio"));
                cv.put("pagoparcial", producto.getString("pagoParcial"));
                cv.put("descuento", producto.getString("descuentoPagoParcial"));
                cv.put("pagoanticipado", producto.getString("pagoAnticipado"));

                MainActivity.basedatos.insertar("pagoparcialanticipado", cv);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void tratarLogAutomaticoCarrusel(String respuesta){

        try {
            JSONObject json = new JSONObject(respuesta);

            if (Configuracion.validarIntegridad(json.getString("data"), json.getString("crc"))) {
                String data = new String(Base64.decode(json.getString("data")));

                json = new JSONObject(data);

                if(json.getString("codigoMensaje").equalsIgnoreCase("00")){
                    cliente.setIdGerenciaExistente(json.get("idGerencia").toString());
                }else {
                    //Toast.makeText(this, getResources().getString(R.string.logautomaticocarrusel), Toast.LENGTH_LONG).show();
                }

                String prodcutosCarrusel = "";
                for(String producto: productosLog){
                    prodcutosCarrusel += producto+"-";
                }

                dialogo = new Dialogo(this,Dialogo.DIALOGO_CUSTOM,"Carrusel",getResources().getString(R.string.mensajeCarrusel)+prodcutosCarrusel.substring(0,prodcutosCarrusel.length()-1)+getResources().getString(R.string.mensajeCarrusel2),"Prospectar","Reconfigurar",carruselOK,carruselCANCEL);
                dialogo.dialogo.show();

            } else {
                //Toast.makeText(this, getResources().getString(R.string.logautomaticocarrusel), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    DialogInterface.OnClickListener carruselOK = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.i(TAG,"Prospectar");
            resultCotizador("gerencia de ruta");
        }
    };

    DialogInterface.OnClickListener carruselCANCEL = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.i(TAG,"Reconfigurar");
        }
    };

    private boolean validarCotizacionClienteNuevo(Cotizacion cotizacion){

        boolean cotizacionValida = false;

        if (!Utilidades.excluir("excluirppca", cliente.getCiudad())) {
            if(clienteNuevo && codigoClienteNuevo.equalsIgnoreCase("00")) {
                if(!UtilidadesTarificadorNew.ventaConExistente(cotizacion)){
                    cotizacionValida = true;
                }
            } else if (clienteNuevo && !codigoClienteNuevo.equalsIgnoreCase("00")) {
                cotizacionValida = true;
            } else {
                cotizacionValida = true;
            }
        } else {
            cotizacionValida = true;
        }


        return cotizacionValida;

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
                        cliente.setDomiciliacion("NO");
                        Dialogo dialogo = null;
                        if(!json.getJSONObject("data").getString("entidad").contains("BANCOLOMBIA")){
                            if(json.getJSONObject("data").getString("tipoDebitoAutomatico").equalsIgnoreCase("DB")){
                                dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,getResources().getString(R.string.mensajedebitoautomaticodebito));
                            }else{
                                dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,getResources().getString(R.string.mensajedebitoautomaticredito));
                            }
                        }else {
                            dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,getResources().getString(R.string.mensajedebitoautomaticredito));
                        }

                        dialogo.dialogo.show();
                        dialogo.dialogo.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                procesarCotizacion(cotizacionCliente);
                            }
                        });
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


        if(!bloqueo){
            procesarCotizacion(cotizacionCliente);
        }



    }

    @Override
    public void addObserver(ObserverTotales o) {
        observerTotales = o;
    }

    @Override
    public void removeObserver(ObserverTotales o) {
        observerTotales = null;
    }

    @Override
    public void notifyObserver() {

    }
}