package co.com.une.appmovilesune.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ListaAdicionales;
import co.com.une.appmovilesune.adapters.ListaAdicionalesAdapter;
import co.com.une.appmovilesune.change.ControlSimulador;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.ObserverAdicionales;
import co.com.une.appmovilesune.interfaces.ObserverTotales;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.interfaces.SubjectTotales;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Tarificador;

/**
 * Created by davids on 25/10/16.
 */

public class CompAdicional extends LinearLayout implements ObserverAdicionales, Observer, Subject {

    public static final int TELEFONIA = 0;
    public static final int TELEVISION = 1;

    /*Componentes graficos del header*/
    public ImageView imgAdicional;
    public TextView lblTipoAdicional;

    public Spinner spnSelectorAdicionales;
    public ListView lstListaAdicionales;

    private int tipo;
    private String departamento;
    private String estrato;
    private Cliente cliente;

    private ArrayList<ListaAdicionales> adicionales = new ArrayList<ListaAdicionales>();

    private Observer observer;

    public CompAdicional(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.compadicional);
        tipo = Integer.parseInt(a.getString(0));
        init();
        cargarHeaderInformation();
    }

    public CompAdicional(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.compadicional);
        tipo = Integer.parseInt(a.getString(0));
        init();
        cargarHeaderInformation();
    }

    private void init(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

        li.inflate(R.layout.compadicional, this, true);

        imgAdicional = (ImageView) findViewById(R.id.imgAdicional);
        lblTipoAdicional = (TextView) findViewById(R.id.lblTipoAdicional);

        spnSelectorAdicionales = (Spinner) findViewById(R.id.spnSelectorAdicionales);
        lstListaAdicionales = (ListView) findViewById(R.id.lstListaAdicionales);

        spnSelectorAdicionales.setOnItemSelectedListener(seleccionarAdicional);
    }

    private void cargarHeaderInformation(){
        switch (tipo) {
            case TELEFONIA:
                imgAdicional.setImageDrawable(getResources().getDrawable(R.drawable.amgto));
                lblTipoAdicional.setText(getResources().getString(R.string.adiconalestelefonia));
                break;
            case TELEVISION:
                imgAdicional.setImageDrawable(getResources().getDrawable(R.drawable.amgtv));
                lblTipoAdicional.setText(getResources().getString(R.string.adicionalestelevision));
                break;
        }
    }

    private void cargarAdicionales(String plan){
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[] { "lst_valor" }, "lst_nombre=? and lst_clave=?",
                new String[] { "adicionalesTVDigital", plan }, null, null, null);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item);
        adaptador.add("-- Seleccione Adicional --");
        if (respuesta != null) {
            for (ArrayList<String> arrayList : respuesta) {
                // adaptador.add(arrayList.get(0));
                System.out.println("adicional name " + arrayList.get(0));
                adaptador.add(arrayList.get(0));

            }
        }
        spnSelectorAdicionales.setAdapter(adaptador);
    }

    private void consultarAdicional(String adicional) {
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Adicionales",
                new String[] { "adicional", "tarifa", "tarifaIva" },
                "departamento like ? and (producto like ? or producto like ?) and tipoProducto = ? and estrato like ? and adicional = ?",
                new String[] { "%" + cliente.getDepartamento() + "%", "%HFC%", "%IPTV%", "tv", "%" + cliente.getEstrato() + "%", adicional },
                null, "producto,adicional ASC", null);

        System.out.println("Respuesta Adicionales "+respuesta);

        if (respuesta != null) {

            String[][] data = new String[1][2];

            data[0][0] = respuesta.get(0).get(0);
            data[0][1] = respuesta.get(0).get(2);

			/*
			 * ArrayList<ArrayList<String>> result = Tarificador
			 * .consultarDescuentos(data, departamento, true, 1);
			 */

            ArrayList<ArrayList<String>> result = Tarificador.consultarDescuentos2(data, departamento, true, 1,
                   UtilidadesTarificador.jsonDatos(cliente, "1", "N/A", "ingresar 0 o 1"));

            System.out.println(result);

            ListaAdicionales adicionalItem = null;
            if (result != null) {
                adicionalItem = new ListaAdicionales(adicional, respuesta.get(0).get(2), result.get(0).get(1),
                        result.get(0).get(2));
            } else {
                adicionalItem = new ListaAdicionales(adicional, respuesta.get(0).get(2), "", "");
            }

            ArrayList<Boolean> agregar = new ArrayList<Boolean>();
            for (int i = 0; i < adicionales.size(); i++) {
                if (!adicionales.get(i).getAdicional().equals(adicional)) {
                    agregar.add(true);
                } else {
                    agregar.add(false);
                }
            }

            if (!agregar.contains(false)) {
                adicionales.add(adicionalItem);
            }

            actualizarLista();
        }
    }

    private void actualizarLista() {

        ListaAdicionalesAdapter adapter = new ListaAdicionalesAdapter((Activity) getContext(), adicionales,
                getContext(), "");
        adapter.addObserver(this);
        lstListaAdicionales.setAdapter(adapter);
        ControlSimulador.setListViewHeightBasedOnChildren(lstListaAdicionales);

        observer.update(null);
    }

    public double calcularTotal(){

        double total = 0;

        for (ListaAdicionales adicional: adicionales) {
            total += Double.parseDouble(adicional.getPrecio());
        }

        return total;

    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    AdapterView.OnItemSelectedListener seleccionarAdicional = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            consultarAdicional((String) parent.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void seleccionarPlan(String plan) {
        cargarAdicionales(plan);
    }

    @Override
    public void deshabilitarAdicionales() {
        setVisibility(GONE);
    }

    @Override
    public void habilitarAdicionales() {
        setVisibility(VISIBLE);
    }

    @Override
    public void limpiarAdicionales() {
        adicionales.clear();
        actualizarLista();
    }

    @Override
    public void update(Object value) {
        ArrayList<Object> data = (ArrayList<Object>) value;
        String lugar = data.get(0).toString();

        if (lugar.equalsIgnoreCase("eliminar")) {
            int position = Integer.parseInt(data.get(1).toString());
            adicionales.remove(position);
            actualizarLista();
        }
    }

    @Override
    public void addObserver(Observer o) {
        observer = o;
    }

    @Override
    public void removeObserver(Observer o) {
        observer = null;
    }

    @Override
    public void notifyObserver() {

    }
}
