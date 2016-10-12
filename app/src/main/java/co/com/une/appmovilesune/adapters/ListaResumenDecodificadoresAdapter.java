package co.com.une.appmovilesune.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.com.une.appmovilesune.R;

public class ListaResumenDecodificadoresAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ItemDecodificador> items;
	public ItemDecodificador item;
	Context ctx;

	public ListaResumenDecodificadoresAdapter(Activity activity, ArrayList<ItemDecodificador> items, Context ctx) {
		super();
		this.activity = activity;
		this.items = items;
		this.ctx = ctx;
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return items.get(position).getId();
	}

	public int getCount() {
		return items.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;

		item = items.get(position);

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = inflater.inflate(R.layout.itemdecodificadorresumen, null);

		TextView txtTransaccion = (TextView) vi.findViewById(R.id.txttransaccion);
		TextView txtFidelizado = (TextView) vi.findViewById(R.id.txtfidelizado);
		TextView txtDecodificadores = (TextView) vi.findViewById(R.id.txtdecodificadores);
		TextView txtPrecio = (TextView) vi.findViewById(R.id.txtprecio);

		txtTransaccion.setText(items.get(position).getTipoTransaccion());

		if (items.get(position).isFideliza()) {
			txtFidelizado.setText(ctx.getResources().getString(R.string.fidelizado));
		} else {
			txtFidelizado.setText("");
		}

		if (items.get(position).getTipoTransaccion().equals("Nuevo")) {
			txtDecodificadores.setText(items.get(position).getOriginal());
		} else if (items.get(position).getTipoTransaccion().equals("Cambio")) {
			txtDecodificadores.setText(items.get(position).getOriginal() + " => " + items.get(position).getDestino());
		} else if (items.get(position).getTipoTransaccion().equals("Retiro")) {
			txtDecodificadores.setText(items.get(position).getOriginal());
		} else if (items.get(position).getTipoTransaccion().equals("Permanece")) {
			txtDecodificadores.setText(items.get(position).getOriginal());
		} else if (items.get(position).getTipoTransaccion().equals("Fideliza")) {
			txtDecodificadores.setText(items.get(position).getOriginal());
		}

		txtPrecio.setText(items.get(position).getPrecio());

		return vi;
	}

}
