package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ListaPrecios;
import co.com.une.appmovilesune.adapters.ListaPreciosAdapter;
import co.com.une.appmovilesune.adapters.ListaAdicionales;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;
import co.com.une.appmovilesune.model.AdicionalCotizador;
import co.com.une.appmovilesune.model.CotizacionCliente;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Producto;
import co.com.une.appmovilesune.model.ProductoCotizador;

public class ResumenTelefonia extends LinearLayout {

    public Spinner sltPagoLinea, sltTipoMigracion, sltTipoTecnologia;
    private TextView lblPlan, lblDescuento, lblValor, lblValorDescuento, lblDuracion, lblDescuentoA,
            lblValorAdicionales, tituloValorDescuento,lblSmartpromo, lblTipoFactura,lblMensajeUpDown;
    private CheckBox chkMigracion, chkLineaAdicional, chkquitarPromo;

    private ListView lstAdicionales;

    private TableRow trlTipoMigracion, trlQuitarPromocion,trlCampoMensajes;

    private Context context;

    private boolean migracion, lineaAdicional;

    private String descuento, estrato, telefonia, precio, tecnologia, planFacturaActual;

    private String duracion, precioDescuento;

    private String planFactura = "";
    private String tecnologiacr;

    private ArrayList<AdicionalCotizador> adicionalesCotizador;
    String[][] adicionales;
    String precioAdicionales;

    private ArrayList<ListaAdicionales> adicionalesTo = new ArrayList<ListaAdicionales>();

    ListaPreciosAdapter adaptador;

    private Activity activity;

    boolean quitarPromo = false;

    private ProductoCotizador productoCotizador;

    public boolean productoNulo = true;


    public ResumenTelefonia(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    protected void onFinishInflate() {

        super.onFinishInflate();

        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.resumentelefonia, this);

        sltPagoLinea = (Spinner) findViewById(R.id.sltPagoLinea);
        sltTipoMigracion = (Spinner) findViewById(R.id.sltTipoMigracion);
        sltTipoTecnologia = (Spinner) findViewById(R.id.sltTipoTecnologia);
        sltTipoTecnologia.setEnabled(false);

        lblPlan = (TextView) findViewById(R.id.lblPlan);
        lblDescuento = (TextView) findViewById(R.id.lblDescuento);
        lblValor = (TextView) findViewById(R.id.lblValor);
        lblValorDescuento = (TextView) findViewById(R.id.lblValorDescuento);
        lblDuracion = (TextView) findViewById(R.id.lblDuracion);
        lblDescuentoA = (TextView) findViewById(R.id.lblDescuentoA);
        tituloValorDescuento = (TextView) findViewById(R.id.tituloValorDescuento);

        trlTipoMigracion = (TableRow) findViewById(R.id.trlTipoMigracion);
        trlQuitarPromocion = (TableRow) findViewById(R.id.trlQuitarPromocion);

        chkMigracion = (CheckBox) findViewById(R.id.chkMigracion);
        chkLineaAdicional = (CheckBox) findViewById(R.id.chkLineaAdicional);
        chkquitarPromo = (CheckBox) findViewById(R.id.chkquitarPromo);

        lstAdicionales = (ListView) findViewById(R.id.lstAdicionales);
        lblValorAdicionales = (TextView) findViewById(R.id.lblValorAdicionales);

        chkLineaAdicional.setOnCheckedChangeListener(cambioLineaAdicional);
        chkMigracion.setOnCheckedChangeListener(cambioMigracion);
        chkquitarPromo.setOnCheckedChangeListener(eventoQuitarPromo);

        lblSmartpromo = (TextView) findViewById(R.id.lblSmartpromo);
        lblTipoFactura = (TextView) findViewById(R.id.lblTipoFactura);

        trlCampoMensajes = (TableRow) findViewById(R.id.trlCampoMensajes);
        lblMensajeUpDown = (TextView) findViewById(R.id.lblMensajeUpDown);

    }

