package co.com.une.appmovilesune.components;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class FormularioResumido extends LinearLayout {

	private Context context;
	private EditText txtDocumento, txtTelefono, txtDireccion;
	private Spinner sltCiudad;

	public FormularioResumido(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public FormularioResumido(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.dialogoasesoria, this);
		sltCiudad = (Spinner) findViewById(R.id.sltCiudad);
		txtDocumento = (EditText) findViewById(R.id.txtDocumento);
		txtTelefono = (EditText) findViewById(R.id.txtTelefono);
		txtDireccion = (EditText) findViewById(R.id.txtDireccion);
	}

	public ArrayAdapter<String> obtenerCiudades(String departamento) {
		System.out.println(departamento);
		// String[] Departamento = new String[] { departamento };
		ArrayList<ArrayList<String>> res = MainActivity.basedatos.consultar(false, "Departamentos",
				new String[] { "Ciudad" }, "Departamento=? and Estado=?", new String[] { departamento, "1" }, null,
				null, null);
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
		if (res != null) {
			for (ArrayList<String> arrayList : res) {
				System.out.println("[ciudades del "+departamento+"] "+arrayList.get(0));
				if (!Utilidades.excluir("ciudadesOcultas", arrayList.get(0))) {
					adaptador.add(arrayList.get(0));
				}
			}
		}
		return adaptador;
	}
}
