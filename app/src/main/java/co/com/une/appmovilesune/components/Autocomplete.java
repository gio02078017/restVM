package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class Autocomplete extends LinearLayout implements TextWatcher {

	Context context;
	public AutoCompleteTextView txtBarrio;
	public ArrayAdapter<String> adaptador;

	public Autocomplete(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub

	}

	public Autocomplete(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.autocomplete, this);

		txtBarrio = (AutoCompleteTextView) findViewById(R.id.txtBarrio);

	}

	public ArrayAdapter<String> obtenerAdaptador() {
		ArrayList<ArrayList<String>> barr = MainActivity.basedatos.consultar(false, "barrios",
				new String[] { "nombre" }, null, null, null, null, null);

		ArrayList<String> dat = new ArrayList<String>();
		for (int i = 0; i < barr.size(); i++) {
			dat.add(barr.get(i).get(0));
		}

		adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, dat);

		return adaptador;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

}
