package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.adapters.ListaDefaultAdapter;
import co.com.une.appmovilesune.change.Busqueda;
import co.com.une.appmovilesune.change.Interprete;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Scooring;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.model.Venta;
import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ControlValidador extends Activity implements Observer {

    private TituloPrincipal tp;
    private Busqueda buscar;
    private Cliente cliente;
    private Scooring scooring;
    private ListView lvcifin;
    ListaDefaultAdapter adapter;

    Spinner tipoIdentificador;

    ArrayList<ListaDefault> validador = new ArrayList<ListaDefault>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewvalidador);

        // viewEstadoPedido =(ListView)findViewById(R.id.viewEstadoPedido);
        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("Validador Cifin");

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            cliente = (Cliente) reicieveParams.getSerializable("cliente");
            scooring = (Scooring) reicieveParams.getSerializable("scooring");
            System.out.println(cliente);
        }

        buscar = (Busqueda) findViewById(R.id.busquedaValidador);

        buscar.setEditable(false);

        if (cliente != null) {
            buscar.setBusqueda(cliente.getCedula());
        }

        buscar.boton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // Resultado_Cartera();
                Validador();
            }
        });

        lvcifin = (ListView) findViewById(R.id.lvcifin);

        tipoIdentificador = (Spinner) findViewById(R.id.tipoIdentificador);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                Utilidades.St_Identificador);
        adaptador.setDropDownViewResource(R.layout.list_radio);

        tipoIdentificador.setAdapter(adaptador);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("Cliente", cliente);
        intent.putExtra("Scooring", scooring);
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
    }

    public void Validador() {
        ArrayList<String> parametros = new ArrayList<String>();

        parametros.add(MainActivity.config.getCodigo());
        parametros.add(Utilidades.Identificador((String) tipoIdentificador.getSelectedItem()));
        parametros.add(buscar.getBusqueda());

        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("Validador");
        params.add(parametros);

        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);
    }

    @Override
    public void update(Object value) {
        ArrayList<Object> resultado = (ArrayList<Object>) value;
        if (resultado.get(0).equals("Validador")) {
            // cliente.setPortafolio(resultado.get(1).toString());
            /*
			 * System.out.println("Resultado Validador" +
			 * resultado.get(1).toString());
			 */
            resultadoValidador(resultado.get(1).toString());
            // Resultado_Portafolio(cliente.getPortafolio());
        }

    }

    public void resultadoValidador(String Resultado) {
        // System.out.println("Resultado VALIDADOR " + Resultado);
        // validador = Interprete.Cifin(Resultado);

        Borrar();

        if (!MainActivity.servidor.equals(Conexion.SERVIDOR_PRODUCCION)) {
            Resultado = Utilidades.pruebaValidadorCifin();
        }

        JSONObject resulta;
        try {
            resulta = new JSONObject(Resultado);

            if (resulta.has("codigoRespuesta")) {
                if (resulta.getString("codigoRespuesta").equalsIgnoreCase("-1")) {
                    if (resulta.has("mensajeRespuesta")
                            && resulta.getString("mensajeRespuesta").contains("Se ha superado el")) {
                        Toast.makeText(MainActivity.context,
                                "Se ha superado el número máximo de consultas autorizadas con este documento intente en 24 horas.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.context, "Error Con La Consulta Informe Al Analista.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (resulta.getString("codigoRespuesta").equalsIgnoreCase("0")) {
                    if (MainActivity.servidor.equals(Conexion.SERVIDOR_PRODUCCION)) {
                        validador = Interprete.Cifin(Resultado);
                    } else {
                        validador = Interprete.Cifin(Utilidades.pruebaValidadorCifin());
                    }

                    if (validador.get(0).getTitulo().equalsIgnoreCase("NO")) {
                        Toast.makeText(this, validador.get(0).getDato(), Toast.LENGTH_SHORT).show();
                        Borrar();
                    } else if (validador.get(0).getTitulo().equalsIgnoreCase("SI")) {
                        validador.remove(0);
                        onPintar();

                        if (scooring == null) {
                            scooring = new Scooring();
                        }

                        if (validador.size() > 6) {
                            scooring.setValidadorCifin(validador.get(4).getDato(), validador.get(2).getDato(),
                                    validador.get(3).getDato(), validador.get(6).getDato());
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.context, "Error El resultado no se puede interpretar",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.context, "Error El resultado no se puede interpretar", Toast.LENGTH_SHORT)
                        .show();
            }

        } catch (JSONException e) {
            Log.w("Error", e.getMessage());
        }

    }

    public void Borrar() {
        validador.clear();
        onPintar();
    }

    public void onPintar() {
        adapter = new ListaDefaultAdapter(this, validador, this);
        lvcifin.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvcifin);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
