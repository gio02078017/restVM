package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

public class ListaIpDinamica implements Serializable {

	private String identificador, ipDinamica;
	protected long id;	
	
	public ListaIpDinamica(String identificador, String ipDinamica) {
		this.identificador = identificador;
		this.ipDinamica = ipDinamica;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getIpDinamica() {
		return ipDinamica;
	}
	public void setIpDinamica(String ipDinamica) {
		this.ipDinamica = ipDinamica;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}	

}
