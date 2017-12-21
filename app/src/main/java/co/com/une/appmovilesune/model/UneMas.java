package co.com.une.appmovilesune.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;

import co.com.une.appmovilesune.MainActivity;

public class UneMas implements Serializable {

    public String Hobbies;
    public String CodigoHogar;
    public String Contrasenia;

    public String codigo, mensaje;

    public String getCodigoHogar() {
        return CodigoHogar;
    }

    public void setCodigoHogar(String codigoHogar) {
        CodigoHogar = codigoHogar;
    }

    public String getContrasenia() {
        return Contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        Contrasenia = contrasenia;
    }

    public UneMas(String hobbies, String codigoHogar, String Contrasenia) {

        this.Hobbies = hobbies;
        this.Hobbies = Hobbies.replace("[", "");
        this.Hobbies = this.Hobbies.replace("]", "");
        this.CodigoHogar = codigoHogar;
        this.Contrasenia = Contrasenia;

    }

    public String getHobbies() {
        return Hobbies;
    }

    public void setHobbies(String hobbies) {
        Hobbies = hobbies;
        Hobbies = Hobbies.replace("[", "");
        Hobbies = this.Hobbies.replace("]", "");
    }

    public UneMas() {
    }
}
