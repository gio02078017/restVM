package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import com.google.analytics.tracking.android.EasyTracker;

import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.adapters.ListaDefaultAdapter;
import co.com.une.appmovilesune.adapters.ListaVentas;
import co.com.une.appmovilesune.adapters.ListaVentasAdapter;
import co.com.une.appmovilesune.change.Busqueda;
import co.com.une.appmovilesune.change.Interprete;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.ResumenTarificador;
import co.com.une.appmovilesune.components.SelectorFecha;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class ControlPedido extends Activity implements Observer {
    private TituloPrincipal tp;
    private Busqueda buscarPed, buscarVen, buscarBlindaje, buscarGerencia;
    private EditText txtLoginRed, txtPasswordRed;
    private Spinner spnTipoUsuario;
    private ListView viewEstadoPedido, lstListaVentas;
    private TextView lblRespuesta, lblEstado, lblPedido, lblObservaciones, lblObservacionesBlindaje, lblEstadoBlindaje;

    private TableRow tlytTablaVentasRespuesta, tlytTablaVentasEstado, tlytTablaVentasPedido,
            tlytTablaVentasObservaciones, trTablaVentasLoginRed, trTablaVentasPasswordRed;

    private TableRow tlytTablaGerenciaRespuesta, tlytTablaGerenciaEstado, tlytTablaGerenciaObservaciones,
            trTablaGerenciaLoginRed, trTablaGerenciaPasswordRed, tlytTablaGerenciaGestion;

    private EditText txtLoginRedGerencia, txtPasswordRedGerencia;
    private Spinner spnTipoUsuarioGerencia;

    private TextView lblRespuestaGerencia, lblEstadoGerencia, lblGestionGerencia, lblObservacionesGerencia;

    ListaDefaultAdapter adapter;

    ArrayList<ListaDefault> estadopedido = new ArrayList<ListaDefault>();

    private TabHost tabs;
    private TabHost.TabSpec spec;

    public SelectorFecha sfp;
    private Dialogo dialogo;

    Cliente cliente;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpedido);

        Bundle reicieveParams = getIntent().getExtras();

        // if (reicieveParams != null) {
        // cliente = (Cliente) reicieveParams.getSerializable("cliente");
        // }

        viewEstadoPedido = (ListView) findViewById(R.id.viewEstadoPedido);
        lblRespuesta = (TextView) findViewById(R.id.lblRespuesta);
        lblEstado = (TextView) findViewById(R.id.lblEstado);
        lblPedido = (TextView) findViewById(R.id.lblPedido);
        lblObservaciones = (TextView) findViewById(R.id.lblObservaciones);

        lblRespuestaGerencia = (TextView) findViewById(R.id.lblRespuestaGerencia);
        lblEstadoGerencia = (TextView) findViewById(R.id.lblEstadoGerencia);
        lblGestionGerencia = (TextView) findViewById(R.id.lblGestionGerencia);
        lblObservacionesGerencia = (TextView) findViewById(R.id.lblObservacionesGerencia);

        lblEstadoBlindaje = (TextView) findViewById(R.id.lblEstadoBlindaje);
        lblObservacionesBlindaje = (TextView) findViewById(R.id.lblObservacionesBlindaje);

        tlytTablaGerenciaRespuesta = (TableRow) findViewById(R.id.tlytTablaGerenciaRespuesta);
        tlytTablaGerenciaEstado = (TableRow) findViewById(R.id.tlytTablaGerenciaEstado);
        tlytTablaGerenciaGestion = (TableRow) findViewById(R.id.tlytTablaGerenciaGestion);
        tlytTablaGerenciaObservaciones = (TableRow) findViewById(R.id.tlytTablaGerenciaObservaciones);
        trTablaGerenciaLoginRed = (TableRow) findViewById(R.id.trTablaGerenciaLoginRed);
        trTablaGerenciaPasswordRed = (TableRow) findViewById(R.id.trTablaGerenciaPasswordRed);

        tlytTablaVentasRespuesta = (TableRow) findViewById(R.id.tlytTablaVentasRespuesta);
        tlytTablaVentasEstado = (TableRow) findViewById(R.id.tlytTablaVentasEstado);
        tlytTablaVentasPedido = (TableRow) findViewById(R.id.tlytTablaVentasPedido);
        tlytTablaVentasObservaciones = (TableRow) findViewById(R.id.tlytTablaVentasObservaciones);
        trTablaVentasLoginRed = (TableRow) findViewById(R.id.trTablaVentasLoginRed);
        trTablaVentasPasswordRed = (TableRow) findViewById(R.id.trTablaVentasPasswordRed);

        txtLoginRed = (EditText) findViewById(R.id.txtLoginRed);
        txtPasswordRed = (EditText) findViewById(R.id.txtPasswordRed);
        spnTipoUsuario = (Spinner) findViewById(R.id.spnTipoUsuario);

        txtLoginRedGerencia = (EditText) findViewById(R.id.txtLoginRedGerencia);
        txtPasswordRedGerencia = (EditText) findViewById(R.id.txtPasswordRedGerencia);
        spnTipoUsuarioGerencia = (Spinner) findViewById(R.id.spnTipoUsuarioGerencia);

        tlytTablaVentasRespuesta.setVisibility(View.GONE);
        tlytTablaVentasEstado.setVisibility(View.GONE);
        tlytTablaVentasPedido.setVisibility(View.GONE);
        tlytTablaVentasObservaciones.setVisibility(View.GONE);
        trTablaVentasLoginRed.setVisibility(View.GONE);
        trTablaVentasPasswordRed.setVisibility(View.GONE);

        tlytTablaGerenciaRespuesta.setVisibility(View.GONE);
        tlytTablaGerenciaEstado.setVisibility(View.GONE);
        tlytTablaGerenciaGestion.setVisibility(View.GONE);
        tlytTablaGerenciaObservaciones.setVisibility(View.GONE);
        trTablaGerenciaLoginRed.setVisibility(View.GONE);
        trTablaGerenciaPasswordRed.setVisibility(View.GONE);

        lstListaVentas = (ListView) findViewById(R.id.lstListaVentas);

        dialogo = new Dialogo(this, Dialogo.DIALOGO_FECHA, "");
        dialogo.dialogo.setOnDismissListener(dl);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("Estados");

        Resources res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        spec = tabs.newTabSpec("Pedido");
        spec.setContent(R.id.llyPedido);
        spec.setIndicator("Pedido", res.getDrawable(R.drawable.estadopedido));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Venta");
        spec.setContent(R.id.llyVenta);
        spec.setIndicator("Venta", res.getDrawable(R.drawable.estadopedido));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Gerencia");
        spec.setContent(R.id.llyGerencia);
        spec.setIndicator("Gerencia", res.getDrawable(R.drawable.estadopedido));
        tabs.addTab(spec);

        // if(habilitarListaVentas()){
        spec = tabs.newTabSpec("Ventas");
        spec.setContent(R.id.llyConsultaVentas);
        spec.setIndicator("Lista Ventas", res.getDrawable(R.drawable.estadopedido));
        tabs.addTab(spec);
        // }

        // spec = tabs.newTabSpec("VentaBlindaje");
        // spec.setContent(R.id.llyVentaBlindaje);
        // spec.setIndicator("Blindaje",
        // res.getDrawable(R.drawable.estadopedido));
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
                /*
				 * tp.setTitulo("Cotizaci�n " + tabId);
				 * 
				 * if (tabId.equalsIgnoreCase("Resultado")) { consultar(); }
				 */

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

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                Utilidades.tipoUsuario);
        spnTipoUsuario.setAdapter(adaptador);

        spnTipoUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                String tipoUsuario = parent.getSelectedItem().toString();

                if (tipoUsuario.equals("Asesor")) {
                    trTablaVentasLoginRed.setVisibility(View.GONE);
                    trTablaVentasPasswordRed.setVisibility(View.GONE);
                    txtLoginRed.setText("");
                    txtPasswordRed.setText("");
                } else if (tipoUsuario.equals("Supervisor")) {
                    trTablaVentasLoginRed.setVisibility(View.VISIBLE);
                    trTablaVentasPasswordRed.setVisibility(View.VISIBLE);
                    txtLoginRed.requestFocus();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // lblMensaje.setText("");
            }
        });

        spnTipoUsuarioGerencia.setAdapter(adaptador);

        spnTipoUsuarioGerencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                String tipoUsuario = parent.getSelectedItem().toString();

                if (tipoUsuario.equals("Asesor")) {
                    trTablaGerenciaLoginRed.setVisibility(View.GONE);
                    trTablaGerenciaPasswordRed.setVisibility(View.GONE);
                    txtLoginRedGerencia.setText("");
                    txtPasswordRedGerencia.setText("");
                } else if (tipoUsuario.equals("Supervisor")) {
                    trTablaGerenciaLoginRed.setVisibility(View.VISIBLE);
                    trTablaGerenciaPasswordRed.setVisibility(View.VISIBLE);
                    txtLoginRedGerencia.requestFocus();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // lblMensaje.setText("");
            }
        });

        buscarPed = (Busqueda) findViewById(R.id.busquedaPedido);

        if (Utilidades.excluir("siebelMunicipios", MainActivity.config.getCiudad())) {
            buscarPed.cambiarTipo("text");
        } else {
            buscarPed.cambiarTipo("");
        }

        buscarPed.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Resultado_Cartera();
                Pedido();
            }
        });

        buscarVen = (Busqueda) findViewById(R.id.busquedaVenta);
        buscarVen.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Resultado_Cartera();
                Venta();
            }
        });

        buscarGerencia = (Busqueda) findViewById(R.id.busquedaGerencia);
        buscarGerencia.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Resultado_Cartera();
                Gerencia();
            }
        });

        buscarBlindaje = (Busqueda) findViewById(R.id.busquedaVentaBlindaje);
        buscarBlindaje.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Resultado_Cartera();
                Blindaje();
            }
        });

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

    public void Pedido() {
        ArrayList<String> parametros = new ArrayList<String>();

        parametros.add(buscarPed.getBusqueda());

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());

        if (Utilidades.excluir("siebelMunicipios", MainActivity.config.getCiudad())) {
            params.add("EstadoOferta");
        } else {
            params.add("EstadoPedido");
        }

        params.add(parametros);

        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void Venta() {
        ArrayList<String> parametros = new ArrayList<String>();

        parametros.add(buscarVen.getBusqueda());
        parametros.add(txtLoginRed.getText().toString());
        parametros.add(txtPasswordRed.getText().toString());

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("EstadoVenta");
        params.add(parametros);

        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void Gerencia() {
        ArrayList<String> parametros = new ArrayList<String>();

        parametros.add(buscarGerencia.getBusqueda());
        parametros.add(txtLoginRedGerencia.getText().toString());
        parametros.add(txtPasswordRedGerencia.getText().toString());

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("EstadoGerencia");
        params.add(parametros);

        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    public void Blindaje() {
        ArrayList<String> parametros = new ArrayList<String>();

        parametros.add(buscarBlindaje.getBusqueda());

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("EstadoBlindaje");
        params.add(parametros);

        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    @Override
    public void update(Object value) {
        ArrayList<Object> resultado = (ArrayList<Object>) value;
        if (resultado.get(0).equals("EstadoPedido")) {
            // cliente.setPortafolio(resultado.get(1).toString());
			/*
			 * System.out.println("Resultado EstadoPedido" +
			 * resultado.get(1).toString());
			 */
            Resultado_EstadoPedido(resultado.get(1).toString());
            // Resultado_Portafolio(cliente.getPortafolio());
        } else if (resultado.get(0).equals("EstadoOferta")) {
            // cliente.setPortafolio(resultado.get(1).toString());
			/*
			 * System.out.println("Resultado EstadoPedido" +
			 * resultado.get(1).toString());
			 */
            Resultado_EstadoOferta(resultado.get(1).toString());
            // Resultado_Portafolio(cliente.getPortafolio());
        } else if (resultado.get(0).equals("EstadoVenta")) {
            // cliente.setPortafolio(resultado.get(1).toString());
			/*
			 * System.out.println("Resultado EstadoVenta" +
			 * resultado.get(1).toString());
			 */
            // Resultado_EstadoPedido(resultado.get(1).toString());
            // Resultado_Portafolio(cliente.getPortafolio());
            ResultadoEstadoVenta(resultado.get(1).toString());
        } else if (resultado.get(0).equals("EstadoGerencia")) {
            // cliente.setPortafolio(resultado.get(1).toString());
			/*
			 * System.out.println("Resultado EstadoVenta" +
			 * resultado.get(1).toString());
			 */
            // Resultado_EstadoPedido(resultado.get(1).toString());
            // Resultado_Portafolio(cliente.getPortafolio());
            ResultadoEstadoGerencia(resultado.get(1).toString());
        } else if (resultado.get(0).equals("EstadoBlindaje")) {
            // cliente.setPortafolio(resultado.get(1).toString());
            System.out.println("Resultado EstadoBlindaje" + resultado.get(1).toString());
            // Resultado_EstadoPedido(resultado.get(1).toString());
            // Resultado_Portafolio(cliente.getPortafolio());
            // ResultadoEstadoVenta(resultado.get(1).toString());
            RespuestaBlindaje(resultado.get(1).toString());
        } else if (resultado.get(0).equals("ConsultarVentas")) {
            System.out.println("Resultado ConsultarVentas" + resultado.get(1).toString());

            ResultadoConsultaVentas(resultado.get(1).toString());
        }

    }

    public void ResultadoEstadoVenta(String Resultado) {
        JSONObject jop;
        try {
            jop = new JSONObject(Resultado);

            String data = jop.get("data").toString();
            if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("data => " + data);
                // pintarDocumentacion(data);
                onPintarVenta(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void ResultadoEstadoGerencia(String Resultado) {
        JSONObject jop;
        try {
            jop = new JSONObject(Resultado);

            String data = jop.get("data").toString();
            if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));
                // System.out.println("data => " + data);
                // pintarDocumentacion(data);
                System.out.println("data " + data);
                onPintarGerencia(data);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void RespuestaBlindaje(String Respuesta) {
        lblEstadoBlindaje.setText("");
        lblObservacionesBlindaje.setText("");

        try {
            JSONObject resultado = new JSONObject(Respuesta);
            if (resultado.has("codigoMensaje")) {
                if (resultado.getString("codigoMensaje").equalsIgnoreCase("00")) {
                    if (resultado.has("estado")) {
                        lblEstadoBlindaje.setText(resultado.getString("estado"));
                    }

                    if (resultado.has("observacion")) {
                        lblObservacionesBlindaje.setText(resultado.getString("observacion"));
                    }

                } else {
                    if (resultado.has("descripcionMensaje")) {
                        lblObservacionesBlindaje.setText(resultado.getString("descripcionMensaje"));
                    }
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void onPintarVenta(String Resultado) {
        // System.out.println("Pintar");

        lblRespuesta.setText("");
        lblEstado.setText("");
        lblPedido.setText("");
        lblObservaciones.setText("");

        try {
            JSONObject jsonObject = new JSONObject(Resultado);

            String respuesta = jsonObject.getString("CodigoMensaje");
            // System.out.println("respuesta " + respuesta);
            lblRespuesta.setText("");

            if (respuesta.equalsIgnoreCase("00")) {
                lblEstado.setText(jsonObject.getString("Estado"));
                lblPedido.setText(jsonObject.getString("Pedido_Fenix"));
                lblObservaciones.setText(jsonObject.getString("Observaciones"));

                tlytTablaVentasRespuesta.setVisibility(View.GONE);
                tlytTablaVentasEstado.setVisibility(View.VISIBLE);
                tlytTablaVentasPedido.setVisibility(View.VISIBLE);
                tlytTablaVentasObservaciones.setVisibility(View.VISIBLE);

            } else if (respuesta.equalsIgnoreCase("-1")) {
                lblRespuesta.setText("Error en La consulta");
                tlytTablaVentasRespuesta.setVisibility(View.VISIBLE);
                tlytTablaVentasEstado.setVisibility(View.GONE);
                tlytTablaVentasPedido.setVisibility(View.GONE);
                tlytTablaVentasObservaciones.setVisibility(View.GONE);
            } else if (respuesta.equalsIgnoreCase("01")) {
                lblRespuesta.setText("Usuario No autorizado");
                tlytTablaVentasRespuesta.setVisibility(View.VISIBLE);
                tlytTablaVentasEstado.setVisibility(View.GONE);
                tlytTablaVentasPedido.setVisibility(View.GONE);
                tlytTablaVentasObservaciones.setVisibility(View.GONE);
            } else if (respuesta.equalsIgnoreCase("02")) {
                lblRespuesta.setText("Venta No Localizada o Consultada Por Usuario No autorizado");
                tlytTablaVentasRespuesta.setVisibility(View.VISIBLE);
                tlytTablaVentasEstado.setVisibility(View.GONE);
                tlytTablaVentasPedido.setVisibility(View.GONE);
                tlytTablaVentasObservaciones.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }

    }

    public void onPintarGerencia(String Resultado) {
        // System.out.println("Pintar");

        lblRespuestaGerencia.setText("");
        lblEstadoGerencia.setText("");
        lblObservacionesGerencia.setText("");

        try {
            JSONObject jsonObject = new JSONObject(Resultado);

            String respuesta = jsonObject.getString("CodigoMensaje");
            // System.out.println("respuesta " + respuesta);
            lblRespuestaGerencia.setText("");

            if (respuesta.equalsIgnoreCase("00")) {

                if (jsonObject.has("dataAMC")) {

                    JSONObject dataAMC = jsonObject.getJSONObject("dataAMC");

                    if (dataAMC.has("CodigoMensaje") && dataAMC.getString("CodigoMensaje").equalsIgnoreCase("00")) {

                        lblEstadoGerencia.setText(dataAMC.getJSONObject("data").getString("estado"));
                        lblGestionGerencia.setText(dataAMC.getJSONObject("data").getString("gestion"));
                        lblObservacionesGerencia.setText(dataAMC.getJSONObject("data").getString("observacion"));

                        tlytTablaGerenciaRespuesta.setVisibility(View.GONE);
                        tlytTablaGerenciaEstado.setVisibility(View.VISIBLE);
                        tlytTablaGerenciaGestion.setVisibility(View.VISIBLE);
                        tlytTablaGerenciaObservaciones.setVisibility(View.VISIBLE);
                    } else {

                        if (jsonObject.has("cpt_observacionAMC")) {
                            tlytTablaGerenciaRespuesta.setVisibility(View.VISIBLE);
                            lblRespuestaGerencia.setText(jsonObject.getString("cpt_observacionAMC"));
                        }
                        // lblRespuestaGerencia.setText("Error en La consulta");
                    }
                }

            } else if (respuesta.equalsIgnoreCase("-1")) {
                lblRespuestaGerencia.setText("Error en La consulta");
                tlytTablaGerenciaRespuesta.setVisibility(View.VISIBLE);
                tlytTablaGerenciaEstado.setVisibility(View.GONE);
                tlytTablaGerenciaGestion.setVisibility(View.GONE);
                tlytTablaGerenciaObservaciones.setVisibility(View.GONE);
            } else if (respuesta.equalsIgnoreCase("01")) {
                lblRespuestaGerencia.setText("Usuario No autorizado");
                tlytTablaGerenciaRespuesta.setVisibility(View.VISIBLE);
                tlytTablaGerenciaEstado.setVisibility(View.GONE);
                tlytTablaGerenciaGestion.setVisibility(View.GONE);
                tlytTablaGerenciaObservaciones.setVisibility(View.GONE);
            } else if (respuesta.equalsIgnoreCase("02")) {
                lblRespuestaGerencia.setText("Gerencia No Localizada o Consultada Por Usuario No autorizado");
                tlytTablaGerenciaRespuesta.setVisibility(View.VISIBLE);
                tlytTablaGerenciaEstado.setVisibility(View.GONE);
                tlytTablaGerenciaGestion.setVisibility(View.GONE);
                tlytTablaGerenciaObservaciones.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }

    }

    public void Resultado_EstadoPedido(String Resultado) {
        estadopedido = Interprete.Estado_Pedido(Resultado);
        if (estadopedido.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, estadopedido.get(0).getDato(), Toast.LENGTH_SHORT).show();
            Borrar();
        } else if (estadopedido.get(0).getTitulo().equalsIgnoreCase("SI")) {
            estadopedido.remove(0);
            onPintar();
        }

    }

    public void Resultado_EstadoOferta(String Resultado) {
        estadopedido = Interprete.Estado_Oferta(Resultado);
        if (estadopedido.get(0).getTitulo().equalsIgnoreCase("NO")) {
            Toast.makeText(this, estadopedido.get(0).getDato(), Toast.LENGTH_SHORT).show();
            Borrar();
        } else if (estadopedido.get(0).getTitulo().equalsIgnoreCase("SI")) {
            estadopedido.remove(0);
            onPintar();
        }

    }

    public void ResultadoConsultaVentas(String resultado) {

        System.out.println("data " + resultado);
        System.out.println(resultado);

        JSONObject jop;

        try {
            jop = new JSONObject(resultado);

            String data = jop.get("data").toString();
            if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                data = new String(Base64.decode(data));

                JSONArray ja = new JSONArray(data);
                System.out.println("ja " + ja);

                if (ja.length() > 0) {

                    final ArrayList<ListaVentas> ventas = new ArrayList<ListaVentas>();

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        ventas.add(new ListaVentas(jo.getString("fecha"), jo.getString("dia"), jo.getString("cliente"),
                                jo.getString("vta_id")));
                    }

                    ListaVentasAdapter adapter = new ListaVentasAdapter(this, ventas, this);
                    lstListaVentas.setAdapter(adapter);

                } else {
                    Toast.makeText(this, getResources().getString(R.string.sinresultados), Toast.LENGTH_LONG).show();
                }

            } else {
                System.out.println("Los datos no se descargaron correctamente");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Borrar() {
        estadopedido.clear();
        onPintar();
    }

    public void onPintar() {
        adapter = new ListaDefaultAdapter(this, estadopedido, this);
        viewEstadoPedido.setAdapter(adapter);
        setListViewHeightBasedOnChildren(viewEstadoPedido);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private boolean habilitarListaVentas() {
        boolean habilitar = false;

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?",
                new String[]{"habilitarListaVentas", "habilitarListaVentas"}, null, null, null);

        if (resultado != null) {
            habilitar = Boolean.valueOf(resultado.get(0).get(0));
        }

        return habilitar;
    }

    public void mostrarDialogo(View v) {
        sfp = (SelectorFecha) v.getParent().getParent();
        dialogo.dialogo.show();
    }

    OnDismissListener dl = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {

            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                sfp.setTexto(dialogo.getFechaSeleccion());
                consultarVentas(dialogo.getFechaSeleccion());

            }
            dialogo.setSeleccion(false);
        }
    };

    private void consultarVentas(String fecha) {

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(MainActivity.config.getCodigo_asesor());
        parametros.add(fecha);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("ConsultarVentas");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

    }

}
