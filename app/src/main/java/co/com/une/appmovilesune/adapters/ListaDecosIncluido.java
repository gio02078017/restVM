package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

/**
 * Created by Gospina on 18/09/2017.
 */

public class ListaDecosIncluido  implements Serializable {
    String tipoDeco;
    int cantidad;
    int id;

    public ListaDecosIncluido(String tipoDeco, int cantidad) {
        this.tipoDeco = tipoDeco;
        this.cantidad = cantidad;
    }

    public String getTipoDeco() {
        return tipoDeco;
    }

    public void setTipoDeco(String tipoDeco) {
        this.tipoDeco = tipoDeco;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
