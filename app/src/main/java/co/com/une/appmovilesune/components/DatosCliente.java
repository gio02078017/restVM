package co.com.une.appmovilesune.components;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.model.Cliente;

public class DatosCliente {
	private Context context;

	private void llenarGenero(String dataType, Spinner spinner) {
		ArrayAdapter<String> adaptador = null;
		String datos = "";
		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "configuracion",
				new String[] { "datos" }, "tipo = ? and campo = ? and evento like?",
				new String[] { "Socioeconomico", dataType }, null, null, null);

		String[] StOpt = null;

		ArrayList<String> arrayListData = new ArrayList<String>();
		if (resultado != null) {
			datos = resultado.get(0).get(0);
			datos = "-- Seleccione Opción --," + datos;
			StOpt = datos.split(",");
			for (int i = 0; i < StOpt.length; i++) {
				arrayListData.add(StOpt[i]);
			}

		} else {
			arrayListData.add("N/A");
		}

		adaptador = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayListData);
		spinner.setAdapter(adaptador);
	}

}
