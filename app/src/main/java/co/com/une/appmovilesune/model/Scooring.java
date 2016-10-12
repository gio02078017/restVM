package co.com.une.appmovilesune.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Scooring implements Serializable {

	private String[] carteraUNE = new String[5];
	private String[] validadorCifin = new String[5];

	private boolean resultadoScooring = false;
	private boolean pasaScooring = false;
	private boolean validarScooring = true;
	private String documentoScooring = "";
	private String totalMaxima = "";
	private String estado = "";
	private String idScooring = "";

	private boolean validarCartera = true;

	public String[] getCarteraUNE() {
		return carteraUNE;
	}

	public void setCarteraUNE(String nombre, String estado, String accion, String origen, String idDocumento) {
		this.carteraUNE[0] = nombre;
		this.carteraUNE[1] = estado;
		this.carteraUNE[2] = accion;
		this.carteraUNE[3] = origen;
		this.carteraUNE[4] = idDocumento;
	}

	public void setResultado(boolean resultadoScooring, boolean pasaScooring, boolean validarScooring,
			String documentoScooring, String totalMaxima, String estado, String idScooring) {
		this.resultadoScooring = resultadoScooring;
		this.pasaScooring = pasaScooring;
		this.validarScooring = validarScooring;
		this.documentoScooring = documentoScooring;
		this.totalMaxima = totalMaxima;
		this.estado = estado;
		this.idScooring = idScooring;
	}

	public void setValidadorCifin(String nombre, String identificacion, String fechaExpedicion,
			String descripcionEstado) {
		this.validadorCifin[0] = nombre;
		this.validadorCifin[1] = identificacion;
		this.validadorCifin[2] = fechaExpedicion;
		this.validadorCifin[3] = descripcionEstado;
	}

	public Scooring() {

	}

	public JSONObject consolidarScooring() {
		JSONObject jo = new JSONObject();

		try {
			jo.put("estado", this.carteraUNE[1]);
			jo.put("accion", this.carteraUNE[2]);
			jo.put("origen", this.carteraUNE[3]);
			jo.put("idCliente", this.carteraUNE[4]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jo;
	}

	public String getIdCliente() {
		return this.carteraUNE[4];
	}

	public String getAccion() {
		return this.carteraUNE[2];
	}

	public String getEstadoValidador() {
		return this.validadorCifin[3];
	}

	public boolean isResultadoScooring() {
		return resultadoScooring;
	}

	public void setResultadoScooring(boolean resultadoScooring) {
		this.resultadoScooring = resultadoScooring;
	}

	public boolean isPasaScooring() {
		return pasaScooring;
	}

	public void setPasaScooring(boolean pasaScooring) {
		this.pasaScooring = pasaScooring;
	}

	public boolean isValidarScooring() {
		return validarScooring;
	}

	public void setValidarScooring(boolean validarScooring) {
		this.validarScooring = validarScooring;
	}

	public String getDocumentoScooring() {
		return documentoScooring;
	}

	public void setDocumentoScooring(String documentoScooring) {
		this.documentoScooring = documentoScooring;
	}

	public String getTotalMaxima() {
		return totalMaxima;
	}

	public void setTotalMaxima(String totalMaxima) {
		this.totalMaxima = totalMaxima;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getIdScooring() {
		return idScooring;
	}

	public void setIdScooring(String idScooring) {
		this.idScooring = idScooring;
	}

	public String[] getValidadorCifin() {
		return validadorCifin;
	}

	public boolean isValidarCartera() {
		return validarCartera;
	}

	public void setValidarCartera(boolean validarCartera) {
		this.validarCartera = validarCartera;
	}

}
