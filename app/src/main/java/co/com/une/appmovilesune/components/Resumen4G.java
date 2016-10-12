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
import android.widget.TextView;

public class Resumen4G extends LinearLayout {

	Spinner sltDuracion;
	TextView lblPlan, lblDescuento, lblValor, lblValorDescuento;

	Context context;

	public Resumen4G(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.resumen4g, this);

		sltDuracion = (Spinner) findViewById(R.id.sltDuracion);

		lblPlan = (TextView) findViewById(R.id.lblPlan);
		lblDescuento = (TextView) findViewById(R.id.lblDescuento);
		lblValor = (TextView) findViewById(R.id.lblValor);
		lblValorDescuento = (TextView) findViewById(R.id.lblValorDescuento);

	}

	private void llenarDuracion(String descuento) {
		ArrayAdapter<String> adaptador = null;
		String duracion = "";
		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "configuracion",
				new String[] { "datos" }, "tipo = ? and campo = ? and Evento like ?",
				new String[] { "Variables", "TiempoPromo4G", "%" + descuento + "%" }, null, null, null);

		String[] StDuracion = null;

		ArrayList<String> listDuracion = new ArrayList<String>();
		if (resultado != null) {
			duracion = resultado.get(0).get(0);
			duracion = "-- Seleccione Duración --," + duracion;
			StDuracion = duracion.split(",");
			for (int i = 0; i < StDuracion.length; i++) {
				listDuracion.add(StDuracion[i]);
			}

		} else {
			listDuracion.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listDuracion);
		sltDuracion.setAdapter(adaptador);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String getPlan() {
		return lblPlan.getText().toString();
	}

	public void asignarPlan(String plan) {
		lblPlan.setText(plan);
	}

	public String getDescuento() {
		return lblDescuento.getText().toString();

	}

	public void asignarDescuento(String descuento) {

		lblDescuento.setText(descuento);
		// llenarDuracion(descuento);

	}

	public String getValor() {
		return lblValor.getText().toString();
	}

	public void asignarValor(String valor) {
		lblValor.setText(valor);
	}

	public String getValorDescuento() {
		return lblValorDescuento.getText().toString();
	}

	public void asignarValorDescuento(String valor) {
		lblValorDescuento.setText(valor);
	}

	public String getDuracion() {
		return (String) sltDuracion.getSelectedItem();
	}

	public void setDuracion(String duracion) {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltDuracion.getAdapter();
		sltDuracion.setSelection(adaptador.getPosition(duracion), true);
	}

	public void Internet4G(Context context, String internet4g, String precio, String descuento, String precioDescuento,
			String estrato) {
		this.context = context;
		asignarPlan(internet4g);
		asignarValor(precio);

		if (descuento.equalsIgnoreCase("-")) {
			precioDescuento = "N/A";
		}

		asignarValorDescuento(precioDescuento);
		asignarDescuento(descuento);
	}

}
