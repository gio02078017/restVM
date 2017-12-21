package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.com.une.appmovilesune.change.Utilidades;

import com.itextpdf.text.xml.simpleparser.NewLineHandler;

public class Localizacion implements Serializable {
    String latitud;
    String longitud;

    public ArrayList<String[]> getLocalizaciones() {
        return localizaciones;
    }

    public void setLocalizaciones(ArrayList<String[]> localizaciones) {
        this.localizaciones = localizaciones;
    }

    String tipo;
    ArrayList<String[]> localizaciones = new ArrayList<String[]>();

    public Localizacion() {

    }

    public void almacenar(String latitud, String longitud, String tipo) {
        localizaciones.add(new String[]{latitud, longitud, tipo});
    }

    public JSONObject consolidarLocalizacion(int tipoAsesoria, String codigoAsesor) {
        JSONObject localizacion = new JSONObject();
        JSONArray arrayLocalizacion = new JSONArray();
        try {
            for (int i = 0; i < localizaciones.size(); i++) {
                arrayLocalizacion.put(Utilidades.jsonLocalizacion(localizaciones.get(i)[0], localizaciones.get(i)[1],
                        localizaciones.get(i)[2]));
            }

            localizacion.put("tipoAsesoria", tipoAsesoria);
            localizacion.put("codigoAsesor", codigoAsesor);
            localizacion.put("localizaciones", arrayLocalizacion);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return localizacion;

    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        tipo = tipo;
    }

}
