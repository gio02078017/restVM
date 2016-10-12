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

public class ListaCarruselAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ItemCarrusel> items;
	public ItemCarrusel item;
	Context ctx;

	public ListaCarruselAdapter(Activity activity, ArrayList<ItemCarrusel> items, Context ctx) {
		super();
		this.activity = activity;
		this.items = items;
		this.ctx = ctx;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;

		item = items.get(position);

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = inflater.inflate(R.layout.itemcarrusel, null);

		// TextView paquete = (TextView) vi.findViewById(R.id.paquete);
		TextView fecharetiro = (TextView) vi.findViewById(R.id.fecharetiro);
		TextView producto = (TextView) vi.findViewById(R.id.producto);
		TextView motivo = (TextView) vi.findViewById(R.id.motivo);

		// paquete.setText(item.getPaquete());
		fecharetiro.setText(item.getFecha());
		producto.setText(item.getProducto());
		motivo.setText(item.getMotivo());

		return vi;
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

}
