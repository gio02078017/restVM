package co.com.une.appmovilesune.adapters;

/**
 * Created by Gospina on 26/10/2017.
 */

public class ListaCotizacion {
    int id;
    String tipoTransacion;
    String tipoProducto;
    int pagoParcial;
    double porcentajeDescuentoPagoParcial;

    String union;
    String clausula;
    String datos;

    public ListaCotizacion(String tipoTransacion, String tipoProducto) {
        this.tipoTransacion = tipoTransacion;
        this.tipoProducto = tipoProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoTransacion() {
        return tipoTransacion;
    }

    public void setTipoTransacion(String tipoTransacion) {
        this.tipoTransacion = tipoTransacion;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public String getClausula() {
        return clausula;
    }

    public void setClausula(String clausula) {
        this.clausula = clausula;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public int getPagoParcial() {
        return pagoParcial;
    }

    public void setPagoParcial(int pagoParcial) {
        this.pagoParcial = pagoParcial;
    }

    public double getPorcentajeDescuentoPagoParcial() {
        return porcentajeDescuentoPagoParcial;
    }

    public void setPorcentajeDescuentoPagoParcial(double porcentajeDescuentoPagoParcial) {
        this.porcentajeDescuentoPagoParcial = porcentajeDescuentoPagoParcial;
    }
}
