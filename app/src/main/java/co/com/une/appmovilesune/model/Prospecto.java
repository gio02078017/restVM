package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;
import co.com.une.appmovilesune.MainActivity;

public class Prospecto implements Serializable {

	public String Productos;
	public String Observaciones;
	public String Motivo;
	public String Distancia;
	private String id;

	public Prospecto(String productos, String observaciones, String motivo, String distancia) {

		this.Productos = productos;
		this.Productos = Productos.replace("[", "");
		this.Productos = this.Productos.replace("]", "");

		this.Observaciones = observaciones;
		this.Motivo = motivo;
		this.Distancia = distancia;
		// System.out.println("Prospect JSON =>
		// "+consolidarProspecto().toString());

	}

	public String getDistancia() {
		return Distancia;
	}

	public void setDistancia(String distancia) {
		Distancia = distancia;
	}

	public String getProductos() {
		return Productos;
	}

	public void setProductos(String productos) {
		Productos = productos;
		Productos = Productos.replace("[", "");
		Productos = this.Productos.replace("]", "");
	}

	public String getObservaciones() {
		return Observaciones;
	}

	public void setObservaciones(String observaciones) {
		Observaciones = observaciones;
	}

	public String getMotivo() {
		return Motivo;
	}

	public void setMotivo(String motivo) {
		Motivo = motivo;
	}

	public Prospecto() {
	}

	public JSONObject consolidarProspecto() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("observacion", Observaciones);
			jo.put("motivo", Motivo);

			JSONArray ja = new JSONArray();
			String[] productos = this.Productos.split(",");
			for (int i = 0; i < productos.length; i++) {
				JSONObject jop = new JSONObject();
				jop.put("producto", productos[i]);
				ja.put(jop);
			}
			jo.put("items", ja);

			jo.put("distancia", Distancia);
		} catch (JSONException e) {
			Log.w("Error ", e.getMessage());
		}
		return jo;
	}

	public void guardarProspecto(String id_asesoria) {
		ContentValues cv = new ContentValues();
		cv.put("id_asesoria", id_asesoria);
		cv.put("motivo", Motivo);
		cv.put("observaciones", Observaciones);
		cv.put("Productos", Productos);
		cv.put("Distancia", Distancia);

		if (MainActivity.basedatos.insertar("prospecto", cv)) {
			ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "prospecto",
					new String[] { "id" }, "id_asesoria=?", new String[] { id_asesoria }, null, null, null);
			if (resultado != null) {
				id = resultado.get(0).get(0);
			} else {
				id = null;
			}

		} else {
			id = null;
		}
	}

}
