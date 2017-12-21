package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

public class ListaPromociones implements Serializable {

    private String titulo;
    private String promocion, meses;
    private String tituloPromocion, tituloMeses;

    private int nivel;
    protected long id;

    public ListaPromociones(int nivel, String titulo, String tituloPromocion, String promocion, String tituloMeses,
                            String meses) {
        this.nivel = nivel;
        this.titulo = titulo;
        this.promocion = promocion;
        this.meses = meses;
        this.tituloPromocion = tituloPromocion;
        this.tituloMeses = tituloMeses;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPromocion() {
        return promocion;
    }

    public void setPromocion(String promocion) {
        this.promocion = promocion;
    }

    public String getMeses() {
        return meses;
    }

    public void setMeses(String meses) {
        this.meses = meses;
    }

    public String getTituloPromocion() {
        return tituloPromocion;
    }

    public void setTituloPromocion(String tituloPromocion) {
        this.tituloPromocion = tituloPromocion;
    }

    public String getTituloMeses() {
        return tituloMeses;
    }

    public void setTituloMeses(String tituloMeses) {
        this.tituloMeses = tituloMeses;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
