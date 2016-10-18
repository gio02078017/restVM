package co.com.une.appmovilesune.controller;

import android.app.Activity;
import android.os.Bundle;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.components.CompProducto;

/**
 * Created by davids on 18/10/16.
 */

public class ControlCotizador extends Activity {

    private CompProducto cprdTelevision;
    private CompProducto cprdInternet;
    private CompProducto cprdTelefonia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewcotizador);

        cprdTelevision = (CompProducto) findViewById(R.id.cprdTelevision);
        cprdInternet = (CompProducto) findViewById(R.id.cprdInternet);
        cprdTelefonia = (CompProducto) findViewById(R.id.cprdTelefonia);

        cprdTelevision.cargarPlanes("Antioquia",3,"HFC");

    }
}
