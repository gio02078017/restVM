package co.com.une.appmovilesune.model;

import java.util.ArrayList;

public class Domicilio {

	private String direccion;
	public ArrayList<Paquete> paquetes;
	public ArrayList<Producto> productos;

	public Domicilio(String direccion) {
		this.direccion = direccion;
		paquetes = new ArrayList<Paquete>();
		productos = new ArrayList<Producto>();
	}

	public void agregarPaquete(Paquete paquete) {
		paquetes.add(paquete);
	}

	public void agregarProducto(Producto producto) {
		productos.add(producto);
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

}
