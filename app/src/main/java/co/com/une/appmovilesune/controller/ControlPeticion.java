package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.SelectorProductos;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Peticion;
import co.com.une.appmovilesune.R;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ControlPeticion extends Activity {

    private Peticion peticion;

    private Spinner sltTransacciones;
    private SelectorProductos slpProductos;
    private EditText txtOtraTransaccion, txtEvento, txtPedido, txtCun;

    private Dialogo dialogoProductos;

    private TituloPrincipal tp;

    ArrayAdapter<String> adaptador;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpeticiones);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("Petición");

        dialogoProductos = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_PRODUCTO, "");
        dialogoProductos.dialogo.setOnDismissListener(dlp);

        peticion = new Peticion();

        asignarCampos();
        llenarSpiners();

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            peticion = (Peticion) reicieveParams.getSerializable("peticion");
            if (peticion != null) {
                llenarCampos();
            }

        } else {
            // System.out.println("no peticion");
        }
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

    private void asignarCampos() {
        sltTransacciones = (Spinner) findViewById(R.id.sltTransaccion);
        slpProductos = (SelectorProductos) findViewById(R.id.slpPeticion);
        txtOtraTransaccion = (EditText) findViewById(R.id.txtOtraTransaccion);
        txtEvento = (EditText) findViewById(R.id.txtEvento);
        txtPedido = (EditText) findViewById(R.id.txtPedido);
        txtCun = (EditText) findViewById(R.id.txtCun);
    }

    private void llenarSpiners() {
        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                peticion.obtenerListaTransaccion());
        sltTransacciones.setAdapter(adaptador);
    }

    private void llenarCampos() {
        sltTransacciones.setSelection(adaptador.getPosition(peticion.getTransaccion()));
        slpProductos.setTexto(peticion.getProductos());
        txtOtraTransaccion.setText(peticion.getCual());
        txtEvento.setText(peticion.getEvento());
        txtPedido.setText(peticion.getPedido());
        txtCun.setText(peticion.getCun());
    }

    public void procesarPeticion(View v) {
        peticion = new Peticion(sltTransacciones.getSelectedItem().toString(), txtOtraTransaccion.getText().toString(),
                slpProductos.getTexto(), txtEvento.getText().toString(), txtPedido.getText().toString(),
                txtCun.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("peticion", peticion);
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
    }

    public void mostrarDialogo(View v) {
        dialogoProductos.dialogo.show();
    }

    OnDismissListener dlp = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {

            // TODO Auto-generated method stub
            // System.out.println(dialogoProductos.isSeleccion());
            if (dialogoProductos.isSeleccion()) {
                slpProductos.setTexto(dialogoProductos.getProductos().toString());
            }

        }
    };
}
