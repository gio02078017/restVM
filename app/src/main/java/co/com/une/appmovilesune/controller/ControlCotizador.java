package co.com.une.appmovilesune.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.BloqueoCobertura;
import co.com.une.appmovilesune.adapters.ItemKeyValue2;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ListaCheckedAdapter;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;
import co.com.une.appmovilesune.complements.Validaciones;
import co.com.une.appmovilesune.components.CompAdicional;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.components.CompProducto;
import co.com.une.appmovilesune.components.CompTotal;
import co.com.une.appmovilesune.components.CompTotalCotizador;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.ObserverTotales;
import co.com.une.appmovilesune.interfaces.SubjectTotales;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.CotizacionCliente;
import co.com.une.appmovilesune.model.GotaBa;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.TarificadorNew;

/**
 * Created by davids on 18/10/16.
 */

public class ControlCotizador extends Activity implements Observer, SubjectTotales{

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
    private CompProducto cprdInternet;
    private CompProducto cprdTelefonia;
    private CompAdicional cadcTelefonia;

    private CompTotalCotizador cttlTotales;

    private TarificadorNew tarificador;
    private Cliente cliente;
    private Cotizacion cotizacion;
    private Scooring scooring;

    ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();
    ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionalesGratis = new ArrayList<ItemPromocionesAdicionales>();

    private ArrayList<ItemKeyValue2> datosValidacion = new ArrayList<ItemKeyValue2>();
    private ArrayList<ItemKeyValue2> countValidacion = new ArrayList<ItemKeyValue2>();
    private ArrayList<String> countValidacion2 = new ArrayList<String>();

    private boolean validarAdicionales = true;
    private boolean validarEstandarizacion = true;
    private boolean validarTelefonoServicio = true;

    private boolean controlDescuentosAd = false;
    private double preciosConDescuentoAd = 0.0;
    private double precioDescuentosAd = 0.0;

