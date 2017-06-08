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

	int counterUpdate, counterUpdateChck,counterUpdatePadresChck;

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
				asignarEstadoDeSeleccionTodos(isChecked);
				asignarEstadoDePermisoDeSeleccionTodos(!isChecked);
			}
		});

		icgListasGenerales = (ItemConfiguracion) findViewById(R.id.icgListasGenerales);
		icgListasValores = (ItemConfiguracion) findViewById(R.id.icgListasValores);
		icgListasEstratos = (ItemConfiguracion) findViewById(R.id.icgListasEstratos);
		icgCiudades = (ItemConfiguracion) findViewById(R.id.icgCiudades);
		icgBarrios = (ItemConfiguracion) findViewById(R.id.icgBarrios);
		icgProductos = (ItemConfiguracion) findViewById(R.id.icgProductos);
		icgPrecios = (ItemConfiguracion) findViewById(R.id.icgPrecios);
		icgAdicionales = (ItemConfiguracion) findViewById(R.id.icgAdicionales);
		icgImpuestos = (ItemConfiguracion) findViewById(R.id.icgImpuestos);
		icgExtensionesHFC = (ItemConfiguracion) findViewById(R.id.icgExtensionesHFC);
		icgReglas = (ItemConfiguracion) findViewById(R.id.icgReglas);
		icgCondiciones = (ItemConfiguracion) findViewById(R.id.icgCondiciones);
		icgDecos = (ItemConfiguracion) findViewById(R.id.icgDecos);

		if(!MainActivity.config.isControlVersionDB()){
			chkTodos.setChecked(true);
			chkTodos.setEnabled(false);
		}


	}

	public void asignarEstadoDeSeleccionTodos(boolean estado){
        icgListasGenerales.chkSelector.setChecked(estado);
        icgListasValores.chkSelector.setChecked(estado);
        icgListasEstratos.chkSelector.setChecked(estado);
        icgCiudades.chkSelector.setChecked(estado);
        icgBarrios.chkSelector.setChecked(estado);
        icgProductos.chkSelector.setChecked(estado);
        icgPrecios.chkSelector.setChecked(estado);
        icgAdicionales.chkSelector.setChecked(estado);
        icgImpuestos.chkSelector.setChecked(estado);
        icgExtensionesHFC.chkSelector.setChecked(estado);
        icgReglas.chkSelector.setChecked(estado);
        icgCondiciones.chkSelector.setChecked(estado);
        icgDecos.chkSelector.setChecked(estado);
    }

    public void asignarEstadoDePermisoDeSeleccionTodos(boolean accion){
		icgListasGenerales.chkSelector.setEnabled(accion);
		icgListasValores.chkSelector.setEnabled(accion);
		icgListasEstratos.chkSelector.setEnabled(accion);
		icgCiudades.chkSelector.setEnabled(accion);
		icgBarrios.chkSelector.setEnabled(accion);
		icgProductos.chkSelector.setEnabled(accion);
		icgPrecios.chkSelector.setEnabled(accion);
		icgAdicionales.chkSelector.setEnabled(accion);
		icgImpuestos.chkSelector.setEnabled(accion);
		icgExtensionesHFC.chkSelector.setEnabled(accion);
		icgReglas.chkSelector.setEnabled(accion);
		icgCondiciones.chkSelector.setEnabled(accion);
		icgDecos.chkSelector.setEnabled(accion);
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
		System.out.println("update actualizar res "+res);

		if (res.get(0).equals("Listas Generales")) {
			icgListasGenerales.setInvisible();
			if ((Boolean) res.get(1)) {
				icgListasGenerales.setOK();
				icgListasGenerales.chkSelector.setChecked(false);
			} else {
				icgListasGenerales.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Listas Estratos")) {
			icgListasEstratos.setInvisible();
			if ((Boolean) res.get(1)) {
				icgListasEstratos.setOK();
				icgListasEstratos.chkSelector.setChecked(false);

			} else {
				icgListasEstratos.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Listas Valores")) {
			icgListasValores.setInvisible();
			if ((Boolean) res.get(1)) {
				icgListasValores.setOK();
				icgListasValores.chkSelector.setChecked(false);
			} else {
				icgListasValores.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Ciudades")) {
			icgCiudades.setInvisible();
			if ((Boolean) res.get(1)) {
				icgCiudades.setOK();
				icgCiudades.chkSelector.setChecked(false);
			} else {
				icgCiudades.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Adicionales")) {
			icgAdicionales.setInvisible();
			if ((Boolean) res.get(1)) {
				icgAdicionales.setOK();
				icgAdicionales.chkSelector.setChecked(false);
			} else {
				icgAdicionales.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Productos")) {
//			icgProductos.setInvisible();
//			if ((Boolean) res.get(1)) {
//				icgProductos.setOK();
//			} else {
//				icgProductos.setWRONG();
//			}
			if ((Boolean) res.get(1)) {
				ejecutarActualizacion("CondicionesxTarifas");
			} else {
				icgProductos.setInvisible();
				icgProductos.setWRONG();
			}
			//counterUpdateChck++;
			counterUpdatePadresChck++;
		}else if (res.get(0).equals("CondicionesxTarifas")) {
			icgProductos.setInvisible();
			if ((Boolean) res.get(1)) {
				icgProductos.setOK();
				icgProductos.chkSelector.setChecked(false);
			} else {
				icgProductos.setWRONG();
			}
			counterUpdateChck++;
		} else if (res.get(0).equals("Precios")) {
			icgPrecios.setInvisible();
			if ((Boolean) res.get(1)) {
				icgPrecios.setOK();
				icgPrecios.chkSelector.setChecked(false);
			} else {
				icgProductos.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Impuestos")) {
			icgImpuestos.setInvisible();
			if ((Boolean) res.get(1)) {
				icgImpuestos.setOK();
				icgImpuestos.chkSelector.setChecked(false);
			} else {
				icgImpuestos.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("ExtensionesHFC")) {
			icgExtensionesHFC.setInvisible();
			if ((Boolean) res.get(1)) {
				icgExtensionesHFC.setOK();
				icgExtensionesHFC.chkSelector.setChecked(false);
			} else {
				icgExtensionesHFC.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Barrios")) {
			icgBarrios.setInvisible();
			if ((Boolean) res.get(1)) {
				icgBarrios.setOK();
				icgBarrios.chkSelector.setChecked(false);
			} else {
				icgBarrios.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Reglas")) {
			icgReglas.setInvisible();
			if ((Boolean) res.get(1)) {
				icgReglas.setOK();
				icgReglas.chkSelector.setChecked(false);
			} else {
				icgReglas.setWRONG();
			}
			counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("Condiciones")) {
			// icgCondiciones.setInvisible();
			if ((Boolean) res.get(1)) {
				ejecutarActualizacion("CondicionesxReglas");
			} else {
				icgCondiciones.setInvisible();
				icgCondiciones.setWRONG();
			}
			//counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("CondicionesxReglas")) {
			icgCondiciones.setInvisible();
			if ((Boolean) res.get(1)) {
				icgCondiciones.setOK();
				icgCondiciones.chkSelector.setChecked(false);
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
			//counterUpdateChck++;
			counterUpdatePadresChck++;
		} else if (res.get(0).equals("CondicionesxDecos")) {
			icgDecos.setInvisible();
			if ((Boolean) res.get(1)) {
				icgDecos.setOK();
				icgDecos.chkSelector.setChecked(false);
			} else {
				icgDecos.setWRONG();
			}
			counterUpdateChck++;
		}

		System.out.println("counterUpdate "+counterUpdate);
		System.out.println("counterUpdateChck "+counterUpdateChck);
		System.out.println("counterUpdatePadresChck "+counterUpdatePadresChck);

		if (counterUpdate == counterUpdateChck) {
			btnUpdate.setEnabled(true);
			regresar = true;
		}else if (counterUpdate == counterUpdatePadresChck) {
			btnUpdate.setEnabled(true);
			//regresar = true;
		}

	}

	private void ejecutarActualizacion(String item) {
		Actualizacion act = new Actualizacion();
		act.addObserver(this);

		System.out.println("ejecutarActualizacion item "+item);

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
			 */
		else if (item.equals("ExtensionesHFC")) {
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
		} else if (item.equals("CondicionesxTarifas")) {
			act.execute("CondicionesxTarifas");
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

		counterUpdate=0;
		counterUpdateChck = 0;
		counterUpdatePadresChck = 0;

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
			//counterUpdate++;
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
			//counterUpdate++;
			ejecutarActualizacion("Reglas");
		}
		if (icgCondiciones.isCheked()) {
			counterUpdate++;
			ejecutarActualizacion("Condiciones");
		}
		if (icgDecos.isCheked()) {
			counterUpdate++;
			//counterUpdate++;
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