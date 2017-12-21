package co.com.une.appmovilesune.components;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class FormularioConfiguracion extends LinearLayout {

    public Spinner sltDepartamento, sltCiudad;
    public ArrayAdapter<String> adaptador;
    public String departamento, ciudad;
    private Context context;

    public FormularioConfiguracion(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    public FormularioConfiguracion(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.dialogoconfiguracion, this);
        sltDepartamento = (Spinner) findViewById(R.id.sltDepartamento);
        sltCiudad = (Spinner) findViewById(R.id.sltCiudad);
    }

    public ArrayAdapter<String> obtenerDepartamentos() {
        ArrayList<ArrayList<String>> res = MainActivity.basedatos.consultar(false, "usuario",
                new String[]{"Departamento"}, null, null, "Departamento", null, null);
        adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        if (res != null) {
            for (ArrayList<String> arrayList : res) {
                System.out.println("[ciudades del " + departamento + "] " + arrayList.get(0));
                adaptador.add(arrayList.get(0));
            }
        }
        return adaptador;
    }

    public ArrayAdapter<String> obtenerCiudades(String departamento) {

        // String[] Departamento = new String[] { departamento };
        // System.out.println("Departamento utf8 => "+Departamento[0]);
        ArrayList<ArrayList<String>> res = MainActivity.basedatos.consultar(false, "Departamentos",
                new String[]{"Ciudad"}, "Departamento=? and Estado=? and visible=?", new String[]{departamento, "1", "1"}, null,
                null, null);
        // System.out.println("res" + res);
        adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        adaptador.add("--Seleccione Ciudad--");
        if (res != null) {
            for (ArrayList<String> arrayList : res) {
                if (!Utilidades.excluir("ciudadesOcultas", arrayList.get(0))) {
                    adaptador.add(arrayList.get(0));
                }
            }
        }

        return adaptador;
    }

    public OnItemSelectedListener onDepartamentoSeleccionado = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
            departamento = parent.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // lblMensaje.setText("");
        }
    };

}
