package co.com.une.appmovilesune.controller;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;
import co.com.une.appmovilesune.complements.Actualizacion;
import co.com.une.appmovilesune.components.ItemConfiguracion;
import co.com.une.appmovilesune.interfaces.Observer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class ControlActualizacion extends Activity implements Observer {

    public CheckBox chkTodos;

    View btnUpdate;

    int counterUpdate, counterUpdateChck;

    boolean regresar = true;

    public ItemConfiguracion icgCiudades, icgListasGenerales, icgListasEstratos, icgListasValores, icgBarrios,
            icgProductos, icgPrecios, icgReglas, icgPromociones, icgExtensionesHFC, icgAdicionales, icgImpuestos,
            icgPromocionesMovilidad, icgGpon, icgCondiciones, icgDecos;

    public void onCreate(Bundle savedInstanceState) {
        // btnUpdate.setEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewactualizacion);

        chkTodos = (CheckBox) findViewById(R.id.chkTodo);

        chkTodos.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    icgListasGenerales.chkSelector.setChecked(true);
                    icgListasValores.chkSelector.setChecked(true);
                    icgListasEstratos.chkSelector.setChecked(true);
                    icgCiudades.chkSelector.setChecked(true);
                    icgBarrios.chkSelector.setChecked(true);
                    icgProductos.chkSelector.setChecked(true);
                    icgPrecios.chkSelector.setChecked(true);
                    // icgPromociones.chkSelector.setChecked(true);
                    icgAdicionales.chkSelector.setChecked(true);
                    icgImpuestos.chkSelector.setChecked(true);
                    icgExtensionesHFC.chkSelector.setChecked(true);
                    icgReglas.chkSelector.setChecked(true);
                    icgCondiciones.chkSelector.setChecked(true);
                    icgDecos.chkSelector.setChecked(true);
                    // icgPromocionesMovilidad.chkSelector.setChecked(true);
                    // icgGpon.chkSelector.setChecked(true);
                } else {
                    icgListasGenerales.chkSelector.setChecked(false);
                    icgListasValores.chkSelector.setChecked(false);
                    icgListasEstratos.chkSelector.setChecked(false);
                    icgCiudades.chkSelector.setChecked(false);
                    icgBarrios.chkSelector.setChecked(false);
                    icgProductos.chkSelector.setChecked(false);
                    icgPrecios.chkSelector.setChecked(false);
                    // icgPromociones.chkSelector.setChecked(false);
                    icgAdicionales.chkSelector.setChecked(false);
                    icgImpuestos.chkSelector.setChecked(false);
                    icgExtensionesHFC.chkSelector.setChecked(false);
                    icgReglas.chkSelector.setChecked(false);
                    icgCondiciones.chkSelector.setChecked(false);
                    icgDecos.chkSelector.setChecked(false);
                    // icgPromocionesMovilidad.chkSelector.setChecked(false);
                    // icgGpon.chkSelector.setChecked(false);
                }
            }
        });

        icgListasGenerales = (ItemConfiguracion) findViewById(R.id.icgListasGenerales);
        icgListasValores = (ItemConfiguracion) findViewById(R.id.icgListasValores);
        icgListasEstratos = (ItemConfiguracion) findViewById(R.id.icgListasEstratos);
        icgCiudades = (ItemConfiguracion) findViewById(R.id.icgCiudades);
        icgBarrios = (ItemConfiguracion) findViewById(R.id.icgBarrios);
        icgProductos = (ItemConfiguracion) findViewById(R.id.icgProductos);
        icgPrecios = (ItemConfiguracion) findViewById(R.id.icgPrecios);
        // icgPromociones = (ItemConfiguracion)
        // findViewById(R.id.icgPromociones);
        icgAdicionales = (ItemConfiguracion) findViewById(R.id.icgAdicionales);
        icgImpuestos = (ItemConfiguracion) findViewById(R.id.icgImpuestos);
        icgExtensionesHFC = (ItemConfiguracion) findViewById(R.id.icgExtensionesHFC);
        icgReglas = (ItemConfiguracion) findViewById(R.id.icgReglas);
        icgCondiciones = (ItemConfiguracion) findViewById(R.id.icgCondiciones);
        icgDecos = (ItemConfiguracion) findViewById(R.id.icgDecos);
        // icgPromocionesMovilidad = (ItemConfiguracion)
        // findViewById(R.id.icgPromocionesMovilidad);
        // icgGpon = (ItemConfiguracion) findViewById(R.id.icgGpon);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (MainActivity.seguimiento) {
            EasyTracker.getInstance(this).activityStart(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (MainActivity.seguimiento) {
            EasyTracker.getInstance(this).activityStop(this);
        }
    }

    @Override
    public void update(Object value) {
        // TODO Auto-generated method stub
        ArrayList<Object> res = (ArrayList<Object>) value;
        if (res.get(0).equals("Listas Generales")) {
            icgListasGenerales.setInvisible();
            if ((Boolean) res.get(1)) {
                icgListasGenerales.setOK();
            } else {
                icgListasGenerales.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Listas Estratos")) {
            icgListasEstratos.setInvisible();
            if ((Boolean) res.get(1)) {
                icgListasEstratos.setOK();
            } else {
                icgListasEstratos.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Listas Valores")) {
            icgListasValores.setInvisible();
            if ((Boolean) res.get(1)) {
                icgListasValores.setOK();
            } else {
                icgListasValores.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Ciudades")) {
            icgCiudades.setInvisible();
            if ((Boolean) res.get(1)) {
                icgCiudades.setOK();
            } else {
                icgCiudades.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Adicionales")) {
            icgAdicionales.setInvisible();
            if ((Boolean) res.get(1)) {
                icgAdicionales.setOK();
            } else {
                icgAdicionales.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Productos")) {
            icgProductos.setInvisible();
            if ((Boolean) res.get(1)) {
                icgProductos.setOK();
            } else {
                icgProductos.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Precios")) {
            icgPrecios.setInvisible();
            if ((Boolean) res.get(1)) {
                icgPrecios.setOK();
            } else {
                icgProductos.setWRONG();
            }
            counterUpdateChck++;
        } /*
             * else if (res.get(0).equals("Promociones")) {
			 * icgPromociones.setInvisible(); if ((Boolean) res.get(1)) {
			 * icgPromociones.setOK(); } else { icgPromociones.setWRONG(); }
			 * counterUpdateChck++; }
			 */ else if (res.get(0).equals("Impuestos")) {
            icgImpuestos.setInvisible();
            if ((Boolean) res.get(1)) {
                icgImpuestos.setOK();
            } else {
                icgImpuestos.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("ExtensionesHFC")) {
            icgExtensionesHFC.setInvisible();
            if ((Boolean) res.get(1)) {
                icgExtensionesHFC.setOK();
            } else {
                icgExtensionesHFC.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Barrios")) {
            icgBarrios.setInvisible();
            if ((Boolean) res.get(1)) {
                icgBarrios.setOK();
            } else {
                icgBarrios.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Reglas")) {
            icgReglas.setInvisible();
            if ((Boolean) res.get(1)) {
                icgReglas.setOK();
            } else {
                icgReglas.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Condiciones")) {
            // icgCondiciones.setInvisible();
            if ((Boolean) res.get(1)) {
                ejecutarActualizacion("CondicionesxReglas");
            } else {
                icgCondiciones.setInvisible();
                icgCondiciones.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("CondicionesxReglas")) {
            icgCondiciones.setInvisible();
            if ((Boolean) res.get(1)) {
                icgCondiciones.setOK();
            } else {
                icgCondiciones.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("Decos")) {
            // icgCondiciones.setInvisible();
            if ((Boolean) res.get(1)) {
                ejecutarActualizacion("CondicionesxDecos");
            } else {
                icgDecos.setInvisible();
                icgDecos.setWRONG();
            }
            counterUpdateChck++;
        } else if (res.get(0).equals("CondicionesxDecos")) {
            icgDecos.setInvisible();
            if ((Boolean) res.get(1)) {
                icgDecos.setOK();
            } else {
                icgDecos.setWRONG();
            }
            counterUpdateChck++;
        } /*
			 * else if (res.get(0).equals("Promociones Movilidad")) {
			 * icgPromocionesMovilidad.setInvisible(); if ((Boolean) res.get(1))
			 * { icgPromocionesMovilidad.setOK(); } else {
			 * icgPromocionesMovilidad.setWRONG(); } counterUpdateChck++; }
			 */ /*
				 * else if (res.get(0).equals("Gpon")) { icgGpon.setInvisible();
				 * if ((Boolean) res.get(1)) { icgGpon.setOK(); } else {
				 * icgGpon.setWRONG(); } counterUpdateChck++; }
				 */

        if (counterUpdate == counterUpdateChck) {
            btnUpdate.setEnabled(true);
            regresar = true;
        }

    }

    private void ejecutarActualizacion(String item) {
        Actualizacion act = new Actualizacion();
        act.addObserver(this);

        if (item.equals("Listas Generales")) {
            icgListasGenerales.setVisible();
            act.execute("Listas Generales");
        } else if (item.equals("Listas Estratos")) {
            icgListasEstratos.setVisible();
            act.execute("Listas Estratos");
        } else if (item.equals("Listas Valores")) {
            icgListasValores.setVisible();
            act.execute("Listas Valores");
        } else if (item.equals("Ciudades")) {
            icgCiudades.setVisible();
            act.execute("Ciudades");
        } else if (item.equals("Adicionales")) {
            icgAdicionales.setVisible();
            act.execute("Adicionales");
        } else if (item.equals("Productos")) {
            icgProductos.setVisible();
            act.execute("Productos");
        } else if (item.equals("Precios")) {
            icgPrecios.setVisible();
            act.execute("Precios");
        } /*
			 * else if (item.equals("Promociones")) {
			 * icgPromociones.setVisible(); act.execute("Promociones"); }
			 */ else if (item.equals("ExtensionesHFC")) {
            icgExtensionesHFC.setVisible();
            act.execute("ExtensionesHFC");
        } else if (item.equals("Impuestos")) {
            icgImpuestos.setVisible();
            act.execute("Impuestos");
        } else if (item.equals("Barrios")) {
            icgBarrios.setVisible();
            act.execute("Barrios");
        } else if (item.equals("Reglas")) {
            icgReglas.setVisible();
            act.execute("Reglas");
        } else if (item.equals("Condiciones")) {
            icgCondiciones.setVisible();
            act.execute("Condiciones");
        } else if (item.equals("CondicionesxReglas")) {
            act.execute("CondicionesxReglas");
        } else if (item.equals("Decos")) {
            icgDecos.setVisible();
            act.execute("Decos");
        } else if (item.equals("CondicionesxDecos")) {
            act.execute("CondicionesxDecos");
        } /*
			 * else if (item.equals("Promociones Movilidad")) {
			 * icgPromocionesMovilidad.setVisible(); act.execute(
			 * "Promociones Movilidad"); }
			 *//*
			 * else if (item.equals("Gpon")) { icgGpon.setVisible();
			 * act.execute("Gpon"); }
			 */
    }

    public void buscar(View v) {
        btnUpdate = findViewById(R.id.btnActualizar);
        btnUpdate.setEnabled(false);
        regresar = false;
        if (icgListasGenerales.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Listas Generales");
        }

        if (icgListasEstratos.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Listas Estratos");
        }

        if (icgListasValores.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Listas Valores");
        }

        if (icgCiudades.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Ciudades");
        }
        if (icgBarrios.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Barrios");
        }

        if (icgProductos.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Productos");
        }
        if (icgPrecios.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Precios");
        }
        // if (icgPromociones.isCheked()) {
        // counterUpdate++;
        // ejecutarActualizacion("Promociones");
        // }
        if (icgAdicionales.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Adicionales");
        }
        if (icgImpuestos.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Impuestos");
        }
        if (icgExtensionesHFC.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("ExtensionesHFC");
        }
        if (icgReglas.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Reglas");
        }
        if (icgCondiciones.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Condiciones");
        }
        if (icgDecos.isCheked()) {
            counterUpdate++;
            ejecutarActualizacion("Decos");
        }
        // if (icgPromocionesMovilidad.isCheked()) {
        // counterUpdate++;
        // ejecutarActualizacion("Promociones Movilidad");
        // }
        // if (icgGpon.isCheked()) {
        // counterUpdate++;
        // ejecutarActualizacion("Gpon");
        // }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (regresar) {
                super.onBackPressed();
                Intent intent = new Intent();
                intent.putExtra("result", "Actualizacion");
                setResult(MainActivity.OK_RESULT_CODE, intent);
                finish();
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {

    }

}