package co.com.une.appmovilesune.change;

public class Ofertas {

	String tipoProducto, producto, tarifa, oferta;

	public Ofertas(String tipoProducto, String producto, String tarifa, String oferta) {
		this.tipoProducto = tipoProducto;
		this.producto = producto;
		this.tarifa = tarifa;
		this.oferta = oferta;
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getTarifa() {
		return tarifa;
	}

	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}

	public String getOferta() {
		return oferta;
	}

	public void setOferta(String oferta) {
		this.oferta = oferta;
	}

}
