package co.com.une.appmovilesune.complements;

import co.com.une.appmovilesune.change.Utilidades;

import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Direcciones extends Activity {

    private Spinner Elemento_A, Elemento_C, Elemento_D, Elemento_E, Elemento_G, Elemento_H, Elemento_L;
    private Button Enviar, Cancelar;
    private EditText Elemento_B, Elemento_F, Elemento_I, Elemento_J, Elemento_K, Elemento_M;

    private ArrayAdapter<String> adaptador;

    protected static final int REQUEST_CODE = 10;
    private static final int OK_RESULT_CODE = 1;

    public String Direccion = "";

    // Metodo principal de la clase
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direccion);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.StElemento_A);
        Elemento_A = (Spinner) findViewById(R.id.Elemento_A);

        adaptador.setDropDownViewResource(R.layout.list_radio);

        Elemento_A.setAdapter(adaptador);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.StLetras);
        Elemento_C = (Spinner) findViewById(R.id.Elemento_C);
        adaptador.setDropDownViewResource(R.layout.list_radio);
        Elemento_C.setAdapter(adaptador);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.StElemento_D);
        Elemento_D = (Spinner) findViewById(R.id.Elemento_D);
        adaptador.setDropDownViewResource(R.layout.list_radio);
        Elemento_D.setAdapter(adaptador);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.StElemento_A);
        Elemento_E = (Spinner) findViewById(R.id.Elemento_E);
        adaptador.setDropDownViewResource(R.layout.list_radio);
        Elemento_E.setAdapter(adaptador);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.StLetras);
        Elemento_G = (Spinner) findViewById(R.id.Elemento_G);
        adaptador.setDropDownViewResource(R.layout.list_radio);
        Elemento_G.setAdapter(adaptador);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.StElemento_D);
        Elemento_H = (Spinner) findViewById(R.id.Elemento_H);
        adaptador.setDropDownViewResource(R.layout.list_radio);
        Elemento_H.setAdapter(adaptador);

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utilidades.StLetras_Inicial);
        Elemento_L = (Spinner) findViewById(R.id.Elemento_L);
        adaptador.setDropDownViewResource(R.layout.list_radio);
        Elemento_L.setAdapter(adaptador);

        Elemento_B = (EditText) findViewById(R.id.Elemento_B);
        Elemento_F = (EditText) findViewById(R.id.Elemento_F);
        Elemento_I = (EditText) findViewById(R.id.Elemento_I);
        Elemento_J = (EditText) findViewById(R.id.Elemento_J);
        Elemento_K = (EditText) findViewById(R.id.Elemento_K);
        Elemento_M = (EditText) findViewById(R.id.Elemento_M);

        Enviar = (Button) findViewById(R.id.Enviar);
        Cancelar = (Button) findViewById(R.id.Cancelar);

        Enviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Direccion();

            }
        });

        Cancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Cerrar();
            }
        });

    }

    public void Direccion() {
        boolean control = false;
        String stDireccion = "";
        String elemento_a = (String) Elemento_A.getSelectedItem();
        String elemento_b = Elemento_B.getText().toString();
        String elemento_c = (String) Elemento_C.getSelectedItem();
        String elemento_d = (String) Elemento_D.getSelectedItem();
        String elemento_e = (String) Elemento_E.getSelectedItem();
        String elemento_f = Elemento_F.getText().toString();
        String elemento_g = (String) Elemento_G.getSelectedItem();
        String elemento_h = (String) Elemento_H.getSelectedItem();
        String elemento_i = Elemento_I.getText().toString().trim();
        String elemento_j = Elemento_J.getText().toString().trim();
        String elemento_k = Elemento_K.getText().toString().toUpperCase();
        String elemento_l = (String) Elemento_L.getSelectedItem();
        String elemento_m = Elemento_M.getText().toString().toUpperCase();

        if (elemento_c != "----") {
            elemento_c = " " + elemento_c;
        } else {
            elemento_c = "";
        }

        if (elemento_d != "----") {
            elemento_d = " " + elemento_d;
        } else {
            elemento_d = "";
        }

        if (elemento_g != "----") {
            elemento_g = " " + elemento_g;
        } else {
            elemento_g = "";
        }

        if (elemento_h != "----") {
            elemento_h = " " + elemento_h;
        } else {
            elemento_h = "";
        }

        if (!elemento_j.equalsIgnoreCase("")) {
            elemento_j = " (INTERIOR " + elemento_j + ")";
        } else {
            elemento_j = "";
        }

        if (!elemento_k.equalsIgnoreCase("")) {
            if (elemento_l != "----") {
                elemento_k = " (TORRE " + elemento_k + "" + elemento_l + ")";
            } else {
                elemento_k = " (TORRE " + elemento_k + ")";
            }

        } else if (elemento_l != "----") {
            elemento_k = " (TORRE " + elemento_l + ")";
        } else {
            elemento_k = "";
        }

        elemento_m = elemento_m.trim();
        if (!elemento_m.equalsIgnoreCase("")) {
            elemento_m = " " + elemento_m + "";
        }

        // CL 25 A SUR CR 25 ACESTE - 25 (INTERIOR 3333) (TORRE 25D) HHHHH

        if (!elemento_a.equalsIgnoreCase("Seleccione")) {
            if (!elemento_b.equalsIgnoreCase("")) {
                if (!elemento_e.equalsIgnoreCase("Seleccione")) {
                    if (!elemento_f.equalsIgnoreCase("")) {
                        if (!elemento_i.equalsIgnoreCase("")) {

                            if (!elemento_a.equalsIgnoreCase(elemento_e)) {
                                stDireccion = elemento_a + " " + elemento_b + "" + elemento_c + "" + elemento_d + " "
                                        + elemento_e + " " + elemento_f + "" + elemento_g + "" + elemento_h + " - "
                                        + elemento_i + "" + elemento_j + "" + elemento_k + "" + elemento_m;

                                stDireccion = stDireccion.toUpperCase();
                                // Toast.makeText(getContext(), stDireccion,
                                // Toast.LENGTH_SHORT).show();
                                control = true;
                            } else {
                                Toast.makeText(this, "El Elemento 1 y El Elemento 5 No Pueden Ser Iguales",
                                        Toast.LENGTH_SHORT).show();
                                control = false;
                            }
                        } else {
                            Toast.makeText(this, "Elemento 9 vacio y es necesario diligenciarse", Toast.LENGTH_SHORT)
                                    .show();
                            control = false;
                        }
                    } else {
                        Toast.makeText(this, "Elemento 6 vacio y es necesario diligenciarse", Toast.LENGTH_SHORT)
                                .show();
                        control = false;
                    }
                } else {
                    Toast.makeText(this, "Elemento 5, sin seleccionar, debe seleccionar algo", Toast.LENGTH_SHORT)
                            .show();
                    control = false;
                }
            } else {
                Toast.makeText(this, "Elemento 2 vacio y es necesario diligenciarse", Toast.LENGTH_SHORT).show();
                control = false;
            }
        } else {
            Toast.makeText(this, "Elemento 1, sin seleccionar, debe seleccionar algo", Toast.LENGTH_SHORT).show();
            control = false;
        }

        if (control) {
            Enviar(stDireccion);
            Cerrar();
        }
    }

    public void Cerrar() {
        finish();
    }

    public void Enviar(String Direccion) {
        Intent intent = new Intent();
        intent.putExtra("result", "Direcciones");
        intent.putExtra("Direccion", Direccion);
        setResult(OK_RESULT_CODE, intent);
        finish();
    }
}
