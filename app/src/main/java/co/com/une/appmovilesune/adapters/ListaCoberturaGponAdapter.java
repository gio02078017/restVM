package co.com.une.appmovilesune.adapters;

import java.util.ArrayList;

import co.com.une.appmovilesune.R;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class ListaCoberturaGponAdapter extends ArrayAdapter<ItemCoberturaGpon> {

	Activity activity;
	private ArrayList<ItemCoberturaGpon> items;
	private ArrayList<ItemCoberturaGpon> itemsAll;
	private ArrayList<ItemCoberturaGpon> suggestions;
	private int viewResourceId;

	public ListaCoberturaGponAdapter(Context ctx, int txtViewResourceId, ArrayList<ItemCoberturaGpon> direccionInicial,
			Activity activity) {
		super(ctx, txtViewResourceId, direccionInicial);
		this.activity = activity;
		this.items = direccionInicial;
		this.itemsAll = (ArrayList<ItemCoberturaGpon>) items.clone();
		this.suggestions = new ArrayList<ItemCoberturaGpon>();
		this.viewResourceId = viewResourceId;
	}

	@Override
	public View getView(int pos, View cnvtView, ViewGroup prnt) {
		return getCustomView(pos, cnvtView, prnt);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		vi = inflater.inflate(R.layout.itemgpon, null);

		TextView txtDireccionInicial = (TextView) vi.findViewById(R.id.txtDireccionInicial);
		TextView txtDireccionFinal = (TextView) vi.findViewById(R.id.txtDireccionFinal);

		ItemCoberturaGpon itemgpon = items.get(position);
		System.out.println("Ini: " + itemgpon.getDireccionInicial());
		System.out.println("Fin: " + itemgpon.getDireccionFinal());
		txtDireccionInicial.setText("Ini: " + itemgpon.getDireccionInicial());
		txtDireccionFinal.setText("Fin: " + itemgpon.getDireccionFinal());

		return vi;
	}

	@Override
	public Filter getFilter() {
		return nameFilter;
	}

	Filter nameFilter = new Filter() {
		public String convertResultToString(Object resultValue) {
			String str = ((ItemCoberturaGpon) (resultValue)).getDireccionInicial();
			return str;
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if (constraint != null) {
				suggestions.clear();
				for (ItemCoberturaGpon itemgpon : itemsAll) {
					if (itemgpon.getDireccionInicial().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
						suggestions.add(itemgpon);
					}
				}
				FilterResults filterResults = new FilterResults();
				filterResults.values = suggestions;
				filterResults.count = suggestions.size();
				return filterResults;
			} else {
				return new FilterResults();
			}
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			ArrayList<ItemCoberturaGpon> filteredList = (ArrayList<ItemCoberturaGpon>) results.values;
			if (results != null && results.count > 0) {
				clear();
				for (ItemCoberturaGpon c : filteredList) {
					add(c);
				}
				notifyDataSetChanged();
			}
		}
	};

}