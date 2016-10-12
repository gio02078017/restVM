package co.com.une.appmovilesune.adapters;

import java.util.ArrayList;

import co.com.une.appmovilesune.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListaCompetenciaAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ItemCompetencia> items;
	public ItemCompetencia item;
	Context ctx;

	public ListaCompetenciaAdapter(Activity activity, ArrayList<ItemCompetencia> items, Context ctx) {
		this.activity = activity;
		this.items = items;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;

		final ItemCompetencia item = items.get(position);

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = inflater.inflate(R.layout.itemcompetencia, null);

		TextView lblProducto = (TextView) vi.findViewById(R.id.lblProducto);
		TextView lblVigencia = (TextView) vi.findViewById(R.id.lblVigencia);
		TextView lblCompetencia = (TextView) vi.findViewById(R.id.lblCompetencia);
		TextView lblValor = (TextView) vi.findViewById(R.id.lblValor);

		lblProducto.setText(item.getProducto());
		lblVigencia.setText(item.getVigencia());
		lblCompetencia.setText(item.getCompetencia());
		lblValor.setText(item.getValor());

		return vi;

	}

}
