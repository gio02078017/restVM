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

public class ListaVentasAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ListaVentas> items;
    public ListaVentas item;
    Context ctx;

    private String type;
    private Observer observer;

    public ListaVentasAdapter(Activity activity, ArrayList<ListaVentas> items, Context ctx) {
        this.activity = activity;
        this.items = items;
        this.ctx = ctx;
        this.type = type;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;

        final ListaVentas item = items.get(position);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vi = inflater.inflate(R.layout.itemlistaventas, null);

        TextView lblFecha = (TextView) vi.findViewById(R.id.lblFecha);
        TextView lblHora = (TextView) vi.findViewById(R.id.lblHora);
        TextView lblCliente = (TextView) vi.findViewById(R.id.lblCliente);
        TextView lblVenta = (TextView) vi.findViewById(R.id.lblVenta);

        lblFecha.setText(item.getFecha());
        lblHora.setText(item.getHora());
        lblCliente.setText(item.getCliente());
        lblVenta.setText(item.getVentaID());

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

}