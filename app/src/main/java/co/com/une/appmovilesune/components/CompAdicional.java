package co.com.une.appmovilesune.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ListaAdicionales;
import co.com.une.appmovilesune.adapters.ListaAdicionalesAdapter;
import co.com.une.appmovilesune.change.ControlSimulador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.change.UtilidadesTarificadorNew;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.ObserverAdicionales;
import co.com.une.appmovilesune.interfaces.ObserverAdicionalesInternet;
import co.com.une.appmovilesune.interfaces.ObserverTotales;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.interfaces.SubjectTotales;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Tarificador;

/**
 * Created by davids on 25/10/16.
 */

public class CompAdicional extends LinearLayout implements ObserverAdicionales, Observer, Subject, ObserverAdicionalesInternet {

    public static final int TELEFONIA = 0;
    public static final int TELEVISION = 1;
    public static final int INTERNET = 2;

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

    private String planBA = "";

    private boolean HBOGOExistente = false;

    private boolean limpiar = false;

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

    private void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

        li.inflate(R.layout.compadicional, this, true);

        imgAdicional = (ImageView) findViewById(R.id.imgAdicional);
        lblTipoAdicional = (TextView) findViewById(R.id.lblTipoAdicional);

        spnSelectorAdicionales = (Spinner) findViewById(R.id.spnSelectorAdicionales);
        lstListaAdicionales = (ListView) findViewById(R.id.lstListaAdicionales);

