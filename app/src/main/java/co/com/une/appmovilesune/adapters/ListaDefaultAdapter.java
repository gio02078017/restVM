package co.com.une.appmovilesune.adapters;

import java.util.ArrayList;

import co.com.une.appmovilesune.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Clase interna que sirve para controlar la programacion visual del desarrollo,
 * esta clase sirve como de adatador para convertir los datos del objeto
 * item_niveles arraylist a una listView
 * 
 * @author Giovanny
 * 
 */
public class ListaDefaultAdapter extends BaseAdapter {

	/**
	 * se declaran las variables globales para el funcionamiento de la clase
	 * interna
	 */
	protected Activity activity;
	protected ArrayList<ListaDefault> items;
	Context ctx;
	int Nivel = -1;

	/**
	 * este el contructor principal de la clase
	 * 
	 * @param activity
	 * @param items
	 * @param ctx
	 */
	public ListaDefaultAdapter(Activity activity, ArrayList<ListaDefault> items, Context ctx) {
		this.activity = activity;
		this.items = items;
		this.ctx = ctx;
	}

	/**
	 * Metodo que permite realizar la adatacion del objeto arraylist a lo
	 * objetos xml del layout niveles_list_item_layout
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi = convertView;

		ListaDefault item = items.get(position);

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Nivel = items.get(position).getNivel();

		switch (Nivel) {

		case 0:
			vi = inflater.inflate(R.layout.titulos, null);
			break;
		case 1:
			vi = inflater.inflate(R.layout.datos, null);
			vi.setBackgroundResource(R.drawable.bg_lists);
			break;
		case 2:
			vi = inflater.inflate(R.layout.datos, null);
			vi.setBackgroundResource(R.color.white);
			break;
		case 3:
			vi = inflater.inflate(R.layout.datos, null);
			vi.setBackgroundResource(R.drawable.bg_lists);
			break;
		case 4:
			vi = inflater.inflate(R.layout.datos, null);
			vi.setBackgroundResource(R.drawable.bg_lists);
			break;
		case 6:
			vi = inflater.inflate(R.layout.titulos, null);
			// vi.setBackgroundResource(R.color.datos3);
			break;
		case 7:
			vi = inflater.inflate(R.layout.titulo_evento, null);
			// vi.setBackgroundResource(R.color.datos3);
			break;

		}

		if (Nivel == 0) {

			TextView tvnivel = (TextView) vi.findViewById(R.id.titulo_default);
			tvnivel.setText(item.getTitulo());

		} else if (Nivel == 6) {

			TextView tvnivel = (TextView) vi.findViewById(R.id.titulo_default);
			tvnivel.setText(item.getTitulo());

		} else if (Nivel == 7) {
			TextView tvnivel = (TextView) vi.findViewById(R.id.titulo_default);
			tvnivel.setText(item.getTitulo());

			TextView nivel0 = (TextView) vi.findViewById(R.id.evento);
			nivel0.setText(item.getDato());
		} else if (Nivel == 3 || Nivel == 4) {
			TextView tvnivel = (TextView) vi.findViewById(R.id.titulo_datos);
			// tvnivel.setTextColor(activity.getResources().getColor(R.color.white));
			tvnivel.setText(item.getTitulo() + ":");

			TextView nivel0 = (TextView) vi.findViewById(R.id.datos_default);
			// nivel0.setTextColor(activity.getResources().getColor(R.color.white));
			nivel0.setText(item.getDato());
		} else {
			TextView tvnivel = (TextView) vi.findViewById(R.id.titulo_datos);
			tvnivel.setText(item.getTitulo() + ":");

			TextView nivel0 = (TextView) vi.findViewById(R.id.datos_default);
			nivel0.setText(item.getDato());
		}

		return vi;
	}

	// Getters and Setters

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return items.get(position).getId();
	}

}