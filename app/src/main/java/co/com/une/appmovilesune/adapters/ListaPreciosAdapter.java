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
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Clase interna que sirve para controlar la programacion visual del desarrollo,
 * esta clase sirve como de adatador para convertir los datos del objeto
 * item_niveles arraylist a una listView
 *
 * @author Giovanny
 */
public class ListaPreciosAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ListaPrecios> items;
    public ListaAgendamiento item;

    public ListaPreciosAdapter(Activity activity, ArrayList<ListaPrecios> items, Context ctx) {
        this.activity = activity;
        this.items = items;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        final ListaPrecios item = items.get(position);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vi = inflater.inflate(R.layout.itemadicionales, null);

        TextView lblNombre = (TextView) vi.findViewById(R.id.txtNombre);
        lblNombre.setText(item.getNombre());

        TextView lblValor = (TextView) vi.findViewById(R.id.txtValor);
        lblValor.setText(item.getValor());

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