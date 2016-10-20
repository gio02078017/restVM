package co.com.une.appmovilesune.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.components.CompProducto;
import co.com.une.appmovilesune.interfaces.Observer;

/**
 * Created by davids on 18/10/16.
 */

public class ControlCotizador extends Activity implements Observer{

    private CheckBox chkHogarNuevo;
    private CheckBox chkAnaloga;
    private Spinner spnestrato;
    private Spinner spntipooferta;
    private Spinner spnoferta;

    private CompProducto cprdTelevision;
    private CompProducto cprdInternet;
    private CompProducto cprdTelefonia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewcotizador);

        chkHogarNuevo = (CheckBox) findViewById(R.id.chkHogarNuevo);
        chkAnaloga = (CheckBox) findViewById(R.id.chkAnaloga);
        spnestrato = (Spinner) findViewById(R.id.spnestrato);
        spntipooferta = (Spinner) findViewById(R.id.spntipooferta);
        spnoferta = (Spinner) findViewById(R.id.spnoferta);

        cprdTelevision = (CompProducto) findViewById(R.id.cprdTelevision);
        cprdInternet = (CompProducto) findViewById(R.id.cprdInternet);
        cprdTelefonia = (CompProducto) findViewById(R.id.cprdTelefonia);

        cprdTelevision.addObserver(this);
        cprdInternet.addObserver(this);
        cprdTelefonia.addObserver(this);

        spnestrato.setOnItemSelectedListener(seleccionarEstrato);
        spntipooferta.setOnItemSelectedListener(seleccionarTipoOferta);
        spnoferta.setOnItemSelectedListener(seleccionarOferta);

    }

    private void llenarOfertas(String estrato, String tipoPaquete){

        if(estrato.equals("-")){
            estrato = "";
        }

        if(tipoPaquete.equals("-")){
            tipoPaquete = "";
        }else if(tipoPaquete.equals("Individual")){
            tipoPaquete = "Ind";
        }

        String clausula = "estrato like ? and tipo_paquete like ?";
        String[] valores = new String[] { "%"+estrato+"%","%"+tipoPaquete+"%" };

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios", new String[] { "Oferta" }, clausula,
                valores, null, null, null);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        System.out.println(respuesta);

        if(respuesta != null){
            for (ArrayList<String> arrayList : respuesta) {
                if(arrayList.get(0).equals("")){
                    adaptador.add("-");
                }else{
                    adaptador.add(arrayList.get(0));
                }
            }
        }

        spnoferta.setAdapter(adaptador);
    }

    private void parametrizarComponentes(){
        cprdTelevision.cargarPlanes("Antioquia",Integer.parseInt((String)spnestrato.getSelectedItem()),"HFC",(String)spnoferta.getSelectedItem());
        cprdInternet.cargarPlanes("Antioquia",Integer.parseInt((String)spnestrato.getSelectedItem()),"HFC",(String)spnoferta.getSelectedItem());
        cprdTelefonia.cargarPlanes("Antioquia",Integer.parseInt((String)spnestrato.getSelectedItem()),"HFC",(String)spnoferta.getSelectedItem());


    }

    AdapterView.OnItemSelectedListener seleccionarEstrato = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            llenarOfertas((String)parent.getSelectedItem(),(String)spntipooferta.getSelectedItem());
            parametrizarComponentes();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener seleccionarTipoOferta = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            llenarOfertas((String)spnestrato.getSelectedItem(),(String)parent.getSelectedItem());
            parametrizarComponentes();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener seleccionarOferta = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            parametrizarComponentes();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void update(Object value) {

    }
}
