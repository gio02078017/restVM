package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;
import co.com.une.appmovilesune.MainActivity;

public class Facturacion implements Serializable {

	private String id;
	private String id_cliente;
	private String Departamento;
	private String Municipio;
	private String Direccion;
	private String Barrio;

	public Facturacion() {
		id = "";
		id_cliente = "";
		Departamento = "";
		Municipio = "";
		Direccion = "";
		Barrio = "";
	}

	public Facturacion(String id_cliente, String departamento, String municipio, String direccion, String barrio) {
		this.id_cliente = id_cliente;
		this.Departamento = departamento;
		this.Municipio = municipio;
		this.Direccion = direccion;
		this.Barrio = barrio;
	}

	public void actualizarFacturacion() {
		ContentValues cv = new ContentValues();
		cv.put("id_cliente", id_cliente);
		cv.put("Departamento", Departamento);
		cv.put("Municipio", Municipio);
		cv.put("Direccion", Direccion);
		cv.put("Barrio", Barrio);
		if (id == null) {
			guardarFacturacion(id_cliente);
		} else if (MainActivity.basedatos.insertar("facturacion", cv)) {
			ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "facturacion",
					new String[] { "id" }, "id_cliente=?", new String[] { id_cliente }, null, "id DESC", null);
			id = resultado.get(0).get(0);
		} else {
			id = null;
		}
	}

	public void guardarFacturacion(String idcliente) {
		String idCliente = idcliente;
		ContentValues cv = new ContentValues();
		cv.put("id_cliente", id_cliente);
		cv.put("Departamento", Departamento);
		cv.put("Municipio", Municipio);
		cv.put("Direccion", Direccion);
		cv.put("Barrio", Barrio);
		if (MainActivity.basedatos.insertar("facturacion", cv)) {
			ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "facturacion",
					new String[] { "id" }, "id_cliente=?", new String[] { idCliente }, null, "id DESC", null);
			if (resultado != null) {
				id = resultado.get(0).get(0);
			} else {
				id = null;
			}

		} else {
			id = null;
		}
	}

	public void cargarFacturacion(String idCliente) {
		this.id_cliente = idCliente;
		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "facturacion", null, "id=?",
				new String[] { this.id_cliente }, null, "id DESC", null);
		if (resultado != null) {
			id_cliente = resultado.get(0).get(1);
			Departamento = resultado.get(0).get(2);
			Municipio = resultado.get(0).get(3);
			Direccion = resultado.get(0).get(4);
			Barrio = resultado.get(0).get(5);
		}
	}

	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getDepartamento() {
		return Departamento;
	}

	public void setDepartamento(String departamento) {
		Departamento = departamento;
	}

	public String getMunicipio() {
		if (Departamento.equals("--Seleccione Departamento--")) {
			Municipio = "--Seleccione Municipio--";
		}
		return Municipio;
	}

	public void setMunicipio(String municipio) {
		Municipio = municipio;
	}

	public String getDireccion() {
		return Direccion;
	}

	public void setDireccion(String direccion) {
		Direccion = direccion;
	}

	public String getBarrio() {
		return Barrio;
	}

	public void setBarrio(String barrio) {
		Barrio = barrio;
	}

}
