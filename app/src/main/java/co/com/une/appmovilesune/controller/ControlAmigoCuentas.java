package co.com.une.appmovilesune.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.ItemAmigoCuentas;
import co.com.une.appmovilesune.components.ItemAmigocuentasAdicional;
import co.com.une.appmovilesune.components.ItemAmigocuentasAdicionalDesc;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.model.AdicionalAMG;
import co.com.une.appmovilesune.model.AmigoCuentas;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.ProductoAMG;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.Tarificador;

public class ControlAmigoCuentas extends Activity {

    private TituloPrincipal tp;
    private TabHost tabs;
    private Cliente cliente;
    private Scooring scooring;
    private AmigoCuentas ac;
    private LinearLayout llyPortafolioAmCuentas, llyOfertaSuper, llyOfertaBasica, llyOfertaRetencion;
    private ScrollView svPortafolioAmCuentas, svOfertaSuper, svOfertaBasica;
    private int productosCount;
    private double valorPaquetePortafolio = 0, valorPaqueteAdPortafolio = 0, valorCanalesPortafolio = 0,
            valorFinancierosPortafolio = 0;
    private double valorPaqueteSuper = 0, valorPaqueteAdSuper = 0, valorCanalesSuper = 0, valorFinancierosSuper = 0;
    private double valorPaqueteBasica = 0, valorPaqueteAdBasica = 0, valorCanalesBasica = 0, valorFinancierosBasica = 0;
    private double valorPaqueteRetencion = 0, valorPaqueteAdRetencion = 0, valorCanalesRetencion = 0,
            valorFinancierosRetencion = 0;
    private String[] tipoProducto = new String[4];
    private String[] productosSuper = new String[4];
    private String[] productosBasico = new String[4];
    private String[] productosRetencion = new String[4];
    private double[] paqueteBasico = new double[4];
    private double[] financierosBasico = new double[4];
    private double[] paqueteSuper = new double[4];
    private double[] financierosSuper = new double[4];
    private double[] paqueteRetencion = new double[4];
    private double[] financierosRetencion = new double[4];
    private double total;
    private String planTvSuper, planTvBasica, planTvRetencion;
    private ArrayList<String> adicionalesSuper = new ArrayList<String>();
    private ArrayList<Double> adicionalesSuperValor = new ArrayList<Double>();
    private ArrayList<ItemPromocionesAdicionales> adicionalesSuperDescuentos = new ArrayList<ItemPromocionesAdicionales>();
    private ArrayList<String> adicionalesBasica = new ArrayList<String>();
    private ArrayList<Double> adicionalesBasicaValor = new ArrayList<Double>();
    private ArrayList<ItemPromocionesAdicionales> adicionalesBasicaDescuentos = new ArrayList<ItemPromocionesAdicionales>();
    private ArrayList<String> adicionalesRetencion = new ArrayList<String>();
    private ArrayList<Double> adicionalesRetencionValor = new ArrayList<Double>();
    private ArrayList<ItemPromocionesAdicionales> adicionalesRetencionDescuentos = new ArrayList<ItemPromocionesAdicionales>();

    private Resources res;

    public TextView txtValorPaqueteBasico, txtValorPaqueteSuper, txtValorPaqueteRetencion, txtPaqueteCanalesSuper,
            txtPaqueteCanalesBasico, txtPaqueteCanalesRetencion, txtTotalSuper, txtTotalBasica, txtTotalRetencion;
    public LinearLayout llyAdicinalesCanSuper, llyAdicinalesCanBasico, llyAdicinalesCanRetencion;

    public TextView txtDescuentoBasicaTO, txtDescuentoBasicaTV, txtDescuentoBasicaBA, txtDescuentoBasicaHD;
    public TextView txtDescuentoSuperTO, txtDescuentoSuperTV, txtDescuentoSuperBA, txtDescuentoSuperHD;
    public TextView txtDescuentoRetencionTO, txtDescuentoRetencionTV, txtDescuentoRetencionBA, txtDescuentoRetencionHD;
    public TextView txtPromocionesAdicionalesBasica, txtPromocionesAdicionalesSuper, txtPromocionesAdicionalesRetencion;
    public TextView txtValorAdicionalBasicaTotal, txtValorAdicionalSuperTotal, txtValorAdicionalRetencionTotal;

    public String auxAdicionalSuperHD, auxAdicionalBasicaHD, auxAdicionalRetencionHD;

    public Spinner spnAdicionalesSuper, spnAdicionalesBasica, spnAdicionalesRetencion;
    public ArrayList<String> nombsSuper, nombsBasica, nombsRetencion;

    private Tarificador tarificador = new Tarificador();

    boolean renderized = false;

