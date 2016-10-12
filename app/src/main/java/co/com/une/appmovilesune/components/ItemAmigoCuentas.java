package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.model.ProductoAMG;

public class ItemAmigoCuentas extends RelativeLayout {

	public ImageView imgProducto;
	public TextView lblPlan, lblPlanFacturacion, lblIdentificador, lblVelocidad, lblCliente, lblCargoBasico,
			lblCargoFinanciero, lblCargoAdicionales, lblTipoProducto, lblVelocidadValor, lblCargoBasicoContenido,
			lblCargoAdicionalesContenido, lblCargoFinancieroContenido, lblTotal, lblTotalContenido, txtDescuento;
	public Spinner spnProducto;
	public String tipo;
	public ProductoAMG producto;
	public ArrayList<ProductoAMG> productos;
	private Resources res;

	public ItemAmigoCuentas(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ItemAmigoCuentas(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ItemAmigoCuentas(Context context, ProductoAMG producto, String tipo) {
		super(context);
		res = getResources();
		this.producto = producto;
		this.tipo = tipo;
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		((Activity) getContext()).getLayoutInflater().inflate(R.layout.itemamigocuentascb, this);

		imgProducto = (ImageView) findViewById(R.id.imgProducto);
		spnProducto = (Spinner) findViewById(R.id.spnProducto);
		lblTipoProducto = (TextView) findViewById(R.id.lblTipoProducto);
		lblPlan = (TextView) findViewById(R.id.lblPlan);
		lblPlanFacturacion = (TextView) findViewById(R.id.lblPlanFacturacion);
		lblIdentificador = (TextView) findViewById(R.id.lblIdentificador);
		lblVelocidad = (TextView) findViewById(R.id.lblVelocidad);
		lblVelocidadValor = (TextView) findViewById(R.id.lblVelocidadValor);
		lblCliente = (TextView) findViewById(R.id.lblClienteId);
		lblCargoBasico = (TextView) findViewById(R.id.lblCargoBasico);
		lblCargoBasicoContenido = (TextView) findViewById(R.id.lblCargoBasicoContenido);
		lblCargoAdicionales = (TextView) findViewById(R.id.lblCargoAdicionales);
		lblCargoAdicionalesContenido = (TextView) findViewById(R.id.lblCargoAdicionalesContenido);
		lblCargoFinanciero = (TextView) findViewById(R.id.lblCargoFinanciero);
		lblCargoFinancieroContenido = (TextView) findViewById(R.id.lblCargoFinancieroContenido);
		lblTotal = (TextView) findViewById(R.id.lblTotal);
		lblTotalContenido = (TextView) findViewById(R.id.lblTotalContenido);
		txtDescuento = (TextView) findViewById(R.id.txtDescuento);

	}

}
