package co.com.une.appmovilesune.adapters;

import java.util.ArrayList;

import com.itextpdf.text.pdf.TextField;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.change.UtilidadesDecos;
import co.com.une.appmovilesune.interfaces.Observer;
import co.com.une.appmovilesune.interfaces.Subject;

public class ListaDecodificadoresAdapter extends BaseAdapter implements Subject {

    public static final String TAG = "ListaDecodificadores";

    protected Activity activity;
    protected ArrayList<ItemDecodificador> items, aux;
    public ItemDecodificador item;
    public String ciudad;
    Context ctx;
    private boolean permisoFideliza;
    private String plan;

    private Observer observer;

    public ListaDecodificadoresAdapter(Activity activity, ArrayList<ItemDecodificador> items, Context ctx,
                                       String ciudad, boolean permisoFideliza) {
        super();
        this.activity = activity;
        this.items = items;
        this.aux = clonarItems(items);

        this.ctx = ctx;
        this.ciudad = ciudad;
        this.permisoFideliza = permisoFideliza;

        System.out.println("Numero de decidificadores " + items.size());
    }

    private ArrayList<ItemDecodificador> clonarItems(ArrayList<ItemDecodificador> decos) {
        ArrayList<ItemDecodificador> items = new ArrayList<ItemDecodificador>();
        for (ItemDecodificador itemDecodificador : decos) {
            items.add(itemDecodificador.clone());
        }

        return items;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;

        item = items.get(position);

        Log.d(TAG, "isFideliza " + items.get(position).isFideliza());

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vi = inflater.inflate(R.layout.itemdecodificador, null);

        final CheckBox chkEliminarDeco = (CheckBox) vi.findViewById(R.id.chkEliminarDeco);
        TextView txtBorrarDecodificador = (TextView) vi.findViewById(R.id.txtBorrarDecodificador);
        TextView lblNombreDeco = (TextView) vi.findViewById(R.id.lblNombreDeco);
        final Spinner spnTipoDeco = (Spinner) vi.findViewById(R.id.spnTipoDeco);
        final CheckBox chkFideliza = (CheckBox) vi.findViewById(R.id.chkFideliza);
        final TextView lblValorDeco = (TextView) vi.findViewById(R.id.lblValorDeco);

        comportamientoTipoBorrado(chkEliminarDeco, txtBorrarDecodificador, lblNombreDeco);
        pintarNombreDecosOriginales(position, lblNombreDeco);
        chequearRetiro(position, chkEliminarDeco, chkFideliza, spnTipoDeco);

        spnTipoDeco.setAdapter(llenarSpinner());

        if (items != null) {
            seleccionarDestinos(position, spnTipoDeco);
        }

        pintarDecodificadorFidelizado(position, spnTipoDeco, chkFideliza);

        txtBorrarDecodificador.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarDecoNuevo(position);
            }

        });
        spnTipoDeco.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                cambiarValorDeco(position, lblValorDeco, arg0.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        chkEliminarDeco.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "chkEliminarDeco.setOnCheckedChangeListener antes => "
                        + items.get(position).getTipoTransaccion());
                if (isChecked) {
                    items.get(position).setTipoTransaccion("Retiro");
                } else {
                    items.get(position).setTipoTransaccion("Permanece");
                }
                Log.d(TAG, "chkEliminarDeco.setOnCheckedChangeListener despues => "
                        + items.get(position).getTipoTransaccion());
                chequearRetiro(position, chkEliminarDeco, chkFideliza, spnTipoDeco);
                cambiarValorDeco(position, lblValorDeco, spnTipoDeco.getSelectedItem().toString());
            }
        });

        chkFideliza.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // if(items.get(position).isNuevo()){
                modificarValorFidelizacion(position, lblValorDeco, isChecked, spnTipoDeco);
                // }
            }
        });

        System.out.println("Adapter Decos isFideliza " + item.isFideliza());
        System.out.println("Adapter Decos getTipoTransaccion " + item.getTipoTransaccion());
        System.out.println("Adapter Decos getOriginal " + item.getOriginal());
        System.out.println("Adapter Decos getDestino " + item.getDestino());
        System.out.println("Adapter Decos isFidelizaIncluido " + item.isFidelizaIncluido());
        if (item.isFidelizaIncluido()) {
            txtBorrarDecodificador.setVisibility(View.GONE);
            chkEliminarDeco.setVisibility(View.GONE);

        }
        permitirFidelizar(chkFideliza);

        return vi;

    }

    private void pintarNombreDecosOriginales(int position, TextView lblNombreDeco) {

        if (items.get(position).getTipoTransaccion().equals("Cambio") || items.get(position).getTipoTransaccion().equals("Retiro") || items.get(position).getTipoTransaccion().equals("Permanece") || items.get(position).getTipoTransaccion().equals("Fideliza")) {
            lblNombreDeco.setText(item.getOriginal());
        } else {
            lblNombreDeco.setText("-");
        }

    }

    private void seleccionarDestinos(int position, Spinner spnTipoDeco) {
        Log.d(TAG, "Destino " + items.get(position).getDestino());
        if (items.get(position).getTipoTransaccion().equals("Nuevo")) {
            Log.d(TAG, "Incluido " + items.get(position).isIncluido());
            Log.d(TAG, "Nuevo " + items.get(position).isNuevo());
            if (items.get(position).isIncluido()) {
                if (!items.get(position).getOriginal().equals("")) {
                    spnTipoDeco.setSelection(llenarSpinner().getPosition(items.get(position).getOriginal()));
                }
            } else {
                if (items.get(position).getDestino() != null) {
                    if (!items.get(position).getDestino().equals("")) {
                        spnTipoDeco.setSelection(llenarSpinner().getPosition(items.get(position).getDestino()));
                    }
                }

            }

        } else if (items.get(position).getTipoTransaccion().equals("Cambio")) {
            items.get(position).setDestino(aux.get(position).getDestino());
            spnTipoDeco.setSelection(llenarSpinner().getPosition(items.get(position).getDestino()));
        }
    }

    private void chequearRetiro(int position, CheckBox chkEliminarDeco, CheckBox chkFideliza, Spinner spnTipoDeco) {
        if (items.get(position).getTipoTransaccion().equals("Retiro")) {
            chkEliminarDeco.setChecked(true);
            chkFideliza.setEnabled(false);
            spnTipoDeco.setEnabled(false);
        } else {
            chkEliminarDeco.setChecked(false);
            if (!items.get(position).isFideliza()) {
                chkFideliza.setEnabled(true);
                spnTipoDeco.setEnabled(true);
            }

        }
    }

    private void modificarValorFidelizacion(int position, TextView lblValorDeco, boolean isChecked,
                                            Spinner spnTipoDeco) {
        items.get(position).setFideliza(isChecked);
        if (isChecked) {
            items.get(position).setTipoAlquiler("FI");
            items.get(position).setTipoFideliza("CP");
        } else {
            items.get(position).setTipoAlquiler("AL");
            items.get(position).setTipoFideliza("NA");
            items.get(position).setPrecio(aux.get(position).getPrecio());
        }

        cambiarValorDeco(position, lblValorDeco, spnTipoDeco.getSelectedItem().toString());

    }

    private void permitirFidelizar(CheckBox chkFideliza) {

        if (!permisoFideliza && !item.isFidelizaIncluido()) {
            if (item.isNuevo()) {
                chkFideliza.setVisibility(View.GONE);
            } else {
                if (!item.isFideliza()) {
                    chkFideliza.setVisibility(View.GONE);
                } else {
                    chkFideliza.setVisibility(View.VISIBLE);
                }
            }
        } else {
            chkFideliza.setVisibility(View.VISIBLE);
        }
    }

    private void pintarDecodificadorFidelizado(int position, Spinner spnTipoDeco, CheckBox chkFideliza) {
        if (items.get(position).isFideliza()) {
            chkFideliza.setChecked(true);
            chkFideliza.setEnabled(false);
            spnTipoDeco.setEnabled(false);

            //spnTipoDeco.setSelection(0);
        } else {
            chkFideliza.setChecked(false);
        }
    }

    private void cambiarValorDeco(int position, TextView lblValorDeco, String seleccionado) {
        items.get(position).setDestino(seleccionado);
        if (!items.get(position).getTipoTransaccion().equals("Retiro")) {
            if (!seleccionado.equals("-")) {
                if (items.get(position).isFideliza()) {
                    items.get(position).setPrecio(valorDecodificadorFidelizado(seleccionado));
                    lblValorDeco.setText(items.get(position).getPrecio());
                } else {
                    if (items.get(position).isNuevo()) {
                        if (items.get(position).isIncluido()) {
                            if (items.get(position).getOriginal().equals(seleccionado)) {
                                items.get(position).setPrecio(aux.get(position).getPrecio());
                                lblValorDeco.setText(items.get(position).getPrecio());
                            } else {
                                items.get(position).setPrecio(valorDecodificador(seleccionado));
                                lblValorDeco.setText(items.get(position).getPrecio());
                            }

                        } else {
                            items.get(position).setPrecio(valorDecodificador(seleccionado));
                            lblValorDeco.setText(items.get(position).getPrecio());
                        }

                    } else {
                        ;
                        if (seleccionado.equals(items.get(position).getDestino())) {
                            if (!seleccionado.equals(aux.get(position).getDestino())) {
                                items.get(position).setPrecio(valorDecodificador(seleccionado));
                                items.get(position).setTipoTransaccion("Cambio");
                                aux.get(position).setDestino(seleccionado);
                                items.get(position).setDestino(seleccionado);
                                lblValorDeco.setText(items.get(position).getPrecio());
                            } else {
                                items.get(position).setPrecio(aux.get(position).getPrecio());
                                lblValorDeco.setText(items.get(position).getPrecio());
                            }

                        } else {
                            items.get(position).setPrecio(valorDecodificador(seleccionado));
                            lblValorDeco.setText(items.get(position).getPrecio());
                        }

                    }

                }

            } else {
                Log.d(TAG, "cambiarValorDeco seleccionado == -");
                if (items.get(position).isFideliza()) {
                    items.get(position).setPrecio("0");
                }

                lblValorDeco.setText(items.get(position).getPrecio());

            }
        } else {
            lblValorDeco.setText("0");
        }

        ArrayList<Object> data = new ArrayList<Object>();
        data.add("cambio");
        observer.update(data);
    }

    private void comportamientoTipoBorrado(CheckBox chkEliminarDeco, TextView txtBorrarDecodificador,
                                           TextView lblNombreDeco) {
        if (item.isNuevo()) {
            chkEliminarDeco.setVisibility(View.GONE);
            txtBorrarDecodificador.setVisibility(View.VISIBLE);
            lblNombreDeco.setText("-");
        } else {
            txtBorrarDecodificador.setVisibility(View.GONE);
            chkEliminarDeco.setVisibility(View.VISIBLE);
        }
    }

    private void eliminarDecoNuevo(int position) {
        ArrayList<Object> data = new ArrayList<Object>();
        data.add("eliminar");
        data.add(position);
        observer.update(data);
    }

    private String valorDecodificador(String decodificador) {

        System.out.println(MainActivity.config.getDepartamento());

		/*Log.w(TAG, plan);
        Log.w(TAG, decodificador);
		Log.w(TAG, String.valueOf(UtilidadesDecos.consultarPrecio(plan, decodificador)));*/

        return String.valueOf(UtilidadesDecos.consultarPrecio(plan, decodificador));

    }

    private String valorDecodificadorFidelizado(String decodificador) {

        System.out.println(MainActivity.config.getDepartamento());
        System.out.println(ciudad);

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos
                .consultar(false, "listasvalores", new String[]{"lst_valor"},
                        "lst_nombre =? and lst_clave = ? and lst_departamento = ? and lst_ciudad = ?", new String[]{
                                "valorDecosFidelizados", decodificador, MainActivity.config.getDepartamento(), ciudad},
                        null, null, null);

        if (resultado != null) {
            return resultado.get(0).get(0);
        }

        return "-";
    }

    private ArrayAdapter<String> llenarSpinner() {
        ArrayAdapter<String> adaptador = null;

        ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "listasgenerales",
                new String[]{"lst_item"}, "lst_nombre = ?", new String[]{"tipoDecos"}, null, null, null);

        System.out.println(resultado);

        ArrayList<String> arrayListData = new ArrayList<String>();
        if (resultado != null) {
            for (int i = 0; i < resultado.size(); i++) {
                if (!item.getOriginal().equals(resultado.get(i).get(0))) {
                    arrayListData.add(resultado.get(i).get(0));
                } else {
                    if (item.isNuevo() && item.isIncluido()) {
                        arrayListData.add(resultado.get(i).get(0));
                    }
                }
            }

        }

        adaptador = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, arrayListData);

        return adaptador;

    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

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
