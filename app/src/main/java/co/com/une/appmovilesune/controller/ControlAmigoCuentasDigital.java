package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.Toast;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ItemTarificador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.CompADDigital;
import co.com.une.appmovilesune.components.CompBADigital;
import co.com.une.appmovilesune.components.CompConfigurable;
import co.com.une.appmovilesune.components.CompDecos;
import co.com.une.appmovilesune.components.CompListaAdicionales;
import co.com.une.appmovilesune.components.CompPortafolio;
import co.com.une.appmovilesune.components.CompTODigital;
import co.com.une.appmovilesune.components.CompTVDigital;
import co.com.une.appmovilesune.components.CompTotal;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.AdicionalAMG;
import co.com.une.appmovilesune.model.AmigoCuentas;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Configuracion;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.ProductoAMG;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.Tarificador;

public class ControlAmigoCuentasDigital extends Activity implements Observer {

    public static final String TAG = "ControlAMCD";

    private TituloPrincipal tp;
    private TabHost tabs;
    private LinearLayout llyCargosBasicos, llyAdicionales;
    private ScrollView svPortafolio, svOferta, svOfertaBalines, svOfertaConfigurable;
    private Resources res;
    private Cliente cliente;
    private Scooring scooring;
    private AmigoCuentas ac;
    private Cotizacion cotizacion;
    private boolean nuevo = false;

    private CompTVDigital cmpTVDigital;
    private CompBADigital cmpBADigital;
    private CompTODigital cmpTODigital;
    private CompADDigital cmpAdicionales, cmpAdicionalesConf, cmpAdicionalesBal;
    private CompTotal cmpTotalPaquetePortafolio, cmpTotalPaqueteAdicionalesPortafolio, cmpTotalPaquete,
            cmpTotalPaqueteAdicionales, cmpTotalPaqueteConf, cmpTotalPaqueteAdicionalesConf, cmpTotalPaqueteBal,
            cmpTotalPaqueteAdicionalesBal;

    private CompConfigurable cmpConfTO, cmpConfTV, cmpConfBA, cmpAdicHD, cmpAdicHDConf, cmpAdicHDBal, cmpBalTV,
            cmpBalBA, cmpBalTO;

    private CompDecos cmpDecosOferta, cmpDecosBalin, cmpDecosConfig;

    private boolean validarAdicionales = true;
    private boolean validarEstandarizacion = true;
    private boolean validarTelefonoServicio = true;

    String descTo = "--Seleccione Descuento--", durDescTo = "N/A", descTv = "--Seleccione Descuento--",
            durDescTv = "N/A", descBa = "--Seleccione Descuento--", durDescBa = "N/A";
    String planTo = "--Seleccione Tipo--", telefonia = "--Seleccione Producto--", idTelefonia = "", paTelefonia = "N/A",
            planTv = "--Seleccione Tipo--", television = "--Seleccione Producto--", idTelevision = "",
            paTelevision = "N/A", planBa = "--Seleccione Tipo--", internet = "--Seleccione Producto--", idInternet = "",
            paInternet = "N/A", pfAnterior = "";

    String ipDinamica = "";

    private String tecnologia = "";

    private String toTeccr, tvTeccr, baTeccr, teccr;
    private String totalDescuentosAdicionales = "0";

    private String tvAnaloga = "0";

    private ArrayList<String> productosLog;

