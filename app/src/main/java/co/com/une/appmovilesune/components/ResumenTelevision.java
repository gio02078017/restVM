package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Log;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ListaDecodificadoresAdapter;
import co.com.une.appmovilesune.adapters.ListaPrecios;
import co.com.une.appmovilesune.adapters.ListaPreciosAdapter;
import co.com.une.appmovilesune.adapters.ListaResumenDecodificadoresAdapter;
import co.com.une.appmovilesune.change.ControlSimulador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesDecos;
import co.com.une.appmovilesune.model.Cotizacion;
import co.com.une.appmovilesune.model.Decodificadores;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ResumenTelevision extends LinearLayout {

    public Spinner sltTelevisores, sltTipoMigracion, sltExtensiones, sltTipoTecnologia;
    TextView lblPlan, lblDescuento, lblValor, lblValorDescuento, lblValorAdicionales, lblDuracion, lblDescuentoA,
            tituloValorDescuento;

    TextView lblExtensiones, lblDecos_SD, lblDecos_HD;

    CheckBox chkMigracion;

    private TableRow trlTipoMigracion;

    ListaPreciosAdapter adaptador;
    ListaResumenDecodificadoresAdapter adaptadorDecos;
    ArrayList<ItemDecodificador> decodificadores;
    Decodificadores decodificador;


    private ListView lstAdicionales;
    private ListView lstDecodificadores;
    private String descuento;
    private String television;
    private String planFacturacion;
    private String tecnologia;
    private String ciudad;

    private Activity activity;

    private boolean migracion;

    String[][] adicionales;
    String precioAdicionales;

    int ext = 0;
    int totalTelevisores;

    private boolean validarTelevisores = false;
    private boolean validarDecosPago = true;
    private boolean habilitarEventos = false;

    String decos = "";

    private String tecnologiacr;

    Context context;

    private Cotizacion cotizacion;

    public ResumenTelevision(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    protected void onFinishInflate() {

        super.onFinishInflate();

        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.resumentelevision, this);

        sltTelevisores = (Spinner) findViewById(R.id.sltTelevisores);
        lblDuracion = (TextView) findViewById(R.id.lblDuracion);
        lblDescuentoA = (TextView) findViewById(R.id.lblDescuentoA);
        sltTipoMigracion = (Spinner) findViewById(R.id.sltTipoMigracion);
        sltTipoTecnologia = (Spinner) findViewById(R.id.sltTipoTecnologia);

        lblPlan = (TextView) findViewById(R.id.lblPlan);
        lblDescuento = (TextView) findViewById(R.id.lblDescuento);
        lblValor = (TextView) findViewById(R.id.lblValor);
        lblValorDescuento = (TextView) findViewById(R.id.lblValorDescuento);
        lblValorAdicionales = (TextView) findViewById(R.id.lblValorAdicionales);
        tituloValorDescuento = (TextView) findViewById(R.id.tituloValorDescuento);

        sltExtensiones = (Spinner) findViewById(R.id.sltExtensiones);
        lstAdicionales = (ListView) findViewById(R.id.lstAdicionales);
        lstDecodificadores = (ListView) findViewById(R.id.lstresumendecodificadores);

        trlTipoMigracion = (TableRow) findViewById(R.id.trlTipoMigracion);

        chkMigracion = (CheckBox) findViewById(R.id.chkMigracion);

        chkMigracion.setOnCheckedChangeListener(cambioMigracion);

    }

    public void llenarExtensiones(String tecnologia) {
        ArrayAdapter<Integer> adaptador = null;

        ext = 0;

        if (tecnologia.equalsIgnoreCase("HFC")) {

            if (Utilidades.excluirNacional("planesDigitales", television)) {
                ext = 2;
            }
        } else {
            ext = 0;
        }

        ArrayList<Integer> extensiones = new ArrayList<Integer>();

        if (ext == 0) {
            extensiones.add(0);
        } else {
            if (!Utilidades.excluirNacional("planesDigitales", television)) {
                for (int i = 0; i < ext; i++) {
                    extensiones.add(i + 1);
                }
            } else {
                for (int i = 0; i <= ext; i++) {
                    extensiones.add(i);
                }
            }
        }

        System.out.println("Nodos Digitales cotizacion.isHFCDigital() " + cotizacion.isHFCDigital());
        if (cotizacion.isHFCDigital()) {
            extensiones.clear();
            extensiones.add(0);
            Utilidades.MensajesToast(
                    getResources().getString(R.string.mensaje_extensiones), context);
        }

        System.out.println("extensiones " + extensiones);

        adaptador = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, extensiones);
        sltExtensiones.setAdapter(adaptador);

    }

    private void llenarTipoMigracion() {

        ArrayAdapter<String> adaptador = null;
        String tipoMigracion = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre = ?", new String[]{"Tipo Migracion TV"}, null, null, null);

        ArrayList<String> listTipoMigracion = new ArrayList<String>();
        if (resultado != null && chkMigracion.isChecked()) {
            listTipoMigracion.add("-- Seleccione Cambio de Plan --");

            for (int i = 0; i < resultado.size(); i++) {
                listTipoMigracion.add(resultado.get(i).get(0));
            }

        } else {
            listTipoMigracion.add("N/A");
        }

        adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listTipoMigracion);
        sltTipoMigracion.setAdapter(adaptador);

    }

    private void llenarTecnologia(String television, String ciudad) {

        ArrayAdapter<String> adaptador = null;
        String tipoTecnologia = "";

        System.out.println("television " + television + "ciudad " + ciudad);

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ? and lst_ciudad = ?",
                new String[]{"tipoTecnologiaTV", television, ciudad}, null, null, null);

        ArrayList<String> listTipoTecnologia = new ArrayList<String>();

        System.out.println("resultado " + resultado);

        if (resultado != null) {
            if (resultado.size() > 1) {
                listTipoTecnologia.add(Utilidades.inicial_opcion);
            }

            for (int i = 0; i < resultado.size(); i++) {
                listTipoTecnologia.add(resultado.get(i).get(0));
            }

        } else {
            listTipoTecnologia.add("N/A");
        }

        adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listTipoTecnologia);
        sltTipoTecnologia.setAdapter(adaptador);

        sltTipoTecnologia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                llenarExtensiones((String) sltTipoTecnologia.getSelectedItem());
                sltTelevisores.setSelection(0);

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // lblMensaje.setText("");
            }
        });
    }

    public void Television(Activity activity, Context context, String tipo, String television, String precio,
                           String descuento, String Duracion, String precioDescuento, String[][] adicionales, String precioAdicionales,
                           String planFacturacion, String estrato, String ciudad, String planAnt, String tecnologiacr,
                           boolean aplicarDescuentos, String tipoTecnologia, ArrayList<ItemPromocionesAdicionales> promoAdicionales,
                           Decodificadores decodificador, Cotizacion cotizacion) {

        this.activity = activity;
        this.context = context;
        this.descuento = descuento;
        this.television = television;
        this.ciudad = ciudad;
        this.decodificador = decodificador;
        this.decodificadores = decodificador.getItemDecodificadors();
        this.cotizacion = cotizacion;
        tecnologia = tipoTecnologia;

        this.tecnologiacr = tecnologiacr;

        System.out.println("rtvTelevision->tecnologiacr " + this.tecnologiacr);

        sltTipoTecnologia.setEnabled(false);

        asignarPlan(television);
        asignarValor(precio);
        setPlanFacturacion(planFacturacion);

        setAdicionales(adicionales);
        setPrecioAdicionales(precioAdicionales);

        if (aplicarDescuentos && descuento.contains("%")) {

            ArrayList<Object> listDescuentos = Utilidades.precioDescuento(descuento, precio);

            if ((Boolean) listDescuentos.get(0)) {

                if (!listDescuentos.get(2).equals(0.0) && !listDescuentos.get(2).equals(0)) {
                    tituloValorDescuento.setVisibility(View.GONE);
                    precioDescuento = "(" + listDescuentos.get(2) + ")";
                } else {
                    tituloValorDescuento.setVisibility(View.VISIBLE);
                }
            }

        }

        if (descuento != null) {
            if (descuento.equalsIgnoreCase("-")) {
                precioDescuento = "N/A";
            }

            if (descuento.equalsIgnoreCase("Sin Promocion")) {
                limpiarDuracion();
            } else if (television.contains("Existente")) {
                limpiarDuracion();
            } else {
                asignarDuracion(Duracion);
                asignarDescuento(descuento);
                asignarValorDescuento(precioDescuento);
            }
        } else {
            precioDescuento = "N/A";
        }

        //if(!Utilidades.excluirNacional("tipoTivo",television)) {
            llenarExtensiones(tipoTecnologia);
        //}

        llenarDecodificadores(decodificadores);
        llenarAdicionales();

        if (tipo != null) {
            if (tipo.equalsIgnoreCase("C")) {
                chkMigracion.setChecked(true);
            } else {
                chkMigracion.setEnabled(false);
            }
        }

        llenarTipoMigracion();
        if (!planAnt.equals("")) {
            setTipoMigracion(planAnt);
            sltTipoMigracion.setEnabled(false);
        }
        llenarTecnologia(television, ciudad);

        setTecnologia(tipoTecnologia);

    }

    public void validarDecosPago(String tecnologia, boolean decosPagoSD, boolean decosPagoHD, boolean decosPagoPVR) {

        System.out.println("Entro ");

        validarDecosPago = true;

        ArrayList<Boolean> validacion = new ArrayList<Boolean>();

        if (tecnologia.equalsIgnoreCase("HFC")) {
            if (decosPagoSD) {
                validacion.add(buscarCanalestipoDeco("SD"));
            }

            if (decosPagoHD) {
                validacion.add(buscarCanalestipoDeco("HD"));
            }

            if (decosPagoPVR) {
                validacion.add(buscarCanalestipoDeco("HD"));
            }
        } else {
            if (decosPagoHD) {
                validacion.add(buscarCanalestipoDeco("HD"));
            }

            if (decosPagoPVR) {
                validacion.add(buscarCanalestipoDeco("HD"));
            }
        }

        System.out.println("validacion " + validacion);

        if (validacion.contains(false)) {
            validarDecosPago = false;
        }

        System.out.println("validarDecosPago " + validarDecosPago);

    }

    public boolean buscarCanalestipoDeco(String tipo) {

        boolean localizado = false;

        String decos_pago = "N/A";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_clave"}, "lst_nombre = ? and lst_valor = ?",
                new String[]{"decos_gratis_adicionales", tipo}, null, null, null);

        System.out.println("resultado " + resultado);
        if (resultado != null) {
            // decos_pago = resultado.get(0).get(0);}
            // System.out.println("resultado.get(0).get(0) "
            // + resultado.get(0).get(0));
            for (int i = 0; i < resultado.size(); i++) {
                System.out.println("resultado [" + i + "]" + resultado.get(i).get(0));
                if (adicionales != null) {
                    for (int j = 0; j < adicionales.length; j++) {
                        // adicional.add(new ListaPrecios(adicionales[i][0],
                        // adicionales[i][1]));

                        if (adicionales[j][0].equalsIgnoreCase(resultado.get(i).get(0))) {
                            localizado = true;
                            break;
                        }

                        // String deco_gratis =
                        // decos_gratis_adicionales(adicionales[i][0]);
                    }
                }
                // System.out.println("resultado
                // ["+i+"]"+resultado.get(0).get(i));
            }
            // System.out.println("consulta decos_pago "+decos_pago);
        }

        // return decos_pago;

        System.out.println("localizado " + localizado);

        return localizado;

    }

    public boolean validarDecos(String tipo) {

        boolean validado = false;

        String decos_gratis_adicional = "N/A";

        ArrayList<String> canales = new ArrayList<String>();

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_clave"}, "lst_nombre = ? and  lst_valor = ?",
                new String[]{"decos_gratis_adicionales", tipo}, null, null, null);

        // System.out.println("resultado " + resultado);

        if (resultado != null) {
            // System.out.println("resultado.get(0) " + resultado.get(0));

            for (int i = 0; i < resultado.size(); i++) {
                canales.add(resultado.get(i).get(0));
            }
            // decos_gratis_adicional = resultado.get(0).get(0);
        }

        for (int i = 0; i < canales.size(); i++) {
            for (int j = 0; j < adicionales.length; j++) {
                // System.out.println("canales " + canales.get(i)
                // + " adicionales[j] " + adicionales[j][0]);

                if (adicionales[j][0].equals(canales.get(i))) {
                    validado = true;
                    break;
                }
            }

        }

        System.out.println("canales " + canales);
        System.out.println("validado " + validado + " tipo " + tipo);

        return validado;
    }

    public String getDecos() {

        int datoExt = (Integer) sltExtensiones.getSelectedItem();

        System.out.println("ext " + ext);

        JSONObject datosDecos = null;

        if(Utilidades.excluirNacional("tipoTivo",television)) {
            datosDecos = UtilidadesDecos.datosValidarDecos(decodificador);
        }else{
            datosDecos = UtilidadesDecos.datosValidarDecos(decodificadores, datoExt);
        }

        try {
            totalTelevisores = datosDecos.getInt("totalTelevisores");
        } catch (JSONException e) {
            Log.w("Error " + e.getMessage());
        }

        return datosDecos.toString();

    }

    public boolean isValidarTelevisores() {
        return validarTelevisores;
    }

    public boolean isValidarDecosPago() {
        return validarDecosPago;
    }

    public void setValidarDecosPago(boolean validarDecosPago) {
        this.validarDecosPago = validarDecosPago;
    }

    public String getTecnologiacr() {
        return tecnologiacr;
    }

    public void setTecnologiacr(String tecnologiacr) {
        this.tecnologiacr = tecnologiacr;
    }

    public String Tipo_Tecnologia() {
        String Tecnologia = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?",
                new String[]{"Tecnologia_TV", television}, null, null, null);

        if (resultado != null) {
            Tecnologia = resultado.get(0).get(0);
        }

        return Tecnologia;
    }

    public String decos_gratis(String tecnologia) {
        String decos_gratis = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?",
                new String[]{"decos_gratis", tecnologia}, null, null, null);

        if (resultado != null) {
            decos_gratis = resultado.get(0).get(0);
        }

        return decos_gratis;
    }

    public String decos_gratis_adicionales(String adicional) {
        String decos_gratis_adicional = "N/A";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?",
                new String[]{"decos_gratis_adicionales", adicional}, null, null, null);

        if (resultado != null) {
            decos_gratis_adicional = resultado.get(0).get(0);
        }

        return decos_gratis_adicional;
    }

    public String decos_pago(String adicional) {
        String decos_pago = "N/A";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre = ? and lst_clave = ?",
                new String[]{"decos_pago", adicional}, null, null, null);

        if (resultado != null) {
            decos_pago = resultado.get(0).get(0);
            // System.out.println("consulta decos_pago "+decos_pago);
        }

        return decos_pago;
    }

    public void llenarAdicionales() {

        ArrayList<ListaPrecios> adicional = new ArrayList<ListaPrecios>();
        // System.out.println("adicionales resumen tv => " + adicionales);
        if (adicionales != null) {
            for (int i = 0; i < adicionales.length; i++) {
                adicional.add(new ListaPrecios(adicionales[i][0], adicionales[i][1]));
            }
        }

        if (adicional.size() > 0) {
            adaptador = new ListaPreciosAdapter(activity, adicional, context);
            lstAdicionales.setAdapter(adaptador);
            setListViewHeightBasedOnChildren(lstAdicionales);
            double total = Double.parseDouble(lblValor.getText().toString()) + totalDecodificadores();

            lblValorAdicionales.setText(getPrecioAdicionales());

            asignarValor(String.valueOf(total));
        }
    }

    private double totalDecodificadores() {
        double totalDecos = 0;

        if (decodificadores != null) {
            for (ItemDecodificador itemDecodificador : decodificadores) {
                totalDecos += Double.parseDouble(itemDecodificador.getPrecio());
            }
        }

        return totalDecos;
    }

    public void llenarDecodificadores(ArrayList<ItemDecodificador> decodificadores) {

        if (decodificadores != null) {

            UtilidadesDecos.imprimirDecos("Resument TV", decodificadores);

            adaptadorDecos = new ListaResumenDecodificadoresAdapter(activity, decodificadores, context);
            lstDecodificadores.setAdapter(adaptadorDecos);
            setListViewHeightBasedOnChildren(lstDecodificadores);
        }

        lblValorAdicionales.setText(getPrecioAdicionales());

    }

    OnCheckedChangeListener cambioMigracion = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                trlTipoMigracion.setVisibility(VISIBLE);
                llenarTipoMigracion();
            } else {
                trlTipoMigracion.setVisibility(GONE);
            }
            migracion = isChecked;
            llenarTipoMigracion();
        }

    };

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        // System.out.println("listView " + listView);
        // System.out.println("listAdapter " + listAdapter);
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
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

    public void setContext(Context context) {
        this.context = context;
    }

    public void asignarPlan(String plan) {
        lblPlan.setText(plan);
    }

    public void limpiarDuracion() {
        asignarDuracion("0 Meses");
        asignarDescuento("-");
        asignarValorDescuento("0");
    }

    public void asignarDuracion(String Duracion) {
        lblDuracion.setText(Duracion);
    }

    public String getDuracion() {
        return lblDuracion.getText().toString();
    }

    public void asignarDescuento(String descuento) {

        lblDescuento.setText(descuento);

    }

    public void asignarValor(String valor) {
        lblValor.setText(valor);
    }

    public void asignarValorDescuento(String valor) {
        lblValorDescuento.setText(valor);
    }

    public String getExtensiones() {

        return "" + totalTelevisores;
    }

    public void setExtensiones(String extensiones) {
        // ArrayAdapter<String> adaptador = (ArrayAdapter<String>)
        // sltTelevisores.getAdapter();
        // sltTelevisores.setSelection(adaptador.getPosition(extensiones),
        // true);

    }

    public String getPlan() {
        return lblPlan.getText().toString();
    }

    public String getDescuento() {
        return lblDescuento.getText().toString();
    }

    public String getValor() {
        return lblValor.getText().toString();
    }

    public String getValorDescuento() {
        return lblValorDescuento.getText().toString();
    }

    public String getMigracion() {
        String Migracion = "";
        if (chkMigracion.isChecked()) {
            Migracion = "Cambio";
        } else {
            Migracion = "Nueva";
        }

        return Migracion;
    }

    public void setMigracion(String migracion) {
        if (migracion.equalsIgnoreCase("Cambio")) {
            chkMigracion.setChecked(true);
        } else {
            chkMigracion.setChecked(false);
        }
    }

    public boolean isMigracion() {
        return migracion;
    }

    public String getTipoMigracion() {
        return (String) sltTipoMigracion.getSelectedItem();
    }

    public void setTipoMigracion(String tipoMigracion) {
        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoMigracion.getAdapter();
        if (adaptador.getPosition(tipoMigracion) == -1) {
            adaptador.add(tipoMigracion);
            sltTipoMigracion.setAdapter(adaptador);
        }

        sltTipoMigracion.setSelection(adaptador.getPosition(tipoMigracion), true);
    }

    public String[][] getAdicionales() {
        return adicionales;
    }

    public String getAdicionalesCadena() {
        String adicionalesCadena = "";
        for (int i = 0; i < adicionales.length; i++) {
            adicionalesCadena += adicionales[i][0] + ",";
        }

        return adicionalesCadena;
    }

    public String getPreciosAdicionalesCadena() {
        String preciosAdicionalesCadena = "";
        for (int i = 0; i < adicionales.length; i++) {
            preciosAdicionalesCadena += adicionales[i][1] + ",";
        }
        return preciosAdicionalesCadena;
    }

    public void setAdicionales(String[][] adicionales) {
        this.adicionales = adicionales;
    }

    public String getPrecioAdicionales() {
        return precioAdicionales;
    }

    public void setPrecioAdicionales(String precioAdicionales) {
        this.precioAdicionales = precioAdicionales;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getPlanFacturacion() {
        return planFacturacion;
    }

    public void setPlanFacturacion(String planFacturacion) {
        this.planFacturacion = planFacturacion;
    }

    public String getTecnologia() {

        String tecno = (String) sltTipoTecnologia.getSelectedItem();

        if (tecno != null && !tecno.equals("")) {
            return tecno;
        } else {
            return tecnologia;
        }

    }


    public void setTecnologia(String TipoTecnologia) {

        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoTecnologia.getAdapter();

        System.out.println("TipoTecnologia " + TipoTecnologia);

        if (TipoTecnologia.equalsIgnoreCase("REDCO")) {
            TipoTecnologia = "IPTV";
        }

        sltTipoTecnologia.setSelection(adaptador.getPosition(TipoTecnologia));
    }

}
