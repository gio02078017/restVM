package co.com.une.appmovilesune.adapters;

import java.util.ArrayList;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ListaCheckedAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<ListaChecked> items;
	public ListaChecked item;
	CheckBox chkEstado;
	Context ctx;
	int Nivel = -1;

	public ListaCheckedAdapter(Activity activity, ArrayList<ListaChecked> items, Context ctx) {
		this.activity = activity;
		this.items = items;
		this.ctx = ctx;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi = convertView;

		final ListaChecked item = items.get(position);

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Nivel = items.get(position).getNivel();

		// String seleccionados = items.get(position).getSelecionados();

		switch (Nivel) {

		case 1:
			vi = inflater.inflate(R.layout.itemlistachecked, null);
			vi.setBackgroundResource(R.color.nivel1);
			break;
		case 2:
			vi = inflater.inflate(R.layout.itemlistachecked, null);
			vi.setBackgroundResource(R.drawable.bg_lists);
			break;

		}

		TextView txtNombre = (TextView) vi.findViewById(R.id.txtNombre);
		txtNombre.setTextColor(activity.getResources().getColor(R.color.black));
		txtNombre.setText(item.getDato());

		TextView txtValor = (TextView) vi.findViewById(R.id.txtValor);
		txtValor.setTextColor(activity.getResources().getColor(R.color.purple));
		txtValor.setText(item.getTitulo());

		chkEstado = (CheckBox) vi.findViewById(R.id.chkEstado);

		String[][] seleccionados = item.getSelecionados();
		String dato = item.getDato().trim();
		if (seleccionados != null) {
			for (int i = 0; i < seleccionados.length; i++) {
				String adicional = seleccionados[i][0];
				if (dato.equalsIgnoreCase(adicional)) {
					item.setChecked(true);
				}
			}
		}

		if (item.isChecked()) {
			chkEstado.setChecked(true);
		}

		chkEstado.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				item.setChecked(isChecked);
			}
		});

		return vi;
	}

	public int calcularValor() {
		int valor = 0;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isChecked()) {
				valor += Integer.parseInt(items.get(i).getTitulo());
			}
		}
		// Double val = valor * 1.16;
		// Double val = valor;
		// valor = val.intValue();
		return valor;
	}

	public String[][] Adicionales() {

		int counter = 0;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isChecked()) {
				counter++;
			}
		}

		String[][] Adicionales = new String[counter][2];
		int j = 0;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isChecked()) {
				Adicionales[j][0] = items.get(i).getDato();
				// Double val = Double.parseDouble(items.get(i).getTitulo()) *
				// 1.16;
				Double val = Double.parseDouble(items.get(i).getTitulo());
				Adicionales[j][1] = "" + val.intValue();
				j++;
			}
		}

		return Adicionales;
	}

	public String otrasPromociones() {
		String otrasPromociones = "";
		int control = 0;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isChecked()) {
				if (control == 0) {
					otrasPromociones = items.get(i).getDato() + " , " + items.get(i).getTitulo();
					control++;
				} else {
					otrasPromociones += " | " + items.get(i).getDato() + " , " + items.get(i).getTitulo();
					control++;
				}
			}
		}
		return otrasPromociones;
	}

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