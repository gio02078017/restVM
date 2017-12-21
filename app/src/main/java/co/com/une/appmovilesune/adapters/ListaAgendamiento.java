package co.com.une.appmovilesune.adapters;

public class ListaAgendamiento {

    private String fecha, disManana, porcManana, disTarde, porcTarde;
    private String tipoDia, dia;
    private int nivel;
    protected long id;
    private String agendaSiebel;

    public ListaAgendamiento(String fecha, String disManana, String porcManana, String disTarde, String porcTarde,
                             String tipoDia, String dia) {
        this.fecha = fecha;
        this.disManana = disManana;
        this.porcManana = porcManana;
        this.disTarde = disTarde;
        this.porcTarde = porcTarde;
        this.tipoDia = tipoDia;
        this.dia = dia;
    }

    public ListaAgendamiento(String fecha, String disManana, String porcManana, String disTarde, String porcTarde,
                             String agendaSiebel) {
        this.fecha = fecha;
        this.disManana = disManana;
        this.porcManana = porcManana;
        this.disTarde = disTarde;
        this.porcTarde = porcTarde;
        this.agendaSiebel = agendaSiebel;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDisManana() {
        return disManana;
    }

    public void setDisManana(String disManana) {
        this.disManana = disManana;
    }

    public String getPorcManana() {
        return porcManana;
    }

    public void setPorcManana(String porcManana) {
        this.porcManana = porcManana;
    }

    public String getDisTarde() {
        return disTarde;
    }

    public void setDisTarde(String disTarde) {
        this.disTarde = disTarde;
    }

    public String getPorcTarde() {
        return porcTarde;
    }

    public void setPorcTarde(String porcTarde) {
        this.porcTarde = porcTarde;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
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

    public String getAgendaSiebel() {
        return agendaSiebel;
    }

    public void setAgendaSiebel(String agendaSiebel) {
        this.agendaSiebel = agendaSiebel;
    }
}
