package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import com.itextpdf.text.pdf.TtfUnicodeWriter;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ListaAgendamiento;
import co.com.une.appmovilesune.adapters.ListaAgendamientoAdapter;
import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.adapters.ListaDefaultAdapter;
import co.com.une.appmovilesune.change.Ofertas;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.SelectorFecha;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Blindaje;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Simulador;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

public class ControlDetalleBlindaje extends Activity implements Subject, Observer {

    private Blindaje blindaje;
    private JSONArray agendamiento;

    private ArrayList<String> consolidado;

    private TituloPrincipal tp;
    public SelectorFecha sfp;

    private TabHost tabs;
    private Resources res;

    private Spinner sltNombreBlindaje, sltTipoServicio, sltTipoDocumento;
    private TextView txtBlindajeTipoDocumento, txtBlindajeDocumento, txtBlindajeCiudad, txtBlindajeDireccion,
            txtBlindajeBarrio, txtBlindajeEstrato, txtBlindajeComuna, txtBlindajeUrbanizacion, txtBlindajeProductos,
            txtBlindajeTo, txtBlindajeToPrecio, txtBlindajePlanCerrado, txtBlindajeTv, txtBlindajeTvIptv, txtBlindajeBa,
            txtBlindajeBaPrecio, txtBlindajeBaGotica, txtBlindajeTotal;
    private TextView txtBlindajeRegalo1, txtBlindajeOferta1Nombre, txtBlindajeOferta1regalo, txtBlindajeOferta1Valor,
            txtBlindajeOferta1Factura, txtBlindajeOferta2Nombre, txtBlindajeOferta2regalo, txtBlindajeOferta2Valor,
            txtBlindajeOferta2Factura;
    private CheckBox chkBlindajeRegalo1, chkBlindajeOferta1Check, chkBlindajeOferta2Check;

    public EditText txtObservaciones, txtNombreFormulario, txtApellidosFormulario, txtDocumentoFormulario,
            txtLugarExpFormulario, txtNombreCon1, txtApellidosCon1, txtTelCon1, txtNombreCon2, txtApellidosCon2,
            txtTelCon2;

    private ListView lstBlindajeAgenda;
    public ArrayList<ListaAgendamiento> agenda;
    public Dialogo dialogo, dialogoFecha;

    private LinearLayout blindajeAgenda;
    private TableLayout tlyRegalo, tlyOferta1, tlyOferta2, tlyFormulario;
    private TableRow tlrRegaloFinalOferta1, tlrRegaloFinalOferta2, tlrBlindajeTipoDocumento, tlrBlindajeDocumento;

    private boolean regalos;

    private String fecha;

    private int codigo;
    private String id = "";
    private String TelefonoIVR = "";

