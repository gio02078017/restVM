package co.com.une.appmovilesune.complements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.controller.ControlConfiguracion;
import co.com.une.appmovilesune.interfaces.Observer;

import com.google.analytics.tracking.android.EasyTracker;

public class Autenticacion extends Activity implements Observer {

    private TituloPrincipal tp;
    private EditText txtCodigo, txtCedula;
    private TextView txtVersion;

    public void onBackPressed() {
        return;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autenticacion);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo(getResources().getString(R.string.autenticacion));

        txtCodigo = (EditText) findViewById(R.id.txtCodigo);
        txtCedula = (EditText) findViewById(R.id.txtCedula);
        txtVersion = (TextView) findViewById(R.id.txtVersion);

        txtVersion.setText("V. " + MainActivity.config.getVersion());

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

    public void autenticar(View v) {

        MainActivity.ccfg = new ControlConfiguracion();
        MainActivity.ccfg.setContext(this);
        MainActivity.ccfg.addObserver(this);
        String[] datos = new String[]{txtCodigo.getText().toString(), txtCedula.getText().toString()};
        MainActivity.ccfg.execute(datos);

    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub
        Boolean respuesta = (Boolean) value;
        if (!respuesta) {
            // System.out.println("No Autenticado");
            Toast.makeText(this, getResources().getString(R.string.errorautenticacion), Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("value " + value);
            Intent intent = new Intent();
            intent.putExtra("result", "Bienvenido");
            setResult(MainActivity.OK_RESULT_CODE, intent);
            finish();
        }
    }

}
