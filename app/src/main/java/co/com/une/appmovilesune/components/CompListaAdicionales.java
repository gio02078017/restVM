package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ListaAdicionales;
import co.com.une.appmovilesune.adapters.ListaAdicionalesAdapter;
import co.com.une.appmovilesune.model.AdicionalAMG;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CompListaAdicionales extends RelativeLayout {

	ImageView imgProducto;
	TextView lblTipoProducto, txtTotalAdicionalesContenido;
	ListView lstAdicionales;
	ArrayList<ArrayList<AdicionalAMG>> adicionales;

	public CompListaAdicionales(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompListaAdicionales(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public CompListaAdicionales(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {

		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);

		li.inflate(R.layout.complistaadicionales, this, true);

		imgProducto = (ImageView) findViewById(R.id.imgProducto);
		lblTipoProducto = (TextView) findViewById(R.id.lblTipoProducto);
		txtTotalAdicionalesContenido = (TextView) findViewById(R.id.txtTotalAdicionalesContenido);
		lstAdicionales = (ListView) findViewById(R.id.lstAdicionales);

	}

	public void setTipo(String tipo) {

		if (tipo.equalsIgnoreCase("TO")) {
			imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tel));
			lblTipoProducto.setText("Adicionales TO");
		} else if (tipo.equalsIgnoreCase("TV")) {
			imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_tv));
			lblTipoProducto.setText("Adicionales TV");
		} else if (tipo.equalsIgnoreCase("BA")) {
			imgProducto.setImageDrawable(getResources().getDrawable(R.drawable.logo_ba));
			lblTipoProducto.setText("Adicionales BA");
		}

	}

	public void setAdicionales(ArrayList<ArrayList<AdicionalAMG>> adicionales) {
		this.adicionales = adicionales;
		mostrarAdicionales();
	}

	private void mostrarAdicionales() {

		ArrayList<ListaAdicionales> adic = new ArrayList<ListaAdicionales>();

		for (int i = 0; i < adicionales.size(); i++) {
			for (int j = 0; j < adicionales.get(i).size(); j++) {
				adic.add(new ListaAdicionales(adicionales.get(i).get(j).nombre,
						String.valueOf(adicionales.get(i).get(j).total), "0", "0"));
			}
		}

		ListaAdicionalesAdapter adapter = new ListaAdicionalesAdapter((Activity) getContext(), adic, getContext(),
				"portafolio");
		lstAdicionales.setAdapter(adapter);

	}

	public String getTotal() {
		return txtTotalAdicionalesContenido.getText().toString();
	}

}