    private String toPlanAnt = "", tvPlanAnt = "", baPlanAnt = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewamigocuentas);

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            cliente = (Cliente) reicieveParams.getSerializable("cliente");
            scooring = (Scooring) reicieveParams.getSerializable("scooring");
            ac = cliente.ac;
        } else {
            System.out.println("no  amigo Cuentas");
        }

        if (savedInstanceState != null) {
            renderized = savedInstanceState.getBoolean("renderized");
            cliente = (Cliente) savedInstanceState.getSerializable("cliente");
            scooring = (Scooring) savedInstanceState.getSerializable("scooring");
            ac = cliente.ac;
        }

        // System.out.println("portafolio Vacio
        // "+ac.productosPortafolio.isEmpty());

        llyPortafolioAmCuentas = (LinearLayout) findViewById(R.id.llyPortafolioAmCuentas);
        llyOfertaSuper = (LinearLayout) findViewById(R.id.llyOfertaSuper);
        llyOfertaBasica = (LinearLayout) findViewById(R.id.llyOfertaBasica);
        llyOfertaRetencion = (LinearLayout) findViewById(R.id.llyOfertaRetencion);
        // svPortafolioAmCuentas =
        // (ScrollView)findViewById(R.id.svPortafolioAmCuentas);
        /*
		 * svOfertaSuper = (ScrollView)findViewById(R.id.svOfertaSuper);
		 * svOfertaBasica = (ScrollView)findViewById(R.id.svOfertaBasica);
		 */

        // System.out.println("Portafolio ac "+ac.portafolio);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("Amigo Cuentas");

        res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("Portafolio");
        spec.setContent(R.id.svPortafolioAmCuentas);
        spec.setIndicator("Portafolio", res.getDrawable(R.drawable.cartera));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Super");
        spec.setContent(R.id.svOfertaSuper);
        spec.setIndicator("Oferta Super", res.getDrawable(R.drawable.cobertura));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Basica");
        spec.setContent(R.id.svOfertaBasica);//
        spec.setIndicator("Oferta Basica", res.getDrawable(R.drawable.portafolio));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Configurable");
        spec.setContent(R.id.svOfertaRetencion);//
        spec.setIndicator("Oferta Configurable", res.getDrawable(R.drawable.portafolio));
        tabs.addTab(spec);

        // tabs.setCurrentTab(0);

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

        tabs.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if (tabId.equals("Contacto")) {
                    tp.setTitulo("Contacto");
                } else if (tabId.equals("Facturacion")) {
                    tp.setTitulo("Facturacion");
                } else if (tabId.equals("Socioeconomico")) {
                    tp.setTitulo("Socioeconomico");
                } else if (tabId.equals("Cliente")) {
                    tp.setTitulo("Cliente");
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

    /*
	 * (non-Javadoc)
	 *
	 * @see android.app.Activity#onResume()
	 */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!renderized) {
            if (ac != null) {
                generarPortafolio();
                generarOfertaSuper();
                generarTotalPaqueteCanalesSuper();
                generarTotalSuper();
                generarOfertaBasica();
                generarTotalPaqueteCanalesBasica();
                generarTotalBasica();
                generarOfertaRetencion();
                generarTotalPaqueteCanalesRetencion();
                generarTotalRetencion();
                renderized = true;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putBoolean("renderized", renderized);
        outState.putSerializable("cliente", cliente);
        outState.putSerializable("scooring", scooring);
    }

    private void generarPortafolio() {
        generarCargosBasicos();
        pintarHD();
        pintarCanales();
        pintarAdicionales();
        mostrarValorPaquete();
        mostrarValorPaqueteCanales();
        mostrarValorTotal();
    }

    private void generarOfertaRetencion() {

        ItemAmigocuentasAdicional iaa = new ItemAmigocuentasAdicional(this);
        LayoutInflater li = LayoutInflater.from(iaa.getContext());
        RelativeLayout componenteTotalPaqueteRetencion = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componenteTotalPaqueteRetencion.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componenteTotalPaqueteRetencion.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componenteTotalPaqueteRetencion.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtValorPaqueteRetencion = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        txtNombreAdicional.setText("Valor Paquete:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtValorPaqueteRetencion.setTextColor(res.getColor(R.color.purple));
        txtValorPaqueteRetencion.setTypeface(null, Typeface.BOLD);
        txtValorPaqueteRetencion.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicional iaac = new ItemAmigocuentasAdicional(this);
        LayoutInflater lic = LayoutInflater.from(iaac.getContext());
        RelativeLayout componenteCanalesRetencion = (RelativeLayout) lic.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProductoC = (ImageView) componenteCanalesRetencion.findViewById(R.id.imgProducto);
        TextView lblTipoProductoC = (TextView) componenteCanalesRetencion.findViewById(R.id.lblTipoProducto);
        llyAdicinalesCanRetencion = (LinearLayout) componenteCanalesRetencion.findViewById(R.id.llyAdicinales);
        txtPromocionesAdicionalesRetencion = (TextView) componenteCanalesRetencion
                .findViewById(R.id.txtPromocionesAdicionales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProductoC.setVisibility(View.GONE);
        imgProductoC.setVisibility(View.GONE);

        for (int i = 0; i < ac.productosOfertaRetencion.size(); i++) {
            llyOfertaRetencion.addView(renderItemsOfertasRetencion(ac.productosOfertaRetencion.get(i)));
        }

        llyOfertaRetencion.addView(componenteCanalesRetencion);
        pintarAdicionalesRetencion();
        pintarCanalesRetencion();
        llyOfertaRetencion.addView(componenteTotalPaqueteRetencion);

    }

    private void generarTotalPaqueteCanalesRetencion() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtPaqueteCanalesRetencion = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        int totalPaquete = 0;
        for (int i = 0; i < paqueteRetencion.length; i++) {
            totalPaquete += paqueteRetencion[i];
        }

        int totalCanales = 0;
        for (int i = 0; i < adicionalesRetencionValor.size(); i++) {
            totalCanales += adicionalesRetencionValor.get(i);
        }

        txtNombreAdicional.setText("Valor Paquete + Canales:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtPaqueteCanalesRetencion
                .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales)));
        txtPaqueteCanalesRetencion.setTextColor(res.getColor(R.color.purple));
        txtPaqueteCanalesRetencion.setTypeface(null, Typeface.BOLD);
        txtPaqueteCanalesRetencion.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyOfertaRetencion.addView(componente);
    }

    private void actualizarPaqueteCanalesRetencion() {

        int totalPaquete = 0;
        for (int i = 0; i < paqueteRetencion.length; i++) {
            totalPaquete += paqueteRetencion[i];
        }

        int totalCanales = 0;
        for (int i = 0; i < adicionalesRetencionValor.size(); i++) {
            totalCanales += adicionalesRetencionValor.get(i);
        }

        txtPaqueteCanalesRetencion
                .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales)));
    }

    private void generarTotalRetencion() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtTotalRetencion = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        double totalPaquete = 0;
        for (int i = 0; i < paqueteRetencion.length; i++) {
            totalPaquete += paqueteRetencion[i];
        }

        double totalFinancieros = 0;
        for (int i = 0; i < financierosRetencion.length; i++) {
            totalFinancieros += financierosRetencion[i];
        }

        double totalCanales = 0;
        for (int i = 0; i < adicionalesRetencionValor.size(); i++) {
            totalCanales += adicionalesRetencionValor.get(i);
        }

        txtNombreAdicional.setText("Total:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtTotalRetencion.setText("$ "
                + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales + totalFinancieros)));
        System.out.println(
                "totalPaquete+totalCanales+totalFinancieros => " + totalPaquete + totalCanales + totalFinancieros);
        txtTotalRetencion.setTextColor(res.getColor(R.color.purple));
        txtTotalRetencion.setTypeface(null, Typeface.BOLD);
        txtTotalRetencion.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyOfertaRetencion.addView(componente);
    }

    private void actualizarTotalRetencion() {

        double totalPaquete = 0;
        for (int i = 0; i < paqueteRetencion.length; i++) {
            totalPaquete += paqueteRetencion[i];
        }

        double totalFinancieros = 0;
        for (int i = 0; i < financierosRetencion.length; i++) {
            totalFinancieros += financierosRetencion[i];
        }

        double totalCanales = 0;
        for (int i = 0; i < adicionalesRetencionValor.size(); i++) {
            totalCanales += adicionalesRetencionValor.get(i);
        }

        txtTotalRetencion.setText("$ "
                + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales + totalFinancieros)));
    }

    private void generarOfertaSuper() {

        ItemAmigocuentasAdicional iaa = new ItemAmigocuentasAdicional(this);
        LayoutInflater li = LayoutInflater.from(iaa.getContext());
        RelativeLayout componenteTotalPaqueteSuper = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componenteTotalPaqueteSuper.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componenteTotalPaqueteSuper.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componenteTotalPaqueteSuper.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtValorPaqueteSuper = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        txtNombreAdicional.setText("Valor Paquete:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtValorPaqueteSuper.setTextColor(res.getColor(R.color.purple));
        txtValorPaqueteSuper.setTypeface(null, Typeface.BOLD);
        txtValorPaqueteSuper.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicional iaac = new ItemAmigocuentasAdicional(this);
        LayoutInflater lic = LayoutInflater.from(iaac.getContext());
        RelativeLayout componenteCanalesSuper = (RelativeLayout) lic.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProductoC = (ImageView) componenteCanalesSuper.findViewById(R.id.imgProducto);
        TextView lblTipoProductoC = (TextView) componenteCanalesSuper.findViewById(R.id.lblTipoProducto);
        llyAdicinalesCanSuper = (LinearLayout) componenteCanalesSuper.findViewById(R.id.llyAdicinales);
        txtPromocionesAdicionalesSuper = (TextView) componenteCanalesSuper.findViewById(R.id.txtPromocionesAdicionales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProductoC.setVisibility(View.GONE);
        imgProductoC.setVisibility(View.GONE);

        for (int i = 0; i < ac.productosOfertaSuper.size(); i++) {
            llyOfertaSuper.addView(renderItemsOfertasSuper(ac.productosOfertaSuper.get(i)));
        }

        llyOfertaSuper.addView(componenteCanalesSuper);
        pintarAdicionalesSuper();
        pintarCanalesSuper();
        llyOfertaSuper.addView(componenteTotalPaqueteSuper);

    }

    private void generarTotalPaqueteCanalesSuper() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtPaqueteCanalesSuper = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        int totalPaquete = 0;
        for (int i = 0; i < paqueteSuper.length; i++) {
            totalPaquete += paqueteSuper[i];
        }

        int totalCanales = 0;
        for (int i = 0; i < adicionalesSuperValor.size(); i++) {
            totalCanales += adicionalesSuperValor.get(i);
        }

        txtNombreAdicional.setText("Valor Paquete + Canales:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtPaqueteCanalesSuper
                .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales)));
        txtPaqueteCanalesSuper.setTextColor(res.getColor(R.color.purple));
        txtPaqueteCanalesSuper.setTypeface(null, Typeface.BOLD);
        txtPaqueteCanalesSuper.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyOfertaSuper.addView(componente);
    }

    private void actualizarPaqueteCanalesSuper() {

        int totalPaquete = 0;
        for (int i = 0; i < paqueteSuper.length; i++) {
            totalPaquete += paqueteSuper[i];
        }

        int totalCanales = 0;
        for (int i = 0; i < adicionalesSuperValor.size(); i++) {
            totalCanales += adicionalesSuperValor.get(i);
        }

        txtPaqueteCanalesSuper
                .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales)));
    }

    private void generarTotalSuper() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtTotalSuper = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        double totalPaquete = 0;
        for (int i = 0; i < paqueteSuper.length; i++) {
            totalPaquete += paqueteSuper[i];
        }

        double totalFinancieros = 0;
        for (int i = 0; i < financierosSuper.length; i++) {
            totalFinancieros += financierosSuper[i];
        }

        double totalCanales = 0;
        for (int i = 0; i < adicionalesSuperValor.size(); i++) {
            totalCanales += adicionalesSuperValor.get(i);
        }

        txtNombreAdicional.setText("Total:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtTotalSuper.setText("$ "
                + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales + totalFinancieros)));
        System.out.println(
                "totalPaquete+totalCanales+totalFinancieros => " + totalPaquete + totalCanales + totalFinancieros);
        txtTotalSuper.setTextColor(res.getColor(R.color.purple));
        txtTotalSuper.setTypeface(null, Typeface.BOLD);
        txtTotalSuper.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyOfertaSuper.addView(componente);
    }

    private void actualizarTotalSuper() {

        double totalPaquete = 0;
        for (int i = 0; i < paqueteSuper.length; i++) {
            totalPaquete += paqueteSuper[i];
        }

        double totalFinancieros = 0;
        for (int i = 0; i < financierosSuper.length; i++) {
            totalFinancieros += financierosSuper[i];
        }

        double totalCanales = 0;
        for (int i = 0; i < adicionalesSuperValor.size(); i++) {
            totalCanales += adicionalesSuperValor.get(i);
        }

        txtTotalSuper.setText("$ "
                + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales + totalFinancieros)));
    }

    private void generarOfertaBasica() {

        ItemAmigocuentasAdicional iaa = new ItemAmigocuentasAdicional(this);
        LayoutInflater li = LayoutInflater.from(iaa.getContext());
        RelativeLayout componenteTotalPaquetebasico = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componenteTotalPaquetebasico.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componenteTotalPaquetebasico.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componenteTotalPaquetebasico.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtValorPaqueteBasico = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        txtNombreAdicional.setText("Valor Paquete:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtValorPaqueteBasico.setTextColor(res.getColor(R.color.purple));
        txtValorPaqueteBasico.setTypeface(null, Typeface.BOLD);
        txtValorPaqueteBasico.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicional iaac = new ItemAmigocuentasAdicional(this);
        LayoutInflater lic = LayoutInflater.from(iaac.getContext());
        RelativeLayout componenteCanalesbasico = (RelativeLayout) lic.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProductoC = (ImageView) componenteCanalesbasico.findViewById(R.id.imgProducto);
        TextView lblTipoProductoC = (TextView) componenteCanalesbasico.findViewById(R.id.lblTipoProducto);
        llyAdicinalesCanBasico = (LinearLayout) componenteCanalesbasico.findViewById(R.id.llyAdicinales);
        txtPromocionesAdicionalesBasica = (TextView) componenteCanalesbasico
                .findViewById(R.id.txtPromocionesAdicionales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProductoC.setVisibility(View.GONE);
        imgProductoC.setVisibility(View.GONE);

        for (int i = 0; i < ac.productosOfertaBasica.size(); i++) {
            llyOfertaBasica.addView(renderItemsOfertasBasica(ac.productosOfertaBasica.get(i)));
        }

        llyOfertaBasica.addView(componenteCanalesbasico);
        pintarAdicionalesBasica();
        pintarCanalesBasica();
        llyOfertaBasica.addView(componenteTotalPaquetebasico);
    }

    private void generarTotalPaqueteCanalesBasica() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtPaqueteCanalesBasico = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        int totalPaquete = 0;
        for (int i = 0; i < paqueteBasico.length; i++) {
            totalPaquete += paqueteBasico[i];
        }

        int totalCanales = 0;
        for (int i = 0; i < adicionalesBasicaValor.size(); i++) {
            totalCanales += adicionalesBasicaValor.get(i);
        }

        txtNombreAdicional.setText("Valor Paquete + Canales:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtPaqueteCanalesBasico
                .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales)));
        txtPaqueteCanalesBasico.setTextColor(res.getColor(R.color.purple));
        txtPaqueteCanalesBasico.setTypeface(null, Typeface.BOLD);
        txtPaqueteCanalesBasico.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyOfertaBasica.addView(componente);
    }

    private void actualizarPaqueteCanalesBasica() {

        int totalPaquete = 0;
        for (int i = 0; i < paqueteBasico.length; i++) {
            totalPaquete += paqueteBasico[i];
        }

        int totalCanales = 0;
        for (int i = 0; i < adicionalesBasicaValor.size(); i++) {
            totalCanales += adicionalesBasicaValor.get(i);
        }

        txtPaqueteCanalesBasico
                .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales)));
    }

    private void generarCargosBasicos() {

        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            System.out.println(ac.productosPortafolio.get(i).producto);
            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TO")) {
                toPlanAnt = ac.productosPortafolio.get(i).plan;
            } else if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")) {
                tvPlanAnt = ac.productosPortafolio.get(i).plan;
            } else if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("BA")) {
                baPlanAnt = ac.productosPortafolio.get(i).plan;
            }

            RelativeLayout componente = renderItems(ac.productosPortafolio.get(i));
            valorPaquetePortafolio += ac.productosPortafolio.get(i).cargoBasico;
            valorFinancierosPortafolio += ac.productosPortafolio.get(i).calcularCargoFinancieros();

            llyPortafolioAmCuentas.addView(componente);
        }

    }

    private void generarTotalBasica() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        txtTotalBasica = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        double totalPaquete = 0;
        for (int i = 0; i < paqueteBasico.length; i++) {
            totalPaquete += paqueteBasico[i];
        }

        double totalFinancieros = 0;
        for (int i = 0; i < financierosBasico.length; i++) {
            totalFinancieros += financierosBasico[i];
        }

        double totalCanales = 0;
        for (int i = 0; i < adicionalesBasicaValor.size(); i++) {
            totalCanales += adicionalesBasicaValor.get(i);
        }

        txtNombreAdicional.setText("Total:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtTotalBasica.setText("$ "
                + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales + totalFinancieros)));
        txtTotalBasica.setTextColor(res.getColor(R.color.purple));
        txtTotalBasica.setTypeface(null, Typeface.BOLD);
        txtTotalBasica.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyOfertaBasica.addView(componente);
    }

    private void actualizarTotalBasica() {

        double totalPaquete = 0;
        for (int i = 0; i < paqueteBasico.length; i++) {
            totalPaquete += paqueteBasico[i];
        }

        double totalFinancieros = 0;
        for (int i = 0; i < financierosBasico.length; i++) {
            totalFinancieros += financierosBasico[i];
        }

        double totalCanales = 0;
        for (int i = 0; i < adicionalesBasicaValor.size(); i++) {
            totalCanales += adicionalesBasicaValor.get(i);
        }

        txtTotalBasica.setText("$ "
                + String.valueOf(new DecimalFormat("#,###.##").format(totalPaquete + totalCanales + totalFinancieros)));
    }

    private void pintarHD() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")) {
                for (int j = 0; j < ac.productosPortafolio.get(i).otrosAdicionales.size(); j++) {
                    for (int k = 0; k < ac.productosPortafolio.get(i).otrosAdicionales.get(j).size(); k++) {
                        if (ac.productosPortafolio.get(i).otrosAdicionales.get(j).get(k).producto
                                .equalsIgnoreCase("TV HD")) {
                            llyPortafolioAmCuentas.addView(renderItemsAdicionales(
                                    ac.productosPortafolio.get(i).otrosAdicionales.get(j), "portafolio"));
                        }
                    }
                }
            }
        }
    }

    private void pintarCanales() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")) {
                for (int j = 0; j < ac.productosPortafolio.get(i).otrosAdicionales.size(); j++) {
                    for (int k = 0; k < ac.productosPortafolio.get(i).otrosAdicionales.get(j).size(); k++) {
                        if (ac.productosPortafolio.get(i).otrosAdicionales.get(j).get(k).producto
                                .equalsIgnoreCase("TV Canales")) {
                            llyPortafolioAmCuentas.addView(renderItemsAdicionales(
                                    ac.productosPortafolio.get(i).otrosAdicionales.get(j), "portafolio"));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void pintarCanalesSuper() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")) {
                for (int j = 0; j < ac.productosPortafolio.get(i).otrosAdicionales.size(); j++) {
                    for (int k = 0; k < ac.productosPortafolio.get(i).otrosAdicionales.get(j).size(); k++) {
                        if (ac.productosPortafolio.get(i).otrosAdicionales.get(j).get(k).producto
                                .equalsIgnoreCase("TV Canales")) {
                            llyOfertaSuper.addView(renderItemsAdicionales(
                                    ac.productosPortafolio.get(i).otrosAdicionales.get(j), "super"));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void pintarCanalesBasica() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")) {
                for (int j = 0; j < ac.productosPortafolio.get(i).otrosAdicionales.size(); j++) {
                    for (int k = 0; k < ac.productosPortafolio.get(i).otrosAdicionales.get(j).size(); k++) {
                        if (ac.productosPortafolio.get(i).otrosAdicionales.get(j).get(k).producto
                                .equalsIgnoreCase("TV Canales")) {
                            llyOfertaBasica.addView(renderItemsAdicionales(
                                    ac.productosPortafolio.get(i).otrosAdicionales.get(j), "basica"));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void pintarCanalesRetencion() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            if (ac.productosPortafolio.get(i).producto.equalsIgnoreCase("TV")) {
                for (int j = 0; j < ac.productosPortafolio.get(i).otrosAdicionales.size(); j++) {
                    for (int k = 0; k < ac.productosPortafolio.get(i).otrosAdicionales.get(j).size(); k++) {
                        if (ac.productosPortafolio.get(i).otrosAdicionales.get(j).get(k).producto
                                .equalsIgnoreCase("TV Canales")) {
                            llyOfertaRetencion.addView(renderItemsAdicionales(
                                    ac.productosPortafolio.get(i).otrosAdicionales.get(j), "retencion"));
                            break;
                        }
                    }
                }
            }
        }
    }

    private void pintarAdicionales() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            for (int j = 0; j < ac.productosPortafolio.get(i).adicionales.size(); j++) {
                llyPortafolioAmCuentas.addView(
                        renderItemsAdicionales(ac.productosPortafolio.get(i).adicionales.get(j), "portafolio"));
            }
        }
    }

    private void pintarAdicionalesSuper() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            for (int j = 0; j < ac.productosPortafolio.get(i).adicionales.size(); j++) {
                llyOfertaSuper
                        .addView(renderItemsAdicionales(ac.productosPortafolio.get(i).adicionales.get(j), "super"));
            }
        }
    }

    private void pintarAdicionalesBasica() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            for (int j = 0; j < ac.productosPortafolio.get(i).adicionales.size(); j++) {
                llyOfertaBasica
                        .addView(renderItemsAdicionales(ac.productosPortafolio.get(i).adicionales.get(j), "basica"));
            }
        }
    }

    private void pintarAdicionalesRetencion() {
        for (int i = 0; i < ac.productosPortafolio.size(); i++) {
            for (int j = 0; j < ac.productosPortafolio.get(i).adicionales.size(); j++) {
                llyOfertaRetencion
                        .addView(renderItemsAdicionales(ac.productosPortafolio.get(i).adicionales.get(j), "retencion"));
            }
        }
    }

    private void mostrarValorPaquete() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        TextView txtValorAdicional = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        txtNombreAdicional.setText("Valor Paquete:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtValorAdicional.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(valorPaquetePortafolio)));
        txtValorAdicional.setTextColor(res.getColor(R.color.purple));
        txtValorAdicional.setTypeface(null, Typeface.BOLD);
        txtValorAdicional.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyPortafolioAmCuentas.addView(componente);
    }

    private void mostrarValorPaqueteCanales() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        TextView txtValorAdicional = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        txtNombreAdicional.setText("Valor Paquete + Canales:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtValorAdicional.setText("$ " + String
                .valueOf(new DecimalFormat("#,###.##").format(valorCanalesPortafolio + valorPaquetePortafolio)));
        txtValorAdicional.setTextColor(res.getColor(R.color.purple));
        txtValorAdicional.setTypeface(null, Typeface.BOLD);
        txtValorAdicional.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyPortafolioAmCuentas.addView(componente);
    }

    private void mostrarValorTotal() {
        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);
        // lblTipoProducto.setText("Total Paquete");
        lblTipoProducto.setVisibility(View.GONE);

        ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

        LayoutInflater lid = LayoutInflater.from(idac.getContext());
        LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
        TextView txtValorAdicional = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

        txtNombreAdicional.setText("Valor Total:");
        txtNombreAdicional.setTextColor(res.getColor(R.color.purple));
        txtNombreAdicional.setTypeface(null, Typeface.BOLD);
        txtNombreAdicional.setTextSize(20);
        txtValorAdicional.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(valorCanalesPortafolio
                + valorPaquetePortafolio + valorFinancierosPortafolio + valorPaqueteAdPortafolio)));
        txtValorAdicional.setTextColor(res.getColor(R.color.purple));
        txtValorAdicional.setTypeface(null, Typeface.BOLD);
        txtValorAdicional.setTextSize(20);

        llyAdicinales.addView(componenteDesc);

        imgProducto.setVisibility(View.GONE);
        llyPortafolioAmCuentas.addView(componente);
    }

    private RelativeLayout renderItems(ProductoAMG producto) {

        // Log.w("PRODUCTO", producto.toString());

        Resources res = getResources();

        final ItemAmigoCuentas iac = new ItemAmigoCuentas(this, producto, "Portafolio");

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentascb, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        ImageView imgSuspendido = (ImageView) componente.findViewById(R.id.imgSuspendido);

        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        TextView lblPlan = (TextView) componente.findViewById(R.id.lblPlan);
        TextView lblPlanFacturacion = (TextView) componente.findViewById(R.id.lblPlanFacturacion);
        TextView lblIdentificador = (TextView) componente.findViewById(R.id.lblIdentificador);
        TextView lblVelocidad = (TextView) componente.findViewById(R.id.lblVelocidad);
        TextView lblVelocidadValor = (TextView) componente.findViewById(R.id.lblVelocidadValor);
        TextView lblCliente = (TextView) componente.findViewById(R.id.lblClienteId);
        TextView lblCargoBasico = (TextView) componente.findViewById(R.id.lblCargoBasico);
        TextView lblCargoBasicoContenido = (TextView) componente.findViewById(R.id.lblCargoBasicoContenido);
        TextView lblCargoAdicionales = (TextView) componente.findViewById(R.id.lblCargoAdicionales);
        TextView lblCargoAdicionalesContenido = (TextView) componente.findViewById(R.id.lblCargoAdicionalesContenido);
        TextView lblCargoFinanciero = (TextView) componente.findViewById(R.id.lblCargoFinanciero);
        TextView lblCargoFinancieroContenido = (TextView) componente.findViewById(R.id.lblCargoFinancieroContenido);
        TextView lblTotal = (TextView) componente.findViewById(R.id.lblTotal);
        TextView lblTotalContenido = (TextView) componente.findViewById(R.id.lblTotalContenido);

        OnClickListener detalle = new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.MODULO_DETALLE_PRODUCTO);
                intent.putExtra("producto", iac.producto);
                startActivity(intent);
            }
        };

        lblPlan.setOnClickListener(detalle);

        if (iac.producto.producto.equalsIgnoreCase("TO")) {
            imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgto));
            tipoProducto[0] = "C";
        } else if (iac.producto.producto.equalsIgnoreCase("TV")) {
            imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
            tipoProducto[1] = "C";
        } else if (iac.producto.producto.equalsIgnoreCase("BA")) {
            imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgba));
            tipoProducto[2] = "C";
        }

        if (producto.estado.equalsIgnoreCase("SXFP")) {
            imgSuspendido.setVisibility(View.VISIBLE);
        }

        lblTipoProducto.setText(iac.producto.nombre);
        lblPlan.setText(iac.producto.plan);
        lblPlanFacturacion.setText(iac.producto.planFacturacion);
        lblIdentificador.setText("Id: " + iac.producto.identificador);
        if (!iac.producto.velocidad.equals("")) {
            lblVelocidadValor.setText(iac.producto.velocidad);
        } else {
            lblVelocidad.setVisibility(View.GONE);
            lblVelocidadValor.setVisibility(View.GONE);
        }

        lblCliente.setText("Cliente: " + iac.producto.cliente);
        if (iac.producto.cliente.equalsIgnoreCase("")) {
            lblCliente.setVisibility(View.GONE);
        }

        lblCargoBasicoContenido
                .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(iac.producto.cargoBasico)));
        lblCargoAdicionalesContenido.setText(
                "$ " + String.valueOf(new DecimalFormat("#,###.##").format(iac.producto.calcularCargoAdicionales())));
        lblCargoFinancieroContenido.setText(
                "$ " + String.valueOf(new DecimalFormat("#,###.##").format(iac.producto.calcularCargoFinancieros())));
        lblTotalContenido
                .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(iac.producto.totalProducto)));

        return componente;

    }

    private RelativeLayout renderItemsAdicionales(ArrayList<AdicionalAMG> adicional, String lugar) {

        // Log.d("adic", adicionales.toString());
        double total = 0;
        Resources res = getResources();

        ItemAmigocuentasAdicional iac = new ItemAmigocuentasAdicional(this);

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentasad, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        LinearLayout llyAdicinales = (LinearLayout) componente.findViewById(R.id.llyAdicinales);

        for (int i = 0; i < adicional.size(); i++) {

            if (adicional.get(i).producto.equalsIgnoreCase("TO")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgto));
            } else if (adicional.get(i).producto.equalsIgnoreCase("TV")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
            } else if (adicional.get(i).producto.equalsIgnoreCase("BA")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgba));
            }

            lblTipoProducto.setText("Adicionales: " + adicional.get(i).producto);

            ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

            LayoutInflater lid = LayoutInflater.from(idac.getContext());
            LinearLayout componenteDesc = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

            ImageView imgEliminar = (ImageView) componenteDesc.findViewById(R.id.imgEliminar);
            TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
            TextView txtValorAdicional = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

            txtNombreAdicional.setText(adicional.get(i).nombre);
            txtValorAdicional
                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(adicional.get(i).cargoBasico)));

            total += adicional.get(i).cargoBasico;

            llyAdicinales.addView(componenteDesc);

            if (adicional.get(i).producto.equals("TV HD")) {
                if (lugar.equals("portafolio")) {
                    valorPaquetePortafolio += adicional.get(i).cargoBasico;
                } else if (lugar.equals("basica")) {
                    valorPaqueteBasica += adicional.get(i).cargoBasico;
                } else if (lugar.equals("super")) {
                    valorPaqueteSuper += adicional.get(i).cargoBasico;
                } else if (lugar.equals("retencion")) {
                    valorPaqueteRetencion += adicional.get(i).cargoBasico;
                }

            } else if (adicional.get(i).producto.equals("TV Canales")) {
                // txtEliminarAdicional.setVisibility(View.VISIBLE);
                if (lugar.equals("portafolio")) {
                    valorCanalesPortafolio += adicional.get(i).cargoBasico;
                } else if (lugar.equals("basica")) {
                    valorCanalesBasica += adicional.get(i).cargoBasico;
                } else if (lugar.equals("super")) {
                    valorCanalesSuper += adicional.get(i).cargoBasico;
                } else if (lugar.equals("retencion")) {
                    valorCanalesRetencion += adicional.get(i).cargoBasico;
                }

            } else if (adicional.get(i).producto.equals("TO")) {
                if (lugar.equals("portafolio")) {
                    valorPaqueteAdPortafolio += adicional.get(i).cargoBasico;
                } else if (lugar.equals("basica")) {
                    valorPaqueteAdBasica += adicional.get(i).cargoBasico;
                } else if (lugar.equals("super")) {
                    valorPaqueteAdSuper += adicional.get(i).cargoBasico;
                } else if (lugar.equals("retencion")) {
                    valorPaqueteAdRetencion += adicional.get(i).cargoBasico;
                }

            } else if (adicional.get(i).producto.equals("TV")) {
                if (lugar.equals("portafolio")) {
                    valorPaqueteAdPortafolio += adicional.get(i).cargoBasico;
                } else if (lugar.equals("basica")) {
                    valorPaqueteAdBasica += adicional.get(i).cargoBasico;
                } else if (lugar.equals("super")) {
                    valorPaqueteAdSuper += adicional.get(i).cargoBasico;
                } else if (lugar.equals("retencion")) {
                    valorPaqueteAdRetencion += adicional.get(i).cargoBasico;
                }

            }
        }

        LinearLayout componenteDescTotal = (LinearLayout) li.inflate(R.layout.itemamigocuentasaddesc, null);

        TextView txtNombreAdicionalTotal = (TextView) componenteDescTotal.findViewById(R.id.txtNombreAdicional);
        TextView txtValorAdicionalTotal = (TextView) componenteDescTotal.findViewById(R.id.txtValorAdicional);

        txtNombreAdicionalTotal.setText("Total");
        txtValorAdicionalTotal.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(total)));
        txtNombreAdicionalTotal.setTextColor(res.getColor(R.color.purple));
        txtValorAdicionalTotal.setTextColor(res.getColor(R.color.purple));

        llyAdicinales.addView(componenteDescTotal);

        return componente;

    }

    private RelativeLayout renderItemsOfertasSuper(ArrayList<ProductoAMG> planes) {

        final ItemAmigoCuentas iac = new ItemAmigoCuentas(this);
        iac.productos = planes;

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentascb, null);

        final ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        final Spinner spnProducto = (Spinner) componente.findViewById(R.id.spnProducto);
        final TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        final TextView lblPlan = (TextView) componente.findViewById(R.id.lblPlan);
        final TextView lblPlanFacturacion = (TextView) componente.findViewById(R.id.lblPlanFacturacion);
        final TextView lblVelocidad = (TextView) componente.findViewById(R.id.lblVelocidad);
        final TextView lblVelocidadValor = (TextView) componente.findViewById(R.id.lblVelocidadValor);
        final TextView lblCargoBasico = (TextView) componente.findViewById(R.id.lblCargoBasico);
        final TextView lblCargoBasicoContenido = (TextView) componente.findViewById(R.id.lblCargoBasicoContenido);
        final TextView lblCargoAdicionales = (TextView) componente.findViewById(R.id.lblCargoAdicionales);
        final TextView lblCargoAdicionalesContenido = (TextView) componente
                .findViewById(R.id.lblCargoAdicionalesContenido);
        final TextView lblCargoFinanciero = (TextView) componente.findViewById(R.id.lblCargoFinanciero);
        final TextView lblCargoFinancieroContenido = (TextView) componente
                .findViewById(R.id.lblCargoFinancieroContenido);
        final TextView lblTotal = (TextView) componente.findViewById(R.id.lblTotal);
        final TextView lblTotalContenido = (TextView) componente.findViewById(R.id.lblTotalContenido);

        if (iac.productos.size() > 0) {
            if (iac.productos.get(0).producto.equalsIgnoreCase("TO")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgto));
                lblTipoProducto.setText("Telefonia");
                txtDescuentoSuperTO = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("TV")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Television");
                txtDescuentoSuperTV = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("BA")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgba));
                lblTipoProducto.setText("Internet");
                txtDescuentoSuperBA = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("ADICHD")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Television HD");
                txtDescuentoSuperHD = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Canales Adicionales");
            }

            lblPlan.setVisibility(View.GONE);
            lblVelocidad.setVisibility(View.GONE);
            lblVelocidadValor.setVisibility(View.GONE);
            lblCargoAdicionales.setVisibility(View.GONE);
            lblCargoAdicionalesContenido.setVisibility(View.GONE);

            final ArrayList<String> noms = new ArrayList<String>();
            if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                noms.add("-");
            }
            for (int i = 0; i < iac.productos.size(); i++) {
                noms.add(iac.productos.get(i).plan);
            }

            if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                nombsSuper = noms;
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, noms);
            spnProducto.setAdapter(adp);
            spnProducto.setVisibility(View.VISIBLE);

            spnProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

					/*
					 * System.out.println("Position "+position);
					 * lblPlanFacturacion .setText(iac.productos.get(position
					 * ).planFactura);
					 *
					 * lblCargoBasicoContenido.setText("$ "+String.valueOf (new
					 * DecimalFormat("#,###.##").format(iac.productos
					 * .get(position).cargoBasico)));
					 * lblCargoFinancieroContenido .setText("$ "
					 * +String.valueOf(new DecimalFormat("#,###.##"
					 * ).format(iac.productos.get (position).obtenerIVA())));
					 * lblTotalContenido.setText("$ "+String.valueOf(new
					 * DecimalFormat ("#,###.##").format(iac.productos.get
					 * (position).totalProducto)));
					 */
                    boolean band = false;

                    if (position == iac.productos.size()) {
                        position = position - 1;
                        band = true;
                    }

                    if (iac.productos.get(position).producto.equals("TO")) {
                        System.out.println("TO");
                        lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                        lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                        lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                        lblTotalContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                        paqueteSuper[0] = iac.productos.get(position).cargoBasico;
                        financierosSuper[0] = iac.productos.get(position).calcularCargoFinancieros();
                        productosSuper[0] = iac.productos.get(position).plan;
                        ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_SUPER);

                        actualizarPaqueteCanalesSuper();
                        actualizarTotalSuper();

                        ac.mostrarDescuentos(AmigoCuentas.OFERTA_SUPER);

                        mostrarDescuentosSuper();
                    } else if (iac.productos.get(position).producto.equals("TV")) {
                        lblPlanFacturacion.setText(iac.productos.get(position).planFactura);
                        planTvSuper = iac.productos.get(position).plan;
                        lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                        lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                        lblTotalContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                        paqueteSuper[1] = iac.productos.get(position).cargoBasico;
                        financierosSuper[1] = iac.productos.get(position).calcularCargoFinancieros();
                        productosSuper[1] = iac.productos.get(position).plan;
                        ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_SUPER);

                        actualizarPaqueteCanalesSuper();
                        actualizarTotalSuper();

                        ac.mostrarDescuentos(AmigoCuentas.OFERTA_SUPER);

                        mostrarDescuentosSuper();

                        adicionarDecosSuper();

                    } else if (iac.productos.get(position).producto.equals("BA")) {
                        System.out.println("BA");
                        lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                        lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                        lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                        lblTotalContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                        paqueteSuper[2] = iac.productos.get(position).cargoBasico;
                        financierosSuper[2] = iac.productos.get(position).calcularCargoFinancieros();
                        productosSuper[2] = iac.productos.get(position).plan;
                        ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_SUPER);

                        actualizarPaqueteCanalesSuper();
                        actualizarTotalSuper();

                        ac.mostrarDescuentos(AmigoCuentas.OFERTA_SUPER);

                        mostrarDescuentosSuper();
                    } else if (iac.productos.get(position).producto.equals("ADICHD")) {
                        System.out.println("ADICHD");
                        lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                        lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                        lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                        lblTotalContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                        paqueteSuper[3] = iac.productos.get(position).cargoBasico;
                        financierosSuper[3] = iac.productos.get(position).calcularCargoFinancieros();
                        productosSuper[3] = iac.productos.get(position).plan;
                        ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_SUPER);

                        actualizarPaqueteCanalesSuper();
                        actualizarTotalSuper();

                        if (adicionalesSuper.size() > 0) {
                            adicionalesSuper.remove(auxAdicionalSuperHD);
                        }
                        auxAdicionalSuperHD = iac.productos.get(position).plan;
                        adicionalesSuper.add(iac.productos.get(position).plan);
                        adicionalesSuperValor.add(iac.productos.get(position).totalProducto);
                        mostrarDescuentosAdicionalesSuper("ADICHD");
                    } else if (iac.productos.get(position).producto.equals("ADICTV")
                            || iac.productos.get(position).producto.equals("DECO")) {
                        System.out.println("ADICTV");
                        if (position != 0) {
                            if (!band) {
                                position = position - 1;
                            }
                            if (!adicionalesSuper.contains(iac.productos.get(position).plan)) {
                                System.out.println("No Contiene");
                                adicionalesSuper.add(iac.productos.get(position).plan);
                                adicionalesSuperValor.add(iac.productos.get(position).totalProducto);
                                lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                                lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                        new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                                lblCargoFinancieroContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).obtenerIVA())));
                                lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).totalProducto)));

                                ItemAmigocuentasAdicionalDesc idaca = new ItemAmigocuentasAdicionalDesc(
                                        getApplication());

                                LayoutInflater lidc = LayoutInflater.from(idaca.getContext());
                                LinearLayout componenteDescc = (LinearLayout) lidc
                                        .inflate(R.layout.itemamigocuentasaddesc, null);

                                ImageView imgEliminar = (ImageView) componenteDescc.findViewById(R.id.imgEliminar);
                                TextView txtNombreAdicionalC = (TextView) componenteDescc
                                        .findViewById(R.id.txtNombreAdicional);
                                TextView txtValor = (TextView) componenteDescc.findViewById(R.id.txtValorAdicional);

                                imgEliminar.setVisibility(View.VISIBLE);
                                txtNombreAdicionalC.setText(iac.productos.get(position).plan);
                                txtNombreAdicionalC.setTextColor(res.getColor(R.color.purple));
                                // txtNombreAdicionalC.setTypeface(null,
                                // Typeface.BOLD);
                                txtNombreAdicionalC.setTextSize(16);
                                txtValor.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).totalProducto)));
                                txtValor.setTextColor(res.getColor(R.color.purple));
                                // txtValor.setTypeface(null,
                                // Typeface.BOLD);
                                txtValor.setTextSize(16);

                                imgEliminar.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated
                                        // method stub
                                        // System.out.println("Se eliminara "+);
                                        RelativeLayout prueba = (RelativeLayout) v.getParent();
                                        TextView txtAdicional = (TextView) prueba.findViewById(R.id.txtNombreAdicional);
                                        for (int i = 0; i < adicionalesSuper.size(); i++) {
                                            if (adicionalesSuper.get(i).equals(txtAdicional.getText().toString())) {
                                                adicionalesSuper.remove(i);
                                                adicionalesSuperValor.remove(i);
                                            }
                                        }
                                        System.out.println(txtAdicional.getText());
                                        llyAdicinalesCanSuper.removeView(prueba);
                                        // System.out.println(llyAdicinalesCanRetencion.get);

                                        double abt = 0;
                                        for (int i = 0; i < adicionalesSuperValor.size(); i++) {
                                            abt += adicionalesSuperValor.get(i);
                                        }

                                        txtValorAdicionalSuperTotal.setText(
                                                "$ " + String.valueOf(new DecimalFormat("#,###.##").format(abt)));

                                        actualizarPaqueteCanalesSuper();
                                        actualizarTotalSuper();

                                        mostrarDescuentosAdicionalesSuper("ADICTV");
                                    }
                                });
                                if (llyAdicinalesCanSuper.getChildCount() > 0) {
                                    llyAdicinalesCanSuper.removeView(llyAdicinalesCanSuper
                                            .getChildAt(llyAdicinalesCanSuper.getChildCount() - 1));
                                }
                                llyAdicinalesCanSuper.addView(componenteDescc);

                                ItemAmigocuentasAdicionalDesc idacaT = new ItemAmigocuentasAdicionalDesc(
                                        getApplication());
                                LayoutInflater lidct = LayoutInflater.from(idacaT.getContext());
                                LinearLayout componenteDescTotal = (LinearLayout) lidct
                                        .inflate(R.layout.itemamigocuentasaddesc, null);

                                TextView txtNombreAdicionalTotal = (TextView) componenteDescTotal
                                        .findViewById(R.id.txtNombreAdicional);
                                txtValorAdicionalSuperTotal = (TextView) componenteDescTotal
                                        .findViewById(R.id.txtValorAdicional);

                                double abt = 0;
                                for (int i = 0; i < adicionalesSuperValor.size(); i++) {
                                    abt += adicionalesSuperValor.get(i);
                                }

                                txtNombreAdicionalTotal.setText("Total");
                                txtNombreAdicionalTotal.setTextColor(res.getColor(R.color.purple));
                                txtNombreAdicionalTotal.setTypeface(null, Typeface.BOLD);
                                txtNombreAdicionalTotal.setTextSize(16);
                                txtValorAdicionalSuperTotal
                                        .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(abt)));
                                txtValorAdicionalSuperTotal.setTextColor(res.getColor(R.color.purple));
                                txtValorAdicionalSuperTotal.setTypeface(null, Typeface.BOLD);
                                txtValorAdicionalSuperTotal.setTextSize(16);

                                llyAdicinalesCanSuper.addView(componenteDescTotal);

                                actualizarPaqueteCanalesSuper();
                                actualizarTotalSuper();

                                mostrarDescuentosAdicionalesSuper("ADICTV");

                            } else {
                                System.out.println("Contiene");
                            }

                        } else {
                            lblPlanFacturacion.setText("");

                            lblCargoBasicoContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblCargoFinancieroContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                        }

                    }

                    calcularValorPaqueteSuper();

                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // lblMensaje.setText("");
                }
            });

            lblPlan.setVisibility(View.GONE);
			/*
			 * lblPlan.setText(iac.producto.plan);
			 * lblPlanFacturacion.setText(iac.producto.identificador);
			 * if(!iac.producto.velocidad.equals("")){
			 * lblVelocidadValor.setText(iac.producto.velocidad); }else{
			 * lblVelocidad.setVisibility(View.GONE);
			 * lblVelocidadValor.setVisibility(View.GONE); }
			 *
			 * lblCargoBasicoContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat("#,###.##").format(iac.producto.cargoBasico)));
			 * lblCargoAdicionalesContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat
			 * ("#,###.##").format(iac.producto.calcularCargoAdicionales())));
			 * lblCargoFinancieroContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat
			 * ("#,###.##").format(iac.producto.calcularCargoFinancieros())));
			 * lblTotalContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat("#,###.##").format(iac.producto.totalProducto)));
			 */

            if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                spnAdicionalesSuper = spnProducto;
            }

        }

        return componente;

    }

    private void adicionarDecosSuper() {
        if (spnAdicionalesSuper != null && nombsSuper != null) {
            for (int i = 0; i < ac.productosOfertaSuper.size(); i++) {
                for (int j = 0; j < ac.productosOfertaSuper.get(i).size(); j++) {
                    if (ac.productosOfertaSuper.get(i).get(j).nombre.equals("DECO")) {
                        ac.productosOfertaSuper.get(i).remove(ac.productosOfertaSuper.get(i).get(j));
                    }
                }
            }

            ArrayList<ArrayList<String>> decos = UtilidadesTarificador.llenarAdicionales(planTvSuper,
                    cliente.getDepartamento(), cliente.getEstrato());
            if (decos != null) {
                for (int i = 0; i < ac.productosOfertaSuper.size(); i++) {
                    if (ac.productosOfertaSuper.get(i).get(0).nombre.equals("ADICTV")) {
                        for (int j = 0; j < decos.size(); j++) {
                            ProductoAMG prod = new ProductoAMG();
                            prod.nombre = "DECO";
                            prod.producto = "DECO";
                            prod.plan = decos.get(j).get(0);
                            prod.cargoBasico = Double.parseDouble(decos.get(j).get(1));
                            prod.totalCargoBasico = Double.parseDouble(decos.get(j).get(1));
                            prod.totalProducto = Double.parseDouble(decos.get(j).get(1));
                            ac.productosOfertaSuper.get(i).add(prod);
                            nombsSuper.add(decos.get(j).get(0));
                        }
                    }
                }
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nombsSuper);
            spnAdicionalesSuper.setAdapter(adp);
        }
    }

    private RelativeLayout renderItemsOfertasBasica(ArrayList<ProductoAMG> planes) {

        final ItemAmigoCuentas iac = new ItemAmigoCuentas(this);
        iac.productos = planes;

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentascb, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        Spinner spnProducto = (Spinner) componente.findViewById(R.id.spnProducto);
        final TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        final TextView lblPlan = (TextView) componente.findViewById(R.id.lblPlan);
        final TextView lblPlanFacturacion = (TextView) componente.findViewById(R.id.lblPlanFacturacion);
        final TextView lblVelocidad = (TextView) componente.findViewById(R.id.lblVelocidad);
        final TextView lblVelocidadValor = (TextView) componente.findViewById(R.id.lblVelocidadValor);
        final TextView lblCargoBasico = (TextView) componente.findViewById(R.id.lblCargoBasico);
        final TextView lblCargoBasicoContenido = (TextView) componente.findViewById(R.id.lblCargoBasicoContenido);
        final TextView lblCargoAdicionales = (TextView) componente.findViewById(R.id.lblCargoAdicionales);
        final TextView lblCargoAdicionalesContenido = (TextView) componente
                .findViewById(R.id.lblCargoAdicionalesContenido);
        final TextView lblCargoFinanciero = (TextView) componente.findViewById(R.id.lblCargoFinanciero);
        final TextView lblCargoFinancieroContenido = (TextView) componente
                .findViewById(R.id.lblCargoFinancieroContenido);
        final TextView lblTotal = (TextView) componente.findViewById(R.id.lblTotal);
        final TextView lblTotalContenido = (TextView) componente.findViewById(R.id.lblTotalContenido);

        if (iac.productos.size() > 0) {
            if (iac.productos.get(0).producto.equalsIgnoreCase("TO")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgto));
                lblTipoProducto.setText("Telefonia");
                txtDescuentoBasicaTO = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("TV")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Television");
                txtDescuentoBasicaTV = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("BA")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgba));
                lblTipoProducto.setText("Internet");
                txtDescuentoBasicaBA = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("ADICHD")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Television HD");
                txtDescuentoBasicaHD = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Canales Adicionales");
            }

            lblPlan.setVisibility(View.GONE);
            lblVelocidad.setVisibility(View.GONE);
            lblVelocidadValor.setVisibility(View.GONE);
            lblCargoAdicionales.setVisibility(View.GONE);
            lblCargoAdicionalesContenido.setVisibility(View.GONE);

            lblCargoFinanciero.setText("IVA:");

            ArrayList<String> noms = new ArrayList<String>();
            if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")
                    || iac.productos.get(0).producto.equalsIgnoreCase("ADICHD")) {
                noms.add("-");
            }

            for (int i = 0; i < iac.productos.size(); i++) {
                noms.add(iac.productos.get(i).plan);
            }

            if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                nombsBasica = noms;
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, noms);
            spnProducto.setAdapter(adp);
            spnProducto.setVisibility(View.VISIBLE);

            spnProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
					/*
					 * lblPlanFacturacion.setText(iac.productos.get(position
					 * ).planFactura);
					 *
					 * lblCargoBasicoContenido.setText("$ "+String.valueOf (new
					 * DecimalFormat("#,###.##").format(iac.productos
					 * .get(position).cargoBasico)));
					 * lblCargoFinancieroContenido .setText("$ "
					 * +String.valueOf(new DecimalFormat("#,###.##"
					 * ).format(iac.productos.get (position).obtenerIVA())));
					 * lblTotalContenido.setText("$ "+String.valueOf(new
					 * DecimalFormat ("#,###.##").format(iac.productos.get
					 * (position).totalProducto)));
					 */

                    boolean band = false;
                    if (position == iac.productos.size()) {
                        position = position - 1;
                        band = true;
                    }

                    if (iac.productos.get(position).producto.equals("TO")) {
                        System.out.println("TO");
                        lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                        lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                        lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                        lblTotalContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                        paqueteBasico[0] = iac.productos.get(position).cargoBasico;
                        financierosBasico[0] = iac.productos.get(position).calcularCargoFinancieros();
                        productosBasico[0] = iac.productos.get(position).plan;
                        ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_BASICA);

                        actualizarPaqueteCanalesBasica();
                        actualizarTotalBasica();
                        mostrarTipoPlan();

                        ac.mostrarDescuentos(AmigoCuentas.OFERTA_BASICA);

                        mostrarDescuentosbasico();

                    } else if (iac.productos.get(position).producto.equals("TV")) {
                        System.out.println("TV");
                        lblPlanFacturacion.setText(iac.productos.get(position).planFactura);
                        planTvBasica = iac.productos.get(position).plan;
                        lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                        lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                        lblTotalContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                        paqueteBasico[1] = iac.productos.get(position).cargoBasico;
                        financierosBasico[1] = iac.productos.get(position).calcularCargoFinancieros();
                        productosBasico[1] = iac.productos.get(position).plan;
                        ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_BASICA);

                        actualizarPaqueteCanalesBasica();
                        actualizarTotalBasica();
                        mostrarTipoPlan();

                        ac.mostrarDescuentos(AmigoCuentas.OFERTA_BASICA);

                        mostrarDescuentosbasico();

                        adicionarDecosBasica();

                    } else if (iac.productos.get(position).producto.equals("BA")) {
                        System.out.println("BA");
                        lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                        lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                        lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                        lblTotalContenido.setText("$ " + String.valueOf(
                                new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                        paqueteBasico[2] = iac.productos.get(position).cargoBasico;
                        financierosBasico[2] = iac.productos.get(position).calcularCargoFinancieros();
                        productosBasico[2] = iac.productos.get(position).plan;
                        ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_BASICA);

                        actualizarPaqueteCanalesBasica();
                        actualizarTotalBasica();
                        mostrarTipoPlan();

                        ac.mostrarDescuentos(AmigoCuentas.OFERTA_BASICA);

                        mostrarDescuentosbasico();

                    } else if (iac.productos.get(position).producto.equals("ADICHD")) {
                        System.out.println("ADICHD");
                        if (position != 0 || band) {
                            if (!band) {
                                position = position - 1;
                            }
                            lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                            lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                            lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                            lblTotalContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                            paqueteBasico[3] = iac.productos.get(position).cargoBasico;
                            financierosBasico[3] = iac.productos.get(position).calcularCargoFinancieros();
                            productosBasico[3] = iac.productos.get(position).plan;
                            ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_BASICA);

                            actualizarPaqueteCanalesBasica();
                            actualizarTotalBasica();
                            mostrarTipoPlan();

                            // ac.mostrarDescuentos(AmigoCuentas.OFERTA_BASICA);
                            if (adicionalesBasica.size() > 0) {
                                adicionalesBasica.remove(auxAdicionalBasicaHD);
                            }
                            auxAdicionalBasicaHD = iac.productos.get(position).plan;
                            adicionalesBasica.add(iac.productos.get(position).plan);
                            adicionalesBasicaValor.add(iac.productos.get(position).totalProducto);
                            mostrarDescuentosAdicionalesBasica("ADICHD");
                        } else {
                            if (adicionalesBasica.size() > 0) {
                                adicionalesBasica.remove(auxAdicionalBasicaHD);
                                for (int j = 0; j < ac.productosBasica.size(); j++) {
                                    if (ac.productosBasica.get(j).producto.equals("ADICHD")) {
                                        ac.productosBasica.remove(j);
                                    }
                                }
                            }
                            lblPlanFacturacion.setText("");
                            lblCargoBasicoContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblCargoFinancieroContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                        }
                    } else if (iac.productos.get(position).producto.equals("ADICTV")
                            || iac.productos.get(position).producto.equals("DECO")) {
                        System.out.println("ADICTV");
                        if (position != 0) {
                            if (!band) {
                                position = position - 1;
                            }
                            if (!adicionalesBasica.contains(iac.productos.get(position).plan)) {
                                System.out.println("No Contiene");
                                adicionalesBasica.add(iac.productos.get(position).plan);
                                adicionalesBasicaValor.add(iac.productos.get(position).totalProducto);
                                lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                                lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                        new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                                lblCargoFinancieroContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).obtenerIVA())));
                                lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).totalProducto)));

                                ItemAmigocuentasAdicionalDesc idaca = new ItemAmigocuentasAdicionalDesc(
                                        getApplication());

                                LayoutInflater lidc = LayoutInflater.from(idaca.getContext());
                                LinearLayout componenteDescc = (LinearLayout) lidc
                                        .inflate(R.layout.itemamigocuentasaddesc, null);

                                ImageView imgEliminar = (ImageView) componenteDescc.findViewById(R.id.imgEliminar);
                                TextView txtNombreAdicionalC = (TextView) componenteDescc
                                        .findViewById(R.id.txtNombreAdicional);
                                TextView txtValor = (TextView) componenteDescc.findViewById(R.id.txtValorAdicional);

                                imgEliminar.setVisibility(View.VISIBLE);
                                txtNombreAdicionalC.setText(iac.productos.get(position).plan);
                                txtNombreAdicionalC.setTextColor(res.getColor(R.color.purple));
                                // txtNombreAdicionalC.setTypeface(null,
                                // Typeface.BOLD);
                                txtNombreAdicionalC.setTextSize(16);
                                txtValor.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).totalProducto)));
                                txtValor.setTextColor(res.getColor(R.color.purple));
                                // txtValor.setTypeface(null,
                                // Typeface.BOLD);
                                txtValor.setTextSize(16);

                                imgEliminar.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated
                                        // method stub
                                        // System.out.println("Se eliminara "+);
                                        RelativeLayout prueba = (RelativeLayout) v.getParent();
                                        TextView txtAdicional = (TextView) prueba.findViewById(R.id.txtNombreAdicional);
                                        for (int i = 0; i < adicionalesBasica.size(); i++) {
                                            if (adicionalesBasica.get(i).equals(txtAdicional.getText().toString())) {
                                                adicionalesBasica.remove(i);
                                                adicionalesBasicaValor.remove(i);
                                            }
                                        }
                                        System.out.println(txtAdicional.getText());
                                        llyAdicinalesCanBasico.removeView(prueba);
                                        // System.out.println(llyAdicinalesCanRetencion.get);

                                        double abt = 0;
                                        for (int i = 0; i < adicionalesBasicaValor.size(); i++) {
                                            abt += adicionalesBasicaValor.get(i);
                                        }

                                        txtValorAdicionalBasicaTotal.setText(
                                                "$ " + String.valueOf(new DecimalFormat("#,###.##").format(abt)));

                                        actualizarPaqueteCanalesBasica();
                                        actualizarTotalBasica();

                                        mostrarDescuentosAdicionalesBasica("ADICTV");
                                    }
                                });

                                if (llyAdicinalesCanBasico.getChildCount() > 0) {
                                    llyAdicinalesCanBasico.removeView(llyAdicinalesCanBasico
                                            .getChildAt(llyAdicinalesCanBasico.getChildCount() - 1));
                                }
                                llyAdicinalesCanBasico.addView(componenteDescc);

                                ItemAmigocuentasAdicionalDesc idacaT = new ItemAmigocuentasAdicionalDesc(
                                        getApplication());
                                LayoutInflater lidct = LayoutInflater.from(idacaT.getContext());
                                LinearLayout componenteDescTotal = (LinearLayout) lidct
                                        .inflate(R.layout.itemamigocuentasaddesc, null);

                                TextView txtNombreAdicionalTotal = (TextView) componenteDescTotal
                                        .findViewById(R.id.txtNombreAdicional);
                                txtValorAdicionalBasicaTotal = (TextView) componenteDescTotal
                                        .findViewById(R.id.txtValorAdicional);

                                double abt = 0;
                                for (int i = 0; i < adicionalesBasicaValor.size(); i++) {
                                    abt += adicionalesBasicaValor.get(i);
                                }

                                txtNombreAdicionalTotal.setText("Total");
                                txtNombreAdicionalTotal.setTextColor(res.getColor(R.color.purple));
                                txtNombreAdicionalTotal.setTypeface(null, Typeface.BOLD);
                                txtNombreAdicionalTotal.setTextSize(16);
                                txtValorAdicionalBasicaTotal
                                        .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(abt)));
                                txtValorAdicionalBasicaTotal.setTextColor(res.getColor(R.color.purple));
                                txtValorAdicionalBasicaTotal.setTypeface(null, Typeface.BOLD);
                                txtValorAdicionalBasicaTotal.setTextSize(16);

                                llyAdicinalesCanBasico.addView(componenteDescTotal);

                                actualizarPaqueteCanalesBasica();
                                actualizarTotalBasica();

                                mostrarDescuentosAdicionalesBasica("ADICTV");
                            } else {
                                System.out.println("Contiene");
                            }

                        } else {
                            lblPlanFacturacion.setText("");

                            lblCargoBasicoContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblCargoFinancieroContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                        }
                    }

                    calcularValorPaqueteBasico();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // lblMensaje.setText("");
                }
            });

			/*
			 * lblPlan.setText(iac.producto.plan);
			 * lblPlanFacturacion.setText(iac.producto.identificador);
			 * if(!iac.producto.velocidad.equals("")){
			 * lblVelocidadValor.setText(iac.producto.velocidad); }else{
			 * lblVelocidad.setVisibility(View.GONE);
			 * lblVelocidadValor.setVisibility(View.GONE); }
			 *
			 * lblCargoBasicoContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat("#,###.##").format(iac.producto.cargoBasico)));
			 * lblCargoAdicionalesContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat
			 * ("#,###.##").format(iac.producto.calcularCargoAdicionales())));
			 * lblCargoFinancieroContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat
			 * ("#,###.##").format(iac.producto.calcularCargoFinancieros())));
			 * lblTotalContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat("#,###.##").format(iac.producto.totalProducto)));
			 */

            if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                spnAdicionalesBasica = spnProducto;
            }
        }

        return componente;

    }

    private void adicionarDecosBasica() {
        if (spnAdicionalesBasica != null && nombsBasica != null) {
            for (int i = 0; i < ac.productosOfertaBasica.size(); i++) {
                for (int j = 0; j < ac.productosOfertaBasica.get(i).size(); j++) {
                    if (ac.productosOfertaBasica.get(i).get(j).nombre.equals("DECO")) {
                        ac.productosOfertaBasica.get(i).remove(ac.productosOfertaBasica.get(i).get(j));
                    }
                }
            }

            ArrayList<ArrayList<String>> decos = UtilidadesTarificador.llenarAdicionales(planTvBasica,
                    cliente.getDepartamento(), cliente.getEstrato());
            if (decos != null) {
                for (int i = 0; i < ac.productosOfertaBasica.size(); i++) {
                    if (ac.productosOfertaBasica.get(i).get(0).nombre.equals("ADICTV")) {
                        for (int j = 0; j < decos.size(); j++) {
                            ProductoAMG prod = new ProductoAMG();
                            prod.nombre = "DECO";
                            prod.producto = "DECO";
                            prod.plan = decos.get(j).get(0);
                            prod.cargoBasico = Double.parseDouble(decos.get(j).get(1));
                            prod.totalCargoBasico = Double.parseDouble(decos.get(j).get(1));
                            prod.totalProducto = Double.parseDouble(decos.get(j).get(1));
                            ac.productosOfertaBasica.get(i).add(prod);
                            nombsBasica.add(decos.get(j).get(0));
                        }
                    }
                }
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    nombsBasica);
            spnAdicionalesBasica.setAdapter(adp);
        }
    }

    private RelativeLayout renderItemsOfertasRetencion(ArrayList<ProductoAMG> planes) {

        final ItemAmigoCuentas iac = new ItemAmigoCuentas(this);
        iac.productos = planes;

        LayoutInflater li = LayoutInflater.from(iac.getContext());
        RelativeLayout componente = (RelativeLayout) li.inflate(R.layout.itemamigocuentascb, null);

        ImageView imgProducto = (ImageView) componente.findViewById(R.id.imgProducto);
        Spinner spnProducto = (Spinner) componente.findViewById(R.id.spnProducto);
        final TextView lblTipoProducto = (TextView) componente.findViewById(R.id.lblTipoProducto);
        final TextView lblPlan = (TextView) componente.findViewById(R.id.lblPlan);
        final TextView lblPlanFacturacion = (TextView) componente.findViewById(R.id.lblPlanFacturacion);
        final TextView lblVelocidad = (TextView) componente.findViewById(R.id.lblVelocidad);
        final TextView lblVelocidadValor = (TextView) componente.findViewById(R.id.lblVelocidadValor);
        final TextView lblCargoBasico = (TextView) componente.findViewById(R.id.lblCargoBasico);
        final TextView lblCargoBasicoContenido = (TextView) componente.findViewById(R.id.lblCargoBasicoContenido);
        final TextView lblCargoAdicionales = (TextView) componente.findViewById(R.id.lblCargoAdicionales);
        final TextView lblCargoAdicionalesContenido = (TextView) componente
                .findViewById(R.id.lblCargoAdicionalesContenido);
        final TextView lblCargoFinanciero = (TextView) componente.findViewById(R.id.lblCargoFinanciero);
        final TextView lblCargoFinancieroContenido = (TextView) componente
                .findViewById(R.id.lblCargoFinancieroContenido);
        final TextView lblTotal = (TextView) componente.findViewById(R.id.lblTotal);
        final TextView lblTotalContenido = (TextView) componente.findViewById(R.id.lblTotalContenido);

        if (iac.productos.size() > 0) {
            if (iac.productos.get(0).producto.equalsIgnoreCase("TO")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgto));
                lblTipoProducto.setText("Telefonia");
                txtDescuentoRetencionTO = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("TV")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Television");
                txtDescuentoRetencionTV = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("BA")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgba));
                lblTipoProducto.setText("Internet");
                txtDescuentoRetencionBA = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("ADICHD")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Television HD");
                txtDescuentoRetencionHD = (TextView) componente.findViewById(R.id.txtDescuento);
            } else if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                imgProducto.setImageDrawable(res.getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText("Canales Adicionales");
            }

            lblPlan.setVisibility(View.GONE);
            lblVelocidad.setVisibility(View.GONE);
            lblVelocidadValor.setVisibility(View.GONE);
            lblCargoAdicionales.setVisibility(View.GONE);
            lblCargoAdicionalesContenido.setVisibility(View.GONE);

            lblCargoFinanciero.setText("IVA:");

            ArrayList<String> noms = new ArrayList<String>();
            noms.add("-");

            for (int i = 0; i < iac.productos.size(); i++) {
                noms.add(iac.productos.get(i).plan);
            }

            if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                nombsRetencion = noms;
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, noms);
            spnProducto.setAdapter(adp);
            spnProducto.setVisibility(View.VISIBLE);

            spnProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
					/*
					 * lblPlanFacturacion.setText(iac.productos.get(position
					 * ).planFactura);
					 *
					 * lblCargoBasicoContenido.setText("$ "+String.valueOf (new
					 * DecimalFormat("#,###.##").format(iac.productos
					 * .get(position).cargoBasico)));
					 * lblCargoFinancieroContenido .setText("$ "
					 * +String.valueOf(new DecimalFormat("#,###.##"
					 * ).format(iac.productos.get (position).obtenerIVA())));
					 * lblTotalContenido.setText("$ "+String.valueOf(new
					 * DecimalFormat ("#,###.##").format(iac.productos.get
					 * (position).totalProducto)));
					 */

                    boolean band = false;
                    if (position == iac.productos.size()) {
                        position = position - 1;
                        band = true;
                    }

                    if (iac.productos.get(position).producto.equals("TO")) {
                        System.out.println("TO");
                        if (position != 0 || band) {
                            if (!band) {
                                position = position - 1;
                            }
                            lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                            lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                            lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                            lblTotalContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                            paqueteRetencion[0] = iac.productos.get(position).cargoBasico;
                            financierosRetencion[0] = iac.productos.get(position).calcularCargoFinancieros();
                            productosRetencion[0] = iac.productos.get(position).plan;
                            ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_RETENCION);

                            actualizarPaqueteCanalesRetencion();
                            actualizarTotalRetencion();
                            mostrarTipoPlan();

                            ac.mostrarDescuentos(AmigoCuentas.OFERTA_RETENCION);

                            mostrarDescuentosRetencion();
                        } else {
                            lblPlanFacturacion.setText("");

                            lblCargoBasicoContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblCargoFinancieroContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                        }

                    } else if (iac.productos.get(position).producto.equals("TV")) {
                        System.out.println("TV");
                        if (position != 0 || band) {
                            if (!band) {
                                position = position - 1;
                            }
                            lblPlanFacturacion.setText(iac.productos.get(position).planFactura);
                            planTvRetencion = iac.productos.get(position).plan;
                            lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                            lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                            lblTotalContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                            paqueteRetencion[1] = iac.productos.get(position).cargoBasico;
                            financierosRetencion[1] = iac.productos.get(position).calcularCargoFinancieros();
                            productosRetencion[1] = iac.productos.get(position).plan;
                            ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_RETENCION);

                            actualizarPaqueteCanalesRetencion();
                            actualizarTotalRetencion();
                            mostrarTipoPlan();

                            ac.mostrarDescuentos(AmigoCuentas.OFERTA_RETENCION);

                            mostrarDescuentosRetencion();

                            adicionarDecosRetencion();
                        } else {
                            lblPlanFacturacion.setText("");

                            lblCargoBasicoContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblCargoFinancieroContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                        }

                    } else if (iac.productos.get(position).producto.equals("BA")) {
                        System.out.println("BA");
                        if (position != 0 || band) {
                            if (!band) {
                                position = position - 1;
                            }
                            lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                            lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                            lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                            lblTotalContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                            paqueteRetencion[2] = iac.productos.get(position).cargoBasico;
                            financierosRetencion[2] = iac.productos.get(position).calcularCargoFinancieros();
                            productosRetencion[2] = iac.productos.get(position).plan;
                            ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_RETENCION);

                            actualizarPaqueteCanalesRetencion();
                            actualizarTotalRetencion();
                            mostrarTipoPlan();

                            ac.mostrarDescuentos(AmigoCuentas.OFERTA_RETENCION);

                            mostrarDescuentosRetencion();
                        } else {
                            lblPlanFacturacion.setText("");

                            lblCargoBasicoContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblCargoFinancieroContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                        }

                    } else if (iac.productos.get(position).producto.equals("ADICHD")) {
                        System.out.println("ADICHD");
                        if (position != 0 || band) {
                            if (!band) {
                                position = position - 1;
                            }
                            lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                            lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                            lblCargoFinancieroContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).obtenerIVA())));
                            lblTotalContenido.setText("$ " + String.valueOf(
                                    new DecimalFormat("#,###.##").format(iac.productos.get(position).totalProducto)));
                            paqueteRetencion[3] = iac.productos.get(position).cargoBasico;
                            financierosRetencion[3] = iac.productos.get(position).calcularCargoFinancieros();
                            productosRetencion[3] = iac.productos.get(position).plan;
                            ac.agrearProducto(iac.productos.get(position), AmigoCuentas.OFERTA_RETENCION);

                            actualizarPaqueteCanalesRetencion();
                            actualizarTotalRetencion();
                            mostrarTipoPlan();
                        } else {
                            lblPlanFacturacion.setText("");

                            lblCargoBasicoContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblCargoFinancieroContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                        }

                        if (adicionalesRetencion.size() > 0) {
                            adicionalesRetencion.remove(auxAdicionalRetencionHD);
                        }
                        auxAdicionalRetencionHD = iac.productos.get(position).plan;
                        adicionalesRetencion.add(iac.productos.get(position).plan);
                        adicionalesRetencionValor.add(iac.productos.get(position).totalProducto);
                        mostrarDescuentosAdicionalesRetencion("ADICHD");
                    } else if (iac.productos.get(position).producto.equals("ADICTV")) {
                        System.out.println("ADICTV");
                        if (position != 0 || band) {
                            if (!band) {
                                position = position - 1;
                            }
                            if (!adicionalesRetencion.contains(iac.productos.get(position).plan)) {
                                System.out.println("No Contiene");
                                adicionalesRetencion.add(iac.productos.get(position).plan);
                                adicionalesRetencionValor.add(iac.productos.get(position).totalProducto);
                                lblPlanFacturacion.setText(iac.productos.get(position).planFactura);

                                lblCargoBasicoContenido.setText("$ " + String.valueOf(
                                        new DecimalFormat("#,###.##").format(iac.productos.get(position).cargoBasico)));
                                lblCargoFinancieroContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).obtenerIVA())));
                                lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).totalProducto)));

                                ItemAmigocuentasAdicionalDesc idaca = new ItemAmigocuentasAdicionalDesc(
                                        getApplication());

                                LayoutInflater lidc = LayoutInflater.from(idaca.getContext());
                                LinearLayout componenteDescc = (LinearLayout) lidc
                                        .inflate(R.layout.itemamigocuentasaddesc, null);

                                ImageView imgEliminar = (ImageView) componenteDescc.findViewById(R.id.imgEliminar);
                                TextView txtNombreAdicionalC = (TextView) componenteDescc
                                        .findViewById(R.id.txtNombreAdicional);
                                TextView txtValor = (TextView) componenteDescc.findViewById(R.id.txtValorAdicional);

                                imgEliminar.setVisibility(View.VISIBLE);
                                txtNombreAdicionalC.setText(iac.productos.get(position).plan);
                                txtNombreAdicionalC.setTextColor(res.getColor(R.color.purple));
                                // txtNombreAdicionalC.setTypeface(null,
                                // Typeface.BOLD);
                                txtNombreAdicionalC.setTextSize(16);
                                txtValor.setText("$ " + String.valueOf(new DecimalFormat("#,###.##")
                                        .format(iac.productos.get(position).totalProducto)));
                                txtValor.setTextColor(res.getColor(R.color.purple));
                                // txtValor.setTypeface(null,
                                // Typeface.BOLD);
                                txtValor.setTextSize(16);

                                imgEliminar.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated
                                        // method stub
                                        // System.out.println("Se eliminara "+);
                                        RelativeLayout prueba = (RelativeLayout) v.getParent();
                                        TextView txtAdicional = (TextView) prueba.findViewById(R.id.txtNombreAdicional);
                                        for (int i = 0; i < adicionalesRetencion.size(); i++) {
                                            if (adicionalesRetencion.get(i).equals(txtAdicional.getText().toString())) {
                                                adicionalesRetencion.remove(i);
                                                adicionalesRetencionValor.remove(i);
                                            }
                                        }
                                        System.out.println(txtAdicional.getText());
                                        llyAdicinalesCanRetencion.removeView(prueba);
                                        // System.out.println(llyAdicinalesCanRetencion.get);

                                        double abt = 0;
                                        for (int i = 0; i < adicionalesRetencionValor.size(); i++) {
                                            abt += adicionalesRetencionValor.get(i);
                                        }

                                        txtValorAdicionalRetencionTotal.setText(
                                                "$ " + String.valueOf(new DecimalFormat("#,###.##").format(abt)));

                                        actualizarPaqueteCanalesRetencion();
                                        actualizarTotalRetencion();

                                        mostrarDescuentosAdicionalesRetencion("ADICTV");
                                    }
                                });

                                if (llyAdicinalesCanRetencion.getChildCount() > 0) {
                                    llyAdicinalesCanRetencion.removeView(llyAdicinalesCanRetencion
                                            .getChildAt(llyAdicinalesCanRetencion.getChildCount() - 1));
                                }
                                llyAdicinalesCanRetencion.addView(componenteDescc);

                                ItemAmigocuentasAdicionalDesc idacaT = new ItemAmigocuentasAdicionalDesc(
                                        getApplication());
                                LayoutInflater lidct = LayoutInflater.from(idacaT.getContext());
                                LinearLayout componenteDescTotal = (LinearLayout) lidct
                                        .inflate(R.layout.itemamigocuentasaddesc, null);

                                TextView txtNombreAdicionalTotal = (TextView) componenteDescTotal
                                        .findViewById(R.id.txtNombreAdicional);
                                txtValorAdicionalRetencionTotal = (TextView) componenteDescTotal
                                        .findViewById(R.id.txtValorAdicional);

                                double abt = 0;
                                for (int i = 0; i < adicionalesRetencionValor.size(); i++) {
                                    abt += adicionalesRetencionValor.get(i);
                                }

                                txtNombreAdicionalTotal.setText("Total");
                                txtNombreAdicionalTotal.setTextColor(res.getColor(R.color.purple));
                                txtNombreAdicionalTotal.setTypeface(null, Typeface.BOLD);
                                txtNombreAdicionalTotal.setTextSize(16);
                                txtValorAdicionalRetencionTotal
                                        .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(abt)));
                                txtValorAdicionalRetencionTotal.setTextColor(res.getColor(R.color.purple));
                                txtValorAdicionalRetencionTotal.setTypeface(null, Typeface.BOLD);
                                txtValorAdicionalRetencionTotal.setTextSize(16);

                                llyAdicinalesCanRetencion.addView(componenteDescTotal);

                                actualizarPaqueteCanalesRetencion();
                                actualizarTotalRetencion();

                                mostrarDescuentosAdicionalesRetencion("ADICTV");
                            } else {
                                System.out.println("Contiene");
                            }

                        } else {
                            lblPlanFacturacion.setText("");

                            lblCargoBasicoContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblCargoFinancieroContenido
                                    .setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                            lblTotalContenido.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(0)));
                        }
                    }

                    calcularValorPaqueteRetencion();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // lblMensaje.setText("");
                }
            });

			/*
			 * lblPlan.setText(iac.producto.plan);
			 * lblPlanFacturacion.setText(iac.producto.identificador);
			 * if(!iac.producto.velocidad.equals("")){
			 * lblVelocidadValor.setText(iac.producto.velocidad); }else{
			 * lblVelocidad.setVisibility(View.GONE);
			 * lblVelocidadValor.setVisibility(View.GONE); }
			 *
			 * lblCargoBasicoContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat("#,###.##").format(iac.producto.cargoBasico)));
			 * lblCargoAdicionalesContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat
			 * ("#,###.##").format(iac.producto.calcularCargoAdicionales())));
			 * lblCargoFinancieroContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat
			 * ("#,###.##").format(iac.producto.calcularCargoFinancieros())));
			 * lblTotalContenido.setText("$ "+String.valueOf(new
			 * DecimalFormat("#,###.##").format(iac.producto.totalProducto)));
			 */

            if (iac.productos.get(0).producto.equalsIgnoreCase("ADICTV")) {
                spnAdicionalesRetencion = spnProducto;
            }
        }

        return componente;

    }

    private void adicionarDecosRetencion() {
        if (spnAdicionalesRetencion != null && nombsRetencion != null) {
            for (int i = 0; i < ac.productosOfertaRetencion.size(); i++) {
                for (int j = 0; j < ac.productosOfertaRetencion.get(i).size(); j++) {
                    if (ac.productosOfertaRetencion.get(i).get(j).nombre.equals("DECO")) {
                        ac.productosOfertaRetencion.get(i).remove(ac.productosOfertaRetencion.get(i).get(j));
                    }
                }
            }

            ArrayList<ArrayList<String>> decos = UtilidadesTarificador.llenarAdicionales(planTvRetencion,
                    cliente.getDepartamento(), cliente.getEstrato());
            if (decos != null) {
                for (int i = 0; i < ac.productosOfertaRetencion.size(); i++) {
                    if (ac.productosOfertaRetencion.get(i).get(0).nombre.equals("ADICTV")) {
                        for (int j = 0; j < decos.size(); j++) {
                            ProductoAMG prod = new ProductoAMG();
                            prod.nombre = "DECO";
                            prod.producto = "DECO";
                            prod.plan = decos.get(j).get(0);
                            prod.cargoBasico = Double.parseDouble(decos.get(j).get(1));
                            prod.totalCargoBasico = Double.parseDouble(decos.get(j).get(1));
                            prod.totalProducto = Double.parseDouble(decos.get(j).get(1));
                            ac.productosOfertaRetencion.get(i).add(prod);
                            nombsRetencion.add(decos.get(j).get(0));
                        }
                    }
                }
            }

            ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    nombsRetencion);
            spnAdicionalesRetencion.setAdapter(adp);
        }
    }

    public void calcularValorPaqueteBasico() {
        double total = 0;
        for (int i = 0; i < paqueteBasico.length; i++) {
            total += paqueteBasico[i];
        }
        txtValorPaqueteBasico.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(total)));
    }

    public void calcularValorPaqueteSuper() {
        double total = 0;
        for (int i = 0; i < paqueteSuper.length; i++) {
            total += paqueteSuper[i];
        }
        txtValorPaqueteSuper.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(total)));
    }

    public void calcularValorPaqueteRetencion() {
        double total = 0;
        for (int i = 0; i < paqueteRetencion.length; i++) {
            total += paqueteRetencion[i];
        }
        txtValorPaqueteRetencion.setText("$ " + String.valueOf(new DecimalFormat("#,###.##").format(total)));
    }

    public void mostrarTipoPlan() {
        System.out.println("Tipo plan");
        for (int i = 0; i < tipoProducto.length; i++) {
            if (tipoProducto[i] != null) {
                System.out.println(tipoProducto[i]);
            }

        }
    }

    public void mostrarDescuentosbasico() {
        for (int j = 0; j < ac.productosBasica.size(); j++) {
            if (ac.productosBasica.get(j).producto.equals("TO")) {
                if (ac.productosBasica.get(j).descuento != null && !ac.productosBasica.get(j).descuento.equals("0")) {
                    if (ac.productosBasica.get(j).duracion != null && !ac.productosBasica.get(j).duracion.equals("0")) {
                        if (ac.productosBasica.get(j).duracion.equals("1")) {
                            txtDescuentoBasicaTO.setText(ac.productosBasica.get(j).duracion + " Mes "
                                    + ac.productosBasica.get(j).descuento + "%");
                        } else {
                            txtDescuentoBasicaTO.setText(ac.productosBasica.get(j).duracion + " Meses "
                                    + ac.productosBasica.get(j).descuento + "%");
                        }
                    }
                }
            }

            if (ac.productosBasica.get(j).producto.equals("TV")) {
                if (ac.productosBasica.get(j).descuento != null && !ac.productosBasica.get(j).descuento.equals("0")) {
                    if (ac.productosBasica.get(j).duracion != null && !ac.productosBasica.get(j).duracion.equals("0")) {
                        if (ac.productosBasica.get(j).duracion.equals("1")) {
                            txtDescuentoBasicaTV.setText(ac.productosBasica.get(j).duracion + " Mes "
                                    + ac.productosBasica.get(j).descuento + "%");
                        } else {
                            txtDescuentoBasicaTV.setText(ac.productosBasica.get(j).duracion + " Meses "
                                    + ac.productosBasica.get(j).descuento + "%");
                        }
                    }
                }
            }

            if (ac.productosBasica.get(j).producto.equals("BA")) {
                if (ac.productosBasica.get(j).descuento != null && !ac.productosBasica.get(j).descuento.equals("0")) {
                    if (ac.productosBasica.get(j).duracion != null && !ac.productosBasica.get(j).duracion.equals("0")) {
                        if (ac.productosBasica.get(j).duracion.equals("1")) {
                            txtDescuentoBasicaBA.setText(ac.productosBasica.get(j).duracion + " Mes "
                                    + ac.productosBasica.get(j).descuento + "%");
                        } else {
                            txtDescuentoBasicaBA.setText(ac.productosBasica.get(j).duracion + " Meses "
                                    + ac.productosBasica.get(j).descuento + "%");
                        }
                    }
                }
            }
        }
    }

    public void mostrarDescuentosSuper() {
        for (int j = 0; j < ac.productosSuper.size(); j++) {
            if (ac.productosSuper.get(j).producto.equals("TO")) {
                if (ac.productosSuper.get(j).descuento != null && !ac.productosSuper.get(j).descuento.equals("0")) {
                    if (ac.productosSuper.get(j).duracion != null && !ac.productosSuper.get(j).duracion.equals("0")) {
                        if (ac.productosSuper.get(j).duracion.equals("1")) {
                            txtDescuentoSuperTO.setText(ac.productosSuper.get(j).duracion + " Mes "
                                    + ac.productosSuper.get(j).descuento + "%");
                        } else {
                            txtDescuentoSuperTO.setText(ac.productosSuper.get(j).duracion + " Meses "
                                    + ac.productosSuper.get(j).descuento + "%");
                        }
                    }
                }
            }

            if (ac.productosSuper.get(j).producto.equals("TV")) {
                if (ac.productosSuper.get(j).descuento != null && !ac.productosSuper.get(j).descuento.equals("0")) {
                    if (ac.productosSuper.get(j).duracion != null && !ac.productosSuper.get(j).duracion.equals("0")) {
                        if (ac.productosSuper.get(j).duracion.equals("1")) {
                            txtDescuentoSuperTV.setText(ac.productosSuper.get(j).duracion + " Mes "
                                    + ac.productosSuper.get(j).descuento + "%");
                        } else {
                            txtDescuentoSuperTV.setText(ac.productosSuper.get(j).duracion + " Meses "
                                    + ac.productosSuper.get(j).descuento + "%");
                        }
                    }
                }
            }

            if (ac.productosSuper.get(j).producto.equals("BA")) {
                if (ac.productosSuper.get(j).descuento != null && !ac.productosSuper.get(j).descuento.equals("0")) {
                    if (ac.productosSuper.get(j).duracion != null && !ac.productosSuper.get(j).duracion.equals("0")) {
                        if (ac.productosSuper.get(j).duracion.equals("1")) {
                            txtDescuentoSuperBA.setText(ac.productosSuper.get(j).duracion + " Mes "
                                    + ac.productosSuper.get(j).descuento + "%");
                        } else {
                            txtDescuentoSuperBA.setText(ac.productosSuper.get(j).duracion + " Meses "
                                    + ac.productosSuper.get(j).descuento + "%");
                        }
                    }
                }
            }
        }
    }

    public void mostrarDescuentosRetencion() {
        for (int j = 0; j < ac.productosRetencion.size(); j++) {
            if (ac.productosRetencion.get(j).producto.equals("TO")) {
                if (ac.productosRetencion.get(j).descuento != null
                        && !ac.productosRetencion.get(j).descuento.equals("0")) {
                    if (ac.productosRetencion.get(j).duracion != null
                            && !ac.productosRetencion.get(j).duracion.equals("0")) {
                        if (ac.productosRetencion.get(j).duracion.equals("1")) {
                            txtDescuentoRetencionTO.setText(ac.productosRetencion.get(j).duracion + " Mes "
                                    + ac.productosRetencion.get(j).descuento + "%");
                        } else {
                            txtDescuentoRetencionTO.setText(ac.productosRetencion.get(j).duracion + " Meses "
                                    + ac.productosRetencion.get(j).descuento + "%");
                        }
                    }
                }
            }

            if (ac.productosRetencion.get(j).producto.equals("TV")) {
                if (ac.productosRetencion.get(j).descuento != null
                        && !ac.productosRetencion.get(j).descuento.equals("0")) {
                    if (ac.productosRetencion.get(j).duracion != null
                            && !ac.productosRetencion.get(j).duracion.equals("0")) {
                        if (ac.productosRetencion.get(j).duracion.equals("1")) {
                            txtDescuentoRetencionTV.setText(ac.productosRetencion.get(j).duracion + " Mes "
                                    + ac.productosRetencion.get(j).descuento + "%");
                        } else {
                            txtDescuentoRetencionTV.setText(ac.productosRetencion.get(j).duracion + " Meses "
                                    + ac.productosRetencion.get(j).descuento + "%");
                        }
                    }
                }
            }

            if (ac.productosRetencion.get(j).producto.equals("BA")) {
                if (ac.productosRetencion.get(j).descuento != null
                        && !ac.productosRetencion.get(j).descuento.equals("0")) {
                    if (ac.productosRetencion.get(j).duracion != null
                            && !ac.productosRetencion.get(j).duracion.equals("0")) {
                        if (ac.productosRetencion.get(j).duracion.equals("1")) {
                            txtDescuentoRetencionBA.setText(ac.productosRetencion.get(j).duracion + " Mes "
                                    + ac.productosRetencion.get(j).descuento + "%");
                        } else {
                            txtDescuentoRetencionBA.setText(ac.productosRetencion.get(j).duracion + " Meses "
                                    + ac.productosRetencion.get(j).descuento + "%");
                        }
                    }
                }
            }
        }
    }

    public void mostrarDescuentosAdicionalesBasica(String tipo) {

        txtPromocionesAdicionalesBasica.setText("");

        if (adicionalesBasica.size() > 0) {
            String[][] adicionales = new String[adicionalesBasica.size()][2];
            for (int i = 0; i < adicionalesBasica.size(); i++) {
                adicionales[i][0] = adicionalesBasica.get(i);
                adicionales[i][1] = String.valueOf(adicionalesBasicaValor.get(i));
            }

            ArrayList<ArrayList<String>> result = Tarificador.consultarDescuentos(adicionales, cliente.getCiudad(),
                    false, ac.productosBasica.size());
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    adicionalesBasicaDescuentos.add(new ItemPromocionesAdicionales(result.get(i).get(0),
                            result.get(i).get(1), "TV", result.get(i).get(2)));
                    if (Integer.valueOf(result.get(i).get(2)) == 1) {
                        if (tipo.equals("ADICTV")) {
                            if (!result.get(i).get(0).equals(auxAdicionalBasicaHD)) {
                                txtPromocionesAdicionalesBasica
                                        .setText(txtPromocionesAdicionalesBasica.getText() + result.get(i).get(0) + " "
                                                + result.get(i).get(2) + " Mes " + result.get(i).get(1) + "% \n");
                            }
                        } else if (tipo.equals("ADICHD")) {
                            txtDescuentoBasicaHD
                                    .setText(result.get(i).get(2) + " Mes " + result.get(i).get(1) + "% \n");
                        }

                    } else if (Integer.valueOf(result.get(i).get(2)) > 1) {
                        if (tipo.equals("ADICTV")) {
                            if (!result.get(i).get(0).equals(auxAdicionalBasicaHD)) {
                                txtPromocionesAdicionalesBasica
                                        .setText(txtPromocionesAdicionalesBasica.getText() + result.get(i).get(0) + " "
                                                + result.get(i).get(2) + " Meses " + result.get(i).get(1) + "% \n");
                            }
                        } else if (tipo.equals("ADICHD")) {
                            txtDescuentoBasicaHD
                                    .setText(result.get(i).get(2) + " Meses " + result.get(i).get(1) + "% \n");
                        }

                    }
                }
            }

        }

    }

    public void mostrarDescuentosAdicionalesSuper(String tipo) {

        txtPromocionesAdicionalesSuper.setText("");
        adicionalesSuperDescuentos.clear();
        if (adicionalesSuper.size() > 0) {
            String[][] adicionales = new String[adicionalesSuper.size()][2];
            for (int i = 0; i < adicionalesSuper.size(); i++) {
                adicionales[i][0] = adicionalesSuper.get(i);
                adicionales[i][1] = String.valueOf(adicionalesSuperValor.get(i));
            }

            ArrayList<ArrayList<String>> result = Tarificador.consultarDescuentos(adicionales, cliente.getCiudad(),
                    false, ac.productosSuper.size());
            if (result != null) {

                for (int i = 0; i < result.size(); i++) {
                    adicionalesSuperDescuentos.add(new ItemPromocionesAdicionales(result.get(i).get(0),
                            result.get(i).get(1), "TV", result.get(i).get(2)));
                    if (Integer.valueOf(result.get(i).get(2)) == 1) {
                        if (tipo.equals("ADICTV")) {
                            if (!result.get(i).get(0).equals(auxAdicionalSuperHD)) {
                                txtPromocionesAdicionalesSuper
                                        .setText(txtPromocionesAdicionalesSuper.getText() + result.get(i).get(0) + " "
                                                + result.get(i).get(2) + " Mes " + result.get(i).get(1) + "% \n");
                            }
                        } else if (tipo.equals("ADICHD")) {
                            txtDescuentoSuperHD.setText(result.get(i).get(2) + " Mes " + result.get(i).get(1) + "% \n");
                        }

                    } else if (Integer.valueOf(result.get(i).get(2)) > 1) {
                        if (tipo.equals("ADICTV")) {
                            if (!result.get(i).get(0).equals(auxAdicionalSuperHD)) {
                                txtPromocionesAdicionalesSuper
                                        .setText(txtPromocionesAdicionalesSuper.getText() + result.get(i).get(0) + " "
                                                + result.get(i).get(2) + " Meses " + result.get(i).get(1) + "% \n");
                            }
                        } else if (tipo.equals("ADICHD")) {
                            txtDescuentoSuperHD
                                    .setText(result.get(i).get(2) + " Meses " + result.get(i).get(1) + "% \n");
                        }

                    }
                }
            }
        }

    }

    public void mostrarDescuentosAdicionalesRetencion(String tipo) {
        txtPromocionesAdicionalesRetencion.setText("");
        if (adicionalesRetencion.size() > 0) {
            String[][] adicionales = new String[adicionalesRetencion.size()][2];
            for (int i = 0; i < adicionalesRetencion.size(); i++) {
                adicionales[i][0] = adicionalesRetencion.get(i);
                adicionales[i][1] = String.valueOf(adicionalesRetencionValor.get(i));
            }

            ArrayList<ArrayList<String>> result = Tarificador.consultarDescuentos(adicionales, cliente.getCiudad(),
                    false, ac.productosRetencion.size());
            if (result != null) {

                for (int i = 0; i < result.size(); i++) {
                    adicionalesRetencionDescuentos.add(new ItemPromocionesAdicionales(result.get(i).get(0),
                            result.get(i).get(1), "TV", result.get(i).get(2)));
                    if (Integer.valueOf(result.get(i).get(2)) == 1) {
                        if (tipo.equals("ADICTV")) {
                            if (!result.get(i).get(0).equals(auxAdicionalRetencionHD)) {
                                txtPromocionesAdicionalesRetencion
                                        .setText(txtPromocionesAdicionalesRetencion.getText() + result.get(i).get(0)
                                                + " " + result.get(i).get(2) + " Mes " + result.get(i).get(1) + "% \n");
                            }
                        } else if (tipo.equals("ADICHD")) {
                            txtDescuentoRetencionHD
                                    .setText(result.get(i).get(2) + " Mes " + result.get(i).get(1) + "% \n");
                        }

                    } else if (Integer.valueOf(result.get(i).get(2)) > 1) {
                        if (tipo.equals("ADICTV")) {
                            if (!result.get(i).get(0).equals(auxAdicionalRetencionHD)) {
                                txtPromocionesAdicionalesRetencion.setText(
                                        txtPromocionesAdicionalesRetencion.getText() + result.get(i).get(0) + " "
                                                + result.get(i).get(2) + " Meses " + result.get(i).get(1) + "% \n");
                            }
                        } else if (tipo.equals("ADICHD")) {
                            txtDescuentoRetencionHD
                                    .setText(result.get(i).get(2) + " Meses " + result.get(i).get(1) + "% \n");
                        }

                    }
                }
            }

        }

    }

    public void seleccionSuper(View v) {
        System.out.println("Se selecciono la oferta de Super");
        System.out.println(ac.productosSuper.size());

        if (ac.productosSuper.size() > 0) {
            Cotizacion cotizacion = new Cotizacion();
            cotizacion.medioIngreso = "Cross";
            cotizacion.setEstrato(cliente.getEstrato());

            String descTo = "Sin Promocion", durDescTo = "N/A", descTv = "Sin Promocion", durDescTv = "N/A",
                    descBa = "Sin Promocion", durDescBa = "N/A";
            String planTo = "--Seleccione Tipo--", telefonia = "--Seleccione Producto--",
                    planTv = "--Seleccione Tipo--", television = "--Seleccione Producto--",
                    planBa = "--Seleccione Tipo--", internet = "--Seleccione Producto--";

            int tamAdic = adicionalesSuper.size(), pos = 0;

            for (int i = 0; i < ac.productosSuper.size(); i++) {
                if (ac.productosSuper.get(i).producto.equals("TO")) {
                    planTo = ac.productosSuper.get(i).nuevoCambioExistente;
                    telefonia = ac.productosSuper.get(i).plan;
                } else if (ac.productosSuper.get(i).producto.equals("TV")) {
                    planTv = ac.productosSuper.get(i).nuevoCambioExistente;
                    television = ac.productosSuper.get(i).plan;
                } else if (ac.productosSuper.get(i).producto.equals("BA")) {
                    planBa = ac.productosSuper.get(i).nuevoCambioExistente;
                    internet = ac.productosSuper.get(i).plan;
                }
            }

            ArrayList<ItemPromocionesAdicionales> adic = new ArrayList<ItemPromocionesAdicionales>();

            String[][] adicionales = new String[tamAdic][2];
            double totalAdicionales = 0;
            for (int i = 0; i < adicionalesSuper.size(); i++) {
                adicionales[i][0] = Utilidades.Quitar_Solo_Tildes(adicionalesSuper.get(i));
                adicionales[i][1] = String.valueOf(adicionalesSuperValor.get(i));
                totalAdicionales += adicionalesSuperValor.get(i);
                adic.add(new ItemPromocionesAdicionales(adicionales[i][0], "", "TV", ""));
            }

            System.out.println(adicionalesSuper.size());
            if (tamAdic > adicionalesSuper.size()) {
                adicionales[adicionales.length - 1][0] = Utilidades.Quitar_Solo_Tildes(ac.productosSuper.get(pos).plan);
                adicionales[adicionales.length - 1][1] = String.valueOf(ac.productosSuper.get(pos).cargoBasico);
                totalAdicionales += ac.productosSuper.get(pos).cargoBasico;
                adic.add(new ItemPromocionesAdicionales(adicionales[adicionales.length - 1][0], "", "TV", ""));
            }

            cotizacion.setAdicionales(adicionales);
            cotizacion.setTotalAdicionales(String.valueOf(totalAdicionales));
            cotizacion.setItemPromocionesAdicionales(adicionalesSuperDescuentos);

            ArrayList<ArrayList<String>> Descuentos = Tarificador.Descuentos(planTo, telefonia, planTv, television,
                    planBa, internet);

            if (Descuentos != null) {
                for (int i = 0; i < Descuentos.size(); i++) {
                    if (Descuentos.get(i).get(0).equalsIgnoreCase("TO")) {
                        descTo = Descuentos.get(i).get(1);
                        durDescTo = Descuentos.get(i).get(2);
                    } else if (Descuentos.get(i).get(0).equalsIgnoreCase("TV")) {
                        descTv = Descuentos.get(i).get(1);
                        durDescTv = Descuentos.get(i).get(2);
                    } else if (Descuentos.get(i).get(0).equalsIgnoreCase("BA")) {
                        descBa = Descuentos.get(i).get(1);
                        durDescBa = Descuentos.get(i).get(2);
                    }

                }
            }

            for (int i = 0; i < ac.productosSuper.size(); i++) {
                if (ac.productosSuper.get(i).producto.equals("TO")) {
                    cotizacion.Telefonia(ac.productosSuper.get(i).nuevoCambioExistente, ac.productosSuper.get(i).plan,
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto), descTo, durDescTo,
                            ac.productosSuper.get(i).identificador, toPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionTo_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosSuper.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionTo_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosSuper.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.toIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("TO")) {
                            if (cotizacion.getTipoTo().equals("C")) {
                                cotizacion.setTipoCotizacionTo("0");
                                cotizacion.toIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoTo().equals("E")) {
                                cotizacion.setTipoCotizacionTo("3");
                                cotizacion.toIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionTo() == null) {
                                cotizacion.setTipoCotizacionTo("1");
                            }

                        }

                    }
                } else if (ac.productosSuper.get(i).producto.equals("TV")) {
                    cotizacion.Television(ac.productosSuper.get(i).nuevoCambioExistente, ac.productosSuper.get(i).plan,
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto), descTv, durDescTv,
                            ac.productosSuper.get(i).identificador, tvPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionTv_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosSuper.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionTv_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosSuper.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.tvIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("TV")) {
                            if (cotizacion.getTipoTv().equals("C")) {
                                cotizacion.setTipoCotizacionTv("0");
                                cotizacion.tvIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoTv().equals("E")) {
                                cotizacion.setTipoCotizacionTv("3");
                                cotizacion.tvIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionTv() == null) {
                                cotizacion.setTipoCotizacionTv("1");
                            }

                        }
                    }
                } else if (ac.productosSuper.get(i).producto.equals("BA")) {
                    cotizacion.Internet(ac.productosSuper.get(i).nuevoCambioExistente, ac.productosSuper.get(i).plan,
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto),
                            String.valueOf(ac.productosSuper.get(i).totalProducto), descBa, durDescBa,
                            ac.productosSuper.get(i).identificador, baPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionBa_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosSuper.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionBa_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosSuper.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.baIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("BA")) {
                            if (cotizacion.getTipoBa().equals("C")) {
                                cotizacion.setTipoCotizacionBa("0");
                                cotizacion.baIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoBa().equals("E")) {
                                cotizacion.setTipoCotizacionBa("3");
                                cotizacion.baIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionBa() == null) {
                                cotizacion.setTipoCotizacionBa("1");
                            }

                        }
                    }
                }
            }

            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionTo_P());
            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionTv_P());
            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionBa_P());

            cotizacion.Internet_3G("-", "", "", "", "", "");
            cotizacion.Internet_4G("-", "", "", "", "", "");

            cotizacion.setAdicionales(UtilidadesTarificador.agregarAdicionalesGratis(
                    cotizacion.getPlanFacturacionTv_P(), cotizacion.getAdicionales(), cliente.getDepartamento(),
                    cliente.getEstrato(), cotizacion.getPromoTv(), cotizacion.getTiempoPromoTv()));
            cotizacion.setTotalEmp(String.valueOf(new DecimalFormat("###.##")
                    .format(valorCanalesSuper + valorPaqueteSuper + valorFinancierosSuper + valorPaqueteAdSuper)));

            if (UtilidadesTarificador.validarOferta(ac.productosSuper, cliente.getEstrato())) {
                if (cliente.isControlEstadoCuenta()) {
                    if (UtilidadesTarificador.validarAdicionalesAMC(adic)) {
                        if (!ac.suspendido) {
                            System.out.println("cliente.getTelefonoDestino() " + cliente.getTelefonoDestino());
                            if (!cliente.getTelefonoDestino().equalsIgnoreCase("")) {
                                if (validarCarteraUNE(cotizacion)) {
                                    if (validarScooring(cotizacion)) {
                                        if (Utilidades.validarCliente(cliente.getCiudad(), scooring.getAccion(),
                                                scooring.getEstadoValidador(), this)) {
                                            resultCotizador("venta", cotizacion);
                                        }
                                    } else {
                                        Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                                "Cliente con calificacion negativa, prospectar por el modulo Gestor de Visitas");
                                        dialogo.dialogo.show();
                                    }
                                } else {
                                    Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                            "Documento del cliente vs Documento de Consulta en Cuenta Une no coincide");
                                    dialogo.dialogo.show();
                                }
                            } else {
                                Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                        "Debe ingresar el telefono de IVR");
                                dialogo.dialogo.show();
                            }

                        } else {
                            Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                    "Imposible continuar, servicios suspendidos");
                            dialogo.dialogo.show();
                        }

                    } else {
                        Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                "Incompatibilidad de canales, favor revisar");
                        dialogo.dialogo.show();
                    }
                } else {
                    Toast.makeText(this,
                            "Recuerda consultar el estado de cuenta del cliente para continuar con la venta",
                            Toast.LENGTH_SHORT).show();
                    resultCotizador("estado cuenta", cotizacion);
                }

            } else {
                Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                        "Incompatibilidad de oferta, favor revisar");
                dialogo.dialogo.show();
            }
            UtilidadesTarificador.limpiar();
        }

    }

    public void seleccionBasica(View v) {
        System.out.println("Se selecciono la oferta de Basica");
        System.out.println(ac.productosBasica.size());
        if (ac.productosBasica.size() > 0) {
            Cotizacion cotizacion = new Cotizacion();
            cotizacion.medioIngreso = "Cross";
            cotizacion.setEstrato(cliente.getEstrato());

            String descTo = "Sin Promocion", durDescTo = "N/A", descTv = "Sin Promocion", durDescTv = "N/A",
                    descBa = "Sin Promocion", durDescBa = "N/A";
            String planTo = "--Seleccione Tipo--", telefonia = "--Seleccione Producto--",
                    planTv = "--Seleccione Tipo--", television = "--Seleccione Producto--",
                    planBa = "--Seleccione Tipo--", internet = "--Seleccione Producto--";

            int tamAdic = adicionalesBasica.size(), pos = 0;

            for (int i = 0; i < ac.productosBasica.size(); i++) {
                if (ac.productosBasica.get(i).producto.equals("TO")) {
                    planTo = ac.productosBasica.get(i).nuevoCambioExistente;
                    telefonia = ac.productosBasica.get(i).plan;
                } else if (ac.productosBasica.get(i).producto.equals("TV")) {
                    planTv = ac.productosBasica.get(i).nuevoCambioExistente;
                    television = ac.productosBasica.get(i).plan;
                } else if (ac.productosBasica.get(i).producto.equals("BA")) {
                    planBa = ac.productosBasica.get(i).nuevoCambioExistente;
                    internet = ac.productosBasica.get(i).plan;
                }
            }

            ArrayList<ItemPromocionesAdicionales> adic = new ArrayList<ItemPromocionesAdicionales>();

            String[][] adicionales = new String[tamAdic][2];
            double totalAdicionales = 0;
            for (int i = 0; i < adicionalesBasica.size(); i++) {
                adicionales[i][0] = Utilidades.Quitar_Solo_Tildes(adicionalesBasica.get(i));
                adicionales[i][1] = String.valueOf(adicionalesBasicaValor.get(i));
                totalAdicionales += adicionalesBasicaValor.get(i);
                adic.add(new ItemPromocionesAdicionales(adicionales[i][0], "", "TV", ""));
            }

            System.out.println(adicionalesBasica.size());
            if (tamAdic > adicionalesBasica.size()) {
                adicionales[adicionales.length - 1][0] = Utilidades
                        .Quitar_Solo_Tildes(ac.productosBasica.get(pos).plan);
                adicionales[adicionales.length - 1][1] = String.valueOf(ac.productosBasica.get(pos).cargoBasico);
                totalAdicionales += ac.productosBasica.get(pos).cargoBasico;
                adic.add(new ItemPromocionesAdicionales(adicionales[adicionales.length - 1][0], "", "TV", ""));
            }

            cotizacion.setAdicionales(adicionales);
            cotizacion.setTotalAdicionales(String.valueOf(totalAdicionales));
            cotizacion.setItemPromocionesAdicionales(adicionalesBasicaDescuentos);

            cotizacion.setTotalEmp(String.valueOf(new DecimalFormat("###.##")
                    .format(valorCanalesBasica + valorPaqueteBasica + valorFinancierosBasica + valorPaqueteAdBasica)));

            ArrayList<ArrayList<String>> Descuentos = Tarificador.Descuentos(planTo, telefonia, planTv, television,
                    planBa, internet);

            if (Descuentos != null) {
                for (int i = 0; i < Descuentos.size(); i++) {
                    if (Descuentos.get(i).get(0).equalsIgnoreCase("TO")) {
                        descTo = Descuentos.get(i).get(1);
                        durDescTo = Descuentos.get(i).get(2);
                    } else if (Descuentos.get(i).get(0).equalsIgnoreCase("TV")) {
                        descTv = Descuentos.get(i).get(1);
                        durDescTv = Descuentos.get(i).get(2);
                    } else if (Descuentos.get(i).get(0).equalsIgnoreCase("BA")) {
                        descBa = Descuentos.get(i).get(1);
                        durDescBa = Descuentos.get(i).get(2);
                    }

                }
            }

            for (int i = 0; i < ac.productosBasica.size(); i++) {
                if (ac.productosBasica.get(i).producto.equals("TO")) {
                    cotizacion.Telefonia(ac.productosBasica.get(i).nuevoCambioExistente, ac.productosBasica.get(i).plan,
                            String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto), descTo, durDescTo,
                            ac.productosBasica.get(i).identificador, toPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionTo_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosBasica.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionTo_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosBasica.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.toIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("TO")) {
                            if (cotizacion.getTipoTo().equals("C")) {
                                cotizacion.setTipoCotizacionTo("0");
                                cotizacion.toIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoTo().equals("E")) {
                                cotizacion.setTipoCotizacionTo("3");
                                cotizacion.toIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionTo() == null) {
                                cotizacion.setTipoCotizacionTo("1");
                            }

                        }

                    }
                } else if (ac.productosBasica.get(i).producto.equals("TV")) {
                    cotizacion.Television(ac.productosBasica.get(i).nuevoCambioExistente,
                            ac.productosBasica.get(i).plan, String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto), descTv, durDescTv,
                            ac.productosBasica.get(i).identificador, tvPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionTv_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosBasica.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionTv_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosBasica.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.tvIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("TV")) {
                            if (cotizacion.getTipoTv().equals("C")) {
                                cotizacion.setTipoCotizacionTv("0");
                                cotizacion.tvIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoTv().equals("E")) {
                                cotizacion.setTipoCotizacionTv("3");
                                cotizacion.tvIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionTv() == null) {
                                cotizacion.setTipoCotizacionTv("1");
                            }

                        }
                    }
                } else if (ac.productosBasica.get(i).producto.equals("BA")) {
                    cotizacion.Internet(ac.productosBasica.get(i).nuevoCambioExistente, ac.productosBasica.get(i).plan,
                            String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto),
                            String.valueOf(ac.productosBasica.get(i).totalProducto), descBa, durDescBa,
                            ac.productosBasica.get(i).identificador, baPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionBa_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosBasica.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionBa_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosBasica.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.baIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("BA")) {
                            if (cotizacion.getTipoBa().equals("C")) {
                                cotizacion.setTipoCotizacionBa("0");
                                cotizacion.baIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoBa().equals("E")) {
                                cotizacion.setTipoCotizacionBa("3");
                                cotizacion.baIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionBa() == null) {
                                cotizacion.setTipoCotizacionBa("1");
                            }

                        }

                    }
                }
            }

            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionTo_P());
            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionTv_P());
            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionBa_P());

            cotizacion.Internet_3G("-", "", "", "", "", "");
            cotizacion.Internet_4G("-", "", "", "", "", "");

            cotizacion.setAdicionales(UtilidadesTarificador.agregarAdicionalesGratis(
                    cotizacion.getPlanFacturacionTv_P(), cotizacion.getAdicionales(), cliente.getDepartamento(),
                    cliente.getEstrato(), cotizacion.getPromoTv(), cotizacion.getTiempoPromoTv()));
            cotizacion.setTotalEmp(String.valueOf(new DecimalFormat("###.##")
                    .format(valorCanalesBasica + valorPaqueteBasica + valorFinancierosBasica + valorPaqueteAdBasica)));

            if (UtilidadesTarificador.validarOferta(ac.productosBasica, cliente.getEstrato())) {
                if (cliente.isControlEstadoCuenta()) {
                    if (UtilidadesTarificador.validarAdicionalesAMC(adic)) {
                        if (!ac.suspendido) {
                            if (!cliente.getTelefonoDestino().equalsIgnoreCase("")) {
                                if (validarCarteraUNE(cotizacion)) {
                                    if (validarScooring(cotizacion)) {
                                        if (Utilidades.validarCliente(cliente.getCiudad(), scooring.getAccion(),
                                                scooring.getEstadoValidador(), this)) {
                                            resultCotizador("venta", cotizacion);
                                        }
                                    } else {
                                        Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                                "Cliente con calificacion negativa, prospectar por el modulo Gestor de Visitas");
                                        dialogo.dialogo.show();
                                    }
                                } else {
                                    Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                            "Documento del cliente vs Documento de Consulta en Cuenta Une no coincide");
                                    dialogo.dialogo.show();
                                }
                            } else {
                                Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                        "Debe ingresar el telefono de IVR");
                                dialogo.dialogo.show();
                            }
                        } else {
                            Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                    "Imposible continuar, servicios suspendidos");
                            dialogo.dialogo.show();
                        }

                    } else {
                        Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                "Incompatibilidad de canales, favor revisar");
                        dialogo.dialogo.show();
                    }
                } else {
                    Toast.makeText(this,
                            "Recuerda consultar el estado de cuenta del cliente para continuar con la venta",
                            Toast.LENGTH_SHORT).show();
                    resultCotizador("estado cuenta", cotizacion);
                }

            } else {
                Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                        "Incompatibilidad de oferta, favor revisar");
                dialogo.dialogo.show();
            }
            UtilidadesTarificador.limpiar();

        }

    }

    public void seleccionRetencion(View v) {
        System.out.println("Se selecciono la oferta de Retencion");
        System.out.println(ac.productosRetencion.size());
        if (ac.productosRetencion.size() > 0) {
            Cotizacion cotizacion = new Cotizacion();
            cotizacion.medioIngreso = "Cross";
            cotizacion.setEstrato(cliente.getEstrato());

            String descTo = "Sin Promocion", durDescTo = "N/A", descTv = "Sin Promocion", durDescTv = "N/A",
                    descBa = "Sin Promocion", durDescBa = "N/A";
            String planTo = "--Seleccione Tipo--", telefonia = "--Seleccione Producto--",
                    planTv = "--Seleccione Tipo--", television = "--Seleccione Producto--",
                    planBa = "--Seleccione Tipo--", internet = "--Seleccione Producto--";

            int tamAdic = adicionalesRetencion.size(), pos = 0;

            for (int i = 0; i < ac.productosRetencion.size(); i++) {
                if (ac.productosRetencion.get(i).producto.equals("TO")) {
                    planTo = ac.productosRetencion.get(i).nuevoCambioExistente;
                    telefonia = ac.productosRetencion.get(i).plan;
                } else if (ac.productosRetencion.get(i).producto.equals("TV")) {
                    planTv = ac.productosRetencion.get(i).nuevoCambioExistente;
                    television = ac.productosRetencion.get(i).plan;
                } else if (ac.productosRetencion.get(i).producto.equals("BA")) {
                    planBa = ac.productosRetencion.get(i).nuevoCambioExistente;
                    internet = ac.productosRetencion.get(i).plan;
                }
            }

            ArrayList<ItemPromocionesAdicionales> adic = new ArrayList<ItemPromocionesAdicionales>();

            String[][] adicionales = new String[tamAdic][2];
            double totalAdicionales = 0;
            for (int i = 0; i < adicionalesRetencion.size(); i++) {
                adicionales[i][0] = Utilidades.Quitar_Solo_Tildes(adicionalesRetencion.get(i));
                adicionales[i][1] = String.valueOf(adicionalesRetencionValor.get(i));
                totalAdicionales += adicionalesRetencionValor.get(i);
                adic.add(new ItemPromocionesAdicionales(adicionales[i][0], "", "TV", ""));
            }

            System.out.println(adicionalesRetencion.size());
            if (tamAdic > adicionalesRetencion.size()) {
                adicionales[adicionales.length - 1][0] = Utilidades
                        .Quitar_Solo_Tildes(ac.productosRetencion.get(pos).plan);
                adicionales[adicionales.length - 1][1] = String.valueOf(ac.productosRetencion.get(pos).cargoBasico);
                totalAdicionales += ac.productosRetencion.get(pos).cargoBasico;
                adic.add(new ItemPromocionesAdicionales(adicionales[adicionales.length - 1][0], "", "TV", ""));
            }

            cotizacion.setAdicionales(adicionales);
            cotizacion.setTotalAdicionales(String.valueOf(totalAdicionales));
            cotizacion.setItemPromocionesAdicionales(adicionalesRetencionDescuentos);

            ArrayList<ArrayList<String>> Descuentos = Tarificador.Descuentos(planTo, telefonia, planTv, television,
                    planBa, internet);

            if (Descuentos != null) {
                for (int i = 0; i < Descuentos.size(); i++) {
                    if (Descuentos.get(i).get(0).equalsIgnoreCase("TO")) {
                        descTo = Descuentos.get(i).get(1);
                        durDescTo = Descuentos.get(i).get(2);
                    } else if (Descuentos.get(i).get(0).equalsIgnoreCase("TV")) {
                        descTv = Descuentos.get(i).get(1);
                        durDescTv = Descuentos.get(i).get(2);
                    } else if (Descuentos.get(i).get(0).equalsIgnoreCase("BA")) {
                        descBa = Descuentos.get(i).get(1);
                        durDescBa = Descuentos.get(i).get(2);
                    }
                }
            }

            for (int i = 0; i < ac.productosRetencion.size(); i++) {
                if (ac.productosRetencion.get(i).producto.equals("TO")) {
                    cotizacion.Telefonia(ac.productosRetencion.get(i).nuevoCambioExistente,
                            ac.productosRetencion.get(i).plan,
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto), descTo, durDescTo,
                            ac.productosRetencion.get(i).identificador, toPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionTo_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosRetencion.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionTo_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosRetencion.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.toIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("TO")) {
                            if (cotizacion.getTipoTo().equals("C")) {
                                cotizacion.setTipoCotizacionTo("0");
                                cotizacion.toIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoTo().equals("E")) {
                                cotizacion.setTipoCotizacionTo("3");
                                cotizacion.toIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionTo() == null) {
                                cotizacion.setTipoCotizacionTo("1");
                            }

                        }

                    }
                } else if (ac.productosRetencion.get(i).producto.equals("TV")) {
                    cotizacion.Television(ac.productosRetencion.get(i).nuevoCambioExistente,
                            ac.productosRetencion.get(i).plan,
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto), descTv, durDescTv,
                            ac.productosRetencion.get(i).identificador, tvPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionTv_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosRetencion.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionTv_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosRetencion.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.tvIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("TV")) {
                            if (cotizacion.getTipoTv().equals("C")) {
                                cotizacion.setTipoCotizacionTv("0");
                                cotizacion.tvIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoTv().equals("E")) {
                                cotizacion.setTipoCotizacionTv("3");
                                cotizacion.tvIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionTv() == null) {
                                cotizacion.setTipoCotizacionTv("1");
                            }
                        }
                    }
                } else if (ac.productosRetencion.get(i).producto.equals("BA")) {
                    cotizacion.Internet(ac.productosRetencion.get(i).nuevoCambioExistente,
                            ac.productosRetencion.get(i).plan,
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto),
                            String.valueOf(ac.productosRetencion.get(i).totalProducto), descBa, durDescBa,
                            ac.productosRetencion.get(i).identificador, baPlanAnt, "");

                    if (cliente.getDepartamento().equalsIgnoreCase("Distrito Capital De Bogota")) {
                        cotizacion.setPlanFacturacionBa_P(Utilidades.homologarPlanFacturacion(cliente.getCiudad(),
                                ac.productosRetencion.get(i).plan, cliente.getEstrato()));
                    } else {
                        cotizacion.setPlanFacturacionBa_P(Utilidades.homologarPlanFacturacion(cliente.getDepartamento(),
                                ac.productosRetencion.get(i).plan, cliente.getEstrato()));
                    }

                    cotizacion.baIdent = "";
                    for (int j = 0; j < ac.productosPortafolio.size(); j++) {
                        if (ac.productosPortafolio.get(j).producto.equals("BA")) {
                            if (cotizacion.getTipoBa().equals("C")) {
                                cotizacion.setTipoCotizacionBa("0");
                                cotizacion.baIdent = ac.productosPortafolio.get(j).identificador;
                            } else if (cotizacion.getTipoBa().equals("E")) {
                                cotizacion.setTipoCotizacionBa("3");
                                cotizacion.baIdent = ac.productosPortafolio.get(j).identificador;
                            }
                        } else {
                            if (cotizacion.getTipoCotizacionBa() == null) {
                                cotizacion.setTipoCotizacionBa("1");
                            }

                        }
                    }
                }
            }

            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionTo_P());
            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionTv_P());
            System.out.println("Cotizacion->" + cotizacion.getPlanFacturacionBa_P());

            cotizacion.Internet_3G("-", "", "", "", "", "");
            cotizacion.Internet_4G("-", "", "", "", "", "");

            cotizacion.setAdicionales(UtilidadesTarificador.agregarAdicionalesGratis(
                    cotizacion.getPlanFacturacionTv_P(), cotizacion.getAdicionales(), cliente.getDepartamento(),
                    cliente.getEstrato(), cotizacion.getPromoTv(), cotizacion.getTiempoPromoTv()));
            cotizacion.setTotalEmp(String.valueOf(new DecimalFormat("###.##").format(valorCanalesRetencion
                    + valorPaqueteRetencion + valorFinancierosRetencion + valorPaqueteAdRetencion)));

            if (UtilidadesTarificador.validarOferta(ac.productosRetencion, cliente.getEstrato())) {
                if (cliente.isControlEstadoCuenta()) {
                    if (UtilidadesTarificador.validarAdicionalesAMC(adic)) {
                        if (!ac.suspendido) {
                            if (!cliente.getTelefonoDestino().equalsIgnoreCase("")) {
                                if (validarCarteraUNE(cotizacion)) {
                                    if (validarScooring(cotizacion)) {
                                        if (Utilidades.validarCliente(cliente.getCiudad(), scooring.getAccion(),
                                                scooring.getEstadoValidador(), this)) {
                                            resultCotizador("venta", cotizacion);
                                        }
                                    } else {
                                        Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                                "Cliente con calificacion negativa, prospectar por el modulo Gestor de Visitas");
                                        dialogo.dialogo.show();
                                    }
                                } else {
                                    Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                            "Documento del cliente vs Documento de Consulta en Cuenta Une no coincide");
                                    dialogo.dialogo.show();
                                }
                            } else {
                                Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                        "Debe ingresar el telefono de IVR");
                                dialogo.dialogo.show();
                            }
                        } else {
                            Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                    "Imposible continuar, servicios suspendidos");
                            dialogo.dialogo.show();
                        }

                    } else {
                        Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                                "Incompatibilidad de canales, favor revisar");
                        dialogo.dialogo.show();
                    }
                } else {
                    Toast.makeText(this,
                            "Recuerda consultar el estado de cuenta del cliente para continuar con la venta",
                            Toast.LENGTH_SHORT).show();
                    resultCotizador("estado cuenta", cotizacion);
                }

            } else {
                Dialogo dialogo = new Dialogo(this, Dialogo.DIALOGO_ALERTA,
                        "Incompatibilidad de oferta, favor revisar");
                dialogo.dialogo.show();
            }
            UtilidadesTarificador.limpiar();

        }

    }

    public void resultCotizador(String tipoIngreso, Cotizacion cotizacion) {
        Intent intent = new Intent();
        intent.putExtra("cotizacion", cotizacion);
        intent.putExtra("cliente", cliente);
        intent.putExtra("tipoIngreso", tipoIngreso);
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
    }

    public boolean validarCarteraUNE(Cotizacion cotizacion) {

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
                resultCotizador("estado cuenta", cotizacion);
                valid = false;
            }

        }

        return valid;
    }

    public boolean validarScooring(Cotizacion cotizacion) {

        boolean valid = true;

        System.out.println();
        if (!scooring.isValidarCartera()) {
            if (cliente.getScooringune() != null) {
                if (cliente.getScooringune().getDocumentoScooring().equalsIgnoreCase(cliente.getCedula())) {
                    if (cliente.getScooringune().isValidarScooring()) {
                        if (!cliente.getScooringune().isPasaScooring()) {
                            if (productosNuevos(cotizacion)) {
                                if (cliente.getDomiciliacion().equals("SI")) {
                                    if (Utilidades.excluirEstadosDomiciliacion()
                                            .contains(cliente.getScooringune().getRazonScooring())) {
                                        cliente.getScooringune().setNoAplicaScooring(true);
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
                                cliente.getScooringune().setNoAplicaScooring(true);
                                System.out.println(
                                        "isNoAplicaScooring() " + cliente.getScooringune().isNoAplicaScooring());
                            }
                        } else {
                            if (!productosNuevos(cotizacion)) {
                                cliente.getScooringune().setNoAplicaScooring(true);
                                System.out.println(
                                        "isNoAplicaScooring() " + cliente.getScooringune().isNoAplicaScooring());

                            }
                        }
                    } else {
                        if (cliente.getScooringune().isPendienteRespuesta()) {
                            if (!productosNuevos(cotizacion)) {
                                cliente.getScooringune().setNoAplicaScooring(true);
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Documento del cliente vs Documento de Consulta en Estado Cuenta no coincide",
                            Toast.LENGTH_SHORT).show();
                    resultCotizador("estado cuenta", cotizacion);
                    valid = false;
                }
            }
        }

        return valid;
    }

    public boolean productosNuevos(Cotizacion cotizacion) {

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
}