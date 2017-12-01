package co.com.une.appmovilesune.adapters;

/**
 * Created by Gospina on 30/11/2017.
 */

public class CrackleExistente {
    private boolean existente;
    private String tipoProducto;

    public CrackleExistente() {
    }

    public CrackleExistente(boolean existente, String tipoProducto) {
        this.existente = existente;
        this.tipoProducto = tipoProducto;
    }

    public boolean isExistente() {
        return existente;
    }

    public void setExistente(boolean existente) {
        this.existente = existente;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
}
