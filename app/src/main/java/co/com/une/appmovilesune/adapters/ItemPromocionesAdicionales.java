package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

public class ItemPromocionesAdicionales implements Serializable {

    String adicional, descuento, tipoProducto, meses;
    int id;

    public ItemPromocionesAdicionales(String adicional, String descuento, String tipoProducto, String meses, int id) {
        super();
        this.adicional = adicional;
        this.descuento = descuento;
        this.tipoProducto = tipoProducto;
        this.meses = meses;
        this.id = id;
    }

    public ItemPromocionesAdicionales(String adicional, String descuento, String tipoProducto, String meses) {
        super();
        this.adicional = adicional;
        this.descuento = descuento;
        this.tipoProducto = tipoProducto;
        this.meses = meses;
    }

    public ItemPromocionesAdicionales(String adicional) {
        super();
        this.adicional = adicional;
    }

    public ItemPromocionesAdicionales() {

    }

    public String getAdicional() {
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getMeses() {
        return meses;
    }

    public void setMeses(String meses) {
        this.meses = meses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
