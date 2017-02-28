package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemCompetencia implements Serializable {

    protected long id;

    private String producto, competencia, vigencia, valor;

    public ItemCompetencia(String producto, String competencia, String vigencia, String valor) {
        super();
        this.producto = producto;
        this.competencia = competencia;
        this.vigencia = vigencia;
        this.valor = valor;
    }

    public JSONObject consolidarItemCompetencia() {

        JSONObject jo = new JSONObject();
        try {
            jo.put("producto", producto);
            jo.put("competencia", competencia);
            jo.put("valor", valor);
            jo.put("vigencia", vigencia);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jo;

    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
