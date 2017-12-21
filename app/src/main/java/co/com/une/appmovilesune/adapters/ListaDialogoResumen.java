package co.com.une.appmovilesune.adapters;

public class ListaDialogoResumen {

    private String nombre, plan, descuento;
    protected long id;

    public ListaDialogoResumen(String nombre, String plan) {
        this.nombre = nombre;
        this.plan = plan;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlan() {
        return plan;
    }

    public void setPalor(String plan) {
        this.plan = plan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
