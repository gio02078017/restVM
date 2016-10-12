package co.com.une.appmovilesune.adapters;

import java.util.ArrayList;

import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.Utilidades;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ListaAgendamientoAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ListaAgendamiento> items;
	public ListaAgendamiento item;
	Context ctx;

	public ListaAgendamientoAdapter(Activity activity, ArrayList<ListaAgendamiento> items, Context ctx) {
		this.activity = activity;
		this.items = items;
		this.ctx = ctx;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		int disManana = 0, disTarde = 0;
		View vi = convertView;

		final ListaAgendamiento item = items.get(position);

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = inflater.inflate(R.layout.itemagendamiento, null);

		disManana = Utilidades.parserDisponibilidad(item.getDisManana());
		disTarde = Utilidades.parserDisponibilidad(item.getDisTarde());

		TextView lblFecha = (TextView) vi.findViewById(R.id.lblFecha);
		TextView lblPorcentajeManana = (TextView) vi.findViewById(R.id.lblPorcentajeManana);
		TextView lblDispinibilidadManana = (TextView) vi.findViewById(R.id.lblDispinibilidadManana);

		TextView lblPorcentajeTarde = (TextView) vi.findViewById(R.id.lblPorcentajeTarde);
		ProgressBar pgbTarde = (ProgressBar) vi.findViewById(R.id.pgbTarde);
		ProgressBar pgbManana = (ProgressBar) vi.findViewById(R.id.pgbManana);
		TextView lblDispinibilidadTarde = (TextView) vi.findViewById(R.id.lblDispinibilidadTarde);

		if (disManana <= 0 && disTarde <= 0) {
			vi.setVisibility(View.GONE);
			lblFecha.setVisibility(View.GONE);
			lblPorcentajeManana.setVisibility(View.INVISIBLE);
			pgbManana.setVisibility(View.INVISIBLE);
			lblDispinibilidadManana.setVisibility(View.INVISIBLE);
			lblPorcentajeTarde.setVisibility(View.INVISIBLE);
			pgbTarde.setVisibility(View.INVISIBLE);
			lblDispinibilidadTarde.setVisibility(View.INVISIBLE);
		} else {

			lblFecha.setText(item.getFecha());

			if (disManana <= 0) {

				lblPorcentajeManana.setText("100");

				pgbManana.setMax(100);
				pgbManana.setProgress(100);

				lblDispinibilidadManana.setText("0");
			} else {
				lblPorcentajeManana.setText(item.getPorcManana());

				pgbManana.setMax(100);
				pgbManana.setProgress(Integer.parseInt(item.getPorcManana()));

				lblDispinibilidadManana.setText(item.getDisManana());
			}
			if (disTarde <= 0) {

				lblPorcentajeTarde.setText("100");

				pgbTarde.setMax(100);
				pgbTarde.setProgress(100);

				lblDispinibilidadTarde.setText("0");
			} else {
				lblPorcentajeTarde.setText(item.getPorcTarde());

				pgbTarde.setMax(100);
				pgbTarde.setProgress(Integer.parseInt(item.getPorcTarde()));

				lblDispinibilidadTarde.setText(item.getDisTarde());
			}

		}

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