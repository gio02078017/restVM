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
 */
public class ListaPromocionesAdapter extends BaseAdapter {

    /**
     * se declaran las variables globales para el funcionamiento de la clase
     * interna
     */
    protected Activity activity;
    protected ArrayList<ListaPromociones> items;
    Context ctx;
    int Nivel = -1;

    /**
     * este el contructor principal de la clase
     *
     * @param activity
     * @param items
     * @param ctx
     */
    public ListaPromocionesAdapter(Activity activity, ArrayList<ListaPromociones> items, Context ctx) {
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

        ListaPromociones item = items.get(position);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Nivel = items.get(position).getNivel();

        switch (Nivel) {

            case 0:
                vi = inflater.inflate(R.layout.titulos, null);
                break;
            case 1:
                vi = inflater.inflate(R.layout.datospromociones, null);
                vi.setBackgroundResource(R.color.white);
                break;
        }

        if (Nivel == 0) {

            TextView tvnivel = (TextView) vi.findViewById(R.id.titulo_default);
            tvnivel.setText(item.getTitulo());

        } else if (Nivel == 1) {
            TextView titulo1 = (TextView) vi.findViewById(R.id.adicionales);
            if (!item.getMeses().equalsIgnoreCase("0 Meses")) {
                titulo1.setText(item.getTitulo() + ", " + item.getMeses() + " al " + item.getPromocion() + "");
            } else {
                titulo1.setText(item.getTitulo());
            }
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