package co.com.une.appmovilesune.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.ProductoCotizador;

/**
 * Created by davids on 13/10/16.
 */

public class CompProducto extends LinearLayout implements Subject {

    public static final int TELEFONIA = 0;
    public static final int TELEVISION = 1;
    public static final int INTERNET = 2;

    /*Componentes graficos del header*/
    private ImageView imgProducto;
    private CheckBox chkHabilitarProducto;
    private TextView lblTipoProducto;

    /*Contenedor de los componentes de producto*/
    private LinearLayout llyProducto;

    /*Componentes graficos del selector*/
    private Spinner spntipeticionproducto;
    private Spinner spnSelectorPlan;
    private TextView txtPlanFacturacion;
    private TextView txtVelocidad;


    /*Componentes graficos del detelle de valores de cargo basico*/
    private TextView txtvalorcargobasicoind;
    private TextView txtvalorcargobasicoemp;
    private TextView txtvalordescuentocargobasico;
    private TextView txtduraciondescuentocargobasico;

    /*Componentes graficos del detelle de valores de pago anticipado*/
    private TextView txtvalorpagoanticipado;

    /*Componentes graficos del detelle de valores de pago parcial*/
    private TextView txtvalorpagoparcial;
    private Spinner spndescuentopagoparcial;
    private TextView txtvalorpagoparcialdescuento;

    private int tipo;
    private String departamento = "Antioquia";
    private int estrato = 3;
    private String tecnologia = "HFC";
    private String oferta = "ULTRA TRIO GOLD";

    private ProductoCotizador producto;

    public CompProducto(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.compproducto);
        tipo = Integer.parseInt(a.getString(0));
        init();
        cargarHeaderInformation();
    }

    public CompProducto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.compproducto);
        tipo = Integer.parseInt(a.getString(0));
        init();
        cargarHeaderInformation();
    }

    private void init(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

        li.inflate(R.layout.compproducto, this, true);

        imgProducto = (ImageView) findViewById(R.id.imgProducto);
        chkHabilitarProducto = (CheckBox) findViewById(R.id.chkHabilitarProducto);
        lblTipoProducto = (TextView) findViewById(R.id.lblTipoProducto);

        llyProducto = (LinearLayout) findViewById(R.id.llyProducto);

        spntipeticionproducto = (Spinner) findViewById(R.id.spntipeticionproducto);
        spnSelectorPlan = (Spinner) findViewById(R.id.spnSelectorPlan);
        txtPlanFacturacion = (TextView) findViewById(R.id.txtPlanFacturacion);
        txtVelocidad = (TextView) findViewById(R.id.txtVelocidad);

        txtvalorcargobasicoind = (TextView) findViewById(R.id.txtvalorcargobasicoind);
        txtvalorcargobasicoemp = (TextView) findViewById(R.id.txtvalorcargobasicoemp);
        txtvalordescuentocargobasico = (TextView) findViewById(R.id.txtvalordescuentocargobasico);
        txtduraciondescuentocargobasico = (TextView) findViewById(R.id.txtduraciondescuentocargobasico);

        txtvalorpagoanticipado = (TextView) findViewById(R.id.txtvalorpagoanticipado);

        txtvalorpagoparcial = (TextView) findViewById(R.id.txtvalorpagoparcial);
        spndescuentopagoparcial = (Spinner) findViewById(R.id.spndescuentopagoparcial);
        txtvalorpagoparcialdescuento = (TextView) findViewById(R.id.txtvalorpagoparcialdescuento);

        chkHabilitarProducto.setChecked(true);
        chkHabilitarProducto.setOnCheckedChangeListener(habilitarCompProducto);

        spntipeticionproducto.setOnItemSelectedListener(seleccionarTipoTransaccion);

    }

    private void cargarHeaderInformation(){
        switch (tipo) {
            case 0:
                imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.amgto));
                lblTipoProducto.setText(getResources().getString(R.string.telefonia));
                break;
            case 1:
                imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.amgtv));
                lblTipoProducto.setText(getResources().getString(R.string.television));
                break;
            case 2:
                imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.amgba));
                lblTipoProducto.setText(getResources().getString(R.string.internet));
                break;
        }
    }

    private String traducirProducto(){

        String productoAbreviacion = "";

        switch (tipo){
            case 0:
                productoAbreviacion = "to";
                break;
            case 1:
                productoAbreviacion = "tv";
                break;
            case 2:
                productoAbreviacion = "ba";
                break;
        }

        return productoAbreviacion;

    }

    private String traducirTipoPeticion(){

        String numeroTipoPeticion = "";

        if (((String)spntipeticionproducto.getSelectedItem()).equals("N")) {
            numeroTipoPeticion = "1";
        }else if(((String)spntipeticionproducto.getSelectedItem()).equals("C")){
            numeroTipoPeticion = "0";
        }else if(((String)spntipeticionproducto.getSelectedItem()).equals("E")){
            numeroTipoPeticion = "3";
        }

        return  numeroTipoPeticion;

    }

    public void cargarPlanes(String departamento, int estrato, String tecnologia, String oferta){

        String clausula = "";
        String[] valores = null;

        if(oferta.equals("-")){
            oferta = "";
        }

        if(traducirTipoPeticion().equals("0") || traducirTipoPeticion().equals("1")){

            clausula = "Departamento=? and Tipo_Producto=? and Nuevo IN(?,?) and Estrato like ? and Tecnologia like ? and Oferta = ?";
            valores = new String[] { departamento, traducirProducto(), traducirTipoPeticion(),"2", "%" + estrato + "%",
                    "%" + tecnologia + "%", oferta };

        }else {
            clausula = "Departamento=? and Tipo_Producto=? and Nuevo IN(?) and Estrato like ? and Tecnologia like ?";
            valores = new String[] { departamento, traducirProducto(), traducirTipoPeticion(), "%" + estrato + "%",
                    "%" + tecnologia + "%"};
        }



        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(false, "Productos", new String[] { "Producto" }, clausula,
                valores, null, null, null);

        System.out.println(respuesta);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);

        adaptador.add(Utilidades.inicial);

        if (respuesta != null) {
            for (ArrayList<String> arrayList : respuesta) {
                adaptador.add(arrayList.get(0));
            }
        }

        spnSelectorPlan.setAdapter(adaptador);

    }

    private void cargarValoresProducto(){

    }

    CompoundButton.OnCheckedChangeListener habilitarCompProducto = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                slide_down(getContext(),llyProducto);
                llyProducto.setVisibility(VISIBLE);
            }else{
                slide_up(getContext(),llyProducto);
                llyProducto.setVisibility(GONE);
            }
        }
    };

    AdapterView.OnItemSelectedListener seleccionarTipoTransaccion = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cargarPlanes(departamento,estrato,tecnologia,oferta);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void slide_down(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public void slide_up(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public boolean isActivo(){
        return chkHabilitarProducto.isChecked();
    }

    @Override
    public void addObserver(Observer o) {

    }

    @Override
    public void removeObserver(Observer o) {

    }

    @Override
    public void notifyObserver() {

    }
}