        spnSelectorAdicionales.setOnItemSelectedListener(seleccionarAdicional);
    }

    private void cargarHeaderInformation() {
        switch (tipo) {
            case TELEFONIA:
                imgAdicional.setImageDrawable(getResources().getDrawable(R.drawable.amgto));
                lblTipoAdicional.setText(getResources().getString(R.string.adiconalestelefonia));
                break;
            case TELEVISION:
                imgAdicional.setImageDrawable(getResources().getDrawable(R.drawable.amgtv));
                lblTipoAdicional.setText(getResources().getString(R.string.adicionalestelevision));
                break;
            case INTERNET:
                imgAdicional.setImageDrawable(getResources().getDrawable(R.drawable.amgba));
                lblTipoAdicional.setText(getResources().getString(R.string.adicionalesinternet));
                break;
        }
    }

    private void cargarAdicionales(String plan) {
        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "listasvalores",
                new String[]{"lst_valor"}, "lst_nombre=? and lst_clave=?",
                new String[]{"adicionalesTVDigital", plan}, null, null, null);

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

    private void cargarAdicionalesBa(String plan, boolean limpiar) {

        System.out.println("plan ba"+plan);
        planBA = plan;

        this.limpiar = limpiar;

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Adicionales",
                new String[]{"adicional"}, "departamento=? and tipoProducto=? and estrato like ? and tecnologia like ?",
                new String[]{cliente.getDepartamento(), "ba", "%" + cliente.getEstrato() + "%", "%" + cliente.getTecnologia() + "%" }, null, null, null);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item);
        adaptador.add("-- Seleccione Adicional --");
        if(!planBA.equalsIgnoreCase(Utilidades.inicial)){
            if (respuesta != null) {
                for (ArrayList<String> arrayList : respuesta) {
                    // adaptador.add(arrayList.get(0));
                    System.out.println("adicional name " + arrayList.get(0));
                    if(UtilidadesTarificadorNew.validarHomologadoHBOGO(arrayList.get(0)).equalsIgnoreCase("HBO GO")){
                        if(!UtilidadesTarificadorNew.televisionExistentePortafolioElite(cliente.getPortafolioElite(),cliente.getCedula()) &&
                                !UtilidadesTarificadorNew.validarHBOGOPortafolioElite(cliente.getPortafolioElite(),cliente.getCedula())){
                            HBOGOExistente = false;
                            if(UtilidadesTarificadorNew.validarVelocidadInternet(planBA,cliente) && !limpiar){
                                adaptador.add(arrayList.get(0));
                            }
                        } else {
                            if(UtilidadesTarificadorNew.televisionExistentePortafolioElite(cliente.getPortafolioElite(),cliente.getCedula())){
                                Toast.makeText(getContext(),getResources().getString(R.string.mensajehbogotvexistente),Toast.LENGTH_LONG).show();
                            } else if(UtilidadesTarificadorNew.validarHBOGOPortafolioElite(cliente.getPortafolioElite(),cliente.getCedula())){
                                Toast.makeText(getContext(),getResources().getString(R.string.mensajehbogoexistente),Toast.LENGTH_LONG).show();
                                HBOGOExistente = true;
                            }
                        }
                    } else {
                        adaptador.add(arrayList.get(0));
                    }

                }
            }
        }

        spnSelectorAdicionales.setAdapter(adaptador);
    }

    private void cargarAdicionalesTo(String plan) {
        System.out.println("plan to "+plan);

        String homologarPlanTO =  Utilidades.claveValor("homologarPlanTO",plan);

        System.out.println("homologarPlanTO "+homologarPlanTO);

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Adicionales",
                new String[]{"adicional"}, "departamento=? and tipoProducto=? and estrato like ? and tecnologia like ? and plan=?",
                new String[]{cliente.getDepartamento(), "to", "%" + cliente.getEstrato() + "%", "%" + cliente.getTecnologia() + "%", homologarPlanTO}, null, null, null);

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

    private void consultarAdicional(String adicional, int tipo) {

        String parametros = "";
        String[] datos = null;

        if (tipo == TELEVISION) {
            parametros = "departamento like ? and (producto like ? or producto like ?) and tipoProducto = ? and estrato like ? and adicional = ?";
            datos = new String[]{"%" + cliente.getDepartamento() + "%", "%HFC%", "%IPTV%", "tv", "%" + cliente.getEstrato() + "%", adicional};
        } else if (tipo == TELEFONIA) {
            parametros = "departamento like ? and tipoProducto = ? and estrato like ? and adicional = ?";
            datos = new String[]{"%" + cliente.getDepartamento() + "%",  "to", "%" + cliente.getEstrato() + "%", adicional};
        } else if(tipo == INTERNET) {
            parametros = "departamento like ? and tipoProducto = ? and estrato like ? and adicional = ?";
            datos = new String[]{"%" + cliente.getDepartamento() + "%",  "ba", "%" + cliente.getEstrato() + "%", adicional};
        }

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Adicionales",
                new String[]{"adicional", "tarifa", "tarifaIva"},
                parametros, datos,
                null, "producto,adicional ASC", null);

        System.out.println("Respuesta Adicionales " + respuesta);

        if (respuesta != null) {

            String[][] data = new String[1][2];

            data[0][0] = respuesta.get(0).get(0);
            data[0][1] = respuesta.get(0).get(2);

			/*
             * ArrayList<ArrayList<String>> result = Tarificador
			 * .consultarDescuentos(data, departamento, true, 1);
			 */

            ArrayList<ArrayList<String>> result = null;

            if(tipo == TELEVISION){
                result = Tarificador.consultarDescuentos2(data, departamento, true, 1,
                        UtilidadesTarificador.jsonDatos(cliente, "1", "N/A", "ingresar 0 o 1"));
            } else if (tipo == INTERNET) {
                result = Tarificador.consultarDescuentosAdicionalesInternet(data, departamento, true, 1,
                        UtilidadesTarificador.jsonDatos(cliente, "1", "N/A", "N/A"));
            }

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

    public double calcularTotal() {

        double total = 0;

        for (ListaAdicionales adicional : adicionales) {
            total += Double.parseDouble(adicional.getPrecio());
        }

        return total;

    }

    public ArrayList<ListaAdicionales> getAdicionales() {
        return adicionales;
    }

    public String[][] arrayAdicionales() {
        int contAdicionales = adicionales.size();

        String[][] arrayAd = new String[contAdicionales][2];

        for (int i = 0; i < adicionales.size(); i++) {
            arrayAd[i][0] = adicionales.get(i).getAdicional();
            arrayAd[i][1] = adicionales.get(i).getPrecio();
        }

        return arrayAd;

    }

    public String[][] arrayAdicionalesHBOGO() {
        int contAdicionales = adicionales.size();

        String[][] arrayAd = new String[contAdicionales+1][2];

        for (int i = 0; i < adicionales.size(); i++) {
            arrayAd[i][0] = adicionales.get(i).getAdicional();
            arrayAd[i][1] = adicionales.get(i).getPrecio();
        }

        arrayAd[contAdicionales][0] = "HBO GO";
        arrayAd[contAdicionales][1] = "0";



        return arrayAd;

    }

    public ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales() {

        ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();

        int contAdicionales = adicionales.size();

        String[][] arrayAd = new String[contAdicionales][2];

        for (int i = 0; i < adicionales.size(); i++) {
            System.out.println("adicionales.get(i).getDescuento() " + adicionales.get(i).getAdicional());
            System.out.println("adicionales.get(i).getDescuento() " + adicionales.get(i).getDescuento());
            System.out.println("adicionales.get(i).getDuracion() " + adicionales.get(i).getDuracion());
            if (!adicionales.get(i).getDescuento().equalsIgnoreCase("") && !adicionales.get(i).getDescuento().equalsIgnoreCase("-")) {

                String meses;

                if (adicionales.get(i).getDuracion().equalsIgnoreCase("1")) {
                    meses = adicionales.get(i).getDuracion() + " Mes";
                } else {
                    meses = adicionales.get(i).getDuracion() + " Meses";
                }

                itemPromocionesAdicionales.add(new ItemPromocionesAdicionales(adicionales.get(i).getAdicional(), adicionales.get(i).getDescuento(), "TV", meses));
            }
        }

        if (itemPromocionesAdicionales.size() == 0) {
            itemPromocionesAdicionales.clear();
            itemPromocionesAdicionales.add(0, new ItemPromocionesAdicionales("Vacia"));
        }

        return itemPromocionesAdicionales;

    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean isHBOGOExistente() {
        return HBOGOExistente;
    }

    public void setHBOGOExistente(boolean HBOGOExistente) {
        this.HBOGOExistente = HBOGOExistente;
    }

    AdapterView.OnItemSelectedListener seleccionarAdicional = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            consultarAdicional((String) parent.getSelectedItem(),tipo);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void seleccionarPlan(String plan) {
        System.out.println("plan adicional " + plan);
        if (tipo == TELEVISION) {
            cargarAdicionales(plan);
        } else if (tipo == TELEFONIA) {
            cargarAdicionalesTo(plan);
        } else if(tipo == INTERNET){
            cargarAdicionalesBa(plan, limpiar);
        }
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
    public void limpiarAdicionalHBOGO(boolean limpiar) {
        for(ListaAdicionales adicional: adicionales){
            Log.d("HBOGO",adicional.getAdicional());
            if(UtilidadesTarificadorNew.validarHomologadoHBOGO(adicional.getAdicional()).equalsIgnoreCase("HBO GO")){
                adicionales.remove(adicional);
            }
        }
        actualizarLista();
        cargarAdicionalesBa(planBA, limpiar);

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
