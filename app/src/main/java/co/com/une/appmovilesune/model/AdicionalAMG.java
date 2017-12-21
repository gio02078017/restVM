package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdicionalAMG implements Serializable {

    public String producto;
    public String codigo;
    public String nombre;
    public double cargoBasico;
    public ArrayList<String[]> iva;
    public double total;

    public AdicionalAMG(JSONObject adicional, String producto) throws JSONException {

        this.producto = producto;
        codigo = adicional.getString("adicionalCod");
        nombre = adicional.getString("adicionalName");
        cargoBasico = adicional.getDouble("cargoBasico");
        if (!adicional.get("iva").getClass().getSimpleName().equals(JSONArray.class.getSimpleName())) {
            iva = new ArrayList<String[]>();
            for (int i = 0; i < adicional.getJSONObject("iva").length(); i++) {
                String[] data = new String[]{adicional.getJSONObject("iva").names().get(i).toString(), adicional
                        .getJSONObject("iva").getString(adicional.getJSONObject("iva").names().get(i).toString())};
                iva.add(data);
            }
        }
        total = adicional.getDouble("totalAdicional");
    }

}
