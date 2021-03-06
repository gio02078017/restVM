package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ResumenInternet extends LinearLayout {

	private Spinner sltDuracion, sltTipoMigracion;
	private TextView lblPlan, lblDescuento, lblValor, lblValorDescuento, lblDuracion, lblDescuentoA;
	private CheckBox chkMigracion, chkWifi;

	private TableRow trlTipoMigracion;

	private Context context;

	private boolean migracion, wifi;

	private String planFacturacion;

	private String tecnologiacr;

	TextView lblGota, lblValorGota, tituloValorDescuento;

	public ResumenInternet(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.resumeninternet, this);

		sltDuracion = (Spinner) findViewById(R.id.sltDuracion);
		sltTipoMigracion = (Spinner) findViewById(R.id.sltTipoMigracion);

		lblPlan = (TextView) findViewById(R.id.lblPlan);
		lblDescuento = (TextView) findViewById(R.id.lblDescuento);
		lblValor = (TextView) findViewById(R.id.lblValor);
		lblValorDescuento = (TextView) findViewById(R.id.lblValorDescuento);
		lblDuracion = (TextView) findViewById(R.id.lblDuracion);
		lblDescuentoA = (TextView) findViewById(R.id.lblDescuentoA);

		lblGota = (TextView) findViewById(R.id.lblGota);
		lblValorGota = (TextView) findViewById(R.id.lblValorGota);
		tituloValorDescuento = (TextView) findViewById(R.id.tituloValorDescuento);

		trlTipoMigracion = (TableRow) findViewById(R.id.trlTipoMigracion);

		chkMigracion = (CheckBox) findViewById(R.id.chkMigracion);
		chkWifi = (CheckBox) findViewById(R.id.chkWifi);

		chkMigracion.setOnCheckedChangeListener(cambioMigracion);
		chkWifi.setOnCheckedChangeListener(cambioWifi);

	}

	private void llenarTipoMigracion() {

		ArrayAdapter<String> adaptador = null;
		String tipoMigracion = "";

		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
				new String[] { "lst_item" }, "lst_nombre = ?", new String[] { "Tipo Migracion BA" }, null, null, null);

		ArrayList<String> listTipoMigracion = new ArrayList<String>();
		if (resultado != null && chkMigracion.isChecked()) {
			listTipoMigracion.add("-- Seleccione Cambio de Plan --");

			for (int i = 0; i < resultado.size(); i++) {
				listTipoMigracion.add(resultado.get(i).get(0));
			}

		} else {
			listTipoMigracion.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listTipoMigracion);
		sltTipoMigracion.setAdapter(adaptador);
	}

	public void setContext(Context context) {
		this.context = context;
		llenarTipoMigracion();
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

	public void limpiarDuracion() {
		asignarDuracion("0 Meses");
		asignarDescuento("-");
		asignarValorDescuento("0");
	}

	public void asignarDuracion(String Duracion) {
		lblDuracion.setText(Duracion);
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
		return lblDuracion.getText().toString();
	}

	public void setDuracion(String duracion) {
		lblDuracion.setText(duracion);
	}

	public String getTipoMigracion() {
		return (String) sltTipoMigracion.getSelectedItem();
	}

	public String getPlanFacturacion() {
		return planFacturacion;
	}

	public void setPlanFacturacion(String planFacturacion) {
		this.planFacturacion = planFacturacion;
	}

	public void setTipoMigracion(String tipoMigracion) {
		ArrayAdapter<String> adaptador = (ArrayAdapter<String>) sltTipoMigracion.getAdapter();
		if (adaptador.getPosition(tipoMigracion) == -1) {
			adaptador.add(tipoMigracion);
			sltTipoMigracion.setAdapter(adaptador);
		}
		sltTipoMigracion.setSelection(adaptador.getPosition(tipoMigracion), true);
	}

	public String getTecnologiacr() {
		return tecnologiacr;
	}

	public void setTecnologiacr(String tecnologiacr) {
		this.tecnologiacr = tecnologiacr;
	}

	public String getWifi() {
		String Wifi = "";
		if (chkWifi.isChecked()) {
			Wifi = "SI";
		} else {
			Wifi = "NO";
		}

		return Wifi;
	}

	public void setWifi(String wifi) {
		if (wifi.equalsIgnoreCase("SI")) {
			chkWifi.setChecked(true);
		} else {
			chkWifi.setChecked(false);
		}
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

	public String getLblGota() {
		return lblGota.getText().toString();
	}

	public void setLblGota(String nombreGota) {
		lblGota.setText(nombreGota);
		;
	}

	public String getLblValorGota() {
		return lblValorGota.getText().toString();
	}

	public void setLblValorGota(String valorGota) {
		lblValorGota.setText(valorGota);
	}

	public void Internet(Context context, String tipo, String internet, String precio, String descuento,
			String Duracion, String precioDescuento, String planFacturacion, String estrato, String planAnt,
			String tecnologiacr, boolean aplicarDescuentos) {
		this.context = context;
		this.tecnologiacr = tecnologiacr;

		System.out.println("rtiInternet->tecnologiacr " + this.tecnologiacr);
		asignarPlan(internet);
		asignarValor(precio);
		setPlanFacturacion(planFacturacion);

		if (aplicarDescuentos && descuento.contains("%")) {

			ArrayList<Object> listDescuentos = Utilidades.precioDescuento(descuento, precio);

			if ((Boolean) listDescuentos.get(0)) {

				if (!listDescuentos.get(2).equals(0.0) && !listDescuentos.get(2).equals(0)) {
					tituloValorDescuento.setVisibility(View.GONE);
					precioDescuento = "(" + listDescuentos.get(2) + ")";
				} else {
					tituloValorDescuento.setVisibility(View.VISIBLE);
				}
			}

		}

		if (descuento != null) {
			if (descuento.equalsIgnoreCase("-")) {
				precioDescuento = "N/A";
			}

			if (descuento.equalsIgnoreCase("Sin Promocion")) {
				limpiarDuracion();
			} else if (internet.contains("Existente")) {
				limpiarDuracion();
			} else {
				asignarDuracion(Duracion);
				asignarValorDescuento(precioDescuento);
				asignarDescuento(descuento);
			}
		}

		chkWifi.setChecked(true);
		chkWifi.setEnabled(false);
		chkWifi.setVisibility(View.INVISIBLE);

		// if (tipo != null) {
		// if (tipo.equalsIgnoreCase("C")) {
		// chkMigracion.setChecked(true);
		// } else {
		// chkMigracion.setEnabled(false);
		// }
		// }

		System.out.println("Internet => " + internet);
		if (internet.equals("Segunda Banda Ancha")) {
			chkMigracion.setEnabled(false);
			chkWifi.setChecked(true);
			chkWifi.setEnabled(false);
		}

		llenarTipoMigracion();
		if (!planAnt.equals("")) {
			setTipoMigracion(planAnt);
			sltTipoMigracion.setEnabled(false);
		}

	}

	OnCheckedChangeListener cambioMigracion = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				trlTipoMigracion.setVisibility(VISIBLE);
			} else {
				trlTipoMigracion.setVisibility(GONE);
			}
			migracion = isChecked;
			llenarTipoMigracion();
		}

	};

	OnCheckedChangeListener cambioWifi = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			wifi = isChecked;
			// System.out.println("linea Adicional => " + wifi);
		}
	};

}
