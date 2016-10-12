package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import co.com.une.appmovilesune.MainActivity;
import android.content.ContentValues;

public class Contacto implements Serializable {

	private String id;
	private String id_cliente;
	private String Nombres;
	private String Apellidos;
	private String Telefono;
	private String Celular;
	private String Parentesco;

	public Contacto() {
		id = "";
		id_cliente = "";
		Nombres = "";
		Apellidos = "";
		Telefono = "";
		Celular = "";
		Parentesco = "";
	}

	public Contacto(String nombres, String apellidos, String telefono, String celular, String parentesco) {
		this.Nombres = nombres;
		this.Apellidos = apellidos;
		this.Telefono = telefono;
		this.Celular = celular;
		this.Parentesco = parentesco;
	}

	public void actualizarContacto() {
		ContentValues cv = new ContentValues();
		cv.put("id_cliente", id_cliente);
		cv.put("nombre", Nombres);
		// cv.put("apellidos", Apellidos);
		cv.put("telefono", Telefono);
		cv.put("celular", Celular);
		cv.put("parentesco", Parentesco);
		if (id == null) {
			guardarContacto(id_cliente);
		} else if (MainActivity.basedatos.insertar("contacto", cv)) {
			ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "contacto",
					new String[] { "id" }, "id_cliente=?", new String[] { id_cliente }, null, "id DESC", null);
			id = resultado.get(0).get(0);
		} else {
			id = null;
		}
	}

	public void guardarContacto(String idcliente) {
		String idCliente = idcliente;
		ContentValues cv = new ContentValues();
		cv.put("id_cliente", id_cliente);
		cv.put("nombre", Nombres);
		// cv.put("apellidos", Apellidos);
		cv.put("telefono", Telefono);
		cv.put("celular", Celular);
		cv.put("parentesco", Parentesco);
		if (MainActivity.basedatos.insertar("contacto", cv)) {
			ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "contacto",
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

	public void cargarContactos(String id) {
		this.id = id;
		ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "contacto", null, "id=?",
				new String[] { this.id }, null, "id DESC", null);
		if (resultado != null) {
			id_cliente = resultado.get(0).get(1);
			Nombres = resultado.get(0).get(2);
			Telefono = resultado.get(0).get(3);
			Celular = resultado.get(0).get(4);
			Parentesco = resultado.get(0).get(5);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getNombres() {
		return Nombres;
	}

	public void setNombres(String nombres) {
		Nombres = nombres;
	}

	public String getApellidos() {
		return Apellidos;
	}

	public void setApellidos(String apellidos) {
		Apellidos = apellidos;
	}

	public String getTelefono() {
		return Telefono;
	}

	public void setTelefono(String telefono) {
		Telefono = telefono;
	}

	public String getCelular() {
		return Celular;
	}

	public void setCelular(String celular) {
		Celular = celular;
	}

	public String getParentesco() {
		return Parentesco;
	}

	public void setParentesco(String parentesco) {
		Parentesco = parentesco;
	}

}
