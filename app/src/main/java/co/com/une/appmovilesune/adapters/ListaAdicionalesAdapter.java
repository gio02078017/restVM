package co.com.une.appmovilesune.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;

public class ListaAdicionalesAdapter extends BaseAdapter implements Subject {

	protected Activity activity;
	protected ArrayList<ListaAdicionales> items;
	public ListaAgendamiento item;
	Context ctx;

	private String type;
	private Observer observer;

	public ListaAdicionalesAdapter(Activity activity, ArrayList<ListaAdicionales> items, Context ctx, String type) {
		this.activity = activity;
		this.items = items;
		this.ctx = ctx;
		this.type = type;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		View vi = convertView;

		final ListaAdicionales item = items.get(position);

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = inflater.inflate(R.layout.itemamigocuentasaddesc, null);

		ImageView txtEliminarAdicional = (ImageView) vi.findViewById(R.id.imgEliminar);
		TextView txtNombreAdicional = (TextView) vi.findViewById(R.id.txtNombreAdicional);
		TextView txtValorAdicional = (TextView) vi.findViewById(R.id.txtValorAdicional);
		TextView txtValorAdicionalDescuento = (TextView) vi.findViewById(R.id.txtValorAdicionalDescuento);
		TextView txtPromociones = (TextView) vi.findViewById(R.id.txtPromociones);
		txtEliminarAdicional.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// Integer position = (Integer)view.getTag();
				ArrayList<Object> data = new ArrayList<Object>();
				data.add("eliminar");
				data.add(position);
				observer.update(data);
			}
		});

		txtNombreAdicional.setText(item.getAdicional());
		txtValorAdicional.setText(item.getPrecio());
		if (type.equals("portafolio")) {
			txtPromociones.setVisibility(View.GONE);
			txtEliminarAdicional.setVisibility(View.GONE);
		}

		if (!item.getDescuento().equals("0") && !item.getDuracion().equals("0")) {
			txtPromociones.setVisibility(View.VISIBLE);
			if (item.getDuracion().equals("1")) {
				txtPromociones.setText(item.getDuracion() + " Mes al " + item.getDescuento() + "%");
				if (item.isBalin()) {
					double precio = Double.parseDouble(item.getPrecio());
					double descuento = Double.parseDouble(item.getDescuento());
					double totalDescuento = precio - (precio * (descuento / 100));
					txtValorAdicionalDescuento.setText(String.valueOf(totalDescuento));
				}
			} else {
				txtPromociones.setText(item.getDuracion() + " Meses al " + item.getDescuento() + "%");
				if (item.isBalin()) {
					double precio = Double.parseDouble(item.getPrecio());
					double descuento = Double.parseDouble(item.getDescuento());
					double totalDescuento = precio - (precio * (descuento / 100));
					item.setValorDescuento(String.valueOf(totalDescuento));
					txtValorAdicionalDescuento.setText("(" + item.getValorDescuento() + ")");
				}
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

	OnClickListener eliminar = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub
		observer = o;
	}

	@Override
	public void removeObserver(Observer o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyObserver() {
		// TODO Auto-generated method stub

	}

}