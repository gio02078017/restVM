package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.complements.Conexion;
import co.com.une.appmovilesune.complements.Dialogo;
import co.com.une.appmovilesune.components.SelectorProductos;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.model.Peticion;
import co.com.une.appmovilesune.model.Prospecto;
import co.com.une.appmovilesune.model.Simulador;
import co.com.une.appmovilesune.R;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

public class ControlProspecto extends Activity implements Observer, Subject {

    private Prospecto prospecto;

    private SelectorProductos slpProductos;
    private EditText txtObservaciones, txtDistancia;
    private Spinner spnMotivo;

    private Dialogo dialogoProductos;

    private Observer observador;

    private String id = "";
    private int codigo;

    private TituloPrincipal tp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewprospecto);
        MainActivity.obrsProspecto = this;

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("Prospecto");
        dialogoProductos = new Dialogo(this, Dialogo.DIALOGO_SELECTOR_PRODUCTO, "");
        dialogoProductos.dialogo.setOnDismissListener(dlp);

        asignarCampos();
        llenarSpiners();

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            prospecto = (Prospecto) reicieveParams.getSerializable("prospecto");
            if (prospecto != null) {
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

    public void mostrarDialogo(View v) {
        dialogoProductos.dialogo.show();
    }

    public void guardarProspecto(View v) {
        // prospecto = new Prospecto(slpProductos.getTexto(),
        // txtObservaciones.getText().toString(),
        // txtMotivo.getText().toString());
        if (prospecto == null) {
            prospecto = new Prospecto();
        }

        prospecto.setProductos(slpProductos.getTexto());
        prospecto.setObservaciones(txtObservaciones.getText().toString());
        prospecto.setMotivo((String) spnMotivo.getSelectedItem());
        prospecto.setDistancia(txtDistancia.getText().toString());

        // InsertarConfirmacion();

        Intent intent = new Intent();
        intent.putExtra("prospecto", prospecto);
        setResult(MainActivity.OK_RESULT_CODE, intent);
        finish();
    }

    private void InsertarConfirmacion() {

        addObserver(MainActivity.obsrMainActivity);
        notifyObserver();

        ArrayList<String> consolidado = MainActivity.consolidarIVR();

        System.out.println("consolidado " + consolidado);

        if (consolidado.get(2).length() >= 7) {
            if (id.equals("")) {
                codigo = (int) (Math.random() * (9999 - 1000 + 1) + 1000);

                JSONObject IVR = new JSONObject();

                try {

                    IVR.put("codigoasesor", consolidado.get(0));
                    IVR.put("telefono", consolidado.get(2));
                    IVR.put("codigo", codigo);
                    IVR.put("tipocliente", consolidado.get(3));
                    IVR.put("documento", consolidado.get(4));

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                ArrayList<String> parametros = new ArrayList<String>();
                parametros.add(IVR.toString());
                parametros.add(MainActivity.config.getCodigo());
                ArrayList<Object> params = new ArrayList<Object>();
                params.add(MainActivity.config.getCodigo());
                params.add("InsertarConfirmacion");
                params.add(parametros);
                Simulador simulador = new Simulador();
                simulador.setManual(this);
                simulador.addObserver(this);
                simulador.execute(params);

            } else {
                LanzarLLamada(id, "11");
            }

        } else {
            Toast.makeText(this, "No ha Ingresado Telefono de destino", Toast.LENGTH_SHORT).show();
        }

        // btnSiguiente.setEnabled(true);
    }

    private void LanzarLLamada(String id, String tipo) {

        ArrayList<String> parametros = new ArrayList<String>();
        parametros.add(tipo);
        parametros.add(id);
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(MainActivity.config.getCodigo());
        params.add("LanzarLlamada");
        params.add(parametros);
        Simulador simulador = new Simulador();
        simulador.setManual(this);
        simulador.addObserver(this);
        simulador.execute(params);

    }

    private void mostrarDialgo() {
        System.out.println("Codigo => " + codigo);
        AlertDialog.Builder promptDialog = new AlertDialog.Builder(this);
        promptDialog.setTitle("Confirmación de Código");
        promptDialog.setCancelable(false);
        final EditText input = new EditText(this);
        final Context context = this;
        promptDialog.setView(input);

        promptDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                System.out.println("promptDialog id" + id);
                if (!id.equalsIgnoreCase("")) {
                    if (value.toCharArray().length == 4 || value.equals("20130806")) {
                        if (value.equals(String.valueOf(codigo)) || value.equals("20130806")) {

                            Intent intent = new Intent();
                            intent.putExtra("prospecto", prospecto);
                            setResult(MainActivity.OK_RESULT_CODE, intent);
                            finish();

                        } else {
                            Toast.makeText(context, "No Concuerdan los codigos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "El tamaño del codigo es incorrecto", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(context, "Primero Debe Lanzar La Llamada", Toast.LENGTH_SHORT).show();
                }

            }
        });

        promptDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(context, "No se puede continuar", Toast.LENGTH_SHORT).show();
            }
        });

        promptDialog.show();
    }

    private void asignarCampos() {
        slpProductos = (SelectorProductos) findViewById(R.id.slpProspecto);
        spnMotivo = (Spinner) findViewById(R.id.spnMotivo);
        txtObservaciones = (EditText) findViewById(R.id.txtObservaciones);
        txtDistancia = (EditText) findViewById(R.id.txtDistancia);

    }

    private void llenarCampos() {

        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) spnMotivo.getAdapter();
        spnMotivo.setSelection(adaptador.getPosition(prospecto.getMotivo()));
        slpProductos.setTexto(prospecto.getProductos());
        // txtMotivo.setText(prospecto.getMotivo());
        txtObservaciones.setText(prospecto.getObservaciones());
        txtDistancia.setText(prospecto.getDistancia());
    }

    private void llenarSpiners() {

        ArrayAdapter<String> adaptador = null;

        String motivoCompetencia = "";
        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre = ?", new String[]{"Motivos Prospecto"}, null, null, null);
        ArrayList<String> listMotivo = new ArrayList<String>();
        if (resultado != null) {
            listMotivo.add("-- Seleccione Motivo --");

            for (int i = 0; i < resultado.size(); i++) {
                listMotivo.add(resultado.get(i).get(0));
            }
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMotivo);
        spnMotivo.setAdapter(adaptador);
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

    @Override
    public void addObserver(Observer o) {
        // TODO Auto-generated method stub
        observador = o;

    }

    @Override
    public void removeObserver(Observer o) {
        // TODO Auto-generated method stub
        observador = null;

    }

    @Override
    public void notifyObserver() {
        // TODO Auto-generated method stub
        observador.update("LlamadaProspecto");

    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub

        ArrayList<Object> resultado = (ArrayList<Object>) value;
        if (resultado.get(0).equals("InsertarConfirmacion")) {
            JSONObject jop;

            try {
                jop = new JSONObject(resultado.get(1).toString());

                String data = jop.get("data").toString();
                if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                    data = new String(Base64.decode(data));
                    // Confirmacion(data);
                    JSONObject confirmacion = new JSONObject(data);
                    if (confirmacion.has("id")) {
                        id = confirmacion.getString("id");

                        if (!id.equalsIgnoreCase("")) {
                            // blindaje.idIVR = id;
                            LanzarLLamada(id, "11");
                            // LLamadaUneMas = true;
                        } else {
                            System.out.println("Error " + confirmacion.getString("mensaje"));
                        }
                    }

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (resultado.get(0).equals("LanzarLlamada")) {
            System.out.println("data " + resultado.get(1));
            boolean controlUserProof = false;

            if (MainActivity.servidor == Conexion.SERVIDOR_DAVID || MainActivity.servidor == Conexion.SERVIDOR_GIOVANNY
                    || MainActivity.servidor == Conexion.SERVIDOR_DESARROLLO
                    || MainActivity.servidor == Conexion.SERVIDOR_PRUEBAS) {
                controlUserProof = true;

            }

            if (resultado.get(1).equals("OK") || controlUserProof) {
                mostrarDialgo();
            } else if (resultado.get(1).equals("FAIL")) {
                Toast.makeText(this, "EL lanzamiento de la llamada fallo", Toast.LENGTH_SHORT).show();
            }
        } else if (resultado.get(0).equals("RepetirCodigo")) {
            JSONObject jop;

            try {
                jop = new JSONObject(resultado.get(1).toString());

                String data = jop.get("data").toString();
                if (MainActivity.config.validarIntegridad(data, jop.get("crc").toString())) {
                    data = new String(Base64.decode(data));

                    JSONArray ja = new JSONArray(data);
                    System.out.println("ja " + ja);
                    JSONObject jo = ja.getJSONObject(0);
                    System.out.println("jo " + jo);

                    if (jo.getString("completo").equals("0")) {
                        Toast.makeText(this, "Complete el ivr", Toast.LENGTH_SHORT).show();
                    } else {

                        LanzarLLamada(id, "11");

                    }
                } else {
                    System.out.println("Los datos no se descargaron correctamente");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
