package co.com.une.appmovilesune.adapters;

public class ItemTarificador {

    private String dato, tipo, tipo_producto;

    protected long id;

    public ItemTarificador(String dato, String tipo, String tipo_producto) {
        super();
        this.dato = dato;
        this.tipo = tipo;
        this.tipo_producto = tipo_producto;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(String tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
