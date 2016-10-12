package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ItemCompetenciaFormulario extends LinearLayout {

	Context context;
	Spinner spnCompetencia, spnProducto;
	EditText txtOtraCompetencia, txtVigenciaContrato, txtPagoMensual;

	public ItemCompetenciaFormulario(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public ItemCompetenciaFormulario(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.itemcompetenciaformulario, this);

		spnCompetencia = (Spinner) findViewById(R.id.spnCompetencia);
		spnProducto = (Spinner) findViewById(R.id.spnCompetenciaProducto);
		txtOtraCompetencia = (EditText) findViewById(R.id.txtOtraCompetencia);
		txtVigenciaContrato = (EditText) findViewById(R.id.txtVigenciaContrato);
		txtPagoMensual = (EditText) findViewById(R.id.txtPagoMensual);

	}

	public ArrayAdapter<String> obtenerCompetencias() {
		ArrayList<ArrayList<String>> res = MainActivity.basedatos.consultar(false, "listasgenerales",
				new String[] { "lst_item" }, "lst_nombre=?", new String[] { "Competencia" }, null, null, null);
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
		if (res != null) {
			for (ArrayList<String> arrayList : res) {
				adaptador.add(arrayList.get(0));
			}
		}
		return adaptador;
	}

	public ArrayAdapter<String> obtenerProductosCompetencia() {
		ArrayList<ArrayList<String>> res = MainActivity.basedatos.consultar(false, "listasgenerales",
				new String[] { "lst_item" }, "lst_nombre=?", new String[] { "productosCompetencias" }, null, null,
				null);
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
		if (res != null) {
			for (ArrayList<String> arrayList : res) {
				adaptador.add(arrayList.get(0));
			}
		}
		return adaptador;
	}

}
