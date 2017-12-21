package co.com.une.appmovilesune.change;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class Busqueda extends RelativeLayout {

    public ImageButton boton;
    private EditText busqueda;

    public Busqueda(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.busqueda, this);
        busqueda = (EditText) findViewById(R.id.txtCampo);
        boton = (ImageButton) findViewById(R.id.btnBuscar);
    }

    public void setBusqueda(String criterio) {
        busqueda.setText(criterio);
    }

    public String getBusqueda() {
        return busqueda.getText().toString();
    }

    public void cambiarTipo(String tipo) {

        if (tipo.equalsIgnoreCase("text")) {
            busqueda.setInputType(android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        } else {
            busqueda.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        }
    }

    public void setEditable(boolean editable) {
        // return busqueda.getText().toString();
        busqueda.setEnabled(editable);
    }

}