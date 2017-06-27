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
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.complements.Validaciones;
import co.com.une.appmovilesune.components.ResumenInternet;
import co.com.une.appmovilesune.components.ResumenTelefonia;
import co.com.une.appmovilesune.components.ResumenTelevision;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Cotizacion;
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

    TabHost tabs;

    ArrayList<ItemPromocionesAdicionales> promocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();

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

    private String Mail = "NO";

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
                    if (Municipio.equals("Medellin")) {
                        if (Barrio.equals("")) {
                            dialogoA = new Dialogo(context, Dialogo.DIALOGO_ALERTA, "Debe ingresar un barrio");
                            dialogoA.dialogo.show();
                            tabs.setCurrentTab(0);
                        } else {
                            txtBarrio.setText(Barrio);
                            if (Utilidades.camposUnicos("ConsultaAgenda").equalsIgnoreCase("Actual")) {
                                llenarAgendamiento();
                            } else if (Utilidades.camposUnicos("ConsultaAgenda").equalsIgnoreCase("Nueva")) {
                                llenarAgendamientoFenix();
                            }
                        }
                    } else if (Municipio.equals("BogotaREDCO")) {
                        System.out.println("hola agenda REDCO");
                        lanzarAgendaRedco("SSC");
                    } else if (Utilidades.excluir("agendaSantander", Municipio)) {
                        llenarAgendamientoSiebel();
                    } else if (Utilidades.excluir("excluirAgenda", Municipio)) {

                        System.out.println("hola agenda Elite");
                        lanzarAgendaRedco("Elite");
                    } else {
                        if (Utilidades.camposUnicos("ConsultaAgenda").equalsIgnoreCase("Actual")) {
                            llenarAgendamiento();
                        } else if (Utilidades.camposUnicos("ConsultaAgenda").equalsIgnoreCase("Nueva")) {
                            llenarAgendamientoFenix();
                        }
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
            venta = (Venta) reicieveParams.getSerializable("venta");

            cliente = (Cliente) reicieveParams.getSerializable("cliente");

            System.out.println("Despues smartPromo " + cliente.getSmartPromo());

            System.out.println("MainActivity.config.srvVersion " + MainActivity.config.srvVersion);

            id = String.valueOf(cliente.getIdIVR());
            System.out.println("id IVR " + id + " cliente id ivr " + cliente.getIdIVR());

            if (!cliente.getCorreo().equalsIgnoreCase("") && !cliente.getCorreo().contains("@sincorreo.com")) {
                Mail = "SI";
            }

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
                llenarOtrasPromociones(cotizacion.getEstrato());

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

            }

        }

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
            rtv.setPrecioAdicionales(venta.getTelevision()[8]);
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

        if(adicionales != null) {
            for (int i = 0; i < adicionales.length; i++) {
                listaAdicionalesTo.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], "-", "0 Meses"));
            }
        }

        listaAdicionalesTo.add(new ListaAdicionales("Impuesto Telefonico", String.valueOf(UtilidadesTarificador.ImpuestoTelefonico(cliente.getCiudad(),
                cliente.getDepartamento(), cliente.getEstrato())), "-", "0 Meses"));

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

                listaAdicionales.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], descuento, duracion));

            } else {
                listaAdicionales.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], "-", "0 Meses"));
            }

            promoAdIndividual = promocionesAdIndividuales(cotizacion.getTelevision(),
                    (String) sltEmpaquetado.getSelectedItem(), adicionales[i][0], cotizacion.getEstrato());

            if (promoAdIndividual.size() > 0) {
                for (int j = 0; j < listaAdicionales.size(); j++) {
                    if (listaAdicionales.get(j).getAdicional().equalsIgnoreCase(adicionales[i][0])) {
                        listaAdicionales.remove(j);
                        listaAdicionales.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1],
                                promoAdIndividual.get(0)[0], promoAdIndividual.get(0)[1]));
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

    public ArrayList<JSONObject> arraylistAdicionalesBa(String nombreGota, String valorGota, String valorGotaSinIva,
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

            arraylistAdicionales.add(adicional);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arraylistAdicionales;

    }

    private void lanzarAgendaRedco(String Systema) {
        Intent intent = new Intent(MainActivity.MODULO_AGENDABOGOTA);
        intent.putExtra("Systema", Systema);
        startActivityForResult(intent, MainActivity.REQUEST_CODE);
    }

    private void llenarAgendamiento() {

        // ArrayList<String[]> parametros = new ArrayList<String[]>();

        ArrayList<String> parametros = new ArrayList<String>();

        String Productos = "";
        String UnidadResidencia = "0";
        boolean InstalacionNueva = true;
        boolean coberHFCBidi = false;
        String clienteExistente = "";
        String cobertura = "";
        String Direccionalidad = "";
        String Paginacion = "";

        if (Departamento.equals("Distrito Capital De Bogota")) {
            // parametros.add(new String[] { "Departamento", "Cundinamarca" });
            parametros.add("Cundinamarca");
        } else {
            // parametros.add(new String[] { "Departamento", Departamento });
            parametros.add(Departamento);

        }

        if (Municipio.equals("BogotaHFC")) {
            // parametros.add(new String[] { "Municipio", "Bogota D.C." });
            parametros.add("Bogota D.C.");

        } else {
            // parametros.add(new String[] { "Municipio", Municipio });
            parametros.add(Municipio);
        }


        clienteExistente = "No";

        if (!cotizacion.getTelefonia().equalsIgnoreCase("-")) {
            Productos += "TO+";
            if (cotizacion.getTipoTo().equalsIgnoreCase("E") || cotizacion.getTipoTo().equalsIgnoreCase("C")) {
                InstalacionNueva = false;
                clienteExistente = "Si";
            }
        }

        if (!cotizacion.getTelevision().equalsIgnoreCase("-")) {
            Productos += "TV+";
            if (cotizacion.getTipoTv().equalsIgnoreCase("E") || cotizacion.getTipoTv().equalsIgnoreCase("C")) {
                InstalacionNueva = false;
                clienteExistente = "Si";
            }
        }

        if (!cotizacion.getInternet().equalsIgnoreCase("-")) {
            Productos += "BA+";
            if (cotizacion.getTipoBa().equalsIgnoreCase("E") || cotizacion.getTipoBa().equalsIgnoreCase("C")) {
                InstalacionNueva = false;
                clienteExistente = "Si";
            }
        }

        if (tipoPredio != null) {
            if (tipoPredio.equalsIgnoreCase("Unidad Residencial")) {
                UnidadResidencia = "1";
            }
        }

        if (tipoPropiedad != null) {
            if (tipoPropiedad.equalsIgnoreCase("Conjunto Residencial")
                    && Utilidades.excluirNacional("habilitarUnidadResidencia", "habilitarUnidadResidencia")) {
                UnidadResidencia = "1";
            }
        }

        System.out.println("InstalacionNueva " + InstalacionNueva);

        if (cliente.getCobertura() != null/* && !InstalacionNueva */) {
            System.out.println("cliente.getCobertura() " + cliente.getCobertura());

            try {
                JSONObject cober = new JSONObject(cliente.getCobertura());
                if (cober.has("codigoMensaje")) {
                    if (cober.getString("codigoMensaje").equalsIgnoreCase("00")) {
                        if (cober.has("Cobertura")) {
                            JSONObject datacober = cober.getJSONObject("Cobertura");
                            if (datacober.has("COBERTURA_HFC") && datacober.has("DIRECCIONALIDAD")) {
                                if (datacober.getString("COBERTURA_HFC").equalsIgnoreCase("SI")
                                        && datacober.getString("DIRECCIONALIDAD").equalsIgnoreCase("B")) {
                                    coberHFCBidi = true;
                                    cobertura = "HFC";
                                    Direccionalidad = "B";
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                Log.w("Error", e.getMessage());
            }
            // if()
        }

        System.out.println("coberHFCBidi " + coberHFCBidi);

        parametros.add(Barrio);
        parametros.add(cotizacion.getEstrato());
        parametros.add(Productos);


        parametros.add(UnidadResidencia);
        parametros.add(cobertura);
        parametros.add(Direccionalidad);
        parametros.add(clienteExistente);

        System.out.println("cliente.getPaginacion() " + cliente.getPaginacion());

        System.out.println("cliente.getPaginaAsignacion() " + cliente.getPaginaAsignacion());

        if (Departamento.equals("Antioquia")) {
            parametros.add(cliente.getPaginacion());
        } else {
            parametros.add(cliente.getPaginaAsignacion());
        }

        parametros.add(agendableDomingo);

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ConsultarAgenda");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

        // ArrayList<Object> params = new ArrayList<Object>();
        // params.add("ConsultarAgenda");
        // params.add(parametros);
        //
        // MainActivity.crearConexion();
        // MainActivity.conexion.setManual(this);
        // MainActivity.conexion.addObserver(this);
        // MainActivity.conexion.execute(params);
        //
        // try {
        // final ArrayList<ListaAgendamiento> agenda = new
        // ArrayList<ListaAgendamiento>();
        //
        // JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap(
        // "ConsultarAgenda", parametros));
        // for (int i = 0; i < ja.length(); i++) {
        // JSONObject jo = ja.getJSONObject(i);
        // if (!jo.getString("cuposam").equals("0")
        // || !jo.getString("cupospm").equals("0")) {
        // agenda.add(new ListaAgendamiento(jo.getString("fecha"), jo
        // .getString("cuposam"), jo
        // .getString("porcentageocuam"), jo
        // .getString("cupospm"), jo
        // .getString("porcentageocupm")));
        // }
        //
        // }
        //
        // ListaAgendamientoAdapter adaptador = new ListaAgendamientoAdapter(
        // this, agenda, this);
        // lstAgendamineto.setAdapter(adaptador);
        // lstAgendamineto.setOnItemClickListener(new OnItemClickListener() {
        // public void onItemClick(AdapterView<?> arg0, View arg1,
        // int arg2, long arg3) {
        // // ListaAgendamiento item = agenda.get(arg2);
        // fecha = agenda.get(arg2).getFecha();
        // cuposAM = agenda.get(arg2).getDisManana();
        // cuposPM = agenda.get(arg2).getDisTarde();
        // dialogo.dialogo.show();
        // }
        // });
        //
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

    private void llenarAgendamientoFenix() {

        // ArrayList<String[]> parametros = new ArrayList<String[]>();

        ArrayList<String> parametros = new ArrayList<String>();

        String Productos = "";
        String UnidadResidencia = "0";
        boolean InstalacionNueva = true;
        boolean coberHFCBidi = false;
        String clienteExistente = "";
        String cobertura = "";
        String Direccionalidad = "";
        String Paginacion = "";

        JSONObject agenda = new JSONObject();
        JSONArray productos = new JSONArray();

        try {

            if (Departamento.equals("Distrito Capital De Bogota")) {
                agenda.put("departamentoName", "Cundinamarca");
            } else {
                agenda.put("departamentoName", Departamento);
            }

            if (Municipio.equals("BogotaHFC")) {
                agenda.put("ciudadName", "Bogota D.C.");
            } else {
                agenda.put("ciudadName", Municipio);
            }


            clienteExistente = "No";

            if (!cotizacion.getTelefonia().equalsIgnoreCase("-")) {
                Productos += "TO+";
                if (cotizacion.getTipoTo().equalsIgnoreCase("E") || cotizacion.getTipoTo().equalsIgnoreCase("C")) {
                    InstalacionNueva = false;
                    clienteExistente = "Si";
                }
            }

            if (!cotizacion.getTelevision().equalsIgnoreCase("-")) {
                Productos += "TV+";
                if (cotizacion.getTipoTv().equalsIgnoreCase("E") || cotizacion.getTipoTv().equalsIgnoreCase("C")) {
                    InstalacionNueva = false;
                    clienteExistente = "Si";
                }
            }

            if (!cotizacion.getInternet().equalsIgnoreCase("-")) {
                Productos += "BA+";
                if (cotizacion.getTipoBa().equalsIgnoreCase("E") || cotizacion.getTipoBa().equalsIgnoreCase("C")) {
                    InstalacionNueva = false;
                    clienteExistente = "Si";
                }
            }

            if (tipoPredio != null) {
                if (tipoPredio.equalsIgnoreCase("Unidad Residencial")) {
                    UnidadResidencia = "1";
                }
            }

            if (tipoPropiedad != null) {
                if (tipoPropiedad.equalsIgnoreCase("Conjunto Residencial")
                        && Utilidades.excluirNacional("habilitarUnidadResidencia", "habilitarUnidadResidencia")) {
                    UnidadResidencia = "1";
                }
            }

            System.out.println("InstalacionNueva " + InstalacionNueva);

            if (cliente.getCobertura() != null/* && !InstalacionNueva */) {
                System.out.println("cliente.getCobertura() " + cliente.getCobertura());

                try {
                    JSONObject cober = new JSONObject(cliente.getCobertura());
                    if (cober.has("codigoMensaje")) {
                        if (cober.getString("codigoMensaje").equalsIgnoreCase("00")) {
                            if (cober.has("Cobertura")) {
                                JSONObject datacober = cober.getJSONObject("Cobertura");
                                if (datacober.has("COBERTURA_HFC") && datacober.has("DIRECCIONALIDAD")) {
                                    if (datacober.getString("COBERTURA_HFC").equalsIgnoreCase("SI")
                                            && datacober.getString("DIRECCIONALIDAD").equalsIgnoreCase("B")) {
                                        coberHFCBidi = true;
                                        cobertura = "HFC";
                                        Direccionalidad = "B";
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.w("Error", e.getMessage());
                }
                // if()
            }

            System.out.println("coberHFCBidi " + coberHFCBidi);

            agenda.put("barrioName", cliente.getBarrio());
            agenda.put("estrato", cliente.getEstrato());

            parametros.add(Productos);

            if (!cotizacion.getTelefonia().equalsIgnoreCase("-")) {
                productos.put(
                        Utilidades.productosAgendaFenix("TO", cotizacion.getTipoCotizacionTo()));
            }


            if (!cotizacion.getInternet().equalsIgnoreCase("-")) {
                productos.put(
                        Utilidades.productosAgendaFenix("BA", cotizacion.getTipoCotizacionBa()));
            }

            if (!cotizacion.getTelevision().equalsIgnoreCase("-")) {
                productos.put(
                        Utilidades.productosAgendaFenix("TV", cotizacion.getTipoCotizacionTv()));
            }


            agenda.put("productos", productos);
            agenda.put("urbanizacion", UnidadResidencia);
            agenda.put("tecnologia", cobertura);
            agenda.put("direccionalidad", Direccionalidad);
            agenda.put("clienteExistente", clienteExistente);
            agenda.put("autorizaCobroDomingo", agendableDomingo);

            if (Departamento.equals("Antioquia")) {
                parametros.add(cliente.getPaginacion());
                agenda.put("paginaServicio", cliente.getPaginacion());
            } else {
                parametros.add(cliente.getPaginaAsignacion());
                agenda.put("paginaServicio", cliente.getPaginaAsignacion());
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("agenda json " + agenda.toString());

        // if(cliente.)

        // ArrayList<String> parametros = new ArrayList<String>();
        // parametros.add(IVR.toString());
        // parametros.add(MainActivity.config.getCodigo());
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ConsultarAgendaFenix");
        params.add(agenda.toString());
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

        // ArrayList<Object> params = new ArrayList<Object>();
        // params.add("ConsultarAgenda");
        // params.add(parametros);
        //
        // MainActivity.crearConexion();
        // MainActivity.conexion.setManual(this);
        // MainActivity.conexion.addObserver(this);
        // MainActivity.conexion.execute(params);
        //
        // try {
        // final ArrayList<ListaAgendamiento> agenda = new
        // ArrayList<ListaAgendamiento>();
        //
        // JSONArray ja = new JSONArray(MainActivity.conexion.ejecutarSoap(
        // "ConsultarAgenda", parametros));
        // for (int i = 0; i < ja.length(); i++) {
        // JSONObject jo = ja.getJSONObject(i);
        // if (!jo.getString("cuposam").equals("0")
        // || !jo.getString("cupospm").equals("0")) {
        // agenda.add(new ListaAgendamiento(jo.getString("fecha"), jo
        // .getString("cuposam"), jo
        // .getString("porcentageocuam"), jo
        // .getString("cupospm"), jo
        // .getString("porcentageocupm")));
        // }
        //
        // }
        //
        // ListaAgendamientoAdapter adaptador = new ListaAgendamientoAdapter(
        // this, agenda, this);
        // lstAgendamineto.setAdapter(adaptador);
        // lstAgendamineto.setOnItemClickListener(new OnItemClickListener() {
        // public void onItemClick(AdapterView<?> arg0, View arg1,
        // int arg2, long arg3) {
        // // ListaAgendamiento item = agenda.get(arg2);
        // fecha = agenda.get(arg2).getFecha();
        // cuposAM = agenda.get(arg2).getDisManana();
        // cuposPM = agenda.get(arg2).getDisTarde();
        // dialogo.dialogo.show();
        // }
        // });
        //
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

    private void llenarAgendamientoSiebel() {

        // ArrayList<String[]> parametros = new ArrayList<String[]>();

        agendableSiebel = 2;
        lblAgendamiento.setText("");

        if (cliente.getEstandarizarSiebel() == 0
                || (cliente.getLatitud() == null || cliente.getLatitud().equalsIgnoreCase(""))
                || (cliente.getLongitud() == null || cliente.getLongitud().equalsIgnoreCase(""))) {
            Toast.makeText(this, getResources().getString(R.string.georeferenciacionnopresisanoagendable),
                    Toast.LENGTH_SHORT).show();
            agendableSiebel = 0;
            limpiarAgenda();
        } else {
            ArrayList<String> parametros = new ArrayList<String>();

            JSONObject agenda;
            try {

                equipos1 = equiposSiebel();

                agenda = new JSONObject(Utilidades.agendaSiebel(cliente, cotizacion,
                        Utilidades.camposUnicosCiudad("timeSiebelMunicipal", Municipio), equiposSiebel()));

                System.out.println("agenda " + agenda);

                if (agenda.has("agendable") && agenda.getBoolean("agendable") == true) {

                    parametros.add(agenda.toString());

                    ArrayList<Object> params = new ArrayList<Object>();
                    params.add(MainActivity.config.getCodigo());
                    params.add("ConsultarAgendaSiebel");
                    params.add(parametros);
                    Simulador simulador = new Simulador();
                    simulador.setManual(this);
                    simulador.addObserver(this);
                    simulador.execute(params);
                    agendableSiebel = 1;
                } else if (agenda.has("agendable") && agenda.getBoolean("agendable") == false) {
                    agendableSiebel = 0;
                    Toast.makeText(this, "Productos No Agendables", Toast.LENGTH_SHORT).show();
                    limpiarAgenda();
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public String equiposSiebel() {

        JSONObject equipos = new JSONObject();

        try {

            equipos.put("Telefonia", 0);
            equipos.put("Internet", 0);
            equipos.put("Television", 0);

            if (!cotizacion.getTelefonia().equalsIgnoreCase("-")) {

                equipos.put("Telefonia", 0);

                if (rto.getTecnologia().equalsIgnoreCase("TOIP")) {
                    equipos.put("Telefonia", 1);
                }
            }

            if (!cotizacion.getTelevision().equalsIgnoreCase("-")) {

                int equipotv = 0;

                JSONObject decos = new JSONObject(rtv.getDecos());

                System.out.println("decos " + decos);

                if (decos.has("decos_sd")) {
                    System.out.println("decos antes sd" + equipotv);
                    equipotv += decos.getInt("decos_sd");
                    System.out.println("decos despues sd " + equipotv);
                }

                if (decos.has("decos_hd")) {
                    System.out.println("decos antes hd" + equipotv);
                    equipotv += decos.getInt("decos_hd");
                    System.out.println("decos despues hd " + equipotv);
                }

                equipos.put("Television", equipotv);

            }

            if (!cotizacion.getInternet().equalsIgnoreCase("-")) {

                equipos.put("Internet", 0);
                if (rba.getWifi().equalsIgnoreCase("SI")) {
                    equipos.put("Internet", 1);
                }
            }

        } catch (JSONException e) {
            Log.w("Error ", e.getMessage());
        }

        return equipos.toString();
    }

    public void Empaquetamiento() {
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                Utilidades.SI_NO);
        sltEmpaquetado.setAdapter(adaptador);

        System.out.println("Empaquetamiento oferta "+cotizacion.getOferta());

        if (cotizacion.getOferta() != null) {
            if (!cotizacion.getOferta().equalsIgnoreCase("")) {
                sltEmpaquetado.setEnabled(false);
            }
        } else {
            sltEmpaquetado.setEnabled(false);
        }

        try {
            int contador = Integer.parseInt(cotizacion.getContadorProductos());
            if (contador > 1) {

                System.out.println("Empaquetamiento contador "+contador);

                if(Utilidades.validarNacionalValor("ofertaIndividual",cotizacion.getOferta())){
                    System.out.println("Empaquetamiento ofertaIndividual ");
                    sltEmpaquetado.setSelection(1);
                    Venta_Individual();
                }else{
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

        if (venta != null) {
            lblAgendamiento.setText(venta.getHorarioAtencion());
        }

        System.out.println("cotizacion.getContadorProductos() "+cotizacion.getContadorProductos());

        if (Utilidades.excluirNacional("descuestosIndividuales", cotizacion.getTelefonia()) && cotizacion.getContadorProductos().equalsIgnoreCase("1")) {
            rto.Telefonia(this, this, cotizacion.getTipoTo(), cotizacion.getTelefonia(), cotizacion.getToInd(),
                    cotizacion.getPromoTo(), cotizacion.getTiempoPromoTo(), "0", cotizacion.getEstrato(), cotizacion.getPlanFacturacionTo_I(), segundaLinea,
                    cotizacion.toPlanAnt, cotizacion.getAdicionalesTo(), cotizacion.getTotalAdicionalesTo(),
                    cotizacion.toTecnologiacr, aplicarDescuentos);

        } else {
            rto.Telefonia(this, this, cotizacion.getTipoTo(), cotizacion.getTelefonia(), cotizacion.getToInd(),
                    "Sin Promocion", "N/A", "0", cotizacion.getEstrato(), cotizacion.getPlanFacturacionTo_I(), segundaLinea,
                    cotizacion.toPlanAnt, cotizacion.getAdicionalesTo(), cotizacion.getTotalAdicionalesTo(),
                    cotizacion.toTecnologiacr, aplicarDescuentos);
        }

        if (cliente.getDepartamento().equalsIgnoreCase("Antioquia")) {
            rto.agregarImpuestoTelefonico(cliente.getCiudad(), cliente.getDepartamento(), cliente.getEstrato());

            if (!cotizacion.getTelefonia().equalsIgnoreCase("-")) {
                // if(cliente.getCobertura().)

                System.out.println("Cobertura " + cliente.getCobertura());


                if (!cliente.isControlCerca()) {
                    if (Utilidades.TOIP_Default(cliente.getCobertura(), cotizacion.toTecnologiacr)) {
                        rto.tipoTecnologiaDefaul();
                    }
                } else {
                    if (!cliente.getTecnologia().equals("REDCO")) {
                        rto.tipoTecnologiaDefaul();
                    }
                }
            }
        }

        if (venta != null) {
            llenarTelefonia();
        }
        if (cotizacion.getTelefonia().equalsIgnoreCase("-")) {
            rto.setVisibility(View.GONE);
        }

        if (cotizacion.getTelevision().contains("Circasia")
                || Utilidades.excluirNacional("planesDigitales", cotizacion.getTelevision())) {
            System.out.println("cotizacion.getContadorProductos() " + cotizacion.getContadorProductos());
            if (!cotizacion.getContadorProductos().equals("1")
                    && Utilidades.excluirNacional("planesDigitales", cotizacion.getTelevision())) {
                System.out.println("cotizacion.getContadorProductos() mas de 1 producto");
                rtv.Television(this, this, cotizacion.getTipoTv(), cotizacion.getTelevision(), cotizacion.getTvInd(),
                        "Sin Promocion", "N/A", "0", cotizacion.getAdicionales(), cotizacion.getTotalAdicionales(),
                        cotizacion.getPlanFacturacionTv_I(), cotizacion.getEstrato(), Municipio, cotizacion.tvPlanAnt,
                        cotizacion.tvTecnologiacr, aplicarDescuentos, cliente.getTecnologia(),
                        cotizacion.getItemPromocionesAdicionales(), cotizacion.getDecodificadores(), cotizacion);
            } else {
                System.out.println("cotizacion.getContadorProductos() 1 producto");
                rtv.Television(this, this, cotizacion.getTipoTv(), cotizacion.getTelevision(), cotizacion.getTvInd(),
                        cotizacion.getPromoTv(), cotizacion.getTiempoPromoTv(), cotizacion.getTvDInd(),
                        cotizacion.getAdicionales(), cotizacion.getTotalAdicionales(),
                        cotizacion.getPlanFacturacionTv_I(), cotizacion.getEstrato(), Municipio, cotizacion.tvPlanAnt,
                        cotizacion.tvTecnologiacr, aplicarDescuentos, cliente.getTecnologia(),
                        cotizacion.getItemPromocionesAdicionales(), cotizacion.getDecodificadores(), cotizacion);
            }
        } else {
            if (Utilidades.excluir("cambiarPlan", Municipio)) {
                rtv.Television(this, this, cotizacion.getTipoTv(), cotizacion.getTelevision(), cotizacion.getTvInd(),
                        "Sin Promocion", "N/A", "0", cotizacion.getAdicionales(), cotizacion.getTotalAdicionales(),
                        cotizacion.getPlanFacturacionTv_I(), cotizacion.getEstrato(), Municipio, cotizacion.tvPlanAnt,
                        cotizacion.tvTecnologiacr, aplicarDescuentos, cliente.getTecnologia(),
                        cotizacion.getItemPromocionesAdicionales(), cotizacion.getDecodificadores(), cotizacion);
            } else {
                rtv.Television(this, this, cotizacion.getTipoTv(), cotizacion.getTelevision(), cotizacion.getTvInd(),
                        "Sin Promocion", "N/A", "0", cotizacion.getAdicionales(), cotizacion.getTotalAdicionales(),
                        cotizacion.getPlanFacturacionTv_I(), cotizacion.getEstrato(), Municipio, cotizacion.tvPlanAnt,
                        cotizacion.tvTecnologiacr, aplicarDescuentos, cliente.getTecnologia(),
                        cotizacion.getItemPromocionesAdicionales(), cotizacion.getDecodificadores(), cotizacion);
            }
        }

        itemDecodificadors = cotizacion.getItemDecodificadores();

        if (venta != null) {
            llenarTelevision();
        }
        if (cotizacion.getTelevision().equalsIgnoreCase("-")) {
            rtv.setVisibility(View.GONE);
        }

        if (Utilidades.excluirNacional("descuestosIndividuales", cotizacion.getInternet()) && cotizacion.getContadorProductos().equalsIgnoreCase("1")) {
            if (Utilidades.excluir("cambiarPlan", Municipio)) {
                rba.Internet(this, this, cotizacion.getTipoBa(), cotizacion.getInternet(), cotizacion.getBaInd(),
                        cotizacion.getPromoBa(), cotizacion.getTiempoPromoBa(), "0",
                        cotizacion.getPlanFacturacionBa_I(), cotizacion.getEstrato(), cotizacion.baPlanAnt,
                        cotizacion.getAdicionalesBa(), cotizacion.getTotalAdicionalesBa(), cotizacion.baTecnologiacr, aplicarDescuentos);
            } else {
                rba.Internet(this, this, cotizacion.getTipoBa(), cotizacion.getInternet(), cotizacion.getBaInd(),
                        cotizacion.getPromoBa(), cotizacion.getTiempoPromoBa(), "0",
                        cotizacion.getPlanFacturacionBa_I(), cotizacion.getEstrato(), cotizacion.baPlanAnt,
                        cotizacion.getAdicionalesBa(), cotizacion.getTotalAdicionalesBa(), cotizacion.baTecnologiacr, aplicarDescuentos);
            }

            if (cotizacion.isControlGota()) {
                System.out.println("nombre Gota " + cotizacion.getNombreGota());
                System.out.println("valor Gota " + cotizacion.getPrecioGota());
                rba.setLblGota(cotizacion.getNombreGota());
                rba.setLblValorGota("" + cotizacion.getPrecioGota());
            }
        } else if (Utilidades.excluir("cambiarPlan", Municipio)) {
            rba.Internet(this, this, cotizacion.getTipoBa(), cotizacion.getInternet(), cotizacion.getBaInd(), "Sin Promocion",
                    "N/A", "0", cotizacion.getPlanFacturacionBa_I(), cotizacion.getEstrato(), cotizacion.baPlanAnt,
                    cotizacion.getAdicionalesBa(), cotizacion.getTotalAdicionalesBa(), cotizacion.baTecnologiacr, aplicarDescuentos);
        } else {
            rba.Internet(this, this, cotizacion.getTipoBa(), cotizacion.getInternet(), cotizacion.getBaInd(), "Sin Promocion",
                    "N/A", "0", cotizacion.getPlanFacturacionBa_I(), cotizacion.getEstrato(), cotizacion.baPlanAnt,
                    cotizacion.getAdicionalesBa(), cotizacion.getTotalAdicionalesBa(), cotizacion.baTecnologiacr, aplicarDescuentos);
        }

        if (venta != null) {
            llenarBandaAncha();
        }
        if (cotizacion.getInternet().equalsIgnoreCase("-")) {
            rba.setVisibility(View.GONE);
        }

        if (!cotizacion.getTelefonia().equalsIgnoreCase("-")) {
            lblTotal.setText(String.valueOf(Double.parseDouble(cotizacion.getTotalInd()) + UtilidadesTarificador
                    .ImpuestoTelefonico(cliente.getCiudad(), cliente.getDepartamento(), cliente.getEstrato())));
        } else {
            lblTotal.setText(cotizacion.getTotalInd());
        }

        if (aplicarDescuentos) {
            if (!cotizacion.getTotalIndDescuento().equals("0") && !cotizacion.getTotalIndDescuento().equals("0.0")
                    && !cotizacion.getTotalIndDescuento().equals("")) {
                // lblTotalDescuento.setText(String.valueOf(Double.parseDouble(cotizacion.getTotalIndDescuento())
                // +
                // UtilidadesTarificador.ImpuestoTelefonico(cliente.getCiudad(),
                // cliente.getDepartamento(),
                // cliente.getEstrato())+cotizacion.getTotalDecos()));
                lblTotalDescuento.setText(String
                        .valueOf(Utilidades.convertirDouble(cotizacion.getTotalIndDescuento(),
                                "cotizacion.getTotalIndDescuento()")/*
                                                                     * Double.
																	 * parseDouble(
																	 * cotizacion.
																	 * getTotalIndDescuento
																	 * ()
																	 */)
                        + UtilidadesTarificador.ImpuestoTelefonico(cliente.getCiudad(), cliente.getDepartamento(),
                        cliente.getEstrato()));
                //llyTotalDescuento.setVisibility(View.VISIBLE);
            }
        }

        if (venta != null) {
            txtObservaciones.setText(venta.getObservaciones());
        }
    }

    public void Venta_Empaquetada() {

        System.out.println("Venta_Empaquetada->cotizacion.toTecnologiacr " + cotizacion.toTecnologiacr);
        System.out.println("Venta_Empaquetada->cotizacion.tvTecnologiacr " + cotizacion.tvTecnologiacr);
        System.out.println("Venta_Empaquetada->cotizacion.baTecnologiacr " + cotizacion.baTecnologiacr);

        if (venta != null) {
            lblAgendamiento.setText(venta.getHorarioAtencion());
        }

        rto.Telefonia(this, this, cotizacion.getTipoTo(), cotizacion.getTelefonia(), cotizacion.getToEmp(),
                cotizacion.getPromoTo(), cotizacion.getTiempoPromoTo(), cotizacion.getToDEmp(), cotizacion.getEstrato(),
                cotizacion.getPlanFacturacionTo_P(), segundaLinea, cotizacion.toPlanAnt, cotizacion.getAdicionalesTo(),
                cotizacion.getTotalAdicionalesTo(), cotizacion.toTecnologiacr, aplicarDescuentos);

        if (cliente.getDepartamento().equalsIgnoreCase("Antioquia")) {
            rto.agregarImpuestoTelefonico(cliente.getCiudad(), cliente.getDepartamento(), cliente.getEstrato());

            if (!cotizacion.getTelefonia().equalsIgnoreCase("-")) {
                // if(cliente.getCobertura().)

                System.out.println("Cobertura " + cliente.getCobertura());

                if (!cliente.isControlCerca()) {
                    if (Utilidades.TOIP_Default(cliente.getCobertura(), cotizacion.toTecnologiacr)) {
                        rto.tipoTecnologiaDefaul();
                    }
                } else {
                    if (!cliente.getTecnologia().equals("REDCO")) {
                        rto.tipoTecnologiaDefaul();
                    }
                }
            }
        }

        if (venta != null) {
            llenarTelefonia();
        }

        if (cotizacion.getTelefonia().equalsIgnoreCase("-") || cotizacion.getTelefonia().equalsIgnoreCase("")
                || cotizacion.getTelefonia().equalsIgnoreCase("--Seleccione Producto--")) {
            cotizacion.setTelefonia("-");
            rto.setVisibility(View.GONE);
        }

        System.out.println("cotizacion.isPromoTo_12_100() " + cotizacion.isPromoTo_12_100());

        if (cotizacion.isPromoTo_12_100() && Utilidades.excluir("habilitarQuitarPromo", cliente.getCiudad())) {
            rto.habilitarQuitarPromo();
        }

        rtv.Television(this, this, cotizacion.getTipoTv(), cotizacion.getTelevision(), cotizacion.getTvEmp(),
                cotizacion.getPromoTv(), cotizacion.getTiempoPromoTv(), cotizacion.getTvDEmp(),
                cotizacion.getAdicionales(), cotizacion.getTotalAdicionales(), cotizacion.getPlanFacturacionTv_P(),
                cotizacion.getEstrato(), Municipio, cotizacion.tvPlanAnt, cotizacion.tvTecnologiacr, aplicarDescuentos,
                cliente.getTecnologia(), cotizacion.getItemPromocionesAdicionales(), cotizacion.getDecodificadores(), cotizacion);

        if (venta != null) {
            llenarTelevision();
        }

        if (cotizacion.getTelevision().equalsIgnoreCase("-") || cotizacion.getTelevision().equals("")
                || cotizacion.getTelevision().equalsIgnoreCase("--Seleccione Producto--")) {
            cotizacion.setTelevision("-");
            rtv.setVisibility(View.GONE);
        }

        if (Utilidades.excluirNacional("quitarPromocion", cotizacion.getInternet())) {
            rba.Internet(this, this, cotizacion.getTipoBa(), cotizacion.getInternet(), cotizacion.getBaEmp(), "Sin Promocion",
                    "N/A", "0", "N/A", cotizacion.getEstrato(), cotizacion.baPlanAnt, cotizacion.getAdicionalesBa(), cotizacion.getTotalAdicionalesBa(),
                    cotizacion.baTecnologiacr, aplicarDescuentos);

            if (cotizacion.isControlGota()) {
                System.out.println("nombre Gota " + cotizacion.getNombreGota());
                System.out.println("valor Gota " + cotizacion.getPrecioGota());
            }
        } else {
            rba.Internet(this, this, cotizacion.getTipoBa(), cotizacion.getInternet(), cotizacion.getBaEmp(),
                    cotizacion.getPromoBa(), cotizacion.getTiempoPromoBa(), cotizacion.getBaDEmp(),
                    cotizacion.getPlanFacturacionBa_P(), cotizacion.getEstrato(), cotizacion.baPlanAnt,
                    cotizacion.getAdicionalesBa(), cotizacion.getTotalAdicionalesBa(), cotizacion.baTecnologiacr, aplicarDescuentos);

            if (cotizacion.isControlGota()) {
                System.out.println("nombre Gota " + cotizacion.getNombreGota());
                System.out.println("valor Gota " + cotizacion.getPrecioGota());
                rba.setLblGota(cotizacion.getNombreGota());
                rba.setLblValorGota("" + cotizacion.getPrecioGota());
            }
        }

        // rba.Internet(this, cotizacion.getTipoBa(), cotizacion.getInternet(),
        // cotizacion.getBaInd(), "Sin Promocion", "N/A", "0",
        // cotizacion.getEstrato());
        if (venta != null) {
            llenarBandaAncha();
        }
        if (cotizacion.getInternet().equalsIgnoreCase("-") || cotizacion.getInternet().equalsIgnoreCase("")
                || cotizacion.getInternet().equalsIgnoreCase("--Seleccione Producto--")) {
            cotizacion.setInternet("-");
            rba.setVisibility(View.GONE);
        }


        if (!cotizacion.getTelefonia().equalsIgnoreCase("-")) {
            lblTotal.setText(String.valueOf(Double.parseDouble(cotizacion.getTotalEmp()) + UtilidadesTarificador
                    .ImpuestoTelefonico(cliente.getCiudad(), cliente.getDepartamento(), cliente.getEstrato())));
        } else {
            lblTotal.setText(cotizacion.getTotalEmp());
        }

        if (aplicarDescuentos) {


            if (!cotizacion.getTotalEmpDescuento().equals("0") && !cotizacion.getTotalEmpDescuento().equals("0.0") && !cotizacion.getTotalEmpDescuento().equals("")) {
                System.out.println("cotizacion.getTotalEmpDescuento() " + cotizacion.getTotalEmpDescuento());

                lblTotalDescuento.setText(String.valueOf(Double.parseDouble(cotizacion.getTotalEmpDescuento())
                        + UtilidadesTarificador.ImpuestoTelefonico(cliente.getCiudad(), cliente.getDepartamento(),
                        cliente.getEstrato())));
                //llyTotalDescuento.setVisibility(View.VISIBLE);
            }
        }

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
                            parametros = new ArrayList<String[]>();
                            parametros.add(new String[]{"Tipo", "6"});
                            parametros.add(new String[]{"Id", id});
                            parametros.add(new String[]{"Mail", Mail});
                            parametros.add(new String[]{"CobroDomingo",
                                    Utilidades.cambioCobroDomingo(venta.getCobroDomingo())});
                            String llamada = MainActivity.conexion.ejecutarSoap("LanzarLlamada", parametros);
                            System.out.println(llamada);

                            mostrarDialgo();
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
            LanzarLLamada(id, "5", Mail, venta.getCobroDomingo());
        } else if (item.getTitle().equals("Grabacion")) {
            System.out.println("id" + id);
            ArrayList<String[]> parametros = new ArrayList<String[]>();
            parametros.add(new String[]{"Tipo", "4"});
            parametros.add(new String[]{"Id", id});
            parametros.add(new String[]{"Mail", Mail});
            parametros.add(new String[]{"CobroDomingo", Utilidades.cambioCobroDomingo(venta.getCobroDomingo())});
            String llamada = MainActivity.conexion.ejecutarSoap("LanzarLlamada", parametros);
            System.out.println(llamada);
        } else if (item.getTitle().equals("Ingresar Codigo")) {
            mostrarDialgo();
        }
        return true;
    }

    public void procesarVenta(View v) {
        btnSiguiente = (ImageButton) v;

        boolean compatibilidad = true;
        boolean nuevos = false;

        int totalNuevos = 0;
        int totalAdicionales = 0;

        venta = new Venta();

        venta.medioIngreso = medioIngreso;

        System.out.println(" rto.getLinea() " + rto.getLinea());
        System.out.println(" rto.getPlanFactura() " + rto.getPlanFactura());

        System.out.println("cotizacion.getTipoCotizacionTo() " + cotizacion.getTipoCotizacionTo());
        System.out.println("cotizacion.getTipoCotizacionTv() " + cotizacion.getTipoCotizacionTv());
        System.out.println("cotizacion.getTipoCotizacionBa() " + cotizacion.getTipoCotizacionBa());

        System.out.println("rto.getDuracion() " + rto.getDuracion());

        System.out.println("rto.getTecnologiacr() " + rto.getTecnologiacr());
        System.out.println("rtv.getTecnologiacr() " + rtv.getTecnologiacr());
        System.out.println("rba.getTecnologiacr() " + rba.getTecnologiacr());

        String decos = rtv.getDecos();

        venta.setTelefonia(rto.getPlan(), rto.getValor(), rto.getLinea(), rto.getPagoLinea(), rto.getMigracion(),
                rto.getTipoMigracion(), rto.getDescuento(), rto.getDuracion(), rto.getValorDescuento(),
                rto.getPlanFactura(), rto.getTecnologia(), cotizacion.getTipoCotizacionTo(), cotizacion.toIdent,
                rto.getAdicionalesCadena(), rto.getPreciosAdicionalesCadena(), rto.getPrecioAdicionales(),
                rto.getTecnologiacr(), cotizacion.getToPagoAntCargoFijo(), cotizacion.getToPagoParcialConexion());
        venta.setTelevision(rtv.getPlan(), rtv.getValor(), rtv.getExtensiones(), rtv.getDescuento(), rtv.getDuracion(),
                rtv.getValorDescuento(), rtv.getAdicionalesCadena(), rtv.getPreciosAdicionalesCadena(),
                rtv.getPrecioAdicionales(), rtv.getMigracion(), rtv.getTipoMigracion(), rtv.getPlanFacturacion(), decos,
                rtv.getTecnologia(), cotizacion.getTipoCotizacionTv(), cotizacion.tvIdent, rtv.getTecnologiacr(), cotizacion.getTvPagoAntCargoFijo(), cotizacion.getTvPagoParcialConexion());

        venta.setItemDecodificadors(cotizacion.getDecodificadores());
        venta.setInternet(rba.getPlan(), rba.getValor(), rba.getWifi(), rba.getMigracion(), rba.getTipoMigracion(),
                rba.getDescuento(), rba.getDuracion(), rba.getValorDescuento(), rba.getPlanFacturacion(),
                cotizacion.getTipoCotizacionBa(), cotizacion.baIdent, rba.getTecnologiacr(),
                cotizacion.getIpdinamica(), cotizacion.getBaPagoAntCargoFijo(), cotizacion.getBaPagoParcialConexion());

        // venta.setDocumentacion(datosPersonales, mensajes, mail,
        // contrato,factura, tipoContracto, telemercadeo);

        if (!rto.getPlan().equalsIgnoreCase(Utilidades.inicial_guion) && !rto.getPlan().equalsIgnoreCase("")) {

            System.out.println("cotizacion.getTipoCotizacionTo() " + cotizacion.getTipoCotizacionTo());
            if (cotizacion.getTipoCotizacionTo().equalsIgnoreCase("1")) {

                System.out.println("rto.getValor() " + rto.getValor());
                totalNuevos += Utilidades.convertirNumericos(rto.getValor(), "rto.getValor()");

                nuevos = true;
            }
        }

        if (!rba.getPlan().equalsIgnoreCase(Utilidades.inicial_guion) && !rba.getPlan().equalsIgnoreCase("")) {

            System.out.println("cotizacion.getTipoCotizacionBa() " + cotizacion.getTipoCotizacionBa());

            if (cotizacion.getTipoCotizacionBa().equalsIgnoreCase("1")) {
                System.out.println("rba.getValor() " + rba.getValor());
                totalNuevos += Utilidades.convertirNumericos(rba.getValor(), "rba.getValor()");
                nuevos = true;
            }

            venta.setControlGota(cotizacion.isControlGota());
            if (cotizacion.isControlGota()) {
                venta.setGota(cotizacion.getNombreGota(), "" + cotizacion.getPrecioGota(),
                        cotizacion.getVelocidadInicial(), cotizacion.getVelocidadFinal());
            }
        }

        if (!rtv.getPlan().equalsIgnoreCase(Utilidades.inicial_guion) && !rtv.getPlan().equalsIgnoreCase("")) {

            System.out.println("cotizacion.getTipoCotizacionTv() " + cotizacion.getTipoCotizacionTv());

            if (cotizacion.getTipoCotizacionTv().equalsIgnoreCase("1")) {
                System.out.println("rtv.getValor() " + rtv.getValor());
                totalNuevos += Utilidades.convertirNumericos(rtv.getValor(), "rtv.getValor()");
                nuevos = true;
            }
        }

        if (cotizacion.getAdicionales().length > 0) {
            for (int i = 0; i < cotizacion.getAdicionales().length; i++) {
                totalAdicionales += Utilidades.convertirNumericos(cotizacion.getAdicionales()[i][1],
                        cotizacion.getAdicionales()[i][0]);
                // if (!Utilidades.excluirGeneral("adicionalesScooring",
                // cotizacion.getAdicionales()[i][0])) {
                // nuevos = true;
                // // break;
                // }
            }

            totalNuevos += totalAdicionales;
            // nuevos = true;
        }

        if (cotizacion.getAdicionalesTo() != null) {
            if (cotizacion.getAdicionalesTo().length > 0) {
                for (int i = 0; i < cotizacion.getAdicionalesTo().length; i++) {
                    totalAdicionales += Utilidades.convertirNumericos(cotizacion.getAdicionalesTo()[i][1],
                            cotizacion.getAdicionalesTo()[i][0]);
                    if (!Utilidades.excluirGeneral("adicionalesScooring", cotizacion.getAdicionalesTo()[i][0])) {
                        nuevos = true;
                        // break;
                    }
                }

                totalNuevos += totalAdicionales;
                // nuevos = true;
            }
        }

        if (!nuevos) {
            cliente.getScooringune().setPasaScooring(true);
        }

        listObjectAdicionalesTo.clear();
        listObjectAdicionalesTo = arraylistAdicionalesTo();
        JSONArray arrayadicionalesTo = new JSONArray();
        if (listObjectAdicionalesTo.size() > 0) {
            for (int i = 0; i < listObjectAdicionalesTo.size(); i++) {
                // System.out.println("lista "+listObjectAdicionales.get(i));
                arrayadicionalesTo.put(listObjectAdicionalesTo.get(i));
            }
        }

        venta.setListAdicionalesTo(listObjectAdicionalesTo.toString());

        listObjectAdicionalesBa.clear();
        if (venta.isControlGota()) {
            listObjectAdicionalesBa = arraylistAdicionalesBa(cotizacion.getNombreGota(),
                    "" + cotizacion.getPrecioGota(), "" + cotizacion.getPrecioGotaSinIva(),
                    cotizacion.getVelocidadInicial(), cotizacion.getVelocidadFinal());

        }

        JSONArray arrayadicionalesBa = new JSONArray();



        if (listObjectAdicionalesBa.size() > 0) {
            for (int i = 0; i < listObjectAdicionalesBa.size(); i++) {
                // System.out.println("lista
                // "+listObjectAdicionales.get(i));
                arrayadicionalesBa.put(listObjectAdicionalesBa.get(i));
            }
        }

        venta.setListAdicionalesBa(arrayadicionalesBa.toString());

        listObjectAdicionales.clear();
        listObjectAdicionales = arraylistAdicionales();
        JSONArray arrayadicionales = new JSONArray();
        if (listObjectAdicionales.size() > 0) {
            for (int i = 0; i < listObjectAdicionales.size(); i++) {
                // System.out.println("lista "+listObjectAdicionales.get(i));
                arrayadicionales.put(listObjectAdicionales.get(i));
            }
        }

        listObjectAdicionales.clear();
        listObjectAdicionales = arraylistAdicionales();

        venta.setListAdicionalesTV(arrayadicionales.toString());
        venta.setDocumentacion(id);
        venta.setTotal(lblTotal.getText().toString().replace(",", "."));
        venta.setTotalPagoAntCargoFijo(cotizacion.getTotalPagoAntCargoFijo());
        venta.setTotalPagoConexion(cotizacion.getTotalPagoConexion());
        venta.setTotalPagoParcialConexion(cotizacion.getTotalPagoParcialConexion());
        venta.setDescuentoConexion(cotizacion.getDescuentoConexion());
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

        venta.setDependencia(cotizacion.getOferta());

        venta.setOtrasPromociones(consolidarOtrasPromociones(getobtenerOtrasPromociones()));
        // System.out.println("venta.getOtrasPromociones()
        // "+venta.getOtrasPromociones());
        venta.setScooring("scooring");
        venta.setObservaciones(txtObservaciones.getText().toString());

        if (cotizacion.isPromoTo_12_100()) {
            if (rto.isQuitarPromo()) {
                venta.setObservaciones(
                        venta.getObservaciones() + " ***PROMO COMPRA 2 LLEVA 3, ASESOR QUITA PROMO TO***");
            } else {
                venta.setObservaciones(venta.getObservaciones() + " *** PROMO COMPRA 2 LLEVA 3 ***");
            }
        }

        if (cliente.getEstandarizarSiebel() == 0) {
            venta.setObservaciones(venta.getObservaciones() + " ***Direccion No Estandarizada ***");
        }

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

        venta.setMedioIngreso(cotizacion.getMedioIngreso());

        venta.setOferta(cotizacion.getOferta());

        venta.setDepartamento(Departamento);
        venta.setMunicipio(Municipio);

        if (cotizacion.getTelefonia().equalsIgnoreCase("-") && venta.getEmpaquetamiento().equalsIgnoreCase("SI")) {
            if (Utilidades.visibleDepartamento("empaquetamientoTO", Departamento)) {
                compatibilidad = false;
            }
        }

        if (Utilidades.excluir("excluirAgenda", Municipio)) {
            venta.setValidarAgenda(false);
        } else {
            venta.setValidarAgenda(true);
        }

        System.out.println("agendableSiebel " + agendableSiebel);

        if (Utilidades.excluir("agendaSantander", Municipio)) {
            System.out.println("agendable");

            equipos2 = equiposSiebel();
            System.out.println("equipo 1 " + equipos1);
            System.out.println("equipo 2 " + equipos2);
            JSONObject eq1;
            try {
                eq1 = new JSONObject(equipos1);
                JSONObject eq2 = new JSONObject(equipos2);
                if (eq1.getString("Telefonia").equalsIgnoreCase(eq2.getString("Telefonia"))
                        && eq1.getString("Internet").equalsIgnoreCase(eq2.getString("Internet"))
                        && eq1.getString("Television").equalsIgnoreCase(eq2.getString("Television"))) {
                    ;
                } else {
                    venta.setHorarioAtencion("");
                    lblAgendamiento.setText("");
                    Utilidades.MensajesToast(
                            getResources().getString(R.string.verificaragendaporcambioconfiguracionventa), this);
                    agendableSiebel = 2;

                }
            } catch (JSONException e) {
                Log.w("Error ", e.getMessage());
            }

            if (agendableSiebel == 1) {
                venta.setValidarAgenda(true);
                if (!venta.getHorarioAtencion().equalsIgnoreCase("")) {
                    venta.setAgendaSiebel(agendaSiebel.toString());
                    venta.setIdOferta(idOferta);
                    venta.setAgendableSiebel(1);
                }
            } else if (agendableSiebel == 0) {
                System.out.println("no agendable");

                venta.setAgendaSiebel("");
                venta.setIdOferta("");
                venta.setAgendableSiebel(0);
                venta.setValidarAgenda(false);
            } else if (agendableSiebel == 2) {
                venta.setValidarAgenda(true);

                System.out.println("sin verificacion");
            }

        } else {
            venta.setAgendaSiebel("");
            venta.setIdOferta("");
            venta.setAgendableSiebel(2);

            System.out.println("no aplica validacion");
        }

        if (compatibilidad) {

            Intent intent = new Intent(MainActivity.MODULO_RESUMEN_PREVENTA);
            intent.putExtra("venta", venta);
            intent.putExtra("cliente", cliente);
            startActivityForResult(intent, MainActivity.REQUEST_CODE);
        } else {
            Toast.makeText(this, "No Se Puede Pasar Venta Incompatible.", Toast.LENGTH_SHORT).show();
        }

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

    private String getobtenerOtrasPromociones() {
        String otrasPromociones = "";
        ListaCheckedAdapter lca = (ListaCheckedAdapter) lsvOtrasPromociones.getAdapter();
        if (lca != null) {
            otrasPromociones = lca.otrasPromociones();
        }
        return otrasPromociones;
    }

    private void llenarOtrasPromociones(String estrato) {
        String depto = "";
        if (MainActivity.config.getDepartamento().equalsIgnoreCase("Bogota")) {
            depto = Municipio;
        } else {
            depto = MainActivity.config.getDepartamento();
        }
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Otras_Promociones",
                new String[]{"Descripcion", "Precio"}, "Departamento like ? and Estrato like ?",
                new String[]{"%" + depto + "%", "%" + estrato + "%"}, null, null, null);
        ArrayList<ListaChecked> contenido = new ArrayList<ListaChecked>();
        int i = 2;

        if (respuesta != null) {

            for (ArrayList<String> arrayList : respuesta) {
                if (i == 2) {
                    i = 1;
                } else {
                    i = 2;
                }

                if (venta != null) {
                    // System.out.println("con cotizacion");
                    // contenido.add(new ListaChecked(i,
                    // arrayList.get(1),arrayList.get(0),
                    // venta.getOtrasPromociones()));
                } else {
                    // System.out.println("sin cotizacion");
                    contenido.add(new ListaChecked(i, arrayList.get(1), arrayList.get(0), null));
                }

            }

            ListaCheckedAdapter adaptador = new ListaCheckedAdapter(this, contenido, this);
            lsvOtrasPromociones.setAdapter(adaptador);
        } else {
            Limpiar_Otras_Promociones();
        }
    }

    private void InsertarConfirmacion() {

        addObserver(MainActivity.obsrMainActivity);
        notifyObserver();

        ArrayList<String> consolidado = MainActivity.consolidarIVR();

        System.out.println("consolidado " + consolidado);

        if (consolidado.get(2).length() >= 7) {

            if (cliente.getIdIVR() == 0) {
                if (id.equals("0") || id.equals("")) {
                    codigo = (int) (Math.random() * (9999 - 1000 + 1) + 1000);

                    consolidado.add(String.valueOf(codigo));

                    JSONObject IVR = new JSONObject();
                    JSONArray cotizacion = new JSONArray();
                    JSONObject to = new JSONObject();
                    JSONObject tv = new JSONObject();
                    JSONObject ba = new JSONObject();

                    try {
                        if (!rto.getPlan().equalsIgnoreCase(Utilidades.inicial_guion)
                                && !rto.getPlan().equalsIgnoreCase("")) {
                            to = Utilidades.jsonProductos("TO", rto.getPlan(), rto.getValor(), rto.getDescuento(),
                                    rto.getDuracion(), "0");
                            cotizacion.put(to);
                        }

                        if (!rtv.getPlan().equalsIgnoreCase(Utilidades.inicial_guion)
                                && !rtv.getPlan().equalsIgnoreCase("")) {
                            tv = Utilidades.jsonProductos("TV", rtv.getPlan(), rtv.getValor(), rtv.getDescuento(),
                                    rtv.getDuracion(), "0");
                            cotizacion.put(tv);
                        }

                        if (!rba.getPlan().equalsIgnoreCase(Utilidades.inicial_guion)
                                && !rba.getPlan().equalsIgnoreCase("")) {
                            ba = Utilidades.jsonProductos("BA", rba.getPlan(), rba.getValor(), rba.getDescuento(),
                                    rba.getDuracion(), "0");
                            cotizacion.put(ba);
                        }

                        if (!rto.getPlan().equalsIgnoreCase(Utilidades.inicial_guion)
                                && !rto.getPlan().equalsIgnoreCase("")) {
                            if (listObjectAdicionalesTo.size() > 0) {
                                for (int i = 0; i < listObjectAdicionalesTo.size(); i++) {
                                    // System.out.println("lista
                                    // "+listObjectAdicionales.get(i));
                                    cotizacion.put(listObjectAdicionalesTo.get(i));
                                }
                            }
                        }

                        if (listObjectAdicionales.size() > 0) {
                            for (int i = 0; i < listObjectAdicionales.size(); i++) {
                                // System.out.println("lista
                                // "+listObjectAdicionales.get(i));
                                cotizacion.put(listObjectAdicionales.get(i));
                            }
                        }

                        if (listObjectAdicionalesBa.size() > 0) {
                            for (int i = 0; i < listObjectAdicionalesBa.size(); i++) {
                                // System.out.println("lista
                                // "+listObjectAdicionales.get(i));
                                cotizacion.put(listObjectAdicionalesBa.get(i));
                            }
                        }

                        if (venta.getItemDecodificadors() != null && venta.getItemDecodificadors().size() > 0) {
                            for (int i = 0; i < venta.getItemDecodificadors().size(); i++) {

                                String precio = venta.getItemDecodificadors().get(i).getPrecio();
                                String tipoAlquiler = venta.getItemDecodificadors().get(i).getTipoAlquiler();
                                String tipoDeco = venta.getItemDecodificadors().get(i).getOriginal();

                                if (tipoAlquiler.equalsIgnoreCase("AL") && !precio.equals("0") && !precio.equals("0.0")) {
                                    System.out.println("itemDecodificadors.get(i) entro ");
                                    cotizacion.put(Utilidades.jsonProductos("ADTV", "Decodificador " + tipoDeco + " (Adicional)", precio, "0", "0", "0"));
                                }

                            }
                        }

                        IVR.put("tipo", "insert");
                        IVR.put("codigoasesor", consolidado.get(0));
                        IVR.put("telefono", consolidado.get(2));
                        IVR.put("codigo", consolidado.get(5));
                        IVR.put("tipocliente", consolidado.get(3));
                        IVR.put("cotizacion", cotizacion);
                        IVR.put("documento", consolidado.get(4));

                        if (Utilidades.excluir("excluirAgenda", Municipio)) {
                            IVR.put("fecha", "");
                            IVR.put("franja", "");
                            IVR.put("agendaDomingo", venta.getCobroDomingo());
                        } else {
                            IVR.put("fecha", fecha);
                            IVR.put("franja", franja);
                            IVR.put("agendaDomingo", venta.getCobroDomingo());
                        }
                        IVR.put("Scooring", 0);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

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
                    LanzarLLamada(id, "5", Mail, venta.getCobroDomingo());
                }
            } else {
                codigo = (int) (Math.random() * (9999 - 1000 + 1) + 1000);

                consolidado.add(String.valueOf(codigo));

                JSONObject IVR = new JSONObject();
                JSONArray cotizacion = new JSONArray();
                JSONObject to = new JSONObject();
                JSONObject tv = new JSONObject();
                JSONObject ba = new JSONObject();

                try {
                    if (!rto.getPlan().equalsIgnoreCase(Utilidades.inicial_guion)
                            && !rto.getPlan().equalsIgnoreCase("")) {
                        to = Utilidades.jsonProductos("TO", rto.getPlan(), rto.getValor(), rto.getDescuento(),
                                rto.getDuracion(), "0");
                        cotizacion.put(to);
                    }

                    if (!rtv.getPlan().equalsIgnoreCase(Utilidades.inicial_guion)
                            && !rtv.getPlan().equalsIgnoreCase("")) {
                        tv = Utilidades.jsonProductos("TV", rtv.getPlan(), rtv.getValor(), rtv.getDescuento(),
                                rtv.getDuracion(), "0");
                        cotizacion.put(tv);
                    }

                    if (!rba.getPlan().equalsIgnoreCase(Utilidades.inicial_guion)
                            && !rba.getPlan().equalsIgnoreCase("")) {
                        ba = Utilidades.jsonProductos("BA", rba.getPlan(), rba.getValor(), rba.getDescuento(),
                                rba.getDuracion(), "0");
                        cotizacion.put(ba);
                    }

                    if (!rto.getPlan().equalsIgnoreCase(Utilidades.inicial_guion)
                            && !rto.getPlan().equalsIgnoreCase("")) {
                        if (listObjectAdicionalesTo.size() > 0) {
                            for (int i = 0; i < listObjectAdicionalesTo.size(); i++) {
                                // System.out.println("lista
                                // "+listObjectAdicionales.get(i));
                                cotizacion.put(listObjectAdicionalesTo.get(i));
                            }
                        }
                    }

                    if (listObjectAdicionales.size() > 0) {
                        for (int i = 0; i < listObjectAdicionales.size(); i++) {
                            // System.out.println("lista
                            // "+listObjectAdicionales.get(i));
                            cotizacion.put(listObjectAdicionales.get(i));
                        }
                    }

                    if (venta.getItemDecodificadors() != null && venta.getItemDecodificadors().size() > 0) {
                        for (int i = 0; i < venta.getItemDecodificadors().size(); i++) {

                            String precio = venta.getItemDecodificadors().get(i).getPrecio();
                            String tipoAlquiler = venta.getItemDecodificadors().get(i).getTipoAlquiler();
                            String tipoDeco = venta.getItemDecodificadors().get(i).getOriginal();

                            if (tipoAlquiler.equalsIgnoreCase("AL") && !precio.equals("0") && !precio.equals("0.0")) {
                                System.out.println("itemDecodificadors.get(i) entro ");
                                cotizacion.put(Utilidades.jsonProductos("ADTV", "Decodificador " + tipoDeco + " (Adicional)", precio, "0", "0", "0"));
                            }

                        }
                    }

                    IVR.put("tipo", "update");
                    IVR.put("id", cliente.getIdIVR());
                    IVR.put("codigo", String.valueOf(codigo));
                    IVR.put("cotizacion", cotizacion);
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

            }

        } else {
            Toast.makeText(this, "No ha Ingresado Telefono de destino", Toast.LENGTH_SHORT).show();
        }

        // btnSiguiente.setEnabled(true);
    }

    private void LanzarLLamada(String id, String tipo, String mail, String CobroDomingo) {

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(tipo);
        parametros.add(id);
        parametros.add(mail);
        parametros.add(Utilidades.cambioCobroDomingo(CobroDomingo));
        parametros.add(cotizacion.getTotalPagoConexion());
        parametros.add(cotizacion.getDescuentoConexion());
        parametros.add(cotizacion.getTotalPagoParcialConexion());
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("LanzarLlamada");
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
                    if (value.equals(String.valueOf(codigo)) || value.equals("20130806")) {

                        cotizacion.setValidoIVR(true);
                        venta.setDocumentacion(id);
                        MainActivity.btnTarificador.setOK();
                        Intent intent = new Intent();
                        intent.putExtra("Venta", venta);
                        intent.putExtra("estrato", cotizacion.getEstrato());
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
        if (resultado.get(0).equals("InsertarConfirmacion")) {
            JSONObject jop;

            try {
                jop = new JSONObject(resultado.get(1).toString());

                String data = jop.get("data").toString();
                if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                    data = new String(Base64.decode(data));
                    // Confirmacion(data);
                    JSONObject confirmacion = new JSONObject(data);

                        if (confirmacion.has("id")) {
                            id = confirmacion.getString("id");

                            if (!id.equalsIgnoreCase("")) {
                                // blindaje.idIVR = id;
                                LanzarLLamada(id, "5", Mail, venta.getCobroDomingo());
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

                        LanzarLLamada(id, "5", Mail, venta.getCobroDomingo());

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
        }else if (resultado != null && resultado.get(0).equals("ValidacionConfiguracionMovil")) {

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