    private Dialogo dialogo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewamigocuentasdigital);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo(getResources().getString(R.string.amigocuentas));

        svPortafolio = (ScrollView) findViewById(R.id.svPortafolio);
        svOferta = (ScrollView) findViewById(R.id.svOferta);
        svOfertaBalines = (ScrollView) findViewById(R.id.svOfertaBalines);
        svOfertaConfigurable = (ScrollView) findViewById(R.id.svOfertaConfigurable);

        llyCargosBasicos = (LinearLayout) findViewById(R.id.llyCargosBasicos);
        llyAdicionales = (LinearLayout) findViewById(R.id.llyAdicionales);

        cmpTotalPaquetePortafolio = (CompTotal) findViewById(R.id.cmpTotalPaquetePortafolio);
        cmpTotalPaqueteAdicionalesPortafolio = (CompTotal) findViewById(R.id.cmpTotalPaqueteAdicionalesPortafolio);

        cmpTVDigital = (CompTVDigital) findViewById(R.id.cmpTVDigital);
        cmpBADigital = (CompBADigital) findViewById(R.id.cmpBADigital);
        cmpTODigital = (CompTODigital) findViewById(R.id.cmpTODigital);
        cmpAdicionales = (CompADDigital) findViewById(R.id.cmpAdicionales);
        cmpDecosOferta = (CompDecos) findViewById(R.id.cmpDecosOferta);
        cmpAdicHD = (CompConfigurable) findViewById(R.id.cmpAdicHD);
        cmpTotalPaquete = (CompTotal) findViewById(R.id.cmpTotalPaquete);
        cmpTotalPaqueteAdicionales = (CompTotal) findViewById(R.id.cmpTotalPaqueteAdicionales);

        cmpBalTO = (CompConfigurable) findViewById(R.id.cmpBalTO);
        cmpBalTV = (CompConfigurable) findViewById(R.id.cmpBalTV);
        cmpBalBA = (CompConfigurable) findViewById(R.id.cmpBalBA);
        cmpAdicionalesBal = (CompADDigital) findViewById(R.id.cmpAdicionalesBal);
        cmpDecosBalin = (CompDecos) findViewById(R.id.cmpDecosBalin);
        cmpAdicHDBal = (CompConfigurable) findViewById(R.id.cmpAdicHDBal);
        cmpTotalPaqueteBal = (CompTotal) findViewById(R.id.cmpTotalPaqueteBal);
        cmpTotalPaqueteAdicionalesBal = (CompTotal) findViewById(R.id.cmpTotalPaqueteAdicionalesBal);

        cmpConfTO = (CompConfigurable) findViewById(R.id.cmpConfTO);
        cmpConfTV = (CompConfigurable) findViewById(R.id.cmpConfTV);
        cmpConfBA = (CompConfigurable) findViewById(R.id.cmpConfBA);
        cmpAdicionalesConf = (CompADDigital) findViewById(R.id.cmpAdicionalesConf);
        cmpDecosConfig = (CompDecos) findViewById(R.id.cmpDecosConfigurable);
        cmpAdicHDConf = (CompConfigurable) findViewById(R.id.cmpAdicHDConf);
        cmpTotalPaqueteConf = (CompTotal) findViewById(R.id.cmpTotalPaqueteConf);
        cmpTotalPaqueteAdicionalesConf = (CompTotal) findViewById(R.id.cmpTotalPaqueteAdicionalesConf);

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            cliente = (Cliente) reicieveParams.getSerializable("cliente");
            scooring = (Scooring) reicieveParams.getSerializable("scooring");
            ac = cliente.ac;

            cmpDecosOferta.setCiudad(cliente.getCiudad());
            cmpDecosOferta.addObserver(this);
            cmpDecosOferta.setOferta("oferta");
            cmpDecosBalin.setCiudad(cliente.getCiudad());
            cmpDecosBalin.addObserver(this);
            cmpDecosBalin.setOferta("balin");
            cmpDecosConfig.setCiudad(cliente.getCiudad());
            cmpDecosConfig.addObserver(this);
            cmpDecosConfig.setOferta("config");

            if (ac != null) {
                if (ac.decodificadores != null) {
                    cmpDecosOferta.setDecosExistentes(ac.decodificadores);
                    cmpDecosBalin.setDecosExistentes(ac.decodificadores);
                    cmpDecosConfig.setDecosExistentes(ac.decodificadores);
                }
            }

            try {
                System.out.println(cliente.getCobertura());
                JSONObject clicob = new JSONObject(cliente.getCobertura());
                JSONObject cob = clicob.getJSONObject("Cobertura");

                teccr = Utilidades.obtenerCobertura(cliente.getCobertura(), this);

                if (Utilidades.tratarCobertura(cliente.getCobertura(), this)) {
                    tecnologia = "HFC";
                } else {
                    if (cliente.getCiudad().equals("Cartagena")) {
                        tecnologia = "HFC";
                    } else {
                        tecnologia = "IPTV";
                    }

                }

                // if(cob.getString("COBERTURA_HFC").equalsIgnoreCase("NO")){
                // tecnologia = "IPTV";
                // }else{
                // tecnologia = "HFC";
                // }
                //
                // tecnologia =
            } catch (Exception e) {
                // TODO: handle exception
                tecnologia = "HFC";
            }

        } else {
            System.out.println("no  amigo Cuentas");
        }

        if (savedInstanceState != null) {
            cliente = (Cliente) savedInstanceState.getSerializable("cliente");
            scooring = (Scooring) savedInstanceState.getSerializable("scooring");
            ac = cliente.ac;
        }

        String deparConsulta = "";

		/*
         * System.out.println("cliente.getDepartamento() " +
		 * cliente.getDepartamento());
		 */

        validarPermisosDecos();
        if (!Utilidades.claveValor("cambiarDeparCotiz", cliente.getDepartamento()).equalsIgnoreCase("")) {
            deparConsulta = Utilidades.claveValor("cambiarDeparCotiz", cliente.getDepartamento());
        } else {
            deparConsulta = cliente.getDepartamento();
        }

        System.out.println("deparConsulta " + deparConsulta);
        System.out.println("ciudad " + cliente.getCiudad());
        cmpBADigital.setDeptoEstrato(deparConsulta, cliente.getEstrato());
        cmpTODigital.setDeptoEstrato(deparConsulta, cliente.getEstrato());
        cmpTVDigital.addObserver(cmpAdicionales);
        cmpTVDigital.addObserver(this);
        cmpTVDigital.addObserver(cmpDecosOferta);
        if (tecnologia.equalsIgnoreCase("IPTV") || tecnologia.equalsIgnoreCase("REDCO")) {
            cmpTVDigital.setIPTV();
        }
        cmpBADigital.addObserver(this);
        cmpTODigital.addObserver(this);
        cmpAdicionales.addObserver(this);
        if (tecnologia.equalsIgnoreCase("IPTV") || tecnologia.equalsIgnoreCase("REDCO")) {
            cmpAdicionales.setIPTV();
        }
        cmpAdicHD.addObserver(this);
        cmpAdicHD.unsetConfigurable();
        if (tecnologia.equalsIgnoreCase("IPTV") || tecnologia.equalsIgnoreCase("REDCO")) {
            cmpAdicHD.setIPTV();
        }

        System.out.println("CompADDigital.OFERTA " + CompADDigital.OFERTA);

        cmpAdicionales.setTipo(CompADDigital.OFERTA);

        if (ac != null && ac.ofertaDigital != null) {
            ArrayList<String> planesBA = planesDigital(ac.ofertaDigital, "BA");

            if (planesBA.size() > 0) {
                cmpBADigital.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesBA);
            } else {
                cmpBADigital.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            }

            ArrayList<String> planesTV = planesDigital(ac.ofertaDigital, "TV");

            if (planesTV.size() > 0) {
                cmpTVDigital.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesTV, cliente);
            } else {
                cmpTVDigital.setDeptoEstrato(deparConsulta, cliente.getCiudad(), cliente.getEstrato());
            }

            ArrayList<String> planesADICTV = planesDigital(ac.ofertaDigital, "ADICTV");

            System.out.println("planesADICTV " + planesADICTV);

            if (planesADICTV.size() > 0 && planesTV.size() > 0) {
                System.out.println("planesTV " + planesTV.size());
                cmpAdicionales.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesADICTV, planesTV.get(0),
                        cliente);
            } else {
                cmpAdicionales.setDeptoEstrato(deparConsulta, cliente.getEstrato(), cliente);
            }

            ArrayList<String> planesADICHD = planesDigital(ac.ofertaDigital, "ADICHD");

            System.out.println("planesADICHD " + planesADICHD);

            if (planesADICHD.size() > 0) {
                cmpAdicHD.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesADICHD);
            } else {
                System.out.println("planes oferta null");

                cmpAdicHD.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), null);
            }

        } else {
            cmpTVDigital.setDeptoEstrato(deparConsulta, cliente.getCiudad(), cliente.getEstrato());
            cmpBADigital.setDeptoEstrato(deparConsulta, cliente.getEstrato());

            cmpAdicionales.setDeptoEstrato(deparConsulta, cliente.getEstrato(), cliente);
            cmpAdicHD.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            nuevo = true;

        }

        cmpTODigital.setDeptoEstrato(deparConsulta, cliente.getEstrato());
        // cmpBADigital.setDeptoEstrato(deparConsulta, cliente.getEstrato());
        // cmpAdicionales.setDeptoEstrato(deparConsulta, cliente.getEstrato());
        cmpTotalPaquete.setTipoTotal(CompTotal.TOTAL_PAQUETE);
        cmpTotalPaqueteAdicionales.setTipoTotal(CompTotal.TOTAL_PAQUETE_ADICIONALES);

        cmpBalTV.addObserver(cmpAdicionalesBal);
        cmpBalTV.addObserver(this);
        cmpBalTV.addObserver(cmpDecosBalin);

        cmpBalTO.addObserver(this);
        cmpBalBA.addObserver(this);
        cmpAdicionalesBal.addObserver(this);
        cmpAdicionalesBal.setTipo(CompADDigital.BALIN);
        cmpAdicHDBal.addObserver(this);

        cmpConfTV.addObserver(cmpAdicionalesConf);
        cmpConfTV.addObserver(this);
        cmpConfTV.addObserver(cmpDecosConfig);
        if (tecnologia.equalsIgnoreCase("IPTV") || tecnologia.equalsIgnoreCase("REDCO")) {
            cmpConfTV.setIPTV();
        }
        cmpConfTV.setCiudad(cliente.getCiudad());
        cmpConfTO.addObserver(this);
        cmpConfBA.addObserver(this);
        cmpAdicionalesConf.addObserver(this);
        if (tecnologia.equalsIgnoreCase("IPTV") || tecnologia.equalsIgnoreCase("REDCO")) {
            cmpAdicionalesConf.setIPTV();
        }
        cmpAdicHDConf.addObserver(this);
        if (tecnologia.equalsIgnoreCase("IPTV") || tecnologia.equalsIgnoreCase("REDCO")) {
            cmpAdicHDConf.setIPTV();
        }
        cmpAdicionalesConf.setTipo(CompADDigital.CONFIGURABLE);

        System.out.println("CompADDigital.CONFIGURABLE " + CompADDigital.CONFIGURABLE);

        if (ac != null) {
            if (ac.productosOfertaRetencion != null) {
                if (!ac.productosDigitalConfig.isEmpty()) {
                    cmpConfTO.setPlanes(ac.productosOfertaDigitalConfig);
                    cmpConfTV.setPlanes(ac.productosOfertaDigitalConfig);
                    cmpConfBA.setPlanes(ac.productosOfertaDigitalConfig);
                }
            }
        }

        if (ac != null && ac.ofertaDigitalConfigurable != null) {
            ArrayList<String> planesBAConf = planesDigital(ac.ofertaDigitalConfigurable, "BA");

            if (planesBAConf.size() > 0) {
                cmpConfBA.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesBAConf);
            } else {
                cmpConfBA.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            }

            ArrayList<String> planesTVConf = planesDigital(ac.ofertaDigitalConfigurable, "TV");

            if (planesTVConf.size() > 0) {
                cmpConfTV.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesTVConf);
            } else {
                cmpConfTV.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            }

            ArrayList<String> planesTOConf = planesDigital(ac.ofertaDigitalConfigurable, "TO");

            if (planesTOConf.size() > 0) {
                cmpConfTO.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesTOConf);
            } else {
                cmpConfTO.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            }

            ArrayList<String> planesADICTVConf = planesDigital(ac.ofertaDigitalConfigurable, "ADICTV");

            System.out.println("planesADICTVConf -> " + planesADICTVConf);
            System.out.println("planesADICTVConf size -> " + planesADICTVConf.size());

            if (planesADICTVConf.size() > 0 && planesTVConf.size() > 0) {
                cmpAdicionalesConf.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesADICTVConf,
                        planesTVConf.get(0), cliente);
            } else {
                cmpAdicionalesConf.setDeptoEstrato(deparConsulta, cliente.getEstrato(), cliente);
            }

            ArrayList<String> planesADICHDConf = planesDigital(ac.ofertaDigitalConfigurable, "ADICHD");

            if (planesADICHDConf.size() > 0) {
                cmpAdicHDConf.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesADICHDConf);
            } else {
                cmpAdicHDConf.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), null);
            }

        } else {
            cmpConfTO.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            cmpConfTV.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            cmpConfBA.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            cmpAdicionalesConf.setDeptoEstrato(deparConsulta, cliente.getEstrato(), cliente);
            cmpAdicHDConf.setDeptoEstrato(deparConsulta, cliente.getEstrato());
        }

        // cmpConfBA.setDeptoEstrato(deparConsulta, cliente.getEstrato());
        // cmpAdicionalesConf.setDeptoEstrato(deparConsulta,
        // cliente.getEstrato());
        cmpTotalPaqueteConf.setTipoTotal(CompTotal.TOTAL_PAQUETE);
        cmpTotalPaqueteAdicionalesConf.setTipoTotal(CompTotal.TOTAL_PAQUETE_ADICIONALES);

        res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec;

        if (ac != null) {

            System.out.println("ac.productosPortafolio " + ac.productosPortafolio);
            if (ac.productosPortafolio != null) {
                cargarPortafolio();

                spec = tabs.newTabSpec("Portafolio");
                spec.setContent(R.id.svPortafolio);
                spec.setIndicator("Portafolio", res.getDrawable(R.drawable.portafolio));
                tabs.addTab(spec);
            }
        }

        spec = tabs.newTabSpec("Oferta");
        spec.setContent(R.id.svOferta);
        spec.setIndicator("Oferta", res.getDrawable(R.drawable.oferta));
        tabs.addTab(spec);

        if (nuevo) {
            System.out.println("cliente.getTecnologia() " + cliente.getTecnologia());
            System.out.println("cliente.getCiudad() " + cliente.getCiudad());
            if (Utilidades.excluirMunicipal("habilitarBalines", cliente.getTecnologia(), cliente.getCiudad())) {
                spec = tabs.newTabSpec("PROMO12");
                spec.setContent(R.id.svOfertaBalines);
                spec.setIndicator("PROMO12", res.getDrawable(R.drawable.oferta));
                tabs.addTab(spec);
            }
        } else {
            if (Utilidades.excluirMunicipal("habilitarBalines", cliente.getTecnologia(), cliente.getCiudad())) {
                if (ac != null && (ac.productosOfertaPromo12 != null && !ac.productosOfertaPromo12.isEmpty())) {
                    spec = tabs.newTabSpec("PROMO12");
                    spec.setContent(R.id.svOfertaBalines);
                    spec.setIndicator("PROMO12", res.getDrawable(R.drawable.oferta));
                    tabs.addTab(spec);
                }
            }
        }

        if (!nuevo) {
            if (ac != null && !ac.ofertaDigitalConfigurable.equals("[]")) {
                spec = tabs.newTabSpec("Oferta Configurable");
                spec.setContent(R.id.svOfertaConfigurable);
                spec.setIndicator("Oferta Configurable", res.getDrawable(R.drawable.oferta));
                tabs.addTab(spec);
            } else {
                svOfertaConfigurable.setVisibility(View.GONE);
            }
        } else {
            spec = tabs.newTabSpec("Oferta Configurable");
            spec.setContent(R.id.svOfertaConfigurable);
            spec.setIndicator("Oferta Configurable", res.getDrawable(R.drawable.oferta));
            tabs.addTab(spec);
        }

        if (nuevo) {
            if (Utilidades.excluirMunicipal("habilitarBalines", cliente.getTecnologia(), cliente.getCiudad())) {
                cmpBalTO.setBalin(true);
                cmpBalTO.setDeptoEstrato(deparConsulta, cliente.getEstrato());

                cmpBalTV.setBalin(true);
                cmpBalTV.setDeptoEstrato(deparConsulta, cliente.getEstrato());

                cmpBalBA.setBalin(true);
                cmpBalBA.setDeptoEstrato(deparConsulta, cliente.getEstrato());

                cmpAdicionalesBal.setDeptoEstrato(deparConsulta, cliente.getEstrato(), cliente);

                cmpAdicHDBal.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            }

        } else {
            if (Utilidades.excluirMunicipal("habilitarBalines", cliente.getTecnologia(), cliente.getCiudad())) {

                ArrayList<String> planesTOPromo12 = planesDigital(ac.ofertaPromo12, "TO");

                if (planesTOPromo12.size() > 0) {
                    cmpBalTO.setBalin(true);
                    cmpBalTO.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesTOPromo12);
                }

                ArrayList<String> planesTVPromo12 = planesDigital(ac.ofertaPromo12, "TV");

                if (planesTVPromo12.size() > 0) {
                    cmpBalTV.setBalin(true);
                    cmpBalTV.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesTVPromo12);
                }

                ArrayList<String> planesBAPromo12 = planesDigital(ac.ofertaPromo12, "BA");

                if (planesBAPromo12.size() > 0) {
                    cmpBalBA.setBalin(true);
                    cmpBalBA.setDeptoEstratoPlan(deparConsulta, cliente.getEstrato(), planesBAPromo12);
                }

                cmpAdicionalesBal.setDeptoEstrato(deparConsulta, cliente.getEstrato(), cliente);

                cmpAdicHDBal.setDeptoEstrato(deparConsulta, cliente.getEstrato());
            }
        }

        Utilidades.DialogoMensaje(this, cliente.getCiudad());

        cmpBalTV.deshabilitarComponente();
        cmpBalTO.deshabilitarComponente();
        cmpDecosBalin.deshabilitarAgregar();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        cargarOfertaDefault();

        cmpTVDigital.mostrarValores();
        cmpTODigital.mostrarValores();

        calcularTotalPaquete();
        calcularTotalPaqueteConfigurable();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putSerializable("cliente", cliente);
        outState.putSerializable("scooring", scooring);
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

    private void cargarPortafolio() {
        int total = 0;

        System.out.println("ac.productosPortafolio " + ac.productosPortafolio);

        if (ac.productosPortafolio != null) {
            for (int i = 0; i < ac.productosPortafolio.size(); i++) {

                System.out.println("ac.productosPortafolio.get(i) " + ac.productosPortafolio.get(i).producto);

                System.out.println("ac.productosPortafolio.get(i) " + ac.productosPortafolio.get(i).plan);

                CompPortafolio comp = new CompPortafolio(this);
                comp.setProducto(ac.productosPortafolio.get(i));
                comp.mostrarDatos();
                total += comp.getTotal();

                llyCargosBasicos.addView(comp);
                if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("BA")) {
                    cmpBADigital.setEstadoProducto("C");
                    cmpBADigital.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpBalBA.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpBalBA.setEstadoProducto("C");
                    cmpBADigital.setExistente(ac.baExistente);
                    cmpConfBA.setEstadoProducto("C");
                    cmpConfBA.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpConfBA.setExistente(ac.baExistente);

                } else if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")
                        || ac.productosPortafolio.get(i).producto.contains("TELEV")) {
                    cmpTVDigital.setEstadoProducto("C");
                    cmpTVDigital.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpBalTV.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpBalTV.setEstadoProducto("C");
                    cmpTVDigital.setExistente(ac.tvExistente);
                    cmpConfTV.setEstadoProducto("C");
                    cmpConfTV.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpConfTV.setExistente(ac.tvExistente);
                    cargarAdicionales(ac.productosPortafolio.get(i).adicionales, "TV");

                    String plan = ac.productosPortafolio.get(i).plan;

                    System.out.println("portafolio planTV " + plan);
                    System.out.println("portafolio tv Analoga " + tvAnaloga);

                    if (!plan.contains("BLACK") && !plan.contains("GOLD") && !plan.contains("SILVER")
                            && !plan.contains("BRONZE") && !plan.contains("ULTRA")) {
                        tvAnaloga = "1";
                    }

                    System.out.println("portafolio tv Analoga " + tvAnaloga);

                } else if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TO")) {
                    cmpTODigital.setEstadoProducto("C");
                    cmpTODigital.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpBalTO.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpBalTO.setEstadoProducto("C");
                    cmpTODigital.setExistente(ac.toExistente);
                    cmpConfTO.setEstadoProducto("C");
                    cmpConfTO.setProducto(ac.productosPortafolio.get(i).clone());
                    cmpConfTO.setExistente(ac.toExistente);
                    cargarAdicionales(ac.productosPortafolio.get(i).adicionales, "TO");
                }
            }
            cargarTotalPaquetePortafolio(total);
            cargarTotalPAqueteAdicionalesPortafolio();
        }
    }

    private void cargarTotalPaquetePortafolio(double total) {
        cmpTotalPaquetePortafolio.setTipoTotal(CompTotal.TOTAL_PAQUETE);
        cmpTotalPaquetePortafolio.setTotal(String.valueOf(total));
    }

    private void cargarTotalPAqueteAdicionalesPortafolio() {
        cmpTotalPaqueteAdicionalesPortafolio.setTipoTotal(CompTotal.TOTAL_PAQUETE_ADICIONALES);
        double total = 0;
        total += Double.parseDouble(cmpTotalPaquetePortafolio.getTotal());
        for (int i = 0; i < llyAdicionales.getChildCount(); i++) {
            CompListaAdicionales cmpad = (CompListaAdicionales) llyAdicionales.getChildAt(i);
            total += Double.parseDouble(cmpad.getTotal());
        }
        cmpTotalPaqueteAdicionalesPortafolio.setTotal(String.valueOf(total));
    }

    private void cargarAdicionales(ArrayList<ArrayList<AdicionalAMG>> adicionales, String tipo) {

        CompListaAdicionales adicionalesLista = new CompListaAdicionales(this);
        adicionalesLista.setTipo(tipo);
        adicionalesLista.setAdicionales(adicionales);

        llyAdicionales.addView(adicionalesLista);

    }

    private void cargarOfertaDefault() {
        String oferta = "";
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasestratos",
                new String[]{"lst_item"}, "lst_nombre=? and lst_estrato=? and lst_ciudad=?",
                new String[]{"planesTVDigitalDefault", cliente.getEstrato(), cliente.getCiudad()}, null, null, null);

        if (respuesta != null) {
            oferta = respuesta.get(0).get(0);
        }

        if (!oferta.equals("")) {
            respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                    new String[]{"lst_clave", "lst_valor"}, "lst_nombre=? and lst_ciudad=?",
                    new String[]{oferta, cliente.getCiudad()}, null, null, null);

            if (respuesta != null) {
                for (int i = 0; i < respuesta.size(); i++) {
                    if (respuesta.get(i).get(0).equals("TV")) {
                        // cmpTVDigital.setPlan(respuesta.get(i).get(1));
                    } else if (respuesta.get(i).get(0).equals("BA")) {
                        cmpBADigital.setPlan(respuesta.get(i).get(1));
                    }
                }
            }
        }
    }

    private void mostrarDescuentos(String oferta) {
        String planTo = "--Seleccione Tipo--", telefonia = "--Seleccione Producto--", planTv = "--Seleccione Tipo--",
                television = "--Seleccione Producto--", planBa = "--Seleccione Tipo--",
                internet = "--Seleccione Producto--";

        cliente.setHogarNuevo("");

        if (oferta.equals("oferta")) {
            if (cmpTODigital.isActive()) {
                planTo = cmpTODigital.getEstadoProducto();
                telefonia = cmpTODigital.getPlan();
            } else {
                planTo = "--Seleccione Tipo--";
                telefonia = "--Seleccione Producto--";
            }

            planTv = cmpTVDigital.getEstadoProducto();
            television = cmpTVDigital.getPlan();

            planBa = cmpBADigital.getEstadoProducto();
            internet = cmpBADigital.getPlan();

        } else if (oferta.equals("configurable")) {

            if (cmpConfTO.isActive()) {
                planTo = cmpConfTO.getEstadoProducto();
                telefonia = cmpConfTO.getPlan();
            } else {
                planTo = "--Seleccione Tipo--";
                telefonia = "--Seleccione Producto--";
            }

            if (cmpConfTV.isActive()) {
                planTv = cmpConfTV.getEstadoProducto();
                television = cmpConfTV.getPlan();
            } else {
                planTv = "--Seleccione Tipo--";
                television = "--Seleccione Producto--";
            }

            if (cmpConfBA.isActive()) {
                planBa = cmpConfBA.getEstadoProducto();
                internet = cmpConfBA.getPlan();
            } else {
                planBa = "--Seleccione Tipo--";
                internet = "--Seleccione Producto--";
            }

        } else if (oferta.equals("balin")) {

            if (cmpBalTO.isActive()) {
                planTo = cmpBalTO.getEstadoProducto();
                telefonia = cmpBalTO.getPlan();
            } else {
                planTo = "--Seleccione Tipo--";
                telefonia = "--Seleccione Producto--";
            }

            if (cmpBalTV.isActive()) {
                planTv = cmpBalTV.getEstadoProducto();
                television = cmpBalTV.getPlan();
            } else {
                planTv = "--Seleccione Tipo--";
                television = "--Seleccione Producto--";
            }

            if (cmpBalBA.isActive()) {
                planBa = cmpBalBA.getEstadoProducto();
                internet = cmpBalBA.getPlan();
            } else {
                planBa = "--Seleccione Tipo--";
                internet = "--Seleccione Producto--";
            }

        }

        System.out.println("planTo " + planTo + " telefonia " + telefonia + " planTv " + planTv + " television "
                + television + " planBa " + planBa + " internet " + internet);

		/*
		 * ArrayList<ArrayList<String>> descuentos = Tarificador.Descuentos(
		 * planTo, telefonia, planTv, television, planBa, internet);
		 */
        Log.e("SmartPromo", cliente.getSmartPromo());
        ArrayList<ArrayList<String>> descuentos = Tarificador.Descuentos2(planTo, telefonia, planTv, television, planBa,
                internet, UtilidadesTarificador.jsonDatos(cliente, cliente.getSmartPromo(), "N/A", tvAnaloga));
        System.out.println("descuentos => " + descuentos);
        if (descuentos != null) {
            for (int i = 0; i < descuentos.size(); i++) {
                System.out.println(descuentos.get(i).get(0));
                if (descuentos.get(i).get(0).equals("to")) {
                    if (oferta.equals("oferta")) {
                        cmpTODigital.setDescuento(descuentos.get(i).get(1));
                        cmpTODigital.setDuracion(descuentos.get(i).get(2));
                        cmpTODigital.mostrarDescuento();
                    } else if (oferta.equals("configurable")) {
                        cmpConfTO.setDescuento(descuentos.get(i).get(1));
                        cmpConfTO.setDuracion(descuentos.get(i).get(2));
                        cmpConfTO.mostrarDescuento();
                    } else if (oferta.equals("balin")) {
                        cmpBalTO.setDescuento(descuentos.get(i).get(1));
                        cmpBalTO.setDuracion(descuentos.get(i).get(2));
                        cmpBalTO.mostrarDescuento();
                    }
                } else if (descuentos.get(i).get(0).equals("tv")) {
                    if (oferta.equals("oferta")) {
                        cmpTVDigital.setDescuento(descuentos.get(i).get(1));
                        cmpTVDigital.setDuracion(descuentos.get(i).get(2));
                        cmpTVDigital.mostrarDescuento();
                    } else if (oferta.equals("configurable")) {
                        cmpConfTV.setDescuento(descuentos.get(i).get(1));
                        cmpConfTV.setDuracion(descuentos.get(i).get(2));
                        cmpConfTV.mostrarDescuento();
                    } else if (oferta.equals("balin")) {
                        cmpBalTV.setDescuento(descuentos.get(i).get(1));
                        cmpBalTV.setDuracion(descuentos.get(i).get(2));
                        cmpBalTV.mostrarDescuento();
                    }
                } else if (descuentos.get(i).get(0).equals("ba")) {
                    if (oferta.equals("oferta")) {
                        cmpBADigital.setDescuento(descuentos.get(i).get(1));
                        cmpBADigital.setDuracion(descuentos.get(i).get(2));
                        cmpBADigital.mostrarDescuento();
                    } else if (oferta.equals("configurable")) {
                        cmpConfBA.setDescuento(descuentos.get(i).get(1));
                        cmpConfBA.setDuracion(descuentos.get(i).get(2));
                        cmpConfBA.mostrarDescuento();
                    } else if (oferta.equals("balin")) {
                        cmpBalBA.setDescuento(descuentos.get(i).get(1));
                        cmpBalBA.setDuracion(descuentos.get(i).get(2));
                        cmpBalBA.mostrarDescuento();
                    }
                }
            }
        } else {
            limpiarDesciuentosComponentes();
        }

    }

    private void limpiarDesciuentosComponentes() {

        cmpTODigital.setDescuento("0");
        cmpTODigital.setDuracion("0");
        cmpTODigital.mostrarDescuento();

        cmpTVDigital.setDescuento("0");
        cmpTVDigital.setDuracion("0");
        cmpTVDigital.mostrarDescuento();

        cmpBADigital.setDescuento("0");
        cmpBADigital.setDuracion("0");
        cmpBADigital.mostrarDescuento();

        cmpConfTO.setDescuento("0");
        cmpConfTO.setDuracion("0");
        cmpConfTO.mostrarDescuento();

        cmpConfTV.setDescuento("0");
        cmpConfTV.setDuracion("0");
        cmpConfTV.mostrarDescuento();

        cmpConfBA.setDescuento("0");
        cmpConfBA.setDuracion("0");
        cmpConfBA.mostrarDescuento();

        cmpBalTO.setDescuento("0");
        cmpBalTO.setDuracion("0");
        cmpBalTO.mostrarDescuento();

        cmpBalTV.setDescuento("0");
        cmpBalTV.setDuracion("0");
        cmpBalTV.mostrarDescuento();

        cmpBalBA.setDescuento("0");
        cmpBalBA.setDuracion("0");
        cmpBalBA.mostrarDescuento();
    }

    public void procesar(View v) {

		/*
		 * if (cmpTODigital.isActive()) { planTo = "N"; telefonia =
		 * cmpTODigital.getPlan(); descTo = cmpTODigital.getDescuento();
		 * durDescTo = cmpTODigital.getDuracion(); }
		 */

        if (cmpTODigital.isActive()) {
            planTo = "N";
            toTeccr = teccr;
            if (cmpTODigital.getPlan().equalsIgnoreCase("Telefonia Existente")) {
                planTo = "E";
                toTeccr = cmpTODigital.getProducto().tecnologiacr;
            } else {
                if (ac != null) {
                    if (ac.productosPortafolio != null) {
                        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
                            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TO")) {
                                if (ac.productosPortafolio.get(i).planFacturacion.equals(cmpTODigital.getProducto().planFacturacion)) {
                                    planTo = "E";
                                } else {
                                    planTo = "C";
                                }
                                idTelefonia = ac.productosPortafolio.get(i).identificador;
                                paTelefonia = ac.productosPortafolio.get(i).plan;
                                toTeccr = cmpTODigital.getProducto().tecnologiacr;
                                System.out.println("Tecnologia CR " + toTeccr);
                                System.out.println("Tecnologia Porta " + cmpTODigital.getProducto().tecnologia);

                            }
                        }
                    }
                }

            }

            telefonia = cmpTODigital.getPlan();
            descTo = cmpTODigital.getDescuento();
            durDescTo = cmpTODigital.getDuracion();
        }

        // planTv = "N";
        // television = cmpTVDigital.getPlan();
        // descTv = cmpTVDigital.getDescuento();
        // durDescTv = cmpTVDigital.getDuracion();

        planTv = "N";
        tvTeccr = teccr;
        if (cmpTVDigital.getPlan().equalsIgnoreCase("HFC Existente")) {
            planTv = "E";
            tvTeccr = cmpTVDigital.getProducto().tecnologiacr;
        } else {
            if (ac != null) {
                if (ac.productosPortafolio != null) {
                    for (int i = 0; i < ac.productosPortafolio.size(); i++) {
                        if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")
                                || ac.productosPortafolio.get(i).producto.contains("TELEV")) {
                            if (ac.productosPortafolio.get(i).planFacturacion.equals(cmpTVDigital.getProducto().planFacturacion)) {
                                planTv = "E";
                            } else {
                                planTv = "C";
                            }
                            idTelevision = ac.productosPortafolio.get(i).identificador;
                            paTelevision = ac.productosPortafolio.get(i).plan;
                            tvTeccr = cmpTVDigital.getProducto().tecnologiacr;
                        }
                    }
                }
            }

        }

        television = cmpTVDigital.getPlan();
        descTv = cmpTVDigital.getDescuento();
        durDescTv = cmpTVDigital.getDuracion();

        // planBa = "N";
        // internet = cmpBADigital.getPlan();
        // descBa = cmpBADigital.getDescuento();
        // durDescBa = cmpBADigital.getDuracion();

        planBa = "N";
        baTeccr = teccr;
        if (cmpBADigital.getPlan().equalsIgnoreCase("Internet Existente")) {
            planBa = "E";
            baTeccr = cmpBADigital.getProducto().tecnologiacr;
        } else {
            if (ac != null) {
                if (ac.productosPortafolio != null) {
                    for (int i = 0; i < ac.productosPortafolio.size(); i++) {
                        if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("BA")) {
                            System.out.println("ac.productosPortafolio.get(i).planFacturacion => " + ac.productosPortafolio.get(i).planFacturacion);
                            System.out.println("cmpBalBA.getProducto().planFacturacion => " + cmpBADigital.getProducto().planFacturacion);
                            if (ac.productosPortafolio.get(i).planFacturacion.equals(cmpBADigital.getProducto().planFacturacion)) {
                                planBa = "E";
                            } else {
                                planBa = "C";
                            }
                            idInternet = ac.productosPortafolio.get(i).identificador;
                            paInternet = ac.productosPortafolio.get(i).plan;
                            baTeccr = cmpBADigital.getProducto().tecnologiacr;
                            System.out.println("ac.ipDinamica " + ac.ipDinamica);
                            if (ac.ipDinamica != null && ac.ipDinamica.size() > 0) {
                                for (int j = 0; j < ac.ipDinamica.size(); j++) {
                                    if (ac.ipDinamica.get(j).getIdentificador().equalsIgnoreCase(idInternet)) {
                                        ipDinamica = ac.ipDinamica.get(j).getIpDinamica();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        internet = cmpBADigital.getPlan();
        descBa = cmpBADigital.getDescuento();
        durDescBa = cmpBADigital.getDuracion();

        System.out.println(cmpAdicionales.getAdicionales().length);

        if (cmpAdicHD.isActive()) {
            int numAdicionales = cmpAdicionales.getAdicionales().length;
            String[][] adicionales = new String[numAdicionales + 1][2];
            for (int i = 0; i < cmpAdicionales.getAdicionales().length; i++) {
                adicionales[i][0] = cmpAdicionales.getAdicionales()[i][0];
                adicionales[i][1] = cmpAdicionales.getAdicionales()[i][1];
            }
            adicionales[numAdicionales][0] = cmpAdicHD.getPlan();
            adicionales[numAdicionales][1] = cmpAdicHD.getTotal();

            double totalAdicionales = cmpAdicionales.getTotal() + Double.parseDouble(cmpAdicHD.getTotal());

            procesarCotizacion(descTo, descTv, descBa, planTo, telefonia, idTelefonia, paTelefonia, toTeccr, planTv,
                    television, idTelevision, paTelevision, tvTeccr, planBa, internet, idInternet, paInternet, baTeccr,
                    adicionales, totalAdicionales, cmpDecosOferta.getDecos(), cmpDecosOferta.obtenerTotalDecos(),
                    ipDinamica);
        } else {
            procesarCotizacion(descTo, descTv, descBa, planTo, telefonia, idTelefonia, paTelefonia, toTeccr, planTv,
                    television, idTelevision, paTelevision, tvTeccr, planBa, internet, idInternet, paInternet, baTeccr,
                    cmpAdicionales.getAdicionales(), cmpAdicionales.getTotal(), cmpDecosOferta.getDecos(),
                    cmpDecosOferta.obtenerTotalDecos(), ipDinamica);
        }

    }

    public void procesarConf(View v) {

        // if (cotizacion == null) {
        cotizacion = new Cotizacion();
        // }

        cotizacion.medioIngreso = "Venta";
        cotizacion.setEstrato(cliente.getEstrato());

        cotizacion.setAdicionales(cmpAdicionalesConf.getAdicionales());
        cotizacion.setTotalAdicionales(String.valueOf(cmpAdicionalesConf.getTotal()));
        cotizacion.setItemPromocionesAdicionales(cmpAdicionalesConf.getPromociones());

        ArrayList<ArrayList<String>> oferta = new ArrayList<ArrayList<String>>();

		System.out.println("TO Ativa? " + cmpConfTO.isActive());
		if (cmpConfTO.isActive()) {
			planTo = "N";
			toTeccr = teccr;
			if (cmpConfTO.getPlan().equalsIgnoreCase("Telefonia Existente")) {
				planTo = "E";
				toTeccr = cmpConfTO.getProducto().tecnologiacr;
				if (ac != null) {
					if (ac.productosPortafolio != null) {
						for (int i = 0; i < ac.productosPortafolio.size(); i++) {
							if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TO")) {
								idTelefonia = ac.productosPortafolio.get(i).identificador;
								paTelefonia = Utilidades.nombrePlan(cliente.getDepartamento(),ac.productosPortafolio.get(i).planFacturacion,cliente.getEstrato());
                                if(paTelefonia.equalsIgnoreCase("")){
                                    paTelefonia = ac.productosPortafolio.get(i).plan;
                                }
                                Log.d("planAnterior","TO "+paTelefonia);
                                pfAnterior = ac.productosPortafolio.get(i).planFacturacion;
							}
						}
					}
				}
			} else {
				if (ac != null) {
					if (ac.productosPortafolio != null) {
						for (int i = 0; i < ac.productosPortafolio.size(); i++) {
							if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TO")) {
								if(ac.productosPortafolio.get(i).planFacturacion.equals(cmpConfTO.getProducto().planFacturacion)){
									planTo = "E";
								}else{
									planTo = "C";
								}
								idTelefonia = ac.productosPortafolio.get(i).identificador;
								paTelefonia = ac.productosPortafolio.get(i).plan;
                                pfAnterior = ac.productosPortafolio.get(i).planFacturacion;
								toTeccr = cmpConfTO.getProducto().tecnologiacr;
							}
						}
					}
				}

            }

            telefonia = cmpConfTO.getPlan();
            descTo = cmpConfTO.getDescuento();
            durDescTo = cmpConfTO.getDuracion();
        }

		System.out.println("TV Ativa? " + cmpConfTO.isActive());
		if (cmpConfTV.isActive()) {
			planTv = "N";
			tvTeccr = teccr;
			if (cmpConfTV.getPlan().equalsIgnoreCase("HFC Existente")) {
				planTv = "E";
				tvTeccr = cmpConfTV.getProducto().tecnologiacr;
				if (ac != null) {
					if (ac.productosPortafolio != null) {
						for (int i = 0; i < ac.productosPortafolio.size(); i++) {
							if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")
									|| ac.productosPortafolio.get(i).producto.contains("TELEV")) {
								idTelevision = ac.productosPortafolio.get(i).identificador;
								paTelevision = Utilidades.nombrePlan(cliente.getDepartamento(),ac.productosPortafolio.get(i).planFacturacion,cliente.getEstrato());
                                if(paTelevision.equalsIgnoreCase("")){
                                    paTelevision = ac.productosPortafolio.get(i).plan;
                                }
                                pfAnterior = ac.productosPortafolio.get(i).planFacturacion;
							}
						}
					}
				}
			} else {
				if (ac != null) {
					if (ac.productosPortafolio != null) {
						for (int i = 0; i < ac.productosPortafolio.size(); i++) {
							if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")
									|| ac.productosPortafolio.get(i).producto.contains("TELEV")) {
								if(ac.productosPortafolio.get(i).planFacturacion.equals(cmpConfTV.getProducto().planFacturacion)){
									planTv = "E";
								}else{
									planTv = "C";
								}
								idTelevision = ac.productosPortafolio.get(i).identificador;
								paTelevision = ac.productosPortafolio.get(i).plan;
                                pfAnterior = ac.productosPortafolio.get(i).planFacturacion;
								tvTeccr = cmpConfTV.getProducto().tecnologiacr;
							}
						}
					}
				}

            }

            television = cmpConfTV.getPlan();
            descTv = cmpConfTV.getDescuento();
            durDescTv = cmpConfTV.getDuracion();
        }

		System.out.println("BA Ativa? " + cmpConfTO.isActive());
		if (cmpConfBA.isActive()) {
			planBa = "N";
			baTeccr = teccr;
			if (cmpConfBA.getPlan().equalsIgnoreCase("Internet Existente")) {
				planBa = "E";
				baTeccr = cmpConfBA.getProducto().tecnologiacr;
				if (ac != null) {
					if (ac.productosPortafolio != null) {
						for (int i = 0; i < ac.productosPortafolio.size(); i++) {
							if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("BA")) {
								idInternet = ac.productosPortafolio.get(i).identificador;
								paInternet = Utilidades.nombrePlan(cliente.getDepartamento(),ac.productosPortafolio.get(i).planFacturacion,cliente.getEstrato());
                                if(paInternet.equalsIgnoreCase("")){
                                    paInternet = ac.productosPortafolio.get(i).plan;
                                }
                                pfAnterior = ac.productosPortafolio.get(i).planFacturacion;
                                Log.d("pfAnterior",pfAnterior);
							}
						}
					}
				}
			} else {
				if (ac != null) {
					if (ac.productosPortafolio != null) {
						for (int i = 0; i < ac.productosPortafolio.size(); i++) {
							if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("BA")) {
								if(ac.productosPortafolio.get(i).planFacturacion.equals(cmpConfBA.getProducto().planFacturacion)){
									planBa = "E";
								}else{
									planBa = "C";
								}
								idInternet = ac.productosPortafolio.get(i).identificador;
								paInternet = ac.productosPortafolio.get(i).plan;
                                pfAnterior = ac.productosPortafolio.get(i).planFacturacion;
								baTeccr = cmpConfBA.getProducto().tecnologiacr;
								if (ac.ipDinamica != null && ac.ipDinamica.size() > 0) {
									for (int j = 0; j < ac.ipDinamica.size(); j++) {
										System.out.println("idInternet " + idInternet);
										System.out.println("ac " + ac.ipDinamica.get(j).getIdentificador());
										System.out.println("ac " + ac.ipDinamica.get(j).getIdentificador());
										if (ac.ipDinamica.get(j).getIdentificador().equalsIgnoreCase(idInternet)) {
											ipDinamica = ac.ipDinamica.get(j).getIpDinamica();
										}
									}
								}
							}
						}
					}
				}

            }

            internet = cmpConfBA.getPlan();
            descBa = cmpConfBA.getDescuento();
            durDescBa = cmpConfBA.getDuracion();
        }

        Log.d("planAnterior",paTelefonia);
        Log.d("planAnterior",paTelevision);
        Log.d("planAnterior",paInternet);

        if (cmpAdicHD.isActive()) {
            int numAdicionales = cmpAdicionalesConf.getAdicionales().length;
            String[][] adicionales = new String[numAdicionales + 1][2];
            for (int i = 0; i < cmpAdicionalesConf.getAdicionales().length; i++) {
                adicionales[i][0] = cmpAdicionalesConf.getAdicionales()[i][0];
                adicionales[i][1] = cmpAdicionalesConf.getAdicionales()[i][1];
            }
            adicionales[numAdicionales][0] = cmpAdicHDConf.getPlan();
            adicionales[numAdicionales][1] = cmpAdicHDConf.getTotal();

            double totalAdicionales = cmpAdicionalesConf.getTotal() + Integer.parseInt(cmpAdicHDConf.getTotal());

            procesarCotizacion(descTo, descTv, descBa, planTo, telefonia, idTelefonia, paTelefonia, toTeccr, planTv,
                    television, idTelevision, paTelevision, tvTeccr, planBa, internet, idInternet, paInternet, baTeccr,
                    adicionales, totalAdicionales, cmpDecosConfig.getDecos(), cmpDecosConfig.obtenerTotalDecos(),
                    ipDinamica);
        } else {
            procesarCotizacion(descTo, descTv, descBa, planTo, telefonia, idTelefonia, paTelefonia, toTeccr, planTv,
                    television, idTelevision, paTelevision, tvTeccr, planBa, internet, idInternet, paInternet, baTeccr,
                    cmpAdicionalesConf.getAdicionales(), cmpAdicionalesConf.getTotal(), cmpDecosConfig.getDecos(),
                    cmpDecosConfig.obtenerTotalDecos(), ipDinamica);

        }

    }

    public void procesarBal(View v) {

        // if (cotizacion == null) {
        cotizacion = new Cotizacion();
        // }

        cotizacion.medioIngreso = "Venta";
        cotizacion.setEstrato(cliente.getEstrato());

        cotizacion.setAdicionales(cmpAdicionalesBal.getAdicionales());
        cotizacion.setTotalAdicionales(String.valueOf(cmpAdicionalesBal.getTotal()));
        cotizacion.setItemPromocionesAdicionales(cmpAdicionalesBal.getPromociones());

        ArrayList<ArrayList<String>> oferta = new ArrayList<ArrayList<String>>();

        System.out.println("TO Ativa? " + cmpBalTO.isActive());
        if (cmpBalTO.isActive()) {
            planTo = "N";
            toTeccr = teccr;
            if (cmpBalTO.getPlan().equalsIgnoreCase("Telefonia Existente")) {
                planTo = "E";
                toTeccr = cmpBalTO.getProducto().tecnologiacr;
            } else {
                if (ac != null) {
                    if (ac.productosPortafolio != null) {
                        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
                            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TO")) {
                                if (ac.productosPortafolio.get(i).planFacturacion.equals(cmpBalTO.getProducto().planFacturacion)) {
                                    planTo = "E";
                                } else {
                                    planTo = "C";
                                }
                                idTelefonia = ac.productosPortafolio.get(i).identificador;
                                paTelefonia = ac.productosPortafolio.get(i).plan;
                                toTeccr = cmpBalTO.getProducto().tecnologiacr;
                            }
                        }
                    }
                }

            }

            telefonia = cmpBalTO.getPlan();
            descTo = cmpBalTO.getDescuento();
            durDescTo = cmpBalTO.getDuracion();
        }

        System.out.println("TV Ativa? " + cmpBalTV.isActive());
        if (cmpBalTV.isActive()) {
            planTv = "N";
            tvTeccr = teccr;
            if (cmpBalTV.getPlan().equalsIgnoreCase("HFC Existente")) {
                planTv = "E";
                tvTeccr = cmpBalTV.getProducto().tecnologiacr;
            } else {
                if (ac != null) {
                    if (ac.productosPortafolio != null) {
                        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
                            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")
                                    || ac.productosPortafolio.get(i).producto.contains("TELEV")) {
                                if (ac.productosPortafolio.get(i).planFacturacion.equals(cmpBalTV.getProducto().planFacturacion)) {
                                    planTv = "E";
                                } else {
                                    planTv = "C";
                                }
                                idTelevision = ac.productosPortafolio.get(i).identificador;
                                paTelevision = ac.productosPortafolio.get(i).plan;
                                tvTeccr = cmpBalTV.getProducto().tecnologiacr;
                            }
                        }
                    }
                }

            }

            television = cmpBalTV.getPlan();
            descTv = cmpBalTV.getDescuento();
            durDescTv = cmpBalTV.getDuracion();
        }

        System.out.println("BA Ativa? " + cmpBalBA.isActive());
        if (cmpBalBA.isActive()) {
            planBa = "N";
            baTeccr = teccr;
            if (cmpBalBA.getPlan().equalsIgnoreCase("Internet Existente")) {
                planBa = "E";
                baTeccr = cmpBalBA.getProducto().tecnologiacr;
            } else {
                if (ac != null) {
                    if (ac.productosPortafolio != null) {
                        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
                            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("BA")) {
                                if (ac.productosPortafolio.get(i).planFacturacion.equals(cmpBalBA.getProducto().planFacturacion)) {
                                    planBa = "E";
                                } else {
                                    planBa = "C";
                                }
                                idInternet = ac.productosPortafolio.get(i).identificador;
                                paInternet = ac.productosPortafolio.get(i).plan;
                                baTeccr = cmpBalBA.getProducto().tecnologiacr;
                                if (ac.ipDinamica != null && ac.ipDinamica.size() > 0) {
                                    for (int j = 0; j < ac.ipDinamica.size(); j++) {
                                        if (ac.ipDinamica.get(i).getIdentificador().equalsIgnoreCase(idInternet)) {
                                            ipDinamica = ac.ipDinamica.get(i).getIpDinamica();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            internet = cmpBalBA.getPlan();
            descBa = cmpBalBA.getDescuento();
            durDescBa = cmpBalBA.getDuracion();
        }

        if (cmpAdicHDBal.isActive()) {
            int numAdicionales = cmpAdicionalesBal.getAdicionales().length;
            String[][] adicionales = new String[numAdicionales + 1][2];
            for (int i = 0; i < cmpAdicionalesBal.getAdicionales().length; i++) {
                adicionales[i][0] = cmpAdicionalesBal.getAdicionales()[i][0];
                adicionales[i][1] = cmpAdicionalesBal.getAdicionales()[i][1];
            }
            adicionales[numAdicionales][0] = cmpAdicHDBal.getPlan();
            adicionales[numAdicionales][1] = cmpAdicHDBal.getTotal();

            double totalAdicionales = cmpAdicionalesBal.getTotal() + Integer.parseInt(cmpAdicHDBal.getTotal());

            procesarCotizacion(descTo, descTv, descBa, planTo, telefonia, idTelefonia, paTelefonia, toTeccr, planTv,
                    television, idTelevision, paTelevision, tvTeccr, planBa, internet, idInternet, paInternet, baTeccr,
                    adicionales, totalAdicionales, cmpDecosBalin.getDecos(), cmpDecosBalin.obtenerTotalDecos(),
                    ipDinamica);
        } else {
            procesarCotizacion(descTo, descTv, descBa, planTo, telefonia, idTelefonia, paTelefonia, toTeccr, planTv,
                    television, idTelevision, paTelevision, tvTeccr, planBa, internet, idInternet, paInternet, baTeccr,
                    cmpAdicionalesBal.getAdicionales(), cmpAdicionalesBal.getTotal(), cmpDecosBalin.getDecos(),
                    cmpDecosBalin.obtenerTotalDecos(), ipDinamica);
        }

    }

    private void procesarCotizacion(String descTo, String descTv, String descBa, String planTo, String telefonia,
                                    String idTelefonia, String paTelefonia, String toTecnologiacr, String planTv, String television,
                                    String idTelevision, String paTelevision, String tvTecnologiacr, String planBa, String internet,
                                    String idInternet, String paInternet, String baTecnologiacr, String[][] adicionales,
                                    double totalAdicionales, ArrayList<ItemDecodificador> decos, double totalDecos, String ipDinamica) {

        boolean trioNuevo = false;
        int numeroProductos = 0;
        System.out.println("procesarCotizacion->Tecnologia TO " + toTecnologiacr);
        System.out.println("procesarCotizacion->Tecnologia TV " + tvTecnologiacr);
        System.out.println("procesarCotizacion->Tecnologia BA " + baTecnologiacr);

        // if (cotizacion == null) {
        cotizacion = new Cotizacion();
        // }

        Tarificador tarificador = new Tarificador();
        Log.e("SmartPromo", cliente.getSmartPromo());
        tarificador.Consulta_Tarifaz(telefonia, television, internet, "--Seleccione Producto--",
                "--Seleccione Producto--", descTo, descTv, descBa, "--Seleccione Descuento--",
                "--Seleccione Descuento--", adicionales, totalAdicionales, cliente.getEstrato(), true,
                cliente.getCiudad(),
                UtilidadesTarificador.jsonDatos(cliente, cliente.getSmartPromo(), "N/A", tvAnaloga), this, totalDecos);

        ArrayList<ItemTarificador> cotizacion_venta = tarificador.Array_Cotizacion_Venta();

        ArrayList<String> telefoniaArr = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_to_d);
        ArrayList<String> televisionArr = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_tv_d);
        ArrayList<String> adicionalesArr = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_ad);
        ArrayList<String> internetArr = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_ba_d);
        ArrayList<String> internet_3dArr = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_3g_d);
        ArrayList<String> internet_4dArr = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_4g_d);
        ArrayList<String> otrosArr = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_otros);
        ArrayList<String> gota = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_producto_gota);

        ArrayList<ArrayList<String>> promociones = tarificador.Descuentos2(planTo, telefonia, planTv, television,
                planBa, internet, UtilidadesTarificador.jsonDatos(cliente, cliente.getSmartPromo(), "N/A", tvAnaloga));

        ArrayList<String> planesFacturacion = Utilidades.getCotizacion(cotizacion_venta, Utilidades.tipo_planes);

        if (otrosArr.get(2).equalsIgnoreCase("3")) {

            if (planTv.equalsIgnoreCase("N") && planTo.equalsIgnoreCase("N") && planBa.equalsIgnoreCase("N")) {
                trioNuevo = true;
            }
        }

        if (televisionArr != null && televisionArr.size() > 0) {

            System.out.println("television " + television);
            ArrayList<String> descuentoTv = UtilidadesTarificador.aplicarDescuentos(promociones,
                    Utilidades.tipo_producto_tv);
            cotizacion.setAdicionales(UtilidadesTarificador.agregarAdicionalesGratis(planesFacturacion.get(1),
                    planesFacturacion.get(4), adicionales, televisionArr.get(0), planTv, otrosArr.get(2), trioNuevo,
                    descuentoTv.get(0).toString(), descuentoTv.get(1).toString(), cliente.getDepartamento(),
                    cliente.getEstrato()));

        }

        cotizacion.setTotalAdicionales(String.valueOf(totalAdicionales));
        cotizacion.setDecodificadores(decos);
        cotizacion.setTotalDecos(totalDecos);

        ArrayList<String> ofertas = Utilidades.getCotizacion(cotizacion_venta, "Oferta");

        cotizacion.setOferta(ofertas.get(0));

        ArrayList<String> controlCotizacion = Utilidades.getCotizacion(cotizacion_venta, "Control");

        if (telefoniaArr != null && telefoniaArr.size() > 0) {
            if (!telefoniaArr.get(0).equals("-")) {
                numeroProductos++;
            }

            ArrayList<String> descuentoTo = UtilidadesTarificador.aplicarDescuentos(promociones,
                    Utilidades.tipo_producto_to);
            // System.out.println("descuentoTo " + descuentoTo);

            cotizacion.Telefonia((String) planTo, telefoniaArr.get(0), telefoniaArr.get(1), telefoniaArr.get(2), "0",
                    "0", descuentoTo.get(0).toString(), descuentoTo.get(1).toString(), idTelefonia, paTelefonia,
                    toTecnologiacr);

            if(!telefoniaArr.get(0).equalsIgnoreCase("Telefonia Existente")){
                cotizacion.setPlanFacturacionTo_I(planesFacturacion.get(0));
                cotizacion.setPlanFacturacionTo_P(planesFacturacion.get(3));
            } else {
                cotizacion.setPlanFacturacionTo_I(pfAnterior);
                cotizacion.setPlanFacturacionTo_P(pfAnterior);
            }


            String tipoCotizacion = Utilidades.planNumerico((String) planTo);
            cotizacion.setTipoCotizacionTo(tipoCotizacion);

            if (tipoCotizacion.equals("1")) {
                cotizacion.setSegundaTelefonia(UtilidadesTarificador.validarSegundaTelefonia(cliente));
            }

        }

        boolean aplicarAd = false;

        if (televisionArr != null && televisionArr.size() > 0) {
            if (!televisionArr.get(0).equals("-")) {
                numeroProductos++;
            }
            ArrayList<String> descuentoTv = UtilidadesTarificador.aplicarDescuentos(promociones,
                    Utilidades.tipo_producto_tv);
            cotizacion.Television((String) planTv, televisionArr.get(0), televisionArr.get(1), televisionArr.get(2),
                    "0", "0", descuentoTv.get(0).toString(), descuentoTv.get(1).toString(), idTelevision, paTelevision,
                    tvTecnologiacr);

            System.out.println();
            ArrayList<ArrayList<String>> result = tarificador.consultarDescuentos(adicionales, cliente.getCiudad(),
                    true, adicionales.length);

            ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();

            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    itemPromocionesAdicionales.add(new ItemPromocionesAdicionales(result.get(i).get(0),
                            result.get(i).get(1), "TV", result.get(i).get(2)));
                }
            }

            cotizacion.setItemPromocionesAdicionales(itemPromocionesAdicionales);

            for (int i = 0; i < itemPromocionesAdicionales.size(); i++) {

                if (!itemPromocionesAdicionales.get(i).getMeses().equalsIgnoreCase("0 Meses")) {
                    aplicarAd = true;
                }

            }

            cotizacion.setAplicarAd(aplicarAd);

            if(!televisionArr.get(0).equalsIgnoreCase("HFC Existente") && !televisionArr.get(0).equalsIgnoreCase("IPTV Existente")){
                cotizacion.setPlanFacturacionTv_I(planesFacturacion.get(1));
                cotizacion.setPlanFacturacionTv_P(planesFacturacion.get(4));
            } else {
                cotizacion.setPlanFacturacionTv_I(pfAnterior);
                cotizacion.setPlanFacturacionTv_P(pfAnterior);
            }

            cotizacion.setTipoCotizacionTv(Utilidades.planNumerico(planTv));
        }

        if (internetArr != null && internetArr.size() > 0) {
            if (!internetArr.get(0).equals("-")) {
                numeroProductos++;
            }
            ArrayList<String> descuentoBa = UtilidadesTarificador.aplicarDescuentos(promociones,
                    Utilidades.tipo_producto_ba);
            cotizacion.Internet((String) planBa, internetArr.get(0), internetArr.get(1), internetArr.get(2), "0", "0",
                    descuentoBa.get(0).toString(), descuentoBa.get(1).toString(), idInternet, paInternet,
                    baTecnologiacr);

            if(!internetArr.get(0).equalsIgnoreCase("Internet Existente")){
                cotizacion.setPlanFacturacionBa_I(planesFacturacion.get(2));
                cotizacion.setPlanFacturacionBa_P(planesFacturacion.get(5));
            } else {
                cotizacion.setPlanFacturacionBa_I(pfAnterior);
                cotizacion.setPlanFacturacionBa_P(pfAnterior);
            }


            System.out.println("(String)sltPlanBa.getSelectedItem() " + (String) planBa);
            cotizacion.setTipoCotizacionBa(Utilidades.planNumerico((String) planBa));
            System.out.println("TipoCotizacionBa " + cotizacion.getTipoCotizacionBa());
            boolean controlGota = Boolean.parseBoolean(gota.get(0).toString());

            System.out.println("controlGota " + controlGota);

            System.out.println("gota.get(0).toString() " + gota.get(0).toString());

            if (controlGota) {

                String nombreGota = "Gota de " + gota.get(3).toString() + " a " + gota.get(4).toString();

                System.out.println("gota.get(1) " + gota.get(1));
                System.out.println("gota.get(2) " + gota.get(2));

                cotizacion.setGota(controlGota, (int) Math.round(Double.parseDouble(gota.get(1).toString())),
                        (int) Math.round(Double.parseDouble(gota.get(2).toString())), gota.get(3).toString(), gota.get(4).toString(),
                        nombreGota);

            } else {
                cotizacion.setControlGota(false);
            }

            cotizacion.setIpdinamica(ipDinamica);
        }

        if (internet_3dArr != null && internet_3dArr.size() > 0) {
            cotizacion.Internet_3G(internet_3dArr.get(0), internet_3dArr.get(1), internet_3dArr.get(2),
                    internet_3dArr.get(3), internet_3dArr.get(4), internet_3dArr.get(5));
        }

        if (internet_4dArr != null && internet_4dArr.size() > 0) {
            cotizacion.Internet_4G(internet_4dArr.get(0), internet_4dArr.get(1), internet_4dArr.get(2),
                    internet_4dArr.get(3), internet_4dArr.get(4), internet_4dArr.get(5));
        }

        if (otrosArr != null && otrosArr.size() > 0) {
            cotizacion.Otros(otrosArr.get(0), otrosArr.get(1), otrosArr.get(2), otrosArr.get(3));
        }

        if (trioNuevo) {
            cotizacion.setMedioIngreso("Venta");
        } else {
            cotizacion.setMedioIngreso("Cross");
        }

        if (cliente.getSmartPromo().equalsIgnoreCase("1")) {
            if (productosNuevos()) {

                // cliente.setSmartPromo("1");

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
                }
            }
        }

        if (aplicaDescuentoTo()) {

            cotizacion.setPromoTo("100%");
            cotizacion.setTiempoPromoTo("12 Meses");

        } else {
            cotizacion.setPromoTo_12_100(false);
        }

        System.out.println("numeroProductos " + numeroProductos);
        if (numeroProductos > 1) {
            cotizacion.setTotalEmpDescuento(totalDescuentosAdicionales);
        } else {
            cotizacion.setTotalIndDescuento(totalDescuentosAdicionales);
        }
        System.out.println("cotizacion.setTotalIndDescuento " + cotizacion.getTotalIndDescuento());

        if (controlCotizacion.get(0).equalsIgnoreCase("00")) {
            if (UtilidadesTarificador.validarDependencias(cotizacion.getTelevision(), cotizacion.getAdicionales(), this)
                    && UtilidadesTarificador.validarDecosMinimos(cotizacion.getTelevision(),
                    cotizacion.getDecodificadores(), this)) {
                if (cliente.isControlEstadoCuenta()) {
                    if (validarCarrusel()) {
                        if (validarDigital()) {
                            if (UtilidadesTarificador.validarTelefonoServicio(cliente.getTelefono())) {
                                if (Utilidades.validarTelefonos(cliente, this)) {
                                    if (validarEstandarizacion || cliente.isControlCerca() || Utilidades.CoberturaRural(cliente)) {

                                        System.out.println("direccion " + cliente.getDireccion());
                                        System.out.println("tipoDocumento " + cliente.getTipoDocumento());
                                        System.out.println("Documento " + cliente.getCedula());

                                        if (!cliente.getDireccion().equalsIgnoreCase("")
                                                && !cliente.getCedula().equalsIgnoreCase("")
                                                && !cliente.getTipoDocumento()
                                                .equalsIgnoreCase(Utilidades.inicial_opcion)) {
                                            if (!cliente.getTelefonoDestino().equalsIgnoreCase("")
                                                    && cliente.getTelefonoDestino().length() >= 7) {

                                                if ((cliente.getTipoPropiedad() != null
                                                        && !cliente.getTipoPropiedad()
                                                        .equalsIgnoreCase(Utilidades.inicial_opcion)
                                                        && !cliente.getTipoPropiedad().equalsIgnoreCase("N/A"))
                                                        || Utilidades.excluir("excluirAgenda", cliente.getCiudad())) {
                                                    System.out.println(
                                                            "scooring.idDocumento() " + scooring.getIdCliente());
                                                    System.out.println("cliente.getCedula() " + cliente.getCedula());
                                                    if (cliente.isRealizoConfronta()) {
                                                        if (cliente.isConfronta()) {
                                                            if (validarCarteraUNE()) {
                                                                if (validarScooring()) {
                                                                    if (Utilidades.validarCliente(cliente.getCiudad(),
                                                                            scooring.getAccion(), scooring.getEstadoValidador(),
                                                                            this)) {
                                                                        if (UtilidadesTarificador.validarBronze(cotizacion,
                                                                                cliente.getCiudad())) {
                                                                            if (UtilidadesTarificador.validarAdicionalesAMC(
                                                                                    tarificador.itemPromocionesAdicionales)) {
                                                                                if (Utilidades.tratarCoberturaBloqueo(cliente.getCobertura(), this).isCoberturaHFC()) {
                                                                                    cotizacion.setHFCDigital(true);
                                                                                } else {
                                                                                    cotizacion.setHFCDigital(false);
                                                                                }
                                                                                resultCotizador("venta");

                                                                            } else {
                                                                                Toast.makeText(this,
                                                                                        "Verifique hay incompatibilidad en los adicionales",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        } else {
                                                                            Toast.makeText(this,
                                                                                    getResources().getString(
                                                                                            R.string.cotizacionInvalida),
                                                                                    Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                } else {
                                                                    Toast.makeText(this,
                                                                            "Cliente con calificacion negativa, prospectar por el modulo Gestor de Visitas",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(this,
                                                                        "Documento del cliente vs Documento de Consulta en Cuenta Une no coincide",
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
                                                Toast.makeText(this, "No ha Ingresado Telefono de destino",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(this,
                                                    "Los Campos Tipo Documento, Documento Y Direccion Siempre Debe Estar Diligenciados Antes De Consolidar La Venta",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(this, "Debe Normalizar La Direccion", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(this, "El Telefono De Servicio Debe Ser Valido", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    } else {
                        cliente.setCarrusel(true);
                        cliente.setProductosCarrusel(UtilidadesTarificador.productosCotizacionCarrusel(cotizacion));
                        //resultCotizador("gerencia de ruta");

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
            Toast.makeText(this, "No puede continuar cotizacion Invalida", Toast.LENGTH_SHORT).show();
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

    public void resultCotizador(String tipoIngreso) {
        Intent intent = new Intent();
        intent.putExtra("cotizacion", cotizacion);
        intent.putExtra("cliente", cliente);
        intent.putExtra("tipoIngreso", tipoIngreso);
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
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

        System.out.println("nuevos " + nuevos);

        return nuevos;
    }

    private void calcularTotalPaquete() {

        double total = 0;
        total += Double.parseDouble(cmpTVDigital.getTotal());
        total += Double.parseDouble(cmpBADigital.getTotal());

        if (cmpTODigital.isActive()) {
            total += Double.parseDouble(cmpTODigital.getTotal());
        }
        cmpTotalPaquete.setTotal(String.valueOf(total));

        if (cmpAdicHD.isActive()) {
            total += Double.parseDouble(cmpAdicHD.getTotal());
        }
        total += cmpDecosOferta.obtenerTotalDecos();
        total += cmpAdicionales.getTotal();
        cmpTotalPaqueteAdicionales.setTotal(String.valueOf(total));

    }

    private void calcularTotalPaqueteConfigurable() {

        int total = 0;
        if (cmpConfTV.isActive()) {
            total += Integer.parseInt(cmpConfTV.getTotal());
        }

        if (cmpConfBA.isActive()) {
            total += Integer.parseInt(cmpConfBA.getTotal());
        }

        if (cmpConfTO.isActive()) {
            total += Integer.parseInt(cmpConfTO.getTotal());
        }
        cmpTotalPaqueteConf.setTotal(String.valueOf(total));

        if (cmpAdicHDConf.isActive()) {
            total += Integer.parseInt(cmpAdicHDConf.getTotal());
        }

        total += cmpAdicionalesConf.getTotal();
        cmpTotalPaqueteAdicionalesConf.setTotal(String.valueOf(total));
    }

    private void calcularTotalPaqueteBalin() {

        int total = 0;
        double totalDescuento = 0;
        if (cmpBalTV.isActive()) {
            System.out.println("cmpBalTV.getTotalDescuento() " + cmpBalTV.getTotalDescuento());

            if (!cmpBalTV.getTotalDescuento().equals("0")) {
                totalDescuento += Double.parseDouble(cmpBalTV.getTotalDescuento());
            } else {
                totalDescuento += Double.parseDouble(cmpBalTV.getTotal());
            }
            total += Integer.parseInt(cmpBalTV.getTotal());
        }

        if (cmpBalBA.isActive()) {
            System.out.println("cmpBalBA.getTotalDescuento() " + cmpBalBA.getTotalDescuento());

            if (!cmpBalBA.getTotalDescuento().equals("0")) {
                totalDescuento += Double.parseDouble(cmpBalBA.getTotalDescuento());
            } else {
                totalDescuento += Double.parseDouble(cmpBalBA.getTotal());
            }

            total += Integer.parseInt(cmpBalBA.getTotal());

            if (!cmpBalBA.getValorGota().equals("0")) {
                totalDescuento += Integer.parseInt(cmpBalBA.getValorGota());
                total += Integer.parseInt(cmpBalBA.getValorGota());
            }

        }

        if (cmpBalTO.isActive()) {
            System.out.println("cmpBalTO.getTotalDescuento() " + cmpBalTO.getTotalDescuento());

            if (!cmpBalTO.getTotalDescuento().equals("0")) {
                totalDescuento += Double.parseDouble(cmpBalTO.getTotalDescuento());
            } else {
                totalDescuento += Double.parseDouble(cmpBalTO.getTotal());
            }
            total += Integer.parseInt(cmpBalTO.getTotal());
        }

        System.out.println("totalDescuento " + totalDescuento);
        if (totalDescuento > 0) {
            cmpTotalPaqueteBal.setTotal(String.valueOf(total) + " (" + String.valueOf(totalDescuento) + ")");
        } else {
            cmpTotalPaqueteBal.setTotal(String.valueOf(total));
        }

        if (!cmpAdicionalesBal.getTotalDescuento().equals("0")) {
            totalDescuento += Double.parseDouble(cmpAdicionalesBal.getTotalDescuento());
        } else {
            totalDescuento += cmpAdicionalesBal.getTotal();
        }

        if (cmpAdicHDBal.isActive()) {
            total += Integer.parseInt(cmpAdicHDBal.getTotal());
            totalDescuento += Integer.parseInt(cmpAdicHDBal.getTotal());
        }

        totalDescuento += cmpDecosBalin.obtenerTotalDecos();
        total += cmpDecosBalin.obtenerTotalDecos();
        total += cmpAdicionalesBal.getTotal();
        totalDescuentosAdicionales = String.valueOf(totalDescuento);
        if (totalDescuento > 0) {
            cmpTotalPaqueteAdicionalesBal.setTotal(String.valueOf(total) + " (" + totalDescuento + ")");
        } else {
            cmpTotalPaqueteAdicionalesBal.setTotal(String.valueOf(total));
        }

    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub
        System.out.println("update => " + value);
        ArrayList<Object> resultado = (ArrayList<Object>) value;
        String lugar = resultado.get(0).toString();

        System.out.println("Lugar " + lugar);

        if (lugar.equals("oferta")) {
            System.out.println("[Evento TV Digital] cmpTVDigital.getPlan() " + cmpTVDigital.getPlan());
            System.out.println("[Evento TV Digital] cmpBADigital.getPlan() " + cmpBADigital.getPlan());
            System.out.println("[Evento TV Digital] resultado.getPlan() " + resultado);

            if (cmpTVDigital.getPlan() != null) {
                if (resultado.size() > 1 && !resultado.get(1).toString().equals("totales")) {
                    cmpBADigital.update(cmpTVDigital.getPlan());
                }
                cmpTODigital.update(cmpTVDigital.getPlan());
            }

            calcularTotalPaquete();
            mostrarDescuentos(lugar);

        } else if (lugar.equals("configurable")) {
            calcularTotalPaqueteConfigurable();
            mostrarDescuentos(lugar);
        } else if (lugar.equals("balin")) {
            mostrarDescuentos(lugar);
            if (!isEmpaquetada()) {
                cmpBalTO.setIndividual(true);
                cmpBalTV.setIndividual(true);
                cmpBalBA.setIndividual(true);
            } else {
                cmpBalTO.setIndividual(false);
                cmpBalTV.setIndividual(false);
                cmpBalBA.setIndividual(false);
            }
            calcularTotalPaqueteBalin();
        } else if (lugar.equals("validarPermiso")) {
            mostarComponenteDecos(resultado.get(1).toString());
        } else if (lugar.equals("decos")) {
            String pestana = resultado.get(1).toString();
            if (pestana.equals("oferta")) {
                calcularTotalPaquete();
            } else if (pestana.equals("balin")) {
                calcularTotalPaqueteBalin();
            } else if (pestana.equals("config")) {
                calcularTotalPaqueteConfigurable();
            }
        } else if (resultado != null && resultado.get(0).equals("guardarLogCarruselAutomatico")) {
            Log.d(TAG,"guardarLogCarruselAutomatico "+resultado.get(1).toString());
            tratarLogAutomaticoCarrusel(resultado.get(1).toString());
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
                        cmpDecosOferta.setPermisoFideliza(true);
                        cmpDecosBalin.setPermisoFideliza(true);
                        cmpDecosConfig.setPermisoFideliza(true);
                    } else {
                        cmpDecosOferta.setPermisoFideliza(false);
                        cmpDecosBalin.setPermisoFideliza(false);
                        cmpDecosConfig.setPermisoFideliza(false);

                    }

                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ArrayList<String> planesDigital(String digital, String tipoProducto) {

        ArrayList<String> planes = new ArrayList<String>();

        System.out.println("planes " + planes);

        try {

            JSONArray array = new JSONArray(digital);

            for (int i = 0; i < array.length(); i++) {

                JSONObject dataProductos = new JSONObject(array.get(i).toString());

                if (dataProductos.getString("producto").equalsIgnoreCase(tipoProducto)) {
                    JSONArray arrayPlanes = new JSONArray(dataProductos.getString("groupeddata"));

                    for (int j = 0; j < arrayPlanes.length(); j++) {

                        planes.add(Utilidades.Quitar_Solo_Tildes(arrayPlanes.getJSONObject(j).get("plan").toString()));

                    }
                }
            }
        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }

        System.out.println("planes planesDigital" + tipoProducto + " " + planes);

        return planes;

    }

    public boolean isEmpaquetada() {
        int numeroProductos = 0;
        if (cmpBalTV.isActive()) {
            numeroProductos++;
        }

        if (cmpBalBA.isActive()) {
            numeroProductos++;
        }

        if (cmpBalTO.isActive()) {
            numeroProductos++;
        }

        if (numeroProductos > 1) {
            return true;
        } else {
            return false;
        }

    }

    public boolean validarCarrusel() {
        boolean validar = true;

        productosLog = new ArrayList<String>();

        if (Utilidades.validarPermiso("carrusel") || cliente.isControlCerca() || Utilidades.CoberturaRural(cliente)) {
            return true;
        }

        System.out.println("carrusel cliente.getArraycarrusel() " + cliente.getArraycarrusel());
        if (Utilidades.excluir("MunicipiosCarrusel", cliente.getCiudad())) {
            if (cliente.isControlCarrusel()) {
                System.out.println("carrusel cliente.getCedula() " + cliente.getCedula());
                System.out.println("carrusel cliente.getDocumentoCarrusel() " + cliente.getDocumentoCarrusel());
                System.out.println("carrusel cliente.getDireccion() " + cliente.getDireccion());
                System.out.println("carrusel cliente.getDireccionCarrusel() " + cliente.getDireccionCarrusel());
                if (cliente.getCedula().equalsIgnoreCase(cliente.getDocumentoCarrusel())
                        && cliente.getDireccion().equalsIgnoreCase(cliente.getDireccionCarrusel())) {

                    if (cliente.getCodigoCarrusel().equals("00") && cliente.getArraycarrusel() != null) {
                        System.out.println(
                                "carrusel cliente.getArraycarrusel().size() " + cliente.getArraycarrusel().size());
                        if (cliente.getArraycarrusel().size() > 0) {
                            for (int i = 0; i < cliente.getArraycarrusel().size(); i++) {
                                System.out.println("carrusel cliente.getArraycarrusel().get(i).getProducto() "
                                        + cliente.getArraycarrusel().get(i).getProducto());
                                if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TO")) {
                                    if(!cotizacion.getTelefonia().equalsIgnoreCase("-")){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                cotizacion.getTipoCotizacionTo(), cotizacion.getTelefonia(), this)) {
                                            System.out.println("carrusel to");
                                            productosLog.add("TO");
                                            break;
                                        }
                                    }

                                } else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("TV")) {
                                    if(!cotizacion.getTelevision().equalsIgnoreCase("-")){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                cotizacion.getTipoCotizacionTv(), cotizacion.getTelevision(), this)) {
                                            System.out.println("carrusel tv");
                                            productosLog.add("TV");
                                            break;
                                        }
                                    }

                                } else if (cliente.getArraycarrusel().get(i).getProducto().equalsIgnoreCase("BA")) {
                                    if(!cotizacion.getInternet().equalsIgnoreCase("-")){
                                        if (!UtilidadesTarificador.validarCarruselProductos(
                                                cotizacion.getTipoCotizacionBa(), cotizacion.getInternet(), this)) {
                                            System.out.println("carrusel ba");
                                            productosLog.add("BA");
                                            break;
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
                        System.out.println("Sin carrusel cliente.getCodigoCarrusel() " + cliente.getCodigoCarrusel());
                        ;
                    } else if (cliente.getCodigoCarrusel().equals("02") || cliente.getCodigoCarrusel().equals("-1")) {
                        System.out.println("Error cliente.getCodigoCarrusel() " + cliente.getCodigoCarrusel());
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

    private void enviarLogCarruselAutomatico(){

        try{

            ArrayList<ProductoAMG> productos = new ArrayList<ProductoAMG>();

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

            if(!cotizacion.getTelefonia().equalsIgnoreCase("-")){
                JSONObject productoJson = new JSONObject();
                productoJson.put("tipo","TO");
                productoJson.put("plan",cotizacion.getTelefonia());
                if(productosLog.contains("TO")){
                    productoJson.put("carrusel","1");
                }else{
                    productoJson.put("carrusel","0");
                }
                dataLogCarruselProductos.put(productoJson);
            }

            if(!cotizacion.getTelevision().equalsIgnoreCase("-")){
                JSONObject productoJson = new JSONObject();
                productoJson.put("tipo","TV");
                productoJson.put("plan",cotizacion.getTelevision());
                if(productosLog.contains("TV")){
                    productoJson.put("carrusel","1");
                }else{
                    productoJson.put("carrusel","0");
                }
                dataLogCarruselProductos.put(productoJson);
            }

            if(!cotizacion.getInternet().equalsIgnoreCase("-")){
                JSONObject productoJson = new JSONObject();
                productoJson.put("tipo","BA");
                productoJson.put("plan",cotizacion.getInternet());
                if(productosLog.contains("BA")){
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
}