    public void Telefonia(Activity activity, Context context, CotizacionCliente cotizacionCliente, Cliente cliente) {

        this.activity = activity;
        this.context = context;
        this.productoCotizador = UtilidadesTarificadorNew.traducirProducto(cotizacionCliente.getProductoCotizador(), ProductoCotizador.getTELEFONIA());
        productoCotizador.imprimir("Resumen Telefonia");
        this.descuento = String.valueOf(productoCotizador.getDescuentoCargobasico());
        this.duracion = String.valueOf(productoCotizador.getDuracionDescuento());
        this.estrato = cliente.getEstrato();

        this.telefonia = productoCotizador.getPlan();

        if(cotizacionCliente.isVentaEmpaquetada()){
            this.precio = String.valueOf(productoCotizador.getCargoBasicoEmp());
        }else {
            this.precio = String.valueOf(productoCotizador.getCargoBasicoInd());
        }

        this.planFacturaActual = productoCotizador.getPlanFacturacionActual();

        this.adicionales = adicionales;
        this.adicionalesCotizador = productoCotizador.getAdicionalesCotizador();
        this.precioAdicionales = String.valueOf(productoCotizador.getTotalAdicionales());
        this.tecnologiacr = tecnologiacr;

        asignarPlan(telefonia);
        asignarValor(precio);

        llenarPagoLinea(telefonia, estrato, false);
        llenarTipoMigracion();
        /*if (!planAnt.equals("")) {
            setTipoMigracion(planAnt);
            // sltTipoMigracion.setEnabled(false);
        }*/

        llenarTecnologia();

        aplicarDescuento();

        /*System.out.println("tipo " + tipo);

        if (tipo != null) {
            if (tipo.equalsIgnoreCase("C")) {
                chkMigracion.setChecked(true);
            } else {
                chkMigracion.setEnabled(false);
            }
        }

        if (segundaTelefonia) {
            chkLineaAdicional.setChecked(true);
            if (Utilidades.excluirNacional("deshabilitarSegundaLinea", "deshabilitarSegundaLinea")) {
                chkLineaAdicional.setEnabled(false);
            }
        } else {
            chkLineaAdicional.setChecked(false);
        }*/

        ArrayAdicionales();

        llenarAdicionales();

        productoNulo = false;

        pintarFacturacionySmartPromo();
        pintarMensajes();

    }

    public void pintarMensajes() {
        trlCampoMensajes.setVisibility(View.GONE);
        lblMensajeUpDown.setText("");

        if (productoCotizador.getTipoTransaccion().equalsIgnoreCase("Cambio")) {
            if (productoCotizador.getTipoFacturacion().equalsIgnoreCase("Anticipada") && productoCotizador.getTipoCambio() != null) {
                if (productoCotizador.getTipoCambio().equalsIgnoreCase("UP")) {
                    mostrarMensajeUpDown(Utilidades.mensajeUp("Telefonía"));
                } else if (productoCotizador.getTipoCambio().equalsIgnoreCase("DOWN")) {
                    mostrarMensajeUpDown(Utilidades.mensajeDown("Telefonía"));
                }
            } else if (productoCotizador.getTipoFacturacion().equalsIgnoreCase("Vencida")) {
                mostrarMensajeUpDown(Utilidades.mensajeVencida("Telefonía"));
            }
        }

    }

    public void mostrarMensajeUpDown(String mensaje){
        trlCampoMensajes.setVisibility(View.VISIBLE);
        lblMensajeUpDown.setText(mensaje);
    }

    public void pintarFacturacionySmartPromo() {
        if(productoCotizador.getTipoFacturacion() != null) {
            lblTipoFactura.setText(productoCotizador.getTipoFacturacion());
        }

        System.out.println("pintarFacturacionySmartPromo productoCotizador.getSmartPromo() "+productoCotizador.getSmartPromo());
        System.out.println("pintarFacturacionySmartPromo productoCotizador.getTipoPeticion() "+productoCotizador.getTipoPeticion());

        if(productoCotizador.getSmartPromo() != null) {
            if(productoCotizador.getTipoPeticion().equalsIgnoreCase("N")){
                lblSmartpromo.setText(productoCotizador.getSmartPromo());
            }else{
                lblSmartpromo.setText("N/A");
            }
        }
    }



