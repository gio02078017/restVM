package co.com.une.appmovilesune.adapters;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ListaChecked {

	private String[][] selecionados;
	private String titulo, dato;
	private boolean checked;
	private int nivel;
	protected long id;

	public ListaChecked(int nivel, String titulo, String dato, String[][] selecionados) {
		this.nivel = nivel;
		this.titulo = titulo;
		this.dato = dato;
		this.selecionados = selecionados;
	}

	public String[][] getSelecionados() {
		return selecionados;
	}

	public void setSelecionados(String[][] selecionados) {
		this.selecionados = selecionados;
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

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
