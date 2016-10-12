package co.com.une.appmovilesune.controller;

import java.text.DecimalFormat;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.components.ItemAmigocuentasAdicionalDesc;
import co.com.une.appmovilesune.model.ProductoAMG;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ControlDetalleProducto extends Activity {

	ProductoAMG producto;

	public TextView txtDetallePlan;
	public LinearLayout llyDetalleFactura, llyAdicionalesDetalle, llyConsumosDetalle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalleproducto);

		txtDetallePlan = (TextView) findViewById(R.id.txtDetallePlan);
		llyDetalleFactura = (LinearLayout) findViewById(R.id.llyDetalleFactura);
		llyAdicionalesDetalle = (LinearLayout) findViewById(R.id.llyAdicionalesDetalle);
		llyConsumosDetalle = (LinearLayout) findViewById(R.id.llyConsumosDetalle);

		Bundle reicieveParams = getIntent().getExtras();
		if (reicieveParams != null) {
			producto = (ProductoAMG) reicieveParams.getSerializable("producto");
			txtDetallePlan.setText(producto.plan);
			generarDetalleFactura();
			generarAdicionales();
			generarConsumos();
		} else {
			System.out.println("no  producto");
		}

	}

	public void generarDetalleFactura() {
		agregarItemsDetalle(getResources().getString(R.string.cargobasico),
				"$ " + String.valueOf(new DecimalFormat("#,###.##").format(producto.cargoBasico)), llyDetalleFactura);

		for (int i = 0; i < producto.iva.size(); i++) {
			if (producto.iva.get(i)[0].equalsIgnoreCase("IVA CARGO BASICO")) {
				agregarItemsDetalle(getResources().getString(R.string.cargobasico),
						"$ " + String.valueOf(
								new DecimalFormat("#,###.##").format(Double.parseDouble(producto.iva.get(i)[1]))),
						llyDetalleFactura);
			}
		}

		for (int i = 0; i < producto.otrosFinancieros.size(); i++) {
			agregarItemsDetalle(producto.otrosFinancieros.get(i)[0], "$ " + String.valueOf(
					new DecimalFormat("#,###.##").format(Double.parseDouble(producto.otrosFinancieros.get(i)[1]))),
					llyDetalleFactura);
		}

		for (int i = 0; i < producto.independientes.size(); i++) {
			agregarItemsDetalle(producto.independientes.get(i)[0], "$ " + String.valueOf(
					new DecimalFormat("#,###.##").format(Double.parseDouble(producto.independientes.get(i)[1]))),
					llyDetalleFactura);
		}

	}

	public void generarAdicionales() {
		if (producto.adicionales.size() > 0) {
			for (int i = 0; i < producto.adicionales.get(0).size(); i++) {
				agregarItemsDetalle(producto.adicionales.get(0).get(i).nombre,
						"$ " + String.valueOf(
								new DecimalFormat("#,###.##").format(producto.adicionales.get(0).get(i).cargoBasico)),
						llyAdicionalesDetalle);
			}
		}

	}

	public void generarConsumos() {
		for (int i = 0; i < producto.detalles.size(); i++) {
			agregarItemsDetalle(producto.detalles.get(i)[0], producto.detalles.get(i)[1], llyConsumosDetalle);
		}
	}

	public void agregarItemsDetalle(String nombre, String valor, LinearLayout contenedor) {
		ItemAmigocuentasAdicionalDesc idac = new ItemAmigocuentasAdicionalDesc(this);

		LayoutInflater lid = LayoutInflater.from(idac.getContext());
		RelativeLayout componenteDesc = (RelativeLayout) lid.inflate(R.layout.itemamigocuentasaddesc, null);

		componenteDesc.setPadding(5, 0, 5, 0);

		TextView txtNombreAdicional = (TextView) componenteDesc.findViewById(R.id.txtNombreAdicional);
		TextView txtValorAdicional = (TextView) componenteDesc.findViewById(R.id.txtValorAdicional);

		txtNombreAdicional.setTextColor(Color.BLACK);
		txtValorAdicional.setTextColor(Color.BLACK);

		txtNombreAdicional.setText(nombre);
		txtValorAdicional.setText(valor);

		contenedor.addView(componenteDesc);
	}

}
