package co.com.une.appmovilesune.model;

public class Producto {

	private String tipoProducto, identificador, datosCliente, Direccion, plan, idPaquete;

	public Producto(String tipoProducto, String identificador, String datosCliente, String direccion, String plan,
			String idPaquete) {
		super();
		this.tipoProducto = tipoProducto;
		this.identificador = identificador;
		this.datosCliente = datosCliente;
		Direccion = direccion;
		this.plan = plan;
		this.idPaquete = idPaquete;
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getDatosCliente() {
		return datosCliente;
	}

	public void setDatosCliente(String datosCliente) {
		this.datosCliente = datosCliente;
	}

	public String getDireccion() {
		return Direccion;
	}

	public void setDireccion(String direccion) {
		Direccion = direccion;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getIdPaquete() {
		return idPaquete;
	}

	public void setIdPaquete(String idPaquete) {
		this.idPaquete = idPaquete;
	}

}
