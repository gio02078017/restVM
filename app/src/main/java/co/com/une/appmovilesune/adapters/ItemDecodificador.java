package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

public class ItemDecodificador implements Serializable, Cloneable {

	protected long id;
	private String tipoEquipo;
	private String norma;
	private String productoId;
	private String marca;
	private String serial;
	private String referencia;
	private String tipoAlquiler;
	private boolean fideliza;
	private String tipoFideliza;
	private String capacidadReal;
	private String descuento;
	private String tiempo;
	private String planPromocional;
	private String equipoId;
	private String destino;
	private String original;
	private String precio;
	private String decosActuales;
	private String infoActualesDecos;
	private boolean procesado = false;
	private String tipoTransaccion = "";
	private boolean existentesFideliza = false;
	private boolean fidelizaIncluido = false;

	private boolean nuevo;
	private boolean incluido = false;

	public ItemDecodificador(ItemDecodificador item) {
		super();
		this.tipoEquipo = item.tipoEquipo;
		this.productoId = item.productoId;
		this.marca = item.marca;
		this.serial = item.serial;
		this.referencia = item.referencia;
		this.tipoAlquiler = item.tipoAlquiler;
		this.fideliza = item.fideliza;
		this.tipoFideliza = item.tipoFideliza;
		this.capacidadReal = item.capacidadReal;
		this.descuento = item.descuento;
		this.tiempo = item.tiempo;
		this.planPromocional = item.planPromocional;
		this.equipoId = item.equipoId;
		this.destino = item.destino;
		this.original = item.original;
		this.norma = item.original;
		this.precio = item.precio;
		this.decosActuales = item.decosActuales;
		this.nuevo = item.nuevo;
	}

	public ItemDecodificador(String tipoEquipo, String productoId, String marca, String serial, String referencia,
			String tipoAlquiler, boolean fideliza, String tipoFideliza, String capacidadReal, String descuento,
			String tiempo, String planPromocional, String equipoId, String destino, String original, String precio,
			String decosActuales, String infoActualesDecos, boolean nuevo) {
		super();
		this.tipoEquipo = tipoEquipo;
		this.productoId = productoId;
		this.marca = marca;
		this.serial = serial;
		this.referencia = referencia;
		this.tipoAlquiler = tipoAlquiler;
		this.fideliza = fideliza;
		this.tipoFideliza = tipoFideliza;
		this.capacidadReal = capacidadReal;
		this.descuento = descuento;
		this.tiempo = tiempo;
		this.planPromocional = planPromocional;
		this.equipoId = equipoId;
		this.destino = destino;
		this.original = original;
		this.norma = original;
		this.precio = precio;
		this.decosActuales = decosActuales;
		this.infoActualesDecos = infoActualesDecos;
		this.nuevo = nuevo;

	}

	public ItemDecodificador(String decosActuales, String tipoAlquiler, boolean fideliza, String tipoFideliza,
			String original, String precio, String tipoTransaccion, boolean nuevo, boolean incluido,
			boolean fidelizaIncluido) {
		super();
		this.decosActuales = decosActuales;
		this.tipoAlquiler = tipoAlquiler;
		this.fideliza = fideliza;
		this.tipoFideliza = tipoFideliza;
		this.original = original;
		this.precio = precio;
		this.tipoTransaccion = tipoTransaccion;
		this.nuevo = nuevo;
		this.incluido = incluido;
		this.fidelizaIncluido = fidelizaIncluido;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTipoEquipo() {
		return tipoEquipo;
	}

	public void setTipoEquipo(String tipoEquipo) {
		this.tipoEquipo = tipoEquipo;
	}

	public String getNorma() {
		return norma;
	}

	public void setNorma(String norma) {
		this.norma = norma;
	}

	public String getProductoId() {
		return productoId;
	}

	public void setProductoId(String productoId) {
		this.productoId = productoId;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getTipoAlquiler() {
		return tipoAlquiler;
	}

	public void setTipoAlquiler(String tipoAlquiler) {
		this.tipoAlquiler = tipoAlquiler;
	}

	public boolean isFideliza() {
		return fideliza;
	}

	public void setFideliza(boolean fideliza) {
		this.fideliza = fideliza;
	}

	public String getTipoFideliza() {
		return tipoFideliza;
	}

	public void setTipoFideliza(String tipoFideliza) {
		this.tipoFideliza = tipoFideliza;
	}

	public String getCapacidadReal() {
		return capacidadReal;
	}

	public void setCapacidadReal(String capacidadReal) {
		this.capacidadReal = capacidadReal;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	public String getPlanPromocional() {
		return planPromocional;
	}

	public void setPlanPromocional(String planPromocional) {
		this.planPromocional = planPromocional;
	}

	public String getEquipoId() {
		return equipoId;
	}

	public void setEquipoId(String equipoId) {
		this.equipoId = equipoId;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getDecosActuales() {
		return decosActuales;
	}

	public void setDecosActuales(String decosActuales) {
		this.decosActuales = decosActuales;
	}

	public String getInfoActualesDecos() {
		return infoActualesDecos;
	}

	public void setInfoActualesDecos(String infoActualesDecos) {
		this.infoActualesDecos = infoActualesDecos;
	}

	public boolean isNuevo() {
		return nuevo;
	}

	public void setNuevo(boolean nuevo) {
		this.nuevo = nuevo;
	}

	public boolean isIncluido() {
		return incluido;
	}

	public void setIncluido(boolean incluido) {
		this.incluido = incluido;
	}

	public boolean isProcesado() {
		return procesado;
	}

	public void setProcesado(boolean procesado) {
		this.procesado = procesado;
	}

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public boolean isExistentesFideliza() {
		return existentesFideliza;
	}

	public void setExistentesFideliza(boolean existentesFideliza) {
		this.existentesFideliza = existentesFideliza;
	}	

	public boolean isFidelizaIncluido() {
		return fidelizaIncluido;
	}

	public void setFidelizaIncluido(boolean fidelizaIncluido) {
		this.fidelizaIncluido = fidelizaIncluido;
	}

	public ItemDecodificador clone() {
		// TODO Auto-generated method stub
		ItemDecodificador clone = null;
		try {
			clone = (ItemDecodificador) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}

		return clone;
	}

}
