package co.com.une.appmovilesune.model;

import java.util.ArrayList;

public class Paquete {

    private String id, datosCliente, tipo;
    public ArrayList<Producto> productos;

    public Paquete(String id, String datosCliente, String tipo) {
        super();
        this.id = id;
        this.datosCliente = datosCliente;
        this.tipo = tipo;
        productos = new ArrayList<Producto>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatosCliente() {
        return datosCliente;
    }

    public void setDatosCliente(String datosCliente) {
        this.datosCliente = datosCliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
