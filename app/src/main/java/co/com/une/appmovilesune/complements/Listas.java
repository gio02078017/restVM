package co.com.une.appmovilesune.complements;

import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.adapters.ItemPromocionesAdicionales;
import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.adapters.ListaDefaultAdapter;
import co.com.une.appmovilesune.adapters.ListaPromociones;
import co.com.une.appmovilesune.adapters.ListaPromocionesAdapter;
import co.com.une.appmovilesune.model.Venta;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

public class Listas extends Activity {

	ArrayList<ItemPromocionesAdicionales> itemPromocionesAdicionales = new ArrayList<ItemPromocionesAdicionales>();
	private ListaPromocionesAdapter adapterPromociones;
	ListView lpa;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewlista);

		Bundle reicieveParams = getIntent().getExtras();
		itemPromocionesAdicionales = (ArrayList<ItemPromocionesAdicionales>) reicieveParams
				.getSerializable("promocionesAdicionales");

		lpa = (ListView) findViewById(R.id.listasDefault);

		onMostrar();

	}

	public void onMostrar() {
		final ArrayList<ListaPromociones> listaPromociones = new ArrayList<ListaPromociones>();

		if (itemPromocionesAdicionales.size() > 0) {
			listaPromociones.add(new ListaPromociones(0, "Adicionales", "", "", "", ""));
		}

		for (int i = 0; i < itemPromocionesAdicionales.size(); i++) {
			String titulo = itemPromocionesAdicionales.get(i).getAdicional();
			String promocion = itemPromocionesAdicionales.get(i).getDescuento();
			String meses = itemPromocionesAdicionales.get(i).getMeses();

			listaPromociones.add(new ListaPromociones(1, titulo, "Promocion: ", promocion + "%", "Tiempo: ", meses));

		}

		adapterPromociones = new ListaPromocionesAdapter(this, listaPromociones, this);
		lpa.setAdapter(adapterPromociones);
		// setListViewHeightBasedOnChildren(lpa);
	}

	// public static void setListViewHeightBasedOnChildren(ListView listView) {
	// ListAdapter listAdapter = listView.getAdapter();
	// if (listAdapter == null) {
	// // pre-condition
	// return;
	// }
	//
	// int totalHeight = 0;
	// int spacefinalHeight = 0;
	// int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
	// MeasureSpec.UNSPECIFIED);
	// for (int i = 0; i < listAdapter.getCount(); i++) {
	// View listItem = listAdapter.getView(i, null, listView);
	// listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	// //
	// System.out.println("listItem.getMeasuredHeight() =>
	// "+listItem.getMeasuredHeight());
	// totalHeight += listItem.getMeasuredHeight();
	// spacefinalHeight = listItem.getMeasuredHeight();
	// // System.out.println("totalHeight => "+totalHeight);
	// }
	//
	// ViewGroup.LayoutParams params = listView.getLayoutParams();
	// params.height = totalHeight
	// + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
	// + spacefinalHeight;
	// listView.setLayoutParams(params);
	// listView.requestLayout();
	// }

	public void aceptarLista(View v) {
		Intent intent = new Intent();
		intent.putExtra("result", "Acepto");
		setResult(MainActivity.OK_RESULT_CODE, intent);
		finish();
	}

	public void cancelarLista(View v) {
		Intent intent = new Intent();
		intent.putExtra("result", "Cancelo");
		setResult(MainActivity.OK_RESULT_CODE, intent);
		finish();
	}

}