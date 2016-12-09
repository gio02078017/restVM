package co.com.une.appmovilesune.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ScooringUne implements Serializable {

    private String[] carteraUNE = new String[5];
    private String[] validadorCifin = new String[5];

    private boolean resultadoScooring = false;
    private boolean pasaScooring = false;
    private boolean prospectoScooring = false;
    private boolean validarScooring = true;
    private boolean pendienteRespuesta = false;
    private boolean noAplicaScooring = false;
    private String documentoScooring = "";
    private String totalMaxima = "";
    private String estado = "";
    private String idScooring = "";
    private String razonScooring = "";
    private String origenScooring = "";

    public void setResultado(boolean resultadoScooring, boolean pasaScooring, boolean prospectoScooring,
                             boolean validarScooring, String documentoScooring, String totalMaxima, String estado, String idScooring) {
        this.resultadoScooring = resultadoScooring;
        this.pasaScooring = pasaScooring;
        this.prospectoScooring = prospectoScooring;
        this.validarScooring = validarScooring;
        this.documentoScooring = documentoScooring;
        this.totalMaxima = totalMaxima;
        this.estado = estado;
        this.idScooring = idScooring;
    }

    public ScooringUne() {
        resultadoScooring = false;
        pasaScooring = false;
        prospectoScooring = false;
        validarScooring = true;
        pendienteRespuesta = false;
        noAplicaScooring = false;
        documentoScooring = "";
        totalMaxima = "";
        estado = "";
        idScooring = "";
        razonScooring = "";
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

    public boolean isProspectoScooring() {
        return prospectoScooring;
    }

    public void setProspectoScooring(boolean prospectoScooring) {
        this.prospectoScooring = prospectoScooring;
    }

    public boolean isPendienteRespuesta() {
        return pendienteRespuesta;
    }

    public void setPendienteRespuesta(boolean pendienteRespuesta) {
        this.pendienteRespuesta = pendienteRespuesta;
    }

    public boolean isNoAplicaScooring() {
        return noAplicaScooring;
    }

    public void setNoAplicaScooring(boolean noAplicaScooring) {
        this.noAplicaScooring = noAplicaScooring;
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

    public String getRazonScooring() {
        return razonScooring;
    }

    public void setRazonScooring(String razonScooring) {
        this.razonScooring = razonScooring;
    }

    public String getIdScooring() {
        return idScooring;
    }

    public void setIdScooring(String idScooring) {
        this.idScooring = idScooring;
    }

    public String getOrigenScooring() {
        return origenScooring;
    }

    public void setOrigenScooring(String origenScooring) {
        this.origenScooring = origenScooring;
    }

}
