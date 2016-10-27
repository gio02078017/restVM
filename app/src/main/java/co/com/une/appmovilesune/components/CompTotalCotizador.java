package co.com.une.appmovilesune.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.interfaces.ObserverTotales;

public class CompTotalCotizador extends LinearLayout implements ObserverTotales {

	public static final String TOTAL_PAQUETE = "Total Paquete";
	public static final String TOTAL_PAQUETE_ADICIONALES = "Total Paquete + Adicionales";

	private TextView txtTipoTotal;
	private TextView txtTotal;

	public CompTotalCotizador(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompTotalCotizador(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	@SuppressLint("NewApi")
	public CompTotalCotizador(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.comptotalcotizador, this, true);

		txtTipoTotal = (TextView) findViewById(R.id.txtTipoTotal);
		txtTotal = (TextView) findViewById(R.id.txtTotal);
	}

	public void setTipoTotal(String tipo) {
		txtTipoTotal.setText(tipo);
	}

	public void setTotal(String total) {
		txtTotal.setText(total);
	}

	public String getTotal() {
		return txtTotal.getText().toString();
	}

	@Override
	public void actualizarTotales(double total) {

	}
}
