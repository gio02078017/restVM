package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.change.Utilidades;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Resumen3G extends LinearLayout {

	private Spinner sltDuracion, sltTipoMigracion, sltPagoModem, sltPorcentaje, sltEntrega;
	private TextView lblPlan, lblDescuento, lblValor, lblValorDescuento;
	private CheckBox chkMigracion, chkModem;
	private EditText txtPrecioModem;

	private TableRow trlTipoMigracion, trlPagoModem, trlPorcentaje, trlPrecioModem, trlEntrega;

	private Context context;

	private boolean migracion, modem;

	public Resumen3G(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.resumen3g, this);

		sltDuracion = (Spinner) findViewById(R.id.sltDuracion);
		sltTipoMigracion = (Spinner) findViewById(R.id.sltTipoMigracion);
		sltPagoModem = (Spinner) findViewById(R.id.sltPagoModem);
		sltPorcentaje = (Spinner) findViewById(R.id.sltPorcentaje);
		sltEntrega = (Spinner) findViewById(R.id.sltEntrega);

		lblPlan = (TextView) findViewById(R.id.lblPlan);
		lblDescuento = (TextView) findViewById(R.id.lblDescuento);
		lblValor = (TextView) findViewById(R.id.lblValor);
		lblValorDescuento = (TextView) findViewById(R.id.lblValorDescuento);

		txtPrecioModem = (EditText) findViewById(R.id.txtPrecioModem);

		trlTipoMigracion = (TableRow) findViewById(R.id.trlTipoMigracion);
		trlPagoModem = (TableRow) findViewById(R.id.trlPagoModem);
		trlPorcentaje = (TableRow) findViewById(R.id.trlPorcentaje);
		trlPrecioModem = (TableRow) findViewById(R.id.trlPrecioModem);
		trlEntrega = (TableRow) findViewById(R.id.trlEntrega);

		chkMigracion = (CheckBox) findViewById(R.id.chkMigracion);
		chkModem = (CheckBox) findViewById(R.id.chkModem);

		chkMigracion.setOnCheckedChangeListener(cambioMigracion);
		chkModem.setOnCheckedChangeListener(cambioModem);

	}

	private void llenarTipoMigracion() {

		ArrayAdapter<String> adaptador = null;
		String tipoMigracion = "";

		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "configuracion",
				new String[] { "datos" }, "tipo = ? and campo = ?", new String[] { "Variables", "TipoMigracion" }, null,
				null, null);

		ArrayList<String> listTipoMigracion = new ArrayList<String>();
		if (resultado != null && chkMigracion.isChecked()) {
			tipoMigracion = resultado.get(0).get(0);
			tipoMigracion = "-- Seleccione Cambio de Plan --," + tipoMigracion;
			String[] StTipoMigracion = tipoMigracion.split(",");
			for (int i = 0; i < StTipoMigracion.length; i++) {
				listTipoMigracion.add(StTipoMigracion[i]);
			}

		} else {
			listTipoMigracion.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listTipoMigracion);
		sltTipoMigracion.setAdapter(adaptador);
	}

	private void llenarPagoModem() {

		ArrayAdapter<String> adaptador = null;
		String pagoModem = "";

		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "configuracion",
				new String[] { "datos" }, "tipo = ? and campo = ?", new String[] { "Variables", "FinModem" }, null,
				null, null);

		ArrayList<String> listpagoModem = new ArrayList<String>();

		if (resultado != null && chkModem.isChecked()) {
			pagoModem = resultado.get(0).get(0);
			pagoModem = "-- Seleccione Tipo Pago --," + pagoModem;
			String[] StPagoModem = pagoModem.split(",");
			for (int i = 0; i < StPagoModem.length; i++) {
				listpagoModem.add(StPagoModem[i]);
			}

		} else {
			listpagoModem.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listpagoModem);
		sltPagoModem.setAdapter(adaptador);

		sltPagoModem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
				String pago = parent.getSelectedItem().toString();

				if (!pago.equals("N/A") && !pago.equalsIgnoreCase("-- Seleccione Tipo Pago --")) {
					llenarPorcentajeModem(pago);
					llenarEntrega(pago);

				} else {
					txtPrecioModem.setText("");
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {
				// lblMensaje.setText("");
			}
		});
	}

	private void llenarPorcentajeModem(String pagoModen) {

		ArrayAdapter<String> adaptador = null;
		String porcentajeModem = "";

		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "configuracion",
				new String[] { "datos" }, "tipo = ? and campo = ?", new String[] { "Variables", "PorcenFinModem" },
				null, null, null);

		ArrayList<String> listporcentajeModem = new ArrayList<String>();

		if (resultado != null && pagoModen.indexOf("Financiado") >= 0 && !pagoModen.equalsIgnoreCase("N/A")) {
			porcentajeModem = resultado.get(0).get(0);
			porcentajeModem = "-- Seleccione Porcentaje --," + porcentajeModem;
			String[] StPorcentajeModem = porcentajeModem.split(",");
			for (int i = 0; i < StPorcentajeModem.length; i++) {
				listporcentajeModem.add(StPorcentajeModem[i]);
			}

		} else {
			listporcentajeModem.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listporcentajeModem);
		sltPorcentaje.setAdapter(adaptador);
	}

	private void llenarEntrega(String pagoModen) {

		ArrayAdapter<String> adaptador = null;
		String entregaModem = "";

		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "configuracion",
				new String[] { "datos" }, "tipo = ? and campo = ?", new String[] { "Variables", "Entrega_Modem" }, null,
				null, null);

		ArrayList<String> listEntregaModem = new ArrayList<String>();

		if (resultado != null && !pagoModen.equalsIgnoreCase("N/A")) {
			entregaModem = resultado.get(0).get(0);
			entregaModem = "-- Seleccione Tipo Entrega --," + entregaModem;
			String[] StEntregaModem = entregaModem.split(",");
			for (int i = 0; i < StEntregaModem.length; i++) {
				listEntregaModem.add(StEntregaModem[i]);
			}

		} else {
			listEntregaModem.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listEntregaModem);
		sltEntrega.setAdapter(adaptador);
	}

	private void llenarDuracion(String descuento) {
		ArrayAdapter<String> adaptador = null;
		String duracion = "";
		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
				new String[] { "lst_item" }, "tipo = ? and campo = ? and Evento like ?",
				new String[] { "Variables", "TiempoPromoIm", "%" + descuento + "%" }, null, null, null);

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
		/*
		 * llenarTipoMigracion(); llenarPagoModem();
		 * llenarPorcentajeModem("N/A"); llenarEntrega("N/A");
		 */
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

	public String getTipoMigracion() {
		return (String) sltTipoMigracion.getSelectedItem();
	}

	public void setTipoMigracion(String tipoMigracion) {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoMigracion.getAdapter();
		sltTipoMigracion.setSelection(adaptador.getPosition(tipoMigracion), true);
	}

	public String getPagoModem() {
		return (String) sltPagoModem.getSelectedItem();
	}

	public void setPagoModem(String pagoModem) {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltPagoModem.getAdapter();
		sltPagoModem.setSelection(adaptador.getPosition(pagoModem), true);
	}

	public String getPorcentaje() {
		return (String) sltPorcentaje.getSelectedItem();
	}

	public void setPorcentaje(String porcentaje) {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltPorcentaje.getAdapter();
		sltPorcentaje.setSelection(adaptador.getPosition(porcentaje), true);
	}

	public String getEntrega() {
		return (String) sltEntrega.getSelectedItem();
	}

	public void setEntrega(String entrega) {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltEntrega.getAdapter();
		sltEntrega.setSelection(adaptador.getPosition(entrega), true);
	}

	public String getMigracion() {
		String Migracion = "";
		if (chkMigracion.isChecked()) {
			Migracion = "Cambio";
		} else {
			Migracion = "Nueva";
		}

		return Migracion;
	}

	public void setMigracion(String migracion) {
		if (migracion.equalsIgnoreCase("Cambio")) {
			chkMigracion.setChecked(true);
		} else {
			chkMigracion.setChecked(false);
		}
	}

	public String getModem() {
		String Modem = "";
		if (chkModem.isChecked()) {
			Modem = "SI";
		} else {
			Modem = "NO";
		}

		return Modem;
	}

	public void setModem(String modem) {
		if (modem.equalsIgnoreCase("SI")) {
			chkModem.setChecked(true);
		} else {
			chkModem.setChecked(false);
		}
	}

	public String getPrecioModem() {
		return txtPrecioModem.getText().toString();
	}

	public void setPrecioModem(String precio) {
		txtPrecioModem.setText(precio);
	}

	public void Internet3G(Context context, String internet3g, String precio, String descuento, String precioDescuento,
			String estrato) {
		this.context = context;
		asignarPlan(internet3g);
		asignarValor(precio);
		asignarDescuento(descuento);

		if (descuento.equalsIgnoreCase("-")) {
			precioDescuento = "N/A";
		}

		asignarValorDescuento(precioDescuento);
		/*
		 * llenarTipoMigracion(); llenarPagoModem();
		 * llenarPorcentajeModem("N/A"); llenarEntrega("N/A");
		 */

	}

	OnCheckedChangeListener cambioMigracion = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				trlTipoMigracion.setVisibility(VISIBLE);
				llenarTipoMigracion();
			} else {
				trlTipoMigracion.setVisibility(GONE);
			}
			migracion = isChecked;
			llenarTipoMigracion();
		}

	};

	OnCheckedChangeListener cambioModem = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				trlPagoModem.setVisibility(VISIBLE);
				trlPorcentaje.setVisibility(VISIBLE);
				trlPrecioModem.setVisibility(VISIBLE);
				trlEntrega.setVisibility(VISIBLE);
			} else {
				trlPagoModem.setVisibility(GONE);
				trlPorcentaje.setVisibility(GONE);
				trlPrecioModem.setVisibility(GONE);
				trlEntrega.setVisibility(GONE);
			}
			modem = isChecked;
			llenarPagoModem();

		}
	};

}
