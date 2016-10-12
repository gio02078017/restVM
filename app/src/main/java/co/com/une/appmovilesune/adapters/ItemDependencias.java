package co.com.une.appmovilesune.adapters;

public class ItemDependencias {

	private String dependencia;
	private int cantidad;
	private String tipoProducto;
	protected long id;

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public ItemDependencias(String dependencia, int cantidad, String tipoProducto, long id) {
		this.dependencia = dependencia;
		this.cantidad = cantidad;
		this.tipoProducto = tipoProducto;
		this.id = id;
	}

	public ItemDependencias(String dependencia, int cantidad, String tipoProducto) {
		this.dependencia = dependencia;
		this.cantidad = cantidad;
		this.tipoProducto = tipoProducto;
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
