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
import co.com.une.appmovilesune.components.CompAdicional;
import co.com.une.appmovilesune.change.UtilidadesTarificador;
import co.com.une.appmovilesune.components.CompProducto;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.ProductoCotizador;
import co.com.une.appmovilesune.model.TarificadorNew;

/**
 * Created by davids on 18/10/16.
 */

public class ControlCotizador extends Activity implements Observer{

    private TituloPrincipal tlpPrincipal;

    private CheckBox chkHogarNuevo;
    private CheckBox chkAnaloga;
    private Spinner spnestrato;
    private Spinner spntipooferta;
    private Spinner spnoferta;

    private CompProducto cprdTelevision;
    private CompAdicional cadcTelevision;
    private CompProducto cprdInternet;
    private CompProducto cprdTelefonia;
    private CompAdicional cadcTelefonia;

    private TarificadorNew tarificador;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tarificador = new TarificadorNew();

        setContentView(R.layout.viewcotizador);

        tlpPrincipal = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tlpPrincipal.setTitulo(getResources().getString(R.string.cotizador));

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            //cotizacion = (Cotizacion) reicieveParams.getSerializable("cotizacion");
            cliente = (Cliente) reicieveParams.getSerializable("cliente");
            //scooring = (Scooring) reicieveParams.getSerializable("scooring");
            /*if (cotizacion != null) {
                Validar = true;

                System.out.println("CambiarCotizacion " + cotizacion.isCambiarCotizacion());
                if (cotizacion.isCambiarCotizacion()) {
                    precargarBa = true;
                    precargarTo = true;
                    precargarTv = true;
                }

                System.out.println("CambiarCotizacion precargarBa" + precargarBa);
                System.out.println("CambiarCotizacion precargarTo" + precargarTo);
                System.out.println("CambiarCotizacion precargarTv" + precargarTv);

            }*/
        }

        chkHogarNuevo = (CheckBox) findViewById(R.id.chkHogarNuevo);
        chkAnaloga = (CheckBox) findViewById(R.id.chkAnaloga);
        spnestrato = (Spinner) findViewById(R.id.spnestrato);
        spntipooferta = (Spinner) findViewById(R.id.spntipooferta);
        spnoferta = (Spinner) findViewById(R.id.spnoferta);

        cprdTelevision = (CompProducto) findViewById(R.id.cprdTelevision);
        cadcTelevision = (CompAdicional) findViewById(R.id.cadcTelevision);
        cprdInternet = (CompProducto) findViewById(R.id.cprdInternet);
        cprdTelefonia = (CompProducto) findViewById(R.id.cprdTelefonia);
        cadcTelefonia = (CompAdicional) findViewById(R.id.cadcTelefonia);

        cprdTelevision.addObserver(cadcTelevision);
        cprdTelefonia.addObserver(cadcTelefonia);

        cadcTelevision.setCliente(cliente);
        cadcTelefonia.setCliente(cliente);

        spnestrato.setOnItemSelectedListener(seleccionarEstrato);
        spntipooferta.setOnItemSelectedListener(seleccionarTipoOferta);
        spnoferta.setOnItemSelectedListener(seleccionarOferta);

    }

    private void llenarOfertas(String estrato, String tipoPaquete) {

        if (estrato.equals("-")) {
            estrato = "";
        }

        if (tipoPaquete.equals("-")) {
            tipoPaquete = "";
        } else if (tipoPaquete.equals("Individual")) {
            tipoPaquete = "Ind";
        }

        String clausula = "estrato like ? and tipo_paquete like ?";
        String[] valores = new String[]{"%" + estrato + "%", "%" + tipoPaquete + "%"};

        ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios", new String[]{"Oferta"}, clausula,
                valores, null, null, null);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        System.out.println(respuesta);

        if (respuesta != null) {
            for (ArrayList<String> arrayList : respuesta) {
                if (arrayList.get(0).equals("")) {
                    adaptador.add("-");
                } else {
                    adaptador.add(arrayList.get(0));
                }
            }
        }else{
            adaptador.add("-");
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
            llenarOfertas((String) parent.getSelectedItem(), (String) spntipooferta.getSelectedItem());
            parametrizarComponentes();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener seleccionarTipoOferta = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            llenarOfertas((String) spnestrato.getSelectedItem(), (String) parent.getSelectedItem());
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
            productosHabilitar((String)parent.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void productosHabilitar(String oferta){

        if(oferta.equals("-")){
            cprdTelevision.habilitarCheckProducto();
            cprdTelevision.setActivo(false);
            cprdInternet.habilitarCheckProducto();
            cprdInternet.setActivo(false);
            cprdTelefonia.habilitarCheckProducto();
            cprdTelefonia.setActivo(false);
        }else{
            String clausula = "Oferta = ?";
            String[] valores = new String[] { oferta };

            ArrayList<ArrayList<String>> respuesta = MainActivity.basedatos.consultar(true, "Precios", new String[] { "tipo_producto" }, clausula,
                    valores, null, null, null);

            if(respuesta != null){

                cprdTelevision.deshabilitarCheckProducto();
                cprdTelevision.setActivo(false);
                cprdInternet.deshabilitarCheckProducto();
                cprdInternet.setActivo(false);
                cprdTelefonia.deshabilitarCheckProducto();
                cprdTelefonia.setActivo(false);

                for (ArrayList<String> arrayList : respuesta) {
                    if(arrayList.get(0).equalsIgnoreCase("tv")){
                        cprdTelevision.setActivo(true);
                        cprdTelevision.deshabilitarCheckProducto();
                    }

                    if(arrayList.get(0).equalsIgnoreCase("ba")){
                        cprdInternet.setActivo(true);
                        cprdInternet.deshabilitarCheckProducto();
                    }

                    if(arrayList.get(0).equalsIgnoreCase("to")){
                        cprdTelefonia.setActivo(true);
                        cprdTelefonia.deshabilitarCheckProducto();
                    }
                }
            }
        }

    }



    @Override
    public void update(Object value) {

    }

    public void cotizar(View v) {


        System.out.println("hola");

        /*private CompProducto cprdTelevision;
        private CompProducto cprdInternet;
        private CompProducto cprdTelefonia;*/

        System.out.println("cprdTelevision " + cprdTelevision.getPlan());
        System.out.println("cprdInternet " + cprdInternet.getPlan());
        System.out.println("cprdTelefonia " + cprdTelefonia.getPlan());

        limpiarComp(cprdTelefonia);
        limpiarComp(cprdTelevision);
        limpiarComp(cprdInternet);

        String[][] adicionales = new String[0][0];

        tarificador.Consulta_Tarifaz(cprdTelefonia.getPeticionProducto(), cprdTelefonia.getPlan(),
                cprdTelevision.getPeticionProducto(), cprdTelevision.getPlan(),
                cprdInternet.getPeticionProducto(), cprdInternet.getPlan(), adicionales,
                0, (String) spnestrato.getSelectedItem(), false, cliente,
                UtilidadesTarificador.jsonDatos(cliente, "N/A", cliente.getTecnologia(), "0"), this,
                0);

        ArrayList<ProductoCotizador> productos = tarificador.cotizacionVenta();

        if (productos != null) {
            System.out.println("cantidad " + productos.size());

            for (int i = 0; i < productos.size(); i++) {

                System.out.println("tipoProducto " + productos.get(i).getTipo());

                switch (productos.get(i).getTipo()) {
                    case 0:
                        System.out.println("planProducto " + productos.get(i).getPlan());
                        System.out.println("promo " + productos.get(i).getDescuentoCargobasico());
                        System.out.println("tiempo promo " + productos.get(i).getDuracionDescuento());
                        llenarComp(cprdTelefonia, productos.get(i));
                        break;
                    case 1:
                        System.out.println("planProducto " + productos.get(i).getPlan());
                        llenarComp(cprdTelevision, productos.get(i));
                        break;
                    case 2:
                        System.out.println("planProducto " + productos.get(i).getPlan());
                        llenarComp(cprdInternet, productos.get(i));
                        break;
                }
            }
        }
    }

    public void llenarComp(CompProducto comp, ProductoCotizador productos) {
        comp.setTxtvalorcargobasicoind("$" + productos.getCargoBasicoInd());
        comp.setTxtvalorcargobasicoemp("$" + productos.getCargoBasicoEmp());
        comp.setTxtvalordescuentocargobasico(productos.getDescuentoCargobasico()+"%");
        comp.setTxtduraciondescuentocargobasico(productos.getDuracionDescuento()+" Meses");
    }

    public void limpiarComp(CompProducto comp) {
        comp.setTxtvalorcargobasicoind("$0.0");
        comp.setTxtvalorcargobasicoemp("$0.0");
        comp.setTxtvalordescuentocargobasico("0%");
        comp.setTxtduraciondescuentocargobasico("0 Meses");
    }

}
