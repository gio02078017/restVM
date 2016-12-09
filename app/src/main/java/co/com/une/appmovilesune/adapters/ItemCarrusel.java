package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

public class ItemCarrusel implements Serializable {

    protected long id;

    private String fecha;
    private String producto;
    private String motivo;
    private String paquete;

    public ItemCarrusel(String fecha, String producto, String motivo, String paquete) {
        super();
        this.fecha = fecha;
        this.producto = producto;
        this.motivo = motivo;
        this.paquete = paquete;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
