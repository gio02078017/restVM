package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.adapters.ItemCompetencia;
import co.com.une.appmovilesune.complements.Calendario;

public class Competencia implements Serializable {

    private String id;
    public ArrayList<ItemCompetencia> Competencia;
    private String Empaquetado;
    private String NoUne;
    private String Observaciones;
    private String Fecha;
    private String Hora;
    private String Atencion;
    private String SubMotivo;

    public Competencia() {
        Atencion = "";
        Competencia = new ArrayList<ItemCompetencia>();
        Empaquetado = "";
        NoUne = "";
        Observaciones = "";
        Fecha = "";
        Hora = "";
        SubMotivo = "";
    }

    public Competencia(String Atencion, ArrayList<ItemCompetencia> Competencia, String NoUne, String Observaciones) {

        this.Atencion = Atencion;
        this.Competencia = Competencia;
        this.NoUne = NoUne;
        this.Observaciones = Observaciones;
        this.Fecha = Calendario.getFecha();
        this.Hora = Calendario.getHora();

    }

    public void guardarCompetencia(String id_asesoria) {
        ContentValues cv = new ContentValues();
        cv.put("id_asesoria", id_asesoria);
        cv.put("Empaquetado", Empaquetado);
        cv.put("NoUne", NoUne);
        cv.put("Observaciones", Observaciones);
        cv.put("Fecha", Fecha);
        cv.put("Hora", Hora);

        if (MainActivity.basedatos.insertar("competencias", cv)) {
            ArrayList<ArrayList<String>> resultado = MainActivity.basedatos.consultar(false, "competencias",
                    new String[]{"id"}, "id_asesoria=?", new String[]{id_asesoria}, null, null, null);
            if (resultado != null) {
                id = resultado.get(0).get(0);
            } else {
                id = null;
            }

        } else {
            id = null;
        }
    }

    public JSONObject consolidarCompetencia(String estrato) {
        JSONObject jo = new JSONObject();
        try {

            JSONArray ja = new JSONArray();
            for (int i = 0; i < Competencia.size(); i++) {
                ja.put(Competencia.get(i).consolidarItemCompetencia());
            }

            jo.put("items", ja);
            jo.put("Empaquetado", Empaquetado);

            jo.put("motivo", NoUne);
            jo.put("submotivo", SubMotivo);
            jo.put("observacion", Observaciones);
            jo.put("fecha", Fecha + " " + Hora);
            jo.put("atencion", Atencion);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jo;
    }

    public String getEmpaquetado() {
        return Empaquetado;
    }

    public void setEmpaquetado(String empaquetado) {
        Empaquetado = empaquetado;
    }

    public String getNoUne() {
        return NoUne;
    }

    public void setNoUne(String noUne) {
        NoUne = noUne;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getAtencion() {
        return Atencion;
    }

    public void setAtencion(String atencion) {
        Atencion = atencion;
    }

    public String getSubMotivo() {
        return SubMotivo;
    }

    public void setSubMotivo(String subMotivo) {
        SubMotivo = subMotivo;
    }
}