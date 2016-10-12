package co.com.une.appmovilesune.adapters;

public class ListaVentas {
	private String fecha, hora, cliente, vta_id;
	protected long id;

	public ListaVentas(String fecha, String hora, String cliente, String vta_id) {
		super();
		this.fecha = fecha;
		this.hora = hora;
		this.cliente = cliente;
		this.vta_id = vta_id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getVentaID() {
		return vta_id;
	}

	public void setVentaID(String vta_id) {
		this.vta_id = vta_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
