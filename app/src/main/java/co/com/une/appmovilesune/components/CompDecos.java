package co.com.une.appmovilesune.components;

import java.sql.SQLOutput;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemDecodificador;
import co.com.une.appmovilesune.adapters.ListaDecodificadoresAdapter;
import co.com.une.appmovilesune.change.ControlSimulador;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.change.UtilidadesDecos;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.ObserverAdicionales;
import co.com.une.appmovilesune.interfaces.ObserverDecodificadores;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Decodificadores;

public class CompDecos extends LinearLayout implements Observer, Subject, ObserverDecodificadores {

    public ListView listaDecodificadores;
    private ImageView imgagregardecodificador;
    private ArrayList<ItemDecodificador> decos;
    private ArrayList<ItemDecodificador> decosExistentes;
    private Decodificadores decodificadores;
    private String ciudad;
    private boolean permisoFideliza;
    private Observer observer;
    private String oferta;
    private String plan;
    private boolean controlUpdate = false;

    public CompDecos(Context context) {
        super(context);
        init();
    }

    public CompDecos(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.compdecos);
    }

    public CompDecos(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.compdecos);
    }

    private void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

        li.inflate(R.layout.compdecos, this, true);

        listaDecodificadores = (ListView) findViewById(R.id.listadecodifcadores);
        imgagregardecodificador = (ImageView) findViewById(R.id.imgagregardecodificador);
        imgagregardecodificador.setOnClickListener(clicAgregarDecodificador);

    }

    private void agregarDecodificador() {

        if (decos == null) {
            decos = new ArrayList<ItemDecodificador>();
        }

        if (!valdiarRetiros()) {
            ItemDecodificador decodificador = new ItemDecodificador("", "AL", false, "NA", "-", "0", "Nuevo", true, false, false);
            decos.add(decodificador);
        } else {
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.noseagreganporretiros),
                    Toast.LENGTH_LONG).show();
        }
        decodificadores.setItemDecodificadors(decos);
        actualizarLista();

    }

    private boolean valdiarRetiros() {
        boolean retiros = false;
        for (ItemDecodificador itemDecodificador : decos) {
            if (itemDecodificador.getTipoTransaccion().equals("Retiro")) {
                retiros = true;
            }
        }

        return retiros;

    }

    private void borrarDecodificador(int position) {
        decos.remove(position);
        decodificadores.setItemDecodificadors(decos);
        actualizarLista();
    }

    private void actualizarLista() {



        if (decos != null) {
            UtilidadesDecos.imprimirDecos("compDecos", decos);
            //System.out.println("decos "+decodificadores);
            //System.out.println("decos item  "+decodificadores.getItemDecodificadors());
            ListaDecodificadoresAdapter adapter = new ListaDecodificadoresAdapter((Activity) getContext(), getContext(), decodificadores);
            adapter.addObserver(this);
            adapter.setPlan(plan);
            listaDecodificadores.setAdapter(adapter);
            ControlSimulador.setListViewHeightBasedOnChildren(listaDecodificadores);
        }

        if (oferta.equals("tarificador")) {
            observer.update(null);
        }


    }

    public void deshabilitarAgregar() {
        imgagregardecodificador.setVisibility(View.GONE);
    }

    OnClickListener clicAgregarDecodificador = new OnClickListener() {

        @Override
        public void onClick(View v) {
            agregarDecodificador();
        }
    };

    @Override
    public void update(Object value) {

        ArrayList<Object> data = (ArrayList<Object>) value;
        String lugar = data.get(0).toString();

        if (lugar.equalsIgnoreCase("eliminar")) {
            int position = Integer.parseInt(data.get(1).toString());
            borrarDecodificador(position);
        } else if (lugar.equalsIgnoreCase("cambio")) {
            notifyObserver();
        } else if (lugar.equalsIgnoreCase("update")) {
            //notifyObserver();
            System.out.println("updateDecoNuevo update deco ");
            UtilidadesDecos.imprimirDecos("updateDecoNuevo getDecos() ",decos);
            //decvcontar
            comprobarDecosGratis();

        } else if (lugar.equalsIgnoreCase("plan")) {
            decos = null;
            System.out.println("data " + data);
            if (data.size() > 1 && data.get(1) != null) {

                plan = data.get(1).toString();

                decodificadores = UtilidadesDecos.consolidado(decosExistentes, data.get(1).toString());
                decodificadores.setPlan(plan);
                decodificadores.setCiudad(ciudad);
                decodificadores.setPermisoFideliza(permisoFideliza);

                //decos = UtilidadesDecos.logicaDecos(decosExistentes, data.get(1).toString());

                decos = decodificadores.getItemDecodificadors();

                if (decos != null) {
                    actualizarLista();
                }
            }

        }

    }

    public double obtenerTotalDecos() {
        double total = 0;

        if (decos != null) {
            for (int i = 0; i < decos.size(); i++) {
                if (!decos.get(i).getPrecio().equals("") && !decos.get(i).getPrecio().equals("-")) {
                    total += Double.parseDouble(decos.get(i).getPrecio());
                }
            }
        }

        return total;
    }

    private ArrayList<ItemDecodificador> limpiarTransacciones(ArrayList<ItemDecodificador> decos) {

        if (decos != null) {
            for (ItemDecodificador itemDecodificador : decos) {
                if (itemDecodificador.getTipoTransaccion().equals("Nuevo")) {
                    if (!itemDecodificador.getDestino().equals("-")) {
                        itemDecodificador.setOriginal(itemDecodificador.getDestino());
                    }
                    itemDecodificador.setDestino("-");
                } else if (itemDecodificador.getTipoTransaccion().equals("Retiro")) {
                    itemDecodificador.setPrecio("0");
                } else if (itemDecodificador.getTipoTransaccion().equals("Permanece")) {
                    if (itemDecodificador.isFideliza()) {
                        if (!itemDecodificador.isExistentesFideliza()) {
                            itemDecodificador.setTipoTransaccion("Fideliza");
                        }
                    }
                }
            }

            return decos;
        } else {
            return null;
        }

    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public boolean isPermisoFideliza() {
        return permisoFideliza;
    }

    public void setPermisoFideliza(boolean permisoFideliza) {
        this.permisoFideliza = permisoFideliza;
        actualizarLista();
    }

    public ArrayList<ItemDecodificador> getDecos() {
        return limpiarTransacciones(decos);
    }

    public void setDecos(ArrayList<ItemDecodificador> decos) {
        this.decos = decos;
        decodificadores.setItemDecodificadors(decos);
        actualizarLista();
    }

    public ArrayList<ItemDecodificador> getDecosExistentes() {
        return decosExistentes;
    }

    public void setDecosExistentes(ArrayList<ItemDecodificador> decosExistentes) {
        this.decosExistentes = decosExistentes;
    }

    public String getOferta() {
        return oferta;
    }

    public void setOferta(String oferta) {
        this.oferta = oferta;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
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
        ArrayList<Object> respuesta = new ArrayList<Object>();
        respuesta.add("decos");
        System.out.println("Oferta notificada =>" + oferta);
        respuesta.add(oferta);
        observer.update(respuesta);
    }

    @Override
    public void seleccionarPlan(String plan) {
        System.out.println("Seleccionar Plan ->" + plan);
        setPlan(plan);
    }

    @Override
    public void deshabilitarDecodificadores() {
        setVisibility(GONE);
    }

    @Override
    public void habilitarDecodificadores() {
        setVisibility(VISIBLE);
    }

    @Override
    public void limpiarDecodificadores() {
        if (decos != null) {
            decos.clear();
            decodificadores.setItemDecodificadors(decos);
        }

        actualizarLista();
    }

    @Override
    public void precargarDecos() {
        if (decos != null) {
            decos = new ArrayList<ItemDecodificador>();
        }
        //decos = UtilidadesDecos.logicaDecos(decos, plan);
        decodificadores = UtilidadesDecos.consolidado(decos, plan);
        decos = decodificadores.getItemDecodificadors();
        decodificadores.setPlan(plan);
        decodificadores.setCiudad(ciudad);
        decodificadores.setPermisoFideliza(permisoFideliza);
        actualizarLista();
    }

    public Decodificadores getDecodificadores() {
        return decodificadores;
    }

    public void setDecodificadores(Decodificadores decodificadores) {
        this.decodificadores = decodificadores;
    }

    public void comprobarDecosGratis(){
        int contadorIncluidos = 0;
        int contadorIncluidosListDecos = 0;
        int resta = 0;

        if(decodificadores !=  null && decodificadores.getListaDecosIncluido() != null) {
            for (int i = 0; i < decodificadores.getListaDecosIncluido().size(); i++) {
                contadorIncluidos += decodificadores.getListaDecosIncluido().get(i).getCantidad();
            }
        }

        for (int i = 0; i < decos.size(); i++) {
            if(decos.get(i).isIncluido()){
                contadorIncluidosListDecos += 1;
            }
        }

        System.out.println("updateDecoNuevo contadorIncluidos "+contadorIncluidos);
        System.out.println("updateDecoNuevo contadorIncluidosListDecos "+contadorIncluidosListDecos);

        if(contadorIncluidos > 0){
            resta = contadorIncluidos - contadorIncluidosListDecos;
            int contador = 0;
            if(resta > 0){
                for (int i = 0; i < decos.size(); i++) {
                    if(contador == resta){
                        break;
                    }
                    if(!decos.get(i).isIncluido()){
                        for (int j = 0; j < decodificadores.getListaDecosIncluido().size(); j++) {
                            if(decodificadores.getListaDecosIncluido().get(j).getTipoDeco().equalsIgnoreCase(decos.get(i).getDestino())){
                                contador ++;
                                /*decos.get(i).setIncluido(true);
                                decos.get(i).setPrecio("0");*/
                                String tipoDeco = decos.get(i).getDestino();
                                decos.remove(i);
                                decos.add(new ItemDecodificador("0|SD-0|HD-0|Ext-0|PVR-", "AL", false,
                                        "NA", tipoDeco, "0", "Nuevo", true, true, false));
                                break;
                            }
                        }
                    }
                }
                if(contador > 0){
                    actualizarLista();
                }
            }
        }

    }
}
