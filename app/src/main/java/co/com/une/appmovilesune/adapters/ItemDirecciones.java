package co.com.une.appmovilesune.adapters;

public class ItemDirecciones {
	private String Direccion, Ciudad;

	protected long id;

	public ItemDirecciones(String Direccion, String Ciudad) {
		this.Direccion = Direccion;
		this.Ciudad = Ciudad;
	}

	public String getDireccion() {
		return Direccion;
	}

	public void setDireccion(String direccion) {
		Direccion = direccion;
	}

	public String getCiudad() {
		return Ciudad;
	}

	public void setCiudad(String ciudad) {
		Ciudad = ciudad;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
