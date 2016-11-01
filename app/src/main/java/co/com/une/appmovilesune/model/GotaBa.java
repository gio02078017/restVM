package co.com.une.appmovilesune.model;

/**
 * Created by Gospina on 27/10/2016.
 */

public class GotaBa {

    private boolean controlGota;
    private double valorGota;
    private double valorGotaSinIva;
    private String velocidadInicial;
    private String velocidadFinal;

    public GotaBa(boolean controlGota, double valorGota, double valorGotaSinIva, String velocidadInicial, String velocidadFinal) {
        this.controlGota = controlGota;
        this.valorGota = valorGota;
        this.valorGotaSinIva = valorGotaSinIva;
        this.velocidadInicial = velocidadInicial;
        this.velocidadFinal = velocidadFinal;
    }

    public boolean isControlGota() {
        return controlGota;
    }

    public void setControlGota(boolean controlGota) {
        this.controlGota = controlGota;
    }

    public double getValorGota() {
        return valorGota;
    }

    public void setValorGota(double valorGota) {
        this.valorGota = valorGota;
    }

    public double getValorGotaSinIva() {
        return valorGotaSinIva;
    }

    public void setValorGotaSinIva(double valorGotaSinIva) {
        this.valorGotaSinIva = valorGotaSinIva;
    }

    public String getVelocidadInicial() {
        return velocidadInicial;
    }

    public void setVelocidadInicial(String velocidadInicial) {
        this.velocidadInicial = velocidadInicial;
    }

    public String getVelocidadFinal() {
        return velocidadFinal;
    }

    public void setVelocidadFinal(String velocidadFinal) {
        this.velocidadFinal = velocidadFinal;
    }
}
