package co.com.une.appmovilesune.adapters;

public class ListaAdicionales {
	private String adicional, precio, descuento, duracion, valorDescuento = "0";
	private boolean balin = false;
	protected long id;

	public ListaAdicionales(String adicional, String precio, String descuento, String duracion) {
		super();
		this.adicional = adicional;
		this.precio = precio;
		this.descuento = descuento;
		this.duracion = duracion;
	}

	public String getAdicional() {
		return adicional;
	}

	public void setAdicional(String adicional) {
		this.adicional = adicional;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isBalin() {
		return balin;
	}

	public void setBalin(boolean balin) {
		this.balin = balin;
	}

	public String getValorDescuento() {
		return valorDescuento;
	}

	public void setValorDescuento(String valorDescuento) {
		this.valorDescuento = valorDescuento;
	}

}