    private ObserverTotales observerTotales;

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
            /*if (cotizacion != null) {
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

            }*/
        }

        chkHogarNuevo = (CheckBox) findViewById(R.id.chkHogarNuevo);
        chkAnaloga = (CheckBox) findViewById(R.id.chkAnaloga);
        spnestrato = (Spinner) findViewById(R.id.spnestrato);
        spntipooferta = (Spinner) findViewById(R.id.spntipooferta);
        spnoferta = (Spinner) findViewById(R.id.spnoferta);

        cprdTelevision = (CompProducto) findViewById(R.id.cprdTelevision);
        cadcTelevision = (CompAdicional) findViewById(R.id.cadcTelevision);
        cprdInternet = (CompProducto) findViewById(R.id.cprdInternet);
        cprdTelefonia = (CompProducto) findViewById(R.id.cprdTelefonia);
        cadcTelefonia = (CompAdicional) findViewById(R.id.cadcTelefonia);
        cttlTotales = (CompTotalCotizador) findViewById(R.id.cttlTotales);

        /*Se Agregan los observadores de adicionales a los producutos que cientan con la
        funcionalidad de adicionales*/
        cprdTelevision.addObserverAdicionales(cadcTelevision);
        cprdTelefonia.addObserverAdicionales(cadcTelefonia);

        cprdTelevision.addObserver(this);
        cadcTelevision.addObserver(this);
        cprdInternet.addObserver(this);
        cprdTelefonia.addObserver(this);
        cadcTelefonia.addObserver(this);

        cadcTelevision.setCliente(cliente);
        cadcTelefonia.setCliente(cliente);

        spnestrato.setOnItemSelectedListener(seleccionarEstrato);
        spntipooferta.setOnItemSelectedListener(seleccionarTipoOferta);
        spnoferta.setOnItemSelectedListener(seleccionarOferta);

        llenarEstrato();

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

        String clausula = "estrato like ? and tipo_paquete like ?";
        String[] valores = new String[]{"%" + estrato + "%", "%" + tipoPaquete + "%"};

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios", new String[]{"Oferta"}, clausula,
                valores, null, null, null);

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
        cprdTelevision.cargarPlanes("Antioquia", Integer.parseInt((String) spnestrato.getSelectedItem()), "HFC", (String) spnoferta.getSelectedItem());
        cprdInternet.cargarPlanes("Antioquia", Integer.parseInt((String) spnestrato.getSelectedItem()), "HFC", (String) spnoferta.getSelectedItem());
        cprdTelefonia.cargarPlanes("Antioquia", Integer.parseInt((String) spnestrato.getSelectedItem()), "HFC", (String) spnoferta.getSelectedItem());
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
            parametrizarComponentes();
            productosHabilitar((String) parent.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
                cprdTelevision.setActivo(false);
                cprdInternet.deshabilitarCheckProducto();
                cprdInternet.setActivo(false);
                cprdTelefonia.deshabilitarCheckProducto();
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

    }

    public void cotizar(View v) {
        cotizar();
    }

    public void cotizar() {


        System.out.println("hola");

        /*private CompProducto cprdTelevision;
        private CompProducto cprdInternet;
        private CompProducto cprdTelefonia;*/

        System.out.println("cprdTelevision " + cprdTelevision.getPlan());
        System.out.println("cprdInternet " + cprdInternet.getPlan());
        System.out.println("cprdTelefonia " + cprdTelefonia.getPlan());

        limpiarComp(cprdTelefonia);
        limpiarComp(cprdTelevision);
        limpiarComp(cprdInternet);

        String[][] adicionales = new String[0][0];

        tarificador.Consulta_Tarifaz(cprdTelefonia.getPeticionProducto(), cprdTelefonia.getPlan(),
                cprdTelevision.getPeticionProducto(), cprdTelevision.getPlan(),
                cprdInternet.getPeticionProducto(), cprdInternet.getPlan(), adicionales,
                0, (String) spnestrato.getSelectedItem(), false, cliente,
                UtilidadesTarificador.jsonDatos(cliente, "N/A", cliente.getTecnologia(), "0"), this,
                0);

        // ArrayList<ProductoCotizador> productos = tarificador.cotizacionVenta();

        CotizacionCliente cotizacionCliente = tarificador.cotizacionCliente();

        ArrayList<ProductoCotizador> productos = cotizacionCliente.getProductoCotizador();
        if (cotizacionCliente.getGotaba().isControlGota()) {
            System.out.println("cotizacionCliente.getGotaba().isControlGota() " + cotizacionCliente.getGotaba().isControlGota());
            System.out.println("cotizacionCliente.getGotaba().getValorGota() " + cotizacionCliente.getGotaba().getValorGota());
        }

        System.out.println("cotizacionCliente total Individual " + cotizacionCliente.getTotalIndividual());
        System.out.println("cotizacionCliente total Paquete " + cotizacionCliente.getTotalEmpaquetado());

        if (productos != null) {
            System.out.println("cantidad " + productos.size());

            for (int i = 0; i < productos.size(); i++) {

                System.out.println("tipoProducto " + productos.get(i).getTipo());

                switch (productos.get(i).getTipo()) {
                    case 0:
                        System.out.println("planProducto " + productos.get(i).getPlan());
                        System.out.println("promo " + productos.get(i).getDescuentoCargobasico());
                        System.out.println("tiempo promo " + productos.get(i).getDuracionDescuento());
                        llenarComp(cprdTelefonia, productos.get(i));
                        break;
                    case 1:
                        System.out.println("planProducto " + productos.get(i).getPlan());
                        llenarComp(cprdTelevision, productos.get(i));
                        break;
                    case 2:
                        System.out.println("planProducto " + productos.get(i).getPlan());
                        llenarComp(cprdInternet, productos.get(i));
                        break;
                }
            }
        }

        if (cotizacionCliente.getControl().equalsIgnoreCase("00")) {
            procesarCotizacion(cotizacionCliente);
        }
    }

    public void llenarComp(CompProducto comp, ProductoCotizador productos) {
        comp.setTxtvalorcargobasicoind("$" + productos.getCargoBasicoInd());
        comp.setTxtvalorcargobasicoemp("$" + productos.getCargoBasicoEmp());
        comp.setTxtvalordescuentocargobasico(productos.getDescuentoCargobasico() + "%");
        comp.setTxtduraciondescuentocargobasico(productos.getDuracionDescuento() + " Meses");
    }

    public void limpiarComp(CompProducto comp) {
        comp.setTxtvalorcargobasicoind("$0.0");
        comp.setTxtvalorcargobasicoemp("$0.0");
        comp.setTxtvalordescuentocargobasico("0%");
        comp.setTxtduraciondescuentocargobasico("0 Meses");
    }

    public void procesarCotizacion(CotizacionCliente cotizacionCliente) {

        boolean trioNuevo = false;
        controlDescuentosAd = false;
        preciosConDescuentoAd = 0.0;
        precioDescuentosAd = 0.0;

//        tarificador.Consulta_Tarifaz(cprdTelefonia.getPeticionProducto(), cprdTelefonia.getPlan(),
//                cprdTelevision.getPeticionProducto(), cprdTelevision.getPlan(),
//                cprdInternet.getPeticionProducto(), cprdInternet.getPlan(), adicionales,
//                0, (String) spnestrato.getSelectedItem(), false, cliente,
//                UtilidadesTarificador.jsonDatos(cliente, "N/A", cliente.getTecnologia(), "0"), this,
//                0);

        String tv = cprdTelefonia.getPeticionProducto();
        String to = cprdTelevision.getPeticionProducto();
        String ba = cprdInternet.getPeticionProducto();

        if (!spnestrato.getSelectedItem().equals("--Seleccione Estrato--")
                && (!cprdTelefonia.getPlan().equals("--Seleccione Producto--")
                || !cprdTelevision.getPlan().equals("--Seleccione Producto--")
                || !cprdInternet.getPlan().equals("--Seleccione Producto--"))) {

            if (cotizacion == null) {
                cotizacion = new Cotizacion();
            }

            System.out.println("cprdTelevision " + cprdTelevision.getPlan());
            System.out.println("cprdInternet " + cprdInternet.getPlan());
            System.out.println("cprdTelefonia " + cprdTelefonia.getPlan());

//            cotizacion_venta = tarificador.Array_Cotizacion_Venta();
//
//            ArrayList<String> telefonia = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_to_d);
//            ArrayList<String> television = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_tv_d);
//            ArrayList<String> adicionales = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_ad);
//            ArrayList<String> gota = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_gota);
//            ArrayList<String> internet = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_ba_d);
//            ArrayList<String> otros = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_otros);

            //System.out.println("otros " + otros);

            // ArrayList<ArrayList<String>> descuentos = consultarDescuentos();
            // ArrayList<ArrayList<String>> descuentos = consultarDescuentos2();
            //System.out.println("descuentos " + descuentos);

            //ArrayList<String> planesFacturacion = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_planes);

            //System.out.println("planesFacturacion " + planesFacturacion);

            // cotizacion.setAdicionales(obtenerAdicionales());

            if (cotizacionCliente.getContadorProductos() == 3) {

                if (tv.equalsIgnoreCase("N") && to.equalsIgnoreCase("N") && ba.equalsIgnoreCase("N")) {
                    trioNuevo = true;
                }
            }

            // if (otros != null && otros.size() > 0) {
            cotizacion.Otros("" + cotizacionCliente.getTotalIndividual(), "" + cotizacionCliente.getTotalEmpaquetado(), "" + cotizacionCliente.getContadorProductos(), cotizacionCliente.getEstrato());
            //}

//            System.out.println("tipoProducto " + productos.get(i).getTipo());
//
//            switch (productos.get(i).getTipo()) {
//                case 0:
//                    System.out.println("planProducto " + productos.get(i).getPlan());
//                    System.out.println("promo " + productos.get(i).getDescuentoCargobasico());
//                    System.out.println("tiempo promo " + productos.get(i).getDuracionDescuento());
//                    llenarComp(cprdTelefonia, productos.get(i));
//                    break;
//                case 1:
//                    System.out.println("planProducto " + productos.get(i).getPlan());
//                    llenarComp(cprdTelevision, productos.get(i));
//                    break;
//                case 2:
//                    System.out.println("planProducto " + productos.get(i).getPlan());
//                    llenarComp(cprdInternet, productos.get(i));
//                    break;
//            }

            if (cotizacionCliente.getProductoCotizador().size() > 0) {
                for (int i = 0; i < cotizacionCliente.getProductoCotizador().size(); i++) {
                    System.out.println("tipoProducto " + cotizacionCliente.getProductoCotizador().get(i).getTipo());

                    switch (cotizacionCliente.getProductoCotizador().get(i).getTipo()) {
                        case 0:
                            llenarcotizacionTelevision(cotizacionCliente.getProductoCotizador().get(i), tv, cotizacionCliente.getContadorProductos(), trioNuevo);
                            break;
                        case 1:
                            llenarcotizacionTelefonia(cotizacionCliente.getProductoCotizador().get(i), to, cotizacionCliente.getContadorProductos(), trioNuevo);
                            break;
                        case 2:
                            llenarcotizacionInternet(cotizacionCliente.getProductoCotizador().get(i), cotizacionCliente, to, cotizacionCliente.getContadorProductos(), trioNuevo);
                            break;
                    }
                }
            }

            //cotizacion.setTotalAdicionales(obtenerValorAdicionales().toString()); // se debe ingresar el dato

            System.out.println("ofertas " + cotizacionCliente.getOferta());

            cotizacion.setOferta(cotizacionCliente.getOferta());

            ArrayList<String> controlCotizacion = new ArrayList<String>();
            controlCotizacion.add(cotizacionCliente.getControl());


            System.out.println("controlCotizacion " + controlCotizacion);

            //int contProductos = 0;


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
                                            if (validarEstandarizacion || cliente.isControlCerca()) {

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
            //tabs.setCurrentTab(0);
        }

    }

    public void llenarcotizacionTelevision(ProductoCotizador productoCotizador, String tv, int contadorProd, boolean trioNuevo) {

        System.out.println("television " + productoCotizador.getPlan());
        ArrayList<String> descuentoTv = UtilidadesTarificadorNew.aplicarDescuentos(String.valueOf(productoCotizador.getDescuentoCargobasico()), String.valueOf(productoCotizador.getDuracionDescuento()));
        cotizacion.setAdicionales(agregarAdicionalesGratis(productoCotizador.getPlanFacturacionInd(), productoCotizador.getPlanFacturacionEmp(),
                obtenerAdicionales(), productoCotizador.getPlan(), tv, "" + contadorProd, trioNuevo,
                descuentoTv.get(0).toString(), descuentoTv.get(1).toString()));

        ArrayList<String> descuento = UtilidadesTarificadorNew.aplicarDescuentos(String.valueOf(productoCotizador.getDescuentoCargobasico()), String.valueOf(productoCotizador.getDuracionDescuento()));
        cotizacion.Television(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), "" + productoCotizador.getCargoBasicoInd(),
                "" + productoCotizador.getCargoBasicoEmp(), "0", "0", descuento.get(0).toString(), descuento.get(1).toString(), "",
                "", getTipoTecnologiaExistente("TV"));

        cotizacion.setItemPromocionesAdicionales(itemPromocionesAdicionales);
        boolean aplicarAd = false;//valor quemado corregir
        cotizacion.setAplicarAd(aplicarAd);

        cotizacion.setPlanFacturacionTv_I(productoCotizador.getPlanFacturacionInd());
        cotizacion.setPlanFacturacionTv_P(productoCotizador.getPlanFacturacionEmp());

        cotizacion.setTipoCotizacionTv(Utilidades.planNumerico(productoCotizador.getTipoPeticion()));
        //contProductos++;
    }

    public void llenarcotizacionTelefonia(ProductoCotizador productoCotizador, String to, int contadorProd, boolean trioNuevo) {

        ArrayList<String> descuento = UtilidadesTarificadorNew.aplicarDescuentos(String.valueOf(productoCotizador.getDescuentoCargobasico()), String.valueOf(productoCotizador.getDuracionDescuento()));

        // System.out.println("descuentoTo " + descuentoTo);
        cotizacion.Internet(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), "" + productoCotizador.getCargoBasicoInd(),
                "" + productoCotizador.getCargoBasicoEmp(), "0", "0", descuento.get(0).toString(), descuento.get(1).toString(), "",
                "", getTipoTecnologiaExistente("TO"));

        cotizacion.setPlanFacturacionTo_I(String.valueOf(productoCotizador.getCargoBasicoInd()));
        cotizacion.setPlanFacturacionTo_P(String.valueOf(productoCotizador.getCargoBasicoEmp()));

        String tipoCotizacion = Utilidades.planNumerico(productoCotizador.getTipoPeticion());
        cotizacion.setTipoCotizacionTo(tipoCotizacion);

        if (tipoCotizacion.equals("1")) {
            System.out.println("Validar segunta TO");

            cotizacion.setSegundaTelefonia(UtilidadesTarificadorNew.validarSegundaTelefonia(cliente));
        }

        //contProductos++;

    }

    public void llenarcotizacionInternet(ProductoCotizador productoCotizador, CotizacionCliente cotizacionCliente, String ba, int contadorProd, boolean trioNuevo) {

        ArrayList<String> descuento = UtilidadesTarificadorNew.aplicarDescuentos(String.valueOf(productoCotizador.getDescuentoCargobasico()), String.valueOf(productoCotizador.getDuracionDescuento()));

        // System.out.println("descuentoTo " + descuentoTo);
        cotizacion.Telefonia(productoCotizador.getTipoPeticion(), productoCotizador.getPlan(), "" + productoCotizador.getCargoBasicoInd(),
                "" + productoCotizador.getCargoBasicoEmp(), "0", "0", descuento.get(0).toString(), descuento.get(1).toString(), "",
                "", getTipoTecnologiaExistente("BA"));

        cotizacion.setPlanFacturacionBa_I(String.valueOf(productoCotizador.getCargoBasicoInd()));
        cotizacion.setPlanFacturacionBa_P(String.valueOf(productoCotizador.getCargoBasicoEmp()));

        String tipoCotizacion = Utilidades.planNumerico(productoCotizador.getTipoPeticion());
        cotizacion.setTipoCotizacionBa(tipoCotizacion);

        if (cotizacionCliente.getGotaba().isControlGota()) {

            String nombreGota = "Gota de " + cotizacionCliente.getGotaba().getVelocidadInicial() + " a " + cotizacionCliente.getGotaba().getVelocidadFinal();

            cotizacion.setGota(cotizacionCliente.getGotaba().isControlGota(), (int) Math.round(cotizacionCliente.getGotaba().getValorGota()),
                    (int) Math.round(cotizacionCliente.getGotaba().getValorGotaSinIva()), cotizacionCliente.getGotaba().getVelocidadInicial(), cotizacionCliente.getGotaba().getVelocidadFinal(),
                    nombreGota);

        } else {
            cotizacion.setControlGota(false);
        }
        //contProductos++;

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
                if (!cotizacion.getTipoTv().equalsIgnoreCase("-") && !cotizacion.getTelevision().equalsIgnoreCase("")) {

                    if (bloqueoCobertura.isHFCDigital()) {
                        cotizacion.setHFCDigital(true);
                    } else {
                        cotizacion.setHFCDigital(false);
                    }

                    if (bloqueoCobertura.getMedioTV().equalsIgnoreCase("HFC")) {
                        if (bloqueoCobertura
                                .isCoberturaHFC()) {
                            pasa = true;
                        } else if (bloqueoCobertura
                                .isCoberturaREDCO()) {
                            bloqueoCobertura.setConsultaMedioTV("IPTV");
                            Utilidades.MensajesToast(getResources().getString(R.string.cambiomedioinstalaciontviptv),
                                    this);
                            cambioMedioTV = true;
                        }

                    } else if (bloqueoCobertura.getMedioTV().equalsIgnoreCase("IPTV")) {
                        if (bloqueoCobertura
                                .isCoberturaREDCO()) {
                            pasa = true;
                        } else if (bloqueoCobertura
                                .isCoberturaHFC()) {
                            bloqueoCobertura.setConsultaMedioTV("HFC");
                            Utilidades.MensajesToast(getResources().getString(R.string.cambiomedioinstalaciontvhfc),
                                    this);
                            cambioMedioTV = true;
                        }
                    }

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

        if (Utilidades.validarPermiso("carrusel") || cliente.isControlCerca()) {
            return true;
        }

        if (Utilidades.excluir("MunicipiosCarrusel", cliente.getCiudad())) {
            if (cliente.isControlCarrusel()) {
                if (cliente.getCodigoCarrusel().equals("02") || cliente.getCodigoCarrusel().equals("-1")) {
                } else if (cliente.getCedula().equalsIgnoreCase(cliente.getDocumentoCarrusel())
                        && cliente.getDireccion().equalsIgnoreCase(cliente.getDireccionCarrusel())) {
                    if (cliente.getCodigoCarrusel().equals("00") && cliente.getArraycarrusel() != null) {
                        if (cliente.getArraycarrusel().size() > 0) {
                            for (int i = 0; i < cliente.getArraycarrusel().size(); i++) {
                                if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TO")) {
                                    if (!UtilidadesTarificador.validarCarruselProductos(
                                            cotizacion.getTipoCotizacionTo(), cotizacion.getTelefonia(), this)) {
                                        validar = false;
                                        break;
                                    }
                                } else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TV")) {
                                    if (!UtilidadesTarificador.validarCarruselProductos(
                                            cotizacion.getTipoCotizacionTv(), cotizacion.getTelevision(), this)) {
                                        validar = false;
                                        break;
                                    }
                                } else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("BA")) {
                                    if (!UtilidadesTarificador.validarCarruselProductos(
                                            cotizacion.getTipoCotizacionBa(), cotizacion.getInternet(), this)) {
                                        validar = false;
                                        break;
                                    }
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

        System.out.println();
        if (!scooring.isValidarCartera()) {
            if (cliente.getScooringune() != null) {
                if (cliente.getScooringune().getDocumentoScooring().equalsIgnoreCase(cliente.getCedula())) {
                    if (cliente.getScooringune().isValidarScooring()) {
                        if (!cliente.getScooringune().isPasaScooring()) {
                            if (productosNuevos()) {
                                if (cliente.getDomiciliacion().equals("SI")) {
                                    if (Utilidades.excluirEstadosDomiciliacion()
                                            .contains(cliente.getScooringune().getRazonScooring())) {
                                        valid = true;
                                        cliente.setBloqueoDomiciliacion("1");
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
                    agenda = false;
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
        intent.putExtra("cliente", cliente);
        intent.putExtra("tipoIngreso", tipoIngreso);
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
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
