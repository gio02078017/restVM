package co.com.une.appmovilesune.adapters;

public class BloqueoCobertura {

	private boolean coberturaHFC;
	private boolean coberturaREDCO;
	private boolean disponibilidadHFC;
	private boolean disponibilidadREDCO;
	private String tipoCobertura;
	private String defaulCobertura;
	private String medioTV = "";
	private String consultaMedioTV = "";
	private boolean HFCDigital = false;
	public BloqueoCobertura() {

	}

	public String getTipoCobertura() {
		return tipoCobertura;
	}

	public void setTipoCobertura(String tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}

	public String getDefaulCobertura() {
		return defaulCobertura;
	}

	public void setDefaulCobertura(String defaulCobertura) {
		this.defaulCobertura = defaulCobertura;
	}

	public boolean isCoberturaHFC() {
		return coberturaHFC;
	}

	public void setCoberturaHFC(boolean coberturaHFC) {
		this.coberturaHFC = coberturaHFC;
	}

	public boolean isCoberturaREDCO() {
		return coberturaREDCO;
	}

	public void setCoberturaREDCO(boolean coberturaREDCO) {
		this.coberturaREDCO = coberturaREDCO;
	}

	public boolean isDisponibilidadHFC() {
		return disponibilidadHFC;
	}

	public void setDisponibilidadHFC(boolean disponibilidadHFC) {
		this.disponibilidadHFC = disponibilidadHFC;
	}

	public boolean isDisponibilidadREDCO() {
		return disponibilidadREDCO;
	}

	public void setDisponibilidadREDCO(boolean disponibilidadREDCO) {
		this.disponibilidadREDCO = disponibilidadREDCO;
	}

	public String getMedioTV() {
		return medioTV;
	}

	public void setMedioTV(String medioTV) {
		this.medioTV = medioTV;
	}

	public String getConsultaMedioTV() {
		return consultaMedioTV;
	}

	public void setConsultaMedioTV(String consultaMedioTV) {
		this.consultaMedioTV = consultaMedioTV;
	}

	public boolean isHFCDigital() {
		return HFCDigital;
	}


	public void setHFCDigital(boolean hFCDigital) {
		HFCDigital = hFCDigital;
	}
}
