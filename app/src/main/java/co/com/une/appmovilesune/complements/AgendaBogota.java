package co.com.une.appmovilesune.complements;

import co.com.une.appmovilesune.change.Utilidades;
import co.com.une.appmovilesune.components.SelectorFecha;
import co.com.une.appmovilesune.components.TituloPrincipal;
import co.com.une.appmovilesune.model.Cliente;
import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class AgendaBogota extends Activity {

    private Context context;
    private Dialogo dialogo;

    private SelectorFecha sltFechaAgenda;

    private ArrayAdapter<String> adaptador;

    protected static final int REQUEST_CODE = 10;
    private static final int OK_RESULT_CODE = 1;

    public String Direccion = "";

    public SelectorFecha sfp;

    private TituloPrincipal tp;

    private RadioGroup groupFranja;

    public String Franja = "";

    public String Systema;

    private TableRow trTablaAgendaFecha;

    // Metodo principal de la clase
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agendabogota);
        dialogo = new Dialogo(this, Dialogo.DIALOGO_FECHA, "");
        dialogo.dialogo.setOnDismissListener(dl);

        Bundle reicieveParams = getIntent().getExtras();
        if (reicieveParams != null) {
            Systema = reicieveParams.getString("Systema");
        }

        sltFechaAgenda = (SelectorFecha) findViewById(R.id.sltFechaAgenda);
        trTablaAgendaFecha = (TableRow) findViewById(R.id.trTablaAgendaFecha);

        if (Systema.equalsIgnoreCase("Elite")) {
            trTablaAgendaFecha.setVisibility(View.GONE);
        }

        groupFranja = (RadioGroup) findViewById(R.id.radioGroupFranjas);

        tp = (TituloPrincipal) findViewById(R.id.tlpPrincipal);
        tp.setTitulo("Agendamiento");

        groupFranja.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Franja = "";
                // TODO Auto-generated method stub
                if (checkedId == R.id.radio_am) {
                    // lblChecked.setText("Ha pulsado el boton 1");
                    Franja = "AM";

                } else if (checkedId == R.id.radio_pm) {
                    Franja = "PM";
                    // lblChecked.setText("Ha pulsado el boton 2");
                }
            }

        });

    }

    public void mostrarDialogo(View v) {
        sfp = (SelectorFecha) v.getParent().getParent();
        dialogo.dialogo.show();
    }

    OnDismissListener dl = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface arg0) {

            // TODO Auto-generated method stub
            if (dialogo.isSeleccion()) {
                sfp.setTexto(dialogo.getFechaSeleccion());
            }
            dialogo.setSeleccion(false);
        }
    };

    public void Cerrar() {
        finish();
    }

    public void guardarAgenda(View v) {
        System.out.println("guardar agenda");

        if (!sltFechaAgenda.getTexto().equalsIgnoreCase("") && !Franja.equalsIgnoreCase("")) {
            String Agenda = sltFechaAgenda.getTexto() + " " + Franja;
            System.out.println("Agenda " + Agenda);

            Intent intent = new Intent();
            intent.putExtra("Agenda", Agenda);
            setResult(OK_RESULT_CODE, intent);
            finish();
        } else if (Systema.equalsIgnoreCase("Elite") && !Franja.equalsIgnoreCase("")) {
            String Agenda = sltFechaAgenda.getTexto() + " " + Franja;
            System.out.println("Agenda " + Agenda);

            Intent intent = new Intent();
            intent.putExtra("Agenda", Agenda);
            setResult(OK_RESULT_CODE, intent);
            finish();
        } else {
            Toast.makeText(this, "Agenda Incompleta, Debe Seleccionar Una Fecha y Franja", Toast.LENGTH_SHORT).show();
        }
    }

    // public void Enviar(String Direccion) {
    // Intent intent = new Intent();
    // intent.putExtra("result", "Direcciones");
    // intent.putExtra("Direccion", Direccion);
    // setResult(OK_RESULT_CODE, intent);
    // finish();
    // }
}