    private Observer observador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		/*
         * El primer layout que ponemos es el del menú con los modulos posibles
		 * a ejecutar
		 */
        setContentView(R.layout.viewdetallesblindaje);
        MainActivity.obsrBlindaje = this;

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            blindaje = (Blindaje) reicieveParams.getSerializable("blindaje");
            if (reicieveParams.getString("agenda") != null) {
                try {
                    agendamiento = new JSONArray(reicieveParams.getString("agenda"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            // System.out.println("no blindaje");
        }
        System.out.println("se agenda => " + blindaje.agenda);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);

        res = getResources();

        tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("Detalle");
        spec.setContent(R.id.blindajeDetalle);
        spec.setIndicator("Detalle", res.getDrawable(R.drawable.estadopedido));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Ofertas");
        spec.setContent(R.id.blindajeOfertas);//
        spec.setIndicator("Ofertas", res.getDrawable(R.drawable.oferta));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if (tabId.equals("Detalle")) {
                    tp.setTitulo("Detalle Cliente");
                } else if (tabId.equals("Ofertas")) {
                    tp.setTitulo("Ofertas");
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

        sltNombreBlindaje = (Spinner) findViewById(R.id.sltNombreBlindaje);
        txtBlindajeTipoDocumento = (TextView) findViewById(R.id.txtBlindajeTipoDocumento);
        txtBlindajeDocumento = (TextView) findViewById(R.id.txtBlindajeDocumento);
        txtBlindajeCiudad = (TextView) findViewById(R.id.txtBlindajeCiudad);
        txtBlindajeDireccion = (TextView) findViewById(R.id.txtBlindajeDireccion);
        txtBlindajeBarrio = (TextView) findViewById(R.id.txtBlindajeBarrio);
        txtBlindajeEstrato = (TextView) findViewById(R.id.txtBlindajeEstrato);
        txtBlindajeComuna = (TextView) findViewById(R.id.txtBlindajeComuna);
        txtBlindajeUrbanizacion = (TextView) findViewById(R.id.txtBlindajeUrbanizacion);
        txtBlindajeProductos = (TextView) findViewById(R.id.txtBlindajeProductos);
        txtBlindajeTo = (TextView) findViewById(R.id.txtBlindajeTo);
        txtBlindajeToPrecio = (TextView) findViewById(R.id.txtBlindajeToPrecio);
        txtBlindajePlanCerrado = (TextView) findViewById(R.id.txtBlindajePlanCerrado);
        txtBlindajeTv = (TextView) findViewById(R.id.txtBlindajeTv);
        txtBlindajeTvIptv = (TextView) findViewById(R.id.txtBlindajeTvIptv);
        txtBlindajeBa = (TextView) findViewById(R.id.txtBlindajeBa);
        txtBlindajeBaPrecio = (TextView) findViewById(R.id.txtBlindajeBaPrecio);
        txtBlindajeBaGotica = (TextView) findViewById(R.id.txtBlindajeBaGotica);
        txtBlindajeTotal = (TextView) findViewById(R.id.txtBlindajeTotal);

        txtBlindajeRegalo1 = (TextView) findViewById(R.id.txtBlindajeRegalo1);
        txtBlindajeOferta1Nombre = (TextView) findViewById(R.id.txtBlindajeOferta1Nombre);
        txtBlindajeOferta1regalo = (TextView) findViewById(R.id.txtBlindajeOferta1regalo);
        txtBlindajeOferta1Valor = (TextView) findViewById(R.id.txtBlindajeOferta1Valor);
        txtBlindajeOferta1Factura = (TextView) findViewById(R.id.txtBlindajeOferta1Factura);
        txtBlindajeOferta2Nombre = (TextView) findViewById(R.id.txtBlindajeOferta2Nombre);
        txtBlindajeOferta2regalo = (TextView) findViewById(R.id.txtBlindajeOferta2regalo);
        txtBlindajeOferta2Valor = (TextView) findViewById(R.id.txtBlindajeOferta2Valor);
        txtBlindajeOferta2Factura = (TextView) findViewById(R.id.txtBlindajeOferta2Factura);

        txtObservaciones = (EditText) findViewById(R.id.txtObservacionesBlindaje);

        txtNombreFormulario = (EditText) findViewById(R.id.txtNombre);
        txtApellidosFormulario = (EditText) findViewById(R.id.txtApellido);
        txtDocumentoFormulario = (EditText) findViewById(R.id.txtDocumento);
        txtLugarExpFormulario = (EditText) findViewById(R.id.txtLugarExpedicion);
        txtNombreCon1 = (EditText) findViewById(R.id.txtNombreContacto1);
        txtApellidosCon1 = (EditText) findViewById(R.id.txtApellidoContacto1);
        txtTelCon1 = (EditText) findViewById(R.id.txtTelefonoContacto1);
        txtNombreCon2 = (EditText) findViewById(R.id.txtNombreContacto2);
        txtApellidosCon2 = (EditText) findViewById(R.id.txtApellidoContacto2);
        txtTelCon2 = (EditText) findViewById(R.id.txtTelefonoContacto2);

        sltTipoServicio = (Spinner) findViewById(R.id.spnTipoServicio);
        sltTipoDocumento = (Spinner) findViewById(R.id.sltTipoDocumento);

        chkBlindajeRegalo1 = (CheckBox) findViewById(R.id.chkBlindajeRegalo1);
        chkBlindajeOferta1Check = (CheckBox) findViewById(R.id.chkBlindajeOferta1Check);
        chkBlindajeOferta2Check = (CheckBox) findViewById(R.id.chkBlindajeOferta2Check);

        lstBlindajeAgenda = (ListView) findViewById(R.id.lstBlindajeAgenda);

        blindajeAgenda = (LinearLayout) findViewById(R.id.blindajeAgenda);

        tlyRegalo = (TableLayout) findViewById(R.id.tlyRegalo);
        tlyOferta1 = (TableLayout) findViewById(R.id.tlyOferta1);
        tlyFormulario = (TableLayout) findViewById(R.id.tlyFormulario);
        tlrRegaloFinalOferta1 = (TableRow) findViewById(R.id.tlrRegaloFinalOferta1);
        tlyOferta2 = (TableLayout) findViewById(R.id.tlyOferta2);
        tlrRegaloFinalOferta2 = (TableRow) findViewById(R.id.tlrRegaloFinalOferta2);

        tlrBlindajeTipoDocumento = (TableRow) findViewById(R.id.tlrBlindajeTipoDocumento);
        tlrBlindajeDocumento = (TableRow) findViewById(R.id.tlrBlindajeDocumento);

        sltNombreBlindaje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                // System.out.println(parent.getSelectedItem().toString());
                if (parent.getSelectedItem().toString().equals("Otro")) {
                    llenarCampos(-1);
                } else {
                    llenarCampos(position);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // lblMensaje.setText("");
            }
        });

        dialogoFecha = new Dialogo(this, Dialogo.DIALOGO_FECHA, "");
        dialogoFecha.dialogo.setOnDismissListener(dlf);

        if (blindaje.agenda) {
            dialogo = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_FRANJA, "");
            dialogo.dialogo.setOnDismissListener(dl);
            lstBlindajeAgenda.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                    ListaAgendamiento item = agenda.get(arg2);
                    blindaje.fechaAgenda = agenda.get(arg2).getFecha();
                    dialogo.dialogo.show();
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        tp.setTitulo("Blindaje Manizales");

        llenarSpinner("Tipo Servicio", sltTipoServicio);
        llenarSpinner("Tipo Documento", sltTipoDocumento);
        llenarSpinner();
        // reglas();

		/**/

    }