    public void aplicarDescuento(){
        if (descuento != null) {
            if (descuento.equalsIgnoreCase("-") || descuento.equalsIgnoreCase("Sin Promocion") || descuento.equalsIgnoreCase("0.0")) {
                limpiarDuracion();
            }else if (telefonia.contains("Existente")) {
                limpiarDuracion();
            } else {
                asignarDuracion(UtilidadesTarificadorNew.traducirMeses(duracion));
                asignarDescuento(UtilidadesTarificadorNew.traducirPorcentaje(descuento));
                asignarValorDescuento(UtilidadesTarificadorNew.calcularDescuento(Utilidades.convertirDouble(descuento,"precioDescuento"),Utilidades.convertirDouble(precio,"precio")));
            }
        } else {
            limpiarDuracion();
        }
    }

    public void Telefonia(Activity activity, Context context, String tipo, String telefonia, String precio,
                          String descuento, String Duracion, String precioDescuento, String estrato, String planFacturaActual,
                          boolean segundaTelefonia, String planAnt, String[][] adicionales, String precioAdicionales,
                          String tecnologiacr, boolean aplicarDescuentos) {

        this.activity = activity;
        this.context = context;
        this.descuento = descuento;
        this.duracion = Duracion;
        this.precioDescuento = precioDescuento;
        this.estrato = estrato;
        this.telefonia = telefonia;

        this.planFacturaActual = planFacturaActual;

        this.adicionales = adicionales;
        this.precioAdicionales = precioAdicionales;
        this.tecnologiacr = tecnologiacr;

        System.out.println("rtoTelefonia->tecnologiacr " + this.tecnologiacr);

        asignarPlan(telefonia);
        asignarValor(precio);

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
            } else if (telefonia.contains("Existente")) {
                limpiarDuracion();
            } else {
                asignarDuracion(Duracion);
                asignarValorDescuento(precioDescuento);
                asignarDescuento(descuento);
            }
        }

        llenarPagoLinea(telefonia, estrato, false);
        llenarTipoMigracion();
        if (!planAnt.equals("")) {
            setTipoMigracion(planAnt);
            // sltTipoMigracion.setEnabled(false);
        }

        llenarTecnologia();

        System.out.println("tipo " + tipo);

        if (tipo != null) {
            if (tipo.equalsIgnoreCase("C")) {
                chkMigracion.setChecked(true);
            } else {
                chkMigracion.setEnabled(false);
            }
        }

        if (segundaTelefonia) {
            chkLineaAdicional.setChecked(true);
            if (Utilidades.excluirNacional("deshabilitarSegundaLinea", "deshabilitarSegundaLinea")) {
                chkLineaAdicional.setEnabled(false);
            }
        } else {
            chkLineaAdicional.setChecked(false);
        }

        ArrayAdicionales();

        llenarAdicionales();

    }

    public void llenarPagoLinea(String telefonia, String estrato, boolean borrar) {
        ArrayAdapter<String> adaptador = null;
        String pagoLinea = "";
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasestratos",
                new String[]{"lst_item"}, "lst_nombre = ? and lst_estrato = ?",
                new String[]{"Pago Linea", estrato}, null, "lst_item DESC", null);

        ArrayList<String> pagolinea = new ArrayList<String>();

        if (resultado != null && !borrar && telefonia.indexOf("Existente") == -1) {

            pagolinea.add("-- Seleccione Pago Linea --");
            if (resultado != null) {

                for (int i = 0; i < resultado.size(); i++) {
                    pagolinea.add(resultado.get(i).get(0));
                }

            } else {
                pagolinea.add("N/A");
            }
        } else {
            pagolinea.add("N/A");
        }

        adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, pagolinea);
        sltPagoLinea.setAdapter(adaptador);
    }

    private void llenarTecnologia() {

        ArrayAdapter<String> adaptador = null;
        String tipoTecnologia = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre = ?", new String[]{"Tecnologia_TO"}, null, null, null);

        ArrayList<String> listTipoTecnologia = new ArrayList<String>();
        if (resultado != null) {

            for (int i = 0; i < resultado.size(); i++) {
                listTipoTecnologia.add(resultado.get(i).get(0));
            }

        } else {
            listTipoTecnologia.add("N/A");
        }

        adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listTipoTecnologia);
        sltTipoTecnologia.setAdapter(adaptador);
    }

    private void llenarTipoMigracion() {

        ArrayAdapter<String> adaptador = null;
        String tipoMigracion = "";

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre = ?", new String[]{"Tipo Migracion TO"}, null, null, null);

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


    public void asignarPlan(String plan) {
        lblPlan.setText(plan);
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

    public void limpiarDuracion() {
        asignarDuracion("0 Meses");
        asignarValorDescuento("0");
        asignarDescuento("-");
    }

    public void asignarDuracion(String Duracion) {
        lblDuracion.setText(Duracion);
    }

    public void ArrayAdicionales() {
        adicionalesTo.clear();
        if (adicionales != null) {
            for (int i = 0; i < adicionales.length; i++) {
                adicionalesTo.add(new ListaAdicionales(adicionales[i][0], adicionales[i][1], "", "",true));
            }
        }else if(adicionalesCotizador != null && adicionalesCotizador.size()>0){
            for (int i = 0; i < adicionalesCotizador.size(); i++) {
                adicionalesTo.add(new ListaAdicionales(adicionalesCotizador.get(i).getNombreAdicional(), String.valueOf(adicionalesCotizador.get(i).getPrecioAdicional()), "", "", true));
            }
        }
    }


    public void llenarAdicionales() {

        imprimirAdicionales();

        ArrayList<ListaPrecios> adicional = new ArrayList<ListaPrecios>();
        // System.out.println("adicionales resumen tv => " + adicionales);
        if (adicionalesTo.size() > 0) {
            for (int i = 0; i < adicionalesTo.size(); i++) {
                adicional.add(new ListaPrecios(adicionalesTo.get(i).getAdicional(), adicionalesTo.get(i).getPrecio()));
            }
        }else if (adicionalesCotizador != null && adicionalesCotizador.size() > 0) {
            for (int i = 0; i < adicionalesCotizador.size(); i++) {
                adicional.add(new ListaPrecios(adicionalesCotizador.get(i).getNombreAdicional(), String.valueOf(adicionalesCotizador.get(i).getPrecioAdicional())));
            }
        }


        adaptador = new ListaPreciosAdapter(activity, adicional, context);
        lstAdicionales.setAdapter(adaptador);
        setListViewHeightBasedOnChildren(lstAdicionales);

        lblValorAdicionales.setText(precioAdicionales);
    }

    public void imprimirAdicionales() {

        for (int i = 0; i < adicionalesTo.size(); i++) {
            System.out.println("adicionales to impresion adicional " + adicionalesTo.get(i).getAdicional());
            System.out.println("adicionales to impresion precio " + adicionalesTo.get(i).getPrecio());
        }
    }

    public void agregarImpuestoTelefonico(String municipio, String departamento, String estrato) {

        double valor = UtilidadesTarificador.ImpuestoTelefonico(municipio, departamento, estrato);
        if (valor >= 0) {

            adicionalesTo.add(new ListaAdicionales("Impuesto Telefonico", String.valueOf(valor), "", "", true));

            llenarAdicionales();
        }

    }

    OnCheckedChangeListener cambioMigracion = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            if (isChecked) {
                trlTipoMigracion.setVisibility(VISIBLE);
                llenarTipoMigracion();
                llenarPagoLinea(telefonia, estrato, true);
            } else {
                trlTipoMigracion.setVisibility(GONE);
                llenarPagoLinea(telefonia, estrato, false);
            }
            migracion = isChecked;
            llenarTipoMigracion();
        }

    };

    OnCheckedChangeListener cambioLineaAdicional = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO Auto-generated method stub
            lineaAdicional = isChecked;
            // System.out.println("linea Adicional => " + lineaAdicional);
        }
    };

    OnCheckedChangeListener eventoQuitarPromo = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                quitarPromo = true;
                limpiarDuracion();
            } else {
                quitarPromo = false;
                asignarDuracion(duracion);
                asignarValorDescuento(precioDescuento);
                asignarDescuento(descuento);
            }
        }
    };

    public void Iniciar() {

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

    public String getTecnologiacr() {
        return tecnologiacr;
    }

    public void setTecnologiacr(String tecnologiacr) {
        this.tecnologiacr = tecnologiacr;
    }

    public String getLinea() {
        String Linea = "";
        if (chkLineaAdicional.isChecked()) {
            Linea = "Adicional";
            // System.out.println("telefono "+telefonia+" planFactura
            // "+planFacturaActual);
            // setPlanFactura(planSegundaLinea(telefonia));
        } else {
            Linea = "Nueva";
            // setPlanFactura(planFacturaActual);
        }

        return Linea;
    }

    public void setLinea(String linea) {
        if (linea.equalsIgnoreCase("Adicional")) {
            chkLineaAdicional.setChecked(true);
            // System.out.println("telefono "+telefonia+" planFactura
            // "+planFacturaActual);
        } else {
            chkLineaAdicional.setChecked(false);
        }
    }

    public String getPlanFactura() {

        if (chkLineaAdicional.isChecked()) {
            planFactura = planSegundaLinea(telefonia);
        } else {

            planFactura = planFacturaActual;

        }

        return planFactura;
    }

    public void setPlanFactura(String planFactura) {
        this.planFactura = planFactura;
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
            llenarPagoLinea(telefonia, estrato, true);
        } else {
            chkMigracion.setChecked(false);
        }
    }

    public String getPagoLinea() {
        return sltPagoLinea.getSelectedItem().toString();
    }

    public void setPagoLinea(String pagoLinea) {
        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltPagoLinea.getAdapter();
        sltPagoLinea.setSelection(adaptador.getPosition(pagoLinea), true);

    }

    public String getDuracion() {
        return lblDuracion.getText().toString();
    }

    public String getTipoMigracion() {
        return (String) sltTipoMigracion.getSelectedItem();

    }

    public void setTipoMigracion(String tipoMigracion) {
        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoMigracion.getAdapter();
        System.out.println("To pos Ad an " + adaptador.getPosition(tipoMigracion));
        if (adaptador.getPosition(tipoMigracion) == -1) {
            adaptador.add(tipoMigracion);
            sltTipoMigracion.setAdapter(adaptador);
        }
        System.out.println("To pos Ad des " + adaptador.getPosition(tipoMigracion));
        sltTipoMigracion.setSelection(adaptador.getPosition(tipoMigracion), true);
    }

    public String getTecnologia() {
        return (String) sltTipoTecnologia.getSelectedItem();
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getAdicionalesCadena() {
        String adicionalesCadena = "";

        if (adicionales != null) {
            for (int i = 0; i < adicionales.length; i++) {
                adicionalesCadena += adicionales[i][0] + ",";
            }
        }

        return adicionalesCadena;
    }

    public String getPreciosAdicionalesCadena() {
        String preciosAdicionalesCadena = "";
        if (adicionales != null) {
            for (int i = 0; i < adicionales.length; i++) {
                preciosAdicionalesCadena += adicionales[i][1] + ",";
            }
        }
        return preciosAdicionalesCadena;
    }

    public String getPrecioAdicionales() {
        return precioAdicionales;
    }

    public static String planSegundaLinea(String producto) {

        System.out.println("MainActivity.config.getDepartamento() " + MainActivity.config.getDepartamento());
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios",
                new String[]{"homoSegundaLinea"}, "Producto=? and Departamento=?",
                new String[]{producto, MainActivity.config.getDepartamento()}, null, null, null);

        System.out.println("respuesta " + respuesta);

        String data = "";

        if (respuesta != null) {
            try {
                data = respuesta.get(0).get(0);
            } catch (Exception e) {
                data = "";
            }
        }

        return data;
    }

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

    public void tipoTecnologiaDefaul() {
        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoTecnologia.getAdapter();
        sltTipoTecnologia.setSelection(adaptador.getPosition("TOIP"), true);
        // sltTipoTecnologia.setEnabled(false);
    }

    public void habilitarQuitarPromo() {
        trlQuitarPromocion.setVisibility(View.VISIBLE);
    }

    public boolean isQuitarPromo() {
        return quitarPromo;
    }

    public void setQuitarPromo(boolean quitarPromo) {
        this.quitarPromo = quitarPromo;
    }

    public ProductoCotizador getProductoCotizador() {
        productoCotizador.setPrecio(precio);
        productoCotizador.setPlanFacturacion(planFacturaActual);
        productoCotizador.setLinea(getLinea());
        productoCotizador.setAdicionalesCotizador(adicionalesCotizador);
        productoCotizador.setCambioPlan(getMigracion());
        productoCotizador.setTecnologia(getTecnologia());
        return productoCotizador;
    }

    public void setProductoCotizador(ProductoCotizador productoCotizador) {
        this.productoCotizador = productoCotizador;
    }

    public boolean isProductoNulo() {
        return productoNulo;
    }

    public void setProductoNulo(boolean productoNulo) {
        this.productoNulo = productoNulo;
    }
}
