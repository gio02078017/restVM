package co.com.une.appmovilesune.adapters;

public class ItemCoberturaGpon {
    private String DireccionInicial, DireccionFinal;

    protected long id;

    public ItemCoberturaGpon(String DireccionInicial, String DireccionFinal) {
        this.DireccionInicial = DireccionInicial;
        this.DireccionFinal = DireccionFinal;
    }

    public String getDireccionInicial() {
        return DireccionInicial;
    }

    public void setDireccionInicial(String DireccionInicial) {
        this.DireccionInicial = DireccionInicial;
    }

    public String getDireccionFinal() {
        return DireccionFinal;
    }

    public void setDireccionFinal(String DireccionFinal) {
        this.DireccionFinal = DireccionFinal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
