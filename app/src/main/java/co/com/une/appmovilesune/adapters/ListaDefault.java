package co.com.une.appmovilesune.adapters;

import java.io.Serializable;

public class ListaDefault implements Serializable {

    private String titulo, dato;
    private int nivel;
    protected long id;

    public ListaDefault(int nivel, String titulo, String dato) {
        this.nivel = nivel;
        this.titulo = titulo;
        this.dato = dato;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
