package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SelectorCompetencia extends LinearLayout {

    public Spinner sltCompetenciaProducto, sltCompetenciaNombre;
    public ArrayAdapter<String> adaptador;
    private Context context;

    public SelectorCompetencia(Context context) {
        super(context);
        this.context = context;
        // TODO Auto-generated constructor stub

    }

    public SelectorCompetencia(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // TODO Auto-generated constructor stub

    }

    protected void onFinishInflate() {

        super.onFinishInflate();

        ((Activity) getContext()).getLayoutInflater().inflate(R.layout.selectorcompetencia, this);

        sltCompetenciaProducto = (Spinner) findViewById(R.id.sltCompetenciaProducto);
        sltCompetenciaNombre = (Spinner) findViewById(R.id.sltCompetenciaNombre);

    }

    public void llenarSelectores() {

        ArrayList<ArrayList<String>> res = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre=?", new String[]{"competencia"}, null, null, null);

        adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);

        adaptador.add("--Seleccione Producto--");
        if (res != null) {
            for (ArrayList<String> arrayList : res) {
                adaptador.add(arrayList.get(0));
            }
        }

    }
}