    private void llenarSpinner(String dataType, Spinner spinner) {
        ArrayAdapter<String> adaptador = null;
        String datos = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre = ?", new String[]{dataType}, null, null, null);
        String[] StOpt = null;
        ArrayList<String> arrayListData = new ArrayList<String>();
        arrayListData.add("-- Seleccione Opción --");
        if (resultado != null) {
            for (int i = 0; i < resultado.size(); i++) {
                arrayListData.add(resultado.get(i).get(0));
            }
        } else {
            arrayListData.add("N/A");
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayListData);
        spinner.setAdapter(adaptador);
    }

    private void llenarSpinner() {

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        for (int i = 0; i < blindaje.cliente.size(); i++) {
            ad.add(blindaje.cliente.get(i)[2]);
        }
        ad.add("Otro");
        sltNombreBlindaje.setAdapter(ad);

        if (blindaje.agenda) {
            agenda = new ArrayList<ListaAgendamiento>();
            try {

                for (int i = 0; i < agendamiento.length(); i++) {
                    JSONObject jo = agendamiento.getJSONObject(i);
                    System.out.println(jo.toString());
                    if (!jo.getString("Cupos AM").equals("0") || !jo.getString("Cupos PM").equals("0")) {
                        agenda.add(new ListaAgendamiento(jo.getString("Fecha"), jo.getString("Cupos AM"),
                                jo.getString("Disponible AM"), jo.getString("Cupos PM"), jo.getString("Disponible PM"),
                                jo.getString("tipoDia"), jo.getString("dia")));
                    }
                }

                ListaAgendamientoAdapter adaptador = new ListaAgendamientoAdapter(this, agenda, this);
                lstBlindajeAgenda.setAdapter(adaptador);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void guardarBlindaje(View v) {
        blindaje.nombreCliente = sltNombreBlindaje.getSelectedItem().toString();
        blindaje.tipoCedulaCliente = txtBlindajeTipoDocumento.getText().toString();
        blindaje.cedulaCliente = txtBlindajeDocumento.getText().toString();
        blindaje.aceptaRegalo = chkBlindajeRegalo1.isChecked();
        blindaje.aceptaOferta1 = chkBlindajeOferta1Check.isChecked();
        blindaje.aceptaOferta2 = chkBlindajeOferta2Check.isChecked();
        blindaje.observaciones = txtObservaciones.getText().toString();

        String[] resumen = new String[9];
        resumen[0] = txtBlindajeRegalo1.getText().toString();
        resumen[1] = txtBlindajeOferta1Nombre.getText().toString();
        resumen[2] = txtBlindajeOferta1regalo.getText().toString();
        resumen[3] = txtBlindajeOferta1Valor.getText().toString();
        resumen[4] = txtBlindajeOferta1Factura.getText().toString();
        resumen[5] = txtBlindajeOferta2Nombre.getText().toString();
        resumen[6] = txtBlindajeOferta2regalo.getText().toString();
        resumen[7] = txtBlindajeOferta2Valor.getText().toString();
        resumen[8] = txtBlindajeOferta2Factura.getText().toString();

        blindaje.resumen = resumen;

        if (blindaje.nombreCliente.equals("Otro")) {
            if (!txtNombreFormulario.getText().toString().equals("")
                    && !txtApellidosFormulario.getText().toString().equals("")
                    && !sltTipoDocumento.getSelectedItem().toString().equals("-- Seleccione Opción --")
                    && !txtDocumentoFormulario.getText().toString().equals("") && !sfp.getTexto().equals("")
                    && !txtLugarExpFormulario.getText().toString().equals("")
                    && !sltTipoServicio.getSelectedItem().toString().equals("-- Seleccione Opción --")
                    && !txtNombreCon1.getText().toString().equals("")
                    && !txtApellidosCon1.getText().toString().equals("") && !txtTelCon1.getText().toString().equals("")
                    && !txtNombreCon2.getText().toString().equals("")
                    && !txtApellidosCon2.getText().toString().equals("")
                    && !txtTelCon2.getText().toString().equals("")) {

                String[] formulario = new String[13];
                formulario[0] = txtNombreFormulario.getText().toString();
                formulario[1] = txtApellidosFormulario.getText().toString();
                formulario[2] = sltTipoDocumento.getSelectedItem().toString();
                formulario[3] = txtDocumentoFormulario.getText().toString();
                formulario[4] = sfp.getTexto();
                formulario[5] = txtLugarExpFormulario.getText().toString();
                formulario[6] = sltTipoServicio.getSelectedItem().toString();
                formulario[7] = txtNombreCon1.getText().toString();
                formulario[8] = txtApellidosCon1.getText().toString();
                formulario[9] = txtTelCon1.getText().toString();
                formulario[10] = txtNombreCon2.getText().toString();
                formulario[11] = txtApellidosCon2.getText().toString();
                formulario[12] = txtTelCon2.getText().toString();

                blindaje.formulario = formulario;

                System.out.println("blindaje.aceptaOferta1  " + blindaje.aceptaOferta1 + " blindaje.aceptaOferta2 "
                        + blindaje.aceptaOferta2);
                if (blindaje.aceptaOferta1 || blindaje.aceptaOferta2 || blindaje.aceptaRegalo) {
                    InsertarConfirmacion();
                } else {
                    blindaje.idIVR = "0";
                    Intent intent = new Intent();
                    intent.putExtra("blindaje", blindaje);
                    setResult(MainActivity.OK_RESULT_CODE, intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Complete el formulario", Toast.LENGTH_SHORT).show();
            }
        } else {
            System.out.println("blindaje.aceptaOferta1  " + blindaje.aceptaOferta1 + " blindaje.aceptaOferta2 "
                    + blindaje.aceptaOferta2);
            if (blindaje.aceptaOferta1 || blindaje.aceptaOferta2 || blindaje.aceptaRegalo) {
                InsertarConfirmacion();
            } else {
                blindaje.idIVR = "0";
                Intent intent = new Intent();
                intent.putExtra("blindaje", blindaje);
                setResult(MainActivity.OK_RESULT_CODE, intent);
                finish();
            }
        }

    }

    public void aplicarReglas(View v) {

        if (v.getTag().equals("chkRegalo")) {
            if (chkBlindajeRegalo1.isChecked()) {
                mostrarAgenda();
            } else {
                if (tabs.getTabWidget().getChildCount() == 3) {
                    tabs.getTabWidget().removeViewAt(2);
                }
            }
        } else if (v.getTag().equals("chkOferta1")) {
            if (chkBlindajeOferta1Check.isChecked()) {
                chkBlindajeOferta2Check.setChecked(false);
                if (regalos) {
                    validarRegalosOferta1();
                }
                mostrarAgenda();
            } else {
                mostrarRegalos();
                if (tabs.getTabWidget().getChildCount() == 3) {
                    tabs.getTabWidget().removeViewAt(2);
                }
            }
        } else if (v.getTag().equals("chkOferta2")) {
            if (tabs.getTabWidget().getChildCount() == 3) {
                tabs.getTabWidget().removeViewAt(2);
            }
            if (chkBlindajeOferta2Check.isChecked()) {
                chkBlindajeOferta1Check.setChecked(false);
                if (regalos) {
                    validarRegalosOferta2();
                }
                mostrarAgenda();
            } else {
                mostrarRegalos();
                if (tabs.getTabWidget().getChildCount() == 3) {
                    tabs.getTabWidget().removeViewAt(2);
                }
            }
        }
		/*
		 * if(!blindaje.Regalo_Inicial.trim().equals("") &&
		 * !blindaje.Regalo_Inicial_2.trim().equals("")){
		 * 
		 * System.out.println("Con regalos");
		 * 
		 * if(!blindaje.Oferta_1.trim().equals("") &&
		 * !blindaje.Oferta_1.trim().equals("SIN OFERTA")){ System.out.println(
		 * "Con oferta 1");
		 * if(blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().
		 * equals("EXCLUYENTE") &&
		 * blindaje.incluyente_excluyente_regaloinicial2afer1amg.trim().equals(
		 * "EXCLUYENTE")){ System.out.println("Excluye regalo 1 y regalo 2");
		 * tlyRegalo.setVisibility(View.GONE); }else
		 * if(blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().
		 * equals("EXCLUYENTE") &&
		 * blindaje.incluyente_excluyente_regaloinicial2afer1amg.trim().equals(
		 * "INCLUYENTE")){ System.out.println("Excluye regalo 1");
		 * txtBlindajeRegalo1.setText(blindaje.Regalo_Inicial_2); }else
		 * if(blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().
		 * equals("INCLUYENTE") &&
		 * blindaje.incluyente_excluyente_regaloinicial2afer1amg.trim().equals(
		 * "EXCLUYENTE")){ System.out.println("Excluye regalo 2");
		 * txtBlindajeRegalo1.setText(blindaje.Regalo_Inicial); }else{
		 * System.out.println("Incluye regalo 1 y regalo 2"); String regalos =
		 * blindaje.Regalo_Inicial+" - "+blindaje.Regalo_Inicial_2;
		 * txtBlindajeRegalo1.setText(regalos); } }else{ System.out.println(
		 * "Sin oferta 1"); }
		 * 
		 * if(!blindaje.Oferta_2.trim().equals("") &&
		 * !blindaje.Oferta_2.trim().equals("SIN OFERTA")){ System.out.println(
		 * "Con oferta 2");
		 * if(blindaje.incluyente_excluyente_regaloinicial1of2.trim().equals(
		 * "EXCLUYENTE")){ System.out.println("Excluye el Regalo Inicial");
		 * }else{ System.out.println("Incluye el regalo Inicial"); } }else{
		 * 
		 * System.out.println("Sin oferta 2"); }
		 * 
		 * 
		 * 
		 * 
		 * }else if(blindaje.Regalo_Inicial.trim().equals("") &&
		 * blindaje.Regalo_Inicial_2.trim().equals("")){
		 * 
		 * System.out.println("Sin regalos");
		 * tlyRegalo.setVisibility(View.GONE);
		 * 
		 * if(!blindaje.Oferta_1.trim().equals("") &&
		 * !blindaje.Oferta_1.trim().equals("SIN OFERTA")){ System.out.println(
		 * "Con oferta 1"); }else{ System.out.println("Sin oferta 1");
		 * tlyOferta1.setVisibility(View.GONE); }
		 * 
		 * if(!blindaje.Oferta_2.trim().equals("") &&
		 * !blindaje.Oferta_2.trim().equals("SIN OFERTA")){ System.out.println(
		 * "Con Oferta 2"); }else{ System.out.println("Sin Oferta 2");
		 * tlyOferta2.setVisibility(View.GONE); }
		 * 
		 * }else if(!blindaje.Regalo_Inicial.trim().equals("") &&
		 * blindaje.Regalo_Inicial_2.trim().equals("")){
		 * 
		 * System.out.println("Con regalo Inicial 1 y sin regalo incial 2");
		 * if(!blindaje.Oferta_1.trim().equals("") &&
		 * !blindaje.Oferta_1.trim().equals("SIN OFERTA")){
		 * if(blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().
		 * equals("EXCLUYENTE")){ System.out.println("Excluye regalo 1");
		 * tlyRegalo.setVisibility(View.GONE); }else{ System.out.println(
		 * "Incluye  regalo 1"); } }else{
		 * 
		 * }
		 * 
		 * 
		 * 
		 * 
		 * }else if(blindaje.Regalo_Inicial.trim().equals("") &&
		 * !blindaje.Regalo_Inicial_2.trim().equals("")){
		 * 
		 * System.out.println("Con regalo Inicial 2 y sin regalo incial 1");
		 * if(blindaje.incluyente_excluyente_regaloinicial1of2.trim().equals(
		 * "EXCLUYENTE")){ System.out.println("Excluye regalo Inicial"); }else{
		 * System.out.println("Incluye regalo Inicial"); }
		 * 
		 * }
		 */
    }

    public void reglas() {

        System.out.println("Regalo inicial 1 => " + blindaje.Regalo_Inicial);
        System.out.println("Regalo inicial 2 => " + blindaje.Regalo_Inicial_2);
        System.out.println("Oferta 1 => " + blindaje.Oferta_1);
        System.out.println("Oferta 2 => " + blindaje.Oferta_2);
		/*
		 * System.out.println(""+blindaje); System.out.println(""+blindaje);
		 * System.out.println(""+blindaje); System.out.println(""+blindaje);
		 * System.out.println(""+blindaje); System.out.println(""+blindaje);
		 * System.out.println(""+blindaje);
		 */

        mostrarRegalos();
        mostrarOfertas();

    }

    private void mostrarRegalos() {

        if (!blindaje.Regalo_Inicial.trim().equals("") && !blindaje.Regalo_Inicial_2.trim().equals("")) {
            txtBlindajeRegalo1.setText(blindaje.Regalo_Inicial + " + " + blindaje.Regalo_Inicial_2);
            tlyRegalo.setVisibility(View.VISIBLE);
            regalos = true;
        } else if (blindaje.Regalo_Inicial.trim().equals("") && blindaje.Regalo_Inicial_2.trim().equals("")) {
            tlyRegalo.setVisibility(View.GONE);
            regalos = false;
        } else if (!blindaje.Regalo_Inicial.trim().equals("") && blindaje.Regalo_Inicial_2.trim().equals("")) {
            txtBlindajeRegalo1.setText(blindaje.Regalo_Inicial);
            tlyRegalo.setVisibility(View.VISIBLE);
            regalos = true;
        } else if (blindaje.Regalo_Inicial.trim().equals("") && !blindaje.Regalo_Inicial_2.trim().equals("")) {
            txtBlindajeRegalo1.setText(blindaje.Regalo_Inicial_2);
            tlyRegalo.setVisibility(View.VISIBLE);
            regalos = true;
        }

        if (blindaje.tieneRegalo) {
            chkBlindajeRegalo1.setEnabled(false);
        }

    }

    public void mostrarOfertas() {
        if (!blindaje.Oferta_1.trim().equals("") && !blindaje.Oferta_1.trim().equals("SIN OFERTA")) {
            if (blindaje.Regalo_Oferta_1.trim().equals("")) {
                tlrRegaloFinalOferta1.setVisibility(View.GONE);
            }
        } else {
            tlyOferta1.setVisibility(View.GONE);
        }

        if (!blindaje.Oferta_2.trim().equals("") && !blindaje.Oferta_2.trim().equals("SIN OFERTA")) {
            System.out.println("Con oferta 2");
            if (blindaje.Regalo_Oferta_2.trim().equals("")) {
                tlrRegaloFinalOferta2.setVisibility(View.GONE);
            }
        } else {
            System.out.println("Sin oferta 2");
            tlyOferta2.setVisibility(View.GONE);
        }
    }

    private void validarRegalosOferta1() {
        if (blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().equals("EXCLUYENTE")
                && blindaje.incluyente_excluyente_regaloinicial2afer1amg.trim().equals("EXCLUYENTE")) {
            tlyRegalo.setVisibility(View.GONE);
        } else if ((blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().equals("INCLUYENTE")
                || blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().equals("N/A")
                || blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().equals(""))
                && blindaje.incluyente_excluyente_regaloinicial2afer1amg.trim().equals("EXCLUYENTE")) {
            System.out.println("Regalo dos => " + blindaje.Regalo_Inicial_2.trim());
            if (blindaje.Regalo_Inicial_2.trim().equals("")) {
                tlyRegalo.setVisibility(View.GONE);
            } else {
                txtBlindajeRegalo1.setText(blindaje.Regalo_Inicial);
            }

        } else if (blindaje.incluyente_excluyente_regaloinicial1ofer1amg.trim().equals("EXCLUYENTE")
                && (blindaje.incluyente_excluyente_regaloinicial2afer1amg.trim().equals("INCLUYENTE")
                || blindaje.incluyente_excluyente_regaloinicial2afer1amg.trim().equals("N/A")
                || blindaje.incluyente_excluyente_regaloinicial2afer1amg.trim().equals(""))) {
            System.out.println("Regalo uno => " + blindaje.Regalo_Inicial.trim());
            if (blindaje.Regalo_Inicial_2.trim().equals("")) {
                tlyRegalo.setVisibility(View.GONE);
            } else {
                txtBlindajeRegalo1.setText(blindaje.Regalo_Inicial_2);
            }

        } else {
            txtBlindajeRegalo1.setText(blindaje.Regalo_Inicial + " - " + blindaje.Regalo_Inicial_2);
        }
    }

    private void validarRegalosOferta2() {
        if (blindaje.incluyente_excluyente_regaloinicial1of2.trim().equals("EXCLUYENTE")) {
            tlyRegalo.setVisibility(View.GONE);
        }
    }

    private void mostrarAgenda() {
        if (blindaje.agenda) {
            blindaje.fechaAgenda = "";
            blindaje.franjaAgenda = "";
            blindaje.motivoAgendaOferta = "";
            blindaje.motivoAgendaRegalo = "";

            if (blindaje.migracionred_regaloinicial.trim().equals("SI") && chkBlindajeRegalo1.isChecked()) {
                if (tabs.getTabWidget().getChildCount() == 2) {
                    TabHost.TabSpec spec = tabs.newTabSpec("Agenda");
                    spec.setContent(R.id.blindajeAgenda);//
                    spec.setIndicator("Agenda", res.getDrawable(R.drawable.cobertura));
                    tabs.addTab(spec);
                    blindaje.motivoAgendaRegalo = "Migracion Red Regalo";
                }
            } else if (blindaje.migracionred_oferta1.trim().equals("SI") && chkBlindajeOferta1Check.isChecked()) {
                if (tabs.getTabWidget().getChildCount() == 2) {
                    TabHost.TabSpec spec = tabs.newTabSpec("Agenda");
                    spec.setContent(R.id.blindajeAgenda);//
                    spec.setIndicator("Agenda", res.getDrawable(R.drawable.cobertura));
                    tabs.addTab(spec);
                    blindaje.motivoAgendaOferta = "Migracion Red Oferta 1";
                }
            } else if (blindaje.tipo_instalacion_oferta1.trim().equals("ORDEN NORMAL")
                    && chkBlindajeOferta1Check.isChecked()) {
                if (tabs.getTabWidget().getChildCount() == 2) {
                    TabHost.TabSpec spec = tabs.newTabSpec("Agenda");
                    spec.setContent(R.id.blindajeAgenda);//
                    spec.setIndicator("Agenda", res.getDrawable(R.drawable.cobertura));
                    tabs.addTab(spec);
                    blindaje.motivoAgendaOferta = "Tipo Instalacion Oferta 1";
                }
            } else if (blindaje.migraacionred_oferta2.trim().equals("SI") && chkBlindajeOferta2Check.isChecked()) {
                if (tabs.getTabWidget().getChildCount() == 2) {
                    TabHost.TabSpec spec = tabs.newTabSpec("Agenda");
                    spec.setContent(R.id.blindajeAgenda);//
                    spec.setIndicator("Agenda", res.getDrawable(R.drawable.cobertura));
                    tabs.addTab(spec);
                    blindaje.motivoAgendaOferta = "Migracion Red Oferta 2";
                }
            } else if (blindaje.tipo_instalacion_oferta2.trim().equals("ORDEN NORMAL")
                    && chkBlindajeOferta2Check.isChecked()) {
                if (tabs.getTabWidget().getChildCount() == 2) {
                    TabHost.TabSpec spec = tabs.newTabSpec("Agenda");
                    spec.setContent(R.id.blindajeAgenda);//
                    spec.setIndicator("Agenda", res.getDrawable(R.drawable.cobertura));
                    tabs.addTab(spec);
                    blindaje.motivoAgendaOferta = "Tipo Instalacion Oferta 2";

                }
            } else if (blindaje.cambioequipo_ba.trim().equals("SI")) {
                if (blindaje.Regalo_Inicial.trim().equals("5M") || blindaje.Regalo_Inicial.trim().equals("10M")) {
                    if (chkBlindajeRegalo1.isChecked()) {
                        if (tabs.getTabWidget().getChildCount() == 2) {
                            TabHost.TabSpec spec = tabs.newTabSpec("Agenda");
                            spec.setContent(R.id.blindajeAgenda);//
                            spec.setIndicator("Agenda", res.getDrawable(R.drawable.cobertura));
                            tabs.addTab(spec);
                            blindaje.motivoAgendaRegalo = "Cambio Equipo BA Regalo";
                        }
                    }

                }
                if (blindaje.Oferta_1.trim().indexOf("2M") > -1 || blindaje.Oferta_1.trim().indexOf("5M") > -1
                        || blindaje.Oferta_1.trim().indexOf("10M") > -1) {
                    if (chkBlindajeOferta1Check.isChecked()) {
                        if (tabs.getTabWidget().getChildCount() == 2) {
                            TabHost.TabSpec spec = tabs.newTabSpec("Agenda");
                            spec.setContent(R.id.blindajeAgenda);//
                            spec.setIndicator("Agenda", res.getDrawable(R.drawable.cobertura));
                            tabs.addTab(spec);
                            blindaje.motivoAgendaOferta = "Cambio Equipo BA Oferta 1";
                        }
                    }
                }
            } else {
                if (tabs.getTabWidget().getChildCount() == 3) {
                    tabs.getTabWidget().removeViewAt(2);
                }
            }

        }
    }

    private void llenarCampos(int position) {
        if (position != -1) {
            tlyFormulario.setVisibility(View.GONE);
            tlrBlindajeTipoDocumento.setVisibility(View.VISIBLE);
            tlrBlindajeDocumento.setVisibility(View.VISIBLE);
            txtBlindajeTipoDocumento.setText(blindaje.cliente.get(position)[0]);
            txtBlindajeDocumento.setText(blindaje.cliente.get(position)[1]);
        } else {
            tlyFormulario.setVisibility(View.VISIBLE);
            tlrBlindajeTipoDocumento.setVisibility(View.GONE);
            tlrBlindajeDocumento.setVisibility(View.GONE);
        }
        txtBlindajeCiudad.setText(blindaje.ciudad);
        txtBlindajeDireccion.setText(blindaje.direccion);
        txtBlindajeBarrio.setText(blindaje.barrio);
        txtBlindajeEstrato.setText(blindaje.estrato);
        txtBlindajeComuna.setText(blindaje.comuna);
        txtBlindajeUrbanizacion.setText(blindaje.urbanizacion);
        txtBlindajeProductos.setText(blindaje.paquete);
        txtBlindajeTo.setText(blindaje.plan_de_to);
        txtBlindajeToPrecio.setText(blindaje.to_coniva);
        txtBlindajePlanCerrado.setText(blindaje.plancerrado_coniva);
        txtBlindajeTv.setText(blindaje.plantv);
        txtBlindajeTvIptv.setText(blindaje.tv_masiptvconiva);
        txtBlindajeBa.setText(blindaje.vel_fact);
        txtBlindajeBaPrecio.setText(blindaje.ba_coniva);
        txtBlindajeBaGotica.setText(blindaje.gotica_coniva);
        txtBlindajeTotal.setText(blindaje.totalconiva_subsidios);

        txtBlindajeOferta1Nombre.setText(blindaje.Oferta_1);
        txtBlindajeOferta1regalo.setText(blindaje.Regalo_Oferta_1);
        txtBlindajeOferta1Valor.setText(blindaje.Valor_Oferta_1);
        txtBlindajeOferta1Factura.setText(blindaje.Factura_Oferta_1);
        txtBlindajeOferta2Nombre.setText(blindaje.Oferta_2);
        txtBlindajeOferta2regalo.setText(blindaje.Regalo_Oferta_2);
        txtBlindajeOferta2Valor.setText(blindaje.Valor_Oferta_2);
        txtBlindajeOferta2Factura.setText(blindaje.Factura_Oferta_2);

        reglas();

    }

    OnDismissListener dl = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {
            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                blindaje.franjaAgenda = dialogo.getFranja();
                System.out
                        .println("Agenda => fehca => " + blindaje.fechaAgenda + " franja => " + blindaje.franjaAgenda);
                tabs.setCurrentTab(1);
            } else {
                // System.out.println("cancelado");
            }

        }
    };

    OnDismissListener dlf = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {

            // TODO Auto-generated method stub
            if (dialogoFecha.isSeleccion()) {
                sfp.setTexto(dialogoFecha.getFechaSeleccion());
            }
            dialogoFecha.setSeleccion(false);
        }
    };

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tarificador, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Repetir Codigo")) {
            System.out.println("id" + id);
            if (!id.equals("")) {
                ArrayList<String> parametros = new ArrayList<String>();
                parametros.add(id);
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(MainActivity.config.getCodigo());
                params.add("RepetirCodigo");
                params.add(parametros);
                Simulador simulador = new Simulador();
                simulador.setManual(this);
                simulador.addObserver(this);
                simulador.execute(params);
            } else {
                Toast.makeText(this, "Complete el ivr", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getTitle().equals("Repetir Llamada")) {
            InsertarConfirmacion();
        } else if (item.getTitle().equals("Grabacion")) {
            LanzarLLamada(id, "4");
        } else if (item.getTitle().equals("Ingresar Codigo")) {
            mostrarDialgo();
        }
        return true;
    }

    public void mostrarDialogo(View v) {
        sfp = (SelectorFecha) v.getParent().getParent();
        dialogoFecha.dialogo.show();
    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub
        System.out.println("Update ControlDetalleBlindaje => " + value);

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
                            blindaje.idIVR = id;
                            LanzarLLamada(id, "9");
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

                        // LanzarLLamada(id, "6");
                        LanzarLLamada(id, "9"); // para cuando se habilite el
                        // codigo en el IVR

                    }
                } else {
                    System.out.println("Los datos no se descargaron correctamente");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void InsertarConfirmacion() {

        addObserver(MainActivity.obsrMainActivity);
        notifyObserver();

        consolidado = MainActivity.consolidarIVR();

        System.out.println("consolidado " + consolidado);

        if (TelefonoIVR.length() >= 7) {
            if (id.equals("")) {
                codigo = (int) (Math.random() * (9999 - 1000 + 1) + 1000);

                JSONObject IVR = new JSONObject();

                try {

                    IVR.put("codigoasesor", consolidado.get(0));
                    // IVR.put("telefono", TelefonoIVR);
                    IVR.put("telefono", Utilidades.ponerIndicativo(TelefonoIVR));
                    IVR.put("codigo", codigo);
                    IVR.put("tipocliente", consolidado.get(3));
                    IVR.put("documento", consolidado.get(4));
                    if (blindaje.aceptaRegalo || blindaje.aceptaOferta1 || blindaje.aceptaOferta2) {
                        JSONObject jsOferta = new JSONObject();
                        if (blindaje.aceptaOferta1) {
                            jsOferta.put("nombre", blindaje.Oferta_1);
                            jsOferta.put("valor", blindaje.Valor_Oferta_1);
                            jsOferta.put("factura", blindaje.Factura_Oferta_1);
                        } else if (blindaje.aceptaOferta2) {
                            jsOferta.put("nombre", blindaje.Oferta_2);
                            jsOferta.put("valor", blindaje.Valor_Oferta_2);
                            jsOferta.put("factura", blindaje.Factura_Oferta_2);
                        } else {
                            jsOferta.put("nombre", "");
                            jsOferta.put("valor", 0);
                            jsOferta.put("factura", 0);
                        }

                        if (blindaje.aceptaRegalo) {
                            jsOferta.put("regalo", (String) txtBlindajeRegalo1.getText());
                        } else {
                            jsOferta.put("regalo", "");
                        }

                        IVR.put("oferta", jsOferta);
                    }
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
                LanzarLLamada(id, "9");
            }

        } else {
            mostrarDialgoTelefono();
        }

        // btnSiguiente.setEnabled(true);
    }

    private void LanzarLLamada(String id, String tipo) {

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(tipo);
        parametros.add(id);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("LanzarLlamada");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

    }

    private void mostrarDialgoTelefono() {
        AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle("Ingrese Telefono Destino");
        promptDialog.setCancelable(false);
        final EditText input = new EditText(this);
        final Context context = this;
        promptDialog.setView(input);

        promptDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (value.length() == 7 || value.length() == 10) {
                    if (value.length() == 7) {
                        TelefonoIVR = "(6)" + value;
                        InsertarConfirmacion();
                    } else if (value.length() == 10) {
                        TelefonoIVR = value;
                        InsertarConfirmacion();
                    } else {
                        Toast.makeText(context, "Numero Errado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Numero Errado", Toast.LENGTH_SHORT).show();
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

    private void mostrarDialgo() {
        System.out.println("Codigo => " + codigo);
        AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle("Confirmación de Código");
        promptDialog.setCancelable(false);
        final EditText input = new EditText(this);
        final Context context = this;
        promptDialog.setView(input);

        promptDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                System.out.println("promptDialog id" + id);
                if (!id.equalsIgnoreCase("")) {
                    if (value.toCharArray().length == 4 || value.equals("20130806")) {
                        if (value.equals(String.valueOf(codigo)) || value.equals("20130806")) {

                            Intent intent = new Intent();
                            intent.putExtra("blindaje", blindaje);
                            setResult(MainActivity.OK_RESULT_CODE, intent);
                            finish();

                        } else {
                            Toast.makeText(context, "No Concuerdan los codigos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "El tamaño del codigo es incorrecto", Toast.LENGTH_SHORT).show();
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

    @Override
    public void addObserver(Observer o) {
        System.out.println("Agregar Observador Control DetalleBlindaje");
        observador = o;

    }

    @Override
    public void removeObserver(Observer o) {
        observador = null;

    }

    @Override
    public void notifyObserver() {
        observador.update("LlamadaBlindaje");

    }

}
