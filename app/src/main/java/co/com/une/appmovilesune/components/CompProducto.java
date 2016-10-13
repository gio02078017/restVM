package co.com.une.appmovilesune.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;

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

    /*Componentes graficos del selector*/
    private Spinner spntipeticionproducto;
    private Spinner spnSelectorPlan;
    private TextView txtPlanFacturacion;
    private TextView txtVelocidad;


    /*Componentes graficos del detelle de valores de cargo basico*/
    private TextView txtvalorcargobasico;
    private TextView txtvalordescuentocargobasico;
    private TextView txtduraciondescuentocargobasico;

    /*Componentes graficos del detelle de valores de pago anticipado*/
    private TextView txtvalorpagoanticipado;

    /*Componentes graficos del detelle de valores de pago parcial*/
    private TextView txtvalorpagoparcial;
    private Spinner spndescuentopagoparcial;
    private TextView txtvalorpagoparcialdescuento;

    private int tipo;

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

        spntipeticionproducto = (Spinner) findViewById(R.id.spntipeticionproducto);
        spnSelectorPlan = (Spinner) findViewById(R.id.spnSelectorPlan);
        txtPlanFacturacion = (TextView) findViewById(R.id.txtPlanFacturacion);
        txtVelocidad = (TextView) findViewById(R.id.txtVelocidad);

        txtvalorcargobasico = (TextView) findViewById(R.id.txtvalorcargobasico);
        txtvalordescuentocargobasico = (TextView) findViewById(R.id.txtvalordescuentocargobasico);
        txtduraciondescuentocargobasico = (TextView) findViewById(R.id.txtduraciondescuentocargobasico);

        txtvalorpagoanticipado = (TextView) findViewById(R.id.txtvalorpagoanticipado);

        txtvalorpagoparcial = (TextView) findViewById(R.id.txtvalorpagoparcial);
        spndescuentopagoparcial = (Spinner) findViewById(R.id.spndescuentopagoparcial);
        txtvalorpagoparcialdescuento = (TextView) findViewById(R.id.txtvalorpagoparcialdescuento);

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
