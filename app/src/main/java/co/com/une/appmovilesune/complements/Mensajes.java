package co.com.une.appmovilesune.complements;

import java.util.ArrayList;

import co.com.une.appmovilesune.adapters.ListaDefault;
import co.com.une.appmovilesune.adapters.ListaDefaultAdapter;
import co.com.une.appmovilesune.adapters.ListaPromociones;
import co.com.une.appmovilesune.adapters.ListaPromocionesAdapter;
import co.com.une.appmovilesune.change.Interprete;
import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class Mensajes extends Activity {

    private ListView lvm;
    private Button Aceptar;

    private ListaDefaultAdapter adapterMensajes;

    private ArrayList<ListaDefault> mensajes = new ArrayList<ListaDefault>();

    private Interprete interprete = new Interprete();

    protected static final int REQUEST_CODE = 10;
    private static final int OK_RESULT_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlista);

        lvm = (ListView) findViewById(R.id.listasDefault);

        Aceptar = (Button) findViewById(R.id.btnAceptarLista);

        Bundle reicieveParams = getIntent().getExtras();

        if (reicieveParams != null) {
            String dato = reicieveParams.getString("mensajes");
            mensajes.clear();
            mensajes = interprete.Mensajes(dato);
            onMostrar(mensajes);
        }

        Aceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (ventaMovilidad()) {
                    Enviar("Pedido Exitoso");
                } else {
                    Cerrar();
                }

            }
        });

    }

    public void onBackPressed() {
        if (ventaMovilidad()) {
            Enviar("Pedido Exitoso");
        } else {
            Cerrar();
        }
    }

    public void onMostrar(ArrayList<ListaDefault> mensajes) {

        if (mensajes.size() == 0) {
            mensajes = Interprete.MensajesVacio();
        }

        adapterMensajes = new ListaDefaultAdapter(this, mensajes, this);
        lvm.setAdapter(adapterMensajes);
    }

    public void Cerrar() {
        finish();
    }

    public void Enviar(String Mensaje) {
        Intent intent = new Intent();
        intent.putExtra("result", "Mensajes");
        intent.putExtra("Mensaje", Mensaje);
        setResult(OK_RESULT_CODE, intent);
        finish();
    }

    public boolean ventaMovilidad() {
        boolean validacion = false;

        for (int i = 0; i < mensajes.size(); i++) {
            System.out.println("Titulo " + mensajes.get(i).getTitulo() + " Datos " + mensajes.get(i).getDato());

            if (mensajes.get(i).getTitulo().equalsIgnoreCase("Ingreso Pedido")
                    && mensajes.get(i).getDato().equalsIgnoreCase("Exitoso")) {
                validacion = true;
            }
        }

        System.out.println("validacion " + validacion);

        return validacion;

    }
}
